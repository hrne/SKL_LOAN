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
 * ClBuilding 擔保品不動產建物檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClBuilding`")
public class ClBuilding implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5159009671094499831L;

	@EmbeddedId
	private ClBuildingId clBuildingId;

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

	// 路名
	@Column(name = "`Road`", length = 40)
	private String road;

	// 段
	@Column(name = "`Section`", length = 10)
	private String section;

	// 巷
	@Column(name = "`Alley`", length = 10)
	private String alley;

	// 弄
	@Column(name = "`Lane`", length = 10)
	private String lane;

	// 號
	@Column(name = "`Num`", length = 10)
	private String num;

	// 號之
	@Column(name = "`NumDash`", length = 10)
	private String numDash;

	// 樓
	@Column(name = "`Floor`", length = 10)
	private String floor;

	// 樓之
	@Column(name = "`FloorDash`", length = 10)
	private String floorDash;

	// 建號
	/* 建號格式為5-3,共8碼,BdNo1為前5碼, */
	@Column(name = "`BdNo1`", length = 5)
	private String bdNo1;

	// 建號(子號)
	/* 建號格式為5-3,共8碼,BdNo2為後3碼 */
	@Column(name = "`BdNo2`", length = 3)
	private String bdNo2;

	// 建物門牌
	@Column(name = "`BdLocation`", length = 150)
	private String bdLocation;

	// 建物主要用途
	/*
	 * 共用代碼檔01:住家用02:商業用03:工業用04:農業用05:農舍06:住商用07:住工用08:工商用09:共用部分10:列管標準廠房11:國民住宅12
	 * :市場攤位13:停車空間14:見使用執照15:見其它登記事項
	 */
	@Column(name = "`BdMainUseCode`", length = 2)
	private String bdMainUseCode;

	// 建物使用別
	/* 共用代碼檔1:自用2:閒置3:投資4:出租5:無償供他人使用6:其他 */
	@Column(name = "`BdUsageCode`", length = 1)
	private String bdUsageCode;

	// 建物主要建材
	/*
	 * 共用代碼檔01:磚水泥02:鋼筋水泥03:鋼骨水泥04:磚造05:鋼骨鋼筋混凝土造06:鋼筋混凝土加強磚造07:鋼造08:混凝土造09:磚鐵皮10:
	 * 石造11:木造12:鐵皮造13:壁式預鑄鋼筋混凝土造14:預力混凝土造15:土造16:土石造17:土磚石混合造18:加強磚19:竹造20:
	 * 鋼筋混凝土加強空心磚造21:土木造22:鋁架造30:見使用執照31:見其它登記事項
	 */
	@Column(name = "`BdMtrlCode`", length = 2)
	private String bdMtrlCode;

	// 建物類別
	/* 共用代碼檔01:公寓02:電梯大廈03:套房04:別墅05:透天厝06:樓中樓07:辦公08:店面09:廠房10:車位11:其它 */
	@Column(name = "`BdTypeCode`", length = 2)
	private String bdTypeCode;

	// 總樓層
	@Column(name = "`TotalFloor`")
	private int totalFloor = 0;

	// 擔保品所在樓層
	@Column(name = "`FloorNo`", length = 7)
	private String floorNo;

	// 擔保品所在樓層面積
	@Column(name = "`FloorArea`")
	private BigDecimal floorArea = new BigDecimal("0");

	// 鑑價單價/坪
	@Column(name = "`EvaUnitPrice`")
	private BigDecimal evaUnitPrice = new BigDecimal("0");

	// 屋頂結構
	/* 共用代碼檔01:平屋頂02:瓦屋頂03:石棉板屋頂04:鐵皮屋頂05:木板屋頂06:石棉瓦屋頂07:其他 */
	@Column(name = "`RoofStructureCode`", length = 2)
	private String roofStructureCode;

	// 建築完成日期
	@Column(name = "`BdDate`")
	private int bdDate = 0;

	// 附屬建物用途
	/* 共用代碼檔01:花台02:露台03:陽台04:其他 */
	@Column(name = "`BdSubUsageCode`", length = 2)
	private String bdSubUsageCode;

	// 附屬建物面積
	@Column(name = "`BdSubArea`")
	private BigDecimal bdSubArea = new BigDecimal("0");

	// 賣方統編
	@Column(name = "`SellerId`", length = 10)
	private String sellerId;

	// 賣方姓名
	@Column(name = "`SellerName`", length = 100)
	private String sellerName;

	// 買賣契約價格
	@Column(name = "`ContractPrice`")
	private BigDecimal contractPrice = new BigDecimal("0");

	// 買賣契約日期
	@Column(name = "`ContractDate`")
	private int contractDate = 0;

	// 停車位形式
	/* CdCode.ParkingTypeCode0:無車位1:坡道平面車位2:機械平面車位3:坡道機械車位4:機械機械車位5:庭院車位 */
	@Column(name = "`ParkingTypeCode`", length = 1)
	private String parkingTypeCode;

	// 登記面積(坪)
	@Column(name = "`ParkingArea`")
	private BigDecimal parkingArea = new BigDecimal("0");

	// 獨立產權車位註記
	/* Y:是N:否 */
	@Column(name = "`ParkingProperty`", length = 1)
	private String parkingProperty;

	// 房屋稅籍號碼
	@Column(name = "`HouseTaxNo`", length = 12)
	private String houseTaxNo;

	// 房屋取得日期
	@Column(name = "`HouseBuyDate`")
	private int houseBuyDate = 0;

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

	public ClBuildingId getClBuildingId() {
		return this.clBuildingId;
	}

	public void setClBuildingId(ClBuildingId clBuildingId) {
		this.clBuildingId = clBuildingId;
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
	 * 路名<br>
	 * 
	 * @return String
	 */
	public String getRoad() {
		return this.road == null ? "" : this.road;
	}

	/**
	 * 路名<br>
	 * 
	 *
	 * @param road 路名
	 */
	public void setRoad(String road) {
		this.road = road;
	}

	/**
	 * 段<br>
	 * 
	 * @return String
	 */
	public String getSection() {
		return this.section == null ? "" : this.section;
	}

	/**
	 * 段<br>
	 * 
	 *
	 * @param section 段
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * 巷<br>
	 * 
	 * @return String
	 */
	public String getAlley() {
		return this.alley == null ? "" : this.alley;
	}

	/**
	 * 巷<br>
	 * 
	 *
	 * @param alley 巷
	 */
	public void setAlley(String alley) {
		this.alley = alley;
	}

	/**
	 * 弄<br>
	 * 
	 * @return String
	 */
	public String getLane() {
		return this.lane == null ? "" : this.lane;
	}

	/**
	 * 弄<br>
	 * 
	 *
	 * @param lane 弄
	 */
	public void setLane(String lane) {
		this.lane = lane;
	}

	/**
	 * 號<br>
	 * 
	 * @return String
	 */
	public String getNum() {
		return this.num == null ? "" : this.num;
	}

	/**
	 * 號<br>
	 * 
	 *
	 * @param num 號
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * 號之<br>
	 * 
	 * @return String
	 */
	public String getNumDash() {
		return this.numDash == null ? "" : this.numDash;
	}

	/**
	 * 號之<br>
	 * 
	 *
	 * @param numDash 號之
	 */
	public void setNumDash(String numDash) {
		this.numDash = numDash;
	}

	/**
	 * 樓<br>
	 * 
	 * @return String
	 */
	public String getFloor() {
		return this.floor == null ? "" : this.floor;
	}

	/**
	 * 樓<br>
	 * 
	 *
	 * @param floor 樓
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * 樓之<br>
	 * 
	 * @return String
	 */
	public String getFloorDash() {
		return this.floorDash == null ? "" : this.floorDash;
	}

	/**
	 * 樓之<br>
	 * 
	 *
	 * @param floorDash 樓之
	 */
	public void setFloorDash(String floorDash) {
		this.floorDash = floorDash;
	}

	/**
	 * 建號<br>
	 * 建號格式為5-3,共8碼,BdNo1為前5碼,
	 * 
	 * @return String
	 */
	public String getBdNo1() {
		return this.bdNo1 == null ? "" : this.bdNo1;
	}

	/**
	 * 建號<br>
	 * 建號格式為5-3,共8碼,BdNo1為前5碼,
	 *
	 * @param bdNo1 建號
	 */
	public void setBdNo1(String bdNo1) {
		this.bdNo1 = bdNo1;
	}

	/**
	 * 建號(子號)<br>
	 * 建號格式為5-3,共8碼,BdNo2為後3碼
	 * 
	 * @return String
	 */
	public String getBdNo2() {
		return this.bdNo2 == null ? "" : this.bdNo2;
	}

	/**
	 * 建號(子號)<br>
	 * 建號格式為5-3,共8碼,BdNo2為後3碼
	 *
	 * @param bdNo2 建號(子號)
	 */
	public void setBdNo2(String bdNo2) {
		this.bdNo2 = bdNo2;
	}

	/**
	 * 建物門牌<br>
	 * 
	 * @return String
	 */
	public String getBdLocation() {
		return this.bdLocation == null ? "" : this.bdLocation;
	}

	/**
	 * 建物門牌<br>
	 * 
	 *
	 * @param bdLocation 建物門牌
	 */
	public void setBdLocation(String bdLocation) {
		this.bdLocation = bdLocation;
	}

	/**
	 * 建物主要用途<br>
	 * 共用代碼檔 01:住家用 02:商業用 03:工業用 04:農業用 05:農舍 06:住商用 07:住工用 08:工商用 09:共用部分
	 * 10:列管標準廠房 11:國民住宅 12:市場攤位 13:停車空間 14:見使用執照 15:見其它登記事項
	 * 
	 * @return String
	 */
	public String getBdMainUseCode() {
		return this.bdMainUseCode == null ? "" : this.bdMainUseCode;
	}

	/**
	 * 建物主要用途<br>
	 * 共用代碼檔 01:住家用 02:商業用 03:工業用 04:農業用 05:農舍 06:住商用 07:住工用 08:工商用 09:共用部分
	 * 10:列管標準廠房 11:國民住宅 12:市場攤位 13:停車空間 14:見使用執照 15:見其它登記事項
	 *
	 * @param bdMainUseCode 建物主要用途
	 */
	public void setBdMainUseCode(String bdMainUseCode) {
		this.bdMainUseCode = bdMainUseCode;
	}

	/**
	 * 建物使用別<br>
	 * 共用代碼檔 1:自用 2:閒置 3:投資 4:出租 5:無償供他人使用 6:其他
	 * 
	 * @return String
	 */
	public String getBdUsageCode() {
		return this.bdUsageCode == null ? "" : this.bdUsageCode;
	}

	/**
	 * 建物使用別<br>
	 * 共用代碼檔 1:自用 2:閒置 3:投資 4:出租 5:無償供他人使用 6:其他
	 *
	 * @param bdUsageCode 建物使用別
	 */
	public void setBdUsageCode(String bdUsageCode) {
		this.bdUsageCode = bdUsageCode;
	}

	/**
	 * 建物主要建材<br>
	 * 共用代碼檔 01:磚水泥 02:鋼筋水泥 03:鋼骨水泥 04:磚造 05:鋼骨鋼筋混凝土造 06:鋼筋混凝土加強磚造 07:鋼造 08:混凝土造
	 * 09:磚鐵皮 10:石造 11:木造 12:鐵皮造 13:壁式預鑄鋼筋混凝土造 14:預力混凝土造 15:土造 16:土石造 17:土磚石混合造
	 * 18:加強磚 19:竹造 20:鋼筋混凝土加強空心磚造 21:土木造 22:鋁架造 30:見使用執照 31:見其它登記事項
	 * 
	 * @return String
	 */
	public String getBdMtrlCode() {
		return this.bdMtrlCode == null ? "" : this.bdMtrlCode;
	}

	/**
	 * 建物主要建材<br>
	 * 共用代碼檔 01:磚水泥 02:鋼筋水泥 03:鋼骨水泥 04:磚造 05:鋼骨鋼筋混凝土造 06:鋼筋混凝土加強磚造 07:鋼造 08:混凝土造
	 * 09:磚鐵皮 10:石造 11:木造 12:鐵皮造 13:壁式預鑄鋼筋混凝土造 14:預力混凝土造 15:土造 16:土石造 17:土磚石混合造
	 * 18:加強磚 19:竹造 20:鋼筋混凝土加強空心磚造 21:土木造 22:鋁架造 30:見使用執照 31:見其它登記事項
	 *
	 * @param bdMtrlCode 建物主要建材
	 */
	public void setBdMtrlCode(String bdMtrlCode) {
		this.bdMtrlCode = bdMtrlCode;
	}

	/**
	 * 建物類別<br>
	 * 共用代碼檔 01:公寓 02:電梯大廈 03:套房 04:別墅 05:透天厝 06:樓中樓 07:辦公 08:店面 09:廠房 10:車位 11:其它
	 * 
	 * @return String
	 */
	public String getBdTypeCode() {
		return this.bdTypeCode == null ? "" : this.bdTypeCode;
	}

	/**
	 * 建物類別<br>
	 * 共用代碼檔 01:公寓 02:電梯大廈 03:套房 04:別墅 05:透天厝 06:樓中樓 07:辦公 08:店面 09:廠房 10:車位 11:其它
	 *
	 * @param bdTypeCode 建物類別
	 */
	public void setBdTypeCode(String bdTypeCode) {
		this.bdTypeCode = bdTypeCode;
	}

	/**
	 * 總樓層<br>
	 * 
	 * @return Integer
	 */
	public int getTotalFloor() {
		return this.totalFloor;
	}

	/**
	 * 總樓層<br>
	 * 
	 *
	 * @param totalFloor 總樓層
	 */
	public void setTotalFloor(int totalFloor) {
		this.totalFloor = totalFloor;
	}

	/**
	 * 擔保品所在樓層<br>
	 * 
	 * @return String
	 */
	public String getFloorNo() {
		return this.floorNo == null ? "" : this.floorNo;
	}

	/**
	 * 擔保品所在樓層<br>
	 * 
	 *
	 * @param floorNo 擔保品所在樓層
	 */
	public void setFloorNo(String floorNo) {
		this.floorNo = floorNo;
	}

	/**
	 * 擔保品所在樓層面積<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFloorArea() {
		return this.floorArea;
	}

	/**
	 * 擔保品所在樓層面積<br>
	 * 
	 *
	 * @param floorArea 擔保品所在樓層面積
	 */
	public void setFloorArea(BigDecimal floorArea) {
		this.floorArea = floorArea;
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
	 * 屋頂結構<br>
	 * 共用代碼檔 01:平屋頂 02:瓦屋頂 03:石棉板屋頂 04:鐵皮屋頂 05:木板屋頂 06:石棉瓦屋頂 07:其他
	 * 
	 * @return String
	 */
	public String getRoofStructureCode() {
		return this.roofStructureCode == null ? "" : this.roofStructureCode;
	}

	/**
	 * 屋頂結構<br>
	 * 共用代碼檔 01:平屋頂 02:瓦屋頂 03:石棉板屋頂 04:鐵皮屋頂 05:木板屋頂 06:石棉瓦屋頂 07:其他
	 *
	 * @param roofStructureCode 屋頂結構
	 */
	public void setRoofStructureCode(String roofStructureCode) {
		this.roofStructureCode = roofStructureCode;
	}

	/**
	 * 建築完成日期<br>
	 * 
	 * @return Integer
	 */
	public int getBdDate() {
		return StaticTool.bcToRoc(this.bdDate);
	}

	/**
	 * 建築完成日期<br>
	 * 
	 *
	 * @param bdDate 建築完成日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setBdDate(int bdDate) throws LogicException {
		this.bdDate = StaticTool.rocToBc(bdDate);
	}

	/**
	 * 附屬建物用途<br>
	 * 共用代碼檔 01:花台 02:露台 03:陽台 04:其他
	 * 
	 * @return String
	 */
	public String getBdSubUsageCode() {
		return this.bdSubUsageCode == null ? "" : this.bdSubUsageCode;
	}

	/**
	 * 附屬建物用途<br>
	 * 共用代碼檔 01:花台 02:露台 03:陽台 04:其他
	 *
	 * @param bdSubUsageCode 附屬建物用途
	 */
	public void setBdSubUsageCode(String bdSubUsageCode) {
		this.bdSubUsageCode = bdSubUsageCode;
	}

	/**
	 * 附屬建物面積<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBdSubArea() {
		return this.bdSubArea;
	}

	/**
	 * 附屬建物面積<br>
	 * 
	 *
	 * @param bdSubArea 附屬建物面積
	 */
	public void setBdSubArea(BigDecimal bdSubArea) {
		this.bdSubArea = bdSubArea;
	}

	/**
	 * 賣方統編<br>
	 * 
	 * @return String
	 */
	public String getSellerId() {
		return this.sellerId == null ? "" : this.sellerId;
	}

	/**
	 * 賣方統編<br>
	 * 
	 *
	 * @param sellerId 賣方統編
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * 賣方姓名<br>
	 * 
	 * @return String
	 */
	public String getSellerName() {
		return this.sellerName == null ? "" : this.sellerName;
	}

	/**
	 * 賣方姓名<br>
	 * 
	 *
	 * @param sellerName 賣方姓名
	 */
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	/**
	 * 買賣契約價格<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getContractPrice() {
		return this.contractPrice;
	}

	/**
	 * 買賣契約價格<br>
	 * 
	 *
	 * @param contractPrice 買賣契約價格
	 */
	public void setContractPrice(BigDecimal contractPrice) {
		this.contractPrice = contractPrice;
	}

	/**
	 * 買賣契約日期<br>
	 * 
	 * @return Integer
	 */
	public int getContractDate() {
		return StaticTool.bcToRoc(this.contractDate);
	}

	/**
	 * 買賣契約日期<br>
	 * 
	 *
	 * @param contractDate 買賣契約日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setContractDate(int contractDate) throws LogicException {
		this.contractDate = StaticTool.rocToBc(contractDate);
	}

	/**
	 * 停車位形式<br>
	 * CdCode.ParkingTypeCode 0:無車位 1:坡道平面車位 2:機械平面車位 3:坡道機械車位 4:機械機械車位 5:庭院車位
	 * 
	 * @return String
	 */
	public String getParkingTypeCode() {
		return this.parkingTypeCode == null ? "" : this.parkingTypeCode;
	}

	/**
	 * 停車位形式<br>
	 * CdCode.ParkingTypeCode 0:無車位 1:坡道平面車位 2:機械平面車位 3:坡道機械車位 4:機械機械車位 5:庭院車位
	 *
	 * @param parkingTypeCode 停車位形式
	 */
	public void setParkingTypeCode(String parkingTypeCode) {
		this.parkingTypeCode = parkingTypeCode;
	}

	/**
	 * 登記面積(坪)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getParkingArea() {
		return this.parkingArea;
	}

	/**
	 * 登記面積(坪)<br>
	 * 
	 *
	 * @param parkingArea 登記面積(坪)
	 */
	public void setParkingArea(BigDecimal parkingArea) {
		this.parkingArea = parkingArea;
	}

	/**
	 * 獨立產權車位註記<br>
	 * Y:是 N:否
	 * 
	 * @return String
	 */
	public String getParkingProperty() {
		return this.parkingProperty == null ? "" : this.parkingProperty;
	}

	/**
	 * 獨立產權車位註記<br>
	 * Y:是 N:否
	 *
	 * @param parkingProperty 獨立產權車位註記
	 */
	public void setParkingProperty(String parkingProperty) {
		this.parkingProperty = parkingProperty;
	}

	/**
	 * 房屋稅籍號碼<br>
	 * 
	 * @return String
	 */
	public String getHouseTaxNo() {
		return this.houseTaxNo == null ? "" : this.houseTaxNo;
	}

	/**
	 * 房屋稅籍號碼<br>
	 * 
	 *
	 * @param houseTaxNo 房屋稅籍號碼
	 */
	public void setHouseTaxNo(String houseTaxNo) {
		this.houseTaxNo = houseTaxNo;
	}

	/**
	 * 房屋取得日期<br>
	 * 
	 * @return Integer
	 */
	public int getHouseBuyDate() {
		return StaticTool.bcToRoc(this.houseBuyDate);
	}

	/**
	 * 房屋取得日期<br>
	 * 
	 *
	 * @param houseBuyDate 房屋取得日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setHouseBuyDate(int houseBuyDate) throws LogicException {
		this.houseBuyDate = StaticTool.rocToBc(houseBuyDate);
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
		return "ClBuilding [clBuildingId=" + clBuildingId + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", irCode=" + irCode + ", road=" + road + ", section=" + section + ", alley=" + alley
				+ ", lane=" + lane + ", num=" + num + ", numDash=" + numDash + ", floor=" + floor + ", floorDash=" + floorDash + ", bdNo1=" + bdNo1 + ", bdNo2=" + bdNo2 + ", bdLocation=" + bdLocation
				+ ", bdMainUseCode=" + bdMainUseCode + ", bdUsageCode=" + bdUsageCode + ", bdMtrlCode=" + bdMtrlCode + ", bdTypeCode=" + bdTypeCode + ", totalFloor=" + totalFloor + ", floorNo="
				+ floorNo + ", floorArea=" + floorArea + ", evaUnitPrice=" + evaUnitPrice + ", roofStructureCode=" + roofStructureCode + ", bdDate=" + bdDate + ", bdSubUsageCode=" + bdSubUsageCode
				+ ", bdSubArea=" + bdSubArea + ", sellerId=" + sellerId + ", sellerName=" + sellerName + ", contractPrice=" + contractPrice + ", contractDate=" + contractDate + ", parkingTypeCode="
				+ parkingTypeCode + ", parkingArea=" + parkingArea + ", parkingProperty=" + parkingProperty + ", houseTaxNo=" + houseTaxNo + ", houseBuyDate=" + houseBuyDate + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
