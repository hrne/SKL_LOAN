package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ444Log 前置調解債務人基本資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ444Log`")
public class JcicZ444Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4905106607245307283L;

	@EmbeddedId
	private JcicZ444LogId jcicZ444LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增;C:異動;D:刪除;X:補件 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人戶籍之郵遞區號及地址
	/* 38個中文 */
	@Column(name = "`CustRegAddr`", length = 76)
	private String custRegAddr;

	// 債務人通訊地之郵遞區號及地址
	/* 38個中文 */
	@Column(name = "`CustComAddr`", length = 76)
	private String custComAddr;

	// 債務人戶籍電話
	@Column(name = "`CustRegTelNo`", length = 16)
	private String custRegTelNo;

	// 債務人通訊電話
	@Column(name = "`CustComTelNo`", length = 16)
	private String custComTelNo;

	// 債務人行動電話
	@Column(name = "`CustMobilNo`", length = 16)
	private String custMobilNo;

	// 轉JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

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

	public JcicZ444LogId getJcicZ444LogId() {
		return this.jcicZ444LogId;
	}

	public void setJcicZ444LogId(JcicZ444LogId jcicZ444LogId) {
		this.jcicZ444LogId = jcicZ444LogId;
	}

	/**
	 * 流水號<br>
	 * 
	 * @return String
	 */
	public String getUkey() {
		return this.ukey == null ? "" : this.ukey;
	}

	/**
	 * 流水號<br>
	 * 
	 *
	 * @param ukey 流水號
	 */
	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxSeq() {
		return this.txSeq == null ? "" : this.txSeq;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param txSeq 交易序號
	 */
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動;D:刪除;X:補件
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動;D:刪除;X:補件
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 債務人戶籍之郵遞區號及地址<br>
	 * 38個中文
	 * 
	 * @return String
	 */
	public String getCustRegAddr() {
		return this.custRegAddr == null ? "" : this.custRegAddr;
	}

	/**
	 * 債務人戶籍之郵遞區號及地址<br>
	 * 38個中文
	 *
	 * @param custRegAddr 債務人戶籍之郵遞區號及地址
	 */
	public void setCustRegAddr(String custRegAddr) {
		this.custRegAddr = custRegAddr;
	}

	/**
	 * 債務人通訊地之郵遞區號及地址<br>
	 * 38個中文
	 * 
	 * @return String
	 */
	public String getCustComAddr() {
		return this.custComAddr == null ? "" : this.custComAddr;
	}

	/**
	 * 債務人通訊地之郵遞區號及地址<br>
	 * 38個中文
	 *
	 * @param custComAddr 債務人通訊地之郵遞區號及地址
	 */
	public void setCustComAddr(String custComAddr) {
		this.custComAddr = custComAddr;
	}

	/**
	 * 債務人戶籍電話<br>
	 * 
	 * @return String
	 */
	public String getCustRegTelNo() {
		return this.custRegTelNo == null ? "" : this.custRegTelNo;
	}

	/**
	 * 債務人戶籍電話<br>
	 * 
	 *
	 * @param custRegTelNo 債務人戶籍電話
	 */
	public void setCustRegTelNo(String custRegTelNo) {
		this.custRegTelNo = custRegTelNo;
	}

	/**
	 * 債務人通訊電話<br>
	 * 
	 * @return String
	 */
	public String getCustComTelNo() {
		return this.custComTelNo == null ? "" : this.custComTelNo;
	}

	/**
	 * 債務人通訊電話<br>
	 * 
	 *
	 * @param custComTelNo 債務人通訊電話
	 */
	public void setCustComTelNo(String custComTelNo) {
		this.custComTelNo = custComTelNo;
	}

	/**
	 * 債務人行動電話<br>
	 * 
	 * @return String
	 */
	public String getCustMobilNo() {
		return this.custMobilNo == null ? "" : this.custMobilNo;
	}

	/**
	 * 債務人行動電話<br>
	 * 
	 *
	 * @param custMobilNo 債務人行動電話
	 */
	public void setCustMobilNo(String custMobilNo) {
		this.custMobilNo = custMobilNo;
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
		return "JcicZ444Log [jcicZ444LogId=" + jcicZ444LogId + ", tranKey=" + tranKey + ", custRegAddr=" + custRegAddr + ", custComAddr=" + custComAddr + ", custRegTelNo=" + custRegTelNo
				+ ", custComTelNo=" + custComTelNo + ", custMobilNo=" + custMobilNo + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
