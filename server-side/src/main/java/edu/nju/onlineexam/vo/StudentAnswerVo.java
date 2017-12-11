/**
 * the student answer vo
 *
 * @author yyy
 * @create 2017-11-11 21:07
 */
package edu.nju.onlineexam.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAnswerVo {
  private long qid;
  private List<String> ans;
}
