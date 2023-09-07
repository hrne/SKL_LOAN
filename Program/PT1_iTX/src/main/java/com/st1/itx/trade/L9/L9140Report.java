package com.st1.itx.trade.L9;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxInquiry;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxInquiryService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L9140Report")
@Scope("prototype")
public class L9140Report extends MakeReport {

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	WebClient webClient;

	@Autowired
	TxInquiryService sTxInquiryService;

	@Autowired
	TxTranCodeService sTxTranCodeService;

	@Autowired
	CustMainService sCustMainService;

	private String pageSize = "A4";
	private String reportCode = "L9140";
	private String reportItem = "結清戶滿五年查詢清單";
	private String security = "機密";
	private String brno = "";
	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	@Override
	public void printHeader() {
		this.info("L9140Report.printHeader");

		this.setCharSpaces(0);

		this.setFontSize(10);

		this.print(-4, 6, "", "L");
		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);
		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

		this.setFontSize(12);
		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		print(1, 1, " ");
		print(1, 1, "　交易代號　　　　　　　　　　戶號　　　　　　　　　　查詢理由　　　　　　　　　　　　　　　經辦　　　　　　　　　交易日期　　　交易時間　");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

	}

	public void exec(TitaVo titaVo) throws LogicException {

		String txcd = "L9140";
		String fileName = "L9140" + "_" + "結清戶滿五年查詢清單";
		this.brno = titaVo.getBrno();
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		// 開啟報表
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setRptSize(pageSize).build();

		this.open(titaVo, reportVo);

//		int Caldy = parse.stringToInteger(titaVo.getCalDy())+19110000;//20230511改用findEntdyImportFg且日期使用會計日

		int acdate = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		this.info("L9140 acdate =" + acdate);
		Slice<TxInquiry> sTxInquiry = sTxInquiryService.findEntdyImportFg(acdate, acdate, "1", 0, 9999999, 0,
				Integer.MAX_VALUE, titaVo);

		List<TxInquiry> iTxInquiry = sTxInquiry == null ? null : sTxInquiry.getContent();

		TempVo tTempVo = new TempVo();

		this.setFont(1);

		this.setFontSize(12);
		this.setCharSpaces(0);
		if (iTxInquiry != null) {

			this.info("list count: " + +iTxInquiry.size());

			for (TxInquiry tTxInquiry : iTxInquiry) {

				String reason = "";

				tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tTxInquiry.getTranData());

				if (tTempVo.get("TxReason") == null || tTempVo.get("TxReason").isEmpty()) {
					continue;
				}

				// 交易代號
				String tranItem = findTranItem(tTempVo.get("TXCD"), titaVo);
				// 戶號
				int custNo = tTxInquiry.getCustNo();
				// 戶名
				CustMain tCustMain = sCustMainService.custNoFirst(custNo, custNo, titaVo);
				// 查詢理由
				reason = tTempVo.get("TxReason");
				// 交易日期
				String date = tTempVo.get("CALDY");
				if (date.length() > 0 && date != null && !date.isEmpty()) {
					date = showDate(date, 1);
				}
				// 交易時間
				String time = tTempVo.get("CALTM");
				time = this.showTime(time);
				this.print(1, 1, "");
				this.print(1, 1, "");
				this.print(0, 3, tTempVo.get("TXCD"));
				this.print(0, 30, parse.IntegerToString(custNo, 7));
				this.print(0, 53, reason);
				this.print(0, 90, tTempVo.get("TLRNO"));
				this.print(0, 115, date);
				this.print(0, 130, time);
				this.print(1, 90, tTempVo.get("EMPNM"));
				this.print(0, 3, tranItem);
				if (tCustMain != null) {
					this.print(0, 30, tCustMain.getCustName());
				}

			}
		} else {
			print(2, 1, "無資料");
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				"L9140 結清戶滿五年查詢清單已完成", titaVo);

		this.close();
	}

	private String showDate(String date, int iType) {

		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);

		if (rocdatex.length() == 7) {
			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
		} else {
			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);

		}

	}

	private String findTranItem(String iTranCode, TitaVo titaVo) {
		String tranitem = "";

		TxTranCode sTxTranCode = sTxTranCodeService.findById(iTranCode, titaVo);

		if (sTxTranCode != null) {
			tranitem = sTxTranCode.getTranItem();
		}

		return tranitem;
	}

}
