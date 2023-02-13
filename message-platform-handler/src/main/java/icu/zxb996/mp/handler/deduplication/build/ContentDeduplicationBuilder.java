package icu.zxb996.mp.handler.deduplication.build;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 消息内容去重参数构造器
 *
 * @author Gavin Zhang
 * @date 2023/2/11 15:13
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        // 没有去重需求，则返回null
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        // 设置去重标识
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;
    }
}