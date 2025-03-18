package org.flowable.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
@Slf4j
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("MyTaskListener triggered: " + delegateTask.getName());
        log.info("MyTaskListener event name:"+delegateTask.getEventName());
        if("申请人提交".equals(delegateTask.getName())&&"create".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("小明");
        }else{
            delegateTask.setAssignee("小李");
        }
    }
}
