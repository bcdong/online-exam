/**
 * 用户vo
 *
 * @author yyy
 * @create 2017-11-09 19:28
 */
package edu.nju.onlineexam.vo;

import edu.nju.onlineexam.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
  private long id;
  private String username;
  private int type;
  private String name;
  private String number;
  private Long courseId;
  private String courseName;
  private String token;

  public UserVo(UserEntity userEntity){

    BeanUtils.copyProperties(userEntity,this);



  }

}
