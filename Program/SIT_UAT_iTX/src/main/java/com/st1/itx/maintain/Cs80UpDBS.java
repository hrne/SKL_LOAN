package com.st1.itx.maintain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.TxRecordService;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.AcEnterCom;
import com.st1.itx.util.common.LockControl;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.CustRmkCom;
import com.st1.itx.dataVO.TotaVo;

@Component("cs80UpDBS")
@Scope("prototype")
public class Cs80UpDBS extends CommBuffer {
	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public TxFlowService txFlowService;

	@Autowired
	public TxRecordService txRecordService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public LockControl lockControl;

	@Autowired
	public AcEnterCom acEnterCom;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public TxBatchCom txBatchCom;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	CustRmkCom custRmkCom;

	@Autowired
	public Parse parse;

	private int AcCnt = 0;

	@Override
	public void exec() throws LogicException {
		this.info("CS80....");

		if (!this.titaVo.isTxcdInq() && !this.titaVo.isHcodeErase() && !this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject() && this.txBuffer.getAcDetailList() != null) {
			if (this.titaVo.getActFgI() <= 1) {
				this.AcCnt = this.txBuffer.getAcDetailList().size();
			} else {
				this.AcCnt = this.txBuffer.getTxCom().getAcCnt();
			}

		}

		// 鎖定戶號處理
		if (!(this.titaVo.isTxcdInq() || this.titaVo.isTxcdSpecial())) {

			if (this.titaVo.isHcodeSendOut() || this.titaVo.isHcodeReject()) {
				TotaVo nulltota = new TotaVo();
				nulltota.init(titaVo);
				nulltota.setMsgId("LC999");
				this.totaVoList.add(nulltota);
			}

			TotaVo tota = this.totaVoList.get(0);

			if (!this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject()) {

				int LockCustNo = 0;
				Long LockNo = 0L;

				try {
					LockCustNo = Integer.valueOf(titaVo.getParam("LockCustNo"));
					LockNo = Long.valueOf(titaVo.getParam("LockNo").toString());
				} catch (Throwable e) {

				}
				// 銷定戶號自動解鑜控制
				if (!tota.isError() && LockNo > 0) {
					if ((titaVo.getRelCodeI() <= 1 && titaVo.getActFgI() <= 1 && (titaVo.isHcodeNormal() || titaVo.isHcodeErase()))
							|| (titaVo.getRelCodeI() == 2 && titaVo.getActFgI() == 1 && titaVo.isHcodeErase()) || (titaVo.getRelCodeI() == 2 && titaVo.getActFgI() == 2 && titaVo.isHcodeNormal())) {

						this.info("CS80 .... LockControl.ToUnLock");

						boolean mustfg = false;
						if (this.titaVo.isHcodeNormal()) {
							mustfg = true;
						}
						lockControl.setTitaVo(this.titaVo);
						lockControl.ToUnLock(LockNo, LockCustNo, mustfg, false);
//						lockControl.ToUnLock(this.titaVo, LockNo, LockCustNo, mustfg, false);
					} else if (titaVo.getRelCodeI() > 2 && Long.valueOf(titaVo.getParam("LockNo").toString()) > 0) {
						throw new LogicException("EC005", "銷定戶號序號有值:(" + titaVo.getParam("LockNo").toString());
					}
				}
			}

			if (!this.titaVo.isTrmtypBatch()) {
				this.updTxTeller();
			}

			if (!tota.isError()) {
				this.mntTxFlow();
			}

			// 2021.10.13 by eric
			this.info("CS80 before custRmk = " + this.titaVo.isTrmtypBatch() + "/" + tota.isError() + "/" + titaVo.getReturnIndex());
			if (this.txBuffer.getTxCom().getCustRmkFg() == 1 && !this.titaVo.isTrmtypBatch() && !tota.isError() && titaVo.getReturnIndex() == 0) {
				this.custRmk();
			}

			if (!(this.titaVo.isTrmtypBatch() && this.titaVo.isHcodeErase()) && !this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject()) {
				this.insTxRecord(tota);
			}

		}

		if (this.totaVoList.size() > 0 && this.totaVoList.get(0).isError()) {
			TxCom txCom = this.txBuffer.getTxCom();
			txCom.setTxRsut(1);
			txCom.setMsgId(this.totaVoList.get(0).getMsgId());
			txCom.setErrorMsg((String) this.totaVoList.get(0).get("ERRMSG"));
			this.txBuffer.setTxCom(txCom);
		}

		if (!this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject()) {
			this.txBatchCom.setTxBuffer(this.getTxBuffer());
			this.txBatchCom.run(this.titaVo);
		}

		if (this.txBuffer.getAmlList().size() > 0) {
			this.txAmlCom.setTxBuffer(this.txBuffer);
			this.txAmlCom.nameCheckInsert(titaVo);
		}

	}

