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

		// this.setFontSize(13);
		this.print(-2, 23, "暫收放貸核心傳票檔資料");
		this.print(-2, 50, "印表日期：" + showRocDate(this.nowDate, 1));
		this.print(-3, 1, "會計日期：" + showRocDate(startDate, 1) + " ~ " + showRocDate(endDate, 1));
		this.print(-3, 50, "印表時間：" + showTime(this.nowTime));
//		this.print(-5, 1, "  科目             借方金額           貸方金額");
		this.print(-5, 1, "會計日期");
		this.print(-5, 13, " 會計科目");
		this.print(-5, 39, "借方金額","R");
		this.print(-5, 57, "貸方金額","R");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
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
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9709", "暫收放貸核心傳票檔資料", "", "A4", "P");

		
		for (Map<String, String> tL9709Vo : l9709List) {
			this.print(1, 1, showRocDate(tL9709Vo.get("AcDate"), 1)); //會計日期
			this.print(0, 13, tL9709Vo.get("AcNoCode")); // 會計科目
			this.print(0, 39, formatAmt(tL9709Vo.get("DrAmt"), 0), "R"); // 借方金額
			this.print(0, 57, formatAmt(tL9709Vo.get("CrAmt"), 0), "R"); // 貸方金額
		}

		this.close();

	}
}
