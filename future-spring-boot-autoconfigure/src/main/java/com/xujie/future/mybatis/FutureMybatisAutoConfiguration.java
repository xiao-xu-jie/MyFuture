package com.xujie.future.mybatis;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.xujie.future.mybatis.config.FutureMybatisConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Xujie
 * @since 2025/3/9 18:45
 **/
@Configuration
@ConditionalOnBean(MybatisConfiguration.class)
@Import(FutureMybatisConfiguration.class)
public class FutureMybatisAutoConfiguration {
}
