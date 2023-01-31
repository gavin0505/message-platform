package icu.zxb996.mp.handler.pending;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 消息发送任务
 *
 * @author Gavin Zhang
 * @date 2023/1/31 18:06
 */

@Data
@Accessors(chain = true)
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {
    @Override
    public void run() {

        // 1. 丢弃消息
        // 2. 屏蔽消息
        // 3. 平台通用去重
        // 4. 发送消息
    }
}
