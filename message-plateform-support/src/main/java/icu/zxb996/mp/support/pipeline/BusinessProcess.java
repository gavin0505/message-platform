package icu.zxb996.mp.support.pipeline;

/**
 * 业务流程处理接口
 *
 * @author Gavin Zhang
 * @date 2023/1/12 17:39
 */
public interface BusinessProcess<T extends ProcessModel> {

    /**
     * 真正处理逻辑
     *
     * @param context 责任链上下文
     */
    void process(ProcessContext<T> context);
}
