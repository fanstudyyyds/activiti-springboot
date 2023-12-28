package com.fan.activiti;

import com.fan.activiti.service.ActFlowCommService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ActivitiSpringbootApplicationTests {

    @Autowired
    ActFlowCommService actFlowCommService;

    @Test
    public void test(){
        actFlowCommService.saveNewDeploy();
    }

}
