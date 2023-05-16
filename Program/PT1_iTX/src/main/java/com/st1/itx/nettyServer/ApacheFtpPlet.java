package com.st1.itx.nettyServer;

import org.apache.ftpserver.ftplet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApacheFtpPlet extends DefaultFtplet {

    private static final Logger logger = LoggerFactory.getLogger(ApacheFtpPlet.class);

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        //路徑
        String path = session.getUser().getHomeDirectory();

        //使用者
        String name = session.getUser().getName();

        //檔名
        String filename = request.getArgument();

        logger.info("用戶:'{}'，上傳文件到目錄：'{}'，文件名稱為：'{}'，狀態：開始上傳~", name, path, filename);
        return super.onUploadStart(session, request);
    }


    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        //獲取上傳文件的上傳路徑
        String path = session.getUser().getHomeDirectory();

        //獲取上傳用戶
        String name = session.getUser().getName();

        //獲取上傳文件名
        String filename = request.getArgument();

        logger.info("用戶:'{}'，上傳文件到目錄：'{}'，文件名稱為：'{}，狀態：成功！'", name, path, filename);
        return super.onUploadEnd(session, request);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadStart(session, request);
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadEnd(session, request);
    }
}
