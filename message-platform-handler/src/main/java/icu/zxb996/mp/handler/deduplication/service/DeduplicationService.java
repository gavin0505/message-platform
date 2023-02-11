package icu.zxb996.mp.handler.deduplication.service;

import icu.zxb996.mp.handler.deduplication.DeduplicationParam;

/**
 * 去重服务接口
 *
 * @author Gavin Zhang
 * @date 2023/2/7 16:18
 */
public interface DeduplicationService {

    /**
     * 去重
     *
     * @param param 去重参数
     */
    void deduplication(DeduplicationParam param);
}
