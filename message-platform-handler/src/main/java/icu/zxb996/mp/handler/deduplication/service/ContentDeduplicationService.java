package icu.zxb996.mp.handler.deduplication.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.DeduplicationType;
import icu.zxb996.mp.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 内容去重服务（默认5分钟相同的文案发给相同的用户去重）
 *
 * @author Gavin Zhang
 * @date 2023/2/11 18:52
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {

    @Autowired
    public ContentDeduplicationService(@Qualifier("slideWindowLimitService") LimitService limitService) {
        this.limitService = limitService;
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    /**
     * 内容去重 构建key
     * <p>
     * key: md5(templateId + receiver + content)
     * <p>
     * 相同的内容相同的模板短时间内发给同一个人
     *
     * @param taskInfo 消息体
     * @return 对应key
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}