	// 2021.10.13 by eric
	private void custRmk() throws LogicException {
		this.info("CS80 custRmk = " + this.titaVo.get("CustNo") + "/" + this.titaVo.get("CustId"));
		int custNo9 = 0;
		if (titaVo.getMrKey().trim().length() >= 7 && parse.isNumeric(titaVo.getMrKey().trim().substring(0, 7))) {
			custNo9 = parse.stringToInteger(titaVo.getMrKey().substring(0, 7));
		} else {
			String custId = this.titaVo.get("CustId");
			if (custId != null) {
				CustMain custMain = custMainService.custIdFirst(custId, titaVo);
				if (custMain != null) {
					custNo9 = custMain.getCustNo();
				}
			}
		}

		if (custNo9 > 0) {
			this.totaVoList.addAll(custRmkCom.getCustRmk(titaVo, custNo9));
		}
	}

	private void updTxTeller() throws LogicException {
		this.info("CS80 updTxTeller....");
		TxTeller tTxTeller = txTellerService.holdById(this.titaVo.getTlrNo());

		tTxTeller.setTxtNo(Integer.valueOf(this.titaVo.getTxtNo()));
		tTxTeller.setLtxDate(Integer.valueOf(this.titaVo.getCalDy()));
		tTxTeller.setLtxTime(Integer.valueOf(this.titaVo.getCalTm()));
		try {
			txTellerService.update(tTxTeller);
		} catch (DBException e) {
			throw new LogicException("EC003", "櫃員檔(TxTeller):" + e.getErrorMsg());
		}
	}

