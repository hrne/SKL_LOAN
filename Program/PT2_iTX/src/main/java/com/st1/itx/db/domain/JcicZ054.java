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
 * JcicZ054 單獨全數受清償資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ054`")
public class JcicZ054 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5417077171127902703L;

	@EmbeddedId
	private JcicZ054Id jcicZ054Id;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 協商申請日
	@Column(name = "`RcDate`", insertable = false, updatable = false)
	private int rcDate = 0;

	// 最大債權金融機構代號
	/* 三位文數字 */
	@Column(name = "`MaxMainCode`", length = 3, insertable = false, updatable = false)
	private String maxMainCode;

	// 單獨全數受清償原因
	/*
	 * A：於協商前已聲請強制執行並獲分配之款項，於日後領取分配款者B：債務人於最高限額抵押權內清償無擔保債務C：保證人代為清償債務D：
	 * 廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務;E：車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 */
	@Column(name = "`PayOffResult`", length = 1)
	private String payOffResult;

	// 單獨全數受清償日期
	/* 指單獨受償金融機構實際之受清償日期 */
	@Column(name = "`PayOffDate`", insertable = false, updatable = false)
	private int payOffDate = 0;

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

	public JcicZ054Id getJcicZ054Id() {
		return this.jcicZ054Id;
	}

	public void setJcicZ054Id(JcicZ054Id jcicZ054Id) {
		this.jcicZ054Id = jcicZ054Id;
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
	 * 協商申請日<br>
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 協商申請日<br>
	 * 
	 *
	 * @param rcDate 協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 最大債權金融機構代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getMaxMainCode() {
		return this.maxMainCode == null ? "" : this.maxMainCode;
	}

	/**
	 * 最大債權金融機構代號<br>
	 * 三位文數字
	 *
	 * @param maxMainCode 最大債權金融機構代號
	 */
	public void setMaxMainCode(String maxMainCode) {
		this.maxMainCode = maxMainCode;
	}

	/**
	 * 單獨全數受清償原因<br>
	 * A：於協商前已聲請強制執行並獲分配之款項，於日後領取分配款者 B：債務人於最高限額抵押權內清償無擔保債務 C：保證人代為清償債務
	 * D：廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務; E：車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 * 
	 * @return String
	 */
	public String getPayOffResult() {
		return this.payOffResult == null ? "" : this.payOffResult;
	}

	/**
	 * 單獨全數受清償原因<br>
	 * A：於協商前已聲請強制執行並獲分配之款項，於日後領取分配款者 B：債務人於最高限額抵押權內清償無擔保債務 C：保證人代為清償債務
	 * D：廠商將分期付款產品之款項退回貸款金融機構，並沖抵貸款金融機構債務; E：車貸及次順位不動產抵押權經債權金融機構處分後收回款項並沖抵貸款金融機構債務
	 *
	 * @param payOffResult 單獨全數受清償原因
	 */
	public void setPayOffResult(String payOffResult) {
		this.payOffResult = payOffResult;
	}

	/**
	 * 單獨全數受清償日期<br>
	 * 指單獨受償金融機構實際之受清償日期
	 * 
	 * @return Integer
	 */
	public int getPayOffDate() {
		return StaticTool.bcToRoc(this.payOffDate);
	}

	/**
	 * 單獨全數受清償日期<br>
	 * 指單獨受償金融機構實際之受清償日期
	 *
	 * @param payOffDate 單獨全數受清償日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setPayOffDate(int payOffDate) throws LogicException {
		this.payOffDate = StaticTool.rocToBc(payOffDate);
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
		return "JcicZ054 [jcicZ054Id=" + jcicZ054Id + ", tranKey=" + tranKey + ", payOffResult=" + payOffResult + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
