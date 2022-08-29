package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
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
import com.st1.itx.util.common.SendRsp;
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
	private int tmpFinishCnt = 0;
	private int functionCode = 0;

	/**
	 * 0.轉暫收 </br>
	 * 1.入帳 </br>
	 */
	private int btnIndex = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420C ");
		this.totaVo.init(titaVo);
		txBatchCom.setTxBuffer(this.getTxBuffer());

		acDate = parse.stringToInteger(titaVo.getParam("OOAcDate")) + 19110000;
		batchNo = titaVo.getParam("OOBatchNo");

		detailSeq = parse.stringToInteger(titaVo.getParam("OODetailSeq"));
		// 處理代碼 0:入帳 1:訂正 2:轉暫收
		functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		btnIndex = parse.stringToInteger(titaVo.getBtnIndex());

		if (btnIndex == 1) {
			functionCode = 0; // 入帳
			titaVo.putParam("FunctionCode", 0);
		}

		this.info("AcDate : " + acDate);
		this.info("BatchNo : " + batchNo);
		this.info("DetailSeq : " + detailSeq);
		this.info("functionCode =" + titaVo.getParam("FunctionCode"));
		this.info("btnIndex ..." + btnIndex);

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(acDate);
		tBatxDetailId.setBatchNo(batchNo);
		tBatxDetailId.setDetailSeq(detailSeq);

		tBatxDetail = batxDetailService.findById(tBatxDetailId);
		if (tBatxDetail == null) {
			throw new LogicException("E0014", tBatxDetailId + " hold not exist"); // 檔案錯誤
		}
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(acDate);
		tBatxHeadId.setBatchNo(batchNo);
		BatxHead tBatxHead = batxHeadService.findById(tBatxHeadId);
		if (tBatxHead == null) {
			throw new LogicException("E0014", tBatxHeadId + " hold not exist"); // 檔案錯誤
		}
		// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		if ("1".equals(tBatxHead.getBatxStsCode())) {
			throw new LogicException("E0014", batchNo + "整批處理中 "); // 檔案錯誤
		}
		boolean isUpdate = false;
		String procStsCode = tBatxDetail.getProcStsCode();
//		1.訂正功能<取消勾選功能>
		if (functionCode == 1) {
			throw new LogicException("E0015", tBatxDetail.getDetailSeq() + "請執行交易訂正"); // 檢查錯誤
		}
		// 2:轉暫收
		if (functionCode == 2) {
			tBatxDetail.setRepayType(9);
		}
		// 暫收抵繳 ， 轉暫收時直接更新、不送交易
		if (tBatxDetail.getRepayCode() == 90) {
			if (functionCode == 2) {
				isUpdate = true;
				procStsCode = "1"; // 失敗不處理
				tmpFinishCnt++;
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
		} else {
			// 入帳一律再檢核一次，移除匯款轉帳同戶號多筆檢核
			tBatxDetail.setProcStsCode("0");
			TempVo tTempVo = new TempVo();
			if (functionCode == 0) {
				tTempVo = tTempVo.getVo(tBatxDetail.getProcNote());
				tTempVo.remove("MergeCnt");
				tTempVo.remove("MergeAmt");
				tTempVo.remove("MergeSeq");
				tBatxDetail.setProcNote(tTempVo.getJsonString());
				tBatxDetail = txBatchCom.txCheck(0, tBatxDetail, titaVo);
				if (!"4".equals(tBatxDetail.getProcStsCode())) {
					tTempVo = tTempVo.getVo(tBatxDetail.getProcNote());
					throw new LogicException("E0010", tTempVo.getParam("CheckMsg")); // 功能選擇錯誤
				}
			}
			// 組入帳交易電文
			TitaVo txTitaVo = new TitaVo();
			txTitaVo = txBatchCom.txTita(functionCode, tBatxDetail, tBatxHead.getBatxTotCnt(), titaVo); // 1:訂正
			// 執行入帳交易
			if (functionCode == 0) {
				tTempVo = tTempVo.getVo(tBatxDetail.getProcNote());
				txTitaVo.put("CheckMsg", tTempVo.getParam("CheckMsg"));
			}
			this.info("L420C excuteTx " + txTitaVo);
			// MySpring.newTask("apControl", this.txBuffer, txTitaVo);
			TotaVoList totaVoList = MySpring.newTaskFuture("apControl", this.txBuffer, txTitaVo);
			/* 錯誤 */
			if (totaVoList != null && totaVoList.size() > 0) {
				if (totaVoList.get(0).isError()) {
					// 先去錯誤訊訊息中文，LogicException會再組一次
					String errmsg = totaVoList.get(0).getErrorMsg();
					int iBeg = 0;
					int iEnd = errmsg.length();
					for (int i = 0; i < errmsg.length(); i++) {
						if (iBeg == 0 && "(".equals(errmsg.substring(i, i + 1))) {
							iBeg = i + 1;
						}
						if (")".equals(errmsg.substring(i, i + 1))) {
							iEnd = i;
						}
					}
					errmsg = errmsg.substring(iBeg, iEnd);
					throw new LogicException(totaVoList.get(0).getMsgId(), errmsg);
				}
			}
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
		tBatxHead.setUnfinishCnt(tBatxHead.getUnfinishCnt() - tmpFinishCnt);

		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "update BatxHead " + tBatxHead + e.getErrorMsg());
		}

		// 啟動背景作業－整批入帳完成
		if (tmpFinishCnt > 0 && tBatxHead.getUnfinishCnt() == 0) {
			TitaVo bs401TitaVo = new TitaVo();
			bs401TitaVo = (TitaVo) titaVo.clone();
			bs401TitaVo.putParam("FunctionCode", "3");// 處理代碼 3.檢核
			bs401TitaVo.putParam("AcDate", acDate - 19110000); // 會計日期
			bs401TitaVo.putParam("BatchNo", batchNo);// 批號
			MySpring.newTask("BS401", this.txBuffer, bs401TitaVo);
		}

	}
}
