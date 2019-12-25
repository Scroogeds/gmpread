package com.bcx.gmp.read.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.bcx.gmp.read.entity.RemoteConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/2/19 10:40
 */
@Service
public class ConnectLinuxCommand {
    private static Logger logger = LoggerFactory.getLogger(ConnectLinuxCommand.class);
    private static String DEFAULT_CHARTEST = "UTF-8";
    private static Connection connection;

    /**
     * 用户名密码登录方式，远程登录linux服务器
     * @param remoteConnect
     * @return
     */
    private static boolean login(RemoteConnect remoteConnect){
        boolean flag = false;
        try {
            connection = new Connection(remoteConnect.getIp());
            connection.connect();
            flag = connection.authenticateWithPassword(remoteConnect.getUserName(), remoteConnect.getPassword());// 认证
            if (flag) {
                logger.info("认证成功！");
            } else {
                logger.info("认证失败！");
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 远程执行shell脚本或者命令
     * @param cmd 脚本命令
     * @return
     */
    private static String execute(String cmd){
        String result = "";
        try {
            //打开一个会话
            Session session = connection.openSession();
            //执行命令
            session.execCommand(cmd);
            //如果为得到标准输出为空，说明脚本执行出错了
            result = processStdout(session.getStdout(), DEFAULT_CHARTEST);
            if (StringUtils.isEmpty(result)){
                result = processStdout(session.getStderr(), DEFAULT_CHARTEST);
            }
            connection.close();
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行的返回结果
     * @param in 输入流对象
     * @param charset 编码
     * @return String 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset){
        InputStream inputStream = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))){
            String line = null;
            while (null != (line = br.readLine())){
                buffer.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 通过用户名和密码关联Linux服务器
     * @param remoteConnect
     * @param commandStr
     * @return
     */
    public static String connectLinux(RemoteConnect remoteConnect, String commandStr){
        logger.info("ConnectLinuxCommand  scpGet===" + "ip:" + remoteConnect.getIp() + "  userName:" + remoteConnect.getUserName() + "  commandStr:" + commandStr);

        String returnStr = "";
        //boolean result = true;

        try {
            if (login(remoteConnect)){
                returnStr = execute(commandStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(returnStr)){
            logger.info("Execute successfully!");
            //result = false;
        }else{
            logger.info("execute result ===" + returnStr);
        }
        //return result;
        return returnStr;
    }

    /*public static List<String> connectWindows(RemoteConnect remoteConnect, String path){
        try {
            if (login(remoteConnect)){
                //打开会话
                Session sess = connection.openSession();
                //    System.out.println("cmd----");
                SCPClient scpClient = connection.createSCPClient();
                //执行命令
                sess.execCommand("ruby C:\\WhatWeb-master\\whatweb --output-xml http://216.139.147.75:443/");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
