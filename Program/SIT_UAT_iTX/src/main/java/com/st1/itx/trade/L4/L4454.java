package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.service.SystemParasService;
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
	public CustMainService custMainService;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public SystemParasService systemParasService;

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
	public AchDeductMediaService achDeductMediaService;
	@Autowired
	public PostDeductMediaService postDeductMediaService;
	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public WebClient webClient;

//	火險成功期款失敗記號
//	private HashMap<tmpFacm, Integer> shortFlag = new HashMap<>();
	private int reportACnt = 0;
	private HashMap<Integer, Integer> custLoanFlag = new HashMap<>();
	private HashMap<Integer, Integer> custFireFlag = new HashMap<>();
	private int payDayA2 = 0;
	private int payDayA1 = 0;
	private int payDayB2 = 0;
	private int payDayB1 = 0;
	private TempVo tTempVo = new TempVo();
	private List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();
	private List<BatxDetail> tempBatxDetail = new ArrayList<BatxDetail>();
	private int iCustNo = 0;
	private int iFacmNo = 0;
	private int iAcDate = 0;
	String iRepayBank = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4454 ");
		this.totaVo.init(titaVo);

		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		txToDoCom.setTxBuffer(this.getTxBuffer());
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iRepayBank = titaVo.getParam("RepayBank");

		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4454AEq(iAcDate, iAcDate, 2, this.index, this.limit,
				titaVo);
		if (slBatxDetail == null) {
			throw new LogicException("E0001", ""); // 查詢資料不存在
		}

//      0.未檢核                    05       01
//		1.不處理     V   扣款失敗             V
//		2.人工處理 X
//		3.檢核錯誤 X
//		4.檢核正常 X
//		5.單筆入帳   V   扣款成功   V
//		6.批次入帳   V   扣款成功   V
//		7.待轉暫收 X
		
		Boolean deduct = false;
// 單筆作業		
		if (functionCode == 1) {
			lBatxDetail = slBatxDetail.getContent();
			
//			檢查期款失敗
			deduct = deductCheck(lBatxDetail);
			
			if(deduct) {
			// 進一扣二扣判斷
			this.info("期款失敗存在進入檢核一扣二扣");
			// 存火險成功期款失敗
			  tempsave(lBatxDetail);
			  this.info("存火險成功期款失敗");
			  for (BatxDetail tBatxDetail : tempBatxDetail) {
				if (iCustNo == tBatxDetail.getCustNo() && iFacmNo == tBatxDetail.getFacmNo()) {
					exec("",tBatxDetail, titaVo);
				}
			  }
		    } // if
//			二扣繳息還本通知單
			if (reportACnt > 0) {
				reportB(titaVo);
			}
//	整批處理
		} else {
//			刪除TxToDoDetail
			deleteTxtoDo("TEXT00", titaVo);
			deleteTxtoDo("MAIL00", titaVo);

			// 挑銀行別
			for (BatxDetail tBatxDetail : slBatxDetail.getContent()) {
				tTempVo = this.tTempVo.getVo(tBatxDetail.getProcNote());
				switch (iRepayBank) {
				case "999": // all
					lBatxDetail.add(tBatxDetail);
					break;
				case "998": // ach
					if (!tTempVo.get("RepayBank").equals("700")) {
						lBatxDetail.add(tBatxDetail);
					}
					break;
				default: // bank
					if (tTempVo.get("RepayBank").equals(iRepayBank)) {
						lBatxDetail.add(tBatxDetail);
					}
					break;
				}
			}

//			檢查期款失敗
			deduct = deductCheck(lBatxDetail);
			
			if(deduct) {
			this.info("期款失敗存在進入檢核一扣二扣");
			// 存火險成功期款失敗
			  tempsave(lBatxDetail);
			  this.info("存火險成功期款失敗");
			  for (BatxDetail tBatxDetail : lBatxDetail) {
				tTempVo = this.tTempVo.getVo(tBatxDetail.getProcNote());
				exec(tTempVo.get("RepayBank"),tBatxDetail, titaVo);
			  }
			} // if
			
//			二扣繳息還本通知單
			if (reportACnt > 0) {
				reportB(titaVo);
			}

			excelA(titaVo);

			excelB(titaVo);
		}
		this.totaVo.putParam("ReportACnt", reportACnt);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				"L4454產生銀扣失敗通知 處理完畢，簡訊筆數：" + (custLoanFlag.size() + custFireFlag.size()), titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void exec(String repayBank, BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {

//		火險成功期款失敗報表-入帳日需放一個月後
		titaVo.putParam("EntryDate", caculateDate(tBatxDetail.getEntryDate(), 0, 1, 0));

		titaVo.putParam("ACCTDATE_ST", this.getTxBuffer().getTxCom().getTbsdyf());
		titaVo.putParam("ACCTDATE_ED", this.getTxBuffer().getTxCom().getTbsdyf());
		titaVo.putParam("CUSTNO", titaVo.getParam("CustNo"));
//		A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行。
//		B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)
		titaVo.putParam("CONDITION1", "B");
		titaVo.putParam("CONDITION2", 0); // 全部
		titaVo.putParam("ID_TYPE", 0); // 全部
		titaVo.putParam("CORP_IND", 0); // 全部
		titaVo.putParam("APNO", 0); // 全部

		this.info("ACCTDATE_ST ..." + titaVo.getParam("ACCTDATE_ST"));
		this.info("ACCTDATE_ED ..." + titaVo.getParam("ACCTDATE_ED"));
		this.info("CUSTNO ..." + titaVo.getParam("CUSTNO"));
		this.info("CONDITION1 ..." + titaVo.getParam("CONDITION1"));
		this.info("CONDITION2 ..." + titaVo.getParam("CONDITION2"));
		this.info("ID_TYPE ..." + titaVo.getParam("ID_TYPE"));
		this.info("CORP_IND ..." + titaVo.getParam("CORP_IND"));
		this.info("APNO ..." + titaVo.getParam("APNO"));
		this.info("FacmNo ..." + titaVo.getParam("FacmNo"));
		this.info("EntryDate ..." + titaVo.getParam("EntryDate"));

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tBatxDetail.getCustNo(), tBatxDetail.getCustNo(), titaVo);

		String custId = "";

		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}

