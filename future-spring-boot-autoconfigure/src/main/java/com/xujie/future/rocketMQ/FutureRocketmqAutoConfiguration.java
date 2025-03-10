package com.xujie.future.rocketMQ;

import com.xujie.future.rocketMQ.config.RocketMQProducerConfig;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Xujie
 * @since 2025/3/9 17:05
 **/

@Configuration
@ConditionalOnBean(RocketMQAutoConfiguration.class)
@Import(RocketMQProducerConfig.class)
public class FutureRocketmqAutoConfiguration {
}
