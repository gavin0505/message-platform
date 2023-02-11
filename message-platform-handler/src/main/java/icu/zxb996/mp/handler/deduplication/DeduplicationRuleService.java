package icu.zxb996.mp.handler.deduplication;

import icu.zxb996.mp.common.constant.CommonConstant;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.support.service.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 去重服务
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:13
 */
@Service
public class DeduplicationRuleService {

    public static final String DEDUPLICATION_RULE_KEY = "deduplicationRule";

    @Resource(name = "configServiceImpl")
    private ConfigService config;

    @Resource
    private DeduplicationHolder deduplicationHolder;

    public void duplication(TaskInfo taskInfo) {
        /*
         * 获取配置样例
         *
         * 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
         */
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, CommonConstant.EMPTY_JSON_OBJECT);

        // 去重
        // 1. 获取去重列表
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();

        for (Integer deduplicationType : deduplicationList) {
            // 2. 通过配置样例和消息体的信息，构建去重参数
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType).build(deduplicationConfig, taskInfo);
            // 3. 若去重参数不为空，执行去重
            if (Objects.nonNull(deduplicationParam)) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }
}