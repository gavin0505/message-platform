package icu.zxb996.mp.support.exception;

import icu.zxb996.mp.common.enums.RespStatusEnum;
import icu.zxb996.mp.support.pipeline.ProcessContext;

/**
 * 责任链流程异常处理
 *
 * @author Gavin Zhang
 * @date 2023/1/12 17:44
 */
public class ProcessException extends RuntimeException {
    /**
     * 流程处理上下文
     */
    private final ProcessContext processContext;

    public ProcessException(ProcessContext processContext) {
        super();
        this.processContext = processContext;
    }

    public ProcessException(ProcessContext processContext, Throwable cause) {
        super(cause);
        this.processContext = processContext;
    }

    @Override
    public String getMessage() {
        if (this.processContext != null) {
            return this.processContext.getResponse().getMessage();
        } else {
            return RespStatusEnum.CONTEXT_IS_NULL.getMsg();
        }
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }
}
