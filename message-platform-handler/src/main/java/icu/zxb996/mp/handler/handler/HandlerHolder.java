package icu.zxb996.mp.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Handler映射器
 *
 * @author Gavin Zhang
 * @date 2023/2/1 16:24
 */

@Component
public class HandlerHolder {


    private final Map<Integer, Handler> handlers = new HashMap<>(128);

    public void putHandler(Integer channelCode, Handler handler) {
        handlers.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}