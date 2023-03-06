package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9740ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9740Report extends MakeReport {

	@Autowired
	L9740ServiceImpl l9740ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal rate = BigDecimal.ZERO;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	// 1 = Q9309141 新撥款之戶號
	// 2 = Q9309142 續期放款利率 最低、最高
	// 3 = Q9309143 利率超過 X.XX% 之借戶
	private int reportType = 1;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
//	private int reportDate = 0;
	private String reportCode = "L9740";
	private String reportItem = "公會無自用住宅放款檢核清單";
	private String security = "密";
	private String pageSize = "A4";
	private String pageOrientation = "P";

	@Override
	public void printHeader() {
		// 左
		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");

		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-1, 67, "機密等級：" + this.security);
		this.print(-2, 67, "日　　期：" + this.nowDate);
		this.print(-3, 67, "時　　間：" + showTime(this.nowTime));

		// 印明細表頭
//		this.printDetailHeader();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(55);

		if (reportType == 1 || reportType == 3) {

			this.print(2, 8, "戶號");
			this.print(0, 18, "額度");
//			this.print(0, 25, "撥款");
			this.print(0, 29, "撥款日期");
			this.print(0, 45, "撥款金額");
			this.print(0, 63, "利率");
			this.print(0, 71, "繳息迄日");
			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ ");

		} else {
			print(2, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ ");
		}

	}

	@Override
	public void printContinueNext() {
		this.print(-57, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

//	@Override
//	public void printFooter() {
//		this.print(-56, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");
//	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9740Report exec");

//		int reportDate = titaVo.getEntDyI() + 19110000;
//		String reportItem = txname;
//		String brno = titaVo.getBrno();
//		String security = "";
//		String pageSize = "A4";
//		String pageOrientation = "P";

		List<Map<String, String>> listL9740Data1 = null;

		List<Map<String, String>> listL9740Data2 = null;

		List<Map<String, String>> listL9740Data3 = null;

		int drawDownDateA1 = Integer.valueOf(titaVo.getParam("DrawDownDateA1")) + 19110000;
		int drawDownDateA2 = Integer.valueOf(titaVo.getParam("DrawDownDateA2")) + 19110000;
		String acctCodeA = titaVo.getParam("AcctCodeA");
		String renewFlagA = titaVo.getParam("RenewFlagA");

		int drawDownDateB1 = Integer.valueOf(titaVo.getParam("DrawDownDateB1")) + 19110000;
		String acctCodeB = titaVo.getParam("AcctCodeB");
		String statusB = titaVo.getParam("StatusB");

		int drawDownDateC1 = Integer.valueOf(titaVo.getParam("DrawDownDateC1")) + 19110000;
		String acctCodeC = titaVo.getParam("AcctCodeC");
		String statusC = titaVo.getParam("StatusC");
		BigDecimal rateC = new BigDecimal(titaVo.getParam("RateC"));
		this.rate = rateC;

		try {

			// Q9309141 新撥款之戶號
			listL9740Data1 = l9740ServiceImpl.findPage1(titaVo, drawDownDateA1, drawDownDateA2, acctCodeA, renewFlagA);

			// Q9309142 續期放款利率 最低、最高
			listL9740Data2 = l9740ServiceImpl.findPage2(titaVo, drawDownDateB1, acctCodeB, statusB);

			// Q9309143 利率超過 X.XX% 之借戶
			listL9740Data3 = l9740ServiceImpl.findPage3(titaVo, drawDownDateC1, acctCodeC, statusC, rateC);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(this.reportCode + "ServiceImpl.findAll error = " + errors.toString());
		}

		// 製表時間
		nowDate = this.showBcDate(titaVo.getEntDyI() + 19110000, 1);
		// 製表日期
		nowTime = dateUtil.getNowStringTime();

		exportData(listL9740Data1, listL9740Data2, listL9740Data3, titaVo);

		boolean result = true;

		if (listL9740Data1.size() == 0 && listL9740Data2.size() == 0 && listL9740Data3.size() == 0) {
			result = false;
		}

		return result;

	}

	private void exportData(List<Map<String, String>> listL9740Data1, List<Map<String, String>> listL9740Data2,
			List<Map<String, String>> listL9740Data3, TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getKinbr();
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(this.reportCode)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();

		this.reportItem = "公會無自用住宅放款檢核清單(新撥款之戶號)";

		this.reportType = 1;

		// 開啟報表
		this.open(titaVo, reportVo);

		this.setFont(1, 12);

		if (listL9740Data1.size() > 0) {
			this.print(1, 1," ");
			// 因此表User說目前還沒看到過有資料，所以暫不確定有資料的格式(各個欄位的項目)
			int count = 1;
			for (Map<String, String> r : listL9740Data1) {
				count++;

				this.print(1, 12, r.get("CustNo"), "R");
				this.print(0, 22, r.get("FacmNo"), "R");
//				this.print(0, 25, r3.get("BormNo"));
				this.print(0, 36, this.showRocDate(r.get("DrawdownDate"), 1), "R");
				this.print(0, 52, this.formatAmt(r.get("DrawdownAmt"), 0), "R");
				BigDecimal rate = r.get("StoreRate").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r.get("StoreRate"));
				this.print(0, 66, fillUpWord(String.valueOf(rate), 6, "0", "R"), "R");
				this.print(0, 78, this.showRocDate(r.get("PrevPayIntDate"), 1), "R");

				if (count == 50) {
					printContinueNext();
					this.newPage();
					this.print(1, 1," ");
					count = 1;
				}

			}

		} else {
			this.print(2, 3, "本日無資料");
		}

//		printContinueNext();

		/*-----------------------------------------------------------------------------*/

		this.reportType = 2;

		this.reportItem = "公會無自用住宅放款檢核清單(續期放款利率 最低、最高)";
		this.newPage();

		if (listL9740Data2.size() > 0) {

			this.print(1, 10, "利率");

			this.print(2, 3, "FINAL TOTALS");
			BigDecimal minRate = listL9740Data2.get(0).get("minRate").isEmpty() ? new BigDecimal("0.0")
					: new BigDecimal(listL9740Data2.get(0).get("minRate"));
			BigDecimal maxRate = listL9740Data2.get(0).get("maxRate").isEmpty() ? new BigDecimal("0.0")
					: new BigDecimal(listL9740Data2.get(0).get("maxRate"));
			this.print(1, 3, "MIN  " + fillUpWord(String.valueOf(minRate), 6, "0", "R"));
			this.print(1, 3, "MAX  " + fillUpWord(String.valueOf(maxRate), 6, "0", "R"));
			this.print(1, 1, " ");

		} else {
			this.print(1, 10, "利率");

			this.print(1, 3, "本日無資料");
		}

//		printContinueNext();
		/*-----------------------------------------------------------------------------*/

		this.reportType = 3;

		this.reportItem = "公會無自用住宅放款檢核清單(利率超過 " + this.rate + " 之借戶)";
		this.newPage();

		if (listL9740Data3.size() > 0) {
			this.print(1, 1," ");
			int count = 1;
			for (Map<String, String> r3 : listL9740Data3) {
				count++;

				this.print(1, 12, r3.get("CustNo"), "R");
				this.print(0, 22, r3.get("FacmNo"), "R");
//				this.print(0, 25, r3.get("BormNo"));
				this.print(0, 36, this.showRocDate(r3.get("DrawdownDate"), 1), "R");
				this.print(0, 52, this.formatAmt(r3.get("DrawdownAmt"), 0), "R");
				BigDecimal rate = r3.get("StoreRate").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r3.get("StoreRate"));
				this.print(0, 66, fillUpWord(String.valueOf(rate), 6, "0", "R"), "R");
				this.print(0, 78, this.showRocDate(r3.get("PrevPayIntDate"), 1), "R");

				if (count == 50) {
					printContinueNext();
					this.newPage();
					this.print(1, 1," ");
					count = 1;
				}

			}

		} else {

			this.print(2, 3, "本日無資料");
		}

		this.print(-56, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");

		// 關閉報表
		this.close();

	}


}
