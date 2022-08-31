package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4606ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component("L4606Report2")
@Scope("prototype")
public class L4606Report2 extends MakeReport {

	@Autowired
	private L4606ServiceImpl l4606ServiceImpl;

	/* 轉換工具 */
	@Autowired
	private Parse parse;

	// 自訂表頭
	@Override
	public void printHeader() {
		
		this.setFont(1, 9);
		this.print(-1, 1, "程式ID：" + "L4606");
		this.print(-1, 84, "新光人壽保險股份有限公司", "C");

		this.print(
				-1, 160, "日　　期：" + dDateUtil.getNowStringBc().substring(6, 8) + "/"
						+ dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(2, 4),
				"R");
		this.print(-2, 1, "報　表：" + "L4606");
		this.print(-2, this.getMidXAxis(), "火險佣金未發明細表", "C");
		this.print(-2, 160, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
				"R");
		this.print(-3, 84,
				titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5), "C");
		this.print(-3, 160, "頁　　次：" + this.getNowPage(), "R");
		this.print(-4, 1,
				"保單號碼         險種   保費 起保日期  到期日期 被保險人地址                                      戶號額度    戶名          火險服務ＩＤ 火險服務人 應領金額"); // fix
		this.print(-5, 1,
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4606", "火險佣金未發明細表", "", "A4", "L");
		// 設定資料庫(必須的)

		List<Map<String, String>> listL4606 = null;
		try {
			listL4606 = l4606ServiceImpl.find(titaVo);
//			this.info("------->list = " + listL4606);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4606ServiceImpl.find error = " + errors.toString());
		}

		int times = 0;
		int cnt = 0;
		int i = 0;
		String officer = "";
		String id = "";
		BigDecimal totamt = new BigDecimal("0");
		for (Map<String, String> tL4606Vo : listL4606) {
			//this.setFont(1, 9);
			this.print(1, 1, " ");
			this.print(0, 1, tL4606Vo.get("F0"));
			this.print(0, 20, tL4606Vo.get("F1"));
			this.print(0, 29, String.format("%,d", Integer.parseInt(tL4606Vo.get("F2"))), "R");
			this.print(0, 39, tL4606Vo.get("F3"), "R");
			this.print(0, 49, tL4606Vo.get("F4"), "R");
			this.print(0, 50, tL4606Vo.get("F5"));
			this.print(0, 104, padStart(7, tL4606Vo.get("F6")));
			this.print(0, 112, "-");
			this.print(0, 116, padStart(3, tL4606Vo.get("F7")), "R");
			this.print(0, 122, tL4606Vo.get("F8"));
			this.print(0, 135, tL4606Vo.get("F9"));
			this.print(0, 146, tL4606Vo.get("F10"));
			this.print(0, 164, String.format("%,d", Integer.parseInt(tL4606Vo.get("F11"))), "R");
			totamt = totamt.add(parse.stringToBigDecimal(tL4606Vo.get("F11")));
			cnt++;
			times++;
			if (i == 0) {
				officer = tL4606Vo.get("F8");
				id = tL4606Vo.get("F10");

			} else {
				if (officer.equals(tL4606Vo.get("F8")) && id.equals(tL4606Vo.get("F10"))) {

				} else {
//					reset比較值
					officer = tL4606Vo.get("F8");
					id = tL4606Vo.get("F10");
				}
			}

			i++;
			if (cnt >= 35) {
				cnt = 0;
				this.print(1, this.getMidXAxis(), "===== 續下頁======", "C");
				this.newPage();
			}
		}

		this.print(1, 1,
				"--------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 1,
				"                                                                總　計：           筆              火險服務人：           人 ");
		this.print(0, 105, String.format("%,d", times), "R");
//		this.print(0, 129, String.format("%,d", people), "R");
		this.print(0, 162, String.format("%,d", totamt.intValue()), "R");
		long sno = this.close();
//		this.toPdf(sno);
	}

	private String padStart(int size, String string) {
		for (int i = 0; i < size; i++) {
			if (string.length() < size) {
				string = "0" + string;
			}
		}
		return string;
	}
}
