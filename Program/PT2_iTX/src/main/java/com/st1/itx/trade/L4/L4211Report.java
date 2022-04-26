//戶名 58
//清償原因 69
//起日 72
//橫槓 81
//迄日 82
//利息 108,R	allsum4
//暫付款 119,R	allsum5
//違約金 130,R	allsum6
//暫收貸 154,R	allsum7
//短腳 162,R	allsum9
//費用 173,R	allsum10
//本金 97,R	allsum3
//暫收借 144,R	allsum8

package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4211AServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4211Report")
@Scope("prototype")
public class L4211Report extends MakeReport {
//    @Autowired
//    private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public L4211AServiceImpl l4211ARServiceImpl;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	@Autowired
	SortMapListCom sortMapListCom;

	// 每頁筆數
	private int pageIndex = 38;
	// 每頁筆數
	private int reportkind = 0;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		if (reportkind == 1) {
			printHeaderP();
		} else if (reportkind == 2) {
			printHeaderP1();
		} else {
			printHeaderP2();
		}
		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	int allsumTransferAmt = 0;
	int allsumMakeferAmt = 0;
	int allsumPrincipal = 0;
	int allsumInterest = 0;
	int allsumPayment = 0;
	int allsumDamages = 0;
	int allsumTemporaryLoan = 0;
	int allsumCollection = 0;
	int allsumShortPayment = 0;
	int allsumOthers = 0;

	int totalsumTransferAmt = 0;
	int totalsumMakerferAmt = 0;
	int totalsumPrincipal = 0;
	int totalsumInterest = 0;
	int totalsumPayment = 0;
	int totalsumDamages = 0;
	int totalsumTemporaryLoan = 0;
	int totalsumCollection = 0;
	int totalsumShortPayment = 0;
	int totalsumOthers = 0;

	int transferamt = 0;
	int makeferamt = 0;
	int principal = 0;
	int interest = 0;
	int payment = 0;
	int damages = 0;
	int temporaryloan = 0;
	int collection = 0;
	int shortpayment = 0;
	int others = 0;

	String acdate = "";
	String year = "";
	String month = "";
	String date = "";
	
	String txCode = "";
	String reportName = "";

	public void printHeaderP() {
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + txCode);
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//            月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + txCode);
		this.print(-3, this.getMidXAxis() - 1 , reportName, "R");
		this.print(-3, this.getMidXAxis() + 1 , " ---- (     )", "L");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");

