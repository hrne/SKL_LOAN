package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4962ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.http.WebClient;

@Service("L4962Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4962Batch extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public CollListService collListService;

	@Autowired
	public L4962ServiceImpl l4962ServiceImpl;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	SortMapListCom sortMapListCom;

	private int row = 1; // 列數:記錄印到第幾列

	List<Map<String, String>> resultListC = new ArrayList<Map<String, String>>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4962 ");
		this.totaVo.init(titaVo);

		String flagA = titaVo.getParam("FlagA");
		String flagB = titaVo.getParam("FlagB");
		String CommericalFlag = titaVo.getParam("CommericalFlag");
		String flagD = titaVo.getParam("FlagD");

//		A.保費、保單未完成檢核表
		if ("Y".equals(flagA)) {
			flagA(titaVo);
		}

//		B.額度無保單檢核表
		if ("Y".equals(flagB)) {
			flagB(titaVo);
		}

//      C.險種註記明細表
		if (!"N".equals(CommericalFlag)) {
			flagC(titaVo);
		}

//      D.擔保品無保險單
		if ("Y".equals(flagD)) {
			flagD(titaVo);
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4962", "L4962保險單資料檢核作業完成", titaVo);

		this.addList(totaVo);
		return this.sendList();
	}

//	A.保費、保單未完成檢核表
//	年月 原保單號碼 戶號 額度 借款人 押品號碼 序號?? 新保單號碼 未完成狀況
//		1.無新保單號碼 : 無新保單號碼
//		2.保費未入帳 : 有新保單號碼，無會計日
	private void flagA(TitaVo titaVo) throws LogicException {
		this.info("L4962Batch flagA");
		int iInsuEndMonthFrom = parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom")) + 191100;
		int iInsuEndMonthTo = parse.stringToInteger(titaVo.getParam("InsuEndMonthTo")) + 191100;

		Slice<InsuRenew> sInsuRenew = insuRenewService.findL4962A(iInsuEndMonthFrom, iInsuEndMonthTo, 0,
				Integer.MAX_VALUE, titaVo);

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4962";
		String fileItem = "保費、保單未完成檢核表";
		if (sInsuRenew == null || sInsuRenew.isEmpty()) {
			fileItem = "保費、保單未完成檢核表(查無資料)";
		}

		String fileName = "L4962-保費、保單未完成檢核表";
		row = 1; // 列數:記錄印到第幾列
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		if (sInsuRenew == null || sInsuRenew.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料", "L");
		} else {
			this.info("sInsuRenew =" + sInsuRenew.toString());
			// 調整欄寬
			makeExcel.setWidth(1, 10);
			makeExcel.setWidth(2, 24);
			makeExcel.setWidth(3, 10);
			makeExcel.setWidth(4, 10);
			makeExcel.setWidth(5, 24);
			makeExcel.setWidth(6, 12);
			makeExcel.setWidth(7, 24);
			makeExcel.setWidth(8, 24);
			makeExcel.setWidth(9, 24);

			ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
			headerStyleVo.setBold(true);
			// 表頭
			// 年月、原保單號碼、戶號、額度、戶名、戶況、新保單號碼、未完成原因狀況 擔保品號碼保留
			makeExcel.setValue(row, 1, "年月", "C", headerStyleVo);
			makeExcel.setValue(row, 2, "原保單號碼", "C", headerStyleVo);
			makeExcel.setValue(row, 3, "戶號", "C", headerStyleVo);
			makeExcel.setValue(row, 4, "額度", "C", headerStyleVo);
			makeExcel.setValue(row, 5, "戶名", "C", headerStyleVo);
			makeExcel.setValue(row, 6, "戶況", "C", headerStyleVo);
			makeExcel.setValue(row, 7, "新保單號碼", "C", headerStyleVo);
			makeExcel.setValue(row, 8, "擔保品號碼", "C", headerStyleVo);
			makeExcel.setValue(row, 9, "未完成原因狀況", "C", headerStyleVo);

			row++;

			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {

				if (tInsuRenew.getRenewCode() == 2) {
					if (tInsuRenew.getAcDate() == 0) {
						exportExcelA(tInsuRenew, 2, titaVo);
					} else if (tInsuRenew.getNowInsuNo() == null || "".equals(tInsuRenew.getNowInsuNo().trim())) {
						exportExcelA(tInsuRenew, 1, titaVo);
					} else {
						exportExcelA(tInsuRenew, 0, titaVo);
					}
				}

			}

		}

		makeExcel.close();
	}

	private void exportExcelA(InsuRenew tInsuRenew, int errorFlag, TitaVo titaVo) throws LogicException {
		this.info("L4962 exportExcelA");

		String colStatus = "99";
		CollList tCollList = collListService.findById(new CollListId(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo()),
				titaVo);
		if (tCollList != null) {
			colStatus = parse.IntegerToString(tCollList.getStatus(), 2);
		}
		if ("4".equals(colStatus)) {
			colStatus = "00";
		}

		CdCode tCdCode = cdCodeService.findById(new CdCodeId("ColStatus", colStatus), titaVo);
		String colStatusX = "";
		if (tCdCode != null) {
			colStatusX = tCdCode.getItem();
		}

//		cntA = cntA + 1;
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

		// 擔保品號碼
		String clno = tInsuRenew.getClCode1() + "-" + tInsuRenew.getClCode2() + "-" + tInsuRenew.getClNo();

		// 年月、原保單號碼、戶號、額度、戶名、戶況、新保單號碼、未完成原因狀況 擔保品號碼保留

		makeExcel.setValue(row, 1, tInsuRenew.getInsuYearMonth() > 191100 ? tInsuRenew.getInsuYearMonth() - 191100
				: tInsuRenew.getInsuYearMonth(), "C"); // 年月
		makeExcel.setValue(row, 2, tInsuRenew.getPrevInsuNo(), "C"); // 原保單號碼
		makeExcel.setValue(row, 3, tInsuRenew.getCustNo(), "C"); // 戶號
		makeExcel.setValue(row, 4, tInsuRenew.getFacmNo(), "C"); // 額度
		makeExcel.setValue(row, 5, StringCut.replaceLineUp(tCustMain.getCustName()), "L"); // 戶名
		makeExcel.setValue(row, 6, colStatusX, "L"); // 戶況
		makeExcel.setValue(row, 7, tInsuRenew.getNowInsuNo(), "C"); // 新保單號碼
		makeExcel.setValue(row, 8, clno, "L"); // 擔保品號碼

		// 未完成原因
		switch (errorFlag) {
		case 1:
			makeExcel.setValue(row, 9, "無新保單號碼");
			break;
		case 2:
			makeExcel.setValue(row, 9, "保費未入帳");
			break;
		default:
			makeExcel.setValue(row, 9, "");
			break;
		}

		row++;

	}

//	B.額度無保單檢核表
//	戶號 額度 借款人 首撥日 押品號碼 保單號碼(最末) 保險起日 保險迄日 說明
//    有撥款且未結案：
//		1.保單資料已到期 
//		2.無保單資料 	
	private void flagB(TitaVo titaVo) throws LogicException {
		this.info("L4962Batch flagB");

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l4962ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error("l4962ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		exportExcelB(titaVo, resultList);// 產excel檔

	}

//	B.額度無保單檢核表
	private void exportExcelB(TitaVo titaVo, List<Map<String, String>> resultList) throws LogicException {
		this.info("L4962 exportExcelB");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4962";
		String fileItem = "額度無保單檢核表";

		if (resultList == null || resultList.isEmpty()) {
			fileItem = "額度無保單檢核表(查無資料)";
		}
		String fileName = "L4962-額度無保單檢核表";
		row = 1; // 列數:記錄印到第幾列
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		// 調整欄寬
		makeExcel.setWidth(1, 10);
		makeExcel.setWidth(2, 10);
		makeExcel.setWidth(3, 10);
		makeExcel.setWidth(4, 28);
		makeExcel.setWidth(5, 12);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 24);
		makeExcel.setWidth(8, 24);
		makeExcel.setWidth(9, 12);
		makeExcel.setWidth(10, 12);
		makeExcel.setWidth(11, 20);

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);
		// 表頭
		makeExcel.setValue(row, 1, "年月", "C", headerStyleVo);
		makeExcel.setValue(row, 2, "戶號", "C", headerStyleVo);
		makeExcel.setValue(row, 3, "額度", "C", headerStyleVo);
		makeExcel.setValue(row, 4, "戶名", "C", headerStyleVo);
		makeExcel.setValue(row, 5, "戶況", "C", headerStyleVo);
		makeExcel.setValue(row, 6, "撥款日", "C", headerStyleVo);
		makeExcel.setValue(row, 7, "原保單號碼", "C", headerStyleVo);
		makeExcel.setValue(row, 8, "擔保品號碼", "C", headerStyleVo);
		makeExcel.setValue(row, 9, "保險起日", "C", headerStyleVo);
		makeExcel.setValue(row, 10, "保險迄日", "C", headerStyleVo);
		makeExcel.setValue(row, 11, "原因狀況", "C", headerStyleVo);
		row++;
		
		int insuStartDate = 0;
		int iInsuEndMonthFrom2 = 0;
		if (parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom2")) > 0) {
			iInsuEndMonthFrom2 = parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom2")) + 191100;
			insuStartDate = parse.stringToInteger(iInsuEndMonthFrom2 + "01");
		} else {
			insuStartDate = (titaVo.getEntDyI() + 19110000);// 會計日
		}

		// 明細
		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料", "L");
		} else {

			for (Map<String, String> result : resultList) {
				String note = "";
				int startDate = parse.stringToInteger(result.get("F8"));
				int endDate = parse.stringToInteger(result.get("F9"));

				if (result.get("F10") == null || "".equals(result.get("F10").trim())) {
					note = "無保單資料";
				} else {
					if (endDate < insuStartDate) {
						note = "保單資料已到期";
					}

				}

				if (!"".equals(note)) {
					if (startDate > 19110000) {
						startDate = startDate - 19110000;
					}

					if (endDate > 19110000) {
						endDate = endDate - 19110000;
					}

					int drDwDate = parse.stringToInteger(result.get("F3"));

					if (drDwDate > 19110000) {
						drDwDate = drDwDate - 19110000;
					}
					int custNo = parse.stringToInteger(result.get("F0"));
					int facmNo = parse.stringToInteger(result.get("F1"));

					int iInsuYearMonth = parse.stringToInteger(result.get("F12"));
					if (iInsuYearMonth > 191100) {
						iInsuYearMonth = iInsuYearMonth - 191100;
					}

					// 擔保品號碼
					String clno = result.get("F4") + "-" + result.get("F5") + "-" + result.get("F6");

					makeExcel.setValue(row, 1, iInsuYearMonth, "C"); // 年月
					makeExcel.setValue(row, 2, custNo, "C"); // 戶號
					makeExcel.setValue(row, 3, facmNo, "C"); // 額度
					makeExcel.setValue(row, 4, StringCut.replaceLineUp(result.get("F2")), "L"); // 戶名
					makeExcel.setValue(row, 5, result.get("F11"), "L"); // 戶況
					makeExcel.setValue(row, 6, drDwDate, "C"); // 撥款日
					makeExcel.setValue(row, 7, result.get("F7"), "L"); // 原保單號碼
					makeExcel.setValue(row, 8, clno, "L"); // 擔保品號碼
					makeExcel.setValue(row, 9, startDate, "C"); // 保險起日
					makeExcel.setValue(row, 10, endDate, "C"); // 保險迄日
					makeExcel.setValue(row, 11, note, "L"); // 原因狀況

					row++;

				}

			}

			// 排除有資料且到期，不列印資料的情況
			if (row == 2) {
				makeExcel.setValue(2, 1, "本日無資料", "L");
			}
		}

		makeExcel.close();

	}

	private void flagC(TitaVo titaVo) throws LogicException {
		this.info("L4962Batch flagC");
		int iInsuEndMonthFrom1 = parse.stringToInteger(titaVo.getParam("InsuEndMonthFrom1")) + 191100;
		int iInsuEndMonthTo1 = parse.stringToInteger(titaVo.getParam("InsuEndMonthTo1")) + 191100;
		String CommericalFlag = titaVo.getParam("CommericalFlag");
		Slice<InsuRenew> sInsuRenew = insuRenewService.findL4962A(iInsuEndMonthFrom1, iInsuEndMonthTo1, 0,
				Integer.MAX_VALUE, titaVo);

		if (sInsuRenew != null) {
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {
				// 險種註記不為全部時 篩出特定種類資料
				if (!"00".equals(CommericalFlag)) {
					if (!CommericalFlag.equals(tInsuRenew.getCommericalFlag())) {
						continue;
					}
				}

				if (!"".equals(tInsuRenew.getCommericalFlag())) { // 00 全部
					exportExcelC1(tInsuRenew, titaVo);
				}
			}
		}

		iInsuEndMonthFrom1 = iInsuEndMonthFrom1 * 100 + 1;
		iInsuEndMonthTo1 = iInsuEndMonthTo1 * 100 + 31;
		Slice<InsuOrignal> sInsuOrignal = insuOrignalService.insuEndDateRange(iInsuEndMonthFrom1, iInsuEndMonthTo1, 0,
				Integer.MAX_VALUE, titaVo);
		if (sInsuOrignal != null) {

			for (InsuOrignal tInsuOrignal : sInsuOrignal.getContent()) {

				// 險種註記不為全部時 篩出特定種類資料
				if (!"00".equals(CommericalFlag)) {
					if (!CommericalFlag.equals(tInsuOrignal.getCommericalFlag())) {
						continue;
					}
				}

				if (!"".equals(tInsuOrignal.getCommericalFlag())) {
					exportExcelC2(tInsuOrignal, titaVo);
				}

			}
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4962";
		String fileItem = "住宅險改商業險註記表";

		if (resultListC == null || resultListC.isEmpty()) {
			fileItem = "住宅險改商業險註記表(查無資料)";
		}

		String fileName = "L4962-住宅險改商業險註記表";
		row = 1; // 列數:記錄印到第幾列
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		if (resultListC == null || resultListC.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料", "L");
		} else {
			// 調整欄寬
			makeExcel.setWidth(1, 10);
			makeExcel.setWidth(2, 10);
			makeExcel.setWidth(3, 10);
			makeExcel.setWidth(4, 24);
			makeExcel.setWidth(5, 24);
			makeExcel.setWidth(6, 10);
			makeExcel.setWidth(7, 10);
			makeExcel.setWidth(8, 24);

			ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
			headerStyleVo.setBold(true);
			// 表頭
			// 年月、戶號、額度、戶名、保單號碼、保險起日、保險迄日、險種註記
			makeExcel.setValue(row, 1, "年月", "C", headerStyleVo);
			makeExcel.setValue(row, 2, "戶號", "C", headerStyleVo);
			makeExcel.setValue(row, 3, "額度", "C", headerStyleVo);
			makeExcel.setValue(row, 4, "戶名", "C", headerStyleVo);
			makeExcel.setValue(row, 5, "保單號碼", "C", headerStyleVo);
			makeExcel.setValue(row, 6, "保險起日", "C", headerStyleVo);
			makeExcel.setValue(row, 7, "保險迄日", "C", headerStyleVo);
			makeExcel.setValue(row, 8, "險種註記", "C", headerStyleVo);

			row++;

			resultListC.sort((c1, c2) -> {
				int result = 0;
				if (c1.get("YearMonth") != c2.get("YearMonth")) {
					result = Integer.valueOf(c1.get("YearMonth").compareTo(c2.get("YearMonth")));
				} else {
					result = 0;
				}

				return result;
			});

			for (Map<String, String> r : resultListC) {

				makeExcel.setValue(row, 1, r.get("YearMonth"), "C");
				; // 年月
				makeExcel.setValue(row, 2, r.get("CustNo"), "C"); // 戶號
				makeExcel.setValue(row, 3, r.get("FacmNo"), "C"); // 額度
				makeExcel.setValue(row, 4, r.get("CustName"), "L"); // 戶名
				makeExcel.setValue(row, 5, r.get("PrevInsuNo"), "C"); // 原保單號碼
				makeExcel.setValue(row, 6, r.get("InsuStartDate"), "L"); // 保險起日
				makeExcel.setValue(row, 7, r.get("InsuEndDate"), "L"); // 保險迄日
				makeExcel.setValue(row, 8, r.get("CommericalFlagX"), "L"); // 險種註記
				row++;

			}
		}

		makeExcel.close();

	}

//	C.住宅險改商業險註記表
	private void exportExcelC1(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("L4962 exportExcelC1");

		Map<String, String> data = new HashMap<String, String>();

		CollList tCollList = collListService.findById(new CollListId(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo()),
				titaVo);
		if (tCollList == null) {
			return;
		}
		int colStatus = tCollList.getStatus();

		if (colStatus == 0 || colStatus == 2 || colStatus == 4 || colStatus == 7) {
		} else {
			return;
		}

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

		CdCode tCdCode = cdCodeService.findById(new CdCodeId("CommericalFlag", tInsuRenew.getCommericalFlag()), titaVo);
		String CommericalFlagX = "";
		if (tCdCode != null) {
			CommericalFlagX = tCdCode.getItem();
		}
		this.info("InsuRenew_CommericalFlag = " + tInsuRenew.getCommericalFlag());
		this.info("InsuRenew_CommericalFlagX = " + CommericalFlagX);

		// 年月、戶號、額度、戶名、保單號碼、保險起日、保險迄日、險種註記

		data.put("YearMonth",
				tInsuRenew.getInsuYearMonth() > 191100 ? String.valueOf(tInsuRenew.getInsuYearMonth() - 191100)
						: String.valueOf(tInsuRenew.getInsuYearMonth())); // 年月
		data.put("CustNo", String.valueOf(tInsuRenew.getCustNo()));// 戶號
		data.put("FacmNo", String.valueOf(tInsuRenew.getFacmNo()));// 額度
		data.put("CustName", StringCut.replaceLineUp(tCustMain.getCustName()));// 戶名
		data.put("PrevInsuNo", String.valueOf(tInsuRenew.getPrevInsuNo()));// 原保單號碼
		data.put("InsuStartDate", String.valueOf(tInsuRenew.getInsuStartDate()));// 保險起日
		data.put("InsuEndDate", String.valueOf(tInsuRenew.getInsuEndDate()));// 保險迄日
		data.put("CommericalFlagX", CommericalFlagX);// 險種註記

		resultListC.add(data);

	}

//	C.住宅險改商業險註記表2
	private void exportExcelC2(InsuOrignal tInsuOrignal, TitaVo titaVo) throws LogicException {
		this.info("L4962 exportExcelC2");

		Map<String, String> data = new HashMap<String, String>();

		Slice<ClFac> sClFac = clFacService.clNoEq(tInsuOrignal.getClCode1(), tInsuOrignal.getClCode2(),
				tInsuOrignal.getClNo(), this.index, this.limit, titaVo);

		if (sClFac == null) {
			return;
		}
		int custno = 0;
		int facmno = 0;
		List<ClFac> lClFac = sClFac.getContent();
		custno = lClFac.get(0).getCustNo();
		facmno = lClFac.get(0).getFacmNo();
		String custname = "";
		CustMain tCustMain = custMainService.custNoFirst(custno, custno, titaVo);
		if (tCustMain != null) {
			custname = tCustMain.getCustName();
		}

		CollList tCollList = collListService.findById(new CollListId(custno, facmno), titaVo);
		if (tCollList == null) {
			return;
		}
		int colStatus = tCollList.getStatus();
		if (colStatus == 0 || colStatus == 2 || colStatus == 4 || colStatus == 7) {
		} else {
			return;
		}

		CdCode tCdCode = cdCodeService.findById(new CdCodeId("CommericalFlag", tInsuOrignal.getCommericalFlag()),
				titaVo);
		String CommericalFlagX = "";
		if (tCdCode != null) {
			CommericalFlagX = tCdCode.getItem();
		}
		this.info("InsuOrignal_CommericalFlag = " + tInsuOrignal.getCommericalFlag());
		this.info("InsuOrignal_CommericalFlagX = " + CommericalFlagX);

		// 年月、戶號、額度、戶名、保單號碼、保險起日、保險迄日、險種註記
		data.put("YearMonth", String.valueOf(tInsuOrignal.getInsuEndDate() / 100)); // 年月
		data.put("CustNo", String.valueOf(custno));// 戶號
		data.put("FacmNo", String.valueOf(facmno));// 額度
		data.put("CustName", StringCut.replaceLineUp(custname));// 戶名
		data.put("PrevInsuNo", String.valueOf(tInsuOrignal.getOrigInsuNo()));// 原保單號碼
		data.put("InsuStartDate", String.valueOf(tInsuOrignal.getInsuStartDate()));// 保險起日
		data.put("InsuEndDate", String.valueOf(tInsuOrignal.getInsuEndDate()));// 保險迄日
		data.put("CommericalFlagX", CommericalFlagX);// 險種註記

		resultListC.add(data);

	}

//	D.擔保品無保險單檢核表
	private void flagD(TitaVo titaVo) throws LogicException {
		this.info("L4962Batch flagD");
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultList = l4962ServiceImpl.findNoInsuCL(titaVo);
		} catch (Exception e) {
			this.error("l4962ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4962";
		String fileItem = "擔保品無保險單檢核表";

		if (resultList == null || resultList.isEmpty()) {
			fileItem = "擔保品無保險單(查無資料)";
		}
		String fileName = "L4962-擔保品無保險單檢核表";
		row = 1; // 列數:記錄印到第幾列
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		// 調整欄寬
		makeExcel.setWidth(1, 10);
		makeExcel.setWidth(2, 10);
		makeExcel.setWidth(3, 28);
		makeExcel.setWidth(4, 12);
		makeExcel.setWidth(5, 24);

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);
		// 表頭
		makeExcel.setValue(row, 1, "戶號", "C", headerStyleVo);
		makeExcel.setValue(row, 2, "額度", "C", headerStyleVo);
		makeExcel.setValue(row, 3, "戶名", "C", headerStyleVo);
		makeExcel.setValue(row, 4, "戶況", "C", headerStyleVo);
		makeExcel.setValue(row, 5, "擔保品號碼", "C", headerStyleVo);
		row++;

		// 明細
		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料", "L");
		} else {
			for (Map<String, String> result : resultList) {
				String clno = result.get("ClCode1") + "-" + result.get("ClCode2") + "-" + result.get("ClNo");
				makeExcel.setValue(row, 1, result.get("CustNo"), "C"); // 戶號
				makeExcel.setValue(row, 2, result.get("FacmNo"), "C"); // 額度
				makeExcel.setValue(row, 3, StringCut.replaceLineUp(result.get("CustName")), "L"); // 戶名
				makeExcel.setValue(row, 4, result.get("Status"), "L"); // 戶況
				makeExcel.setValue(row, 5, clno, "L"); // 擔保品號碼
				row++;
			}
		}
		makeExcel.close();
	}
}