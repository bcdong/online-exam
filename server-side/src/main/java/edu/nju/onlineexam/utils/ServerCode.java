/**
 * return code and msg
 *
 * @author yyy
 * @create 2017-11-09 21:29
 */
package edu.nju.onlineexam.utils;


public enum ServerCode {

  SUCCESS(200,"OK"),

  EMAIL_FAILURE(601,"邮件发送失败"),

  UNAUTHORIZED(401, "用户名或密码错误"),

  // 注册时用户名或者学号重复
  USER_DUPLICATE(700, "用户已存在"),

  USER_NOT_EXIST(701,"用户不存在"),

  USER_EXAM_NOT_EXIST(702,"学生未参加本场考试"),

  JSON_MAPPER_ERROR(801,"json数据转换错误");


  private int code;
  private String msg;
  ServerCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

}
