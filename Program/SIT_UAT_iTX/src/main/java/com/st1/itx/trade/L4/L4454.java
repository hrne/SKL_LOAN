package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.springjpa.cm.L4454ServiceImpl;
import com.st1.itx.trade.L9.L9705Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * CHKBXA=X,1<br>
 * CHKBXB=X,1<br>
 * CHKBXC=X,1<br>
 * CHKBXD=X,1<br>
 * CHKBXE=X,1<br>
 * CHKBXF=X,1<br>
 * CHKBXG=X,1<br>
 * END=X,1<br>
 */

@Service("L4454")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4454 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public BankDeductDtlService bankDeductDtlService;

	@Autowired
	public L4454ServiceImpl l4454ServiceImpl;

	@Autowired
	public L4454Report l4454Report;

	@Autowired
	public L9705Report l9705Report;

	@Autowired
	public L4454Report2 l4454Report2;

	@Autowired
	public L4454Report3 l4454Report3;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public WebClient webClient;

	private int reportACnt = 0;
	private HashMap<Integer, Integer> custLoanFlag = new HashMap<>();
	private HashMap<Integer, Integer> custFireFlag = new HashMap<>();
	private TempVo tTempVo = new TempVo();
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<Map<String, String>> l9705ListA = new ArrayList<>();
	private List<Map<String, String>> l9705ListB = new ArrayList<>();
	private List<Map<String, String>> l4454List = new ArrayList<>();
	private int entryDate = 0;
	private int custNo = 0;
	private int facmNo = 0;
	private int repayType = 0;
	private int prevIntDate = 0;
	private int cntEmail = 0;
	private int cntText = 0;
	private int cntUnsend = 0;
	private int cntL9705 = 0;
	private int cntL4454 = 0;
	private BigDecimal repayAmt = BigDecimal.ZERO;
	private int functionCode = 0;
	String iRepayBank = "";
	private List<TxToDoDetailReserve> lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4454 ");
		this.totaVo.init(titaVo);

		functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		txToDoCom.setTxBuffer(this.getTxBuffer());
		iRepayBank = titaVo.getParam("RepayBank");

		// 訂正交易處理 (刪除應處理明細)
		if (titaVo.isHcodeErase()) {
			String msg = "L4454產生銀扣失敗通知 訂正完畢。";
			cntText = txToDoCom.delDetailByTxNo("TEXT00", titaVo.getOrgEntdyI(), titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
			cntEmail = txToDoCom.delDetailByTxNo("MAIL00", titaVo.getOrgEntdyI(), titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
			cntL9705 = txToDoCom.delReserveByTxNo("NOTI01", titaVo.getOrgEntdyI(), titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
			cntL4454 = txToDoCom.delReserveByTxNo("NOTI02", titaVo.getOrgEntdyI(), titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
			if (functionCode == 1 || functionCode == 2) {
				msg += ", 刪除簡訊+eMail筆數：" + (cntText + cntEmail);
				if (cntL4454 > 0) {
					msg += ", 勿寄送明信片份數" + cntL4454;
				}
			}
			msg += ", 勿寄送繳息還本通知單份數：" + cntL9705;
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", msg, titaVo);
		}

		// 正常交易處理
		if (titaVo.isHcodeNormal()) {

			try {
				fnAllList = l4454ServiceImpl.findAll(functionCode, titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("l4454R3ServiceImpl.findAll error = " + errors.toString());
			}

			if (fnAllList == null || fnAllList.size() == 0) {
				throw new LogicException(titaVo, "E0001", ""); // 無資料
			}

			switch (functionCode) {

			case 1: // 個別列印
				for (Map<String, String> t : fnAllList) {
					exec("", t, titaVo);
				}
				if (titaVo.isHcodeNormal()) {
					if (reportACnt > 0) {
						reportB(titaVo); // 繳息還本通知單
					}
				}
				break;

			case 2: // 整批列印
				for (Map<String, String> t : fnAllList) {
					exec(iRepayBank, t, titaVo);
				}

				reportB(titaVo); // 繳息還本通知單

				reportC(titaVo); // 列印明信片

				excelA(titaVo); // 扣款失敗5萬元以上+地區別，統計到額度

				excelB(titaVo); // 新貸戶1年內扣款失敗，到額度(首撥日抓額度)
				txToDoCom.updDetailStatus(2, new TxToDoDetailId("L4454", 0, 0, 0, " "), titaVo);
				break;

			case 3: // 連續扣款失敗明細＆通知
				for (Map<String, String> t : fnAllList) {
					exec("", t, titaVo);
				}
				if (reportACnt > 0) {
					reportB(titaVo);// 二扣繳息還本通知單
				}
				break;
			}

			// 應處理明細留存檔(銀扣失敗書面通知單)
			if (titaVo.isHcodeNormal()) {
				txToDoCom.addReserve(lTxToDoDetailReserve, titaVo);
			}

			String msg = "L4454產生銀扣失敗通知 處理完畢。";
			if (functionCode == 1 || functionCode == 2) {
				msg += ", 簡訊+eMail筆數：" + (cntText + cntEmail);
				if (cntUnsend > 0) {
					msg += "(未送：" + cntUnsend + ")";
				}
				msg += ", 明信片份數：" + l4454List.size();
			}
			msg += ", 繳息還本通知單份數：" + l9705ListA.size() + l9705ListB.size();
			if (l4454List.size() > 0) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
						titaVo.getTlrNo(), msg, titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", msg, titaVo);
			}
		}
		this.totaVo.putParam("ReportACnt", reportACnt);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void exec(String repayBank, Map<String, String> t, TitaVo titaVo) throws LogicException {
		entryDate = parse.stringToInteger(t.get("EntryDate"));
		custNo = parse.stringToInteger(t.get("CustNo"));
		facmNo = parse.stringToInteger(t.get("FacmNo"));
		repayType = parse.stringToInteger(t.get("RepayType"));
		repayAmt = parse.stringToBigDecimal(t.get("RepayType"));
		prevIntDate = parse.stringToInteger(t.get("PrevIntDate"));
		tTempVo = tTempVo.getVo(t.get("JsonFields"));
		this.info(t.toString());
//		抓取火險到期年月 = 火險應繳日前5碼
		int insuMon = 0;
		if (repayType == 5) {
			insuMon = parse.stringToInteger(FormatUtil.pad9("" + tTempVo.get("InsuDate"), 7).substring(0, 5));
		}

//		1.不成功簡訊通知(ALL)	
//		2.火險成功期款失敗通知(一扣) = reportA(tBatxDetail);
//		3.列印明信片並寄發(二扣) = reportB(titaVo);

//		if   = 1扣   出火險成功期款失敗通知
//		else = 2扣   列印明信片
		if (repayType == 1) {
			switch (functionCode) {
			case 1: // 個別列印
			case 2: // 整批列印
				int fistDeduct = 0;
				unSuccText(iRepayBank, t, insuMon, titaVo); // 不成功簡訊通知
				fistDeduct = fistDeductCheck(titaVo);
				if (fistDeduct == 1) {
					if ("Y".equals(t.get("FireFeeSuccess")) && repayType == 1) {
						putTotaA(t, titaVo); // 列印清單
						if ("1".equals(t.get("RowNumber"))) {
							l9705ListB.add(t); // 繳息還本通知單(火險成功期款失敗通知) ，同戶號、額度只印一份
						}
						failNoticeDateUpdate("火險成功期款失敗通知", titaVo); // 失敗通知日期
					}
				}
				if (fistDeduct == 2) {
					if (titaVo.isHcodeNormal()) {
						if ("1".equals(t.get("RowNumber"))) {
							l4454List.add(t); // 列印明信片，同戶號、額度只印一份
						}
					}
					failNoticeDateUpdate("期款二扣失敗明信片", titaVo); // 失敗通知日期
				}
				break;
			case 3: // 連續扣款失敗明細＆通知
				putTotaA(t, titaVo); // 列印清單
				if ("1".equals(t.get("RowNumber"))) {
					l9705ListA.add(t); // 繳息還本通知單，同戶號、額度只印一份
				}
				failNoticeDateUpdate("連續扣款失敗通知", titaVo); // 失敗通知日期
				break;
			}
		}
	}

	// 應處理明細留存檔(銀扣失敗書面通知單)
	private void failNoticeDateUpdate(String note, TitaVo titaVo) throws LogicException {
		TxToDoDetailReserve tDetail = new TxToDoDetailReserve();
		tDetail.setItemCode("NOTI01"); // 銀扣失敗書面通知單
		tDetail.setCustNo(custNo);
		tDetail.setFacmNo(facmNo);
		tDetail.setDtlValue(note);
		lTxToDoDetailReserve.add(tDetail);
	}

	private void unSuccText(String repayBank, Map<String, String> t, int insuM, TitaVo titaVo) throws LogicException {
		boolean isSend = false;
		TempVo tempVo = new TempVo();
		tempVo = custNoticeCom.getCustNotice("L4454", custNo, facmNo, titaVo);

		String phoneNo = tempVo.getParam("MessagePhoneNo");
		String emailAd = tempVo.getParam("EmailAddress");

//		寄送簡訊/電郵，若第一順位為書信，則找第二順位
//		若為皆1則傳簡訊
//		若為皆0則傳簡訊

		if ("Y".equals(tempVo.getParam("isMessage"))) {
			sendText(repayBank, t, phoneNo, insuM, titaVo);
			isSend = true;
		}
		if ("Y".equals(tempVo.getParam("isEmail"))) {
			sendEmail(repayBank, t, emailAd, insuM, titaVo);
			isSend = true;
		}
		if (!isSend) {
			cntUnsend++;
		}
	}

	private void sendText(String repayBank, Map<String, String> t, String phoneNo, int insuM, TitaVo titaVo)
			throws LogicException {
		if (repayType == 1 || repayType == 3) {
			this.info("RepayType() == 1...");
			if (!custLoanFlag.containsKey(custNo)) {
				cntText++;
				String dataLines = "";
				dataLines = "\"H1\",\"" + t.get("CustId") + "\",\"" + phoneNo + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\""
						+ this.getTxBuffer().getTxCom().getTbsdy() + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(0);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);
				tTxToDoDetail.setTitaEntdy(titaVo.getEntDyI());
				tTxToDoDetail.setTitaKinbr(titaVo.getKinbr());
				tTxToDoDetail.setTitaTlrNo(titaVo.getTlrNo());
				tTxToDoDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		} else if (repayType == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(custNo)) {
				cntText++;
//				轉全形
				String sInsuAmt = toFullWidth("" + repayAmt);
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuM), 5).substring(3, 5);

				this.info("sInsuAmt ... " + sInsuAmt);
				this.info("sInsuMonth ... " + toFullWidth("" + insuM));

				String dataLines = "";
				dataLines = "\"H1\",\"" + t.get("CustId") + "\",\"" + phoneNo + "\",\"您好：提醒您" + sInsuMonth
						+ "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\""
						+ dateSlashFormat(this.getTxBuffer().getMgBizDate().getTbsDy()) + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(0);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險費扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);
				tTxToDoDetail.setTitaEntdy(titaVo.getEntDyI());
				tTxToDoDetail.setTitaKinbr(titaVo.getKinbr());
				tTxToDoDetail.setTitaTlrNo(titaVo.getTlrNo());
				tTxToDoDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		} else {
			this.info("RepayType() != 1 or 5...");
		}
	}

	private void sendEmail(String repayBank, Map<String, String> t, String emailAd, int insuM, TitaVo titaVo)
			throws LogicException {
//		RepayType = 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
		this.info("setMail...");
		if (repayType == 1 || repayType == 3) {
			this.info("RepayType() == 1...");
			if (!custLoanFlag.containsKey(custNo)) {
				cntEmail++;
				String dataLines = "";
				dataLines = "親愛的客戶，繳款通知；新光人壽關心您。";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(0);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);
				tTxToDoDetail.setTitaEntdy(titaVo.getEntDyI());
				tTxToDoDetail.setTitaKinbr(titaVo.getKinbr());
				tTxToDoDetail.setTitaTlrNo(titaVo.getTlrNo());
				tTxToDoDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));

				txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		} else if (repayType == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(custNo)) {
				cntEmail++;
//				轉全形
//				轉全形
				String sInsuAmt = toFullWidth("" + repayAmt);
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuM), 5).substring(3, 5);

				this.info("sInsuAmt ... " + sInsuAmt);
				this.info("sInsuMonth ... " + toFullWidth("" + insuM));

				String dataLines = "";
				dataLines = "您好：提醒您" + sInsuMonth + "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(0);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);
				tTxToDoDetail.setTitaEntdy(titaVo.getEntDyI());
				tTxToDoDetail.setTitaKinbr(titaVo.getKinbr());
				tTxToDoDetail.setTitaTlrNo(titaVo.getTlrNo());
				tTxToDoDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));

				txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		}
	}

	private int fistDeductCheck(TitaVo titaVo) throws LogicException {
		int result = 1;
		// 銀扣檔有相同繳息迄日的期款扣款失敗資料
		BankDeductDtl tBankDeductDtl = bankDeductDtlService.findL4450PrevIntDateFirst(custNo, facmNo, 
				prevIntDate + 19110000, titaVo);
		if (tBankDeductDtl != null) {
			if ((tBankDeductDtl.getEntryDate() < entryDate && tBankDeductDtl.getRepayType() == 1)) {
				if ("Y".equals(tBankDeductDtl.getMediaCode().trim())
						|| !("00".equals(tBankDeductDtl.getReturnCode().trim())
								&& "".equals(tBankDeductDtl.getReturnCode().trim()))) {
					result = 2;
				}
			}
		}
		this.info("fistDeductCheck="+ result );
		return result;
	}

	private void putTotaA(Map<String, String> t, TitaVo titaVo) {
		this.info("ReportA Start...");
//		戶號 額度 戶名 計息起日 計息迄日 期款金額 扣款銀行 帳號
//		總計 筆數
		reportACnt = reportACnt + 1;

		OccursList occursList = new OccursList();
		occursList.putParam("ReportACustNo", custNo);
		occursList.putParam("ReportAFacmNo", facmNo);
		occursList.putParam("ReportACustName", t.get("CustName"));
		occursList.putParam("ReportAIntStartDate", t.get("IntStartDate"));
		occursList.putParam("ReportAIntEndDate", t.get("IntEndDate"));
		occursList.putParam("ReportARepayAmt", repayAmt);
		occursList.putParam("ReportARepayBank", t.get("RepayBank"));
		occursList.putParam("ReportARepayAcctNo", t.get("RepayAcctNo"));

		this.totaVo.addOccursList(occursList);
	}

