package icu.zxb996.mp.service.api.domain;

import java.util.Map;

/**
 * @author Gavin Zhang
 * @date 2023/1/12 13:53
 */
public class MessageParam {

    /**
     * @description 接收者
     * 多个用,逗号号分隔开
     * 【不能大于100个】
     * 必传
     */
    private String receiver;

    /**
     * @description 消息内容中的可变部分(占位符替换)
     * 可选
     */
    private Map<String, String> variables;

    /**
     * @description 扩展参数
     * 可选
     */
    private Map<String, String> extra;
}
