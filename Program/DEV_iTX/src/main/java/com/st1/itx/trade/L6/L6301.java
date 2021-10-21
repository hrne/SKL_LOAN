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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 Code=9,2 Item=X,40 END=X,1
 */

@Service("L6301")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6301 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	public CdBaseRateService sCdBaseRateService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6301 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iCode = titaVo.getParam("Code");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6301"); // 功能選擇錯誤
		}

//		// isActfgSuprele() 放行
//		this.info("ActfgSuprele="+titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele() && iFuncCode == 1) {
			iFuncCode = 2;
	    } else if ((!(titaVo.isActfgSuprele())) && iFuncCode == 4) {
			iFuncCode = 2;
		}
				
		// BaseRate0的選單-商品用含99:自訂(執行L6604建立)
		insCdCode(iFuncCode, "BaseRate0", iCode, titaVo);

		// BaseRate的選單-指標利率種類
		insCdCode(iFuncCode, "BaseRate", iCode, titaVo);

		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void insCdCode(int mFuncCode, String mDefCode, String mCode, TitaVo titaVo) throws LogicException {

		// 更新各類代碼檔
		CdCode tCdCode = new CdCode();
		CdCodeId tCdCodeId = new CdCodeId();

		tCdCodeId.setDefCode(mDefCode);
		tCdCodeId.setCode(mCode);

		Slice<CdBaseRate> tCdBaseRate = null;
		List<CdBaseRate> lCdBaseRate = null;

		switch (mFuncCode) {
		case 1: // 新增
			tCdCode.setCdCodeId(tCdCodeId);
			tCdCode = moveCdCode(tCdCode, mFuncCode, titaVo);
			try {
				this.info("1");
				sCdCodeService.insert(tCdCode, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", "代碼檔代號:"+mDefCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdCode = sCdCodeService.holdById(new CdCodeId(mDefCode, mCode));
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", mCode); // 修改 資料不存在
			}
			
			// 檢查CdBAseRate是否已建立指標利率，如已有請先刪除再修改指標利率種類
			tCdBaseRate = sCdBaseRateService.baseRateCodeEq("TWD", mCode, 00000000, 99999999, 0, 1, titaVo);
			lCdBaseRate = tCdBaseRate == null ? null : tCdBaseRate.getContent();
			if (!titaVo.isActfgSuprele() && lCdBaseRate != null) {
				throw new LogicException(titaVo, "", "請先至L6302刪除指標利率，才可修改/刪除指指標利率種類");
			}

			if (!titaVo.isActfgSuprele() && tCdCode.getEffectFlag() == 2) {
				throw new LogicException(titaVo, "", "未放行交易不可修改/刪除");
			}
			
			CdCode tCdCode2 = (CdCode) dataLog.clone(tCdCode); ////
			try {
				tCdCode = moveCdCode(tCdCode, mFuncCode, titaVo);
				tCdCode = sCdCodeService.update2(tCdCode, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode2, tCdCode); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdCode = sCdCodeService.holdById(new CdCodeId(mDefCode, mCode));

			// 檢查CdBAseRate是否已建立指標利率，如已有請先刪除再刪除指標利率種類
			tCdBaseRate = sCdBaseRateService.baseRateCodeEq("TWD", mCode, 00000000, 99999999, 0, 1, titaVo);
			lCdBaseRate = tCdBaseRate == null ? null : tCdBaseRate.getContent();
			if (!titaVo.isActfgSuprele() && lCdBaseRate != null) {
				throw new LogicException(titaVo, "", "請先至L6302刪除指標利率，才可修改/刪除指標利率種類");
			}
			
			if (!titaVo.isActfgSuprele() && tCdCode.getEffectFlag() == 2) {
				throw new LogicException(titaVo, "", "未放行交易不可修改/刪除");
			}

			if (tCdCode != null) {
				try {
					sCdCodeService.delete(tCdCode);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", mCode); // 刪除資料不存在
			}
			break;
		case 5: // inq
			break;
		}
	}

	private CdCode moveCdCode(CdCode mCdCode, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdCode.setDefType(2);
		mCdCode.setItem(titaVo.getParam("Item"));
		mCdCode.setEnable("N");
		//0:已放行 1:未放行
		if(("BaseRate").equals(mCdCode.getCdCodeId().getDefCode())) {
			if (titaVo.isActfgSuprele()) {
				mCdCode.setEffectFlag(0);
				mCdCode.setEnable("Y");
			} else {
				mCdCode.setEffectFlag(1);
			}
			
		}
		
		

		if (mFuncCode != 2) {
			mCdCode.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdCode.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdCode.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdCode.setLastUpdateEmpNo(titaVo.getTlrNo());

		return mCdCode;
	}
}
