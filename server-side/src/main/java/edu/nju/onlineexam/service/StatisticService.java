/**
 * 考试后的统计接口
 *
 * @author yyy
 * @create 2017-11-17 14:52
 */
package edu.nju.onlineexam.service;

import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.PaperAnsweredVo;
import edu.nju.onlineexam.vo.PaperBlankVo;
import edu.nju.onlineexam.vo.ScoreVo;
import java.util.List;

public interface StatisticService {

  /**
   * 获取成绩单
   * @param examId 考试id
   * @return 成绩单
   */
  List<ScoreVo> getScoreReport(long examId) throws ServerException;

  /**
   * 生成空白试卷
   * @param examId 考试id
   * @param userId 学生id
   * @return 空白试卷
   * @throws ServerException 异常
   */
  PaperBlankVo getBlankPaper(long examId, long userId) throws ServerException;

  /**
   * 生成单个学生已作答的试卷
   * @param examId 考试id
   * @param userId 学生Id
   * @return 已作答的试卷
   */
  PaperAnsweredVo getAnsweredPaper(long examId, long userId) throws ServerException;

  /**
   * 生成本场考试所有学生的已作答试卷
   * @param examId 考试id
   * @return 所有考生的已作答试卷
   */
  List<PaperAnsweredVo> getAllAnsweredPaper(long examId) throws ServerException;
}
