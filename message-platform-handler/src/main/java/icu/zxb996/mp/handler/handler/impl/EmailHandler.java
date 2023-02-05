package icu.zxb996.mp.handler.handler.impl;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.dto.model.EmailContentModel;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.handler.handler.BaseHandler;
import icu.zxb996.mp.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            MimeMessage mimeMessage = getMimeMessage(taskInfo);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("邮件发送失败");
            return false;
        }
    }

    private MimeMessage getMimeMessage(TaskInfo taskInfo) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(taskInfo.getReceiver().stream().toList().stream().toArray(String[]::new));
        mimeMessageHelper.setSubject(emailContentModel.getTitle());
        mimeMessageHelper.setText(emailContentModel.getContent());
        return mimeMessage;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {
    }
}