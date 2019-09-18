package com.tct.smart_lock.domain;

import lombok.Data;

@Data
public class Locks {
    //用于区分不同车位锁
    private String lockId;
    //车位锁状态
    private int lockState;
    //推送的注册ID，由app从极光接口获取，再上传至服务器
    private String regId;
    //车位锁绑定的用户ID
    private String userId;

    //位置信息，经纬度
    private double longitude;
    private double latitude;
    private String address;
    private int enable;

}
