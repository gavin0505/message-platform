package icu.zxb996.mp.handler.handler;

import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.support.utils.LogUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
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
