package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
/*DB服務*/
import com.st1.itx.util.common.NegCom;
import com.st1.itx.db.service.NegTransService;
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

		String TrialFunc = titaVo.getParam("TrialFunc").trim();
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

		// 入帳
		// 正常
		if (titaVo.isHcodeNormal()) {
			sNegCom.trialNegtrans(tNegTrans, TrialFunc, TransTxKind, titaVo );
		}
		// 訂正
		else {
			sNegCom.NegRepayEraseRoutine(titaVo);
		}
		// 會計帳
		if (Arrays.asList(new String[] { "0", "1", "2", "3", "4", "5" }).contains(TransTxKind)) {
			UpdAcDB(tNegTransId, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void UpdAcDB(NegTransId tNegTransId, TitaVo titaVo) throws LogicException {
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
			acDetail.setCustNo(tNegTrans.getCustNo());// 戶號
			acDetailList.add(acDetail);
			/* 貸：債協退還款科目 */
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(acNegCom.getReturnAcctCode(tNegTrans.getCustNo(), titaVo));
			acDetail.setTxAmt(tNegTrans.getSklShareAmt()); // 新壽攤分金額
			acDetail.setCustNo(tNegTrans.getCustNo());// 戶號
			acDetailList.add(acDetail);
			/* 貸：債協退還款科目 */
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(acNegCom.getReturnAcctCode(tNegTrans.getCustNo(), titaVo));
			acDetail.setTxAmt(tNegTrans.getReturnAmt()); // 退還金額
			acDetail.setCustNo(tNegTrans.getCustNo());// 戶號
			acDetailList.add(acDetail);

			this.txBuffer.addAllAcDetailList(acDetailList);

			/* 產生會計分錄 */
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}
		return;
	}
}