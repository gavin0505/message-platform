package icu.zxb996.mp.handler.deduplication.build;

import cn.hutool.core.date.DateUtil;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 消息频率去重构造器
 *
 * @author Gavin Zhang
 * @date 2023/2/11 15:22
 */
@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder {

    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        // 没有去重需求，则返回null
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }

        // 设置去重时间和去重标识
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}