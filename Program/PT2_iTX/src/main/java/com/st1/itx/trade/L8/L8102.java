package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;
import com.st1.itx.db.domain.TxAmlNotice;
import com.st1.itx.db.domain.TxAmlNoticeId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.TxAmlCreditService;
import com.st1.itx.db.service.TxAmlNoticeService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.db.service.springjpa.cm.L8102ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

@Service("L8102")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8102 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8102.class);

	/* DB服務注入 */
	@Autowired
	CustMainService custMainService;
	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	CustTelNoService custTelNoService;

	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	TxAmlNoticeService txAmlNoticeService;

	@Autowired
	TxToDoMainService txToDoMainService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public CdBcmService cdBcmService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public L8102ServiceImpl sL8102ServiceImpl;

	@Autowired
	private MakeExcel makeExcel1;
	@Autowired
	private MakeExcel makeExcel2;
	@Autowired
	private MakeFile makeFileText;
	@Autowired
	private MakeFile makeFileMail;
	private int wkCalDy = 0;

	@Autowired
	private MailService mailService;

	@Autowired
	Parse parse;

	int custNo;
	int excelRow = 0;

	CustMain custMain = null;

	String messagePhone = "";

	int dataDt7 = 0;
	int dataDt8 = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8102 ");
		this.totaVo.init(titaVo);
		wkCalDy = dateUtil.getNowIntegerForBC();
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L8102").setRptItem("期款扣款通知(簡訊)").build();
		// 開啟報表
		makeFileText.open(titaVo, reportVo, "簡訊檔.txt");
		ReportVo mailReportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L8102").setRptItem("期款扣款通知(EMail)").build();
		// 開啟報表
		makeFileMail.open(titaVo, mailReportVo, "email檔.txt");

		int dataSource = 1;
		if (titaVo.get("DataSource") != null) {
			dataSource = parse.stringToInteger(titaVo.get("DataSource"));
		}
		dataDt7 = parse.stringToInteger(titaVo.getCalDy());
		if (titaVo.get("DataDt") != null) {
			dataDt7 = parse.stringToInteger(titaVo.get("DataDt"));
		}
		dataDt8 = dataDt7 + 19110000;

		// delete TxAmlCredit

		this.index = 0;

		this.limit = 500;

		Slice<TxAmlCredit> slTxAmlCredit = null;

		slTxAmlCredit = txAmlCreditService.dataDtAll(dataDt8, this.index, Integer.MAX_VALUE, titaVo);

		List<TxAmlCredit> lTxAmlCredit = slTxAmlCredit == null ? null : slTxAmlCredit.getContent();

		// delete TxAmlNotice
		Slice<TxAmlNotice> slTxAmlNotice = null;

		slTxAmlNotice = txAmlNoticeService.dataDtAll(dataDt8, this.index, this.limit, titaVo);

		List<TxAmlNotice> lTxAmlNotice = slTxAmlNotice == null ? null : slTxAmlNotice.getContent();

		// 人工輸入測試資料,會刪除定審及通知資料
		if (dataSource == 2) {
			if (lTxAmlCredit != null) {
				try {
					txAmlCreditService.deleteAll(lTxAmlCredit, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "TxAmlCredit=" + e.getErrorMsg());
				}
			}

			if (lTxAmlNotice != null) {
				try {
					txAmlNoticeService.deleteAll(lTxAmlNotice, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "TxAmlNotice=" + e.getErrorMsg());
				}
			}
		}

		// must
		txToDoCom.setTxBuffer(this.getTxBuffer());

		// delete detail
		deleteTxToDoDetail("AMLH", "ReBuild Data", titaVo);
		deleteTxToDoDetail("AMLM", "ReBuild Data", titaVo);
		deleteTxToDoDetail("AMLL", "ReBuild Data", titaVo);
