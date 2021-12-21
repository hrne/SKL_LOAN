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
 * JcicB096 聯徵不動產擔保品明細-地號附加檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB096`")
public class JcicB096 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2936938106746416903L;

	@EmbeddedId
	private JcicB096Id jcicB096Id;

	// 資料日期
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 資料別
	/* 96:聯徵不動產擔保品明細地號附加檔資料 */
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

	// 土地序號
	@Column(name = "`LandSeq`", insertable = false, updatable = false)
	private int landSeq = 0;

	// 擔保品所有權人或代表人IDN/BAN
	/* 左靠，擔保品所有權人或代表人身份證或統一證號 */
	@Column(name = "`OwnerId`", length = 10, insertable = false, updatable = false)
	private String ownerId;

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

	// 地目
	@Column(name = "`LandCode`", length = 1)
	private String landCode;

	// 面積
	/* 右靠左補0，單位:平方公尺，填至小數點2位;面積小於0.01者，本欄於出檔時填"X"，右靠左補空白 */
	@Column(name = "`Area`")
	private BigDecimal area = new BigDecimal("0");

	// 使用分區
	@Column(name = "`LandZoningCode`", length = 1)
	private String landZoningCode;

	// 使用地類別
	@Column(name = "`LandUsageType`", length = 2)
	private String landUsageType;

	// 公告土地現值
	/* 右靠左補0，單位:新台幣千元，不足1千者填1，超過1千者四捨五入；空白填9999999999 */
	@Column(name = "`PostedLandValue`")
	private BigDecimal postedLandValue = new BigDecimal("0");

	// 公告土地現值年月
	@Column(name = "`PostedLandValueYearMonth`")
	private int postedLandValueYearMonth = 0;

	// 空白
	/* 空白 */
	@Column(name = "`Filler18`", length = 30)
	private String filler18;

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

	public JcicB096Id getJcicB096Id() {
		return this.jcicB096Id;
	}

	public void setJcicB096Id(JcicB096Id jcicB096Id) {
		this.jcicB096Id = jcicB096Id;
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
	 * 96:聯徵不動產擔保品明細地號附加檔資料
	 * 
	 * @return String
	 */
	public String getDataType() {
		return this.dataType == null ? "" : this.dataType;
	}

	/**
	 * 資料別<br>
	 * 96:聯徵不動產擔保品明細地號附加檔資料
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
	 * 土地序號<br>
	 * 
	 * @return Integer
	 */
	public int getLandSeq() {
		return this.landSeq;
	}

	/**
	 * 土地序號<br>
	 * 
	 *
	 * @param landSeq 土地序號
	 */
	public void setLandSeq(int landSeq) {
		this.landSeq = landSeq;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * 左靠，擔保品所有權人或代表人身份證或統一證號
	 * 
	 * @return String
	 */
	public String getOwnerId() {
		return this.ownerId == null ? "" : this.ownerId;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * 左靠，擔保品所有權人或代表人身份證或統一證號
	 *
	 * @param ownerId 擔保品所有權人或代表人IDN/BAN
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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
	 * 地目<br>
	 * 
	 * @return String
	 */
	public String getLandCode() {
		return this.landCode == null ? "" : this.landCode;
	}

	/**
	 * 地目<br>
	 * 
	 *
	 * @param landCode 地目
	 */
	public void setLandCode(String landCode) {
		this.landCode = landCode;
	}

	/**
	 * 面積<br>
	 * 右靠左補0，單位:平方公尺，填至小數點2位;面積小於0.01者，本欄於出檔時填"X"，右靠左補空白
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getArea() {
		return this.area;
	}

	/**
	 * 面積<br>
	 * 右靠左補0，單位:平方公尺，填至小數點2位;面積小於0.01者，本欄於出檔時填"X"，右靠左補空白
	 *
	 * @param area 面積
	 */
	public void setArea(BigDecimal area) {
		this.area = area;
	}

	/**
	 * 使用分區<br>
	 * 
	 * @return String
	 */
	public String getLandZoningCode() {
		return this.landZoningCode == null ? "" : this.landZoningCode;
	}

	/**
	 * 使用分區<br>
	 * 
	 *
	 * @param landZoningCode 使用分區
	 */
	public void setLandZoningCode(String landZoningCode) {
		this.landZoningCode = landZoningCode;
	}

	/**
	 * 使用地類別<br>
	 * 
	 * @return String
	 */
	public String getLandUsageType() {
		return this.landUsageType == null ? "" : this.landUsageType;
	}

	/**
	 * 使用地類別<br>
	 * 
	 *
	 * @param landUsageType 使用地類別
	 */
	public void setLandUsageType(String landUsageType) {
		this.landUsageType = landUsageType;
	}

	/**
	 * 公告土地現值<br>
	 * 右靠左補0，單位:新台幣千元，不足1千者填1，超過1千者四捨五入；空白填9999999999
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPostedLandValue() {
		return this.postedLandValue;
	}

	/**
	 * 公告土地現值<br>
	 * 右靠左補0，單位:新台幣千元，不足1千者填1，超過1千者四捨五入；空白填9999999999
	 *
	 * @param postedLandValue 公告土地現值
	 */
	public void setPostedLandValue(BigDecimal postedLandValue) {
		this.postedLandValue = postedLandValue;
	}

	/**
	 * 公告土地現值年月<br>
	 * 
	 * @return Integer
	 */
	public int getPostedLandValueYearMonth() {
		return this.postedLandValueYearMonth;
	}

	/**
	 * 公告土地現值年月<br>
	 * 
	 *
	 * @param postedLandValueYearMonth 公告土地現值年月
	 */
	public void setPostedLandValueYearMonth(int postedLandValueYearMonth) {
		this.postedLandValueYearMonth = postedLandValueYearMonth;
	}

	/**
	 * 空白<br>
	 * 空白
	 * 
	 * @return String
	 */
	public String getFiller18() {
		return this.filler18 == null ? "" : this.filler18;
	}

	/**
	 * 空白<br>
	 * 空白
	 *
	 * @param filler18 空白
	 */
	public void setFiller18(String filler18) {
		this.filler18 = filler18;
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
		return "JcicB096 [jcicB096Id=" + jcicB096Id + ", dataType=" + dataType + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", filler4=" + filler4

				+ ", landCode=" + landCode + ", area=" + area + ", landZoningCode=" + landZoningCode + ", landUsageType=" + landUsageType + ", postedLandValue=" + postedLandValue
				+ ", postedLandValueYearMonth=" + postedLandValueYearMonth + ", filler18=" + filler18 + ", jcicDataYM=" + jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
