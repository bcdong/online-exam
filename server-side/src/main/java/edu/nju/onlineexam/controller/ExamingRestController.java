package edu.nju.onlineexam.controller;

import edu.nju.onlineexam.json.AnswerJson;
import edu.nju.onlineexam.service.ExamingService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.PaperBlankVo;
import edu.nju.onlineexam.vo.ResultVo;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * exam rest api
 *
 * @author yyy
 * @create 2017-11-09 21:53
 */
@RestController
@RequestMapping("/api/v1/examing")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class ExamingRestController {

  @Resource
  ExamingService examingService;

  @PostMapping(value="/checkPwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<Boolean> checkPwd(@RequestParam long examId, @RequestParam String password)
      throws ServerException{
    return new ResultVo<>(ServerCode.SUCCESS,examingService.checkPassword(examId,password));
  }

  @PostMapping(value="/generate",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<PaperBlankVo> generatePaper(@RequestParam long examId,@RequestParam String password) {

    JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    try {
      return new ResultVo<>(ServerCode.SUCCESS,examingService.generatePaper(examId,jwtUser.getId(),password));
    } catch (ServerException e) {
      return new ResultVo<>(e,null);
    }
  }

  @PostMapping(value = "/commit",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<Boolean> commit(@RequestBody AnswerJson answerJson) {
    JwtUser user = (JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    boolean success = false;
    try {
      success = examingService.commit(answerJson,user.getId());
      return new ResultVo<>(ServerCode.SUCCESS,success);
    } catch (ServerException e) {
      return new ResultVo<>(e,success);
    }
  }




}
