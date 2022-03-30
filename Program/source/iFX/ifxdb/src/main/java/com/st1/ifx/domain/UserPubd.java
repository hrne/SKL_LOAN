package com.st1.ifx.domain;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FX_USERPUB")
public class UserPubd implements Serializable {

	private static final long serialVersionUID = -6045922124148658147L;

	// SessionId scriptSessionId 是否有要從USERINFO移出來獨立一個欄位?

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_USERPUB_SEQ")
	@SequenceGenerator(name = "FX_USERPUB_SEQ", sequenceName = "FX_USERPUB_SEQ", allocationSize = 1)
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// 目前固定值USERS
	@Column(name = "TABLENM")
	private String tableName;

	// DB2 -> SQL SERVER 記得也要把schema = "IFX" 移除
	// DB2 -> SQL SERVER 不能有這個關鍵字 KEY -> D_KEY
	@Column(name = "XEY")
	private String xey;

	// NEW 4
	@Column(name = "BRNO")
	private String brno;

	// NEW 1
	@Column(name = "LVEL")
	private String lvel;

	// NEW 10
	@Column(name = "NAME")
	private String name;

	// NEW 40
	@Column(name = "HTTPSESSIONID")
	private String httpSessionId;

	// NEW 60
	@Column(name = "SCRIPTSESSIONID")
	private String scriptSessionId;

	// NEW 30
	@Column(name = "DAPKND")
	private String dapKnd;

	// NEW 30
	@Column(name = "OAPKND")
	private String oapKnd;

	@Column(name = "CLDEPT")
	private String cldept;

	// NEW 60
	@Column(name = "PWDD")
	private String pwdd;

	// NEW use? 30
	@Column(name = "OVRTOKEN")
	private String ovrToken;

	// NEW
	@Column(name = "LASTJNLSEQ")
	private int lastJnlSeq;

	@Column(name = "DATED")
	private java.sql.Date dated;

	@Column(name = "TIME")
	private java.sql.Time time;

	@Column(name = "LOCATE")
	private String locate;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "USERINFO")
	private String userInfo;

	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.setDated(new java.sql.Date(t));
		this.setTime(new Time(t));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getXey() {
		return xey;
	}

	public void setXey(String xey) {
		this.xey = xey;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getLvel() {
		return lvel;
	}

	public void setLvel(String lvel) {
		this.lvel = lvel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}

	public String getScriptSessionId() {
		return scriptSessionId;
	}

	public void setScriptSessionId(String scriptSessionId) {
		this.scriptSessionId = scriptSessionId;
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

	public String getPwdd() {
		return pwdd;
	}

	public void setPwdd(String pwdd) {
		this.pwdd = pwdd;
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

	public java.sql.Date getDated() {
		return dated;
	}

	public void setDated(java.sql.Date dated) {
		this.dated = dated;
	}

	public java.sql.Time getTime() {
		return time;
	}

	public void setTime(java.sql.Time time) {
		this.time = time;
	}

	public String getLocate() {
		return locate;
	}

	public void setLocate(String locate) {
		this.locate = locate;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String toRtnstring() {
		return xey + "|" + xey.substring(0, 4) + "-" + xey.substring(4, 6) + "-" + lvel + "|" + name;
	}

	@Override
	public String toString() {
		return "UserPubd [id=" + id + ", tableName=" + tableName + ", xey=" + xey + ", brno=" + brno + ", lvel=" + lvel + ", name=" + name + ", httpSessionId=" + httpSessionId + ", scriptSessionId="
				+ scriptSessionId + ", dapKnd=" + dapKnd + ", oapKnd=" + oapKnd + ", cldept=" + cldept + ", pwdd=" + pwdd + ", ovrToken=" + ovrToken + ", lastJnlSeq=" + lastJnlSeq + ", dated=" + dated
				+ ", time=" + time + ", locate=" + locate + ", userInfo=" + userInfo + "]";
	}

}
