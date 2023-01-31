package icu.zxb996.mp.handler.receiver;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.receiver.service.ConsumerService;
import icu.zxb996.mp.handler.utils.GroupIdMappingUtils;
import icu.zxb996.mp.support.constants.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Kafka消费者实例
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:40
 */

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ConditionalOnProperty(name = "mp.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class Receiver {

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    /**
     * 发送消息
     *
     * @param consumerRecord kafka中的消息
     * @param topicGroupId   对应消费组的groupId
     */
    @KafkaListener(topics = "#{'${mp.business.topic.name}'}", containerFactory = "filterContainerFactory")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        // 拿到序列化的信息
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            // 反序列化并组装TaskInfo类 (List接收是由于fastJson的parseArray决定)
            List<TaskInfo> taskInfoLists = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            // 拿到messageTemplateId。因为用这个作为消费者groupId
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
             // 每个消费者组 只消费 他们自身关心的消息
            if (topicGroupId.equals(messageGroupId)) {
                // 使用了动态线程池
                consumerService.consume2Send(taskInfoLists);
            }
        }
    }
}
