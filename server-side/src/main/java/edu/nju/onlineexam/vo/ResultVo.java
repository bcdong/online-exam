/**
 * server response
 *
 * @author yyy
 * @create 2017-11-09 21:17
 */
package edu.nju.onlineexam.vo;

import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo<T> {
  private int code;
  private String msg;
  private T data;

  public ResultVo(ServerCode serverCode,T data){
    this.code = serverCode.getCode();
    this.msg = serverCode.getMsg();
    this.data = data;
  }

  public ResultVo(ServerException e,T data) {
    this.code = e.getCode();
    this.msg = e.getClass().getName();
    this.data = data;
  }


}
