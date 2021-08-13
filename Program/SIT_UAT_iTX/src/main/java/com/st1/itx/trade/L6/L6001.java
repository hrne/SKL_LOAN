package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6001")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6001 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6001.class);

	/* DB服務注入 */
	@Autowired
	public TxToDoMainService sTxToDoMainService;

	/* DB服務注入 */
	@Autowired
	public TxToDoDetailService sTxToDoDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6001 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// new ArrayList
		List<TxToDoMain> lTxToDoMain = new ArrayList<TxToDoMain>();
		// new OccursList
		OccursList OccursList = new OccursList();

		Slice<TxToDoMain> slTxToDoMain = sTxToDoMainService.findAll(this.index, this.limit);
		lTxToDoMain = slTxToDoMain == null ? null : slTxToDoMain.getContent();

		if (lTxToDoMain == null) {
			throw new LogicException(titaVo, "E2003", "應處理事項清單");
		}

		for (TxToDoMain tTxToDoMain : lTxToDoMain) {
			// eric 2021.2.4 for 定審
			if ("AMLH".equals(tTxToDoMain.getItemCode())) {
				//放款部.審查課
				if (!("0000".equals(titaVo.get("KINBR")) && "4".equals(this.txBuffer.getTxCom().getTlrDept()) )) {
					continue;	
				}
			}
			if ("AMLM".equals(tTxToDoMain.getItemCode()) || "AMLL".equals(tTxToDoMain.getItemCode())) {
				//放款部.服務課
				if (!("0000".equals(titaVo.get("KINBR")) && "2".equals(this.txBuffer.getTxCom().getTlrDept()) )) {
					continue;	
				}
			}

			// new occurslist
			OccursList = new OccursList();

			OccursList.putParam("ItemCode", tTxToDoMain.getItemCode());
			OccursList.putParam("ItemDesc", tTxToDoMain.getItemDesc());
			OccursList.putParam("ChainInqTxcd", tTxToDoMain.getChainInqTxcd());
			OccursList.putParam("ChainUpdTxcd", tTxToDoMain.getChainUpdTxcd());
			OccursList.putParam("YdReserveFg", tTxToDoMain.getYdReserveFg());
			OccursList.putParam("YdReserve", tTxToDoMain.getYdReserveCnt());
			OccursList.putParam("TdNew", tTxToDoMain.getTdNewCnt());
			OccursList.putParam("TotalCnt", tTxToDoMain.getYdReserveCnt() + tTxToDoMain.getTdNewCnt());
			OccursList.putParam("TdProcess", tTxToDoMain.getTdProcessCnt());
			OccursList.putParam("TdDelete", tTxToDoMain.getTdDeleteCNT());
			OccursList.putParam("DeleteFg", tTxToDoMain.getDeleteFg());
			OccursList.putParam("ReserveCnt", tTxToDoMain.getReserveCnt());
			OccursList.putParam("ReserveFg", tTxToDoMain.getReserveFg());
			OccursList.putParam("UnProcessCnt", tTxToDoMain.getUnProcessCnt());
			OccursList.putParam("AcClsCheck", tTxToDoMain.getAcClsCheck());
			OccursList.putParam("AutoFg", tTxToDoMain.getAutoFg());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(OccursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}