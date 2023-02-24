package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4961ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L4961ReportB extends MakeReport {

	@Autowired
	WebClient webClient;

	@Autowired
	L4961ServiceImpl l4961ServiceImpl;

	@Autowired
	public CollListService collListService;

	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CdCodeService cdCodeService;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L4961-B";
	private String reportItem = "催收火險保費明細表";
	private String security = "密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	private String batchNo;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	String InsuYearMonth;
	String InsuYearMonthEnd;

	// 自訂表頭
	@Override
	public void printHeader() {
		this.print(-2, 2, "程式ID：" + this.getParentTranCode());
		this.print(-2, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-2, 145, "機密等級：" + this.security);
		this.print(-3, 2, "報　表：" + this.reportCode);
//		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-3, 145, "日　　期：" + showBcDate(this.nowDate, 1));
//		this.print(-3, 1, "來源別：放款服務課");
		this.print(-4, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-5, 2, "會計日期:" + showRocDate(titaVo.getEntDyI(), 1));
		this.print(-5, 145, "頁　　次：" + this.getNowPage());
		this.print(-6, 145, "單　　位：元");
//		this.print(-4, this.getMidXAxis(), showRocDate(this.reportDate), "C");
		this.print(-3, this.getMidXAxis(), this.reportItem, "C");
		this.print(-6, 2, "到期日期:" + this.InsuYearMonth + "-" + this.InsuYearMonthEnd);

		// 印明細表頭
		this.print(-7, 1, "┌————┬————┬—————┬——————┬——————┬————————┬——————┬————————┬————————┬——————————┬—————┐");
		this.print(-8, 1, "｜　戶號　｜　額度　｜　被保人　｜　續單年月　｜　 總保費 　｜　　火險保額　　｜　火險保費　｜　 地震險保額 　｜　 地震險保費 　｜　 保單號碼 　　　　｜　 戶況 　｜");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(37);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4961ReportC exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI();

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.InsuYearMonth = titaVo.getParam("InsuYearMonth").substring(0, 3) + "/"
				+ titaVo.getParam("InsuYearMonth").substring(3, 5);
		this.InsuYearMonthEnd = titaVo.getParam("InsuYearMonthEnd").substring(0, 3) + "/"
				+ titaVo.getParam("InsuYearMonthEnd").substring(3, 5);

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l4961ServiceImpl.findAll(0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			this.error("l4961ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem + (resultList.size() > 0 ? "" : "(本日無資料)")).setSecurity(security)
				.setRptSize(pageSize).setPageOrientation(pageOrientation).build();
		this.open(titaVo, reportVo);
		setCharSpaces(0);

		BigDecimal totInsuPremTotal = BigDecimal.ZERO;
		BigDecimal fireInsuPremTotal = BigDecimal.ZERO;
		BigDecimal ethqInsuPremTotal = BigDecimal.ZERO;

		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, String> result : resultList) {

				if (this.NowRow == 44) {
					print(1, 1, "└————┴————┴—————┴——————┴——————┴————————┴——————┴————————┴————————┴——————————┴—————┘");
				}

//				print(1, 1, "｜　　　　　　　　　　　　　｜　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　　　　｜");
				print(1, 1, "├————┼————┼—————┼——————┼——————┼————————┼——————┼————————┼————————┼——————————┼—————┤");

				BigDecimal totInsuPrem = getBigDecimal(result.get("F6"));
				BigDecimal fireInsuPrem = getBigDecimal(result.get("F11"));
				BigDecimal ethqInsuPrem = getBigDecimal(result.get("F13"));
				// 到期年月

				int insuYearMonth = 0;
				if (!result.get("F0").isEmpty() && Integer.parseInt(result.get("F0")) > 0) {
					insuYearMonth = Integer.parseInt(result.get("F0")) - 191100;
				}
				print(1, 1, "｜　　　　｜　　　　｜　　　　　｜　　　　　　｜　　　　　　｜　　　　　　　　｜　　　　　　｜　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　｜　　　　　｜");

				print(0, 7, FormatUtil.pad9(String.valueOf(result.get("F3")), 7), "C"); // 戶號
				print(0, 17, FormatUtil.pad9(String.valueOf(result.get("F4")), 3), "C"); // 額度
				print(0, 25, result.get("F5"), "L"); // 戶名
				print(0, 43, "" + insuYearMonth, "C"); // 到期年月
				print(0, 60, formatAmt(result.get("F6"), 0), "R");// 總保費
				print(0, 78, formatAmt(result.get("F10"), 0), "R");// 火險保額
				print(0, 92, formatAmt(result.get("F11"), 0), "R");// 火險保費
				print(0, 110, formatAmt(result.get("F12"), 0), "R");// 地震險保額
				print(0, 126, formatAmt(result.get("F13"), 0), "R");// 地震險保費
				print(0, 150, formatAmt(result.get("F2"), 0), "R");// 保單號碼

				// 戶況
				String colStatus = "99";
				CollList tCollList = collListService.findById(
						new CollListId(Integer.valueOf(result.get("F3")), Integer.valueOf(result.get("F4"))), titaVo);
				if (tCollList != null) {
					colStatus = parse.IntegerToString(tCollList.getStatus(), 2);
				}
				if ("4".equals(colStatus)) {
					colStatus = "00";
				}

				CdCode tCdCode = cdCodeService.findById(new CdCodeId("ColStatus", colStatus), titaVo);
				String colStatusX = "";
				if (tCdCode != null) {
					colStatusX = tCdCode.getItem();
				}

				print(0, 160, formatAmt(colStatusX, 0), "R");// 戶況

				// 加總
				totInsuPremTotal = totInsuPremTotal.add(totInsuPrem);
				fireInsuPremTotal = fireInsuPremTotal.add(fireInsuPrem);
				ethqInsuPremTotal = ethqInsuPremTotal.add(ethqInsuPrem);

			}
			// 印總計
			print(1, 1, "├————┴————┼—————┴——————┴——————┼————————┴——————┼————————┴————————┼——————————┼—————┤");
			print(1, 1, "｜　合　　　　　計　｜　　　　　　　　　　　　｜　　　　　　｜　　　　　　　　｜　　　　　　｜　　　　　　　　｜　　　　　　　　｜　　　　　　　　　　　　　　　｜");
			print(0, 69, formatAmt(totInsuPremTotal, 0), "R");// F6
			print(0, 105, formatAmt(fireInsuPremTotal, 0), "R");// F11
			print(0, 139, formatAmt(ethqInsuPremTotal, 0), "R");// F13
			print(1, 1, "└—————————┴————————————┴——————┴————————┴——————┴————————┴————————┴——————————┴—————┘");
			print(2, this.getMidXAxis(), "經理：　　　　　　　　　　　　　　　經辦：", "C");

			webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y",
					"LC009", titaVo.getTlrNo() + "L4961", reportCode + "-報表已完成", titaVo);
		} else {	
			print(1, 1, "｜　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　｜");
			print(0, 1, "　　本　日　無　資　料　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
			print(1, 1, "└————————————————————————————————————————————————————————————————————————————————┘");

		}

	}
}
