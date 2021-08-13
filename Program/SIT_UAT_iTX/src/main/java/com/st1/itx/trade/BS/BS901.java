package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.domain.TxToDoDetailReserveId;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.TxToDoDetailReserveService;
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
 * 2.新增應處理明細－未付火險費提存入帳
 * 2.1 月底日，本月提存，傳票批號=95<br>
 * 2.2 月初日，迴轉上月，傳票批號=94<br>
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS901 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS901.class);
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	private TxToDoDetailReserveService txToDoDetailReserveService;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public GSeqCom gSeqCom;

	private int iAcDate;
	private int iAcDateReverse;
	private List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();;
	private List<TxToDoDetailReserve> lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();
	private Slice<TxToDoDetail> slTxToDoDetail;
	private Slice<TxToDoDetailReserve> slTxToDoDetailReserve;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS901 ......");
		int yearMonth = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100; // 提存年月
		txToDoCom.setTxBuffer(this.getTxBuffer());
		iAcDate = this.getTxBuffer().getMgBizDate().getTbsDy();

		// 未付火險費提存，月初日迴轉上月
		if (this.txBuffer.getMgBizDate().getTbsDy() / 100 != this.txBuffer.getMgBizDate().getLbsDy() / 100) {
			iAcDateReverse = this.getTxBuffer().getMgBizDate().getLbsDy();
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
			// 2.刪除處理清單留存檔 ACCL02-未付火險費提存 入帳 //
			this.info("3.bs901 delete Reserve ACCL02");
			slTxToDoDetailReserve = txToDoDetailReserveService.DataDateRange("ACCL02", 0, 3, iAcDate + 19110000,
					iAcDate + 19110000, this.index, Integer.MAX_VALUE, titaVo);
			if (slTxToDoDetailReserve != null) {
				try {
					txToDoDetailReserveService.deleteAll(slTxToDoDetailReserve.getContent(), titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E0008", "TxToDoDetailReserve deleteAll " + e.getErrorMsg());
				}
			}
			// 4.月底提存，寫入應處理清單
			this.info("4.BS901 TxToDo");
			procTxToDo(yearMonth, titaVo);

			this.info("lTxToDoDetail size" + lTxToDoDetail.size());
			// 應處理明細檔
			if (lTxToDoDetail.size() > 0) {
				txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
			}
			// 應處理明細留存檔
			if (lTxToDoDetailReserve.size() > 0) {
				try {
					txToDoDetailReserveService.insertAll(lTxToDoDetailReserve, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E0005", "TxToDoDetailReserve insertAll " + e.getErrorMsg());
				}
			}
		}
		if (iAcDateReverse > 0) {
			// 3.迴轉上月
			this.info("3.bs901 last month ACCL02");
			slTxToDoDetailReserve = txToDoDetailReserveService.DataDateRange("ACCL02", 0, 3, iAcDateReverse + 19110000,
					iAcDateReverse + 19110000, this.index, Integer.MAX_VALUE, titaVo);
			if (slTxToDoDetailReserve != null) {
				for (TxToDoDetailReserve t2 : slTxToDoDetailReserve.getContent()) {
					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setItemCode(t2.getItemCode());
					tTxToDoDetail.setDtlValue(t2.getDtlValue() + "R");
					TempVo t2TempVo = new TempVo();
					t2TempVo = t2TempVo.getVo(t2.getProcessNote());
					// 迴轉上月(傳票批號:94)
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("AcDate", iAcDate);
					tTempVo.putParam("SlipBatNo", "94");
					tTempVo.putParam("AcclType", "迴轉上月");
					tTempVo.putParam("AcctCode", t2TempVo.getParam("AcctCode"));
					tTempVo.putParam("AcBookCode", t2TempVo.getParam("AcBookCode"));
					tTempVo.putParam("AcSubBookCode", t2TempVo.getParam("AcSubBookCode"));
					tTempVo.putParam("SlipNote", t2TempVo.getParam("SlipNote"));
					tTempVo.putParam("CrAcctCode1", t2TempVo.getParam("DbAcctCode1"));
					tTempVo.putParam("CrRvNo1", t2TempVo.getParam("DbRvNo1"));
					tTempVo.putParam("CrTxAmt1", t2TempVo.getParam("DbTxAmt1"));
					tTempVo.putParam("DbAcctCode1", t2TempVo.getParam("CrAcctCode1"));
					tTempVo.putParam("DbTxAmt1", t2TempVo.getParam("CrTxAmt1"));
					tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
					lTxToDoDetail.add(tTxToDoDetail);
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

	/* 寫入應處理清單 ACCL02 */
	private void procTxToDo(int yearMonth, TitaVo titaVo) throws LogicException {
//		A.月底提存
//	    借: 10320301其它應收款-火險保費 ORI
//	        貸: 20211019其它應付款-其它  OPO  
//	    摘要:yyy年xx月其它應收款火險保費

		// 銷帳編號：AC+民國年後兩碼+流水號六碼
		String rvNo = "AC"
				+ parse.IntegerToString(this.getTxBuffer().getMgBizDate().getTbsDyf() / 10000, 4).substring(2, 4)
				+ parse.IntegerToString(
						gSeqCom.getSeqNo(this.getTxBuffer().getMgBizDate().getTbsDy(), 1, "L6", "RvNo", 999999, titaVo),
						6);
		BigDecimal txAmt = BigDecimal.ZERO;
		int yyy = this.txBuffer.getMgBizDate().getTbsDy() / 10000;
		int mm = (this.txBuffer.getMgBizDate().getTbsDy() / 100) - yyy * 100;

		// AcMain 會計總帳檔， TMI 暫收款－火險保費
		Slice<AcMain> slAcMain = acMainService.acctCodeEq(this.getTxBuffer().getMgBizDate().getTbsDyf(), "TMI",
				this.index, Integer.MAX_VALUE);
		List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();
		// 計入已收，未收不計
		if (lAcMain != null) {
			for (AcMain ac : lAcMain) {
				txAmt = ac.getTdBal(); // 本日餘額
				this.info("procTxToDoThisMonth  txAmt =" + txAmt);
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

				// 應處理明細留存檔
				TxToDoDetailReserve tTxToDoDetailReserve = new TxToDoDetailReserve();
				TxToDoDetailReserveId tTxToDoDetailReserveId = new TxToDoDetailReserveId();
				tTxToDoDetailReserveId.setItemCode(tTxToDoDetail.getItemCode());
				tTxToDoDetailReserveId.setDtlValue(tTxToDoDetail.getDtlValue());
				tTxToDoDetailReserve.setTxToDoDetailReserveId(tTxToDoDetailReserveId);
				tTxToDoDetailReserve.setDataDate(iAcDate);
				tTxToDoDetailReserve.setProcessNote(tTxToDoDetail.getProcessNote());
				lTxToDoDetailReserve.add(tTxToDoDetailReserve);
			}
		}
	}
}