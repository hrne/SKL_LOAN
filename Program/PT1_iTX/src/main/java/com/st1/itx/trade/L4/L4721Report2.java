package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

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
import com.st1.itx.util.format.FormatUtil;
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

	String fileName = "L4721.txt";

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		this.info("L4721Report2 exec start");

		this.titaVo = titaVo;
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

		for (BatxRateChange tBatxRateChange : lBatxRateChange) {

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

			List<Map<String, String>> listL4721Detail = new ArrayList<Map<String, String>>();

			try {
				listL4721Detail = l4721ServiceImpl.doDetail(custNo, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (listL4721Detail != null && !listL4721Detail.isEmpty()) {

				String line = "";

				int tempdate = parse.stringToInteger(listL4721Detail.get(0).get("SpecificDd"));
				for (Map<String, String> mapL4721Detail : listL4721Detail) {
					if (tempdate != parse.stringToInteger(mapL4721Detail.get("SpecificDd"))) {
//							sameFlg = true;
						break;
					}
				}

				int tempfacmno = parse.stringToInteger(listL4721Detail.get(0).get("FacmNo"));
//				int tempcustno = parse.stringToInteger(listL4721Detail.get(0).get("CustNo"));
				int times = 0;
				for (Map<String, String> mapL4721Detail : listL4721Detail) {

					// 相同戶號不同額度的輸出

					if (tempfacmno == parse.stringToInteger(mapL4721Detail.get("FacmNo"))) { // 相同額度
						// 該額度第一次進要印0102
						if (times == 0) {
							result = sameFacmno(mapL4721Detail, result, true);
						} else {
							result = sameFacmno(mapL4721Detail, result, false);
						}
						times++;
					} else { // 不同額度 印04並且切到下一個額度循環
						// 04

						line = "";
						line += "04";
						result.add(line);

						// 45

						// 45 額度 003 利率自 109 年 09 月 01 日起， 由 1.68% 調整為 1.41% 。
						line = "";
						line += "45";
						line += " 額度 " + FormatUtil.pad9(mapL4721Detail.get("FacmNo"), 3) + "     " + "利率自"
								+ showRocDate(mapL4721Detail.get("TxEffectDate"), 0) + "起，　由"
								+ formatAmt(mapL4721Detail.get("PresentRate"), 2) + "%" + "調整為"
								+ formatAmt(mapL4721Detail.get("AdjustedRate"), 2) + "。";
						result.add(line);

						// 05

						// 0500036341+9510200000174395103000001743
						line = "";
						line += "05";
						line += FormatUtil.pad9(headerDueAmt, 8) + "+" + "9510200"
								+ FormatUtil.pad9(mapL4721Detail.get("CustNo"), 7) + "9510300"
								+ FormatUtil.pad9(mapL4721Detail.get("CustNo"), 7);
						result.add(line);

						result = sameFacmno(mapL4721Detail, result, true);
						// 換額度要重新算次數
						times = 0;
					} // else

				} // for
			} // for
		} // for
		return result;
	}

	private List<String> sameFacmno(Map<String, String> tmap, List<String> result, Boolean same) throws LogicException {

		String line = "";

		if (same) {
			// 01

			// 011 1 0 0 0 台北市信義區永吉路１２０巷５０弄１號３樓 0001743 陳清耀
			// 011 1 0 0 0 台北市信義區永吉路１２０巷５０弄１號３樓 0001743 陳清耀

			String locationX = tmap.get("Location").toString();
			String locationXX = "";
			String s = "";
			for (int i = 0; i < locationX.length(); i++) {
				locationXX = toX(locationX.substring(i, i + 1), locationXX);
			}
			String X = "";
			X = toXX(locationXX, "　", 30);
			this.info("X = " + X);
			String C = "";
			C = toXX("", "　", 20);

			line = "";
			line += "01";
			line += FormatUtil.padX("", 10) + X + "    " + C + " " + FormatUtil.pad9(tmap.get("CustNo"), 7) + " "
					+ FormatUtil.padX(tmap.get("CustName"), 10) + "    " + FormatUtil.padX("", 65);
			// 加入明細
			result.add(line);

			// 02

			int effectDate = parse.stringToInteger(tmap.get("TxEffectDate"));
			if (effectDate != 0) {
				baTxCom.getDueAmt(effectDate, parse.stringToInteger(tmap.get("CustNo")),
						parse.stringToInteger(tmap.get("FacmNo")), 0, titaVo);
				headerDueAmt = "" + (baTxCom.getPrincipal().add(baTxCom.getInterest()));
				headerExcessive = "" + baTxCom.getExcessive().subtract(baTxCom.getShortfall());
			} else {
				headerDueAmt = tmap.get("DueAmt");
			}
			String loanBalX = "+";
			if (parse.stringToBigDecimal(tmap.get("LoanBal")).compareTo(BigDecimal.ZERO) < 0) {
				loanBalX = "-";
			}

			String headerExcessiveX = "+";
			if (parse.stringToBigDecimal(tmap.get("headerExcessive")).compareTo(BigDecimal.ZERO) < 0) {
				headerExcessiveX = "-";
			}

			// 02 陳＊耀 0001743 10 日 銀行扣款 0109091600003683931+00000000000+
			line = "";
			line += "02";
			line += " " + FormatUtil.padX(tmap.get("CustName"), 40) + " " + FormatUtil.pad9(tmap.get("CustNo"), 7)
					+ FormatUtil.rightPad9(tmap.get("SpecificDd"), 6) + " 日" + "          "
					+ FormatUtil.padX(tmap.get("RepayCodeX"), 8) + "   " + FormatUtil.pad9(titaVo.getCalDy(), 8)
					+ FormatUtil.pad9(tmap.get("LoanBal"), 11) + loanBalX + FormatUtil.pad9(headerExcessive, 11)
					+ headerExcessiveX;
			// 加入明細
			result.add(line);

		} // if
			// 03

		// 030109041301090310-01090410 銀行扣款
		// 0000037738+0000032175+00005563+00000000+00000000+
		line = "";
		line += "03";

		String dateRange = " ";
		String startDate = tmap.get("IntStartDate");
		String endDate = tmap.get("IntEndDate");
		String tstartDate = "00000000";
		String tendDate = "00000000";
		// 組成yyymmdd-yyymmdd
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {

//			if (!"".equals(showRocDate(startDate, 3))) {
//				tstartDate = showRocDate(startDate, 3);
//			}
//
//			if (!"".equals(showRocDate(endDate, 3))) {
//				tendDate = showRocDate(endDate, 3);
//			}

			if (!"".equals(showRocDate(startDate, 3))) {
				tstartDate = FormatUtil.pad9(startDate, 8);
			}

			if (!"".equals(showRocDate(endDate, 3))) {
				tendDate = FormatUtil.pad9(endDate, 8);
			}
			dateRange = tstartDate + "-" + tendDate;
		}

		String txAmtX = "+";
		if (parse.stringToBigDecimal(tmap.get("TxAmt")).compareTo(BigDecimal.ZERO) < 0) {
			txAmtX = "-";
		}

		String principalX = "+";
		if (parse.stringToBigDecimal(tmap.get("Principal")).compareTo(BigDecimal.ZERO) < 0) {
			principalX = "-";
		}

		String interestX = "+";
		if (parse.stringToBigDecimal(tmap.get("Interest")).compareTo(BigDecimal.ZERO) < 0) {
			interestX = "-";
		}

		String breachAmtX = "+";
		if (parse.stringToBigDecimal(tmap.get("BreachAmt")).compareTo(BigDecimal.ZERO) < 0) {
			breachAmtX = "-";
		}

		String OtherFeeX = "+";
		if (parse.stringToBigDecimal(tmap.get("OtherFee")).compareTo(BigDecimal.ZERO) < 0) {
			OtherFeeX = "-";
		}

//		line += showRocDate(tmap.get("EntryDate"), 3) + dateRange + " " + tmap.get("RepayCodeX") + "   "
		line += FormatUtil.pad9(tmap.get("EntryDate"), 8) + dateRange + " " + FormatUtil.padX(tmap.get("RepayCodeX"), 8)
				+ "   " + FormatUtil.pad9(tmap.get("TxAmt"), 10) + txAmtX + FormatUtil.pad9(tmap.get("Principal"), 10)
				+ principalX + FormatUtil.pad9(tmap.get("Interest"), 10) + interestX
				+ FormatUtil.pad9(tmap.get("BreachAmt"), 10) + breachAmtX + FormatUtil.pad9(tmap.get("OtherFee"), 10)
				+ OtherFeeX;
		// 加入明細
		result.add(line);

		return result;
	}

	// 改全型
	private String toX(String s, String x) {

		if ("0".equals(s)) {
			x += "０";
		} else if ("1".equals(s)) {
			x += "１";
		} else if ("2".equals(s)) {
			x += "２";
		} else if ("3".equals(s)) {
			x += "３";
		} else if ("4".equals(s)) {
			x += "４";
		} else if ("5".equals(s)) {
			x += "５";
		} else if ("6".equals(s)) {
			x += "６";
		} else if ("7".equals(s)) {
			x += "７";
		} else if ("8".equals(s)) {
			x += "８";
		} else if ("9".equals(s)) {
			x += "９";
		} else if (" ".equals(s)) {
			x += "　";
		} else if ("%".equals(s)) {
			x += "％";
		} else if (",".equals(s)) {
			x += "，";
		} else {
			x += s;
		}
		return x;
	}

	// 改補足出出切位置
	// s 傳入值
	// x 補什麼值
	// 長度
	private String toXX(String s, String x, int i) {
		String z = "";
		if (s.length() < i) {
			int il = i - s.length();
			for (int ii = 0; ii < il; ii++) {
				s += x;

				this.info("s =" + s);
			}
			z = s;
		}
		this.info("z =" + z);
		return z;
	}
}
