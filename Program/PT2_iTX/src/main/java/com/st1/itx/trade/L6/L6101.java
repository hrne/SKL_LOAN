package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.trade.L9.L9130;
import com.st1.itx.trade.L9.L9130Report;
import com.st1.itx.trade.L9.L9131;
import com.st1.itx.trade.L9.L9131Report;
import com.st1.itx.trade.L9.L9132;
import com.st1.itx.trade.L9.L9132Report;
import com.st1.itx.trade.L9.L9132ReportA;
import com.st1.itx.trade.L9.L9132ReportB;
import com.st1.itx.trade.L9.L9132ReportC;
import com.st1.itx.trade.L9.L9132ReportD;
import com.st1.itx.trade.L9.L9133;
import com.st1.itx.trade.L9.L9133Report;
import com.st1.itx.trade.L9.L9134Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.TxToDoCom;
//import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita SecNo=9,2 ClsFg=9,1 BatNo=9,2 ClsNo=9,2 CoreSeqNo=9,3 END=X,1
 */

@Service("L6101")
@Scope("prototype")
/**
 * 業務關帳作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6101 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	AcCloseService sAcCloseService;

	@Autowired
	TxToDoMainService sTxToDoMainService;

	@Autowired
	BatxHeadService sBatxHeadService;

	@Autowired
	TxFlowService sTxFlowService;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	BankRemitService bankRemitService;

	@Autowired
	AcReceivableService sAcReceivableService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	L9130 tranL9130;

	@Autowired
	L9131 tranL9131;

	@Autowired
	L9132 tranL9132;

	@Autowired
	L9133 tranL9133;
	
	@Autowired
	L9130Report l9130Report;

	@Autowired
	L9131Report l9131Report;

	@Autowired
	L9132Report l9132Report;
	
	@Autowired
	L9132ReportA l9132ReportA;
	
	@Autowired
	L9132ReportB l9132ReportB;
	
	@Autowired
	L9132ReportC l9132ReportC;
	
	@Autowired
	L9132ReportD l9132ReportD;

	@Autowired
	L9133Report l9133Report;
	
	@Autowired
	L9134Report l9134Report;
	


	@Autowired
	L6101Excel l6101Excel;

	@Autowired
	TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6101 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iSecNo = titaVo.getParam("SecNo");
		int iClsFg = this.parse.stringToInteger(titaVo.getParam("ClsFg"));
		int iMsgCode = 0;
		int iClsNo = 0;

		// 檢查關帳作業
		iMsgCode = CheckAcClose(iSecNo, iClsFg, iMsgCode, titaVo);
		this.info("L6101 Check iMsgCode : " + iMsgCode);

		// 更新會計業務關帳控制檔
		if (iMsgCode == 0) {
			iClsNo = updAcClose(iSecNo, iClsFg, iClsNo, titaVo);
			this.info("L6101 after updAcClose iClsNo : " + iClsNo);
		}

		// 更新上傳核心序號(09:放款)
		// 啟動 L9130核心傳票媒體檔產生作業 ; L9131核心日結單代傳票列印 ; L9132傳票媒體明細表(核心) ; L9133會計與主檔餘額檢核表
		if (iMsgCode == 0 && (iClsFg == 1 || iClsFg == 2)) {
			if ("02".equals(iSecNo) || "09".equals(iSecNo)) {
				updCoreSeq(iSecNo, iClsFg, iClsNo, titaVo);
			}
		}

		// 寫入應處理清單-業績工作月結算啟動通知
		// 放款關帳、工作月結束
		if (iMsgCode == 0 && iClsFg == 1 && "09".equals(iSecNo)) {
			txToDoPFCL(titaVo);
		}

		// 月底日關帳啟動提存
		if (iMsgCode == 0 && (iClsFg == 1 || iClsFg == 2) && "09".equals(iSecNo)) {
			if (this.txBuffer.getMgBizDate().getTbsDy() == this.txBuffer.getMgBizDate().getMfbsDy()) {
				// 應收利息提存
				MySpring.newTask("BS900", this.txBuffer, titaVo);
				// 應付未付火險費提存
				MySpring.newTask("BS901", this.txBuffer, titaVo);
			}
		}

		// 檢查正常
		if (iMsgCode == 0) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOMsgCode", 00);

			// 0.開帳 1.關帳 2.關帳取消
			if (iClsFg == 0) {
				occursList.putParam("OOMessage", "開帳作業完成");
			} else if (iClsFg == 1) {
				occursList.putParam("OOMessage", "關帳作業完成");
			} else {
				occursList.putParam("OOMessage", "關帳取消作業完成");
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		// 檢查錯誤
		if (iMsgCode != 0) {
			this.totaVo.putParam("OOResult", "9999");
			this.totaVo.putParam("OOBatNo09", 00);
			this.totaVo.putParam("OOBatNo01", 00);
			this.totaVo.putParam("OOCoreSeqNo", 000);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 檢查關帳作業
	private int CheckAcClose(String cSecNo, int cClsFg, int cMsgCode, TitaVo titaVo) throws LogicException {

		int clsFg = 0;
		int unProcessCnt = 0;
		int batxTotCnt = 0;
		int txFlowCnt = 0;
		int acReceivableCnt = 0;

		switch (cSecNo) {
		case "02": // 2-支票繳款

			// MsgCode=06 檢查是否有未放行交易
			if (cClsFg == 1) {
				txFlowCnt = findTxFlow(titaVo.getParam("SecNo"), txFlowCnt, titaVo);
				if (txFlowCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			this.info("L6101 case 2 : " + cMsgCode);
			break;
		case "09": // 09-放款
			// MsgCode=01 撥款匯款作業未完成
			String batchNo = "LN" + titaVo.getParam("BatNo") + "  ";
			Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(this.txBuffer.getTxCom().getTbsdyf(), batchNo,
					00, 99, 0, 0, 0, Integer.MAX_VALUE, titaVo);
			if (slBankRemit != null) {
				cMsgCode = cMsgCode + 1;
				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage", "撥款退款作業(撥款)未完成");
				this.totaVo.addOccursList(occursList);
			}
			slBankRemit = null;
			batchNo = "RT" + titaVo.getParam("BatNo") + "  ";
			slBankRemit = bankRemitService.findL4901B(this.txBuffer.getTxCom().getTbsdyf(), batchNo, 00, 99, 0, 0, 0,
					Integer.MAX_VALUE, titaVo);
			if (slBankRemit != null) {
				cMsgCode = cMsgCode + 1;
				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage", "撥款退款作業(退款)未完成");
				this.totaVo.addOccursList(occursList);
			}
			// MsgCode=02 檢查支票繳款業務必須先關帳
			if (cClsFg == 1) {
				clsFg = findAcClose("02", clsFg, titaVo);
				if (clsFg != 1) {
					cMsgCode = cMsgCode + 1;
					// 將訊息放入Tota
					OccursList occursList = new OccursList();
					occursList.putParam("OOMsgCode", "錯誤");
					occursList.putParam("OOMessage", "支票繳款業務未關帳");
					this.totaVo.addOccursList(occursList);
				}
			}

			// MsgCode=04 檢查是否有應處理清單未完成
			if (cClsFg == 1) {
				unProcessCnt = findTxToDoMain(unProcessCnt, titaVo);
				if (unProcessCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			// MsgCode=05 檢查所有戶號之整批入帳是否完成
			if (cClsFg == 1) {
				batxTotCnt = findBatxHead(batxTotCnt, titaVo);
				if (batxTotCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			// MsgCode=06 檢查是否有未放行交易
			if (cClsFg == 1) {
				txFlowCnt = findTxFlow(titaVo.getParam("SecNo"), txFlowCnt, titaVo);
				if (txFlowCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			// MsgCode=06 檢查是否有未放行交易 , 其他由9-放款檢查
			txFlowCnt = 0;
			if (cClsFg == 1) {
				txFlowCnt = findTxFlow("  ", txFlowCnt, titaVo);
				if (txFlowCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			// MsgCode=06 檢查是否有未放行交易 , 其他由9-放款檢查
			txFlowCnt = 0;
			if (cClsFg == 1) {
				txFlowCnt = findTxFlow("  ", txFlowCnt, titaVo);
				if (txFlowCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			// MsgCode=07 檢查會計銷帳檔是否有應銷未銷資料
			if (cClsFg == 1) {
				acReceivableCnt = findAcReceivable(acReceivableCnt, titaVo);
				if (acReceivableCnt > 0) {
					cMsgCode = cMsgCode + 1;
				}
			}

			this.info("L6101 case 9 : " + cMsgCode);
			break;
		}

		this.info("L6101 CheckAcClose cMsgCode : " + cMsgCode);
		return cMsgCode;
	}

	// 讀取會計業務關帳控制檔
	private int findAcClose(String fSecNo, int fClsFg, TitaVo titaVo) throws LogicException {

		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo(fSecNo); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款

		tAcClose = sAcCloseService.findById(tAcCloseId);

		// 當日該業務有帳務產生時自動鍵一筆 , 找不到時為當日無帳務不需關帳
		if (tAcClose == null) {
			fClsFg = 1;
		} else {
			fClsFg = tAcClose.getClsFg();
		}

		this.info("L6101 findAcClose fClsFg : " + fClsFg);
		return fClsFg;
	}

	// 讀取應處理清單主檔
	private int findTxToDoMain(int fUnProcessCnt, TitaVo titaVo) throws LogicException {

		Slice<TxToDoMain> slTxToDoMain;
		slTxToDoMain = sTxToDoMainService.findAll(this.index, Integer.MAX_VALUE, titaVo);
		List<TxToDoMain> lTxToDoMain = slTxToDoMain == null ? null : slTxToDoMain.getContent();

		if (lTxToDoMain == null || lTxToDoMain.size() == 0) {
			fUnProcessCnt = 0; // 查無資料
		} else {
			// 如有找到資料
			for (TxToDoMain tTxToDoMain : lTxToDoMain) {

				// Y-關帳檢核有未處理則不許關帳
				if (!(tTxToDoMain.getAcClsCheck().equals("Y"))) {
					continue;
				}

				// 未處理筆數
				if (!(tTxToDoMain.getUnProcessCnt() > 0)) {
					continue;
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage",
						"應處理清單，" + tTxToDoMain.getItemDesc() + "，未處理筆數：" + tTxToDoMain.getUnProcessCnt());
				this.totaVo.addOccursList(occursList);

				fUnProcessCnt = fUnProcessCnt + tTxToDoMain.getUnProcessCnt();
			}
		}

		this.info("L6101 findTxToDoMain fUnProcessCnt : " + fUnProcessCnt);
		return fUnProcessCnt;
	}

	// 讀取整批入帳總數檔
	private int findBatxHead(int fBatxTotCnt, TitaVo titaVo) throws LogicException {
		int errocount = 0;
		int acDate = this.txBuffer.getTxBizDate().getTbsDyf();
		Slice<BatxHead> slBatxHead;
		slBatxHead = sBatxHeadService.acDateRange(acDate, acDate, this.index, Integer.MAX_VALUE);
		List<BatxHead> lBatxHead = slBatxHead == null ? null : slBatxHead.getContent();

		if (lBatxHead == null || lBatxHead.size() == 0) {
			fBatxTotCnt = 0; // 查無資料
		} else {
			// 如有找到資料
			for (BatxHead tBatxHead : lBatxHead) {

				// 4.入帳完成 8.已刪除
				if (tBatxHead.getBatxExeCode().equals("4") || tBatxHead.getBatxExeCode().equals("8")) {
					continue;
				}
				errocount = 1;
				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage",
						"整批入帳未完成，批號：" + tBatxHead.getBatchNo() + "，筆數：" + tBatxHead.getUnfinishCnt());
				this.totaVo.addOccursList(occursList);

				fBatxTotCnt = fBatxTotCnt + tBatxHead.getUnfinishCnt();

			}
		}

		this.info("L6101 findBatxHead fBatxTotCnt : " + fBatxTotCnt);
		return errocount;
	}

	// 讀取交易流程控制檔
	private int findTxFlow(String fSecNo, int fTxFlowCnt, TitaVo titaVo) throws LogicException {

		Slice<TxFlow> slTxFlow;
		slTxFlow = sTxFlowService.findBySecNo(this.txBuffer.getTxCom().getTbsdy() + 19110000, fSecNo, this.index,
				Integer.MAX_VALUE);
		List<TxFlow> lTxFlow = slTxFlow == null ? null : slTxFlow.getContent();

		if (lTxFlow == null || lTxFlow.size() == 0) {
			fTxFlowCnt = 0; // 查無資料
		} else {
			// 如有找到資料
			for (TxFlow tTxFlow : lTxFlow) {
				// 0.已放行 1.待放行 2.待審核 3.待提交 9.已訂正
				if (tTxFlow.getFlowMode() == 1 || tTxFlow.getFlowMode() == 3) {
					OccursList occursList = new OccursList();

					if (tTxFlow.getAcCnt() == 0) {// 帳務筆數
						occursList.putParam("OOMsgCode", "警告");
						fTxFlowCnt = 0;
					} else {
						occursList.putParam("OOMsgCode", "錯誤");
						fTxFlowCnt = fTxFlowCnt + 1;
					}
					occursList.putParam("OOMessage", "有未放行交易：" + tTxFlow.getTranNo() + "，交易序號：" + tTxFlow.getFlowNo());
					this.totaVo.addOccursList(occursList);

				}
			}
		}

		this.info("L6101 findTxFlow fTxFlowCnt : " + fTxFlowCnt);
		return fTxFlowCnt;
	}

	// 讀取會計銷帳檔
	private int findAcReceivable(int fAcReceivableCnt, TitaVo titaVo) throws LogicException {

		Slice<AcReceivable> slAcReceivable = sAcReceivableService.acctCodeEq(0, "TRO", 0, 9999999, 0, Integer.MAX_VALUE,
				titaVo);
		if (slAcReceivable != null) {
			for (AcReceivable tAcReceivable : slAcReceivable.getContent()) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage", "暫收款－借新還舊，戶號：" + parse.IntegerToString(tAcReceivable.getCustNo(), 7)
						+ "，有未銷餘額=" + tAcReceivable.getRvBal());
				this.totaVo.addOccursList(occursList);
				fAcReceivableCnt++;
			}
		}

		slAcReceivable = sAcReceivableService.acctCodeEq(0, "THC", 0, 9999999, 0, Integer.MAX_VALUE, titaVo);
		if (slAcReceivable != null) {
			for (AcReceivable tAcReceivable : slAcReceivable.getContent()) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOMsgCode", "錯誤");
				occursList.putParam("OOMessage", "暫收款－沖正，戶號：" + parse.IntegerToString(tAcReceivable.getCustNo(), 7)
						+ "，有未銷餘額=" + tAcReceivable.getRvBal());
				this.totaVo.addOccursList(occursList);
				fAcReceivableCnt++;
			}
		}

		this.info("L6101 findAcReceivable fUnProcessCnt : " + fAcReceivableCnt);
		return fAcReceivableCnt;
	}

	// 更新會計業務關帳控制檔
	private int updAcClose(String uSecNo, int uClsFg, int uClsNo, TitaVo titaVo) throws LogicException {

		int batNo = 0;
		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo(uSecNo); // 業務類別: 1-撥款匯款 2-支票繳款 3-債協 9-放款

		tAcClose = sAcCloseService.holdById(tAcCloseId);

		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0003", titaVo.getParam("SecNo")); // 修改資料不存在
		}

		// uClsFg 0.開帳 1.關帳 2.關帳取消
		// 業務批號=>預設為01，關帳後再開帳則＋１
		if (uClsFg == 0) {
			tAcClose.setBatNo(tAcClose.getBatNo() + 1);
		}

		// 業務關帳次數 => 預設為00，關帳時+1、關帳取消則-1
		if (uClsFg == 1) {
			tAcClose.setClsNo(tAcClose.getClsNo() + 1);
		}

		if (uClsFg == 2) {
			tAcClose.setClsNo(tAcClose.getClsNo() - 1);
		}

		tAcClose.setClsFg(uClsFg);

		try {
			sAcCloseService.update(tAcClose);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}

		if ("01".equals(uSecNo) && uClsFg == 1) { // 01.撥款匯款&關帳時，將整批批號帶到L4101
			this.totaVo.putParam("OOResult", "4101");
		} else if ("02".equals(uSecNo) && uClsFg == 1) { // 02.支票繳款&關帳時，自動連結[票據媒體製作]畫面
			this.totaVo.putParam("OOResult", "4701");
		} else {
			this.totaVo.putParam("OOResult", "0000");
		}

		batNo = findBatNo("09", batNo, titaVo);
		this.totaVo.putParam("OOBatNo09", batNo); // 9-放款_批號
		this.totaVo.putParam("OOBatNo01", tAcClose.getBatNo()); // 1-撥款匯款_批號
		this.totaVo.putParam("OOCoreSeqNo", tAcClose.getCoreSeqNo());

		uClsNo = tAcClose.getClsNo();

		this.info("L6101 updAcClose uClsNo : " + uClsNo);
		return uClsNo;

	}

	// 讀取會計業務關帳控制檔 - 業務批號
	private int findBatNo(String fSecNo, int fBatNo, TitaVo titaVo) throws LogicException {

		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo(fSecNo); // 業務類別: 1-撥款匯款 2-支票繳款 3-債協 9-放款

		tAcClose = sAcCloseService.findById(tAcCloseId);

		// 當日該業務有帳務產生時自動鍵一筆 , 找不到時為當日無帳務不需關帳
		if (tAcClose == null) {
			fBatNo = 1;
		} else {
			fBatNo = tAcClose.getBatNo();
		}

		this.info("L6101 findBatNo fBatNo : " + fBatNo);
		return fBatNo;
	}

	// 更新上傳核心序號(09:放款)
	// 啟動 L9130核心傳票媒體檔產生作業 ; L9131核心日結單代傳票列印 ; L9132傳票媒體明細表(核心) ; L9133會計與主檔餘額檢核表
	private void updCoreSeq(String uSecNo, int uClsFg, int uClsNo, TitaVo titaVo) throws LogicException {

		AcClose tAcClose = new AcClose();
		AcCloseId tAcCloseId = new AcCloseId();

		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 09-放款

		tAcClose = sAcCloseService.holdById(tAcCloseId);

		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0003", "更新上傳核心序號(09:放款)"); // 修改資料不存在
		}

		// 只更新特定筆(09:放款)預設為000，產生上傳媒體(02:支票繳款，09:放款)關帳時＋１
		if (uClsFg == 1) {
			tAcClose.setCoreSeqNo(tAcClose.getCoreSeqNo() + 1);
		}

		try {
			sAcCloseService.update(tAcClose);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "更新上傳核心序號(09:放款)"); // 更新資料時，發生錯誤
		}

		// 下行電文
		this.totaVo.putParam("OOCoreSeqNo", tAcClose.getCoreSeqNo());

		// 2:取消關帳 不產生報表
		if (uClsFg == 2) {
			return;
		}

		// 啟動L9130核心傳票媒體檔產生作業
		titaVo.putParam("AcDate", this.txBuffer.getTxCom().getTbsdy()); // 會計日期
		if ("02".equals(uSecNo)) {
			titaVo.putParam("BatchNo", 11); // 傳票批號 , 02:支票繳款時固定為11
		} else {
			titaVo.putParam("BatchNo", uClsNo); // 傳票批號 , 09:放款時為業務關帳之次數
		}
		titaVo.putParam("MediaSeq", tAcClose.getCoreSeqNo()); // 核心傳票
		titaVo.putParam("MediaType", tAcClose.getCoreSeqNo()); // 核心傳票
		if ("09".equals(uSecNo)) {
			// uSecNo = 09 時，才執行L9133
			titaVo.putParam("DoL9133", "Y");
		} else {
			titaVo.putParam("DoL9133", "N");
		}

		// 2021-12-15 智誠修改
//		MySpring.newTask("L6101Report", this.txBuffer, titaVo);
		l6101Excel.exec(titaVo);
		
		if ("09".equals(uSecNo)) {
			this.info("09=MySpring.newTask L9130");
			// 2021-10-05 智偉修改: 透過L9130控制 L9130、L9131、L9132、L9133
			MySpring.newTask("L9130", this.txBuffer, titaVo);
		}else if ("02".equals(uSecNo)) {
			this.info("02=exec L9130、L9131、L9132A、L9132B、L9132C");
			
			l9130Report.exec(titaVo);
			l9131Report.exec(titaVo);
			l9132ReportA.exec(titaVo);
			l9132ReportB.exec(titaVo);
			l9132ReportC.exec(titaVo);

		}
	}

	// 寫入應處理清單-業績工作月結算啟動通知(工作月結束，放款關帳)
	private void txToDoPFCL(TitaVo titaVo) throws LogicException {
		int acDateF = this.txBuffer.getTxBizDate().getTbsDyf();
		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findDateFirst(acDateF, acDateF);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + acDateF); // 查詢資料不存在
		}
		// 終止日期 >= 本營業日 && 終止日期 < 下營業日

		if (tCdWorkMonth.getEndDate() >= this.txBuffer.getTxBizDate().getTbsDy()
				&& tCdWorkMonth.getEndDate() < this.txBuffer.getTxBizDate().getNbsDy()) {
			txToDoCom.setTxBuffer(this.txBuffer);
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("PFCL00"); // 業績工作月結算啟動通知
			TempVo tTempVo = new TempVo();
			tTempVo.putParam("Note",
					parse.IntegerToString(tCdWorkMonth.getYear() - 1911, 3) + "-"
							+ parse.IntegerToString(tCdWorkMonth.getMonth(), 2) + "工作月結束 " + tCdWorkMonth.getStartDate()
							+ "~" + tCdWorkMonth.getEndDate() + "，請啟動業績工作月結算作業");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
	}
}
