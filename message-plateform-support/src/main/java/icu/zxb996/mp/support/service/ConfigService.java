package icu.zxb996.mp.support.service;

/**
 * 读取配置服务
 *
 * @author Gavin Zhang
 * @date 2023/2/6 15:22
 */
public interface ConfigService {

    /**
     * 读取配置
     * 1、当启动使用了配置中心，优先读取远程配置
     * 2、当没有启动远程配置，读取本地 local.properties 配置文件的内容
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 对应配置结果
     */
    String getProperty(String key, String defaultValue);
}