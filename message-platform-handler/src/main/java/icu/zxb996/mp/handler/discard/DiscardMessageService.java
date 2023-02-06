package icu.zxb996.mp.handler.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import icu.zxb996.mp.common.constant.CommonConstant;
import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.support.service.ConfigService;
import icu.zxb996.mp.support.utils.LogUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 丢弃消息服务
 *
 * @author Gavin Zhang
 * @date 2023/2/6 15:21
 */
@Service("discardMessageService")
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discardMsgIds";

    @Resource(name = "configServiceImpl")
    private ConfigService config;

    @Resource
    private LogUtils logUtils;

    /**
     * 丢弃消息，配置在Nacos配置中心
     *
     * @param taskInfo 消息体
     * @return true-丢弃；false-不丢弃
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        // 配置示例:	["1","2"]
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY, CommonConstant.EMPTY_VALUE_JSON_ARRAY));

        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(AnchorState.DISCARD.getCode()).build());
            return true;
        }
        return false;
    }
}