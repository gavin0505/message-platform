package icu.zxb996.mp.handler.deduplication;

import com.alibaba.fastjson.annotation.JSONField;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 去重服务所需要的参数
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:16
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeduplicationParam {

    /**
     * TaskInfo消息体
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    @JSONField(name = "time")
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    @JSONField(name = "num")
    private Integer countNum;

    /**
     * 标识属于哪种去重(数据埋点)
     */
    private AnchorState anchorState;
}