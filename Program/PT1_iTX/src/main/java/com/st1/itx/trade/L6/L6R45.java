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
import com.st1.itx.db.domain.CdSyndFee;
import com.st1.itx.db.service.CdSyndFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R45")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6R45 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R45.class);

	@Autowired
	public CdSyndFeeService cdSyndFeeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R45 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("RimFunCd"));
		String iTxCode = titaVo.getParam("RimTxCode");
		String iSyndFeeCode = titaVo.getParam("RimSyndFeeCode");

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R45"); // 交易代號不可為空白
		}
		if (!(iFunCd >= 1 && iFunCd <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R45"); // 功能選擇錯誤
		}
		// 初始值Tota
		moveTotaCdSyndFee(new CdSyndFee());

		CdSyndFee tCdSyndFee = cdSyndFeeService.findById(iSyndFeeCode, titaVo);

		/* 如有找到資料 */
		if (tCdSyndFee != null) {
			if (iFunCd == 1) {
				throw new LogicException(titaVo, "E0002", iSyndFeeCode); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdSyndFee(tCdSyndFee);
			}
		} else {
			if (iFunCd == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "聯貸費用代碼檔"); // 查無資料
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveTotaCdSyndFee(CdSyndFee mCdSyndFee) throws LogicException {

		this.totaVo.putParam("L6R45SyndFeeCode", mCdSyndFee.getSyndFeeCode());
		this.totaVo.putParam("L6R45SyndFeeItem", mCdSyndFee.getSyndFeeItem());
		this.totaVo.putParam("L6R45AcctCode", mCdSyndFee.getAcctCode());
	}
}