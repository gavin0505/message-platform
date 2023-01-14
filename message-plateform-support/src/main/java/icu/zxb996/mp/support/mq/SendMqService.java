package icu.zxb996.mp.support.mq;

/**
 * 发送消息到MQ
 *
 * @author Gavin Zhang
 * @date 2023/1/14 16:49
 */
public interface SendMqService {

    /**
     * 发送消息
     *
     * @param topic     指定topic
     * @param jsonValue JSON序列化后的消息
     * @param tagId     消费者组标识id
     */
    void send(String topic, String jsonValue, String tagId);


    /**
     * 发送消息
     *
     * @param topic     指定topic
     * @param jsonValue JSON序列化后的消息
     */
    void send(String topic, String jsonValue);
}
