package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9710ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component("L9710Report")
@Scope("prototype")

public class L9710Report extends MakeReport {

	@Autowired
	L9710ServiceImpl l9710ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	// 製表日期
//	private String nowDate;
	// 製表時間
//	private String nowTime;

	String f0 = "";
	String f1 = "";
	int ptfg = 0;
	int cnt = 0;
	int tcnt = 0;
	int amt = 0;
	int tamt = 0;

	int tempCount = 0;

	// 橫式規格
	@Override
	public void printHeader() {

		this.setFontSize(10);
		this.setCharSpaces(0);
		this.print(-1, 146, "機密等級：普通");
		this.print(-2, 3, "　程式ID：" + this.getParentTranCode());
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		this.print(-3, 3, "　報　表：" + this.getRptCode());
		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));

		this.print(-2, 146, "日　  期：" + tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/"
				+ dateUtil.getNowStringBc().substring(2, 4));
		this.print(-3, 80, "寬限到期明細表", "C");
		this.print(-3, 146, "時　  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 146, "頁　  次：");
		this.print(0, 160, Integer.toString(this.getNowPage()), "R");
		this.print(-5, 80,
				showRocDate(titaVo.get("ACCTDATE_ST"), 0) + " － " + showRocDate(titaVo.get("ACCTDATE_ED"), 0), "C");
		this.print(0, 146, "單　  位：元");

//		this.print(-7, 1, "地區別  經辦      戶號       戶名      核准號碼  寬限到期日     核准額度     貸放餘額 首次撥款日  上次繳息日 計息利率  聯絡電話      聯絡人　   下次還本日");
		this.print(-7, 1, "擔保品地區別");
		this.print(-7, 15, "經辦");
		this.print(-7, 23, "戶號");
		this.print(-7, 37, "戶名");
		this.print(-7, 46, "核准號碼");
		this.print(-7, 57, "寬限到期日");
		this.print(-7, 72, "核准額度");
		this.print(-7, 84, "貸放餘額");
		this.print(-7, 94, "首次撥款日");
		this.print(-7, 107, "上次繳息日");
		this.print(-7, 120, "計息利率");
		this.print(-7, 133, "連絡電話");
		this.print(-7, 145, "聯絡人");
		this.print(-7, 154, "下次還本日");
		this.print(-8, 1,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	// 直式規格
//	public void printHeader() {
//
//		this.setFontSize(8);
//		this.setCharSpaces(0);
//		this.print(-1, 149, "機密等級：普通");
//		this.print(-2, 3, "　程式ID：" + this.getParentTranCode());
//		this.print(-2, 64, "新光人壽保險股份有限公司");
//		this.print(-3, 3, "　報　表：" + this.getRptCode());
//		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));
//		this.print(-2, 149, "日　  期：" + tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/"
//				+ dateUtil.getNowStringBc().substring(2, 4));
//		this.print(-3, 69, "寬限到期明細表");
//		this.print(-3, 149, "時　  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
//				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
//		this.print(-4, 149, "頁　  次：");
//		this.print(0, 164, Integer.toString(this.getNowPage()), "R");
//		this.print(-5, 75,
//				showRocDate(titaVo.get("ACCTDATE_ST"), 2) + " － " + showRocDate(titaVo.get("ACCTDATE_ED"), 2), "C");
//		this.print(0, 149, "單　  位：元");
//
//		this.print(-7, 1,
//				" 站別  押品地區別  經辦          戶號       戶名      核准號碼  寬限到期日       核准額度       貸放餘額 首次撥款日  上次繳息日 計息利率  聯絡電話        聯絡人　   下次還本日");
//		this.print(-8, 1,
//				"-------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//		// 明細起始列(自訂亦必須)
//		this.setBeginRow(9);
//
//		// 設定明細列數(自訂亦必須)
//		this.setMaxRows(45);
//	}

	public List<Map<String, String>> exec(TitaVo titaVo, int iAcDate) throws LogicException {

		this.info("L9710Report exec");

		List<Map<String, String>> l9710List = null;

		try {

			l9710List = l9710ServiceImpl.findAll(titaVo, iAcDate);

		} catch (Exception e) {

			this.info("L9710ServiceImpl.LoanBorTx error = " + e.toString());

		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9710", "寬限到期明細表", "", "A4", "L");

		// 記錄筆數
		int count = 0;
		int divderCount = 0;

		if (l9710List != null && l9710List.size() != 0) {

			// 輸出Excel

			for (Map<String, String> tL9710Vo : l9710List) {

				count++;
//				if (!f0.equals(tL9710Vo.get("F0")) || (f0.equals("總公司") && !f1.equals(tL9710Vo.get("CityCode")))) {
				// 不同地區別
				if (!f1.equals(tL9710Vo.get("CityCode"))) {
					if (tempCount % 40 > 1) {
						divderCount++;
						reportTot();
						tempCount = (tempCount % 40) + (3 * divderCount);
					}
					if (f0.equals(tL9710Vo.get("F0"))) {
						ptfg = 0;
					}
					info("tempCount" + count + "=" + tempCount);
				}

//				f0 = tL9710Vo.get("F0");
				f1 = tL9710Vo.get("CityCode");

				// 小計 會加3行，超過40行 換新頁

				if (tempCount >= 40) {

					this.newPage();
//					count = 0 ;
				}

				report(tL9710Vo);
			}

			if (this.getNowPage() > 0 && count == l9710List.size()) {
				ptfg = 9;
				reportTot();
			}

		} else {
			this.print(1, 1, "本日無資料");
		}

		this.close();
		// this.toPdf(sno);
		return l9710List;

	}

	private void report(Map<String, String> tL9710Vo) throws LogicException {
		String tmp = "";
		tempCount++;
		info("tempCount=" + tempCount);
		// 押品地區別 (地區代號+地區名字)
		if (ptfg == 0) {
			this.print(1, 2, f0);
			ptfg = 1;
			this.print(0, 3, tL9710Vo.get("CityCode"));
		} else {
			this.print(1, 3, tL9710Vo.get("CityCode"));
		}
		this.print(0, 6, substr(tL9710Vo.get("CityItem"), 3));

		// 經辦
		this.print(0, 15, substr(tL9710Vo.get("Fullname"), 5));

		// 戶號(戶號+額度)
		tmp = String.format("%07d", Integer.valueOf(tL9710Vo.get("CustNo"))) + "-"
				+ String.format("%03d", Integer.valueOf(tL9710Vo.get("FacmNo")));
		this.print(0, 22, tmp);

		// 戶名
		this.print(0, 36, substr(tL9710Vo.get("CustName"), 5));

		// 核准號碼
		this.print(0, 47, String.format("%07d", Integer.valueOf(tL9710Vo.get("ApplNo"))));

		// 寬限到期日
		this.print(0, 67, showRocDate(tL9710Vo.get("GraceDate"), 1), "R");

		// 核准額度
		this.print(0, 80, showAmt(tL9710Vo.get("LineAmt")), "R");

		// 貸放餘額
		this.print(0, 92, showAmt(tL9710Vo.get("LoanBal")), "R");

		// 首次撥款日
		this.print(0, 104, showRocDate(tL9710Vo.get("FirstDrawdownDate"), 1), "R");

		// 上次繳息日
		this.print(0, 117, showRocDate(tL9710Vo.get("PrevPayIntDate"), 1), "R");

		// 計息利率
		this.print(0, 128, String.format("%.4f", Double.valueOf(tL9710Vo.get("StoreRate"))), "R");

		// 連絡電話
		this.print(0, 132, substr(tL9710Vo.get("TelNo"), 15));

		// 聯絡人
		this.print(0, 145, substr(tL9710Vo.get("LiaisonName"), 5));

		// 下次還本日
		this.print(0, 164, showRocDate(tL9710Vo.get("NextRepayDate"), 1), "R");

		cnt += 1;
		amt += Integer.valueOf(tL9710Vo.get("LoanBal"));
	}

	/**
	 * 小計(總計)筆數及合計核准額度
	 * 
	 */
	private void reportTot() {
		tempCount = tempCount + 3;
		this.print(1, 1,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 12, "小　計");
		this.print(0, 24, String.valueOf(cnt), "R");
		this.print(0, 25, "筆");
		this.print(0, 92, String.format("%,d", amt), "R");
		this.print(1, 1, "");
		tcnt += cnt;
		tamt += amt;
		cnt = 0;
		amt = 0;
		if (ptfg != 9) {
			return;
		}
		this.print(1, 1,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 12, "總　計");
		this.print(0, 24, String.valueOf(tcnt), "R");
		this.print(0, 25, "筆");
		this.print(0, 92, String.format("%,d", tamt), "R");
	}

	private String substr(String data, int iLen) {
		if (data == null) {
			return "";
		}
		if (data.length() > iLen) {
			return data.substring(0, iLen);
		} else {
			return data;
		}
	}

	private String showAmt(String xamt) {
		this.info("MakeReport.toPdf showRocDate1 = " + xamt);
		if (xamt == null || xamt.equals("") || xamt.equals("0")) {
			return "";
		}
		int amt = Integer.valueOf(xamt);
		return String.format("%,d", amt);
	}

}
