package icu.zxb996.mp.handler.deduplication.service;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 频次去重服务
 *
 * @author Gavin Zhang
 * @date 2023/2/12 14:54
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {

    @Autowired
    public FrequencyDeduplicationService(@Qualifier("simpleLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return null;
    }
}