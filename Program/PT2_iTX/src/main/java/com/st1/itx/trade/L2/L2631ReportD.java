package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2631ReportD")
@Scope("prototype")
public class L2631ReportD extends MakeReport {

	@Autowired
	LoanBorMainService loanBorMainService;
	@Autowired
	CdCodeService cdCodeService;
	@Autowired
	CustMainService custMainService;
	@Autowired
	ClFacService clFacService;
	@Autowired
	ClLandService clLandService;
	@Autowired
	ClBuildingService clBuildingService;

	@Autowired
	FacMainService facMainService;
	@Autowired
	FacProdService facProdService;
	@Autowired
	GuarantorService guarantorService;
	@Autowired
	LoanChequeService loanChequeService;
	@Autowired
	DateUtil dateUtil;
	@Autowired
	LoanCom loanCom;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L2631-D";
	private String reportItem = "清償作業";
	private String defaultPdf = "";
	private TempVo t2TempVo = new TempVo();

	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "P";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2631ReportD.printHeader");

		this.print(1, 42, "清償作業", "C");
		this.print(-1, 76, "機密等級：" + this.security, "R");
		this.print(-4, 6, "", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(4);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.setFontSize(8);
		this.print(-83, 2, "SKL-B#DBB94!5");
	}

	public Boolean exec(TitaVo titaVo, TotaVo totaVo) throws LogicException {

		this.info("L2631ReportD exec");

		exportPdf(titaVo, totaVo);

		return true;
	}

