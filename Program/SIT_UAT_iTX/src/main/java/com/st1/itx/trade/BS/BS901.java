package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("BS901")
@Scope("prototype")
/**
 * 月底應付未付火險費提存、次月初迴轉<br>
 * 執行時機：1.經辦執行，應處理清單－各項提存啟動作業<br>
 * 
 * 1.讀取會計總帳檔檔(科目：暫收款－火險保費)餘額 <br>
 * 2.新增應處理明細－未付火險費提存入帳 2.1 月底日，本月提存，傳票批號=95<br>
 * 2.2 月初日，迴轉上月，傳票批號=94<br>
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS901 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	private AcDetailService acDetailService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public GSeqCom gSeqCom;

	private int iAcDate = 0;
	private int iAcDateReverse = 0;
	private List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();;
	private Slice<TxToDoDetail> slTxToDoDetail;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS901 ......");
		int yearMonth = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100; // 提存年月
		txToDoCom.setTxBuffer(this.getTxBuffer());
		iAcDate = this.getTxBuffer().getMgBizDate().getTmnDy();

		// 未付火險費提存，月初日迴轉上月
		if (this.txBuffer.getMgBizDate().getTbsDy() / 100 != this.txBuffer.getMgBizDate().getLbsDy() / 100) {
			iAcDateReverse = this.getTxBuffer().getMgBizDate().getLmnDy();
		}
		if (iAcDateReverse == 0) {
			// 1.刪除處理清單 ACCL02-未付火險費提存 BS901 //
			this.info("1.BS901 delete ACCL02");
			// 本月提存
			slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL02", 0, 3, this.index, Integer.MAX_VALUE);
			if (slTxToDoDetail != null) {
				for (TxToDoDetail t : slTxToDoDetail.getContent()) {
					txToDoCom.delByDetail(t, titaVo);
				}
			}
			// 2.月底提存，寫入應處理清單
			this.info("2.BS901 TxToDo");
			procTxToDo(yearMonth, titaVo);

			this.info("lTxToDoDetail size" + lTxToDoDetail.size());
			// 應處理明細檔
			if (lTxToDoDetail.size() > 0) {
				txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
			}
		}
		
		lTxToDoDetail = new ArrayList<TxToDoDetail>();
		if (iAcDateReverse > 0) {
			// 3.迴轉上月
			this.info("3.bs901 last month ACCL02");
			Slice<AcDetail> slAcDetail = acDetailService.findL9RptData(iAcDateReverse + 19110000, 95, this.index,
					Integer.MAX_VALUE, titaVo);
			if (slAcDetail != null) {
				for (AcDetail t : slAcDetail.getContent()) {
					if ("ORI".equals(t.getAcctCode())) {
						String rvNo = getRvNo(titaVo);
						TxToDoDetail tTxToDoDetail = new TxToDoDetail();
						tTxToDoDetail.setItemCode("ACCL02");
						tTxToDoDetail.setDtlValue(rvNo);
						// 迴轉上月(傳票批號:94)
						TempVo tTempVo = new TempVo();
						tTempVo.clear();
						tTempVo.putParam("AcDate", iAcDate);
						tTempVo.putParam("SlipBatNo", "94");
						tTempVo.putParam("AcclType", "迴轉上月");
						tTempVo.putParam("AcctCode", "ORI");
						tTempVo.putParam("AcctCode", t.getAcctCode());
						tTempVo.putParam("AcBookCode", t.getAcBookCode());
						tTempVo.putParam("AcSubBookCode", t.getAcSubBookCode());
						tTempVo.putParam("SlipNote", t.getSlipNote());
						tTempVo.putParam("CrAcctCode1", "ORI");
						tTempVo.putParam("CrRvNo1", rvNo);
						tTempVo.putParam("CrTxAmt1", t.getTxAmt());
						tTempVo.putParam("DbAcctCode1", "OPO");
						tTempVo.putParam("DbTxAmt1", t.getTxAmt());
						tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
						lTxToDoDetail.add(tTxToDoDetail);
					}
				}
				this.info("3.last month ACCL02 " + lTxToDoDetail.size());
				if (lTxToDoDetail.size() > 0) {
					txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
				}
			}
		}

		// END
		this.batchTransaction.commit();
		return null;
	}

	private String getRvNo(TitaVo titaVo) throws LogicException {
		// 銷帳編號：AC+民國年後兩碼+流水號六碼
		String rvNo = "AC"
				+ parse.IntegerToString(this.getTxBuffer().getMgBizDate().getTbsDyf() / 10000, 4).substring(2, 4)
				+ parse.IntegerToString(
						gSeqCom.getSeqNo(this.getTxBuffer().getMgBizDate().getTbsDy(), 1, "L6", "RvNo", 999999, titaVo),
						6);
		return rvNo;
	}

	/* 寫入應處理清單 ACCL02 */
	private void procTxToDo(int yearMonth, TitaVo titaVo) throws LogicException {
//		A.月底提存
//	    借: 10320301其它應收款-火險保費 ORI
//	        貸: 20211019其它應付款-其它  OPO  
//	    摘要:yyy年xx月其它應收款火險保費

		// 銷帳編號：AC+民國年後兩碼+流水號六碼
		String rvNo = getRvNo(titaVo);
		BigDecimal txAmt = BigDecimal.ZERO;
		int yyy = this.txBuffer.getMgBizDate().getTbsDy() / 10000;
		int mm = (this.txBuffer.getMgBizDate().getTbsDy() / 100) - yyy * 100;

		int insuYearMonth = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100;
		// 計入已收，未收不計
		// 本月到期未繳火險保費
		Slice<InsuRenew> slInsuRenew = insuRenewService.findL4604A(insuYearMonth, 2, 0, 0, this.index,
				Integer.MAX_VALUE);
		if (slInsuRenew != null) {
			for (InsuRenew tInsuRenew : slInsuRenew.getContent()) {
				if (tInsuRenew.getStatusCode() == 0) {
					txAmt = txAmt.add(tInsuRenew.getTotInsuPrem());
				}
			}
		}
		this.info("procTxToDoThisMonth  txAmt =" + txAmt);
		if (txAmt.compareTo(BigDecimal.ZERO) > 0) {
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("ACCL02");
			tTxToDoDetail.setDtlValue(rvNo);
			TempVo tTempVo = new TempVo();
			tTempVo.clear();
			tTempVo.putParam("AcDate", iAcDate);
			tTempVo.putParam("SlipBatNo", "95");
			tTempVo.putParam("AcclType", "火險費提存");
			tTempVo.putParam("AcBookCode", this.txBuffer.getSystemParas().getAcBookCode()); // 帳冊別 000
			tTempVo.putParam("AcSubBookCode", this.txBuffer.getSystemParas().getAcSubBookCode()); // 區隔帳冊 00A
			tTempVo.putParam("AcctCode", "ORI");
			tTempVo.putParam("SlipNote",
					parse.IntegerToString(yyy, 3) + "年" + parse.IntegerToString(mm, 2) + "月" + "其它應收款火險保費");
			tTempVo.putParam("DbAcctCode1", "ORI");
			tTempVo.putParam("DbRvNo1", rvNo);
			tTempVo.putParam("DbTxAmt1", txAmt);
			tTempVo.putParam("CrAcctCode1", "OPO");
			tTempVo.putParam("CrRvNo1", rvNo);
			tTempVo.putParam("CrTxAmt1", txAmt);
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			lTxToDoDetail.add(tTxToDoDetail);
		}
	}
}