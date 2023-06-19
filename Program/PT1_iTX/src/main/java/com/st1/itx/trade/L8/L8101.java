package com.st1.itx.trade.L8;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;
import com.st1.itx.db.service.TxAmlCreditService;

import com.st1.itx.db.domain.TxAmlNotice;
import com.st1.itx.db.domain.TxAmlNoticeId;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.TxAmlNoticeService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.springjpa.cm.L9705ServiceImpl;
import com.st1.itx.trade.L9.L9703Report2;
import com.st1.itx.trade.L9.L9705Report;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;

import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CustTelNoService;

import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.db.domain.TxToDoDetailId;

import com.st1.itx.util.common.CustNoticeCom;

@Service("L8101")
@Scope("prototype")
/**
 * 
 * LogFlag : Y.YES N.NO B.BATCH LOG
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8101 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	TxFileService txFileService;

	@Autowired
	CustMainService custMainService;

	@Autowired
	CustTelNoService custTelNoService;

	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	TxAmlNoticeService txAmlNoticeService;

	@Autowired
	public TxToDoCom txToDoCom;

//	@Autowired
//	L9703Report2 l9703report2;
	
	@Autowired
	private L9705ServiceImpl l9705ServiceImpl;

	@Autowired
	L9705Report l9705Report;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8101 ");
		this.totaVo.init(titaVo);

		int dataDt = parse.stringToInteger(titaVo.get("DataDt")) + 19110000;
		String custKey = titaVo.get("CustKey").trim();
		int processDate = parse.stringToInteger(titaVo.get("ProcessDate")) + 19110000;
		String processTlrNo = titaVo.get("ProcessTlrNo").trim();
		String processNote = titaVo.get("ProcessNote").trim();

		TxAmlCreditId txAmlCreditId = new TxAmlCreditId(dataDt, custKey);
		TxAmlCredit txAmlCredit = txAmlCreditService.holdById(txAmlCreditId, titaVo);

		if (txAmlCredit == null) {
			throw new LogicException("E0001", dataDt + "/" + custKey);
		}

		String processType = txAmlCredit.getProcessType();

//		if ("2".equals(processType) && "B".equals(titaVo.get("LogFlag").trim())) {
//			throw new LogicException("E0000", "不可整批處理");
//		}

		String custName = "";
		String custAddr = "";
		String custMobile = "";

		CustMain custMain = custMainService.custIdFirst(custKey);
		if (custMain == null) {
			throw new LogicException("E0001", "客戶資料 " + custKey);
		} else {
			custAddr = custMain.getCurrZip2().trim() + custMain.getCurrZip3().trim() + custMain.getCurrRoad();
			custAddr = custMain.getCurrZip3() + custMain.getCurrZip2() + custNoticeCom.getCurrAddress(custMain, titaVo);
			custName = custMain.getCustName();

		}

		if ("3".equals(processType)) {

			CustTelNo custTelNo = custTelNoService.custUKeyFirst(custMain.getCustUKey(), "03", titaVo);
			if (custTelNo == null) {
				throw new LogicException("E0001", "客戶 " + custKey + " 無行動電話資料");
			}

			if (custTelNo.getTelNo().length() > 10) {
				custMobile = custTelNo.getTelNo().substring(0, 10);
			} else {
				custMobile = custTelNo.getTelNo();
			}

			// must
			txToDoCom.setTxBuffer(this.getTxBuffer());

			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setCustNo(custMain.getCustNo());
			tTxToDoDetail.setFacmNo(0);
			tTxToDoDetail.setBormNo(0);
			tTxToDoDetail.setDtlValue("<AML定審簡訊通知>");
			tTxToDoDetail.setItemCode("TEXT00");
			tTxToDoDetail.setStatus(0);
			tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(custMobile, "房貸客戶提醒：為維護您的權益，戶籍或通訊地址、電子信箱及連絡電話，或姓名、身分證統一編號等重要資訊有異動時，敬請洽詢公司服務人員或客戶服務部（０８００—０３１１１５）辦理變更。", this.getTxBuffer().getTxCom().getTbsdy()));

			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

		}

		this.info("L8101 custMobile = " + custMobile);

		int count = txAmlCredit.getProcessCount();

		if (!"N".equals(titaVo.get("LogFlag").trim())) {

			txAmlCredit.setProcessCount(count + 1);
			txAmlCredit.setProcessDate(processDate);
			txAmlCredit.setProcessBrNo(titaVo.get("KINBR").trim());
			txAmlCredit.setProcessGroupNo(this.txBuffer.getTxCom().getTlrDept());
			txAmlCredit.setProcessTlrNo(processTlrNo);
			txAmlCredit.setProcessNote(processNote);
			if ("3".equals(processType)) {
				txAmlCredit.setProcessMobile(custMobile);
			} else {
				txAmlCredit.setProcessAddress(custAddr);
				txAmlCredit.setProcessName(custName);
			}

			try {
				txAmlCreditService.update(txAmlCredit);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 新增資料已存在
			}

			TxAmlNotice txAmlNotice = new TxAmlNotice();
			TxAmlNoticeId txAmlNoticeId = new TxAmlNoticeId();

			txAmlNoticeId.setDataDt(dataDt);
			txAmlNoticeId.setCustKey(custKey);
			txAmlNoticeId.setProcessSno(count);

			txAmlNotice.setTxAmlNoticeId(txAmlNoticeId);

			txAmlNotice.setReviewType(txAmlCredit.getReviewType());
			txAmlNotice.setProcessType(txAmlCredit.getProcessType());
			txAmlNotice.setProcessBrNo(titaVo.get("KINBR").trim());
			txAmlNotice.setProcessGroupNo(this.txBuffer.getTxCom().getTlrDept());
			txAmlNotice.setProcessTlrNo(titaVo.get("ProcessTlrNo").trim());
			txAmlNotice.setProcessDate(processDate);
			if ("3".equals(processType)) {
				txAmlNotice.setProcessMobile(custMobile);
			} else {
				txAmlNotice.setProcessAddress(custAddr);
				txAmlNotice.setProcessName(custName);
			}

			txAmlNotice.setProcessNote(processNote);

			try {
				txAmlNoticeService.insert(txAmlNotice, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}

			// 待辦清單
			txToDoCom.setTxBuffer(this.getTxBuffer());

			TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
			tTxToDoDetailId.setItemCode("AML" + txAmlCredit.getReviewType());
			tTxToDoDetailId.setCustNo(0);
			tTxToDoDetailId.setFacmNo(0);
			tTxToDoDetailId.setBormNo(0);
			tTxToDoDetailId.setDtlValue(txAmlCredit.getDataDt() + "-" + txAmlCredit.getCustKey());
			txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
		}

		Long pdfSno = 0L;

//		if ("2".equals(txAmlCredit.getProcessType()) && custMain.getCustNo() > 0 && "N".equals(titaVo.get("LogFlag").trim())) {
		if ("2".equals(txAmlCredit.getProcessType()) && custMain.getCustNo() > 0) {
//			#AcctDate     會計日期
//			#CustNo       戶號-1
//			#FacmNo       戶號-2
//			#UnpaidCond   滯繳條件
//			#UnpaidTermSt 滯繳期數-1
//			#UnpaidTermEd 滯繳期數-2
//			#UnpaidDaySt  滯繳日數-1
//			#UnpaidDayEd  滯繳日數-2
//			#RepayType    繳款方式
//			#CustType     戶別

//			titaVo.putParam("AcctDate", Integer.toString(this.getTxBuffer().getTxCom().getTbsdyf()));
//			titaVo.putParam("CustNo", Integer.toString(custMain.getCustNo()));
//			titaVo.putParam("FacmNo", "0");
//			titaVo.putParam("UnpaidCond", "1");
//			titaVo.putParam("UnpaidTermSt", "01");
//			titaVo.putParam("UnpaidTermEd", "02");
//			titaVo.putParam("UnpaidDaySt", "001");
//			titaVo.putParam("UnpaidDayEd", "001");
//			titaVo.putParam("RepayType", "0");
//			titaVo.putParam("CustType", "0");
			
//			String acctDateStart = titaVo.getParam("ACCTDATE_ST");
//			String acctDateEnd = titaVo.getParam("ACCTDATE_ED");
//			String custNoStart = titaVo.getParam("CUSTNO");
//			String custNoEnd = titaVo.getParam("CUSTNOB");
//			String condition1 = titaVo.getParam("CONDITION1");
//			String condition2 = titaVo.getParam("CONDITION2");
//			String idType = titaVo.getParam("ID_TYPE");
//			String corpInd = titaVo.getParam("CORP_IND");
//			String apNo = titaVo.getParam("APNO");
			
//			l9703report2.setParentTranCode(titaVo.get("TXCD"));
//			pdfSno = l9703report2.exec(titaVo, this.txBuffer);

			titaVo.putParam("ACCTDATE_ST", this.getTxBuffer().getTxCom().getTbsdyf());
			titaVo.putParam("ACCTDATE_ED", this.getTxBuffer().getTxCom().getTbsdyf());
			titaVo.putParam("CUSTNO", custMain.getCustNo());
			titaVo.putParam("CUSTNOB", custMain.getCustNo());
			titaVo.putParam("CONDITION1", 0);
			titaVo.putParam("CONDITION2", 0);
			titaVo.putParam("ID_TYPE", 0);
			titaVo.putParam("CORP_IND", 0);
			titaVo.putParam("APNO", 0);
			titaVo.putParam("Terms", 2); //只印2期
			
			l9705Report.setParentTranCode(titaVo.getTxcd());
			List<Map<String, String>> l9705List = null;
			try {
				l9705List = l9705ServiceImpl.findAll(titaVo,"");
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9705ServiceImpl.findAll error = " + errors.toString());
			}
			pdfSno = l9705Report.exec(l9705List, titaVo, this.getTxBuffer());

		}

		this.totaVo.putParam("CustAddr", custAddr);
		this.totaVo.putParam("CustName", custName);
		this.totaVo.putParam("PdfSno", pdfSno);

		this.addList(this.totaVo);
		return this.sendList();
	}
}