//		抓取火險到期年月 = 火險應繳日前5碼
		int insuMon = 0;
		if (tBatxDetail.getRepayType() == 5) {
			Slice<BankDeductDtl>slBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
					tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, this.limit, titaVo);
//			抓取火險應繳日，相同者會合為一筆，故抓取第0筆即可
			if (slBankDeductDtl != null) {
				BankDeductDtl tBankDeductDtl = slBankDeductDtl.getContent().get(0);
					insuMon = parse
							.stringToInteger(FormatUtil.pad9("" + tBankDeductDtl.getPayIntDate(), 7).substring(0, 5));
			}
		}

//		1.不成功簡訊通知(ALL)	
		unSuccText(repayBank,tBatxDetail.getCustNo(), tBatxDetail.getFacmNo(), tBatxDetail.getRepayType(),
				tBatxDetail.getRepayAmt(), custId, insuMon, titaVo);

		tmpFacm tmp = new tmpFacm(tBatxDetail.getCustNo(), tBatxDetail.getFacmNo(), 0);

//		2.火險成功期款失敗通知(一扣) = reportA(tBatxDetail);
//		3.列印明信片並寄發(二扣) = reportB(titaVo);

//		抓取該媒體種類1:ACH新光2:ACH他行3:郵局 確定該項目為特定扣款or每日扣款
//		if   = 1扣  出火險成功期款失敗通知
//		else = 2扣   列印明信片
		SystemParas tSystemParas = systemParasService.findById("LN", titaVo);

//		tBatxDetail.getMediaKind() = 1銀行-新光 2銀行-他行 3郵局
//		tSystemParas.getAchDeductFlag() = 1特定日 2連續日
//		iType = 1一扣 2二扣
//		int iType = 2;
		int iType = check(tBatxDetail, titaVo);

		this.info("CustNo... " + tBatxDetail.getCustNo());
		this.info("FacmNo... " + tBatxDetail.getFacmNo());
		this.info("MediaKind... " + tBatxDetail.getMediaKind());
		this.info("AchDeductFlag... " + tSystemParas.getAchDeductFlag());
		this.info("iType... " + iType);
		this.info("PostDeductFlag... " + tSystemParas.getPostDeductFlag());