//		String reviewType = "";
		if (dataSource == 2) {
			for (int i = 1; i < 10; i++) {
				String custKey = titaVo.get("CustKey" + i).trim();
				if (!"".equals(custKey)) {

					BigDecimal rrSeq = new BigDecimal(titaVo.get("RRSeq" + i));
					String reviewType = titaVo.get("ReviewType" + i).trim();
					String unit = titaVo.get("Unit" + i).trim();
					int isStatus = parse.stringToInteger(titaVo.get("IsStatus" + i));
					String ConfirmStatus = titaVo.get("ConfirmStatus" + i).trim();

					TxAmlCredit txAmlCredit = new TxAmlCredit();
					TxAmlCreditId txAmlCreditId = new TxAmlCreditId(dataDt8, custKey);
					txAmlCredit.setTxAmlCreditId(txAmlCreditId);

					txAmlCredit.setCustKey(custKey);
					txAmlCredit.setRRSeq(rrSeq);
					txAmlCredit.setReviewType(reviewType);
					txAmlCredit.setUnit(unit);
					txAmlCredit.setIsStatus(isStatus);
					txAmlCredit.setWlfConfirmStatus(ConfirmStatus);

					processDetail(titaVo, txAmlCredit, true);
				}
			}
		} else {
			if (lTxAmlCredit != null && lTxAmlCredit.size() > 0) {
				this.info("L8102 lTxAmlCredit.size = " + lTxAmlCredit.size());

				int l = 0;
				int m = 0;
				int h = 0;

				for (TxAmlCredit txAmlCredit : lTxAmlCredit) {
					processDetail(titaVo, txAmlCredit, false);
					String iReviewType = txAmlCredit.getReviewType();

					if ("L".equals(iReviewType) && l == 0) {
						l++;
						String X = "L";
						findemail(X, titaVo, txAmlCredit, false);

					}
					if ("M".equals(iReviewType) && m == 0) {
						m++;
						String X = "M";
						findemail(X, titaVo, txAmlCredit, false);

					}
					if ("H".equals(iReviewType) && h == 0) {
						h++;
						String X = "H";
						findemail(X, titaVo, txAmlCredit, false);
					}

				}
			}

		}

		if (excelRow > 0) {
			makeExcel1.close();
			makeExcel2.close();
		}
		makeFileText.close();
		makeFileMail.close();
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void findemail(String X, TitaVo titaVo, TxAmlCredit txAmlCredit, boolean newflag) throws LogicException {
		List<Map<String, String>> L8102DateList = null;
		// 寄送main 要依照高中低人員分別寄送
		if (X.equals("L")) {
			L8102DateList = sL8102ServiceImpl.findAll(X, titaVo);
		}
		if (X.equals("M")) {
			L8102DateList = sL8102ServiceImpl.findAll(X, titaVo);
		}
		if (X.equals("H")) {
			L8102DateList = sL8102ServiceImpl.findAll(X, titaVo);
		}
		//email  含要再修改 先測試是否有正常傳送
		String keeperemail = "";
		for (Map<String, String> t : L8102DateList) {
			keeperemail = t.get("Email");
		}

		String subject1 = "[放款帳務系統]AML定期審查案件通知 ";
		String bodyText = "";
		bodyText += "新增客戶定期審查案件，請登入放款帳務系統進行定期審查作業。";
		bodyText += "AML作業路徑:定期審查功能>定期審查問卷作業";
		
		mailService.setParams(keeperemail, subject1, bodyText);
		mailService.exec();

	}

	private void processDetail(TitaVo titaVo, TxAmlCredit txAmlCredit, boolean newflag) throws LogicException {

		custMain = custMainService.custIdFirst(txAmlCredit.getCustKey());
		if (custMain == null) {
			this.info("L8102 CustMain not found = " + txAmlCredit.getCustKey());
			return;
		}

		txAmlCredit = checkProcessType(titaVo, txAmlCredit.getCustKey(), txAmlCredit);

		this.info("TxAmlCredit.ProcessType = " + txAmlCredit.getProcessType());
		txAmlCredit.setProcessCount(0);

		try {// 判斷分法人且有手機 FLAG 3 要 insert TxAmlNotice
			if (newflag) {
				txAmlCreditService.insert(txAmlCredit, titaVo);
			} else {
				int count = txAmlCredit.getProcessCount();
				txAmlCredit.setProcessCount(count + 1);
				txAmlCredit = txAmlCreditService.update2(txAmlCredit, titaVo);

				int iDataDt = txAmlCredit.getDataDt();
				String iCustKey = txAmlCredit.getCustKey();
				String iProcessType = txAmlCredit.getProcessType();
				String iReviewType = txAmlCredit.getReviewType();
//				String iProcessType =txAmlCredit.getProcessType();
				String iProcessBrNo = txAmlCredit.getProcessBrNo();
				String iProcessGroupNo = txAmlCredit.getProcessGroupNo();
				String iProcessTlrNo = txAmlCredit.getProcessTlrNo();
				int iProcessDate = 0;
				if(txAmlCredit.getProcessDate() == 0 ) {
					iProcessDate = 19110101;
				}
				String iProcessMobile = txAmlCredit.getProcessMobile();
				String iProcessAddress = txAmlCredit.getProcessAddress();
				String iProcessName = txAmlCredit.getProcessName();
				String iProcessNote = txAmlCredit.getProcessNote();
				int iProcessCount = txAmlCredit.getProcessCount();

				if (iProcessType.equals("3")) {
					TxAmlNotice itxAmlNotice = new TxAmlNotice();
					TxAmlNotice txAmlNotice = null;
					TxAmlNoticeId txAmlNoticeId = new TxAmlNoticeId();
					txAmlNoticeId.setCustKey(iCustKey);
					txAmlNoticeId.setDataDt(iDataDt);
					txAmlNoticeId.setProcessSno(iProcessCount);
					txAmlNotice = txAmlNoticeService.findById(txAmlNoticeId, titaVo);
					if (txAmlNotice != null) {
						throw new LogicException("E0002", "已有相同資料");
					}
					itxAmlNotice.setTxAmlNoticeId(txAmlNoticeId);
//					itxAmlNotice.setDataDt(iDataDt);
//					itxAmlNotice.setCustKey(iCustKey);
//					itxAmlNotice.setProcessSno(iProcessCount);
					itxAmlNotice.setReviewType(iReviewType);
					itxAmlNotice.setProcessType(iProcessType);
					itxAmlNotice.setProcessBrNo(iProcessBrNo);
					itxAmlNotice.setProcessGroupNo(iProcessGroupNo);
					itxAmlNotice.setProcessTlrNo(iProcessTlrNo);
					itxAmlNotice.setProcessDate(iProcessDate);
					itxAmlNotice.setProcessMobile(iProcessMobile);
					itxAmlNotice.setProcessAddress(iProcessAddress);
					itxAmlNotice.setProcessName(iProcessName);
					itxAmlNotice.setProcessNote(iProcessNote);

					try {
						txAmlNoticeService.insert(itxAmlNotice, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "新增資料有誤");
					}

				}

			}
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "TxAmlCredit=" + e.getErrorMsg());
		}

		// 高風險EXCEL名單
		if ("H".equals(txAmlCredit.getReviewType())) {
			toExcle(titaVo, txAmlCredit);
		}

		// 發送簡訊內容,待處理
		this.info("ProcessType   = " + txAmlCredit.getProcessType());
		if ("3".equals(txAmlCredit.getProcessType())) {
			String dataLines = txToDoCom.getProcessNoteForText(messagePhone,
					"房貸客戶提醒：為維護您的權益，戶籍或通訊地址、電子信箱及連絡電話，或姓名、身分證統一編號等重要資訊有異動時，敬請洽詢公司服務人員或客戶服務部（０８００—０３１１１５）辦理變更。",
					wkCalDy);

			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setCustNo(custMain.getCustNo());
			tTxToDoDetail.setFacmNo(0);
			tTxToDoDetail.setBormNo(0);
			tTxToDoDetail.setDtlValue("<AML定審簡訊通知>");
			tTxToDoDetail.setItemCode("TEXT00");
			tTxToDoDetail.setStatus(0);
			tTxToDoDetail.setProcessNote(dataLines);
			makeFileText.put(
					parse.IntegerToString(custMain.getCustNo(), 7) + "-" + parse.IntegerToString(0, 3) + dataLines);

			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

		}

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(0);
		tTxToDoDetail.setFacmNo(0);
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue(dataDt7 + "-" + txAmlCredit.getCustKey());

		String itemCode = "";

		if ("H".equals(txAmlCredit.getReviewType())) {
			itemCode = "AMLH";
		} else {
			itemCode = "AMLM";
		}
		tTxToDoDetail.setItemCode(itemCode);
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote("AML定審");

		txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

	}

