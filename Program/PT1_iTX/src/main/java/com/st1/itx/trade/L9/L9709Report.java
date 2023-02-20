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
import com.st1.itx.db.service.springjpa.cm.L9709ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class L9709Report extends MakeReport {

	@Autowired
	L9709ServiceImpl l9709ServiceImpl;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	String startDate = "";
	String endDate = "";

	@Override
	public void printHeader() {

		this.print(-2, 1, "　程式ID：" + this.getParentTranCode());
		this.print(-3, 1, "　報　表：" + this.getRptCode());

		this.print(-2, this.getMidXAxis(), "暫收放貸核心傳票檔資料", "C");
		this.print(-2, 80, "印表日期：" + showRocDate(this.nowDate, 1));
		this.print(-5, 2, "會計日期：" + showRocDate(startDate, 1) + " ~ " + showRocDate(endDate, 1));
		this.print(-3, 80, "印表時間：" + showTime(this.nowTime));
//		this.print(-5, 1, "  科目             借方金額           貸方金額");
//		this.print(-5, 1, "會計日期");
//		this.print(-6, 1, "－－－－");
		this.print(-6, 2, " 會計科目");
		this.print(-7, 2, "－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(-6, 59, "借方金額", "R");
		this.print(-7, 61, "－－－－－－", "R");
		this.print(-6, 77, "貸方金額", "R");
		this.print(-7, 79, "－－－－－－", "R");
		this.print(-6, 94, "合計金額", "R");
		this.print(-7, 96, "－－－－－－", "R");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);
	}

	@Override
	public void printFooter() {
		this.print(-61, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L9709Report exec");

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		startDate = String.valueOf(Integer.parseInt(titaVo.get("StartDate")) + 19110000);
		endDate = String.valueOf(Integer.parseInt(titaVo.get("EndDate")) + 19110000);

		List<Map<String, String>> l9709List = null;

		try {
			l9709List = l9709ServiceImpl.findAll(startDate, endDate, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9709ServiceImpl findAll error = " + errors.toString());
		}

		if (l9709List == null || l9709List.isEmpty()) {
			this.info("l9709List is null");
			return;
		}

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("L9709").setRptItem("暫收放貸核心傳票檔資料").setSecurity("").setRptSize("A4").setPageOrientation("P")
				.build();

		this.open(titaVo, reportVo);

		// 明細起始列(自訂亦必須)
		for (Map<String, String> tL9709Vo : l9709List) {
//			this.print(1, 1, showRocDate(tL9709Vo.get("AcDate"), 1)); // 會計日期
			this.print(1, 2, tL9709Vo.get("AcNoCode") + "  " + tL9709Vo.get("AcNoItem")); // 會計科目 + 會計科目中文
			this.print(0, 61, formatAmt(tL9709Vo.get("DbAmt"), 0), "R"); // 借方金額
			this.print(0, 79, formatAmt(tL9709Vo.get("CrAmt"), 0), "R"); // 貸方金額
			this.print(0, 96, formatAmt(tL9709Vo.get("Total"), 0), "R"); // 小計金額
		}

		this.close();

	}
}
