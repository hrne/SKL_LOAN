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
 * JcicZ440Log 前置調解受理申請暨請求回報債權通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ440Log`")
public class JcicZ440Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4904483989484416774L;

	@EmbeddedId
	private JcicZ440LogId jcicZ440LogId;

	// 流水號
	@Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
	private String ukey;

	// 交易序號
	@Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
	private String txSeq;

	// 交易代碼
	/* A:新增;C:異動 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 同意書取得日期
	@Column(name = "`AgreeDate`")
	private int agreeDate = 0;

	// 首次調解日
	@Column(name = "`StartDate`")
	private int startDate = 0;

	// 債權計算基準日
	@Column(name = "`RemindDate`")
	private int remindDate = 0;

	// 受理方式
	/* 1:法院調解2:鄉鎮市區調解委員會調解 */
	@Column(name = "`ApplyType`", length = 1)
	private String applyType;

	// 協辦行是否需自行回報債權
	/* Y;N */
	@Column(name = "`ReportYn`", length = 1)
	private String reportYn;

	// 未揭露債權機構代號1
	@Column(name = "`NotBankId1`", length = 3)
	private String notBankId1;

	// 未揭露債權機構代號2
	@Column(name = "`NotBankId2`", length = 3)
	private String notBankId2;

	// 未揭露債權機構代號3
	@Column(name = "`NotBankId3`", length = 3)
	private String notBankId3;

	// 未揭露債權機構代號4
	@Column(name = "`NotBankId4`", length = 3)
	private String notBankId4;

	// 未揭露債權機構代號5
	@Column(name = "`NotBankId5`", length = 3)
	private String notBankId5;

	// 未揭露債權機構代號6
	@Column(name = "`NotBankId6`", length = 3)
	private String notBankId6;

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

	public JcicZ440LogId getJcicZ440LogId() {
		return this.jcicZ440LogId;
	}

	public void setJcicZ440LogId(JcicZ440LogId jcicZ440LogId) {
		this.jcicZ440LogId = jcicZ440LogId;
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
	 * 同意書取得日期<br>
	 * 
	 * @return Integer
	 */
	public int getAgreeDate() {
		return StaticTool.bcToRoc(this.agreeDate);
	}

	/**
	 * 同意書取得日期<br>
	 * 
	 *
	 * @param agreeDate 同意書取得日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAgreeDate(int agreeDate) throws LogicException {
		this.agreeDate = StaticTool.rocToBc(agreeDate);
	}

	/**
	 * 首次調解日<br>
	 * 
	 * @return Integer
	 */
	public int getStartDate() {
		return StaticTool.bcToRoc(this.startDate);
	}

	/**
	 * 首次調解日<br>
	 * 
	 *
	 * @param startDate 首次調解日
	 * @throws LogicException when Date Is Warn
	 */
	public void setStartDate(int startDate) throws LogicException {
		this.startDate = StaticTool.rocToBc(startDate);
	}

	/**
	 * 債權計算基準日<br>
	 * 
	 * @return Integer
	 */
	public int getRemindDate() {
		return StaticTool.bcToRoc(this.remindDate);
	}

	/**
	 * 債權計算基準日<br>
	 * 
	 *
	 * @param remindDate 債權計算基準日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRemindDate(int remindDate) throws LogicException {
		this.remindDate = StaticTool.rocToBc(remindDate);
	}

	/**
	 * 受理方式<br>
	 * 1:法院調解 2:鄉鎮市區調解委員會調解
	 * 
	 * @return String
	 */
	public String getApplyType() {
		return this.applyType == null ? "" : this.applyType;
	}

	/**
	 * 受理方式<br>
	 * 1:法院調解 2:鄉鎮市區調解委員會調解
	 *
	 * @param applyType 受理方式
	 */
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	/**
	 * 協辦行是否需自行回報債權<br>
	 * Y;N
	 * 
	 * @return String
	 */
	public String getReportYn() {
		return this.reportYn == null ? "" : this.reportYn;
	}

	/**
	 * 協辦行是否需自行回報債權<br>
	 * Y;N
	 *
	 * @param reportYn 協辦行是否需自行回報債權
	 */
	public void setReportYn(String reportYn) {
		this.reportYn = reportYn;
	}

	/**
	 * 未揭露債權機構代號1<br>
	 * 
	 * @return String
	 */
	public String getNotBankId1() {
		return this.notBankId1 == null ? "" : this.notBankId1;
	}

	/**
	 * 未揭露債權機構代號1<br>
	 * 
	 *
	 * @param notBankId1 未揭露債權機構代號1
	 */
	public void setNotBankId1(String notBankId1) {
		this.notBankId1 = notBankId1;
	}

	/**
	 * 未揭露債權機構代號2<br>
	 * 
	 * @return String
	 */
	public String getNotBankId2() {
		return this.notBankId2 == null ? "" : this.notBankId2;
	}

	/**
	 * 未揭露債權機構代號2<br>
	 * 
	 *
	 * @param notBankId2 未揭露債權機構代號2
	 */
	public void setNotBankId2(String notBankId2) {
		this.notBankId2 = notBankId2;
	}

	/**
	 * 未揭露債權機構代號3<br>
	 * 
	 * @return String
	 */
	public String getNotBankId3() {
		return this.notBankId3 == null ? "" : this.notBankId3;
	}

	/**
	 * 未揭露債權機構代號3<br>
	 * 
	 *
	 * @param notBankId3 未揭露債權機構代號3
	 */
	public void setNotBankId3(String notBankId3) {
		this.notBankId3 = notBankId3;
	}

	/**
	 * 未揭露債權機構代號4<br>
	 * 
	 * @return String
	 */
	public String getNotBankId4() {
		return this.notBankId4 == null ? "" : this.notBankId4;
	}

	/**
	 * 未揭露債權機構代號4<br>
	 * 
	 *
	 * @param notBankId4 未揭露債權機構代號4
	 */
	public void setNotBankId4(String notBankId4) {
		this.notBankId4 = notBankId4;
	}

	/**
	 * 未揭露債權機構代號5<br>
	 * 
	 * @return String
	 */
	public String getNotBankId5() {
		return this.notBankId5 == null ? "" : this.notBankId5;
	}

	/**
	 * 未揭露債權機構代號5<br>
	 * 
	 *
	 * @param notBankId5 未揭露債權機構代號5
	 */
	public void setNotBankId5(String notBankId5) {
		this.notBankId5 = notBankId5;
	}

	/**
	 * 未揭露債權機構代號6<br>
	 * 
	 * @return String
	 */
	public String getNotBankId6() {
		return this.notBankId6 == null ? "" : this.notBankId6;
	}

	/**
	 * 未揭露債權機構代號6<br>
	 * 
	 *
	 * @param notBankId6 未揭露債權機構代號6
	 */
	public void setNotBankId6(String notBankId6) {
		this.notBankId6 = notBankId6;
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
		return "JcicZ440Log [jcicZ440LogId=" + jcicZ440LogId + ", tranKey=" + tranKey + ", agreeDate=" + agreeDate + ", startDate=" + startDate + ", remindDate=" + remindDate + ", applyType="
				+ applyType + ", reportYn=" + reportYn + ", notBankId1=" + notBankId1 + ", notBankId2=" + notBankId2 + ", notBankId3=" + notBankId3 + ", notBankId4=" + notBankId4 + ", notBankId5="
				+ notBankId5 + ", notBankId6=" + notBankId6 + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
