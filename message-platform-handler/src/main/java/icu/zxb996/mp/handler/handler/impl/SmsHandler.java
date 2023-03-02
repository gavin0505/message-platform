package icu.zxb996.mp.handler.handler.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import icu.zxb996.mp.common.constant.CommonConstant;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.dto.account.sms.TencentSmsAccount;
import icu.zxb996.mp.common.dto.model.SmsContentModel;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.common.enums.SmsStatus;
import icu.zxb996.mp.handler.domain.sms.SmsParam;
import icu.zxb996.mp.handler.handler.BaseHandler;
import icu.zxb996.mp.support.domain.MessageTemplate;
import icu.zxb996.mp.support.domain.SmsRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 短信发送
 *
 * @author Gavin Zhang
 * @date 2023/3/1 19:46
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler {

    private static final Integer PHONE_NUM = 11;

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .build();


        List<SmsRecord> recordList = send(smsParam);

        return false;
    }

    /**
     * 发送短信
     *
     * @param smsParam 短信参数模板
     * @return 短信回执和记录
     */
    private List<SmsRecord> send(SmsParam smsParam) {
        try {

            // 获取短信账号参数
            TencentSmsAccount tencentSmsAccount = getSmsConfig();

            // 初始化短信客户端
            SmsClient client = init(tencentSmsAccount);

            // 组装短信发送参数，构造请求
            SendSmsRequest request = assembleSendReq(smsParam, tencentSmsAccount);

            // 执行发送，获取回执信息
            SendSmsResponse response = client.SendSms(request);

            return assembleSendSmsRecord(smsParam, response, tencentSmsAccount);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }

    /**
     * 组装发送短信参数
     */
    private SendSmsRequest assembleSendReq(SmsParam smsParam, TencentSmsAccount account) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(account.getSmsSdkAppId());
        req.setSignName(account.getSignName());
        req.setTemplateId(account.getTemplateId());
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 组装发送短信的返回值
     *
     * @param smsParam          短信参数模板
     * @param response          短信发送的返回值
     * @param tencentSmsAccount 腾讯云短信账号参数
     * @return 组装好的短信返回值
     */
    private List<SmsRecord> assembleSendSmsRecord(SmsParam smsParam, SendSmsResponse response, TencentSmsAccount tencentSmsAccount) {
        if (Objects.isNull(response) || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();
        for (SendStatus sendStatus : response.getSendStatusSet()) {

            // 腾讯返回的电话号有前缀，这里取巧直接翻转获取手机号
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();

            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(DateUtil.date())
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsAccount.getSupplierId())
                    .supplierName(tencentSmsAccount.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .build();

            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }

    /**
     * 初始化 client
     *
     * @param account 腾讯云短信账号参数
     */
    private SmsClient init(TencentSmsAccount account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getRegion(), clientProfile);
    }

    /**
     * 如果有输入链接，则把链接拼在文案后
     * <p>
     * PS: 这里可以考虑将链接 转 短链
     * PS: 如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + StrUtil.SPACE + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }

    /**
     * 获取短信配置
     *
     * @return 短信配置
     */
    private TencentSmsAccount getSmsConfig() {
        String config = configService.getProperty("smsAccount", CommonConstant.EMPTY_JSON_OBJECT);
        JSONObject object = JSONObject.parseObject(config);

        return JSONObject.toJavaObject(object, TencentSmsAccount.class);
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {
    }
}