package icu.zxb996.mp.handler.handler;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.support.domain.MessageTemplate;

/**
 * 消息处理器
 *
 * @author Gavin Zhang
 * @date 2023/2/1 16:25
 */
public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo 消息体
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param messageTemplate 消息模板
     */
    void recall(MessageTemplate messageTemplate);

}