	private void mntTxFlow() throws LogicException {
		this.info("CS80 mntTxFlow : " + this.titaVo.getRelCode() + "/" + this.titaVo.getActFgS() + "/" + this.titaVo.getEntDyI() + "/" + this.titaVo.getHCode() + "/"
				+ this.txBuffer.getTxCom().getFlowNo());

		this.titaVo.checkFlow();

		TxFlowId tTxFlowId = new TxFlowId();
		tTxFlowId.setEntdy(this.titaVo.getEntDyI());
		tTxFlowId.setFlowNo(this.txBuffer.getTxCom().getFlowNo());

		TxFlow tTxFlow = txFlowService.holdById(tTxFlowId);

		// 送出放行
		if (this.titaVo.isHcodeSendOut()) {
			if (tTxFlow == null) {
				throw new LogicException("EC001", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo());
			} else if (tTxFlow.getFlowMode() != 3) {
				throw new LogicException("EC004", "本筆資料為非「待送出放行」狀態");
			}
			tTxFlow.setFlowMode(1);
			dbsTxFlow(2, tTxFlow);
			return;
		}

		// 主管退回
		if (this.titaVo.isHcodeReject()) {
			if (tTxFlow == null) {
				throw new LogicException("EC001", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo());
			} else if (tTxFlow.getFlowMode() != 1) {
				throw new LogicException("EC004", "本筆資料為非「待放行」狀態");
			}
			tTxFlow.setFlowMode(3);
			tTxFlow.setRejectReason(this.titaVo.get("Reject"));
			dbsTxFlow(2, tTxFlow);
			return;
		}

		int mntfg = 0;
		if (this.titaVo.getRelCodeI() > 1 && this.titaVo.isActfgEntry() && (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify())) {
			if (tTxFlow == null) {
				mntfg = 1;
				tTxFlow = new TxFlow();
				tTxFlow.setTxFlowId(tTxFlowId);
				tTxFlow.setTranNo(this.titaVo.getTxCode());

				tTxFlow.setLockNo(Long.valueOf(this.titaVo.get("LockNo").toString()));
				tTxFlow.setLockCustNo(Integer.parseInt(this.titaVo.get("LockCustNo").toString()));

			} else {
				mntfg = 2;
			}
		} else if (this.titaVo.getRelCodeI() > 1) {
			if (tTxFlow == null) {
				throw new LogicException("EC001", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo());
			}
			mntfg = 2;
		} else {
			if (tTxFlow != null) {
				try {
					txFlowService.delete(tTxFlow);
				} catch (DBException e) {
					throw new LogicException("EC006", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo() + "/" + e.getErrorMsg());
				}
			}
			return;
		}

		if (this.titaVo.getActFgI() == 1 && (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify())) {
			tTxFlow.setFlowType(this.titaVo.getRelCodeI());

			if (this.titaVo.getRelCodeI() > 2) {
				if (this.getTxBuffer().getTxCom().getConfirmBrNo() == null || "".equals(this.getTxBuffer().getTxCom().getConfirmBrNo())) {
					throw new LogicException("EC005", this.titaVo.getRelCodeI() + "段式交易,需指定審核單位(txCom.ConfirmBrNo)");
				}
				if (this.getTxBuffer().getTxCom().getConfirmGroupNo() == null || "".equals(this.getTxBuffer().getTxCom().getConfirmGroupNo())) {
					throw new LogicException("EC005", this.titaVo.getRelCodeI() + "段式交易,需指定審核科組別((txCom.ConfirmGroupNo))");
				}
				tTxFlow.setConBrNo(this.getTxBuffer().getTxCom().getConfirmBrNo());
				tTxFlow.setConGroupNo(this.getTxBuffer().getTxCom().getConfirmGroupNo());
			}
		}

		if (this.titaVo.isHcodeErase()) {
			tTxFlow.setFlowStep(this.titaVo.getActFgI() - 1);
		} else {
			tTxFlow.setSecNo(this.titaVo.getSecNo());
			tTxFlow.setBookAc(this.getTxBuffer().getTxCom().getBookAc());
			tTxFlow.setFlowStep(this.titaVo.getActFgI());
		}

		// eric 2020.08.17 1 > 3
		if (tTxFlow.getFlowStep() == 1 && tTxFlow.getFlowType() == 2 && this.txBuffer.getTxCom().getSubmitFg() == 1) {
			tTxFlow.setFlowMode(3);
		} else if (tTxFlow.getFlowStep() == 1 || tTxFlow.getFlowStep() == 3) {
			tTxFlow.setFlowMode(1);
		} else if (tTxFlow.getFlowStep() == 1 && tTxFlow.getFlowType() == 3) {
			tTxFlow.setFlowMode(2);
		} else if (tTxFlow.getFlowStep() == 2 && tTxFlow.getFlowType() == 4) {
			tTxFlow.setFlowMode(2);
		} else {
			tTxFlow.setFlowMode(0);
		}

		switch (this.titaVo.getActFgI()) {
		case 1:
			if (this.titaVo.isHcodeErase()) {
				tTxFlow.setTxNo1("");
				tTxFlow.setAcCnt(this.AcCnt);
			} else {
				tTxFlow.setTxNo1(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());
				tTxFlow.setBrNo(this.titaVo.getKinbr());
				tTxFlow.setGroupNo(this.getTxBuffer().getTxCom().getTlrDept());
				tTxFlow.setFlowBrNo(tTxFlow.getBrNo());
				tTxFlow.setFlowGroupNo(tTxFlow.getGroupNo());
				tTxFlow.setAcCnt(this.AcCnt);
			}
			break;
		case 2:
			if (this.titaVo.isHcodeErase()) {
				tTxFlow.setTxNo2("");
				tTxFlow.setFlowBrNo(tTxFlow.getBrNo());
				tTxFlow.setFlowGroupNo(tTxFlow.getGroupNo());
				if (this.titaVo.getRelCodeI() == 2) {
					tTxFlow.setLockNo(Long.valueOf(this.titaVo.getParam("LockNo")));
				}
			} else {
				tTxFlow.setTxNo2(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());
				if (this.titaVo.getRelCodeI() > 2) {
					tTxFlow.setFlowBrNo(tTxFlow.getConBrNo());
					tTxFlow.setFlowGroupNo(tTxFlow.getConGroupNo());
				}
			}
			break;
		case 3:
			if (this.titaVo.isHcodeErase()) {
				tTxFlow.setTxNo3("");
				tTxFlow.setFlowBrNo(tTxFlow.getBrNo());
				tTxFlow.setFlowGroupNo(tTxFlow.getGroupNo());
			} else {
				tTxFlow.setTxNo3(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());
			}
			break;
		case 4:
			if (this.titaVo.isHcodeErase()) {
				tTxFlow.setTxNo4("");
			} else {
				tTxFlow.setTxNo4(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());
			}
			break;
		}

		dbsTxFlow(mntfg, tTxFlow);

	}

	private void dbsTxFlow(int mntfg, TxFlow tTxFlow) throws LogicException {
		if (mntfg == 1) {
			try {
				tTxFlow.setSubmitFg(this.txBuffer.getTxCom().getSubmitFg());
				txFlowService.insert(tTxFlow);
			} catch (DBException e) {
				throw new LogicException("EC002", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo() + "/" + e.getErrorMsg());
			}
		} else if (mntfg == 2) {
			try {
				txFlowService.update(tTxFlow);
			} catch (DBException e) {
				throw new LogicException("EC003", "交易流程控制檔(TxFlow):" + this.titaVo.getEntDy() + "-" + this.txBuffer.getTxCom().getFlowNo() + "/" + e.getErrorMsg());
			}
		}

	}

