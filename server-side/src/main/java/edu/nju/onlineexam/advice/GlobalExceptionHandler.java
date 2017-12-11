package edu.nju.onlineexam.advice;

import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = ServerException.class)
  @ResponseBody
  public ResultVo<Object> handle(ServerException e) {
    log.error("Log ServerException by GlobalExceptionHandler", e);
    return new ResultVo<>(e.getCode(), e.getMsg(), null);
  }
}
