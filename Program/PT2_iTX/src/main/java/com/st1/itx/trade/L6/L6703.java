package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.domain.CdInsurerId;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 InsurerType=X,1 InsurerCode=X,2 InsurerItem=X,80
 * InsurerShort=X,20 TelArea=X,5 TelNo=X,10 TelExt=X,5 END=X,1
 */

@Service("L6703")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6703 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdInsurerService sCdInsurerService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6703 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iInsurerType = titaVo.getParam("InsurerType");
		String iInsurerCode = titaVo.getParam("InsurerCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6703"); // 功能選擇錯誤
		}

		// 更新保險公司資料檔
		CdInsurer tCdInsurer = new CdInsurer();
		CdInsurerId tCdInsurerId = new CdInsurerId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdInsurer(tCdInsurer, tCdInsurerId, iFuncCode, iInsurerType, iInsurerCode, titaVo);
			try {
				this.info("1");
				sCdInsurerService.insert(tCdInsurer, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iInsurerType + "-" + iInsurerCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdInsurer = sCdInsurerService.holdById(new CdInsurerId(iInsurerType, iInsurerCode));
			if (tCdInsurer == null) {
				throw new LogicException(titaVo, "E0003", iInsurerType + "-" + iInsurerCode); // 修改資料不存在
			}
			CdInsurer tCdInsurer2 = (CdInsurer) dataLog.clone(tCdInsurer); ////
			try {
				moveCdInsurer(tCdInsurer, tCdInsurerId, iFuncCode, iInsurerType, iInsurerCode, titaVo);
				tCdInsurer = sCdInsurerService.update2(tCdInsurer, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdInsurer2, tCdInsurer); ////
			dataLog.exec("修改保險/鑑定公司資料"); ////
			break;

		case 4: // 刪除
			tCdInsurer = sCdInsurerService.holdById(new CdInsurerId(iInsurerType, iInsurerCode));
			if (tCdInsurer != null) {
				try {
					sCdInsurerService.delete(tCdInsurer);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iInsurerType + "-" + iInsurerCode); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdInsurer, tCdInsurer); ////
			dataLog.exec("刪除保險/鑑定公司資料"); ////
			break;

		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdInsurer(CdInsurer mCdInsurer, CdInsurerId mCdInsurerId, int mFuncCode, String mInsurerType, String mInsurerCode, TitaVo titaVo) throws LogicException {

		mCdInsurerId.setInsurerType(mInsurerType);
		mCdInsurerId.setInsurerCode(mInsurerCode);
		mCdInsurer.setCdInsurerId(mCdInsurerId);

		mCdInsurer.setInsurerType(titaVo.getParam("InsurerType"));
		mCdInsurer.setInsurerCode(titaVo.getParam("InsurerCode"));
		mCdInsurer.setInsurerItem(titaVo.getParam("InsurerItem"));
		mCdInsurer.setInsurerShort(titaVo.getParam("InsurerShort"));
		mCdInsurer.setTelArea(titaVo.getParam("TelArea"));
		mCdInsurer.setTelNo(titaVo.getParam("TelNo"));
		mCdInsurer.setTelExt(titaVo.getParam("TelExt"));
		mCdInsurer.setInsurerId(titaVo.getParam("InsurerId"));

		if (mFuncCode != 2) {
			mCdInsurer.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdInsurer.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdInsurer.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdInsurer.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
