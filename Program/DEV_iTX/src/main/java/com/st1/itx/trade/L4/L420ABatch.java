package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
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
	private String iReconCode;
	private List<BatxDetail> lBatxDetail;
	private HashMap<Integer, Integer> mergeCnt = new HashMap<>();
	private HashMap<Integer, BigDecimal> mergeAmt = new HashMap<>();
	private HashMap<Integer, String> mergeProcStsCode = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420ABatch ");
		this.totaVo.init(titaVo);

		txBatchCom.setTxBuffer(this.getTxBuffer());

		// 會計日期、批號
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");
		iReconCode = titaVo.getParam("ReconCode").trim();
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
		this.batchTransaction.commit();
		// findAll 整批入帳明細檔
		Slice<BatxDetail> slBatxDetail = batxDetailService.findL4002AEq(iAcDate, iBatchNo, this.index,
				Integer.MAX_VALUE);
		lBatxDetail = slBatxDetail == null ? null : slBatxDetail.getContent();
		List<BatxDetail> l1BatxDetail = new ArrayList<BatxDetail>();
		List<BatxDetail> l2BatxDetail = new ArrayList<BatxDetail>();
		if (lBatxDetail != null) {
			for (BatxDetail t : lBatxDetail) {
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					if ("0".equals(t.getProcStsCode()) || "2".equals(t.getProcStsCode())
							|| "3".equals(t.getProcStsCode()) || "4".equals(t.getProcStsCode())) {
						// 匯款轉帳期款
						if (t.getRepayCode() == 1 && t.getRepayType() == 1) {
							if (mergeAmt.containsKey(t.getCustNo())) {
								mergeCnt.put(t.getCustNo(), mergeCnt.get(t.getCustNo()) + 1);
								mergeAmt.put(t.getCustNo(), mergeAmt.get(t.getCustNo()).add(t.getRepayAmt()));
								if (!"4".equals(t.getProcStsCode())) {
									mergeProcStsCode.put(t.getCustNo(), t.getProcStsCode());
								}
							} else {
								mergeCnt.put(t.getCustNo(), 1);
								mergeAmt.put(t.getCustNo(), t.getRepayAmt());
								mergeProcStsCode.put(t.getCustNo(), t.getProcStsCode());
							}
						}
					}
				}
			}

			for (BatxDetail t : lBatxDetail) {
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					if ("0".equals(t.getProcStsCode()) || "2".equals(t.getProcStsCode())
							|| "3".equals(t.getProcStsCode()) || "4".equals(t.getProcStsCode())) {
						// 檢核正常，不需再檢核
						// 匯款轉帳期款多筆
						if (t.getRepayCode() == 1 && t.getRepayType() == 1 && mergeCnt.get(t.getCustNo()) >= 2) {
							if ("4".equals(mergeProcStsCode.get(t.getCustNo()))) {
								continue;
							} else {
								l2BatxDetail.add(t);
							}
						} else {
							if ("4".equals(t.getProcStsCode())) {
								continue;
							} else {
								l1BatxDetail.add(t);
							}
						}
					}
				}
			}
		}

		int cntTrans = 0;
		// 單筆
		if (l1BatxDetail.size() > 0) {
			for (BatxDetail tDetail : l1BatxDetail) {
				this.info("L1 BatxDetail=" + tDetail.toString());
				if (cntTrans > this.commitCnt) {
					cntTrans = 0;
					this.batchTransaction.commit();
				}
				cntTrans++;
				// 更新整批入帳明細檔
				batxDetailService.holdById(tDetail);
				// 02.銀行扣款 03.員工扣款 => 整批檢核時設定為 4.檢核正常，整批入帳時才進行檢核
				if (tDetail.getRepayCode() == 2 || tDetail.getRepayCode() == 3) {
					TempVo tTempVo = new TempVo();
					tTempVo = tTempVo.getVo(tDetail.getProcNote());
					tTempVo.putParam("CheckMsg", "");
					tTempVo.putParam("ErrorMsg", "");
					tDetail.setProcNote(tTempVo.getJsonString());
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

		// 匯款轉帳期款多筆
		if (l2BatxDetail.size() > 0) {
			int mergeSeq = 0;
			List<BatxDetail> l3BatxDetail = new ArrayList<BatxDetail>();
			for (BatxDetail tDetail : l2BatxDetail) {
				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tDetail.getProcNote());
				mergeSeq++;
				this.info("L2BatxDetail = " + tDetail.getCustNo() + ", mergeSeq =" + mergeSeq + " ,mergeCnt="
						+ mergeCnt.get(tDetail.getCustNo()));
				if (mergeSeq < mergeCnt.get(tDetail.getCustNo())) {
					l3BatxDetail.add(tDetail);
				} else {
					// 檢核總金額
					batxDetailService.holdById(tDetail);
					BigDecimal repayAmt = tDetail.getRepayAmt();
					tDetail.setRepayAmt(mergeAmt.get(tDetail.getCustNo()));
					tDetail = txBatchCom.txCheck(0, tDetail, titaVo);
					// 還原金額
					tTempVo = tTempVo.getVo(tDetail.getProcNote());
					tTempVo.putParam("CheckMsg",
							"同戶號合併檢核 總金額:" + tDetail.getRepayAmt() + ", " + tTempVo.getParam("CheckMsg"));
					tTempVo.putParam("MergeCnt", mergeCnt.get(tDetail.getCustNo()));
					tTempVo.putParam("MergeAmt", mergeAmt.get(tDetail.getCustNo()));
					tTempVo.putParam("MergeSeq", mergeSeq);
					mergeSeq = 0;
					tDetail.setProcNote(tTempVo.getJsonString());
					tDetail.setRepayAmt(repayAmt);
					try {
						batxDetailService.update(tDetail);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "lA2BatxDetail" + e.getErrorMsg());
					}
					// 更新同戶號狀態
					if (l3BatxDetail.size() > 0) {
						tTempVo.putParam("CheckMsg", "同戶號合併檢核");
						mergeSeq = 0;
						for (BatxDetail t : l3BatxDetail) {
							mergeSeq++;
							tTempVo.putParam("MergeSeq", mergeSeq);
							t.setProcNote(tTempVo.getJsonString());
							t.setProcStsCode(tDetail.getProcStsCode());
						}
						try {
							batxDetailService.updateAll(l3BatxDetail);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "lA2BatxDetail" + e.getErrorMsg());
						}
						l3BatxDetail = new ArrayList<BatxDetail>();
					}
					this.batchTransaction.commit();
				}
			}
		}

		// 更新作業狀態
		String msg = updateHead(tBatxHead, lBatxDetail, titaVo);

		// end
		this.batchTransaction.commit();
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002", titaVo.getTlrNo(),
				iBatchNo + " 整批檢核, " + msg, titaVo);

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
		//
		int unfinishTotalCnt = 0; // 未完 2.人工處理 3.檢核錯誤 4.檢核正常 7.虛擬轉暫收
		int checkErrorTotalCnt = 0; // 檢核錯誤 ,3.檢核錯誤

