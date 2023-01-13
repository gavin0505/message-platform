package icu.zxb996.mp.support.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gavin Zhang
 * @date 2023/1/13 14:54
 */
@Configuration
@MapperScan(basePackages = "icu.zxb996.mp")
public class MyBatisPlusConfig {
}
