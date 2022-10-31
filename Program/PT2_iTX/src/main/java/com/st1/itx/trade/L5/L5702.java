package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
/*DB服務*/
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 * CaseSeq=X,3<br>
 * CustNo=X,7<br>
 * TransAcDate=9,7<br>
 * TransTitaTlrNo=X,6<br>
 * TransTitaTxtNo=X,8<br>
 * TransCustNo=X,7<br>
 * TransCaseSeq=X,3<br>
 * NewTransTxKind=9,1<br>
 * NewTransReturnAmt=9,14.2<br>
 */

@Service("L5702")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5702 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegCom sNegCom;
	@Autowired
	public AcNegCom acNegCom;
	@Autowired
	public AcDetailCom acDetailCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public NegAppr02Service sNegAppr02Service;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	private NegTrans tNegTrans = new NegTrans();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5702 ");
		this.totaVo.init(titaVo);
		acDetailCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		this.info("L5702 titaVo=[" + titaVo + "]");

		String TransAcDate = "";
		String TransTitaTlrNo = "";
		String TransTitaTxtNo = "";

		String TransTxKind = "";
		String btnIndex = "";
		String TrialFunc = "";
		String TXCDCHAIN = titaVo.getParam("TXCDCHAIN");
		int icustNo = 0;
		int icaseSeq = 0;
		int iPayerCustNo = 0;
		NegMainId tNegMainId = new NegMainId();
		NegMain tNegMain = new NegMain();

		if (titaVo.isHcodeNormal()) { // 正常交易

			if ("L597A".equals(TXCDCHAIN)) {

				btnIndex = titaVo.getParam("btnIndex");
				TrialFunc = titaVo.getParam("TrialFunc").trim();
				TransAcDate = titaVo.getParam("OOAcctDate").trim();
				TransTitaTlrNo = titaVo.getParam("OOTitaTlrNo").trim();
				TransTitaTxtNo = titaVo.getParam("OOTitaTxtNo").trim();
				TransTxKind = titaVo.getParam("OONewtransTxKind").trim();

			} else {

				TrialFunc = titaVo.getParam("TrialFunc").trim();
				TransAcDate = titaVo.getParam("TransAcDate").trim();
				TransTitaTlrNo = titaVo.getParam("TransTitaTlrNo").trim();
				TransTitaTxtNo = titaVo.getParam("TransTitaTxtNo").trim();
				TransTxKind = titaVo.getParam("NewTransTxKind").trim();

			}

			NegTransId tNegTransId = new NegTransId();
			int AcDate = parse.stringToInteger(TransAcDate);
			tNegTransId.setAcDate(AcDate);
			tNegTransId.setTitaTlrNo(TransTitaTlrNo);
			tNegTransId.setTitaTxtNo(parse.stringToInteger(TransTitaTxtNo));
			tNegTrans = sNegTransService.findById(tNegTransId);
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E0001", "債務協商交易檔");
			}
			// 找付款人戶號
			icustNo = tNegTrans.getCustNo();
			icaseSeq = tNegTrans.getCaseSeq();
			tNegMainId.setCaseSeq(icaseSeq);
			tNegMainId.setCustNo(icustNo);

			tNegMain = sNegMainService.findById(tNegMainId, titaVo);
			if (tNegMain == null) {
			} else {
				iPayerCustNo = tNegMain.getPayerCustNo();
			}
			if ("".equals(btnIndex) || "1".equals(btnIndex)) {
				sNegCom.trialNegtrans(tNegTrans, "2", TransTxKind, titaVo);
				// 會計帳
				if (Arrays.asList(new String[] { "0", "1", "2", "3", "4", "5" }).contains(TransTxKind)) {
					UpdAcDB(tNegTransId,iPayerCustNo, titaVo);
				}
//				updateNegAppr02(tNegTransId, titaVo);// 維護NegAppr02,2022-3-22取消L5712暫收解入功能

			} else {
				NegTrans bNegTrans = (NegTrans) dataLog.clone(tNegTrans); ////
				
				if (tNegTrans.getTxStatus() == 0) {
					tNegTrans.setTxStatus(1); // 交易狀態0:未入帳;1:待處理;2:已入帳

				} else if (tNegTrans.getTxStatus() == 1) {
					tNegTrans.setTxStatus(0); // 交易狀態0:未入帳;1:待處理;2:已入帳

				} else {
					throw new LogicException(titaVo, "E0010", "已入帳，如需變更交易狀態請做訂正");
				}
				try {
					sNegTransService.update(tNegTrans, titaVo);//
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());// E0007 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, bNegTrans, tNegTrans); ////
				dataLog.exec("修改債務協商交易檔"); ////
				
			}
		} else { // 訂正

			TrialFunc = titaVo.getParam("TrialFunc").trim();
			TransAcDate = titaVo.getParam("TransAcDate").trim();
			TransTitaTlrNo = titaVo.getParam("TransTitaTlrNo").trim();
			TransTitaTxtNo = titaVo.getParam("TransTitaTxtNo").trim();
			TransTxKind = titaVo.getParam("NewTransTxKind").trim();

			NegTransId tNegTransId = new NegTransId();
			int AcDate = parse.stringToInteger(TransAcDate);
			tNegTransId.setAcDate(AcDate);
			tNegTransId.setTitaTlrNo(TransTitaTlrNo);
			tNegTransId.setTitaTxtNo(parse.stringToInteger(TransTitaTxtNo));
			tNegTrans = sNegTransService.findById(tNegTransId);
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E0001", "債務協商交易檔");
			}
			// 找付款人戶號
			icustNo = tNegTrans.getCustNo();
			icaseSeq = tNegTrans.getCaseSeq();
			tNegMainId.setCaseSeq(icaseSeq);
			tNegMainId.setCustNo(icustNo);

			tNegMain = sNegMainService.findById(tNegMainId, titaVo);
			if (tNegMain == null) {
			} else {
				iPayerCustNo = tNegMain.getCustNo();
			}

			this.info("TrialFunc = " + TrialFunc + "TransTxKind = " + TransTxKind);

			sNegCom.NegRepayEraseRoutine(titaVo);

			// 會計帳
			if (Arrays.asList(new String[] { "0", "1", "2", "3", "4", "5" }).contains(TransTxKind)) {
				UpdAcDB(tNegTransId,iPayerCustNo, titaVo);
			}