	private void exportPdf(TitaVo titaVo, TotaVo totaVo) throws LogicException {

		this.info("exportExcel ... ");

		this.reportDate = Integer.valueOf(titaVo.getParam("TranDate")) + 19110000;
		this.brno = titaVo.getBrno();
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.setFont(1);

		this.setFontSize(12);
		this.setCharSpaces(0);
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		this.open(titaVo, reportVo);
		this.setFontSize(12);
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 入帳日期
		int iEntryDate = parse.stringToInteger(titaVo.getParam("TranDate"));
		// 結案區分
		String iCloseInd = titaVo.getParam("CloseInd");
		// 清償原因
		String iCloseReasonCode = titaVo.getParam("CloseReasonCode");
//		CdCode.AdvanceCloseCode
		// 領取方式
		String iCollectWayCode = titaVo.getParam("CollectWayCode");
//		CdCode.CollectWayCode
		// 連絡電話１
		String iTelNo1 = titaVo.getParam("TelNo1");
		// 連絡電話２
		String iTelNo2 = titaVo.getParam("TelNo2");
		// 連絡電話３
		String iTelNo3 = titaVo.getParam("TelNo3");
		// 備註
		String iRmk1 = titaVo.getParam("Rmk1");
		// 計息期間
		String iIntStartDate = titaVo.getParam("IntStartDate");
		String iIntEndDate = titaVo.getParam("IntEndDate");
		// 建物門牌
		String iBdLocation = titaVo.getParam("BdLocation");
		// 建物標示備註
		String iBdRmk = titaVo.getParam("BdRmk");
		// 限制期間
		String iProhibitperiod = titaVo.getParam("Prohibitperiod");
		// 限制清償期限
		String iProhibitMonthMsg = titaVo.getParam("ProhibitMonthMsg");
		// 本金
		BigDecimal iPrincipal = parse.stringToBigDecimal(titaVo.getParam("Principal"));
		// 利息
		BigDecimal iInterest = parse.stringToBigDecimal(titaVo.getParam("Interest"));
		// 違約金
		BigDecimal iBreachAmt = parse.stringToBigDecimal(titaVo.getParam("BreachAmt"));
		// 清償金額
		BigDecimal iCloseAmt = parse.stringToBigDecimal(titaVo.getParam("CloseAmt"));
		// 清償違約金
		BigDecimal iCloseBreachAmt = parse.stringToBigDecimal(titaVo.getParam("CloseBreachAmt"));
		// 延遲息
		BigDecimal iDelayInt = parse.stringToBigDecimal(titaVo.getParam("DelayInt"));
		// 法拍費
		BigDecimal iLawFee = parse.stringToBigDecimal(titaVo.getParam("LawFee"));
		// 火險費
		BigDecimal iFireFee = parse.stringToBigDecimal(titaVo.getParam("FireFee"));
		// 帳管費
		BigDecimal iAcctFee = parse.stringToBigDecimal(titaVo.getParam("AcctFee"));
		// 契變手續費
		BigDecimal iModifyFee = parse.stringToBigDecimal(titaVo.getParam("ModifyFee"));
		// 累短收
		BigDecimal iShortfall = parse.stringToBigDecimal(titaVo.getParam("Shortfall"));
		// 累短收-清償違約金
		BigDecimal iShortCloseBreach = parse.stringToBigDecimal(titaVo.getParam("ShortCloseBreach"));
		// 累溢收
		BigDecimal iExcessive = parse.stringToBigDecimal(titaVo.getParam("Excessive"));

		print(1, 2, "借款人戶號 ... " + parse.IntegerToString(iCustNo, 7) + " - " + parse.IntegerToString(iFacmNo, 3)); // 戶號
		print(0, 45, "入帳日期 ..... " + this.showDate("" + iEntryDate)); // 入帳日期
		// 戶名
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		String custName = "";
		if (tCustMain != null) {
			custName = tCustMain.getCustName();
		}
		// 結案區分
		CdCode tCdCode1 = cdCodeService.getItemFirst(3, "CaseCloseCode", iCloseInd, titaVo);
		String closeInd = "";
		if (tCdCode1 != null) {
			closeInd = tCdCode1.getItem();
		}
		print(1, 2, "戶名 ......... " + custName); // 戶名
		print(0, 45, "結案區分 ..... " + closeInd); // 結案區分
		FacMain tFacMain = new FacMain();
		BigDecimal lineAmt = BigDecimal.ZERO;
		BigDecimal utilAmt = BigDecimal.ZERO;
		String prodNo = "";

		if (iFacmNo == 0) {
			List<FacMain> lFacMain = new ArrayList<FacMain>();
			Slice<FacMain> slFacMain = facMainService.CustNoAll(iCustNo, 0, Integer.MAX_VALUE, titaVo);
			if (slFacMain == null) {
				throw new LogicException("E2003", "該戶號不存在額度主檔");// 查無資料
			}
			for (FacMain t : slFacMain.getContent()) {
				lineAmt = lineAmt.add(t.getLineAmt());
				utilAmt = utilAmt.add(t.getUtilAmt());
				prodNo = t.getProdNo();
			}
		} else {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException("E2003", "該戶號額度不存在額度主檔");// 查無資料
			}
			lineAmt = lineAmt.add(tFacMain.getLineAmt());
			utilAmt = utilAmt.add(tFacMain.getUtilAmt());
			prodNo = tFacMain.getProdNo();
		}
		print(1, 2, "放款餘額 ..... "); // 放款餘額 額度000顯示最後一筆額度的放款餘額
		print(0, 32, formatAmt(utilAmt, 0), "R");
		print(0, 45, "核准額度 ..... "); // 核准額度 額度000顯示最後一筆額度的核准額度
		print(0, 75, formatAmt(lineAmt, 0), "R");
		FacProd tFacProd = facProdService.findById(prodNo, titaVo);
		if (tFacProd == null) {
			throw new LogicException("E2003", "該額度商品代碼不存在商品主檔");// 查無資料
		}
		print(1, 2, "計息期間 ..... " + this.showDate(iIntStartDate) + " - " + this.showDate(iIntEndDate)); // 計息期間IntStartDate-IntEndDate
		print(0, 45, "商品代碼 ..... " + prodNo + " " + tFacProd.getProdName()); // 商品代碼 額度000顯示最後一筆額度的商品

		print(1, 2, "門牌號碼 ..... " + iBdLocation); // 門牌號碼BdLocation

		print(1, 2, "建物標示備註 . " + iBdRmk); // 建物標示備註BdRmk

		print(1, 2, "本金 ......... "); // 本金PrincipalX
		print(0, 32, formatAmt(iPrincipal, 0), "R");
		print(0, 45, iProhibitMonthMsg); // 限制清償期限

		print(1, 2, "利息 ......... "); // 利息InterestX
		print(0, 32, formatAmt(iInterest, 0), "R");
		print(0, 45, "限制期間　 ... " + this.showDate(iProhibitperiod)); // 限制期間

		print(1, 2, "延遲息 ....... "); // 延遲息DelayIntX
		print(0, 32, formatAmt(iDelayInt, 0), "R");
		print(1, 2, "違約金 ....... "); // 違約金BreachAmtX
		print(0, 32, formatAmt(iBreachAmt, 0), "R");

