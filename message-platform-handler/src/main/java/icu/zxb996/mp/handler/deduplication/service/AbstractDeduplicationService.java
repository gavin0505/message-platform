package icu.zxb996.mp.handler.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationHolder;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;
import icu.zxb996.mp.handler.deduplication.limit.LimitService;
import icu.zxb996.mp.support.utils.LogUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

/**
 * 去重服务模板方法
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:19
 */
public abstract class AbstractDeduplicationService implements DeduplicationService {

    protected Integer deduplicationType;

    protected LimitService limitService;

    @Resource(name = "deduplicationHolder")
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    private void init() {
        deduplicationHolder.putService(deduplicationType, this);
    }

    @Resource
    private LogUtils logUtils;

    @Override
    public void deduplication(DeduplicationParam param) {

        TaskInfo taskInfo = param.getTaskInfo();

        Set<String> filterReceiver = limitService.limitFilter(this, taskInfo, param);

        // 剔除符合去重条件的用户
        if (CollUtil.isNotEmpty(filterReceiver)) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(filterReceiver).state(param.getAnchorState().getCode()).build());
        }
    }

    /**
     * 构建去重的Key
     *
     * @param taskInfo 消息体
     * @param receiver 消息接收者
     * @return 对应去重Key
     */
    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);
}