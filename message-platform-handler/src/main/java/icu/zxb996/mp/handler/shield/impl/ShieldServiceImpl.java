package icu.zxb996.mp.handler.shield.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.common.enums.ShieldType;
import icu.zxb996.mp.handler.shield.ShieldService;
import icu.zxb996.mp.support.utils.LogUtils;
import icu.zxb996.mp.support.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

/**
 * 屏蔽消息服务实现类
 *
 * @author Gavin Zhang
 * @date 2023/2/7 15:28
 */
@Service("shieldServiceImpl")
public class ShieldServiceImpl implements ShieldService {

    /**
     * 夜间屏蔽key
     */
    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private LogUtils logUtils;

    @Override
    public void shield(TaskInfo taskInfo) {
        if (ShieldType.NIGHT_NO_SHIELD.getCode().equals(taskInfo.getShieldType())) {
            return;
        }

        /*
         * example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送
         * (配合 分布式任务定时任务框架搞掂)
         */
        if (isNight()) {
            if (ShieldType.NIGHT_SHIELD.getCode().equals(taskInfo.getShieldType())) {
                logUtils.print(AnchorInfo.builder().state(AnchorState.NIGHT_SHIELD.getCode())
                        .businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            }
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getShieldType())) {
                redisUtils.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY, JSON.toJSONString(taskInfo,
                                SerializerFeature.WriteClassName),
                        (DateUtil.offsetDay(new Date(), 1).getTime() / 1000) - DateUtil.currentSeconds());
                logUtils.print(AnchorInfo.builder().state(AnchorState.NIGHT_SHIELD_NEXT_SEND.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
            }
            taskInfo.setReceiver(new HashSet<>());
        }
    }

    /**
     * 小时 < 8 默认就认为是凌晨(夜晚)
     *
     * @return true-夜晚；false-白天
     */
    private boolean isNight() {
        return LocalDateTime.now().getHour() < 8;
    }

}
