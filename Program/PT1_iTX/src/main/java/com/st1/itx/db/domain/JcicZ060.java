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
 * JcicZ060 債務人繳款資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ060`")
public class JcicZ060 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 175144295980566884L;

	@EmbeddedId
	private JcicZ060Id jcicZ060Id;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 原前置協商申請日
	/* 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日) */
	@Column(name = "`RcDate`", insertable = false, updatable = false)
	private int rcDate = 0;

	// 申請變更還款條件日
	@Column(name = "`ChangePayDate`", insertable = false, updatable = false)
	private int changePayDate = 0;

	// 已清分足月期付金年月
	/* YYYYMM */
	@Column(name = "`YM`")
	private int yM = 0;

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

	public JcicZ060Id getJcicZ060Id() {
		return this.jcicZ060Id;
	}

	public void setJcicZ060Id(JcicZ060Id jcicZ060Id) {
		this.jcicZ060Id = jcicZ060Id;
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
	 * 報送單位代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 *
	 * @param submitKey 報送單位代號
	 */
	public void setSubmitKey(String submitKey) {
		this.submitKey = submitKey;
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
	 * 原前置協商申請日<br>
	 * 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日)
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 原前置協商申請日<br>
	 * 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日)
	 *
	 * @param rcDate 原前置協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 申請變更還款條件日<br>
	 * 
	 * @return Integer
	 */
	public int getChangePayDate() {
		return StaticTool.bcToRoc(this.changePayDate);
	}

	/**
	 * 申請變更還款條件日<br>
	 * 
	 *
	 * @param changePayDate 申請變更還款條件日
	 * @throws LogicException when Date Is Warn
	 */
	public void setChangePayDate(int changePayDate) throws LogicException {
		this.changePayDate = StaticTool.rocToBc(changePayDate);
	}

	/**
	 * 已清分足月期付金年月<br>
	 * YYYYMM
	 * 
	 * @return Integer
	 */
	public int getYM() {
		return this.yM;
	}

	/**
	 * 已清分足月期付金年月<br>
	 * YYYYMM
	 *
	 * @param yM 已清分足月期付金年月
	 */
	public void setYM(int yM) {
		this.yM = yM;
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
		return "JcicZ060 [jcicZ060Id=" + jcicZ060Id + ", tranKey=" + tranKey + ", yM=" + yM + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
