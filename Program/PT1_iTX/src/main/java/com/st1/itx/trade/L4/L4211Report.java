package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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


	BigDecimal allsumTransferAmt = BigDecimal.ZERO;
	BigDecimal allsumMakeferAmt = BigDecimal.ZERO;
	BigDecimal allsumPrincipal = BigDecimal.ZERO;
	BigDecimal allsumInterest = BigDecimal.ZERO;
	BigDecimal allsumPayment = BigDecimal.ZERO;
	BigDecimal allsumDamages = BigDecimal.ZERO;
	BigDecimal allsumTemporaryLoan = BigDecimal.ZERO;
	BigDecimal allsumCollection = BigDecimal.ZERO;
	BigDecimal allsumShortPayment = BigDecimal.ZERO;
	BigDecimal allsumOthers = BigDecimal.ZERO;

	BigDecimal totalsumTransferAmt = BigDecimal.ZERO;
	BigDecimal totalsumMakerferAmt = BigDecimal.ZERO;
	BigDecimal totalsumPrincipal = BigDecimal.ZERO;
	BigDecimal totalsumInterest = BigDecimal.ZERO;
	BigDecimal totalsumPayment = BigDecimal.ZERO;
	BigDecimal totalsumDamages = BigDecimal.ZERO;
	BigDecimal totalsumTemporaryLoan = BigDecimal.ZERO;
	BigDecimal totalsumCollection = BigDecimal.ZERO;
	BigDecimal totalsumShortPayment = BigDecimal.ZERO;
	BigDecimal totalsumOthers = BigDecimal.ZERO;

	BigDecimal transferamt = BigDecimal.ZERO;
	BigDecimal makeferamt = BigDecimal.ZERO;
	BigDecimal principal = BigDecimal.ZERO;
	BigDecimal interest = BigDecimal.ZERO;
	BigDecimal payment = BigDecimal.ZERO;
	BigDecimal damages = BigDecimal.ZERO;
	BigDecimal temporaryloan = BigDecimal.ZERO;
	BigDecimal collection = BigDecimal.ZERO;
	BigDecimal shortpayment = BigDecimal.ZERO;
	BigDecimal others = BigDecimal.ZERO;

	// ??????????????????
	int c1 = 1; // ?????????
	int c2 = 16; // ????????????
	int c3 = 33;// ????????????
	int c4 = 46;// ????????????
	int c5 = 48;// ??????
	int c6 = 69;// ??????
	int c7 = 83;// ???????????????
	int c8 = 118;// ??????
	int c9 = 132;// ??????
	int c10 = 145;// ?????????
	int c11 = 157;// ?????????
	int c12 = 171;// ?????????
	int c13 = 186;// ?????????
	int c14 = 197;// ??????
	int c15 = 209;// ??????????????????

	String acdate = "";
	String year = "";
	String month = "";
	String date = "";

	String txCode = "";
	String reportName = "";

	//??????????????????
	int rightSd = 190;
	
	// ????????????
	private int pageIndex = 38;
	// ????????????
	private int reportkind = 0;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		if (reportkind == 1) {
//			printHeader();
			publicHeader(reportName +" ---- (     )");
		} else if (reportkind == 2) {
//			printHeaderP1();
			publicHeader(reportName + " - ???????????????" + " ---- (     )");
		} else {
//			printHeaderP2();
			publicHeader(reportName + " - ?????????" + " ---- (     )");
		}
		// ???????????????(???????????????)
		this.setBeginRow(9);

		// ??????????????????(???????????????)
		this.setMaxRows(54);
	}
	
	public void publicHeader(String text) {
		this.print(-1, rightSd, "??????????????????", "L");
		this.print(-2, 3, "?????? ID???" + txCode);
		this.print(-2, this.getMidXAxis(), "????????????????????????????????????", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//            ???/???/???(???????????????)
		this.print(-2, rightSd, "???    ??????" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "L");
		this.print(-3, 3, "???  ??? ???" + txCode);
		this.print(-3, this.getMidXAxis(), text, "C");
		this.print(-3, rightSd, "???    ??????" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "L");
		this.print(-4, rightSd, "???    ??????" + this.getNowPage(), "L");

		this.print(-5, 2, "???????????? ....");

		if (String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}

		this.print(-5, this.getMidXAxis(), year + " ??? " + month + " ??? " + date + " ???", "C");


		this.print(-5, rightSd, "???    ?????????", "L");
		this.print(-6, 0, "");
		/**
		 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8---------9
		 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */

		this.print(-7, 1," ?????????   ????????????     ????????????    ????????????  ??????           	   ??????    	        ???????????????       	      ??????         ??????       ?????????     ?????????   	   ?????????        ?????????      ??????    	   ??????");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	};
	
//	public void printHeaderP() {
//		this.print(-1, rightSd, "??????????????????", "L");
//		this.print(-2, 3, "?????? ID???" + txCode);
//		this.print(-2, this.getMidXAxis(), "????????????????????????????????????", "C");
//		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
////            ???/???/???(???????????????)
//		this.print(-2, rightSd, "???    ??????" + dateUtil.getNowStringBc().substring(4, 6) + "/"
//				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "L");
//		this.print(-3, 3, "???  ??? ???" + txCode);
//		this.print(-3, this.getMidXAxis() - 1, reportName, "R");
//		this.print(-3, this.getMidXAxis() + 1, " ---- (     )", "L");
//		this.print(-3, rightSd, "???    ??????" + dateUtil.getNowStringTime().substring(0, 2) + ":"
//				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "L");
//		this.print(-4, rightSd, "???    ??????" + this.getNowPage(), "L");
//
//		this.print(-5, 2, "???????????? ....");
//
//		if (String.valueOf(acdate).length() == 7) {
//			year = String.valueOf(acdate).substring(0, 3);
//			month = String.valueOf(acdate).substring(3, 5);
//			date = String.valueOf(acdate).substring(5, 7);
//		} else {
//			year = String.valueOf(acdate).substring(0, 2);
//			month = String.valueOf(acdate).substring(2, 4);
//			date = String.valueOf(acdate).substring(4, 6);
//		}
//
//		this.print(-5, this.getMidXAxis(), year + " ??? " + month + " ??? " + date + " ???", "C");
//
//
//		this.print(-5, rightSd, "???    ?????????", "L");
//		this.print(-6, 0, "");
//		/**
//		 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6---------7---------8---------9
//		 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
//		 */
//
//		
//		this.print(-7, 1," ?????????    ????????????     ????????????    ???????????? ??????           	   ??????    	        ???????????????       	      ??????         ??????       ?????????     ?????????   	     ?????????        ?????????      ??????  	   ??????");
//		this.print(-8, 0,
//				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//		// 1234567-001-001 11 ??????????????? 11 111/11/11-111/11/11
//	}
//
//	public void printHeaderP1() {
//		this.print(-1, rightSd, "??????????????????","L");
//		this.print(-2, 3, "?????? ID???" + txCode);
//		this.print(-2, 80, "????????????????????????????????????", "C");
//		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
////            ???/???/???(???????????????)
//		this.print(-2, rightSd, "???    ??????" + dateUtil.getNowStringBc().substring(4, 6) + "/"
//				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "L");
//		this.print(-3, 3, "???  ??? ???" + txCode);
//		this.print(-3, this.getMidXAxis() - 1, reportName + " - ???????????????", "R");
//		this.print(-3, this.getMidXAxis() + 1, " ---- (     )", "L");
//		this.print(-3, rightSd, "???    ??????" + dateUtil.getNowStringTime().substring(0, 2) + ":"
//				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "L");
//		this.print(-4, rightSd, "???    ??????" + this.getNowPage(),"L");
//		this.print(-5, 2, "???????????? ....");
////		this.print(-5, 81, "???    ???   ???", "C");
//
//		if (String.valueOf(acdate).length() == 7) {
//			year = String.valueOf(acdate).substring(0, 3);
//			month = String.valueOf(acdate).substring(3, 5);
//			date = String.valueOf(acdate).substring(5, 7);
//		} else {
//			year = String.valueOf(acdate).substring(0, 2);
//			month = String.valueOf(acdate).substring(2, 4);
//			date = String.valueOf(acdate).substring(4, 6);
//		}
//
//		this.print(-5, this.getMidXAxis(), year + " ??? " + month + " ??? " + date + " ???", "C");
////		this.print(-5, 71, year);
////		this.print(-5, 78, month);
////		this.print(-5, 83, date);
//
//		this.print(-5, rightSd, "???    ?????????", "R");
//		this.print(-6, 0, "");
//		this.print(-7, 1,
//				" ?????????    ????????????     ????????????    ???????????? ??????           	   ??????    	        ???????????????       	      ??????         ??????       ?????????     ?????????   	     ?????????        ?????????      ??????  	   ??????");
//		this.print(-8, 0,
//				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//	}
//
//	public void printHeaderP2() {
//		this.print(-1, rightSd, "??????????????????","L");
//		this.print(-2, 3, "?????? ID???" + txCode);
//		this.print(-2, 80, "????????????????????????????????????", "C");
//		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
////            ???/???/???(???????????????)
//		this.print(-2, rightSd, "???    ??????" + dateUtil.getNowStringBc().substring(4, 6) + "/"
//				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "L");
//		this.print(-3, 3, "???  ??? ???" + txCode);
//		this.print(-3, this.getMidXAxis() - 1, reportName + " - ?????????", "R");
//		this.print(-3, this.getMidXAxis() + 1, " ---- (     )", "L");
//		this.print(-3, rightSd, "???    ??????" + dateUtil.getNowStringTime().substring(0, 2) + ":"
//				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "L");
//		this.print(-4, rightSd, "???    ??????" + this.getNowPage(),"L");
//		this.print(-5, 2, "???????????? ....");
////		this.print(-5, 81, "???    ???   ???", "C");
//
//		if (String.valueOf(acdate).length() == 7) {
//			year = String.valueOf(acdate).substring(0, 3);
//			month = String.valueOf(acdate).substring(3, 5);
//			date = String.valueOf(acdate).substring(5, 7);
//		} else {
//			year = String.valueOf(acdate).substring(0, 2);
//			month = String.valueOf(acdate).substring(2, 4);
//			date = String.valueOf(acdate).substring(4, 6);
//		}
//
////		this.print(-5, 71, year);
////		this.print(-5, 78, month);
////		this.print(-5, 83, date);
//
//		this.print(-5, this.getMidXAxis(), year + " ??? " + month + " ??? " + date + " ???", "C");
//		
//		this.print(-5, rightSd, "???    ?????????", "R");
//		this.print(-6, 0, "");
//		this.print(-7, 1,
//				" ?????????    ????????????     ????????????    ???????????? ??????           	   ??????    	        ???????????????       	      ??????         ??????       ?????????     ?????????   	     ?????????        ?????????      ??????  	   ??????");
//		this.print(-8, 0,
//				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<Map<String, String>>();

		try {
			fnAllList = l4211ARServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}

		List<Map<String, String>> fnAllList1 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fnAllList2 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fnAllList3 = new ArrayList<Map<String, String>>();

		fnAllList1 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").ascNumber("DetailSeq").ascString("CustNo")
				.getList();

		fnAllList2 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").descNumber("RepayAmt").ascString("CustNo")
				.getList();

		fnAllList3 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").ascString("CustNo").getList();

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

		// facmno, bormno ????????? query ?????? concat ??? custno????????????????????????sort

		fnAllList1 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").ascString("DetailSeq").ascString("CustNo")
				.getList();

		fnAllList2 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").descNumber("RepayAmt").ascString("CustNo")
				.getList();

		fnAllList3 = sortMapListCom.beginSort(fnAllList).ascString("ReconCode").ascString("BatchNo")
				.ascString("SortingForSubTotal").ascString("EntryDate").ascString("CustNo").getList();

		makePdf(fnAllList1, fnAllList2, fnAllList3, false, titaVo);
	}

	private void makePdf(List<Map<String, String>> fnAllList1, List<Map<String, String>> fnAllList2,
			List<Map<String, String>> fnAllList3, boolean isBatchMapList, TitaVo titaVo) throws LogicException {

		txCode = this.getParentTranCode();

		if (txCode == null || txCode.trim().isEmpty()) {
			txCode = titaVo.getTxcd();
		}
		reportName = "L420A".equals(txCode) ? "???????????????????????????" : "????????????????????????";
		acdate = titaVo.get("AcDate");

		if (fnAllList1.size() == 0) {
			throw new LogicException("E2003", "????????????"); // ????????????
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCode, reportName, "", "A4", "L");
		this.setFont(1, 7);

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

	private void report1(List<Map<String, String>> fnAllList, boolean isBatchMapList) throws LogicException {
		String lastSortingForSubTotal = ""; // ?????????SortingForSubTotal
		String lastAcctItem = ""; // ?????????AcctItem
		String msName = ""; // ??????P???
		String msNum = ""; // ????????????
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // ??????????????????(?????????????????????????????????????????????)
//		
		HashMap<tmpFacm, BigDecimal> TxAmtMap = new HashMap<>();

		// ??????L4211?????????????????????TxAmt??????
		if ("L4211".equals(txCode)) {
			for (Map<String, String> tfnAllList : fnAllList) {

				tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
						parse.stringToInteger(tfnAllList.get("DetailSeq")),
						parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

				if (!TxAmtMap.containsKey(tmp)) {
					TxAmtMap.put(tmp, getBigDecimal(tfnAllList.get("TxAmt")));
				} else {
					TxAmtMap.put(tmp, TxAmtMap.get(tmp).add(getBigDecimal(tfnAllList.get("TxAmt"))));
				}
			}
		}
		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("TxAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);
//			String dfPrincipal = formatAmt("20846000", 0);
//			String dfInterest = formatAmt("2816142", 0);
//			String dfPayment = formatAmt("2650271", 0);
//			String dfDamages = formatAmt("2650271", 0);
//			String dfTemporaryLoan = formatAmt("524167", 0);
//			String dfCollection = formatAmt("15100000", 0);
//			String dfShortPayment = formatAmt("20000", 0);
//			String dfOthers = formatAmt("5415645", 0);

			transferamt = getBigDecimal(tfnAllList.get("TxAmt"));
			makeferamt = getBigDecimal(tfnAllList.get("AcctAmt"));
			principal = getBigDecimal(tfnAllList.get("Principal"));
			interest = getBigDecimal(tfnAllList.get("Interest"));
			payment = getBigDecimal(tfnAllList.get("TempPayAmt"));
			damages = getBigDecimal(tfnAllList.get("BreachAmt"));
			temporaryloan = getBigDecimal(tfnAllList.get("TempDr"));
			collection = getBigDecimal(tfnAllList.get("TempCr"));
			shortpayment = getBigDecimal(tfnAllList.get("Shortfall"));
			others = getBigDecimal(tfnAllList.get("Fee"));
			count++;

			// ??????????????????????????????????????????
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // ??????????????????
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
							|| lastSortingForSubTotal.equals(" ")) {
						this.print(1, 2, "?????????");
					} else {
						this.print(1, 2, lastAcctItem);
					}
					this.print(0, 14, " ?????? ");

					atAll();

					totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
					totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
					totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
					totalsumInterest = totalsumInterest.add(allsumInterest);
					totalsumPayment = totalsumPayment.add(allsumPayment);
					totalsumDamages = totalsumDamages.add(allsumDamages);
					totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
					totalsumCollection = totalsumCollection.add(allsumCollection);
					totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
					totalsumOthers = totalsumOthers.add(allsumOthers);

					allsumTransferAmt = BigDecimal.ZERO;
					allsumMakeferAmt = BigDecimal.ZERO;
					allsumPrincipal = BigDecimal.ZERO;
					allsumInterest = BigDecimal.ZERO;
					allsumPayment = BigDecimal.ZERO;
					allsumDamages = BigDecimal.ZERO;
					allsumTemporaryLoan = BigDecimal.ZERO;
					allsumCollection = BigDecimal.ZERO;
					allsumShortPayment = BigDecimal.ZERO;
					allsumOthers = BigDecimal.ZERO;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// ??????????????????
				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 11, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 11, A17, "C");// ????????????(??????)A1~A7 (P03?????????????????????????????????)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// ????????????????????????????????????
				if (tround > 0) {
					// ??????????????????????????????????????????
					String currentSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (!lastSortingForSubTotal.equals(currentSortingForSubTotal)) {
						this.info("currSort     = " + currentSortingForSubTotal);
						this.info("curracctItem = " + tfnAllList.get("AcctItem"));

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
								|| lastSortingForSubTotal.equals(" ")) {
							this.print(1, 2, "?????????");
						} else {
							this.print(1, 2, lastAcctItem);
						}
						this.print(0, 14, " ?????? ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
						totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
						totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
						totalsumInterest = totalsumInterest.add(allsumInterest);
						totalsumPayment = totalsumPayment.add(allsumPayment);
						totalsumDamages = totalsumDamages.add(allsumDamages);
						totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
						totalsumCollection = totalsumCollection.add(allsumCollection);
						totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
						totalsumOthers = totalsumOthers.add(allsumOthers);

						allsumTransferAmt = BigDecimal.ZERO;
						allsumMakeferAmt = BigDecimal.ZERO;
						allsumPrincipal = BigDecimal.ZERO;
						allsumInterest = BigDecimal.ZERO;
						allsumPayment = BigDecimal.ZERO;
						allsumDamages = BigDecimal.ZERO;
						allsumTemporaryLoan = BigDecimal.ZERO;
						allsumCollection = BigDecimal.ZERO;
						allsumShortPayment = BigDecimal.ZERO;
						allsumOthers = BigDecimal.ZERO;
					}

				}

				if (pageCnt >= 30) { // ??????30??????????????? ????????????????????????

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 11, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 11, tfnAllList.get("ReconCode"), "C");// ????????????(??????)A1~A7
																									// (P03?????????????????????????????????)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
					this.print(-8, 0, "");

				}
			} // else

			npcount++;
			tround++;

