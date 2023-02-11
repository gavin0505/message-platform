package icu.zxb996.mp.handler.deduplication.limit;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;
import icu.zxb996.mp.handler.deduplication.service.AbstractDeduplicationService;

import java.util.Set;

/**
 * 去重限制接口
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:21
 */
public interface LimitService {

    /**
     * 去重限制
     *
     * @param service  去重器对象
     * @param taskInfo 消息体
     * @param param    去重参数
     * @return 返回不符合条件的手机号码
     */
    Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param);
}
