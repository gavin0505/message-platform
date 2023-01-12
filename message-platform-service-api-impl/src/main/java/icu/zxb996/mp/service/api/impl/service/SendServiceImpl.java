package icu.zxb996.mp.service.api.impl.service;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.domain.SendRequest;
import icu.zxb996.mp.service.api.service.SendService;
import org.springframework.stereotype.Service;

/**
 * @author Gavin Zhang
 * @date 2023/1/12 13:36
 */
@Service("sendServiceImpl")
public class SendServiceImpl implements SendService {


    @Override
    public ResultVO<String> send(SendRequest sendRequest) {

        return null;
    }
}
