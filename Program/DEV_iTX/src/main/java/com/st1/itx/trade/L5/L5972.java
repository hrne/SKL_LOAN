package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* DB容器 */
import com.st1.itx.db.domain.CustMain;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

/**
 * Tita<br>
 * FunctionCode=X,2<br>
 * CustNo=9,7<br>
 * CaseSeq=9,3<br>
 * AcDate=9,7<br>
 * TitaTlrNo=X,6<br>
 * TitaTxtNo=9,8<br>
 * CustId=X,10<br>
 * DateType=9,1<br>
 * DateFrom=9,7<br>
 * DateTo=9,7<br>
 */

@Service("L5972")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5972 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5972");
		this.info("active L5972 ");
		this.totaVo.init(titaVo);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 313 * 100 = 31300

		String FunctionCode = titaVo.getParam("FunctionCode").trim(); // 功能代碼
		String CustNo = titaVo.getParam("CustNo").trim(); // 戶號
		String CaseSeq = titaVo.getParam("CaseSeq").trim(); // 案件序號
		String AcDate = titaVo.getParam("AcDate").trim(); // 會計日期
		String TitaTlrNo = titaVo.getParam("TitaTlrNo").trim(); // 經辦
		String TitaTxtNo = titaVo.getParam("TitaTxtNo").trim(); // 交易序號
		String CustId = titaVo.getParam("CustId").trim(); // 身分證字號
		String DateType = titaVo.getParam("DateType").trim(); // 日期選項(1:會計日期;2:入帳日期)
		String DateFrom = titaVo.getParam("DateFrom").trim(); // 日期區間(起)
		String DateTo = titaVo.getParam("DateTo").trim(); // 日期區間(訖)

		this.info("L5972 Input FunctionCode=[" + FunctionCode + "],CustNo=[" + CustNo + "],CaseSeq=[" + CaseSeq + "],AcDate=[" + AcDate + "],TitaTlrNo=[" + TitaTlrNo + "],TitaTxtNo=[" + TitaTxtNo
				+ "],CustId=[" + CustId + "],DateType=[" + DateType + "],DateFrom=[" + DateFrom + "],DateTo=[" + DateTo + "]");
		int iCustNo = 0;
		int IntAcDate = 0;
		if (AcDate != null && AcDate.length() != 0) {
			IntAcDate = parse.stringToInteger(AcDate);
			if (String.valueOf(IntAcDate).length() == 7) {
				IntAcDate = IntAcDate + 19110000;
			}
		}

		Slice<NegTrans> slNegTrans = null;
		List<NegTrans> lNegTrans = new ArrayList<NegTrans>();
		if (FunctionCode != null) {
			if (("").equals(FunctionCode)) {
				// 使用L5972原始的查詢
				// CustId,DateType,DateFrom,DateTo
				// DateType 1:會計日期;2:入帳日期
				int iDateFrom = parse.stringToInteger(DateFrom);
				int iDateTo = parse.stringToInteger(DateTo);
				if (String.valueOf(iDateFrom).length() == 7) {
					iDateFrom = iDateFrom + 19110000;
				}
				if (String.valueOf(iDateTo).length() == 7) {
					iDateTo = iDateTo + 19110000;
				}
				this.info("L5972 FunctionCode IS Null iDateFrom=[" + iDateFrom + "],iDateTo=[" + iDateTo + "]");
				if (CustId != null && CustId.length() != 0) {
					CustMain CustMainVO = sCustMainService.custIdFirst(CustId);
					if (CustMainVO != null) {
						iCustNo = CustMainVO.getCustNo();
					} else {
						// E0001 查詢資料不存在
						throw new LogicException("E0001", "客戶資料主檔");
					}

					if (("1").equals(DateType)) {
						// 會計日期
						slNegTrans = sNegTransService.custAndAcDate(iCustNo, iDateFrom, iDateTo, this.index, this.limit, titaVo);
					} else if (("2").equals(DateType)) {
						// 入帳日期
						slNegTrans = sNegTransService.custAndEntryDate(iCustNo, iDateFrom, iDateTo, this.index, this.limit, titaVo);
					} else {
						slNegTrans = sNegTransService.custNoEq(iCustNo, this.index, this.limit, titaVo);
					}
				} else {
					// CustId is null
					if (("1").equals(DateType)) {
						// 會計日期
						slNegTrans = sNegTransService.acDateBetween(iDateFrom, iDateTo, this.index, this.limit, titaVo);
					} else if (("2").equals(DateType)) {
						// 入帳日期
						slNegTrans = sNegTransService.entryDateBetween(iDateFrom, iDateTo, this.index, this.limit, titaVo);
					}
				}
				lNegTrans = slNegTrans == null ? null : slNegTrans.getContent();
			} else if (("01").equals(FunctionCode)) {
				// 連動交易直接用KEY查詢 (L5971) - 一筆
				if (CustNo != null && CustNo.length() != 0) {
					iCustNo = parse.stringToInteger(CustNo);
				}
				NegTransId NegTransIdVO = new NegTransId();
				NegTransIdVO.setAcDate(IntAcDate);
				NegTransIdVO.setTitaTlrNo(TitaTlrNo);
				NegTransIdVO.setTitaTxtNo(parse.stringToInteger(TitaTxtNo));

				NegTrans NegTransVO = sNegTransService.findById(NegTransIdVO);
				if (NegTransVO != null) {
					lNegTrans.add(NegTransVO);
				}
			} else if (("02").equals(FunctionCode)) {
				// 連動交易 (L5075)
				if (CustNo != null && CustNo.length() != 0) {
					iCustNo = parse.stringToInteger(CustNo);
				}
				slNegTrans = sNegTransService.custNoEq(iCustNo, this.index, this.limit, titaVo);
				lNegTrans = slNegTrans == null ? null : slNegTrans.getContent();
			}

		}

		if (lNegTrans != null && lNegTrans.size() != 0) {
			for (NegTrans NegTransVO : lNegTrans) {
				OccursList occursList = new OccursList();
				int ThisCustNo = NegTransVO.getCustNo();
				CustMain CustMainVO = sCustMainService.custNoFirst(ThisCustNo, ThisCustNo, titaVo);

				occursList.putParam("OOCustId", CustMainVO.getCustId());// 身分證號
				occursList.putParam("OOCustNo", ThisCustNo);// 戶號
				occursList.putParam("OOCustNM", CustMainVO.getCustName());// 戶名
				occursList.putParam("OOEntryDate", NegTransVO.getEntryDate());// 入帳日期
				occursList.putParam("OOTxKind", NegTransVO.getTxKind());// 交易別
				occursList.putParam("OOTxAmt", NegTransVO.getTxAmt());// 交易金額
				occursList.putParam("OOIntEndDate", NegTransVO.getIntEndDate());// 繳息迄日
				occursList.putParam("OOTempRepayAmt", NegTransVO.getTempRepayAmt());// 暫收抵繳
				occursList.putParam("OOOverRepayAmt", NegTransVO.getOverRepayAmt());// 溢收抵繳
				occursList.putParam("OOPrincipalAmt", NegTransVO.getPrincipalAmt());// 本金
				occursList.putParam("OOInterestAmt", NegTransVO.getInterestAmt());// 利息
				occursList.putParam("OOOverAmt", NegTransVO.getOverAmt());// 轉入溢繳
				occursList.putParam("OOSklShareAmt", NegTransVO.getSklShareAmt());// 新壽攤分
				occursList.putParam("OOAcDate", NegTransVO.getAcDate());// 會計日期
				occursList.putParam("OOTitaTxtNo", NegTransVO.getTitaTxtNo());// 交易序號
				occursList.putParam("OOCaseSeq", NegTransVO.getCaseSeq());// 案件序號
				occursList.putParam("OOTitaTlrNo", NegTransVO.getTitaTlrNo());// 經辦
				occursList.putParam("OOTitaHCode", "");// 訂正別
				occursList.putParam("OOPrincipalBal", NegTransVO.getPrincipalBal());// 本金餘額
				occursList.putParam("OOReturnAmt", NegTransVO.getReturnAmt());// 退還金額
				occursList.putParam("OOApprAmt", NegTransVO.getApprAmt());// 撥付金額
				occursList.putParam("OOExportDate", NegTransVO.getExportDate());// 撥付製檔日
				occursList.putParam("OOIntStartDate", NegTransVO.getIntStartDate());// 繳息起日
				occursList.putParam("OORepayPeriod", NegTransVO.getRepayPeriod());// 還款期數
				this.totaVo.addOccursList(occursList);
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slNegTrans != null && slNegTrans.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}

		} else {
			// 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "債務協商交易檔");
		}
		/*
		 * #OOCustId #OOCustNo #OOCustNM #OOEntryDate #OOTxKind #OOTxAmt #OOIntEndDate
		 * #OOTempRepayAmt #OOOverRepayAmt #OOPrincipalAmt #OOInterestAmt #OOOverAmt
		 * #OOSklShareAmt #OOAcDate #OOTitaTxtNo #OOCaseSeq #OOTitaTlrNo #OOTitaHCode
		 * #OOPrincipalBal #OOReturnAmt #OOApprAmt #OOExportDate #OOIntStartDate
		 * #OORepayPeriod
		 */
		this.addList(this.totaVo);
		return this.sendList();
	}
}