package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

	private Map<String, String> tmpProdNoMap = null;

	private boolean result = false;

	private String txcd = "L9739";
	private String txname = "檢核政府優惠房貸利率脫勾";

	@Override
	public void printHeader() {
		this.print(1, 0, " ");
		this.setBeginRow(1);
		this.setMaxRows(50);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param iYearMonth    西元年月
	 * @param loanTypeCount 貸款種類數量<BR>
	 *                      目前(IA~II)共9個
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int iYearMonth, int loanTypeCount) throws LogicException {
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

		List<Map<String, String>> listL9739Title = new ArrayList<Map<String, String>>();
		Map<String, String> tmpAll = null;

		List<Map<String, String>> listL9739Detail = new ArrayList<Map<String, String>>();

		try {

			// 商品利率比對用
			this.tmpProdNoMap = new HashMap<String, String>();

			for (int i = 1; i <= loanTypeCount; i++) {

				this.tmpProdNoMap.put(titaVo.getParam("ProdNo" + i) + "Rate", titaVo.getParam("FitRate" + i));
				this.tmpProdNoMap.put(titaVo.getParam("ProdNo" + i) + "EffectDate", titaVo.getParam("EffectDate" + i));

				// 資料來源為畫面上顯示的資料
				tmpAll = new HashMap<String, String>();
				tmpAll.put("ProdNo", titaVo.getParam("ProdNo" + i));
				tmpAll.put("ProdName", titaVo.getParam("ProdName" + i));
				tmpAll.put("EffectDate", titaVo.getParam("EffectDate" + i));
				tmpAll.put("FitRate", titaVo.getParam("FitRate" + i));
				listL9739Title.add(tmpAll);

			}

			listL9739Detail = l9739ServiceImpl.findAll(titaVo, iYearMonth);

			exportData(listL9739Title, listL9739Detail);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		// 關閉報表
		this.close();

		return this.result;

	}

	private void exportData(List<Map<String, String>> listL9739Title, List<Map<String, String>> listL9739Detail)
			throws LogicException {

		this.setFont(1, 12);

		String tim = String.format("%02d", Integer.parseInt(dateUtil.getNowStringBc().substring(4, 6)));
		this.print(-2, 69, "日　期：" + tim + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/"
				+ dateUtil.getNowStringBc().substring(2, 4));
		this.print(-3, 69, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));

		this.setFont(1, 18);
		this.print(1, this.getMidXAxis(), "逐項檢核政府優惠房貸利率不符當時利率", "C");

		this.setFont(1, 12);
		this.print(1, 1, " ");
		// 標題

		if (listL9739Title.size() > 0) {
			for (Map<String, String> t : listL9739Title) {
				String text = t.get("ProdNo") + "     " + fillUpWord(t.get("ProdName"), 22, " ", "R")
						+ this.showRocDate(t.get("EffectDate"), 1) + "     "
						+ fillUpWord(t.get("FitRate"), 5, "0", "R");
				this.print(1, this.getMidXAxis(), text, "C");
			}
		} else {

			this.print(9, this.getMidXAxis(), " ", "C");

		}

		this.print(1, 1, " ");
		this.print(1, 3, "不符者，產出明細欄位如下");

		this.print(1, 10, "戶號");
		this.print(0, 20, "額度");
		this.print(0, 30, "額度");
		this.print(0, 40, "利率");
		this.print(0, 53, "生效日期");
		this.print(0, 66, "基本利率代碼");

		if (listL9739Detail.size() > 0) {

			int tmpCount = 0;
			for (Map<String, String> r : listL9739Detail) {

				this.info("tmpEffectDate = " + this.tmpProdNoMap.get(r.get("ProdNo").toString() + "EffectDate"));
				this.info("tmpRate = " + this.tmpProdNoMap.get(r.get("ProdNo").toString() + " Rate"));
				this.info("nowEffectDate = " + r.get("EffectDate").toString());
				this.info("nowRate = " + r.get("ProdNo").toString());

				if (!this.tmpProdNoMap.get(r.get("ProdNo").toString() + "Rate").equals(r.get("StoreRate").toString())) {
					tmpCount++;

					this.print(1, 13, r.get("CustNo"), "R");
					this.print(0, 23, r.get("FacmNo"), "R");
					this.print(0, 33, r.get("BormNo"), "R");
					this.print(0, 43, r.get("StoreRate"), "R");
					this.print(0, 60, showRocDate(Integer.valueOf(r.get("EffectDate")), 1), "R");
					this.print(0, 77, r.get("ProdNo"), "R");
				}

			}

			if (tmpCount == 0) {
				this.print(1, 3, "本日無資料");
			} else {
				this.result = true;
			}

		} else {

			this.print(1, 3, "本日無資料");

		}

		this.print(-55, this.getMidXAxis(), "＊＊＊＊＊＊＊＊    報  表  結  束    ＊＊＊＊＊＊＊＊ ", "C");

	}

}
