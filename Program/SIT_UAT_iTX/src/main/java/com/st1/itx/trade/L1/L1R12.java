package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R12")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R12 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdAreaService iCdAreaService;
	@Autowired
	public CdCityService iCdCityService;


	// TITA
	private String RimCityCode;
	private String RimAreaCode;
	private String RimRoad;
	private String RimSection;
	private String RimAlley;
	private String RimLane;
	private String RimNum;
	private String RimNumDash;
	private String RimFloor;
	private String RimFloorDash;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R12 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		RimCityCode = titaVo.getParam("RimCityCode");
		RimAreaCode = titaVo.getParam("RimAreaCode");
		RimRoad = titaVo.getParam("RimRoad");
		RimSection = titaVo.getParam("RimSection");
		RimAlley = titaVo.getParam("RimAlley");
		RimLane = titaVo.getParam("RimLane");
		RimNum = titaVo.getParam("RimNum");
		RimNumDash = titaVo.getParam("RimNumDash");
		RimFloor = titaVo.getParam("RimFloor");
		RimFloorDash = titaVo.getParam("RimFloorDash");

		// 通訊地址
		String Address = "";

		CdCity cdCity = iCdCityService.findById(RimCityCode);
		Address += cdCity.getCityItem();

		CdAreaId cdAreaId = new CdAreaId();
		cdAreaId.setCityCode(RimCityCode);
		cdAreaId.setAreaCode(RimAreaCode);
		CdArea cdArea = iCdAreaService.findById(cdAreaId);
		if (cdArea != null) {
			Address += cdArea.getAreaItem();
		}

		Address += RimRoad;
		if (!"".equals(RimSection)) {
			Address += RimSection + "段";
		}
		if (!"".equals(RimAlley)) {
			Address += RimAlley + "巷";
		}
		if (!"".equals(RimLane)) {
			Address += RimLane + "弄";
		}
		if (!"".equals(RimNum)) {
			Address += RimNum + "號";
		}
		String numDash = "";
		if (!"".equals(RimNumDash)) {
			Address += "-" + RimNumDash;
			numDash = ",";
		}
		if (!"".equals(RimFloor)) {
			Address += numDash + RimFloor + "樓";
		}
		if (!"".equals(RimFloorDash)) {
			Address += "-" + RimFloorDash;
		}
		this.totaVo.putParam("L1r12Address", Address);

		this.addList(this.totaVo);
		return this.sendList();
	}
}