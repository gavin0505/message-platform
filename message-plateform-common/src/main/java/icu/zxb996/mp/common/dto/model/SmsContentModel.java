package icu.zxb996.mp.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信内容模型
 * 在前端填写的时候分开，但最后处理的时候会将url拼接在content上
 *
 * @author Gavin Zhang
 * @date 2023/1/13 17:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsContentModel extends ContentModel {

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送链接
     */
    private String url;
}
