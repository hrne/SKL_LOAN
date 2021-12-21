package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * JcicZ448Log 前置調解無擔保債務還款分配資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ448Log`")
public class JcicZ448Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1488901103925354042L;

	@EmbeddedId
	private JcicZ448LogId jcicZ448LogId;

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

	// 簽約金額-本金
	@Column(name = "`SignPrin`")
	private int signPrin = 0;

	// 簽約金額-利息、違約金及其他費用
	@Column(name = "`SignOther`")
	private int signOther = 0;

	// 債權比例
	/* XXX.XX */
	@Column(name = "`OwnPercentage`")
	private BigDecimal ownPercentage = new BigDecimal("0");

	// 每月清償金額
	@Column(name = "`AcQuitAmt`")
	private int acQuitAmt = 0;

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

	public JcicZ448LogId getJcicZ448LogId() {
		return this.jcicZ448LogId;
	}

	public void setJcicZ448LogId(JcicZ448LogId jcicZ448LogId) {
		this.jcicZ448LogId = jcicZ448LogId;
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
	 * 簽約金額-本金<br>
	 * 
	 * @return Integer
	 */
	public int getSignPrin() {
		return this.signPrin;
	}

	/**
	 * 簽約金額-本金<br>
	 * 
	 *
	 * @param signPrin 簽約金額-本金
	 */
	public void setSignPrin(int signPrin) {
		this.signPrin = signPrin;
	}

	/**
	 * 簽約金額-利息、違約金及其他費用<br>
	 * 
	 * @return Integer
	 */
	public int getSignOther() {
		return this.signOther;
	}

	/**
	 * 簽約金額-利息、違約金及其他費用<br>
	 * 
	 *
	 * @param signOther 簽約金額-利息、違約金及其他費用
	 */
	public void setSignOther(int signOther) {
		this.signOther = signOther;
	}

	/**
	 * 債權比例<br>
	 * XXX.XX
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOwnPercentage() {
		return this.ownPercentage;
	}

	/**
	 * 債權比例<br>
	 * XXX.XX
	 *
	 * @param ownPercentage 債權比例
	 */
	public void setOwnPercentage(BigDecimal ownPercentage) {
		this.ownPercentage = ownPercentage;
	}

	/**
	 * 每月清償金額<br>
	 * 
	 * @return Integer
	 */
	public int getAcQuitAmt() {
		return this.acQuitAmt;
	}

	/**
	 * 每月清償金額<br>
	 * 
	 *
	 * @param acQuitAmt 每月清償金額
	 */
	public void setAcQuitAmt(int acQuitAmt) {
		this.acQuitAmt = acQuitAmt;
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
		return "JcicZ448Log [jcicZ448LogId=" + jcicZ448LogId + ", tranKey=" + tranKey + ", signPrin=" + signPrin + ", signOther=" + signOther + ", ownPercentage=" + ownPercentage + ", acQuitAmt="
				+ acQuitAmt + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
