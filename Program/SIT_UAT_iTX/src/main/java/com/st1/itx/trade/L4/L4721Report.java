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
import com.st1.itx.db.service.springjpa.cm.BankStatementServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;

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
	public BankStatementServiceImpl bankStatementServiceImpl;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public BaTxCom baTxCom;

	String headerCustName = "";
	String headerCustNo = "0";
	String headerPrintDate = "0";
	String headerRepayDate = "0";
	String headerRepayTypeDesc = "";
	String headerLoanBal = "0";
	String headerSumAmt = "0";
	String headerTxAmt = "0";
	Map<tmpFacm, Integer> reportedCode = new HashMap<tmpFacm, Integer>();
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
		print(-6, 98, formatAmt(headerSumAmt, 0), "R");

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
		print(-33, 44, headerTxAmt, "R");
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

		Slice<BatxRateChange> sBatxRateChange = null;
		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();

		sBatxRateChange = batxRateChangeService.findL4321Report(adjDate, adjDate, custType1, custType2, txKind, 2, this.index, this.limit, titaVo);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange != null && lBatxRateChange.size() != 0) {

			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4721", "放款本息對帳單暨繳息通知單", "密", "8.5,12", "P");

			for (BatxRateChange tBatxRateChange : lBatxRateChange) {
				List<Map<String, String>> listBankStatement = new ArrayList<Map<String, String>>();
				int custNo = tBatxRateChange.getCustNo();
				int facmNo = tBatxRateChange.getFacmNo();

				tmpFacm tmp = new tmpFacm(custNo, facmNo);

//				每張表有該戶號額度對應之Header，所以必須有兩個迴圈
//				第一層為應產出list，第二層為產出客戶之列印內容
				try {
					listBankStatement = bankStatementServiceImpl.doQuery(custNo, facmNo, adjDate, titaVo);
				} catch (Exception e) {
					this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
					throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
				}

				if (listBankStatement == null || listBankStatement.isEmpty()) {
					// 產空表?
					this.info("custNo ..." + custNo);
					this.info("facmNo ..." + facmNo);
					this.info("listBankStatement == null ...");
				} else {
					if (!reportedCode.containsKey(tmp)) {
						reportedCode.put(tmp, 1);
					} else {
						this.info("此戶號額度已產出通知單，continue...");
						continue;
					}

					Map<String, String> headerBankStatement = listBankStatement.get(0);

					// 先更新表頭資料
					setHead(headerBankStatement, custNo, facmNo);

					// 有資料產表
					cnt = cnt + 1;

					this.info("custNo ..." + custNo);
					this.info("facmNo ..." + facmNo);
					this.info("cnt ..." + cnt);

					if (cnt >= 2) {
						this.newPage();
					}

					for (Map<String, String> rowBankStatement : listBankStatement) {
						print(1, 1, "　");

						// 入帳日期
						print(0, 1, showRocDate(rowBankStatement.get("F0"), 3));

						// 計息期間
						String dateRange = " ";
						String startDate = rowBankStatement.get("F1");
						String endDate = rowBankStatement.get("F2");

						// 組成yyymmdd-yyymmdd
						if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
							dateRange = showRocDate(startDate, 3) + "-" + showRocDate(endDate, 3);
						}

						print(0, 10, dateRange);

						// 繳款方式
						print(0, 26, rowBankStatement.get("F3"));

						// 繳款金額
						print(0, 47, formatAmt(rowBankStatement.get("F4"), 0), "R");

						// 攤還本金
						print(0, 60, formatAmt(rowBankStatement.get("F5"), 0), "R");

						// 繳息金額
						print(0, 73, formatAmt(rowBankStatement.get("F6"), 0), "R");

						// 違約金
						String bt = "";
						if (!"0".equals(formatAmt(rowBankStatement.get("F7"), 0))) {
							bt = formatAmt(rowBankStatement.get("F7"), 0);
						}
						print(0, 82, bt, "R");

						// 火險費或其他費用
						String fe = "";
						if (!"0".equals(formatAmt(rowBankStatement.get("F8"), 0))) {
							fe = formatAmt(rowBankStatement.get("F8"), 0);
						}
						print(0, 98, fe, "R");
					}

					this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");

					// 額度號碼
					print(0, 6, FormatUtil.pad9("" + facmNo, 3));

					// 利率變動日
					String rateChangeDate = showRocDate(headerBankStatement.get("F16"), 0);
					print(0, 16, rateChangeDate);

					// 原利率
					String originRate = formatAmt(headerBankStatement.get("F17"), 2) + "%";
					print(0, 43, originRate, "R");

					// 現在利率
					String newRate = formatAmt(headerBankStatement.get("F18"), 2) + "%";
					print(0, 55, newRate, "R");

					this.print(1, 1, "＊其他額度利率，若有調整另行通知。");

				} // if list.size > 0
			} // for
			long sno = this.close();
			this.toPdf(sno);
		} else {
			throw new LogicException("E0001", "查無資料");
		}
	}

	private Map<String, BigDecimal> doBatxCom(int custNo, int facmNo, int nextPayIntDate, TitaVo titaVo) throws LogicException {

		Map<String, BigDecimal> mapResult = new HashMap<String, BigDecimal>();

		// 初始化
		mapResult.put("TempAmt", BigDecimal.ZERO);
		mapResult.put("ShortAmt", BigDecimal.ZERO);
		mapResult.put("TxAmt", BigDecimal.ZERO);

		baTxCom.setTxBuffer(this.getTxBuffer());

		this.info("nextPayIntDate ..." + nextPayIntDate);

		if (nextPayIntDate > 19110000) {
			nextPayIntDate = nextPayIntDate - 19110000;
		}

		this.info("nextPayIntDate ..." + nextPayIntDate);

		List<BaTxVo> listBaTxVo = baTxCom.settingUnPaid(nextPayIntDate, custNo, facmNo, 0, 1, BigDecimal.ZERO, titaVo);

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			for (BaTxVo tBaTxVo : listBaTxVo) {
				// 暫收款累加
				if (tBaTxVo.getDataKind() == 3) {
					mapResult.put("TempAmt", mapResult.get("TempAmt").add(tBaTxVo.getUnPaidAmt()));
				}
				if (tBaTxVo.getDataKind() == 1) {
					// 短收款累加
					if (tBaTxVo.getReceivableFlag() == 5) {
						mapResult.put("ShortAmt", mapResult.get("ShortAmt").add(tBaTxVo.getUnPaidAmt()));
					}
				}
				// 應收期金
				if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getRepayType() == 1) {
					mapResult.put("TxAmt", mapResult.get("TxAmt").add(tBaTxVo.getUnPaidAmt()));
				}
			}
		}
		return mapResult;
	}

	private void setHead(Map<String, String> headerBankStatement, int custNo, int facmNo) throws NumberFormatException, LogicException {
		headerCustName = headerBankStatement.get("F10");
		headerCustNo = headerBankStatement.get("F11");
		headerPrintDate = showRocDate(titaVo.getEntDyI(), 1);
		headerRepayDate = FormatUtil.pad9(headerBankStatement.get("F13"), 8).substring(6, 8) + "日";
		headerRepayTypeDesc = headerBankStatement.get("F14");
		if (headerBankStatement.get("F15") != null) {
			headerLoanBal = headerBankStatement.get("F15");
		}

		int nextPayIntDate = 0;

		if (headerBankStatement.get("F19") != null && !"".equals(headerBankStatement.get("F19"))) {
			nextPayIntDate = Integer.parseInt(headerBankStatement.get("F19"));
		}

		Map<String, BigDecimal> mapAmts = doBatxCom(custNo, facmNo, nextPayIntDate, titaVo);

		headerTxAmt = mapAmts.get("TxAmt").toString();

		headerSumAmt = mapAmts.get("TempAmt").subtract(mapAmts.get("ShortAmt")).toString();
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
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
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private L4721Report getEnclosingInstance() {
			return L4721Report.this;
		}
	}
}
