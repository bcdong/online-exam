package edu.nju.onlineexam.utils.async;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 自定义executor，以便异步方法执行
 *
 * @author yyy
 * @create 2017-12-05 17:48
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

  @Value("${online.exam.executor.corePoolSize}")
  int corePoolSize;

  @Value("${online.exam.executor.maxPoolSize}")
  int maxPoolSize;

  @Value("${online.exam.executor.queueCapacity}")
  int queueCapacity;

  @Bean
  public Executor onlineExamAsync() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setThreadNamePrefix("OnlineExamExecutor-");
    executor.initialize();
    log.info("executor is : {}",executor);
    return executor;
  }

}
