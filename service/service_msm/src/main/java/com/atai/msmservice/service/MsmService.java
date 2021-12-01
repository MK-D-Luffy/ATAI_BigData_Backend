package com.atai.msmservice.service;

public interface MsmService {

    //发送短信的方法
    boolean send(String[] param, String phoneNumber);
}
