package com.st1.itx.trade.L4;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4452Report")
@Scope("prototype")

public class L4452Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L4452Report.class);
	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private CdCodeService cdCodeService;

	private String repayBank = "";
	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 1, "程式ID：" + "L4452");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "日　　期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 1, "報　表：" + "L4452");
		this.print(-2, 70, "銀扣媒體檔未產出清單", "C");
		this.print(-2, 130, "時　　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
//		this.print(-3, 1, "扣款銀行：" + repayBank);
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
//		this.print(-4, 1,
//				"入帳日期   戶號   額度 撥款    繳息迄日   應繳日   還款類別           應扣金額        暫收抵繳金額           扣款金額 媒體   會計日期");
		this.print(-4, 1, "戶號   額度 撥款    還款類別                扣款金額    摘要");
		this.print(-5, 1,
				"--------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4452Report exec");

		if (occursList == null) {
			this.info("occursList = null ...");
			return;
		}

		this.info("occursList.size = " + occursList.size());

		if (occursList.size() > 0) {

			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4452", "銀扣媒體檔未產出清單", "", "A4", "L");

			int i = 0, pageCnt = 0;
			for (int j = 1; j <= occursList.size(); j++) {

				i = j - 1;

//				每頁筆數相加
				pageCnt++;

				DecimalFormat df1 = new DecimalFormat("#,##0");

				this.info("occursList.get(i)-------->" + occursList.get(i).toString());
//				1.每筆先印出明細
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, FormatUtil.pad9(occursList.get(i).get("OOCustNo"), 7));// 戶號
				this.print(0, 8, "-");
				this.print(0, 9, FormatUtil.pad9(occursList.get(i).get("OOFacmNo"), 3));// 額度
				this.print(0, 12, "-");
				this.print(0, 13, FormatUtil.pad9(occursList.get(i).get("OOBormNo"), 3));// 撥款
				this.print(0, 20, occursList.get(i).get("OORepayType"));// 還款類別
				this.print(0, 50, df1.format(parse.stringToBigDecimal(occursList.get(i).get("OORepayAmt"))), "R");// 還款金額
				this.print(0, 55, occursList.get(i).get("OONote"));// 摘要

//				每頁第42筆 跳頁 
				if (j != occursList.size() && pageCnt == 42) {
					this.print(1, 70, "=====續下頁=====", "C");

					pageCnt = 0;
					this.newPage();
					continue;
				}
			}
			long sno = this.close();
			this.toPdf(sno);
		}
	}

	public void doReport(ArrayList<OccursList> doReportOccurs, TitaVo titaVo) throws LogicException {
		this.info("doReportOccurs = " + doReportOccurs);
		occursList = doReportOccurs;

		this.exec(titaVo);
	}

	private String repayTypeX(String repayType) {
		String result = "";

		String srt = FormatUtil.pad9(repayType.trim(), 2);

		CdCode cdCode = cdCodeService.getItemFirst(4, "RepayType", srt, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}
}