		print(1, 2, "清償違約金 ... "); // 清償違約金
		print(0, 32, formatAmt(iCloseBreachAmt, 0), "R");

		print(1, 2, "累短收 ....... "); // 累短收 ShortfallX
		print(0, 32, formatAmt(iShortfall, 0), "R");
		print(0, 45, "聯絡電話１ ... " + iTelNo1); // 聯絡電話１

		print(1, 2, "累溢收 ....... "); // 累溢收 ExcessiveX
		print(0, 32, formatAmt(iExcessive, 0), "R");
		print(0, 45, "聯絡電話２ ... " + iTelNo2); // 聯絡電話２
		// 小計
		BigDecimal stotal = iPrincipal.add(iInterest).add(iDelayInt).add(iBreachAmt).add(iCloseBreachAmt)
				.add(iShortfall).subtract(iExcessive);
		print(1, 2, "小計 ......... "); // 小計stotal
		print(0, 32, formatAmt(stotal, 0), "R");
		print(0, 45, "聯絡電話３ ... " + iTelNo3); // 聯絡電話３

		// 清償原因
		CdCode tCdCode2 = cdCodeService.getItemFirst(3, "AdvanceCloseCode", iCloseReasonCode, titaVo);
		String closeReasonCode = "";
		if (tCdCode2 != null) {
			closeReasonCode = tCdCode2.getItem();
		}
		print(1, 45, "清償原因 ..... " + iCloseReasonCode + " " + closeReasonCode); // 清償原因

		// 領取方式
		CdCode tCdCode3 = cdCodeService.getItemFirst(2, "CollectWayCode", iCollectWayCode, titaVo);
		String collectWayCode = "";
		if (tCdCode3 != null) {
			collectWayCode = tCdCode3.getItem();
		}
		print(1, 2, "法拍費用 ..... "); // 法拍費用LawFeeX
		print(0, 32, formatAmt(iLawFee, 0), "R");
		print(0, 45, "領取方式 ..... " + iCollectWayCode + " " + collectWayCode); // 領取方式

		print(1, 2, "借支火險費用 . "); // 借支火險費用 FireFeeX
		print(0, 32, formatAmt(iFireFee, 0), "R");
		print(0, 45, "火險月份 ..... "); // 火險月份

		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

		CustMain tCustMain2 = new CustMain();
		FacMain tFacMain2 = new FacMain();

