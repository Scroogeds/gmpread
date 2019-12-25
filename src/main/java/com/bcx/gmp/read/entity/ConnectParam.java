package com.bcx.gmp.read.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/2/26 20:51
 */
public class ConnectParam implements Serializable {

    /**
     * 服务器
     * 1-windows
     * 2-linux
     */
    private String selectSystem;

    private String windowsHost;

    private String windowsPath;

    private String windowsUsername;

    private String windowsPassword;

    private String filePathString;

    /**
     * 存储的方式
     * 1-local
     * 2-ssh
     * 3-ftp
     */
    private String saveWay;

    private String localPath;

    /**
     * ssh传送的一些参数
     */
    private String sshHost;

    private String sshPort;

    private String sshPath;

    private String sshTimeout;

    /*private String sshPrivateKey;*/

    private String sshUsername;

    private String sshPassword;

    /**
     * ftp传送的一些参数
     */
    private String ftpHost;

    private String ftpPort;

    private String ftpPath;

    private String ftpTimeout;

    private String ftpUsername;

    private String ftpPassword;

    /**
     * 定时时间
     */
    private String timing;

    public String getSelectSystem() {
        return selectSystem;
    }

    public void setSelectSystem(String selectSystem) {
        this.selectSystem = selectSystem;
    }

    public String getWindowsHost() {
        return windowsHost;
    }

    public void setWindowsHost(String windowsHost) {
        this.windowsHost = windowsHost;
    }

    public String getWindowsPath() {
        return windowsPath;
    }

    public void setWindowsPath(String windowsPath) {
        this.windowsPath = windowsPath;
    }

    public String getWindowsUsername() {
        return windowsUsername;
    }

    public void setWindowsUsername(String windowsUsername) {
        this.windowsUsername = windowsUsername;
    }

    public String getWindowsPassword() {
        return windowsPassword;
    }

    public void setWindowsPassword(String windowsPassword) {
        this.windowsPassword = windowsPassword;
    }

    public String getFilePathString() {
        return filePathString;
    }

    public void setFilePathString(String filePathString) {
        this.filePathString = filePathString;
    }

    public String getSaveWay() {
        return saveWay;
    }

    public void setSaveWay(String saveWay) {
        this.saveWay = saveWay;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getSshHost() {
        return sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }

    public String getSshPort() {
        return StringUtils.isEmpty(sshPort) ? "22" : sshPort;
    }

    public void setSshPort(String sshPort) {
        this.sshPort = sshPort;
    }

    public String getSshPath() {
        return sshPath;
    }

    public void setSshPath(String sshPath) {
        this.sshPath = sshPath;
    }

    public String getSshTimeout() {
        return StringUtils.isEmpty(sshTimeout) ? "300" : sshTimeout;
    }

    public void setSshTimeout(String sshTimeout) {
        this.sshTimeout = sshTimeout;
    }

    /*public String getSshPrivateKey() {
        return StringUtils.isEmpty(sshPrivateKey) ? "~/.ssh/id_rsa" : sshPrivateKey;
    }

    public void setSshPrivateKey(String sshPrivateKey) {
        this.sshPrivateKey = sshPrivateKey;
    }*/

    public String getSshUsername() {
        return sshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpPort() {
        return StringUtils.isEmpty(ftpPort) ? "21" : ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFtpTimeout() {
        return StringUtils.isEmpty(ftpTimeout) ? "300" : ftpTimeout;
    }

    public void setFtpTimeout(String ftpTimeout) {
        this.ftpTimeout = ftpTimeout;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