		if (String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}

		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);

		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		/**
		 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8---------9
		 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-7, 1,
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	   戶名    	          計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 費用");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 1234567-001-001 11 我是一二三 11 111/11/11-111/11/11
	}

	public void printHeaderP1() {
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + txCode);
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//            月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + txCode);
		this.print(-3, this.getMidXAxis() - 1 , reportName + " - 以金額排序", "R");
		this.print(-3, this.getMidXAxis() + 1 , " ---- (     )", "L");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");

		if (String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}

		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);

		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		this.print(-7, 1,
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	   戶名    	          計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 費用");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void printHeaderP2() {
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + txCode);
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//            月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + txCode);
		this.print(-3, this.getMidXAxis() - 1 , reportName + " - 依戶號", "R");
		this.print(-3, this.getMidXAxis() + 1 , " ---- (     )", "L");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");

		if (String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}

		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);

		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		this.print(-7, 1,
				/**
				 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8---------9
				 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
				 */
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	   戶名    	          計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 費用");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fnAllList2 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fnAllList3 = new ArrayList<Map<String, String>>();

		try {
			fnAllList1 = l4211ARServiceImpl.findAll(titaVo, 1);
			fnAllList2 = l4211ARServiceImpl.findAll(titaVo, 2);
			fnAllList3 = l4211ARServiceImpl.findAll(titaVo, 3);

			this.info("Going to execWithBatchMapList");
			execWithBatchMapList(l4211ARServiceImpl.findAll(titaVo, 0), titaVo);
			this.info("Done execWithBatchMapList !! wow!");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}

		makePdf(fnAllList1, fnAllList2, fnAllList3, false, titaVo);
	}

	public void execWithBatchMapList(List<Map<String, String>> fnAllList, TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList1, fnAllList2, fnAllList3;

//        1
//        "ReconCode" ASC
//        "BatchNo" ASC
//        "SortingForSubTotal" ASC
//        "EntryDate" ASC
//        "DetailSeq" ASC
//        "CustNo" ASC
//        "FacmNo" ASC
//        "BormNo" ASC
//
//        2
//        "ReconCode" ASC
//        "BatchNo" ASC
//        "SortingForSubTotal" ASC
//        "EntryDate" ASC
//        "RepayAmt" DESC
//        "CustNo" ASC
//        "FacmNo" ASC
//        "BormNo" ASC
//
//        3
//        "ReconCode" ASC
//        "BatchNo" ASC
//        "SortingForSubTotal" ASC
//        "EntryDate" ASC
//        "CustNo" ASC
//        "FacmNo" ASC
//        "BormNo" ASC

		// facmno, bormno 已經在 query 裡面 concat 到 custno，所以不在這裡加sort
		
		fnAllList1 = sortMapListCom.beginSort(fnAllList)
				.ascString("ReconCode")
				.ascString("BatchNo")
				.ascString("SortingForSubTotal")
				.ascString("EntryDate")
				.ascString("DetailSeq")
				.ascString("CustNo")
				.getList();

		fnAllList2 = sortMapListCom.beginSort(fnAllList)
				.ascString("ReconCode")
				.ascString("BatchNo")
				.ascString("SortingForSubTotal")
				.ascString("EntryDate")
				.descNumber("RepayAmt")
				.ascString("CustNo")
				.getList();

		fnAllList3 = sortMapListCom.beginSort(fnAllList)
				.ascString("ReconCode")
				.ascString("BatchNo")
				.ascString("SortingForSubTotal")
				.ascString("EntryDate")
				.ascString("CustNo")
				.getList();

		makePdf(fnAllList1, fnAllList2, fnAllList3, true, titaVo);
	}

	private void makePdf(List<Map<String, String>> fnAllList1, List<Map<String, String>> fnAllList2,
			List<Map<String, String>> fnAllList3, boolean isBatchMapList, TitaVo titaVo) throws LogicException {
		
		txCode = this.getParentTranCode();
		
		if (txCode == null || txCode.trim().isEmpty())
		{
			txCode = titaVo.getTxcd();
		}
		reportName = "L420A".equals(txCode) ? "匯款轉帳檢核明細表" : "匯款總傳票明細表";
		acdate = titaVo.get("AcDate");

		if (fnAllList1.size() == 0) {
			throw new LogicException("E2003", "查無資料"); // 查無資料
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCode, reportName, "", "A4", "L");
		this.setFont(1, 8);

		reportkind = 1;
		report1(fnAllList1, isBatchMapList);

		reportkind = 2;
		newPage();
		report2(fnAllList2, isBatchMapList);

		reportkind = 3;
		newPage();
		report3(fnAllList3, isBatchMapList);

//        long sno = 
		this.close();
//        this.toPdf(sno);
	}

	private void report1(List<Map<String, String>> fnAllList, boolean isBatchMapList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("RepayAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("RepayAmt"));
			makeferamt = Integer.valueOf(tfnAllList.get("AcctAmt"));
			principal = Integer.valueOf(tfnAllList.get("Principal"));
			interest = Integer.valueOf(tfnAllList.get("Interest"));
			payment = Integer.valueOf(tfnAllList.get("TempPayAmt"));
			damages = Integer.valueOf(tfnAllList.get("BreachAmt"));
			temporaryloan = Integer.valueOf(tfnAllList.get("TempDr"));
			collection = Integer.valueOf(tfnAllList.get("TempCr"));
			shortpayment = Integer.valueOf(tfnAllList.get("Shortfall"));
			others = Integer.valueOf(tfnAllList.get("Fee"));
			count++;

			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("AcctItem");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					totalsumTransferAmt += allsumTransferAmt;
					totalsumMakerferAmt += allsumMakeferAmt;
					totalsumPrincipal += allsumPrincipal;
					totalsumInterest += allsumInterest;
					totalsumPayment += allsumPayment;
					totalsumDamages += allsumDamages;
					totalsumTemporaryLoan += allsumTemporaryLoan;
					totalsumCollection += allsumCollection;
					totalsumShortPayment += allsumShortPayment;
					totalsumOthers += allsumOthers;

					allsumTransferAmt = 0;
					allsumMakeferAmt = 0;
					allsumPrincipal = 0;
					allsumInterest = 0;
					allsumPayment = 0;
					allsumDamages = 0;
					allsumTemporaryLoan = 0;
					allsumCollection = 0;
					allsumShortPayment = 0;
					allsumOthers = 0;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// 頁面設置配置
				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 10, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 10, A17, "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("AcctItem").toString())
							|| !txCode.equals(tfnAllList.get("RepayItem").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("AcctItem").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt += allsumTransferAmt;
						totalsumMakerferAmt += allsumMakeferAmt;
						totalsumPrincipal += allsumPrincipal;
						totalsumInterest += allsumInterest;
						totalsumPayment += allsumPayment;
						totalsumDamages += allsumDamages;
						totalsumTemporaryLoan += allsumTemporaryLoan;
						totalsumCollection += allsumCollection;
						totalsumShortPayment += allsumShortPayment;
						totalsumOthers += allsumOthers;

						allsumTransferAmt = 0;
						allsumMakeferAmt = 0;
						allsumPrincipal = 0;
						allsumInterest = 0;
						allsumPayment = 0;
						allsumDamages = 0;
						allsumTemporaryLoan = 0;
						allsumCollection = 0;
						allsumShortPayment = 0;
						allsumOthers = 0;
					}

				}

				if (pageCnt >= 30) { // 超過30筆自動換頁 並印出當前的代碼

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 10, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 10, tfnAllList.get("ReconCode"), "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
					this.print(-8, 0, "");

				}
			} // else

			npcount++;
			tround++;