//			updateNegAppr02(tNegTransId, titaVo);// 維護NegAppr02,2022-3-22取消L5712暫收解入功能

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void UpdAcDB(NegTransId tNegTransId, int iPayerCustNo ,TitaVo titaVo) throws LogicException {
		// 異動會計分錄檔
		/* 帳務 */
		// 經辦登帳非訂正交易
		this.info("UpdAcDB Run");

		if (this.txBuffer.getTxCom().isBookAcYes()) {
			this.info("NegTransAcYes");
			NegTrans tNegTrans = sNegTransService.findById(tNegTransId);

			List<AcDetail> acDetailList = new ArrayList<AcDetail>();
			/* 借：債協暫收款科目 */
			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(acNegCom.getAcctCode(tNegTrans.getCustNo(), titaVo));
			acDetail.setTxAmt(tNegTrans.getSklShareAmt().add(tNegTrans.getReturnAmt())); // 新壽攤分金額 + 退還金額
			acDetail.setCustNo(tNegTrans.getCustNo());// 客戶戶號
			acDetailList.add(acDetail);
			/* 貸：債協退還款科目 */
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(acNegCom.getReturnAcctCode(tNegTrans.getCustNo(), titaVo));
			acDetail.setTxAmt(tNegTrans.getSklShareAmt()); // 新壽攤分金額
			if (iPayerCustNo > 0) {
				acDetail.setCustNo(iPayerCustNo);// 有保證人時使用付款人戶號-20221027修改
			} else {
				acDetail.setCustNo(tNegTrans.getCustNo());// 戶號
			}
			acDetailList.add(acDetail);
			/* 貸：債協退還款科目 */
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(acNegCom.getReturnAcctCode(tNegTrans.getCustNo(), titaVo));
			acDetail.setTxAmt(tNegTrans.getReturnAmt()); // 退還金額
			if (iPayerCustNo > 0) {
				acDetail.setCustNo(iPayerCustNo);// 有保證人時使用付款人戶號-20221027修改
			} else {
				acDetail.setCustNo(tNegTrans.getCustNo());// 戶號
			}
			acDetailList.add(acDetail);

			this.txBuffer.addAllAcDetailList(acDetailList);

			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}
		return;
	}

	/* 更新 NegAppr02一般債權撥付資料檔 */
	private void updateNegAppr02(NegTransId tNegTransId, TitaVo titaVo) throws LogicException {
		// NegAppr02一般債權撥付資料檔，有找到NegTrans的KEY值相同才維護
		// 2022-3-22取消L5712暫收解入功能(更新NegAppr02.TxStatus,NegTransAcDate,NegTransTlrNo,NegTransTxtNo),無更新NegAppr02資料故以下程式不做
		NegTrans tNegTrans = sNegTransService.findById(tNegTransId);
		if (tNegTrans == null) {
			throw new LogicException(titaVo, "E0001", "債務協商交易檔");
		}
		Slice<NegAppr02> slNegAppr02 = sNegAppr02Service.NegTransEq(tNegTrans.getAcDate() + 19110000, tNegTrans.getTitaTlrNo(), tNegTrans.getTitaTxtNo(), 0, 500, titaVo);
		List<NegAppr02> lNegAppr02 = slNegAppr02 == null ? null : slNegAppr02.getContent();

		NegAppr02 tNegAppr02 = new NegAppr02();
		if (lNegAppr02 != null) {
			for (NegAppr02 cNegAppr02 : lNegAppr02) {// 應該只有一筆

				tNegAppr02 = sNegAppr02Service.holdById(cNegAppr02.getNegAppr02Id(), titaVo);
				NegAppr02 bNegAppr02 = (NegAppr02) dataLog.clone(tNegAppr02); ////
				if (titaVo.isHcodeNormal()) {
					tNegAppr02.setTxStatus(2);
				} else {
					tNegAppr02.setTxStatus(1);
				}
				try {
					sNegAppr02Service.update(tNegAppr02,titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "一般債權撥付資料檔 + tNegAppr02Id)"); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, bNegAppr02, tNegAppr02); ////
				dataLog.exec("修改一般債權撥付資料檔"); ////
				break;
			}
		}
	}

}