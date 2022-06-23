package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/*
 * *
 * @author Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L4721Report2 extends MakeReport {

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	DateUtil dateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	MakeFile makeFile;

	@Autowired
	Parse parse;

	@Autowired
	public L4721ServiceImpl l4721ServiceImpl;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public BaTxCom baTxCom;

	String headerCustName = "";
	String headerCustNo = "0";
	String headerFacmNo = "0";
	String headerPrintDate = "0";
	String headerRepayDate = "0";
	String headerRepayTypeDesc = "";
	String headerLoanBal = "0";
	String headerExcessive = "";
	String headerDueAmt = "";

	private HashMap<Integer, tmpFacm> sameMap = new HashMap<>();

	String fileName = "L4721.txt";

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		this.info("L4721Report2 exec start");

		this.setTxBuffer(txbuffer);
		baTxCom.setTxBuffer(txbuffer);

		List<String> file = getData(titaVo);

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + "-L4721.txt", fileName, 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4721", titaVo.getTxCode() + " 已產生L4721.txt", titaVo);
	}

	private List<String> getData(TitaVo titaVo) throws LogicException {

		this.info("L4721Report2 getData start");

		List<String> result = new ArrayList<>();

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

		sBatxRateChange = batxRateChangeService.findL4321Report(adjDate, adjDate, custType1, custType2, txKind, 0, 9, 2,
				this.index, this.limit, titaVo);

		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange == null || lBatxRateChange.size() == 0) {
			return result;
		}

		/*
		 * 011 1 0 0 0 台北市信義區永吉路１２０巷５０弄１號３樓 0001743 陳清耀 02 陳＊耀 0001743 10 日 銀行扣款
		 * 0109091600003683931+00000000000+ 030109041301090310-01090410 銀行扣款
		 * 0000037738+0000032175+00005563+00000000+00000000+ 030109042801090410-01090428
		 * 匯款轉帳 0000100000+0000100000+00000000+00000000+00000000+
		 * 030109051201090410-01090510 銀行扣款
		 * 0000036863+0000031402+00005461+00000000+00000000+ 030109061101090510-01090610
		 * 銀行扣款 0000036780+0000031446+00005334+00000000+00000000+
		 * 030109071301090610-01090710 銀行扣款
		 * 0000036780+0000031490+00005290+00000000+00000000+ 030109081101090710-01090810
		 * 銀行扣款 0000038939+0000031534+00005246+00000000+00000000+ 030109081200000000
		 * 00000000 0000000000+0000000000+00000000+00000000+00002159+
		 * 030109091101090810-01090910 銀行扣款
		 * 0000036780+0000031820+00004960+00000000+00000000+ 04 45 額度 003 利率自 109 年 09 月
		 * 01 日起， 由 1.68% 調整為 1.41% 。 45 ※其他額度利率，若有調整另行通知。
		 * 0500036341+9510200000174395103000001743
		 */
		for (BatxRateChange tBatxRateChange : lBatxRateChange) {

			List<Map<String, String>> listL4721Temp = new ArrayList<Map<String, String>>();
			List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();

//			// 放款利率變動檔生效日，利率未變動為零
			if (tBatxRateChange.getTxEffectDate() == 0) {
				continue;
			}
			// 戶號不同
			if (custNo == tBatxRateChange.getCustNo() && facmNo == tBatxRateChange.getFacmNo()) {
				continue;
			}

			custNo = tBatxRateChange.getCustNo();
			facmNo = tBatxRateChange.getFacmNo();

			try {
				listL4721Temp = l4721ServiceImpl.TempQuery(custNo, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl TempQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			try {
				listL4721Head = l4721ServiceImpl.doQuery(custNo, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (listL4721Temp != null && !listL4721Temp.isEmpty() && listL4721Head != null
					&& !listL4721Head.isEmpty()) {
				// 準備第一張
				Map<String, String> mapL4721Head = listL4721Head.get(0);
				Map<String, String> mapL4721Temp = listL4721Temp.get(0);

				// 先更新表頭資料
				setHead(mapL4721Head, parse.stringToInteger(mapL4721Head.get("CustNo")),
						parse.stringToInteger(mapL4721Head.get("FacmNo")),
						parse.stringToInteger(mapL4721Head.get("TxEffectDate")));

				String line = "";

				// 01
				line += "01";
				line += "";
				// 加入明細
				result.add(line);
				line = "";

				List<Map<String, String>> listL4721Detail = new ArrayList<Map<String, String>>();

				try {
					listL4721Detail = l4721ServiceImpl.doDetail(custNo, titaVo);
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
							parse.stringToInteger(listL4721Detail.get(0).get("TxEffectDate")) - 19110000);

					int times = 0;
					int txeffectdate = 0;
					BigDecimal presentrate = new BigDecimal("0");
					BigDecimal adjustedrate = new BigDecimal("0");

					// 02
					line += "02";
					line += " ";
					// 加入明細
					result.add(line);

					for (Map<String, String> mapL4721Detail : listL4721Detail) {

						if (!sameFlg) { // 額度不同天 印不同張

							if (tempfacmno != parse.stringToInteger(mapL4721Detail.get("FacmNo"))) {

								if (txeffectdate != 0) {
									this.print(1, 1, "額度　　　利率自　　　　　　　起，　由　　　　調整為　　　　。");
//
//									// 03 資料
//									line += "03";
//									line += "";
//									// 加入明細
//									result.add(line);
								}

								setHead(mapL4721Detail, parse.stringToInteger(mapL4721Detail.get("CustNo")),
										parse.stringToInteger(mapL4721Detail.get("FacmNo")),
										parse.stringToInteger(mapL4721Detail.get("TxEffectDate")));

							} // if
						} // if

						// 計息期間
						

						// 03 資料
						line += "03";
						line += "";
						// 加入明細
						result.add(line);
						
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

			line += "" + dateRange;
			
//			if ("-".equals(dateRange.trim())) {
//				dateRange = "";
//			}
//			print(0, 10, dateRange);
//
//			if (parse.stringToInteger(mapL4721Detail.get("RepayCode")) != 9) {
//				// 繳款方式
//				print(0, 26, mapL4721Detail.get("RepayCodeX"));
//			}
//			// 繳款金額
//			print(0, 47, formatAmt(mapL4721Detail.get("TxAmt"), 0), "R");
//
//			// 攤還本金
//			print(0, 60, formatAmt(mapL4721Detail.get("Principal"), 0), "R");
//
//			// 繳息金額
//			print(0, 73, formatAmt(mapL4721Detail.get("Interest"), 0), "R");
//
//			// 違約金
//			String bt = "";
//			if (!"0".equals(formatAmt(mapL4721Detail.get("BreachAmt"), 0))) {
//				bt = formatAmt(mapL4721Detail.get("BreachAmt"), 0);
//			}
//			print(0, 82, bt, "R");
//
//			// 火險費或其他費用
//			String fe = "";
//			if (!"0".equals(formatAmt(mapL4721Detail.get("OtherFee"), 0))) {
//				fe = formatAmt(mapL4721Detail.get("OtherFee"), 0);
//			}
//			print(0, 98, fe, "R");
//
//			times++;
//

						// 03 資料
						line += "03";
						line += "";
						// 加入明細
						result.add(line);
//			// 最後一筆
						if (times == listL4721Detail.size() && txeffectdate != 0) {

							// 04
							line += "04";
							line += "";
							// 加入明細
							result.add(line);

							if (sameFlg) { // 所有額度都同一天 印同一張

								for (Integer key : sameMap.keySet()) {

									// 45

									this.info(line);

									// 加入明細
									result.add(line);
									line = "";
									// 加入換行
									result.add(line);

								}
								// this.print(1, 1, "＊其他額度利率，若有調整另行通知。");
							} else {
								// this.print(1, 1, "額度 利率自 起， 由 調整為 。");

								// 45

								this.info(line);

								// 加入明細
								result.add(line);
								line = "";
								// 加入換行
								result.add(line);
							}

							break;
						}
//
						tmpFacm tmp = new tmpFacm(parse.stringToInteger(mapL4721Detail.get("FacmNo")),
								parse.stringToInteger(mapL4721Detail.get("TxEffectDate")),
								new BigDecimal(mapL4721Detail.get("PresentRate")),
								new BigDecimal(mapL4721Detail.get("AdjustedRate")));
//
						if (!sameMap.containsKey(parse.stringToInteger(mapL4721Detail.get("FacmNo")))) {
							sameMap.put(parse.stringToInteger(mapL4721Detail.get("FacmNo")), tmp);
						}

						tempfacmno = parse.stringToInteger(mapL4721Detail.get("FacmNo"));
						txeffectdate = parse.stringToInteger(mapL4721Detail.get("TxEffectDate"));
						presentrate = new BigDecimal(mapL4721Detail.get("PresentRate"));
						adjustedrate = new BigDecimal(mapL4721Detail.get("AdjustedRate"));

					} // for
				} // if

			}

		}

		return result;
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

//		暫時紀錄戶號額度
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

		private L4721Report2 getEnclosingInstance() {
			return L4721Report2.this;
		}
	}

}
