package icu.zxb996.mp.handler.handler.impl;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.handler.handler.BaseHandler;
import icu.zxb996.mp.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Gavin Zhang
 * @date 2023/2/3 11:22
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler {

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Resource
    private JavaMailSender javaMailSender;

    //发送人
    private String from = "gavin233@qq.com";
    //接收人
    private String to = "zxb_worky@163.com";
    //标题
    private String subject = "测试邮件";
    //正文
    private String context = "测试邮件正文内容";

    @Override
    public boolean handler(TaskInfo taskInfo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from + "(zxb)");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(context);
        javaMailSender.send(message);
        return true;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}
