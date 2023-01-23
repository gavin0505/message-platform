package icu.zxb996.mp.web.controller;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.service.api.domain.SendRequest;
import icu.zxb996.mp.service.api.service.SendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Gavin Zhang
 * @date 2023/1/11 10:32
 */

@RestController
@Slf4j
@Api(tags = {"发送消息接口"})
public class SendController {

    @Resource(name = "sendServiceImpl")
    private SendService sendService;

    @PostMapping("/send")
    @ApiOperation("发送消息")
    public ResultVO<String> send(@RequestBody SendRequest sendRequest) {
        return sendService.send(sendRequest);
    }
}
