package com.fan.activiti.entity;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserEntity {
    private String id;

    private String name;

    private String dept;

    private String post;

    public UserEntity(String id, String name, String dept, String post) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.post = post;
    }


    private static final Map<String, UserEntity> mapUserEntity = new HashMap();

    static {
        mapUserEntity.put("1001", new UserEntity("1001", "张三", "", ""));
        mapUserEntity.put("1002", new UserEntity("1002", "李四", "", ""));
        mapUserEntity.put("1003", new UserEntity("1003", "王五-部门经理--》", "", "部门经理"));
        mapUserEntity.put("1004", new UserEntity("1004", "赵六-财务主管--》", "", "财务主管"));
        mapUserEntity.put("1005", new UserEntity("1005", "杨七-总经理--》", "", "总经理"));
    }

    public static final UserEntity getMapUserEntity(String key) {
        return mapUserEntity.get(key);
    }
}

