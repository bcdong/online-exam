package edu.nju.onlineexam.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * register request body
 *
 * @author yyy
 * @create 2017-11-19 14:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJson {

  private String username;
  private String password;
  private String name;
  private String email;
  private String studentNumber;
}
