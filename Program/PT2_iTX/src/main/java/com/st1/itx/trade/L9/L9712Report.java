package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9712ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class L9712Report extends MakeReport {

	@Autowired
	L9712ServiceImpl l9712ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	private int totalInterestReceive = 0;
	private int totalBreachAmtReceive = 0;
	private int totalInterestGrace = 0;
	private int totalBreachAmtGrace = 0;

	@Override
	public void printHeader() {

		this.setFontSize(10);
		this.setCharSpaces(0);

		this.print(-1, 146, "機密案件："+this.getSecurity());
		this.print(-2, 3, "程式ID：" + this.getParentTranCode());
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		this.print(-3, 3, "報  表：" + this.getRptCode());
		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));
		this.print(-2, 146, "日　　期：" + tim + "/" + dDateUtil.getNowStringBc().substring(6) + "/" + dDateUtil.getNowStringBc().substring(2, 4));

		this.print(-3, 80, "利息違約金減免明細表", "C");

		this.print(-3, 146, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 146, "頁　　次：");
		this.print(0, 160, Integer.toString(this.getNowPage()), "R");

		this.print(-5, 80, showRocDate(titaVo.get("AcDateMin"), 1) + " - " + showRocDate(titaVo.get("AcDateMax"), 1), "C");
		this.print(-5, 146, "單　　位：元");

		this.print(-7, 84, "應    收                               減    免");
		this.print(-8, 73, "-----------------------------            ---------------------------");
		this.print(-9, 1, "   會計日      業務科目          戶號           戶名                        利息             違約金                利息             違約金  　 授權主管       備註");
		this.print(-10, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L9712Report exec");

		String tradeNo = "L9712";
		String tradeName = "利息違約金減免明細表";
		String brno =titaVo.getBrno();


		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(brno)
				.setRptCode(tradeNo).setRptItem(tradeName).setRptSize("A4")
				.setPageOrientation("L").build();

		this.open(titaVo, reportVo);

		try {
			List<Map<String, String>> l9712List = l9712ServiceImpl.findAll(titaVo);

			this.info("L9712Report findAll =" + l9712List.toString());
			if (l9712List.size() > 0) {
				for (Map<String, String> tL9712Vo : l9712List) {
					report1(tL9712Vo);
				}

				// 合計

				this.print(1, 1,
						"----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 37, "合　　　計");
				this.print(0, 82, String.format("%,d", totalInterestReceive), "R");
				this.print(0, 102, String.format("%,d", totalBreachAmtReceive), "R");
				this.print(0, 120, String.format("%,d", totalInterestGrace), "R");
				this.print(0, 140, String.format("%,d", totalBreachAmtGrace), "R");
				this.print(1, 1,
						"----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			} else {
				drawLine(0, 0, 0, 0);
				this.print(1, 3, "本日無資料");
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9712ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		this.close();

		//this.toPdf(sno);
	}

	private void report1(Map<String, String> tL9712Vo) throws LogicException {
		String tmp;
		// 會計日
		this.print(1, 2, showRocDate(tL9712Vo.get("F0"), 1));

		// 擔保放款
		this.print(0, 16, "擔保放款");

		// 戶號(戶號+額度)
		tmp = String.format("%07d", Integer.valueOf(tL9712Vo.get("F1"))) + "-" + String.format("%03d", Integer.valueOf(tL9712Vo.get("F2")));
		this.print(0, 33, tmp);

		// 戶名
		tmp = tL9712Vo.get("F3");

		if (tmp.length() > 6) {
			tmp = tmp.substring(0, 6);
		}
		this.print(0, 49, tmp);

		// 應收 利息
		this.print(0, 82, showAmt(tL9712Vo.get("F4")), "R");
		totalInterestReceive += Integer.parseInt((tL9712Vo.get("F4")));

		// 應收 違約金
		this.print(0, 102, showAmt(tL9712Vo.get("F5")), "R");
		totalBreachAmtReceive += Integer.parseInt((tL9712Vo.get("F5")));

//		double amt = Double.parseDouble(tL9712Vo.get("F7"));
		//減免 利息
		int amt1 = Integer.valueOf(tL9712Vo.get("F6"));
		//減免 違約金
		int amt2 = Integer.valueOf(tL9712Vo.get("F7"));
//
//		if (amt > amt2) {
//			amt2 = 0;
//			amt -= amt2;
//		} else {
//			amt2 -= amt;
//			amt = 0;
//		}
//		if (amt > amt1) {
//			amt1 = 0;
//		} else {
//			amt1 -= amt;
//		}

		// 減免 利息
		if (amt1 > 0) {
			this.print(0, 120, String.format("%,d", amt1), "R");
			totalInterestGrace += amt1;
		}

		// 減免 違約金
		if (amt2 > 0) {
			this.print(0, 140, String.format("%,d", amt2), "R");
			totalBreachAmtGrace += amt2;
		}

//		 tL9712Vo.get("F8")
		String txtNo =tL9712Vo.get("F8");
		// 授權主管
		this.print(0, 147, txtNo);
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
