/**
 * 设置考试接口
 *
 * @author yyy
 * @create 2017-11-16 17:04
 */
package edu.nju.onlineexam.service;


import edu.nju.onlineexam.utils.ServerException;
import edu.nju.onlineexam.vo.ExamVo;
import java.util.List;

public interface ExamService {

  /**
   * 添加考试
   * @param courseId 课程id
   * @param title 考试名称
   * @param questionCount 试题数量
   * @param questionScore 每个试题的分数
   * @param startTime 考试开始时间
   * @param endTime 考试结束时间
   * @param students 参见考试的学生学号名单
   * @return 考试信息
   */
  ExamVo addExam(long courseId,String title,int questionCount,
      int questionScore,long startTime,long endTime,List<String> students) throws ServerException;

  /**
   * 查询考试列表
   * @param userId 用户id：学生id或者教师id
   * @param status -1：未开始，0：进行中，1：已完成
   * @return 考试信息
   */
  List<ExamVo> queryExam(long userId,int status) throws ServerException;

}
