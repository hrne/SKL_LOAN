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
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 GuaRelCode=X,2 GuaRelItem=X,60 GuaRelJcic=X,2 END=X,1
 */

@Service("L6607")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6607 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6607.class);

	/* DB服務注入 */
	@Autowired
	public CdGuarantorService sCdGuarantorService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6607 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iGuaRelCode = titaVo.getParam("GuaRelCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6607"); // 功能選擇錯誤
		}

		// 更新保證人關係代碼檔
		CdGuarantor tCdGuarantor = new CdGuarantor();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdGuarantor(tCdGuarantor, iFuncCode, titaVo);
			try {
				this.info("1");
				sCdGuarantorService.insert(tCdGuarantor, titaVo);
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
			tCdGuarantor = sCdGuarantorService.holdById(iGuaRelCode);
			if (tCdGuarantor == null) {
				throw new LogicException(titaVo, "E0003", iGuaRelCode); // 修改資料不存在
			}
			CdGuarantor tCdGuarantor2 = (CdGuarantor) dataLog.clone(tCdGuarantor); ////
			try {
				moveCdGuarantor(tCdGuarantor, iFuncCode, titaVo);
				tCdGuarantor = sCdGuarantorService.update2(tCdGuarantor, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdGuarantor2, tCdGuarantor); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdGuarantor = sCdGuarantorService.holdById(iGuaRelCode);
			this.info("L6607 del : " + iFuncCode + iGuaRelCode);
			if (tCdGuarantor != null) {
				try {
					sCdGuarantorService.delete(tCdGuarantor);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iGuaRelCode); // 刪除資料不存在
			}
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdGuarantor(CdGuarantor mCdGuarantor, int mFuncCode, TitaVo titaVo) throws LogicException {

		mCdGuarantor.setGuaRelCode(titaVo.getParam("GuaRelCode"));
		mCdGuarantor.setGuaRelItem(titaVo.getParam("GuaRelItem"));
		mCdGuarantor.setGuaRelJcic(titaVo.getParam("GuaRelJcic"));

		if (mFuncCode != 2) {
			mCdGuarantor.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdGuarantor.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdGuarantor.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdGuarantor.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
