package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM021ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("lM021Report")
@Scope("prototype")

public class LM021Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(LM021Report.class);

	@Autowired
	public LM021ServiceImpl lM021ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lM021List = null;

		try {
			lM021List = lM021ServiceImpl.findAll(titaVo);

			exportExcel(titaVo, lM021List);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM021ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("exportExcel---------");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM021", "已逾期未減損-帳齡分析", "LM021已逾期未減損-帳齡分析", "LM021已逾期未減損-帳齡分析.xlsx", "已逾期未減損-帳齡分析");

		int entdy = Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000;

		// 起始列
		int row = 2;

		// 有無資料
		if (LDList.size() > 0) {
			BigDecimal loanBal = BigDecimal.ZERO;
			BigDecimal interest = BigDecimal.ZERO;
			BigDecimal foreclosureFire = BigDecimal.ZERO;
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal days = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : LDList) {

				// 帳號
				makeExcel.setValue(row, 1, tLDVo.get("F0").toString());
				// 本金餘額
				loanBal = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
				makeExcel.setValue(row, 2, loanBal, "#,##0", "R");
				// 應收利息
				interest = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(row, 3, interest.equals(BigDecimal.ZERO) ? "  -  " : interest, "#,##0", "R");
				// 法拍及火險費用
				foreclosureFire = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 4, foreclosureFire.equals(BigDecimal.ZERO) ? "  -  " : foreclosureFire, "#,##0", "R");
				// 三項總計
				total = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 5, total, "#,##0", "R");
				// 逾期繳款天數
				days = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 6, days, "##0", "R");

				// 換列
				row++;

			}

		} else {
			makeExcel.setValue(1, 2, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
