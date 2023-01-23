package icu.zxb996.mp.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import icu.zxb996.mp.common.enums.RespStatusEnum;
import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.enums.BusinessCode;
import icu.zxb996.mp.service.api.impl.domain.SendTaskModel;
import icu.zxb996.mp.support.mq.SendMqService;
import icu.zxb996.mp.support.pipeline.BusinessProcess;
import icu.zxb996.mp.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 将消息发送到MQ
 *
 * @author Gavin Zhang
 * @date 2023/1/13 15:23
 */
@Slf4j
@Service("sendMqAction")
public class SendMqAction implements BusinessProcess<SendTaskModel> {

    @Resource(name = "kafkaSendMqServiceImpl")
    private SendMqService sendMqService;

    @Value("${mp.business.topic.name}")
    private String sendMessageTopic;

    @Value("${mp.business.recall.topic.name}")
    private String mpRecall;
    @Value("${mp.business.tagId.value}")
    private String tagId;

    @Value("${mp.mq.pipeline}")
    private String mqPipeline;


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        try {
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                // 序列化信息，读的时候再反序列化（丢进TaskInfo）
                String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), new SerializerFeature[]{SerializerFeature.WriteClassName});
                sendMqService.send(sendMessageTopic, message, tagId);
            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getMessageTemplate(), new SerializerFeature[]{SerializerFeature.WriteClassName});
                sendMqService.send(mpRecall, message, tagId);
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(ResultVO.error(RespStatusEnum.SERVICE_ERROR.getCode(), RespStatusEnum.SERVICE_ERROR.getMsg()));
            log.error("send {} fail! e:{},params:{}", mqPipeline, Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
        }
    }
}
