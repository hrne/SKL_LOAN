package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.service.springjpa.cm.L4101ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4101ReportD")
@Scope("prototype")

public class L4101ReportD extends MakeReport {

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService sLoanNotYetService;

	@Autowired
	public L4101ServiceImpl l4101RServiceImpl;

	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)

	private String reportCode = "L4101";
	private String reportItem = "撥款未齊件明細表";
	private String security = "機密";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	int acDate = 0;
	String batchNo = "";
	List<String> tempList = null;
	int cnt = 0; // 總筆數
	BigDecimal sumDbAmt = BigDecimal.ZERO;// 借方總金額
	BigDecimal sumCrAmt = BigDecimal.ZERO;// 貸方總金額

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L4101ReportC.printHeader");

		this.print(-1, 2, "程式ID：" + this.getParentTranCode());
		this.print(-1, 85, "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 2, "報　表：" + this.reportCode);
		this.print(-2, 85, this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 2, "來源別：放款服務課");
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 145, "頁　　次：" + this.getNowPage());

		// 頁首帳冊別判斷
//		print(-4, 10, this.nowAcBookCode);
//		print(-4, 14, this.nowAcBookItem);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(28);

		/**
		 * ---------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */

		print(2, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
		print(1, 1, "　　　　　　戶號　　　　　　　戶名　　　　　　　　　　　　　額度　　　　　　　　　　撥款　　　　　　未齊件代碼　　　　　　　　　未齊件說明　　　　　　　　　　　　　　　　");
		print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4101ReportD exec ...");

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getBacthNo();
		reportCode = titaVo.getTxcd();
		reportCode = reportCode + "-D";
		reportItem = reportItem ;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			resultList = l4101RServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4101ServiceImpl.findAll error = " + errors.toString());
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem+ "-" + batchNo, "", "A4", "L");
		// 統一大小
		this.setFont(1, 10);

		this.setCharSpaces(0);
		int pageCnt = 0, pageIndex = 28;
		if (resultList != null && resultList.size() > 0) {

			for (Map<String, String> result : resultList) {

				int lengthCustName = 20;
				if (result.get("CustName").length() < 20) {
					lengthCustName = result.get("CustName").length();
				}

				// 明細資料第一行
				print(1, 1, "　　");
				print(0, 17, FormatUtil.pad9(result.get("CustNo"), 7), "R");
				print(0, 31, result.get("CustName").substring(0, lengthCustName));
				print(0, 64, result.get("FacmNo"), "R");
				print(0, 88, result.get("BormNo"), "R");
				print(0, 107, result.get("NotYetCode"), "R");
				print(0, 129, result.get("NotYetItem"));

				pageCnt++;

//				每頁第25筆 跳頁 
				if (pageCnt == 25) {
					print(1, 1,
							"－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
					print(1, 1, "小計");
					print(0, 16, "" + pageCnt);
					this.print(1, 85, "=====續下頁=====", "C");
					pageCnt = 0;
					this.newPage();
				}

			} // for

			print(1, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
			print(1, 1, "小計");
			print(0, 16, "" + resultList.size());
			this.print(pageIndex - pageCnt - 2, 85, "=====報表結束=====", "C");
		} else {

			this.setRptItem("撥款未齊件明細表(無符合資料)");
			this.print(1, 1, "*******    查無資料   ******");

		}
	}
}
