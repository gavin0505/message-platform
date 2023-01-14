package icu.zxb996.mp.service.api.impl.action;

import icu.zxb996.mp.service.api.impl.domain.SendTaskModel;
import icu.zxb996.mp.support.pipeline.BusinessProcess;
import icu.zxb996.mp.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 将消息发送到MQ
 *
 * @author Gavin Zhang
 * @date 2023/1/13 15:23
 */
@Slf4j
@Service("sendMqAction")
public class SendMqAction implements BusinessProcess<SendTaskModel> {

    @Override
    public void process(ProcessContext<SendTaskModel> context) {

    }
}
