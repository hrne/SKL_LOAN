package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9709ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class L9709Report extends MakeReport {

	@Autowired
	L9709ServiceImpl l9709ServiceImpl;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	@Override
	public void printHeader() {

		// this.setFontSize(13);
		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);
		this.print(-2, 23, "暫收放貸核心傳票檔資料");
		this.print(0, 50, "印表日期：" + showDate(this.nowDate));
		this.print(-3, 1, "會計日期：" + showDate(iDAY, 1));
		this.print(0, 50, "印表時間：" + showTime(this.nowTime));
		this.print(-5, 1, "  科目             借方金額           貸方金額           本日餘額");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L9709Report exec");

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9709", "暫收放貸核心傳票檔資料", "", "A4", "P");

		try {
			List<Map<String, String>> l9709List = l9709ServiceImpl.findAll(titaVo);

			this.info("L9709Report findAll =" + l9709List.toString());

			for (Map<String, String> tL9709Vo : l9709List) {
				this.print(1, 1, tL9709Vo.get("F0"));
				this.print(0, 27, formatAmt(tL9709Vo.get("F1"), 0), "R");
				this.print(0, 45, formatAmt(tL9709Vo.get("F2"), 0), "R");
				this.print(0, 63, formatAmt(tL9709Vo.get("F3"), 0), "R");
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9709ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		long sno = this.close();

		// 測試用
		this.toPdf(sno);
	}

	// 顯示民國年
	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
		if (iType == 1) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);
			} else {
				return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月" + rocdatex.substring(4, 6) + "日";
			} else {
				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月" + rocdatex.substring(5, 7) + "日";
			}
		} else {
			return rocdatex;
		}
	}

}
