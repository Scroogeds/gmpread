package com.bcx.gmp.read.entity;

import java.io.Serializable;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description 连接ssh远程服务器类
 * @date 2019/2/19 10:37
 */
public class RemoteConnect implements Serializable {

    private String ip;

    private String userName;

    private String password;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
