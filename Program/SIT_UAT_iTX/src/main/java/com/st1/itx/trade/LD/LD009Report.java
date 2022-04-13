package com.st1.itx.trade.LD;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD009ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.StringCut;

@Component("LD009Report")
@Scope("prototype")
public class LD009Report extends MakeReport {

	@Autowired
	LD009ServiceImpl lD009ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "LD009";
	private String reportItem = "放款授信日報表";
	private String security = "密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// length: 167
	// usually we do print() with x at 1 instead of 0 so the last symbol output is
	// actually at length-1;
	// hence the extra space to avoid print()s using newBorder.length() to pin the
	// output on right side and gets wrong position
	// e.g. print(0, newBorder.length, s, "R");
	private String newBorder = "====================================================================================================================================================================="
			+ " ";

	private int thisMaxRow = 45;

	/**
	 * Make sure there's space for <i>line</i> lines of output on current page.
	 * 
	 * @param line amount of lines
	 */
	private void MakeSpace(int line) {
		if (thisMaxRow - this.NowRow < line) {
			newPage();
		}
	}

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("LD009Report.printHeader");

		this.print(-1, 1, "程式ID  ：" + this.getParentTranCode());
		this.print(-2, 1, "報表代號：" + reportCode);

		this.print(-1, 150, "機密等級：" + this.security);
		this.print(-2, 150, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 150, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 150, "頁　　數：" + this.getNowPage());
		this.print(-5, 150, "單　　位：元");

		this.print(-2, newBorder.length() / 2, "新光人壽保險股份有限公司", "C");
		this.print(-3, newBorder.length() / 2, this.reportItem, "C");