//  產製高風險定期審查郵寄名單.xlsx , 高風險定期審查明細.xlsx
	private void toExcle(TitaVo titaVo, TxAmlCredit txAmlCredit) throws LogicException {

		String dataDtX = String.valueOf(dataDt7);

		this.info("L8102.toExcle = " + dataDt7);

		if (excelRow == 0) {
			makeExcel1 = (MakeExcel) MySpring.getBean("makeExcel");

			makeExcel1.open(titaVo, dataDt8, "0000", "L8101A", "高風險定期審查郵寄名單", "高風險定期審查郵寄名單");

			String s = "";
			if (dataDtX.length() >= 5) {
				s = dataDtX.substring(0, 3) + "年" + dataDtX.substring(3, 5) + "月定審名單";
			} else {
				s = dataDtX + "定審名單";
			}

			makeExcel1.setMergedRegionValue(1, 1, 1, 10, s, "", "C");

			makeExcel1.setWidth(1, 12);
			makeExcel1.setValue(2, 1, "流水編號");
			makeExcel1.setWidth(2, 12);
			makeExcel1.setValue(2, 2, "審查日期");
			makeExcel1.setWidth(3, 12);
			makeExcel1.setValue(2, 3, "戶號");
			makeExcel1.setWidth(4, 20);
			makeExcel1.setValue(2, 4, "客戶ID");
			makeExcel1.setWidth(5, 20);
			makeExcel1.setValue(2, 5, "姓名");
			makeExcel1.setWidth(6, 80);
			makeExcel1.setValue(2, 6, "住址");
			makeExcel1.setWidth(7, 12);
			makeExcel1.setValue(2, 7, "郵遞區號");
			makeExcel1.setWidth(8, 12);
			makeExcel1.setValue(2, 8, "審查類型");
			makeExcel1.setWidth(9, 30);
			makeExcel1.setValue(2, 9, "審查單位 ");
			makeExcel1.setWidth(10, 20);
			makeExcel1.setValue(2, 10, "名單類型 ");

			//

			makeExcel2 = (MakeExcel) MySpring.getBean("makeExcel");
			makeExcel2.open(titaVo, dataDt8, "0000", "L8101B", "高風險定期審查明細", "高風險定期審查明細");

			makeExcel2.setMergedRegionValue(1, 1, 1, 8, s, "", "C");

			makeExcel2.setWidth(1, 12);
			makeExcel2.setValue(2, 1, "流水編號");
			makeExcel2.setWidth(2, 12);
			makeExcel2.setValue(2, 2, "審查日期");
			makeExcel2.setWidth(3, 20);
			makeExcel2.setValue(2, 3, "客戶ID");
			makeExcel2.setWidth(4, 20);
			makeExcel2.setValue(2, 4, "姓名");
			makeExcel2.setWidth(5, 12);
			makeExcel2.setValue(2, 5, "審查類型");
			makeExcel2.setWidth(6, 20);
			makeExcel2.setValue(2, 6, "審查單位 ");
			makeExcel2.setWidth(7, 20);
			makeExcel2.setValue(2, 7, "名單類型 ");
			makeExcel2.setWidth(8, 20);
			makeExcel2.setValue(2, 8, "審查狀態");

			excelRow = 2;
		}

		String unitItem = "";

		CdBcm cdBcm = cdBcmService.findById(txAmlCredit.getUnit(), titaVo);
		if (cdBcm != null) {
			unitItem = cdBcm.getUnitItem();
		}

		excelRow++;

		makeExcel1.setValue(excelRow, 1, String.valueOf(txAmlCredit.getRRSeq()));
		makeExcel1.setValue(excelRow, 2, String.valueOf(dataDt7));
		makeExcel1.setValue(excelRow, 3, String.valueOf(custMain.getCustNo()));
		makeExcel1.setValue(excelRow, 4, txAmlCredit.getCustKey());
		makeExcel1.setValue(excelRow, 5, custMain.getCustName());
		makeExcel1.setValue(excelRow, 6, custNoticeCom.getCurrAddress(custMain, titaVo));
		makeExcel1.setValue(excelRow, 7, custMain.getCurrZip2() + custMain.getCurrZip3());
		makeExcel1.setValue(excelRow, 8, "高");
		makeExcel1.setValue(excelRow, 9, txAmlCredit.getUnit() + "-" + unitItem);

		String ConfirmStatus = "";
		CdCodeId cdCodeId = new CdCodeId("AmlConfirmStatus", txAmlCredit.getWlfConfirmStatus());
		CdCode cdCode = cdCodeService.findById(cdCodeId, titaVo);
		if (cdCode != null) {
			ConfirmStatus = cdCode.getItem();
		}

		makeExcel1.setValue(excelRow, 10, txAmlCredit.getWlfConfirmStatus() + ":" + ConfirmStatus);

		makeExcel2.setValue(excelRow, 1, String.valueOf(txAmlCredit.getRRSeq()));
		makeExcel2.setValue(excelRow, 2, String.valueOf(dataDt7));
		makeExcel2.setValue(excelRow, 3, txAmlCredit.getCustKey());
		makeExcel2.setValue(excelRow, 4, custMain.getCustName());
		makeExcel2.setValue(excelRow, 5, "高");
		makeExcel2.setValue(excelRow, 6, txAmlCredit.getUnit() + "-" + unitItem);
		makeExcel2.setValue(excelRow, 7, txAmlCredit.getWlfConfirmStatus() + ":" + ConfirmStatus);

		String isStatus = "";
		cdCodeId = new CdCodeId("AmlIsStatus", String.format("%02d", txAmlCredit.getIsStatus()));
		cdCode = cdCodeService.findById(cdCodeId, titaVo);
		if (cdCode != null) {
			isStatus = cdCode.getItem();
		}
		makeExcel2.setValue(excelRow, 8, txAmlCredit.getIsStatus() + ":" + isStatus);

	}

