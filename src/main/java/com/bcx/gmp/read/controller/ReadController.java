package com.bcx.gmp.read.controller;

import com.bcx.gmp.read.entity.Config;
import com.bcx.gmp.read.entity.ConnectParam;
import com.bcx.gmp.read.entity.Recovery;
import com.bcx.gmp.read.entity.RemoteConnect;
import com.bcx.gmp.read.utils.ConnectLinuxCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * @author Design By Scrooged
 * @version 1.0
 * @description
 * @date 2019/2/15 10:20
 */
@EnableConfigurationProperties(Config.class)
@Controller
public class ReadController {

    private static Logger logger = LoggerFactory.getLogger(ReadController.class);

    /*@Autowired
    private ApplicationContext context;*/

    /*@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }*/

    @Autowired
    Config config;

    @GetMapping("/content/get")
    public String getContent(Model model, @RequestParam("selectSystem") String selectSystem){
        String filePath = null;
        if ("1".equals(selectSystem)) {
            filePath = config.getWindowsFilePath();
        }else if ("2".equals(selectSystem)){
            filePath = config.getFilePath();
        }else {
            filePath = "/opt/pgbackup/recover.sh";
        }
        config.setRealPath(filePath);
        config.setContent(getStringValue(filePath));
        model.addAttribute("defineConfig", config);
        return "config";
    }

