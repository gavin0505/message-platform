package icu.zxb996.mp.handler.flowcontrol.annotations;

import icu.zxb996.mp.handler.enums.RateLimitStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 单机限流注解
 *
 * @author Gavin Zhang
 * @date 2023/2/14 15:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LocalRateLimit {

    RateLimitStrategy rateLimitStrategy() default RateLimitStrategy.REQUEST_RATE_LIMIT;
}