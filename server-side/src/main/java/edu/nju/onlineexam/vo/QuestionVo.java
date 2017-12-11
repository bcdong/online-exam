/**
 * question vo
 *
 * @author yyy
 * @create 2017-11-10 0:07
 */
package edu.nju.onlineexam.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.entity.QuestionEntity;
import edu.nju.onlineexam.utils.json.JsonTrans;
import edu.nju.onlineexam.utils.ServerException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
  private long id;
  private long courseId;
  private int type;
  private String content;
  private List<OptionVo> options;

  public QuestionVo(QuestionEntity questionEntity){
    BeanUtils.copyProperties(questionEntity,this,"options");
    try {
      options = JsonTrans.json2List(questionEntity.getOptions(), new TypeReference<List<OptionVo>>() {});
    } catch (ServerException e) {
      e.printStackTrace();
    }
  }

}
