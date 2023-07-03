package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS903")
@Scope("prototype")
/**
 * 未兌現應收票據提存<br>
 * 執行時機：經辦執行L9713應收票據之帳齡分析表(上傳核心系統應收票據明細檔)<br>
 * 寫入處理清單 ACCL05未兌現應收票據提存<br>
 * 1.月底提存，傳票批號=92<br>
 * 2.迴轉上月，傳票批號=91<br>
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS903 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	WebClient webClient;

	private int iAcDate = 0;
	private int iAcDateReverse = 0;
	private BigDecimal iChecqueAmt = BigDecimal.ZERO;
	private List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();;
	private Slice<TxToDoDetail> slTxToDoDetail;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS903 ......");
		txToDoCom.setTxBuffer(this.getTxBuffer());
		// 輸入日期為迴轉日期
		iAcDateReverse = parse.stringToInteger(titaVo.getParam("ACCTDATE"));
		// 輸入日期的上月底日提存日期為提存日期
		dateUtil.init();
		dateUtil.setDate_1(iAcDateReverse + 19110000);
		TxBizDate tTxBizDate = dateUtil.getForTxBizDate();// 輸入日期不可為假日
		iAcDate = tTxBizDate.getLmnDy();
		iChecqueAmt = parse.stringToBigDecimal(titaVo.getParam("ChecqueAmt"));
		// 0.檢核是否已入帳 /
		slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL05", 2, 2, 0, Integer.MAX_VALUE, titaVo);
		if (slTxToDoDetail != null) {
			this.addList(this.totaVo);
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "",
					"本月未兌現應收票據提存已入帳，請執行訂正", titaVo);
			return this.sendList();
		}

		// 1.刪除處理清單 ACCL05-未兌現應收票據提存 BS903 //
		this.info("1.BS903 delete ACCL05");
		slTxToDoDetail = txToDoDetailService.detailStatusRange("ACCL05", 0, 3, 0, Integer.MAX_VALUE);
		if (slTxToDoDetail != null) {
			for (TxToDoDetail t : slTxToDoDetail.getContent()) {
				txToDoCom.delByDetail(t, titaVo);
			}
		}
		// 2.月底提存，寫入應處理清單
		this.info("2.BS903 TxToDo");
		procTxToDo(titaVo);

		this.info("lTxToDoDetail size" + lTxToDoDetail.size());
		// 應處理明細檔
		if (lTxToDoDetail.size() > 0) {
			txToDoCom.addByDetailList(false, 0, lTxToDoDetail, titaVo); // DupSkip = false ->重複 error
		}
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001", titaVo.getTlrNo(),
				"請執行 各項提存入帳作業(未兌現應收票據", titaVo);

		// END
		this.batchTransaction.commit();
		return null;
	}

	/* 寫入應處理清單 ACCL05 */
	private void procTxToDo(TitaVo titaVo) throws LogicException {
// 月底提存(傳票批號:92)
//	    借: 20222020000 暫收及待結轉帳項－擔保放款	TCK暫收款－支票
//	        貸: 10310120000 應收票據－一般票據－非核心	RCK	應收票據－一般票據－非核心
//	    摘要:yyy年xx月放款應收票據未兌現

		int yyy = iAcDate / 10000;
		int mm = (iAcDate / 100) - yyy * 100;
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		TempVo tTempVo = new TempVo();
		tTxToDoDetail.setItemCode("ACCL05");
		tTxToDoDetail.setDtlValue("92");
		tTempVo.clear();
		tTempVo.putParam("AcDate", iAcDate);
		tTempVo.putParam("SlipBatNo", "92");
		tTempVo.putParam("AcclType", "月底提存");
		tTempVo.putParam("AcBookCode", this.txBuffer.getSystemParas().getAcBookCode()); // 帳冊別 000
		tTempVo.putParam("AcSubBookCode", this.txBuffer.getSystemParas().getAcSubBookCode()); // 區隔帳冊 00A
		tTempVo.putParam("AcctCode", "TCK");
		tTempVo.putParam("SlipNote",
				parse.IntegerToString(yyy, 3) + "年" + parse.IntegerToString(mm, 2) + "月" + "放款應收票據未兌現");
		tTempVo.putParam("DbAcctCode1", "TCK");
		tTempVo.putParam("DbRvNo1", "");
		tTempVo.putParam("DbTxAmt1", iChecqueAmt);
		tTempVo.putParam("CrAcctCode1", "RCK");
		tTempVo.putParam("CrRvNo1", "");
		tTempVo.putParam("CrTxAmt1", iChecqueAmt);
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		lTxToDoDetail.add(tTxToDoDetail);
// 迴轉上月(傳票批號:91)
//	     借:10310120000 應收票據－一般票據－非核心	RCK	應收票據－一般票據－非核心
//	       貸: 20222020000 暫收及待結轉帳項－擔保放款	TCK暫收款－支票
//	    摘要:yyy年xx月放款應收票據未兌現
		tTxToDoDetail = new TxToDoDetail();
		tTempVo = new TempVo();
		tTempVo.clear();
		tTxToDoDetail.setItemCode("ACCL05");
		tTxToDoDetail.setDtlValue("91");
		tTempVo.clear();
		tTempVo.putParam("AcDate", iAcDateReverse);
		tTempVo.putParam("SlipBatNo", "91");
		tTempVo.putParam("AcclType", "迴轉上月");
		tTempVo.putParam("AcBookCode", this.txBuffer.getSystemParas().getAcBookCode()); // 帳冊別 000
		tTempVo.putParam("AcSubBookCode", this.txBuffer.getSystemParas().getAcSubBookCode()); // 區隔帳冊 00A
		tTempVo.putParam("AcctCode", "TCK");
		tTempVo.putParam("SlipNote",
				parse.IntegerToString(yyy, 3) + "年" + parse.IntegerToString(mm, 2) + "月" + "放款應收票據未兌現");
		tTempVo.putParam("DbAcctCode1", "RCK");
		tTempVo.putParam("DbRvNo1", "");
		tTempVo.putParam("DbTxAmt1", iChecqueAmt);
		tTempVo.putParam("CrAcctCode1", "TCK");
		tTempVo.putParam("CrRvNo1", "");
		tTempVo.putParam("CrTxAmt1", iChecqueAmt);
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		lTxToDoDetail.add(tTxToDoDetail);
	}
}