//		this.info("shortFlag ... " + shortFlag.get(tmp));
		this.info("RepayType ... " + tBatxDetail.getRepayType());

		if (iType == 1) {
//			if (shortFlag.get(tmp) == 9 || shortFlag.get(tmp) == 1 ) {
				reportA(tBatxDetail, titaVo);
//			}
		} else if (iType == 2) {
			reportC(titaVo);
		}
	}

	private void unSuccText(String repayBank, int custNo, int facmNo, int repayType, BigDecimal repayAmt, String custId, int insuM,
			TitaVo titaVo) throws LogicException {

		TempVo tempVo = new TempVo();
		tempVo = custNoticeCom.getCustNotice("L4454", custNo, facmNo, titaVo);

		String phoneNo = tempVo.getParam("MessagePhoneNo");
		String emailAd = tempVo.getParam("EmailAddress");

//		寄送簡訊/電郵，若第一順位為書信，則找第二順位
//		若為皆1則傳簡訊
//		若為皆0則傳簡訊

		if (!"".equals(phoneNo)) {
			sendText(repayBank, custNo, facmNo, repayType, repayAmt, phoneNo, custId, insuM, titaVo);
		} else if (!"".equals(emailAd)) {
			sendEmail(repayBank, custNo, facmNo, repayType, repayAmt, emailAd, custId, insuM, titaVo);
		}
	}

	private void sendText(String repayBank, int custNo, int facmNo, int repayType, BigDecimal repayAmt, String phoneNo, String custId,
			int insuM, TitaVo titaVo) throws LogicException {
		if (repayType == 1 || repayType == 3) {
			this.info("RepayType() == 1...");
			if (!custLoanFlag.containsKey(custNo)) {
				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);

				String dataLines = "";
				dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + phoneNo + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\""
						+ this.getTxBuffer().getTxCom().getTbsdy() + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(facmNo);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		} else if (repayType == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(custNo)) {
//				轉全形
				String sInsuAmt = toFullWidth("" + repayAmt);
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuM), 5).substring(3, 5);

				this.info("sInsuAmt ... " + sInsuAmt);
				this.info("sInsuMonth ... " + toFullWidth("" + insuM));

				String dataLines = "";
				dataLines = "\"H1\",\"" + custId + "\",\"" + phoneNo + "\",\"您好：提醒您" + sInsuMonth
						+ "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\""
						+ dateSlashFormat(this.getTxBuffer().getMgBizDate().getTbsDy()) + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(facmNo);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險費扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

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

	private void sendEmail(String repayBank, int custNo, int facmNo, int repayType, BigDecimal repayAmt, String emailAd, String custId,
			int insuM, TitaVo titaVo) throws LogicException {
//		RepayType = 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
		this.info("setMail...");
		if (repayType == 1 || repayType == 3) {
			this.info("RepayType() == 1...");
			if (!custLoanFlag.containsKey(custNo)) {
				String dataLines = "";
				dataLines = "親愛的客戶，繳款通知；新光人壽關心您。";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(custNo);
				tTxToDoDetail.setFacmNo(facmNo);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		} else if (repayType == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(custNo)) {
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
				tTxToDoDetail.setFacmNo(facmNo);
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款失敗>" + repayBank);
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlag.put(custNo, 1);
			} else {
				this.info("CustNo : " + custNo + " is continue ...");
			}
		}
	}

	private int check(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		int result = 0;
		int deductDay = 0;
		int entryDay = 0;

		List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();

		Slice<BankDeductDtl> sBankDeductDtl = null;

		sBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, this.limit, titaVo);

		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

//		扣款日為KEY，故僅抓取第一筆
		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			BankDeductDtl tBankDeductDtl = lBankDeductDtl.get(0);
			deductDay = tBankDeductDtl.getPayIntDate();
		}

//		入帳日
		entryDay = tBatxDetail.getEntryDate();
//		應繳迄日為入帳日之上營業日
		dateUtil.init();
		payDayA2 = dateUtil.getbussDate(entryDay, -1);
//		應繳起日為入帳日之兩個營業日前的隔天
		dateUtil.init();
		payDayA1 = caculateDate(dateUtil.getbussDate(entryDay, -2), 0, 0, 1);
//		一扣二扣之間隔為5個營業日
		dateUtil.init();
		payDayB2 = dateUtil.getbussDate(entryDay, -6);
		dateUtil.init();
		payDayB1 = caculateDate(dateUtil.getbussDate(entryDay, -7), 0, 0, 1);

		this.info("entryDay ... " + entryDay);
		this.info("payDayA1 ... " + payDayA1);
		this.info("payDayA2 ... " + payDayA2);
		this.info("payDayB1 ... " + payDayB1);
		this.info("payDayB2 ... " + payDayB2);
		this.info("deductDay ... " + deductDay);

