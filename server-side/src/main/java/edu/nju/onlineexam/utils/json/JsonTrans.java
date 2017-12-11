package edu.nju.onlineexam.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nju.onlineexam.utils.ServerCode;
import edu.nju.onlineexam.utils.ServerException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * json对象转换工具类
 *
 * @author yyy
 * @create 2017-12-04 15:09
 */
@Slf4j
public class JsonTrans {

  public static ObjectMapper objectMapper;

  /**
   * json转list对象
   * @param json json字符串
   * @param tClass 对象类型
   * @param <T> 泛型
   * @return 对象列表
   */
  public static <T> T json2List(String json,TypeReference<T> tClass) throws ServerException {
    if(objectMapper == null){
      objectMapper = new ObjectMapper();
    }
    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    try {
      return objectMapper.readValue(json,tClass);
    } catch (IOException e) {
      log.error("json2list occurs exception {}",e);
      throw new ServerException(ServerCode.JSON_MAPPER_ERROR);
    }
  }

  /**
   * json转对象
   * @param json json字符串
   * @param tClass 对象类型
   * @param <T> 泛型
   * @return 对象
   */
  public static <T> T json2Obj(String json, Class<T> tClass) throws ServerException {
    if(objectMapper == null){
      objectMapper = new ObjectMapper();
    }

    try {
      return objectMapper.readValue(json,tClass);
    } catch (IOException e) {
      log.error("json2Obj occurs exception {}",e);
      throw new ServerException(ServerCode.JSON_MAPPER_ERROR);
    }
  }

  /**
   * 对象转json
   * @param t java对象
   * @param <T> 泛型
   * @return 字符串
   */
  public static <T> String toJson(T t) throws ServerException {
    if(objectMapper == null){
      objectMapper = new ObjectMapper();
    }

    try {
      return objectMapper.writeValueAsString(t);
    } catch (JsonProcessingException e) {
      log.error("obj 2 json occurs exception {}",e);
      throw new ServerException(ServerCode.JSON_MAPPER_ERROR);
    }
  }
}