		tCustMain2 = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain2 == null) {
			throw new LogicException("E2003", "該戶號不存在客戶主檔");
		}
		Slice<Guarantor> slGuarantor = guarantorService.guaUKeyEq(tCustMain2.getCustUKey(), 0, Integer.MAX_VALUE,
				titaVo);
		String isGuarantor = "N";
		if (slGuarantor != null) {
			isGuarantor = "Y";
		}
		print(1, 2, "帳管費用 ..... "); // 帳管費用 AcctFeeX
		print(0, 32, formatAmt(iAcctFee, 0), "R");
		print(0, 45, "是否保證 ..... " + isGuarantor); // 是否保證

		List<String> lStatusCode = new ArrayList<String>();
		lStatusCode.add("0");
		Slice<LoanCheque> slLoanCheque = loanChequeService.chequeCustNoEq(iCustNo, lStatusCode, 0, 99991231, 0,
				Integer.MAX_VALUE, titaVo);
		String isLoanCheque = "N";
		if (slLoanCheque != null) {
			isLoanCheque = "Y";
		}
		print(1, 2, "契變手續費 ... "); // 契變手續費 iModifyFeeX
		print(0, 32, formatAmt(iModifyFee, 0), "R");
		print(0, 45, "暫收支票 ..... " + isLoanCheque); // 暫收支票

		// 總計
		BigDecimal total = stotal.add(iLawFee).add(iFireFee).add(iAcctFee).add(iModifyFee);
		print(1, 2, "總計 ......... "); // 總計total
		print(0, 32, formatAmt(total, 0), "R");
		print(0, 45, "備註欄 ....... " + iRmk1); // 備註欄iRmk1
		print(1, 1, "　　　　　　           ");

		// 備註
		// 被保人 最高保證金額 目前保證總額 金額 戶況 繳息迄日
		// L2R64

		print(1, 1, "　被保人ID　　　最高保證金額　　目前保證總額　　戶名　　　　　　　　戶況　　繳息迄日");
		print(1, 1,
				"===================================================================================================");
		BigDecimal GuaAmt = BigDecimal.ZERO;
		if (slGuarantor != null) {
			for (Guarantor tGuarantor : slGuarantor.getContent()) {
				// new occurs
				OccursList occurslist = new OccursList();
				// new Table
				tCustMain2 = new CustMain();
				tFacMain2 = new FacMain();

				lLoanBorMain = new ArrayList<LoanBorMain>();
				GuaAmt = BigDecimal.ZERO;
				int[] test = { 0, 0 };

				// 取額度號碼
				tFacMain2 = facMainService.facmApplNoFirst(tGuarantor.getApproveNo(), titaVo);
				if (tFacMain2 == null) {
					tFacMain2 = new FacMain();
				}
				int custNo = tFacMain2.getCustNo();
				// 取戶號,戶名
				tCustMain2 = custMainService.custNoFirst(custNo, custNo, titaVo);
				if (tCustMain2 == null) {
					tCustMain2 = new CustMain();
				}
				// 取戶況,繳息迄日
				Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(tFacMain2.getCustNo(),
						tFacMain2.getFacmNo(), tFacMain2.getFacmNo(), 0, 900, 0, Integer.MAX_VALUE, titaVo);

				lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

				// 戶況
				String status = "";
				if (lLoanBorMain != null && !lLoanBorMain.isEmpty()) {
					test = judgeStatus(lLoanBorMain);
					CdCode tCdCode4 = cdCodeService.getItemFirst(3, "Status", parse.IntegerToString(test[0], 2),
							titaVo);
					if (tCdCode4 != null) {
						status = tCdCode4.getItem();
					}
				} else {
					status = "尚未撥款";
				}
				if (lLoanBorMain != null) {
					for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
						GuaAmt = GuaAmt.add(tmpLoanBorMain.getLoanBal());
					}
				} else {
					GuaAmt = BigDecimal.ZERO;
				}
				print(1, 1, "　　　　　　           ");
				print(0, 2, tCustMain2.getCustId());// 被保人ID
				print(0, 27, formatAmt(tGuarantor.getGuaAmt(), 0), "R");// 最高保證金額
				print(0, 42, formatAmt(GuaAmt, 0), "R");// 目前保證總額
				print(0, 44, tCustMain2.getCustName());// 戶名
				print(0, 65, status, "C");// 戶況
				print(0, 75, test[1] == 0 ? "無" : this.showDate("" + test[1]), "C");// 繳息迄日
			}
		} else {
			print(1, 1, "無資料");
		}

		print(1, 1, " ");
		print(1, 1, "額度資料區");
		print(1, 1, "　額度編號　　　核准額度　　門牌號碼");
		print(1, 1,
				"===================================================================================================");
		if (iFacmNo == 0) {

			List<FacMain> lFacMain = new ArrayList<FacMain>();
			Slice<FacMain> slFacMain = facMainService.CustNoAll(iCustNo, 0, Integer.MAX_VALUE, titaVo);
			if (slFacMain == null) {
				print(1, 1, "無資料");
			} else {
				for (FacMain t : slFacMain.getContent()) {
					setTotaA(t, titaVo);
				}
			}

		} else {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain != null) {
				setTotaA(tFacMain, titaVo);
			} else {
				print(1, 1, "無資料");
			}
		}

