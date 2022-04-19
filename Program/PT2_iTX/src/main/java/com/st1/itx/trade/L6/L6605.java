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
import com.st1.itx.db.domain.CdOverdue;
import com.st1.itx.db.domain.CdOverdueId;
import com.st1.itx.db.service.CdOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 OverdueSign=X,1 OverdueCode=X,4 OverdueItem=X,100 END=X,1
 */

@Service("L6605")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6605 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdOverdueService sCdOverdueService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6605 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iOverdueSign = titaVo.getParam("OverdueSign");
		String iOverdueCode = titaVo.getParam("OverdueCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6605"); // 功能選擇錯誤
		}

		// 更新逾期新增減少原因檔
		CdOverdue tCdOverdue = new CdOverdue();
		CdOverdueId tCdOverdueId = new CdOverdueId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdOverdue(tCdOverdue, tCdOverdueId, iFuncCode, iOverdueSign, iOverdueCode, titaVo);
			try {
				this.info("1");
				sCdOverdueService.insert(tCdOverdue, titaVo);
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
			tCdOverdue = sCdOverdueService.holdById(new CdOverdueId(iOverdueSign, iOverdueCode));
			if (tCdOverdue == null) {
				throw new LogicException(titaVo, "E0003", iOverdueSign + iOverdueCode); // 修改資料不存在
			}
			CdOverdue tCdOverdue2 = (CdOverdue) dataLog.clone(tCdOverdue); ////
			try {
				moveCdOverdue(tCdOverdue, tCdOverdueId, iFuncCode, iOverdueSign, iOverdueCode, titaVo);
				tCdOverdue = sCdOverdueService.update2(tCdOverdue, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdOverdue2, tCdOverdue); ////
			dataLog.exec("修改逾期新增減少原因"); ////
			break;

		case 4: // 刪除
			// 去右邊零
			String mOverduecode = iOverdueCode.replaceAll("0+$", "");
			String cOverduecode = "";
			tCdOverdue = sCdOverdueService.holdById(new CdOverdueId(iOverdueSign, iOverdueCode));

			Slice<CdOverdue> slCdOverdue;
			slCdOverdue = sCdOverdueService.overdueCodeRange(iOverdueSign, iOverdueSign, "0000", "ZZZZ", this.index, this.limit, titaVo);
			List<CdOverdue> lCdOverdue = slCdOverdue == null ? null : slCdOverdue.getContent();

			if (lCdOverdue != null) {
				// 判斷是否為增減原因大類
				if (mOverduecode.length() == 1) {
					for (CdOverdue cCdOverdue : lCdOverdue) {
						cOverduecode = cCdOverdue.getOverdueCode().replaceAll("0+$", "");
						// 若第一碼相同
						if (cOverduecode.length() > 1) {
							if ((mOverduecode).equals(cOverduecode.subSequence(0, 1))) {
								this.info("Overduecode delet mOverduecode =" + mOverduecode + ",Db Overduecode=" + cOverduecode);
								throw new LogicException("", "增減原因下仍有細項存在,不可刪除");
							}

						}
					}

				}
			}

			if (tCdOverdue != null) {
				try {
					sCdOverdueService.delete(tCdOverdue);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iOverdueSign + iOverdueCode); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdOverdue, tCdOverdue); ////
			dataLog.exec("刪除逾期新增減少原因"); ////
			break;

		case 5: // inq
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdOverdue(CdOverdue mCdOverdue, CdOverdueId mCdOverdueId, int mFuncCode, String mOverdueSign, String mOverdueCode, TitaVo titaVo) throws LogicException {

		mCdOverdueId.setOverdueSign(mOverdueSign);
		mCdOverdueId.setOverdueCode(mOverdueCode);
		mCdOverdue.setCdOverdueId(mCdOverdueId);

		mCdOverdue.setOverdueSign(titaVo.getParam("OverdueSign"));
		mCdOverdue.setOverdueCode(titaVo.getParam("OverdueCode"));
		mCdOverdue.setOverdueItem(titaVo.getParam("OverdueItem"));

		if (mFuncCode != 2) {
			mCdOverdue.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdOverdue.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdOverdue.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdOverdue.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
