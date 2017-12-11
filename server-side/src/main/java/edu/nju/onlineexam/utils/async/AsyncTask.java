package edu.nju.onlineexam.utils.async;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.dao.ExamRepository;
import edu.nju.onlineexam.dao.ExamScoreRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.ExamEntity;
import edu.nju.onlineexam.entity.ExamScoreEntity;
import edu.nju.onlineexam.entity.QuestionEntity;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.json.SingleAnswer;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.email.MailSender;
import edu.nju.onlineexam.utils.json.JsonTrans;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步任务类
 *
 * @author yyy
 * @create 2017-12-05 18:36
 */
@Component
@Slf4j
public class AsyncTask {

  @Resource
  UserRepository userRepository;

  @Resource
  ExamRepository examRepository;

  @Resource
  ExamScoreRepository examScoreRepository;

  /**
   * 异步发送注册邮件
   * @param email 邮箱
   * @throws ServerException 异常
   */
  @Async("onlineExamAsync")
  public void sendRegisterEmail(String email) throws ServerException {
    try {
      MailSender.sendEmail(new String[]{email},"您已成功注册在线考试系统",
          "您好</br>"+
              "您已成功注册online_exam在线考试系统</br>"+
              "如非本人，请忽略此邮件");
    } catch (Exception e) {
      log.error("email send occurs exception {}",e);
      throw new ServerException(ServerCode.EMAIL_FAILURE);
    }
  }
  /**
   * 异步发送考试密码
   * @param students 学生学号列表
   * @param title 考试名称
   * @param code 考试密码
   * @throws ServerException 异常
   */
  @Async("onlineExamAsync")
  public void sendCode(List<String> students, String title, String code) throws ServerException {
    log.info("start send email");
    int len = students.size();
    String[] emails = new String[len];
    for(int i=0;i<len;i++){
      String number = students.get(i);
      UserEntity user = userRepository.findByNumber(number);
      if(user == null){
        throw new ServerException(601,"Student"+number+"is not exist");
      }
      emails[i] = userRepository.findByNumber(number).getEmail();
    }
    try {
      MailSender.sendEmail(emails,"考试通知","您好，您已被加入考试 "+title+"</br>"
          +"考试密码为: "+code+"</br>"
          +"请保存好此密码，以便参加考试");
    } catch (Exception e) {
      log.error("send email occurs exception {}",e);
      throw new ServerException(ServerCode.EMAIL_FAILURE);
    }
    log.info("end send email");
  }


  @Async("onlineExamAsync")
  public void doAfterCommit(long examId,long userId, List<SingleAnswer> answerList)
      throws ServerException {
    ExamScoreEntity examScoreEntity = examScoreRepository.findByExamIdAndStudentId(examId,userId);
    if(null == examScoreEntity){
      throw new ServerException(ServerCode.USER_EXAM_NOT_EXIST);
    }
    //计算成绩
    List<QuestionEntity> questionEntities = JsonTrans.json2List(examScoreEntity.getQuestions(),
        new TypeReference<List<QuestionEntity>>() {});

    ExamEntity examEntity = examRepository.findOne(examId);
    int score = calculateScore(questionEntities,answerList,examEntity.getScore());
    //持久化数据库
    examScoreEntity.setScore(score);
    examScoreRepository.save(examScoreEntity);
    //发送邮件
    UserEntity user = userRepository.findOne(userId);
    try {
      MailSender.sendEmail(new String[]{user.getEmail()},"考试完成通知",
          "亲爱的"+user.getNumber()+"用户，您好！</br>" +
              "恭喜您,您已完成本场考试："+examEntity.getTitle()+"</br>"+
              "您本次的得分为："+score+",本次考试满分为："+examEntity.getQuestionCount()*examEntity.getScore()+"</br>"+
              "感谢使用在线考试系统，欢迎您下次再来</br>"+
              "如非本人请忽略此邮件");
    } catch (Exception e) {
      throw new ServerException(ServerCode.EMAIL_FAILURE);
    }
  }

  /**
   * 比较答案
   * @param questions 问题及正确答案
   * @param answers 学生提交的答案
   * @param singleScore 每个题目的分数
   * @return 得分
   */
  private int calculateScore(List<QuestionEntity> questions, List<SingleAnswer> answers, int singleScore)
      throws ServerException {
    log.info("begin calculate score");
    int correctNum = 0;
    for(QuestionEntity question : questions){
      List<String> ans = JsonTrans.json2List(question.getAnswer(),
          new TypeReference<List<String>>() {});
      SingleAnswer sa = new SingleAnswer(question.getId(),ans);
      if(answers.contains(sa)){
        correctNum++;
      }
    }
    log.info("end calculate score");
    return correctNum*singleScore;
  }

}
