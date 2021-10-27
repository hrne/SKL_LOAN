package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD005ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LD005Report extends MakeReport {

	@Autowired
	public LD005ServiceImpl lD005ServiceImpl;
	
	@Autowired
	Parse parse;

	private String CustNo = "";
	private String ChequeName = "";

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
		this.print(-7, 25, CustNo, "R");
		this.print(-7, 27, ChequeName);
		this.print(-9, 1, "支 票 帳 號　　支 票 號 碼　　　　支 票 金 額 　　　收 票 日　　　　到 期 日　　支 票 銀 行　　支 票 分 行");

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {
		// 讀取VAR參數
		this.setCharSpaces(0);
		this.open(titaVo
				, titaVo.getEntDyI()
				, titaVo.getKinbr()
				, "LD005"
				, "暫收支票收據列印(個人戶)"
				, ""
				, "A4"
				, "L");

		int temp = 0;
		List<Map<String, String>> LD005List = null;
		try {
			LD005List = lD005ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lD005ServiceImpl.findAll error = " + e.toString());
		}

		if (LD005List != null && !LD005List.isEmpty()) {

			if (LD005List.get(0).get("F0").isEmpty()) { // isEmpty()只有在0的時候會返回true
				CustNo = "";
			} else {
				CustNo = LD005List.get(0).get("F0");
			}

			if (LD005List.get(0).get("F1").isEmpty()) {
				ChequeName = "";
			} else {
				ChequeName = LD005List.get(0).get("F1");
			}

			String tempNo = "";
			String tempName = "";
			for (int i = 0; i < LD005List.size(); i++) {

				if (LD005List.get(i).get("F0") != null && !LD005List.get(i).get("F0").isEmpty()) {
					tempNo = LD005List.get(i).get("F0");
				} else {
					tempNo = "";
				}

				if (LD005List.get(i).get("F1") != null && !LD005List.get(i).get("F1").isEmpty()) {
					tempName = LD005List.get(i).get("F1");
				} else {
					tempName = "";
				}

				if (!CustNo.equals(tempNo) || !ChequeName.equals(tempName)) {
					// 有項目不一樣, 換頁
					
					printSum(temp);
					temp = 0;
					CustNo = tempNo;
					ChequeName = tempName;
					newPage();
				}

				this.print(1, 1, LD005List.get(i).get("F2"));
				this.print(0, 16, LD005List.get(i).get("F3"));

				BigDecimal f4 = getBigDecimal(LD005List.get(i).get("F4"));
				
				this.print(0, 45, formatAmt(f4, 0), "R");

				this.print(0, 52, this.showRocDate(LD005List.get(i).get("F5"), 1));
				this.print(0, 68, this.showRocDate(LD005List.get(i).get("F6"), 1));

				if (LD005List.get(i).get("F7") != null && LD005List.get(i).get("F7").length() >= 4) {
					this.print(0, 82, LD005List.get(i).get("F7").substring(0, 4));
				} else {
					this.print(0, 82, LD005List.get(i).get("F7"));
				}
				this.print(0, 98, LD005List.get(i).get("F8"));
				temp++;

				CheckRow();

				if (i == LD005List.size() - 1) {
					printSum(temp);
					CheckRow();

				}
			}

		} else {
			// 本日無資料
			this.print(-7, 1, " 借 款 人 戶 號  . . . ");
			this.print(-7, 25, CustNo, "R");
			this.print(-7, 27, ChequeName);
			this.print(-9, 1, "支 票 帳 號　　支 票 號 碼　　　　支 票 金 額 　　　收 票 日　　　　到 期 日　　支 票 銀 行　　支 票 分 行");
			this.print(1, 1, "本日無資料	");
			this.print(1, 50, "===== 報 表 結 束 =====", "C");
		}

		long sno = this.close();
		this.toPdf(sno);

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
			this.print(-7, 1, " 借款人戶號  . . . ");
			this.print(-7, 25, CustNo, "R");
			this.print(-7, 27, ChequeName);
			this.print(-9, 1, "支票帳號　　支票號碼　　　　支票 額　　　　收票日　　　　到期日　　支票銀行　　支票分行");
		}

	}

}
