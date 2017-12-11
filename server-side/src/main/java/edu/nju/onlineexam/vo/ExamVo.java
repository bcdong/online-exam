/**
 * exam vo
 *
 * @author yyy
 * @create 2017-11-09 21:58
 */
package edu.nju.onlineexam.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.nju.onlineexam.entity.ExamEntity;
import edu.nju.onlineexam.utils.json.JsonTrans;
import edu.nju.onlineexam.utils.ServerException;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class ExamVo {


  private long id;
//  private Timestamp createTime;
//  private Timestamp updateTime;
  private long courseId;
  private String courseName;
  private String title;
  private int questionCount;
  private int score;
  private Timestamp startTime;
  private Timestamp endTime;
  private List<String> students;

  public ExamVo(ExamEntity examEntity){
    BeanUtils.copyProperties(examEntity,this,"students","code");
    try {
      students = JsonTrans.json2List(examEntity.getStudents(), new TypeReference<List<String>>() {});
    } catch (ServerException e) {
      e.printStackTrace();
    }
  }
}
