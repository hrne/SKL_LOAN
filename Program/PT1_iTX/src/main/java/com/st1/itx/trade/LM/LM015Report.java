package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM015ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM015Report extends MakeReport {

	@Autowired
	LM015ServiceImpl lM015ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		this.print(-2, 80, "機密等級：密");
		this.print(-3, 1, "程式ID：" + this.getParentTranCode());
		this.print(-3, 50, "新光人壽保險股份有限公司", "C");
		this.print(-4, 1, "報  表：" + this.getRptCode());
		this.print(-4, 50, "信用曝險分佈報表", "C");
//		String bcDate=dDateUtil.getNowStringBc().substring(6, 8)+"/"+dDateUtil.getNowStringBc().substring(4, 6)+"/"+dDateUtil.getNowStringBc().substring(2, 4);
		this.print(-3, 80, "日　　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		this.print(-4, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-5, 80, "頁　　次：" + this.getNowPage());
		String yearMon = this.showRocDate(this.getReportDate());
		this.print(-6, 50, "年月份：" + yearMon.substring(0, 7), "C");
		this.print(-6, 80, "單　　位：元");

		this.setBeginRow(8);
		this.setMaxRows(35);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM015", "信用曝險分佈報表", "密", "A4", "P");
		List<Map<String, String>> lm015List = new ArrayList<>();

		try {
			lm015List = lM015ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			this.info("lM015ServiceImpl.findAll error = " + e.toString());
		}
		// Ted 2021/01/22
		if (lm015List.size() > 0) {
			// 格式
			DecimalFormat df1 = new DecimalFormat("#,##0");

			// SELECT 資料已有各區統計值
			// 擔保放款
			BigDecimal[] totalH0 = new BigDecimal[4];
			// 催收款
			BigDecimal[] totalH1 = new BigDecimal[4];
			// 擔保放款和催收款的合計
			BigDecimal[] totalV = new BigDecimal[2];
			this.print(1, 1, "擔保品坐落區域　　　　 　　北區　　　　　　　中區　　　　　　　南區　　　　　　　東區　　　　　　　合計");
			this.print(1, 1, "────────────────────────────────────────────────────");

			String temptt0 = "0";
			String temptt1 = "1";
			boolean printT = true;

			for (Map<String, String> tLDVo : lm015List) {
				// 擔保放款or催收 區分
				String f0 = tLDVo.get("F0") == null || tLDVo.get("F0").length() == 0 ? "" : tLDVo.get("F0");

				// 區域
				String f1 = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? "" : tLDVo.get("F1");

				// 擔保放款or催收 金額
				BigDecimal f3 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

				// 相同為0=擔保放款 反之為1= 催收款
				if (temptt0.equals(f0)) {
					// SQL有排序 所以只會印一次
					if (printT) {
						this.print(1, 2, "擔保放款");
						printT = false;
					}
					// 區域
					switch (f1) {
					case "A":
						this.print(0, 29, df1.format(f3), "R");
						totalH0[0] = f3;
						break;
					case "B":
						this.print(0, 46, df1.format(f3), "R");
						totalH0[1] = f3;
						break;
					case "C":
						this.print(0, 62, df1.format(f3), "R");
						totalH0[2] = f3;
						break;
					case "D":
						this.print(0, 79, df1.format(f3), "R");
						totalH0[3] = f3;

						totalV[0] = totalH0[0].add(totalH0[1]).add(totalH0[2]).add(totalH0[3]);
						this.print(0, 95, df1.format(totalV[0]), "R");
						break;
					// 都不是則作合計
					default:

						break;
					}
				} else if (temptt1.equals(f0)) {
					if (!printT) {
						this.print(1, 2, "催收款");
						printT = true;
					}
					switch (f1) {
					case "A":
						this.print(0, 29, df1.format(f3), "R");
						totalH1[0] = f3;
						break;
					case "B":
						this.print(0, 46, df1.format(f3), "R");
						totalH1[1] = f3;
						break;
					case "C":
						this.print(0, 62, df1.format(f3), "R");
						totalH1[2] = f3;
						break;
					case "D":
						this.print(0, 79, df1.format(f3), "R");
						totalH1[3] = f3;

						totalV[1] = totalH1[0].add(totalH1[1]).add(totalH1[2]).add(totalH1[3]);
						this.print(0, 95, df1.format(totalV[1]), "R");

						break;
					default:
						break;
					}

				}

			}

			this.print(1, 2, "合計");

			this.print(0, 29, df1.format(totalH0[0].add(totalH1[0])), "R");
			this.print(0, 46, df1.format(totalH0[1].add(totalH1[1])), "R");
			this.print(0, 62, df1.format(totalH0[2].add(totalH1[2])), "R");
			this.print(0, 79, df1.format(totalH0[3].add(totalH1[3])), "R");

			this.print(0, 95, df1.format(totalV[0].add(totalV[1])), "R");
			this.print(1, 1, "────────────────────────────────────────────────────");
			this.print(1, 2, "佔整體比率                  %                %               %                %               %");

			this.print(0, 29, totalH0[0].add(totalH1[0]).multiply(new BigDecimal("100")).divide(totalV[0].add(totalV[1]), 2, BigDecimal.ROUND_HALF_UP) + "", "R");
			this.print(0, 46, totalH0[1].add(totalH1[1]).multiply(new BigDecimal("100")).divide(totalV[0].add(totalV[1]), 2, BigDecimal.ROUND_HALF_UP) + "", "R");
			this.print(0, 62, totalH0[2].add(totalH1[2]).multiply(new BigDecimal("100")).divide(totalV[0].add(totalV[1]), 2, BigDecimal.ROUND_HALF_UP) + "", "R");
			this.print(0, 79, totalH0[3].add(totalH1[3]).multiply(new BigDecimal("100")).divide(totalV[0].add(totalV[1]), 2, BigDecimal.ROUND_HALF_UP) + "", "R");

			this.print(0, 95, "100.00", "R");
		} else {
			noData();
		}
		this.close();
		// this.toPdf(sno);
	}

	// 無資料列印格式
	private void noData() {
		this.print(1, 1, "擔保品坐落區域　　　　 　　北區　　　　　　　中區　　　　　　　南區　　　　　　　東區　　　　　　　合計");
		this.print(1, 1, "────────────────────────────────────────────────────");
		this.print(1, 2, "擔保放款");
		this.print(0, 29, "本日無資料");
		this.print(1, 2, "催收款");
		this.print(1, 2, "合計");
		this.print(1, 1, "────────────────────────────────────────────────────");
		this.print(1, 2, "佔整體比率                  %                %               %                %               %");
	}
}
