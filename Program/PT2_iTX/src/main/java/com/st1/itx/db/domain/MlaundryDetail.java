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
 * MlaundryDetail 疑似洗錢交易合理性明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MlaundryDetail`")
public class MlaundryDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -619580125979051449L;

	@EmbeddedId
	private MlaundryDetailId mlaundryDetailId;

	// 入帳日期
	@Column(name = "`EntryDate`", insertable = false, updatable = false)
	private int entryDate = 0;

	// 交易樣態
	@Column(name = "`Factor`", insertable = false, updatable = false)
	private int factor = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 累積筆數
	@Column(name = "`TotalCnt`")
	private int totalCnt = 0;

	// 累積金額
	@Column(name = "`TotalAmt`")
	private BigDecimal totalAmt = new BigDecimal("0");

	// 合理性記號
	/* Y:是N:否空白:未註記 */
	@Column(name = "`Rational`", length = 1)
	private String rational;

	// 經辦合理性說明
	/* 2022/8/25長度放大150 */
	@Column(name = "`EmpNoDesc`", length = 150)
	private String empNoDesc;

	// 主管覆核
	/* Y:同意N:不同意空白:未覆核 */
	@Column(name = "`ManagerCheck`", length = 1)
	private String managerCheck;

	// 主管同意日期
	@Column(name = "`ManagerDate`")
	private int managerDate = 0;

	// 主管覆核日期
	/* 主管第二次覆核時顯示欄位 */
	@Column(name = "`ManagerCheckDate`")
	private int managerCheckDate = 0;

	// 主管覆核說明
	/* 2022/8/25長度放大150 */
	@Column(name = "`ManagerDesc`", length = 150)
	private String managerDesc;

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

	public MlaundryDetailId getMlaundryDetailId() {
		return this.mlaundryDetailId;
	}

	public void setMlaundryDetailId(MlaundryDetailId mlaundryDetailId) {
		this.mlaundryDetailId = mlaundryDetailId;
	}

	/**
	 * 入帳日期<br>
	 * 
	 * @return Integer
	 */
	public int getEntryDate() {
		return StaticTool.bcToRoc(this.entryDate);
	}

	/**
	 * 入帳日期<br>
	 * 
	 *
	 * @param entryDate 入帳日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setEntryDate(int entryDate) throws LogicException {
		this.entryDate = StaticTool.rocToBc(entryDate);
	}

	/**
	 * 交易樣態<br>
	 * 
	 * @return Integer
	 */
	public int getFactor() {
		return this.factor;
	}

	/**
	 * 交易樣態<br>
	 * 
	 *
	 * @param factor 交易樣態
	 */
	public void setFactor(int factor) {
		this.factor = factor;
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
	 * 累積筆數<br>
	 * 
	 * @return Integer
	 */
	public int getTotalCnt() {
		return this.totalCnt;
	}

	/**
	 * 累積筆數<br>
	 * 
	 *
	 * @param totalCnt 累積筆數
	 */
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * 累積金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAmt() {
		return this.totalAmt;
	}

	/**
	 * 累積金額<br>
	 * 
	 *
	 * @param totalAmt 累積金額
	 */
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	/**
	 * 合理性記號<br>
	 * Y:是 N:否 空白:未註記
	 * 
	 * @return String
	 */
	public String getRational() {
		return this.rational == null ? "" : this.rational;
	}

	/**
	 * 合理性記號<br>
	 * Y:是 N:否 空白:未註記
	 *
	 * @param rational 合理性記號
	 */
	public void setRational(String rational) {
		this.rational = rational;
	}

	/**
	 * 經辦合理性說明<br>
	 * 2022/8/25長度放大150
	 * 
	 * @return String
	 */
	public String getEmpNoDesc() {
		return this.empNoDesc == null ? "" : this.empNoDesc;
	}

	/**
	 * 經辦合理性說明<br>
	 * 2022/8/25長度放大150
	 *
	 * @param empNoDesc 經辦合理性說明
	 */
	public void setEmpNoDesc(String empNoDesc) {
		this.empNoDesc = empNoDesc;
	}

	/**
	 * 主管覆核<br>
	 * Y:同意 N:不同意 空白:未覆核
	 * 
	 * @return String
	 */
	public String getManagerCheck() {
		return this.managerCheck == null ? "" : this.managerCheck;
	}

	/**
	 * 主管覆核<br>
	 * Y:同意 N:不同意 空白:未覆核
	 *
	 * @param managerCheck 主管覆核
	 */
	public void setManagerCheck(String managerCheck) {
		this.managerCheck = managerCheck;
	}

	/**
	 * 主管同意日期<br>
	 * 
	 * @return Integer
	 */
	public int getManagerDate() {
		return StaticTool.bcToRoc(this.managerDate);
	}

	/**
	 * 主管同意日期<br>
	 * 
	 *
	 * @param managerDate 主管同意日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setManagerDate(int managerDate) throws LogicException {
		this.managerDate = StaticTool.rocToBc(managerDate);
	}

	/**
	 * 主管覆核日期<br>
	 * 主管第二次覆核時顯示欄位
	 * 
	 * @return Integer
	 */
	public int getManagerCheckDate() {
		return StaticTool.bcToRoc(this.managerCheckDate);
	}

	/**
	 * 主管覆核日期<br>
	 * 主管第二次覆核時顯示欄位
	 *
	 * @param managerCheckDate 主管覆核日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setManagerCheckDate(int managerCheckDate) throws LogicException {
		this.managerCheckDate = StaticTool.rocToBc(managerCheckDate);
	}

	/**
	 * 主管覆核說明<br>
	 * 2022/8/25長度放大150
	 * 
	 * @return String
	 */
	public String getManagerDesc() {
		return this.managerDesc == null ? "" : this.managerDesc;
	}

	/**
	 * 主管覆核說明<br>
	 * 2022/8/25長度放大150
	 *
	 * @param managerDesc 主管覆核說明
	 */
	public void setManagerDesc(String managerDesc) {
		this.managerDesc = managerDesc;
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
		return "MlaundryDetail [mlaundryDetailId=" + mlaundryDetailId + ", totalCnt=" + totalCnt + ", totalAmt=" + totalAmt + ", rational=" + rational + ", empNoDesc=" + empNoDesc + ", managerCheck="
				+ managerCheck + ", managerDate=" + managerDate + ", managerCheckDate=" + managerCheckDate + ", managerDesc=" + managerDesc + ", createDate=" + createDate + ", createEmpNo="
				+ createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
