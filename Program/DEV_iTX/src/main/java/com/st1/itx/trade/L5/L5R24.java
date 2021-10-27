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
					String iCityItem = r5R24SqlReturn.get("F2");
					String iAreaItem = r5R24SqlReturn.get("F3");
					String iRoad = r5R24SqlReturn.get("F4");
					String iRegSection = r5R24SqlReturn.get("F5");
					String iRegAlley = r5R24SqlReturn.get("F6");
					String iRegLane = r5R24SqlReturn.get("F7");
					String iRegNum = r5R24SqlReturn.get("F8");
					String iRegNumDash = r5R24SqlReturn.get("F9");
					String iRegFloor = r5R24SqlReturn.get("F10");
					String iRegFloorDash = r5R24SqlReturn.get("F11");
					
					reAddress = iCityItem+iAreaItem;
					if (!"".equals(iRoad)) {
						reAddress = reAddress+iRoad;
					}
					if (!"".equals(iRegSection)) {
						reAddress = reAddress+iRegSection+"段";
					}
					if (!"".equals(iRegAlley)) {
						reAddress = reAddress+iRegAlley+"弄";
					}
					if (!"".equals(iRegLane)) {
						reAddress = reAddress+iRegLane+"巷";
					}
					if (!"".equals(iRegNum)) {
						reAddress = reAddress+iRegNum+"號";
					}
					if (!"".equals(iRegNumDash)) {
						reAddress = reAddress+"之"+iRegNumDash;
					}
					if (!"".equals(iRegFloor)) {
						reAddress = reAddress+iRegFloor+"樓";
					}
					if (!"".equals(iRegFloorDash)) {
						reAddress = reAddress+"之"+iRegFloorDash;
					}
					totaVo.putParam("L5R24FullAddress", reAddress);
				}
				break;
			case "2":
				for (Map<String, String> r5R24SqlReturn : i5R24SqlReturn) {
					String reAddress = "";
					String iCityItem = r5R24SqlReturn.get("F2");
					String iAreaItem = r5R24SqlReturn.get("F3");
					String iCurrRoad = r5R24SqlReturn.get("F4");
					String iCurrSection = r5R24SqlReturn.get("F5");
					String iCurrAlley = r5R24SqlReturn.get("F6");
					String iCurrLane = r5R24SqlReturn.get("F7");
					String iCurrNum = r5R24SqlReturn.get("F8");
					String iCurrNumDash = r5R24SqlReturn.get("F9");
					String iCurrFloor = r5R24SqlReturn.get("F10");
					String iCurrFloorDash = r5R24SqlReturn.get("F11");
					
					reAddress = iCityItem+iAreaItem;
					if (!"".equals(iCurrRoad)) {
						reAddress = reAddress+iCurrRoad;
					}
					if (!"".equals(iCurrSection)) {
						reAddress = reAddress+iCurrSection+"段";
					}
					if (!"".equals(iCurrAlley)) {
						reAddress = reAddress+iCurrAlley+"弄";
					}
					if (!"".equals(iCurrLane)) {
						reAddress = reAddress+iCurrLane+"巷";
					}
					if (!"".equals(iCurrNum)) {
						reAddress = reAddress+iCurrNum+"號";
					}
					if (!"".equals(iCurrNumDash)) {
						reAddress = reAddress+"之"+iCurrNumDash;
					}
					if (!"".equals(iCurrFloor)) {
						reAddress = reAddress+iCurrFloor+"樓";
					}
					if (!"".equals(iCurrFloorDash)) {
						reAddress = reAddress+"之"+iCurrFloorDash;
					}
					totaVo.putParam("L5R24FullAddress", reAddress);
				}
				break;
			case "3":
				for (Map<String, String> r5R24SqlReturn : i5R24SqlReturn) {
					String reAddress = "";
					String iCityItem = r5R24SqlReturn.get("F2");
					String iAreaItem = r5R24SqlReturn.get("F3");
					String iIrtem = r5R24SqlReturn.get("F4");
					String iRoad = r5R24SqlReturn.get("F5");
					String iSection = r5R24SqlReturn.get("F6");
					String iAlley = r5R24SqlReturn.get("F7");
					String iLane = r5R24SqlReturn.get("F8");
					String iNum = r5R24SqlReturn.get("F9");
					String iNumDash = r5R24SqlReturn.get("F10");
					String iFloor = r5R24SqlReturn.get("F11");
					String iFloorDash = r5R24SqlReturn.get("F12");
					
					reAddress = iCityItem+iAreaItem;
					if (!"".equals(iIrtem)) {
						reAddress = reAddress+iIrtem;
					}
					if (!"".equals(iRoad)) {
						reAddress = reAddress+iRoad;
					}
					if (!"".equals(iSection)) {
						reAddress = reAddress+iSection+"段";
					}
					if (!"".equals(iAlley)) {
						reAddress = reAddress+iAlley+"弄";
					}
					if (!"".equals(iLane)) {
						reAddress = reAddress+iLane+"巷";
					}
					if (!"".equals(iNum)) {
						reAddress = reAddress+iNum+"號";
					}
					if (!"".equals(iNumDash)) {
						reAddress = reAddress+"之"+iNumDash;
					}
					if (!"".equals(iFloor)) {
						reAddress = reAddress+iFloor+"樓";
					}
					if (!"".equals(iFloorDash)) {
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