//            ??????????????????
			pageCnt++;

			// ??????????????????????????????????????? ??????????????? ????????????
			lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");

			lastAcctItem = tfnAllList.get("AcctItem");

			// ?????????????????????

			// ????????? * type = 1: yyy/mm/dd<BR>
			this.print(1, c1, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // ?????????????????? ???????????????
				this.print(0, c2, tfnAllList.get("DetailSeq"), "C");// ????????????

				if ("L4211".equals(txCode)) {

					tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
							parse.stringToInteger(tfnAllList.get("DetailSeq")),
							parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

					if (TxAmtMap.get(tmp) != null) {
						this.print(0, c3, formatAmt(TxAmtMap.get(tmp), 0), "R");// ????????????
					}
				} else {
					this.print(0, c3, dfTransferAmt, "R");// ????????????
				}

				allsumTransferAmt = allsumTransferAmt.add(transferamt);

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, c4, dfMakeferAmt, "R");// ????????????
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("PaidTerms");
			this.print(0, c5, custNo);// ??????
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// ??????
				name = name.substring(0, 5);
			}
			this.print(0, c6, name);
			this.print(0, c6 + 9, tfnAllList.get("CloseReasonCode"));

			if ("999/12/31".equals(showRocDate(tfnAllList.get("IntStartDate"), 1))) { // ?????????????????????????????????
				this.print(0, c7, "-" + showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????

			} else {
				this.print(0, c7, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
						+ showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????
			}

			if (dfPrincipal.equals("0")) {
				this.print(0, c8, "", "R"); // ??????
			} else {
				this.print(0, c8, dfPrincipal, "R"); // ??????
			}
			if (dfInterest.equals("0")) {
				this.print(0, c9, "", "R"); // ??????
			} else {
				this.print(0, c9, dfInterest, "R"); // ??????
			}
			if (dfPayment.equals("0")) {
				this.print(0, c10, "", "R"); // ?????????
			} else {
				this.print(0, c10, dfPayment, "R"); // ?????????
			}
			if (dfDamages.equals("0")) {
				this.print(0, c11, "", "R"); // ?????????
			} else {
				this.print(0, c11, dfDamages, "R"); // ?????????
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, c12, "", "R"); // ?????????
			} else {
				this.print(0, c12, dfTemporaryLoan, "R"); // ?????????
			}
			if (dfCollection.equals("0")) {
				this.print(0, c13, "", "R"); // ?????????
			} else {
				this.print(0, c13, dfCollection, "R"); // ?????????
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, c14, "", "R"); // ??????
			} else {
				this.print(0, c14, dfShortPayment, "R"); // ??????
			}
			if (dfOthers.equals("0")) {
				this.print(0, c15, "", "R"); // ??????????????????
			} else {
				this.print(0, c15, dfOthers, "R"); // ??????????????????
			}

			allsumMakeferAmt = allsumMakeferAmt.add(makeferamt);
			allsumPrincipal = allsumPrincipal.add(principal);
			allsumInterest = allsumInterest.add(interest);
			allsumPayment = allsumPayment.add(payment);
			allsumDamages = allsumDamages.add(damages);
			allsumTemporaryLoan = allsumTemporaryLoan.add(temporaryloan);
			allsumCollection = allsumCollection.add(collection);
			allsumShortPayment = allsumShortPayment.add(shortpayment);
			allsumOthers = allsumOthers.add(others);

			// ??????????????????
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
						|| lastSortingForSubTotal.equals(" ")) {
					this.print(1, 2, "?????????");
				} else {
					this.print(1, 2, lastAcctItem);
				}
				this.print(0, 14, " ?????? ");

				atAll();

				totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
				totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
				totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
				totalsumInterest = totalsumInterest.add(allsumInterest);
				totalsumPayment = totalsumPayment.add(allsumPayment);
				totalsumDamages = totalsumDamages.add(allsumDamages);
				totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
				totalsumCollection = totalsumCollection.add(allsumCollection);
				totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
				totalsumOthers = totalsumOthers.add(allsumOthers);

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " ?????? ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = BigDecimal.ZERO;
				allsumMakeferAmt = BigDecimal.ZERO;
				allsumPrincipal = BigDecimal.ZERO;
				allsumInterest = BigDecimal.ZERO;
				allsumPayment = BigDecimal.ZERO;
				allsumDamages = BigDecimal.ZERO;
				allsumTemporaryLoan = BigDecimal.ZERO;
				allsumCollection = BigDecimal.ZERO;
				allsumShortPayment = BigDecimal.ZERO;
				allsumOthers = BigDecimal.ZERO;

				totalsumTransferAmt = BigDecimal.ZERO;
				totalsumMakerferAmt = BigDecimal.ZERO;
				totalsumPrincipal = BigDecimal.ZERO;
				totalsumInterest = BigDecimal.ZERO;
				totalsumPayment = BigDecimal.ZERO;
				totalsumDamages = BigDecimal.ZERO;
				totalsumTemporaryLoan = BigDecimal.ZERO;
				totalsumCollection = BigDecimal.ZERO;
				totalsumShortPayment = BigDecimal.ZERO;
				totalsumOthers = BigDecimal.ZERO;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====????????????=====", "C");
				this.print(2, this.getMidXAxis(), "???????????????????????????????????????????????????", "C");

				pageCnt = 0;
				npcount = 0;
				tround = 0;
			}

		} // for
	}

	private void report2(List<Map<String, String>> fnAllList, boolean isBatchMapList) throws LogicException {
		String lastSortingForSubTotal = ""; // ?????????SortingForSubTotal
		String lastAcctItem = ""; // ?????????AcctItem
		String msName = ""; // ??????P???
		String msNum = ""; // ????????????
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // ??????????????????(?????????????????????????????????????????????)

		HashMap<tmpFacm, BigDecimal> TxAmtMap = new HashMap<>();

		// ??????L4211?????????????????????TxAmt??????
		if ("L4211".equals(txCode)) {
			for (Map<String, String> tfnAllList : fnAllList) {

				tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
						parse.stringToInteger(tfnAllList.get("DetailSeq")),
						parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

				if (!TxAmtMap.containsKey(tmp)) {
					TxAmtMap.put(tmp, getBigDecimal(tfnAllList.get("TxAmt")));
				} else {
					TxAmtMap.put(tmp, TxAmtMap.get(tmp).add(getBigDecimal(tfnAllList.get("TxAmt"))));
				}
			}
		}
		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("TxAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);
//			String dfPrincipal = formatAmt("20846000", 0);
//			String dfInterest = formatAmt("2816142", 0);
//			String dfPayment = formatAmt("2650271", 0);
//			String dfDamages = formatAmt("2650271", 0);
//			String dfTemporaryLoan = formatAmt("524167", 0);
//			String dfCollection = formatAmt("15100000", 0);
//			String dfShortPayment = formatAmt("20000", 0);
//			String dfOthers = formatAmt("5415645", 0);

			transferamt = getBigDecimal(tfnAllList.get("TxAmt"));
			makeferamt = getBigDecimal(tfnAllList.get("AcctAmt"));
			principal = getBigDecimal(tfnAllList.get("Principal"));
			interest = getBigDecimal(tfnAllList.get("Interest"));
			payment = getBigDecimal(tfnAllList.get("TempPayAmt"));
			damages = getBigDecimal(tfnAllList.get("BreachAmt"));
			temporaryloan = getBigDecimal(tfnAllList.get("TempDr"));
			collection = getBigDecimal(tfnAllList.get("TempCr"));
			shortpayment = getBigDecimal(tfnAllList.get("Shortfall"));
			others = getBigDecimal(tfnAllList.get("Fee"));
			count++;

			// ??????????????????????????????????????????
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // ??????????????????
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
							|| lastSortingForSubTotal.equals(" ")) {
						this.print(1, 2, "?????????");
					} else {
						this.print(1, 2, lastAcctItem);
					}
					this.print(0, 14, " ?????? ");

					atAll();

					totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
					totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
					totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
					totalsumInterest = totalsumInterest.add(allsumInterest);
					totalsumPayment = totalsumPayment.add(allsumPayment);
					totalsumDamages = totalsumDamages.add(allsumDamages);
					totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
					totalsumCollection = totalsumCollection.add(allsumCollection);
					totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
					totalsumOthers = totalsumOthers.add(allsumOthers);

					allsumTransferAmt = BigDecimal.ZERO;
					allsumMakeferAmt = BigDecimal.ZERO;
					allsumPrincipal = BigDecimal.ZERO;
					allsumInterest = BigDecimal.ZERO;
					allsumPayment = BigDecimal.ZERO;
					allsumDamages = BigDecimal.ZERO;
					allsumTemporaryLoan = BigDecimal.ZERO;
					allsumCollection = BigDecimal.ZERO;
					allsumShortPayment = BigDecimal.ZERO;
					allsumOthers = BigDecimal.ZERO;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// ??????????????????

				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 18, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 18, A17, "C");// ????????????(??????)A1~A7 (P03?????????????????????????????????)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// ????????????????????????????????????
				if (tround > 0) {
					// ??????????????????????????????????????????
					String currentSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (!lastSortingForSubTotal.equals(currentSortingForSubTotal)) {
						this.info("currSort     = " + currentSortingForSubTotal);
						this.info("curracctItem = " + tfnAllList.get("AcctItem"));

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
								|| lastSortingForSubTotal.equals(" ")) {
							this.print(1, 2, "?????????");
						} else {
							this.print(1, 2, lastAcctItem);
						}
						this.print(0, 14, " ?????? ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
						totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
						totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
						totalsumInterest = totalsumInterest.add(allsumInterest);
						totalsumPayment = totalsumPayment.add(allsumPayment);
						totalsumDamages = totalsumDamages.add(allsumDamages);
						totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
						totalsumCollection = totalsumCollection.add(allsumCollection);
						totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
						totalsumOthers = totalsumOthers.add(allsumOthers);

						allsumTransferAmt = BigDecimal.ZERO;
						allsumMakeferAmt = BigDecimal.ZERO;
						allsumPrincipal = BigDecimal.ZERO;
						allsumInterest = BigDecimal.ZERO;
						allsumPayment = BigDecimal.ZERO;
						allsumDamages = BigDecimal.ZERO;
						allsumTemporaryLoan = BigDecimal.ZERO;
						allsumCollection = BigDecimal.ZERO;
						allsumShortPayment = BigDecimal.ZERO;
						allsumOthers = BigDecimal.ZERO;
					}

				}

				if (pageCnt >= 30) { // ??????40??????????????? ????????????????????????

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 18, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 18, tfnAllList.get("ReconCode"), "C");// ????????????(??????)A1~A7
																									// (P03?????????????????????????????????)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
					this.print(-8, 0, "");
				}
			} // else

			npcount++;
			tround++;

