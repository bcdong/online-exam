package edu.nju.onlineexam.service.impl;

import edu.nju.onlineexam.dao.CourseRepository;
import edu.nju.onlineexam.dao.UserRepository;
import edu.nju.onlineexam.entity.UserEntity;
import edu.nju.onlineexam.json.UserJson;
import edu.nju.onlineexam.service.AuthService;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.utils.async.AsyncTask;
import edu.nju.onlineexam.utils.email.MailSender;
import edu.nju.onlineexam.utils.token.JwtTokenUtil;
import edu.nju.onlineexam.utils.token.JwtUser;
import edu.nju.onlineexam.vo.UserVo;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author yyy
 * @create 2017-11-18 20:12
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  @Resource
  private AuthenticationManager authenticationManager;
  @Resource
  private UserDetailsService userDetailsService;
  @Resource
  private JwtTokenUtil jwtTokenUtil;
  @Resource
  private UserRepository userRepository;
  @Resource
  private CourseRepository courseRepository;
  @Resource
  private AsyncTask asyncTask;

  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Override
  public UserVo register(UserJson user) throws ServerException {
    UserEntity existUser = userRepository.findByUsernameOrNumber(user.getUsername(), user.getStudentNumber());
    if (existUser != null) {
      throw new ServerException(ServerCode.USER_DUPLICATE);
    }
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(user.getUsername());
    userEntity.setPassword(user.getPassword());
    userEntity.setEmail(user.getEmail());
    userEntity.setName(user.getName());
    userEntity.setNumber(user.getStudentNumber());
    userEntity.setType(0);  //0为学生
    UserVo userVo = new UserVo(userRepository.save(userEntity));
    //发送注册邮件
    asyncTask.sendRegisterEmail(user.getEmail());

    return userVo;
  }

  @Override
  public UserVo login(String username, String password) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
    final Authentication authentication = authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);
    final String token = jwtTokenUtil.generateToken(jwtUser);

    final UserVo userVo = new UserVo(userRepository.findOne(jwtUser.getId()));
    if(userVo.getType()==1){
      userVo.setCourseName(courseRepository.findOne(userVo.getCourseId()).getName());
    }
    userVo.setToken(token);
    return userVo;

  }

  @Override
  public String refresh(String oldToken) {
    String username = jwtTokenUtil.getUsernameFromToken(oldToken);
    JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.canTokenBeRefreshed(oldToken, user.getLastPasswordResetDate())){
      return jwtTokenUtil.refreshToken(oldToken);
    }
    return null;
  }

  @Override
  public UserVo updatePassword(String username, String oldPass, String newPass) throws ServerException {
    UserEntity existUser = userRepository.findByUsername(username);
    if (existUser == null) {
      throw new ServerException(ServerCode.USER_NOT_EXIST);
    }
    if (!existUser.getPassword().equals(oldPass)) {
      throw new ServerException(ServerCode.UNAUTHORIZED);
    } else {
      existUser.setPassword(newPass);
    }

    return new UserVo(userRepository.save(existUser));
  }
}
