package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.main.ApControl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS401")
@Scope("prototype")
/**
 * 整批入帳 執行時機：經辦執行，整批入帳啟動(L420B)
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class BS401 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxBatchCom txBatchCom;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public WebClient webClient;

	private int commitCnt = 1;
	private int ProcessCnt = 0;
	private int iFunctionCode;
	private int iAcDate;
	private String iBatchNo;
	private String iReconCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS401 ");

		txBatchCom.setTxBuffer(this.getTxBuffer());
		txToDoCom.setTxBuffer(this.getTxBuffer());

		// 處理代碼 0:入帳 1:刪除 2:訂正
		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		// 會計日期、批號
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");
		iReconCode = titaVo.getParam("ReconCode").trim();

		// hold 整批入帳總數檔
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo(iBatchNo);
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null) {
			throw new LogicException("E0014", tBatxHeadId + " not exist"); // E0014 檔案錯誤
		}
		// update BatxStsCode 整批作業狀態
		tBatxHead.setBatxStsCode("1"); // 0.正常 1.整批處理中
		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "BS401 update BatxHead " + tBatxHeadId + e.getErrorMsg());
		}

		// findAll 整批入帳明細檔
		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index,
				Integer.MAX_VALUE);
		List<BatxDetail> lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();

		// 檢查明細狀態
		checkDetail(iFunctionCode, lBatxDetail, titaVo);
		this.batchTransaction.commitEnd();
		this.batchTransaction.init();
		boolean isUpdate = false;
		// functionCode 處理代碼 0:入帳 1:刪除 4.刪除回復
		// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.需轉暫收
		// 訂正使用L420C
		//
		TempVo tTempVo = new TempVo();
		for (BatxDetail tDetail : lBatxDetail) {
			if (!"".equals(iReconCode) && !tDetail.getReconCode().equals(iReconCode)) {
				continue;
			}
			if (isUpdate && ProcessCnt % commitCnt == 0) {
				this.batchTransaction.commitEnd();
				this.batchTransaction.init();
				isUpdate = false;
			}
			switch (iFunctionCode) {

			case 0: // 0:入帳
				// 02.銀行扣款 03.員工扣款 => 1.整批檢核時設定檢核正常，整批入帳時才進行檢核 2.人工入帳則再檢核一次
				boolean isCheck = false;
				if ("3".equals(tDetail.getProcStsCode())) {
					isCheck = true;
				}
				if (tDetail.getRepayCode() == 2 || tDetail.getRepayCode() == 3) {
					if ("4".equals(tDetail.getProcStsCode()) || "2".equals(tDetail.getProcStsCode())) {
						isCheck = true;
					}
				}
				if (isCheck) {
					tDetail = txBatchCom.txCheck(0, tDetail, titaVo);
					if (!"4".equals(tDetail.getProcStsCode())) {
						ProcessCnt++;
						try {
							batxDetailService.update(tDetail);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007",
									"BS401 update batxDetail " + tDetail + e.getErrorMsg());
						}
						isUpdate = true;
					}
				}
				if ("4".equals(tDetail.getProcStsCode())) {
					ProcessCnt++;
					excuteTx(0, tDetail, titaVo); // 正常交易
					isUpdate = true;
				}
				break;

			case 1: // 1:刪除
				ProcessCnt++;
				tTempVo = tTempVo.getVo(tDetail.getProcNote());
				tTempVo.putParam("StsCode", tDetail.getProcStsCode());
				tDetail.setProcNote(tTempVo.getJsonString());
				tDetail.setProcStsCode("D");
				try {
					batxDetailService.update(tDetail);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "BS401 update batxDetail " + tDetail + e.getErrorMsg());
				}
				isUpdate = true;
				break;

			case 4: // 4.刪除回復
				ProcessCnt++;
				tTempVo = tTempVo.getVo(tDetail.getProcNote());
				tDetail.setProcStsCode(tTempVo.getParam("StsCode"));
				try {
					batxDetailService.update(tDetail);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "BS401 update batxDetail " + tDetail + e.getErrorMsg());
				}
				isUpdate = true;
				break;
			}

		}

		// 更新作業狀態
		this.batchTransaction.commitEnd();
		this.batchTransaction.init();

		String msg = updateHead(titaVo);
		this.batchTransaction.commit();

		// end
		if (iFunctionCode == 0) { // 0:入帳
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4002", titaVo.getTlrNo(),
					iBatchNo + " 整批入帳, " + msg, titaVo);
		}
		return this.sendList();
	}

	/* 更新作業狀態 */
	private String updateHead(TitaVo titaVo) throws LogicException {

		// findAll 整批入帳明細檔
		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index,
				Integer.MAX_VALUE);
		List<BatxDetail> lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();
// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳  7.虛擬轉暫收

		int doneCnt = 0; // 已入帳
		int manualCnt = 0; // 需人工處理
		int unCheckCnt = 0; // 待檢核
		int unDoCnt = 0; // 未處理
		int checkErrorCnt = 0; // 檢核錯誤
		int toDoTotalCnt = 0; // 未完需處理
		int doneTotalCnt = 0; // 已入帳
		int unCheckTotalCnt = 0; // 待檢核
		int checkErrorTotalCnt = 0; // 檢核錯誤
		int tempCnt = 0; // 需轉暫收
		boolean bankDeductFlag = false; // 銀扣交易失敗記號 0:成功 1:有一筆失敗以上
		boolean bankRmtFlag = false; // 匯款轉帳

		for (BatxDetail t : lBatxDetail) {
			if (t.getRepayCode() == 2) {
				bankDeductFlag = true;
			}
			if (t.getRepayCode() == 1) {
				bankRmtFlag = true;
			}
			switch (t.getProcStsCode()) {
			case "0": // 待檢核
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					unCheckCnt++;
				}
				toDoTotalCnt++;
				unCheckTotalCnt++;
				break;
			case "2":
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					manualCnt++;
				}
				toDoTotalCnt++;
				break;
			case "3": // 錯誤
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					checkErrorCnt++;
				}
				toDoTotalCnt++;
				checkErrorTotalCnt++;
				break;
			case "4": // 未處理
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					unDoCnt++;
				}
				toDoTotalCnt++;
				break;
			case "5":
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					doneCnt++;
				}
				doneTotalCnt++;
				break;
			case "6":
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					doneCnt++;
				}
				doneTotalCnt++;
				break;
			case "7":
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					doneCnt++;
					tempCnt++;
				}
				doneTotalCnt++;
				toDoTotalCnt++;
				break;
			}
		}
// BatxExeCode 作業狀態 0.待檢核 1.檢核有誤 2.檢核正常 3.入帳未完 4.入帳完成 8.已刪除
// 處理代碼 0:入帳 1:刪除 2:訂正 4.刪除回復
		String batxExeCode = null;
		String msg = "已入帳筆數 :" + doneCnt;
		if (unCheckCnt > 0) {
			msg += ", 待檢核筆數 :" + unCheckCnt;
		}
		if (checkErrorCnt > 0) {
			msg += ", 錯誤筆數 :" + checkErrorCnt;
		}
		if (manualCnt > 0) {
			msg += ", 需人工處理筆數 :" + manualCnt;
		}
		if (unDoCnt > 0) {
			msg += ", 未處理筆數 :" + unDoCnt;
		}
		if (tempCnt > 0) {
			msg += ", 待轉暫收筆數 :" + tempCnt;
		}
		//
		switch (iFunctionCode) {
		case 1:
			if (doneTotalCnt > 0)
				throw new LogicException("E0010", " 已入帳筆數：" + doneCnt++); // E0010 功能選擇錯誤
			else
				batxExeCode = "8";
			break;
		default:
			if (checkErrorTotalCnt > 0)
				batxExeCode = "1";
			else if (unCheckTotalCnt > 0)
				batxExeCode = "0";
			else if (toDoTotalCnt > 0)
				batxExeCode = "3";
			else
				batxExeCode = "4";
		}
		this.info("BS401 updateHead batxExeCode = " + batxExeCode + ", unCheckTotalCnt=" + unCheckTotalCnt
				+ ", checkErrorTotalCnt=" + checkErrorTotalCnt + ", toDoTotalCnt=" + toDoTotalCnt + ", doneTotalCnt="
				+ doneTotalCnt);
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo(iBatchNo);
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null)
			throw new LogicException("E0014", tBatxHeadId + " hold not exist"); // E0014 檔案錯誤
		tBatxHead.setBatxExeCode(batxExeCode);
		// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		tBatxHead.setBatxStsCode("0");
		try {
			batxHeadService.update(tBatxHead);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "TxBatchCom update BatxHead " + tBatxHead + e.getErrorMsg());
		}

		TxToDoDetail tTxToDoDetail;

//		若銀扣處理完畢，寫1筆進入應處理清單
		if ("4".equals(batxExeCode) && bankDeductFlag) {
			// size > 0 -> 新增應處理明細
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4454"); // 產生銀扣扣款失敗
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

//		若匯款轉帳處理完畢，寫1筆進入應處理清單
		if ("4".equals(batxExeCode) && bankRmtFlag) {
			// size > 0 -> 新增應處理明細
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4702"); // 產生繳息通知單
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過

			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4703"); // 產生滯繳通知單
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
		return msg;
	}

	/* 檢查明細狀態 */
	private void checkDetail(int functionCode, List<BatxDetail> lBatxDetail, TitaVo titaVo) throws LogicException {
// 處理代碼 0:入帳 1:刪除 2:訂正 4.刪除回復
// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.虛擬轉暫收

		if (functionCode == 1) {
			for (BatxDetail t : lBatxDetail) {
				if ("5".equals(t.getProcStsCode()) || "6".equals(t.getProcStsCode()) || "7".equals(t.getProcStsCode()))
					throw new LogicException("E0010", "已有入帳成功資料，請執行<整批訂正>"); // E0010 功能選擇錯誤
			}
		}
	}

	private void excuteTx(int Hcode, BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		// 組入帳交易電文
		TitaVo txTitaVo = new TitaVo();
		txTitaVo = txBatchCom.txTita(Hcode, tDetail, titaVo); // 正常交易電文

		// 執行入帳交易
		this.info("BS401 excuteTx " + txTitaVo);
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		apControl.callTrade(txTitaVo);
		this.addAllList(apControl.getTotaVoList());
		apControl = null;
	}

}