    private String getStringValue(String filePath){
        StringBuffer buffer = new StringBuffer();//gb2312
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"))){
            String lineText = null;
            while (null != (lineText = bufferedReader.readLine())){
                buffer.append(lineText).append("\r");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private Object returnContent(String filePath, String executeResult){
        Config nextConfig = new Config();
        nextConfig.setRealPath(filePath);
        nextConfig.setContent(getStringValue(filePath));
        if (null != executeResult && executeResult.contains("Permission denied")){
            //返回1表示shell脚本未授权
            nextConfig.setExecuteResult("1");
        }
        return nextConfig;
    }

    @GetMapping("/content/refresh")
    public String getRefresh(Model model, @RequestParam("filePath") String filePath){
        model.addAttribute("defineConfig", returnContent(filePath, null));
        return "config";
    }

    @PostMapping("/content/update")
    public String updateContent(Model model, @ModelAttribute Config updateConfig){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(updateConfig.getFilePath()))))){
            String content = updateConfig.getContent();
            bufferedWriter.write(content, 0, content.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("defineConfig", returnContent(updateConfig.getFilePath(), null));
        return "config";
    }

    //@ResponseBody
    @GetMapping("/content/execute")
    public String execute(Model model, @RequestParam("filePath") String filePath, @RequestParam(value = "authorization", required = false) String authorization){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        String commandStr = filePath;
        //String commandStr = "/opt/test/test.sh";
        if (!StringUtils.isEmpty(authorization)){
            //commandStr = authorization;
            //commandStr = authorization + " " + filePath + ";" + filePath;
            commandStr = "chmod 744 " + " " + commandStr + ";" + commandStr;
        }
        model.addAttribute("defineConfig", returnContent(filePath, ConnectLinuxCommand.connectLinux(remoteConnect, commandStr)));
        return "config";
    }

    @PostMapping("/content/save-path-timing")
    public String savePathAndTiming(@ModelAttribute ConnectParam connectParam){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        String systemShellPath = null;
        String resourceLocation = null;
        String filePathString = connectParam.getFilePathString();
        if ("1".equals(connectParam.getSelectSystem())){
            systemShellPath = config.getWindowsFilePath();
            //resourceLocation = "classpath:windowsShell.txt";
            resourceLocation = config.getWindowsShell();
            ConnectLinuxCommand.connectLinux(remoteConnect, "echo \"" + connectParam.getTiming() + " " + systemShellPath + " >> ~/.gobackup/gobackup.log\" > /var/spool/cron/root");
        }else {
            //ConnectLinuxCommand.connectLinux(remoteConnect, "echo \"" + connectParam.getTiming() + " /usr/local/bin/gobackup perform >> ~/.gobackup/gobackup.log\" > /var/spool/cron/root");
            ConnectLinuxCommand.connectLinux(remoteConnect, "echo \"" + connectParam.getTiming() + " " + config.getFilePath() + " >> ~/.gobackup/gobackup.log\" > /var/spool/cron/root");
            String saveWay = connectParam.getSaveWay();
            if (!StringUtils.isEmpty(filePathString)){
                String[] filePaths = filePathString.split(" ");
                filePathString = "";
                for (String filePath : filePaths){
                    filePathString = filePathString + "        - " + filePath + "\r";
                }
            }
            systemShellPath = config.getGobackupFilePath();
            if ("1".equals(saveWay)){
                resourceLocation = "classpath:gobackup-local.yml";
            }else if ("2".equals(saveWay)){
                resourceLocation = "classpath:gobackup-ssh.yml";
            }else {
                resourceLocation = "classpath:gobackup-ftp.yml";
            }//new FileInputStream(ResourceUtils.getFile(resourceLocation)), "UTF-8"
        }
        logger.info("resourceLocation: " + resourceLocation);
        if ("1".equals(connectParam.getSelectSystem())){
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(config.getWindowsShell())), "UTF-8"));
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(systemShellPath))))){
                String lineText = null;
                while (null != (lineText = bufferedReader.readLine())){
                    bufferedWriter.write(lineText.replace("@filePathString@", filePathString).replace("@windowsPassword@", connectParam.getWindowsPassword())
                            .replace("@windowsUsername@", connectParam.getWindowsUsername())
                            .replace("@windowsHost@", connectParam.getWindowsHost()).replace("@windowsPath@", connectParam.getWindowsPath())+"\n");
                    //bufferedWriter.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DefaultResourceLoader().getResource(resourceLocation).getInputStream(), "UTF-8"));
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(systemShellPath))))){
                String lineText = null;
                while (null != (lineText = bufferedReader.readLine())){
                    bufferedWriter.write(lineText.replace("@temporaryLocation@", config.getTemporaryLocation()).replace("@localPath@", connectParam.getLocalPath())
                            .replace("@sshHost@", connectParam.getSshHost()).replace("@sshPort@", connectParam.getSshPort()).replace("@sshPath@", connectParam.getSshPath())
                            .replace("@sshTimeout@", connectParam.getSshTimeout()).replace("@sshUsername@", connectParam.getSshUsername()).replace("@sshPassword@", connectParam.getSshPassword())
                            .replace("@ftpHost@", connectParam.getFtpHost()).replace("@ftpPort@", connectParam.getFtpPort()).replace("@ftpPath@", connectParam.getFtpPath())
                            .replace("@ftpTimeout@", connectParam.getFtpTimeout()).replace("@ftpUsername@", connectParam.getFtpUsername()).replace("@ftpPassword@", connectParam.getFtpPassword())
                            .replace("@filePathString@", filePathString));
                    bufferedWriter.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(config.getTxtPath()))))){
            bufferedWriter.write("selectSystem=" + connectParam.getSelectSystem() + "&&windowsHost=" + connectParam.getWindowsHost()
                    + "&&windowsPath=" + connectParam.getWindowsPath() + "&&windowsUsername=" + connectParam.getWindowsUsername()
                    + "&&windowsPassword=" + connectParam.getWindowsPassword() + "&&filePathString=" + connectParam.getFilePathString() + "&&saveWay=" + connectParam.getSaveWay()
                    + "&&localPath=" + connectParam.getLocalPath() + "&&sshHost=" + connectParam.getSshHost() + "&&sshPort=" + connectParam.getSshPort()
                    + "&&sshPath=" + connectParam.getSshPath() + "&&sshTimeout=" + connectParam.getSshTimeout() + "&&sshUsername=" + connectParam.getSshUsername()
                    + "&&sshPassword=" + connectParam.getSshPassword() + "&&ftpHost=" + connectParam.getFtpHost() + "&&ftpPort=" +connectParam.getFtpPort() + "&&ftpPath=" + connectParam.getFtpPath()
                    + "&&ftpTimeout=" + connectParam.getFtpTimeout() + "&&ftpUsername=" + connectParam.getFtpUsername() + "&&ftpPassword=" + connectParam.getFtpPassword() + "&&timing=" + connectParam.getTiming());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*if ("1".equals(connectParam.getSelectSystem())){
            //int length = systemShellPath.lastIndexOf("/");
            //String suffix = systemShellPath.substring(0,length+1);
            // ConnectLinuxCommand.connectLinux(remoteConnect, "vi +':w ++ff=unix' +':q' " + systemShellPath + " >> " + suffix + "gmp-read.log");
            ConnectLinuxCommand.connectLinux(remoteConnect, "vi +':w ++ff=unix' +':q' " + systemShellPath);
        }*/
        //model.addAttribute("defineConfig", returnContent(config.getFilePath(), null));
        return "redirect:/index.html";
    }

    @ResponseBody
    @GetMapping("/content/backfill")
    public String backfill(){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        String dbFilePath = config.getDbFilePath();
        if (!dbFilePath.endsWith("/")) {
            dbFilePath += "/";
        }
        String dbFilePathResult = ConnectLinuxCommand.connectLinux(remoteConnect, "ls " + dbFilePath + "recovery.conf");
        if (dbFilePathResult.contains("ls") && dbFilePathResult.contains(dbFilePath+"recovery.conf")){
            dbFilePathResult = "(主库)";
        }else {
            dbFilePathResult = "(从库)";
        }
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DefaultResourceLoader().getResource("classpath:config.txt").getInputStream(), "UTF-8")
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(config.getTxtPath())), "UTF-8"))){
            String backfillString = bufferedReader.readLine()+"&&serviceIP="+config.getIp()+dbFilePathResult;
            /*String[] backfills = backfillString.split("&&");
            if (backfills.length > 0){
                String ip = null;
                String username = null;
                String password = null;
                String path = null;
                for (int i = 0; i < backfills.length; i++) {
                    String keyAndValue[] = backfills[i].split("=");

                }
            }*/
            return backfillString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @ResponseBody
    @GetMapping("/content/check-box-value")
    public String getCheckBoxValues(@RequestParam("ip") String ip, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("path") String path){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        if (null != path && path.length() > 0){
            int index = path.lastIndexOf(":");
            path = path.substring(0, index) + path.substring(index+1);
        }
        return ConnectLinuxCommand.connectLinux(remoteConnect, "sshpass -p " + password + " ssh " + username+ "@" + ip + " \"ls " + path + "\"");
    }

    @PostMapping("/content/recover")
    public String recover(@ModelAttribute Recovery recovery){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        //String defaultRecovery = backfill();
        /*if (!"".equals(defaultRecovery)){
            String[] recoveryStrings = defaultRecovery.split("&&");
            for (String recoverString : recoveryStrings) {
                if (recoverString.contains("selectSystem")) {

                }
            }
        }*/
        String recoverPath = recovery.getRecoverPath();
        if (!recoverPath.endsWith("/")) {
            recoverPath += "/";
        }
        ConnectLinuxCommand.connectLinux(remoteConnect, "sshpass -p " + recovery.getPwd() +" scp " + recovery.getUserName() + "@" + recovery.getHost() + ":" + recoverPath + recovery.getRecoverFile() + " /opt/pgbackup/recover/");
        ConnectLinuxCommand.connectLinux(remoteConnect, "/opt/pgbackup/recover.sh /opt/pgbackup/windows_backup.sh " + recovery.getRecoverFile());
        return "redirect:/index.html";
    }

    @GetMapping("/container/handle")
    public String handleContainer(@RequestParam("containerName") String containerName, @RequestParam("handle") String handle){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        String commandStr = "docker " + handle + " " + containerName;
        /*// TODO: 2019/3/15 只对北大维信的
        if ("postgres".equals(containerName) || "postgres-247-slave".equals(containerName)) {
            if ("start".equals(handle)){
                commandStr = commandStr + "; systemctl start keepalived.service";
            }else if ("stop".equals(handle)){
                commandStr = "systemctl stop keepalived.service ; " + commandStr;
            }else {
                commandStr = "systemctl stop keepalived.service ; " + commandStr + "; systemctl start keepalived.service";
            }
        }*/
        ConnectLinuxCommand.connectLinux(remoteConnect, commandStr);
        return "redirect:/index.html";
    }

    @GetMapping("/content/backup-execute")
    public String execute(@RequestParam("selectSystem") String selectSystem){
        RemoteConnect remoteConnect = new RemoteConnect();
        remoteConnect.setIp(config.getIp());
        remoteConnect.setUserName(config.getUserName());
        remoteConnect.setPassword(config.getPassword());
        String filePath = null;
        if ("1".equals(selectSystem)) {
            filePath = config.getWindowsFilePath();
        }else {
            filePath = config.getFilePath();
        }
        ConnectLinuxCommand.connectLinux(remoteConnect, filePath);
        return "redirect:/index.html";
    }

}
