package icu.zxb996.mp.handler.utils;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.ChannelType;
import icu.zxb996.mp.common.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * GroupId映射器
 * groupId 标识着每一个消费者组
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:48
 */
public class GroupIdMappingUtils {

    /**
     * 获取所有的groupIds
     * (不同的渠道不同的消息类型拥有自己的groupId)
     * 命名示例：email.notice, 代表：渠道为email的通知类消息
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }


    /**
     * 根据TaskInfo获取当前消息的groupId
     *
     * @param taskInfo 消息体
     * @return 当前消息的groupId
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = ChannelType.getEnumByCode(taskInfo.getSendChannel()).getCodeEn();
        String msgCodeEn = MessageType.getEnumByCode(taskInfo.getMsgType()).getCodeEn();
        return channelCodeEn + "." + msgCodeEn;
    }
}