//		for (int i = 1; i <= 400; i++) {
//			if ((i % 10) == 0) {
//				this.print(-2, i, "" + (i / 10));
//			}
//			this.print(-1, i, "" + (i % 10));
//			this.print(-i, 1, "" + (i % 10));
//		}

		long rptNo = this.close();
		totaVo.put("PdfSnoF", Long.toString(rptNo));

	}

	/**
	 * 根據該額度底下所有撥款戶戶況對應的優先度判斷tota應放的戶況及繳息迄日<BR>
	 * 優先度 : 戶況 : 中文<BR>
	 * 1 : 6 : 呆帳戶<BR>
	 * 2 : 7 : 部分轉呆戶<BR>
	 * 3 : 2 : 催收戶<BR>
	 * 4 : 4 : 逾期戶<BR>
	 * 5 : 0 : 正常戶<BR>
	 * 6 : 5 : 催收結案戶<BR>
	 * 7 : 8 : 債權轉讓戶<BR>
	 * 8 : 9 : 呆帳結案戶<BR>
	 * 9 : 3 : 結案戶<BR>
	 * 10 : 97 : 預約撥款已刪除<BR>
	 * 10 : 98 : 預約已撥款<BR>
	 * 10 : 99 : 預約撥款<BR>
	 * 
	 * @param lLoanBorMain 撥款資料
	 * @return [0]=戶況<BR>
	 *         [1]=繳息迄日
	 * @throws LogicException LogicException
	 */
	public int[] judgeStatus(List<LoanBorMain> lLoanBorMain) throws LogicException {
		int priorty = 10;
		int status = 0;
		int tbsdy = this.getTxBuffer().getTxCom().getTbsdy();
		int loandate = 0;
		int[] result = new int[2];

		for (LoanBorMain tmpLoanBorMain : lLoanBorMain) {
			if (tmpLoanBorMain.getStatus() < 90) {
//						if (tmpLoanBorMain.getStatus() > 1 && tmpLoanBorMain.getStatus() < 90) {
				int thisStatus = tmpLoanBorMain.getStatus();
				int thisPriorty;
				switch (thisStatus) {
				case 6:
					thisPriorty = 1;
					break;
				case 7:
					thisPriorty = 2;
					break;
				case 2:
					thisPriorty = 3;
					break;
				case 4:
					thisPriorty = 4;
					break;
				case 0:
					thisPriorty = 5;
					break;
				case 5:
					thisPriorty = 6;
					break;
				case 8:
					thisPriorty = 7;
					break;
				case 9:
					thisPriorty = 8;
					break;
				case 3:
					thisPriorty = 9;
					break;
				default:
					thisPriorty = 10;
					break;
				}

				if (thisPriorty < priorty) {
					status = thisStatus;
					if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
						loandate = tmpLoanBorMain.getDrawdownDate();
					} else {
						loandate = tmpLoanBorMain.getPrevPayIntDate();
					}
					priorty = thisPriorty;
					// 判斷是否為逾期戶
					if (status == 0 && tmpLoanBorMain.getNextPayIntDate() < tbsdy) {
						dateUtil.init();
						dateUtil.setDate_1(tbsdy);
						dateUtil.setMons(-1);
						int payDate = dateUtil.getCalenderDay();
						if (tmpLoanBorMain.getNextPayIntDate() < payDate) { // 逾期超過一個月
							status = 4;
							priorty = 4;
							if (tmpLoanBorMain.getPrevPayIntDate() == 0) {
								loandate = tmpLoanBorMain.getDrawdownDate();
							} else {
								loandate = tmpLoanBorMain.getPrevPayIntDate();
							}
						}
					}
				}
			} // if
		} // for

		result[0] = status;
		result[1] = loandate;

		return result;
	}

	// totaA 額度資料
	private void setTotaA(FacMain tFacMain, TitaVo titaVo) throws LogicException {

		// 額度 核准額度 門牌號碼
		int cfFacmNo = tFacMain.getFacmNo();
		BigDecimal cfLineAmt = tFacMain.getLineAmt();
		String cfLocation = "";

		// 門牌號碼、土地建號

		// ClFac擔保品與額度關聯檔

		ClFac tClFac = clFacService.mainClNoFirst(tFacMain.getCustNo(), tFacMain.getFacmNo(), "Y", titaVo);

		if (tClFac != null) {
			// 房地
			if (tClFac.getClCode1() == 1) {
				// ClBuilding 擔保品建物檔
				ClBuilding tClBuilding = clBuildingService
						.findById(new ClBuildingId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo()), titaVo);
				if (tClBuilding != null) {
					cfLocation = tClBuilding.getBdLocation();
				}
			}
			if (tClFac.getClCode1() == 2) {
				// ClBuilding 擔保品建物檔
				ClLand tClLand = clLandService
						.findById(new ClLandId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(), 0), titaVo);
				if (tClLand != null) {
					cfLocation = tClLand.getLandLocation();
				}
			}

		}
		print(1, 1, "　　　　　　           ");
		print(0, 5, parse.IntegerToString(cfFacmNo, 3));// 額度編號
		print(0, 23, formatAmt(cfLineAmt, 0), "R");// 核准額度
		print(0, 29, cfLocation);// 門牌號碼
	}
}
