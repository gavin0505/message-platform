package icu.zxb996.mp.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 限流策略枚举
 *
 * @author Gavin Zhang
 * @date 2023/2/14 15:44
 */

@Getter
@ToString
@AllArgsConstructor
public enum RateLimitStrategy {

    /**
     * 根据真实请求数限流 (实际意义上的QPS）
     */
    REQUEST_RATE_LIMIT(10, "根据真实请求数限流"),

    /**
     * 根据发送用户数限流（人数限流）
     */
    SEND_USER_NUM_RATE_LIMIT(20, "根据发送用户数限流"),
    ;

    private final Integer code;
    private final String description;
}