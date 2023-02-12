package icu.zxb996.mp.handler.deduplication.limit;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.service.AbstractDeduplicationService;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象去重限制服务器
 *
 * @author Gavin Zhang
 * @date 2023/2/7 18:14
 */
public abstract class AbstractLimitService implements LimitService {

    /**
     * 获取得到当前消息模板所有的去重Key
     *
     * @param taskInfo 消息体
     * @return 对应key
     */
    protected List<String> deduplicationAllKey(AbstractDeduplicationService service, TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(service, taskInfo, receiver);
            result.add(key);
        }
        return result;
    }

    /**
     * 调用去重服务的方法，构建去重key
     *
     * @param service  去重服务
     * @param taskInfo 消息体
     * @param receiver 接收者
     * @return 去重key
     */
    protected String deduplicationSingleKey(AbstractDeduplicationService service, TaskInfo taskInfo, String receiver) {
        return service.deduplicationSingleKey(taskInfo, receiver);
    }
}
