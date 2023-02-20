package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryDetail;
import com.st1.itx.db.domain.MlaundryDetailId;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.domain.MlaundryChkDtl;
import com.st1.itx.db.service.MlaundryDetailService;
import com.st1.itx.db.service.MlaundryChkDtlService;
import com.st1.itx.db.service.TxFlowService;
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
	public MlaundryChkDtlService sMlaundryChkDtlService;
	@Autowired
	public TxFlowService sTxFlowService;

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
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFEntryDate = iEntryDate + 19110000;
		int iLevel = this.txBuffer.getTxCom().getTlrLevel();
		//this.info("L8203 iFAcDate : " + iFEntryDate);

		String iManagerCheck = titaVo.getParam("ManagerCheck");
		int iManagerDate = this.parse.stringToInteger(titaVo.getParam("ManagerDate"));//主管同意日期

		int iFManagerDate = 0;
		if (iManagerDate != 0) {
			iFManagerDate = iManagerDate + 19110000;
		}
		//this.info("L8203 iFManagerDate : " + iFManagerDate);

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
			moveMlaundryDetail(tMlaundryDetail, tMlaundryDetailId, iFuncCode, iFEntryDate, iFactor, iCustNo,
					iManagerCheck, iFManagerDate, titaVo);
			try {
				//this.info("1");
				sMlaundryDetailService.insert(tMlaundryDetail, titaVo);
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
			tMlaundryDetail = sMlaundryDetailService.holdById(new MlaundryDetailId(iFEntryDate, iFactor, iCustNo), titaVo);
			this.info("L8203 upd : " + iFuncCode + "-" + iFEntryDate + "-" + iFactor + "-" + iCustNo);
			if (tMlaundryDetail == null) {
				throw new LogicException(titaVo, "E0003", titaVo.getParam("CustNo")); // 修改資料不存在
			}
			MlaundryDetail tMlaundryDetail2 = (MlaundryDetail) dataLog.clone(tMlaundryDetail); ////

			moveMlaundryDetail(tMlaundryDetail, tMlaundryDetailId, iFuncCode, iFEntryDate, iFactor, iCustNo,
					iManagerCheck, iFManagerDate, titaVo);
			try {
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
			tMlaundryDetail = sMlaundryDetailService.holdById(new MlaundryDetailId(iFEntryDate, iFactor, iCustNo), titaVo);
			this.info("L8203 del : " + iFuncCode + "-" + iFEntryDate + "-" + iFactor + "-" + iCustNo);

			// 刷主管卡後始可刪除
			// 交易需主管核可
			if (!titaVo.getHsupCode().equals("1")) {
				// titaVo.getSupCode();
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			if (tMlaundryDetail != null) {
				try {
					sMlaundryDetailService.delete(tMlaundryDetail, titaVo);

				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", titaVo.getParam("CustNo")); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tMlaundryDetail, tMlaundryDetail); ////
			dataLog.exec("刪除疑似洗錢交易合理性");
			// 一併刪除疑似洗錢檢核資料MlaundryChkDtl
			DelMlaundryChkDtl(iFEntryDate, iFEntryDate, iFactor, iCustNo, titaVo);

			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveMlaundryDetail(MlaundryDetail mMlaundryDetail, MlaundryDetailId mMlaundryDetailId, int mFuncCode,
			int mFEntryDate, int mFactor, int mCustNo, String iManagerCheck, int iFManagerDate, TitaVo titaVo)
			throws LogicException {

		int selectTotal = this.parse.stringToInteger(titaVo.get("selectTotal"));//整批放行筆數
		int iAcDate = this.getTxBuffer().getMgBizDate().getTbsDyf();//本營業日-西元

		Date dateNow = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int iToday = Integer.parseInt(sdf.format(dateNow)) ; // 日曆日-西元年月日

		int iManagerCheckDate = 0;
		String iFlowNo = titaVo.getTxSeq();// 流程控制序號
		int flowmode = 0;

		int flentdy = 0;
		if (mMlaundryDetail.getFlEntdy() != 0) {// 經辦做過修改
			flentdy = mMlaundryDetail.getFlEntdy() + 19110000;
		}
		if (titaVo.isActfgEntry() && flentdy != 0) {// 經辦
			TxFlowId tTxFlowId = new TxFlowId();
			tTxFlowId.setEntdy(flentdy);
			tTxFlowId.setFlowNo(mMlaundryDetail.getFlowNo());
			TxFlow tTxFlow = sTxFlowService.findById(tTxFlowId, titaVo);
			if (tTxFlow != null) {
				flowmode = tTxFlow.getFlowMode();// 1:待放行
			}
		}
		
		if (titaVo.isActfgEntry()) {// 經辦需檢核流程模式,若為待放行則只可修正,不可用修改以免產生新的TxFlow
			if (titaVo.isHcodeNormal()) {// 修改
				if (flowmode == 0) {// 非待放行
					mMlaundryDetail.setFlEntdy(iAcDate);
					mMlaundryDetail.setFlowNo(iFlowNo);
				} else {
					throw new LogicException(titaVo, "E0015", "主管未放行，只可修正"); // 檢查錯誤
				}
			}
		}
		
		if (Integer.valueOf(titaVo.getParam("ManagerCheckDate")) != 0) {//主管覆核日期
			iManagerCheckDate = Integer.valueOf(titaVo.getParam("ManagerCheckDate")) + 19110000;
		}
//		this.info("ManagerCheckDate=" + iManagerCheckDate);

		if (titaVo.isActfgSuprele()) {// 主管放行檢查欄位:整批放行需更新主管覆核欄位=Y及日期
			if ( selectTotal > 0) {//整批放行
				iManagerCheck = "Y";
				iFManagerDate = iToday;
				iManagerCheckDate = iToday;
			}
		}

		mMlaundryDetailId.setEntryDate(mFEntryDate);
		mMlaundryDetailId.setFactor(mFactor);
		mMlaundryDetailId.setCustNo(mCustNo);
		mMlaundryDetail.setMlaundryDetailId(mMlaundryDetailId);

		mMlaundryDetail.setManagerDate(iFManagerDate);// 主管同意日期

		mMlaundryDetail.setManagerCheckDate(iManagerCheckDate);// 主管覆核日期
		mMlaundryDetail.setManagerCheck(iManagerCheck);

		mMlaundryDetail.setTotalAmt(this.parse.stringToBigDecimal(titaVo.getParam("TotalAmt")));
		mMlaundryDetail.setTotalCnt(this.parse.stringToInteger(titaVo.getParam("TotalCnt")));
		mMlaundryDetail.setRational(titaVo.getParam("Rational"));
		mMlaundryDetail.setEmpNoDesc(titaVo.getParam("EmpNoDesc"));
		mMlaundryDetail.setManagerDesc(titaVo.getParam("ManagerDesc"));

		mMlaundryDetail
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mMlaundryDetail.setLastUpdateEmpNo(titaVo.getTlrNo());
//		this.info("getTlrNo==" + titaVo.getTlrNo());
	}

	public void DelMlaundryChkDtl(int iFEntryDateS, int iFEntryDateE, int iFactor, int iCustNo, TitaVo titaVo)
			throws LogicException {
		// 2022/11/11 :刪除該疑似洗錢合理性對應的疑似洗錢檢核資料BY珮瑜需求
		Slice<MlaundryChkDtl> slMlaundryChkDtl = sMlaundryChkDtlService.findEntryDateRangeFactorCustNo(iFEntryDateS,
				iFEntryDateE, iFactor, iCustNo, 0, Integer.MAX_VALUE, titaVo);
		List<MlaundryChkDtl> lMlaundryChkDtl = slMlaundryChkDtl == null ? null : slMlaundryChkDtl.getContent();

		if (lMlaundryChkDtl != null && lMlaundryChkDtl.size() != 0) {
			try {
				sMlaundryChkDtlService.deleteAll(lMlaundryChkDtl, titaVo);
			} catch (DBException e) {
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}

}
