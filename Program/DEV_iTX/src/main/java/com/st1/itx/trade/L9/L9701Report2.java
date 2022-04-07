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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L9701ServiceImpl;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9701Report2 extends MakeReport {

	@Autowired
	L9701ServiceImpl l9701ServiceImpl;

	@Autowired
	CustMainService sCustMainService;

	@Autowired
	Parse parse;

	@Autowired
	CustNoticeCom custNoticeCom;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String custName;

	private String custNo;
	private String facmNo;

	int ptfg = 0;
	int entday = 0;

	@Override
	public void printHeader() {

//		this.setFontSize(16);
		this.info("L9701 exec" + this.titaVo.get("ENTDY"));
		this.print(-2, 70, "客 戶 往 來 費 用 明 細 表", "C");
		this.print(-3, 70, showRocDate(this.titaVo.get("ENTDY"), 0), "C");
		this.print(-4, 110, "印表日期：" + showRocDate(this.nowDate, 1) + " " + showTime(this.nowTime));

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
		this.setMaxRows(36);
	}

	private void printFaHead1() {
		this.print(1, 1, "額度     : " + String.format("%03d", Integer.valueOf(facmNo)));
		this.print(1, 33, "－－－－－　－－－－－－－－－－－－　－－－－－－－－　－－－－－");
		this.print(1, 33, "　入帳日　　　　　　費用類別　　　　　　　　金額　　　　　應繳日");
		this.print(1, 33, "－－－－－　－－－－－－－－－－－－　－－－－－－－－　－－－－－");
	}

	private void printFaHead2() {
		this.print(1, 1, "額度     : " + String.format("%03d", Integer.valueOf(facmNo)));
		this.print(1, 33, "　　　　　　－－－－－－－－－－－－　－－－－－－－－　－－－－－");
		this.print(1, 33, "　　　　　　　　　　費用類別　　　　　　　　金額　　　　　應繳日");
		this.print(1, 33, "　　　　　　－－－－－－－－－－－－　－－－－－－－－　－－－－－");
	}

	public void exec(TitaVo titaVo, List<BaTxVo> listBaTxVo) throws LogicException {

		this.info("L9701Report2 exec");
		entday = Integer.valueOf(titaVo.getParam("ENTDY"));

		this.custNo = String.format("%07d", parse.stringToInteger(titaVo.getParam("CustNo")));
		this.custName = "";
		this.facmNo = "";
		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		List<Map<String, String>> listL9701 = null;

		try {
			listL9701 = l9701ServiceImpl.doQuery2(titaVo);
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

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9701", "客戶往來費用明細表", "", "A4", "L");

		if (listL9701 != null && !listL9701.isEmpty()) {

			this.print(1, 1, "已收費用 :");

			for (Map<String, String> tL9701Vo : listL9701) {

//			String inputCustNo = titaVo.get("CustNo");
//			int recordCustNo = parse.stringToInteger(inputCustNo);
//			String recordFacmNoString = tL9701Vo.get("FacmNo");
//			int recordFacmNo = parse.stringToInteger(recordFacmNoString);
//
//			if (!custNoticeCom.checkIsLetterSendable(inputCustNo, recordCustNo, recordFacmNo, "L9701", titaVo)) {
//				continue;
//			}

				String rowFacmNo = String.format("%03d", parse.stringToInteger(tL9701Vo.get("F3")));

				// 額度號碼與前一筆不同時，印額度號碼
				if (facmNo.isEmpty() || !this.facmNo.equals(rowFacmNo)) {
					this.facmNo = rowFacmNo;
					printFaHead1();
				}

				// 已收費用
				report1(tL9701Vo);
			}
		}
		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			this.facmNo = "";
			this.print(1, 1, "未收費用 :");
			// 應收費用
			report2(listBaTxVo);

		}

		if (this.getNowPage() == 0) {
			this.print(1, 20, "******* 查無資料 ******");
		}

		this.close();
	}

	private void report1(Map<String, String> tL9701Vo) {
		this.print(1, 33, showRocDate(tL9701Vo.get("F0"), 1)); // 入帳日
		this.print(0, 44, tL9701Vo.get("F1"));
		this.print(0, 82, formatAmt(tL9701Vo.get("F2"), 0), "R");
	}

	private void report2(List<BaTxVo> listBaTxVo) {
		String feeType = "";

		for (BaTxVo tBaTxVo : listBaTxVo) {
			if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getReceivableFlag() != 4
					&& tBaTxVo.getUnPaidAmt().intValue() > 0) {

				String rowFacmNo = String.format("%03d", tBaTxVo.getFacmNo());

				// 額度號碼與前一筆不同時，印額度號碼
				if (this.facmNo.isEmpty() || !this.facmNo.equals(rowFacmNo)) {
					this.facmNo = rowFacmNo;
					printFaHead2();
				}

				switch (tBaTxVo.getRepayType()) {
				case 4:
					feeType = "帳管費";
					break;
				case 5:
					feeType = "火險費";
					break;
				case 6:
					feeType = "契變手續費";
					break;
				case 7:
					feeType = "法務費";
					break;
				case 9:
					feeType = "其他";
					break;
				default:
					feeType = "";
					break;
				}
				this.print(1, 44, feeType);
				this.print(0, 82, formatAmt(tBaTxVo.getUnPaidAmt(), 0), "R");
				this.print(0, 84, showRocDate(tBaTxVo.getPayIntDate(), 1));
			}
		}
	}
}
