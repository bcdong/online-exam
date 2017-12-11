/**
 * 考试设置接口的实现类
 *
 * @author yyy
 * @create 2017-11-17 15:00
 */
package edu.nju.onlineexam.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.dao.CourseRepository;
import edu.nju.onlineexam.dao.ExamRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.ExamEntity;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.service.ExamService;
import edu.nju.onlineexam.utils.async.AsyncTask;
import edu.nju.onlineexam.utils.email.MailSender;
import edu.nju.onlineexam.utils.json.JsonTrans;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.ExamVo;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService{

  @Resource
  ExamRepository examRepository;

  @Resource
  UserRepository userRepository;

  @Resource
  CourseRepository courseRepository;

  @Resource
  AsyncTask asyncTask;

  @Value("${online.exam.encrypt.key}")
  String KEY;

  @Override
  public ExamVo addExam(long courseId, String title, int questionCount, int questionScore,
      long startTime, long endTime, List<String> students) throws ServerException {
    ExamEntity entity = new ExamEntity();
    entity.setCourseId(courseId);
    entity.setTitle(title);
    entity.setQuestionCount(questionCount);
    entity.setScore(questionScore);
    entity.setStartTime(new Timestamp(startTime));
    entity.setEndTime(new Timestamp(endTime));
    entity.setStudents(JsonTrans.toJson(students));

    String code = encrypt(title,courseId);
    entity.setCode(code);
    examRepository.save(entity);

    //发送邮件
    asyncTask.sendCode(students,title,code);

    ExamVo vo = new ExamVo(entity);
    vo.setCourseName(courseRepository.findOne(entity.getCourseId()).getName());
    log.info("end add exam");
    return vo;
  }

  @Override
  public List<ExamVo> queryExam(long userId, int status)throws ServerException {
    UserEntity user = userRepository.findOne(userId);
    if(user == null){
      throw new ServerException(ServerCode.USER_NOT_EXIST);
    }
    List<ExamEntity> examList = new ArrayList<>();
    //查询的用户是老师身份
    if(user.getCourseId() != null){
      examList = examRepository.findByCourseId(user.getCourseId());//一位老师仅教授一门课程
    }else{
      //查询的用户是学生身份
      List<ExamEntity> allList = examRepository.findAll();
      for(ExamEntity exam: allList){
        List<String> students = JsonTrans.json2List(exam.getStudents(),
            new TypeReference<List<String>>(){});
        if(students.indexOf(user.getNumber())>=0){
          examList.add(exam);
        }
      }
    }

    List<ExamVo> voList = examList.stream().map(ExamVo::new).collect(Collectors.toList());
    voList.forEach(x->x.setCourseName(courseRepository.findOne(x.getCourseId()).getName()));
    //查询进行中的考试
    if(status == 0){
      return voList.stream().filter(x->(x.getStartTime().getTime()<System.currentTimeMillis() && x.getEndTime().getTime()>System.currentTimeMillis())).
          collect(Collectors.toList());
    }else if(status == 1){
      //查询已完成的考试
      return voList.stream().filter(x->(x.getEndTime().getTime()<System.currentTimeMillis())).collect(
          Collectors.toList());
    }else if(status == -1){
      //查询未开始的考试
      return voList.stream().filter(x->(x.getStartTime().getTime()>System.currentTimeMillis())).collect(
          Collectors.toList());
    }else{
      throw new ServerException(400,"参数错误，status:"+status+" is not -1,0,1");
    }

  }

  private String encrypt(String examTitle,long courseId){
    String inputStr = examTitle+courseId;
    BigInteger bigInteger=null;

    try {
      MessageDigest md = MessageDigest.getInstance(KEY);
      byte[] inputData = inputStr.getBytes();
      md.update(inputData);
      bigInteger = new BigInteger(md.digest());
    } catch (Exception e) {e.printStackTrace();}
    return bigInteger.toString(16);
  }

}
