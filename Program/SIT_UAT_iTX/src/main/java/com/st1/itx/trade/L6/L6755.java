package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 UnitCode=X,6 UnitItem=X,40 DeptCode=X,6 DeptItem=X,40
 * DistCode=X,6 DistItem=X,40 UnitManager=X,6 DeptManager=X,6 DistManager=X,6
 * END=X,1
 */

@Service("L6755")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6755 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6755 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iUnitCode = titaVo.getParam("UnitCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6755"); // 功能選擇錯誤
		}

		// 更新分公司資料檔
		CdBcm tCdBcm = new CdBcm();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdBcm(tCdBcm, iFuncCode, titaVo);
			try {
				sCdBcmService.insert(tCdBcm, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdBcm = sCdBcmService.holdById(iUnitCode);
			if (tCdBcm == null) {
				throw new LogicException(titaVo, "E0003", iUnitCode); // 修改資料不存在
			}
			CdBcm tCdBcm2 = (CdBcm) dataLog.clone(tCdBcm); ////
			try {
				moveCdBcm(tCdBcm, iFuncCode, titaVo);
				tCdBcm = sCdBcmService.update2(tCdBcm, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdBcm2, tCdBcm); ////
			dataLog.exec("修改單位及主管代號"); ////
			break;

		case 4: // 刪除
			tCdBcm = sCdBcmService.holdById(iUnitCode);
			if (tCdBcm != null) {
				try {
					sCdBcmService.delete(tCdBcm);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iUnitCode); // 刪除資料不存在
			}
			break;
		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdBcm(CdBcm mCdBcm, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdBcm.setUnitCode(titaVo.getParam("UnitCode"));
		mCdBcm.setUnitItem(titaVo.getParam("UnitItem"));
		mCdBcm.setDeptCode(titaVo.getParam("DeptCode"));
		mCdBcm.setDeptItem(titaVo.getParam("DeptItem"));
		mCdBcm.setDistCode(titaVo.getParam("DistCode"));
		mCdBcm.setDistItem(titaVo.getParam("DistItem"));
		mCdBcm.setUnitManager(titaVo.getParam("UnitManager"));
		mCdBcm.setDeptManager(titaVo.getParam("DeptManager"));
		mCdBcm.setDistManager(titaVo.getParam("DistManager"));
		mCdBcm.setEnable(titaVo.getParam("Enable"));

		if (mFuncCode != 2) {
			mCdBcm.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdBcm.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdBcm.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdBcm.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
