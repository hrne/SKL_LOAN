package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 CurrencyCode=X,3 BaseRateCode=X,2 EffectDate=9,7
 * BaseRate=m,2.4 Remark=x,80 END=X,1
 */

@Service("L6302")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6302 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService sCdBaseRateService;
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6302 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iBaseRateCode = titaVo.getParam("BaseRateCode");
		int iEffectDate = this.parse.stringToInteger(titaVo.getParam("EffectDate"));
		this.info("L6302 iEffectDate : " + iEffectDate);
		int iFEffectDate = iEffectDate + 19110000;
		this.info("L6302 iFEffectDate : " + iFEffectDate);

		int iTbsdy = titaVo.getEntDyI();
		int iCalDy = parse.stringToInteger(titaVo.getCalDy());
		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6302"); // 功能選擇錯誤
		}

		this.info("L6302 isActfgSuprele : " + titaVo.isActfgSuprele());

		// isActfgSuprele() 放行
		if (titaVo.isActfgSuprele() && iFuncCode == 1) {
			iFuncCode = 2;
		} else if ((!(titaVo.isActfgSuprele())) && iFuncCode == 4) {
			iFuncCode = 2;
		}

		// 更新指標利率檔
		CdBaseRate tCdBaseRate = new CdBaseRate();
		CdBaseRateId tCdBaseRateId = new CdBaseRateId();
		switch (iFuncCode) {
		case 1: // 新增

			if (iEffectDate < iTbsdy) {
				throw new LogicException(titaVo, "E0005", "生效日期需大於會計日"); // 新增資料時，發生錯誤
			}

			// 生效日期需為最大
			Slice<CdBaseRate> cCdBaseRate1 = sCdBaseRateService.effectFlagDescFirst1("TWD", iBaseRateCode, 0, Integer.MAX_VALUE, titaVo);
			List<CdBaseRate> cCdBaseRate2 = cCdBaseRate1 == null ? null : cCdBaseRate1.getContent();
			if (cCdBaseRate2 != null) {
				this.info("iEffectDate==" + iEffectDate);
				this.info("cCdBaseRate2==" + cCdBaseRate2.get(0).getEffectDate());
				if (iEffectDate < cCdBaseRate2.get(0).getEffectDate()) {
					throw new LogicException(titaVo, "E0005", "生效日期需大於" + cCdBaseRate2.get(0).getEffectDate()); // 新增資料時，發生錯誤
				}

			}

			CdBaseRate bCdBaseRate = sCdBaseRateService.effectFlagDescFirst("TWD", iBaseRateCode, 0, titaVo);
			if (bCdBaseRate != null) {
				this.info("bCdBaseRate==" + bCdBaseRate.getEffectDate());
				this.info("iCalDy==" + iCalDy);
				if (bCdBaseRate.getEffectDate() > iCalDy && iEffectDate > iCalDy) {
					throw new LogicException(titaVo, "E0005", "未生效指標利率只能有一筆"); // 新增資料時，發生錯誤
				}
			}

			CdCode tCdCode = sCdCodeService.findById(new CdCodeId("BaseRate", iBaseRateCode), titaVo);
			if (tCdCode != null) {
				if (tCdCode.getEffectFlag() != 0) {
					throw new LogicException(titaVo, "E0005", "該指標利率種類未放行"); // 新增資料時，發生錯誤
				}
			}

			moveCdBaseRate(tCdBaseRate, tCdBaseRateId, iFuncCode, iCurrencyCode, iBaseRateCode, iFEffectDate, titaVo);

			try {
				this.info("1");
				sCdBaseRateService.insert(tCdBaseRate, titaVo);
				this.info("L6302 ins : " + iFuncCode + iCurrencyCode + iBaseRateCode + iFEffectDate);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdBaseRate = sCdBaseRateService.holdById(new CdBaseRateId(iCurrencyCode, iBaseRateCode, iFEffectDate));

			if (tCdBaseRate == null) {
				throw new LogicException(titaVo, "E0003", iBaseRateCode + "-" + iFEffectDate); // 修改資料不存在
			}

			if (!titaVo.isActfgSuprele() && tCdBaseRate.getEffectFlag() == 2) {
				throw new LogicException(titaVo, "", "未放行交易不可修改/刪除");
			}

			CdBaseRate tCdBaseRate2 = (CdBaseRate) dataLog.clone(tCdBaseRate); ////
			try {
				moveCdBaseRate(tCdBaseRate, tCdBaseRateId, iFuncCode, iCurrencyCode, iBaseRateCode, iFEffectDate, titaVo);
				tCdBaseRate = sCdBaseRateService.update2(tCdBaseRate, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdBaseRate2, tCdBaseRate); ////
			dataLog.exec("修改指標利率"); ////
			break;

		case 4: // 刪除
			tCdBaseRate = sCdBaseRateService.holdById(new CdBaseRateId(iCurrencyCode, iBaseRateCode, iFEffectDate));
			this.info("L6302 del : " + iFuncCode + iCurrencyCode + iBaseRateCode + iFEffectDate);
			if (tCdBaseRate != null) {
				try {
					sCdBaseRateService.delete(tCdBaseRate);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iBaseRateCode + "-" + iFEffectDate); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdBaseRate, tCdBaseRate);
			dataLog.exec("刪除指標利率");
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdBaseRate(CdBaseRate mCdBaseRate, CdBaseRateId mCdBaseRateId, int mFuncCode, String mCurrencyCode, String mBaseRateCode, int mFEffectDate, TitaVo titaVo) throws LogicException {

		mCdBaseRateId.setCurrencyCode(mCurrencyCode);
		mCdBaseRateId.setBaseRateCode(mBaseRateCode);
		mCdBaseRateId.setEffectDate(mFEffectDate);
		mCdBaseRate.setCdBaseRateId(mCdBaseRateId);

		mCdBaseRate.setBaseRate(this.parse.stringToBigDecimal(titaVo.getParam("BaseRate")));
		mCdBaseRate.setRemark(titaVo.getParam("Remark"));

		// isActfgSuprele() 放行
		// EffectFlag 0:已放行 1:已生效不可刪除 2:未放行
		if (titaVo.isActfgSuprele()) {
			mCdBaseRate.setEffectFlag(0);
		} else {
			mCdBaseRate.setEffectFlag(2);
		}

		if (mFuncCode != 2) {
			mCdBaseRate.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdBaseRate.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdBaseRate.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdBaseRate.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
