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
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.CdInsurerId;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R10InsurerType=X,1
 * RimL6R10InsurerCode=X,2
 */
@Service("L6R10") // 尋找保險公司資料檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R10 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R10.class);

	/* DB服務注入 */
	@Autowired
	public CdInsurerService sCdInsurerService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R10 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimInsurerType = titaVo.getParam("RimL6R10InsurerType");
		String iRimInsurerCode = titaVo.getParam("RimL6R10InsurerCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R10"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R10"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdInsurer(new CdInsurer());

		// 查詢保險公司資料檔
		CdInsurer tCdInsurer = sCdInsurerService.findById(new CdInsurerId(iRimInsurerType, iRimInsurerCode), titaVo);

		/* 如有找到資料 */
		if (tCdInsurer != null) {
			if (iRimTxCode.equals("L6703") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimInsurerType); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdInsurer(tCdInsurer);
			}
		} else {
			if (iRimTxCode.equals("L6703") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "保險公司資料檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 保險公司資料檔
	private void moveTotaCdInsurer(CdInsurer mCdInsurer) throws LogicException {
		this.totaVo.putParam("L6R10InsurerType", mCdInsurer.getInsurerType());
		this.totaVo.putParam("L6R10InsurerCode", mCdInsurer.getInsurerCode());
		this.totaVo.putParam("L6R10InsurerItem", mCdInsurer.getInsurerItem());
		this.totaVo.putParam("L6R10InsurerShort", mCdInsurer.getInsurerShort());
		this.totaVo.putParam("L6R10TelArea", mCdInsurer.getTelArea());
		this.totaVo.putParam("L6R10TelNo", mCdInsurer.getTelNo());
		this.totaVo.putParam("L6R10TelExt", mCdInsurer.getTelExt());

	}

}