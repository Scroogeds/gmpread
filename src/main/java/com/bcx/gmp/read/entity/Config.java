package com.bcx.gmp.read.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/2/15 10:24
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "config")
@PropertySource("classpath:config.properties")
public class Config extends RemoteConnect {

    private String filePath;

    private String shellCommand;

    private String content;

    //执行脚本返回的结果，目前只对shell未授权作处理，未授权--------------"1"
    private String executeResult = "0";

    //从docker中备份出数据库临时存放的位置
    private String temporaryLocation;

    private String gobackupFilePath;

    private String windowsFilePath;

    private String windowsShellCommand;

    private String realPath;

    private String txtPath;

    private String windowsShell;

    private String dbFilePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public void setShellCommand(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getTemporaryLocation() {
        return temporaryLocation;
    }

    public void setTemporaryLocation(String temporaryLocation) {
        this.temporaryLocation = temporaryLocation;
    }

    public String getGobackupFilePath() {
        return gobackupFilePath;
    }

    public void setGobackupFilePath(String gobackupFilePath) {
        this.gobackupFilePath = gobackupFilePath;
    }

    public String getWindowsFilePath() {
        return windowsFilePath;
    }

    public void setWindowsFilePath(String windowsFilePath) {
        this.windowsFilePath = windowsFilePath;
    }

    public String getWindowsShellCommand() {
        return windowsShellCommand;
    }

    public void setWindowsShellCommand(String windowsShellCommand) {
        this.windowsShellCommand = windowsShellCommand;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath;
    }

    public String getWindowsShell() {
        return windowsShell;
    }

    public void setWindowsShell(String windowsShell) {
        this.windowsShell = windowsShell;
    }

    public String getDbFilePath() {
        return dbFilePath;
    }

    public void setDbFilePath(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }
}
