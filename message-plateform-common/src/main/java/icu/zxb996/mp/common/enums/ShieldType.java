package icu.zxb996.mp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 屏蔽类型枚举
 *
 * @author Gavin Zhang
 * @date 2023/2/7 15:34
 */

@Getter
@ToString
@AllArgsConstructor
public enum ShieldType {

    /**
     * 屏蔽类型
     */
    NIGHT_NO_SHIELD(10, "夜间不屏蔽"),
    NIGHT_SHIELD(20, "夜间屏蔽"),
    NIGHT_SHIELD_BUT_NEXT_DAY_SEND(30, "夜间屏蔽(次日早上9点发送)");

    private final Integer code;
    private final String description;
}