package icu.zxb996.mp.service.api.impl.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import icu.zxb996.mp.common.constant.MpConstant;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.dto.model.ContentModel;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.common.enums.RespStatusEnum;
import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.domain.MessageParam;
import icu.zxb996.mp.service.api.enums.BusinessCode;
import icu.zxb996.mp.service.api.impl.domain.SendTaskModel;
import icu.zxb996.mp.support.dao.MessageTemplateMapper;
import icu.zxb996.mp.support.domain.MessageTemplate;
import icu.zxb996.mp.support.pipeline.BusinessProcess;
import icu.zxb996.mp.support.pipeline.ProcessContext;
import icu.zxb996.mp.support.utils.ContentHolderUtil;
import icu.zxb996.mp.support.utils.TaskInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 组装参数
 *
 * @author Gavin Zhang
 * @date 2023/1/13 14:08
 */
@Slf4j
@Service("assembleAction")
public class AssembleAction implements BusinessProcess<SendTaskModel> {

    @Resource(name = "messageTemplateMapper")
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {

        // 获取责任链上下文数据模型
        SendTaskModel sendTaskModel = context.getProcessModel();

        // 获取数据模型的消息模板id
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();

        try {
            // 查询消息模板
            Optional<MessageTemplate> messageTemplate = Optional.ofNullable(messageTemplateMapper.selectById(messageTemplateId));

            // 若查询为空或模板被删除，中断责任链
            if (messageTemplate.isEmpty() || messageTemplate.get().getIfDeleted().equals(MpConstant.TRUE)) {
                context.setNeedBreak(true).setResponse(ResultVO.error(RespStatusEnum.TEMPLATE_NOT_FOUND.getCode(), RespStatusEnum.TEMPLATE_NOT_FOUND.getMsg()));
                return;
            }
            // 若为普通发送
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                // 组装并设置任务信息到模型中
                List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate.get());
                sendTaskModel.setTaskInfo(taskInfos);

                // 若为撤回消息
            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                // 设置消息模板类型到模型中
                sendTaskModel.setMessageTemplate(messageTemplate.get());
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(ResultVO.error(RespStatusEnum.SERVICE_ERROR.getCode(), RespStatusEnum.SERVICE_ERROR.getMsg()));
            log.error("assemble task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 组装 TaskInfo 任务消息
     *
     * @param sendTaskModel   责任链上下文数据模型
     * @param messageTemplate 消息模板
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {

            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .shieldType(messageTemplate.getShieldType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;
    }

    /**
     * 获取 contentModel，替换模板msgContent中占位符信息
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {

        // 得到真正的ContentModel 类型
        Integer sendChannel = messageTemplate.getSendChannel();
        // 反射拿到实际的ContentModel类
        Class<?> contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);


        // 得到模板的 msgContent 和 入参
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());


        // 通过反射 组装出 contentModel
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtil.replacePlaceHolder(originValue, variables);
                Object resultObj = JSONUtil.isJsonObj(resultValue) ? JSONUtil.toBean(resultValue, field.getType()) : resultValue;
                ReflectUtil.setFieldValue(contentModel, field, resultObj);
            }
        }

        // 如果 url 字段存在，则在url拼接对应的埋点参数
        String url = (String) ReflectUtil.getFieldValue(contentModel, "url");
        if (StrUtil.isNotBlank(url)) {
            String resultUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());
            ReflectUtil.setFieldValue(contentModel, "url", resultUrl);
        }
        return contentModel;
    }

}
