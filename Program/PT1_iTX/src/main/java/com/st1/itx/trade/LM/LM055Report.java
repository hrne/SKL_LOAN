package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM055ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Service
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM055Report extends MakeReport {

	@Autowired
	LM055ServiceImpl lM055ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	/*
	 * 用LM051的表 去分別做擔保品類別與： 1.逾期數的餘額表 2.資產五分類的餘額表
	 * 
	 * 放款種類： A.銀行保證放款 B.動產擔保放款 C.不動產抵押放款 D.有價證券質押放款 E.壽險貸款 F墊繳保費 Z.其他。
	 */

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info("LM055Report exec");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表_工作表", "LM055-A042放款餘額彙總表",
				"LM055_底稿_A042放款餘額彙總表.xlsx", "A042放款餘額彙總表");

		makeExcel.setValue(2, 3, yearMonth);

		try {

			fnAllList = lM055ServiceImpl.findAll(titaVo, yearMonth);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM055ServiceImpl.findAll error = " + errors.toString());

		}
		exportExcel(fnAllList);

		makeExcel.close();

	}

	private void exportExcel(List<Map<String, String>> listData) throws LogicException {

		int col = 0;
		int row = 0;

//		BigDecimal normalAmount = BigDecimal.ZERO;
//		BigDecimal specificAmount = BigDecimal.ZERO;
//		BigDecimal allowAmount = BigDecimal.ZERO;

		String type = "";
		int kind = 0;
		BigDecimal amount = BigDecimal.ZERO;

		for (Map<String, String> r : listData) {
			type = r.get("F0");
			kind = Integer.valueOf(r.get("F1"));
			amount = new BigDecimal(r.get("F2"));

			switch (type) {
			case "A":
				row = 8;
				break;
			case "B":
				row = 9;
				break;
			case "C":
				row = 10;
				break;
			case "D":
				row = 11;
				break;
			case "Z":
				row = 12;
				break;
			case "ZZ":
				row = 13;
				break;
			default:
				break;
			}
		}
		
		col = kind + 5;
		
		makeExcel.setValue(row, col, amount, "#,##0");

	}

	private void exportExcel2(List<Map<String, String>> listData) throws LogicException {

		int col = 0;
		int colAllow = 0;
		int row = 0;

		BigDecimal normalAmount = BigDecimal.ZERO;
		BigDecimal specificAmount = BigDecimal.ZERO;

		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal allowAmount = BigDecimal.ZERO;

		for (Map<String, String> lM055Vo : listData) {

			/*
			 * COL 1=逾期放款(F) 2=未列入逾期應予評估放款(G) 3=正常放款I(H) 4=應予注意II(I) 5=可望收回III(J)
			 * 6=收回困難IV(K) 7=收回無望V(L)
			 * 
			 * 99=購置住宅+修繕貸款
			 * 
			 */

			// 起始欄E欄位 + (1~7)
			// (自訂 FIVE=五類資產、9=備呆子目)
			if (!lM055Vo.get("F0").equals("N")) {

				if (lM055Vo.get("F0").equals("FIVE") || lM055Vo.get("F0").equals("9")) {
					col = 19;
				} else if (!lM055Vo.get("F0").equals("99")) {
					col = 5 + Integer.valueOf(lM055Vo.get("F0"));
					colAllow = 10 + Integer.valueOf(lM055Vo.get("F0"));
				}

				// 依放款種類 區分列數
				// (自訂 FIVE=五類資產、AL=備呆子目)
				row = lM055Vo.get("F1").equals("C") ? 10
						: (lM055Vo.get("F1").equals("D") ? 11
								: (lM055Vo.get("F1").equals("FIVE") || lM055Vo.get("F1").equals("AL") ? 16 : 12));

				// 放款金額
				if (!lM055Vo.get("F0").equals("99")) {

					amount = lM055Vo.get("F2").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM055Vo.get("F2"));

					makeExcel.setValue(row, col, amount, "#,##0");
				}

				// 備抵損失
				if (lM055Vo.get("F0").equals("3")) {

					normalAmount = lM055Vo.get("F3").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM055Vo.get("F3"));

				} else if (lM055Vo.get("F0").equals("99")) {

					specificAmount = lM055Vo.get("F3").equals("0") ? BigDecimal.ZERO
							: new BigDecimal(lM055Vo.get("F3"));

					allowAmount = specificAmount.add(normalAmount);

					makeExcel.setValue(row, 13, allowAmount, "#,##0");

				} else {

					allowAmount = lM055Vo.get("F3").equals("0") ? BigDecimal.ZERO : new BigDecimal(lM055Vo.get("F3"));

					makeExcel.setValue(row, colAllow, allowAmount, "#,##0");

				}
			}

		}

		// F15~S15
		for (int i = 6; i <= 19; i++) {
			makeExcel.formulaCaculate(15, i);
		}
		// R16、R17
		makeExcel.formulaCaculate(18, 16);
		makeExcel.formulaCaculate(18, 17);

	}

}