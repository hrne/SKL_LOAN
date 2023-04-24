package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
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
	CustNoticeCom custNoticeCom;

	@Autowired
	Parse parse;

	@Autowired
	DateUtil dateUtil;

	String headerCustName = "";
	String headerCustNo = "0";
	String headerFacmNo = "0";
	String headerPrintDate = "0";
	String headerRepayDate = "0";
	String headerRepayTypeDesc = "";
	String headerLoanBal = "0";
	String headerExcessive = "";
	String headerDueAmt = "";
	int cnt = 0;

	private HashMap<Integer, tmpFacm> sameMap = new HashMap<>();
	private Boolean first = true;

	@Override
	public void printHeader() {

		this.info("L4721Report.printHeader");

		this.setFontSize(16);
		print(-1, 33, "新光人壽保險股份有限公司", "C");

		this.setFontSize(14);
		print(-2, 37, "放款本息對帳暨繳息通知單", "C");

		this.setFontSize(10);

		print(-4, 1, "客戶名稱：" + headerCustName, "L");
		if (first) {
			print(-4, 57, "戶　　號：" + headerCustNo, "L");
		} else {
			print(-4, 57, "戶　　號：" + headerCustNo + "-" + headerFacmNo, "L");
		}
		print(-4, 78, "列印日期：" + headerPrintDate, "L");

		print(-5, 1, "應繳日：" + headerRepayDate, "L");
		print(-5, 57, "繳款方式：" + headerRepayTypeDesc, "L");
		print(-5, 78, "貸款餘額：", "L");
		print(-5, 98, formatAmt(headerLoanBal, 0), "R");

		print(-6, 78, "累溢短繳：", "L");
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
		print(-45, 3, "期款專用帳號：9510200" + headerCustNo + "　　還本專用帳號：9510300" + headerCustNo);

		this.setFontSize(10);
	}

	/**
	 * titaVo需有利率種類TxKind及利率調整日期AdjDate
	 * 
	 * @param titaVo   TitaVo
	 * @param txbuffer TxBuffer
	 * @param txKind   利率種類
	 * @param kindItem 利率種類名稱
	 * @throws LogicException ...
	 */
	public void exec(TitaVo titaVo, TxBuffer txbuffer, int txKind, String kindItem) throws LogicException {
		this.info("L4721Report.printHeader");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		this.setTxBuffer(txbuffer);
		baTxCom.setTxBuffer(txbuffer);

		int sAdjDate = Integer.parseInt(titaVo.getParam("sAdjDate")) + 19110000;
		int eAdjDate = Integer.parseInt(titaVo.getParam("eAdjDate")) + 19110000;
		int custType1 = 0;
		int custType2 = 0;

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

		sBatxRateChange = batxRateChangeService.findL4321Report(sAdjDate, eAdjDate, custType1, custType2, txKind, 0, 9,
				2, this.index, this.limit, titaVo);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange == null || lBatxRateChange.size() == 0) {
			throw new LogicException("E0001", "查無資料");
		}

