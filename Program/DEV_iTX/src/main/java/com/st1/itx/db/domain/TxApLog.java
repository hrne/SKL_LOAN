package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxApLog ApLog敏感資料查詢紀錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxApLog`")
public class TxApLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3339527689296505281L;

	// 序號
	@Id
	@Column(name = "`AutoSeq`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxApLog_SEQ`")
	@SequenceGenerator(name = "`TxApLog_SEQ`", sequenceName = "`TxApLog_SEQ`", allocationSize = 1)
	private Long autoSeq = 0L;

	// 查詢日
	@Column(name = "`Entdy`")
	private int entdy = 0;

	// 員工編號
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo;

	// 動作
	@Column(name = "`Act`", length = 10)
	private String act;

	// 動作時間
	@Column(name = "`ActTime`", length = 8)
	private String actTime;

	// 使用著IP位置
	@Column(name = "`Ip`", length = 14)
	private String ip;

	// 系統名稱
	@Column(name = "`SystemName`", length = 20)
	private String systemName;

	// 伺服器IP
	@Column(name = "`ServerIp`", length = 15)
	private String serverIp;

	// 伺服器名稱
	@Column(name = "`ServerName`", length = 20)
	private String serverName;

	// 作業名稱
	@Column(name = "`ActName`", length = 50)
	private String actName;

	// 程式名稱
	@Column(name = "`PgName`", length = 50)
	private String pgName;

	// 方法名稱
	@Column(name = "`MethodName`", length = 50)
	private String methodName;

	// 輸入參數
	@Column(name = "`InParam`", length = 3000)
	private String inParam;

	// 執行結果
	@Column(name = "`ResultStatus`", length = 1)
	private String resultStatus;

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	/**
	 * 序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAutoSeq() {
		return this.autoSeq;
	}

	/**
	 * 序號<br>
	 * 
	 *
	 * @param autoSeq 序號
	 */
	public void setAutoSeq(Long autoSeq) {
		this.autoSeq = autoSeq;
	}

	/**
	 * 查詢日<br>
	 * 
	 * @return Integer
	 */
	public int getEntdy() {
		return StaticTool.bcToRoc(this.entdy);
	}

	/**
	 * 查詢日<br>
	 * 
	 *
	 * @param entdy 查詢日
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntdy(int entdy) throws LogicException {
		this.entdy = StaticTool.rocToBc(entdy);
	}

	/**
	 * 員工編號<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 員工編號<br>
	 * 
	 *
	 * @param tlrNo 員工編號
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
	}

	/**
	 * 動作<br>
	 * 
	 * @return String
	 */
	public String getAct() {
		return this.act == null ? "" : this.act;
	}

	/**
	 * 動作<br>
	 * 
	 *
	 * @param act 動作
	 */
	public void setAct(String act) {
		this.act = act;
	}

	/**
	 * 動作時間<br>
	 * 
	 * @return String
	 */
	public String getActTime() {
		return this.actTime == null ? "" : this.actTime;
	}

	/**
	 * 動作時間<br>
	 * 
	 *
	 * @param actTime 動作時間
	 */
	public void setActTime(String actTime) {
		this.actTime = actTime;
	}

	/**
	 * 使用著IP位置<br>
	 * 
	 * @return String
	 */
	public String getIp() {
		return this.ip == null ? "" : this.ip;
	}

	/**
	 * 使用著IP位置<br>
	 * 
	 *
	 * @param ip 使用著IP位置
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 系統名稱<br>
	 * 
	 * @return String
	 */
	public String getSystemName() {
		return this.systemName == null ? "" : this.systemName;
	}

	/**
	 * 系統名稱<br>
	 * 
	 *
	 * @param systemName 系統名稱
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * 伺服器IP<br>
	 * 
	 * @return String
	 */
	public String getServerIp() {
		return this.serverIp == null ? "" : this.serverIp;
	}

	/**
	 * 伺服器IP<br>
	 * 
	 *
	 * @param serverIp 伺服器IP
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * 伺服器名稱<br>
	 * 
	 * @return String
	 */
	public String getServerName() {
		return this.serverName == null ? "" : this.serverName;
	}

	/**
	 * 伺服器名稱<br>
	 * 
	 *
	 * @param serverName 伺服器名稱
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * 作業名稱<br>
	 * 
	 * @return String
	 */
	public String getActName() {
		return this.actName == null ? "" : this.actName;
	}

	/**
	 * 作業名稱<br>
	 * 
	 *
	 * @param actName 作業名稱
	 */
	public void setActName(String actName) {
		this.actName = actName;
	}

	/**
	 * 程式名稱<br>
	 * 
	 * @return String
	 */
	public String getPgName() {
		return this.pgName == null ? "" : this.pgName;
	}

	/**
	 * 程式名稱<br>
	 * 
	 *
	 * @param pgName 程式名稱
	 */
	public void setPgName(String pgName) {
		this.pgName = pgName;
	}

	/**
	 * 方法名稱<br>
	 * 
	 * @return String
	 */
	public String getMethodName() {
		return this.methodName == null ? "" : this.methodName;
	}

	/**
	 * 方法名稱<br>
	 * 
	 *
	 * @param methodName 方法名稱
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 輸入參數<br>
	 * 
	 * @return String
	 */
	public String getInParam() {
		return this.inParam == null ? "" : this.inParam;
	}

	/**
	 * 輸入參數<br>
	 * 
	 *
	 * @param inParam 輸入參數
	 */
	public void setInParam(String inParam) {
		this.inParam = inParam;
	}

	/**
	 * 執行結果<br>
	 * 
	 * @return String
	 */
	public String getResultStatus() {
		return this.resultStatus == null ? "" : this.resultStatus;
	}

	/**
	 * 執行結果<br>
	 * 
	 *
	 * @param resultStatus 執行結果
	 */
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "TxApLog [autoSeq=" + autoSeq + ", entdy=" + entdy + ", tlrNo=" + tlrNo + ", act=" + act + ", actTime=" + actTime + ", ip=" + ip + ", systemName=" + systemName + ", serverIp="
				+ serverIp + ", serverName=" + serverName + ", actName=" + actName + ", pgName=" + pgName + ", methodName=" + methodName + ", inParam=" + inParam + ", resultStatus=" + resultStatus
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
