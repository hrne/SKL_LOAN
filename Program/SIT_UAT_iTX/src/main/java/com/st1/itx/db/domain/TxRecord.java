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
 * TxRecord 交易記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxRecord`")
public class TxRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4521813440799603563L;

	@EmbeddedId
	private TxRecordId txRecordId;

	// 帳務日
	@Column(name = "`Entdy`", insertable = false, updatable = false)
	private int entdy = 0;

	// 交易編號(單位+使用者編號+交易序號)
	@Column(name = "`TxNo`", length = 18, insertable = false, updatable = false)
	private String txNo;

	// 單位
	@Column(name = "`BrNo`", length = 4)
	private String brNo;

	// 使用者編號
	@Column(name = "`TlrNo`", length = 6)
	private String tlrNo;

	// 交易序號
	@Column(name = "`TxSeq`", length = 8)
	private String txSeq;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 科組別
	@Column(name = "`GroupNo`", length = 1)
	private String groupNo;

	// 櫃台機種類
	@Column(name = "`TrmType`", length = 2)
	private String trmType;

	// 交易編號/帳號
	@Column(name = "`MrKey`", length = 20)
	private String mrKey;

	// 帳務別
	@Column(name = "`SecNo`", length = 2)
	private String secNo;

	// 借貸
	/* D.借Debit C.貸Credit */
	@Column(name = "`DeCr`", length = 1)
	private String deCr;

	// 出帳記號
	@Column(name = "`BookAc`")
	private int bookAc = 0;

	// 帳務筆數
	@Column(name = "`AcCnt`")
	private int acCnt = 0;

	// 幣別
	@Column(name = "`CurCode`")
	private int curCode = 0;

	// 幣別swift Code
	@Column(name = "`CurName`", length = 3)
	private String curName;

	// 交易金額
	@Column(name = "`TxAmt`")
	private BigDecimal txAmt = new BigDecimal("0");

	// 主管編號
	@Column(name = "`SupNo`", length = 6)
	private String supNo;

	// 交易型態
	/* 0.正常交易 1.訂正交易 2.修正交易 */
	@Column(name = "`Hcode`")
	private int hcode = 0;

	// 交易日期
	@Column(name = "`CalDate`")
	private int calDate = 0;

	// 交易時間
	@Column(name = "`CalTime`")
	private int calTime = 0;

	// 交易結果
	/* S:成功 E:失敗 */
	@Column(name = "`TxResult`", length = 1)
	private String txResult;

	// 訊息代號
	@Column(name = "`MsgId`", length = 5)
	private String msgId;

	// 錯誤訊息
	@Column(name = "`ErrMsg`", length = 300)
	private String errMsg;

	// 流程控制序號
	@Column(name = "`FlowNo`", length = 18)
	private String flowNo;

	// 流程類別
	@Column(name = "`FlowType`")
	private int flowType = 0;

	// 流程步驟
	/* 1.登錄 2.放行 3.審核 4.審核放行 */
	@Column(name = "`FlowStep`")
	private int flowStep = 0;

	// 鎖定借款人戶號
	@Column(name = "`LockCustNo`")
	private int lockCustNo = 0;

	// 鎖定序號
	@Column(name = "`LockNo`")
	private Long lockNo = 0L;

	// 允許更正記號
	/* 0.不允許,1-允許,9-被訂正 */
	@Column(name = "`CanCancel`")
	private int canCancel = 0;

	// 允許修正記號
	/* 0.不允許,1-允許,9-已訂正 */
	@Column(name = "`CanModify`")
	private int canModify = 0;

	// 已訂正/修正交易記號
	/* 1.已訂正 2.已修正 3.已沖正 */
	@Column(name = "`ActionFg`")
	private int actionFg = 0;

	// 原訂正/修正序號-帳務日
	@Column(name = "`OrgEntdy`")
	private int orgEntdy = 0;

	// 原訂正/修正序號-交易序號
	@Column(name = "`OrgTxNo`", length = 18)
	private String orgTxNo;

	// 交易完整電文
	@Column(name = "`TranData`", length = 3000)
	private String tranData;

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

	public TxRecordId getTxRecordId() {
		return this.txRecordId;
	}

	public void setTxRecordId(TxRecordId txRecordId) {
		this.txRecordId = txRecordId;
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
	 * 交易編號(單位+使用者編號+交易序號)<br>
	 * 
	 * @return String
	 */
	public String getTxNo() {
		return this.txNo == null ? "" : this.txNo;
	}

	/**
	 * 交易編號(單位+使用者編號+交易序號)<br>
	 * 
	 *
	 * @param txNo 交易編號(單位+使用者編號+交易序號)
	 */
	public void setTxNo(String txNo) {
		this.txNo = txNo;
	}

	/**
	 * 單位<br>
	 * 
	 * @return String
	 */
	public String getBrNo() {
		return this.brNo == null ? "" : this.brNo;
	}

	/**
	 * 單位<br>
	 * 
	 *
	 * @param brNo 單位
	 */
	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}

	/**
	 * 使用者編號<br>
	 * 
	 * @return String
	 */
	public String getTlrNo() {
		return this.tlrNo == null ? "" : this.tlrNo;
	}

	/**
	 * 使用者編號<br>
	 * 
	 *
	 * @param tlrNo 使用者編號
	 */
	public void setTlrNo(String tlrNo) {
		this.tlrNo = tlrNo;
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
	 * 科組別<br>
	 * 
	 * @return String
	 */
	public String getGroupNo() {
		return this.groupNo == null ? "" : this.groupNo;
	}

	/**
	 * 科組別<br>
	 * 
	 *
	 * @param groupNo 科組別
	 */
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 * 櫃台機種類<br>
	 * 
	 * @return String
	 */
	public String getTrmType() {
		return this.trmType == null ? "" : this.trmType;
	}

	/**
	 * 櫃台機種類<br>
	 * 
	 *
	 * @param trmType 櫃台機種類
	 */
	public void setTrmType(String trmType) {
		this.trmType = trmType;
	}

	/**
	 * 交易編號/帳號<br>
	 * 
	 * @return String
	 */
	public String getMrKey() {
		return this.mrKey == null ? "" : this.mrKey;
	}

	/**
	 * 交易編號/帳號<br>
	 * 
	 *
	 * @param mrKey 交易編號/帳號
	 */
	public void setMrKey(String mrKey) {
		this.mrKey = mrKey;
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
	 * 借貸<br>
	 * D.借Debit C.貸Credit
	 * 
	 * @return String
	 */
	public String getDeCr() {
		return this.deCr == null ? "" : this.deCr;
	}

	/**
	 * 借貸<br>
	 * D.借Debit C.貸Credit
	 *
	 * @param deCr 借貸
	 */
	public void setDeCr(String deCr) {
		this.deCr = deCr;
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
	 * 幣別<br>
	 * 
	 * @return Integer
	 */
	public int getCurCode() {
		return this.curCode;
	}

	/**
	 * 幣別<br>
	 * 
	 *
	 * @param curCode 幣別
	 */
	public void setCurCode(int curCode) {
		this.curCode = curCode;
	}

	/**
	 * 幣別swift Code<br>
	 * 
	 * @return String
	 */
	public String getCurName() {
		return this.curName == null ? "" : this.curName;
	}

	/**
	 * 幣別swift Code<br>
	 * 
	 *
	 * @param curName 幣別swift Code
	 */
	public void setCurName(String curName) {
		this.curName = curName;
	}

	/**
	 * 交易金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTxAmt() {
		return this.txAmt;
	}

	/**
	 * 交易金額<br>
	 * 
	 *
	 * @param txAmt 交易金額
	 */
	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}

	/**
	 * 主管編號<br>
	 * 
	 * @return String
	 */
	public String getSupNo() {
		return this.supNo == null ? "" : this.supNo;
	}

	/**
	 * 主管編號<br>
	 * 
	 *
	 * @param supNo 主管編號
	 */
	public void setSupNo(String supNo) {
		this.supNo = supNo;
	}

	/**
	 * 交易型態<br>
	 * 0.正常交易 1.訂正交易 2.修正交易
	 * 
	 * @return Integer
	 */
	public int getHcode() {
		return this.hcode;
	}

	/**
	 * 交易型態<br>
	 * 0.正常交易 1.訂正交易 2.修正交易
	 *
	 * @param hcode 交易型態
	 */
	public void setHcode(int hcode) {
		this.hcode = hcode;
	}

	/**
	 * 交易日期<br>
	 * 
	 * @return Integer
	 */
	public int getCalDate() {
		return StaticTool.bcToRoc(this.calDate);
	}

	/**
	 * 交易日期<br>
	 * 
	 *
	 * @param calDate 交易日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCalDate(int calDate) throws LogicException {
		this.calDate = StaticTool.rocToBc(calDate);
	}

	/**
	 * 交易時間<br>
	 * 
	 * @return Integer
	 */
	public int getCalTime() {
		return this.calTime;
	}

	/**
	 * 交易時間<br>
	 * 
	 *
	 * @param calTime 交易時間
	 */
	public void setCalTime(int calTime) {
		this.calTime = calTime;
	}

	/**
	 * 交易結果<br>
	 * S:成功 E:失敗
	 * 
	 * @return String
	 */
	public String getTxResult() {
		return this.txResult == null ? "" : this.txResult;
	}

	/**
	 * 交易結果<br>
	 * S:成功 E:失敗
	 *
	 * @param txResult 交易結果
	 */
	public void setTxResult(String txResult) {
		this.txResult = txResult;
	}

	/**
	 * 訊息代號<br>
	 * 
	 * @return String
	 */
	public String getMsgId() {
		return this.msgId == null ? "" : this.msgId;
	}

	/**
	 * 訊息代號<br>
	 * 
	 *
	 * @param msgId 訊息代號
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * 錯誤訊息<br>
	 * 
	 * @return String
	 */
	public String getErrMsg() {
		return this.errMsg == null ? "" : this.errMsg;
	}

	/**
	 * 錯誤訊息<br>
	 * 
	 *
	 * @param errMsg 錯誤訊息
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	 * 允許更正記號<br>
	 * 0.不允許,1-允許,9-被訂正
	 * 
	 * @return Integer
	 */
	public int getCanCancel() {
		return this.canCancel;
	}

	/**
	 * 允許更正記號<br>
	 * 0.不允許,1-允許,9-被訂正
	 *
	 * @param canCancel 允許更正記號
	 */
	public void setCanCancel(int canCancel) {
		this.canCancel = canCancel;
	}

	/**
	 * 允許修正記號<br>
	 * 0.不允許,1-允許,9-已訂正
	 * 
	 * @return Integer
	 */
	public int getCanModify() {
		return this.canModify;
	}

	/**
	 * 允許修正記號<br>
	 * 0.不允許,1-允許,9-已訂正
	 *
	 * @param canModify 允許修正記號
	 */
	public void setCanModify(int canModify) {
		this.canModify = canModify;
	}

	/**
	 * 已訂正/修正交易記號<br>
	 * 1.已訂正 2.已修正 3.已沖正
	 * 
	 * @return Integer
	 */
	public int getActionFg() {
		return this.actionFg;
	}

	/**
	 * 已訂正/修正交易記號<br>
	 * 1.已訂正 2.已修正 3.已沖正
	 *
	 * @param actionFg 已訂正/修正交易記號
	 */
	public void setActionFg(int actionFg) {
		this.actionFg = actionFg;
	}

	/**
	 * 原訂正/修正序號-帳務日<br>
	 * 
	 * @return Integer
	 */
	public int getOrgEntdy() {
		return StaticTool.bcToRoc(this.orgEntdy);
	}

	/**
	 * 原訂正/修正序號-帳務日<br>
	 * 
	 *
	 * @param orgEntdy 原訂正/修正序號-帳務日
	 * @throws LogicException when Date Is Warn
	 */
	public void setOrgEntdy(int orgEntdy) throws LogicException {
		this.orgEntdy = StaticTool.rocToBc(orgEntdy);
	}

	/**
	 * 原訂正/修正序號-交易序號<br>
	 * 
	 * @return String
	 */
	public String getOrgTxNo() {
		return this.orgTxNo == null ? "" : this.orgTxNo;
	}

	/**
	 * 原訂正/修正序號-交易序號<br>
	 * 
	 *
	 * @param orgTxNo 原訂正/修正序號-交易序號
	 */
	public void setOrgTxNo(String orgTxNo) {
		this.orgTxNo = orgTxNo;
	}

	/**
	 * 交易完整電文<br>
	 * 
	 * @return String
	 */
	public String getTranData() {
		return this.tranData == null ? "" : this.tranData;
	}

	/**
	 * 交易完整電文<br>
	 * 
	 *
	 * @param tranData 交易完整電文
	 */
	public void setTranData(String tranData) {
		this.tranData = tranData;
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
		return "TxRecord [txRecordId=" + txRecordId + ", brNo=" + brNo + ", tlrNo=" + tlrNo + ", txSeq=" + txSeq + ", tranNo=" + tranNo + ", groupNo=" + groupNo + ", trmType=" + trmType + ", mrKey="
				+ mrKey + ", secNo=" + secNo + ", deCr=" + deCr + ", bookAc=" + bookAc + ", acCnt=" + acCnt + ", curCode=" + curCode + ", curName=" + curName + ", txAmt=" + txAmt + ", supNo=" + supNo
				+ ", hcode=" + hcode + ", calDate=" + calDate + ", calTime=" + calTime + ", txResult=" + txResult + ", msgId=" + msgId + ", errMsg=" + errMsg + ", flowNo=" + flowNo + ", flowType="
				+ flowType + ", flowStep=" + flowStep + ", lockCustNo=" + lockCustNo + ", lockNo=" + lockNo + ", canCancel=" + canCancel + ", canModify=" + canModify + ", actionFg=" + actionFg
				+ ", orgEntdy=" + orgEntdy + ", orgTxNo=" + orgTxNo + ", tranData=" + tranData + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
