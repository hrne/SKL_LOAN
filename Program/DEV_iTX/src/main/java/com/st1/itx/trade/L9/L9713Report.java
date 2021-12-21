package com.st1.itx.trade.L9;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class L9713Report extends MakeReport {

	@Autowired
	MakeExcel makeExcel;

	Calendar calendar = Calendar.getInstance();

	// excel底稿路徑
	@Value("${iTXInFolder}")
	private String iTXInFolder = "";
//
//	@Autowired
//	TxCom txCom;
	// 製表日期
//	private String nowDate;

	int t1 = 0;
	int t2 = 0;
	int t3 = 0;
	int t4 = 0;
	int t5 = 0;
	int t6 = 0;
	int t7 = 0;
	int t = 0;
	int p = 0;
	int tp = 0;
	String iday = "";
	String iday0 = "";
	String iday1 = "";
	String iday2 = "";
	String iday3 = "";
	String iday4 = "";
	String iday5 = "";
	String iday4_1 = "";
	String iday5_1 = "";
	// 表格起始行
	int startCol = 8;
	// 表格起始列
	int startRow = 1;

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(37);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9713Report exec");

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		iday = (iYear - 1911) + String.format("%02d", iMonth);

		// 以會計日區分級數
		iday1 = addDate(iday, 1);
		iday2 = addDate(iday, 2);
		iday3 = addDate(iday, 3);
		iday4_1 = addDate(iday, 4);
		iday4 = addDate(iday, 6);
		iday5_1 = addDate(iday, 7);
		iday5 = addDate(iday, 12);

		// open pdf
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9713", "應收票據之帳齡分析表", "", "A4", "L");

		// 當日 西元年月日 20210208
		String iCALDY = String.valueOf(Integer.valueOf(titaVo.get("CALDY")) + 19110000);
		// 使用者編號 001718
		String iTLRNO = titaVo.get("TLRNO");
		// 取得上傳的檔名 L9713-應收票據之帳齡分析表.csv
		String iUPFILE2 = titaVo.get("UPFILE");
		this.info("iUPFILE2:" + iUPFILE2);
		String iUPFILE = titaVo.get("UPFILE").trim();
		this.info("iUPFILE:" + iUPFILE);
		// 檔案絕對路徑
		String filename = iTXInFolder + iCALDY + File.separator + iTLRNO + File.separator + iUPFILE;
		// 讀取csv 資料
		makeExcel.openCsv(filename, ",");

		String tmp = "";
		int cnt = 0;
		int tday = 0;
		int tamt = 0;

		this.info(" makeExcel.getListMap:" + makeExcel.getListMap());

		for (Map<String, Object> map : makeExcel.getListMap()) {

//			this.info("map f6:" + map.get("f6") + "," + map.get("f6").toString().length());// 金額
//			this.info("map f7:" + map.get("f7") + "," + map.get("f7").toString().length());// 支票日期

			cnt += 1;

			if (cnt > 1) {

				// 取得支票日期
				if (!Integer.valueOf((String) map.get("f6")).equals(t7)) {

					tmp = (String) map.get("f7");
					tmp = tmp.replace("\"", "");

					String[] tmpDate = tmp.split("/");

					// 此陣列大小必為3
					if (tmpDate.length == 3) {
						tday = Integer.valueOf(tmpDate[0] + String.format("%02d", Integer.parseInt(tmpDate[1])) + String.format("%02d", Integer.parseInt(tmpDate[2]))) - 19110000;
					}

					// 金額
					tmp = (String) map.get("f6");
					tmp = tmp.trim().replace("\"", "");
					tamt = Integer.valueOf(tmp);

					t7 += tamt;
					// 排除低於當月份的

					/* 結論：CSV上有當月份以前的都算低於30日以下的帳齡 */
					// 當月+1的交換日+
					if (tday < Integer.valueOf(iday1)) {
						t1 += tamt;

						// 當月+2的交換日
					} else if (tday < Integer.valueOf(iday2)) {
						t2 += tamt;

						// 當月+3的交換日
					} else if (tday < Integer.valueOf(iday3)) {
						t3 += tamt;

						// 當月+4～6的交換日
					} else if (tday < Integer.valueOf(iday4)) {
						t4 += tamt;

						// 當月+7～12的交換日
					} else if (tday < Integer.valueOf(iday5)) {
						t5 += tamt;
						// 1年以上
					} else {
						t6 += tamt;
					}
				}

			}
		}
		// print的row至少要大於0，才能讓getNowpage()=1，才可以newPage()換頁
		report();

//		this.info("this.getNowPage()=" + this.getNowPage());

//		this.setParentTranCode("P");

		this.setFontSize(12);
//		this.print(1, 0, "");
//		newPage();

		long sno = this.close();

		// this.toPdf(sno);

		if (makeExcel.getListMap().size() > 0) {
			return true;
		} else {
			return false;

		}
	}

	private void report() {
		String tmp = "";
		String iCALDY = String.valueOf(Integer.valueOf(titaVo.get("CALDY")) + 19110000);
		this.setCharSpaces(0);

		// 以下為字體大小12 以12字體 橫的最右為140
		this.setFontSize(12);
		this.print(-2, 138, "機密等級：密", "R");

		this.print(-3, 138, "製表日期：" + iCALDY.substring(0, 4) + "/" + iCALDY.substring(4, 6) + "/" + iCALDY.substring(6), "R");
		this.print(-9, 128, "單位：元", "R");

		// 以下為字體大小20點
		this.setFontSize(20);
		this.print(-5, 45, "放款部應收票據之帳齡分析表─ " + iday.substring(0, 3) + "年" + iday.substring(3, 5) + "月份", "C");

		// 先畫框線
		// 間距18 16 16
		this.print(-6 - startRow, startCol, "┌──────────────┬─────────┬────────┐");
		for (int j = 0; j < 7; j++) {
			int num = j * 2;
			this.print(-7 - num - startRow, startCol, "│　　　　　　　　　　　　　　│　　　　　　　　　│　　　　　　　　│");
			this.print(-8 - num - startRow, startCol, "├──────────────┼─────────┼────────┤");
		}
		this.print(-21 - startRow, startCol, "│　　　　　　　　　　　　　　│　　　　　　　　　│　　　　　　　　│");
		this.print(-22 - startRow, startCol, "└──────────────┴─────────┴────────┘");

		this.print(-7 - startRow, 24, "帳            齡", "C");
		this.print(-7 - startRow, 49, "金      額", "C");
		this.print(-7 - startRow, 68, "比 例", "C");

		this.print(-9 - startRow, 24, "30日以下", "C");

		tmp = iday2.substring(0, 3) + "/" + iday2.substring(3, 5) + "月";
		this.print(-11 - startRow, 24, "30日~60日(" + tmp + ")", "C");

		tmp = iday3.substring(0, 3) + "/" + iday3.substring(3, 5) + "月";
		this.print(-13 - startRow, 24, "60日~90日(" + tmp + ")", "C");

		tmp = iday4_1.substring(0, 3) + "/" + iday4_1.substring(3, 5) + "~" + iday4.substring(0, 3) + "/" + iday4.substring(3, 5) + "月";
		this.print(-15 - startRow, 24, "4~6個月(" + tmp + ")", "C");

		tmp = iday5_1.substring(0, 3) + "/" + iday5_1.substring(3, 5) + "~" + iday5.substring(0, 3) + "/" + iday5.substring(3, 5) + "月";
		this.print(-17 - startRow, 24, "7~12個月(" + tmp + ")", "C");
		this.print(-19 - startRow, 24, "1年以上", "C");
		this.print(-21 - startRow, 24, "合           計", "C");

		// 金額及比例部分
		if (t7 == 0) {
			return;
		}

		t = 0;
		tp = 0;
		// 百分比
		int percent = 0;
		tot(t1);
		this.print(-9 - startRow, 58, showAmt(t1), "R");
		percent += p;
		this.print(-9 - startRow, 68, showP(p), "C");

		tot(t2);
		this.print(-11 - startRow, 58, showAmt(t2), "R");
		percent += p;
		this.print(-11 - startRow, 68, showP(p), "C");

		tot(t3);
		this.print(-13 - startRow, 58, showAmt(t3), "R");
		percent += p;
		this.print(-13 - startRow, 68, showP(p), "C");

		tot(t4);
		this.print(-15 - startRow, 58, showAmt(t4), "R");
		percent += p;
		this.print(-15 - startRow, 68, showP(p), "C");

		tot(t5);
		this.print(-17 - startRow, 58, showAmt(t5), "R");
		percent += p;
		this.print(-17 - startRow, 68, showP(p), "C");

		tot(t6);
		this.print(-19 - startRow, 58, showAmt(t6), "R");
		percent += p;
		this.print(-19 - startRow, 68, showP(p), "C");

		this.print(-21 - startRow, 58, showAmt(t7), "R");

		this.print(-21 - startRow, 68, showP(percent), "C");
	}

	/**
	 * 民國年 推算帳齡
	 * 
	 * @param iDate 現在 民國年月日 (7位數)
	 * @param submm 帳齡級距 (1個月份為1單位)
	 * 
	 */
	private String addDate(String iDate, int submm) {
		int yy = Integer.valueOf(iDate.substring(0, 3));
		int mm = Integer.valueOf(iDate.substring(3, 5));

		mm = mm + submm;

		if (mm > 12) {
			mm -= 12;
			yy += 1;
		}

		int ndate = yy * 10000 + mm * 100 + 99;
		return String.valueOf(ndate);

	}

	private void tot(int amt) {
		double d = 0.00;

		t += amt;
		if (t == t7) {
			p = 100 - tp;
		} else {
			d = Math.round(amt * 100.00 / t7);
			p = (int) d;
		}
		tp += p;
	}

	private String showAmt(int amt) {
		if (amt == 0) {
			return "-";
		}
		return String.format("%,d", amt);
	}

	private String showP(int p) {
		return String.valueOf(p) + "％";
	}

