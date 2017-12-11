/**
 * controller of the statistic after exam
 *
 * @author yyy
 * @create 2017-11-09 23:45
 */
package edu.nju.onlineexam.controller;

import edu.nju.onlineexam.dao.ExamRepository;
import edu.nju.onlineexam.service.StatisticService;
import edu.nju.onlineexam.service.impl.ExamingServiceImpl;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.file.FileUtil;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.PaperAnsweredVo;
import edu.nju.onlineexam.vo.PaperBlankVo;
import edu.nju.onlineexam.vo.ResultVo;
import edu.nju.onlineexam.vo.ScoreVo;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistic")
@Slf4j
public class StatisticRestController {

  @Resource
  StatisticService statisticService;

  @Resource
  ExamRepository examRepository;

  /**
   * 老师生成成绩单
   * @param examId
   * @return
   * @throws ServerException
   */
  @PreAuthorize("hasRole('ROLE_TEACHER')")
  @GetMapping(value = "/scoreReport/{examId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<org.springframework.core.io.Resource> getScoreReport(@PathVariable long examId)
      throws ServerException {
    //创建表头
    List<String> titles = Arrays.asList("studentId","name","score");
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("成绩统计表");
    FileUtil.createExcelTitle(workbook,sheet,titles);
    //插入数据
    List<ScoreVo> scoreVos = statisticService.getScoreReport(examId);
    if(scoreVos == null){
      return ResponseEntity.badRequest().body(null);
    }
    int rowNum = 1;
    for(ScoreVo vo:scoreVos){
      HSSFRow row = sheet.createRow(rowNum);
      row.createCell(0).setCellValue(vo.getStudentNumber());
      row.createCell(1).setCellValue(vo.getName());
      row.createCell(2).setCellValue(vo.getScore());
      rowNum++;
    }
    //将文件内容返回
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      return FileUtil.download(out.toByteArray(),examRepository.findOne(examId).getTitle()+"成绩单.xls");
    } catch (IOException e) {
      log.error("get score report occurs exception {}",e);
      return ResponseEntity.badRequest().body(null);
    }
  }

  /**
   * 学生生成考前试卷
   * @param examId
   * @return
   */
  @PostMapping(value = "/blankPaper", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<PaperBlankVo> getBlankPaper(@RequestParam long examId) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    try {
      return new ResultVo<>(ServerCode.SUCCESS,statisticService.getBlankPaper(examId,user.getId()));
    } catch (ServerException e) {
      return new ResultVo<>(e,null);
    }
  }

  /**
   * 学生生成考后试卷
   * @param examId
   * @return
   */
  @PostMapping(value = "/answeredPaper", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<PaperAnsweredVo> getAnsweredPaper(@RequestParam long examId) {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    try {
      return new ResultVo<>(ServerCode.SUCCESS,statisticService.getAnsweredPaper(examId,user.getId()));
    } catch (ServerException e) {
      return new ResultVo<>(e,null);
    }
  }

  /**
   * 老师批量生成试卷，未实现
   * @param examId
   * @return
   * @throws ServerException
   */
//  todo
  @PostMapping(value = "/allAnsweredPaper/{examId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<List<PaperAnsweredVo>> getAllAnsweredPaper(@PathVariable long examId)
      throws ServerException{
    return null;
  }

}
