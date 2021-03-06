package com.st1.itx.trade.L4;

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
				titaVo.getTlrNo() + "L4721", titaVo.getTxCode() + " ?????????L4721.txt", titaVo);
	}

	private List<String> getData(TitaVo titaVo) throws LogicException {

		this.info("L4721Report2 getData start");

		List<String> result = new ArrayList<>();

		int adjDate = Integer.parseInt(titaVo.getParam("AdjDate")) + 19110000;
		int custType1 = 0;
		int custType2 = 0;
		int txKind = Integer.parseInt(titaVo.getParam("TxKind"));

//		???????????? ?????? CustType 1:??????;2:??????????????????????????????
//		????????? 0:??????1:??????2:???????????????
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

//			// ??????????????????????????????????????????????????????
			if (tBatxRateChange.getTxEffectDate() == 0) {
				continue;
			}
			// ????????????
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
				throw new LogicException("E9003", "???????????????????????????????????????????????????");
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

					// ?????????????????????????????????

					if (tempfacmno == parse.stringToInteger(mapL4721Detail.get("FacmNo"))) { // ????????????
						// ???????????????????????????0102
						if (times == 0) {
							result = sameFacmno(mapL4721Detail, result, false);
						} else {
							result = sameFacmno(mapL4721Detail, result, true);
						}
						times++;
					} else { // ???????????? ???04?????????????????????????????????
						// 04

						line = "";
						line += "04";
						result.add(line);
						line = "";
						result.add(line);

						// 45

						// 45 ?????? 003 ????????? 109 ??? 09 ??? 01 ????????? ??? 1.68% ????????? 1.41% ???
						line = "";
						line += "45";
						line += " ?????? " + FormatUtil.pad9(mapL4721Detail.get("FacmNo"), 3) + "     " + "?????????"
								+ showRocDate(mapL4721Detail.get("TxEffectDate"), 0) + "????????????"
								+ formatAmt(mapL4721Detail.get("PresentRate"), 2) + "%" + "?????????"
								+ formatAmt(mapL4721Detail.get("AdjustedRate"), 2) + "???";
						result.add(line);
						line = "";
						// ????????????
						result.add(line);

						// 05

						// 0500036341+9510200000174395103000001743
						line = "";
						line += "05";
						line += FormatUtil.pad9(headerDueAmt, 8) + "+" + "9510200"
								+ FormatUtil.pad9(mapL4721Detail.get("CustNo"), 7) + "9510300"
								+ FormatUtil.pad9(mapL4721Detail.get("CustNo"), 7);
						result.add(line);
						line = "";
						// ????????????
						result.add(line);
						result = sameFacmno(mapL4721Detail, result, false);
						// ???????????????????????????
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

			// 011 1 0 0 0 ???????????????????????????????????????????????????????????? 0001743 ?????????
			line = "";
			line += "01";
			line += "??????" + tmap.get("Location") + " " + FormatUtil.pad9(tmap.get("CustNo"), 7) + " "
					+ tmap.get("CustName");
			// ????????????
			result.add(line);
			line = "";
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

			// 02 ????????? 0001743 10 ??? ???????????? 0109091600003683931+00000000000+
			line = "";
			line += "02";
			line += " " + tmap.get("CustName") + " " + FormatUtil.pad9(tmap.get("CustNo"), 7) + tmap.get("SpecificDd")
					+ " ???" + "          " + tmap.get("RepayCodeX") + "   " + FormatUtil.pad9(tmap.get("LoanBal"), 11)
					+ headerExcessive;

			// ????????????
			result.add(line);
			line = "";
			result.add(line);

		} // if
			// 03

		// 030109041301090310-01090410 ????????????
		// 0000037738+0000032175+00005563+00000000+00000000+
		line = "";
		line += "03";

		String dateRange = " ";
		String startDate = tmap.get("IntStartDate");
		String endDate = tmap.get("IntEndDate");
		String tstartDate = "0000000";
		String tendDate = "0000000";
		// ??????yyymmdd-yyymmdd
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {

			if (!"".equals(showRocDate(startDate, 3))) {
				tstartDate = showRocDate(startDate, 3);
			}

			if (!"".equals(showRocDate(endDate, 3))) {
				tendDate = showRocDate(endDate, 3);
			}

			dateRange = tstartDate + "-" + tendDate;
		}

		line += showRocDate(tmap.get("EntryDate"), 3) + dateRange + " " + tmap.get("RepayCodeX") + "   "
				+ FormatUtil.pad9(formatAmt(tmap.get("TxAmt"), 0), 10) + "+"
				+ FormatUtil.pad9(formatAmt(tmap.get("Principal"), 0), 10) + "+"
				+ FormatUtil.pad9(formatAmt(tmap.get("Interest"), 0), 10) + "+"
				+ FormatUtil.pad9(formatAmt(tmap.get("BreachAmt"), 0), 10) + "+"
				+ FormatUtil.pad9(formatAmt(tmap.get("OtherFee"), 0), 10) + "+";
		// ????????????
		result.add(line);
		line = "";
		result.add(line);

		return result;
	}
}