//	public void printHeaderP12321() {
//		this.setFontSize(8);
//		this.setCharSpaces(0);
//
//		this.print(-1, 147, "機密案件：密");
//		this.print(-2, 3, "程式ID：" + this.getParentTranCode());
//
//		this.print(-2, 3, "報表名稱：" + this.getParentTranCode());
//		this.print(-2, 60, "新光人壽保險股份有限公司");
//		this.print(-2, 147, "製表者：");
//		String tim = String.valueOf(Integer.parseInt(dDateUtil.getNowStringBc().substring(4, 6)));
//		this.print(-2, 165, "日　　期： " + tim + "/" + dDateUtil.getNowStringBc().substring(6) + "/"
//				+ dDateUtil.getNowStringBc().substring(2, 4), "R");
//
//		this.print(-3, 3, "報　表：" + this.getRptCode());
//		this.print(-3, 61, "長中短期放款到期明細表");
//		this.print(-3, 165, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
//				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
//				"R");
//		this.print(-4, 147, "頁　　次：　" + this.getNowPage());
//		this.print(-5, 50, "到期起訖日...　" + showRocDate(titaVo.get("ACCTDATE_ST"), 1) + " -  "
//				+ showRocDate(titaVo.get("ACCTDATE_ED"), 1));
//		this.print(-5, 147, "單位：元");
//		this.print(-7, 0, "　 站別　　 押品地區別　房貸專員　　　戶號　　　　　戶名　　　　核准號碼　　　　到期日　　　　貸放餘額　 上次繳息日　 計息利率　聯絡電話　　　　　聯絡人　　　是否本利攤");
//		this.print(-8, 0,
//				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//	}

