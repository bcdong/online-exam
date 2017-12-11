package edu.nju.onlineexam.service;

import edu.nju.onlineexam.json.UserJson;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.UserVo;

public interface AuthService {

  UserVo register(UserJson user) throws ServerException;

  UserVo login(String username, String password);

  String refresh(String oldToken);

  UserVo updatePassword(String username, String oldPass, String newPass) throws ServerException;
}
