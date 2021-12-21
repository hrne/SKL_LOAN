package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 製作利率變動對帳單 <BR>
 * 1.同一pdf印製該批對帳單 <BR>
 * 2. call by L4721<BR>
 * 
 * @author St1-Wei
 * @version 1.0.0
 */
@Component("L4721Report")
@Scope("prototype")
public class L4721Report extends MakeReport {

	@Autowired
	public L4721ServiceImpl l4721ServiceImpl;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	Parse parse;

	String headerCustName = "";
	String headerCustNo = "0";
	String headerPrintDate = "0";
	String headerRepayDate = "0";
	String headerRepayTypeDesc = "";
	String headerLoanBal = "0";
	String headerExcessive = "";
	String headerDueAmt = "";
	int cnt = 0;

	@Override
	public void printHeader() {

		this.info("L4721Report.printHeader");

		this.setFontSize(16);
		print(-1, 33, "新光人壽保險股份有限公司", "C");

		this.setFontSize(14);
		print(-2, 37, "放款本息對帳暨繳息通知單", "C");

		this.setFontSize(10);

		print(-4, 1, "客戶名稱：" + headerCustName, "L");
		print(-4, 60, "戶　　號：" + headerCustNo, "L");
		print(-4, 80, "列印日期：" + headerPrintDate, "L");

		print(-5, 1, "應繳日：" + headerRepayDate, "L");
		print(-5, 60, "繳款方式：" + headerRepayTypeDesc, "L");
		print(-5, 80, "貸款餘額：", "L");
		print(-5, 98, formatAmt(headerLoanBal, 0), "R");

		print(-6, 80, "累溢短繳：", "L");
		print(-6, 98, formatAmt(headerExcessive, 0), "R");

		print(-7, 1, "入帳日期　計息期間　　　　　繳款方式　　　繳款金額　　　攤還本金　　　繳息金額　　違約金　火險費或其他費用");

		print(-8, 1, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(26);

		// (x,y)
		// (5,425)┌┐(590,425)
		// (5,545)└┘(590,545)
		drawLine(5, 425, 590, 425);
		drawLine(5, 425, 5, 545);
		drawLine(590, 425, 590, 545);
		drawLine(5, 545, 590, 545);

		this.setFontSize(12);
		print(-33, 3, "＊利率調整後次月份應繳金額為＄　　　　　　　　元，如因部分還本、利率調整或契約內容");
		print(-33, 44, headerDueAmt, "R");
		print(-34, 3, "　變更，致使應繳金額有所變動時再另行通知。");
		print(-35, 3, "＊本公司各項放款利率公佈於全省各營業處所。");
		print(-36, 3, "＊本單之貸款餘額計算至列印日期為止。");
		print(-37, 3, "＊每月應繳金額以最新列印日期之通知單為準。");
		print(-38, 3, "＊本單據僅供對帳使用，不做任何證明用途。");
		print(-39, 3, "＊為了維護您的權益，請立即詳閱本帳單，並核對所列金額與日期是否與您的紀錄相符，");
		print(-40, 3, "　如有任何意見或發現不符，請立即電洽本公司 TEL: 02-23895858 轉 放款服務課");

		// (x,y)
		// (5,550)┌┐(590,550)
		// (5,615)└┘(590,615)
		drawLine(5, 550, 590, 550);
		drawLine(5, 550, 5, 615);
		drawLine(590, 550, 590, 615);
		drawLine(5, 615, 590, 615);

		print(-42, 3, "銀行匯款");
		print(-43, 3, "戶名：新光人壽保險股份有限公司");
		print(-44, 3, "解款行：新光銀行城內分行　新光銀行城內分行代號：1030116");
		print(-45, 3, "期款專用帳號：95102001302796　　還本專用帳號：95103001302796");

		this.setFontSize(10);
	}

	/**
	 * titaVo需有利率種類TxKind及利率調整日期AdjDate
	 * 
	 * @param titaVo   TitaVo
	 * @param txbuffer TxBuffer
	 * @throws LogicException ...
	 */
	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		this.info("L4721Report.printHeader");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		this.setTxBuffer(txbuffer);
		baTxCom.setTxBuffer(txbuffer);

		int adjDate = Integer.parseInt(titaVo.getParam("AdjDate")) + 19110000;
		int custType1 = 0;
		int custType2 = 0;
		int txKind = Integer.parseInt(titaVo.getParam("TxKind"));

//		輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//		客戶檔 0:個金1:企金2:企金自然人
		if (Integer.parseInt(titaVo.getParam("CustType")) == 2) {
			custType1 = 1;
			custType2 = 2;
		}

		int custNo = 0;
		int facmNo = 0;

		Slice<BatxRateChange> sBatxRateChange = null;
		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();

		sBatxRateChange = batxRateChangeService.findL4321Report(adjDate, adjDate, custType1, custType2, txKind, 2,
				this.index, this.limit, titaVo);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange == null || lBatxRateChange.size() == 0) {
			throw new LogicException("E0001", "查無資料");
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4721", "放款本息對帳單暨繳息通知單", "密", "8.5,12", "P");

		for (BatxRateChange tBatxRateChange : lBatxRateChange) {
			List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();
			// 放款利率變動檔生效日，利率未變動為零
			if (tBatxRateChange.getTxEffectDate() == 0) {
				continue;
			}
			if (custNo == tBatxRateChange.getCustNo() && facmNo == tBatxRateChange.getFacmNo()) {
				continue;
			}

			custNo = tBatxRateChange.getCustNo();
			facmNo = tBatxRateChange.getFacmNo();

//				每張表有該戶號額度對應之Header，所以必須有兩個迴圈
//				第一層為應產出list，第二層為產出客戶之列印內容
			try {
				listL4721Head = l4721ServiceImpl.doQuery(custNo, facmNo, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (listL4721Head == null || listL4721Head.isEmpty()) {
				// 產空表?
				this.info("custNo ..." + custNo);
				this.info("facmNo ..." + facmNo);
				this.info("listL4721Head == null ...");
				continue;
			}

			Map<String, String> mapL4721Head = listL4721Head.get(0);

			// 先更新表頭資料
			setHead(mapL4721Head, custNo, facmNo, tBatxRateChange.getTxEffectDate());

			// 有資料產表
			cnt = cnt + 1;

			if (cnt >= 2) {
				this.newPage();
			}
			List<Map<String, String>> listL4721Detail = new ArrayList<Map<String, String>>();

			try {
				listL4721Detail = l4721ServiceImpl.doDetail(custNo, facmNo, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (listL4721Detail != null && !listL4721Detail.isEmpty()) {

				for (Map<String, String> mapL4721Detail : listL4721Detail) {
					print(1, 1, "　");

					// 入帳日期
					print(0, 1, showRocDate(mapL4721Detail.get("EntryDate"), 3));

					// 計息期間
					String dateRange = " ";
					String startDate = mapL4721Detail.get("IntStartDate");
					String endDate = mapL4721Detail.get("IntEndDate");

					// 組成yyymmdd-yyymmdd
					if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
						dateRange = showRocDate(startDate, 3) + "-" + showRocDate(endDate, 3);
					}

					print(0, 10, dateRange);

					// 繳款方式
					print(0, 26, mapL4721Detail.get("RepayCodeX"));

					// 繳款金額
					print(0, 47, formatAmt(mapL4721Detail.get("TxAmt"), 0), "R");

					// 攤還本金
					print(0, 60, formatAmt(mapL4721Detail.get("Principal"), 0), "R");

					// 繳息金額
					print(0, 73, formatAmt(mapL4721Detail.get("Interest"), 0), "R");

					// 違約金
					String bt = "";
					if (!"0".equals(formatAmt(mapL4721Detail.get("BreachAmt"), 0))) {
						bt = formatAmt(mapL4721Detail.get("BreachAmt"), 0);
					}
					print(0, 82, bt, "R");

					// 火險費或其他費用
					String fe = "";
					if (!"0".equals(formatAmt(mapL4721Detail.get("OtherFee"), 0))) {
						fe = formatAmt(mapL4721Detail.get("OtherFee"), 0);
					}
					print(0, 98, fe, "R");
				}
			}

			this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");

			// 額度號碼
			print(0, 6, FormatUtil.pad9("" + facmNo, 3));

			// 利率變動日
			String rateChangeDate = showRocDate(tBatxRateChange.getTxEffectDate(), 0);
			print(0, 16, rateChangeDate);

			// 原利率
			String originRate = formatAmt(tBatxRateChange.getPresentRate(), 2) + "%";
			print(0, 43, originRate, "R");

			// 現在利率
			String newRate = formatAmt(tBatxRateChange.getAdjustedRate(), 2) + "%";
			print(0, 55, newRate, "R");

			this.print(1, 1, "＊其他額度利率，若有調整另行通知。");
		}
		long sno = this.close();
		this.toPdf(sno);
	}

	private void setHead(Map<String, String> headerBankStatement, int custNo, int facmNo, int effectDate)
			throws NumberFormatException, LogicException {
		this.info("L4721Report.setHead" + custNo + "-" + facmNo + "" + effectDate);
		headerCustName = headerBankStatement.get("CustName");
		headerCustNo = headerBankStatement.get("CustNo");
		headerPrintDate = showRocDate(titaVo.getCalDy(), 1);
		headerRepayDate = headerBankStatement.get("SpecificDd") + "日";
		headerRepayTypeDesc = headerBankStatement.get("RepayCodeX");
		headerLoanBal = headerBankStatement.get("LoanBal");
		baTxCom.getDueAmt(effectDate, custNo, facmNo, 0, titaVo);
		headerDueAmt = "" + (baTxCom.getPrincipal().add(baTxCom.getInterest()));
		headerExcessive = "" + baTxCom.getExcessive().subtract(baTxCom.getShortfall());

	}

}
