package icu.zxb996.mp.web.controller;

import icu.zxb996.mp.common.vo.ResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gavin Zhang
 * @date 2023/1/11 10:32
 */

@RestController
@Slf4j
@Api(tags = {"发送消息接口"})
public class SendController {

    @PostMapping("/send")
    public ResultVO<String> send() {
        return null;
    }
}
