package icu.zxb996.mp.handler.handler;

import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.handler.flowcontrol.FlowControlFactory;
import icu.zxb996.mp.handler.flowcontrol.FlowControlParam;
import icu.zxb996.mp.support.utils.LogUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * 发送各个渠道的handler
 *
 * @author Gavin Zhang
 * @date 2023/2/1 16:26
 */
public abstract class BaseHandler implements Handler {

    @Resource
    private HandlerHolder handlerHolder;
    @Resource
    private LogUtils logUtils;

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;

    /**
     * 限流控制工厂
     */
    @Resource(name = "flowControlFactory")
    private FlowControlFactory flowControlFactory;

    /**
     * 限流相关的参数
     * 子类初始化的时候指定
     */
    protected FlowControlParam flowControlParam;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    /**
     * 流量控制
     *
     * @param taskInfo 消息体
     */
    public void flowControl(TaskInfo taskInfo) {
        // 只有子类指定了限流参数，才需要限流
        if (Objects.nonNull(flowControlParam)) {
            flowControlFactory.flowControl(taskInfo, flowControlParam);
        }
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {

        // 限流控制
        flowControl(taskInfo);
        // 通过handler接口发送信息
        if (handler(taskInfo)) {
            logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            return;
        }
        logUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo 消息体
     * @return true-消息发送成功；false-消息发送失败
     */
    public abstract boolean handler(TaskInfo taskInfo);
}