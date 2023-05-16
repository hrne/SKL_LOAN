package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.springjpa.cm.L9711ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9711Report extends MakeReport {

	@Autowired
	L9711ServiceImpl l9711ServiceImpl;

	@Autowired
	L9711Report3 l9711report3;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	@Autowired
	private CustNoticeCom custNoticeCom;
	
	@Autowired
	private CustNoticeService sCustNoticeService;

	private boolean isLetterNoticeFlag = true;
	// 起始欄位 位置
	int startPos = 4;

	@Override
	public void printHeader() {

		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

		// 字體大小
		this.setFont(14);

		// 字體大小
		this.setFontSize(8);

		// 字間距
		this.setCharSpaces(0);

		// 左中右排 標題位置
		int rightPos = 3;
		int centerPos = 73;
		int leftPos = 125;

		// 左側
		this.print(-2, rightPos, "程式ID：" + this.getParentTranCode(), "L");
		this.print(-3, rightPos, "報　表：" + this.getRptCode(), "L");

		// 中間
		String tim = String.format("%02d", Integer.parseInt(dDateUtil.getNowStringBc().substring(4, 6)));

		this.print(-1, centerPos, "新光人壽保險股份有限公司", "C");
		this.print(-2, centerPos, "長中短期放款到期明細表", "C");
		this.print(-4, centerPos, "到期起訖日...　" + showRocDate(titaVo.get("ACCTDATE_ST"), 1) + " -  "
				+ showRocDate(titaVo.get("ACCTDATE_ED"), 1), "C");
//		this.print(-4, centerPos, "（　需列印到期通知單之清單　　）", "C");
		if (!isLetterNoticeFlag) {
			this.print(-3, centerPos, "(已申請不列印書面通知書客戶)", "C");
		}
		// 右排
		this.print(-1, leftPos, "機密案件：" + this.getSecurity(), "L");
		this.print(-2, leftPos, "日　　期：" + tim + "/" + dDateUtil.getNowStringBc().substring(6) + "/"
				+ dDateUtil.getNowStringBc().substring(2, 4), "L");
		this.print(-3, leftPos, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
				"L");
		this.print(-4, leftPos, "頁　　次：　" + this.getNowPage(), "L");

		// startPos=6
		int rowNum = -6;

		// 各項目 位置
		this.print(rowNum, startPos - 2, "擔保品地區別", "L");
		this.print(rowNum, startPos + 12, "房貸專員", "L");
		this.print(rowNum, startPos + 22, "戶號", "L");
		this.print(rowNum, startPos + 34, "戶名", "L");
		this.print(rowNum, startPos + 52, "核准號碼", "L");
		this.print(rowNum, startPos + 62, "到期日", "L");
		this.print(rowNum, startPos + 75, "貸放餘額", "L");
		this.print(rowNum, startPos + 86, "上次繳息日", "L");
		this.print(rowNum, startPos + 98, "計息利率", "L");
		this.print(rowNum, startPos + 110, "聯絡電話", "L");
		this.print(rowNum, startPos + 125, "聯絡人", "L");
		this.print(rowNum, startPos + 133, "是否本利攤", "L");
//		this.print(-6, 0, "　 站別　　 押品地區別　房貸專員　　　戶號　　　　　戶名　　　　核准號碼　　　　到期日　　　　貸放餘額　 上次繳息日　 計息利率　聯絡電話　　　　　聯絡人　　　是否本利攤");

		this.print(-7, 0,
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public List<Map<String, String>> exec(TitaVo titaVo) throws LogicException {

		this.info("L9711Report exec");

		List<Map<String, String>> l9711List = null;

		try {

			l9711List = l9711ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9711ServiceImpl.LoanBorTx error = " + errors.toString());
			return null;

		}
		String txcd = titaVo.getTxcd();
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String reportItem = "放款到期明細表";
		String pageSize = "A4";
		String pageOrientation = "P";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(reportItem).setRptSize(pageSize).setPageOrientation(pageOrientation).build();
		this.open(titaVo, reportVo);

		// 書面列印通知書客戶
		List<Map<String, String>> isLetterList = new ArrayList<Map<String, String>>();
		// 書面不列印通知書客戶
		List<Map<String, String>> isNotLetterList = new ArrayList<Map<String, String>>();

		if (l9711List.size() > 0) {

			// 先找初以申請不列印書面通知書之客戶
			for (Map<String, String> r : l9711List) {

				int custNo = parse.stringToInteger(r.get("F4"));
				int facmNo = parse.stringToInteger(r.get("F5"));

				CustNotice lCustNotice = new CustNotice();
				CustNoticeId lCustNoticeId = new CustNoticeId();

				lCustNoticeId.setCustNo(custNo);
				lCustNoticeId.setFacmNo(facmNo);
				lCustNoticeId.setFormNo("L9710");

				lCustNotice = sCustNoticeService.findById(lCustNoticeId, titaVo);

//				TempVo tempVo = new TempVo();
//				tempVo = sCustNoticeService....getCustNotice("L9703", custNo, facmNo, titaVo);

//				if ("Y".equals(tempVo.getParam("isLetter"))) {
				// custNotice 空的 表示 沒有申請列印 或 有值但是 paper為N 也是沒有申請列印
				if (lCustNotice == null) {
					isLetterList.add(r);
				} else {
					if ("Y".equals(lCustNotice.getPaperNotice())) {
						isLetterList.add(r);
					}

					if ("N".equals(lCustNotice.getPaperNotice())) {
						isNotLetterList.add(r);
					}
				}

			}
		} else {

			this.print(1, startPos, "本日無資料");

		}

		// 計算筆數
		int count = 0;
		if (isLetterList.size() > 0) {
			// 書面列印通知書客戶
			for (Map<String, String> tL9711Vo : isLetterList) {
				count++;
				printData(tL9711Vo);
				// 每到 60 筆，換一頁
				if (count >= 60) {
//					this.info("換一頁：" + count + "筆");
					count = 0;
					newPage();
				}
			}

		} else {
			this.print(1, startPos, "本日無資料");
		}

		isLetterNoticeFlag = false;
		newPage();
		count = 0;
		if (isNotLetterList.size() > 0) {
			// 書面不列印通知書客戶
			for (Map<String, String> tL9711Vo : isNotLetterList) {
				count++;
				printData(tL9711Vo);
				// 每到 60 筆，換一頁
				if (count >= 60) {
//					this.info("換一頁：" + count + "筆");
					count = 0;
					newPage();
				}
			}
		} else {
			this.print(1, startPos, "本日無資料");
		}
		long sno = this.close();

		this.toPdf(sno);

		l9711report3.exec(titaVo, isLetterList);

		return l9711List;

	}

	private void printData(Map<String, String> tL9711Vo) {

		// 押品地區別
		this.print(1, startPos - 2, tL9711Vo.get("F1") == null || tL9711Vo.get("F1").length() == 0 ? ""
				: tL9711Vo.get("F1") + " " + tL9711Vo.get("F2"), "L");

		// 房貸專員
		this.print(0, startPos + 12, tL9711Vo.get("F3"), "L");

		// 戶號
		this.print(0, startPos + 22, String.format("%07d", Integer.valueOf(tL9711Vo.get("F4"))) + " "
				+ String.format("%03d", Integer.valueOf(tL9711Vo.get("F5"))), "L");

		int nameLength = 10;
		if (tL9711Vo.get("F6").length() < 10) {
			nameLength = tL9711Vo.get("F6").length();
		}

		// 戶名
		this.print(0, startPos + 34, tL9711Vo.get("F6").substring(0, nameLength), "L");

		// 核准號碼
		this.print(0, startPos + 53, tL9711Vo.get("F7") == null || tL9711Vo.get("F7").length() == 0 ? "0000000"
				: String.format("%07d", Integer.valueOf(tL9711Vo.get("F7"))), "L");

		// 到期日
		this.print(0, startPos + 62, showRocDate(tL9711Vo.get("F8"), 1), "L");

		// 貸放餘額
		this.print(0, startPos + 83, showAmt(tL9711Vo.get("F10")), "R");

		// 上次繳息日
		this.print(0, startPos + 86, showRocDate(tL9711Vo.get("F12"), 1), "L");

		// 計息利率
		this.print(0, startPos + 106, String.format("%.4f", Double.valueOf(tL9711Vo.get("F13"))), "R");

		// 聯絡電話
		this.print(0, startPos + 108,
				tL9711Vo.get("F14") == null || tL9711Vo.get("F14").length() == 0 ? "" : tL9711Vo.get("F14"), "L");

		// 聯絡人
		this.print(0, startPos + 125,
				tL9711Vo.get("F15") == null || tL9711Vo.get("F15").length() == 0 ? "" : tL9711Vo.get("F15"), "L");

		// 是否本利攤
		this.print(0, startPos + 139, "3".equals(tL9711Vo.get("F21")) || "4".equals(tL9711Vo.get("F21")) ? "Y" : "N",
				"R");
	}

	private String showAmt(String xamt) {

		if (xamt == null || xamt.equals("") || xamt.equals("0")) {
			return "";
		}

		int amt = Integer.valueOf(xamt);

		return String.format("%,d", amt);
	}

}
