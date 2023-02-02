package icu.zxb996.mp.support.config;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gavin Zhang
 * @date 2023/2/2 15:55
 */
@Configuration
@NacosPropertySource(dataId = "mp-dynamic-tp.yml", autoRefreshed = true)
public class NacosConfig {
}