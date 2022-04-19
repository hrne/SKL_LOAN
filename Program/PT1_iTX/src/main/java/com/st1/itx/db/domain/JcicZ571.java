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
 * JcicZ571 更生款項統一收付回報債權資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ571`")
public class JcicZ571 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8248275357129662009L;

	@EmbeddedId
	private JcicZ571Id jcicZ571Id;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 報送單位代號
	/* 3位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 受理款項統一收付之債權金融機構代號
	/* 3位文數字 */
	@Column(name = "`BankId`", length = 3, insertable = false, updatable = false)
	private String bankId;

	// 申請日期
	@Column(name = "`ApplyDate`", insertable = false, updatable = false)
	private int applyDate = 0;

	// 是否為更生債權人
	/* Y;NN:以下欄位皆為空白 */
	@Column(name = "`OwnerYn`", length = 1)
	private String ownerYn;

	// 債務人是否仍依更生方案正常還款予本金融機構
	/* Y;N若本次[參與分配債權金額]為0者,本欄位談報為"Y" */
	@Column(name = "`PayYn`", length = 1)
	private String payYn;

	// 本金融機構更生債權總金額
	@Column(name = "`OwnerAmt`")
	private int ownerAmt = 0;

	// 參與分配債權金額
	@Column(name = "`AllotAmt`")
	private int allotAmt = 0;

	// 未參與分配債權金額
	@Column(name = "`UnallotAmt`")
	private int unallotAmt = 0;

	// 轉出JCIC文字檔日期
	@Column(name = "`OutJcicTxtDate`")
	private int outJcicTxtDate = 0;

	// 流水號
	@Column(name = "`Ukey`", length = 32)
	private String ukey;

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

	public JcicZ571Id getJcicZ571Id() {
		return this.jcicZ571Id;
	}

	public void setJcicZ571Id(JcicZ571Id jcicZ571Id) {
		this.jcicZ571Id = jcicZ571Id;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動
	 *
	 * @param tranKey 交易代碼
	 */
	public void setTranKey(String tranKey) {
		this.tranKey = tranKey;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 報送單位代號<br>
	 * 3位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 3位文數字
	 *
	 * @param submitKey 報送單位代號
	 */
	public void setSubmitKey(String submitKey) {
		this.submitKey = submitKey;
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 3位文數字
	 * 
	 * @return String
	 */
	public String getBankId() {
		return this.bankId == null ? "" : this.bankId;
	}

	/**
	 * 受理款項統一收付之債權金融機構代號<br>
	 * 3位文數字
	 *
	 * @param bankId 受理款項統一收付之債權金融機構代號
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	/**
	 * 申請日期<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 申請日期<br>
	 * 
	 *
	 * @param applyDate 申請日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 是否為更生債權人<br>
	 * Y;N N:以下欄位皆為空白
	 * 
	 * @return String
	 */
	public String getOwnerYn() {
		return this.ownerYn == null ? "" : this.ownerYn;
	}

	/**
	 * 是否為更生債權人<br>
	 * Y;N N:以下欄位皆為空白
	 *
	 * @param ownerYn 是否為更生債權人
	 */
	public void setOwnerYn(String ownerYn) {
		this.ownerYn = ownerYn;
	}

	/**
	 * 債務人是否仍依更生方案正常還款予本金融機構<br>
	 * Y;N 若本次[參與分配債權金額]為0者,本欄位談報為"Y"
	 * 
	 * @return String
	 */
	public String getPayYn() {
		return this.payYn == null ? "" : this.payYn;
	}

	/**
	 * 債務人是否仍依更生方案正常還款予本金融機構<br>
	 * Y;N 若本次[參與分配債權金額]為0者,本欄位談報為"Y"
	 *
	 * @param payYn 債務人是否仍依更生方案正常還款予本金融機構
	 */
	public void setPayYn(String payYn) {
		this.payYn = payYn;
	}

	/**
	 * 本金融機構更生債權總金額<br>
	 * 
	 * @return Integer
	 */
	public int getOwnerAmt() {
		return this.ownerAmt;
	}

	/**
	 * 本金融機構更生債權總金額<br>
	 * 
	 *
	 * @param ownerAmt 本金融機構更生債權總金額
	 */
	public void setOwnerAmt(int ownerAmt) {
		this.ownerAmt = ownerAmt;
	}

	/**
	 * 參與分配債權金額<br>
	 * 
	 * @return Integer
	 */
	public int getAllotAmt() {
		return this.allotAmt;
	}

	/**
	 * 參與分配債權金額<br>
	 * 
	 *
	 * @param allotAmt 參與分配債權金額
	 */
	public void setAllotAmt(int allotAmt) {
		this.allotAmt = allotAmt;
	}

	/**
	 * 未參與分配債權金額<br>
	 * 
	 * @return Integer
	 */
	public int getUnallotAmt() {
		return this.unallotAmt;
	}

	/**
	 * 未參與分配債權金額<br>
	 * 
	 *
	 * @param unallotAmt 未參與分配債權金額
	 */
	public void setUnallotAmt(int unallotAmt) {
		this.unallotAmt = unallotAmt;
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 * @return Integer
	 */
	public int getOutJcicTxtDate() {
		return StaticTool.bcToRoc(this.outJcicTxtDate);
	}

	/**
	 * 轉出JCIC文字檔日期<br>
	 * 
	 *
	 * @param outJcicTxtDate 轉出JCIC文字檔日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
		this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
		return "JcicZ571 [jcicZ571Id=" + jcicZ571Id + ", tranKey=" + tranKey + ", ownerYn=" + ownerYn + ", payYn=" + payYn + ", ownerAmt=" + ownerAmt + ", allotAmt=" + allotAmt + ", unallotAmt="
				+ unallotAmt + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
