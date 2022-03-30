package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.domain.AchDeductMediaId;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.PostDeductMediaId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.PostDeductMediaService;
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
	public AchDeductMediaService achDeductMediaService;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public PostDeductMediaService postDeductMediaService;

	@Autowired
	public BankDeductDtlService bankDeductDtlService;

	@Autowired
	public LoanChequeService loanChequeService;

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

		// 處理代碼 0:入帳 1:刪除 2.轉暫收 3.檢核(單筆入帳完畢由TxBatchCom啟動)
		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		// 會計日期、批號
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");
		iReconCode = "";
		if (titaVo.get("ReconCode") != null) {
			iReconCode = titaVo.getParam("ReconCode").trim();
		}

		if (iFunctionCode == 3) {
		} else {
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

			this.batchTransaction.commit();

			// findAll 整批入帳明細檔
			Slice<BatxDetail> slBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index,
					Integer.MAX_VALUE);
			if (slBatxDetail == null) {
				throw new LogicException("E0014", "整批入帳明細檔= null"); // E0014 檔案錯誤
			}
			List<BatxDetail> lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();
			// 員工扣薪費用先入帳
			if (lBatxDetail.get(0).getRepayCode() == 3) {
				Collections.sort(lBatxDetail, new Comparator<BatxDetail>() {
					public int compare(BatxDetail c1, BatxDetail c2) {
						// status
						if (c1.getRepayType() != c2.getRepayType()) {
							return c2.getRepayType() - c1.getRepayType();
						}
						return 0;
					}
				});
			}
			// 檢查明細狀態
			checkDetail(iFunctionCode, lBatxDetail, titaVo);
			this.batchTransaction.commitEnd();
			this.batchTransaction.init();
			boolean isUpdate = false;
			// functionCode 處理代碼 0:入帳 1:刪除 3.檢核 4.刪除回復
			// ProcStsCode 處理狀態 0.未檢核 1.不處理 2.人工處理 3.檢核錯誤 4.檢核正常 5.人工入帳 6.批次入帳 7.需轉暫收
			// 訂正使用L420C
			//
			for (BatxDetail tDetail : lBatxDetail) {
				if (!"".equals(iReconCode) && !tDetail.getReconCode().equals(iReconCode)) {
					continue;
				}
				if (isUpdate && ProcessCnt % commitCnt == 0) {
					this.batchTransaction.commitEnd();
					this.batchTransaction.init();
					isUpdate = false;
				}

				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tDetail.getProcNote());
				this.info("ProcNote=" + tDetail.getProcNote());
				switch (iFunctionCode) {

				case 0: // 0:入帳

					// 是否重新檢核
					boolean isCheck = false;

					// 同戶號合併檢核須重新檢核(入暫收款後)
					if (tTempVo.get("MergeCnt") != null && tTempVo.get("MergeSeq").equals(tTempVo.get("MergeCnt"))) {
						isCheck = true;
					}

					// 人工入帳再檢和一次
					if ("2".equals(tDetail.getProcStsCode())) {
						isCheck = true;
					}

					// 02.銀行扣款 03.員工扣款 => 1.整批檢核時設定檢核正常，整批入帳時才進行檢核
					if (tDetail.getRepayCode() == 2 || tDetail.getRepayCode() == 3) {
						if ("4".equals(tDetail.getProcStsCode())) {
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
						if (tTempVo.get("MergeCnt") != null
								&& !tTempVo.get("MergeSeq").equals(tTempVo.get("MergeCnt"))) {
							excuteTx(2, tDetail, tBatxHead, titaVo); // 轉暫收
						} else {
							excuteTx(0, tDetail, tBatxHead, titaVo); // 正常交易
						}
						isUpdate = true;
					}
					break;

				case 1: // 1:刪除
					if ("5".equals(tDetail.getProcStsCode()) || "6".equals(tDetail.getProcStsCode())
							|| "7".equals(tDetail.getProcStsCode())) {
						throw new LogicException(titaVo, "E0007", "BS401 update batxDetail 已入帳 ");
					}
					ProcessCnt++;
					tTempVo = tTempVo.getVo(tDetail.getProcNote());
					tTempVo.putParam("StsCode", tDetail.getProcStsCode());
					tDetail.setProcNote(tTempVo.getJsonString());
					tDetail.setProcStsCode("D");
					try {
						batxDetailService.update(tDetail);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007",
								"BS401 update batxDetail " + tDetail + e.getErrorMsg());
					}
					isUpdate = true;
					cancelUpdate(tDetail, tTempVo.getParam("StsCode"), titaVo);
					break;

				case 2: // 2.轉暫收
					ProcessCnt++;
					// 暫收抵繳 ， 轉暫收時直接更新、不送交易
					if ("2".equals(tDetail.getProcStsCode()) || "3".equals(tDetail.getProcStsCode())
							|| "4".equals(tDetail.getProcStsCode())) {
						ProcessCnt++;
						// 暫收抵繳 ， 轉暫收時直接更新、不送交易
						if (tDetail.getRepayCode() == 90) {
							tDetail.setProcStsCode("7");
							try {
								batxDetailService.update(tDetail);
							} catch (DBException e) {
								throw new LogicException(titaVo, "E0007",
										"BS401 update batxDetail " + tDetail + e.getErrorMsg());
							}
						} else {
							excuteTx(2, tDetail, tBatxHead, titaVo); // 轉暫收
						}
						isUpdate = true;
					}
					break;

				case 4: // 4.刪除回復
					ProcessCnt++;
					tDetail.setProcStsCode(tTempVo.getParam("StsCode"));
					try {
						batxDetailService.update(tDetail);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007",
								"BS401 update batxDetail " + tDetail + e.getErrorMsg());
					}
					isUpdate = true;
					cancelUpdate(tDetail, tTempVo.getParam("StsCode"), titaVo);
					break;
				}

			}
		}
		// 更新作業狀態
		this.batchTransaction.commitEnd();
		this.batchTransaction.init();

		String msg = updateHead(titaVo);
		this.batchTransaction.commit();

		// end
		if (iFunctionCode == 0 || iFunctionCode == 3) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002", titaVo.getTlrNo(),
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
		boolean isBankDeduct = false; // 銀行扣款入帳
		boolean isBankRmtF = false; // 匯款轉帳入帳
		boolean isTempRepay = false; // 暫收抵繳入帳

		for (BatxDetail t : lBatxDetail) {
			if (t.getRepayCode() == 2) {
				isBankDeduct = true;
			}
			if (t.getRepayCode() == 1) {
				isBankRmtF = true;
			}
			if (t.getRepayCode() == 90) {
				isTempRepay = true;
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
					tempCnt++;
				}
				doneTotalCnt++;
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
			msg += ", 轉暫收筆數 :" + tempCnt;
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

//      銀扣整批入帳、暫收抵繳整批入帳，自動轉暫收  
		if (isBankDeduct || isTempRepay) {
			if (iFunctionCode == 0 && toDoTotalCnt > 0) {
				titaVo.putParam("FunctionCode", "2");
				MySpring.newTask("BS401", this.txBuffer, titaVo);
			}
		}

		TxToDoDetail tTxToDoDetail;

//		若銀扣處理完畢，寫1筆進入應處理清單
		if ("4".equals(batxExeCode) && isBankDeduct) {
			// size > 0 -> 新增應處理明細
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4454"); // 產生銀扣扣款失敗
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

//		若匯款轉帳處理完畢，寫1筆進入應處理清單
		if ("4".equals(batxExeCode) && isBankRmtF) {
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

	private void excuteTx(int functionCode, BatxDetail tDetail, BatxHead tBatxHead, TitaVo titaVo)
			throws LogicException {
		// 組入帳交易電文
		TitaVo txTitaVo = new TitaVo();
		txTitaVo = txBatchCom.txTita(functionCode, tDetail, tBatxHead.getBatxTotCnt(), titaVo); // 正常交易電文

		// 執行入帳交易
		this.info("BS401 excuteTx " + txTitaVo);
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		apControl.callTrade(txTitaVo);
		this.addAllList(apControl.getTotaVoList());
		apControl = null;
	}

	private void cancelUpdate(BatxDetail tBatxDetail, String StsCode, TitaVo titaVo) throws LogicException {
		switch (tBatxDetail.getRepayCode()) {
		case 2:
			if ("3".equals(tBatxDetail.getMediaKind())) {
				updatePostDeduct(tBatxDetail, titaVo);
			} else {
				updateAchDeduct(tBatxDetail, titaVo);
			}
			break;
		case 3:
			updateEmpDeduct(tBatxDetail, titaVo);
			break;
		case 4:
			updateLoanCheque(tBatxDetail, StsCode, titaVo);
			break;
		}
	}

	private void updateEmpDeduct(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
//		回寫媒體檔
		EmpDeductMedia tEmpDeductMedia = empDeductMediaService.holdById(new EmpDeductMediaId(
				tBatxDetail.getMediaDate() + 19110000, tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq()), titaVo);
		if (tEmpDeductMedia == null || !tEmpDeductMedia.getBatchNo().equals(tBatxDetail.getBatchNo())) {
			return;
		}
		if ("D".equals(tBatxDetail.getProcStsCode())) {
			tEmpDeductMedia.setTxAmt(BigDecimal.ZERO);
			tEmpDeductMedia.setErrorCode("");
		} else {
			tEmpDeductMedia.setTxAmt(tBatxDetail.getRepayAmt());
			if ("00000".equals(tBatxDetail.getProcCode())) {
				tEmpDeductMedia.setErrorCode("01");
			} else {
				tEmpDeductMedia.setErrorCode(tBatxDetail.getProcCode().substring(3, 5));
			}
		}
		try {
			empDeductMediaService.update(tEmpDeductMedia, titaVo);
		} catch (DBException e) {
			e.printStackTrace();
			throw new LogicException("E0007", "EmpDeductMedia update Fail");
		}
	}

	private void updateLoanCheque(BatxDetail tBatxDetail, String StsCode, TitaVo titaVo) throws LogicException {
		// 狀態為正常才會更新支票檔
		if ("D".equals(tBatxDetail.getProcStsCode()) && !"0".equals(StsCode)) {
			return;
		}
		int chequeAcct = parse.stringToInteger(tBatxDetail.getRvNo().substring(0, 9));
		int chequeNo = parse.stringToInteger(tBatxDetail.getRvNo().substring(10, 17));
		LoanCheque tLoanCheque = loanChequeService.holdById(new LoanChequeId(chequeAcct, chequeNo), titaVo);
		if (tLoanCheque == null) {
			return;
		}
		if ("D".equals(tBatxDetail.getProcStsCode())) {
			tLoanCheque.setStatusCode("0");
			tLoanCheque.setEntryDate(0);
		} else {
			tLoanCheque.setStatusCode("4");
			tLoanCheque.setEntryDate(tBatxDetail.getEntryDate());
		}
		try {
			loanChequeService.update(tLoanCheque, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "LoanCheque update error : " + e.getErrorMsg());
		}

	}

	private void updateAchDeduct(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		// 正常回應碼為0XXXX
		if (tBatxDetail.getProcCode().isEmpty() || !"0".equals(tBatxDetail.getProcCode().substring(0, 1))) {
			return;
		}
		AchDeductMedia tAchDeductMedia = achDeductMediaService.holdById(new AchDeductMediaId(
				tBatxDetail.getMediaDate() + 19110000, tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq()), titaVo);
		if (tAchDeductMedia == null || !tAchDeductMedia.getBatchNo().equals(tBatxDetail.getBatchNo())) {
			return;
		}
		if ("D".equals(tBatxDetail.getProcStsCode())) {
			tAchDeductMedia.setReturnCode("");
		} else {
			tAchDeductMedia.setReturnCode(tBatxDetail.getProcCode().substring(3, 5));
		}
		try {
			achDeductMediaService.update(tAchDeductMedia, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "AchDeductMedia update Fail");
		}

		Slice<BankDeductDtl> sBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), 0, Integer.MAX_VALUE, titaVo);
		List<BankDeductDtl> lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tB : lBankDeductDtl) {
				BankDeductDtl tBankDeductDtl = bankDeductDtlService.holdById(tB, titaVo);
				if ("D".equals(tBatxDetail.getProcStsCode())) {
					tBankDeductDtl.setReturnCode("");
				} else {
					tBankDeductDtl.setReturnCode(tBatxDetail.getProcCode().substring(3, 5));
				}
				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "bankDeductDtlService update " + e.getErrorMsg());
				}
			}

		}
	}

	private void updatePostDeduct(BatxDetail tBatxDetail, TitaVo titaVo) throws LogicException {
		// 正常回應碼為0XXXX
		if (tBatxDetail.getProcCode().isEmpty() || !"0".equals(tBatxDetail.getProcCode().substring(0, 1))) {
			return;
		}
		PostDeductMedia tPostDeductMedia = postDeductMediaService.holdById(
				new PostDeductMediaId(tBatxDetail.getMediaDate() + 19110000, tBatxDetail.getMediaSeq()), titaVo);
		if (tPostDeductMedia == null || !tPostDeductMedia.getBatchNo().equals(tBatxDetail.getBatchNo())) {
			return;
		}
		if ("D".equals(tBatxDetail.getProcStsCode())) {
			tPostDeductMedia.setProcNoteCode("");
		} else {
			tPostDeductMedia.setProcNoteCode(tBatxDetail.getProcCode().substring(3, 5));
		}

		try {
			postDeductMediaService.update(tPostDeductMedia, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "PostDeductMedia update Fail");
		}

		Slice<BankDeductDtl> sBankDeductDtl = bankDeductDtlService.mediaSeqRng(tBatxDetail.getMediaDate() + 19110000,
				tBatxDetail.getMediaKind(), tBatxDetail.getMediaSeq(), 0, Integer.MAX_VALUE, titaVo);
		List<BankDeductDtl> lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tB : lBankDeductDtl) {
				BankDeductDtl tBankDeductDtl = bankDeductDtlService.holdById(tB, titaVo);
				if ("D".equals(tBatxDetail.getProcStsCode())) {
					tBankDeductDtl.setReturnCode("");
				} else {
					tBankDeductDtl.setReturnCode(tBatxDetail.getProcCode().substring(3, 5));
				}
				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "bankDeductDtlService update " + e.getErrorMsg());
				}
			}

		}
	}

}
