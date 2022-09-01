package com.st1.itx.trade.L4;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RimAcDate=9,7<br>
 */

@Service("L4R10")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R10 extends TradeBuffer {
	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R10 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String txCode = titaVo.getParam("RimTxCode");
		this.info("txCode : " + txCode);
		String batchNo = "";
//		2.其他來源建檔 L4210 > 如果有L4210&非刪除者，同經辦則抓取該批號，否則抓取目前最大的批號+1
		if ("L4210".equals(txCode)) {
			Slice<BatxHead> slBatxHead = batxHeadService.acDateRange(iAcDate, iAcDate, 0, Integer.MAX_VALUE);
			for (BatxHead t2BatxHead : slBatxHead.getContent()) {
				if (txCode.equals(t2BatxHead.getTitaTxCd())) {
					if (!"8".equals(t2BatxHead.getBatxExeCode())
							&& t2BatxHead.getTitaTlrNo().equals(titaVo.getTlrNo())) {
						batchNo = t2BatxHead.getBatchNo();
					}
				}
			}
		}

		// 抓取目前最大的批號+1
		if (batchNo.isEmpty()) {
			BatxHead tBatxHead = batxHeadService.batchNoDescFirst(iAcDate, "BATX%", titaVo);
			if (tBatxHead == null)
				batchNo = "BATX01";
			else
				batchNo = "BATX"
						+ parse.IntegerToString(parse.stringToInteger(tBatxHead.getBatchNo().substring(4)) + 1, 2);
		}

		this.totaVo.putParam("L4r10BatchNo", batchNo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}