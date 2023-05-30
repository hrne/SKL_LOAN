package com.st1.itx.util.common.data;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("smsDataVo")
@Scope("prototype")
public class SmsDataVo extends LinkedHashMap<String, String> {
	/**
		 * 
		 */
	private static final long serialVersionUID = 4827193178166707921L;

	private static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";

	private String workId = "SKL10H000"; // 簡訊代碼/簡訊帳號
	private String smsp = "SKL10H000"; // 簡訊密碼
	private int type = 2; // 發送型態(即時/預約/即時API)
	private String mobile = ""; // 客戶手機
	private String msgContent = ""; // 簡訊內容
	private String schduleTime = ""; // 簡訊預約時間

	/**
	 * @return the workId
	 */
	public String getWorkId() {
		return workId;
	}

	/**
	 * @return the smsp
	 */
	public String getSmsp() {
		return smsp;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @return the msgContent
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * @return the schduleTime
	 */
	public String getSchduleTime() {
		return schduleTime;
	}

	/**
	 * @param workId the workId to set
	 */
	public void setWorkId(String workId) {
		this.workId = workId;
	}

	/**
	 * @param smsp the smsp to set
	 */
	public void setSmsp(String smsp) {
		this.smsp = smsp;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @param msgContent the msg_content to set
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	/**
	 * @param schduleTime the schduleTime to set
	 */
	public void setSchduleTime(String schduleTime) {
		this.schduleTime = schduleTime;
	}

	public String getJsonString() throws Exception {
		String jsonString = "";
		JSONObject smsData = new JSONObject();
		smsData.put("Work_ID", this.workId);
		smsData.put("SMSPWD", this.smsp);
		smsData.put("Type", this.type);
		smsData.put("mobile", this.mobile);
		smsData.put("msg_content", this.msgContent);
		smsData.put("ScheduleTime", this.schduleTime);
		jsonString = smsData.toString();
		jsonString = encrypt(jsonString, "Q)Esdf0#wejNCAht", "Q)Esdf0#wejNCAht");

		return jsonString;
	}

	public String getDecryptJsonString(String encryptJsonString) throws Exception {
		return decrypt(encryptJsonString, "Q)Esdf0#wejNCAht", "Q)Esdf0#wejNCAht");
	}

	private String encrypt(String data, String key, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
		byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	private String decrypt(String encryptedData, String key, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8));
		cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
		byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedData, StandardCharsets.UTF_8);
	}
}
