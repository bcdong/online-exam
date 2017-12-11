/**
 * server exception
 *
 * @author yyy
 * @create 2017-11-09 21:34
 */
package edu.nju.onlineexam.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerException extends Exception {
  private int code;
  private String msg;

  public ServerException(ServerCode serverCode){
    this.code = serverCode.getCode();
    this.msg = serverCode.getMsg();
  }


}
