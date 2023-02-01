package icu.zxb996.mp.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import icu.zxb996.mp.handler.config.HandlerThreadPoolConfig;
import icu.zxb996.mp.handler.utils.GroupIdMappingUtils;
import icu.zxb996.mp.support.utils.ThreadPoolUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 消息类型保持器
 *
 * @author Gavin Zhang
 * @date 2023/1/31 18:26
 */
@Component
public class TaskPendingHolder {

    @Resource(name = "threadPoolUtils")
    private ThreadPoolUtils threadPoolUtils;

    /**
     * 通过不同groupId保存不同的线程池
     */
    private final Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的groupId
     */
    private static final List<String> GROUP_IDS = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     */
    @PostConstruct
    public void init() {
        /*
         * example ThreadPoolName: mp.email.notice
         *
         * 可以通过Nacos配置：dynamic-tp-Nacos-dtp.yml  动态修改线程池的信息
         */
        for (String groupId : GROUP_IDS) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(executor);
            // 保存线程池
            taskPendingHolder.put(groupId, executor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId 消费者组Id
     * @return 对应线程池
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }
}
