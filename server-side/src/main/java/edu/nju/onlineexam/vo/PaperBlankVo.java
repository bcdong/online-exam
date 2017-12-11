package edu.nju.onlineexam.vo;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * the exam vo not answered, the blank paper
 *
 * @author yyy
 * @create 2017-11-09 22:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperBlankVo {
  private long studentId;
  private long examId;
  private String studentName;
  private String studentNumber;
  private String courseName;
  private Timestamp startTime;
  private Timestamp endTime;
  private List<QuestionVo> questions;

}
