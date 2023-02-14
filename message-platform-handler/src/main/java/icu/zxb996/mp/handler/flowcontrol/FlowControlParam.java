package icu.zxb996.mp.handler.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import icu.zxb996.mp.handler.enums.RateLimitStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流量控制所需参数
 *
 * @author Gavin Zhang
 * @date 2023/2/14 15:43
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {


    /**
     * 限流器
     * 子类初始化的时候指定
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器初始限流大小
     * 子类初始化的时候指定
     */
    protected Double rateInitValue;

    /**
     * 限流的策略
     * 子类初始化的时候指定
     */
    protected RateLimitStrategy rateLimitStrategy;
}