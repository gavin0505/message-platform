package icu.zxb996.mp.handler.flowcontrol;

import icu.zxb996.mp.common.domain.TaskInfo;

/**
 * 流量控制服务
 *
 * @author Gavin Zhang
 * @date 2023/2/14 15:48
 */
public interface FlowControlService {

    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo         消息体
     * @param flowControlParam 流量控制参数
     * @return 耗费的时间
     */
    Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);
}