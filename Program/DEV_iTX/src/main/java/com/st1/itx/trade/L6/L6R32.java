package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R32Zip3=A,3
 * RimL6R32Zip2=A,2
 */
@Service("L6R32") // 尋找郵遞區號
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R32 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R32.class);

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R32 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iZip3 = titaVo.getParam("RimL6R32Zip3");
		String iCityItem = "";

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R32"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R32"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdArea(new CdArea(), iCityItem);

		// 查詢地區別與鄉鎮區對照檔
		CdArea tCdArea = sCdAreaService.Zip3First(iZip3, titaVo);

		/* 如有找到資料 */
		if (tCdArea != null) {
			iCityItem = inqCdCity(tCdArea.getCityCode(), iCityItem, titaVo);
			/* 將每筆資料放入Tota */
			moveTotaCdArea(tCdArea, iCityItem);
		} else {
			if (iRimFuncCode == 4 || iRimFuncCode == 5) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "地區別與鄉鎮區對照檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢地區別代碼檔
	private String inqCdCity(String cCityCode, String cCityItem, TitaVo titaVo) throws LogicException {

		CdCity tCdCity = new CdCity();

		tCdCity = sCdCityService.findById(cCityCode, titaVo);

		if (tCdCity == null) {
			cCityItem = "";
		} else {
			cCityItem = tCdCity.getCityItem();
		}

		return cCityItem;

	}

	// 將每筆資料放入Tota
	// 地區別與鄉鎮區對照檔
	private void moveTotaCdArea(CdArea mCdArea, String mCityItem) throws LogicException {

		this.totaVo.putParam("L6R32CityCode", mCdArea.getCityCode());
		this.totaVo.putParam("L6R32AreaCode", mCdArea.getAreaCode());
		this.totaVo.putParam("L6R32CityShort", mCdArea.getCityShort());
		this.totaVo.putParam("L6R32AreaItem", mCdArea.getAreaItem());
		this.totaVo.putParam("L6R32AreaShort", mCdArea.getAreaShort());
		this.totaVo.putParam("L6R32CityType", mCdArea.getCityType());
		this.totaVo.putParam("L6R32Zip3", mCdArea.getZip3());
		this.totaVo.putParam("L6R32CityGroup", mCdArea.getCityGroup());
		this.totaVo.putParam("L6R32DepartCode", mCdArea.getDepartCode());
		this.totaVo.putParam("L6R32CityItem", mCityItem);

	}

}