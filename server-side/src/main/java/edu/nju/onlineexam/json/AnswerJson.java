/**
 * the request body of answers
 *
 * @author yyy
 * @create 2017-11-09 23:40
 */
package edu.nju.onlineexam.json;

import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class AnswerJson {
  private long examId;

  private List<SingleAnswer> answers;

}
