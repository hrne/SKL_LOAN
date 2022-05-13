package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.TxBatchCom;
import com.st1.itx.util.common.data.BaTxVo;
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
	private BaTxCom baTxCom;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public WebClient webClient;

	@Autowired
	public L4211Report l4211Report;

	private int commitCnt = 20;
	private int iAcDate;
	private String iBatchNo;
	private String iReconCode;
	private String mapKey;
	private HashMap<String, Integer> mergeCnt = new HashMap<>();
	private HashMap<String, BigDecimal> mergeAmt = new HashMap<>();
	private HashMap<String, String> mergeProcStsCode = new HashMap<>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private List<Map<String, String>> l4211MapList = new ArrayList<Map<String, String>>();
	private TempVo tTempVo = new TempVo();
	private List<BatxDetail> lBatxDetail;
	private List<BatxDetail> l1BatxDetail;
	private List<BatxDetail> l2BatxDetail;
	private List<BatxDetail> l3BatxDetail;
	private Slice<BatxDetail> slBatxDetail;
	boolean isRepaytypeNotEqual = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420ABatch ");
		this.totaVo.init(titaVo);

		txBatchCom.setTxBuffer(this.getTxBuffer());
		baTxCom.setTxBuffer(this.getTxBuffer());

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
		slBatxDetail = batxDetailService.findL4002AEq(iAcDate, iBatchNo, this.index, Integer.MAX_VALUE);

		doCheckAll(titaVo);

		// 合併的還款類別設定為費用則須再全部檢核一次
		if (isRepaytypeNotEqual) {
			this.info("RepaytypeNotEqual check again ...");
			this.l4211MapList = new ArrayList<Map<String, String>>();
			doCheckAll(titaVo);
		}
		// 更新作業狀態
		String msg = updateHead(tBatxHead, slBatxDetail.getContent(), titaVo);

		// end
		this.batchTransaction.commit();
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002", titaVo.getTlrNo(),
				iBatchNo + " 整批檢核, " + msg, titaVo);

		// 匯款轉帳檢核明細表
		if (l4211MapList.size() > 0) {
			// 產生匯款轉帳檢核明細表
			l4211Report.setParentTranCode("L420A");
			l4211Report.execWithBatchMapList(l4211MapList, titaVo);
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L420A", "L4211-匯款轉帳檢核明細表", titaVo);
		}
		return null;

	}

	private void doCheckAll(TitaVo titaVo) throws LogicException {
		lBatxDetail = new ArrayList<BatxDetail>();
		l1BatxDetail = new ArrayList<BatxDetail>();
		l2BatxDetail = new ArrayList<BatxDetail>();
		if (slBatxDetail != null) {
			for (BatxDetail t : slBatxDetail.getContent()) {
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					if ("0".equals(t.getProcStsCode()) || "2".equals(t.getProcStsCode())
							|| "3".equals(t.getProcStsCode()) || "4".equals(t.getProcStsCode())) {
						batxDetailService.holdById(t);
						// 批次作業期間已人工入帳或轉帳收，不處理
						if ("5".equals(t.getProcStsCode()) || "6".equals(t.getProcStsCode())
								|| "7".equals(t.getProcStsCode())) {
							continue;
						}
						this.tTempVo = new TempVo();
						tTempVo = tTempVo.getVo(t.getProcNote());
						tTempVo.remove("MergeCnt");
						tTempVo.remove("MergeAmt");
						tTempVo.remove("MergeSeq");
						t.setProcNote(tTempVo.getJsonString());
						lBatxDetail.add(t);
						// 匯款轉帳同戶號多筆檢核放 00-未定 01-期款 02-部分還本 03-結案
						if (t.getRepayCode() == 1 && t.getRepayType() <= 3) {
							mapKey = parse.IntegerToString(t.getCustNo(), 7)
									+ parse.IntegerToString(t.getRepayType(), 2);
							if (mergeAmt.containsKey(mapKey)) {
								mergeCnt.put(mapKey, mergeCnt.get(mapKey) + 1);
								mergeAmt.put(mapKey, mergeAmt.get(mapKey).add(t.getRepayAmt()));
								if (!"4".equals(t.getProcStsCode())) {
									mergeProcStsCode.put(mapKey, t.getProcStsCode());
								}
							} else {
								mergeCnt.put(mapKey, 1);
								mergeAmt.put(mapKey, t.getRepayAmt());
								mergeProcStsCode.put(mapKey, t.getProcStsCode());
							}
						}
					}
				}
			}
			// 需檢核明細
			for (BatxDetail t : lBatxDetail) {
				this.tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(t.getProcNote());
				// 匯款轉帳期款多筆，看整個多筆狀態，合併筆數不同需再次檢核
				if (t.getRepayCode() == 1 && t.getRepayType() <= 3) {
					mapKey = parse.IntegerToString(t.getCustNo(), 7) + parse.IntegerToString(t.getRepayType(), 2);
					if (mergeCnt.get(mapKey) != null && mergeCnt.get(mapKey) >= 2) {
						l2BatxDetail.add(t);
					} else {
						l1BatxDetail.add(t);
					}
				} else {
					l1BatxDetail.add(t);
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
				// 02.銀行扣款 03.員工扣款 => 整批檢核時設定為 4.檢核正常，整批入帳時才進行檢核
				if (tDetail.getRepayCode() == 2 || tDetail.getRepayCode() == 3) {
					this.tTempVo = new TempVo();
					tTempVo = tTempVo.getVo(tDetail.getProcNote());
					tTempVo.putParam("CheckMsg", "");
					tTempVo.putParam("ErrorMsg", "");
					tDetail.setProcNote(tTempVo.getJsonString());
					tDetail.setProcStsCode("4"); // 4.檢核正常
				} else {
					tDetail = doTxCheck(tDetail, titaVo);
					addL4211MapList(tDetail, this.baTxList, titaVo);
				}
				try {
					batxDetailService.update(tDetail);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "batxDetail " + tDetail + e.getErrorMsg());
				}

			}
		}

		// 匯款轉帳同戶號多筆檢核
		if (l2BatxDetail.size() > 0) {
			// sort by CustNo, RepayType, MediaSeq
			Collections.sort(l2BatxDetail, new Comparator<BatxDetail>() {
				@Override
				public int compare(BatxDetail c1, BatxDetail c2) {
					if (c1.getCustNo() != c2.getCustNo()) {
						return c1.getCustNo() - c2.getCustNo();
					}
					if (c1.getRepayType() != c2.getRepayType()) {
						return c1.getRepayType() - c2.getRepayType();
					}
					if (c1.getMediaSeq() != c2.getMediaSeq()) {
						return c1.getMediaSeq() - c2.getMediaSeq();
					}
					return 0;
				}
			});
			int mergeSeq = 0;
			l3BatxDetail = new ArrayList<BatxDetail>();
			for (BatxDetail tDetail : l2BatxDetail) {
				this.info("L2 BatxDetail=" + tDetail.toString());
				this.tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tDetail.getProcNote());
				mapKey = parse.IntegerToString(tDetail.getCustNo(), 7)
						+ parse.IntegerToString(tDetail.getRepayType(), 2);
				mergeSeq++;
				tTempVo.putParam("MergeCnt", mergeCnt.get(mapKey));
				tTempVo.putParam("MergeAmt", mergeAmt.get(mapKey));
				tTempVo.putParam("MergeSeq", mergeSeq);
				tDetail.setProcNote(tTempVo.getJsonString());
				tDetail = doTxCheck(tDetail, titaVo);
				// 同步更新同戶號檢核狀態
				if (mergeSeq <= mergeCnt.get(mapKey)) {
					l3BatxDetail.add(tDetail);
				}
				if (mergeSeq == mergeCnt.get(mapKey)) {
					doMergeUpdate(tDetail.getRepayType(), tDetail.getProcStsCode(), titaVo);
					mergeSeq = 0;
					l3BatxDetail = new ArrayList<BatxDetail>();
				}
			}
		}
		this.batchTransaction.commit();

	}

	// 更新同步更新同戶號檢核狀態
	private void doMergeUpdate(int iRepayType, String iProcStsCode, TitaVo titaVo) throws LogicException {
		// 任一筆 1.<檢核錯誤> 非成功=> 2:人工處理
		// 還款類別不同需重新檢核((合併總額不同)
		// 還款類別為結案則全部為結案(結案收全部費用)
		String procStsCode = "4";
		// 任一筆檢核錯誤 => 3.檢核錯誤
		for (BatxDetail t : l3BatxDetail) {
			if ("3".equals(t.getProcStsCode())) {
				procStsCode = "3";
			}
			if (!"4".equals(t.getProcStsCode()) && "4".equals(procStsCode)) {
				procStsCode = "2";
			}
			if (t.getRepayType() != iRepayType) {
				isRepaytypeNotEqual = true;
			}
			if (iRepayType == 3) {
				t.setRepayType(3);
			}
		}

		for (BatxDetail t : l3BatxDetail) {
			t.setProcStsCode(procStsCode);
		}
		try {
			batxDetailService.updateAll(l3BatxDetail);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "lA2BatxDetail" + e.getErrorMsg());
		}

		for (BatxDetail t : l3BatxDetail) {
			addL4211MapList(t, this.baTxList, titaVo);
		}

		this.batchTransaction.commit();
	}

	private BatxDetail doTxCheck(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("doTxCheck ... " + tDetail.toString());
		tDetail = txBatchCom.txCheck(0, tDetail, titaVo);
		this.baTxList = txBatchCom.getBaTxList();
		return tDetail;
	}

	/* 匯款明細表 */
	private void addL4211MapList(BatxDetail tDetail, ArrayList<BaTxVo> iBatxList, TitaVo titaVo) throws LogicException {
		this.info("addL4211MapList ... " + tDetail.toString());
		if (tDetail.getRepayCode() != 1 || tDetail.getReconCode().equals("A6")) {
			return;
		}
		if (iBatxList == null || iBatxList.size() == 0) {
			return;
		}
		String custName = " ";
		CustMain tCustMain = custMainService.custNoFirst(tDetail.getCustNo(), tDetail.getCustNo(), titaVo);
		if (tCustMain != null) {
			custName = tCustMain.getCustName();
		}
		/* STEP 9 : 計算帳務作帳金額 */

		// 成功則本金利息(加總至撥款)、合併檢核轉戰收(不計費用)、人工及錯誤則轉暫收(不計費用)
		for (BaTxVo ba : iBatxList) {
			this.info("input =" + ba.toString());
		}

		ArrayList<BaTxVo> lbaTxVo = new ArrayList<BaTxVo>();
		int repayType = tDetail.getRepayType();
		TempVo iTempVo = new TempVo();
		iTempVo = iTempVo.getVo(tDetail.getProcNote());
		int facmNo = tDetail.getFacmNo();
		for (BaTxVo ba : iBatxList) {
			if (facmNo == 0 && ba.getDataKind() == 4) {
				facmNo = ba.getFacmNo();
			}
			// 另收費用同費用
			if (ba.getDataKind() == 6) {
				ba.setDataKind(1);
			}
		}

		String facAcctCode = "999";
		String facAcctItem = "暫收款 ";
		if (tDetail.getRepayType() >= 1 && tDetail.getRepayType() <= 3 && "4".equals(tDetail.getProcStsCode())) {
			FacMain tFacMain = facMainService.findById(new FacMainId(tDetail.getCustNo(), facmNo));
			if (tFacMain != null) {
				facAcctCode = tFacMain.getAcctCode();
				facAcctItem = getCdCode("AcctCode", facAcctCode, titaVo);
			}
		}

		ArrayList<BaTxVo> listbaTxVo = new ArrayList<BaTxVo>();
		if ("4".equals(tDetail.getProcStsCode()) && iTempVo.getParam("MergeSeq").equals(iTempVo.getParam("MergeCnt"))) {
			for (BaTxVo ba : iBatxList) {
				if (ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0 || ba.getDataKind() == 4) {
					listbaTxVo.add(ba);
				}
			}
			lbaTxVo = baTxCom.addByBormNo(listbaTxVo, titaVo);
		} else {
			BaTxVo baTxVo = new BaTxVo();
			baTxVo.setDataKind(4);
			baTxVo.setCustNo(tDetail.getCustNo()); // 借款人戶號
			baTxVo.setFacmNo(facmNo); // 額度編號
			baTxVo.setDbCr("C");
			baTxVo.setAcctAmt(tDetail.getRepayAmt());
			lbaTxVo.add(baTxVo);
		}

		for (BaTxVo ba : lbaTxVo) {
			this.info("ccc =" + ba.toString());
		}
		// 計算帳務作帳金額
		lbaTxVo = settleAcAmt(repayType, tDetail.getRepayAmt(), lbaTxVo, titaVo);

		for (BaTxVo baTxVo : lbaTxVo) {
			if (baTxVo.getAcAmt().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			Map<String, String> da = new HashMap<>();
			da.put("ReconCode", "" + tDetail.getReconCode());
			da.put("BatchNo", "" + tDetail.getBatchNo());
			da.put("EntryDate", "" + tDetail.getEntryDate());
			da.put("DetailSeq", "" + tDetail.getDetailSeq());
			da.put("RepayAmt", "" + tDetail.getRepayAmt());
			da.put("TxAmt", "" + baTxVo.getTxAmt());
			da.put("AcctAmt", "" + baTxVo.getAcAmt());
			da.put("CustNo",
					parse.IntegerToString(tDetail.getCustNo(), 7) + "-" + parse.IntegerToString(baTxVo.getFacmNo(), 3)
							+ "-" + parse.IntegerToString(baTxVo.getBormNo(), 3));
			da.put("PaidTerms", baTxVo.getPaidTerms() > 0 ? "" + baTxVo.getPaidTerms() : "");
			da.put("CustName", custName);
			da.put("CloseReasonCode", tTempVo.getParam("CloseReasonCode"));
			da.put("IntStartDate", "" + baTxVo.getIntStartDate());
			da.put("IntEndDate", "" + baTxVo.getIntEndDate());
			da.put("Principal", "" + baTxVo.getPrincipal()); // // 本金(A)
			da.put("Interest", "" + baTxVo.getInterest()); // 利息(B)
			da.put("TempPayAmt", "0"); // 暫付款(C)
			da.put("BreachAmt", "" + baTxVo.getDelayInt().add(baTxVo.getBreachAmt()).add(baTxVo.getCloseBreachAmt())); // 違約金(D)
			da.put("TempDr",
					"" + (baTxVo.getTempAmt().compareTo(BigDecimal.ZERO) < 0
							? BigDecimal.ZERO.subtract(baTxVo.getTempAmt())
							: "0")); // 暫收借(E)
			da.put("TempCr", "" + (baTxVo.getTempAmt().compareTo(BigDecimal.ZERO) > 0 ? baTxVo.getTempAmt() : "0")); // 暫收貸(F)
			da.put("Shortfall", "" + baTxVo.getUnpaidPrin().add(baTxVo.getUnpaidInt())); // 短繳(G)
			da.put("Fee", "" + baTxVo.getFeeAmt()); // 帳管費及其他(H) );
			da.put("AcDate", "" + tDetail.getAcDate());
			da.put("TitaTlrNo", titaVo.getTlrNo());
			da.put("TitaTxtNo",
					tDetail.getBatchNo().substring(4, 6) + parse.IntegerToString(tDetail.getDetailSeq(), 6));
			da.put("SortingForSubTotal", facAcctCode); // 配合小計產生的排序
			da.put("AcctItem", facAcctItem);
			da.put("RepayItem", getCdCode("RepayType", parse.IntegerToString(tDetail.getRepayType(), 2), titaVo));
			l4211MapList.add(da);
		}

		for (Map<String, String> da : l4211MapList) {
			this.info("addL4211MapList = " + da.toString());
		}
	}

	private String getCdCode(String iDefCode, String iCode, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = cdCodeService.findById(new CdCodeId(iDefCode, iCode), titaVo);
		if (tCdCode == null) {
			return " ";
		} else {
			return tCdCode.getItem();
		}
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
		if (checkErrorCnt > 0) {
			msg = msg + ", 檢核錯誤筆數 :" + checkErrorCnt;
		}

		if (doneCnt > 0) {
			msg = msg + ", 已入帳筆數 :" + doneCnt;
		}

		if (manualCnt > 0) {
			msg = msg + ", 需人工處理筆數 :" + manualCnt;
		}

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

	private ArrayList<BaTxVo> settleAcAmt(int iRepayType, BigDecimal iTxAmt, ArrayList<BaTxVo> iBaTxList, TitaVo titaVo)
			throws LogicException {
		this.info("settleAcAmt ... iRepayType=" + iRepayType + ", iTxAmt=" + iTxAmt);
		// 計算金額
		BigDecimal wkTxAmt = iTxAmt;
		BigDecimal wkTxBal = iTxAmt;
		BigDecimal wkOverAmt = BigDecimal.ZERO;
		BigDecimal wkOverBal = BigDecimal.ZERO;
		BigDecimal wkShortAmt = BigDecimal.ZERO;
		BigDecimal wkTempAmt = BigDecimal.ZERO;
		BigDecimal wkTempBal = BigDecimal.ZERO;
		BigDecimal wkFeeAmt = BigDecimal.ZERO;
		// 計算短溢收金額 ，溢(C)短(D)繳
		for (BaTxVo ba : iBaTxList) {
			this.info("settleAcAmt= " + ba.toString());
			// 計算費用
			if (ba.getFeeAmt().compareTo(BigDecimal.ZERO) > 0) {
				wkFeeAmt = wkFeeAmt.add(ba.getAcctAmt());
			}
			// 計算暫收抵繳
			if (ba.getDataKind() == 3) {
				wkTempAmt = wkTempAmt.add(ba.getAcctAmt());
			}
			// 計算短溢收金額 ，溢(C)短(D)繳
			if (ba.getDataKind() == 4) {
				if ("C".equals(ba.getDbCr())) {
					wkOverAmt = wkOverAmt.add(ba.getAcctAmt());
				} else {
					wkShortAmt = wkShortAmt.add(ba.getUnPaidAmt());
				}
			}
		}
		wkOverBal = wkOverAmt;
		wkTempBal = wkTempAmt;

		this.info("settleAcAmt wkFeeAmt=" + wkFeeAmt + ", wkOverAmt=" + wkOverAmt + ", wkShortAmt=" + wkShortAmt
				+ ", wkTempAmt=" + wkTempAmt);

		// 回收短繳(G)、本次短繳
		// 1.期金 => 已在BaTxCom 計算
		// 2. 部分償還本金時，短繳金額先扣除回收短繳利息，再扣除利息
		if (iRepayType == 2 && wkShortAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 短繳金額先扣除回收短繳利息
			for (BaTxVo ba : iBaTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() == 1 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (wkShortAmt.compareTo(ba.getInterest()) > 0) {
						wkShortAmt = wkShortAmt.subtract(ba.getInterest());
						ba.setInterest(BigDecimal.ZERO);
						ba.setAcctAmt(BigDecimal.ZERO);
					} else {
						ba.setInterest(ba.getInterest().subtract(wkShortAmt));
						ba.setAcctAmt(ba.getAcctAmt().subtract(wkShortAmt));
						wkShortAmt = BigDecimal.ZERO;
					}
					this.info("RepayType == 2 xxx wkShortAmt=" + wkShortAmt + ba.toString());
				}
			}
			// 短繳金額先扣除扣除利息
			for (BaTxVo ba : iBaTxList) {
				if (ba.getDataKind() == 2) {
					this.info("RepayType == 2 wkShortAmt=" + wkShortAmt + ba.toString());
					if (wkShortAmt.compareTo(ba.getInterest()) > 0) {
						ba.setUnpaidInt(ba.getInterest());
						wkShortAmt = wkShortAmt.subtract(ba.getInterest());
					} else {
						ba.setUnpaidInt(wkShortAmt);
						ba.setInterest(ba.getInterest().subtract(wkShortAmt));
						wkShortAmt = BigDecimal.ZERO;
					}
					this.info("RepayType == 2 end wkShortAmt=" + wkShortAmt + ba.toString());
				}
			}
		}

		// 回收短繳(G) ==> 放在償還本利那筆
		if (iRepayType >= 1 && iRepayType <= 3) {
			for (BaTxVo ba : iBaTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() == 1 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					// 放相同額度撥款的本利資料筆
					for (BaTxVo da : iBaTxList) {
						if (ba.getDataKind() == 2 && da.getFacmNo() == ba.getFacmNo()
								&& da.getBormNo() == ba.getBormNo()) {
							da.setPrincipal(da.getPrincipal().add(ba.getPrincipal()));
							da.setInterest(da.getInterest().add(ba.getInterest()));
							da.setCloseBreachAmt(da.getCloseBreachAmt().add(ba.getCloseBreachAmt()));
							da.setAcctAmt(da.getAcctAmt().add(ba.getAcctAmt()));
							ba.setPrincipal(BigDecimal.ZERO);
							ba.setInterest(BigDecimal.ZERO);
							ba.setCloseBreachAmt(BigDecimal.ZERO);
							ba.setAcctAmt(BigDecimal.ZERO);
						}
					}
				}
			}
		}

		// 作帳金額 = 本金(A)+利息(B)+違約金(D)+費用(H)+回收短繳(G)
// 交易金額 + 暫收借 = 作帳金額 + 暫收貸

		// 本金利息 本金(A)+利息(B)+違約金(D)+費用(H) ==> add to 作帳金額
		for (BaTxVo ba : iBaTxList) {
			if (ba.getRepayType() >= 1 && ba.getRepayType() <= 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				// 交易金額放第一筆
				ba.setTxAmt(wkTxAmt);
				wkTxAmt = BigDecimal.ZERO;
				// 本金、利息已包含
				ba.setAcAmt(ba.getAcctAmt().add(wkFeeAmt));
				// 本金、利息包含本次短繳，作帳金額扣除
				ba.setAcAmt(ba.getAcAmt().subtract(ba.getUnpaidPrin()).subtract(ba.getUnpaidInt()));
				// 費用放第一筆
				if (wkFeeAmt.compareTo(BigDecimal.ZERO) > 0) {
					ba.setFeeAmt(wkFeeAmt);
					wkFeeAmt = BigDecimal.ZERO;
				}
				// 溢繳款放第一筆
				if (wkOverAmt.compareTo(BigDecimal.ZERO) > 0) {
					ba.setTempAmt(wkOverBal);
					wkOverBal = BigDecimal.ZERO;
				}
				// 暫收抵繳
				if (wkTempAmt.compareTo(BigDecimal.ZERO) > 0) {
					if (wkTxBal.compareTo(ba.getAcAmt()) >= 0) {
						wkTxBal = wkTxBal.subtract(ba.getAcAmt());
					} else {
						BigDecimal tempAmt = wkTxBal.subtract(ba.getAcAmt()); // 暫收抵繳為負值
						ba.setTempAmt(tempAmt);
						wkTxBal = BigDecimal.ZERO;
						wkTempBal = wkTempBal.add(tempAmt);
					}
				}
				this.info("settleAcAmt 1.TxBal = " + wkTxBal + ", AcAmt = " + ba.getAcAmt() + ", TempAmt="
						+ ba.getTempAmt() + ba.toString());
			}
		}
		for (BaTxVo ba : iBaTxList) {
			if (ba.getDataKind() == 4) {
				// 交易金額放第一筆
				ba.setTxAmt(wkTxAmt);
				wkTxAmt = BigDecimal.ZERO;
				// 費用放第一筆
				if (wkFeeAmt.compareTo(BigDecimal.ZERO) > 0) {
					ba.setFeeAmt(wkFeeAmt);
					wkFeeAmt = BigDecimal.ZERO;
				}
				// 溢繳款放第一筆
				if (wkOverAmt.compareTo(BigDecimal.ZERO) > 0) {
					ba.setTempAmt(wkOverBal);
					wkOverBal = BigDecimal.ZERO;
				}
				// 暫收抵繳
				if (wkTempAmt.compareTo(BigDecimal.ZERO) > 0) {
					if (wkTxBal.compareTo(ba.getAcAmt()) >= 0) {
						wkTxBal = wkTxBal.subtract(ba.getAcAmt());
					} else {
						BigDecimal tempAmt = wkTxBal.subtract(ba.getAcAmt()); // 暫收抵繳為負值
						ba.setTempAmt(tempAmt);
						wkTxBal = BigDecimal.ZERO;
						wkTempBal = wkTempBal.add(tempAmt);
					}
				}
				this.info("settleAcAmt 2.TxBal = " + wkTxBal + ", AcAmt = " + ba.getAcAmt() + ", TempAmt="
						+ ba.getTempAmt() + ba.toString());
			}
		}

		// 作帳金額 = 作帳金額 + (暫收貸 - 暫收借)
		BigDecimal acAmt = BigDecimal.ZERO;
		for (BaTxVo ba : iBaTxList) {
			ba.setAcAmt(ba.getAcAmt().add(ba.getTempAmt()));
			acAmt = acAmt.add(ba.getAcAmt());
			this.info("SettleAcAmt end " + ba.toString());
		}
		if (iTxAmt.compareTo(acAmt) == 0) {
			this.info("SettleAcAmt Equal 交易金額" + iTxAmt + "= 作帳金額 " + acAmt);
		} else {
			this.info("SettleAcAmt unEqual 交易金額" + iTxAmt + "<> 作帳金額 " + acAmt + "，差額= " + iTxAmt.subtract(acAmt));
		}
		return iBaTxList;
	}

}