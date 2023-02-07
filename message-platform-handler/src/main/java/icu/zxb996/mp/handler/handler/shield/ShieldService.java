package icu.zxb996.mp.handler.handler.shield;

import icu.zxb996.mp.common.domain.TaskInfo;

/**
 * 屏蔽消息服务接口
 *
 * @author Gavin Zhang
 * @date 2023/2/7 15:27
 */
public interface ShieldService {

    /**
     * 屏蔽消息
     *
     * @param taskInfo 消息体
     */
    void shield(TaskInfo taskInfo);
}
