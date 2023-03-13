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
import com.st1.itx.db.service.springjpa.cm.L9714ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class L9714Report extends MakeReport {

	@Autowired
	L9714ServiceImpl l9714ServiceImpl;

	String f0 = "";
	String f1 = "";
	String f2 = "";
	String f3 = "";
	String f4 = "";
	String f5 = "";
	String f6 = "";
	String f7 = "";
	String f8 = "";
	String f9 = "";
	String f10 = "";
	String f11 = "";
	String f12 = "";

	int cnt = 0;

	// 起始欄
	int colCount = 8;

	@Override
	public void printHeader() {
		this.setCharSpaces(0);

		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);
		// 明細起始列(自訂亦必須)
//		this.setBeginRow(31);

		this.print(-4, colCount + 1, "新　光　人　壽　保　險　股　份　有　限　公　司");
		this.print(-6, colCount + 6, "房　屋　擔　保　借　款　繳　息　清　單");
		this.print(-7, colCount + 6, "￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣");

		this.print(-2, colCount + 67, "  機密等級：" + this.getSecurity());
		this.print(-3, colCount + 67, "┌─┬─────────────┐");
		this.print(-4, colCount + 67, "│　│房屋擔保借款繳息證明專用章│");
		this.print(-5, colCount + 67, "│戳├─────────────┤");
		this.print(-6, colCount + 67, "│　│  ０３４５８９０２        │");
		this.print(-7, colCount + 67, "│記├─────────────┤");
		this.print(-8, colCount + 67, "│　│新光人壽保險股份有限公司　│");
		this.print(-9, colCount + 67, "│欄├─────────────┤");
		this.print(-10, colCount + 67, "│　│台北市忠孝西路一段６６號　│");
		this.print(-11, colCount + 67, "└─┴─────────────┘");

		this.print(-13, colCount + 1, "┌────┬──────┬────────┬───────────────────────────┐");
		this.print(-14, colCount + 1,
				"│借戶姓名│            │房屋所有權人姓名│※                                                    │");
		this.print(-15, colCount + 1, "├────┼──────┼────────┼───────────────────────────┤");
		this.print(-16, colCount + 1,
				"│統一編號│            │統一編號        │※                                                    │");
		this.print(-17, colCount + 1, "├────┼──────┼────────┼───────────────────────────┤");
		this.print(-18, colCount + 1,
				"│房屋稅籍│※          │房屋坐落        │※                                                    │");
		this.print(-19, colCount + 1, "└────┴──────┴────────┴───────────────────────────┘");

		this.print(-21, colCount + 1, "┌──────┬───────┬─────────┬──────┬──────┬─────────┐");
		this.print(-22, colCount + 1,
				"│房屋所有權　│              │                  │            │            │      本期未償還　│");
		this.print(-23, colCount + 1, "│取得日※    │ 貸款帳號     │     最初貸款金額 │   貸款起日 │   貸款迄日 │      本金額（元）│");
		this.print(-24, colCount + 1, "├──────┼───────┼─────────┼──────┼──────┼─────────┤");
		this.print(-25, colCount + 1,
				"│            │              │                  │            │            │                  │");
		this.print(-26, colCount + 1, "└──────┴───────┴─────────┴──────┴──────┴─────────┘");

		this.print(-28, colCount + 1, "┌──────────────┬────────────────┬────────────────┐");
		this.print(-29, colCount + 1,
				"│繳息所屬年月                │                      繳息金額　│                        用途別  │");
		this.print(-30, colCount + 1, "├──────────────┼────────────────┼────────────────┤");
		this.print(-31, colCount + 1,
				"│自                          │                                │                                │");
		this.print(-32, colCount + 1,
				"│至                          │                                │                                │");

		String tmp;

		tmp = f0;
		if (tmp.length() > 5) {
			tmp = tmp.substring(0, 5);
		}
		// 姓名
		this.print(-14, colCount + 14, tmp);
		// 身分證
		this.print(-16, colCount + 14, f1);
		// 房屋所有權取得日
		this.print(-25, colCount + 14, showRocDate(f10, 1), "R");
		// 貸款帳號(戶號+額度)
		tmp = String.format("%07d", Integer.valueOf(f2)) + "-" + String.format("%03d", Integer.valueOf(f3));
		this.print(-25, colCount + 18, tmp);
		// 最初貸款金額
		this.print(-25, colCount + 50, formatAmt(f4, 0), "R");
		// 貸款起日
		this.print(-25, colCount + 64, showRocDate(f11, 1), "R");
		// 貸款迄日
		this.print(-25, colCount + 78, showRocDate(f12, 1), "R");
		// 本期未償還本金額
		this.print(-25, colCount + 98, formatAmt(f7, 0), "R");
		// 繳息期間
		this.print(-31, colCount + 7, showRocDate(f5 + "01", 5)); // showRocDate 吃 yyymmdd, 加個dummy
		this.print(-32, colCount + 7, showRocDate(f6 + "01", 5)); // showRocDate 吃 yyymmdd, 加個dummy
		// 繳息金額
		this.print(-31, colCount + 63, formatAmt(f9, 0), "R");
		// 用途別
		this.print(-31, colCount + 97, f8, "R");

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9714Report exec");

		List<Map<String, String>> l9714List = null;

		try {
			l9714List = l9714ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9714ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		if (l9714List == null || l9714List.isEmpty()) {
			return false;
		}

		String tradeNo = "L9714";
		String tradeName = "繳息證明單";
		String brno = titaVo.getBrno();
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(brno).setRptCode(tradeNo)
				.setRptItem(tradeName).setSecurity(this.getSecurity()).setRptSize("A4").setPageOrientation("P").build();

		this.open(titaVo, reportVo);

		if (l9714List.size() > 0) {
			for (Map<String, String> tL9714Vo : l9714List) {

				if (!tL9714Vo.get("F3").equals(f3)) {

					f0 = tL9714Vo.get("F0").isEmpty() ? " " : tL9714Vo.get("F0");
					f1 = tL9714Vo.get("F1");
					f2 = tL9714Vo.get("F2");
					f3 = tL9714Vo.get("F3");
					f4 = tL9714Vo.get("F4");
					f5 = tL9714Vo.get("F5");
					f6 = tL9714Vo.get("F6");
					f7 = tL9714Vo.get("F7");
					f8 = tL9714Vo.get("F8");
					f9 = tL9714Vo.get("F9");
					f10 = tL9714Vo.get("F10");
					f11 = tL9714Vo.get("F11");
					f12 = tL9714Vo.get("F12");
					this.newPage();
					cnt = 0;

				}

				printEnd();
			}
		} else {

			f0 = "本日無資料";
			f1 = " ";
			f2 = "0";
			f3 = "0";
			f4 = "0";
			f5 = "0";
			f6 = "0";
			f7 = "0";
			f8 = "";
			f9 = "0";
			f10 = "0";
			f11 = "0";
			f12 = "0";
			printHeader();

			printEnd();
		}

		this.close();

		return true;

	}

	/**
	 * 列印頁尾
	 * 
	 */
	private void printEnd() {

		this.print(-33, colCount + 1, "└──────────────┴────────────────┴────────────────┘");
		this.print(-34, colCount + 1, " ( 註１：有※註記的欄位，由借戶自行填寫 )");
		this.print(-35, colCount + 1, " ( 註２：申報所得稅─非自用住宅借款利息，不得列舉 )");
	}
}