//            每頁筆數相加
			pageCnt++;

			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("AcctItem").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("RepayItem").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("DetailSeq"), "C");// 匯款序號
				this.print(0, 29, dfTransferAmt, "R");// 匯款金額

				allsumTransferAmt += transferamt;

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, 40, dfMakeferAmt, "R");// 作帳金額
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("RepaidPeriod");
			this.print(0, 41, custNo);// 戶號
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 60, name + " " + tfnAllList.get("CloseReasonCode"));
			this.print(0, 74, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
					+ showRocDate(tfnAllList.get("IntEndDate"), 1));// 起日與迄日

			if (dfInterest.equals("0")) {
				this.print(0, 112, "", "R"); // 利息
			} else {
				this.print(0, 112, dfInterest, "R"); // 利息
			}
			if (dfPayment.equals("0")) {
				this.print(0, 121, "", "R"); // 暫付款
			} else {
				this.print(0, 121, dfPayment, "R"); // 暫付款
			}
			if (dfDamages.equals("0")) {
				this.print(0, 131, "", "R"); // 違約金
			} else {
				this.print(0, 131, dfDamages, "R"); // 違約金
			}
			if (dfCollection.equals("0")) {
				this.print(0, 151, "", "R"); // 暫收貸
			} else {
				this.print(0, 151, dfCollection, "R"); // 暫收貸
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, 160, "", "R"); // 短繳
			} else {
				this.print(0, 160, dfShortPayment, "R"); // 短繳
			}
			if (dfOthers.equals("0")) {
				this.print(0, 167, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 167, dfOthers, "R"); // 帳管費及其他
			}
			if (dfPrincipal.equals("0")) {
				this.print(0, 103, "", "R"); // 本金
			} else {
				this.print(0, 103, dfPrincipal, "R"); // 本金
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, 143, "", "R"); // 暫收借
			} else {
				this.print(0, 143, dfTemporaryLoan, "R"); // 暫收借
			}

			allsumMakeferAmt += makeferamt;
			allsumPrincipal += principal;
			allsumInterest += interest;
			allsumPayment += payment;
			allsumDamages += damages;
			allsumTemporaryLoan += temporaryloan;
			allsumCollection += collection;
			allsumShortPayment += shortpayment;
			allsumOthers += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if ("".equals(tfnAllList.get("RepayItem"))) {
					if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
				} else {
					this.print(1, 2, tfnAllList.get("RepayItem"));
				}
				this.print(0, 14, " 小計 ");

				atAll();

				totalsumTransferAmt += allsumTransferAmt;
				totalsumMakerferAmt += allsumMakeferAmt;
				totalsumPrincipal += allsumPrincipal;
				totalsumInterest += allsumInterest;
				totalsumPayment += allsumPayment;
				totalsumDamages += allsumDamages;
				totalsumTemporaryLoan += allsumTemporaryLoan;
				totalsumCollection += allsumCollection;
				totalsumShortPayment += allsumShortPayment;
				totalsumOthers += allsumOthers;

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = 0;
				allsumMakeferAmt = 0;
				allsumPrincipal = 0;
				allsumInterest = 0;
				allsumPayment = 0;
				allsumDamages = 0;
				allsumTemporaryLoan = 0;
				allsumCollection = 0;
				allsumShortPayment = 0;
				allsumOthers = 0;

				totalsumTransferAmt = 0;
				totalsumMakerferAmt = 0;
				totalsumPrincipal = 0;
				totalsumInterest = 0;
				totalsumPayment = 0;
				totalsumDamages = 0;
				totalsumTemporaryLoan = 0;
				totalsumCollection = 0;
				totalsumShortPayment = 0;
				totalsumOthers = 0;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====報表結束=====", "C");
				this.print(2, this.getMidXAxis(), "課長：　　　　　　　　　　製表人：", "C");

				pageCnt = 0;
				npcount = 0;
				tround = 0;
			}

		} // for
	}

	private void report2(List<Map<String, String>> fnAllList, boolean isBatchMapList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("RepayAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("RepayAmt"));
			makeferamt = Integer.valueOf(tfnAllList.get("AcctAmt"));
			principal = Integer.valueOf(tfnAllList.get("Principal"));
			interest = Integer.valueOf(tfnAllList.get("Interest"));
			payment = Integer.valueOf(tfnAllList.get("TempPayAmt"));
			damages = Integer.valueOf(tfnAllList.get("BreachAmt"));
			temporaryloan = Integer.valueOf(tfnAllList.get("TempDr"));
			collection = Integer.valueOf(tfnAllList.get("TempCr"));
			shortpayment = Integer.valueOf(tfnAllList.get("Shortfall"));
			others = Integer.valueOf(tfnAllList.get("Fee"));
			count++;

			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("AcctItem");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					totalsumTransferAmt += allsumTransferAmt;
					totalsumMakerferAmt += allsumMakeferAmt;
					totalsumPrincipal += allsumPrincipal;
					totalsumInterest += allsumInterest;
					totalsumPayment += allsumPayment;
					totalsumDamages += allsumDamages;
					totalsumTemporaryLoan += allsumTemporaryLoan;
					totalsumCollection += allsumCollection;
					totalsumShortPayment += allsumShortPayment;
					totalsumOthers += allsumOthers;

					allsumTransferAmt = 0;
					allsumMakeferAmt = 0;
					allsumPrincipal = 0;
					allsumInterest = 0;
					allsumPayment = 0;
					allsumDamages = 0;
					allsumTemporaryLoan = 0;
					allsumCollection = 0;
					allsumShortPayment = 0;
					allsumOthers = 0;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// 頁面設置配置

				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 10, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 10, A17, "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("AcctItem").toString())
							|| !txCode.equals(tfnAllList.get("RepayItem").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("AcctItem").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt += allsumTransferAmt;
						totalsumMakerferAmt += allsumMakeferAmt;
						totalsumPrincipal += allsumPrincipal;
						totalsumInterest += allsumInterest;
						totalsumPayment += allsumPayment;
						totalsumDamages += allsumDamages;
						totalsumTemporaryLoan += allsumTemporaryLoan;
						totalsumCollection += allsumCollection;
						totalsumShortPayment += allsumShortPayment;
						totalsumOthers += allsumOthers;

						allsumTransferAmt = 0;
						allsumMakeferAmt = 0;
						allsumPrincipal = 0;
						allsumInterest = 0;
						allsumPayment = 0;
						allsumDamages = 0;
						allsumTemporaryLoan = 0;
						allsumCollection = 0;
						allsumShortPayment = 0;
						allsumOthers = 0;
					}

				}

				if (pageCnt >= 30) { // 超過40筆自動換頁 並印出當前的代碼

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 10, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 10, tfnAllList.get("ReconCode"), "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
					this.print(-8, 0, "");
				}
			} // else

			npcount++;
			tround++;

