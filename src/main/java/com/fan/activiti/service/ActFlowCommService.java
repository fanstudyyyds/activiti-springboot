package com.fan.activiti.service;

import com.fan.activiti.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ActFlowCommService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    public void saveNewDeploy() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection.bpmn") // 添加bpmn资源
                .name("出差申请")
                .deploy();
//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    public ProcessInstance startProcess(String userId) {

        Map<String, Object> variables = new HashMap<>();
//		启动流程
        log.info("【启动流程】，formKey ：{},bussinessKey:{}", "formKey", "bussinessKey");
        variables.put("assignee0", UserEntity.getMapUserEntity(userId).getId());
        variables.put("assignee1", UserEntity.getMapUserEntity("1003").getId());
        variables.put("assignee2", UserEntity.getMapUserEntity("1005").getId());
        variables.put("assignee3", UserEntity.getMapUserEntity("1004").getId());
        variables.put("bussinessKey", "faneeeeeeeeeeeeee");
        variables.put("num", 4);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection", "faneeeeeeeeeeeeee", variables);
//		流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("【启动流程】- 成功，processDefinitionId：{}", processDefinitionId);
        String taskId = processInstance.getSuperExecutionId();
        log.info("【执行任务】- 成功，processDefinitionId：{}", taskId);
        completeProcess(UserEntity.getMapUserEntity("1001").getId());
        return processInstance;
    }

    /**
     * 查看个人任务信息
     */
    public List<Task> getMyTaskInfoList(String userid) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userid);
        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();
        return list;
    }

    /**
     * 完成提交任务
     */
    public void completeProcess(String userId) {
        //任务Id 查询任务对象
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
        if (tasks == null) {
            log.error("completeProcess - task is null!!");
            return;
        }
        //设置审批人的userId
        Authentication.setAuthenticatedUserId(userId);
        for (Task task : tasks) {
            //添加记录
            System.out.println("-----------完成任务操作 开始----------");
            System.out.println("-----------" + task.getId() + "----------");
            System.out.println("任务Id=" + task.getId());
            System.out.println("负责人id=" + userId + UserEntity.getMapUserEntity(userId).getName());
            //完成办理
            taskService.complete(task.getId());
            System.out.println("-----------完成任务操作 结束----------");
        }
    }


    /**
     * 查询历史记录
     *
     * @param businessKey
     */
    public void searchHistory(String businessKey) {
        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
        if (CollectionUtils.isEmpty(list1)) {
            return;
        }

        String processDefinitionId = list1.get(0).getProcessDefinitionId();
        String id = list1.get(0).getId();
        System.out.println("历史记录id" + processDefinitionId);
        // 历史相关Service
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .processInstanceId(id)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        System.out.println("长度" + list.size());
        for (HistoricActivityInstance hiact : list) {
            if (StringUtils.isBlank(hiact.getAssignee())) {
                continue;
            }
            System.out.println("活动ID:" + hiact.getId());
            System.out.println("流程实例ID:" + hiact.getProcessInstanceId());
            System.out.println("办理人ID：" + hiact.getAssignee());
            System.out.println("开始时间：" + hiact.getStartTime());
            System.out.println("结束时间：" + hiact.getEndTime());
            System.out.println("==================================================================");
        }
    }


}
