package icu.zxb996.mp.handler.receiver.service.impl;

import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.receiver.service.ConsumerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消费者服务方法实现类
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:44
 */
@Service("consumerServiceImpl")
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {

    }
}