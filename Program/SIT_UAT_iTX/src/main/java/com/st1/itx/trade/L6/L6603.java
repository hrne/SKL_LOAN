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
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.CdClId;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 ClCode1=9,1 ClCode2=9,2 ClItem=X,40 ClTypeJCIC=X,2 END=X,1
 */

@Service("L6603")
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */
public class L6603 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6603.class);

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6603 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iClCode1 = this.parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = this.parse.stringToInteger(titaVo.getParam("ClCode2"));

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6603"); // 功能選擇錯誤
		}

		// 更新擔保品代號資料檔
		CdCl tCdCl = new CdCl();
		CdClId tCdClId = new CdClId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdCl(tCdCl, tCdClId, iFuncCode, iClCode1, iClCode2, titaVo);
			try {
				this.info("1");
				sCdClService.insert(tCdCl, titaVo);
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
			tCdCl = sCdClService.holdById(new CdClId(iClCode1, iClCode2));
			if (tCdCl == null) {
				throw new LogicException(titaVo, "E0003", titaVo.getParam("ClCode1") + titaVo.getParam("ClCode2")); // 修改資料不存在
			}
			CdCl tCdCl2 = (CdCl) dataLog.clone(tCdCl); ////
			try {
				moveCdCl(tCdCl, tCdClId, iFuncCode, iClCode1, iClCode2, titaVo);
				tCdCl = sCdClService.update2(tCdCl, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCl2, tCdCl); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdCl = sCdClService.holdById(new CdClId(iClCode1, iClCode2));
			if (tCdCl != null) {
				try {
					sCdClService.delete(tCdCl);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("ClCode1") + titaVo.getParam("ClCode2")); // 刪除資料不存在
			}
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdCl(CdCl mCdCl, CdClId mCdClId, int mFuncCode, int mClCode1, int mClCode2, TitaVo titaVo) throws LogicException {

		mCdClId.setClCode1(mClCode1);
		mCdClId.setClCode2(mClCode2);
		mCdCl.setCdClId(mCdClId);

		mCdCl.setClCode1(this.parse.stringToInteger(titaVo.getParam("ClCode1")));
		mCdCl.setClCode2(this.parse.stringToInteger(titaVo.getParam("ClCode2")));
		mCdCl.setClItem(titaVo.getParam("ClItem"));
		mCdCl.setClTypeJCIC(titaVo.getParam("ClTypeJCIC"));

		if (mFuncCode != 2) {
			mCdCl.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdCl.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdCl.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdCl.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
