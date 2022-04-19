package com.st1.itx.trade.L6;

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
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimCityCode=X,2
 * RimAreaCode=X,2
 */
@Service("L6R11") // 尋找地區別與鄉鎮區對照檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R11 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R11 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimCityCode = titaVo.getParam("RimCityCode");
		String iRimAreaCode = titaVo.getParam("RimAreaCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R11"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R11"); // 功能選擇錯誤
		}

		// 查詢地區別代碼檔
		CdCity tCdCity = new CdCity();
		tCdCity = sCdCityService.findById(iRimCityCode, titaVo);
		/* 必須先建立地區別代碼檔資料 */
		if (tCdCity != null) {
			/* 將每筆資料放入Tota */
			this.totaVo.putParam("L6R11CityItem", tCdCity.getCityItem());
		} else {
			throw new LogicException(titaVo, "E0001", "必須先建立地區別代碼檔資料"); // 查無資料
		}

		// 初始值Tota
		moveTotaCdArea(new CdArea());

		// 查詢地區別與鄉鎮區對照檔
		CdArea tCdArea = sCdAreaService.findById(new CdAreaId(iRimCityCode, iRimAreaCode), titaVo);

		/* 如有找到資料 */
		if (tCdArea != null) {
			if (iRimTxCode.equals("L6705") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimCityCode")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdArea(tCdArea);
			}
		} else {
			if (iRimTxCode.equals("L6705") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "地區別與鄉鎮區對照檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 地區別與鄉鎮區對照檔
	private void moveTotaCdArea(CdArea mCdArea) throws LogicException {
		this.totaVo.putParam("L6R11CityCode", mCdArea.getCityCode());
		this.totaVo.putParam("L6R11AreaCode", mCdArea.getAreaCode());
		this.totaVo.putParam("L6R11CityShort", mCdArea.getCityShort());
		this.totaVo.putParam("L6R11AreaItem", mCdArea.getAreaItem());
		this.totaVo.putParam("L6R11AreaShort", mCdArea.getAreaShort());
		this.totaVo.putParam("L6R11CityType", mCdArea.getCityType());
		this.totaVo.putParam("L6R11Zip3", mCdArea.getZip3());
		this.totaVo.putParam("L6R11CityGroup", mCdArea.getCityGroup());
		this.totaVo.putParam("L6R11DepartCode", mCdArea.getDepartCode());
		this.totaVo.putParam("L6R11JcicCityCode", mCdArea.getJcicCityCode());
		this.totaVo.putParam("L6R11JcicAreaCode", mCdArea.getJcicAreaCode());
	}

}