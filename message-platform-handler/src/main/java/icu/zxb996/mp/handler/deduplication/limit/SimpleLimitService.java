package icu.zxb996.mp.handler.deduplication.limit;

import cn.hutool.core.collection.CollUtil;
import icu.zxb996.mp.common.constant.CommonConstant;
import icu.zxb996.mp.common.domain.TaskInfo;
import icu.zxb996.mp.handler.deduplication.DeduplicationParam;
import icu.zxb996.mp.handler.deduplication.service.AbstractDeduplicationService;
import icu.zxb996.mp.support.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 普通计数去重
 * <p/>
 * 采用普通计数去重方法，限制的是每天发送的条数。
 *
 * @author Gavin Zhang
 * @date 2023/2/11 15:27
 */
@Service(value = "simpleLimitService")
public class SimpleLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SP_";

    @Resource(name = "redisUtils")
    private RedisUtils redisUtils;

    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {

        // 需要被去重的对象名单
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        // 获取消息接收对象在redis的记录
        Map<String, String> readyPutRedisReceiver = new HashMap<>(taskInfo.getReceiver().size());
        // redis数据隔离，构建新键：SP_xxx
        List<String> keys = deduplicationAllKey(service, taskInfo).stream().map(key -> LIMIT_TAG + key).collect(Collectors.toList());
        Map<String, String> receiverFromRedis = redisUtils.mGet(keys);

        // 判断本次消息接收对象是否满足去重条件
        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String value = receiverFromRedis.get(key);

            // 符合条件的用户，即用户达到了一定的次数，需要去重
            if (Objects.nonNull(value) && Integer.parseInt(value) >= param.getCountNum()) {
                // 将该用户添加到去重名单中
                filterReceiver.add(receiver);
            } else {
                readyPutRedisReceiver.put(receiver, key);
            }
        }

        // 不符合条件的用户：即不需要去重，但需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceiver, receiverFromRedis, param.getDeduplicationTime());

        return filterReceiver;
    }

    /**
     * 存入redis 实现去重
     *
     * @param readyPutRedisReceiver 接收者
     * @param receiverFromRedis     redis原来已经存有的接收者
     * @param deduplicationTime     过期时间
     */
    private void putInRedis(Map<String, String> readyPutRedisReceiver,
                            Map<String, String> receiverFromRedis,
                            Long deduplicationTime) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());
        for (Map.Entry<String, String> entry : readyPutRedisReceiver.entrySet()) {
            String key = entry.getValue();
            if (Objects.nonNull(receiverFromRedis.get(key))) {
                keyValues.put(key, String.valueOf(Integer.parseInt(receiverFromRedis.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(CommonConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEx(keyValues, deduplicationTime);
        }
    }
}