/**
 * 单个题目学生的答案
 *
 * @author yyy
 * @create 2017-11-17 14:48
 */
package edu.nju.onlineexam.json;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleAnswer {
  private long qid;
  private List<String> ans;

  @Override
  public boolean equals(Object o){
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SingleAnswer that = (SingleAnswer) o;

    if(qid != that.qid){
      return false;
    }

    if(ans.size() != that.ans.size()){
      return false;
    }

    for(int i=0;i<that.ans.size();i++){
      if(!ans.contains(that.ans.get(i))){
        return false;
      }
    }

    return true;
  }
}
