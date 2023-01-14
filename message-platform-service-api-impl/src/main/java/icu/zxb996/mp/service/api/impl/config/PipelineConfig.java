package icu.zxb996.mp.service.api.impl.config;

import icu.zxb996.mp.service.api.enums.BusinessCode;
import icu.zxb996.mp.service.api.impl.action.AfterParamCheckAction;
import icu.zxb996.mp.service.api.impl.action.AssembleAction;
import icu.zxb996.mp.service.api.impl.action.PreParamCheckAction;
import icu.zxb996.mp.service.api.impl.action.SendMqAction;
import icu.zxb996.mp.support.pipeline.ProcessController;
import icu.zxb996.mp.support.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 责任链流程控制装配
 *
 * @author Gavin Zhang
 * @date 2023/1/13 12:58
 */
@Configuration
public class PipelineConfig {

    @Resource(name = "preParamCheckAction")
    private PreParamCheckAction preParamCheckAction;
    @Resource(name = "assembleAction")
    private AssembleAction assembleAction;
    @Resource(name = "afterParamCheckAction")
    private AfterParamCheckAction afterParamCheckAction;
    @Resource(name = "sendMqAction")
    private SendMqAction sendMqAction;

    /**
     * 普通发送执行流程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息至MQ
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction,
                afterParamCheckAction, sendMqAction));
        return processTemplate;
    }

    /**
     * 消息撤回执行流程
     * 1.组装参数
     * 2.发送MQ
     */
    @Bean("recallMessageTemplate")
    public ProcessTemplate recallMessageTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(assembleAction, sendMqAction));
        return processTemplate;
    }


    /**
     * pipeline流程控制器
     * 后续扩展则加BusinessCode和ProcessTemplate
     */
    @Bean("processController")
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        templateConfig.put(BusinessCode.RECALL.getCode(), recallMessageTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }
}