//            ??????????????????
			pageCnt++;

			// ??????????????????????????????????????? ??????????????? ????????????
			lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");

			lastAcctItem = tfnAllList.get("AcctItem");

			// ?????????????????????

			// ????????? * type = 1: yyy/mm/dd<BR>
			this.print(1, c1, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // ?????????????????? ???????????????
				this.print(0, c2, tfnAllList.get("DetailSeq"), "C");// ????????????

				if ("L4211".equals(txCode)) {

					tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
							parse.stringToInteger(tfnAllList.get("DetailSeq")),
							parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

					if (TxAmtMap.get(tmp) != null) {
						this.print(0, c3, formatAmt(TxAmtMap.get(tmp), 0), "R");// ????????????
					}

				} else {
					this.print(0, c3, dfTransferAmt, "R");// ????????????
				}

				allsumTransferAmt = allsumTransferAmt.add(transferamt);

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, c4, dfMakeferAmt, "R");// ????????????
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("PaidTerms");
			this.print(0, c5, custNo);// ??????
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// ??????
				name = name.substring(0, 5);
			}
			this.print(0, c6, name);
			this.print(0, c6 + 9, tfnAllList.get("CloseReasonCode"));

			if ("999/12/31".equals(showRocDate(tfnAllList.get("IntStartDate"), 1))) { // ?????????????????????????????????
				this.print(0, c7, "-" + showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????

			} else {
				this.print(0, c7, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
						+ showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????
			}

			if (dfPrincipal.equals("0")) {
				this.print(0, c8, "", "R"); // ??????
			} else {
				this.print(0, c8, dfPrincipal, "R"); // ??????
			}
			if (dfInterest.equals("0")) {
				this.print(0, c9, "", "R"); // ??????
			} else {
				this.print(0, c9, dfInterest, "R"); // ??????
			}
			if (dfPayment.equals("0")) {
				this.print(0, c10, "", "R"); // ?????????
			} else {
				this.print(0, c10, dfPayment, "R"); // ?????????
			}
			if (dfDamages.equals("0")) {
				this.print(0, c11, "", "R"); // ?????????
			} else {
				this.print(0, c11, dfDamages, "R"); // ?????????
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, c12, "", "R"); // ?????????
			} else {
				this.print(0, c12, dfTemporaryLoan, "R"); // ?????????
			}
			if (dfCollection.equals("0")) {
				this.print(0, c13, "", "R"); // ?????????
			} else {
				this.print(0, c13, dfCollection, "R"); // ?????????
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, c14, "", "R"); // ??????
			} else {
				this.print(0, c14, dfShortPayment, "R"); // ??????
			}
			if (dfOthers.equals("0")) {
				this.print(0, c15, "", "R"); // ??????????????????
			} else {
				this.print(0, c15, dfOthers, "R"); // ??????????????????
			}

			allsumMakeferAmt = allsumMakeferAmt.add(makeferamt);
			allsumPrincipal = allsumPrincipal.add(principal);
			allsumInterest = allsumInterest.add(interest);
			allsumPayment = allsumPayment.add(payment);
			allsumDamages = allsumDamages.add(damages);
			allsumTemporaryLoan = allsumTemporaryLoan.add(temporaryloan);
			allsumCollection = allsumCollection.add(collection);
			allsumShortPayment = allsumShortPayment.add(shortpayment);
			allsumOthers = allsumOthers.add(others);

			// ??????????????????
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
						|| lastSortingForSubTotal.equals(" ")) {
					this.print(1, 2, "?????????");
				} else {
					this.print(1, 2, lastAcctItem);
				}
				this.print(0, 14, " ?????? ");

				atAll();

				totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
				totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
				totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
				totalsumInterest = totalsumInterest.add(allsumInterest);
				totalsumPayment = totalsumPayment.add(allsumPayment);
				totalsumDamages = totalsumDamages.add(allsumDamages);
				totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
				totalsumCollection = totalsumCollection.add(allsumCollection);
				totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
				totalsumOthers = totalsumOthers.add(allsumOthers);

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " ?????? ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = BigDecimal.ZERO;
				allsumMakeferAmt = BigDecimal.ZERO;
				allsumPrincipal = BigDecimal.ZERO;
				allsumInterest = BigDecimal.ZERO;
				allsumPayment = BigDecimal.ZERO;
				allsumDamages = BigDecimal.ZERO;
				allsumTemporaryLoan = BigDecimal.ZERO;
				allsumCollection = BigDecimal.ZERO;
				allsumShortPayment = BigDecimal.ZERO;
				allsumOthers = BigDecimal.ZERO;

				totalsumTransferAmt = BigDecimal.ZERO;
				totalsumMakerferAmt = BigDecimal.ZERO;
				totalsumPrincipal = BigDecimal.ZERO;
				totalsumInterest = BigDecimal.ZERO;
				totalsumPayment = BigDecimal.ZERO;
				totalsumDamages = BigDecimal.ZERO;
				totalsumTemporaryLoan = BigDecimal.ZERO;
				totalsumCollection = BigDecimal.ZERO;
				totalsumShortPayment = BigDecimal.ZERO;
				totalsumOthers = BigDecimal.ZERO;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====????????????=====", "C");
				this.print(2, this.getMidXAxis(), "???????????????????????????????????????????????????", "C");
			}

		} // for
	}

	private void report3(List<Map<String, String>> fnAllList, boolean isBatchMapList) throws LogicException {
		String lastSortingForSubTotal = ""; // ?????????SortingForSubTotal
		String lastAcctItem = ""; // ?????????AcctItem
		String msName = ""; // ??????P???
		String msNum = ""; // ????????????
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;

		String scode = ""; // ??????????????????(?????????????????????????????????????????????)

		HashMap<tmpFacm, BigDecimal> TxAmtMap = new HashMap<>();

		// ??????L4211?????????????????????TxAmt??????
		if ("L4211".equals(txCode)) {

			for (Map<String, String> tfnAllList : fnAllList) {

				tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
						parse.stringToInteger(tfnAllList.get("DetailSeq")),
						parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

				if (!TxAmtMap.containsKey(tmp)) {
					TxAmtMap.put(tmp, getBigDecimal(tfnAllList.get("TxAmt")));
				} else {
					TxAmtMap.put(tmp, TxAmtMap.get(tmp).add(getBigDecimal(tfnAllList.get("TxAmt"))));
				}
			}
		}

		for (Map<String, String> tfnAllList : fnAllList) {

			String dfTransferAmt = formatAmt(tfnAllList.get("TxAmt"), 0);
			String dfMakeferAmt = formatAmt(tfnAllList.get("AcctAmt"), 0);
			String dfPrincipal = formatAmt(tfnAllList.get("Principal"), 0);
			String dfInterest = formatAmt(tfnAllList.get("Interest"), 0);
			String dfPayment = formatAmt(tfnAllList.get("TempPayAmt"), 0);
			String dfDamages = formatAmt(tfnAllList.get("BreachAmt"), 0);
			String dfTemporaryLoan = formatAmt(tfnAllList.get("TempDr"), 0);
			String dfCollection = formatAmt(tfnAllList.get("TempCr"), 0);
			String dfShortPayment = formatAmt(tfnAllList.get("Shortfall"), 0);
			String dfOthers = formatAmt(tfnAllList.get("Fee"), 0);
//			String dfPrincipal = formatAmt("20846000", 0);
//			String dfInterest = formatAmt("2816142", 0);
//			String dfPayment = formatAmt("2650271", 0);
//			String dfDamages = formatAmt("2650271", 0);
//			String dfTemporaryLoan = formatAmt("524167", 0);
//			String dfCollection = formatAmt("15100000", 0);
//			String dfShortPayment = formatAmt("20000", 0);
//			String dfOthers = formatAmt("5415645", 0);

			transferamt = getBigDecimal(tfnAllList.get("TxAmt"));
			makeferamt = getBigDecimal(tfnAllList.get("AcctAmt"));
			principal = getBigDecimal(tfnAllList.get("Principal"));
			interest = getBigDecimal(tfnAllList.get("Interest"));
			payment = getBigDecimal(tfnAllList.get("TempPayAmt"));
			damages = getBigDecimal(tfnAllList.get("BreachAmt"));
			temporaryloan = getBigDecimal(tfnAllList.get("TempDr"));
			collection = getBigDecimal(tfnAllList.get("TempCr"));
			shortpayment = getBigDecimal(tfnAllList.get("Shortfall"));
			others = getBigDecimal(tfnAllList.get("Fee"));
			count++;

			// ??????????????????????????????????????????
			if (!msName.equals(tfnAllList.get("ReconCode")) || !msNum.equals(tfnAllList.get("BatchNo"))) {

				if (npcount > 0) { // ??????????????????
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
							|| lastSortingForSubTotal.equals(" ")) {
						this.print(1, 2, "?????????");
					} else {
						this.print(1, 2, lastAcctItem);
					}
					this.print(0, 14, " ?????? ");

					atAll();

					totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
					totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
					totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
					totalsumInterest = totalsumInterest.add(allsumInterest);
					totalsumPayment = totalsumPayment.add(allsumPayment);
					totalsumDamages = totalsumDamages.add(allsumDamages);
					totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
					totalsumCollection = totalsumCollection.add(allsumCollection);
					totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
					totalsumOthers = totalsumOthers.add(allsumOthers);

					allsumTransferAmt = BigDecimal.ZERO;
					allsumMakeferAmt = BigDecimal.ZERO;
					allsumPrincipal = BigDecimal.ZERO;
					allsumInterest = BigDecimal.ZERO;
					allsumPayment = BigDecimal.ZERO;
					allsumDamages = BigDecimal.ZERO;
					allsumTemporaryLoan = BigDecimal.ZERO;
					allsumCollection = BigDecimal.ZERO;
					allsumShortPayment = BigDecimal.ZERO;
					allsumOthers = BigDecimal.ZERO;

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0;
					tround = 0;
				} // if

				// ??????????????????

				String A17 = tfnAllList.get("ReconCode");
				if (A17.equals("P03")) {
					this.print(-3, this.getMidXAxis() + 16, "A7", "C");
				} else {
					this.print(-3, this.getMidXAxis() + 16, A17, "C");// ????????????(??????)A1~A7 (P03?????????????????????????????????)
				}
				this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
				this.print(-8, 0, "");

				msName = tfnAllList.get("ReconCode");
				msNum = tfnAllList.get("BatchNo");
			} else {
				// ????????????????????????????????????
				if (tround > 0) {
					// ??????????????????????????????????????????
					String currentSortingForSubTotal = tfnAllList.get("SortingForSubTotal");
					if (!lastSortingForSubTotal.equals(currentSortingForSubTotal)) {
						this.info("currSort     = " + currentSortingForSubTotal);
						this.info("curracctItem = " + tfnAllList.get("AcctItem"));

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
								|| lastSortingForSubTotal.equals(" ")) {
							this.print(1, 2, "?????????");
						} else {
							this.print(1, 2, lastAcctItem);
						}
						this.print(0, 14, " ?????? ");

						atAll();

						this.print(1, 0, "");

						pageCnt = pageCnt + 2;

						totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
						totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
						totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
						totalsumInterest = totalsumInterest.add(allsumInterest);
						totalsumPayment = totalsumPayment.add(allsumPayment);
						totalsumDamages = totalsumDamages.add(allsumDamages);
						totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
						totalsumCollection = totalsumCollection.add(allsumCollection);
						totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
						totalsumOthers = totalsumOthers.add(allsumOthers);

						allsumTransferAmt = BigDecimal.ZERO;
						allsumMakeferAmt = BigDecimal.ZERO;
						allsumPrincipal = BigDecimal.ZERO;
						allsumInterest = BigDecimal.ZERO;
						allsumPayment = BigDecimal.ZERO;
						allsumDamages = BigDecimal.ZERO;
						allsumTemporaryLoan = BigDecimal.ZERO;
						allsumCollection = BigDecimal.ZERO;
						allsumShortPayment = BigDecimal.ZERO;
						allsumOthers = BigDecimal.ZERO;
					}

				}

				if (pageCnt >= 30) { // ??????40??????????????? ????????????????????????

					this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====?????????=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("ReconCode").equals("P03")) {
						this.print(-3, this.getMidXAxis() + 16, "A7", "C");
					} else {
						this.print(-3, this.getMidXAxis() + 16, tfnAllList.get("ReconCode"), "C");// ????????????(??????)A1~A7
																									// (P03?????????????????????????????????)
					}
					this.print(-5, 15, tfnAllList.get("BatchNo"));// ????????????(??????)
					this.print(-8, 0, "");
				}
			} // else

			npcount++;
			tround++;

