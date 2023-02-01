package icu.zxb996.mp.handler.receiver.service.impl;

import cn.hutool.core.collection.CollUtil;
import icu.zxb996.mp.common.domain.AnchorInfo;
import icu.zxb996.mp.common.domain.LogParam;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.common.enums.AnchorState;
import icu.zxb996.mp.handler.pending.Task;
import icu.zxb996.mp.handler.pending.TaskPendingHolder;
import icu.zxb996.mp.handler.receiver.service.ConsumerService;
import icu.zxb996.mp.handler.utils.GroupIdMappingUtils;
import icu.zxb996.mp.support.utils.LogUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消费者服务方法实现类
 *
 * @author Gavin Zhang
 * @date 2023/1/31 15:44
 */
@Service("consumerServiceImpl")
public class ConsumerServiceImpl implements ConsumerService {

    private static final String LOG_BIZ_TYPE = "Receiver#consumer";
    private static final String LOG_BIZ_RECALL_TYPE = "Receiver#recall";

    @Resource
    private ApplicationContext context;

    @Resource
    private TaskPendingHolder taskPendingHolder;

    @Resource(name = "logUtils")
    private LogUtils logUtils;


    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            logUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(), AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());
            // 将每个信息封装到Task里，进行消费
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            // 使用了线程池处理task -- execute()
            taskPendingHolder.route(topicGroupId).execute(task);
        }
    }
}