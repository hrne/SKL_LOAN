package com.st1.msw;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class UserInfo implements Serializable {
	/**
	 * UserInfo 的版本識別
	 */
	private static final long serialVersionUID = -2975115357253366861L;
	static final Logger logger = LoggerFactory.getLogger(UserInfo.class);
	String brno;
	String id;
	String name;
	String level;
	String httpSessionId;
	String dapKnd;
	String oapKnd;
	String cldept;
	String pswd;
	String ovrToken;
	int lastJnlSeq = -1;

	LinkedBlockingDeque<String> scriptSessionIdList = new LinkedBlockingDeque<String>();

	public UserInfo() {

	}

	public UserInfo(String brno, String id, String level, String name, String httpSessionId, String dapKnd, String oapKnd, String cldept, String pswd) {
		this.brno = brno;
		this.id = id;
		this.level = level;
		this.name = name;
		this.httpSessionId = httpSessionId;
		// 柯 新增主管授權時對交易的權限。
		this.dapKnd = dapKnd;
		this.oapKnd = oapKnd;
		this.cldept = cldept;
		this.pswd = pswd;
	}

	public String getFullId() {
		// return this.brno + this.id;
		return this.id;
	}

	public String toLine() {
		// key | value
		return this.brno + this.id + "|" + this.brno + "-" + this.id + "-" + this.level + "|" + this.name;
	}

	public void addScriptSessionId(String scriptSessionId) {
		logger.info("addScriptSessionId User " + this.brno + this.id + " scriptSessionId:" + scriptSessionId + " add !");
		// CTRL+F5時會ADD 故需要超過一定上限次數應該要移除最前面的陣列 (目前經測試應該可以容納25項目?)
		this.scriptSessionIdList.add(scriptSessionId);
		if (this.scriptSessionIdList.size() > 10) {
			logger.info("scriptSessionIdList length may too long,removeFirst.");
			this.scriptSessionIdList.removeFirst();
		}
	}

	public void removeScriptSessionId(String scriptSessionId) {
		boolean removed = this.scriptSessionIdList.remove(scriptSessionId);
		logger.info(FilterUtils.escape("removeScriptSessionId User " + this.brno + this.id + " scriptSessionId:" + scriptSessionId + " removed ? " + removed));
	}

	public String[] getScripSessions() {
		String[] ss = new String[0];
		return this.scriptSessionIdList.toArray(ss);
	}

	public String getLastSession() {
		String[] ss = getScripSessions();
		logger.info("getLastSession len:" + ss.length);
		if (ss.length > 0) {
			// logger.info("getLastSession:"+ss[ss.length - 1].toString());
			return ss[ss.length - 1];
		} else {
			return null;
		}
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

	public String getDapKnd() {
		return dapKnd;
	}

	public void setDapKnd(String dapKnd) {
		this.dapKnd = dapKnd;
	}

	public String getOapKnd() {
		return oapKnd;
	}

	public void setOapKnd(String oapKnd) {
		this.oapKnd = oapKnd;
	}

	public String getCldept() {
		return cldept;
	}

	public void setCldept(String cldept) {
		this.cldept = cldept;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getOvrToken() {
		return ovrToken;
	}

	public void setOvrToken(String ovrToken) {
		this.ovrToken = ovrToken;
	}

	public int getLastJnlSeq() {
		return lastJnlSeq;
	}

	public void setLastJnlSeq(int lastJnlSeq) {
		this.lastJnlSeq = lastJnlSeq;
	}

	public LinkedBlockingDeque<String> getScriptSessionIdList() {
		return scriptSessionIdList;
	}

	public void setScriptSessionIdList(LinkedBlockingDeque<String> scriptSessionIdList) {
		this.scriptSessionIdList = scriptSessionIdList;
	}

	@Override
	public String toString() {
		return "UserInfo [brno=" + brno + ", id=" + id + ", name=" + name + ", level=" + level + ", httpSessionId=" + httpSessionId + ", dapKnd=" + dapKnd + ", oapKnd=" + oapKnd + ", cldept=" + cldept
				+ ", pswd=" + pswd + ", ovrToken=" + ovrToken + ", lastJnlSeq=" + lastJnlSeq + ", scriptSessionIdList=" + scriptSessionIdList + "]";
	}

}
