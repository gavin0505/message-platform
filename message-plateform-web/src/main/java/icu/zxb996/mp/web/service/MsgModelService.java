package icu.zxb996.mp.web.service;

import icu.zxb996.mp.common.vo.ResultVO;
import icu.zxb996.mp.support.domain.MessageTemplate;

/**
 * 消息模板服务类
 *
 * @author Gavin Zhang
 * @date 2023/2/25 21:08
 */
public interface MsgModelService {

    /**
     * 添加新模板
     *
     * @param messageTemplate 模板信息
     * @return 插入是否成功
     */
    ResultVO<String> addNewModel(MessageTemplate messageTemplate);
}