//	還本繳息通知單(出火險成功期款失敗通知)
	private void reportB(TitaVo titaVo) throws LogicException {
		this.info("ReportB Start...");
		titaVo.putParam("CONDITION1", "A");	
		l9705Report.exec(l9705ListA, titaVo, this.getTxBuffer());
		
		titaVo.putParam("CONDITION1", "B");
		l9705Report.exec(l9705ListB, titaVo, this.getTxBuffer());
	}

//	二扣未成功之明信片
	private void reportC(TitaVo titaVo) throws LogicException {
		this.info("ReportC Start...");
		l4454Report.exec(titaVo, this.getTxBuffer(), l4454List);
	}

	private class tmpFacm {

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType + "]";
		}

		private int custNo = 0;
		private int facmNo = 0;
		private int repayType = 0;

		public tmpFacm(int custNo, int facmNo, int repayType) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setRepayType(repayType);
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private int getRepayType() {
			return repayType;
		}

		private void setRepayType(int repayType) {
			this.repayType = repayType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
			result = prime * result + repayType;
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
			if (repayType != other.repayType)
				return false;
			return true;
		}

		private L4454 getEnclosingInstance() {
			return L4454.this;
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private String dateSlashFormat(int today) {
		String slashedDate = "";
		String acToday = "";
		if (today >= 1 && today < 19110000) {
			acToday = FormatUtil.pad9("" + (today + 19110000), 8);
		} else if (today >= 19110000) {
			acToday = FormatUtil.pad9("" + today, 8);
		}
		slashedDate = acToday.substring(0, 4) + "/" + acToday.substring(4, 6) + "/" + acToday.substring(6, 8);

		return slashedDate;
	}

	private String toFullWidth(String Pwd) {
		String outStr = "";
		char[] chars = Pwd.toCharArray();
		int tranTemp = 0;

		for (int i = 0; i < chars.length; i++) {
			tranTemp = (int) chars[i];
			if (tranTemp != 45) // ASCII碼:45 是減號 -
				tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差
			outStr += (char) tranTemp;
		}
		return outStr;
	}

//	扣款失敗5萬元以上+地區別，統計到額度
	private void excelA(TitaVo titaVo) throws LogicException {
		l4454Report2.exec(titaVo);
	}

//	新貸戶1年內扣款失敗，到額度(首撥日抓額度)
	private void excelB(TitaVo titaVo) throws LogicException {
		l4454Report3.exec(titaVo);
	}
}