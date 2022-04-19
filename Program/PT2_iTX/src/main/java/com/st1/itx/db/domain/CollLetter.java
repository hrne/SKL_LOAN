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
 * CollLetter 法催紀錄函催檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CollLetter`")
public class CollLetter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8234636855502524398L;

	@EmbeddedId
	private CollLetterId collLetterId;

	// 案件種類
	@Column(name = "`CaseCode`", length = 1, insertable = false, updatable = false)
	private String caseCode;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 作業日期
	@Column(name = "`AcDate`", insertable = false, updatable = false)
	private int acDate = 0;

	// 經辦
	@Column(name = "`TitaTlrNo`", length = 6, insertable = false, updatable = false)
	private String titaTlrNo;

	// 交易序號
	@Column(name = "`TitaTxtNo`", length = 8, insertable = false, updatable = false)
	private String titaTxtNo;

	// 發函種類
	@Column(name = "`MailTypeCode`", length = 1)
	private String mailTypeCode;

	// 發函日期
	@Column(name = "`MailDate`")
	private int mailDate = 0;

	// 發函對象
	@Column(name = "`MailObj`", length = 1)
	private String mailObj;

	// 姓名
	@Column(name = "`CustName`", length = 100)
	private String custName;

	// 送達否
	@Column(name = "`DelvrYet`", length = 1)
	private String delvrYet;

	// 送達方式
	@Column(name = "`DelvrCode`", length = 1)
	private String delvrCode;

	// 寄送地點選項
	@Column(name = "`AddressCode`")
	private int addressCode = 0;

	// 寄送地點
	@Column(name = "`Address`", length = 60)
	private String address;

	// 其他記錄
	@Column(name = "`Remark`", length = 500)
	private String remark;

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

	public CollLetterId getCollLetterId() {
		return this.collLetterId;
	}

	public void setCollLetterId(CollLetterId collLetterId) {
		this.collLetterId = collLetterId;
	}

	/**
	 * 案件種類<br>
	 * 
	 * @return String
	 */
	public String getCaseCode() {
		return this.caseCode == null ? "" : this.caseCode;
	}

	/**
	 * 案件種類<br>
	 * 
	 *
	 * @param caseCode 案件種類
	 */
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 作業日期<br>
	 * 
	 * @return Integer
	 */
	public int getAcDate() {
		return StaticTool.bcToRoc(this.acDate);
	}

	/**
	 * 作業日期<br>
	 * 
	 *
	 * @param acDate 作業日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setAcDate(int acDate) throws LogicException {
		this.acDate = StaticTool.rocToBc(acDate);
	}

	/**
	 * 經辦<br>
	 * 
	 * @return String
	 */
	public String getTitaTlrNo() {
		return this.titaTlrNo == null ? "" : this.titaTlrNo;
	}

	/**
	 * 經辦<br>
	 * 
	 *
	 * @param titaTlrNo 經辦
	 */
	public void setTitaTlrNo(String titaTlrNo) {
		this.titaTlrNo = titaTlrNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 * @return String
	 */
	public String getTitaTxtNo() {
		return this.titaTxtNo == null ? "" : this.titaTxtNo;
	}

	/**
	 * 交易序號<br>
	 * 
	 *
	 * @param titaTxtNo 交易序號
	 */
	public void setTitaTxtNo(String titaTxtNo) {
		this.titaTxtNo = titaTxtNo;
	}

	/**
	 * 發函種類<br>
	 * 
	 * @return String
	 */
	public String getMailTypeCode() {
		return this.mailTypeCode == null ? "" : this.mailTypeCode;
	}

	/**
	 * 發函種類<br>
	 * 
	 *
	 * @param mailTypeCode 發函種類
	 */
	public void setMailTypeCode(String mailTypeCode) {
		this.mailTypeCode = mailTypeCode;
	}

	/**
	 * 發函日期<br>
	 * 
	 * @return Integer
	 */
	public int getMailDate() {
		return StaticTool.bcToRoc(this.mailDate);
	}

	/**
	 * 發函日期<br>
	 * 
	 *
	 * @param mailDate 發函日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setMailDate(int mailDate) throws LogicException {
		this.mailDate = StaticTool.rocToBc(mailDate);
	}

	/**
	 * 發函對象<br>
	 * 
	 * @return String
	 */
	public String getMailObj() {
		return this.mailObj == null ? "" : this.mailObj;
	}

	/**
	 * 發函對象<br>
	 * 
	 *
	 * @param mailObj 發函對象
	 */
	public void setMailObj(String mailObj) {
		this.mailObj = mailObj;
	}

	/**
	 * 姓名<br>
	 * 
	 * @return String
	 */
	public String getCustName() {
		return this.custName == null ? "" : this.custName;
	}

	/**
	 * 姓名<br>
	 * 
	 *
	 * @param custName 姓名
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * 送達否<br>
	 * 
	 * @return String
	 */
	public String getDelvrYet() {
		return this.delvrYet == null ? "" : this.delvrYet;
	}

	/**
	 * 送達否<br>
	 * 
	 *
	 * @param delvrYet 送達否
	 */
	public void setDelvrYet(String delvrYet) {
		this.delvrYet = delvrYet;
	}

	/**
	 * 送達方式<br>
	 * 
	 * @return String
	 */
	public String getDelvrCode() {
		return this.delvrCode == null ? "" : this.delvrCode;
	}

	/**
	 * 送達方式<br>
	 * 
	 *
	 * @param delvrCode 送達方式
	 */
	public void setDelvrCode(String delvrCode) {
		this.delvrCode = delvrCode;
	}

	/**
	 * 寄送地點選項<br>
	 * 
	 * @return Integer
	 */
	public int getAddressCode() {
		return this.addressCode;
	}

	/**
	 * 寄送地點選項<br>
	 * 
	 *
	 * @param addressCode 寄送地點選項
	 */
	public void setAddressCode(int addressCode) {
		this.addressCode = addressCode;
	}

	/**
	 * 寄送地點<br>
	 * 
	 * @return String
	 */
	public String getAddress() {
		return this.address == null ? "" : this.address;
	}

	/**
	 * 寄送地點<br>
	 * 
	 *
	 * @param address 寄送地點
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 其他記錄<br>
	 * 
	 * @return String
	 */
	public String getRemark() {
		return this.remark == null ? "" : this.remark;
	}

	/**
	 * 其他記錄<br>
	 * 
	 *
	 * @param remark 其他記錄
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
		return "CollLetter [collLetterId=" + collLetterId + ", mailTypeCode=" + mailTypeCode + ", mailDate=" + mailDate + ", mailObj=" + mailObj + ", custName=" + custName + ", delvrYet=" + delvrYet
				+ ", delvrCode=" + delvrCode + ", addressCode=" + addressCode + ", address=" + address + ", remark=" + remark + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
