package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9719ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component("L9719Report")
@Scope("prototype")
public class L9719Report extends MakeReport {

	@Autowired
	L9719ServiceImpl l9719ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9719";
	private String reportItem = "放款利息法折溢價攤銷表";
	private String security = "密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// number with commas
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	String rocYear;
	String rocMonth;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9719Report.printHeader");

		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 85, this.reportItem, "C");

		// 明細表頭
		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-3, 1, "┌──────────────────────────────────────────────────────────────────────────────┐");
		this.print(-4, 1,
				"｜                                                                                                                                         資料日期：" + rocYear + "年" + rocMonth + "月｜");
		this.print(-5, 1, "├──────────────────────────────────────────────────────────────────────────────┤");
		this.print(-6, 1, "｜                                                                                                                                              單位：新台幣元｜");
		this.print(-7, 1, "├──────┬──────────┬────────────┬──────┬───────────┬───────────┬────────────────┤");
		this.print(-8, 1, "｜  傳票序號  ｜    會計科目代號    ｜        科目名稱        ｜    幣別    ｜       借方金額       ｜       貸方金額       ｜             摘要說明           ｜");
		this.print(-9, 1, "├──────┼──────────┼────────────┼──────┼───────────┼───────────┼────────────────┤");
		this.print(-10, 1, "｜    001     ｜                    ｜                        ｜    NTD     ｜                      ｜                      ｜                                ｜");
		this.print(-11, 1, "├──────┼──────────┼────────────┼──────┼───────────┼───────────┼────────────────┤");
		this.print(-12, 1, "｜    002     ｜                    ｜                        ｜    NTD     ｜                      ｜                      ｜                                ｜");
		this.print(-13, 1, "├──────┼──────────┼────────────┼──────┼───────────┼───────────┼────────────────┤");
		this.print(-14, 1, "｜    003     ｜                    ｜                        ｜    NTD     ｜                      ｜                      ｜                                ｜");
		this.print(-15, 1, "├──────┼──────────┼────────────┼──────┼───────────┼───────────┼────────────────┤");
		this.print(-16, 1, "｜            ｜     合計 Total     ｜                        ｜    NTD     ｜                      ｜                      ｜                                ｜");
		this.print(-17, 1, "├──────┴──────────┴────────────┴──────┴───────────┼───────────┴────────────────┤");
		this.print(-18, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-19, 1, "├─────────────────────────────────────────────────┼────────────────────────────┤");
		this.print(-20, 1, "｜                                          放款管理課                                              ｜                         會辦單位                       ｜");
		this.print(-21, 1, "├─────────────────────────────────────────────────┼────────────────────────────┤");
		this.print(-22, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-23, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-24, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-25, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-26, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-27, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-28, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-29, 1, "｜                                                                                                  ｜                                                        ｜");
		this.print(-30, 1, "└─────────────────────────────────────────────────┴────────────────────────────┘");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(31);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}

	// 自訂表尾
	@Override
	public void printFooter() {

	}

	private void fillData(TitaVo titaVo) {
		this.newPage();

		List<Map<String, String>> lL9719 = null;

		try {
			lL9719 = l9719ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9719ServiceImpl.findAll error = " + errors.toString());
		}

		String F12 = "0";
		String F13 = "0";

		if (lL9719 != null && !lL9719.isEmpty()) {

			// should only have 1 result in this query!

//			會計科目代號 26 C
//			科目名稱 50 C
//			借方金額 99 R
//			貸方金額 124 R
//			摘要說明 143 C
//
//			F0 擔保放款-借方金額
//			F1 擔保放款-貸方金額
//			F2 擔保代號
//			F3 擔保名稱
//			F4 催收款-借方金額
//			F5 催收款-貸方金額
//			F6 催收代號
//			F7 催收名稱
//			F8 利息借金
//			F9 利息貸金
//			F10 利息代號
//			F11 利息名稱
//			F12 催收累積
//			F13 擔保累積
//			F14 傳票摘要

			for (Map<String, String> tLDVo : lL9719) {
				this.info("lL9719:" + lL9719.get(0));
				for (int i = 0; i <= 2; i++) {
					print(-(10 + i * 2), 26, tLDVo.get("F" + Integer.toString(2 + i * 4)), "C");
					print(-(10 + i * 2), 50, tLDVo.get("F" + Integer.toString(3 + i * 4)), "C");
					print(-(10 + i * 2), 99, formatAmt(tLDVo.get("F" + (0 + i * 4)), 0), "R");
					print(-(10 + i * 2), 124, formatAmt(tLDVo.get("F" + (1 + i * 4)), 0), "R");
					print(-(10 + i * 2), 143, tLDVo.get("F14"), "C");
				}

				print(-16, 99, formatAmt(getBigDecimal(tLDVo.get("F0")).add(getBigDecimal(tLDVo.get("F4"))).add(getBigDecimal(tLDVo.get("F8"))), 0), "R");
				print(-16, 124, formatAmt(getBigDecimal(tLDVo.get("F1")).add(getBigDecimal(tLDVo.get("F5"))).add(getBigDecimal(tLDVo.get("F9"))), 0), "R");

				F12 = formatAmt(tLDVo.get("F12"), 0);
				F13 = formatAmt(tLDVo.get("F13"), 0);
			}

		} else {
			print(-2, 4, "本月無資料!!");
		}

		print(-23, 4, "一、至" + rocYear + "." + rocMonth + ".止，累積");
		print(-24, 4, "    擔保放款溢折價攤銷數為" + F13 + "元整");
		print(-25, 4, "    催收款溢折價攤銷數為" + F12 + "元整");
		print(-26, 4, "二、陳核。");

	}

	public void makePdf(TitaVo titaVo) throws LogicException {
		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		fillData(titaVo);

		this.toPdf(this.close(), "L9719_放款利息法折溢價攤銷表");

	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9719Report exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		rocYear = titaVo.getParam("inputYear");
		rocMonth = titaVo.getParam("inputMonth");

		makePdf(titaVo);

		return true;
	}
}
