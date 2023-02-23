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
import com.st1.itx.db.service.springjpa.cm.L9739ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L9739Report
 * 
 * @author
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9739Report extends MakeReport {

	@Autowired
	L9739ServiceImpl l9739ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;


	String txcd = "L9739";
	String txname = "檢核政府優惠房貸利率脫勾";

	@Override
	public void printHeader() {
		this.print(1, 0, " ");
		this.setBeginRow(1);
		this.setMaxRows(45);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param iYearMonth 西元年月
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int iYearMonth) throws LogicException {
		this.info("L9739 exec");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String reportItem = txname;
		String brno = titaVo.getBrno();
		String security = "";
		String pageSize = "A4";
		String pageOrientation = "P";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		// 開啟報表
		this.open(titaVo, reportVo);

		List<Map<String, String>> listL9739Title = null;
		List<Map<String, String>> listL9739Detail = null;
		try {

			listL9739Title = l9739ServiceImpl.findStandard(titaVo, reportDate);
			listL9739Detail = l9739ServiceImpl.findAll(titaVo, iYearMonth);

			exportData(listL9739Title, listL9739Detail);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		// 關閉報表
		this.close();

		boolean result = false;

		if (listL9739Detail.size() > 0) {
			result = true;
		}

		return result;

	}

	private void exportData(List<Map<String, String>> listL9739Title, List<Map<String, String>> listL9739Detail)
			throws LogicException {

		this.setFont(1, 18);
		this.print(1, this.getMidXAxis(), "逐項檢核政府優惠房貸利率不符當時利率", "C");

		this.setFont(1, 12);
		this.print(1, 1, " ");
		// 標題

		if (listL9739Title.size() > 0) {
			for (Map<String, String> t : listL9739Title) {
				String text = t.get("ProdNo") + "     " + fillUpWord(t.get("ProdName"), 22, " ", "R")
						+ this.showRocDate(t.get("EffectDate"), 1) + "     "
						+ fillUpWord(t.get("FitRate"), 6, "0", "R");

				this.print(1, this.getMidXAxis(), text, "C");
			}
		}else {
			this.print(9, this.getMidXAxis(), " ", "C");
			
		}
		this.print(1, 1, " ");
		this.print(1, 3, "不符者，產出明細欄位如下");
		this.print(1, 13, "戶號");
		this.print(0, 23, "額度");
		this.print(0, 30, "額度");
		this.print(0, 37, "利率");
		this.print(0, 45, "生效日期");
		this.print(0, 60, "基本利率代碼");

		if (listL9739Detail.size() > 0) {

			for (Map<String, String> r : listL9739Detail) {

				this.print(1, 13, r.get("CustNo"));
				this.print(0, 23, r.get("FacmNo"));
				this.print(0, 30, r.get("BormNo"));
				this.print(0, 37, r.get("StoreRate"));
				this.print(0, 45, showBcDate(r.get("EffectDate"), 0));
				this.print(0, 60, r.get("ProdNo"));

			}

		} else {

			this.print(1, 3, "本日無資料");

		}

		this.print(1, 3, "＊＊＊＊＊＊＊＊    End of report    ＊＊＊＊＊＊＊＊ ");

	}

	
}
