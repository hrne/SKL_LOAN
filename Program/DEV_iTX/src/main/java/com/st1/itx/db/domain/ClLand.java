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
 * ClLand 擔保品不動產土地檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClLand`")
public class ClLand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5791258907580287267L;

	@EmbeddedId
	private ClLandId clLandId;

	// 擔保品代號1
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	/* 擔保品代號檔CdCl */
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`", insertable = false, updatable = false)
	private int clNo = 0;

	// 土地序號
	/* 房地:從1起編土地:固定000 */
	@Column(name = "`LandSeq`", insertable = false, updatable = false)
	private int landSeq = 0;

	// 縣市
	/* 地區別與鄉鎮區對照檔CdArea */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode;

	// 鄉鎮市區
	/* 地區別與鄉鎮區對照檔CdArea */
	@Column(name = "`AreaCode`", length = 3)
	private String areaCode;

	// 段小段代碼
	/* 地段代碼檔CdLandSection */
	@Column(name = "`IrCode`", length = 5)
	private String irCode;

	// 地號
	/* 地號格式為4-4 */
	@Column(name = "`LandNo1`", length = 4)
	private String landNo1;

	// 地號(子號)
	/* 地號格式為4-4 */
	@Column(name = "`LandNo2`", length = 4)
	private String landNo2;

	// 土地座落
	@Column(name = "`LandLocation`", length = 150)
	private String landLocation;

	// 地目
	/*
	 * 共用代碼檔01:建02:田03:旱04:雜05:水06:道07:溜08:原09:林10:養11:墓12:祠13:鐵14:暫未編定15:公16:堤17:
	 * 池18:溝19:礦
	 */
	@Column(name = "`LandCode`", length = 2)
	private String landCode;

	// 面積
	@Column(name = "`Area`")
	private BigDecimal area = new BigDecimal("0");

	// 土地使用區分
	/*
	 * 共用代碼檔01:特定農業區02:一般農業區03:鄉村區04:工業區05:森林區06:山坡地保育區07:風景區08:特定專用區09:國家公園區10:
	 * 住宅區11:商業區12:行政區13:工業區14:文教區15:農業區16:風景區17:保護區18:水岸發展區19:漁業區20:倉儲區21:保存區22:
	 * 葬儀業區23:特定專用區24:其他分區25:道路26:公園27:綠地28:廣場29:兒童遊樂場30:民用航空站31:停車場32:河道33:港埠34:
	 * 學校35:社教機構36:體育場37:市場38:醫療衛生機構39:機關40:公用事業41:綠帶42:加油站43:其他公共設施44:道路保留地45:
	 * 公園保留地46:綠地保留地47:廣場保留地48:兒童樂園場保留地49:民用航空站保留地50:停車場保留地51:河道保留地52:港埠保留地53:
	 * 學校保留地54:社教機構保留地55:體育場保留地56:市場保留地57:醫療衛生機構保留地58:機關保留地59:公用事業保留地60:加油站保留地61:
	 * 其他保留地
	 */
	@Column(name = "`LandZoningCode`", length = 2)
	private String landZoningCode;

	// 使用地類別
	/*
	 * 01:甲種建築用地02:乙種建築用地03:丙種建築用地04:丁種建築用地05:農牧用地06:礦業用地07:交通用地08:水利用地09:遊憩用地10:
	 * 古蹟保存用地11:生態保護用地12:國土保安13:墳墓用地14:特定目的事業用地15:鹽業用地16:窯業用地17:林業用地18:養殖用地19:都市用地20
	 * :暫未編定
	 */
	@Column(name = "`LandUsageType`", length = 2)
	private String landUsageType;

	// 土地使用別
	/* 共用代碼檔1:自用2:閒置3:投資4:出租5:無償供他人使用6:其他 */
	@Column(name = "`LandUsageCode`", length = 2)
	private String landUsageCode;

	// 公告土地現值
	@Column(name = "`PostedLandValue`")
	private BigDecimal postedLandValue = new BigDecimal("0");

	// 公告土地現值年月
	@Column(name = "`PostedLandValueYearMonth`")
	private int postedLandValueYearMonth = 0;

	// 移轉年度
	@Column(name = "`TransferedYear`")
	private int transferedYear = 0;

	// 前次移轉金額
	@Column(name = "`LastTransferedAmt`")
	private BigDecimal lastTransferedAmt = new BigDecimal("0");

	// 土地增值稅
	/* LandValueIncrementTax */
	@Column(name = "`LVITax`")
	private BigDecimal lVITax = new BigDecimal("0");

	// 土地增值稅年月
	@Column(name = "`LVITaxYearMonth`")
	private int lVITaxYearMonth = 0;

	// 鑑價單價/坪
	@Column(name = "`EvaUnitPrice`")
	private BigDecimal evaUnitPrice = new BigDecimal("0");

	// 土地租約起日
	@Column(name = "`LandRentStartDate`")
	private int landRentStartDate = 0;

	// 土地租約到期日
	@Column(name = "`LandRentEndDate`")
	private int landRentEndDate = 0;

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

	public ClLandId getClLandId() {
		return this.clLandId;
	}

	public void setClLandId(ClLandId clLandId) {
		this.clLandId = clLandId;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號檔CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 擔保品代號檔CdCl
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號檔CdCl
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 擔保品代號檔CdCl
	 *
	 * @param clCode2 擔保品代號2
	 */
	public void setClCode2(int clCode2) {
		this.clCode2 = clCode2;
	}

	/**
	 * 擔保品編號<br>
	 * 
	 * @return Integer
	 */
	public int getClNo() {
		return this.clNo;
	}

	/**
	 * 擔保品編號<br>
	 * 
	 *
	 * @param clNo 擔保品編號
	 */
	public void setClNo(int clNo) {
		this.clNo = clNo;
	}

	/**
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 * 
	 * @return Integer
	 */
	public int getLandSeq() {
		return this.landSeq;
	}

	/**
	 * 土地序號<br>
	 * 房地:從1起編 土地:固定000
	 *
	 * @param landSeq 土地序號
	 */
	public void setLandSeq(int landSeq) {
		this.landSeq = landSeq;
	}

	/**
	 * 縣市<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 縣市<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 *
	 * @param cityCode 縣市
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 鄉鎮市區<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 鄉鎮市區<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 *
	 * @param areaCode 鄉鎮市區
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 段小段代碼<br>
	 * 地段代碼檔CdLandSection
	 * 
	 * @return String
	 */
	public String getIrCode() {
		return this.irCode == null ? "" : this.irCode;
	}

	/**
	 * 段小段代碼<br>
	 * 地段代碼檔CdLandSection
	 *
	 * @param irCode 段小段代碼
	 */
	public void setIrCode(String irCode) {
		this.irCode = irCode;
	}

	/**
	 * 地號<br>
	 * 地號格式為4-4
	 * 
	 * @return String
	 */
	public String getLandNo1() {
		return this.landNo1 == null ? "" : this.landNo1;
	}

	/**
	 * 地號<br>
	 * 地號格式為4-4
	 *
	 * @param landNo1 地號
	 */
	public void setLandNo1(String landNo1) {
		this.landNo1 = landNo1;
	}

	/**
	 * 地號(子號)<br>
	 * 地號格式為4-4
	 * 
	 * @return String
	 */
	public String getLandNo2() {
		return this.landNo2 == null ? "" : this.landNo2;
	}

	/**
	 * 地號(子號)<br>
	 * 地號格式為4-4
	 *
	 * @param landNo2 地號(子號)
	 */
	public void setLandNo2(String landNo2) {
		this.landNo2 = landNo2;
	}

	/**
	 * 土地座落<br>
	 * 
	 * @return String
	 */
	public String getLandLocation() {
		return this.landLocation == null ? "" : this.landLocation;
	}

	/**
	 * 土地座落<br>
	 * 
	 *
	 * @param landLocation 土地座落
	 */
	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	/**
	 * 地目<br>
	 * 共用代碼檔 01:建 02:田 03:旱 04:雜 05:水 06:道 07:溜 08:原 09:林 10:養 11:墓 12:祠 13:鐵
	 * 14:暫未編定 15:公 16:堤 17:池 18:溝 19:礦
	 * 
	 * @return String
	 */
	public String getLandCode() {
		return this.landCode == null ? "" : this.landCode;
	}

	/**
	 * 地目<br>
	 * 共用代碼檔 01:建 02:田 03:旱 04:雜 05:水 06:道 07:溜 08:原 09:林 10:養 11:墓 12:祠 13:鐵
	 * 14:暫未編定 15:公 16:堤 17:池 18:溝 19:礦
	 *
	 * @param landCode 地目
	 */
	public void setLandCode(String landCode) {
		this.landCode = landCode;
	}

	/**
	 * 面積<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getArea() {
		return this.area;
	}

	/**
	 * 面積<br>
	 * 
	 *
	 * @param area 面積
	 */
	public void setArea(BigDecimal area) {
		this.area = area;
	}

	/**
	 * 土地使用區分<br>
	 * 共用代碼檔 01:特定農業區 02:一般農業區 03:鄉村區 04:工業區 05:森林區 06:山坡地保育區 07:風景區 08:特定專用區
	 * 09:國家公園區 10:住宅區 11:商業區 12:行政區 13:工業區 14:文教區 15:農業區 16:風景區 17:保護區 18:水岸發展區
	 * 19:漁業區 20:倉儲區 21:保存區 22:葬儀業區 23:特定專用區 24:其他分區 25:道路 26:公園 27:綠地 28:廣場
	 * 29:兒童遊樂場 30:民用航空站 31:停車場 32:河道 33:港埠 34:學校 35:社教機構 36:體育場 37:市場 38:醫療衛生機構
	 * 39:機關 40:公用事業 41:綠帶 42:加油站 43:其他公共設施 44:道路保留地 45:公園保留地 46:綠地保留地 47:廣場保留地
	 * 48:兒童樂園場保留地 49:民用航空站保留地 50:停車場保留地 51:河道保留地 52:港埠保留地 53:學校保留地 54:社教機構保留地
	 * 55:體育場保留地 56:市場保留地 57:醫療衛生機構保留地 58:機關保留地 59:公用事業保留地 60:加油站保留地 61:其他保留地
	 * 
	 * @return String
	 */
	public String getLandZoningCode() {
		return this.landZoningCode == null ? "" : this.landZoningCode;
	}

	/**
	 * 土地使用區分<br>
	 * 共用代碼檔 01:特定農業區 02:一般農業區 03:鄉村區 04:工業區 05:森林區 06:山坡地保育區 07:風景區 08:特定專用區
	 * 09:國家公園區 10:住宅區 11:商業區 12:行政區 13:工業區 14:文教區 15:農業區 16:風景區 17:保護區 18:水岸發展區
	 * 19:漁業區 20:倉儲區 21:保存區 22:葬儀業區 23:特定專用區 24:其他分區 25:道路 26:公園 27:綠地 28:廣場
	 * 29:兒童遊樂場 30:民用航空站 31:停車場 32:河道 33:港埠 34:學校 35:社教機構 36:體育場 37:市場 38:醫療衛生機構
	 * 39:機關 40:公用事業 41:綠帶 42:加油站 43:其他公共設施 44:道路保留地 45:公園保留地 46:綠地保留地 47:廣場保留地
	 * 48:兒童樂園場保留地 49:民用航空站保留地 50:停車場保留地 51:河道保留地 52:港埠保留地 53:學校保留地 54:社教機構保留地
	 * 55:體育場保留地 56:市場保留地 57:醫療衛生機構保留地 58:機關保留地 59:公用事業保留地 60:加油站保留地 61:其他保留地
	 *
	 * @param landZoningCode 土地使用區分
	 */
	public void setLandZoningCode(String landZoningCode) {
		this.landZoningCode = landZoningCode;
	}

	/**
	 * 使用地類別<br>
	 * 01:甲種建築用地 02:乙種建築用地 03:丙種建築用地 04:丁種建築用地 05:農牧用地 06:礦業用地 07:交通用地 08:水利用地
	 * 09:遊憩用地 10:古蹟保存用地 11:生態保護用地 12:國土保安 13:墳墓用地 14:特定目的事業用地 15:鹽業用地 16:窯業用地
	 * 17:林業用地 18:養殖用地 19:都市用地 20:暫未編定
	 * 
	 * @return String
	 */
	public String getLandUsageType() {
		return this.landUsageType == null ? "" : this.landUsageType;
	}

	/**
	 * 使用地類別<br>
	 * 01:甲種建築用地 02:乙種建築用地 03:丙種建築用地 04:丁種建築用地 05:農牧用地 06:礦業用地 07:交通用地 08:水利用地
	 * 09:遊憩用地 10:古蹟保存用地 11:生態保護用地 12:國土保安 13:墳墓用地 14:特定目的事業用地 15:鹽業用地 16:窯業用地
	 * 17:林業用地 18:養殖用地 19:都市用地 20:暫未編定
	 *
	 * @param landUsageType 使用地類別
	 */
	public void setLandUsageType(String landUsageType) {
		this.landUsageType = landUsageType;
	}

	/**
	 * 土地使用別<br>
	 * 共用代碼檔 1:自用 2:閒置 3:投資 4:出租 5:無償供他人使用 6:其他
	 * 
	 * @return String
	 */
	public String getLandUsageCode() {
		return this.landUsageCode == null ? "" : this.landUsageCode;
	}

	/**
	 * 土地使用別<br>
	 * 共用代碼檔 1:自用 2:閒置 3:投資 4:出租 5:無償供他人使用 6:其他
	 *
	 * @param landUsageCode 土地使用別
	 */
	public void setLandUsageCode(String landUsageCode) {
		this.landUsageCode = landUsageCode;
	}

	/**
	 * 公告土地現值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPostedLandValue() {
		return this.postedLandValue;
	}

	/**
	 * 公告土地現值<br>
	 * 
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
	 * 移轉年度<br>
	 * 
	 * @return Integer
	 */
	public int getTransferedYear() {
		return this.transferedYear;
	}

	/**
	 * 移轉年度<br>
	 * 
	 *
	 * @param transferedYear 移轉年度
	 */
	public void setTransferedYear(int transferedYear) {
		this.transferedYear = transferedYear;
	}

	/**
	 * 前次移轉金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLastTransferedAmt() {
		return this.lastTransferedAmt;
	}

	/**
	 * 前次移轉金額<br>
	 * 
	 *
	 * @param lastTransferedAmt 前次移轉金額
	 */
	public void setLastTransferedAmt(BigDecimal lastTransferedAmt) {
		this.lastTransferedAmt = lastTransferedAmt;
	}

	/**
	 * 土地增值稅<br>
	 * LandValueIncrementTax
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLVITax() {
		return this.lVITax;
	}

	/**
	 * 土地增值稅<br>
	 * LandValueIncrementTax
	 *
	 * @param lVITax 土地增值稅
	 */
	public void setLVITax(BigDecimal lVITax) {
		this.lVITax = lVITax;
	}

	/**
	 * 土地增值稅年月<br>
	 * 
	 * @return Integer
	 */
	public int getLVITaxYearMonth() {
		return this.lVITaxYearMonth;
	}

	/**
	 * 土地增值稅年月<br>
	 * 
	 *
	 * @param lVITaxYearMonth 土地增值稅年月
	 */
	public void setLVITaxYearMonth(int lVITaxYearMonth) {
		this.lVITaxYearMonth = lVITaxYearMonth;
	}

	/**
	 * 鑑價單價/坪<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getEvaUnitPrice() {
		return this.evaUnitPrice;
	}

	/**
	 * 鑑價單價/坪<br>
	 * 
	 *
	 * @param evaUnitPrice 鑑價單價/坪
	 */
	public void setEvaUnitPrice(BigDecimal evaUnitPrice) {
		this.evaUnitPrice = evaUnitPrice;
	}

	/**
	 * 土地租約起日<br>
	 * 
	 * @return Integer
	 */
	public int getLandRentStartDate() {
		return StaticTool.bcToRoc(this.landRentStartDate);
	}

	/**
	 * 土地租約起日<br>
	 * 
	 *
	 * @param landRentStartDate 土地租約起日
	 * @throws LogicException when Date Is Warn
	 */
	public void setLandRentStartDate(int landRentStartDate) throws LogicException {
		this.landRentStartDate = StaticTool.rocToBc(landRentStartDate);
	}

	/**
	 * 土地租約到期日<br>
	 * 
	 * @return Integer
	 */
	public int getLandRentEndDate() {
		return StaticTool.bcToRoc(this.landRentEndDate);
	}

	/**
	 * 土地租約到期日<br>
	 * 
	 *
	 * @param landRentEndDate 土地租約到期日
	 * @throws LogicException when Date Is Warn
	 */
	public void setLandRentEndDate(int landRentEndDate) throws LogicException {
		this.landRentEndDate = StaticTool.rocToBc(landRentEndDate);
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
		return "ClLand [clLandId=" + clLandId + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", irCode=" + irCode + ", landNo1=" + landNo1 + ", landNo2=" + landNo2 + ", landLocation="
				+ landLocation + ", landCode=" + landCode + ", area=" + area + ", landZoningCode=" + landZoningCode + ", landUsageType=" + landUsageType + ", landUsageCode=" + landUsageCode
				+ ", postedLandValue=" + postedLandValue + ", postedLandValueYearMonth=" + postedLandValueYearMonth + ", transferedYear=" + transferedYear + ", lastTransferedAmt=" + lastTransferedAmt
				+ ", lVITax=" + lVITax + ", lVITaxYearMonth=" + lVITaxYearMonth + ", evaUnitPrice=" + evaUnitPrice + ", landRentStartDate=" + landRentStartDate + ", landRentEndDate=" + landRentEndDate
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
