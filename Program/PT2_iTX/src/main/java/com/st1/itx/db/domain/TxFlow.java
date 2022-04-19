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
 * TxFlow 交易流程控制檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxFlow`")
public class TxFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4847973600562041156L;

	@EmbeddedId
	private TxFlowId txFlowId;

	// 帳務日
	@Column(name = "`Entdy`", insertable = false, updatable = false)
	private int entdy = 0;

	// 流程控制序號
	@Column(name = "`FlowNo`", length = 18, insertable = false, updatable = false)
	private String flowNo;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 流程類別
	@Column(name = "`FlowType`")
	private int flowType = 0;

	// 流程步驟
	/* 1.登錄 2.放行 3.審核 4.審核放行 */
	@Column(name = "`FlowStep`")
	private int flowStep = 0;

	// 流程模式
	/* 1.待放行 2.待審核 3.待送出 9.已結案 */
	@Column(name = "`FlowMode`")
	private int flowMode = 0;

	// 登錄單位
	@Column(name = "`BrNo`", length = 4)
	private String brNo;

	// 登錄科組別
	@Column(name = "`GroupNo`", length = 1)
	private String groupNo;

	// 審核單位
	@Column(name = "`ConBrNo`", length = 4)
	private String conBrNo;

	// 審核科組別
	@Column(name = "`ConGroupNo`", length = 1)
	private String conGroupNo;

	// 流程單位
	@Column(name = "`FlowBrNo`", length = 4)
	private String flowBrNo;

	// 流程科組別
	@Column(name = "`FlowGroupNo`", length = 1)
	private String flowGroupNo;

	// 流程1交易序號
	@Column(name = "`TxNo1`", length = 18)
	private String txNo1;

	// 流程2交易序號
	@Column(name = "`TxNo2`", length = 18)
	private String txNo2;

	// 流程3交易序號
	@Column(name = "`TxNo3`", length = 18)
	private String txNo3;

	// 流程4交易序號
	@Column(name = "`TxNo4`", length = 18)
	private String txNo4;

	// 鎖定借款人戶號
	@Column(name = "`LockCustNo`")
	private int lockCustNo = 0;

	// 鎖定序號
	@Column(name = "`LockNo`")
	private Long lockNo = 0L;

	// 帳務別
	@Column(name = "`SecNo`", length = 2)
	private String secNo;

	// 出帳記號
	@Column(name = "`BookAc`")
	private int bookAc = 0;

	// 帳務筆數
	@Column(name = "`AcCnt`")
	private int acCnt = 0;

	// 主管退回原因
	@Column(name = "`RejectReason`", length = 100)
	private String rejectReason;

	// 登錄需提交記號
	/* 0.否 1.是 (限二段式交易使用) */
	@Column(name = "`SubmitFg`")
	private int submitFg = 0;

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

	public TxFlowId getTxFlowId() {
		return this.txFlowId;
	}

	public void setTxFlowId(TxFlowId txFlowId) {
		this.txFlowId = txFlowId;
	}

	/**
	 * 帳務日<br>
	 * 
	 * @return Integer
	 */
	public int getEntdy() {
		return StaticTool.bcToRoc(this.entdy);
	}

	/**
	 * 帳務日<br>
	 * 
	 *
	 * @param entdy 帳務日
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntdy(int entdy) throws LogicException {
		this.entdy = StaticTool.rocToBc(entdy);
	}

	/**
	 * 流程控制序號<br>
	 * 
	 * @return String
	 */
	public String getFlowNo() {
		return this.flowNo == null ? "" : this.flowNo;
	}

	/**
	 * 流程控制序號<br>
	 * 
	 *
	 * @param flowNo 流程控制序號
	 */
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 * @return String
	 */
	public String getTranNo() {
		return this.tranNo == null ? "" : this.tranNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 *
	 * @param tranNo 交易代號
	 */
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}

	/**
	 * 流程類別<br>
	 * 
	 * @return Integer
	 */
	public int getFlowType() {
		return this.flowType;
	}

	/**
	 * 流程類別<br>
	 * 
	 *
	 * @param flowType 流程類別
	 */
	public void setFlowType(int flowType) {
		this.flowType = flowType;
	}

	/**
	 * 流程步驟<br>
	 * 1.登錄 2.放行 3.審核 4.審核放行
	 * 
	 * @return Integer
	 */
	public int getFlowStep() {
		return this.flowStep;
	}

	/**
	 * 流程步驟<br>
	 * 1.登錄 2.放行 3.審核 4.審核放行
	 *
	 * @param flowStep 流程步驟
	 */
	public void setFlowStep(int flowStep) {
		this.flowStep = flowStep;
	}

	/**
	 * 流程模式<br>
	 * 1.待放行 2.待審核 3.待送出 9.已結案
	 * 
	 * @return Integer
	 */
	public int getFlowMode() {
		return this.flowMode;
	}

	/**
	 * 流程模式<br>
	 * 1.待放行 2.待審核 3.待送出 9.已結案
	 *
	 * @param flowMode 流程模式
	 */
	public void setFlowMode(int flowMode) {
		this.flowMode = flowMode;
	}

	/**
	 * 登錄單位<br>
	 * 
	 * @return String
	 */
	public String getBrNo() {
		return this.brNo == null ? "" : this.brNo;
	}

	/**
	 * 登錄單位<br>
	 * 
	 *
	 * @param brNo 登錄單位
	 */
	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}

	/**
	 * 登錄科組別<br>
	 * 
	 * @return String
	 */
	public String getGroupNo() {
		return this.groupNo == null ? "" : this.groupNo;
	}

	/**
	 * 登錄科組別<br>
	 * 
	 *
	 * @param groupNo 登錄科組別
	 */
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 * 審核單位<br>
	 * 
	 * @return String
	 */
	public String getConBrNo() {
		return this.conBrNo == null ? "" : this.conBrNo;
	}

	/**
	 * 審核單位<br>
	 * 
	 *
	 * @param conBrNo 審核單位
	 */
	public void setConBrNo(String conBrNo) {
		this.conBrNo = conBrNo;
	}

	/**
	 * 審核科組別<br>
	 * 
	 * @return String
	 */
	public String getConGroupNo() {
		return this.conGroupNo == null ? "" : this.conGroupNo;
	}

	/**
	 * 審核科組別<br>
	 * 
	 *
	 * @param conGroupNo 審核科組別
	 */
	public void setConGroupNo(String conGroupNo) {
		this.conGroupNo = conGroupNo;
	}

	/**
	 * 流程單位<br>
	 * 
	 * @return String
	 */
	public String getFlowBrNo() {
		return this.flowBrNo == null ? "" : this.flowBrNo;
	}

	/**
	 * 流程單位<br>
	 * 
	 *
	 * @param flowBrNo 流程單位
	 */
	public void setFlowBrNo(String flowBrNo) {
		this.flowBrNo = flowBrNo;
	}

	/**
	 * 流程科組別<br>
	 * 
	 * @return String
	 */
	public String getFlowGroupNo() {
		return this.flowGroupNo == null ? "" : this.flowGroupNo;
	}

	/**
	 * 流程科組別<br>
	 * 
	 *
	 * @param flowGroupNo 流程科組別
	 */
	public void setFlowGroupNo(String flowGroupNo) {
		this.flowGroupNo = flowGroupNo;
	}

	/**
	 * 流程1交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxNo1() {
		return this.txNo1 == null ? "" : this.txNo1;
	}

	/**
	 * 流程1交易序號<br>
	 * 
	 *
	 * @param txNo1 流程1交易序號
	 */
	public void setTxNo1(String txNo1) {
		this.txNo1 = txNo1;
	}

	/**
	 * 流程2交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxNo2() {
		return this.txNo2 == null ? "" : this.txNo2;
	}

	/**
	 * 流程2交易序號<br>
	 * 
	 *
	 * @param txNo2 流程2交易序號
	 */
	public void setTxNo2(String txNo2) {
		this.txNo2 = txNo2;
	}

	/**
	 * 流程3交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxNo3() {
		return this.txNo3 == null ? "" : this.txNo3;
	}

	/**
	 * 流程3交易序號<br>
	 * 
	 *
	 * @param txNo3 流程3交易序號
	 */
	public void setTxNo3(String txNo3) {
		this.txNo3 = txNo3;
	}

	/**
	 * 流程4交易序號<br>
	 * 
	 * @return String
	 */
	public String getTxNo4() {
		return this.txNo4 == null ? "" : this.txNo4;
	}

	/**
	 * 流程4交易序號<br>
	 * 
	 *
	 * @param txNo4 流程4交易序號
	 */
	public void setTxNo4(String txNo4) {
		this.txNo4 = txNo4;
	}

	/**
	 * 鎖定借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getLockCustNo() {
		return this.lockCustNo;
	}

	/**
	 * 鎖定借款人戶號<br>
	 * 
	 *
	 * @param lockCustNo 鎖定借款人戶號
	 */
	public void setLockCustNo(int lockCustNo) {
		this.lockCustNo = lockCustNo;
	}

	/**
	 * 鎖定序號<br>
	 * 
	 * @return Long
	 */
	public Long getLockNo() {
		return this.lockNo;
	}

	/**
	 * 鎖定序號<br>
	 * 
	 *
	 * @param lockNo 鎖定序號
	 */
	public void setLockNo(Long lockNo) {
		this.lockNo = lockNo;
	}

	/**
	 * 帳務別<br>
	 * 
	 * @return String
	 */
	public String getSecNo() {
		return this.secNo == null ? "" : this.secNo;
	}

	/**
	 * 帳務別<br>
	 * 
	 *
	 * @param secNo 帳務別
	 */
	public void setSecNo(String secNo) {
		this.secNo = secNo;
	}

	/**
	 * 出帳記號<br>
	 * 
	 * @return Integer
	 */
	public int getBookAc() {
		return this.bookAc;
	}

	/**
	 * 出帳記號<br>
	 * 
	 *
	 * @param bookAc 出帳記號
	 */
	public void setBookAc(int bookAc) {
		this.bookAc = bookAc;
	}

	/**
	 * 帳務筆數<br>
	 * 
	 * @return Integer
	 */
	public int getAcCnt() {
		return this.acCnt;
	}

	/**
	 * 帳務筆數<br>
	 * 
	 *
	 * @param acCnt 帳務筆數
	 */
	public void setAcCnt(int acCnt) {
		this.acCnt = acCnt;
	}

	/**
	 * 主管退回原因<br>
	 * 
	 * @return String
	 */
	public String getRejectReason() {
		return this.rejectReason == null ? "" : this.rejectReason;
	}

	/**
	 * 主管退回原因<br>
	 * 
	 *
	 * @param rejectReason 主管退回原因
	 */
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	/**
	 * 登錄需提交記號<br>
	 * 0.否 1.是 (限二段式交易使用)
	 * 
	 * @return Integer
	 */
	public int getSubmitFg() {
		return this.submitFg;
	}

	/**
	 * 登錄需提交記號<br>
	 * 0.否 1.是 (限二段式交易使用)
	 *
	 * @param submitFg 登錄需提交記號
	 */
	public void setSubmitFg(int submitFg) {
		this.submitFg = submitFg;
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
		return "TxFlow [txFlowId=" + txFlowId + ", tranNo=" + tranNo + ", flowType=" + flowType + ", flowStep=" + flowStep + ", flowMode=" + flowMode + ", brNo=" + brNo + ", groupNo=" + groupNo
				+ ", conBrNo=" + conBrNo + ", conGroupNo=" + conGroupNo + ", flowBrNo=" + flowBrNo + ", flowGroupNo=" + flowGroupNo + ", txNo1=" + txNo1 + ", txNo2=" + txNo2 + ", txNo3=" + txNo3
				+ ", txNo4=" + txNo4 + ", lockCustNo=" + lockCustNo + ", lockNo=" + lockNo + ", secNo=" + secNo + ", bookAc=" + bookAc + ", acCnt=" + acCnt + ", rejectReason=" + rejectReason
				+ ", submitFg=" + submitFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
