package icu.zxb996.mp.support.service.impl;

import cn.hutool.setting.dialect.Props;
import icu.zxb996.mp.support.service.ConfigService;
import icu.zxb996.mp.support.utils.NacosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 读取配置类服务
 *
 * @author Gavin Zhang
 * @date 2023/2/6 15:23
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    /**
     * 本地配置文件
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private final Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);

    /**
     * nacos配置
     */
    @Value("${mp.nacos.enabled}")
    private Boolean enableNacos;
    @Autowired
    private NacosUtils nacosUtils;

    @Override
    public String getProperty(String key, String defaultValue) {

        if (enableNacos) {
            return nacosUtils.getProperty(key, defaultValue);
        } else {
            return props.getProperty(key, defaultValue);
        }
    }
}