/**
 * the answered exam papar vo
 *
 * @author yyy
 * @create 2017-11-11 20:37
 */
package edu.nju.onlineexam.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperAnsweredVo {
  private PaperBlankVo paperBlank;
  private List<StudentAnswerVo> studentAnswer;

}