//	private void reportDetail() {
//
//		String tmp = "";
//		String iCALDY = String.valueOf(Integer.valueOf(titaVo.get("CALDY")) + 19110000);
//		this.setCharSpaces(0);
//		this.print(-2, 80, "機密等級：密");
////		this.print(-3, 72, "製表日期：" + showDate(this.nowDate));
//		this.print(-3, 72, "製表日期：" + iCALDY.substring(0, 4) + "/" + iCALDY.substring(4, 6) + "/" + iCALDY.substring(6));
//		this.print(-6, 23, "放款部應收票據之帳齡分析表─ " + iday.substring(0, 3) + "年" + iday.substring(3, 5) + "月份");
//		this.print(-7, 77, "單位：元");
//
//		this.print(-8, startCol, "┌─────────────────┬───────────┬───────────┐");
//		this.print(-9, startCol, "│          帳          齡          │      金       額     │         比例         │");
//		this.print(-10, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-11, startCol, "│              30日以下            │                      │                      │");
//		this.print(-12, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-13, startCol, "│        30日~60日(        )       │                      │                      │");
//		this.print(-14, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-15, startCol, "│        60日~90日(        )       │                      │                      │");
//		this.print(-16, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-17, startCol, "│      4~6個月(               )    │                      │                      │");
//		this.print(-18, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-19, startCol, "│     7~12個月(               )    │                      │                      │");
//		this.print(-20, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-21, startCol, "│             1年以上              │                      │                      │");
//		this.print(-22, startCol, "├─────────────────┼───────────┼───────────┤");
//		this.print(-23, startCol, "│          合           計         │                      │                      │");
//		this.print(-24, startCol, "└─────────────────┴───────────┴───────────┘");
//
//		this.print(-26, 39, "經理：                  經辦：");
//
//		tmp = iday2.substring(0, 3) + "/" + iday2.substring(3, 5) + "月";
//		this.print(-13, 21, tmp);
//		tmp = iday3.substring(0, 3) + "/" + iday3.substring(3, 5) + "月";
//		this.print(-15, 21, tmp);
//		tmp = iday4_1.substring(0, 3) + "/" + iday4_1.substring(3, 5) + "~" + iday4.substring(0, 3) + "/"
//				+ iday4.substring(3, 5) + "月";
//		this.print(-17, 17, tmp);
//		tmp = iday5_1.substring(0, 3) + "/" + iday5_1.substring(3, 5) + "~" + iday5.substring(0, 3) + "/"
//				+ iday5.substring(3, 5) + "月";
//		this.print(-19, 17, tmp);
//
//		if (t7 == 0) {
//			return;
//		}
//
//		t = 0;
//		tp = 0;
//
//		tot(t1);
//		this.print(-11, 60, showAmt(t1), "R");
//		this.print(-11, 76, showP(p), "R");
//
//		tot(t2);
//		this.print(-13, 60, showAmt(t2), "R");
//		this.print(-13, 76, showP(p), "R");
//
//		tot(t3);
//		this.print(-15, 60, showAmt(t3), "R");
//		this.print(-15, 76, showP(p), "R");
//
//		tot(t4);
//		this.print(-17, 60, showAmt(t4), "R");
//		this.print(-17, 76, showP(p), "R");
//
//		tot(t5);
//		this.print(-19, 60, showAmt(t5), "R");
//		this.print(-19, 76, showP(p), "R");
//
//		tot(t6);
//		this.print(-21, 60, showAmt(t6), "R");
//		this.print(-21, 76, showP(p), "R");
//
//		this.print(-23, 60, showAmt(t7), "R");
//		this.print(-23, 76, showP(100), "R");
//
//	}

}
