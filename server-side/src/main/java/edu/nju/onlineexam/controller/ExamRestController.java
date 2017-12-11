/**
 * exam setting api
 *
 * @author yyy
 * @create 2017-11-09 21:56
 */
package edu.nju.onlineexam.controller;

import edu.nju.onlineexam.json.ExamJson;
import edu.nju.onlineexam.service.ExamService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.file.FileUtil;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.ExamVo;
import edu.nju.onlineexam.vo.ResultVo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamRestController {

  @Resource
  ExamService examService;

  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @GetMapping(value="/template")
  public ResponseEntity<org.springframework.core.io.Resource> getTemplate() throws IOException {
    return FileUtil.download("static/student_template.xlsx","考生名单模板.xlsx");
  }

  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @PostMapping(value="/add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<ExamVo> add(@RequestBody ExamJson examJson) {
    ExamVo vo;
    try {
      vo = examService.addExam(examJson.getCourseId(),examJson.getTitle(),
          examJson.getQuestionCount(),examJson.getQuestionScore(),
          examJson.getStartTime(), examJson.getEndTime(),examJson.getStudents());
    } catch (ServerException e) {
      return new ResultVo<>(e,null);
    }
    return new ResultVo<>(ServerCode.SUCCESS,vo);
  }

  /**
   * 查询考试接口
   * @param status -1 未开始，0 进行中，1 已结束
   * @return 考试列表
   */
  @PostMapping(value="/query",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<List<ExamVo>> query(@RequestParam int status) {

    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<ExamVo> voList = new ArrayList<>();
    try {
      voList = examService.queryExam(user.getId(),status);
      return new ResultVo<>(ServerCode.SUCCESS,voList);
    } catch (ServerException e) {
      return new ResultVo<>(e,voList);
    }

  }






}
