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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 AcNoCode=X,8 AcSubCode=X,5 AcDtlCode=X,2 AcNoItem=X,80
 * AcctCode=X,3 AcctItem=X,40 ClassCode=N,1 AcBookFlag=N,1 DbCr=X,1 AcctFlag=N,1
 * ReceivableFlag=N,1 ClsChkFlag=N,1 InuseFlag=N,1 END=X,1
 */

@Service("L6601")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6601 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6601.class);

	/* DB服務注入 */
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6601 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iAcNoCode = titaVo.getParam("AcNoCode");
		String iAcSubCode = titaVo.getParam("AcSubCode");
		String iAcDtlCode = titaVo.getParam("AcDtlCode");

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6601"); // 功能選擇錯誤
		}
		if (iAcNoCode.isEmpty()) {
			throw new LogicException(titaVo, "E6010", "科目代號"); // 新增資料不可為空白
		}

		// 更新會計科子細目設定檔
		CdAcCode tCdAcCode = new CdAcCode();
		CdAcCodeId tCdAcCodeId = new CdAcCodeId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdAcCode(tCdAcCode, tCdAcCodeId, iFuncCode, iAcNoCode, iAcSubCode, iAcDtlCode, titaVo);
			try {
				this.info("1");
				sCdAcCodeService.insert(tCdAcCode, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iAcNoCode + "-" + iAcSubCode + "-" + iAcDtlCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdAcCode = sCdAcCodeService.holdById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode));
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E0003", iAcNoCode + "-" + iAcSubCode + "-" + iAcDtlCode); // 修改資料不存在
			}
			CdAcCode tCdAcCode2 = (CdAcCode) dataLog.clone(tCdAcCode); ////
			try {
				moveCdAcCode(tCdAcCode, tCdAcCodeId, iFuncCode, iAcNoCode, iAcSubCode, iAcDtlCode, titaVo);
				tCdAcCode = sCdAcCodeService.update2(tCdAcCode, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdAcCode2, tCdAcCode); ////
			dataLog.exec(); ////
			break;

		case 4: // 刪除
			tCdAcCode = sCdAcCodeService.holdById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode));
			if (tCdAcCode != null) {
				try {
					sCdAcCodeService.delete(tCdAcCode);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iAcNoCode + "-" + iAcSubCode + "-" + iAcDtlCode); // 刪除資料不存在
			}
			break;

		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdAcCode(CdAcCode mCdAcCode, CdAcCodeId mCdAcCodeId, int mFuncCode, String mAcNoCode, String mAcSubCode, String mAcDtlCode, TitaVo titaVo) throws LogicException {

		mCdAcCodeId.setAcNoCode(mAcNoCode);
		mCdAcCodeId.setAcSubCode(mAcSubCode);
		mCdAcCodeId.setAcDtlCode(mAcDtlCode);
		mCdAcCode.setCdAcCodeId(mCdAcCodeId);

		mCdAcCode.setAcNoItem(titaVo.getParam("AcNoItem"));
		mCdAcCode.setAcctCode(titaVo.getParam("AcctCode"));
		mCdAcCode.setAcctItem(titaVo.getParam("AcctItem"));
		mCdAcCode.setClassCode(this.parse.stringToInteger(titaVo.getParam("ClassCode")));
		mCdAcCode.setAcBookFlag(this.parse.stringToInteger(titaVo.getParam("AcBookFlag")));
		mCdAcCode.setDbCr(titaVo.getParam("DbCr"));
		mCdAcCode.setAcctFlag(this.parse.stringToInteger(titaVo.getParam("AcctFlag")));
		mCdAcCode.setReceivableFlag(this.parse.stringToInteger(titaVo.getParam("ReceivableFlag")));
		mCdAcCode.setClsChkFlag(this.parse.stringToInteger(titaVo.getParam("ClsChkFlag")));
		mCdAcCode.setInuseFlag(this.parse.stringToInteger(titaVo.getParam("InuseFlag")));

		if (mFuncCode != 2) {
			mCdAcCode.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdAcCode.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdAcCode.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdAcCode.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
