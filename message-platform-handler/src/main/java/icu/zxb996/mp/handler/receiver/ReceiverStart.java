package icu.zxb996.mp.handler.receiver;

import icu.zxb996.mp.handler.utils.GroupIdMappingUtils;
import icu.zxb996.mp.support.constants.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Kafka消费者示例初始化器
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:40
 */
@Service
@ConditionalOnProperty(name = "mp.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
@Slf4j
public class ReceiverStart {

    @Resource
    private ApplicationContext context;
    @Resource
    private ConsumerFactory<String, String> consumerFactory;

    /**
     * receiver的消费方法常量
     */
    private static final String RECEIVER_METHOD_NAME = "Receiver.consumer";

    /**
     * 获取得到所有的groupId
     */
    private static final List<String> GROUP_ID = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 下标(用于迭代groupIds位置)
     */
    private static Integer index = 0;

    /**
     * 为每个渠道不同的消息类型 创建一个Receiver对象
     * 时机：ApplicationContext注入之后，groupIdEnhancer()执行之前--PostConstruct在static之前执行
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < GROUP_ID.size(); i++) {
            context.getBean(Receiver.class);
        }
    }

    /**
     * 给每个Receiver对象的consumer方法 @KafkaListener赋值相应的groupId
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                // 如果拿到的是Receiver类的consumer方法
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    // 将@KafkaListener注解中groupId属性设置为对应Consumer Group名字
                    attrs.put("groupId", GROUP_ID.get(index++));
                }
            }
            return attrs;
        };
    }

    /**
     * 针对tag消息过滤
     * producer 将tag写进header里
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> filterContainerFactory(@Value("${austin.business.tagId.key}") String tagIdKey,
                                                                                          @Value("${austin.business.tagId.value}") String tagIdValue) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setAckDiscarded(true);

        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Optional.ofNullable(consumerRecord.value()).isPresent()) {
                for (Header header : consumerRecord.headers()) {
                    if (header.key().equals(tagIdKey) && new String(header.value()).equals(new String(tagIdValue.getBytes(StandardCharsets.UTF_8)))) {
                        return false;
                    }
                }
            }
            //返回true将会被丢弃
            return true;
        });
        return factory;
    }

}