//		ex. 1091008 做L4450 假日提前作業
//		1091009 10 11 放假 
//		1091001 02 03 04 放假 
//		下營業日為1091012 (應繳迄日)
//		下下營業日為1091013 (入帳日)
//		入帳日之兩個營業日前的隔天為 1091009 (應繳起日)
//		若扣款日為1091009~1091012則為一扣
//		若扣款日為1091001~1091005則為二扣
		if (payDayA1 <= deductDay && deductDay <= payDayA2) {
			result = 1;
		} else if (payDayB1 <= deductDay && deductDay <= payDayB2) {
			result = 2;
		}
		return result;
	}

	private void reportA(BatxDetail tBatxDetail, TitaVo titaVo) {
		this.info("ReportA Start...");
//		戶號 額度 戶名 計息起日 計息迄日 期款金額 扣款銀行 帳號
//		總計 筆數
		reportACnt = reportACnt + 1;

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tBatxDetail.getCustNo(), tBatxDetail.getCustNo(), titaVo);

		HashMap<tmpFacm, Integer> startDate = new HashMap<>();
		HashMap<tmpFacm, Integer> endDate = new HashMap<>();
		HashMap<tmpFacm, String> bank = new HashMap<>();
		HashMap<tmpFacm, String> acct = new HashMap<>();

		List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();

		Slice<BankDeductDtl> sBankDeductDtl = null;

		sBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), this.index, this.limit, titaVo);

		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

//		Group By 戶號 額度 還款類別
		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
				tmpFacm tmp = new tmpFacm(tBankDeductDtl.getCustNo(), tBankDeductDtl.getFacmNo(),
						tBankDeductDtl.getRepayType());
//				計息起日取最小
				if (startDate.containsKey(tmp)) {
					if (tBankDeductDtl.getIntStartDate() < startDate.get(tmp)) {
						startDate.put(tmp, tBankDeductDtl.getIntStartDate());
					}
				} else {
					startDate.put(tmp, tBankDeductDtl.getIntStartDate());
				}
//				計息迄日取最大
				if (endDate.containsKey(tmp)) {
					if (tBankDeductDtl.getIntEndDate() > endDate.get(tmp)) {
						endDate.put(tmp, tBankDeductDtl.getIntEndDate());
					}
				} else {
					endDate.put(tmp, tBankDeductDtl.getIntEndDate());
				}
//				
				if (bank.containsKey(tmp)) {
					if ("000".equals(bank.get(tmp))) {
						bank.put(tmp, tBankDeductDtl.getRepayBank());
					}
				} else {
					bank.put(tmp, tBankDeductDtl.getRepayBank());
				}

				if (!acct.containsKey(tmp)) {
					acct.put(tmp, tBankDeductDtl.getRepayAcctNo());
				}
			}
		}

		tmpFacm tmp = new tmpFacm(tBatxDetail.getCustNo(), tBatxDetail.getFacmNo(), tBatxDetail.getRepayType());

		OccursList occursList = new OccursList();
		occursList.putParam("ReportACustNo", tBatxDetail.getCustNo());
		occursList.putParam("ReportAFacmNo", tBatxDetail.getFacmNo());
		occursList.putParam("ReportACustName", tCustMain.getCustName());
		occursList.putParam("ReportAIntStartDate", startDate.get(tmp));
		occursList.putParam("ReportAIntEndDate", endDate.get(tmp));
		occursList.putParam("ReportARepayAmt", tBatxDetail.getRepayAmt());
		occursList.putParam("ReportARepayBank", bank.get(tmp));
		occursList.putParam("ReportARepayAcctNo", acct.get(tmp));

		this.totaVo.addOccursList(occursList);
	}

//	還本繳息通知單
	private void reportB(TitaVo titaVo) throws LogicException {
		this.info("ReportB Start...");

		titaVo.putParam("EntryDay1", payDayA2);
		titaVo.putParam("AcDay1", payDayA1);

		this.info("EntryDay1 ..." + payDayA2);
		this.info("AcDay1 ..." + payDayA1);

		l9705Report.exec(titaVo, this.getTxBuffer());
	}

//	二扣未成功之明信片
	private void reportC(TitaVo titaVo) throws LogicException {
		this.info("ReportC Start...");
		List<Map<String, String>> L4454List = null;

		try {
			L4454List = l4454ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L4454ServiceImpl error = " + e.getMessage());
		}

		if (L4454List != null && !L4454List.isEmpty()) {
			l4454Report.exec(titaVo, this.getTxBuffer(), L4454List);
		} else {
			this.info("L4454List is null...");
		}
	}

