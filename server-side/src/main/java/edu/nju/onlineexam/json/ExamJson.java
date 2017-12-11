/**
 * the request json of adding new exam
 *
 * @author yyy
 * @create 2017-11-09 22:00
 */
package edu.nju.onlineexam.json;

import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class ExamJson {

  private long courseId;
  @NonNull
  private String title;
  private int questionCount;
  private int questionScore;
  private long startTime;
  private long endTime;
  @NonNull
  private List<String> students;

}
