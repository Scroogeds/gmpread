package com.bcx.gmp.read.entity;

import java.io.Serializable;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/3/14 20:11
 */
public class Recovery implements Serializable {

    private String host;

    private String recoverFile;

    private String userName;

    private String pwd;

    private String recoverPath;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRecoverFile() {
        return recoverFile;
    }

    public void setRecoverFile(String recoverFile) {
        this.recoverFile = recoverFile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRecoverPath() {
        return recoverPath;
    }

    public void setRecoverPath(String recoverPath) {
        this.recoverPath = recoverPath;
    }
}
