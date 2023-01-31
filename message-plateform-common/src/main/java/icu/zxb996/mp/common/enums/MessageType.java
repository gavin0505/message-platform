package icu.zxb996.mp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送消息类型枚举
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:50
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType {

    /**
     *
     */
    NOTICE(10, "通知类消息", "notice"),
    MARKETING(20, "营销类消息", "marketing"),
    AUTH_CODE(30, "验证码消息", "auth_code");

    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;


    /**
     * 英文标识
     */
    private final String codeEn;


    /**
     * 通过code获取enum
     *
     * @param code 状态码
     * @return 对应枚举
     */
    public static MessageType getEnumByCode(Integer code) {
        MessageType[] values = values();
        for (MessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
