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
	private String mapKey;
	private HashMap<String, Integer> mergeCnt = new HashMap<>();
	private HashMap<String, BigDecimal> mergeAmt = new HashMap<>();
	private HashMap<String, BigDecimal> mergeTempAmt = new HashMap<>();
	private HashMap<String, String> mergeProcStsCode = new HashMap<>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private List<Map<String, String>> l4211MapList = new ArrayList<Map<String, String>>();
	private TempVo tTempVo = new TempVo();
	private List<BatxDetail> lBatxDetail;
	private List<BatxDetail> l1BatxDetail;
	private List<BatxDetail> l2BatxDetail;
	private List<BatxDetail> l3BatxDetail;
	private boolean isMergeCheckAgain = false;
	private String custName = " ";
	private String facAcctCode = "999";
	private String facAcctItem = "暫收款 ";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L420ABatch ");
		this.totaVo.init(titaVo);

		txBatchCom.setTxBuffer(this.getTxBuffer());
		baTxCom.setTxBuffer(this.getTxBuffer());

		// 會計日期、批號
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");
		String iReconCode = titaVo.getParam("ReconCode").trim();
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

		doCheckAll(true, iReconCode, slBatxDetail, titaVo);

		// 合併的還款類別設定為另收費用則須再全部檢核一次
		if (isMergeCheckAgain) {
			this.info("RepaytypeNotEqual check again ...");
			this.l4211MapList = new ArrayList<Map<String, String>>();
			doCheckAll(true, iReconCode, slBatxDetail, titaVo);
		}
		// 更新作業狀態
		String msg = updateHead(iReconCode, tBatxHead, slBatxDetail.getContent(), titaVo);

		// end
		this.batchTransaction.commit();
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002",
				titaVo.getEntDyI() + "0" + tBatxHead.getTitaTlrNo(), iBatchNo + " 整批檢核, " + msg, titaVo);

		// 匯款轉帳檢核明細表
		if (l4211MapList.size() > 0) {
			// 產生匯款轉帳檢核明細表
			l4211Report.setParentTranCode("L420A");
			l4211Report.execWithBatchMapList(l4211MapList, titaVo);
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L420A", "L420A-匯款轉帳檢核明細表", titaVo);
		}
		return null;

	}

	/**
	 * 整批檢核
	 * 
	 * @param isCommit     true/false
	 * @param iReconCode   對帳類別
	 * @param slBatxDetail slice of BatxDetail
	 * @param titaVo       TitaVo
	 * @throws LogicException ....
	 */
	public void doCheckAll(boolean isCommit, String iReconCode, Slice<BatxDetail> slBatxDetail, TitaVo titaVo)
			throws LogicException {
		this.info("doCheckAll .....");

		mergeCnt = new HashMap<>();
		mergeAmt = new HashMap<>();
		mergeProcStsCode = new HashMap<>();
		lBatxDetail = new ArrayList<BatxDetail>();
		l1BatxDetail = new ArrayList<BatxDetail>();
		l2BatxDetail = new ArrayList<BatxDetail>();
		if (slBatxDetail != null) {
			for (BatxDetail t : slBatxDetail.getContent()) {
				if ("".equals(iReconCode) || t.getReconCode().equals(iReconCode)) {
					if ("5".equals(t.getProcStsCode()) || "6".equals(t.getProcStsCode())
							|| "7".equals(t.getProcStsCode())) {
					}
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
						// 已訂正須人工處理
						if (this.tTempVo.get("EraseCnt") != null) {
							continue;
						}
						tTempVo.remove("MergeCnt");
						tTempVo.remove("MergeAmt");
						tTempVo.remove("MergeSeq");
						tTempVo.remove("MergeTempAmt");
						t.setProcNote(tTempVo.getJsonString());
						lBatxDetail.add(t);
						// 匯款轉帳同戶號多筆檢核放 00-未定 01-期款 02-部分還本 03-結案 09-其他
						if (t.getRepayCode() == 1 && (t.getRepayType() <= 3 || t.getRepayType() == 9)
								&& t.getCustNo() != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
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
								mergeTempAmt.put(mapKey, BigDecimal.ZERO);
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
				if (t.getRepayCode() == 1 && (t.getRepayType() <= 3 || t.getRepayType() == 9)) {
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
				if (isCommit && cntTrans > this.commitCnt) {
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
				tTempVo.putParam("MergeTempAmt", mergeTempAmt.get(mapKey));
				mergeTempAmt.put(mapKey, mergeTempAmt.get(mapKey).add(tDetail.getRepayAmt()));
				tDetail.setProcNote(tTempVo.getJsonString());
				tDetail = doTxCheck(tDetail, titaVo);
				// 同步更新同戶號檢核狀態
				if (mergeSeq <= mergeCnt.get(mapKey)) {
					l3BatxDetail.add(tDetail);
				}
				if (mergeSeq == mergeCnt.get(mapKey)) {
					doMergeUpdate(isCommit, tDetail.getRepayType(), tDetail.getProcStsCode(), titaVo);
					mergeSeq = 0;
					l3BatxDetail = new ArrayList<BatxDetail>();
				}
			}
		}
		if (isCommit) {
			this.batchTransaction.commit();
		}

	}

	// 更新同步更新同戶號檢核狀態
	private void doMergeUpdate(boolean isCommit, int iRepayType, String iProcStsCode, TitaVo titaVo)
			throws LogicException {
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
				isMergeCheckAgain = true;
			} else {
				t.setRepayType(iRepayType);
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
		if (isCommit) {
			this.batchTransaction.commit();
		}
	}

	private BatxDetail doTxCheck(BatxDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("doTxCheck ... " + tDetail.toString());
		txBatchCom.setTxBuffer(this.getTxBuffer());
		tDetail = txBatchCom.txCheck(0, tDetail, titaVo);
		this.baTxList = txBatchCom.getBaTxList();
		addL4211MapList(tDetail, this.baTxList, titaVo);
		return tDetail;
	}

	/* 匯款明細表 */
	private void addL4211MapList(BatxDetail tDetail, ArrayList<BaTxVo> iBatxList, TitaVo titaVo) throws LogicException {
		this.info("addL4211MapList ... " + tDetail.toString());
		if (tDetail.getRepayCode() != 1) {
			return;
		}
		if (iBatxList == null || iBatxList.size() == 0) {
			this.info("addL4211MapList iBatxList.size = 0 return ");
			return;
		}

		// 按AcSeq，小至大排序
		Collections.sort(iBatxList, new Comparator<BaTxVo>() {
			@Override
			public int compare(BaTxVo c1, BaTxVo c2) {
				if (c1.getAcSeq() != c2.getAcSeq()) {
					return c1.getAcSeq() - c2.getAcSeq();
				}
				return 0;
			}
		});

		custName = " ";
		CustMain tCustMain = custMainService.custNoFirst(tDetail.getCustNo(), tDetail.getCustNo(), titaVo);
		if (tCustMain != null) {
			custName = tCustMain.getCustName();
		}
		/* STEP 9 : 計算帳務作帳金額 */

		facAcctCode = "999";
		facAcctItem = "暫收款 ";

		TempVo iTempVo = new TempVo();
		iTempVo = iTempVo.getVo(tDetail.getProcNote());
		// 檢核失敗
		if (!"4".equals(tDetail.getProcStsCode())) {
			addL4211TempAmt(tDetail, iTempVo, iBatxList, titaVo);
		}
		// 檢核正常
		if ("4".equals(tDetail.getProcStsCode())) {
			for (BaTxVo baTxVo : iBatxList) {
				if (baTxVo.getTxAmt().add(baTxVo.getAcAmt()).add(baTxVo.getUnpaidInt()).add(baTxVo.getUnpaidPrin())
						.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				facAcctCode = baTxVo.getFacAcctCode().trim();
				if (facAcctCode.isEmpty()) {
					facAcctCode = "999";
					FacMain tFacMain = facMainService.findById(new FacMainId(tDetail.getCustNo(), baTxVo.getFacmNo()));
					if (tFacMain != null) {
						facAcctCode = tFacMain.getAcctCode();
					}
				}
				if ("999".equals(facAcctCode)) {
					facAcctItem = "暫收款 ";
				} else {
					facAcctItem = getCdCode("AcctCode", facAcctCode, titaVo);
				}
				addL4211AcAmt(tDetail, iTempVo, baTxVo, titaVo);
			}
		}
	}

	private void addL4211AcAmt(BatxDetail tDetail, TempVo iTempVo, BaTxVo baTxVo, TitaVo titaVo) throws LogicException {
		this.info("addL4211AcAmt baTxVo = " + baTxVo.toString());
		Map<String, String> da = new HashMap<>();
		da.put("ReconCode", "" + tDetail.getReconCode());
		da.put("BatchNo", "" + tDetail.getBatchNo());
		da.put("EntryDate", "" + tDetail.getEntryDate());
		da.put("DetailSeq", "" + tDetail.getDetailSeq());
		da.put("RepayCode", "" + tDetail.getRepayCode());
		da.put("RepayAmt", "" + tDetail.getRepayAmt());
		da.put("AcSeq", parse.IntegerToString(baTxVo.getAcSeq(), 4));
		da.put("TxAmt", "" + baTxVo.getTxAmt());
		da.put("AcctAmt", "" + baTxVo.getAcAmt().add(baTxVo.getOverflow().subtract(baTxVo.getTempAmt())));
		da.put("CustFacmBorm", parse.IntegerToString(tDetail.getCustNo(), 7) + "-"
				+ parse.IntegerToString(baTxVo.getFacmNo(), 3) + "-" + parse.IntegerToString(baTxVo.getBormNo(), 3));
		da.put("CustNo", parse.IntegerToString(baTxVo.getCustNo(), 7));
		da.put("FacmNo", parse.IntegerToString(baTxVo.getFacmNo(), 3));
		da.put("BormNo", parse.IntegerToString(baTxVo.getBormNo(), 3));
		da.put("PaidTerms", baTxVo.getPaidTerms() > 0 ? "" + baTxVo.getPaidTerms() : "");
		da.put("CustName", custName);
		da.put("CloseReasonCode", iTempVo.getParam("CloseReasonCode"));
		da.put("IntStartDate", "" + baTxVo.getIntStartDate());
		da.put("IntEndDate", "" + baTxVo.getIntEndDate());
		da.put("Principal", "" + baTxVo.getPrincipal().add(baTxVo.getShortFallPrin()).subtract(baTxVo.getUnpaidPrin())); // //
																															// 本金(A)
		da.put("Interest", "" + baTxVo.getInterest().add(baTxVo.getShortFallInt()).subtract(baTxVo.getUnpaidInt())); // 利息(B)
		da.put("TempPayAmt", "0"); // 暫付款(C)
		da.put("BreachAmt", "" + baTxVo.getDelayInt().add(baTxVo.getBreachAmt()).add(baTxVo.getCloseBreachAmt())
				.add(baTxVo.getShortFallCloseBreach())); // 違約金(D)
		da.put("TempDr", "" + baTxVo.getTempAmt()); // 暫收借(E)
		da.put("TempCr", "" + baTxVo.getOverflow()); // 暫收貸(F)
		da.put("Shortfall", "" + baTxVo.getUnpaidPrin().add(baTxVo.getUnpaidInt())); // 短繳(G)
		da.put("Fee", "" + baTxVo.getFeeAmt()); // 帳管費及其他(H) );
		da.put("AcDate", "" + tDetail.getAcDate());
		da.put("TitaTlrNo", titaVo.getTlrNo());
		da.put("TitaTxtNo", tDetail.getBatchNo().substring(4, 6) + parse.IntegerToString(tDetail.getDetailSeq(), 6));
		da.put("SortingForSubTotal", facAcctCode); // 配合小計產生的排序
		da.put("AcctItem", facAcctItem);
		da.put("RepayItem", getCdCode("RepayType", parse.IntegerToString(tDetail.getRepayType(), 2), titaVo));
		l4211MapList.add(da);
		this.info("addL4211AcAmt = " + da.toString());
	}

	private void addL4211TempAmt(BatxDetail tDetail, TempVo iTempVo, ArrayList<BaTxVo> iBatxList, TitaVo titaVo)
			throws LogicException {
		BigDecimal wkTempBal = BigDecimal.ZERO;
		if (iTempVo.get("MergeAmt") != null) {
			wkTempBal = parse.stringToBigDecimal(iTempVo.getParam("MergeTempAmt"));
		}
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 3) {
				// 計算暫收抵繳
				wkTempBal = wkTempBal.add(ba.getAcctAmt());
			}
		}
		for (BaTxVo baTxVo : iBatxList) {
			if (baTxVo.getDataKind() != 4) {
				continue;
			}
			Map<String, String> da = new HashMap<>();
			da.put("ReconCode", "" + tDetail.getReconCode());
			da.put("BatchNo", "" + tDetail.getBatchNo());
			da.put("EntryDate", "" + tDetail.getEntryDate());
			da.put("DetailSeq", "" + tDetail.getDetailSeq());
			da.put("RepayCode", "" + tDetail.getRepayCode());
			da.put("RepayAmt", "" + tDetail.getRepayAmt());
			da.put("AcSeq", parse.IntegerToString(baTxVo.getAcSeq(), 4));
			da.put("TxAmt", "" + tDetail.getRepayAmt());
			da.put("AcctAmt", "" + tDetail.getRepayAmt());
			da.put("CustNo",
					parse.IntegerToString(tDetail.getCustNo(), 7) + "-" + parse.IntegerToString(baTxVo.getFacmNo(), 3)
							+ "-" + parse.IntegerToString(baTxVo.getBormNo(), 3));
			da.put("PaidTerms", "");
			da.put("CustName", custName);
			da.put("CloseReasonCode", "");
			da.put("IntStartDate", "");
			da.put("IntEndDate", "");
			da.put("Principal", "0");
			da.put("Interest", "0"); // 利息(B)
			da.put("TempPayAmt", "0"); // 暫付款(C)
			da.put("BreachAmt", "0"); // 違約金(D)
			da.put("TempDr", "" + wkTempBal); // 暫收借(E)
			da.put("TempCr", "" + tDetail.getRepayAmt().add(wkTempBal)); // 暫收貸(F)
			da.put("Shortfall", "0"); // 短繳(G)
			da.put("Fee", "0"); // 帳管費及其他(H) );
			da.put("AcDate", "" + tDetail.getAcDate());
			da.put("TitaTlrNo", titaVo.getTlrNo());
			da.put("TitaTxtNo",
					tDetail.getBatchNo().substring(4, 6) + parse.IntegerToString(tDetail.getDetailSeq(), 6));
			da.put("SortingForSubTotal", facAcctCode); // 配合小計產生的排序
			da.put("AcctItem", facAcctItem);
			da.put("RepayItem", "暫收");
			l4211MapList.add(da);
			this.info("addL4211TempAmt = " + da.toString());
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
	private String updateHead(String iReconCode, BatxHead tHead, List<BatxDetail> lBatxDetail, TitaVo titaVo)
			throws LogicException {
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
}