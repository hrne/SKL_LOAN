package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9743ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9743Report extends MakeReport {

	@Autowired
	L9743ServiceImpl l9743ServiceImpl;

	@Autowired
	Parse parse;

	private String custNo = "";
	private String chequeName = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void printHeaderP() {
		this.print(-1, 108, "新 光 人 壽 保 險 股 份 有 限 公 司", "R");
		this.print(-3, 89, "_  還 本", "R");
		this.print(-4, 109, "收 據 ( 支 票 件 )", "R");
		this.print(-5, 89, "_  繳 息", "R");
		this.print(-7, 1, " 借 款 人 戶 號  . . . ");
		this.print(-7, 25, custNo, "R");
		this.print(-7, 27, chequeName);
		this.print(-9, 1, "支 票 帳 號　　支 票 號 碼　　　　支 票 金 額 　　　收 票 日　　　　到 期 日　　支 票 銀 行　　支 票 分 行");

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {
		// 讀取VAR參數
		this.setCharSpaces(0);

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("L9743").setRptItem("暫收支票收據列印(個人戶)").setSecurity("").setRptSize("A4")
				.setPageOrientation("L").build();

		this.open(titaVo, reportVo);

		int temp = 0;
		List<Map<String, String>> listL9743 = null;
		try {
			listL9743 = l9743ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L9743erviceImpl.findAll error = " + e.toString());
		}

		if (listL9743 != null && !listL9743.isEmpty()) {

			if (listL9743.get(0).get("F0").isEmpty()) { // isEmpty()只有在0的時候會返回true
				custNo = "";
			} else {
				custNo = listL9743.get(0).get("F0");
			}

			if (listL9743.get(0).get("F1").isEmpty()) {
				chequeName = "";
			} else {
				chequeName = listL9743.get(0).get("F1");
			}

			String tempNo = "";
			String tempName = "";
			for (int i = 0; i < listL9743.size(); i++) {

				if (listL9743.get(i).get("F0") != null && !listL9743.get(i).get("F0").isEmpty()) {
					tempNo = listL9743.get(i).get("F0");
				} else {
					tempNo = "";
				}

				if (listL9743.get(i).get("F1") != null && !listL9743.get(i).get("F1").isEmpty()) {
					tempName = listL9743.get(i).get("F1");
				} else {
					tempName = "";
				}

				if (!custNo.equals(tempNo) || !chequeName.equals(tempName)) {
					// 有項目不一樣, 換頁

					printSum(temp);
					temp = 0;
					custNo = tempNo;
					chequeName = tempName;
					newPage();
				}

				this.print(1, 1, listL9743.get(i).get("F2"));
				this.print(0, 16, listL9743.get(i).get("F3"));

				BigDecimal f4 = getBigDecimal(listL9743.get(i).get("F4"));

				this.print(0, 46, formatAmt(f4, 0), "R");

				this.print(0, 60, this.showRocDate(listL9743.get(i).get("F5"), 1), "R");
				this.print(0, 76, this.showRocDate(listL9743.get(i).get("F6"), 1), "R");

				if (listL9743.get(i).get("F7") != null && listL9743.get(i).get("F7").length() >= 4) {
					this.print(0, 92, listL9743.get(i).get("F7").substring(0, 4), "R");
				} else {
					this.print(0, 92, listL9743.get(i).get("F7"), "R");
				}
				this.print(0, 107, listL9743.get(i).get("F8"), "R");
				temp++;

				CheckRow();

				if (i == listL9743.size() - 1) {
					printSum(temp);
					CheckRow();

				}
			}

		} else {
			// 本日無資料
			this.print(-7, 1, " 借 款 人 戶 號  . . . ");
			this.print(-7, 25, custNo, "R");
			this.print(-7, 27, chequeName);
			this.print(-9, 1, "支 票 帳 號　　支 票 號 碼　　　　支 票 金 額 　　　收 票 日　　　　到 期 日　　支 票 銀 行　　支 票 分 行");
			this.print(1, 1, "本日無資料	");
			this.print(1, 50, "===== 報 表 結 束 =====", "C");
		}

		this.close();
		// this.toPdf(sno);

		return true;
	}

	private void printSum(int temp) {
		this.print(1, 1, "");
		this.print(1, 1, "共計 " + parse.IntegerToString(temp, 3) + " 件");
		this.print(1, 50, "===== 報 表 結 束 =====", "C");
	}

	// 預設列印格式
	private void CheckRow() {
		if (this.NowRow >= 30) {
			newPage();
		}

	}

}
