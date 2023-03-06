package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L4455ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4455Report")
@Scope("prototype")

public class L4455Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public L4455ServiceImpl sL4455ServiceImpl;

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;

	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	// 自訂表頭

	private String irepaybank = "";

	private int acdate = 0;
	private String year = "";
	private String month = "";
	private String date = "";
	private String batchno = "";
	private String entrydate = "";
	private String repaybank = "";
	private String bank = "";
	private String acctcode = "";
	private String acctcodex = "";
	private String tCustName = "";
	private String repaybankX = "";

	// 合計

	private BigDecimal totRepayAmt = new BigDecimal("0");
	private BigDecimal totAcctAmt = new BigDecimal("0");
	private BigDecimal totPrincipal = new BigDecimal("0");
	private BigDecimal totInterest = new BigDecimal("0");
	private BigDecimal totTempPayAmt = new BigDecimal("0");
	private BigDecimal totBreachAmt = new BigDecimal("0");
	private BigDecimal totTempDr = new BigDecimal("0");
	private BigDecimal totTempCr = new BigDecimal("0");
	private BigDecimal totShortfall = new BigDecimal("0");
	// 業務科目 合計

	private BigDecimal RepayAmt = new BigDecimal("0");
	private BigDecimal AcctAmt = new BigDecimal("0");
	private BigDecimal Principal = new BigDecimal("0");
	private BigDecimal Interest = new BigDecimal("0");
	private BigDecimal TempPayAmt = new BigDecimal("0");
	private BigDecimal BreachAmt = new BigDecimal("0");
	private BigDecimal TempDr = new BigDecimal("0");
	private BigDecimal TempCr = new BigDecimal("0");
	private BigDecimal Shortfall = new BigDecimal("0");

	private List<CdCode> lCdCode = null;
	private List<CdCode> lCdCode2 = null;
	// 每頁筆數
	private int pageIndex = 38;
	private int funcd = 0;

	private int dataSize = 0;

	private String tmpBank = "";
	private String tmpBankX = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void printHeaderP() {

		this.setFont(1, 9);
		this.print(-1, 185, "機密等級：密");
		this.print(-2, 1, "程式ID：" + "L4455Report");
		this.print(-2, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 203, "日　期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 1, "報　表：" + "L4455Report");

		String feeItem = "";
		switch (funcd) {
		case 2:
			feeItem = "（帳管費）";
			break;
		case 3:
			feeItem = "（契變手續費）";
			break;
		case 4:
			feeItem = "（火險費）";
			break;
		case 5:
			feeItem = "（法務費）";
			break;
		default:
			feeItem = "";
			break;
		}

		if ("999".equals(irepaybank)) {
			this.print(-3, this.getMidXAxis(), "銀行扣款總傳票明細表" + feeItem, "C");
		} else {
			this.print(-3, this.getMidXAxis(), "ACH 扣款總傳票明細表" + feeItem, "C");
		}

		this.print(-3, 203, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 185, "頁　數：");
		this.print(-4, 200, "" + this.getNowPage(), "R");
		this.print(-5, 3, "批次號碼：" + batchno);

		if (String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}

		this.print(-5, this.getMidXAxis(), year + " 年 " + month + " 月 " + date + " 日", "C");

		this.print(-5, 185, "單　位：元");
		if (String.valueOf(entrydate).length() == 7) {
			this.print(-6, 3, "扣款日期：" + entrydate.substring(0, 3) + "/" + entrydate.substring(3, 5) + "/"
					+ entrydate.substring(5, 7));
		} else if (String.valueOf(entrydate).length() == 6) {
			this.print(-6, 3, "扣款日期：" + entrydate.substring(0, 2) + "/" + entrydate.substring(2, 4) + "/"
					+ entrydate.substring(4, 6));
		} else {
			this.print(-6, 3, "扣款日期：");
		}

		this.info("RepayBank = " + titaVo.get("RepayBank"));

		// 排除998 ACH
		if (!"998".equals(titaVo.get("RepayBank"))) {
			for (CdCode tCdCode : lCdCode) {
				if (titaVo.get("RepayBank").equals(tCdCode.getCode())) {
					bank = tCdCode.getItem();
					repaybank = titaVo.get("RepayBank").trim();
					repaybankX = titaVo.get("RepayBankX").trim();
					this.info("RepayBankX = " + repaybankX);
				}
			}
		}

		if (dataSize == 0) {
			repaybank = titaVo.get("RepayBank") + ' ' + titaVo.get("RepayBankX");
			bank = "";
		}

		// 998 ACH個別處理
		if ("998".equals(titaVo.get("RepayBank"))) {
			if (dataSize == 0) {
				this.print(-6, 35, "扣款銀行：" + titaVo.get("RepayBank") + " " + titaVo.get("RepayBankX"));
			} else {
				this.print(-6, 35, "扣款銀行：" + tmpBank + " " + tmpBankX);
			}
		} else {
			this.print(-6, 35, "扣款銀行：" + bank + " " + repaybank);
		}

		this.print(-8, 1,
				"戶號              戶名           扣款金額    作帳金額        計息起迄日                本金          利息        暫付款        違約金        暫收借        暫收貸          短繳        ");
		this.print(-9, 1,
				"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4455Report exec");

		acdate = parse.stringToInteger(titaVo.getParam("AcDate"));
		entrydate = titaVo.getParam("EntryDate");
		irepaybank = titaVo.getParam("RepayBank");

		List<Map<String, String>> L4455List = new ArrayList<Map<String, String>>();

		this.info("L4455Report All");
		funcd = 1;
		try {
			L4455List = sL4455ServiceImpl.findAll(titaVo, funcd);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		execReport(titaVo, L4455List, funcd);

		L4455List = new ArrayList<Map<String, String>>();
		this.info("L4455Report AcctFee + LawFee");
		funcd = 2;
		try {
			L4455List = sL4455ServiceImpl.findAll(titaVo, funcd);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		execReport(titaVo, L4455List, funcd);

		this.info("L4455Report ModifyFee");
		funcd = 3;
		try {
			L4455List = sL4455ServiceImpl.findAll(titaVo, funcd);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		execReport(titaVo, L4455List, funcd);

		this.info("L4455Report FireFee");
		funcd = 4;
		try {
			L4455List = sL4455ServiceImpl.findAll(titaVo, funcd);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		execReport(titaVo, L4455List, funcd);

		this.info("L4455Report Collist");
		funcd = 5;
		try {
			L4455List = sL4455ServiceImpl.findAll(titaVo, funcd);
		} catch (Exception e) {
			this.error("L4455ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L4455");
		}
		execReport(titaVo, L4455List, funcd);
	}

	private void execReport(TitaVo titaVo, List<Map<String, String>> L4455List, int function) throws LogicException {

		// 是否有資料
		dataSize = L4455List.size() > 0 && !L4455List.isEmpty() ? L4455List.size() : 0;

		String tradeReportName = "";
		switch (function) {
		case 1:
			tradeReportName = "銀行扣款總傳票明細表";
			break;
		case 2:
			tradeReportName = "銀行扣款總傳票明細表(帳管費)";
			break;
		case 3:
			tradeReportName = "銀行扣款總傳票明細表(契變手續費)";
			break;
		case 4:
			tradeReportName = "銀行扣款總傳票明細表(火險費)";
			break;
		case 5:
			tradeReportName = "銀行扣款總傳票明細表(法務費)";
			break;
		default:
			break;
		}

		this.info("L4455List = " + L4455List.toString() + ",size = " + L4455List.size());

		// 找銀行代碼
		Slice<CdCode> slCdCode = sCdCodeDefService.defItemEq("BankDeductCd", "%", this.index, this.limit, titaVo);
		lCdCode = slCdCode == null ? null : slCdCode.getContent();

		// 找科目代碼
		Slice<CdCode> slCdCode2 = sCdCodeDefService.defItemEq("AcctCode", "%", this.index, this.limit, titaVo);
		lCdCode2 = slCdCode2 == null ? null : slCdCode2.getContent();

		if (titaVo.getParam("BatchNo") != null) {
			this.info("BatchNo   = " + titaVo.getParam("BatchNo"));
		}

		if (L4455List.size() == 0) {
			batchno = "BATX" + titaVo.getParam("BatchNo");
			entrydate = String.valueOf(parse.stringToInteger(titaVo.getParam("EntryDate")));
			repaybank = titaVo.getParam("RepayBank");

		}

		if (L4455List.size() > 0) {
			int i = 0, pageCnt = 0;

			if (!batchno.equals(L4455List.get(i).get("BatchNo"))) {
				batchno = L4455List.get(i).get("BatchNo");
			}

			if (!entrydate
					.equals(String.valueOf(parse.stringToInteger(L4455List.get(i).get("EntryDate")) - 19110000))) {
				entrydate = String.valueOf(parse.stringToInteger(L4455List.get(i).get("EntryDate")) - 19110000);
			}
			if ("998".equals(titaVo.get("RepayBank"))) {
				tmpBank = L4455List.get(0).get("RepayBank");
				for (CdCode tCdCode : lCdCode) {
					if (tmpBank.equals(tCdCode.getCode())) {
						tmpBankX = tCdCode.getItem();

					}
				}
			} else {
				if (!repaybank.equals(L4455List.get(i).get("RepayBank"))) {
					repaybank = L4455List.get(i).get("RepayBank");
					for (CdCode tCdCode : lCdCode) {
						if (repaybank.equals(tCdCode.getCode())) {
							bank = tCdCode.getItem();

						}
					}

					if (this.getNowPage() > 1) {
						this.info("getNowPage  = " + this.getNowPage());
						this.info("NowRow  = " + this.NowRow);
						this.info("newPage  = " + L4455List.get(i).get("RepayBank"));
						this.newPage();
					}

				}
			}

			if (!acctcode.equals(L4455List.get(i).get("AcctCode"))) {
				acctcode = L4455List.get(i).get("AcctCode");
			}

			// 設定報表格式
			ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
					.setRptCode("L4455").setRptItem(tradeReportName).setSecurity("").setRptSize("A4")
					.setPageOrientation("L").build();
			// 開啟報表
			this.open(titaVo, reportVo);

			// 計數用
			int tmpCount = 0;

			for (int j = 1; j <= L4455List.size(); j++) {
				tmpCount++;
				i = j - 1;

				// 給 998 ACH扣款 判斷加的
				if ("998".equals(titaVo.get("RepayBank"))) {

					if (!tmpBank.equals(L4455List.get(i).get("RepayBank"))) {
						tmpBank = L4455List.get(i).get("RepayBank");
						for (CdCode tCdCode : lCdCode) {
							if (tmpBank.equals(tCdCode.getCode())) {
								tmpBankX = tCdCode.getItem();
							}
						}

						// 第一頁
//						if (tmpCount == 1) {
//							this.info("first print header");
//							this.print(-6, 35, "扣款銀行：" + tmpBank + " " + tmpBankX);
//						}

						if (this.getNowPage() > 1) {
							this.info("getNowPage  = " + this.getNowPage());
							this.info("NowRow  = " + this.NowRow);
							this.info("newPage  = " + tmpBank + "  " + tmpBankX);
							this.newPage();

//							this.print(-6, 35, "扣款銀行：" + bank + " " + repaybank);

						}
					}

					this.info(tmpCount + " repaybank  = " + tmpBank);
					this.info(tmpCount + " bank  = " + tmpBankX);
				}

//				this.info(tmpCount + " repaybank  = " + repaybank);
//				this.info(tmpCount + " bank  = " + bank);

//				每頁筆數相加
				pageCnt++;

				DecimalFormat df1 = new DecimalFormat("#,##0");

//				1.每筆先印出明細
				this.print(1, 1, " ");
				this.print(0, 1, L4455List.get(i).get("CustNo"));// 戶號

				if (!tCustName.equals(fillUpWord(L4455List.get(i).get("CustName"), 20, " ", "R"))) {
					this.print(0, 19, fillUpWord(L4455List.get(i).get("CustName"), 20, " ", "R"));// 戶名
				}

				tCustName = fillUpWord(L4455List.get(i).get("CustName"), 20, " ", "R");

				if (parse.stringToInteger(L4455List.get(i).get("TxSeq")) == 1) {
					if (parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt"))
							.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
						this.print(0, 47, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt"))), "R");// 扣款金額
					}
				}
				if (parse.stringToBigDecimal(L4455List.get(i).get("AcctAmt")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 61, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("AcctAmt"))), "R");// 作帳金額
				}

				if (parse.stringToInteger(L4455List.get(i).get("IntStartDate")) != 0) {
					String IntStartDate = String
							.valueOf(parse.stringToInteger(L4455List.get(i).get("IntStartDate")) - 19110000);

					if (String.valueOf(IntStartDate).length() == 7) {
						this.print(0, 63, IntStartDate.substring(0, 3) + "/" + IntStartDate.substring(3, 5) + "/"
								+ IntStartDate.substring(5, 7));// 計息起訖日
					} else {
						this.print(0, 63, IntStartDate.substring(0, 2) + "/" + IntStartDate.substring(2, 4) + "/"
								+ IntStartDate.substring(4, 6));// 計息起訖日
					}

				}

				this.print(0, 73, "-");// 計息起訖日

				if (parse.stringToInteger(L4455List.get(i).get("IntEndDate")) != 0) {

					String IntEndDate = String
							.valueOf(parse.stringToInteger(L4455List.get(i).get("IntEndDate")) - 19110000);

					if (String.valueOf(IntEndDate).length() == 7) {
						this.print(0, 75, IntEndDate.substring(0, 3) + "/" + IntEndDate.substring(3, 5) + "/"
								+ IntEndDate.substring(5, 7));// 計息起訖日
					} else {
						this.print(0, 75, IntEndDate.substring(0, 2) + "/" + IntEndDate.substring(2, 4) + "/"
								+ IntEndDate.substring(4, 6));// 計息起訖日
					}

				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("Principal")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 103, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("Principal"))), "R");// 本金
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("Interest")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 119, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("Interest"))), "R");// 利息
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("TempPayAmt")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 134, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("TempPayAmt"))), "R");// 暫付款
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("BreachAmt")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 150, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("BreachAmt"))), "R");// 違約金
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("TempDr")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 167, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("TempDr"))), "R");// 暫收借
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("TempCr")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 183, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("TempCr"))), "R");// 暫收貸
				}

				if (parse.stringToBigDecimal(L4455List.get(i).get("Shortfall")).compareTo(new BigDecimal("0")) != 0) { // 0不顯示
					this.print(0, 198, df1.format(parse.stringToBigDecimal(L4455List.get(i).get("Shortfall"))), "R");// 短繳
				}

				if (parse.stringToInteger(L4455List.get(i).get("TxSeq")) == 1) {
					RepayAmt = RepayAmt.add(parse.stringToBigDecimal(L4455List.get(i).get("RepayAmt")));
				}
				AcctAmt = AcctAmt.add(parse.stringToBigDecimal(L4455List.get(i).get("AcctAmt")));
				Principal = Principal.add(parse.stringToBigDecimal(L4455List.get(i).get("Principal")));
				Interest = Interest.add(parse.stringToBigDecimal(L4455List.get(i).get("Interest")));
				TempPayAmt = TempPayAmt.add(parse.stringToBigDecimal(L4455List.get(i).get("TempPayAmt")));
				BreachAmt = BreachAmt.add(parse.stringToBigDecimal(L4455List.get(i).get("BreachAmt")));
				TempDr = TempDr.add(parse.stringToBigDecimal(L4455List.get(i).get("TempDr")));
				TempCr = TempCr.add(parse.stringToBigDecimal(L4455List.get(i).get("TempCr")));
				Shortfall = Shortfall.add(parse.stringToBigDecimal(L4455List.get(i).get("Shortfall")));

				if (j != L4455List.size()) {

//					批次號碼/扣款日期/扣款銀行 不同則跳頁，並且累計歸零
					batchno = L4455List.get(j).get("BatchNo");
					entrydate = String.valueOf(parse.stringToInteger(L4455List.get(j).get("EntryDate")) - 19110000);
					repaybank = L4455List.get(j).get("RepayBank");
					acctcode = L4455List.get(j).get("AcctCode");

					for (CdCode tCdCode : lCdCode) {
						if (repaybank.equals(tCdCode.getCode())) {
							bank = tCdCode.getItem();

						}
					}
					this.info("repaybank new  = " + repaybank);

					this.info("bank  = " + bank);

					if (!L4455List.get(i).get("BatchNo").equals(batchno)
							|| !String.valueOf(parse.stringToInteger(L4455List.get(i).get("EntryDate")) - 19110000)
									.equals(entrydate)
							|| !L4455List.get(i).get("RepayBank").equals(repaybank)) {
						this.info("Not Match...");
						// 換頁科目合計
						this.print(1, 1,
								"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 1,
								"                                                                                                                                                                               ");
						String tmpAcctcodex = L4455List.get(i).get("AcctCode");
						for (CdCode tCdCode : lCdCode2) {
							if (tmpAcctcodex.equals(tCdCode.getCode())) {
								acctcodex = tCdCode.getItem();
							}
						}
						if (funcd != 1) {
							acctcodex = "暫收款";
						}

						pageCnt = pageCnt + 2;
						this.print(0, 1, acctcodex);
						this.print(0, 19, "小計");

						amt();

						if (pageIndex - pageCnt - 2 <= 0) {
							this.print(1, this.getMidXAxis(), "=====續下頁=====", "C");
						} else {
							this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
						}
						pageCnt = 0;
						this.newPage();

						amttototal(); // 業務科目金額to總和
						init(); // 業務科目金額歸0

						continue;

					}
					// 銀行扣款總傳票明細表(火險費)
					if (funcd != 4) {
						if (!L4455List.get(i).get("AcctCode").equals(acctcode)) { // 科目不同 科目合計
							this.print(1, 1,
									"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							this.print(1, 1,
									"                                                                                                                                                                               ");
							for (CdCode tCdCode : lCdCode2) {
								if (L4455List.get(i).get("AcctCode").equals(tCdCode.getCode())) {
									acctcodex = tCdCode.getItem();
								}
							}
							if (funcd != 1) {
								acctcodex = "暫收款";
							}
							this.print(0, 1, acctcodex);
							this.print(0, 19, "小計");

							amt();

							this.print(2, 1,
									"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							pageCnt = pageCnt + 4;

							amttototal(); // 業務科目金額to總和
							init(); // 業務科目金額歸0

						}
					}
//					每頁第38筆 跳頁 
					if (pageCnt >= 34) {
						this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
//
						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
//					扣除總計合計的行數 +1 
					this.print(1, 1,
							"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1,
							"                                                                                                                                                                               ");
					for (CdCode tCdCode : lCdCode2) {
						if (acctcode.equals(tCdCode.getCode())) {
							acctcodex = tCdCode.getItem();
						}
					}
					if (funcd != 1) {
						acctcodex = "暫收款";
					}

					this.print(0, 1, acctcodex);
					this.print(0, 19, "小計");

					amt();

					amttototal(); // 業務科目金額to總和
					init(); // 業務科目金額歸0

					this.print(2, 1,
							"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1, "合計");

					totalamt();
					inittotal(); // 合計歸0
					pageCnt = pageCnt + 4;
					if (pageIndex - pageCnt - 2 <= 0) {
						this.print(1, this.getMidXAxis(), "=====報表結束=====", "C");
					} else {
						this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====報表結束=====", "C");
					}

					this.print(2, this.getMidXAxis(), "　　　　　　　　　　　　　　　　　　　　課長：　　　　　　　　　　製表人：", "C");
				}

			} // for

		} else {

			this.print(1, this.getMidXAxis(), "*******    查無資料    ******");
		}

		long sno = this.close();

		this.toPdf(sno);
	}

	private void init() {
		RepayAmt = new BigDecimal("0");
		AcctAmt = new BigDecimal("0");
		Principal = new BigDecimal("0");
		Interest = new BigDecimal("0");
		TempPayAmt = new BigDecimal("0");
		BreachAmt = new BigDecimal("0");
		TempDr = new BigDecimal("0");
		TempCr = new BigDecimal("0");
		Shortfall = new BigDecimal("0");
	}

	private void amt() {

		DecimalFormat df1 = new DecimalFormat("#,##0");

		if (RepayAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 47, df1.format(RepayAmt), "R");// 扣款金額
		}

		if (AcctAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 61, df1.format(AcctAmt), "R");// 作帳金額
		}

		if (Principal.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 103, df1.format(Principal), "R");// 本金
		}

		if (Interest.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 119, df1.format(Interest), "R");// 利息
		}

		if (TempPayAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 134, df1.format(TempPayAmt), "R");// 暫付款
		}

		if (BreachAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 150, df1.format(BreachAmt), "R");// 違約金
		}

		if (TempDr.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 167, df1.format(TempDr), "R");// 暫收借
		}

		if (TempCr.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 183, df1.format(TempCr), "R");// 暫收貸
		}

		if (Shortfall.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 198, df1.format(Shortfall), "R");// 短繳
		}

	}

	private void totalamt() {

		DecimalFormat df1 = new DecimalFormat("#,##0");

		if (totRepayAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 47, df1.format(totRepayAmt), "R");// 扣款金額
		}

		if (totAcctAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 61, df1.format(totAcctAmt), "R");// 作帳金額
		}

		if (totPrincipal.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 103, df1.format(totPrincipal), "R");// 本金
		}

		if (totInterest.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 119, df1.format(totInterest), "R");// 利息
		}

		if (totTempPayAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 134, df1.format(totTempPayAmt), "R");// 暫付款
		}

		if (totBreachAmt.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 150, df1.format(totBreachAmt), "R");// 違約金
		}

		if (totTempDr.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 167, df1.format(totTempDr), "R");// 暫收借
		}

		if (totTempCr.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 183, df1.format(totTempCr), "R");// 暫收貸
		}

		if (totShortfall.compareTo(new BigDecimal("0")) != 0) { // 0不顯示
			this.print(0, 198, df1.format(totShortfall), "R");// 短繳
		}

	}

	private void inittotal() {
		totRepayAmt = new BigDecimal("0");
		totAcctAmt = new BigDecimal("0");
		totPrincipal = new BigDecimal("0");
		totInterest = new BigDecimal("0");
		totTempPayAmt = new BigDecimal("0");
		totBreachAmt = new BigDecimal("0");
		totTempDr = new BigDecimal("0");
		totTempCr = new BigDecimal("0");
		totShortfall = new BigDecimal("0");
	}

	private void amttototal() {
		totRepayAmt = totRepayAmt.add(RepayAmt);
		totAcctAmt = totAcctAmt.add(AcctAmt);
		totPrincipal = totPrincipal.add(Principal);
		totInterest = totInterest.add(Interest);
		totTempPayAmt = totTempPayAmt.add(TempPayAmt);
		totBreachAmt = totBreachAmt.add(BreachAmt);
		totTempDr = totTempDr.add(TempDr);
		totTempCr = totTempCr.add(TempCr);
		totShortfall = totShortfall.add(Shortfall);
	}

}