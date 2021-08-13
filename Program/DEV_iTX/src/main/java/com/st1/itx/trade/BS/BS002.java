package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.transaction.BaseTransaction;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcMainCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 會計總帳檔換日過帳 <br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS002")
@Scope("prototype")
public class BS002 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(BS002.class);

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	public TxBizDateService txBizDateService;

	@Autowired
	public AcMainCom acMainCom;

	@Autowired
	public BaseTransaction baseTransaction;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS002 ......");
		this.totaVo.init(titaVo);
		/*---------- 系統換日過帳(含年初損益類結轉) ----------*/
		acMainCom.setTxBuffer(this.txBuffer);

		// 抓總帳檔(批次日期)
		TxBizDate tTxBizDate = txBizDateService.findById("BATCH");

		this.info("BatchDate =" + tTxBizDate.getTbsDyf() + ", OnlineDate=" + this.txBuffer.getMgBizDate().getTbsDyf());
		Slice<AcMain> slAcMain = acMainService.acmainAcDateEq(tTxBizDate.getTbsDyf(), this.index, Integer.MAX_VALUE);
		List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();

		// 過總帳檔(連線日期)
		if (lAcMain != null) {
			acMainCom.changeDate(tTxBizDate.getTbsDy(), this.txBuffer.getMgBizDate().getTbsDy(), lAcMain, titaVo);
		}

		baseTransaction.commitEnd();
		return null;
	}

}