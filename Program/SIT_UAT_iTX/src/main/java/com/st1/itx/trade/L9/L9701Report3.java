package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")
public class L9701Report3 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9701Report3.class);

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String custName;
	private String facmNo;

	// 最大明細筆數
	private int maxDetailCnt = 50;

	/**
	 * 帳務日
	 */
	int entday = 0;

	BigDecimal txAmtTotal = BigDecimal.ZERO; // 交易金額合計
	BigDecimal tempAmtTotal = BigDecimal.ZERO; // 暫收轉帳合計
	BigDecimal repaidPrinTotal = BigDecimal.ZERO; // 償還本金合計
	BigDecimal repaidInterestTotal = BigDecimal.ZERO; // 償還利息合計
	BigDecimal repaidExpTotal = BigDecimal.ZERO; // 償還費用合計
	BigDecimal tempDiffTotal = BigDecimal.ZERO; // 暫收差額合計

	@Override
	public void printHeader() {

		this.setFontSize(9);
		this.info("L9701 exec" + this.titaVo.get("ENTDY"));
		this.print(-2, 86, "客 戶 往 來 交 易 明 細 表", "C");
		this.print(-3, 86, showRocDate(this.titaVo.get("ENTDY"), 0), "C");
		this.print(-4, 130, "印表日期：" + showRocDate(this.nowDate, 1) + " " + showTime(this.nowTime));
		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);
		if (iTYPE.equals("1")) {
			this.print(-5, 1, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-5, 1, "會計日期 : " + iSDAY + " - " + iEDAY);
		}
		this.print(-5, 130, "頁　　次：" + Integer.toString(this.getNowPage()));
		String iCUSTNO = titaVo.get("CustNo");
		if (this.facmNo.equals("")) {
			this.print(-6, 1, "戶號     : " + iCUSTNO);
		} else {
			this.print(-6, 1, "戶號     : " + iCUSTNO + " " + custName);
		}
		if (this.NowRow >= maxDetailCnt - 4) {
			// 若剩餘行數不足四行,先換頁
			this.newPage(); // 換頁時會印表頭
		} else {
			printDataHeader();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(maxDetailCnt);
	}

	/**
	 * 印資料表頭
	 */
	private void printDataHeader() {

		String tmpFacmNo = FormatUtil.pad9(facmNo, 3);

		this.print(1, 1, "額度     : " + tmpFacmNo);
		this.print(1, 5, "－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－");
		this.print(1, 5, "　入帳日　　　交易內容　　交易金額(Ａ)　暫收轉帳(Ｂ)　償還本金(ａ)　償還利息(ｂ)　償還費用(ｃ)　溢短收(Ｘ)　　暫收差額(Ｍ)");
		this.print(1, 5, "－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L9701Report3 exec");
		entday = titaVo.getEntDyI();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();


		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery3(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		this.custName = "";
		this.facmNo = "";

		if (listL9701 != null && listL9701.size() > 0) {

			this.custName = listL9701.get(0).get("F13");
			this.facmNo = listL9701.get(0).get("F10");
			
			this.open(titaVo, entday, titaVo.getKinbr(), "L9701_3", "客戶往來交易明細表", "", "A4", "L");
			
			boolean isFirst = true;

			for (Map<String, String> tL9701Vo : listL9701) {

				// 判斷額度號碼是否與前筆不同
				if (!this.facmNo.equals(tL9701Vo.get("F10"))) {

					// 若非首筆且額度號碼與前筆不同時
					if (!isFirst) {
						// 印合計
						printTotal();

						this.facmNo = tL9701Vo.get("F10");

						if (this.NowRow > 11 && this.NowRow < maxDetailCnt - 4) {
							// 印表頭
							printDataHeader();
						} else if (this.NowRow >= maxDetailCnt - 4) {
							// 若剩餘行數不足四行,先換頁
							this.newPage(); // 換頁時會印表頭
						}
					} else {
						this.facmNo = tL9701Vo.get("F10");
					}
				}

				// 印明細
				printDetail(tL9701Vo);

				isFirst = false;
			}

			if (!isFirst) {
				// 印合計
				printTotal();
			}
		} else {
			
			this.open(titaVo, entday, titaVo.getKinbr(), "L9701_3", "客戶往來交易明細表", "", "A4", "L");
			
			this.print(1, 20, "*******    查無資料   ******");
		}

		long sno = this.close();

		// 測試用
		this.toPdf(sno);
	}

	/**
	 * 印明細
	 * 
	 * @param tL9701Vo 單筆資料
	 */
	private void printDetail(Map<String, String> tL9701Vo) {
		String htyp = "";

		if (Integer.valueOf(tL9701Vo.get("F14")) > 0) {
			htyp = "訂正";
		}

		this.print(1, 1, htyp);

		this.print(0, 5, showRocDate(tL9701Vo.get("F0"), 1)); // 入帳日

		String tmp = tL9701Vo.get("F1");

		if (tmp.length() > 6) {
			tmp = tmp.substring(0, 6);
		}

		this.print(0, 17, tmp); // 交易內容 txAmtTotal = txAmtTotal.add(new BigDecimal(tL9701Vo.get("F2")));

		// 計算合計
		// 該筆為"非訂正"時,累加該筆金額
		// 該筆為"訂正"時,累減該筆金額
		if (htyp.isEmpty()) {
			tempAmtTotal = tempAmtTotal.add(new BigDecimal(tL9701Vo.get("F3")));
			repaidPrinTotal = repaidPrinTotal.add(new BigDecimal(tL9701Vo.get("F4")));
			repaidInterestTotal = repaidInterestTotal.add(new BigDecimal(tL9701Vo.get("F5")));
			repaidExpTotal = repaidExpTotal.add(new BigDecimal(tL9701Vo.get("F6")));
			tempDiffTotal = tempDiffTotal.add(new BigDecimal(tL9701Vo.get("F9")));
		} else {
			tempAmtTotal = tempAmtTotal.subtract(new BigDecimal(tL9701Vo.get("F3")));
			repaidPrinTotal = repaidPrinTotal.subtract(new BigDecimal(tL9701Vo.get("F4")));
			repaidInterestTotal = repaidInterestTotal.subtract(new BigDecimal(tL9701Vo.get("F5")));
			repaidExpTotal = repaidExpTotal.subtract(new BigDecimal(tL9701Vo.get("F6")));
			tempDiffTotal = tempDiffTotal.subtract(new BigDecimal(tL9701Vo.get("F9")));
		}

		this.print(0, 43, formatAmt(tL9701Vo.get("F2"), 0), "R"); // 交易金額

		BigDecimal f3 = new BigDecimal(tL9701Vo.get("F3"));
		this.print(0, 57, htyp.isEmpty() ? formatAmt(f3, 0) : formatAmt(BigDecimal.ZERO.subtract(f3), 0), "R"); // 暫收轉帳

		BigDecimal f4 = new BigDecimal(tL9701Vo.get("F4"));
		this.print(0, 71, htyp.isEmpty() ? formatAmt(f4, 0) : formatAmt(BigDecimal.ZERO.subtract(f4), 0), "R"); // 償還本金

		BigDecimal f5 = new BigDecimal(tL9701Vo.get("F5"));
		this.print(0, 85, htyp.isEmpty() ? formatAmt(f5, 0) : formatAmt(BigDecimal.ZERO.subtract(f5), 0), "R"); // 償還利息

		BigDecimal f6 = new BigDecimal(tL9701Vo.get("F6"));
		this.print(0, 99, htyp.isEmpty() ? formatAmt(f6, 0) : formatAmt(BigDecimal.ZERO.subtract(f6), 0), "R"); // 償還費用

		BigDecimal tmpAmt = new BigDecimal(tL9701Vo.get("F7"));

		if (tmpAmt.compareTo(BigDecimal.ZERO) < 0) {
			tmp = "短收";
			tmpAmt = tmpAmt.abs(); // 輸出時為絕對值
		} else if (tmpAmt.compareTo(BigDecimal.ZERO) > 0) {
			tmp = "溢收";
		} else {
			tmp = "";
		}

		this.print(0, 101, tmp);
		this.print(0, 113, htyp.isEmpty() ? formatAmt(tmpAmt, 0) : formatAmt(BigDecimal.ZERO.subtract(tmpAmt), 0), "R"); // 溢短收

		BigDecimal f9 = new BigDecimal(tL9701Vo.get("F9"));
		this.print(0, 127, htyp.isEmpty() ? formatAmt(f9, 0) : formatAmt(BigDecimal.ZERO.subtract(f9), 0), "R"); // 暫收差額
	}

	/**
	 * 印合計
	 */
	private void printTotal() {

		// 若合計無法印在一起,先換頁
		if (this.NowRow > maxDetailCnt - 2) {
			this.newPage();
		}

		this.print(1, 5, "－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－　－－－－－－");
		this.print(1, 43, formatAmt(txAmtTotal, 0), "R"); // 交易金額合計
		this.print(0, 57, formatAmt(tempAmtTotal, 0), "R"); // 暫收轉帳合計
		this.print(0, 71, formatAmt(repaidPrinTotal, 0), "R"); // 償還本金合計
		this.print(0, 85, formatAmt(repaidInterestTotal, 0), "R"); // 償還利息合計
		this.print(0, 99, formatAmt(repaidExpTotal, 0), "R"); // 償還費用合計

		this.print(0, 127, formatAmt(tempDiffTotal, 0), "R"); // 暫收差額

		// 合計歸零
		txAmtTotal = BigDecimal.ZERO;
		tempAmtTotal = BigDecimal.ZERO;
		repaidPrinTotal = BigDecimal.ZERO;
		repaidInterestTotal = BigDecimal.ZERO;
		repaidExpTotal = BigDecimal.ZERO;
		tempDiffTotal = BigDecimal.ZERO;
	}

}