//            每頁筆數相加
			pageCnt++;

			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("AcctItem").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("RepayItem").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("DetailSeq"), "C");// 匯款序號
				this.print(0, 29, dfTransferAmt, "R");// 匯款金額

				allsumTransferAmt += transferamt;

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, 40, dfMakeferAmt, "R");// 作帳金額
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("RepaidPeriod");
			this.print(0, 41, custNo);// 戶號
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 60, name + " " + tfnAllList.get("CloseReasonCode"));
			this.print(0, 74, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
					+ showRocDate(tfnAllList.get("IntEndDate"), 1));// 起日與迄日

			if (dfInterest.equals("0")) {
				this.print(0, 112, "", "R"); // 利息
			} else {
				this.print(0, 112, dfInterest, "R"); // 利息
			}
			if (dfPayment.equals("0")) {
				this.print(0, 121, "", "R"); // 暫付款
			} else {
				this.print(0, 121, dfPayment, "R"); // 暫付款
			}
			if (dfDamages.equals("0")) {
				this.print(0, 131, "", "R"); // 違約金
			} else {
				this.print(0, 131, dfDamages, "R"); // 違約金
			}
			if (dfCollection.equals("0")) {
				this.print(0, 151, "", "R"); // 暫收貸
			} else {
				this.print(0, 151, dfCollection, "R"); // 暫收貸
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, 160, "", "R"); // 短繳
			} else {
				this.print(0, 160, dfShortPayment, "R"); // 短繳
			}
			if (dfOthers.equals("0")) {
				this.print(0, 167, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 167, dfOthers, "R"); // 帳管費及其他
			}
			if (dfPrincipal.equals("0")) {
				this.print(0, 103, "", "R"); // 本金
			} else {
				this.print(0, 103, dfPrincipal, "R"); // 本金
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, 143, "", "R"); // 暫收借
			} else {
				this.print(0, 143, dfTemporaryLoan, "R"); // 暫收借
			}

			allsumMakeferAmt += makeferamt;
			allsumPrincipal += principal;
			allsumInterest += interest;
			allsumPayment += payment;
			allsumDamages += damages;
			allsumTemporaryLoan += temporaryloan;
			allsumCollection += collection;
			allsumShortPayment += shortpayment;
			allsumOthers += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if ("".equals(tfnAllList.get("RepayItem"))) {
					if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
				} else {
					this.print(1, 2, tfnAllList.get("RepayItem"));
				}
				this.print(0, 14, " 小計 ");

				atAll();

				totalsumTransferAmt += allsumTransferAmt;
				totalsumMakerferAmt += allsumMakeferAmt;
				totalsumPrincipal += allsumPrincipal;
				totalsumInterest += allsumInterest;
				totalsumPayment += allsumPayment;
				totalsumDamages += allsumDamages;
				totalsumTemporaryLoan += allsumTemporaryLoan;
				totalsumCollection += allsumCollection;
				totalsumShortPayment += allsumShortPayment;
				totalsumOthers += allsumOthers;

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = 0;
				allsumMakeferAmt = 0;
				allsumPrincipal = 0;
				allsumInterest = 0;
				allsumPayment = 0;
				allsumDamages = 0;
				allsumTemporaryLoan = 0;
				allsumCollection = 0;
				allsumShortPayment = 0;
				allsumOthers = 0;

				totalsumTransferAmt = 0;
				totalsumMakerferAmt = 0;
				totalsumPrincipal = 0;
				totalsumInterest = 0;
				totalsumPayment = 0;
				totalsumDamages = 0;
				totalsumTemporaryLoan = 0;
				totalsumCollection = 0;
				totalsumShortPayment = 0;
				totalsumOthers = 0;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====報表結束=====", "C");
				this.print(2, this.getMidXAxis(), "課長：　　　　　　　　　　製表人：", "C");
			}

		} // for
	}

	private void report3(List<Map<String, String>> fnAllList, boolean isBatchMapList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("RepayAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("RepayAmt"));
			makeferamt = Integer.valueOf(tfnAllList.get("AcctAmt"));
			principal = Integer.valueOf(tfnAllList.get("Principal"));
			interest = Integer.valueOf(tfnAllList.get("Interest"));
			payment = Integer.valueOf(tfnAllList.get("TempPayAmt"));
			damages = Integer.valueOf(tfnAllList.get("BreachAmt"));
			temporaryloan = Integer.valueOf(tfnAllList.get("TempDr"));
			collection = Integer.valueOf(tfnAllList.get("TempCr"));
			shortpayment = Integer.valueOf(tfnAllList.get("Shortfall"));
			others = Integer.valueOf(tfnAllList.get("Fee"));
			count++;

			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("AcctItem");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					totalsumTransferAmt += allsumTransferAmt;
					totalsumMakerferAmt += allsumMakeferAmt;
					totalsumPrincipal += allsumPrincipal;
					totalsumInterest += allsumInterest;
					totalsumPayment += allsumPayment;
					totalsumDamages += allsumDamages;
					totalsumTemporaryLoan += allsumTemporaryLoan;
					totalsumCollection += allsumCollection;
					totalsumShortPayment += allsumShortPayment;
					totalsumOthers += allsumOthers;

					allsumTransferAmt = 0;
					allsumMakeferAmt = 0;
					allsumPrincipal = 0;
					allsumInterest = 0;
					allsumPayment = 0;
					allsumDamages = 0;
					allsumTemporaryLoan = 0;
					allsumCollection = 0;
					allsumShortPayment = 0;
					allsumOthers = 0;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// 頁面設置配置

				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 10, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 10, A17, "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("AcctItem").toString())
							|| !txCode.equals(tfnAllList.get("RepayItem").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("AcctItem").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt += allsumTransferAmt;
						totalsumMakerferAmt += allsumMakeferAmt;
						totalsumPrincipal += allsumPrincipal;
						totalsumInterest += allsumInterest;
						totalsumPayment += allsumPayment;
						totalsumDamages += allsumDamages;
						totalsumTemporaryLoan += allsumTemporaryLoan;
						totalsumCollection += allsumCollection;
						totalsumShortPayment += allsumShortPayment;
						totalsumOthers += allsumOthers;

						allsumTransferAmt = 0;
						allsumMakeferAmt = 0;
						allsumPrincipal = 0;
						allsumInterest = 0;
						allsumPayment = 0;
						allsumDamages = 0;
						allsumTemporaryLoan = 0;
						allsumCollection = 0;
						allsumShortPayment = 0;
						allsumOthers = 0;
					}

				}

				if (pageCnt >= 30) { // 超過40筆自動換頁 並印出當前的代碼

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 10, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 10, tfnAllList.get("ReconCode"), "C");// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// 批次號碼(表頭)
					this.print(-8, 0, "");
				}
			} // else

			npcount++;
			tround++;