	private void insTxRecord(TotaVo tota) throws LogicException {
		this.info("CS80 insTxRecord : " + this.titaVo.getEntDy() + "/" + this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());

		TxRecordId txRecordId = new TxRecordId();
		txRecordId.setEntdy(this.titaVo.getEntDyI());
		txRecordId.setTxNo(this.titaVo.getKinbr() + this.titaVo.getTlrNo() + this.titaVo.getTxtNo());

		boolean newfg = true;

		TxRecord txRecord = new TxRecord();

		if (this.titaVo.isTrmtypBatch()) {
			txRecord = txRecordService.holdById(txRecordId);
			if (txRecord == null) {
				txRecord = new TxRecord();
			} else {
				newfg = false;
			}
		}

		txRecord.setTxRecordId(txRecordId);
		txRecord.setBrNo(this.titaVo.getKinbr());
		txRecord.setGroupNo(this.getTxBuffer().getTxCom().getTlrDept());
		txRecord.setTlrNo(this.titaVo.getTlrNo());
		txRecord.setTxSeq(this.titaVo.getTxtNo());
		txRecord.setTranNo(this.titaVo.getTxCode());
		txRecord.setTrmType(this.titaVo.getTrmTyp());
		txRecord.setMrKey(this.titaVo.getMrKey());
		txRecord.setBookAc(this.txBuffer.getTxCom().getBookAc());
		txRecord.setAcCnt(this.AcCnt);
		txRecord.setSecNo(this.titaVo.getSecNo());
		txRecord.setDeCr(this.titaVo.getCrdb());
		txRecord.setCurCode(this.titaVo.getCurCodeI());
		txRecord.setCurName(this.titaVo.getCurName());
		txRecord.setTxAmt(this.parse.stringToBigDecimal(this.titaVo.getTxAmt()));
		txRecord.setLockNo(Long.valueOf(this.titaVo.get("LockNo").toString()));
		txRecord.setLockCustNo(Integer.parseInt(this.titaVo.get("LockCustNo").toString()));
		if (this.titaVo.isHcodeErase()) {
			txRecord.setSupNo(this.titaVo.getSupCode());
		} else {
			txRecord.setSupNo(this.titaVo.getEmpNos());
		}

		txRecord.setHcode(this.titaVo.getHCodeI());
		txRecord.setCalDate(this.parse.stringToInteger(this.titaVo.getCalDy()) + 19110000);
		txRecord.setCalTime(this.parse.stringToInteger(titaVo.getCalTm()));

		// eric 20200702
		if (this.titaVo.isTrmtypBatch() && this.titaVo.isHcodeNormal()) {
			txRecord.setActionFg(0);
		}
		// lai 20200922
		txRecord.setOrgEntdy(this.titaVo.getOrgEntdyI());
		txRecord.setOrgTxNo(this.titaVo.getOrgTxSeq());

//		TotaVo tota = this.totaVoList.get(0);
		txRecord.setMsgId(tota.getMsgId());
		if (tota.isError()) {
			// 交易失敗
			txRecord.setTxResult("E");
			txRecord.setErrMsg(tota.get("ERRMSG").toString());
		} else {
			txRecord.setTxResult("S");
			txRecord.setErrMsg("");
		}

		txRecord.setFlowNo(this.txBuffer.getTxCom().getFlowNo());
		txRecord.setFlowType(this.titaVo.getRelCodeI());
		txRecord.setFlowStep(this.titaVo.getActFgI());
		txRecord.setCanCancel(this.txBuffer.getTxCom().getCanCancel());
//		txRecord.setCanModify(this.txBuffer.getTxCom().getCanModify());
		if (titaVo.getActFgI() == 1)
			txRecord.setCanModify(this.txBuffer.getTxCom().getCanModify());
		else
			txRecord.setCanModify(0);

		if (!this.titaVo.getReason().isEmpty())
			txRecord.setImportFg("1");

		try {
			/* E-Loan 完整電文 */
			if (this.titaVo.isEloan())
				txRecord.setTranData(this.titaVo.getOrgTitaVO());
			else
				txRecord.setTranData(new ObjectMapper().writeValueAsString(this.titaVo));

			if (newfg) {
				txRecordService.insert(txRecord);
			} else {
				txRecordService.update(txRecord);
			}
		} catch (DBException e) {
			if (newfg) {
				throw new LogicException("EC002", "交易記錄檔(TxRecord):" + e.getErrorMsg());
			} else {
				throw new LogicException("EC003", "交易記錄檔(TxRecord)" + txRecordId.getEntdy() + "-" + txRecordId.getTxNo() + ":" + e.getErrorMsg());
			}
		} catch (JsonProcessingException e) {
			throw new LogicException("EC009", "MtRecord titaVo 轉換失敗");
		}
	}

}