//	檢查期款失敗
	private Boolean deductCheck(List<BatxDetail> lBatxDetail) throws LogicException {
		for (BatxDetail tBatxDetail : lBatxDetail) {
			this.info("...deductCheck Start... ");
			this.info("CustNo... " + tBatxDetail.getCustNo());
			this.info("FacmNo... " + tBatxDetail.getFacmNo());
			this.info("RepayType... " + tBatxDetail.getRepayType());
			this.info("ProcStsCode... " + tBatxDetail.getProcStsCode());

//			期款失敗上記號
			if (tBatxDetail.getRepayType() == 1 && "1".equals(tBatxDetail.getProcStsCode())) {
				return true;
			}
		}
		
		return false ;
	}

	
	
	
//	檢查期款失敗
	private void tempsave(List<BatxDetail> lBatxDetail) throws LogicException {
		for (BatxDetail tBatxDetail : lBatxDetail) {
//			火險成功存
			if (tBatxDetail.getRepayType() == 5 && "5".equals(tBatxDetail.getProcStsCode()) ||
					tBatxDetail.getRepayType() == 5 && "6".equals(tBatxDetail.getProcStsCode())) {
				tempBatxDetail.add(tBatxDetail);
			}
		}
		
		for (BatxDetail tBatxDetail : lBatxDetail) {

//			期款失敗存
			if (tBatxDetail.getRepayType() == 1 && "1".equals(tBatxDetail.getProcStsCode())) {
				tempBatxDetail.add(tBatxDetail);
			}
		}
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

//	找出當月特殊扣款日後第五個營業日
	private int find5BsDd(int today) throws LogicException {
		dateUtil.init();

		int todayPlus1BsDay = 0;
		int todayPlus2BsDay = 0;
		int todayPlus3BsDay = 0;
		int todayPlus4BsDay = 0;
		int todayPlus5BsDay = 0;

		dateUtil.init();
		todayPlus1BsDay = findNextBsDay(today);
		todayPlus2BsDay = findNextBsDay(todayPlus1BsDay);
		todayPlus3BsDay = findNextBsDay(todayPlus2BsDay);
		todayPlus4BsDay = findNextBsDay(todayPlus3BsDay);
		todayPlus5BsDay = findNextBsDay(todayPlus4BsDay);

		return todayPlus5BsDay;
	}

	private int findNextBsDay(int today) throws LogicException {
		while (true) {
			dateUtil.init();
			dateUtil.setDate_1(today);
			dateUtil.setDays(1);
			today = dateUtil.getCalenderDay();
			if (!dateUtil.isHoliDay()) {
				break;
			}
		}
		return today;
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

	private int caculateDate(int today, int year, int month, int days) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setYears(year);
		dateUtil.setMons(month);
		dateUtil.setDays(days);
		today = dateUtil.getCalenderDay();
		return today;
	}

//	刪除TxToDoDetail
	private void deleteTxtoDo(String itemCode, TitaVo titaVo) {
//		刪除未處理且為今天的
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0,
				this.getTxBuffer().getTxCom().getTbsdyf(), this.getTxBuffer().getTxCom().getTbsdyf(), this.index,
				this.limit, titaVo);
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		if (slTxToDoDetail != null) {
			for (TxToDoDetail tTxToDoDetail : slTxToDoDetail.getContent()) {
				if (tTxToDoDetail.getDtlValue().length() >= 9) {
					if (tTxToDoDetail.getDtlValue().substring(0, 8).equals("<期款扣款失敗>")
							|| tTxToDoDetail.getDtlValue().substring(0, 8).equals("<火險扣款失敗>")) {
						switch (iRepayBank) {
						case "999": // ALL
							lTxToDoDetail.add(tTxToDoDetail);
							break;
						case "998": // ACH
							if (!tTxToDoDetail.getDtlValue().substring(8, 3).equals("700")) {
								lTxToDoDetail.add(tTxToDoDetail);
							}
							break;
						default: // Bank
							if (!tTxToDoDetail.getDtlValue().substring(8, 3).equals(iRepayBank)) {
								lTxToDoDetail.add(tTxToDoDetail);
							}
							break;
						}
					}
				}
			}
		}
		if (lTxToDoDetail.size() > 0) {
			try {
				this.info("DeleteAll...");
				txToDoCom.addByDetailList(false, 1, lTxToDoDetail, titaVo);
			} catch (LogicException e) {
				this.info("DeleteAll Error : " + e.getErrorMsg());
			}
		}
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