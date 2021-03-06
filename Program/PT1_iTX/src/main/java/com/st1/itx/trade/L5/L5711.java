package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
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
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5711")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5711 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	@Autowired
	public NegCom sNegCom;
	@Autowired
	public AcNegCom acNegCom;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public AcDetailCom acDetailCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	BigDecimal SumApprAmt = new BigDecimal(0);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5711 ");
		this.totaVo.init(titaVo);

		int iAcDAte = Integer.parseInt(titaVo.getParam("AcDate")) + 19110000;
		String iTlrNo = titaVo.getParam("TlrNo");
		int iTxtNo = Integer.parseInt(titaVo.getParam("TxtNo"));
		acDetailCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		this.info("iAcDAte==" + iAcDAte + ",iTlrNo==" + iTlrNo + ",iTxtNo==" + iTxtNo);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;// 查全部 Integer.MAX_VALUE

		for (int i = 1; i <= 30; i++) {
			String iFinCode = titaVo.getParam("FinCode" + i);

			if (iFinCode.trim().isEmpty()) {
				break;
			}

			if (("458").equals(iFinCode)) {

				BigDecimal ApprAmt = parse.stringToBigDecimal(titaVo.getParam("ApprAmt" + i));// 分攤金額
				updateNegTrans(iAcDAte, iTlrNo, iTxtNo, ApprAmt, titaVo);

				BigDecimal AccuApprAmt = parse.stringToBigDecimal(titaVo.getParam("AccuApprAmt" + i));// 累積分攤金額
				updateNegMain(AccuApprAmt, titaVo);

			} else {

				NegAppr01 mNegAppr01 = sNegAppr01Service.holdById(new NegAppr01Id(iAcDAte, iTlrNo, iTxtNo, iFinCode), titaVo);

				if (mNegAppr01 == null) {
					throw new LogicException(titaVo, "E0003", "會計日=" + iAcDAte + ",經辦=" + iTlrNo + ",交易序號=" + iTxtNo + ",債權機構=" + iFinCode); // 修改資料不存在
				}
				NegAppr01 bNegAppr01 = (NegAppr01) dataLog.clone(mNegAppr01); 
				try {
					SumApprAmt = SumApprAmt.add(parse.stringToBigDecimal(titaVo.getParam("ApprAmt" + i)));

					mNegAppr01.setApprAmt(parse.stringToBigDecimal(titaVo.getParam("ApprAmt" + i)));
					mNegAppr01.setAccuApprAmt(parse.stringToBigDecimal(titaVo.getParam("AccuApprAmt" + i)));

					sNegAppr01Service.update(mNegAppr01, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, bNegAppr01, mNegAppr01); ////
				dataLog.exec("修改最大債權撥付資料檔,債權機構:"+iFinCode); ////
			}
		}

		/* 產生會計分錄 */
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void updateNegTrans(int mAcDAte, String mTlrNo, int mTxtNo, BigDecimal mApprAmt, TitaVo titaVo) throws LogicException {
		this.info("into updateNegTrans ");
		NegTrans iNegTrans = sNegTransService.findById(new NegTransId(mAcDAte, mTlrNo, mTxtNo), titaVo);

		if (iNegTrans != null) {
			if (mApprAmt.compareTo(iNegTrans.getSklShareAmt()) == 0) {
				return;
			}
			BigDecimal oldSklShareAmt = iNegTrans.getSklShareAmt();
			NegTrans bNegTrans = (NegTrans) dataLog.clone(iNegTrans); ////
			try {
				iNegTrans.setSklShareAmt(mApprAmt);
				iNegTrans.setApprAmt(SumApprAmt);
				sNegTransService.update(iNegTrans, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "債務協商交易檔"); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, bNegTrans, iNegTrans); ////
			dataLog.exec("修改債務協商交易檔"); ////

			if (this.txBuffer.getTxCom().isBookAcYes()) {
				List<AcDetail> acDetailList = new ArrayList<AcDetail>();

				// 先沖正原帳務
				/* 借：債協退還款科目 */
				AcDetail acDetail = new AcDetail();
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode(acNegCom.getReturnAcctCode(iNegTrans.getCustNo(), titaVo));
				acDetail.setTxAmt(oldSklShareAmt); // 原DB金額
				acDetail.setCustNo(iNegTrans.getCustNo());// 戶號
				acDetailList.add(acDetail);
				/* 貸：債協暫收款科目 */
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(acNegCom.getAcctCode(iNegTrans.getCustNo(), titaVo));
				acDetail.setTxAmt(oldSklShareAmt); // 原DB金額
				acDetail.setCustNo(iNegTrans.getCustNo());// 戶號
				acDetailList.add(acDetail);

				// 寫本次新壽攤分金額
				/* 借：債協暫收款科目 */
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode(acNegCom.getAcctCode(iNegTrans.getCustNo(), titaVo));
				acDetail.setTxAmt(mApprAmt); // 新壽攤分金額
				acDetail.setCustNo(iNegTrans.getCustNo());// 戶號
				acDetailList.add(acDetail);
				/* 貸：債協退還款科目 */
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(acNegCom.getReturnAcctCode(iNegTrans.getCustNo(), titaVo));
				acDetail.setTxAmt(mApprAmt); // 新壽攤分金額
				acDetail.setCustNo(iNegTrans.getCustNo());// 戶號
				acDetailList.add(acDetail);

				this.txBuffer.addAllAcDetailList(acDetailList);

			}
		}
	}

	public void updateNegMain(BigDecimal mAccuApprAmt, TitaVo titaVo) throws LogicException {
		this.info("into updateNegMain ");

		int iCustNo = Integer.parseInt(titaVo.getParam("CustNo"));
		int iCaseSeq = Integer.parseInt(titaVo.getParam("CaseSeq"));

		NegMain tNegMain = sNegMainService.findById(new NegMainId(iCustNo, iCaseSeq), titaVo);

		if (tNegMain != null) {

			if (mAccuApprAmt.compareTo(tNegMain.getAccuSklShareAmt()) == 0) {
				return;
			}
			NegMain bNegMain = (NegMain) dataLog.clone(tNegMain); 
			
			try {
				this.info("mAccuApprAmt==" + mAccuApprAmt);
				tNegMain.setAccuSklShareAmt(mAccuApprAmt);
				sNegMainService.update(tNegMain, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, bNegMain, tNegMain); ////
			dataLog.exec("修改債務協商案件主檔"); ////
			
		}
	}

}