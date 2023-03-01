package icu.zxb996.mp.web.service.impl;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.support.dao.MessageTemplateMapper;
import icu.zxb996.mp.support.domain.MessageTemplate;
import icu.zxb996.mp.web.service.MsgModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息模板服务类
 *
 * @author Gavin Zhang
 * @date 2023/2/25 21:09
 */

@Service
public class MsgModelServiceImpl implements MsgModelService {

    @Resource
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public ResultVO<String> addNewModel(MessageTemplate messageTemplate) {

        int insert = messageTemplateMapper.insert(messageTemplate);
        if (insert > 0) {
            return ResultVO.success("模板保存成功");
        }
        return ResultVO.error("模板保存失败");
    }

    public ResultVO<String> getModel() {
        return null;
    }
}