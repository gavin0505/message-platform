package icu.zxb996.mp.support.pipeline;

import java.util.List;

/**
 * 业务执行模板（把责任链的逻辑串起来）
 *
 * @author Gavin Zhang
 * @date 2023/1/12 17:39
 */
public class ProcessTemplate {

    private List<BusinessProcess> processList;

    public List<BusinessProcess> getProcessList() {
        return processList;
    }

    public void setProcessList(List<BusinessProcess> processList) {
        this.processList = processList;
    }
}
