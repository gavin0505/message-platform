package icu.zxb996.mp.handler.handler.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import icu.zxb996.mp.common.constant.CommonConstant;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.dto.model.EmailContentModel;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.handler.enums.RateLimitStrategy;
import icu.zxb996.mp.handler.flowcontrol.FlowControlParam;
import icu.zxb996.mp.handler.handler.BaseHandler;
import icu.zxb996.mp.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gavin Zhang
 * @date 2023/2/3 11:22
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler {

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

        // 按照请求限流，默认单机 3 qps （具体数值配置在nacos动态调整)
        Double rateInitValue = Double.valueOf(3);
        flowControlParam = FlowControlParam.builder().rateInitValue(rateInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .rateLimiter(RateLimiter.create(rateInitValue)).build();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();

        // 获取发件邮箱配置
        MailAccount account = getAccountConfig();

        List<String> tos = new ArrayList<>(taskInfo.getReceiver());

        // 真正发送邮件
        MailUtil.send(account, tos, emailContentModel.getTitle(), emailContentModel.getContent(), true);

        return true;
    }

    /**
     * 获取邮箱账号配置
     *
     * @return 邮箱账号配置
     */
    private MailAccount getAccountConfig() {

        // 解析邮箱配置
        String config = configService.getProperty("emailAccount", CommonConstant.EMPTY_JSON_OBJECT);
        JSONObject object = JSONObject.parseObject(config);

        // 转换为固定account格式
        MailAccount account = JSONObject.toJavaObject(object, MailAccount.class);

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth()).setStarttlsEnable(account.isStarttlsEnable()).setSslEnable(account.isSslEnable()).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {
    }
}