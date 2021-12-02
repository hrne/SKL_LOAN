package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.CdAcBookId;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 AcBookCode=X,3 AcBookItem=X,40 CurrencyCode=X,3
 * TargetAmt=m,14 AssignSeq=9,2 AcctSource=X,1 AdjDate=9,7 END=X,1
 */

@Service("L6709")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6709 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdAcBookService sCdAcBookService;
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6709 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iAdjDate = this.parse.stringToInteger(titaVo.getParam("AdjDate"));
//    int iDefNo8002 = 8002;
//    int iDefNo8003 = 8003;
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6709"); // 功能選擇錯誤
		}

		// 8002的選單含 000全帳冊 & 10H放款帳冊(執行L6604建立)
		// 更新各類代碼檔 - 新增時建立代碼檔 , 刪除時 Enable維護為"N" , 需執行L6604才會刪除代碼檔
		insCdCode(iFuncCode, "AcSubBookCode", iAcSubBookCode, titaVo);

		// 8003的選單只有利變年金
		// 更新各類代碼檔 - 新增時建立代碼檔 , 刪除時 Enable維護為"N" , 需執行L6604才會刪除代碼檔
//		insCdCode(iFuncCode, "AcBookCode0", iAcBookCode, titaVo);

		// 更新系統參數設定檔 - 帳冊別帳務調整日期 (批次執行帳務調整)
		updSystemParas(iFuncCode, iAdjDate, titaVo);

		// 更新帳冊別金額設定檔
		CdAcBook tCdAcBook = new CdAcBook();
		CdAcBookId tCdAcBookId = new CdAcBookId();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdAcBook(tCdAcBook, tCdAcBookId, iFuncCode, iAcBookCode, iAcSubBookCode, titaVo);

			try {
				this.info("1");
				this.info("tCdAcBooktCdAcBook=" + tCdAcBook);
				sCdAcBookService.insert(tCdAcBook, titaVo);
				this.info("L6709 ins : " + iFuncCode + iAcBookCode);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdAcBook = sCdAcBookService.holdById(new CdAcBookId(iAcBookCode, iAcSubBookCode));
			if (tCdAcBook == null) {
				throw new LogicException(titaVo, "E0003", iAcBookCode); // 修改資料不存在
			}
			CdAcBook tCdAcBook2 = (CdAcBook) dataLog.clone(tCdAcBook); ////
			try {
				moveCdAcBook(tCdAcBook, tCdAcBookId, iFuncCode, iAcBookCode, iAcSubBookCode, titaVo);
				tCdAcBook = sCdAcBookService.update2(tCdAcBook, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdAcBook2, tCdAcBook); ////
			dataLog.exec("修改帳冊別目標金額"); ////
			break;

		case 4: // 刪除
			tCdAcBook = sCdAcBookService.holdById(new CdAcBookId(iAcBookCode, iAcSubBookCode));
			this.info("L6709 del : " + iFuncCode + iAcBookCode);
			if (tCdAcBook != null) {
				try {
					sCdAcBookService.delete(tCdAcBook);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iAcBookCode); // 刪除資料不存在
			}
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdAcBook(CdAcBook mCdAcBook, CdAcBookId mCdAcBookId, int mFuncCode, String mAcBookCode, String mAcSubBookCode, TitaVo titaVo) throws LogicException {
		mCdAcBookId.setAcBookCode(titaVo.getParam("AcBookCode"));
		mCdAcBookId.setAcSubBookCode(titaVo.getParam("AcSubBookCode"));
		mCdAcBook.setCdAcBookId(mCdAcBookId);
		mCdAcBook.setAcBookCode(titaVo.getParam("AcBookCode"));
		mCdAcBook.setAcSubBookCode(titaVo.getParam("AcSubBookCode"));
		mCdAcBook.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		mCdAcBook.setTargetAmt(this.parse.stringToBigDecimal(titaVo.getParam("TargetAmt")));
		mCdAcBook.setAssignSeq(this.parse.stringToInteger(titaVo.getParam("AssignSeq")));
		mCdAcBook.setAcctSource(titaVo.getParam("AcctSource"));

		if (mFuncCode != 2) {
			mCdAcBook.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdAcBook.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdAcBook.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdAcBook.setLastUpdateEmpNo(titaVo.getTlrNo());

	}

	private void updSystemParas(int uFuncCode, int uAdjDate, TitaVo titaVo) throws LogicException {

		SystemParas tSystemParas = new SystemParas();
		tSystemParas = sSystemParasService.holdById("LN");
		if (tSystemParas == null) {
			throw new LogicException(titaVo, "E0003", "系統參數設定檔:" + titaVo.getParam("AdjDate")); // 修改資料不存在
		}
		SystemParas tSystemParas2 = (SystemParas) dataLog.clone(tSystemParas); ////
		try {
			tSystemParas.setAcBookAdjDate(uAdjDate);
			tSystemParas = sSystemParasService.update2(tSystemParas); ////
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		dataLog.setEnv(titaVo, tSystemParas2, tSystemParas); ////
		dataLog.exec("修改系統參數設定檔"); ////

	}

	private void insCdCode(int mFuncCode, String mDefCode, String mAcBookCode, TitaVo titaVo) throws LogicException {

		// 檢查代碼檔是否已存在
		CdCode fCdCode = new CdCode();
		fCdCode = sCdCodeService.findById(new CdCodeId(mDefCode, mAcBookCode));
		if (fCdCode != null && mFuncCode == 1) {
			mFuncCode = 2;
		}
		this.info("L6709 mFuncCode : " + mFuncCode);

		// 更新各類代碼檔
		CdCode tCdCode = new CdCode();
		CdCodeId tCdCodeId = new CdCodeId();

		tCdCodeId.setDefCode(mDefCode);
		tCdCodeId.setCode(mAcBookCode);

		switch (mFuncCode) {
		case 1: // 新增
			tCdCode.setCdCodeId(tCdCodeId);
			tCdCode = moveCdCode(tCdCode, mFuncCode, "Y", titaVo);
			try {
				sCdCodeService.insert(tCdCode);
				this.info("L6709 insCdCode : " + mFuncCode + mDefCode + mAcBookCode);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					break; // 代碼檔新增資料已存在時離開
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdCode = sCdCodeService.holdById(tCdCodeId);
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", "代碼檔:" + mAcBookCode); // 修改資料不存在
			}
			CdCode tCdCode2 = (CdCode) dataLog.clone(tCdCode); ////
			try {
				tCdCode = moveCdCode(tCdCode, mFuncCode, "Y", titaVo);
				tCdCode = sCdCodeService.update2(tCdCode); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode2, tCdCode); ////
			dataLog.exec("修改各類代碼檔"); ////
			break;

		case 4: // 刪除時 Enable維護為"N"
			tCdCode = sCdCodeService.holdById(tCdCodeId);
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", "代碼檔:" + mAcBookCode); // 修改資料不存在
			}
			CdCode tCdCode4 = (CdCode) dataLog.clone(tCdCode); ////
			try {
				tCdCode = moveCdCode(tCdCode, mFuncCode, "N", titaVo);
				tCdCode = sCdCodeService.update2(tCdCode); ////
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
		mCdCode.setItem(titaVo.getParam("AcSubBookItem"));
		mCdCode.setEnable(mEnable);

		if (mFuncCode == 1) {
			mCdCode.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdCode.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdCode.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdCode.setLastUpdateEmpNo(titaVo.getTlrNo());

		return mCdCode;
	}

}
