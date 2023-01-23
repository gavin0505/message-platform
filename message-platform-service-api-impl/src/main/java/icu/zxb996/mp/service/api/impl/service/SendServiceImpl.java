package icu.zxb996.mp.service.api.impl.service;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.domain.SendRequest;
import icu.zxb996.mp.service.api.impl.domain.SendTaskModel;
import icu.zxb996.mp.service.api.service.SendService;
import icu.zxb996.mp.support.pipeline.ProcessContext;
import icu.zxb996.mp.support.pipeline.ProcessController;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author Gavin Zhang
 * @date 2023/1/12 13:36
 */
@Service("sendServiceImpl")
public class SendServiceImpl implements SendService {

    @Resource(name = "processController")
    private ProcessController processController;

    @Override
    public ResultVO<String> send(SendRequest sendRequest) {

        // 组装发送消息信息任务模型
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(ResultVO.success("")).build();

        ProcessContext process = processController.process(context);
        return ResultVO.success(process.getResponse().getCode(), process.getResponse().getMessage());
    }
}
