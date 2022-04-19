package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component("lM004Report1")
@Scope("prototype")

// Making Pdf

public class LM004Report1 extends MakeReport {

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {
		printHeaderL();
		this.setBeginRow(9);
		this.setMaxRows(60);

	}
	public void printHeaderL() {
		this.print(-1, 172, "機密等級：密");
		this.print(-2, 1, "　 程式 ID：" + this.getParentTranCode());
		this.print(-2, 100, "新光人壽保險股份有限公司", "C");
		this.print(-2, 172, "日　期：" + dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8) + "/" + dDateUtil.getNowStringBc().substring(2, 4));
		this.print(-3, 1, "　 報　 表："+ this.getRptCode());
		this.print(-3, 100, "長中短期放款到期明細表", "C");
		this.print(-3, 172, "時　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 172, "頁　數：" + this.getNowPage());
		this.print(-5, 100, getshowRocDate(this.getReportDate()), "C");
		this.print(-5, 172, "單　位：元");

	}

	public void exec(TitaVo titaVo, List<Map<String, String>> LM004List) throws LogicException {
		this.setCharSpaces(0);
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM004", "長中短期放款到期明細表", "", "A4", "L");

		if (LM004List.size() != 0) {

			setFontSize(8);
			this.print(-6, 1, "　 是否　　商品");
			this.print(-6, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　介紹人");
			this.print(-7, 1, "　本利攤　 代號　 擔保品地區別　　房貸專員　　　戶號　　　　戶名　　　　　　到期日　　　應完成日　　　　　貸放餘額　　上次繳息日　　介紹人　　　　代號　　部室　　　　　區部" + "　　　　　　　通訊處");
			this.print(-8, 0,
					"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			DecimalFormat df1 = new DecimalFormat("#,##0");

			for (Map<String, String> LM4Vo : LM004List) {

				print(1, 5, LM4Vo.get("F0")); // 本利攤
				print(0, 12, LM4Vo.get("F1")); // 代號
				print(0, 20, LM4Vo.get("F2")); // 押品地區別
				print(0, 23, LM4Vo.get("F3")); // 地區別
				print(0, 34, LM4Vo.get("F4")); // 房貸專員
				print(0, 47, PadStart(7, LM4Vo.get("F5")) + "-" + PadStart(3, LM4Vo.get("F6")));// 戶號
				print(0, 60, LM4Vo.get("F7")); // 戶名
				print(0, 76, showDate(LM4Vo.get("F8"), 1)); // 到期別
				print(0, 88, showDate(LM4Vo.get("F9"), 1)); // 應完成日

				BigDecimal f10 = new BigDecimal(LM4Vo.get("F10").toString());
				print(0, 114, df1.format(f10), "R"); // 貸放餘額

				print(0, 118, showDate(LM4Vo.get("F11"), 1)); // 上次繳息日
				print(0, 132, LM4Vo.get("F12")); // 介紹人
				print(0, 145, LM4Vo.get("F13")); // 介紹人代號
				print(0, 154, LM4Vo.get("F14")); // 部室
				print(0, 168, LM4Vo.get("F15")); // 區部
				print(0, 186, LM4Vo.get("F16")); // 通訊處

				CheckRow();
			} // for

		} // if
		
		this.close();		
		//this.toPdf(sno);
	}

	private String showDate(String date, int iType) {

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
				return rocdatex.substring(0, 2) + " 年 " + rocdatex.substring(2, 4) + " 月 " + rocdatex.substring(4, 6) + " 日";
			} else {
				return rocdatex.substring(0, 3) + " 年 " + rocdatex.substring(3, 5) + " 月 " + rocdatex.substring(5, 7) + " 日";
			}
		} else {
			return rocdatex;
		}
	}

	private String PadStart(int size, String intfor) {
		for (int i = 0; i < size; i++) {
			if (intfor.length() < size) {
				intfor = "0" + intfor;
			}
		}
		return intfor;
	}

	private void CheckRow() {
		if (this.NowRow >= 50) {
			newPage();
			this.print(-6, 1, "　 是否　　商品");
			this.print(-6, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　介紹人");
			this.print(-7, 1, "　本利攤　 代號　　押品地區別　　房貸專員　　　戶號　　　　戶名　　　　　　到期別　　　應完成日　　　　　貸放餘額　　上次繳息日　　介紹人　　　　代號　　部室　　　　　區部" + "　　　　　　　通訊處");
			this.print(-8, 0,
					"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}

	}
}
