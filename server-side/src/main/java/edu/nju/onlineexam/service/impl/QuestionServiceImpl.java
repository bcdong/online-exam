/**
 * 问题接口的实现类
 *
 * @author yyy
 * @create 2017-11-18 14:19
 */
package edu.nju.onlineexam.service.impl;

import edu.nju.onlineexam.dao.QuestionRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.QuestionEntity;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.json.QuestionJson;
import edu.nju.onlineexam.service.QuestionService;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.QuestionVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService{

  @Resource
  QuestionRepository questionRepository;

  @Resource
  UserRepository userRepository;

  @Override
  public String getTemplateUrl()throws ServerException {
    return null;
  }

  @Override
  public boolean uploadQuestions(List<QuestionJson> questions, long userId)throws ServerException {
    UserEntity user = userRepository.findOne(userId);
    List<QuestionEntity> entities = new ArrayList<>();
    for(QuestionJson question:questions){
      QuestionEntity entity = new QuestionEntity();
      entity.setAnswer(question.getAnswer());
      entity.setContent(question.getContent());
      entity.setCourseId(user.getCourseId());
      entity.setOptions(question.getOptions());
      entity.setType(question.getType());
      entities.add(entity);
    }
    entities = questionRepository.save(entities);


//    List<QuestionVo> vos = entities.stream().map(QuestionVo::new).collect(
//        Collectors.toList());

    return entities != null;
  }
}
