package icu.zxb996.mp.handler.deduplication.build;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;

/**
 * 去重参数构造器接口
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:16
 */
public interface Builder {

    /**
     * 去重类型标识头，对应properties文件，例如：deduplication_10
     */
    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置文件信息构建去重参数类
     *
     * @param deduplication 去重配置文件信息
     * @param taskInfo      消息体
     * @return 去重参数
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}