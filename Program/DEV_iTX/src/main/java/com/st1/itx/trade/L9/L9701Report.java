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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

/**
 * L9701Report 客戶往來本息明細表
 * 
 * @author ST1 Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L9701Report extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Autowired
	Parse parse;

	// 製表日期
	private String NowDate;
	// 製表時間
	private String NowTime;

	// 客戶姓名
	private String custName;

	// 戶號
	private String custNo;

	// 額度號碼
	private String facmNo;

	// 客戶主要擔保品地址
	private String clAddr;

	int entday = 0;

	int maturityDate = 0; // 到期日
	BigDecimal loanBal = BigDecimal.ZERO; // 放款餘額
	BigDecimal shortInt = BigDecimal.ZERO; // 短收利息
	BigDecimal shortPrin = BigDecimal.ZERO; // 短收本金
	BigDecimal overflow = BigDecimal.ZERO; // 溢收
	BigDecimal repaidPrin = BigDecimal.ZERO; // 應繳本金
	BigDecimal repaidInt = BigDecimal.ZERO; // 應繳利息
	BigDecimal repaidExp = BigDecimal.ZERO; // 應繳費用

	BigDecimal loanBalTotal = BigDecimal.ZERO; // 放款餘額合計
	BigDecimal shortIntTotal = BigDecimal.ZERO; // 短收利息合計
	BigDecimal shortPrinTotal = BigDecimal.ZERO; // 短收本金合計
	BigDecimal overflowTotal = BigDecimal.ZERO; // 溢收合計
	BigDecimal repaidPrinTotal = BigDecimal.ZERO; // 應繳本金合計
	BigDecimal repaidIntTotal = BigDecimal.ZERO; // 應繳利息合計
	BigDecimal repaidExpTotal = BigDecimal.ZERO; // 應繳費用合計

	@Override
	public void printHeader() {

		this.print(-2, 70, "客 戶 往 來 本 息 明 細 表", "C");

		this.print(-3, 70, showRocDate(this.titaVo.get("ENTDY"), 0), "C");

		this.print(-4, 110, "印表日期：" + showRocDate(this.NowDate, 1) + " " + showTime(this.NowTime));

		String iTYPE = titaVo.get("DateType");
		String iSDAY = showRocDate(titaVo.get("BeginDate"), 1);
		String iEDAY = showRocDate(titaVo.get("EndDate"), 1);

		if (iTYPE.equals("1")) {
			this.print(-5, 1, "入帳日期 : " + iSDAY + " - " + iEDAY);
		} else {
			this.print(-5, 1, "會計日期 : " + iSDAY + " - " + iEDAY);
		}

		this.print(-5, 110, "頁　　次：" + Integer.toString(this.getNowPage()));

		this.print(-6, 1, "戶號     : " + custNo + " " + custName);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(7);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(38);

	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	private void printFacHead() {
		this.print(1, 1, " ");
		this.print(1, 1, "額度     : " + String.format("%03d", Integer.valueOf(facmNo)));
		this.print(0, 22, "押品地址 : " + clAddr);
		this.print(1, 7, "　入帳日　　　計息本金　　　　　計息期間　　　　　利率　　　　利息　　　　　逾期息　　　　　違約金　　　　　本金　　　　本息合計");
		this.print(1, 7, "－－－－－　－－－－－－　－－－－－－－－－－　－－－－　－－－－－－　－－－－－－－　－－－－－－－　－－－－－－　－－－－－－");
	}

	public void exec(TitaVo titaVo, List<BaTxVo> listBaTxVo) throws LogicException {

		entday = titaVo.getEntDyI();

		this.custNo = String.format("%07d", parse.stringToInteger(titaVo.getParam("CustNo")));
		this.custName = "";
		this.facmNo = "";
		this.NowDate = dDateUtil.getNowStringRoc();
		this.NowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery1(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9701ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		CustMain tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);

		if (tCustMain != null) {
			this.custName = tCustMain.getCustName();
		}

		this.open(titaVo, entday, titaVo.getKinbr(), "L9701", "客戶往來本息明細表", "", "A4", "L");

		this.clAddr = "";

		for (Map<String, String> tL9701Vo : listL9701) {

			String rowfacmNo = String.format("%03d", parse.stringToInteger(tL9701Vo.get("F13")));
			clAddr = tL9701Vo.get("F14");
			
			// 若額度號碼與上一筆不同
			if (facmNo.isEmpty() || !this.facmNo.equals(rowfacmNo)) {
				facmNo = rowfacmNo;
				printFacHead();
			}
			printDetail(tL9701Vo);
		}

		this.close();
	}

	/**
	 * 列印已收明細
	 * 
	 * @param vo 單筆資料
	 */
	private void printDetail(Map<String, String> vo) {
		this.print(1, 7, showRocDate(vo.get("F0"), 1));
		this.print(0, 29, formatAmt(vo.get("F1"), 0), "R"); // 計息本金
		this.print(0, 31, showRocDate(vo.get("F2"), 2) + "-" + showRocDate(vo.get("F3"), 2)); // 計息期間
		this.print(0, 58, formatAmt(vo.get("F4"), 4), "R"); // 利率
		this.print(0, 71, formatAmt(vo.get("F5"), 0), "R"); // 利息
		this.print(0, 86, formatAmt(vo.get("F6"), 0), "R"); // 逾期息
		this.print(0, 100, formatAmt(vo.get("F7"), 0), "R"); // 違約金
		this.print(0, 113, formatAmt(vo.get("F8"), 0), "R"); // 本金
		this.print(0, 126, formatAmt(vo.get("F9"), 0), "R"); // 本息合計
//		 this.print(0, 128, showRocDate(vo.get("F10"), 1)); // 未繳才會有應繳日 但這裡只出已繳
	}
}
