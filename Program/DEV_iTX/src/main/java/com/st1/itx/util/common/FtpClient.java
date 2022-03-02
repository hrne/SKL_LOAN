package com.st1.itx.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.tradeService.CommBuffer;

/**
 * FtpClient
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Component("ftpClient")
@Scope("prototype")
public class FtpClient extends CommBuffer {

	private FTPClient ftp;
	private Boolean isSendingFile;

	/**
	 * 取得目前連接的 FTP 伺服器位置。<br>
	 * 未連接時，回傳 null。
	 * 
	 * @return IP
	 */
	public String getCurrentIP() {
		return this.isConnectionAlive() ? ftp.getRemoteAddress().getHostAddress() : null;
	}

	/**
	 * 取得目前連接狀態。
	 * 
	 * @return true 連接中；false 未連接
	 */
	public Boolean isConnectionAlive() {
		return ftp != null && ftp.isAvailable();
	}

	/**
	 * 連接至指定的 FTP<br>
	 * 如果原本有其他連接，會先關閉該連接再開新的
	 * 
	 * @param ip       該 FTP 的位置
	 * @param username 登入帳號
	 * @param password 登入密碼
	 */
	public void setConnection(String ip, String username, String password) {
		setConnection(ip, 21, username, password);
	}

	/**
	 * 連接至指定的 FTP<br>
	 * 如果原本有其他連接，會先關閉該連接再開新的
	 * 
	 * @param ip       該 FTP 的位置
	 * @param port     使用的連接埠
	 * @param username 登入帳號
	 * @param password 登入密碼
	 */
	public void setConnection(String ip, int port, String username, String password) {
		this.info("setConnection()");
		this.info("ip: " + ip);
		this.info("port: " + port);

		if (!closeConnection()) {
			this.error("Tried to set new connection but old connection is still ongoing!");
			this.error("OldConnection: " + this.getCurrentIP());
			return;
		}
		ftp = new FTPClient();

		try {
			ftp.connect(ip, port);
			this.info("Connecting to " + this.getCurrentIP());
			this.info(ftp.getReplyString());

			if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				this.info("Connected successfully.");
			} else {
				this.error("FTPServer refused connection.");
				this.closeConnection();
			}
			this.info("setConecttion() login success: " + ftp.login(username, password));
		} catch (Exception e) {
			this.error("error while setConnection()!");
			this.error(e.toString());
			this.closeConnection();
		}
	}

	/**
	 * 上傳指定的檔案至目前連接的 FTP
	 * 
	 * @param fileLocation 要傳送的檔案所在位置
	 */
	public void sendFile(String fileLocation) {
		File file = new File(fileLocation);

		if (file == null || !file.exists() || file.isDirectory()) {
			this.error("sendFile(): File is not viable: " + file.getAbsolutePath());
			return;
		}

		this.info("sending file: " + fileLocation);

		if (!this.isConnectionAlive()) {
			// you should do setConnection() first.
			this.error("... but the connection is not alive!");
			return;
		}

		isSendingFile = true;
		Boolean isSuccessful = false;
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			isSuccessful = ftp.storeFile(file.getName(), fileInputStream);
		} catch (Exception e) {
			this.error("sendFile Failed!");
			this.error(e.toString());
		}

		this.info("sendFile isSuccessful: " + isSuccessful + " - " + ftp.getReplyString());
		this.info("sendFile ending operation...");

		isSendingFile = false;
	}

	public Boolean closeConnection() {
		if (!this.isConnectionAlive())
			return true;

		if (this.isSendingFile) {
			this.error("Tried to closeConnection() but a file is still sending.");
			return false;
		}

		try {
			ftp.disconnect();
			return true;
		} catch (IOException e) {
			this.error("Error while disconnecting!");
			this.error(e.toString());
			return false;
		}
	}

	/**
	 * 傳送檔案至指定的 FTP 伺服器。<br>
	 * 會自己處理開啟與關閉連線。
	 * 
	 * @param ip           FTP 伺服器的位置
	 * @param username     登入帳號
	 * @param password     登入密碼
	 * @param fileLocation 完整的檔案位置
	 */
	public void sendFile(String ip, String username, String password, String fileLocation) {
		sendFile(ip, 21, username, password, fileLocation);
	}

	/**
	 * 傳送檔案至指定的 FTP 伺服器。<br>
	 * 會自己處理開啟與關閉連線。
	 * 
	 * @param ip           FTP 伺服器的位置
	 * @param port         連接埠
	 * @param username     登入帳號
	 * @param password     登入密碼
	 * @param fileLocation 完整的檔案位置
	 */
	public void sendFile(String ip, int port, String username, String password, String fileLocation) {
		this.setConnection(ip, port, username, password);
		this.sendFile(fileLocation);
		this.closeConnection();
	}

	@Override
	public void exec() {
		// use methods above instead!
	}
}
