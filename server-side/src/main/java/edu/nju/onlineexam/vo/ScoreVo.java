/**
 * 成绩单中的单个成绩vo
 *
 * @author yyy
 * @create 2017-11-09 23:50
 */
package edu.nju.onlineexam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreVo {
  private String studentNumber;
  private String name;
  private int score;
}
