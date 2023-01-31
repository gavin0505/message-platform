package icu.zxb996.mp.support.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author Gavin Zhang
 * @date 2023/1/15 13:45
 */
@Configuration
public class KafkaConfig {

    @Value("${mp.business.topic.name}")
    private String businessTopic;

    @Value("${mp.business.log.topic.name}")
    private String logTopic;

    @Bean
    public NewTopic businessTopic() {
        return TopicBuilder.name(businessTopic)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic logTopic() {
        return TopicBuilder.name(logTopic)
                .partitions(10)
                .replicas(1)
                .build();
    }
}
