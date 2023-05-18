package com.st1.itx.nettyServer;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope("singleton")
public class ApacheFtpServer {
    private final Logger logger = LoggerFactory.getLogger(ApacheFtpServer.class);

    @Autowired
    private DataSource dataSource;

    protected FtpServer server;

    @Value("${itx_Config}")
    private String itxResource;

//    @PostConstruct
    public void initFtp() throws IOException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();

        listenerFactory.setPort(3131);

        DataConnectionConfigurationFactory dataConnectionConfFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfFactory.setPassivePorts("10000-10500");
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfFactory.createDataConnectionConfiguration());

//      SSL
//      SslConfigurationFactory ssl = new SslConfigurationFactory();
//      ssl.setKeystoreFile(new File("src/main/resources/ftpserver.jks"));
//      ssl.setKeystorePassword("password");
//      ssl.setSslProtocol("SSL");
//      set the SSL configuration for the listener
//      listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
//      listenerFactory.setImplicitSsl(true);

        //4、替換默認的監聽器
        Listener listener = listenerFactory.createListener();
        serverFactory.addListener("default", listener);

        //5、配置自定義用戶事件
        Map<String, Ftplet> ftpLets = new HashMap();
        ftpLets.put("ftpService", new ApacheFtpPlet());
        serverFactory.setFtplets(ftpLets);

//      6、讀取用戶的配置信息
//      注意：配置文件位於resources目錄下，如果項目使用內置容器打成jar包發布，FTPServer無法直接直接讀取Jar包中的配置文件。
//      解決辦法：將文件覆制到指定目錄(本文指定到根目錄)下然後FTPServer才能讀取到。
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        String tempPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".properties";
        File tempConfig = new File(itxResource + "/spring/users.properties");
//      ClassPathResource resource = new ClassPathResource("spring/ftp/users.properties");
//      IOUtils.copy(resource.getInputStream(), new FileOutputStream(tempConfig));
        userManagerFactory.setFile(tempConfig);
        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());  //密碼以明文的方式
        serverFactory.setUserManager(userManagerFactory.createUserManager());

//      6.2、基於數據庫來存儲用戶實例
//        DbUserManagerFactory dbUserManagerFactory = new DbUserManagerFactory();
//        dbUserManagerFactory.setDataSource(dataSource);
//        dbUserManagerFactory.setAdminName("admin");
//        dbUserManagerFactory.setSqlUserAdmin("SELECT userid FROM FTP_USER WHERE userid='{userid}' AND userid='admin'");
//        dbUserManagerFactory.setSqlUserInsert("INSERT INTO FTP_USER (userid, userpassword, homedirectory, " +
//                "enableflag, writepermission, idletime, uploadrate, downloadrate) VALUES " +
//                "('{userid}', '{userpassword}', '{homedirectory}', {enableflag}, " +
//                "{writepermission}, {idletime}, uploadrate}, {downloadrate})");
//        dbUserManagerFactory.setSqlUserDelete("DELETE FROM FTP_USER WHERE userid = '{userid}'");
//        dbUserManagerFactory.setSqlUserUpdate("UPDATE FTP_USER SET userpassword='{userpassword}',homedirectory='{homedirectory}',enableflag={enableflag},writepermission={writepermission},idletime={idletime},uploadrate={uploadrate},downloadrate={downloadrate},maxloginnumber={maxloginnumber}, maxloginperip={maxloginperip} WHERE userid='{userid}'");
//        dbUserManagerFactory.setSqlUserSelect("SELECT * FROM FTP_USER WHERE userid = '{userid}'");
//        dbUserManagerFactory.setSqlUserSelectAll("SELECT userid FROM FTP_USER ORDER BY userid");
//        dbUserManagerFactory.setSqlUserAuthenticate("SELECT userid, userpassword FROM FTP_USER WHERE userid='{userid}'");
//        dbUserManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
//        serverFactory.setUserManager(dbUserManagerFactory.createUserManager());

//      7、實例化FTP Server
        server = serverFactory.createServer();
    }


    /**
     * ftp server start
     */
    public void start() {
        try {
            server.start();
            logger.info("Apache Ftp server is starting!");
        } catch (FtpException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(errors.toString());
        }
    }


    /**
     * ftp server stop
     */
    public void stop() {
        server.stop();
        logger.info("Apache Ftp server is stoping!");
    }

}