//		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
//				.setRptCode("L4721").setRptItem("放款本息對帳單暨繳息通知單(" + kindItem + ")").setSecurity("密")
//				.setRptSize("inch,8.5,12").setPageOrientation("P").build();
//
//		this.openForm(titaVo, reportVo);

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4721", "放款本息對帳單暨繳息通知單(" + kindItem + ")", "密", "8.5,12", "P");

		Boolean Firstfg = false;

		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");

		for (BatxRateChange tBatxRateChange : lBatxRateChange) {

			List<Map<String, String>> listL4721Temp = new ArrayList<Map<String, String>>();
			List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();

//			// 放款利率變動檔生效日，利率未變動為零
			if (tBatxRateChange.getTxEffectDate() == 0) {
				continue;
			}
			// 相同戶號跳過
			if (custNo == tBatxRateChange.getCustNo()) {
				continue;
			}

			// 不同戶號額度相同跳過(也可能換戶號時額度相同)
			if (custNo == tBatxRateChange.getCustNo() && facmNo == tBatxRateChange.getFacmNo()) {
				continue;
			}

			custNo = tBatxRateChange.getCustNo();
			facmNo = tBatxRateChange.getFacmNo();

			try {

				listL4721Temp = l4721ServiceImpl.TempQuery(custNo, isday, ieday, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl TempQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			try {
				listL4721Head = l4721ServiceImpl.doQuery(custNo, isday, ieday, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			// 檢查 CustNotice 確認這份表是否能出
			// 跳過 測試用
			if (!custNoticeCom.checkIsLetterSendable(null, custNo, facmNo, "L4721", titaVo))
				continue;

			// 印出有 暫收款額度000的資料 下方帶出利率變動額度

			Boolean HeadFlag = false;
			if (listL4721Temp != null && !listL4721Temp.isEmpty() && listL4721Head != null
					&& !listL4721Head.isEmpty()) {

				HeadFlag = true;
				first = true;
				// 準備第一張
				Map<String, String> mapL4721Head = listL4721Head.get(0);
				Map<String, String> mapL4721Temp = listL4721Temp.get(0);

				// 先更新表頭資料
				setHead(mapL4721Head, parse.stringToInteger(mapL4721Head.get("CustNo")),
						parse.stringToInteger(mapL4721Head.get("FacmNo")),
						parse.stringToInteger(mapL4721Head.get("TxEffectDate")));

				if (!Firstfg) { // 第一筆戶號不先換頁
					Firstfg = true;
				} else {
					this.newPage();
				}

				print(1, 1, "　");

				// 入帳日期
				print(0, 1, showRocDate(mapL4721Temp.get("EntryDate"), 3));

				// 繳款方式
				print(0, 26, mapL4721Temp.get("RepayCodeX"));

				// 繳款金額
				print(0, 47, formatAmt(mapL4721Temp.get("TxAmt"), 0), "R");

				for (Map<String, String> Head : listL4721Head) {
					if ("Y".equals(Head.get("Flag")) && parse.stringToInteger(Head.get("TxEffectDate")) != 0) {
						this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");

						// 額度號碼
						print(0, 6, FormatUtil.pad9("" + Head.get("FacmNo"), 3));

						// 利率變動日
						String rateChangeDate = showRocDate(Head.get("TxEffectDate"), 0);
						print(0, 16, rateChangeDate);

						// 原利率
						String originRate = formatAmt(Head.get("PresentRate"), 2) + "%";
						print(0, 43, originRate, "R");

						// 現在利率
						String newRate = formatAmt(Head.get("AdjustedRate"), 2) + "%";
						print(0, 55, newRate, "R");
					}
				}

				first = false;
				// 先更新表頭資料
				setHead(mapL4721Head, parse.stringToInteger(mapL4721Head.get("CustNo")),
						parse.stringToInteger(mapL4721Head.get("FacmNo")),
						parse.stringToInteger(mapL4721Head.get("TxEffectDate")));
				this.newPage();
			}

			first = false;
			List<Map<String, String>> listL4721Detail = new ArrayList<Map<String, String>>();

			try {
				listL4721Detail = l4721ServiceImpl.doDetail(custNo, isday, ieday, tBatxRateChange.getAdjDate(), titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (listL4721Detail != null && !listL4721Detail.isEmpty()) {

				// 判斷是否都為同一天

				Boolean sameFlg = false;

				int tempdate = parse.stringToInteger(listL4721Detail.get(0).get("SpecificDd"));
				for (Map<String, String> mapL4721Detail : listL4721Detail) {
					if (tempdate != parse.stringToInteger(mapL4721Detail.get("SpecificDd"))) {
						sameFlg = true;
						break;
					}
				}

				int tempfacmno = parse.stringToInteger(listL4721Detail.get(0).get("FacmNo"));
				int tempcustno = parse.stringToInteger(listL4721Detail.get(0).get("CustNo"));

				// 先更新表頭資料
				setHead(listL4721Detail.get(0), tempcustno, tempfacmno,
						parse.stringToInteger(listL4721Detail.get(0).get("TxEffectDate")));

				if (!HeadFlag) { // 下一筆資料沒有第一張時的換頁
					this.newPage();
				}

				int times = 0;
				int txeffectdate = 0;
				BigDecimal presentrate = new BigDecimal("0");
				BigDecimal adjustedrate = new BigDecimal("0");

				for (Map<String, String> mapL4721Detail : listL4721Detail) {

					if (!sameFlg) { // 額度不同天 印不同張

						if (tempfacmno != parse.stringToInteger(mapL4721Detail.get("FacmNo"))) {

							if (txeffectdate != 0) {
								this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");

								// 額度號碼
								print(0, 6, FormatUtil.pad9("" + tempfacmno, 3));

								// 利率變動日
								String rateChangeDate = showRocDate(txeffectdate, 0);
								print(0, 16, rateChangeDate);

								// 原利率
								String originRate = formatAmt(presentrate, 2) + "%";
								print(0, 43, originRate, "R");

								// 現在利率
								String newRate = formatAmt(adjustedrate, 2) + "%";
								print(0, 55, newRate, "R");
							}

							setHead(mapL4721Detail, parse.stringToInteger(mapL4721Detail.get("CustNo")),
									parse.stringToInteger(mapL4721Detail.get("FacmNo")),
									parse.stringToInteger(mapL4721Detail.get("TxEffectDate")));

							this.newPage();
						} // if
					} // if

					writedetail(mapL4721Detail);

					times++;

					// 最後一筆
					if (times == listL4721Detail.size() && txeffectdate != 0) {

						if (sameFlg) { // 所有額度都同一天 印同一張
							for (Integer key : sameMap.keySet()) {
								this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");
								writeLastdetail(key, sameMap.get(key).getTxEffectDate(),
										sameMap.get(key).getPresentRate(), sameMap.get(key).getAdjustedRate());
							} // for
						} else {
							this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");
							writeLastdetail(tempfacmno, txeffectdate, presentrate, adjustedrate);
						} // else

						this.print(1, 1, "＊其他額度利率，若有調整另行通知。");

						break;
					}

					tmpFacm tmp = new tmpFacm(parse.stringToInteger(mapL4721Detail.get("FacmNo")),
							parse.stringToInteger(mapL4721Detail.get("TxEffectDate")),
							new BigDecimal(mapL4721Detail.get("PresentRate")),
							new BigDecimal(mapL4721Detail.get("AdjustedRate")));

					if (!sameMap.containsKey(parse.stringToInteger(mapL4721Detail.get("FacmNo")))) {
						sameMap.put(parse.stringToInteger(mapL4721Detail.get("FacmNo")), tmp);
					}

					tempfacmno = parse.stringToInteger(mapL4721Detail.get("FacmNo"));
					txeffectdate = parse.stringToInteger(mapL4721Detail.get("TxEffectDate"));
					presentrate = new BigDecimal(mapL4721Detail.get("PresentRate"));
					adjustedrate = new BigDecimal(mapL4721Detail.get("AdjustedRate"));

				} // for
			} // if
		} // for
		long sno = this.close();
		this.toPdf(sno);
	}

	private void setHead(Map<String, String> headerBankStatement, int custNo, int facmNo, int effectDate)
			throws NumberFormatException, LogicException {
		this.info("L4721Report.setHead" + custNo + "-" + facmNo + "" + effectDate);
		headerCustName = headerBankStatement.get("CustName");
		headerCustNo = String.format("%07d", Integer.valueOf(headerBankStatement.get("CustNo")));
		headerFacmNo = String.format("%03d", Integer.valueOf(headerBankStatement.get("FacmNo")));
		headerPrintDate = showRocDate(titaVo.getCalDy(), 1);
		headerRepayDate = headerBankStatement.get("SpecificDd") + "日";
		headerRepayTypeDesc = headerBankStatement.get("RepayCodeX");
		headerLoanBal = headerBankStatement.get("LoanBal");

		headerDueAmt = "";
		headerExcessive = "";
		if (effectDate != 0) {
			baTxCom.getDueAmt(effectDate, custNo, facmNo, 0, titaVo);
			headerDueAmt = "" + (baTxCom.getPrincipal().add(baTxCom.getInterest()));
			headerExcessive = "" + baTxCom.getExcessive().subtract(baTxCom.getShortfall());
		} else {
			headerDueAmt = headerBankStatement.get("DueAmt");
		}

	}

//	暫時紀錄戶號額度
	/**
	*/
	private class tmpFacm {

		public tmpFacm(int facmNo, int txeffectdate, BigDecimal presentrate, BigDecimal adjustedrate) {
			this.setFacmNo(facmNo);
			this.setTxEffectDate(txeffectdate);
			this.setPresentRate(presentrate);
			this.setAdjustedRate(adjustedrate);
		}

		private int facmNo = 0;
		private int txeffectdate = 0;
		private BigDecimal presentrate = new BigDecimal("0");
		private BigDecimal adjustedrate = new BigDecimal("0");

		@Override
		public String toString() {
			return "tmpFacm [facmNo=" + facmNo + ", TxEffectDate=" + txeffectdate + ", PresentRate=" + presentrate
					+ ", AdjustedRate=" + adjustedrate + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + facmNo;
			result = prime * result + txeffectdate;
			result = prime * result + presentrate.intValue();
			result = prime * result + adjustedrate.intValue();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (facmNo != other.facmNo)
				return false;
			if (txeffectdate != other.txeffectdate)
				return false;
			if (presentrate != other.presentrate)
				return false;
			if (adjustedrate != other.adjustedrate)
				return false;
			return true;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private int getTxEffectDate() {
			return txeffectdate;
		}

		private void setTxEffectDate(int txeffectdate) {
			this.txeffectdate = txeffectdate;
		}

		private BigDecimal getPresentRate() {
			return presentrate;
		}

		private void setPresentRate(BigDecimal presentrate) {
			this.presentrate = presentrate;
		}

		private BigDecimal getAdjustedRate() {
			return adjustedrate;
		}

		private void setAdjustedRate(BigDecimal adjustedrate) {
			this.adjustedrate = adjustedrate;
		}

		private L4721Report getEnclosingInstance() {
			return L4721Report.this;
		}
	}

	private void writedetail(Map<String, String> mapL4721Detail) throws LogicException {
		print(1, 1, "　");

		// 入帳日期
		print(0, 1, showRocDate(mapL4721Detail.get("EntryDate"), 3));

		// 計息期間
		String dateRange = " ";
		String startDate = mapL4721Detail.get("IntStartDate");
		String endDate = mapL4721Detail.get("IntEndDate");
		String tstartDate = "       ";
		String tendDate = "       ";
		// 組成yyymmdd-yyymmdd
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {

			if (!"".equals(showRocDate(startDate, 3))) {
				tstartDate = showRocDate(startDate, 3);
			}

			if (!"".equals(showRocDate(endDate, 3))) {
				tendDate = showRocDate(endDate, 3);
			}

			dateRange = tstartDate + "-" + tendDate;
		}

		if ("-".equals(dateRange.trim())) {
			dateRange = "";
		}
		print(0, 10, dateRange);

		if (parse.stringToInteger(mapL4721Detail.get("RepayCode")) != 9) {
			// 繳款方式
			print(0, 26, mapL4721Detail.get("RepayCodeX"));
		}
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

	private void writeLastdetail(int facmno, int txeffectdate, BigDecimal presentrate, BigDecimal adjustedrate) {
		// 額度號碼
		print(0, 6, FormatUtil.pad9("" + facmno, 3));

		// 利率變動日
		String rateChangeDate = showRocDate(txeffectdate, 0);
		print(0, 16, rateChangeDate);

		// 原利率
		String originRate = formatAmt(presentrate, 2) + "%";
		print(0, 43, originRate, "R");

		// 現在利率
		String newRate = formatAmt(adjustedrate, 2) + "%";
		print(0, 55, newRate, "R");
	}
}
