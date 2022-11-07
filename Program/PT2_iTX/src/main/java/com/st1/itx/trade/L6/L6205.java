package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLand;
import com.st1.itx.db.domain.CdLandId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6205")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6205 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdLandService cdLandService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public SendRsp sendRsp;
	@Autowired
	DateUtil dDateUtil;

	public String iCityCode;
	public String iLandOfficeCode;
	public String iLandOfficeItem;

	private CdLand tCdLand;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6205 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCd = this.parse.stringToInteger(titaVo.getParam("FunCd"));
		iCityCode = titaVo.getParam("CityCode");
		iLandOfficeCode = titaVo.getParam("LandOfficeCode");
		iLandOfficeItem = titaVo.getParam("LandOfficeItem");

		// 檢查輸入資料
		if (!(iFunCd >= 1 && iFunCd <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6205"); // 功能選擇錯誤
		}

		CdLandId cdLandId = new CdLandId();
		cdLandId.setCityCode(iCityCode);
		cdLandId.setLandOfficeCode(iLandOfficeCode);
		tCdLand = cdLandService.holdById(new CdLandId(iCityCode, iLandOfficeCode), titaVo);

		switch (iFunCd) {
		case 1: // 新增
			tCdLand = new CdLand();
			moveCdLand(iFunCd, titaVo);
			this.info("tCdLand =  " + tCdLand);
			try {
				cdLandService.insert(tCdLand, titaVo);

			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			if (tCdLand == null) {
				throw new LogicException(titaVo, "E0003", "縣市 = " + iCityCode + "地政所 = " + iLandOfficeCode); // 修改資料不存在
			}
			CdLand tCdLand2 = (CdLand) dataLog.clone(tCdLand);
			try {
				moveCdLand(iFunCd, titaVo);
				this.info("tCdLand =  " + tCdLand);
				tCdLand = cdLandService.update2(tCdLand, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdLand2, tCdLand);
			dataLog.exec("修改地政代碼說明");
			break;

		case 4: // 刪除
			if (tCdLand != null) {
				// 刪除須刷主管卡
				if (titaVo.getEmpNos().trim().isEmpty()) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
				}
				try {
					dataLog.setEnv(titaVo, tCdLand, tCdLand);
					dataLog.exec("刪除地政代碼");
					cdLandService.delete(tCdLand, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", "縣市 = " + iCityCode + "地政所 = " + iLandOfficeCode); // 刪除資料不存在
			}
			break;
		}

		insCdCode(iFunCd, "LandOfficeCode", iLandOfficeCode, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdLand(int mFuncCode, TitaVo titaVo) throws LogicException {
		CdLandId cdLandId = new CdLandId();
		cdLandId.setCityCode(iCityCode);
		cdLandId.setLandOfficeCode(iLandOfficeCode);
		tCdLand.setCdLandId(cdLandId);
		tCdLand.setCityCode(iCityCode);
		tCdLand.setLandOfficeCode(iLandOfficeCode);
		tCdLand.setLandOfficeItem(iLandOfficeItem);

	}

	private void insCdCode(int mFuncCode, String mDefCode, String mLoanCode, TitaVo titaVo) throws LogicException {

		// 檢查代碼檔是否已存在
		CdCode fCdCode = new CdCode();
		fCdCode = cdCodeService.findById(new CdCodeId(mDefCode, mLoanCode));
		if (fCdCode != null && mFuncCode == 1) {
			mFuncCode = 2;
		}
		this.info("L6205 mFuncCode : " + mFuncCode);

		// 更新各類代碼檔
		CdCode tCdCode = new CdCode();
		CdCodeId tCdCodeId = new CdCodeId();

		tCdCodeId.setDefCode(mDefCode);
		tCdCodeId.setCode(mLoanCode);

		switch (mFuncCode) {
		case 1: // 新增
			tCdCode.setCdCodeId(tCdCodeId);
			tCdCode = moveCdCode(tCdCode, mFuncCode, "Y", titaVo);
			try {
				cdCodeService.insert(tCdCode, titaVo);
				this.info("L6205 insCdCode : " + mFuncCode + mDefCode + mLoanCode);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					break; // 代碼檔新增資料已存在時離開
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdCode = cdCodeService.holdById(tCdCodeId);
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", "代碼檔:" + mLoanCode); // 修改資料不存在
			}
			CdCode tCdCode2 = (CdCode) dataLog.clone(tCdCode); ////
			try {
				tCdCode = moveCdCode(tCdCode, mFuncCode, "Y", titaVo);
				tCdCode = cdCodeService.update2(tCdCode, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode2, tCdCode); ////
			dataLog.exec("修改各類代碼檔"); ////
			break;

		case 4: // 刪除時 Enable維護為"N"
			tCdCode = cdCodeService.holdById(tCdCodeId);
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", "代碼檔:" + mLoanCode); // 修改資料不存在
			}
			CdCode tCdCode4 = (CdCode) dataLog.clone(tCdCode); ////
			try {
				tCdCode = moveCdCode(tCdCode, mFuncCode, "N", titaVo);
				tCdCode = cdCodeService.update2(tCdCode, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode4, tCdCode); ////
			dataLog.exec("刪除各類代碼檔"); ////
			break;

		}
	}

	private CdCode moveCdCode(CdCode mCdCode, int mFuncCode, String mEnable, TitaVo titaVo) throws LogicException {

		mCdCode.setDefType(6);
		mCdCode.setItem(iLandOfficeItem);
		mCdCode.setEnable(mEnable);

		return mCdCode;
	}
}