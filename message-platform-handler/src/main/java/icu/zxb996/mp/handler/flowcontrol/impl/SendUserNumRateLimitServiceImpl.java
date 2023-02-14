package icu.zxb996.mp.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.enums.RateLimitStrategy;
import icu.zxb996.mp.handler.flowcontrol.FlowControlParam;
import icu.zxb996.mp.handler.flowcontrol.FlowControlService;
import icu.zxb996.mp.handler.flowcontrol.annotations.LocalRateLimit;

/**
 * 根据发送用户数限流
 *
 * @author Gavin Zhang
 * @date 2023/2/14 20:07
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumRateLimitServiceImpl implements FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo         消息体
     * @param flowControlParam 限流控制参数
     */
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(taskInfo.getReceiver().size());
    }
}