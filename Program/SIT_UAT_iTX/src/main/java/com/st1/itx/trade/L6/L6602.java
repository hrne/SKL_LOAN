package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 IndustryCode=X,6 IndustryItem=X,100 MainType=X,1 END=X,1
 */

@Service("L6602")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6602 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6602.class);

	/* DB服務注入 */
	@Autowired
	public CdIndustryService sCdIndustryService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6602 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iIndustryCode = titaVo.getParam("IndustryCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6602"); // 功能選擇錯誤
		}

		// 更新行業別代號資料檔
		CdIndustry tCdIndustry = new CdIndustry();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdIndustry(tCdIndustry, iFuncCode, titaVo);
			try {
				this.info("1");
				sCdIndustryService.insert(tCdIndustry, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdIndustry = sCdIndustryService.holdById(iIndustryCode);
			if (tCdIndustry == null) {
				throw new LogicException(titaVo, "E0003", iIndustryCode); // 修改資料不存在
			}
			CdIndustry tCdIndustry2 = (CdIndustry) dataLog.clone(tCdIndustry); ////
			try {
				moveCdIndustry(tCdIndustry, iFuncCode, titaVo);
				tCdIndustry = sCdIndustryService.update2(tCdIndustry, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdIndustry2, tCdIndustry); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdIndustry = sCdIndustryService.holdById(iIndustryCode);
			if (tCdIndustry != null) {
				try {
					sCdIndustryService.delete(tCdIndustry);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iIndustryCode); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdIndustry(CdIndustry mCdIndustry, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdIndustry.setIndustryCode(titaVo.getParam("IndustryCode"));
		mCdIndustry.setIndustryItem(titaVo.getParam("IndustryItem"));
		mCdIndustry.setMainType(titaVo.getParam("MainType"));

		if (mFuncCode != 2) {
			mCdIndustry.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdIndustry.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdIndustry.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdIndustry.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
