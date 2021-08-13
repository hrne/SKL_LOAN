package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimIndustryCode=X,6
 */
@Service("L6R06") // 尋找行業別代號資料檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R06 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R06.class);

	/* DB服務注入 */
	@Autowired
	public CdIndustryService sCdIndustryService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R06 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimIndustryCode = titaVo.getParam("RimIndustryCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R06"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R06"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdIndustry(new CdIndustry());

		// 查詢行業別代號資料檔
		CdIndustry tCdIndustry = sCdIndustryService.findById(iRimIndustryCode, titaVo);

		/* 如有找到資料 */
		if (tCdIndustry != null) {
			if (iRimTxCode.equals("L6602") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimIndustryCode")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdIndustry(tCdIndustry);
			}
		} else {
			if (iRimTxCode.equals("L6602") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "行業別代號資料檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 行業別代號資料檔
	private void moveTotaCdIndustry(CdIndustry mCdIndustry) throws LogicException {
		this.totaVo.putParam("L6R06IndustryCode", mCdIndustry.getIndustryCode());
		this.totaVo.putParam("L6R06IndustryItem", mCdIndustry.getIndustryItem());
		this.totaVo.putParam("L6R06MainType", mCdIndustry.getMainType());
	}

}