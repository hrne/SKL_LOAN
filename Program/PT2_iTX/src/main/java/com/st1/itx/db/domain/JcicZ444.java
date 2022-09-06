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
 * JcicZ444 前置調解債務人基本資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ444`")
public class JcicZ444 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -900178902635837641L;

	@EmbeddedId
	private JcicZ444Id jcicZ444Id;

	// 交易代碼
	/* A:新增C:異動D:刪除X:補件 */
	@Column(name = "`TranKey`", length = 1)
	private String tranKey;

	// 債務人IDN
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
	private String submitKey;

	// 調解申請日
	@Column(name = "`ApplyDate`", insertable = false, updatable = false)
	private int applyDate = 0;

	// 受理調解機構代號
	/* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
	@Column(name = "`CourtCode`", length = 3, insertable = false, updatable = false)
	private String courtCode;

	// 債務人戶籍之郵遞區號及地址
	/* 38個中文 */
	@Column(name = "`CustRegAddr`", length = 76)
	private String custRegAddr;

	// 債務人通訊地之郵遞區號及地址
	/* 38個中文 */
	@Column(name = "`CustComAddr`", length = 76)
	private String custComAddr;

	// 債務人戶籍電話
	@Column(name = "`CustRegTelNo`", length = 16)
	private String custRegTelNo;

	// 債務人通訊電話
	@Column(name = "`CustComTelNo`", length = 16)
	private String custComTelNo;

	// 債務人行動電話
	@Column(name = "`CustMobilNo`", length = 16)
	private String custMobilNo;

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

	// 實際報送日期
	@Column(name = "`ActualFilingDate`")
	private int actualFilingDate = 0;

	// 實際報送記號
	@Column(name = "`ActualFilingMark`", length = 3)
	private String actualFilingMark;

	public JcicZ444Id getJcicZ444Id() {
		return this.jcicZ444Id;
	}

	public void setJcicZ444Id(JcicZ444Id jcicZ444Id) {
		this.jcicZ444Id = jcicZ444Id;
	}

	/**
	 * 交易代碼<br>
	 * A:新增 C:異動 D:刪除 X:補件
	 * 
	 * @return String
	 */
	public String getTranKey() {
		return this.tranKey == null ? "" : this.tranKey;
	}

	/**
	 * 交易代碼<br>
	 * A:新增 C:異動 D:刪除 X:補件
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
	 * 調解申請日<br>
	 * 
	 * @return Integer
	 */
	public int getApplyDate() {
		return StaticTool.bcToRoc(this.applyDate);
	}

	/**
	 * 調解申請日<br>
	 * 
	 *
	 * @param applyDate 調解申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setApplyDate(int applyDate) throws LogicException {
		this.applyDate = StaticTool.rocToBc(applyDate);
	}

	/**
	 * 受理調解機構代號<br>
	 * 三位文數字 法院名稱代號表(CdCode.CourtCode)或郵遞區號
	 * 
	 * @return String
	 */
	public String getCourtCode() {
		return this.courtCode == null ? "" : this.courtCode;
	}

	/**
	 * 受理調解機構代號<br>
	 * 三位文數字 法院名稱代號表(CdCode.CourtCode)或郵遞區號
	 *
	 * @param courtCode 受理調解機構代號
	 */
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	/**
	 * 債務人戶籍之郵遞區號及地址<br>
	 * 38個中文
	 * 
	 * @return String
	 */
	public String getCustRegAddr() {
		return this.custRegAddr == null ? "" : this.custRegAddr;
	}

	/**
	 * 債務人戶籍之郵遞區號及地址<br>
	 * 38個中文
	 *
	 * @param custRegAddr 債務人戶籍之郵遞區號及地址
	 */
	public void setCustRegAddr(String custRegAddr) {
		this.custRegAddr = custRegAddr;
	}

	/**
	 * 債務人通訊地之郵遞區號及地址<br>
	 * 38個中文
	 * 
	 * @return String
	 */
	public String getCustComAddr() {
		return this.custComAddr == null ? "" : this.custComAddr;
	}

	/**
	 * 債務人通訊地之郵遞區號及地址<br>
	 * 38個中文
	 *
	 * @param custComAddr 債務人通訊地之郵遞區號及地址
	 */
	public void setCustComAddr(String custComAddr) {
		this.custComAddr = custComAddr;
	}

	/**
	 * 債務人戶籍電話<br>
	 * 
	 * @return String
	 */
	public String getCustRegTelNo() {
		return this.custRegTelNo == null ? "" : this.custRegTelNo;
	}

	/**
	 * 債務人戶籍電話<br>
	 * 
	 *
	 * @param custRegTelNo 債務人戶籍電話
	 */
	public void setCustRegTelNo(String custRegTelNo) {
		this.custRegTelNo = custRegTelNo;
	}

	/**
	 * 債務人通訊電話<br>
	 * 
	 * @return String
	 */
	public String getCustComTelNo() {
		return this.custComTelNo == null ? "" : this.custComTelNo;
	}

	/**
	 * 債務人通訊電話<br>
	 * 
	 *
	 * @param custComTelNo 債務人通訊電話
	 */
	public void setCustComTelNo(String custComTelNo) {
		this.custComTelNo = custComTelNo;
	}

	/**
	 * 債務人行動電話<br>
	 * 
	 * @return String
	 */
	public String getCustMobilNo() {
		return this.custMobilNo == null ? "" : this.custMobilNo;
	}

	/**
	 * 債務人行動電話<br>
	 * 
	 *
	 * @param custMobilNo 債務人行動電話
	 */
	public void setCustMobilNo(String custMobilNo) {
		this.custMobilNo = custMobilNo;
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

	/**
	 * 實際報送日期<br>
	 * 
	 * @return Integer
	 */
	public int getActualFilingDate() {
		return StaticTool.bcToRoc(this.actualFilingDate);
	}

	/**
	 * 實際報送日期<br>
	 * 
	 *
	 * @param actualFilingDate 實際報送日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setActualFilingDate(int actualFilingDate) throws LogicException {
		this.actualFilingDate = StaticTool.rocToBc(actualFilingDate);
	}

	/**
	 * 實際報送記號<br>
	 * 
	 * @return String
	 */
	public String getActualFilingMark() {
		return this.actualFilingMark == null ? "" : this.actualFilingMark;
	}

	/**
	 * 實際報送記號<br>
	 * 
	 *
	 * @param actualFilingMark 實際報送記號
	 */
	public void setActualFilingMark(String actualFilingMark) {
		this.actualFilingMark = actualFilingMark;
	}

	@Override
	public String toString() {
		return "JcicZ444 [jcicZ444Id=" + jcicZ444Id + ", tranKey=" + tranKey + ", custRegAddr=" + custRegAddr + ", custComAddr=" + custComAddr + ", custRegTelNo=" + custRegTelNo + ", custComTelNo="
				+ custComTelNo + ", custMobilNo=" + custMobilNo + ", outJcicTxtDate=" + outJcicTxtDate + ", ukey=" + ukey + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", actualFilingDate=" + actualFilingDate + ", actualFilingMark=" + actualFilingMark + "]";
	}
}
