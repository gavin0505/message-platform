package icu.zxb996.mp.web.controller;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.support.domain.MessageTemplate;
import icu.zxb996.mp.web.service.MsgModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息模板服务接口
 *
 * @author Gavin Zhang
 * @date 2023/2/25 16:20
 */

@RestController
@RequestMapping("/msgModel")
@Slf4j
public class MsgModelController {

    @Resource(name = "msgModelServiceImpl")
    private MsgModelService msgModelService;

    @PostMapping("/addNewModel")
    public ResultVO<String> addNewModel(@RequestBody MessageTemplate messageTemplate) {
        return msgModelService.addNewModel(messageTemplate);
    }

}