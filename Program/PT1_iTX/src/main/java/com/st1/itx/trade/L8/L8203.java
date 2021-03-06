package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 AcDate=9,7 CustNo=9,7 FacmNo=9,3 BormNo=9,3 Factor=9,2
 * TotalAmt=9,14 TotalCnt=9,4 MemoSeq=9,2 Rational=X,1 EmpNoDesc=X,100
 * ManagerDesc=X,100 END=X,1
 */

@Service("L8203")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8203 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public MlaundryDetailService sMlaundryDetailService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8203 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		int iFactor = this.parse.stringToInteger(titaVo.getParam("Factor"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
//		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
//		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFEntryDate = iEntryDate + 19110000;
		int iLevel = this.txBuffer.getTxCom().getTlrLevel();
		this.info("L8203 iFAcDate : " + iFEntryDate);

		String iManagerCheck = titaVo.getParam("ManagerCheck");
		int iManagerDate = this.parse.stringToInteger(titaVo.getParam("ManagerDate"));

		int iFManagerDate = 0;
		if (iManagerDate != 0) {
			iFManagerDate = iManagerDate + 19110000;
		}
		this.info("L8203 iFManagerDate : " + iFManagerDate);

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L8203"); // 功能選擇錯誤
		}

		if (iFuncCode == 4) {
			titaVo.put("RELCD", "1");
		}

		// 更新疑似洗錢交易合理性明細檔
		MlaundryDetail tMlaundryDetail = new MlaundryDetail();
		MlaundryDetailId tMlaundryDetailId = new MlaundryDetailId();
		switch (iFuncCode) {
		case 1: // 新增
			moveMlaundryDetail(tMlaundryDetail, tMlaundryDetailId, iFuncCode, iFEntryDate, iFactor, iCustNo, iManagerCheck, iFManagerDate, titaVo);
			try {
				this.info("1");
				sMlaundryDetailService.insert(tMlaundryDetail);
				this.info("L8203 ins : " + iFuncCode + "-" + iFEntryDate + "-" + iFactor + "-" + iCustNo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", titaVo.getParam("CustNo")); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;
		case 2: // 修改
			tMlaundryDetail = sMlaundryDetailService.holdById(new MlaundryDetailId(iFEntryDate, iFactor, iCustNo));
			this.info("L8203 upd : " + iFuncCode + "-" + iFEntryDate + "-" + iFactor + "-" + iCustNo);
			if (tMlaundryDetail == null) {
				throw new LogicException(titaVo, "E0003", titaVo.getParam("CustNo")); // 修改資料不存在
			}
			MlaundryDetail tMlaundryDetail2 = (MlaundryDetail) dataLog.clone(tMlaundryDetail); ////

			try {
				moveMlaundryDetail(tMlaundryDetail, tMlaundryDetailId, iFuncCode, iFEntryDate, iFactor, iCustNo, iManagerCheck, iFManagerDate, titaVo);
				tMlaundryDetail = sMlaundryDetailService.update2(tMlaundryDetail, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tMlaundryDetail2, tMlaundryDetail); ////
			if (iLevel == 1) {
				dataLog.exec("主管覆核");
			} else {
				dataLog.exec("經辦修改合理性");
			}

			break;
		case 4: // 刪除
			tMlaundryDetail = sMlaundryDetailService.holdById(new MlaundryDetailId(iFEntryDate, iFactor, iCustNo));
			this.info("L8203 del : " + iFuncCode + "-" + iFEntryDate + "-" + iFactor + "-" + iCustNo);

			// 刷主管卡後始可刪除
			// 交易需主管核可
			if (!titaVo.getHsupCode().equals("1")) {
				// titaVo.getSupCode();
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			if (tMlaundryDetail != null) {

				try {
					sMlaundryDetailService.delete(tMlaundryDetail);

				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("CustNo")); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tMlaundryDetail, tMlaundryDetail); ////
			dataLog.exec("刪除疑似洗錢交易合理性");
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveMlaundryDetail(MlaundryDetail mMlaundryDetail, MlaundryDetailId mMlaundryDetailId, int mFuncCode, int mFEntryDate, int mFactor, int mCustNo, String iManagerCheck,
			int iFManagerDate, TitaVo titaVo) throws LogicException {

		mMlaundryDetailId.setEntryDate(mFEntryDate);
		mMlaundryDetailId.setFactor(mFactor);
		mMlaundryDetailId.setCustNo(mCustNo);
		mMlaundryDetail.setMlaundryDetailId(mMlaundryDetailId);

		mMlaundryDetail.setManagerDate(iFManagerDate);// 主管同意日期

		int iManagerCheckDate = 0;
		if (Integer.valueOf(titaVo.getParam("ManagerCheckDate")) != 0) {
			iManagerCheckDate = Integer.valueOf(titaVo.getParam("ManagerCheckDate")) + 19110000;
		}
		this.info("ManagerCheckDate=" + iManagerCheckDate);
		mMlaundryDetail.setManagerCheckDate(iManagerCheckDate);// 主管覆核日期
		mMlaundryDetail.setManagerCheck(iManagerCheck);

		mMlaundryDetail.setTotalAmt(this.parse.stringToBigDecimal(titaVo.getParam("TotalAmt")));
		mMlaundryDetail.setTotalCnt(this.parse.stringToInteger(titaVo.getParam("TotalCnt")));
		mMlaundryDetail.setRational(titaVo.getParam("Rational"));
		mMlaundryDetail.setEmpNoDesc(titaVo.getParam("EmpNoDesc"));
		mMlaundryDetail.setManagerDesc(titaVo.getParam("ManagerDesc"));

//		if (mFuncCode != 2) {
//			mMlaundryDetail.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
//			mMlaundryDetail.setCreateEmpNo(titaVo.getTlrNo());
//		}
		mMlaundryDetail.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mMlaundryDetail.setLastUpdateEmpNo(titaVo.getTlrNo());
		this.info("getTlrNo==" + titaVo.getTlrNo());
	}
}
