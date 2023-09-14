package com.st1.itx.trade.L5;

import java.math.BigDecimal;
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

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;

import com.st1.itx.db.domain.NegTrans;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

/**
 * Tita<br>
 * CustId=X,10<br>
 * EntryDateStart=9,7<br>
 * EntryDateEnd=9,7<br>
 */

@Service("L5971")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5971 extends TradeBuffer {
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
		this.info("Run L5971");
		this.info("active L5971 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 120;// 查全部

		String CustId = titaVo.getParam("CustId").trim(); // 身份證號
		String CaseSeq = titaVo.getParam("CaseSeq").trim(); // 案件序號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo").trim()); // 戶號
		String EntryDateStart = titaVo.getParam("EntryDateStart").trim(); // 入帳日期(起始)
		String EntryDateEnd = titaVo.getParam("EntryDateEnd").trim(); // 入帳日期(結束)
		
		int iCaseSeq = 0;
		if (CaseSeq != null && CaseSeq.length() != 0) {
			iCaseSeq = parse.stringToInteger(CaseSeq);
		}

		int IntEntryDateStart = 0;
		if (EntryDateStart != null && EntryDateStart.length() != 0 && parse.stringToInteger(EntryDateStart) != 0) {
			IntEntryDateStart = parse.stringToInteger(EntryDateStart) + 19110000;
		}

		int IntEntryDateEnd = 0;
		if (EntryDateEnd != null && EntryDateEnd.length() != 0 && parse.stringToInteger(EntryDateEnd) != 0) {
			IntEntryDateEnd = parse.stringToInteger(EntryDateEnd) + 19110000;
		}

		NegMainId NegMainId = new NegMainId();
		NegMainId.setCaseSeq(iCaseSeq);
		NegMainId.setCustNo(iCustNo);

		NegMain NegMainVO = new NegMain();
		NegMainVO = sNegMainService.findById(NegMainId, titaVo);
		String NegMainCaseKindCode = "";// 案件種類
		BigDecimal NegMainPrincipalBal = new BigDecimal(0);// 總本金餘額
		BigDecimal NegMainAccuOverAmt = new BigDecimal(0);// 累溢收金額
		BigDecimal NegMainDueAmt = new BigDecimal(0);// 月付金(期款)
		String NegMainCustLoanKind = "";// 債權戶別
		BigDecimal NegMainAccuTempAmt = new BigDecimal(0);// 累暫收金額
		BigDecimal NegMainAccuDueAmt = new BigDecimal(0);// 累期款金額
		int OORemainPeriod = 0;// 尚餘期數=期數TotalPeriod-已繳期數RepaidPeriod
		String NegMainStatus = "";// 狀態
		BigDecimal NegMainAccuSklShareAmt = new BigDecimal(0);// 累新壽分攤金額

		if (NegMainVO != null) {
			NegMainCaseKindCode = NegMainVO.getCaseKindCode();
			NegMainPrincipalBal = NegMainVO.getPrincipalBal();
			NegMainAccuOverAmt = NegMainVO.getAccuOverAmt();
			NegMainDueAmt = NegMainVO.getDueAmt();
			NegMainCustLoanKind = NegMainVO.getCustLoanKind();
			NegMainAccuTempAmt = NegMainVO.getAccuTempAmt();
			NegMainAccuDueAmt = NegMainVO.getAccuDueAmt();
			int TotalPeriod = NegMainVO.getTotalPeriod();// 期數
			int RepaidPeriod = NegMainVO.getRepaidPeriod();// 已繳期數
			OORemainPeriod = TotalPeriod - RepaidPeriod;
			if (OORemainPeriod < 0) {//轉舊資料時有錯誤的已經期數造成期數為負值,調整為0 - 2022/10/28
				OORemainPeriod = 0;
			}
			NegMainStatus = NegMainVO.getStatus();
			NegMainAccuSklShareAmt = NegMainVO.getAccuSklShareAmt();
		}

		
		Slice<NegTrans> slNegTrans = null;
		if (iCustNo != 0) {
			if (IntEntryDateStart != 0 || IntEntryDateEnd != 0) {
				slNegTrans = sNegTransService.custAndentryDateBetween(iCustNo,iCaseSeq, IntEntryDateStart, IntEntryDateEnd, this.index, this.limit, titaVo);
			} else {
				slNegTrans = sNegTransService.custAndCaseSeq(iCustNo,iCaseSeq, this.index, this.limit, titaVo);
			}
		} else {
			if (IntEntryDateStart != 0 && IntEntryDateEnd != 0) {
				slNegTrans = sNegTransService.entryDateBetween(IntEntryDateStart, IntEntryDateEnd, this.index, this.limit, titaVo);
			}
		}
		List<NegTrans> lNegTrans = slNegTrans == null ? null : slNegTrans.getContent();
		if (lNegTrans != null && lNegTrans.size() != 0) {
			int TestFinnd = 0;
			for (NegTrans NegTransVO : lNegTrans) {
				int NegTransCaseSeq = NegTransVO.getCaseSeq();// 案件序號
				int NegTransCustNo = NegTransVO.getCustNo();// 戶號

				if (iCaseSeq != 0) {
					if (NegTransCaseSeq != iCaseSeq) {
						continue;
					}
				}
				OccursList occursList = new OccursList();

				this.info("L5971 " + String.valueOf(NegTransCustNo));
				totaVo.putParam("OCustNo", NegTransCustNo);// 戶號
				if (iCaseSeq != 0) {
					totaVo.putParam("OCaseSeq", NegTransCaseSeq);
				} else {
					totaVo.putParam("OCaseSeq", "");
				}
				totaVo.putParam("OCaseKindCode", NegMainCaseKindCode);// 案件種類
				totaVo.putParam("OMainPrincipalBal", NegMainPrincipalBal);// 總本金餘額
				totaVo.putParam("OAccuOverAmt", NegMainAccuOverAmt);// 累溢收金額
				totaVo.putParam("ODueAmt", NegMainDueAmt);// 月付金(期款)
				totaVo.putParam("OCustLoanKind", NegMainCustLoanKind);// 債權戶別
				totaVo.putParam("OAccuTempAmt", NegMainAccuTempAmt);// 累暫收金額
				totaVo.putParam("OAccuDueAmt", NegMainAccuDueAmt);// 累期款金額
				totaVo.putParam("ORemainPeriod", OORemainPeriod);// 尚餘期數=期數TotalPeriod-已繳期數RepaidPeriod
				totaVo.putParam("OCustStatus", NegMainStatus);// 借戶狀態
				totaVo.putParam("OAccuSklShareAmt", NegMainAccuSklShareAmt);// 累新壽分攤金額

				occursList.putParam("OOCaseSeq", NegTransVO.getCaseSeq());// 交易序號
				occursList.putParam("OOEntryDate", NegTransVO.getEntryDate());// 入帳日期
				occursList.putParam("OOTxKind", NegTransVO.getTxKind());// 交易別
				occursList.putParam("OOTxAmt", NegTransVO.getTxAmt());// 交易金額
				occursList.putParam("OOTransPrincipalBal", NegTransVO.getPrincipalBal());// 本金餘額
				occursList.putParam("OOAcDate", NegTransVO.getAcDate());// 會計日期
				occursList.putParam("OOReturnAmt", NegTransVO.getReturnAmt());// 退還金額
				occursList.putParam("OOSklShareAmt", NegTransVO.getSklShareAmt());// 新壽攤分
				occursList.putParam("OOApprAmt", NegTransVO.getApprAmt());// 撥付金額
				occursList.putParam("OOExportDate", NegTransVO.getExportDate());// 撥付製檔日
				occursList.putParam("OOTitaTlrNo", NegTransVO.getTitaTlrNo());// 經辦
				occursList.putParam("OOTitaTxtNo", NegTransVO.getTitaTxtNo());// 交易序號
				this.totaVo.addOccursList(occursList);
				TestFinnd++;
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slNegTrans != null && slNegTrans.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				// this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}

			if (TestFinnd == 0) {
				throw new LogicException(titaVo, "E0001", "債務協商交易檔");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "債務協商交易檔");
		}
		/*
		 * #OOCustNo #OOCaseKindCode #OOMainPrincipalBal #OOAccuOverAmt #OODueAmt
		 * #OOCustLoanKind #OOAccuTempAmt #OOAccuDueAmt #OORemainPeriod #OOCustStatus
		 * #OOAccuSklShareAmt #OOEntryDate #OOTxKind #OOTxAmt #OOTransPrincipalBal
		 * #OOAcDate #OOReturnAmt #OOSklShareAmt #OOApprAmt #OOExportDate OOTitaTlrNo
		 * OOTitaTxtNo
		 */

		this.addList(this.totaVo);
		return this.sendList();
	}
}