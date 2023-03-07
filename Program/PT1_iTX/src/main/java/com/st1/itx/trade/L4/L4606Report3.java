package com.st1.itx.trade.L4;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeReport;

@Component("L4606Report3")
@Scope("prototype")

public class L4606Report3 extends MakeReport {

	// 自訂表頭
	@Override
	public void printHeader() {
		
		this.print(-1, 1, "程式ID：" + "L4606");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");

		this.print(-1, 120, "日　　期： " + dDateUtil.getNowStringBc().substring(6, 8) + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dDateUtil.getNowStringBc().substring(2, 4), "L");
		this.print(-2, 1, "報　表：" + "L4606");
		this.print(-2, 70, "佣金媒體檔轉入清單", "C");
		this.print(-2, 120, "時　　間： " + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
				"L");
		this.print(-3, 70, titaVo.get("InsuEndMonth").toString().substring(0, 3) + "/"
				+ titaVo.get("InsuEndMonth").toString().substring(3, 5), "C");
		this.print(-3, 120, "頁　　次：" + this.getNowPage(), "L");
		this.print(-4, 1, "Seq.  火險單編號                   姓名                                         "); // fix
		this.print(-5, 1,
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void exec(TitaVo titaVo, List<OccursList> successList) throws LogicException {
		// 讀取VAR參數
		this.titaVo = titaVo;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4606", "佣金媒體檔轉入清單", "", "A4", "L");

		int cnt = 0;
		for (OccursList tL4606Vo : successList) {
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 1, tL4606Vo.get("Seq"));
			this.print(0, 7, tL4606Vo.get("InsuNo"));
			this.print(0, 33, tL4606Vo.get("InsuredName"));

			cnt++;
			if (cnt >= 42) {
				cnt = 0;
				this.print(1, 70, "===== 續下頁======", "C");
				this.newPage();
			}
		}

		this.print(1, 1,
				"                                                                          總　計：           筆              ");
		this.print(0, 91, String.format("%,d", successList.size()), "R");
		long sno = this.close();
//		this.toPdf(sno);
	}

}