//            ??????????????????
			pageCnt++;

			// ??????????????????????????????????????? ??????????????? ????????????
			lastSortingForSubTotal = tfnAllList.get("SortingForSubTotal");

			lastAcctItem = tfnAllList.get("AcctItem");

			// ?????????????????????

			// ????????? * type = 1: yyy/mm/dd<BR>
			this.print(1, c1, showRocDate((tfnAllList.get("EntryDate")), 1));

			if (!scode.equals(tfnAllList.get("DetailSeq"))) { // ?????????????????? ???????????????
				this.print(0, c2, tfnAllList.get("DetailSeq"), "C");// ????????????

				if ("L4211".equals(txCode)) {
					tmpFacm tmp = new tmpFacm(parse.stringToInteger(tfnAllList.get("CustNo").substring(0, 7)),
							parse.stringToInteger(tfnAllList.get("DetailSeq")),
							parse.stringToInteger(tfnAllList.get("SortingForSubTotal")));

					if (TxAmtMap.get(tmp) != null) {
						this.print(0, c3, formatAmt(TxAmtMap.get(tmp), 0), "R");// ????????????
					}
				} else {
					this.print(0, c3, dfTransferAmt, "R");// ????????????
				}

				allsumTransferAmt = allsumTransferAmt.add(transferamt);

				scode = tfnAllList.get("DetailSeq");

			}

			this.print(0, c4, dfMakeferAmt, "R");// ????????????
			String custNo = tfnAllList.get("CustNo");
			custNo += isBatchMapList ? "-" : " ";
			custNo += tfnAllList.get("PaidTerms");
			this.print(0, c5, custNo);// ??????
			String name = tfnAllList.get("CustName");
			if (name.length() > 5) {// ??????
				name = name.substring(0, 5);
			}
			this.print(0, c6, name);
			this.print(0, c6 + 9, tfnAllList.get("CloseReasonCode"));

			if ("999/12/31".equals(showRocDate(tfnAllList.get("IntStartDate"), 1))) { // ?????????????????????????????????
				this.print(0, c7, "-" + showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????

			} else {
				this.print(0, c7, showRocDate(tfnAllList.get("IntStartDate"), 1) + "-"
						+ showRocDate(tfnAllList.get("IntEndDate"), 1));// ???????????????
			}

			if (dfPrincipal.equals("0")) {
				this.print(0, c8, "", "R"); // ??????
			} else {
				this.print(0, c8, dfPrincipal, "R"); // ??????
			}
			if (dfInterest.equals("0")) {
				this.print(0, c9, "", "R"); // ??????
			} else {
				this.print(0, c9, dfInterest, "R"); // ??????
			}
			if (dfPayment.equals("0")) {
				this.print(0, c10, "", "R"); // ?????????
			} else {
				this.print(0, c10, dfPayment, "R"); // ?????????
			}
			if (dfDamages.equals("0")) {
				this.print(0, c11, "", "R"); // ?????????
			} else {
				this.print(0, c11, dfDamages, "R"); // ?????????
			}
			if (dfTemporaryLoan.equals("0")) {
				this.print(0, c12, "", "R"); // ?????????
			} else {
				this.print(0, c12, dfTemporaryLoan, "R"); // ?????????
			}
			if (dfCollection.equals("0")) {
				this.print(0, c13, "", "R"); // ?????????
			} else {
				this.print(0, c13, dfCollection, "R"); // ?????????
			}
			if (dfShortPayment.equals("0")) {
				this.print(0, c14, "", "R"); // ??????
			} else {
				this.print(0, c14, dfShortPayment, "R"); // ??????
			}
			if (dfOthers.equals("0")) {
				this.print(0, c15, "", "R"); // ??????????????????
			} else {
				this.print(0, c15, dfOthers, "R"); // ??????????????????
			}

			allsumMakeferAmt = allsumMakeferAmt.add(makeferamt);
			allsumPrincipal = allsumPrincipal.add(principal);
			allsumInterest = allsumInterest.add(interest);
			allsumPayment = allsumPayment.add(payment);
			allsumDamages = allsumDamages.add(damages);
			allsumTemporaryLoan = allsumTemporaryLoan.add(temporaryloan);
			allsumCollection = allsumCollection.add(collection);
			allsumShortPayment = allsumShortPayment.add(shortpayment);
			allsumOthers = allsumOthers.add(others);

			// ??????????????????
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if (lastSortingForSubTotal.equals("999") || lastSortingForSubTotal.equals("")
						|| lastSortingForSubTotal.equals(" ")) {
					this.print(1, 2, "?????????");
				} else {
					this.print(1, 2, lastAcctItem);
				}
				this.print(0, 14, " ?????? ");

				atAll();

				totalsumTransferAmt = totalsumTransferAmt.add(allsumTransferAmt);
				totalsumMakerferAmt = totalsumMakerferAmt.add(allsumMakeferAmt);
				totalsumPrincipal = totalsumPrincipal.add(allsumPrincipal);
				totalsumInterest = totalsumInterest.add(allsumInterest);
				totalsumPayment = totalsumPayment.add(allsumPayment);
				totalsumDamages = totalsumDamages.add(allsumDamages);
				totalsumTemporaryLoan = totalsumTemporaryLoan.add(allsumTemporaryLoan);
				totalsumCollection = totalsumCollection.add(allsumCollection);
				totalsumShortPayment = totalsumShortPayment.add(allsumShortPayment);
				totalsumOthers = totalsumOthers.add(allsumOthers);

				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " ?????? ");

				totalAll();
				pageCnt = pageCnt + 4;
				allsumTransferAmt = BigDecimal.ZERO;
				allsumMakeferAmt = BigDecimal.ZERO;
				allsumPrincipal = BigDecimal.ZERO;
				allsumInterest = BigDecimal.ZERO;
				allsumPayment = BigDecimal.ZERO;
				allsumDamages = BigDecimal.ZERO;
				allsumTemporaryLoan = BigDecimal.ZERO;
				allsumCollection = BigDecimal.ZERO;
				allsumShortPayment = BigDecimal.ZERO;
				allsumOthers = BigDecimal.ZERO;

				totalsumTransferAmt = BigDecimal.ZERO;
				totalsumMakerferAmt = BigDecimal.ZERO;
				totalsumPrincipal = BigDecimal.ZERO;
				totalsumInterest = BigDecimal.ZERO;
				totalsumPayment = BigDecimal.ZERO;
				totalsumDamages = BigDecimal.ZERO;
				totalsumTemporaryLoan = BigDecimal.ZERO;
				totalsumCollection = BigDecimal.ZERO;
				totalsumShortPayment = BigDecimal.ZERO;
				totalsumOthers = BigDecimal.ZERO;

				this.print(pageIndex - pageCnt - 2, this.getMidXAxis(), "=====????????????=====", "C");
				this.print(2, this.getMidXAxis(), "???????????????????????????????????????????????????", "C");
			}

		} // for
	}

	private void atAll() {

		if (allsumTransferAmt.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c3, formatAmt(allsumTransferAmt, 0), "R");
		}

		if (allsumMakeferAmt.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c4, formatAmt(allsumMakeferAmt, 0), "R");
		}

		if (allsumPrincipal.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c8, formatAmt(allsumPrincipal, 0), "R");
		}

		if (allsumPayment.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c10, formatAmt(allsumPayment, 0), "R");
		}

		if (allsumDamages.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c11, formatAmt(allsumDamages, 0), "R");
		}

		if (allsumTemporaryLoan.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c12, formatAmt(allsumTemporaryLoan, 0), "R");
		}

		if (allsumShortPayment.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c14, formatAmt(allsumShortPayment, 0), "R");
		}

		if (allsumOthers.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c15, formatAmt(allsumOthers, 0), "R");
		}

		if (allsumInterest.compareTo(BigDecimal.ZERO) != 0) {
			this.print(1, c9, formatAmt(allsumInterest, 0), "R");
		}

		if (allsumCollection.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c13, formatAmt(allsumCollection, 0), "R");
		}

	}

	private void totalAll() {

		if (totalsumTransferAmt.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c3, formatAmt(totalsumTransferAmt, 0), "R");
		}

		if (totalsumMakerferAmt.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c4, formatAmt(totalsumMakerferAmt, 0), "R");
		}
		if (totalsumPrincipal.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c8, formatAmt(totalsumPrincipal, 0), "R");
		}

		if (totalsumPayment.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c10, formatAmt(totalsumPayment, 0), "R");
		}

		if (totalsumDamages.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c11, formatAmt(totalsumDamages, 0), "R");
		}

		if (totalsumTemporaryLoan.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c12, formatAmt(totalsumTemporaryLoan, 0), "R");
		}

		if (totalsumShortPayment.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c14, formatAmt(totalsumShortPayment, 0), "R");
		}

		if (totalsumOthers.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c15, formatAmt(totalsumOthers, 0), "R");
		}

		if (totalsumInterest.compareTo(BigDecimal.ZERO) != 0) {
			this.print(1, c9, formatAmt(totalsumInterest, 0), "R");
		}

		if (totalsumCollection.compareTo(BigDecimal.ZERO) != 0) {
			this.print(0, c13, formatAmt(totalsumCollection, 0), "R");
		}

	}

//	????????????????????????
	/**
	 * 
	 * @author custNo <br>
	 *         sortingForSubTotal <br>
	 *
	 */
	private class tmpFacm {

		public tmpFacm(int custNo, int detailSeq, int sortingForSubTotal) {
			this.setCustNo(custNo);
			this.setDetailSeq(detailSeq);
			this.setSortingForSubTotal(sortingForSubTotal);
		}

		private int custNo = 0;
		private int detailSeq = 0;
		private int sortingForSubTotal = 0;

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", detailSeq=" + detailSeq + ", sortingForSubTotal="
					+ sortingForSubTotal + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + detailSeq;
			result = prime * result + sortingForSubTotal;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (detailSeq != other.detailSeq)
				return false;
			if (sortingForSubTotal != other.sortingForSubTotal)
				return false;
			return true;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private void setDetailSeq(int detailSeq) {
			this.detailSeq = detailSeq;
		}

		private void setSortingForSubTotal(int sortingForSubTotal) {
			this.sortingForSubTotal = sortingForSubTotal;
		}

		private L4211Report getEnclosingInstance() {
			return L4211Report.this;
		}
	}

}
