package com.st1.itx.trade.L4;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeReport;

@Component("L4606Report4")
@Scope("prototype")

public class L4606Report4 extends MakeReport {

	// 自訂表頭
	@Override
	public void printHeader() {
		
		this.print(-1, 1, "程式ID：" + "L4606");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dDateUtil.getNowStringBc().substring(4, 6)));

		this.print(-1, 130, "日　　期：" + tim + "/" + dDateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dDateUtil.getNowStringBc().substring(2, 4), "R");
		this.print(-2, 1, "報　表：" + "L4606");
		this.print(-2, 70, "佣金媒體檔錯誤清單", "C");
		this.print(-2, 130, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6),
				"R");
		this.print(-3, 70,
				titaVo.get("InsuEndMonth").substring(0, 3) + "/" + titaVo.get("InsuEndMonth").substring(3, 5), "C");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
		this.print(-4, 1, "Seq.  火險單編號                   姓名                                         "); // fix
		this.print(-5, 1,
				"-------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);

	}

	public void exec(TitaVo titaVo, List<OccursList> errorList) throws LogicException {
		this.titaVo = titaVo;
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4606", "佣金媒體檔錯誤清單", "", "A4", "L");

		int cnt = 0;
		for (OccursList tL4606Vo : errorList) {
			this.print(1, 1,
					"                                                                                                                                                                               ");
			this.print(0, 1, tL4606Vo.get("Seq"));
			this.print(0, 7, tL4606Vo.get("InsuNo"));
			this.print(0, 33, tL4606Vo.get("InsuredName"));
			this.print(0, 70, tL4606Vo.get("ErrorMsg"));
			cnt++;
			if (cnt >= 42) {
				cnt = 0;
				this.print(1, 70, "===== 續下頁======", "C");
				this.newPage();
			}
		}

		this.print(1, 1,
				"                                                                          總　計：           筆              ");
		this.print(0, 91, String.format("%,d", errorList.size()), "R");
		long sno = this.close();
	}
}
