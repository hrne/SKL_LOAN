package com.st1.itx.maintain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.dataVO.TxCom;

import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.CustDataCtrl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.service.CustDataCtrlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxFlowService;

import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.TxRecordService;

import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.common.AcEnterCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.parse.Parse;

import com.st1.itx.util.common.LockControl;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.CheckAuth;

/**
 * base process
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
@Component("mainProcess")
@Scope("prototype")
//@Scope("singleton")
public class MainProcess extends SysLogger {
	/* DB */
	@Autowired
	public TxTranCodeService txTranCodeService;

//	@Autowired
//	public TellerService tellerService;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	public CustDataCtrlService sCustDataCtrlService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public LockControl lockControl;

	/* Data Value Object */
	private TitaVo titaVo;

	/* var */
	@Autowired
	public TxBuffer txBuffer;

	private TotaVoList totaVoList;

	private TxTeller txTeller;

	/* Tools */
	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcEnterCom acEnterCom;

	@Autowired
	public CheckAuth checkAuth;

	@Autowired
	public SendRsp sendRsp;

	/* Work Area */
	private int EcCount = 0;

	// eric 2020.6.25
	public void init(TitaVo titaVo) throws LogicException {
		this.info("MainProcess.init");

		this.setTitaVo(titaVo);

		/*
		 * ??????DB?????? 0.onLine 1.onDay 2.onMon 3.onHist
		 */
		TxTeller tTxTeller = txTellerService.findById(this.titaVo.getTlrNo());

		if (tTxTeller != null) {
			if (tTxTeller.getReportDb() == 3)
				this.titaVo.putParam(ContentName.dataBase, ContentName.onHist);
			else if (tTxTeller.getReportDb() == 2)
				this.titaVo.putParam(ContentName.dataBase, ContentName.onMon);
			else if (tTxTeller.getReportDb() == 1)
				this.titaVo.putParam(ContentName.dataBase, ContentName.onDay);
			else
				this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

			// LC??????????????????onLine
//			if (this.titaVo.getTxcd().startsWith("LC") && this.titaVo.getTxCode().startsWith("LC"))
//				this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

			this.titaVo.putParam(ContentName.orgDataBase, this.titaVo.getDataBase());

			ThreadVariable.setObject(ContentName.dataBase, this.titaVo.getDataBase());
			ThreadVariable.setObject(ContentName.loggerFg, tTxTeller.getLoggerFg() == 1 ? true : false);
			ThreadVariable.setObject(ContentName.empnot, this.titaVo.getEmpNot());

			txBuffer.init(titaVo);
			txTeller = tTxTeller;
		} else
			txBuffer.init(titaVo);
	}

	public void cs10Process() throws LogicException {
		this.info("Start cs10Process..." + this.titaVo.getTxCode() + "/" + this.titaVo.isTxcdSpecial());

		this.Cs10Init();

		if (!this.titaVo.isTxcdSpecial() || this.titaVo.getTxcd().equals("LC009") || this.titaVo.getTxcd().equals("LC109")) {
			this.Cs11Check();
		}

		if (!this.getTitaVo().isTxcdInq() && !this.titaVo.isTxcdSpecial() && !this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject()) {
			this.doAcEnterCom();
		}

		this.info("CS10 End TxCom : " + this.txBuffer.getTxCom());
	}

	/**
	 * InitTx
	 * 
	 * @throws LogicException logicexception
	 */
	private void Cs10Init() throws LogicException {
		this.info("Main Process Init...");

		TxCom txCom = this.txBuffer.getTxCom();

		if (this.titaVo.isTxcdInq())
			this.titaVo.putParam(ContentName.hCode, "0");

		if (this.titaVo.getRelCode().trim().isEmpty())
			this.titaVo.putParam(ContentName.relcd, "0");

		this.titaVo.putParam(ContentName.mttpseq, "0");

		if (this.titaVo.isTxcdInq() || this.titaVo.isTxcdSpecial())
			try {
//				Integer.parseInt(this.titaVo.getDelay());
			} catch (NumberFormatException e) {
				this.titaVo.putParam(ContentName.delay, "0");
			}

//		if (this.titaVo.getSecNo() == null || this.titaVo.getSecNo().trim().isEmpty())
//			this.titaVo.putParam(ContentName.secno, this.titaVo.getTxcd().subSequence(2, 3));

		if (this.titaVo.getFbrNo() == null || this.titaVo.getFbrNo().trim().isEmpty())
			this.titaVo.putParam(ContentName.fbrno, this.titaVo.getKinbr());

		// ???????????????
		if (this.txTeller != null)
			this.titaVo.put(ContentName.empnm, this.txTeller.getTlrItem());

		// ??????????????????(0.??????????????????1.??????????????????2.??????????????????)
		if (this.titaVo.isTxcdInq())
			txCom.setTxType(0);
		else if (this.titaVo.isTxcdSpecial())
			txCom.setTxType(2);
		else if (this.titaVo.isTxcdInq())
			txCom.setTxType(2);
		else if (this.titaVo.isTrmtypTermUpdf())
			txCom.setTxType(2);
		else
			txCom.setTxType(1);

		// ?????????????????????????????? APCTL ??????
		this.titaVo.putParam(ContentName.caldy, dateUtil.getNowStringRoc());
		this.titaVo.putParam(ContentName.caltm, dateUtil.getNowStringTime());

//		// eric remark 2020.6.8
//		// ?????????????????????
//		if (this.titaVo.getEntdd().equals("0" + this.txBuffer.getTxBizDate().getTbsDy()) || !this.titaVo.getEntdd().equals("0" + this.txBuffer.getNtxBizDate().getTbsDy())) {
//			txCom.setPreDo(0);
//			txCom.setAccDyFg(0);
//		} else {
//			txCom.setPreDo(1);
//			txCom.setAccDyFg(1);
//		}

		// ?????????
		if (this.titaVo.getObuFg().equals(ContentName.OBU))
			txCom.setbCurcd(1);
		else
			txCom.setbCurcd(0);

		// eric remark
		// ?????????????????????????????????
//		if (this.titaVo.isActfgEntry() && this.titaVo.isHcodeNormal() && this.titaVo.isDelayNormal() && !titaVo.isTrmtypTermTeller() && !this.titaVo.isOrgtlrAuto()) {
//			txCom.setRelNo(this.titaVo.getTxSeq());
//			txCom.setReldy(txCom.getTbsdyf());
//		
//		} else {
//			txCom.setRelNo(this.titaVo.getOrgTxSeq());
//			int orgdd = parse.stringToInteger(this.titaVo.getOrgdd());
//			if (orgdd == txCom.getTbsdd()) {
//				txCom.setReldy(txCom.getTbsdyf());
//			} else if (orgdd > txCom.getTbsdd()) {
//				txCom.setReldy(txCom.getLmndy());
//				txCom.setReldd(orgdd);
//			} else if (orgdd < txCom.getTbsdd()) {
//				txCom.setReldy(txCom.getTbsdy());
//				txCom.setReldd(orgdd);
//			}
//		}

		// ????????????????????????????????????
		txCom.setOrgEntdy(txCom.getTbsdyf());
		if (this.titaVo.isActfgEntry() && this.titaVo.isHcodeNormal()) {
			txCom.setOrgTxSeq(this.titaVo.getTxSeq());
			txCom.setOrgKin(this.titaVo.getKinbr());
			txCom.setOrgTlr(this.titaVo.getTlrNo());
			txCom.setOrgTno(this.titaVo.getTxtNo());
		} else {
			txCom.setOrgTxSeq(this.titaVo.getOrgTxSeq());
			txCom.setOrgKin(this.titaVo.getOrgKin());
			txCom.setOrgTlr(this.titaVo.getOrgTlr());
			txCom.setOrgTno(this.titaVo.getOrgTno());
		}

//		// ?????????????????? eric
//		txCom.setFlowEntday(txCom.getTbsdy());
//		txCom.setFlowNo(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());
//		/* ???????????? */
//		txCom.setRelKin(this.titaVo.getKinbr());
//		txCom.setRelTlr(this.titaVo.getTlrNo());
//		txCom.setRelTno(parse.stringToInteger(this.titaVo.getTxtNo()));
//		txCom.setRelNo(txCom.getFlowNo());
//
//		txCom.setReldy(txCom.getTbsdy());

		this.info(titaVo.toString());
		txCom = this.setFlowNo(txCom, txCom.getTbsdy(), this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());

		this.txBuffer.setTxCom(txCom);

		txCom = null;

		if (txTeller != null && txTeller.getReportDb() != 0 && !this.titaVo.isTxcdInq() && !this.titaVo.isFuncindInquire() && !this.titaVo.isTxcdSpecial())
			throw new LogicException("EC000", "???OnLine????????? ????????????!!");

	}

	private TxCom setFlowNo(TxCom txCom, int entday, String no) throws LogicException {

		// ?????????????????? eric
		txCom.setFlowEntday(entday);
		txCom.setFlowNo(no);

		/* ???????????? */
		txCom.setReldy(entday);

		txCom.setRelNo(no);
		txCom.setRelKin(no.substring(0, 4));
		txCom.setRelTlr(no.substring(4, 10));
		txCom.setRelTno(parse.stringToInteger(no.substring(10, 18)));

		return txCom;

	}

	private void Cs11Check() throws LogicException {
		this.info("MainProcess.Cs11Check");
		/* Tita?????? CS12-CHECK-TITA */
		this.checkTita();

		/* ???????????? */

		if (!"000000".equals(this.titaVo.getTlrNo())) {
			this.checkTeller();
		}

		/* ?????????????????? */
		if (!this.titaVo.isTxcdRim()) {
			this.checkTranCode();
		}

		if (this.titaVo.isTxcdInq()) {
			return;
		}

		/* ???????????? CS12-CHECK-XCBRTR */
//		this.checkBranch();

		/* ?????????????????? */
		if (!(this.titaVo.getActFgI() <= 1 && this.titaVo.isHcodeNormal())) {
			this.checkTranFlow();
		}

		// 2020.08.17 by eric ?????????????????? / ??????????????????
		if (this.titaVo.isHcodeSendOut() || this.titaVo.isHcodeReject()) {
			return;
		}

		/* ?????????????????? */
//		int LockCustNo = Integer.parseInt(this.titaVo.get("LockCustNo").toString());
//		long LockNo = Long.parseLong(this.titaVo.get("LockNo").toString());
//		if ((this.titaVo.getRelCodeI() <= 1 && this.titaVo.getActFgI() <= 1 && this.titaVo.isHcodeErase() && LockCustNo > 0 && LockNo > 0)
//				|| (this.titaVo.getRelCodeI() == 2 && this.titaVo.getActFgI() == 2 && this.titaVo.isHcodeErase() && this.titaVo.isHcodeErase() && LockCustNo > 0 && LockNo > 0)) {
//			lockControl.setTitaVo(this.titaVo);
//			long newLockNo = lockControl.ToLock(Integer.parseInt(this.titaVo.get("LockCustNo").toString()), this.titaVo.getTxCode(), 0L);
//			this.titaVo.put("LockNo", String.valueOf(newLockNo));
//		}

		/* FOR UPACTR CS13-SET-BOOKING */
		this.setBooking();
	}

	/**
	 * Check ENTDD and ACTFG in titaVo
	 * 
	 * @throws LogicException
	 */
	private void checkTita() throws LogicException {
		this.info("checkTita..." + this.titaVo.toString());

		/* E-Loan????????? */
		if (this.titaVo.isEloan()) {
			this.titaVo.putParam("TBSDY", this.txBuffer.getTxCom().getTbsdy());
			this.titaVo.putParam("ENTDY", this.txBuffer.getTxCom().getTbsdy());
		}

		// eric 2020.6.6
		int entdy = this.txBuffer.getTxBizDate().getTbsDy();
		if (!this.titaVo.isTxcdSpecial() && Integer.valueOf(this.titaVo.getEntDy()) != entdy) {
			throw new LogicException("CE004", "????????????(" + this.titaVo.getEntDy().trim() + ")???????????????(" + entdy + ")??????????????????????????????");
		}

		if (!(this.titaVo.isTxcdInq() || this.titaVo.isTxcdSpecial())) {
			this.titaVo.checkFlow();
		}

		if (this.titaVo.getCurName().trim().isEmpty() && this.titaVo.getCurCodeS().equals("00"))
			this.titaVo.putParam(ContentName.curnm, "TWD");
		else
			this.titaVo.putParam(ContentName.curnm, txBuffer.getMgCurr().getCurnm(titaVo.getCurCodeS()));

		if (this.titaVo.isHolidayChange(this.txBuffer.getTxBizDate().getNbsDy(), this.txBuffer.getTxBizDate().getNnbsDy()) && !this.titaVo.isEloan())
			this.mustInfo("CE000,????????????????????????????????????,?????????????????????");
//			throw new LogicException("CE000", "????????????????????????????????????,?????????????????????");

		// eric 2020.6.8 remark

//		Pattern pattern = Pattern.compile("[a-qA-Q]|[s-z\\S-Z]");
//		Matcher matcher = pattern.matcher(this.titaVo.getTxcd().substring(0, 1));

//		if (!this.titaVo.isTxcdInq() && matcher != null) {
//			while (matcher.find()) {
//				if ((this.titaVo.getRelCode().equals("0") || this.titaVo.getRelCode().equals("1")) && (this.titaVo.getActFgS().equals("0") || this.titaVo.getActFgS().equals("1")))
//					;
//				else if (this.titaVo.getRelCode().equals("2") && (this.titaVo.getActFgS().equals("5") || this.titaVo.getActFgS().equals("6")))
//					;
//				else if (this.titaVo.getRelCode().equals("3") && (this.titaVo.getActFgS().equals("1") || this.titaVo.getActFgS().equals("2") || this.titaVo.getActFgS().equals("3")
//						|| this.titaVo.getActFgS().equals("4") || this.titaVo.getActFgS().equals("9")))
//					;
//				else {
//					????????????
//					entdd = null;
//					pattern = null;
//					matcher = null;
//					throw new LogicException("CE000", "TITA ERROR RELCD=" + this.titaVo.getRelCode() + " AND IT-TITA-ACTFG=" + this.titaVo.getActFgS());
//				}
//			}

//		entdd = null;
//		pattern = null;
//		matcher = null;
	}

	/**
	 * 
	 * @throws LogicException LogicException
	 */
	private void checkTeller() throws LogicException {
		this.info("Cs20.checkTeller=" + this.titaVo.getTlrNo());
		TxCom txCom = this.txBuffer.getTxCom();

		// eric 2020.6.8
		// ??????????????????????????? ???????????????????????????
		if (!this.titaVo.isTxcdOperation()) {
			this.info("check=" + this.titaVo.getTlrNo());
			TxTeller tTxTeller = txTellerService.findById(this.titaVo.getTlrNo());

			if (tTxTeller == null && !this.titaVo.isTrmtypTermAuto()) {
				throw new LogicException("EC00!", "???????????????:" + this.titaVo.getTlrNo());
			}

			// ???????????????(???????????????????????????????????????) ????????????????????????

			// ????????????
//			if (tTxTeller.getTxtNo() > 900000 && txCom.getTbsdy() != tTxTeller.getLtxDate()) {
//				tTxTeller = txTellerService.holdById(tTxTeller);
//				tTxTeller.setTxtNo(0);
//				this.titaVo.putParam(ContentName.txtno, FormatUtil.pad9("0", 8));
//				try {
//					txTellerService.update(tTxTeller);
//				} catch (DBException e) {
//					throw new LogicException("EC004", "?????????(" + this.titaVo.getTlrNo() + ")????????????????????????,?????????????????????");
//				}
//			}

			// ??????????????????
			if (this.titaVo.isTrmtypTermAuto()) {
				tTxTeller = txTellerService.holdById(tTxTeller);
				tTxTeller.setTxtNo(tTxTeller.getTxtNo() + 1);
				try {
					txTellerService.update(tTxTeller);
					this.titaVo.putParam(ContentName.txtno, parse.IntegerToString(tTxTeller.getTxtNo(), 8));
				} catch (DBException e) {
					throw new LogicException("EC004", "?????????(" + this.titaVo.getTlrNo() + ")????????????????????????,?????????????????????");
				}
			}

			// ??????????????????
			if (!this.titaVo.isTxcdSpecial() && this.titaVo.isTrmtypTerminal()) {

				if (!this.titaVo.isTxcdInq() && Integer.parseInt(this.titaVo.getTxtNo().trim()) < tTxTeller.getTxtNo())
					throw new LogicException("EC004", "?????????(" + this.titaVo.getTlrNo() + ")??????????????????,?????????????????????");

				if (tTxTeller.getEntdy() == 0)
					tTxTeller.setEntdy(this.txBuffer.getMgBizDate().getTbsDy());

				if (this.titaVo.getEntDyI() != tTxTeller.getEntdy())
					throw new LogicException("EC004", "??????????????????????????????????????????,?????????????????????");

				if (tTxTeller.getLogonFg() != 1)
					throw new LogicException("EC007", "?????????????????????!");

			}

			txCom.setAuthNo(tTxTeller.getAuthNo());
			txCom.setTlrLevel(tTxTeller.getLevelFg());
			txCom.setTlrDept(tTxTeller.getGroupNo());

			this.txBuffer.setTxCom(txCom);

			txCom = null;
		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @throws LogicException LogicException
	 */
	private void checkTranCode() throws LogicException {
		this.info("checkTranCode...");

		TxCom txCom = this.txBuffer.getTxCom();

		/* ???????????? */
		TxTranCode tTxTranCode = txTranCodeService.findById(this.titaVo.getTxCode());

		if (this.titaVo.isTxcdSpecial()) {
			txCom.setCanCancel(0);
			txCom.setCanModify(0);
			txCom.setSubmitFg(0);
			txCom.setCustRmkFg(0);
		} else {
			if (tTxTranCode == null)
				throw new LogicException("EC001", "???????????????(TxTranCode):" + this.titaVo.getTxCode());

			if (tTxTranCode.getStatus() == 1)
				throw new LogicException("EC008", "???????????? " + this.titaVo.getTxCode() + " ?????????");

			if (this.titaVo.isTxcdInq()) {
				txCom.setCanCancel(0);
				txCom.setCanModify(0);
				txCom.setSubmitFg(0);
			} else {
				txCom.setCanCancel(tTxTranCode.getCancelFg());
				txCom.setCanModify(tTxTranCode.getModifyFg());
				txCom.setSubmitFg(tTxTranCode.getSubmitFg());

				int funcode = Integer.parseInt(this.titaVo.getFuncind());
				if (!checkAuth.isCan(this.titaVo, this.titaVo.getTlrNo(), this.titaVo.getAgent(), this.titaVo.getAuthNo(), this.titaVo.getTxCode(), this.titaVo.getActFgI(), funcode)) {
					throw new LogicException("EC008", "?????? [" + this.titaVo.getTlrNo() + "] ????????? [" + this.titaVo.getTxCode() + "] ????????????");
				}
			}
			// 2021.10.13 by eric
			txCom.setCustRmkFg(tTxTranCode.getCustRmkFg());
		}

//		if (txCom.isTxTypeInq() && tTxTranCode != null && tTxTranCode.getCustDataCtrlFg() == 1 && this.titaVo.getReason().isEmpty()) {
		if (txCom.isTxTypeInq() && tTxTranCode != null && tTxTranCode.getCustDataCtrlFg() == 1) {
			CustDataCtrl tCustDataCtrl = null;
			if (titaVo.getMrKey().length() >= 7 && parse.isNumeric(titaVo.getMrKey().substring(0, 7))) {
				tCustDataCtrl = sCustDataCtrlService.findById(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
//				if (tCustDataCtrl != null && "Y".equals(tCustDataCtrl.getEnable()))
//					sendRsp.addvReason(this.txBuffer, titaVo, "0004", "?????????????????????????????????");
//
			} else {
				if (titaVo.get("CustId") != null) {
					CustMain custMain = custMainService.custIdFirst(titaVo.get("CustId").trim(), titaVo);
					if (custMain != null) {
						tCustDataCtrl = sCustDataCtrlService.findById(custMain.getCustNo());
//						if (tCustDataCtrl != null && "Y".equals(tCustDataCtrl.getEnable()))
//							sendRsp.addvReason(this.txBuffer, titaVo, "0004", "?????????????????????????????????");
					}
				}
			}
			if (tCustDataCtrl != null) {
				if (tCustDataCtrl.getApplMark() == 2) {
					if (titaVo.getReason().isEmpty()) {
						throw new LogicException("EC998", "?????????????????????????????????");
					} else if (titaVo.getEmpNos().trim().isEmpty() && tTxTranCode.getCustDataCtrlFg() != 1) {
						sendRsp.addvReason(this.txBuffer, titaVo, "0004", "?????????????????????????????????,??????:" + titaVo.getReason());
					}
				} else if (tCustDataCtrl.getApplMark() == 1) {
//					if ("L3".equals(this.titaVo.getTxcd().substring(0, 2))) {
//						txCom.setCustDataCtrl(1);
//					} else {
//						throw new LogicException("E0015", "?????????????????????????????????");
//					}
				}
			}
		}

		/*  */
		if (titaVo.isHcodeErase() && txCom.getCanCancel() == 0) {
			throw new LogicException("EC008", "?????????????????????");
		}
		if (titaVo.isHcodeModify() && txCom.getCanModify() == 0) {
			throw new LogicException("EC008", "?????????????????????");
		}

		this.txBuffer.setTxCom(txCom);
		txCom = null;
	}

	private void checkTranFlow() throws LogicException {
		this.info("checkTranFlow...");

		TxRecordId tTxRecordId = new TxRecordId();

		tTxRecordId.setEntdy(this.titaVo.getOrgEntdyI());
		tTxRecordId.setTxNo(this.titaVo.getOrgTxSeq());

		TxRecord txRecord = txRecordService.holdById(tTxRecordId);
		if (txRecord == null) {
			throw new LogicException("EC001", "??????????????????:" + tTxRecordId.getEntdy() + "-" + tTxRecordId.getTxNo());
		}

		if (txRecord.getActionFg() == 1) {
			throw new LogicException("EC004", "??????????????????:" + tTxRecordId.getEntdy() + "-" + tTxRecordId.getTxNo() + ",?????????");
		} else if (txRecord.getActionFg() == 2) {
			throw new LogicException("EC004", "??????????????????:" + tTxRecordId.getEntdy() + "-" + tTxRecordId.getTxNo() + ",?????????");
		} else if (txRecord.getActionFg() == 3) {
			throw new LogicException("EC004", "??????????????????:" + tTxRecordId.getEntdy() + "-" + tTxRecordId.getTxNo() + ",?????????");
		}

		if (this.titaVo.isHcodeModify() || this.titaVo.isHcodeErase()) {
			txRecord.setActionFg(this.titaVo.getHCodeI());
			if (this.titaVo.isHcodeErase() && this.titaVo.getOrgEntdyI() != this.titaVo.getEntDyI())
				txRecord.setActionFg(3); // ?????????
		}
//		txRecord.setOrgEntdy(this.titaVo.getOrgEntdyI());
//		txRecord.setOrgTxNo(this.titaVo.getOrgTxSeq());

		try {
			txRecordService.update(txRecord);
		} catch (DBException e) {
			throw new LogicException("EC003", "??????????????????:" + tTxRecordId.getEntdy() + "-" + tTxRecordId.getTxNo());
		}

		TxCom txCom = this.txBuffer.getTxCom();

		txCom = this.setFlowNo(txCom, txRecord.getEntdy(), txRecord.getFlowNo());

		txCom.setAcCnt(txRecord.getAcCnt());

		if (this.titaVo.getRelCodeI() > 1) {
			txCom = checkTxFlow(txCom);
		}

		this.txBuffer.setTxCom(txCom);

//		this.txBuffer.getTxCom().setFlowNo(txRecord.getFlowNo());

	}

	private TxCom checkTxFlow(TxCom txCom) throws LogicException {
		TxFlowId txFlowId = new TxFlowId();

		txFlowId.setEntdy(txCom.getFlowEntday());
		txFlowId.setFlowNo(txCom.getFlowNo());

		TxFlow txFlow = txFlowService.findById(txFlowId);

		if (txFlow == null) {
			throw new LogicException("EC001", "?????????????????????(TxFlow):" + txCom.getFlowEntday() + "-" + txCom.getFlowNo());
		}

		if (this.titaVo.getActFgI() <= 1 && (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify())) {

		} else if (this.titaVo.isHcodeSendOut() && txFlow.getFlowMode() != 3) {
			throw new LogicException("EC004", "?????????????????????????????????????????????");
		} else if (this.titaVo.isHcodeReject() && txFlow.getFlowMode() != 1) {
			throw new LogicException("EC004", "???????????????????????????????????????");
		} else if (this.titaVo.getRelCodeI() != txFlow.getFlowType()) {
			throw new LogicException("EC005", "??????????????????????????????(TxFlow)????????????????????????:" + this.titaVo.getRelCodeI() + " / " + txFlow.getFlowType());
		} else if (this.titaVo.isHcodeNormal() && this.titaVo.getActFgI() != txFlow.getFlowStep() + 1) {
			throw new LogicException("EC004", "????????????(" + txCom.getFlowEntday() + "-" + txCom.getFlowNo() + "),???" + getFlowStepDesc(txFlow.getFlowStep()) + "(" + txFlow.getFlowStep() + ")??????");
		} else if (this.titaVo.isHcodeModify() && this.titaVo.getActFgI() != txFlow.getFlowStep()) {
			throw new LogicException("EC004", "????????????(" + txCom.getFlowEntday() + "-" + txCom.getFlowNo() + "),???" + getFlowStepDesc(txFlow.getFlowStep()) + "(" + txFlow.getFlowStep() + ")??????");
		} else if (this.titaVo.isHcodeErase() && this.titaVo.getActFgI() != txFlow.getFlowStep()) {
			throw new LogicException("EC004", "????????????(" + txCom.getFlowEntday() + "-" + txCom.getFlowNo() + "),???" + getFlowStepDesc(txFlow.getFlowStep()) + "(" + txFlow.getFlowStep() + ")??????");

		}

//		this.titaVo.put("LockNo", String.valueOf(txFlow.getLockNo()));
//		this.titaVo.put("LockCustNo", String.valueOf(txFlow.getLockCustNo()));

		txCom.setAcCnt(txFlow.getAcCnt());

		return txCom;
	}

	private String getFlowStepDesc(int flowstep) {

		switch (flowstep) {
		case 1:
			return "??????";
		case 2:
			return "??????";
		case 3:
			return "??????";
		case 4:
			return "????????????";
		}
		return "";
	}

	private void setBooking() {
		this.info("setBooking....");

		TxCom txCom = this.txBuffer.getTxCom();

		if (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify()) {
			switch (this.titaVo.getRelCode()) {
			case "0":
			case "1":
				txCom.setBookAc(0);
				break;
			case "2":
				if (this.titaVo.getActFgI() == 1) {
					txCom.setBookAc(1);
				} else {
					txCom.setBookAc(2);
				}
				break;
			default:
				txCom.setBookAc(3);

			}

		} else if (this.titaVo.isHcodeErase()) {
			txCom.setBookAc(2);
		}

		this.txBuffer.setTxCom(txCom);
		txCom = null;
	}

	private void doAcEnterCom() throws LogicException {
		this.info("CS10 doAcEnterCom....");
		TxCom txCom = this.txBuffer.getTxCom();

		if (this.titaVo.isHcodeModify() && !this.titaVo.isTxcdInq() && txCom.getAcCnt() > 0) {
			txCom.setBookAcHcode(1);
			this.acEnterCom.setTxBuffer(this.txBuffer);
			this.acEnterCom.run(this.titaVo);
			this.txBuffer.setTxCom(txCom);
			txCom = null;
		}
	}

	public TitaVo getTitaVo() {
		return titaVo;
	}

	public void setTitaVo(TitaVo titaVo) {
		this.titaVo = titaVo;
	}

	public int getEcCount() {
		return EcCount;
	}

	public void setEcCount(int ecCount) {
		EcCount = ecCount;
	}

	public TxBuffer getTxBuffer() {
		return txBuffer;
	}

	public void setTxBuffer(TxBuffer txBuffer) {
		this.txBuffer = txBuffer;
	}

	public TotaVoList getTotaVoList() {
		return totaVoList;
	}

	public void setTotaVoList(TotaVoList totaVoList) {
		this.totaVoList = totaVoList;
	}
}