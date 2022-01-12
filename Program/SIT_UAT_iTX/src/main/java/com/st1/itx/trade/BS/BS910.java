package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Ias39LoanCommit;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.service.Ias39LoanCommitService;
import com.st1.itx.db.service.TxToDoDetailReserveService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("BS910")
@Scope("prototype")
/**
 * 月底放款承諾提存<br>
 * 執行時機：月底日EomFinal 月底維護結束自動啟動<br>
 * 
 * 1.讀取IAS39放款承諾明細檔累計提存金額 <br>
 * 2.新增應處理明細－各項提存入帳作業，傳票批號=96<br>
 * 3.依前月底的應處理明細留存檔內容，產生迴轉上月，傳票批號=97<br>
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS910 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public Ias39LoanCommitService ias39LoanCommitService;

	@Autowired
	private TxToDoDetailReserveService txToDoDetailReserveService;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public GSeqCom gSeqCom;

	private int iAcDate;
	private int iAcDateReverse;
	private List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();;
	private List<TxToDoDetailReserve> lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS910 ......");// IAS39放款承諾明細檔
		txToDoCom.setTxBuffer(this.getTxBuffer());

		String dateYear = titaVo.getParam("Year");
		String dateMonth = titaVo.getParam("Month");
		int dateSent = Integer.parseInt(dateYear + dateMonth + "01") ;
		int yearMonth = Integer.parseInt(dateYear + dateMonth) + 191100 ; // 提存年月
		dateUtil.init();
		dateUtil.setDate_1(dateSent);
		TxBizDate tTxBizDate = dateUtil.getForTxBizDate(true);// 若1號為假日,參數true則會找次一營業日,不會踢錯誤訊息

		iAcDate = tTxBizDate.getMfbsDy();// 畫面輸入年月的月底營業日
		// 迴轉上個月底營業日
		iAcDateReverse = tTxBizDate.getLmnDy();
		this.info("BS910 iAcDate = " + iAcDate + ",iAcDateReverse=" + iAcDateReverse);		
		
		// 1.刪除處理清單 ACCL03-放款承諾提存入帳 //
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL03", 0, 3, this.index, Integer.MAX_VALUE);
		lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();
		if (lTxToDoDetail != null) {
			txToDoCom.delByDetailList(lTxToDoDetail, titaVo);
		}

		// 2.刪除處理清單留存檔 ACCL03 //
		this.info("3.bs910 delete Reserve ACCL03");
		Slice<TxToDoDetailReserve> slTxToDoDetailReserve = txToDoDetailReserveService.DataDateRange("ACCL03", 0, 3, iAcDate + 19110000, iAcDate + 19110000, this.index, Integer.MAX_VALUE, titaVo);
		lTxToDoDetailReserve = slTxToDoDetailReserve == null ? null : slTxToDoDetailReserve.getContent();
		if (lTxToDoDetailReserve != null) {
			try {
				txToDoDetailReserveService.deleteAll(lTxToDoDetailReserve, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "TxToDoDetailReserve deleteAll " + e.getErrorMsg());
			}
		}
		lTxToDoDetail = new ArrayList<TxToDoDetail>();
		lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();
		// 3.迴轉上月
		this.info("3.bs900 last month ACCL03");
		slTxToDoDetailReserve = txToDoDetailReserveService.DataDateRange("ACCL03", 0, 3, iAcDateReverse + 19110000, iAcDateReverse + 19110000, this.index, Integer.MAX_VALUE, titaVo);
		lTxToDoDetailReserve = slTxToDoDetailReserve == null ? null : slTxToDoDetailReserve.getContent();
		if (lTxToDoDetailReserve != null) {
			for (TxToDoDetailReserve t2 : lTxToDoDetailReserve) {
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode(t2.getItemCode());
				tTxToDoDetail.setDtlValue(t2.getDtlValue());
				TempVo t2TempVo = new TempVo();
				t2TempVo = t2TempVo.getVo(t2.getProcessNote());
				// 迴轉上月(傳票批號:97)
				TempVo tTempVo = new TempVo();
				tTempVo.clear();
				tTempVo.putParam("AcDate", iAcDate);
				tTempVo.putParam("SlipBatNo", "97");
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
			this.info("3.last month ACCL03 " + lTxToDoDetail.size());
		}

		lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();

		// xxxxx 放款承諾傳票批號設定為 96, 迴轉傳票批號設定為 97
		// 4.寫入應處理清單
		this.info("4.bs910 TxToDo ThisMonth");

		procTxToDo(yearMonth, titaVo);

		this.info("lTxToDoDetail size" + lTxToDoDetail.size());
		// 應處理明細檔
		if (lTxToDoDetail != null && lTxToDoDetail.size() > 0) {
			txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
		}

		// 應處理明細留存檔
		if (lTxToDoDetailReserve != null && lTxToDoDetailReserve.size() > 0) {
			try {
				txToDoDetailReserveService.insertAll(lTxToDoDetailReserve, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0005", "TxToDoDetailReserve insertAll " + e.getErrorMsg());
			}
		}

		// END
		this.batchTransaction.commit();
		return null;
	}

	/* 寫入應處理清單 ACCL03-各項提存作業，寫入新提存 */
	private void procTxToDo(int yearMonth, TitaVo titaVo) throws LogicException {
		HashMap<String, BigDecimal> map = new HashMap<>();
		Slice<Ias39LoanCommit> slIas39LoanCommit = ias39LoanCommitService.findDataYmEq(yearMonth, this.index, Integer.MAX_VALUE);
		List<Ias39LoanCommit> lIas39LoanCommit = slIas39LoanCommit == null ? null : slIas39LoanCommit.getContent();

		if (lIas39LoanCommit != null) {
			for (Ias39LoanCommit ac : lIas39LoanCommit) {
				String key = ac.getAcBookCode() + "," + ac.getAcSubBookCode();
				if (map.containsKey(key)) {
					map.put(key, map.get(key).add(ac.getExpLimitAmt())); // 表外曝險金額
				} else {
					map.put(key, ac.getExpLimitAmt());
				}
			}
			for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
				String[] keyArray = entry.getKey().split(",");
				addTxToDo(keyArray[0], keyArray[1], entry.getValue(), titaVo);
				this.info("bs910 TxToDo =" + entry.getKey() + ", Interest=" + entry.getValue());
			}
		}
	}

	private void addTxToDo(String acBookCode, String acSubBookCode, BigDecimal intAmt, TitaVo titaVo) throws LogicException {
		// 借：不可撤銷放款承諾 貸：待抵銷不可撤銷放款承諾
		// 銷帳編號：AC+民國年後兩碼+流水號六碼
		String rvNo = "AC" + parse.IntegerToString(this.getTxBuffer().getMgBizDate().getTbsDyf() / 10000, 4).substring(2, 4)
				+ parse.IntegerToString(gSeqCom.getSeqNo(this.getTxBuffer().getMgBizDate().getTbsDy(), 1, "L6", "RvNo", 999999, titaVo), 6);
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode("ACCL03");
		tTxToDoDetail.setDtlValue(rvNo);
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("AcDate", iAcDate);
		tTempVo.putParam("SlipBatNo", "96");
		tTempVo.putParam("AcclType", "放款承諾提存");
		tTempVo.putParam("AcBookCode", acBookCode); // 帳冊別
		tTempVo.putParam("AcSubBookCode", acSubBookCode);// 區隔帳冊
		tTempVo.putParam("SlipNote", "");
		tTempVo.putParam("DbTxAmt1", intAmt);
		tTempVo.putParam("DbAcctCode1", "LC1");
		tTempVo.putParam("DbRvNo1", rvNo);
		tTempVo.putParam("CrTxAmt1", intAmt);
		tTempVo.putParam("CrAcctCode1", "LC2");
		tTempVo.putParam("CrRvNo1", rvNo);
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		lTxToDoDetail.add(tTxToDoDetail);
		this.info("addTxToDo lTxToDoDetail size" + lTxToDoDetail.size());

		// 應處理明細留存檔
		TxToDoDetailReserve tTxToDoDetailReserve = new TxToDoDetailReserve();
		tTxToDoDetailReserve.setItemCode(tTxToDoDetail.getItemCode());
		tTxToDoDetailReserve.setDtlValue(tTxToDoDetail.getDtlValue());
		tTxToDoDetailReserve.setDataDate(iAcDate);
		tTxToDoDetailReserve.setProcessNote(tTxToDoDetail.getProcessNote());
		lTxToDoDetailReserve.add(tTxToDoDetailReserve);

	}

}