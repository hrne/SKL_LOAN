package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L420ABatch")
@Scope("prototype")
/**
 * 整批入帳－整批檢核作業<br>
 * 執行時機：經辦執行，整批檢核啟動(L420A)
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class L420ABatch extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxBatchCom txBatchCom;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public WebClient webClient;

	private int commitCnt = 20;
	private int iAcDate;
	private String iBatchNo;
	private List<BatxDetail> lBatxDetail;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420ABatch ");
		this.totaVo.init(titaVo);

		txBatchCom.setTxBuffer(this.getTxBuffer());

		// 會計日期、批號
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");

		// hold 整批入帳總數檔
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo(iBatchNo);
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null)
			throw new LogicException("E0014", tBatxHeadId + " not exist"); // E0014 檔案錯誤

		// update BatxStsCode 整批作業狀態
		tBatxHead.setBatxStsCode("1"); // 0.正常 1.整批處理中
		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "BatxHead " + tBatxHeadId + e.getErrorMsg());
		}

		// findAll 整批入帳明細檔
		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, Integer.MAX_VALUE);
		lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();
		this.batchTransaction.commit();
		if (lBatxDetail != null) {
			int cntTrans = 0;
			this.info("lBatxDetail, size=" + lBatxDetail.size());
			for (BatxDetail tDetail : lBatxDetail) {
				// 0.未檢核 3.檢核錯誤
				if ("0".equals(tDetail.getProcStsCode()) || "3".equals(tDetail.getProcStsCode())) {
					// 逐筆 call BaTxCom 計算並寫入提息明細檔 //
					if (cntTrans > this.commitCnt) {
						cntTrans = 0;
						this.batchTransaction.commit();
					}
					cntTrans++;

					// 更新整批入帳明細檔
					batxDetailService.holdById(tDetail);

					// 執行交易檢核 TxBatchCom.txCheck

					// 02.銀行扣款 03.員工扣款 => 整批檢核時設定為 4.檢核正常，整批入帳時才進行檢核
					if (tDetail.getRepayCode() == 2 || tDetail.getRepayCode() == 3) {
						tDetail.setProcStsCode("4"); // 4.檢核正常
					} else {
						tDetail = txBatchCom.txCheck(0, tDetail, titaVo);
					}
					try {
						batxDetailService.update(tDetail);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "batxDetail " + tDetail + e.getErrorMsg());
					}

				}
			}
		}

		// 更新作業狀態
		String msg = updateHead(tBatxHead, lBatxDetail, titaVo);

		// end
		this.batchTransaction.commit();
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4002", titaVo.getTlrNo(), iBatchNo + " 整批檢核, " + msg, titaVo);

		return null;

	}

	/* 更新作業狀態 */
	private String updateHead(BatxHead tHead, List<BatxDetail> lBatxDetail, TitaVo titaVo) throws LogicException {
// BatxExeCode 作業狀態 1.檢核有誤 2.檢核正常 3.入帳未完 4.入帳完成 8.已刪除		
// this.procStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入 7.虛擬轉暫收
		int manualCnt = 0; // 需人工處理, 2.人工處理
		int checkErrorCnt = 0; // 檢核錯誤 ,3.檢核錯誤
		int checkOkCnt = 0; // 待檢核, 4.檢核正常
		int doneCnt = 0; // 已入帳 5.人工入帳 6.批次入 7.虛擬轉暫收
		int unfinishCnt = 0; // 未完 2.人工處理 3.檢核錯誤 4.檢核正常 7.虛擬轉暫收

//
		for (BatxDetail tDetai : lBatxDetail) {
			switch (tDetai.getProcStsCode()) {
			case "2":
				manualCnt++;
				unfinishCnt++;
				break;
			case "3":
				checkErrorCnt++;
				unfinishCnt++;
				break;
			case "4":
				checkOkCnt++;
				unfinishCnt++;
				break;
			case "5":
				doneCnt++;
				break;
			case "6":
				doneCnt++;
				break;
			case "7":
				doneCnt++;
				unfinishCnt++;
				break;
			default:
				break;
			}
		}

		String msg = "檢核正常筆數 :" + checkOkCnt;
		if (checkErrorCnt > 0)
			msg = msg + ", 檢核錯誤筆數 :" + checkErrorCnt;
		if (doneCnt > 0)
			msg = msg + ", 已入帳筆數 :" + doneCnt;
		if (manualCnt > 0)
			msg = msg + ", 需人工處理筆數 :" + manualCnt;

		String batxExeCode = "2"; // 2.檢核正常
		if (checkErrorCnt > 0)
			batxExeCode = "1"; // 1.檢核有誤

		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(tHead.getAcDate());
		tBatxHeadId.setBatchNo(tHead.getBatchNo());
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		tBatxHead.setUnfinishCnt(unfinishCnt);
		tBatxHead.setBatxStsCode("0");
		tBatxHead.setBatxExeCode(batxExeCode);
		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "BatxHead " + tBatxHeadId + e.getErrorMsg());
		}

		return msg;
	}

}