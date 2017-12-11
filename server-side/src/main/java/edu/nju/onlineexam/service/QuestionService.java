/**
 * 下载问题模板与问题上传
 *
 * @author yyy
 * @create 2017-11-16 17:22
 */
package edu.nju.onlineexam.service;

import edu.nju.onlineexam.json.QuestionJson;
import edu.nju.onlineexam.utils.ServerException;
import java.util.List;

public interface QuestionService {

  /**
   * 获取模板url(暂定，未必使用)
   * @return url
   */
  String getTemplateUrl() throws ServerException;

  /**
   * 上传问题
   * @param questions 问题
   * @return 是否上传成功
   */
  boolean uploadQuestions(List<QuestionJson> questions,long userId) throws ServerException;

}
