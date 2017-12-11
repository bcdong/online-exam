package edu.nju.onlineexam.service;

import edu.nju.onlineexam.json.QuestionJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  @Test
  public void getTemplateUrl() throws Exception {
  }

  @Test
  public void uploadQuestions() throws Exception {
    log.info("execute this test");
//    List<QuestionJson> list = new ArrayList<>();
//    QuestionJson q1 = new QuestionJson("content1", "options1", "answer1");
//    q1.setType(0);
//    QuestionJson q2 = new QuestionJson("content2", "options2", "answer2");
//    q1.setType(1);
//    list.add(q1);
//    list.add(q2);
//    questionService.uploadQuestions(list, 1);
  }

}