package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R43")
@Scope("prototype")
/**
 * L2R43 : 取建物或土地所有權人姓名
 * 
 * @author ST1 Chih Wei
 * @version 1.0.0
 */
public class L2R43 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public ClBuildingService sClBuildingService;
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;
	@Autowired
	public ClLandService sClLandService;
	@Autowired
	public ClLandOwnerService sClLandOwnerService;
	@Autowired
	public CdClService sCdClService;
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String iCustId;
	private int iClCode1;
	private int iClCode2;
	private int clNo;
	private String bdLocation;
	private CustMain tCustMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R43 ");
		this.totaVo.init(titaVo);
		// tita
		iCustId = titaVo.getParam("RimCustId");
		iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		clNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		tCustMain = new CustMain();
		this.totaVo.putParam("L2r43CustName", "");
		tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);

		if (tCustMain != null) {
			this.totaVo.putParam("L2r43CustName", tCustMain.getCustName());
		}
		this.info("CustName = " + totaVo.get("L2r43CustName"));
		//
		int clNo = 0;

//		 擔保品編號唯一性規則
//		  1. 房地擔保品：
//		         建物門牌+建物所有權人(多)
//		  2. 土地擔保品：
//			  土地座落+土地所有權人(多)

		if (iFunCd == 1 && clNo == 0) {
			// 擔保品編號唯一性規則
			if (iClCode1 == 1) {
				bdLocation = getBdLocation(titaVo);
				clNo = getBuildingClNo(titaVo);

			} else {
				clNo = getLandClNo(titaVo);
			}
		}
		this.totaVo.putParam("L2r43ClNo", clNo);

		this.info("CustName = " + totaVo.get("L2r43CustName"));
		this.info("ClNo = " + totaVo.get("L2r43ClNo"));
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 房地擔保品編號
	private int getBuildingClNo(TitaVo titaVo) throws LogicException {
//	 擔保品編號唯一性規則
//	  1. 房地擔保品：
//	         建物門牌+建物所有權人(多)
//	  2. 土地擔保品：
//		  土地座落+土地所有權人(多)
		int clNo = 0;
		Slice<ClBuilding> slClBuilding = sClBuildingService.findBdLocationEq(bdLocation, this.index, Integer.MAX_VALUE, titaVo);
		List<ClBuilding> lClBuilding = slClBuilding == null ? null : slClBuilding.getContent();
		if (lClBuilding != null) {
			for (ClBuilding cl : lClBuilding) {
				this.info("ClCode1 =" + iClCode1);
				this.info("cl.getClCode1() =" + cl.getClCode1());
				this.info("iClCode2 =" + iClCode2);
				this.info("cl.getClCode2() =" + cl.getClCode2());
				if (cl.getClCode1() == iClCode1 && cl.getClCode2() == iClCode2) {
					if (checkBuildingOwner(cl.getClNo(), titaVo)) {
						clNo = cl.getClNo();
						break;
					}
				}
			}
		}
		return clNo;
	}

	// 判斷建物所有權人是否相同
	private boolean checkBuildingOwner(int clNo, TitaVo titaVo) throws LogicException {
// 有一個有權人相同即視為相同
		boolean isSameOwner = false;
//		ClBuildingOwner tClBuildingOwner = sClBuildingOwnerService.findById(new ClBuildingOwnerId(iClCode1, iClCode2, clNo, iCustId), titaVo);
		ClBuildingOwner tClBuildingOwner = sClBuildingOwnerService.findById(new ClBuildingOwnerId(iClCode1, iClCode2, clNo, tCustMain.getCustUKey()), titaVo);
		if (tClBuildingOwner != null) {
			isSameOwner = true;
		}
		return isSameOwner;
	}

	// 土地擔保品編號
	private int getLandClNo(TitaVo titaVo) throws LogicException {
		int clNo = 0;
		Slice<ClLand> slClLand = sClLandService.findLandLocationEq(titaVo.getParam("LandLocation"), this.index, Integer.MAX_VALUE, titaVo);
		List<ClLand> lClLand = slClLand == null ? null : slClLand.getContent();
		if (lClLand != null) {
			for (ClLand cl : lClLand) {
				if (cl.getClCode1() == iClCode1 && cl.getClCode2() == iClCode2) {
					if (checkLandOwner(cl.getClNo(), titaVo)) {
						clNo = cl.getClNo();
						break;
					}
				}
			}
		}
		return clNo;
	}

	// 判斷土地所有權人是否相同
	private boolean checkLandOwner(int clNo, TitaVo titaVo) throws LogicException {
// 有一個有權人相同即視為相同
		boolean isSameOwner = false;
//		ClLandOwner tClLandOwner = sClLandOwnerService.findById(new ClLandOwnerId(iClCode1, iClCode2, clNo, 0, iCustId), titaVo);
		ClLandOwner tClLandOwner = sClLandOwnerService.findById(new ClLandOwnerId(iClCode1, iClCode2, clNo, 0, tCustMain.getCustUKey()), titaVo);
		if (tClLandOwner != null) {
			isSameOwner = true;
		}
		return isSameOwner;
	}

	private String getBdLocation(TitaVo titaVo) throws LogicException {

		String result = "";

		String CityCode = titaVo.getParam("CityCode").trim();

		String CityItem = "";
		CityItem = sCdCityService.findById(CityCode).getCityItem().trim();

		String AreaCode = titaVo.getParam("AreaCode").trim();

		String AreaItem = "";
		CdAreaId cdAreaId = new CdAreaId(CityCode, AreaCode);
		AreaItem = sCdAreaService.findById(cdAreaId).getAreaItem().trim();

		String Road = titaVo.getParam("Road").trim();
		String Section = titaVo.getParam("Section").trim();
		String Alley = titaVo.getParam("Alley").trim();
		String Lane = titaVo.getParam("Lane").trim();
		String Num = titaVo.getParam("Num").trim();
		String NumDash = titaVo.getParam("NumDash").trim();
		String Floor = titaVo.getParam("Floor").trim();
		String FloorDash = titaVo.getParam("FloorDash").trim();
		String BdNo1 = titaVo.getParam("BdNo1").trim();
		String BdNo2 = titaVo.getParam("BdNo2").trim();

		if (!CityItem.isEmpty()) {
			result += CityItem;
		}
		if (!AreaItem.isEmpty()) {
			result += AreaItem;
		}
		if (!Road.isEmpty()) {
			result += Road;
		}
		if (!Section.isEmpty()) {
			result += Section + "段";
		}
		if (!Alley.isEmpty()) {
			result += Alley + "巷";
		}
		if (!Lane.isEmpty()) {
			result += Lane + "弄";
		}
		if (!Num.isEmpty()) {
			result += Num + "號";
		}
		if (!NumDash.isEmpty()) {
			result += "之" + NumDash + ",";
		}
		if (!Floor.isEmpty()) {
			result += Floor + "樓";
		}
		if (!FloorDash.isEmpty()) {
			result += "之" + FloorDash;
		}
		if (!BdNo1.isEmpty()) {
			result += "，建號" + BdNo1;
		}
		if (!BdNo2.isEmpty()) {
			result += "-" + BdNo2;
		}
		this.info("L2415 getBdLocation result = " + result);
		return result;
	}

}