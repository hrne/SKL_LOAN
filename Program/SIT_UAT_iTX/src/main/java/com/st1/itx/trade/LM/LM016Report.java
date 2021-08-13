package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM016ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM016Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM016Report.class);

	@Autowired
	public LM016ServiceImpl lM016ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

//	自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);
	}

	public void printHeaderL() {
		this.print(-3, 5, "程式ID：" + this.getParentTranCode());
		this.print(-3, 50, "新光人壽保險股份有限公司", "C");
		this.print(-4, 5, "報  表：" + this.getRptCode());
		this.print(-4, 41, "寬限條件控管繳息列印");
		String bcDate = dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8)
				+ "/" + dDateUtil.getNowStringBc().substring(2, 4);
		this.print(-3, 80, "日　　期：" + bcDate);
		this.print(-4, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-5, 80, "頁　　次：　　" + this.getNowPage());
	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		// 戶號區間 Min
		String custNoMin = titaVo.getParam("CustNoMin");

		// 戶號區間 Max
		String custNoMax = titaVo.getParam("CustNoMax");

		this.info("LM016Report custNoMin " + custNoMin + " custNoMax " + custNoMax);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM016", "寬限條件控管繳息", "", "", "P");

		List<Map<String, String>> LM016List = null;

		try {
			LM016List = lM016ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lM016ServiceImpl.findAll error = " + e.toString());
		}

		if (LM016List != null && LM016List.size() > 0) {
			DecimalFormat df1 = new DecimalFormat("#,##0");

			this.print(-6, 4, "借款人戶號　... " + custNoMin + "－" + custNoMax);
			this.print(-8, 4, "戶號　　　　戶名　　　　　額度　　　寬限到期日　　　繳息迄日　　　　　　　貸出金額　　　使用碼");
			this.print(-9, 3, "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");

			for (Map<String, String> tLM016Vo : LM016List) {
				// 戶號
				print(1, 3, padStart(tLM016Vo.get("F0"), 7, "0"));

				// 戶名
				print(0, 14, tLM016Vo.get("F1"));

				// 額度
				print(0, 28, padStart(tLM016Vo.get("F2"), 3, "0"));

				// 寬限到期日
				print(0, 37, tLM016Vo.get("F4") == "0" || tLM016Vo.get("F4") == null || tLM016Vo.get("F4").length() == 0
						|| tLM016Vo.get("F4").equals(" ") ? " " : showDate(tLM016Vo.get("F4"), 1));

				// 繳息迄日
				print(0, 52, tLM016Vo.get("F4") == "0" || tLM016Vo.get("F3") == null || tLM016Vo.get("F3").length() == 0
						|| tLM016Vo.get("F3").equals(" ") ? " " : showDate(tLM016Vo.get("F3"), 1));

				// 貸出金額
				BigDecimal f5 = tLM016Vo.get("F5") == "0" || tLM016Vo.get("F5") == null
						|| tLM016Vo.get("F5").length() == 0 || tLM016Vo.get("F5").equals(" ") ? BigDecimal.ZERO
								: new BigDecimal(tLM016Vo.get("F5"));

				print(0, 79, f5.equals(BigDecimal.ZERO) ? " " : df1.format(f5), "R");

				// 使用碼
				print(0, 87, tLM016Vo.get("F6"));

				// 檢查列數
				checkRow(custNoMin, custNoMax);
			}

		} else {

			this.print(1, 3, "本日無資料");
			this.print(-6, 4, "借款人戶號　... " + custNoMin + "－" + custNoMax);
			this.print(-8, 4, "戶號　　　　戶名　　　　　額度　　　寬限到期日　　　繳息迄日　　　　　　　貸出金額　　　使用碼");
			this.print(-9, 3, "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");

		}

		long sno = this.close();
		this.toPdf(sno);

		if (LM016List != null && LM016List.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

	/**
	 * 檢查列數
	 * 
	 * @param cnMin 借款人戶號 區間-起
	 * @param cnMax 借款人戶號 區間-止
	 */
	private void checkRow(String cnMin, String cnMax) {
		if (this.NowRow >= 60) {

			newPage();
			this.print(-6, 4, "借款人戶號　... " + cnMin + "－" + cnMax);
			this.print(-8, 4, "戶號　　　　戶名　　　　　額度　　　寬限到期日　　　繳息迄日　　　　　　　貸出金額　　　使用碼");
			this.print(-9, 3, "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
		}

	}

	private String showDate(String date, int iType) {
//		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
//		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);

		if (rocdatex.length() == 7) {
			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
		} else {
			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);

		}

	}

}
