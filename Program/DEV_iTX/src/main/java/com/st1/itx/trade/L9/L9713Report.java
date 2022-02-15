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

	// 帳齡30日以下 金額
	int t1 = 0;
	// 帳齡30~60日 金額
	int t2 = 0;
	// 帳齡60~90日 金額
	int t3 = 0;
	// 帳齡4~6個月 金額
	int t4 = 0;
	// 帳齡7~12個月 金額
	int t5 = 0;
	// 帳齡1年以上 金額
	int t6 = 0;
	// 帳齡金額合計 金額
	int t7 = 0;

	String iday = "";
	String iday0 = "";
	String iday1 = "";
	String iday2 = "";
	String iday3 = "";
	String iday4 = "";
	String iday5 = "";
	String iday4_1 = "";
	String iday5_1 = "";

	// 最大金額的的區間
	int maxAmtNo = 0;
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

		// 以會計日區分各區間
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
		String tmpAmt = "";

		if (makeExcel.getListMap().size() == 0) {
			return false;
		}

//		this.info(" count:" + makeExcel.getListMap().size());
//		this.info(" makeExcel.getListMap:" + makeExcel.getListMap());

		for (Map<String, Object> map : makeExcel.getListMap()) {

//			this.info("map f6:" + map.get("f6") + "," + map.get("f6").toString().length());// 金額
//			this.info("map f7:" + map.get("f7") + "," + map.get("f7").toString().length());// 支票日期

			cnt += 1;

			if (cnt > 1) {

				tmpAmt = map.get("f6").toString();
				// 去除雙引號和單引號
				tmpAmt = tmpAmt.replace("\"", "");
				tmpAmt = tmpAmt.replace("\'", "");

				// 取得支票日期
				if (!"0".equals(tmpAmt)) {

					tmp = (String) map.get("f7");
					// 去除雙引號和單引號
					tmp = tmp.replace("\"", "");
					tmp = tmp.replace("\'", "");

					String[] tmpDate = tmp.split("/");

					// 此陣列大小必為3
					if (tmpDate.length == 3) {
						tday = Integer.valueOf(tmpDate[0] + String.format("%02d", Integer.parseInt(tmpDate[1]))
								+ String.format("%02d", Integer.parseInt(tmpDate[2]))) - 19110000;
					}

					// 金額
					tmp = (String) map.get("f6");
					tmp = tmp.trim().replace("\"", "");
					tamt = Integer.valueOf(tmp);

					t7 += tamt;
					// 排除低於當月份的
//					this.info("total=" + t7);
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

		// 暫存金額
		int[] tempAmtPos = { t1, t2, t3, t4, t5, t6 };
		int[] rank = { 1, 2, 3, 4, 5, 6 };

		//判斷各區帳齡金額 由大到小排列
		for (int i = 0; i < tempAmtPos.length; i++) {
			for (int j = 0; j < tempAmtPos.length; j++) {
				if (tempAmtPos[i] > tempAmtPos[j]) {
//					int temp = tempAmtPos[i];
//					tempAmtPos[i] = tempAmtPos[j];
//					tempAmtPos[j] = temp;
					int rankTemp = rank[i];
					rank[i] = rank[j];
					rank[j] = rankTemp;
				}
			}
		}

		// print的row至少要大於0，才能讓getNowpage()=1，才可以newPage()換頁
		// this.print(1, 0, "");
		// newPage();

		// 輸出報表
		report(rank);

		// 輸出結束
		this.close();

		return true;

	}
	/**
	 * 輸出報表
	 * @param rank 帳齡排序
	 * */
	private void report(int[] rank) {
		String tmp = "";
		String iCALDY = String.valueOf(Integer.valueOf(titaVo.get("CALDY")) + 19110000);
		this.setCharSpaces(0);

		// 以下為字體大小12 以12字體 橫的最右為140
		this.setFontSize(12);

		this.print(-2, 138, "機密等級：密", "R");
		this.print(-3, 138, "製表日期：" + iCALDY.substring(0, 4) + "/" + iCALDY.substring(4, 6) + "/" + iCALDY.substring(6),
				"R");
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

		tmp = iday4_1.substring(0, 3) + "/" + iday4_1.substring(3, 5) + "~" + iday4.substring(0, 3) + "/"
				+ iday4.substring(3, 5) + "月";
		this.print(-15 - startRow, 24, "4~6個月(" + tmp + ")", "C");

		tmp = iday5_1.substring(0, 3) + "/" + iday5_1.substring(3, 5) + "~" + iday5.substring(0, 3) + "/"
				+ iday5.substring(3, 5) + "月";
		this.print(-17 - startRow, 24, "7~12個月(" + tmp + ")", "C");
		this.print(-19 - startRow, 24, "1年以上", "C");
		this.print(-21 - startRow, 24, "合           計", "C");

		// 金額及比例部分
		if (t7 == 0) {
			return;
		}

		// 印製各區間金額
		this.print(-9 - startRow, 58, showAmt(t1), "R");
		this.print(-11 - startRow, 58, showAmt(t2), "R");
		this.print(-13 - startRow, 58, showAmt(t3), "R");
		this.print(-15 - startRow, 58, showAmt(t4), "R");
		this.print(-17 - startRow, 58, showAmt(t5), "R");
		this.print(-19 - startRow, 58, showAmt(t6), "R");
		this.print(-21 - startRow, 58, showAmt(t7), "R");

		// 各帳齡區間比例
		int pt1 = (int) Math.floor(t1 * 100.00 / t7);// 30日以下
		int pt2 = (int) Math.floor(t2 * 100.00 / t7);// 30~60日
		int pt3 = (int) Math.floor(t3 * 100.00 / t7);// 60~90日
		int pt4 = (int) Math.floor(t4 * 100.00 / t7);// 4~6個月
		int pt5 = (int) Math.floor(t5 * 100.00 / t7);// 7~12個月
		int pt6 = (int) Math.floor(t6 * 100.00 / t7);// 1年以上

		// 比例總和 暫存
		int t = 0;

		t = pt1 + pt2 + pt3 + pt4 + pt5 + pt6;

		int tempT = 0;

		// 如果% 總和不滿100%
		if (t != 100) {
			tempT = 100 - t;
			//根據排序分配餘數
			for (int no = 0; no < tempT; no++) {
				switch (rank[no]) {
				case 1:
					pt1 = pt1 + 1;
					break;
				case 2:
					pt2 = pt2 + 1;
					break;
				case 3:
					pt3 = pt3 + 1;
					break;
				case 4:
					pt4 = pt4 + 1;
					break;
				case 5:
					pt5 = pt5 + 1;
					break;
				case 6:
					pt6 = pt6 + 1;
					break;
				default:
					break;
				}
			}
			// 再所有%總和
			t = pt1 + pt2 + pt3 + pt4 + pt5 + pt6;
		}

		// 印製各區間比例
		this.print(-9 - startRow, 68, showP(pt1), "C");
		this.print(-11 - startRow, 68, showP(pt2), "C");
		this.print(-13 - startRow, 68, showP(pt3), "C");
		this.print(-15 - startRow, 68, showP(pt4), "C");
		this.print(-17 - startRow, 68, showP(pt5), "C");
		this.print(-19 - startRow, 68, showP(pt6), "C");
		this.print(-21 - startRow, 68, showP(t), "C");
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

//	private void tot(int amt) {
//		double d = 0.00;
//
//		t += amt;
//		// t7為總金額
//		if (t == t7) {
//			p = 100 - tp;
//		} else {
//			d = Math.round(amt * 100.00 / t7);
//			p = (int) d;
//		}
//		tp += p;
//	}

	private String showAmt(int amt) {
		if (amt == 0) {
			return "-";
		}
		return String.format("%,d", amt);
	}

	private String showP(int p) {
		return String.valueOf(p) + "％";
	}

}
