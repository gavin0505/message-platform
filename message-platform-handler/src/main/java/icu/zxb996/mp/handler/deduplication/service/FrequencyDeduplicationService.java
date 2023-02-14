package icu.zxb996.mp.handler.deduplication.service;

import cn.hutool.core.util.StrUtil;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 频次去重服务
 *
 * @author Gavin Zhang
 * @date 2023/2/12 14:54
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {

    @Autowired
    public FrequencyDeduplicationService(@Qualifier("simpleLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    private static final String PREFIX = "FRE";

    /**
     * 业务规则去重 构建key
     * <p>
     * key ： receiver + templateId + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     *
     * @param taskInfo 消息体
     * @param receiver 接收者
     * @return 对应Key
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}