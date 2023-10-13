package com.st1.itx.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * SftpClient
 * 
 * @author ST1 Wei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class SftpClient extends CommBuffer {

	/* 轉型共用工具 */
	@Autowired
	private Parse parse;

	/**
	 * upload
	 * 
	 * @param url             SFTP server url
	 * @param port            SFTP server port
	 * @param auth            username:password
	 * @param pathToFile      path to file
	 * @param targetDirectory target directory
	 * @param titaVo          titaVo
	 * @return true when upload success else false
	 * @throws LogicException parse exception
	 */
	public boolean upload(String url, String port, String[] auth, Path pathToFile, String targetDirectory,
			TitaVo titaVo) throws LogicException {

		this.setTitaVo(titaVo);

		JSch jsch = new JSch();
		Session session = null;

		try {
			// 建立連線
			session = jsch.getSession(auth[0], url, parse.stringToInteger(port));
			// 設定密碼
			session.setPassword(auth[1]);

			// 設定SSH連線的安全性設定
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// 連接到伺服器
			session.connect();

			// 創建SFTP通道
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			// 上傳檔案
			// 第一個參數是本地文件的路徑，第二個參數是遠端伺服器的目錄
			sftpChannel.put(pathToFile.toString(), targetDirectory);

			// 關閉連線
			sftpChannel.exit();
			session.disconnect();
		} catch (SftpException | JSchException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("SftpClient upload error = " + errors.toString());
			return false;
		}
		return true;
	}

	/**
	 * download
	 * 
	 * @param url        SFTP server url
	 * @param port       SFTP server port
	 * @param auth       username:password
	 * @param localFilePath  localFilePath
	 * @param remoteFile remoteFile
	 * @param titaVo     titaVo
	 * @return true when upload success else false
	 * @throws LogicException parse exception
	 */
	public boolean download(String url, String port, String[] auth, String localFilePath, String remoteFile, TitaVo titaVo)
			throws LogicException {

		this.setTitaVo(titaVo);

		JSch jsch = new JSch();
		Session session = null;

		try {
			// 建立連線
			session = jsch.getSession(auth[0], url, parse.stringToInteger(port));
			// 設定密碼
			session.setPassword(auth[1]);

			// 設定SSH連線的安全性設定
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// 連接到伺服器
			session.connect();

			// 創建SFTP通道
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			// 下載檔案
	        File localFile = new File(localFilePath);
	        if (!localFile.exists()) {
	            // 確保上層目錄存在
	        	localFile.getParentFile().mkdirs();
	            
	            try {
	                // 嘗試建立檔案
	            	localFile.createNewFile();
	            } catch (IOException e) {
	    			StringWriter errors = new StringWriter();
	    			e.printStackTrace(new PrintWriter(errors));
	    			this.error("SftpClient createNewFile error = " + errors.toString());
	    			return false;
	            }
	        }
			FileOutputStream fos = new FileOutputStream(localFile);
			sftpChannel.get(remoteFile, fos);

			// 關閉連線
			sftpChannel.exit();
			session.disconnect();
		} catch (SftpException | JSchException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("SftpClient download error = " + errors.toString());
			return false;
		} catch (FileNotFoundException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("SftpClient download FileNotFoundException error = " + errors.toString());
			return false;
		}
		return true;
	}

	@Override
	public void exec() {
		// use methods above instead!
	}
}
