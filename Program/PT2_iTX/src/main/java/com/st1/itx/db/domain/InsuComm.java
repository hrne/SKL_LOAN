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
 * InsuComm 火險佣金檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`InsuComm`")
public class InsuComm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7821497442070044431L;

	@EmbeddedId
	private InsuCommId insuCommId;

	// 年月份
	@Column(name = "`InsuYearMonth`", insertable = false, updatable = false)
	private int insuYearMonth = 0;

	// 佣金媒體檔序號
	/* 寫入檔之排序 */
	@Column(name = "`InsuCommSeq`", insertable = false, updatable = false)
	private int insuCommSeq = 0;

	// 經紀人代號
	@Column(name = "`ManagerCode`", length = 3)
	private String managerCode;

	// 保單號碼
	@Column(name = "`NowInsuNo`", length = 20)
	private String nowInsuNo;

	// 批號
	@Column(name = "`BatchNo`", length = 20)
	private String batchNo;

	// 險別
	@Column(name = "`InsuType`")
	private int insuType = 0;

	// 簽單日期
	@Column(name = "`InsuSignDate`")
	private int insuSignDate = 0;

	// 被保險人
	@Column(name = "`InsuredName`", length = 60)
	private String insuredName;

	// 被保險人地址
	@Column(name = "`InsuredAddr`", length = 60)
	private String insuredAddr;

	// 被保險人電話
	@Column(name = "`InsuredTeleph`", length = 20)
	private String insuredTeleph;

	// 起保日期
	@Column(name = "`InsuStartDate`")
	private int insuStartDate = 0;

	// 到期日期
	@Column(name = "`InsuEndDate`")
	private int insuEndDate = 0;

	// 險種
	@Column(name = "`InsuCate`")
	private int insuCate = 0;

	// 保費
	@Column(name = "`InsuPrem`")
	private BigDecimal insuPrem = new BigDecimal("0");

	// 佣金率
	@Column(name = "`CommRate`")
	private BigDecimal commRate = new BigDecimal("0");

	// 佣金
	@Column(name = "`Commision`")
	private BigDecimal commision = new BigDecimal("0");

	// 合計保費
	@Column(name = "`TotInsuPrem`")
	private BigDecimal totInsuPrem = new BigDecimal("0");

	// 合計佣金
	@Column(name = "`TotComm`")
	private BigDecimal totComm = new BigDecimal("0");

	// 收件號碼
	@Column(name = "`RecvSeq`", length = 14)
	private String recvSeq;

	// 收費日期
	@Column(name = "`ChargeDate`")
	private int chargeDate = 0;

	// 佣金日期
	@Column(name = "`CommDate`")
	private int commDate = 0;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 火險服務
	@Column(name = "`FireOfficer`", length = 6)
	private String fireOfficer;

	// 統一編號
	@Column(name = "`EmpId`", length = 10)
	private String empId;

	// 員工姓名
	@Column(name = "`EmpName`", length = 20)
	private String empName;

	// 應領金額
	@Column(name = "`DueAmt`")
	private BigDecimal dueAmt = new BigDecimal("0");

	// 媒體碼
	/* Y:已產生 */
	@Column(name = "`MediaCode`", length = 1)
	private String mediaCode;

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

	public InsuCommId getInsuCommId() {
		return this.insuCommId;
	}

	public void setInsuCommId(InsuCommId insuCommId) {
		this.insuCommId = insuCommId;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getInsuYearMonth() {
		return this.insuYearMonth;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param insuYearMonth 年月份
	 */
	public void setInsuYearMonth(int insuYearMonth) {
		this.insuYearMonth = insuYearMonth;
	}

	/**
	 * 佣金媒體檔序號<br>
	 * 寫入檔之排序
	 * 
	 * @return Integer
	 */
	public int getInsuCommSeq() {
		return this.insuCommSeq;
	}

	/**
	 * 佣金媒體檔序號<br>
	 * 寫入檔之排序
	 *
	 * @param insuCommSeq 佣金媒體檔序號
	 */
	public void setInsuCommSeq(int insuCommSeq) {
		this.insuCommSeq = insuCommSeq;
	}

	/**
	 * 經紀人代號<br>
	 * 
	 * @return String
	 */
	public String getManagerCode() {
		return this.managerCode == null ? "" : this.managerCode;
	}

	/**
	 * 經紀人代號<br>
	 * 
	 *
	 * @param managerCode 經紀人代號
	 */
	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}

	/**
	 * 保單號碼<br>
	 * 
	 * @return String
	 */
	public String getNowInsuNo() {
		return this.nowInsuNo == null ? "" : this.nowInsuNo;
	}

	/**
	 * 保單號碼<br>
	 * 
	 *
	 * @param nowInsuNo 保單號碼
	 */
	public void setNowInsuNo(String nowInsuNo) {
		this.nowInsuNo = nowInsuNo;
	}

	/**
	 * 批號<br>
	 * 
	 * @return String
	 */
	public String getBatchNo() {
		return this.batchNo == null ? "" : this.batchNo;
	}

	/**
	 * 批號<br>
	 * 
	 *
	 * @param batchNo 批號
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 險別<br>
	 * 
	 * @return Integer
	 */
	public int getInsuType() {
		return this.insuType;
	}

	/**
	 * 險別<br>
	 * 
	 *
	 * @param insuType 險別
	 */
	public void setInsuType(int insuType) {
		this.insuType = insuType;
	}

	/**
	 * 簽單日期<br>
	 * 
	 * @return Integer
	 */
	public int getInsuSignDate() {
		return StaticTool.bcToRoc(this.insuSignDate);
	}

	/**
	 * 簽單日期<br>
	 * 
	 *
	 * @param insuSignDate 簽單日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setInsuSignDate(int insuSignDate) throws LogicException {
		this.insuSignDate = StaticTool.rocToBc(insuSignDate);
	}

	/**
	 * 被保險人<br>
	 * 
	 * @return String
	 */
	public String getInsuredName() {
		return this.insuredName == null ? "" : this.insuredName;
	}

	/**
	 * 被保險人<br>
	 * 
	 *
	 * @param insuredName 被保險人
	 */
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	/**
	 * 被保險人地址<br>
	 * 
	 * @return String
	 */
	public String getInsuredAddr() {
		return this.insuredAddr == null ? "" : this.insuredAddr;
	}

	/**
	 * 被保險人地址<br>
	 * 
	 *
	 * @param insuredAddr 被保險人地址
	 */
	public void setInsuredAddr(String insuredAddr) {
		this.insuredAddr = insuredAddr;
	}

	/**
	 * 被保險人電話<br>
	 * 
	 * @return String
	 */
	public String getInsuredTeleph() {
		return this.insuredTeleph == null ? "" : this.insuredTeleph;
	}

	/**
	 * 被保險人電話<br>
	 * 
	 *
	 * @param insuredTeleph 被保險人電話
	 */
	public void setInsuredTeleph(String insuredTeleph) {
		this.insuredTeleph = insuredTeleph;
	}

	/**
	 * 起保日期<br>
	 * 
	 * @return Integer
	 */
	public int getInsuStartDate() {
		return StaticTool.bcToRoc(this.insuStartDate);
	}

	/**
	 * 起保日期<br>
	 * 
	 *
	 * @param insuStartDate 起保日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setInsuStartDate(int insuStartDate) throws LogicException {
		this.insuStartDate = StaticTool.rocToBc(insuStartDate);
	}

	/**
	 * 到期日期<br>
	 * 
	 * @return Integer
	 */
	public int getInsuEndDate() {
		return StaticTool.bcToRoc(this.insuEndDate);
	}

	/**
	 * 到期日期<br>
	 * 
	 *
	 * @param insuEndDate 到期日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setInsuEndDate(int insuEndDate) throws LogicException {
		this.insuEndDate = StaticTool.rocToBc(insuEndDate);
	}

	/**
	 * 險種<br>
	 * 
	 * @return Integer
	 */
	public int getInsuCate() {
		return this.insuCate;
	}

	/**
	 * 險種<br>
	 * 
	 *
	 * @param insuCate 險種
	 */
	public void setInsuCate(int insuCate) {
		this.insuCate = insuCate;
	}

	/**
	 * 保費<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInsuPrem() {
		return this.insuPrem;
	}

	/**
	 * 保費<br>
	 * 
	 *
	 * @param insuPrem 保費
	 */
	public void setInsuPrem(BigDecimal insuPrem) {
		this.insuPrem = insuPrem;
	}

	/**
	 * 佣金率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCommRate() {
		return this.commRate;
	}

	/**
	 * 佣金率<br>
	 * 
	 *
	 * @param commRate 佣金率
	 */
	public void setCommRate(BigDecimal commRate) {
		this.commRate = commRate;
	}

	/**
	 * 佣金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCommision() {
		return this.commision;
	}

	/**
	 * 佣金<br>
	 * 
	 *
	 * @param commision 佣金
	 */
	public void setCommision(BigDecimal commision) {
		this.commision = commision;
	}

	/**
	 * 合計保費<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotInsuPrem() {
		return this.totInsuPrem;
	}

	/**
	 * 合計保費<br>
	 * 
	 *
	 * @param totInsuPrem 合計保費
	 */
	public void setTotInsuPrem(BigDecimal totInsuPrem) {
		this.totInsuPrem = totInsuPrem;
	}

	/**
	 * 合計佣金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotComm() {
		return this.totComm;
	}

	/**
	 * 合計佣金<br>
	 * 
	 *
	 * @param totComm 合計佣金
	 */
	public void setTotComm(BigDecimal totComm) {
		this.totComm = totComm;
	}

	/**
	 * 收件號碼<br>
	 * 
	 * @return String
	 */
	public String getRecvSeq() {
		return this.recvSeq == null ? "" : this.recvSeq;
	}

	/**
	 * 收件號碼<br>
	 * 
	 *
	 * @param recvSeq 收件號碼
	 */
	public void setRecvSeq(String recvSeq) {
		this.recvSeq = recvSeq;
	}

	/**
	 * 收費日期<br>
	 * 
	 * @return Integer
	 */
	public int getChargeDate() {
		return StaticTool.bcToRoc(this.chargeDate);
	}

	/**
	 * 收費日期<br>
	 * 
	 *
	 * @param chargeDate 收費日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setChargeDate(int chargeDate) throws LogicException {
		this.chargeDate = StaticTool.rocToBc(chargeDate);
	}

	/**
	 * 佣金日期<br>
	 * 
	 * @return Integer
	 */
	public int getCommDate() {
		return StaticTool.bcToRoc(this.commDate);
	}

	/**
	 * 佣金日期<br>
	 * 
	 *
	 * @param commDate 佣金日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCommDate(int commDate) throws LogicException {
		this.commDate = StaticTool.rocToBc(commDate);
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 火險服務<br>
	 * 
	 * @return String
	 */
	public String getFireOfficer() {
		return this.fireOfficer == null ? "" : this.fireOfficer;
	}

	/**
	 * 火險服務<br>
	 * 
	 *
	 * @param fireOfficer 火險服務
	 */
	public void setFireOfficer(String fireOfficer) {
		this.fireOfficer = fireOfficer;
	}

	/**
	 * 統一編號<br>
	 * 
	 * @return String
	 */
	public String getEmpId() {
		return this.empId == null ? "" : this.empId;
	}

	/**
	 * 統一編號<br>
	 * 
	 *
	 * @param empId 統一編號
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * 員工姓名<br>
	 * 
	 * @return String
	 */
	public String getEmpName() {
		return this.empName == null ? "" : this.empName;
	}

	/**
	 * 員工姓名<br>
	 * 
	 *
	 * @param empName 員工姓名
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * 應領金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDueAmt() {
		return this.dueAmt;
	}

	/**
	 * 應領金額<br>
	 * 
	 *
	 * @param dueAmt 應領金額
	 */
	public void setDueAmt(BigDecimal dueAmt) {
		this.dueAmt = dueAmt;
	}

	/**
	 * 媒體碼<br>
	 * Y:已產生
	 * 
	 * @return String
	 */
	public String getMediaCode() {
		return this.mediaCode == null ? "" : this.mediaCode;
	}

	/**
	 * 媒體碼<br>
	 * Y:已產生
	 *
	 * @param mediaCode 媒體碼
	 */
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
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
		return "InsuComm [insuCommId=" + insuCommId + ", managerCode=" + managerCode + ", nowInsuNo=" + nowInsuNo + ", batchNo=" + batchNo + ", insuType=" + insuType + ", insuSignDate=" + insuSignDate
				+ ", insuredName=" + insuredName + ", insuredAddr=" + insuredAddr + ", insuredTeleph=" + insuredTeleph + ", insuStartDate=" + insuStartDate + ", insuEndDate=" + insuEndDate
				+ ", insuCate=" + insuCate + ", insuPrem=" + insuPrem + ", commRate=" + commRate + ", commision=" + commision + ", totInsuPrem=" + totInsuPrem + ", totComm=" + totComm + ", recvSeq="
				+ recvSeq + ", chargeDate=" + chargeDate + ", commDate=" + commDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", fireOfficer=" + fireOfficer + ", empId=" + empId + ", empName="
				+ empName + ", dueAmt=" + dueAmt + ", mediaCode=" + mediaCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
