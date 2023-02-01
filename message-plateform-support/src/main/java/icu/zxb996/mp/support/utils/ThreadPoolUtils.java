package icu.zxb996.mp.support.utils;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import icu.zxb996.mp.support.config.ThreadPoolExecutorShutdownDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 线程池工具类
 *
 * @author Gavin Zhang
 * @date 2023/1/31 18:40
 */
@Component
public class ThreadPoolUtils {

    @Resource(name = "threadPoolExecutorShutdownDefinition")
    private ThreadPoolExecutorShutdownDefinition shutdownDefinition;

    /**
     * dtp日志记录头名
     */
    private static final String SOURCE_NAME = "mp";


    /**
     * 1. 将当前线程池 加入到 动态线程池内
     * 2. 注册 线程池 被Spring管理，优雅关闭
     */
    public void register(DtpExecutor dtpExecutor) {
        DtpRegistry.registerDtp(dtpExecutor, SOURCE_NAME);
        shutdownDefinition.registryExecutor(dtpExecutor);
    }
}
