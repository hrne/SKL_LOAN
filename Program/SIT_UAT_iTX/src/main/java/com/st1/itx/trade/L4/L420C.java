package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L420C")
@Scope("prototype")
/**
 * 虛擬轉暫收
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L420C extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public TxBatchCom txBatchCom;
	private int acDate = 0;
	private String batchNo = "";
	private int detailSeq = 0;
	private int finishCnt = 0;
	private int functionCode = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420C ");
		this.totaVo.init(titaVo);
		txBatchCom.setTxBuffer(this.getTxBuffer());

		acDate = parse.stringToInteger(titaVo.getParam("OOAcDate")) + 19110000;
		batchNo = titaVo.getParam("OOBatchNo");

		detailSeq = parse.stringToInteger(titaVo.getParam("OODetailSeq"));
		// 處理代碼 1:訂正 2:虛擬轉暫收
		functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		this.info("AcDate : " + acDate);
		this.info("BatchNo : " + batchNo);
		this.info("DetailSeq : " + detailSeq);
		this.info("functionCode =" + titaVo.getParam("FunctionCode"));

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(acDate);
		tBatxDetailId.setBatchNo(batchNo);
		tBatxDetailId.setDetailSeq(detailSeq);

		tBatxDetail = batxDetailService.findById(tBatxDetailId);
		if (tBatxDetail == null) {
			throw new LogicException("E0014", tBatxDetailId + " hold not exist"); // 檔案錯誤
		}
		boolean isUpdate = false;
		String procStsCode = tBatxDetail.getProcStsCode();
//		1.訂正
//		2.轉暫收  7.虛擬轉暫收改為6.批次入帳，未入帳則執行L3210
		if (functionCode == 1 && "5".equals(tBatxDetail.getProcStsCode())) {
			throw new LogicException("E0015", tBatxDetail.getDetailSeq() + "非整批入帳，請執行交易訂正"); // 檢查錯誤
		}

		// 2.轉暫收 7.虛擬轉暫收改為6、 BS020 暫收抵繳 改為1
		if (functionCode == 2) {
			if ("7".equals(tBatxDetail.getProcStsCode())) {
				isUpdate = true;
				procStsCode = "6";
				finishCnt++;
			}
			if ("BS020".equals(tBatxDetail.getFileName())) {
				isUpdate = true;
				procStsCode = "6";
				finishCnt++;
			}
		}

		if (functionCode == 1) {
			if ("BS020".equals(tBatxDetail.getFileName()) && "".equals(tBatxDetail.getTitaTxtNo())) {
				isUpdate = true;
				procStsCode = "0";
				finishCnt--;
			} else {
				// 組入帳交易電文
				TitaVo txTitaVo = new TitaVo();
				txTitaVo = txBatchCom.txTita(functionCode, tBatxDetail, titaVo); // 1:訂正 2:虛擬轉暫收改

				// 執行入帳交易
				this.info("L420c excuteTx " + txTitaVo);
				// MySpring.newTask("apControl", this.txBuffer, txTitaVo);
				TotaVoList totaVoList = MySpring.newTaskFuture("apControl", this.txBuffer, txTitaVo);

				/* 錯誤 */
				if (totaVoList != null && totaVoList.size() > 0) {
					if (totaVoList.get(0).isError())
						throw new LogicException(totaVoList.get(0).getMsgId(), totaVoList.get(0).getErrorMsg());
				}
			}
		}

		if (isUpdate) {
			tBatxDetail = batxDetailService.holdById(tBatxDetailId);
			tBatxDetail.setProcStsCode(procStsCode);
			try {
				batxDetailService.update(tBatxDetail);
			} catch (DBException e) {
				e.printStackTrace();
			}
			updateHeadRoutine(titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void updateHeadRoutine(TitaVo titaVo) throws LogicException {
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(acDate);
		tBatxHeadId.setBatchNo(batchNo);
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null) {
			throw new LogicException("E0014", tBatxHeadId + " hold not exist"); // E0014 檔案錯誤
		}

		if (functionCode == 1) {
			tBatxHead.setBatxExeCode("0");// 0.待檢核
		}

		tBatxHead.setUnfinishCnt(tBatxHead.getUnfinishCnt() - finishCnt);

		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
		}

		// 啟動背景作業－整批入帳完成
		if (finishCnt > 0 && tBatxHead.getUnfinishCnt() == 0) {
			TitaVo bs401TitaVo = new TitaVo();
			bs401TitaVo = (TitaVo) titaVo.clone();
			bs401TitaVo.putParam("FunctionCode", "0");// 處理代碼 0:入帳
			bs401TitaVo.putParam("AcDate", acDate - 19110000); // 會計日期
			bs401TitaVo.putParam("BatchNo", batchNo);// 批號
			MySpring.newTask("BS401", this.txBuffer, bs401TitaVo);
		}

	}
}
