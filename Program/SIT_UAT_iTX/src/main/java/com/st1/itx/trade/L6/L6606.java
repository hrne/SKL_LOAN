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
import com.st1.itx.db.domain.CdSupv;
import com.st1.itx.db.service.CdSupvService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 SupvReasonCode=X,4 SupvReasonItem=X,80 SupvReasonLevel=X,1
 * END=X,1
 */

@Service("L6606")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6606 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6606.class);

	/* DB服務注入 */
	@Autowired
	public CdSupvService sCdSupvService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6606 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iSupvReasonCode = titaVo.getParam("SupvReasonCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6606"); // 功能選擇錯誤
		}

		// 更新主管理由檔
		CdSupv tCdSupv = new CdSupv();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdSupv(tCdSupv, iFuncCode, titaVo);
			try {
				this.info("1");
				sCdSupvService.insert(tCdSupv, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iSupvReasonCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdSupv = sCdSupvService.holdById(iSupvReasonCode);
			if (tCdSupv == null) {
				throw new LogicException(titaVo, "E0003", iSupvReasonCode); // 修改資料不存在
			}
			CdSupv tCdSupv2 = (CdSupv) dataLog.clone(tCdSupv); ////
			try {
				moveCdSupv(tCdSupv, iFuncCode, titaVo);
				tCdSupv = sCdSupvService.update2(tCdSupv, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdSupv2, tCdSupv); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdSupv = sCdSupvService.holdById(iSupvReasonCode);
			if (tCdSupv != null) {
				try {
					sCdSupvService.delete(tCdSupv);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iSupvReasonCode); // 刪除資料不存在
			}
			break;

		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdSupv(CdSupv mCdSupv, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdSupv.setSupvReasonCode(titaVo.getParam("SupvReasonCode"));
		mCdSupv.setSupvReasonItem(titaVo.getParam("SupvReasonItem"));
		mCdSupv.setSupvReasonLevel(titaVo.getParam("SupvReasonLevel"));

		if (mFuncCode != 2) {
			mCdSupv.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdSupv.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdSupv.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdSupv.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
