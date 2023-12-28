package com.fan.activiti.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fan.activiti.entity.UserEntity;
import com.fan.activiti.service.ActFlowCommService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/act")
public class ActFlowCommController {
    @Autowired
    ActFlowCommService actFlowCommService;

    @RequestMapping("/saveNewDeploy")
    public String saveNewDeploy() {
        actFlowCommService.saveNewDeploy();
        return "ok";
    }

    @RequestMapping("/startProcess")
    public String startProcess(String userid) {
        actFlowCommService.startProcess(userid);
        return "ok";
    }


    @RequestMapping("/getMyTaskInfoList")
    public String getMyTaskInfoList(String userid) {
        System.out.println(userid);
        List<Task> myTaskInfoList = actFlowCommService.getMyTaskInfoList(userid);
        for (Task task : myTaskInfoList) {
            System.out.println(task.getName());
        }
        return JSON.toJSONString(myTaskInfoList.toString());
    }


    @RequestMapping("/completeProcess")
    public String completeProcess(String userid) {
        System.out.println(userid);
        actFlowCommService.completeProcess(userid);

        return "ok";
    }

    @RequestMapping("/searchHistory")
    public void searchHistory(String key) {
        System.out.println(key);
        actFlowCommService.searchHistory(key);
    }

}
