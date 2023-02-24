package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM086ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM086Report extends MakeReport {

	@Autowired
	LM086ServiceImpl lM086ServiceImpl;

	@Autowired
	Parse parse;

	String inputYear = "";
	String inputMonth = "";
	String lowerLimit = "             0";

	String acctCode = "";
	String acctItem = "";

	int countByAcct = 0;
	BigDecimal sumByAcct = BigDecimal.ZERO;

	int count = 0;
	BigDecimal sum = BigDecimal.ZERO;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		this.print(-2, 3, "程式ID：" + this.getParentTranCode());
		this.print(-3, 3, "報　表：" + this.getRptCode());

		this.print(-1, 98, "機密等級：" + this.getRptSecurity());
		this.print(-2, 98, "日　　期：" + this.showBcDate(titaVo.getEntDyI(), 1));
		this.print(-3, 98, "時　　間：" + showTime(this.getNowTime()));
		this.print(-4, 98, "頁　　數：" + this.getNowPage());

		this.print(-2, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-3, this.getMidXAxis(), this.getRptItem(), "C");
		this.print(-4, this.getMidXAxis(), inputYear + " 年 " + inputMonth + " 月", "C");
		this.print(-5, this.getMidXAxis(), "金額  " + lowerLimit + "  元以上", "C");

		this.print(-6, 0, "     業務科目 ..... " + acctCode + "  " + acctItem);
		this.print(-7, 0,
				"   戶    號    戶    名         客戶別    貸款餘額  利率 %  擔保品別     規定管制代碼    行業別                           ");
		this.print(-8, 0,
				"  ===================================================================================================================  ");
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(57);
	}

	// 自訂續下頁
	@Override
	public void printContinueNext() {
		this.print(0, this.getMidXAxis(), "===== 續下頁 =====", "C");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;
		inputYear = titaVo.getParam("inputYear");
		inputMonth = titaVo.getParam("inputMonth");
		lowerLimit = FormatUtil.padLeft(formatAmt(titaVo.getParam("lowerLimit"), 0), 14);

		List<Map<String, String>> resultList = null;

		try {
			resultList = lM086ServiceImpl.doQuery(titaVo);
		} catch (Exception e) {
			this.error("LM086ServiceImpl doQuery " + e.getMessage());
			throw new LogicException("E0013", "LM086Report");
		}

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("LM086").setRptItem("放款餘額明細表").setSecurity("密").setRptSize("A4").setPageOrientation("P")
				.build();

		if (resultList != null && !resultList.isEmpty()) {
			acctCode = resultList.get(0).get("AcctCode");
			acctItem = resultList.get(0).get("AcctItem");
		}

		this.open(titaVo, reportVo);

		if (resultList != null && !resultList.isEmpty()) {
			for (Map<String, String> result : resultList) {

				String thisAcctCode = result.get("AcctCode");

				if (!acctCode.equals(thisAcctCode)) {
					printSumByAcct();
					countByAcct = 0;
					sumByAcct = BigDecimal.ZERO;
					acctCode = thisAcctCode;
					acctItem = result.get("AcctItem");
					this.newPage();
				}

				BigDecimal bal = this.getBigDecimal(result.get("PrinBalance"));

				print(1, 0, "");
				print(0, 3, result.get("CustNo"));
				print(0, 14, result.get("CustName"));
				print(0, 32, result.get("CustType"));
				print(0, 50, formatAmt(bal, 0), "R");
				print(0, 58, formatAmt(result.get("StoreRate"), 4), "R");
				print(0, 60, result.get("ClItem"));
				print(0, 78, result.get("RuleCode"));
				
				//行業別擷取長度
				String tmpName = result.get("IndustryCode") + ":" + result.get("IndustryItem");
				String cdInName = tmpName.length() < 13 ? tmpName : tmpName.substring(0, 12) + "...";
				
				if (result.get("IndustryCode").equals("06000")) {
					print(0, 90, "","L");
				} else {
					print(0, 90, cdInName,"L");
				}

				countByAcct++;
				sumByAcct = sumByAcct.add(bal);
				count++;
				sum = sum.add(bal);
			}
		}

		printSumByAcct();
		printSum();

		this.close();
	}

	private void printSumByAcct() {
		this.print(1, 0,
				"  -------------------------------------------------------------------------------------------------------------------  ");
		this.print(1, 0, "    小    計                     筆                              元");
		this.print(0, 30, formatAmt("" + countByAcct, 0), "R");
		this.print(0, 62, formatAmt(sumByAcct, 0), "R");
		this.print(1, 0, "");
	}

	private void printSum() {
		this.print(1, 0,
				"  -------------------------------------------------------------------------------------------------------------------  ");
		this.print(1, 0, "    合    計                     筆                              元");
		this.print(0, 30, formatAmt("" + count, 0), "R");
		this.print(0, 62, formatAmt(sum, 0), "R");
		this.print(1, 0,
				"  -------------------------------------------------------------------------------------------------------------------  ");
	}

}
