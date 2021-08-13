package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
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
	// private static final Logger logger = LoggerFactory.getLogger(L4R10.class);
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

		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo("BATX01");

		BatxHead tBatxHead = new BatxHead();
		List<BatxHead> lBatxHead = new ArrayList<BatxHead>();
		tBatxHead = batxHeadService.findById(tBatxHeadId);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<BatxHead> sBatxHead = null;

		String batchno = "";

		this.info("txCode : " + txCode);

//		1.整批入帳 L4200 > 抓取目前最大的批號+1
//		2.其他來源 L4210 > 如果有4210&非刪除者，則抓取該批號，否則抓取目前最大的批號+1
		if (tBatxHead == null) {
			this.totaVo.putParam("L4r10BatchNo", "BATX01");
		} else {
			sBatxHead = batxHeadService.acDateRange(iAcDate, iAcDate, this.index, this.limit);

			lBatxHead = sBatxHead == null ? null : sBatxHead.getContent();

			int listSize = lBatxHead.size() + 1;
			if ("L4200".equals(txCode)) {
				batchno = "BATX" + FormatUtil.pad9("" + listSize, 2);

				this.totaVo.putParam("L4r10BatchNo", batchno);
			} else if ("L4210".equals(txCode)) {
				for (BatxHead t2BatxHead : lBatxHead) {
					if (txCode.equals(t2BatxHead.getTitaTxCd())) {
//						20210414 不同櫃員建立不同批號 by 賴桑
						if (!"8".equals(t2BatxHead.getBatxExeCode())
								&& t2BatxHead.getTitaTlrNo().equals(titaVo.getTlrNo())) {
							batchno = t2BatxHead.getBatchNo();
							break;
						} else {
							batchno = "BATX" + FormatUtil.pad9("" + listSize, 2);
						}
					} else {
						batchno = "BATX" + FormatUtil.pad9("" + listSize, 2);
					}
				}
			}

			this.totaVo.putParam("L4r10BatchNo", batchno);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}