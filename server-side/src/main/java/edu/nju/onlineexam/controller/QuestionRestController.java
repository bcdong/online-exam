/**
 * controller of exam's questions
 *
 * @author yyy
 * @create 2017-11-09 22:56
 */
package edu.nju.onlineexam.controller;

import edu.nju.onlineexam.json.QuestionJson;
import edu.nju.onlineexam.service.QuestionService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.file.FileUtil;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.ResultVo;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/questions")
@Slf4j
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class QuestionRestController {

  @Autowired
  QuestionService questionService;

  @GetMapping(value = "/templateUrl")
  public ResponseEntity<Resource> getTemplateUrl()
      throws IOException {
    return FileUtil.download("static/question_template.xlsx","问题模板.xlsx");
  }

  @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<Boolean> uploadQuestions(@RequestBody List<QuestionJson> questionsJson)
      throws ServerException{
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.debug("Got upload questions. The first questions is:");
    log.debug("content: " + questionsJson.get(0).getContent());
    log.debug("options: " + questionsJson.get(0).getOptions());
    log.debug("answer: " + questionsJson.get(0).getAnswer());
    log.debug("type: " + questionsJson.get(0).getType());
    return new ResultVo<>(ServerCode.SUCCESS,questionService.uploadQuestions(questionsJson,user.getId()));
  }

}
