/**
 * 考试过程支持的接口
 *
 * @author yyy
 * @create 2017-11-17 14:02
 */
package edu.nju.onlineexam.service;

import edu.nju.onlineexam.json.AnswerJson;
import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.PaperBlankVo;

public interface ExamingService {

  /**
   * 检查考试密码
   * @param examId 本场考试id
   * @param password 考试密码
   * @return 检查是否通过
   */
  boolean checkPassword(long examId, String password) throws ServerException;

  /**
   * 随机生成考生试卷
   * @param examId 考试Id
   * @param userId  学生id
   * @return 未作答试卷
   */
  PaperBlankVo generatePaper(long examId, long userId, String password) throws ServerException;

  /**
   * 提交试卷
   * @param answer 学生的答案
   * @param userId 学生id
   * @return 提交是否成功
   */
  boolean commit(AnswerJson answer, long userId) throws ServerException;
}