		this.print(-5, newBorder.length() / 2, showRocDate(this.reportDate), "C");

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ----------------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
		this.print(-7, 1, "　　　　　　　　　　　　　　　 　件　　　　　　　　　　　數　　　　　　　　　　　　　　　　　　 金 　　　　　　　　　　　　　　　　　　 額");
		this.print(-8, 1, "　　　　　　　　　　　　----------------------------------------------　---------------------------------------------------------------------------------------------");
		this.print(-9, 1, "業務科目      　           前日　　加　　減　展入　展出　淨值　　本日　　　　前　　日　　　　　　　加　　　　　　　減　　　　淨增減　　　　本　　日　　　　　　展　期");
		this.print(-10, 1, newBorder);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(thisMaxRow);

	}

	// 自訂表尾
	@Override
	public void printFooter() {

	}

	private void fillData(TitaVo titaVo) {

		this.newPage();

		List<Map<String, String>> lLD009 = null;

		try {
			lLD009 = lD009ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LD009ServiceImpl.findAll error = " + errors.toString());
		}

		/**
		 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6-----
		 * ----------------123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345
		 */
//		this.print(-7, 1, "　　　　　　　　　　　　　　　　件　　　　　　　　　　　數　　　　　　　　　　　　　　　　　　 金 　　　　　　　　　　　　　　　　　　 額");
//		this.print(-8, 1, "　　　　　　　　　　　 ----------------------------------------------　------------------------------------------------------------------------------------------");
//		this.print(-9, 1, "業務科目                　前日　　加　　減　展入　展出　淨值　　本日　　　　前　　日　　　　　　　加　　　　　　　減　　　　淨增減　　　　本　　日　　　　　　展　期");

		if (lLD009 != null && lLD009.size() != 0) {

			// 全表總計
			BigDecimal[] totalAll = new BigDecimal[13];
			Arrays.fill(totalAll, BigDecimal.ZERO);

			// 小計
			BigDecimal[] totalPerItem = new BigDecimal[13];
			Arrays.fill(totalPerItem, BigDecimal.ZERO);

			String lastAcctItem = null;

			for (Map<String, String> tLDVo : lLD009) {
				print(1, 0, "    ");

				// 每個 AcctItem 僅第一次出現時 print
				if (lastAcctItem == null || !lastAcctItem.equals(tLDVo.get("F1"))) {
					// 避免最開頭出小計
					if (lastAcctItem != null) {
						// 小計
						print(0, 18, "小　　計");
						print(0, 32, totalPerItem[0].toString(), "R");
						print(0, 38, totalPerItem[1].toString(), "R");
						print(0, 44, totalPerItem[2].toString(), "R");
						print(0, 50, totalPerItem[3].toString(), "R");
						print(0, 56, totalPerItem[4].toString(), "R");
						print(0, 62, totalPerItem[5].toString(), "R");
						print(0, 70, totalPerItem[6].toString(), "R");
						print(0, 86, formatAmt(totalPerItem[7], 0), "R");
						print(0, 102, formatAmt(totalPerItem[8], 0), "R");
						print(0, 118, formatAmt(totalPerItem[9], 0), "R");
						print(0, 132, formatAmt(totalPerItem[10], 0), "R");
						print(0, 148, formatAmt(totalPerItem[11], 0), "R");
						print(0, 166, formatAmt(totalPerItem[12], 0), "R");

						// 小計重置
						totalPerItem = new BigDecimal[13];
						Arrays.fill(totalPerItem, BigDecimal.ZERO);

						// 換行
						print(1, 0, "    ");
						print(1, 0, "    ");
						print(1, 0, "    ");

					}

					lastAcctItem = tLDVo.get("F1");
					print(0, 1, lastAcctItem);
				}

				print(0, 18, StringCut.stringCut(tLDVo.get("F3"), 0, 8));
				print(0, 32, tLDVo.get("F4"), "R");
				print(0, 38, tLDVo.get("F5"), "R");
				print(0, 44, tLDVo.get("F6"), "R");
				print(0, 50, tLDVo.get("F7"), "R");
				print(0, 56, tLDVo.get("F8"), "R");
				print(0, 62, tLDVo.get("F9"), "R");
				print(0, 70, tLDVo.get("F10"), "R");
				print(0, 86, formatAmt(tLDVo.get("F11"), 0), "R");
				print(0, 102, formatAmt(tLDVo.get("F12"), 0), "R");
				print(0, 118, formatAmt(tLDVo.get("F13"), 0), "R");
				print(0, 132, formatAmt(tLDVo.get("F14"), 0), "R");
				print(0, 148, formatAmt(tLDVo.get("F15"), 0), "R");
				print(0, 166, formatAmt(tLDVo.get("F16"), 0), "R");

				// 計算小計與總計

				for (int i = 0; i < 13; i++) {
					totalAll[i] = totalAll[i].add(getBigDecimal(tLDVo.get("F" + (4 + i))));
					totalPerItem[i] = totalPerItem[i].add(getBigDecimal(tLDVo.get("F" + (4 + i))));
				}

			}

			// 最後一項的小計

			print(1, 0, "   ");
			print(0, 18, "小　　計");
			print(0, 32, totalPerItem[0].toString(), "R");
			print(0, 38, totalPerItem[1].toString(), "R");
			print(0, 44, totalPerItem[2].toString(), "R");
			print(0, 50, totalPerItem[3].toString(), "R");
			print(0, 56, totalPerItem[4].toString(), "R");
			print(0, 62, totalPerItem[5].toString(), "R");
			print(0, 70, totalPerItem[6].toString(), "R");
			print(0, 86, formatAmt(totalPerItem[7], 0), "R");
			print(0, 102, formatAmt(totalPerItem[8], 0), "R");
			print(0, 118, formatAmt(totalPerItem[9], 0), "R");
			print(0, 132, formatAmt(totalPerItem[10], 0), "R");
			print(0, 148, formatAmt(totalPerItem[11], 0), "R");
			print(0, 166, formatAmt(totalPerItem[12], 0), "R");

			MakeSpace(3);
			this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			this.print(1, 1, " 合　　計");
			print(0, 32, totalAll[0].toString(), "R");
			print(0, 38, totalAll[1].toString(), "R");
			print(0, 44, totalAll[2].toString(), "R");
			print(0, 50, totalAll[3].toString(), "R");
			print(0, 56, totalAll[4].toString(), "R");
			print(0, 62, totalAll[5].toString(), "R");
			print(0, 70, totalAll[6].toString(), "R");
			print(0, 86, formatAmt(totalAll[7], 0), "R");
			print(0, 102, formatAmt(totalAll[8], 0), "R");
			print(0, 118, formatAmt(totalAll[9], 0), "R");
			print(0, 132, formatAmt(totalAll[10], 0), "R");
			print(0, 148, formatAmt(totalAll[11], 0), "R");
			print(0, 166, formatAmt(totalAll[12], 0), "R");
			this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		} else {
			print(1, 0, "本日無資料");
		}

		print((thisMaxRow + 2) * -1, newBorder.length() / 2, "===== 報　表　結　束 =====", "C");

	}

	public void makePdf(TitaVo titaVo) throws LogicException {
		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		fillData(titaVo);

		this.close();

		// this.toPdf(sno, reportCode + "_" + reportItem);

	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LD009Report exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		makePdf(titaVo);

		return true;
	}
}