//
		for (BatxDetail tDetail : lBatxDetail) {
			this.info("ReconCode = " + iReconCode + "," + tDetail.getReconCode());
			switch (tDetail.getProcStsCode()) {
			case "2":
				if ("".equals(iReconCode) || tDetail.getReconCode().equals(iReconCode)) {
					manualCnt++;
				}
				unfinishTotalCnt++;
				break;
			case "3":
				if ("".equals(iReconCode) || tDetail.getReconCode().equals(iReconCode)) {
					checkErrorCnt++;
				}
				unfinishTotalCnt++;
				checkErrorTotalCnt++;
				break;
			case "4":
				if ("".equals(iReconCode) || tDetail.getReconCode().equals(iReconCode)) {
					checkOkCnt++;
				}
				unfinishTotalCnt++;
				break;
			case "5":
			case "6":
			case "7":
				if ("".equals(iReconCode) || tDetail.getReconCode().equals(iReconCode)) {
					doneCnt++;
				}
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
		if (checkErrorTotalCnt > 0) {
			batxExeCode = "1"; // 1.檢核有誤
		}

		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(tHead.getAcDate());
		tBatxHeadId.setBatchNo(tHead.getBatchNo());
		BatxHead tBatxHead = batxHeadService.holdById(tBatxHeadId);
		// BatxStsCode 整批作業狀態 0.正常 1.整批處理中
		tBatxHead.setUnfinishCnt(unfinishTotalCnt);
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