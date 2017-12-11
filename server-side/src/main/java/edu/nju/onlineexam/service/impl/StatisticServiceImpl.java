/**
 * 考试统计接口的实现类
 *
 * @author yyy
 * @create 2017-11-18 14:20
 */
package edu.nju.onlineexam.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.dao.ExamRepository;
import edu.nju.onlineexam.dao.ExamScoreRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.ExamScoreEntity;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.service.ExamingService;
import edu.nju.onlineexam.service.StatisticService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.file.FileUtil;
import edu.nju.onlineexam.utils.json.JsonTrans;
import edu.nju.onlineexam.vo.PaperAnsweredVo;
import edu.nju.onlineexam.vo.PaperBlankVo;
import edu.nju.onlineexam.vo.ScoreVo;
import edu.nju.onlineexam.vo.StudentAnswerVo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

  @Resource
  ExamScoreRepository examScoreRepository;

  @Resource
  UserRepository userRepository;

  @Resource
  ExamingService examingService;

  @Resource
  ExamRepository examRepository;

  @Override
  public List<ScoreVo> getScoreReport(long examId) throws ServerException {
    List<ExamScoreEntity> entities = examScoreRepository.findByExamId(examId);
    if(entities == null){
      return null;
    }
    List<ScoreVo> scoreVos = new ArrayList<>();
    for(ExamScoreEntity entity:entities){
      ScoreVo vo = new ScoreVo();
      UserEntity user = userRepository.findOne(entity.getStudentId());
      vo.setStudentNumber(user.getNumber());
      vo.setName(user.getName());
      vo.setScore(entity.getScore());
      scoreVos.add(vo);
    }
    return scoreVos;
  }

  @Override
  public PaperBlankVo getBlankPaper(long examId, long userId) throws ServerException {
    ExamScoreEntity examScoreEntity = examScoreRepository.findByExamIdAndStudentId(examId,userId);
    if(examScoreEntity != null){
      String password = examRepository.findOne(examId).getCode();
      return examingService.generatePaper(examId,userId,password);
    }
    throw new ServerException(ServerCode.USER_EXAM_NOT_EXIST);
  }

  @Override
  public PaperAnsweredVo getAnsweredPaper(long examId, long userId) throws ServerException{
    PaperBlankVo paperBlankVo = getBlankPaper(examId,userId);
    ExamScoreEntity examScoreEntity = examScoreRepository.findByExamIdAndStudentId(examId,userId);
    String answerStr = examScoreEntity.getAnswers();
    List<StudentAnswerVo> answerVos = new ArrayList<>();
    //参加考试但是没有答题
    if(answerStr != null){
      answerVos = JsonTrans.json2List(examScoreEntity.getAnswers(),
          new TypeReference<List<StudentAnswerVo>>() {});
    }
    return new PaperAnsweredVo(paperBlankVo,answerVos);
  }

  @Override
  public List<PaperAnsweredVo> getAllAnsweredPaper(long examId)throws ServerException{
    return null;
  }



}
