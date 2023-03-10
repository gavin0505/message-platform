package icu.zxb996.mp.handler.pending;

import cn.hutool.core.collection.CollUtil;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationRuleService;
import icu.zxb996.mp.handler.discard.DiscardMessageService;
import icu.zxb996.mp.handler.handler.HandlerHolder;
import icu.zxb996.mp.handler.shield.ShieldService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Resource(name = "discardMessageService")
    private DiscardMessageService discardMessageService;

    @Resource(name = "shieldServiceImpl")
    private ShieldService shieldService;

    @Resource
    private DeduplicationRuleService deduplicationRuleService;


    @Resource
    private HandlerHolder handlerHolder;
    private TaskInfo taskInfo;

    @Override
    public void run() {

        // 1. 丢弃消息
        if (discardMessageService.isDiscard(taskInfo)) {
            return;
        }

        // 2. 屏蔽消息
        shieldService.shield(taskInfo);

        // 3.平台通用去重
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            deduplicationRuleService.duplication(taskInfo);
        }

        // 4. 发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
        }
    }
}