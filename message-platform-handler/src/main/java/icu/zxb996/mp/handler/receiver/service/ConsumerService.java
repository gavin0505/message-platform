package icu.zxb996.mp.handler.receiver.service;

import icu.zxb996.mp.common.domain.TaskInfo;

import java.util.List;

/**
 * 消费者服务方法
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:43
 */
public interface ConsumerService {

    /**
     * 从MQ拉到消息进行消费，发送消息
     *
     * @param taskInfoLists 消息体
     */
    void consume2Send(List<TaskInfo> taskInfoLists);
}
