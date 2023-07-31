package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
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
public class L4721Report2 extends TradeBuffer {

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	DateUtil dateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	MakeFile makeFile;

	@Autowired
	MakeReport makeReport;

	@Autowired
	Parse parse;

	@Autowired
	public L4721ServiceImpl l4721ServiceImpl;

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
	int cntAll = 0;

	/**
	 * 資料明細
	 * 
	 * @param titaVo
	 * @param txbuffer
	 * @param data       主要資料來源
	 * @param txKind     利率種類代碼
	 * @param kindItem   利率代碼中文名稱
	 * @param sAdjDate   利率調整起日
	 * @param eAdjDate   利率調整止日
	 * @param sEntryDate 入帳起日(六個月前月初)
	 * @param eEntryDate 入帳止日
	 * @return
	 * @throws LogicException
	 */
	public Integer exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> data, String kindItem, int sAdjDate,
			int eAdjDate, int sEntryDate, int eEntryDate) throws LogicException {
		this.info("L4721Report2 exec start");

		this.setTxBuffer(txbuffer);
		baTxCom.setTxBuffer(txbuffer);

		this.info("data.list = " + data.toString());
		this.info("data.size = " + data.size());

		List<String> file = getData(sAdjDate, eAdjDate, sEntryDate, eEntryDate, data, titaVo);

		String fileName = "L4721-" + kindItem + "(總筆數：" + cntAll + ")";

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno())
				.setRptCode(titaVo.getTxCode()).setRptItem(fileName).build();

		makeFile.open(titaVo, reportVo, fileName + ".txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4721", titaVo.getTxCode() + " 已產生L4721.txt", titaVo);

		return cntAll;
	}

	/**
	 * 資料明細
	 *
	 * @param sAdjDate   利率調整起日
	 * @param eAdjDate   利率調整止日
	 * @param sEntryDate 入帳起日(六個月前月初)
	 * @param eEntryDate 入帳止日
	 * @param data       利率變動檔資料源
	 * @param titaVo
	 * 
	 */
	private List<String> getData(int sAdjDate, int eAdjDate, int sEntryDate, int eEntryDate,
			List<Map<String, String>> data, TitaVo titaVo) throws LogicException {

		// data 來源 findAll
		// CustNo
		// MaxSpecificDd
		// MinSpecificDd

		List<String> result = new ArrayList<>();

		int custNo = 0;

		int cntTrans = 0;

//		 * @param isDoDetail 是否為明細(否則為額度變動日)
//		 * @param isSameSpecificDd 應繳日額度是否一樣

		boolean isSameSpecificDd = false;

		boolean isByCustNo = false;

		for (Map<String, String> r : data) {

			cntTrans++;
			custNo = parse.stringToInteger(r.get("CustNo"));
			int MinSpecificDd = parse.stringToInteger(r.get("MinSpecificDd"));
			int MaxSpecificDd = parse.stringToInteger(r.get("MaxSpecificDd"));
			int tempFacmNo = 999;
			int tempNextFacmNo = 999;
			int tempCustNo = 0;
			int tempNextCustNo = 0;
			// 應繳日是否一樣
			isSameSpecificDd = MinSpecificDd == MaxSpecificDd;

			isByCustNo = isSameSpecificDd;

			cntAll = cntAll + 1;

			List<Map<String, String>> rDetail = new ArrayList<Map<String, String>>();
			List<Map<String, String>> rTxffectDetail = new ArrayList<Map<String, String>>();

			// 明細
			try {
				rDetail = l4721ServiceImpl.doDetailTxffect(custNo, sAdjDate, eAdjDate, sEntryDate, eEntryDate, true,
						isSameSpecificDd, titaVo);
			} catch (Exception e) {
				this.error("l4721ServiceImpl doDetailTxffect = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			// 利率變動明細
			try {
				rTxffectDetail = l4721ServiceImpl.doDetailTxffect(custNo, sAdjDate, eAdjDate, sEntryDate, eEntryDate,
						false, isSameSpecificDd, titaVo);

			} catch (Exception e) {
				this.error("l4721ServiceImpl doDetailTxffect = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			if (rDetail.size() > 0 && rTxffectDetail.size() > 0) {

				String line = "";

				int times = 0;
				int cntNext = 1;
				tempFacmNo = parse.stringToInteger(rDetail.get(0).get("FacmNo"));
				tempCustNo = parse.stringToInteger(rDetail.get(0).get("CustNo"));
				for (Map<String, String> r1 : rDetail) {
					this.info("size =" + rDetail.size());
					this.info("cnt  =" + cntNext);
					if (cntNext < rDetail.size()) {
						tempNextCustNo = parse.stringToInteger(rDetail.get(cntNext).get("CustNo"));
						tempNextFacmNo = parse.stringToInteger(rDetail.get(cntNext).get("FacmNo"));
//						this.info("tempNextfacmno = " + tempNextfacmno);
					}

					// 相同戶號不同額度的輸出

					// 新的一筆戶號的第一次 或 相同戶號時
					if (times == 0 || (tempCustNo == parse.stringToInteger(r1.get("CustNo"))
							&& tempFacmNo == parse.stringToInteger(r1.get("FacmNo")))) { // 相同額度

						// 第一次
						if (times == 0) {

							result = sameFacmno(r1, rTxffectDetail, result, true, isByCustNo, titaVo);

						} else {

							result = sameFacmno(r1, rTxffectDetail, result, false, isByCustNo, titaVo);

						}

//						result.add(line);

					}
					tempCustNo = parse.stringToInteger(r1.get("CustNo"));
					tempFacmNo = parse.stringToInteger(r1.get("FacmNo"));
					cntNext++;
					times++;

					this.info("tempCustNo =" + tempCustNo);
					this.info("tempFacmno  =" + tempFacmNo);
					this.info("tempNextCustNo =" + tempNextCustNo);
					this.info("tempNextFacmNo  =" + tempNextFacmNo);
					// 1.by 戶號 額度一定是0，只會六筆
					// 2.by 額度 不同額度 會各有六筆
					// 當前額度和下一筆額度不同才進入
					if (tempFacmNo != tempNextFacmNo || tempCustNo != tempNextCustNo
							|| (cntNext - 1) == rDetail.size()) { // 只要不同戶號或不同額度
						// 印04並且切到下一個額度循環
						// 04
						cntNext = 1;
						line = "";
						line += "04";
						result.add(line);

						for (Map<String, String> r2 : rTxffectDetail) {
							// 45
							// 45 額度 003 利率自 109 年 09 月 01 日起， 由 1.68% 調整為 1.41% 。

							// 明細的額度是0的話表示 輸出再同一份 或是依據額度印
							this.info("r1 tempFacmNo =" + tempFacmNo);
							this.info("r2 tempFacmNo =" + parse.stringToInteger(r2.get("FacmNo")));
							if (tempFacmNo == parse.stringToInteger(r2.get("FacmNo")) || tempFacmNo == 0) {

								BigDecimal presentRate = new BigDecimal(r2.get("PresentRate"));
								BigDecimal adjustedRate = new BigDecimal(r2.get("AdjustedRate"));

								this.info("presentRate =" + presentRate.toString());
								this.info("adjustedRate =" + adjustedRate.toString());

							

								if (presentRate.compareTo(adjustedRate) != 0) {
									
									// 20220101 => 1110101
									int intTxeffect = parse.stringToInteger(r2.get("TxEffectDate")) - 19110000;
									// 1110101 / 10000 = 111
									String year = (intTxeffect / 10000) + " 年 ";
									// 1110101 /100 % 100 = 01
									String month = FormatUtil.pad9((intTxeffect / 100 % 100) + "", 2) + " 月 ";
									// 1110101 % 100 = 01
									String day = FormatUtil.pad9((intTxeffect % 100) + "", 2) + " 日 ";
									String txeffect = year + month + day;
									
									
									line = "";
									line += "45";
									line += " 額度 " + FormatUtil.pad9(r2.get("FacmNo"), 3) + "     " + "利率自 " + txeffect
											+ "起，　由 " + makeReport.formatAmt(r2.get("PresentRate"), 2) + "% " + "調整為 "
											+ makeReport.formatAmt(r2.get("AdjustedRate"), 2) + "% 。";
									result.add(line);
								}

							}

						}
						if (rTxffectDetail.size() > 0) {
							line = "";
							line += "45";
							line += " ※其他額度利率，若有調整另行通知。";
							result.add(line);
						}
						// 05

						// 0500036341+9510200000174395103000001743
						line = "";
						line += "05";
						line += FormatUtil.pad9(headerDueAmt, 8) + "+" + "9510200"
								+ FormatUtil.pad9(r1.get("CustNo"), 7) + "9510300"
								+ FormatUtil.pad9(r1.get("CustNo"), 7);
						result.add(line);
						// 換額度要重新算次數
						times = 0;
					}

				} // for
			} // for

			if (cntTrans > 200) {
				cntTrans = 0;
				this.batchTransaction.commit();
			}

		} // for
		return result;
	}

	private List<String> sameFacmno(Map<String, String> r, List<Map<String, String>> txEffectData, List<String> result,
			Boolean same, Boolean isByCustNo, TitaVo titaVo) throws LogicException {

		String startDate = r.get("IntStartDate");
		String endDate = r.get("IntEndDate");
		String custName = r.get("CustName");
		String RepayItem = "99991231".equals(startDate) || "99991231".equals(endDate) ? " " : r.get("RepayCodeX");

		String line = "";

		if (same) {
			// 01
			// 011 1 0 0 0 台北市信義區永吉路１２０巷５０弄１號３樓 0001743 陳清耀
			String locationX = txEffectData.get(0).get("Location").length() > 0
					? txEffectData.get(0).get("Location").toString()
					: " ";
			String locationXX = "";

			for (int i = 0; i < locationX.length(); i++) {
				locationXX = toX(locationX.substring(i, i + 1), locationXX);
			}
			// 中文地址
			String X = makeReport.fillUpWord(locationXX, 60, " ", "R");

			// 空白
			String C = makeReport.fillUpWord(" ", 45, " ", "R");

			// 011 1 0 0 0 台北市信義區永吉路１２０巷５０弄１號３樓 0001743 陳清耀
			line = "";
			line = "01" + FormatUtil.padX("", 10) + X + C + FormatUtil.pad9(r.get("CustNo"), 7) + " "
					+ makeReport.fillUpWord(custName, 10, " ", "R") + "    " + makeReport.fillUpWord(" ", 65, " ", "R");
			// 加入明細
			result.add(line);

			// 02
			int effectDate = parse.stringToInteger(txEffectData.get(0).get("TxEffectDate"));
			if (effectDate != 0) {
				baTxCom.getDueAmt(titaVo.getEntDyI(), parse.stringToInteger(r.get("CustNo")),
						parse.stringToInteger(r.get("FacmNo")), 0, titaVo);
				headerDueAmt = "" + (baTxCom.getPrincipal().add(baTxCom.getInterest()));
				headerExcessive = "" + baTxCom.getExcessive().subtract(baTxCom.getShortfall());
			} else {
				headerDueAmt = r.get("DueAmt");
			}
			String loanBalX = "+";
			if (parse.stringToBigDecimal(r.get("LoanBal")).compareTo(BigDecimal.ZERO) < 0) {
				loanBalX = "-";
			}

			String headerExcessiveX = "+";
			if (parse.stringToBigDecimal(headerExcessive).compareTo(BigDecimal.ZERO) < 0) {
				headerExcessiveX = "-";
			}

			// 02 陳＊耀 0001743 10 日 銀行扣款 0109091600003683931+00000000000+
			int specificDd = 0;
			if (r.get("SpecificDd") != null || !r.get("SpecificDd").isEmpty()) {
				specificDd = parse.stringToInteger(r.get("SpecificDd"));
			}

			String facmNo = isByCustNo ? "    " : "-" + FormatUtil.pad9(r.get("FacmNo"), 3);

			line = "";
			line += "02";
			line += " " + FormatUtil.padX(r.get("CustName"), 40) + " " + FormatUtil.pad9(r.get("CustNo"), 7) + facmNo
					+ leftPadding(parse.IntegerToString(specificDd, 2), 2, ' ') + " 日" + "          "
					+ FormatUtil.padX(RepayItem, 8) + "   " + FormatUtil.pad9(titaVo.getEntDy(), 8)
					+ FormatUtil.pad9(r.get("LoanBal"), 11) + loanBalX + FormatUtil.pad9(headerExcessive, 11)
					+ headerExcessiveX;
			// 加入明細
			result.add(line);

		} // if
			// 03

		// 030109041301090310-01090410 銀行扣款
		// 0000037738+0000032175+00005563+00000000+00000000+
		line = "";
		line += "03";

		// 組成yyymmdd-yyymmdd

		// 西元年轉民國年
		if ("99991231".equals(startDate)) {

			startDate = FormatUtil.pad9("0", 8);
		} else {
			startDate = FormatUtil.pad9(makeReport.showRocDate(startDate, 3), 8);
		}

		if ("99991231".equals(endDate)) {

			endDate = FormatUtil.pad9("0", 8);
		} else {
			endDate = FormatUtil.pad9(makeReport.showRocDate(endDate, 3), 8);
		}

		String dateRange = startDate + "-" + endDate;

		String txAmtX = "+";
		if (parse.stringToBigDecimal(r.get("TxAmt")).compareTo(BigDecimal.ZERO) < 0) {
			txAmtX = "-";
		}

		String principalX = "+";
		if (parse.stringToBigDecimal(r.get("Principal")).compareTo(BigDecimal.ZERO) < 0) {
			principalX = "-";
		}

		String interestX = "+";
		if (parse.stringToBigDecimal(r.get("Interest")).compareTo(BigDecimal.ZERO) < 0) {
			interestX = "-";
		}

		String breachAmtX = "+";
		if (parse.stringToBigDecimal(r.get("BreachAmt")).compareTo(BigDecimal.ZERO) < 0) {
			breachAmtX = "-";
		}

		String OtherFeeX = "+";
		if (parse.stringToBigDecimal(r.get("OtherFee")).compareTo(BigDecimal.ZERO) < 0) {
			OtherFeeX = "-";
		}

		line += FormatUtil.pad9(r.get("EntryDate"), 8) + dateRange + " " + FormatUtil.padX(RepayItem, 8) + "   "
				+ FormatUtil.pad9(r.get("TxAmt"), 10) + txAmtX + FormatUtil.pad9(r.get("Principal"), 10) + principalX
				+ FormatUtil.pad9(r.get("Interest"), 8) + interestX + FormatUtil.pad9(r.get("BreachAmt"), 8)
				+ breachAmtX + FormatUtil.pad9(r.get("OtherFee"), 8) + OtherFeeX;
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

//	向左補特定字元
//	str 原本的字串
//	length 填補後的總長度
//	padChar 要填補的字元
	private static String leftPadding(String str, int length, char padChar) {
		if (str == null) {
			str = "";
		}
		if (str.length() > length) {
			return str;
		}
		String pattern = "%" + length + "s";
		return String.format(pattern, str).replace(' ', padChar);
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
}
