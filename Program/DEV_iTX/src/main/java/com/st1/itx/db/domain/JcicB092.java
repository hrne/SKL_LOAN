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

/**
 * JcicB092 聯徵不動產擔保品明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB092`")
public class JcicB092 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3946536093365226033L;

	@EmbeddedId
	private JcicB092Id jcicB092Id;

	// 資料日期
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 資料別
	/* 92:不動產擔保品明細檔資料 */
	@Column(name = "`DataType`", length = 2)
	private String dataType;

	// 總行代號
	/* Key,金融機構總機構之代號，三位數字 */
	@Column(name = "`BankItem`", length = 3)
	private String bankItem;

	// 分行代號
	/* Key,金融機構分支機構之代號，四位數字 */
	@Column(name = "`BranchItem`", length = 4)
	private String branchItem;

	// 空白
	/* 空白 */
	@Column(name = "`Filler4`", length = 2)
	private String filler4;

	// 擔保品控制編碼
	/* Key,左靠右補空白 */
	@Column(name = "`ClActNo`", length = 50, insertable = false, updatable = false)
	private String clActNo;

	// 擔保品類別
	/* 附件9 */
	@Column(name = "`ClTypeJCIC`", length = 2, insertable = false, updatable = false)
	private String clTypeJCIC;

	// 擔保品所有權人或代表人IDN/BAN
	/* Key,左靠，股票持有人身份證或統一證號 */
	@Column(name = "`OwnerId`", length = 10, insertable = false, updatable = false)
	private String ownerId;

	// 鑑估(總市)值
	/* 右靠，左補0，單位新台幣千元 */
	@Column(name = "`EvaAmt`", length = 9)
	private String evaAmt;

	// 鑑估日期
	/* 擔保品鑑估年月，以YYYYMM(民國年)表示 */
	@Column(name = "`EvaDate`")
	private int evaDate = 0;

	// 可放款值
	/* 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數 */
	@Column(name = "`LoanLimitAmt`", length = 9)
	private String loanLimitAmt;

	// 設定日期
	/* 以YYYYMM(民國年)表示 */
	@Column(name = "`SettingDate`")
	private int settingDate = 0;

	// 本行本月設定金額
	/* 右靠，左補0，單位新台幣千元，本月新增設定總金額 */
	@Column(name = "`MonthSettingAmt`", length = 9)
	private String monthSettingAmt;

	// 本行設定抵押順位
	/* 本月如有設定，填報本月設定之設定順位 */
	@Column(name = "`SettingSeq`")
	private int settingSeq = 0;

	// 本行累計已設定總金額
	/* 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額 */
	@Column(name = "`SettingAmt`", length = 9)
	private String settingAmt;

	// 其他債權人已設定金額
	/* 右靠，左補0，單位新台幣千元，如無則填0 */
	@Column(name = "`PreSettingAmt`", length = 9)
	private String preSettingAmt;

	// 處分價格
	/* 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0 */
	@Column(name = "`DispPrice`", length = 9)
	private String dispPrice;

	// 權利到期年月
	/* YYYMM(民國年)，如無到期年月，請填99999 */
	@Column(name = "`IssueEndDate`")
	private int issueEndDate = 0;

	// 縣市別
	/* Key, */
	@Column(name = "`CityJCICCode`", length = 1, insertable = false, updatable = false)
	private String cityJCICCode;

	// 鄉鎮市區別
	/* Key, */
	@Column(name = "`AreaJCICCode`", insertable = false, updatable = false)
	private int areaJCICCode = 0;

	// 段、小段號
	/* Key, */
	@Column(name = "`IrCode`", length = 4, insertable = false, updatable = false)
	private String irCode;

	// 地號-前四碼
	/* Key, */
	@Column(name = "`LandNo1`", insertable = false, updatable = false)
	private int landNo1 = 0;

	// 地號-後四碼
	/* Key, */
	@Column(name = "`LandNo2`", insertable = false, updatable = false)
	private int landNo2 = 0;

	// 建號-前五碼
	/* Key, */
	@Column(name = "`BdNo1`", insertable = false, updatable = false)
	private int bdNo1 = 0;

	// 建號-後三碼
	/* Key, */
	@Column(name = "`BdNo2`", insertable = false, updatable = false)
	private int bdNo2 = 0;

	// 郵遞區號
	@Column(name = "`Zip`", length = 5)
	private String zip;

	// 是否有保險
	/* Y=是，N=否 */
	@Column(name = "`InsuFg`", length = 1)
	private String insuFg;

	// 預估應計土地增值稅
	@Column(name = "`LVITax`", length = 9)
	private String lVITax;

	// 應計土地增值稅之預估年月
	@Column(name = "`LVITaxYearMonth`", length = 5)
	private String lVITaxYearMonth;

	// 買賣契約價格
	@Column(name = "`ContractPrice`", length = 9)
	private String contractPrice;

	// 買賣契約日期
	@Column(name = "`ContractDate`", length = 7)
	private String contractDate;

	// 停車位形式
	@Column(name = "`ParkingTypeCode`", length = 1)
	private String parkingTypeCode;

	// 車位單獨登記面積
	/* 單位:平方公尺，填至小數點2位 */
	@Column(name = "`Area`", length = 9)
	private String area;

	// 土地持份面積
	/* 單位:平方公尺，填至小數點2位 */
	@Column(name = "`LandOwnedArea`", length = 10)
	private String landOwnedArea;

	// 建物類別
	@Column(name = "`BdTypeCode`", length = 2)
	private String bdTypeCode;

	// 空白
	/* 空白 */
	@Column(name = "`Filler33`", length = 29)
	private String filler33;

	// 資料所屬年月
	/* 請填報本筆授信資料所屬年月(民國年) */
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

	public JcicB092Id getJcicB092Id() {
		return this.jcicB092Id;
	}

	public void setJcicB092Id(JcicB092Id jcicB092Id) {
		this.jcicB092Id = jcicB092Id;
	}

	/**
	 * 資料日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料日期<br>
	 * 
	 *
	 * @param dataYM 資料日期
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 資料別<br>
	 * 92:不動產擔保品明細檔資料
	 * 
	 * @return String
	 */
	public String getDataType() {
		return this.dataType == null ? "" : this.dataType;
	}

	/**
	 * 資料別<br>
	 * 92:不動產擔保品明細檔資料
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
	 * Key,金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getBranchItem() {
		return this.branchItem == null ? "" : this.branchItem;
	}

	/**
	 * 分行代號<br>
	 * Key,金融機構分支機構之代號，四位數字
	 *
	 * @param branchItem 分行代號
	 */
	public void setBranchItem(String branchItem) {
		this.branchItem = branchItem;
	}

	/**
	 * 空白<br>
	 * 空白
	 * 
	 * @return String
	 */
	public String getFiller4() {
		return this.filler4 == null ? "" : this.filler4;
	}

	/**
	 * 空白<br>
	 * 空白
	 *
	 * @param filler4 空白
	 */
	public void setFiller4(String filler4) {
		this.filler4 = filler4;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 * 
	 * @return String
	 */
	public String getClActNo() {
		return this.clActNo == null ? "" : this.clActNo;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 *
	 * @param clActNo 擔保品控制編碼
	 */
	public void setClActNo(String clActNo) {
		this.clActNo = clActNo;
	}

	/**
	 * 擔保品類別<br>
	 * 附件9
	 * 
	 * @return String
	 */
	public String getClTypeJCIC() {
		return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
	}

	/**
	 * 擔保品類別<br>
	 * 附件9
	 *
	 * @param clTypeJCIC 擔保品類別
	 */
	public void setClTypeJCIC(String clTypeJCIC) {
		this.clTypeJCIC = clTypeJCIC;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * Key,左靠，股票持有人身份證或統一證號
	 * 
	 * @return String
	 */
	public String getOwnerId() {
		return this.ownerId == null ? "" : this.ownerId;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * Key,左靠，股票持有人身份證或統一證號
	 *
	 * @param ownerId 擔保品所有權人或代表人IDN/BAN
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * 鑑估(總市)值<br>
	 * 右靠，左補0，單位新台幣千元
	 * 
	 * @return String
	 */
	public String getEvaAmt() {
		return this.evaAmt == null ? "" : this.evaAmt;
	}

	/**
	 * 鑑估(總市)值<br>
	 * 右靠，左補0，單位新台幣千元
	 *
	 * @param evaAmt 鑑估(總市)值
	 */
	public void setEvaAmt(String evaAmt) {
		this.evaAmt = evaAmt;
	}

	/**
	 * 鑑估日期<br>
	 * 擔保品鑑估年月，以YYYYMM(民國年)表示
	 * 
	 * @return Integer
	 */
	public int getEvaDate() {
		return this.evaDate;
	}

	/**
	 * 鑑估日期<br>
	 * 擔保品鑑估年月，以YYYYMM(民國年)表示
	 *
	 * @param evaDate 鑑估日期
	 */
	public void setEvaDate(int evaDate) {
		this.evaDate = evaDate;
	}

	/**
	 * 可放款值<br>
	 * 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數
	 * 
	 * @return String
	 */
	public String getLoanLimitAmt() {
		return this.loanLimitAmt == null ? "" : this.loanLimitAmt;
	}

	/**
	 * 可放款值<br>
	 * 右靠，左補0，單位新台幣千元，鑑估值 * 可貸放成數
	 *
	 * @param loanLimitAmt 可放款值
	 */
	public void setLoanLimitAmt(String loanLimitAmt) {
		this.loanLimitAmt = loanLimitAmt;
	}

	/**
	 * 設定日期<br>
	 * 以YYYYMM(民國年)表示
	 * 
	 * @return Integer
	 */
	public int getSettingDate() {
		return this.settingDate;
	}

	/**
	 * 設定日期<br>
	 * 以YYYYMM(民國年)表示
	 *
	 * @param settingDate 設定日期
	 */
	public void setSettingDate(int settingDate) {
		this.settingDate = settingDate;
	}

	/**
	 * 本行本月設定金額<br>
	 * 右靠，左補0，單位新台幣千元，本月新增設定總金額
	 * 
	 * @return String
	 */
	public String getMonthSettingAmt() {
		return this.monthSettingAmt == null ? "" : this.monthSettingAmt;
	}

	/**
	 * 本行本月設定金額<br>
	 * 右靠，左補0，單位新台幣千元，本月新增設定總金額
	 *
	 * @param monthSettingAmt 本行本月設定金額
	 */
	public void setMonthSettingAmt(String monthSettingAmt) {
		this.monthSettingAmt = monthSettingAmt;
	}

	/**
	 * 本行設定抵押順位<br>
	 * 本月如有設定，填報本月設定之設定順位
	 * 
	 * @return Integer
	 */
	public int getSettingSeq() {
		return this.settingSeq;
	}

	/**
	 * 本行設定抵押順位<br>
	 * 本月如有設定，填報本月設定之設定順位
	 *
	 * @param settingSeq 本行設定抵押順位
	 */
	public void setSettingSeq(int settingSeq) {
		this.settingSeq = settingSeq;
	}

	/**
	 * 本行累計已設定總金額<br>
	 * 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額
	 * 
	 * @return String
	 */
	public String getSettingAmt() {
		return this.settingAmt == null ? "" : this.settingAmt;
	}

	/**
	 * 本行累計已設定總金額<br>
	 * 右靠，左補0，單位新台幣千元，至本月底該動產本行已設定總金額
	 *
	 * @param settingAmt 本行累計已設定總金額
	 */
	public void setSettingAmt(String settingAmt) {
		this.settingAmt = settingAmt;
	}

	/**
	 * 其他債權人已設定金額<br>
	 * 右靠，左補0，單位新台幣千元，如無則填0
	 * 
	 * @return String
	 */
	public String getPreSettingAmt() {
		return this.preSettingAmt == null ? "" : this.preSettingAmt;
	}

	/**
	 * 其他債權人已設定金額<br>
	 * 右靠，左補0，單位新台幣千元，如無則填0
	 *
	 * @param preSettingAmt 其他債權人已設定金額
	 */
	public void setPreSettingAmt(String preSettingAmt) {
		this.preSettingAmt = preSettingAmt;
	}

	/**
	 * 處分價格<br>
	 * 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0
	 * 
	 * @return String
	 */
	public String getDispPrice() {
		return this.dispPrice == null ? "" : this.dispPrice;
	}

	/**
	 * 處分價格<br>
	 * 八位數字，右靠，左補0，單位新台幣千元，填報該擔保品至本月底前已被處分之價格金額，如無則填0
	 *
	 * @param dispPrice 處分價格
	 */
	public void setDispPrice(String dispPrice) {
		this.dispPrice = dispPrice;
	}

	/**
	 * 權利到期年月<br>
	 * YYYMM(民國年)，如無到期年月，請填99999
	 * 
	 * @return Integer
	 */
	public int getIssueEndDate() {
		return this.issueEndDate;
	}

	/**
	 * 權利到期年月<br>
	 * YYYMM(民國年)，如無到期年月，請填99999
	 *
	 * @param issueEndDate 權利到期年月
	 */
	public void setIssueEndDate(int issueEndDate) {
		this.issueEndDate = issueEndDate;
	}

	/**
	 * 縣市別<br>
	 * Key,
	 * 
	 * @return String
	 */
	public String getCityJCICCode() {
		return this.cityJCICCode == null ? "" : this.cityJCICCode;
	}

	/**
	 * 縣市別<br>
	 * Key,
	 *
	 * @param cityJCICCode 縣市別
	 */
	public void setCityJCICCode(String cityJCICCode) {
		this.cityJCICCode = cityJCICCode;
	}

	/**
	 * 鄉鎮市區別<br>
	 * Key,
	 * 
	 * @return Integer
	 */
	public int getAreaJCICCode() {
		return this.areaJCICCode;
	}

	/**
	 * 鄉鎮市區別<br>
	 * Key,
	 *
	 * @param areaJCICCode 鄉鎮市區別
	 */
	public void setAreaJCICCode(int areaJCICCode) {
		this.areaJCICCode = areaJCICCode;
	}

	/**
	 * 段、小段號<br>
	 * Key,
	 * 
	 * @return String
	 */
	public String getIrCode() {
		return this.irCode == null ? "" : this.irCode;
	}

	/**
	 * 段、小段號<br>
	 * Key,
	 *
	 * @param irCode 段、小段號
	 */
	public void setIrCode(String irCode) {
		this.irCode = irCode;
	}

	/**
	 * 地號-前四碼<br>
	 * Key,
	 * 
	 * @return Integer
	 */
	public int getLandNo1() {
		return this.landNo1;
	}

	/**
	 * 地號-前四碼<br>
	 * Key,
	 *
	 * @param landNo1 地號-前四碼
	 */
	public void setLandNo1(int landNo1) {
		this.landNo1 = landNo1;
	}

	/**
	 * 地號-後四碼<br>
	 * Key,
	 * 
	 * @return Integer
	 */
	public int getLandNo2() {
		return this.landNo2;
	}

	/**
	 * 地號-後四碼<br>
	 * Key,
	 *
	 * @param landNo2 地號-後四碼
	 */
	public void setLandNo2(int landNo2) {
		this.landNo2 = landNo2;
	}

	/**
	 * 建號-前五碼<br>
	 * Key,
	 * 
	 * @return Integer
	 */
	public int getBdNo1() {
		return this.bdNo1;
	}

	/**
	 * 建號-前五碼<br>
	 * Key,
	 *
	 * @param bdNo1 建號-前五碼
	 */
	public void setBdNo1(int bdNo1) {
		this.bdNo1 = bdNo1;
	}

	/**
	 * 建號-後三碼<br>
	 * Key,
	 * 
	 * @return Integer
	 */
	public int getBdNo2() {
		return this.bdNo2;
	}

	/**
	 * 建號-後三碼<br>
	 * Key,
	 *
	 * @param bdNo2 建號-後三碼
	 */
	public void setBdNo2(int bdNo2) {
		this.bdNo2 = bdNo2;
	}

	/**
	 * 郵遞區號<br>
	 * 
	 * @return String
	 */
	public String getZip() {
		return this.zip == null ? "" : this.zip;
	}

	/**
	 * 郵遞區號<br>
	 * 
	 *
	 * @param zip 郵遞區號
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * 是否有保險<br>
	 * Y=是，N=否
	 * 
	 * @return String
	 */
	public String getInsuFg() {
		return this.insuFg == null ? "" : this.insuFg;
	}

	/**
	 * 是否有保險<br>
	 * Y=是，N=否
	 *
	 * @param insuFg 是否有保險
	 */
	public void setInsuFg(String insuFg) {
		this.insuFg = insuFg;
	}

	/**
	 * 預估應計土地增值稅<br>
	 * 
	 * @return String
	 */
	public String getLVITax() {
		return this.lVITax == null ? "" : this.lVITax;
	}

	/**
	 * 預估應計土地增值稅<br>
	 * 
	 *
	 * @param lVITax 預估應計土地增值稅
	 */
	public void setLVITax(String lVITax) {
		this.lVITax = lVITax;
	}

	/**
	 * 應計土地增值稅之預估年月<br>
	 * 
	 * @return String
	 */
	public String getLVITaxYearMonth() {
		return this.lVITaxYearMonth == null ? "" : this.lVITaxYearMonth;
	}

	/**
	 * 應計土地增值稅之預估年月<br>
	 * 
	 *
	 * @param lVITaxYearMonth 應計土地增值稅之預估年月
	 */
	public void setLVITaxYearMonth(String lVITaxYearMonth) {
		this.lVITaxYearMonth = lVITaxYearMonth;
	}

	/**
	 * 買賣契約價格<br>
	 * 
	 * @return String
	 */
	public String getContractPrice() {
		return this.contractPrice == null ? "" : this.contractPrice;
	}

	/**
	 * 買賣契約價格<br>
	 * 
	 *
	 * @param contractPrice 買賣契約價格
	 */
	public void setContractPrice(String contractPrice) {
		this.contractPrice = contractPrice;
	}

	/**
	 * 買賣契約日期<br>
	 * 
	 * @return String
	 */
	public String getContractDate() {
		return this.contractDate == null ? "" : this.contractDate;
	}

	/**
	 * 買賣契約日期<br>
	 * 
	 *
	 * @param contractDate 買賣契約日期
	 */
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	/**
	 * 停車位形式<br>
	 * 
	 * @return String
	 */
	public String getParkingTypeCode() {
		return this.parkingTypeCode == null ? "" : this.parkingTypeCode;
	}

	/**
	 * 停車位形式<br>
	 * 
	 *
	 * @param parkingTypeCode 停車位形式
	 */
	public void setParkingTypeCode(String parkingTypeCode) {
		this.parkingTypeCode = parkingTypeCode;
	}

	/**
	 * 車位單獨登記面積<br>
	 * 單位:平方公尺，填至小數點2位
	 * 
	 * @return String
	 */
	public String getArea() {
		return this.area == null ? "" : this.area;
	}

	/**
	 * 車位單獨登記面積<br>
	 * 單位:平方公尺，填至小數點2位
	 *
	 * @param area 車位單獨登記面積
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 土地持份面積<br>
	 * 單位:平方公尺，填至小數點2位
	 * 
	 * @return String
	 */
	public String getLandOwnedArea() {
		return this.landOwnedArea == null ? "" : this.landOwnedArea;
	}

	/**
	 * 土地持份面積<br>
	 * 單位:平方公尺，填至小數點2位
	 *
	 * @param landOwnedArea 土地持份面積
	 */
	public void setLandOwnedArea(String landOwnedArea) {
		this.landOwnedArea = landOwnedArea;
	}

	/**
	 * 建物類別<br>
	 * 
	 * @return String
	 */
	public String getBdTypeCode() {
		return this.bdTypeCode == null ? "" : this.bdTypeCode;
	}

	/**
	 * 建物類別<br>
	 * 
	 *
	 * @param bdTypeCode 建物類別
	 */
	public void setBdTypeCode(String bdTypeCode) {
		this.bdTypeCode = bdTypeCode;
	}

	/**
	 * 空白<br>
	 * 空白
	 * 
	 * @return String
	 */
	public String getFiller33() {
		return this.filler33 == null ? "" : this.filler33;
	}

	/**
	 * 空白<br>
	 * 空白
	 *
	 * @param filler33 空白
	 */
	public void setFiller33(String filler33) {
		this.filler33 = filler33;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月(民國年)
	 * 
	 * @return Integer
	 */
	public int getJcicDataYM() {
		return this.jcicDataYM;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月(民國年)
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
		return "JcicB092 [jcicB092Id=" + jcicB092Id + ", dataType=" + dataType + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", filler4=" + filler4 + ", evaAmt=" + evaAmt + ", evaDate="
				+ evaDate + ", loanLimitAmt=" + loanLimitAmt + ", settingDate=" + settingDate + ", monthSettingAmt=" + monthSettingAmt + ", settingSeq=" + settingSeq + ", settingAmt=" + settingAmt
				+ ", preSettingAmt=" + preSettingAmt + ", dispPrice=" + dispPrice + ", issueEndDate=" + issueEndDate

				+ ", zip=" + zip + ", insuFg=" + insuFg + ", lVITax=" + lVITax + ", lVITaxYearMonth=" + lVITaxYearMonth + ", contractPrice=" + contractPrice + ", contractDate=" + contractDate
				+ ", parkingTypeCode=" + parkingTypeCode + ", area=" + area + ", landOwnedArea=" + landOwnedArea + ", bdTypeCode=" + bdTypeCode + ", filler33=" + filler33 + ", jcicDataYM="
				+ jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
