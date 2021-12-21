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

/**
 * JcicB080 聯徵授信額度資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB080`")
public class JcicB080 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5802661612258287663L;

	@EmbeddedId
	private JcicB080Id jcicB080Id;

	// 資料年月
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 資料別
	/* 80:授信額度資料 */
	@Column(name = "`DataType`", length = 2)
	private String dataType;

	// 總行代號
	/* Key,金融機構總機構之代號，三位數字 */
	@Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
	private String bankItem;

	// 分行代號
	/* 金融機構分支機構之代號，四位數字 */
	@Column(name = "`BranchItem`", length = 4)
	private String branchItem;

	// 交易代碼
	/*
	 * A 新增 C 異動 D刪除 ;每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼：1.
	 * 新增資料時填A;2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D;4.
	 * 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
	 */
	@Column(name = "`TranCode`", length = 1)
	private String tranCode;

	// 空白
	/* 備用 */
	@Column(name = "`Filler4`", length = 4)
	private String filler4;

	// 授信戶IDN/BAN
	/* 左靠，身份證統一編號或統一證號 */
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 本階共用額度控制編碼
	/* Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複) */
	@Column(name = "`FacmNo`", length = 50, insertable = false, updatable = false)
	private String facmNo;

	// 授信幣別
	/* 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表" */
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 本階訂約金額(台幣)
	/* 右靠左補0，單位新台幣千元 */
	@Column(name = "`DrawdownAmt`")
	private BigDecimal drawdownAmt = new BigDecimal("0");

	// 本階訂約金額(外幣)
	/* 右靠左補0，單位新台幣千元 */
	@Column(name = "`DrawdownAmtFx`")
	private BigDecimal drawdownAmtFx = new BigDecimal("0");

	// 本階額度開始年月
	/* 以'YYYMM'(民國年)表示，填報本階訂約額度開始年月，如92年1月，請填'09201 */
	@Column(name = "`DrawdownDate`")
	private int drawdownDate = 0;

	// 本階額度約定截止年月
	/* 以'YYYMM'(民國年)表示，填報本階額度約定截止年月，如112年1月，請填'11201 */
	@Column(name = "`MaturityDate`")
	private int maturityDate = 0;

	// 循環信用註記
	/* 'Y':是，'N':否，填報本階額度是否屬循環性質 */
	@Column(name = "`RecycleCode`", length = 1)
	private String recycleCode;

	// 額度可否撤銷
	/* Y：可撤銷，N:不可撤銷 */
	@Column(name = "`IrrevocableFlag`", length = 1)
	private String irrevocableFlag;

	// 上階共用額度控制編碼
	/* 左靠，填寫上階額度控制編碼；若本階額度已為最上層，則以'9'填滿本欄位 */
	@Column(name = "`UpFacmNo`", length = 50)
	private String upFacmNo;

	// 科目別
	/* 請參考附件三授信科目代號表；若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。 */
	@Column(name = "`AcctCode`", length = 1)
	private String acctCode;

	// 科目別註記
	/*
	 * 以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;
	 * 若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;
	 * 另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表；
	 * 若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。
	 */
	@Column(name = "`SubAcctCode`", length = 1)
	private String subAcctCode;

	// 擔保品類別
	/*
	 * 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:
	 * 華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證)
	 */
	@Column(name = "`ClTypeCode`", length = 2)
	private String clTypeCode;

	// 空白
	/* 備用 */
	@Column(name = "`Filler18`", length = 24)
	private String filler18;

	// 資料所屬年月
	/* 請填報本筆授信資料所屬年月；每月正常報送授信額度資料及發函更正(已上線)額度資料時，本欄位皆需報送資料所屬年月 */
	@Column(name = "`JcicDataYM`")
	private int jcicDataYM = 0;

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

	public JcicB080Id getJcicB080Id() {
		return this.jcicB080Id;
	}

	public void setJcicB080Id(JcicB080Id jcicB080Id) {
		this.jcicB080Id = jcicB080Id;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 資料別<br>
	 * 80:授信額度資料
	 * 
	 * @return String
	 */
	public String getDataType() {
		return this.dataType == null ? "" : this.dataType;
	}

	/**
	 * 資料別<br>
	 * 80:授信額度資料
	 *
	 * @param dataType 資料別
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getBankItem() {
		return this.bankItem == null ? "" : this.bankItem;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 *
	 * @param bankItem 總行代號
	 */
	public void setBankItem(String bankItem) {
		this.bankItem = bankItem;
	}

	/**
	 * 分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getBranchItem() {
		return this.branchItem == null ? "" : this.branchItem;
	}

	/**
	 * 分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 *
	 * @param branchItem 分行代號
	 */
	public void setBranchItem(String branchItem) {
		this.branchItem = branchItem;
	}

	/**
	 * 交易代碼<br>
	 * A 新增 C 異動 D刪除 ; 每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼： 1.
	 * 新增資料時填A; 2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D; 4.
	 * 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
	 * 
	 * @return String
	 */
	public String getTranCode() {
		return this.tranCode == null ? "" : this.tranCode;
	}

	/**
	 * 交易代碼<br>
	 * A 新增 C 異動 D刪除 ; 每月正常報送資料(尚未上線時)僅限填A 代碼;發函更正(已上線)授信資料時，依下列狀況填A C D代碼： 1.
	 * 新增資料時填A; 2. 修改非key值欄位值時填C; 3. 刪除原報送資料時填D; 4.
	 * 若為更改Key欄位值，請先以一筆D刪除原報送資料，再以A代碼報送一筆新增(異動後)資料
	 *
	 * @param tranCode 交易代碼
	 */
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller4() {
		return this.filler4 == null ? "" : this.filler4;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler4 空白
	 */
	public void setFiller4(String filler4) {
		this.filler4 = filler4;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * 左靠，身份證統一編號或統一證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * 左靠，身份證統一編號或統一證號
	 *
	 * @param custId 授信戶IDN/BAN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 本階共用額度控制編碼<br>
	 * Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複)
	 * 
	 * @return String
	 */
	public String getFacmNo() {
		return this.facmNo == null ? "" : this.facmNo;
	}

	/**
	 * 本階共用額度控制編碼<br>
	 * Key,左靠，填報本階共用額度控制編碼（即授信額度月報之第28欄「上階共用額度控制編碼」），該編碼在同一金融機構內需為唯一(不可重複)
	 *
	 * @param facmNo 本階共用額度控制編碼
	 */
	public void setFacmNo(String facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 授信幣別<br>
	 * 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表"
	 * 
	 * @return String
	 */
	public String getCurrencyCode() {
		return this.currencyCode == null ? "" : this.currencyCode;
	}

	/**
	 * 授信幣別<br>
	 * 三個英文字．填報該幣別國際通用英文字母代號，請參照附件六"國際通用幣別代號表"
	 *
	 * @param currencyCode 授信幣別
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * 本階訂約金額(台幣)<br>
	 * 右靠左補0，單位新台幣千元
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDrawdownAmt() {
		return this.drawdownAmt;
	}

	/**
	 * 本階訂約金額(台幣)<br>
	 * 右靠左補0，單位新台幣千元
	 *
	 * @param drawdownAmt 本階訂約金額(台幣)
	 */
	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		this.drawdownAmt = drawdownAmt;
	}

	/**
	 * 本階訂約金額(外幣)<br>
	 * 右靠左補0，單位新台幣千元
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDrawdownAmtFx() {
		return this.drawdownAmtFx;
	}

	/**
	 * 本階訂約金額(外幣)<br>
	 * 右靠左補0，單位新台幣千元
	 *
	 * @param drawdownAmtFx 本階訂約金額(外幣)
	 */
	public void setDrawdownAmtFx(BigDecimal drawdownAmtFx) {
		this.drawdownAmtFx = drawdownAmtFx;
	}

	/**
	 * 本階額度開始年月<br>
	 * 以'YYYMM'(民國年)表示，填報本階訂約額度開始年月，如92年1月，請填'09201
	 * 
	 * @return Integer
	 */
	public int getDrawdownDate() {
		return this.drawdownDate;
	}

	/**
	 * 本階額度開始年月<br>
	 * 以'YYYMM'(民國年)表示，填報本階訂約額度開始年月，如92年1月，請填'09201
	 *
	 * @param drawdownDate 本階額度開始年月
	 */
	public void setDrawdownDate(int drawdownDate) {
		this.drawdownDate = drawdownDate;
	}

	/**
	 * 本階額度約定截止年月<br>
	 * 以'YYYMM'(民國年)表示，填報本階額度約定截止年月，如112年1月，請填'11201
	 * 
	 * @return Integer
	 */
	public int getMaturityDate() {
		return this.maturityDate;
	}

	/**
	 * 本階額度約定截止年月<br>
	 * 以'YYYMM'(民國年)表示，填報本階額度約定截止年月，如112年1月，請填'11201
	 *
	 * @param maturityDate 本階額度約定截止年月
	 */
	public void setMaturityDate(int maturityDate) {
		this.maturityDate = maturityDate;
	}

	/**
	 * 循環信用註記<br>
	 * 'Y':是，'N':否，填報本階額度是否屬循環性質
	 * 
	 * @return String
	 */
	public String getRecycleCode() {
		return this.recycleCode == null ? "" : this.recycleCode;
	}

	/**
	 * 循環信用註記<br>
	 * 'Y':是，'N':否，填報本階額度是否屬循環性質
	 *
	 * @param recycleCode 循環信用註記
	 */
	public void setRecycleCode(String recycleCode) {
		this.recycleCode = recycleCode;
	}

	/**
	 * 額度可否撤銷<br>
	 * Y：可撤銷，N:不可撤銷
	 * 
	 * @return String
	 */
	public String getIrrevocableFlag() {
		return this.irrevocableFlag == null ? "" : this.irrevocableFlag;
	}

	/**
	 * 額度可否撤銷<br>
	 * Y：可撤銷，N:不可撤銷
	 *
	 * @param irrevocableFlag 額度可否撤銷
	 */
	public void setIrrevocableFlag(String irrevocableFlag) {
		this.irrevocableFlag = irrevocableFlag;
	}

	/**
	 * 上階共用額度控制編碼<br>
	 * 左靠，填寫上階額度控制編碼；若本階額度已為最上層，則以'9'填滿本欄位
	 * 
	 * @return String
	 */
	public String getUpFacmNo() {
		return this.upFacmNo == null ? "" : this.upFacmNo;
	}

	/**
	 * 上階共用額度控制編碼<br>
	 * 左靠，填寫上階額度控制編碼；若本階額度已為最上層，則以'9'填滿本欄位
	 *
	 * @param upFacmNo 上階共用額度控制編碼
	 */
	public void setUpFacmNo(String upFacmNo) {
		this.upFacmNo = upFacmNo;
	}

	/**
	 * 科目別<br>
	 * 請參考附件三授信科目代號表；若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 科目別<br>
	 * 請參考附件三授信科目代號表；若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。
	 *
	 * @param acctCode 科目別
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 科目別註記<br>
	 * 以 S
	 * 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表；若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。
	 * 
	 * @return String
	 */
	public String getSubAcctCode() {
		return this.subAcctCode == null ? "" : this.subAcctCode;
	}

	/**
	 * 科目別註記<br>
	 * 以 S
	 * 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信，如無前述狀況請填X;若該擔保品於當月處理完畢，則當月份本欄位。屬部分擔保及副擔保之授信，本欄位亦請田X，但仍需將該部分擔保品或副擔保品相關資料填報於第45、46、47欄位;另辦理留學生就學貸款(授信科目Z)業務時，以R註記為留學生就學貸款，以X註記為高級中等以上學校就學貸款，請參考附件三之一科目別註記代號表；若本額度為非最低額度且為不同科目共用額度者，本欄位請填'*'。
	 *
	 * @param subAcctCode 科目別註記
	 */
	public void setSubAcctCode(String subAcctCode) {
		this.subAcctCode = subAcctCode;
	}

	/**
	 * 擔保品類別<br>
	 * 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證)
	 * 
	 * @return String
	 */
	public String getClTypeCode() {
		return this.clTypeCode == null ? "" : this.clTypeCode;
	}

	/**
	 * 擔保品類別<br>
	 * 擔保品類別代號表請參照附件九(本修訂版將原擔保品類別:信用保證機構保證細分為5項，05:：中小企業信用保證基金保證，06:農業信用保證基金保證、07:華僑貸款信用保證基金保證，08:國際合作發展基金會信用保證，09:原住民族綜合發展基金信用保證)
	 *
	 * @param clTypeCode 擔保品類別
	 */
	public void setClTypeCode(String clTypeCode) {
		this.clTypeCode = clTypeCode;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller18() {
		return this.filler18 == null ? "" : this.filler18;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler18 空白
	 */
	public void setFiller18(String filler18) {
		this.filler18 = filler18;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月；每月正常報送授信額度資料及發函更正(已上線)額度資料時，本欄位皆需報送資料所屬年月
	 * 
	 * @return Integer
	 */
	public int getJcicDataYM() {
		return this.jcicDataYM;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月；每月正常報送授信額度資料及發函更正(已上線)額度資料時，本欄位皆需報送資料所屬年月
	 *
	 * @param jcicDataYM 資料所屬年月
	 */
	public void setJcicDataYM(int jcicDataYM) {
		this.jcicDataYM = jcicDataYM;
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
		return "JcicB080 [jcicB080Id=" + jcicB080Id + ", dataType=" + dataType + ", branchItem=" + branchItem + ", tranCode=" + tranCode + ", filler4=" + filler4 + ", custId=" + custId
				+ ", currencyCode=" + currencyCode + ", drawdownAmt=" + drawdownAmt + ", drawdownAmtFx=" + drawdownAmtFx + ", drawdownDate=" + drawdownDate + ", maturityDate=" + maturityDate
				+ ", recycleCode=" + recycleCode + ", irrevocableFlag=" + irrevocableFlag + ", upFacmNo=" + upFacmNo + ", acctCode=" + acctCode + ", subAcctCode=" + subAcctCode + ", clTypeCode="
				+ clTypeCode + ", filler18=" + filler18 + ", jcicDataYM=" + jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
