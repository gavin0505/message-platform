package icu.zxb996.mp.support.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;

/**
 * Nacos工具类
 *
 * @author Gavin Zhang
 * @date 2023/2/6 15:25
 */
@Slf4j
@Component
public class NacosUtils {

    @Value("${mp.nacos.server}")
    private String nacosServer;
    @Value("${mp.nacos.username}")
    private String nacosUsername;
    @Value("${mp.nacos.password}")
    private String nacosPassword;
    @Value("${mp.nacos.group}")
    private String nacosGroup;
    @Value("${mp.nacos.dataId}")
    private String nacosDataId;
    @Value("${mp.nacos.namespace}")
    private String nacosNamespace;
    private final Properties request = new Properties();
    private final Properties properties = new Properties();

    /**
     * 获取配置
     */
    public String getProperty(String key, String defaultValue) {
        try {
            String property = this.getContext();
            if (StringUtils.hasText(property)) {
                properties.load(new StringReader(property));
            }
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        String property = properties.getProperty(key);
        return StrUtil.isBlank(property) ? defaultValue : property;
    }

    private String getContext() {
        String context = null;
        try {
            request.put(PropertyKeyConst.SERVER_ADDR, nacosServer);
            request.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
            request.put(PropertyKeyConst.USERNAME, nacosUsername);
            request.put(PropertyKeyConst.PASSWORD, nacosPassword);
            context = NacosFactory.createConfigService(request)
                    .getConfig(nacosDataId, nacosGroup, 5000);
        } catch (NacosException e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}
