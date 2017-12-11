/**
 * 认证模块
 *
 * @author yyy
 * @create 2017-11-09 19:25
 */
package edu.nju.onlineexam.controller;

import edu.nju.onlineexam.json.UserJson;
import edu.nju.onlineexam.service.AuthService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.ResultVo;
import edu.nju.onlineexam.vo.UserVo;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

  @Resource
  AuthService authService;

  @PostMapping(value="/register",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<UserVo> register(@RequestBody UserJson user)
      throws ServerException {
    return new ResultVo<>(ServerCode.SUCCESS,authService.register(user));
  }

  @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<UserVo> login(@RequestParam String username,@RequestParam String password)
      throws ServerException{
    return new ResultVo<>(ServerCode.SUCCESS,authService.login(username,password));
  }

  @PostMapping(value="refreshToken", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResultVo<String> refreshToken(@RequestParam String oldToken){
    return new ResultVo<>(ServerCode.SUCCESS,authService.refresh(oldToken));
  }

  // @PreAuthorize("authentication")
  // 此处不要加PreAuthorize，因为在WebSecurityConfig配置类中加过了
  @PostMapping(value = "updatePassword")
  public ResultVo<UserVo> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) throws ServerException {
    JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return new ResultVo<>(ServerCode.SUCCESS, authService.updatePassword(user.getUsername(), oldPassword, newPassword));
  }

//  @GetMapping(value="/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  public ResultVo<Void> logout(HttpServletRequest httpServletRequest)
//      throws ServerException{
//
//    return null;
//  }



}
