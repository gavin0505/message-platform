package icu.zxb996.mp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Gavin Zhang
 * @date 2023/1/13 13:01
 */
@Getter
@ToString
@AllArgsConstructor
public enum IdType {
    /**
     * USER_ID
     */
    USER_ID(10, "userId"),
    DID(20, "did"),
    PHONE(30, "phone"),
    OPEN_ID(40, "openId"),
    EMAIL(50, "email"),
    ENTERPRISE_USER_ID(60, "enterprise_user_id"),
    DING_DING_USER_ID(70, "ding_ding_user_id"),
    CID(80, "cid"),
    FEI_SHU_USER_ID(90, "fei_shu_user_id"),
    ;

    private final Integer code;
    private final String description;


}