//            每頁筆數相加
			pageCnt++;

			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("AcctItem").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("RepayItem").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("DetailSeq"), "C");// 匯款序號
				this.print(0, 29, dfTransferAmt, "R");// 匯款金額

				allsumTransferAmt += transferamt;

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, 40, dfMakeferAmt, "R");// 作帳金額
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("RepaidPeriod");
			this.print(0, 41, custNo);// 戶號
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 60, name + " " + tfnAllList.get("CloseReasonCode"));
			this.print(0, 74, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
					+ showRocDate(tfnAllList.get("IntEndDate"), 1));// 起日與迄日

			if (dfInterest.equals("0")) {
				this.print(0, 112, "", "R"); // 利息
			} else {
				this.print(0, 112, dfInterest, "R"); // 利息
			}
			if (dfPayment.equals("0")) {
				this.print(0, 121, "", "R"); // 暫付款
			} else {
				this.print(0, 121, dfPayment, "R"); // 暫付款
			}
			if (dfDamages.equals("0")) {
				this.print(0, 131, "", "R"); // 違約金
			} else {
				this.print(0, 131, dfDamages, "R"); // 違約金
			}
			if (dfCollection.equals("0")) {
				this.print(0, 151, "", "R"); // 暫收貸
			} else {
				this.print(0, 151, dfCollection, "R"); // 暫收貸
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, 160, "", "R"); // 短繳
			} else {
				this.print(0, 160, dfShortPayment, "R"); // 短繳
			}
			if (dfOthers.equals("0")) {
				this.print(0, 167, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 167, dfOthers, "R"); // 帳管費及其他
			}
			if (dfPrincipal.equals("0")) {
				this.print(0, 103, "", "R"); // 本金
			} else {
				this.print(0, 103, dfPrincipal, "R"); // 本金
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, 143, "", "R"); // 暫收借
			} else {
				this.print(0, 143, dfTemporaryLoan, "R"); // 暫收借
			}

			allsumMakeferAmt += makeferamt;
			allsumPrincipal += principal;
			allsumInterest += interest;
			allsumPayment += payment;
			allsumDamages += damages;
			allsumTemporaryLoan += temporaryloan;
			allsumCollection += collection;
			allsumShortPayment += shortpayment;
			allsumOthers += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if ("".equals(tfnAllList.get("RepayItem"))) {
					if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
				} else {
					this.print(1, 2, tfnAllList.get("RepayItem"));
				}
				this.print(0, 14, " 小計 ");

				atAll();

				totalsumTransferAmt += allsumTransferAmt;
				totalsumMakerferAmt += allsumMakeferAmt;
				totalsumPrincipal += allsumPrincipal;
				totalsumInterest += allsumInterest;
				totalsumPayment += allsumPayment;
				totalsumDamages += allsumDamages;
				totalsumTemporaryLoan += allsumTemporaryLoan;
				totalsumCollection += allsumCollection;
				totalsumShortPayment += allsumShortPayment;
				totalsumOthers += allsumOthers;

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = 0;
				allsumMakeferAmt = 0;
				allsumPrincipal = 0;
				allsumInterest = 0;
				allsumPayment = 0;
				allsumDamages = 0;
				allsumTemporaryLoan = 0;
				allsumCollection = 0;
				allsumShortPayment = 0;
				allsumOthers = 0;

				totalsumTransferAmt = 0;
				totalsumMakerferAmt = 0;
				totalsumPrincipal = 0;
				totalsumInterest = 0;
				totalsumPayment = 0;
				totalsumDamages = 0;
				totalsumTemporaryLoan = 0;
				totalsumCollection = 0;
				totalsumShortPayment = 0;
				totalsumOthers = 0;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====報表結束=====", "C");
				this.print(2, this.getMidXAxis(), "課長：　　　　　　　　　　製表人：", "C");
			}

		} // for
	}

	private void atAll() {

		if (allsumTransferAmt != 0) {
			this.print(0, 29, String.format("%,d", allsumTransferAmt), "R");
		} else {
			this.print(0, 29, "");
		}
		if (allsumMakeferAmt != 0) {
			this.print(0, 40, String.format("%,d", allsumMakeferAmt), "R");
		} else {
			this.print(0, 40, "");
		}

		if (allsumPrincipal != 0) {
			this.print(0, 103, String.format("%,d", allsumPrincipal), "R");
		} else {
			this.print(0, 103, "");
		}
		if (allsumPayment != 0) {
			this.print(0, 121, String.format("%,d", allsumPayment), "R");
		} else {
			this.print(0, 121, "");
		}
		if (allsumDamages != 0) {
			this.print(0, 131, String.format("%,d", allsumDamages), "R");
		} else {
			this.print(0, 131, "");
		}
		if (allsumTemporaryLoan != 0) {
			this.print(0, 143, String.format("%,d", allsumTemporaryLoan), "R");
		} else {
			this.print(0, 143, "");
		}
		if (allsumShortPayment != 0) {
			this.print(0, 160, String.format("%,d", allsumShortPayment), "R");
		} else {
			this.print(0, 160, "");
		}
		if (allsumOthers != 0) {
			this.print(0, 167, String.format("%,d", allsumOthers), "R");
		} else {
			this.print(0, 167, "");
		}
		if (allsumInterest != 0) {
			this.print(1, 112, String.format("%,d", allsumInterest), "R");
		} else {
			this.print(1, 112, "");
		}
		if (allsumCollection != 0) {
			this.print(0, 151, String.format("%,d", allsumCollection), "R");
		} else {
			this.print(0, 151, "");
		}

	}

	private void totalAll() {

		if (totalsumTransferAmt != 0) {
			this.print(0, 29, String.format("%,d", totalsumTransferAmt), "R");
		} else {
			this.print(0, 29, "");
		}
		if (totalsumMakerferAmt != 0) {
			this.print(0, 40, String.format("%,d", totalsumMakerferAmt), "R");
		} else {
			this.print(0, 40, "");
		}

		if (totalsumPrincipal != 0) {
			this.print(0, 103, String.format("%,d", totalsumPrincipal), "R");
		} else {
			this.print(0, 103, "");
		}
		if (totalsumPayment != 0) {
			this.print(0, 121, String.format("%,d", totalsumPayment), "R");
		} else {
			this.print(0, 121, "");
		}
		if (totalsumDamages != 0) {
			this.print(0, 131, String.format("%,d", totalsumDamages), "R");
		} else {
			this.print(0, 131, "");
		}
		if (totalsumTemporaryLoan != 0) {
			this.print(0, 143, String.format("%,d", totalsumTemporaryLoan), "R");
		} else {
			this.print(0, 143, "");
		}
		if (totalsumShortPayment != 0) {
			this.print(0, 160, String.format("%,d", totalsumShortPayment), "R");
		} else {
			this.print(0, 160, "");
		}
		if (totalsumOthers != 0) {
			this.print(0, 167, String.format("%,d", totalsumOthers), "R");
		} else {
			this.print(0, 167, "");
		}
		if (totalsumInterest != 0) {
			this.print(1, 112, String.format("%,d", totalsumInterest), "R");
		} else {
			this.print(1, 112, "");
		}
		if (totalsumCollection != 0) {
			this.print(0, 151, String.format("%,d", totalsumCollection), "R");
		} else {
			this.print(0, 151, "");
		}

	}

}
