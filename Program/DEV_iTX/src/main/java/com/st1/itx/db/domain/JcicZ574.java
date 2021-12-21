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
 * JcicZ574 更生款項統一收付結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ574`")
public class JcicZ574 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3844760027354755128L;

	@EmbeddedId
	private JcicZ574Id jcicZ574Id;

	// 交易代碼
	/* A:新增;C:異動;D:刪除 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 報送單位代號
	/* 3位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 申請日期
	@Column(name = "`ApplyDate`", insertable = false, updatable = false)
	private int applyDate = 0;

	// 結案日期
	@Column(name = "`CloseDate`")
	private int closeDate = 0;

	// 結案原因
	/*
	 * 01:債務人主動撤案02:債務人申請更生統收統付前未依約履行更生方案03:更生款項統一收復申請生效後債務人未依約履行04:Key值欄位輸入錯誤，
	 * 本行結案05:債權金融機構未全數回報債權99:主辦行停止辦理更生統一收付作業
	 */
	@Column(name = "`CloseMark`", length = 2)
	private String closeMark;

	// 通訊電話
	/* 限16碼 */
	@Column(name = "`PhoneNo`", length = 16)
	private String phoneNo;

	// 轉JCIC文字檔日期
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

	public JcicZ574Id getJcicZ574Id() {
		return this.jcicZ574Id;
	}

	public void setJcicZ574Id(JcicZ574Id jcicZ574Id) {
		this.jcicZ574Id = jcicZ574Id;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動;D:刪除
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增;C:異動;D:刪除
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
	 * 結案日期<br>
	 * 
	 * @return Integer
	 */
	public int getCloseDate() {
		return StaticTool.bcToRoc(this.closeDate);
	}

	/**
	 * 結案日期<br>
	 * 
	 *
	 * @param closeDate 結案日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCloseDate(int closeDate) throws LogicException {
		this.closeDate = StaticTool.rocToBc(closeDate);
	}

	/**
	 * 結案原因<br>
	 * 01:債務人主動撤案 02:債務人申請更生統收統付前未依約履行更生方案 03:更生款項統一收復申請生效後債務人未依約履行
	 * 04:Key值欄位輸入錯誤，本行結案 05:債權金融機構未全數回報債權 99:主辦行停止辦理更生統一收付作業
	 * 
	 * @return String
	 */
	public String getCloseMark() {
		return this.closeMark == null ? "" : this.closeMark;
	}

	/**
	 * 結案原因<br>
	 * 01:債務人主動撤案 02:債務人申請更生統收統付前未依約履行更生方案 03:更生款項統一收復申請生效後債務人未依約履行
	 * 04:Key值欄位輸入錯誤，本行結案 05:債權金融機構未全數回報債權 99:主辦行停止辦理更生統一收付作業
	 *
	 * @param closeMark 結案原因
	 */
	public void setCloseMark(String closeMark) {
		this.closeMark = closeMark;
	}

	/**
	 * 通訊電話<br>
	 * 限16碼
	 * 
	 * @return String
	 */
	public String getPhoneNo() {
		return this.phoneNo == null ? "" : this.phoneNo;
	}

	/**
	 * 通訊電話<br>
	 * 限16碼
	 *
	 * @param phoneNo 通訊電話
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
		return "JcicZ574 [jcicZ574Id=" + jcicZ574Id + ", tranKey=" + tranKey + ", closeDate=" + closeDate + ", closeMark=" + closeMark + ", phoneNo=" + phoneNo + ", outJcicTxtDate=" + outJcicTxtDate
				+ ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
