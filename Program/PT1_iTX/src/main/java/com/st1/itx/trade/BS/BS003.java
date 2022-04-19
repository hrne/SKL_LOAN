package com.st1.itx.trade.BS;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 一般應處理清單產生 <br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS003")
@Scope("prototype")
public class BS003 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(BS003.class);

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoCom txToDoCom;

	private TempVo tTempVo = new TempVo();

	@Override
	/* 應處理清單每日維護 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS003 ......");

		txToDoCom.setTxBuffer(this.txBuffer);
// 各項提存 由 LC700 發動日終維護批次 及 EomFinal 月底維護結束自動啟動，僅執行後的入帳資料寫入應處理清單 
//		/* 1.每月月底營業日寫入應處理清單 */
//		// ACCS00 各項提存啟動作業"
//		if (this.txBuffer.getMgBizDate().getTbsDy() == this.txBuffer.getMgBizDate().getMfbsDy()) {
//			tTempVo.clear();
//			tTempVo.putParam("Note", "應收利息月底提存");
//			addTxToDo("ACCS00", "BS900", "BS900", titaVo);
//
//			tTempVo.clear();
//			tTempVo.putParam("Note", "應付未付火險費月底提存");
//			addTxToDo("ACCS00", "BS901", "BS901", titaVo);
//			
//			tTempVo.clear();
//			tTempVo.putParam("Note", "放款承諾月底提存");
//			addTxToDo("ACCS00", "BS910", "BS910", titaVo);
//		}
//
//		this.batchTransaction.commit();

		/* 3.每月21日(遇假日順延)火險保費未繳轉借支 */
		int processDate = (this.txBuffer.getMgBizDate().getTbsDy() / 100) * 100 + 21;
		if (this.txBuffer.getMgBizDate().getLbsDy() < processDate && this.txBuffer.getMgBizDate().getTbsDy() >= processDate) {
			tTempVo.clear();
			tTempVo.putParam("Note", "每月21日火險保費未繳轉借支");
			addTxToDo("L4604", "", "", titaVo);
		}

		/* 4.每月第2營業日寫入應處理清單 */
		// L9710寬限到期明細表 每月第2營業日印後2個月
		// L9711 放款到期明細表及通知單 每月第2營業日印後4個月
		dateUtil.init();
		dateUtil.setDate_1(this.txBuffer.getMgBizDate().getLbsDyf());
		TxBizDate bizDate = dateUtil.getForTxBizDate();
		if (bizDate.getTbsDy() / 100 != bizDate.getLbsDy() / 100) { // 上營業日為月初日(換月)
			tTempVo.clear();
			tTempVo.putParam("Note", "寬限到期明細表 每月第2營業日印後2個月");
			addTxToDo("L9710", "", "", titaVo);

			tTempVo.clear();
			tTempVo.putParam("Note", "放款到期明細表及通知單 每月第2營業日印後4個月");
			addTxToDo("L9711", "", "", titaVo);
		}

		this.batchTransaction.commit();
		return null;
	}

	private void addTxToDo(String itemCode, String dtlValue, String excuteTxcd, TitaVo titaVo) throws LogicException {
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode(itemCode);
		tTxToDoDetail.setDtlValue(dtlValue);
		tTxToDoDetail.setExcuteTxcd(excuteTxcd);
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // boolean dupSkip, int HCode
	}

}