//	刪除TxToDoDetail
	private void deleteTxToDoDetail(String itemCode, String dtlValue, TitaVo titaVo) {
		Slice<TxToDoDetail> sTxToDoDetail = null;
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
//		刪除未處理且為今天的
		sTxToDoDetail = txToDoDetailService.itemCodeRange(itemCode, dtlValue, 0, 0,
				this.getTxBuffer().getTxCom().getTbsdyf(), this.getTxBuffer().getTxCom().getTbsdyf(), this.index,
				this.limit, titaVo);

		lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			try {
				txToDoCom.addByDetailList(false, 1, lTxToDoDetail, titaVo);
			} catch (LogicException e) {
				logger.info("DeleteAll Error : " + e.getErrorMsg());
			}
		}
	}

	// 判別AML記號
	private TxAmlCredit checkProcessType(TitaVo titaVo, String custKey, TxAmlCredit txAmlCredit) {
		this.info("L8102.checkProcessType CustKey = " + custKey + "/" + txAmlCredit.getReviewType());

		if ("H".equals(txAmlCredit.getReviewType())) {
			this.info("L8102.checkProcessType 1");
			txAmlCredit.setProcessType("1");
		} else if (custKey.length() <= 8) {
			this.info("L8102.checkProcessType 3");
			txAmlCredit.setProcessType("2");
		} else {
			if (custMain == null) {
				this.info("L8102.checkProcessType 3");
				txAmlCredit.setProcessType("2");
			} else {
				messagePhone = custNoticeCom.getPhone(custMain, titaVo);
				if (messagePhone.isEmpty()) {
					this.info("L8102.checkProcessType 4");
					txAmlCredit.setProcessType("2");
				} else {
					this.info("L8102.checkProcessType 5");
					txAmlCredit.setProcessType("3");
				}

			}
		}
		return txAmlCredit;
	}
}