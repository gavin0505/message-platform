package icu.zxb996.mp.service.api.service;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.domain.SendRequest;

/**
 * @author Gavin Zhang
 * @date 2023/1/11 10:36
 */
public interface SendService {

    /**
     * 消息发送
     *
     * @param sendRequest 消息体
     * @return 发送结果
     */
    ResultVO<String> send(SendRequest sendRequest);

}
