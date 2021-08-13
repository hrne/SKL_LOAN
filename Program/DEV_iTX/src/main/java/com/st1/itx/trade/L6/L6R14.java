package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R14BaseRateCode=X,2
 * RimL6R14CurrencyCode=X,3
 * RimL6R14EffectDate=9,7
 */
@Service("L6R14") // 尋找指標利率檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R14 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R14.class);

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService sCdBaseRateService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R14 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iMsgCode = 0;
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iBaseRateCode = titaVo.getParam("RimL6R14BaseRateCode");
		String iCurrencyCode = titaVo.getParam("RimL6R14CurrencyCode");
		int iEffectDate = this.parse.stringToInteger(titaVo.getParam("RimL6R14EffectDate"));
		int iFEffectDate = iEffectDate + 19110000;
		this.info("L6R14 1 iFEffectDate : " + iFEffectDate);

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R14"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R14"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdBaseRate(new CdBaseRate());

		// 檢查是否已有生效之利率
		iMsgCode = CheckCdBaseRate(iCurrencyCode, iBaseRateCode, iFEffectDate, iMsgCode, titaVo);

		// 查詢指標利率檔
		CdBaseRate tCdBaseRate = sCdBaseRateService.findById(new CdBaseRateId(iCurrencyCode, iBaseRateCode, iFEffectDate), titaVo);

		/* 如有找到資料 */
		if (tCdBaseRate != null) {
			if (iRimTxCode.equals("L6302") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimL6R14EffectDate")); // 新增資料已存在
			} else if (tCdBaseRate.getEffectFlag() == 1) {
				throw new LogicException(titaVo, "E6017", titaVo.getParam("RimL6R14BaseRateCode") + "-" + titaVo.getParam("RimL6R14EffectDate") + " 請先做 L4320 交易之訂正 "); // 該利率已生效
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdBaseRate(tCdBaseRate);
			}
		} else {
			if (iRimTxCode.equals("L6302") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "指標利率檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private int CheckCdBaseRate(String cCurrencyCode, String cBaseRateCode, int cFEffectDate, int cMsgCode, TitaVo titaVo) throws LogicException {

		Slice<CdBaseRate> slCdBaseRate;
		slCdBaseRate = sCdBaseRateService.effectFlagEq(cCurrencyCode, cBaseRateCode, 1, cFEffectDate, this.index, Integer.MAX_VALUE, titaVo);
		List<CdBaseRate> lCdBaseRate = slCdBaseRate == null ? null : slCdBaseRate.getContent();

		if (lCdBaseRate == null || lCdBaseRate.size() == 0) {
			cMsgCode = 0; // 查無資料
		} else {
			// 如有找到資料
			throw new LogicException(titaVo, "E6017", titaVo.getParam("RimL6R14BaseRateCode") + "-" + titaVo.getParam("RimL6R14EffectDate") + " 請先做 L4320 交易之訂正 "); // 該利率已生效
		}

		return cMsgCode;
	}

	// 將每筆資料放入Tota
	// 指標利率檔
	private void moveTotaCdBaseRate(CdBaseRate mCdBaseRate) throws LogicException {

		this.totaVo.putParam("L6R14BaseRate", mCdBaseRate.getBaseRate());
		this.totaVo.putParam("L6R14Remark", mCdBaseRate.getRemark());
		this.totaVo.putParam("L6R14EffectFlag", mCdBaseRate.getEffectFlag());
	}

}