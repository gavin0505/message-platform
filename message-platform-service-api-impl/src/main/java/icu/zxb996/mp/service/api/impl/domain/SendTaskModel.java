package icu.zxb996.mp.service.api.impl.domain;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.service.api.domain.MessageParam;
import icu.zxb996.mp.support.domain.MessageTemplate;
import icu.zxb996.mp.support.pipeline.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送消息信息任务模型
 *
 * @author Gavin Zhang
 * @date 2023/1/12 13:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel{


    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 发送任务的信息
     */
    private List<TaskInfo> taskInfo;

    /**
     * 撤回任务的信息
     */
    private MessageTemplate messageTemplate;
}
