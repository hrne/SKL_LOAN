package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5R24ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R24")
@Scope("prototype")
/**
 * 房貸專員績效津貼計算
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R24 extends TradeBuffer {
	@Autowired
	public L5R24ServiceImpl iL5R24ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R24 ");
		this.totaVo.init(titaVo);
		String iCustUKey = titaVo.getParam("RimCustUKey");
		String iRimAddressCode = titaVo.getParam("RimAddressCode");
		String iClNo = titaVo.getParam("RimClNo");
		String iRimClCode1 = titaVo.getParam("RimClCode1");
		String iRimClCode2 = titaVo.getParam("RimClCode2");
		List<Map<String, String>> i5R24SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			i5R24SqlReturn = iL5R24ServiceImpl.FindData(iRimAddressCode, iCustUKey, iClNo, iRimClCode1, iRimClCode2, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5R24 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		this.info("db return = " + i5R24SqlReturn.toString());
		if (i5R24SqlReturn.size() == 0) {
			totaVo.putParam("L5R24FullAddress", "");
		} else {
			switch(iRimAddressCode) {
			case "1":
				for (Map<String, String> r5R24SqlReturn : i5R24SqlReturn) {
					String reAddress = "";
					String iCityItem = r5R24SqlReturn.get("CityItem");
					String iAreaItem = r5R24SqlReturn.get("AreaItem");
					String iRoad = r5R24SqlReturn.get("RegRoad");
					String iRegSection = r5R24SqlReturn.get("RegSection");
					String iRegAlley = r5R24SqlReturn.get("RegAlley");
					String iRegLane = r5R24SqlReturn.get("RegLane");
					String iRegNum = r5R24SqlReturn.get("RegNum");
					String iRegNumDash = r5R24SqlReturn.get("RegNumDash");
					String iRegFloor = r5R24SqlReturn.get("RegFloor");
					String iRegFloorDash = r5R24SqlReturn.get("RegFloorDash");
					
					reAddress = iCityItem+iAreaItem;
					if (!iRoad.equals("")) {
						reAddress = reAddress+iRoad;
					}
					if (!iRegSection.equals("")) {
						reAddress = reAddress+iRegSection+"段";
					}
					if (!iRegAlley.equals("")) {
						reAddress = reAddress+iRegAlley+"弄";
					}
					if (!iRegLane.equals("")) {
						reAddress = reAddress+iRegLane+"巷";
					}
					if (!iRegNum.equals("")) {
						reAddress = reAddress+iRegNum+"號";
					}
					if (!iRegNumDash.equals("")) {
						reAddress = reAddress+"之"+iRegNumDash;
					}
					if (!iRegFloor.equals("")) {
						reAddress = reAddress+iRegFloor+"樓";
					}
					if (!iRegFloorDash.equals("")) {
						reAddress = reAddress+"之"+iRegFloorDash;
					}
					totaVo.putParam("L5R24FullAddress", reAddress);
				}
				break;
			case "2":
				for (Map<String, String> r5R24SqlReturn : i5R24SqlReturn) {
					String reAddress = "";
					String iCityItem = r5R24SqlReturn.get("CityItem");
					String iAreaItem = r5R24SqlReturn.get("AreaItem");
					String iCurrRoad = r5R24SqlReturn.get("CurrRoad");
					String iCurrSection = r5R24SqlReturn.get("CurrSection");
					String iCurrAlley = r5R24SqlReturn.get("CurrAlley");
					String iCurrLane = r5R24SqlReturn.get("CurrLane");
					String iCurrNum = r5R24SqlReturn.get("CurrNum");
					String iCurrNumDash = r5R24SqlReturn.get("CurrNumDash");
					String iCurrFloor = r5R24SqlReturn.get("CurrFloor");
					String iCurrFloorDash = r5R24SqlReturn.get("CurrFloorDash");
					
					reAddress = iCityItem+iAreaItem;
					if (!iCurrRoad.equals("")) {
						reAddress = reAddress+iCurrRoad;
					}
					if (!iCurrSection.equals("")) {
						reAddress = reAddress+iCurrSection+"段";
					}
					if (!iCurrAlley.equals("")) {
						reAddress = reAddress+iCurrAlley+"弄";
					}
					if (!iCurrLane.equals("")) {
						reAddress = reAddress+iCurrLane+"巷";
					}
					if (!iCurrNum.equals("")) {
						reAddress = reAddress+iCurrNum+"號";
					}
					if (!iCurrNumDash.equals("")) {
						reAddress = reAddress+"之"+iCurrNumDash;
					}
					if (!iCurrFloor.equals("")) {
						reAddress = reAddress+iCurrFloor+"樓";
					}
					if (!iCurrFloorDash.equals("")) {
						reAddress = reAddress+"之"+iCurrFloorDash;
					}
					totaVo.putParam("L5R24FullAddress", reAddress);
				}
				break;
			case "3":
				for (Map<String, String> r5R24SqlReturn : i5R24SqlReturn) {
					String reAddress = "";
					String iCityItem = r5R24SqlReturn.get("CityItem");
					String iAreaItem = r5R24SqlReturn.get("AreaItem");
					String iIrtem = r5R24SqlReturn.get("IrItem");
					String iRoad = r5R24SqlReturn.get("Road");
					String iSection = r5R24SqlReturn.get("Section");
					String iAlley = r5R24SqlReturn.get("Alley");
					String iLane = r5R24SqlReturn.get("Lane");
					String iNum = r5R24SqlReturn.get("Num");
					String iNumDash = r5R24SqlReturn.get("NumDash");
					String iFloor = r5R24SqlReturn.get("Floor");
					String iFloorDash = r5R24SqlReturn.get("FloorDash");
					
					reAddress = iCityItem+iAreaItem;
					if (!iIrtem.equals("")) {
						reAddress = reAddress+iIrtem;
					}
					if (!iRoad.equals("")) {
						reAddress = reAddress+iRoad;
					}
					if (!iSection.equals("")) {
						reAddress = reAddress+iSection+"段";
					}
					if (!iAlley.equals("")) {
						reAddress = reAddress+iAlley+"弄";
					}
					if (!iLane.equals("")) {
						reAddress = reAddress+iLane+"巷";
					}
					if (!iNum.equals("")) {
						reAddress = reAddress+iNum+"號";
					}
					if (!iNumDash.equals("")) {
						reAddress = reAddress+"之"+iNumDash;
					}
					if (!iFloor.equals("")) {
						reAddress = reAddress+iFloor+"樓";
					}
					if (!iFloorDash.equals("")) {
						reAddress = reAddress+"之"+iFloorDash;
					}
					totaVo.putParam("L5R24FullAddress", reAddress);
				}
				break;
			}
			
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}