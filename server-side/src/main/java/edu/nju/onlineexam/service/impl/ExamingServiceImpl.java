/**
 * 考试过程支持的接口实现类
 *
 * @author yyy
 * @create 2017-11-18 14:18
 */
package edu.nju.onlineexam.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.dao.CourseRepository;
import edu.nju.onlineexam.dao.ExamRepository;
import edu.nju.onlineexam.dao.ExamScoreRepository;
import edu.nju.onlineexam.dao.QuestionRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.CourseEntity;
import edu.nju.onlineexam.entity.ExamEntity;
import edu.nju.onlineexam.entity.ExamScoreEntity;
import edu.nju.onlineexam.entity.QuestionEntity;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.json.AnswerJson;
import edu.nju.onlineexam.json.SingleAnswer;
import edu.nju.onlineexam.service.ExamingService;
import edu.nju.onlineexam.utils.async.AsyncTask;
import edu.nju.onlineexam.utils.json.JsonTrans;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.email.MailSender;
import edu.nju.onlineexam.vo.PaperBlankVo;
import edu.nju.onlineexam.vo.QuestionVo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExamingServiceImpl implements ExamingService {

  @Resource
  ExamRepository examRepository;

  @Resource
  QuestionRepository questionRepository;

  @Resource
  UserRepository userRepository;

  @Resource
  CourseRepository courseRepository;

  @Resource
  ExamScoreRepository examScoreRepository;

  @Resource
  AsyncTask asyncTask;

  @Override
  public boolean checkPassword(long examId, String password) throws ServerException {
    ExamEntity examEntity = examRepository.findOne(examId);
    if(examEntity != null){
      password = password.trim();
      if(password.equals(examEntity.getCode())){
        return true;
      }
    }
    return false;
  }

  @Override
  public PaperBlankVo generatePaper(long examId, long userId,String password) throws ServerException{
    if(!checkPassword(examId,password)){
      throw new ServerException(400,"考试密码错误");
    }
    ExamScoreEntity examScoreEntity = examScoreRepository.findByExamIdAndStudentId(examId,userId);
    ExamEntity examEntity = examRepository.findOne(examId);
    //学生退出了考试页面并重入
    if(examScoreEntity != null ){
      List<QuestionEntity> questionEntities = JsonTrans.json2List(examScoreEntity.getQuestions(),
          new TypeReference<List<QuestionEntity>>() {});
      List<QuestionVo> questionVos = questionEntities.stream().map(QuestionVo::new).collect(
          Collectors.toList());
      UserEntity student = userRepository.findOne(userId);

      CourseEntity course = courseRepository.findOne(examEntity.getCourseId());
      return new PaperBlankVo(userId,examId,student.getName(),student.getNumber(),course.getName(),
          examEntity.getStartTime(),examEntity.getEndTime(),questionVos);
    }
    //学生第一次进入考试页面
    List<QuestionEntity> questionEntities = questionRepository.findByCourseId(examEntity.getCourseId());
    List<Integer> indexList = getRandomIndexes(questionEntities.size(),examEntity.getQuestionCount());
    List<QuestionEntity> resultQuestions = questionEntities.stream().
        filter(x->indexList.contains(questionEntities.indexOf(x))).collect(Collectors.toList());
    List<QuestionVo> questionVos = resultQuestions.stream().map(QuestionVo::new).collect(
        Collectors.toList());
    UserEntity student = userRepository.findOne(userId);
    CourseEntity course = courseRepository.findOne(examEntity.getCourseId());

    //保存考生试卷
    examScoreEntity = new ExamScoreEntity();
    examScoreEntity.setExamId(examId);
    examScoreEntity.setStudentId(userId);
    examScoreEntity.setQuestions(JsonTrans.toJson(resultQuestions));
    examScoreEntity.setStatus(0);//进行中
    examScoreRepository.save(examScoreEntity);

    return new PaperBlankVo(userId,examId,student.getName(),student.getNumber(),course.getName(),
        examEntity.getStartTime(),examEntity.getEndTime(),questionVos);
  }

  @Override
  public boolean commit(AnswerJson answer, long userId) throws ServerException{
    long examId = answer.getExamId();
    ExamScoreEntity examScoreEntity = examScoreRepository.findByExamIdAndStudentId(examId,userId);
    if(null == examScoreEntity){
      return false;
    }
    List<SingleAnswer> answerList = answer.getAnswers();
    examScoreEntity.setAnswers(JsonTrans.toJson(answerList));
    examScoreEntity.setStatus(1);//完成考试
    if(null != examScoreRepository.save(examScoreEntity)){
      //计算成绩并发送邮件
      asyncTask.doAfterCommit(examId,userId,answerList);
      return true;
    }else{
      return false;
    }

  }

  /**
   * 随机从问题列表中抽取试题对应的索引
   * @param total 所有的问题数量
   * @param count 抽取数量
   * @return 抽取的试题对应的索引值
   */
  private List<Integer> getRandomIndexes(int total,int count){
    List<Integer> resultList = new ArrayList<>();
    if(total < count){
      for(int i=0;i<total;i++){
        resultList.add(i);
      }
      return resultList;
    }

    int temp;
    Random random = new Random();
    for(int i=0;i<count;i++){
      temp = random.nextInt(total);
      if(!resultList.contains(temp)){
        resultList.add(temp);
      }else{
        i--;
      }
    }
    return resultList;
  }


}
