package icu.zxb996.mp.collection.kafka;

import cn.hutool.core.date.DateUtil;
import com.dxy.library.json.gson.GsonUtil;
import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.support.constants.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送情况日志的收集
 *
 * <p>从Kafka消费日志</>
 *
 * @author Gavin Zhang
 * @date 2023/2/26 20:11
 */

@Component
@Slf4j
@ConditionalOnProperty(name = "mp.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class MessageHandler {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "#{'${mp.business.log.topic.name}'}", groupId = "group.#{'${mp.business.log.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> logMessage = Optional.ofNullable(consumerRecord.value());
        if (!logMessage.isPresent()) {
            return;
        }
        AnchorInfo anchorInfo = GsonUtil.from(logMessage.get(), AnchorInfo.class);

        /*
         * 1.构建userId维度的链路信息 数据结构list:{key,list}
         * key:userId,listValue:[{timestamp,state,businessId},{timestamp,state,businessId}]
         * expire: 到次日0时
         *
         */
        for (String id : anchorInfo.getIds()) {
            redisTemplate.opsForList().leftPush(id, GsonUtil.to(anchorInfo));
            redisTemplate.expire(id, (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000, TimeUnit.SECONDS);
        }

        /*
         * 2.构建消息模板维度的链路信息 数据结构hash:{key,hash}
         * key:businessId,hashValue:{state,stateCount}
         * expire: 30天
         */
        redisTemplate.opsForHash().increment(String.valueOf(anchorInfo.getBusinessId()), String.valueOf(anchorInfo.getState()), anchorInfo.getIds().size());
        redisTemplate.expire(String.valueOf(anchorInfo.getBusinessId()), ((DateUtil.offsetDay(new Date(), 30).getTime()) / 1000) - DateUtil.currentSeconds(), TimeUnit.SECONDS);

    }
}