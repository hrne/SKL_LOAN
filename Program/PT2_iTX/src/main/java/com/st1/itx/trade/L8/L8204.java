package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.MlaundryRecordId;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 RecordDate=9,7 CustNo=9,7 FacmNo=9,3 BormNo=9,3
 * RepayDate=9,7 RepayAmt=9,14 Career=X,40 Income=X,60 RepaySource=9,2
 * RepayBank=X,20 ActualRepayDate=9,7 Description=X,120 END=X,1
 */

@Service("L8204")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8204 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryRecordService sMlaundryRecordService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;

//	private List<String> lStatusCode = new ArrayList<String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8204 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iRecordDate = this.parse.stringToInteger(titaVo.getParam("RecordDate"));
		int iFRecordDate = iRecordDate + 19110000;
		
		long iLogNo = Long.parseLong(titaVo.getParam("LogNo"));
		this.info("L8204 iFRecordDate : " + iFRecordDate);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L8204"); // 功能選擇錯誤
		}

		// 更新疑似洗錢交易訪談記錄檔
		this.info("iFuncCode: "+ iFuncCode);
		switch (iFuncCode) {
		case 1: // 新增
		case 3: // 複製
			insertMlaundryRecord(iFuncCode, iFRecordDate, iCustNo, iFacmNo, iBormNo,
					titaVo);
			break;
		case 2: // 修改
			updateMlaundryRecord(iLogNo, iFuncCode, iFRecordDate, iCustNo, iFacmNo,
					iBormNo, titaVo);
			break;
		case 4: // 刪除
			MlaundryRecord tMlaundryRecord = sMlaundryRecordService.findById(iLogNo, titaVo);
			MlaundryRecord tMlaundryRecordOriginal = (MlaundryRecord) dataLog.clone(tMlaundryRecord); ////
			this.info("L8204 del : " + iLogNo);

			// 刷主管卡後始可刪除
			// 交易需主管核可
			if (!titaVo.getHsupCode().equals("1")) {
				// titaVo.getSupCode();
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			if (tMlaundryRecord != null) {
				try {
					sMlaundryRecordService.delete(tMlaundryRecord, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("CustNo")); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tMlaundryRecordOriginal, tMlaundryRecordOriginal); ////
			dataLog.exec("刪除疑似洗錢交易訪談記錄"); ////
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void insertMlaundryRecord(int mFuncCode,
			int mFRecordDate, int mCustNo, int mFacmNo, int mBormNo, TitaVo titaVo) throws LogicException {

		MlaundryRecord tMlaundryRecord = new MlaundryRecord();
			
		setValues(tMlaundryRecord, mFuncCode, mFRecordDate, mCustNo, mFacmNo, mBormNo, titaVo);
		
		try {
			this.info("L8204 ins : " + mFRecordDate + "-" + mCustNo + "-" + mFacmNo + "-"
					+ mBormNo);
			sMlaundryRecordService.insert(tMlaundryRecord, titaVo);
		} catch (DBException e) {
			if (e.getErrorId() == 2) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("CustNo")); // 新增資料已存在
			} else {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}
	
	private void updateMlaundryRecord(long iLogNo, int mFuncCode,
			int mFRecordDate, int mCustNo, int mFacmNo, int mBormNo, TitaVo titaVo) throws LogicException {
		
		MlaundryRecord tMlaundryRecordToEdit = sMlaundryRecordService.holdById(iLogNo, titaVo);
		
		if (tMlaundryRecordToEdit == null)
			throw new LogicException("E0007", "找不到目標 LogNo: " + iLogNo);
		
		MlaundryRecord tMlaundryRecordOriginal = (MlaundryRecord) dataLog.clone(tMlaundryRecordToEdit); ////
		
		setValues(tMlaundryRecordToEdit, mFuncCode, mFRecordDate, mCustNo, mFacmNo, mBormNo, titaVo);
		
		try {

			tMlaundryRecordToEdit = sMlaundryRecordService.update2(tMlaundryRecordToEdit, titaVo); ////
		} catch (DBException e) {
			if (e.getErrorId() == 2) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("CustNo")); // 新增資料已存在
			} else {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		dataLog.setEnv(titaVo, tMlaundryRecordOriginal, tMlaundryRecordToEdit); ////
		dataLog.exec("修改疑似洗錢交易訪談記錄"); ////
	}
	
	private void setValues(MlaundryRecord tMlaundryRecord, int mFuncCode,
			int mFRecordDate, int mCustNo, int mFacmNo, int mBormNo, TitaVo titaVo) throws LogicException
	{
		tMlaundryRecord.setRecordDate(mFRecordDate);
		tMlaundryRecord.setCustNo(mCustNo);
		tMlaundryRecord.setFacmNo(mFacmNo);
		tMlaundryRecord.setBormNo(mBormNo);
		tMlaundryRecord.setRepayDate(this.parse.stringToInteger(titaVo.getParam("RepayDate")));
		tMlaundryRecord.setRepayAmt(this.parse.stringToBigDecimal(titaVo.getParam("RepayAmt")));
		tMlaundryRecord.setActualRepayDate(this.parse.stringToInteger(titaVo.getParam("ActualRepayDate")));
		tMlaundryRecord.setActualRepayAmt(this.parse.stringToBigDecimal(titaVo.getParam("ActualRepayAmt")));
		tMlaundryRecord.setCareer(titaVo.getParam("Career"));
		tMlaundryRecord.setIncome(titaVo.getParam("Income"));
		tMlaundryRecord.setRepaySource(this.parse.stringToInteger(titaVo.getParam("RepaySource")));
		tMlaundryRecord.setRepayBank(titaVo.getParam("RepayBank"));
		tMlaundryRecord.setDescription(titaVo.getParam("Description"));

		if (mFuncCode != 2) {
			tMlaundryRecord.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tMlaundryRecord.setCreateEmpNo(titaVo.getTlrNo());
		}
		tMlaundryRecord
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		tMlaundryRecord.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
