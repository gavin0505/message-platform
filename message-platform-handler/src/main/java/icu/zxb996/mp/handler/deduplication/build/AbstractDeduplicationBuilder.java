package icu.zxb996.mp.handler.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationHolder;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * 抽象去重构造器模板
 *
 * @author Gavin Zhang
 * @date 2023/2/7 17:39
 */
public abstract class AbstractDeduplicationBuilder implements Builder {

    protected Integer deduplicationType;

    @Resource(name = "deduplicationHolder")
    private DeduplicationHolder deduplicationHolder;

    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig, TaskInfo taskInfo) {
        JSONObject object = JSONObject.parseObject(duplicationConfig);
        if (Objects.isNull(object)) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setTaskInfo(taskInfo);
        return deduplicationParam;
    }
}
