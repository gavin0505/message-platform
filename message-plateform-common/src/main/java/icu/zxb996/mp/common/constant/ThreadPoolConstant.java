package icu.zxb996.mp.common.constant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 线程池常用配置
 * 仅供初始化的使用，代码里的线程池配置有可能被NacOS动态修改)
 *
 * @author Gavin Zhang
 * @date 2023/1/31 18:37
 */
public class ThreadPoolConstant {

    /**
     * small
     */
    public static final Integer SINGLE_CORE_POOL_SIZE = 1;
    public static final Integer SINGLE_MAX_POOL_SIZE = 1;
    public static final Integer SMALL_KEEP_LIVE_TIME = 10;

    /**
     * medium
     */
    public static final Integer COMMON_CORE_POOL_SIZE = 2;
    public static final Integer COMMON_MAX_POOL_SIZE = 2;
    public static final Integer COMMON_KEEP_LIVE_TIME = 60;
    public static final Integer COMMON_QUEUE_SIZE = 128;


    /**
     * big
     */
    public static final Integer BIG_QUEUE_SIZE = 1024;
    public static final BlockingQueue BIG_BLOCKING_QUEUE = new LinkedBlockingQueue(BIG_QUEUE_SIZE);

}
