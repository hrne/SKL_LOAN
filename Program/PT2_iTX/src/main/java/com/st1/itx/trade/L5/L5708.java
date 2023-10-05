package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

//import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;

/*DB服務*/
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
import com.st1.itx.util.common.NegApprCom;
import com.st1.itx.db.service.springjpa.cm.L597AServiceImpl;

/**
 * Tita<br>
 * BringUpDate=9,7<br>
 */

@Service("L5708")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5708 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegAppr01Service sNegAppr01Service;
	@Autowired
	NegApprCom negApprCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	private L597AServiceImpl l597AServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5708");
		this.info("active L5708 ");
		this.totaVo.init(titaVo);
		acDetailCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		// int Today=dateUtil.getNowIntegerForBC();
		// int Today = titaVo.getOrgEntdyI();// 會計日期
		String BringUpDate = titaVo.getParam("BringUpDate").trim();// 提兌日

		// 要有製過檔-要確定L5707有做的(製檔日)
		// 出使用撥付傳票日
		// 觀看及溝通的時候要用提兌日

		if (BringUpDate != null && BringUpDate.length() != 0) {

		} else {
			throw new LogicException("", "[提兌日]未輸入");
		}

		// 提兌日
		int IntBringUpdate = parse.stringToInteger(BringUpDate);
		if (String.valueOf(IntBringUpdate).length() <= 7) {
			IntBringUpdate = IntBringUpdate + 19110000;
		}
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		Map<Integer, BigDecimal> apprAmtCustNoMap = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> sklAmtCustNoMap = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> returnAmtCustNoMap = new HashMap<Integer, BigDecimal>();
		Map<Integer, Integer> tempCustNoMap = new HashMap<Integer, Integer>();

		// 匯入專戶失敗重撥資料累計出傳票金額-訂正不需做此段
		if (titaVo.isHcodeNormal()) {
			List<Map<String, String>> sNegAppr01 = null;
			sNegAppr01 = l597AServiceImpl.findNegAppr01(IntBringUpdate, 2, 0, titaVo);
			if (sNegAppr01 != null) {
				this.info("***3 findNegAppr01*** has data");
				for (Map<String, String> tNegAppr01 : sNegAppr01) {
					int iacdate = parse.stringToInteger(tNegAppr01.get("AcDate"));
					String iTlrNo = tNegAppr01.get("TitaTlrNo");
					int iTxtNo = parse.stringToInteger(tNegAppr01.get("TitaTxtNo"));
					String iFinCode = tNegAppr01.get("FinCode");
					int iCsutNo = parse.stringToInteger(tNegAppr01.get("CustNo"));
					NegAppr01 NegAppr01Org = sNegAppr01Service
							.findById(new NegAppr01Id(iacdate, iTlrNo, iTxtNo, iFinCode, iCsutNo), titaVo);
					this.info("***4*key=" + iacdate + "," + iTlrNo + "," + iTxtNo + "," + iFinCode + "," + iCsutNo);

					NegMain NegMainVO = sNegMainService
							.findById(new NegMainId(NegAppr01Org.getCustNo(), NegAppr01Org.getCaseSeq()), titaVo);
					if (NegMainVO != null) {
						String IsMainFin = NegMainVO.getIsMainFin();
						if (("Y").equals(IsMainFin)) {
							// 最大債權
							// 異動會計檔
							int CustNo = NegMainVO.getCustNo();
							int iPayerCustNo = NegMainVO.getPayerCustNo();// 保證人的付款戶號
							if (iPayerCustNo > 0) {
								tempCustNoMap.put(CustNo, iPayerCustNo);
							} else {
								tempCustNoMap.put(CustNo, CustNo);
							}
							if (!apprAmtCustNoMap.containsKey(CustNo)) {
								apprAmtCustNoMap.put(CustNo, NegAppr01Org.getApprAmt());
								sklAmtCustNoMap.put(CustNo, BigDecimal.ZERO);
								returnAmtCustNoMap.put(CustNo, BigDecimal.ZERO);
							} else {
								apprAmtCustNoMap.put(CustNo,
										apprAmtCustNoMap.get(CustNo).add(NegAppr01Org.getApprAmt()));
								sklAmtCustNoMap.put(CustNo, sklAmtCustNoMap.get(CustNo).add(BigDecimal.ZERO));
								returnAmtCustNoMap.put(CustNo, returnAmtCustNoMap.get(CustNo).add(BigDecimal.ZERO));
							}
						}
					} else {
						// E0006 鎖定資料時，發生錯誤
						throw new LogicException(titaVo, "E0006", "債務協商案件主檔");
					}
				}

			}
		}

		List<NegAppr01> lNegAppr01 = negApprCom.InsUpdNegApprO1(IntBringUpdate, 2, titaVo);
		this.info("L5708 lNegAppr01!=null titaVo.isHcodeNormal()=[" + titaVo.isHcodeNormal() + "]");
		if (titaVo.isHcodeNormal()) {
			// 正向交易

			if (lNegAppr01 != null && lNegAppr01.size() != 0) {
//				List<String[]> lTempData=new ArrayList<String[]>();
//				List<String> lKey=new ArrayList<String>();

				List<NegTransId> DistinctNegTransId = new ArrayList<NegTransId>();
				for (NegAppr01 NegAppr01VO : lNegAppr01) {
					NegTransId NegTransIdVO = new NegTransId();
					NegTransIdVO.setAcDate(NegAppr01VO.getAcDate());
					NegTransIdVO.setTitaTlrNo(NegAppr01VO.getTitaTlrNo());
					NegTransIdVO.setTitaTxtNo(NegAppr01VO.getTitaTxtNo());
					NegTransIdVO.setCustNo(NegAppr01VO.getCustNo());

					if (!DistinctNegTransId.contains(NegTransIdVO)) {
						DistinctNegTransId.add(NegTransIdVO);
					}
				}

				for (NegTransId NegTransIdVO : DistinctNegTransId) {
					NegTrans NegTransVO = sNegTransService.findById(NegTransIdVO, titaVo);
					if (NegTransVO != null) {

						NegMainId NegMainIdVO = new NegMainId();
						NegMainIdVO.setCaseSeq(NegTransVO.getCaseSeq());
						NegMainIdVO.setCustNo(NegTransVO.getCustNo());

						NegMain NegMainVO = sNegMainService.findById(NegMainIdVO, titaVo);
						if (NegMainVO != null) {

							String IsMainFin = NegMainVO.getIsMainFin();
							if (("Y").equals(IsMainFin)) {
								// 最大債權
								// 異動會計檔
								int CustNo = NegTransVO.getCustNo();
								int iPayerCustNo = NegMainVO.getPayerCustNo();// 保證人的付款戶號
								if (iPayerCustNo > 0) {
									tempCustNoMap.put(CustNo, iPayerCustNo);
								} else {
									tempCustNoMap.put(CustNo, CustNo);
								}
								if (!apprAmtCustNoMap.containsKey(CustNo)) {
									apprAmtCustNoMap.put(CustNo, NegTransVO.getApprAmt());
									sklAmtCustNoMap.put(CustNo, NegTransVO.getSklShareAmt());
									returnAmtCustNoMap.put(CustNo, NegTransVO.getReturnAmt());

								} else {
									apprAmtCustNoMap.put(CustNo,
											apprAmtCustNoMap.get(CustNo).add(NegTransVO.getApprAmt()));
									sklAmtCustNoMap.put(CustNo,
											sklAmtCustNoMap.get(CustNo).add(NegTransVO.getSklShareAmt()));
									returnAmtCustNoMap.put(CustNo,
											returnAmtCustNoMap.get(CustNo).add(NegTransVO.getReturnAmt()));
								}

								int ExportDate = NegTransVO.getExportDate();
								if (ExportDate != 0) {

								} else {
									throw new LogicException(titaVo, "", "[撥付製檔日]為空值");
								}
								//// 異動DB
								//// ExportAcDate 撥付出帳日
								// try {
								// NegTransVO.setExportAcDate(Today);
								// sNegTransService.update(NegTransVO);
								//
								// } catch (DBException e) {
								// //E0007 更新資料時，發生錯誤
								// this.info("L5708 NegTrans ");
								// throw new LogicException(titaVo, "E0007", "");
								// }
							}
						} else {
							// E0006 鎖定資料時，發生錯誤
							throw new LogicException(titaVo, "E0006", "債務協商案件主檔");
						}
//					} else {
//						// E0006 鎖定資料時，發生錯誤
//						throw new LogicException(titaVo, "E0006", "債務協商交易檔");
					}

				}

				if (apprAmtCustNoMap != null && apprAmtCustNoMap.size() != 0) {
					for (int CustNo : apprAmtCustNoMap.keySet()) {
						BigDecimal apprAmt = apprAmtCustNoMap.get(CustNo);
						BigDecimal sklAmt = sklAmtCustNoMap.get(CustNo);
						BigDecimal returnAmt = returnAmtCustNoMap.get(CustNo);
						/* 帳務 */
						// 經辦登帳非訂正交易
						if (this.txBuffer.getTxCom().isBookAcYes()) {
							List<AcDetail> acDetailList = new ArrayList<AcDetail>();
							AcDetail acDetail = new AcDetail();
							// 2023/9/10 第一套帳改在L5702出,故點掉
							/* 借：債協暫收款－抵繳款 */
//							acDetail.setDbCr("D");
//							acDetail.setAcctCode(acNegCom.getAcctCode(CustNo, titaVo));
//							acDetail.setTxAmt(apprAmt.add(sklAmt).add(returnAmt)); // 撥付+新壽攤分金額+結清退還款
//							acDetail.setCustNo(CustNo);// 戶號
//							acDetailList.add(acDetail);

							/* 貸：應付代收款 */
//							acDetail = new AcDetail();
//							acDetail.setDbCr("C");
//							acDetail.setAcctCode(acNegCom.getApprAcctCode(CustNo, titaVo));
//							acDetail.setTxAmt(apprAmt.add(sklAmt).add(returnAmt)); // 撥付+新壽攤分金額+結清退還款
//							acDetail.setCustNo(CustNo);// 戶號
//							acDetailList.add(acDetail);

							/* 借：應付代收款 */
							acDetail = new AcDetail();
							acDetail.setDbCr("D");
							acDetail.setAcctCode(acNegCom.getApprAcctCode(CustNo, titaVo));
							acDetail.setTxAmt(apprAmt); // 撥付金額
							acDetail.setCustNo(CustNo);// 戶號
							acDetailList.add(acDetail);

							/* 貸：P02 銀行存款－新光 */
							acDetail = new AcDetail();
							acDetail.setDbCr("C");
							acDetail.setAcctCode("P02");
							acDetail.setTxAmt(apprAmt); // 撥付金額
							acDetail.setCustNo(CustNo);// 戶號+額度+撥款
							acDetailList.add(acDetail);

							/* 借：應付代收款 */
							acDetail = new AcDetail();
							acDetail.setDbCr("D");
							acDetail.setAcctCode(acNegCom.getApprAcctCode(CustNo, titaVo));
							acDetail.setTxAmt(sklAmt); // 新壽攤分金額
							acDetail.setCustNo(CustNo);// 戶號
							acDetailList.add(acDetail);

							/* 貸：暫收可抵繳科目 */
							acDetail = new AcDetail();
							acDetail.setDbCr("C");
							TempVo tTempVo = new TempVo();
							tTempVo = acNegCom.getReturnAcctCode(tempCustNoMap.get(CustNo), titaVo);
							acDetail.setCustNo(tempCustNoMap.get(CustNo));// 實際借款人戶號
							acDetail.setFacmNo(parse.stringToInteger(tTempVo.getParam("FacmNo")));
							acDetail.setAcctCode(tTempVo.getParam("AcctCode"));
							acDetail.setTxAmt(sklAmt); // 新壽攤分金額
							acDetail.setSlipNote("最大債權新壽攤分款");
							acDetailList.add(acDetail);
							// L5708 最大債權撥付入帳產生放款交易明細(新壽攤分款、結清退還款轉入客戶暫收可抵繳)
							acNegCom.addL5708LoanBorTxHcodeNormal(acDetail, titaVo);

							/* 借：應付代收款 */
							acDetail = new AcDetail();
							acDetail.setDbCr("D");
							acDetail.setAcctCode(acNegCom.getApprAcctCode(CustNo, titaVo));
							acDetail.setTxAmt(returnAmt); // 結清退還款
							acDetail.setCustNo(CustNo);// 戶號
							acDetailList.add(acDetail);

							/* 貸：暫收可抵繳科目 */
							acDetail = new AcDetail();
							acDetail.setDbCr("C");
							TempVo tTempVo2 = new TempVo();
							tTempVo2 = acNegCom.getReturnAcctCode(tempCustNoMap.get(CustNo), titaVo);
							acDetail.setCustNo(tempCustNoMap.get(CustNo));// 實際借款人戶號
							acDetail.setFacmNo(parse.stringToInteger(tTempVo2.getParam("FacmNo")));
							acDetail.setAcctCode(tTempVo.getParam("AcctCode"));
							acDetail.setTxAmt(returnAmt); // 結清退還款
							acDetail.setSlipNote("最大債權結清退還款");
							acDetailList.add(acDetail);
							// L5708 最大債權撥付入帳產生放款交易明細(新壽攤分款、結清退還款轉入客戶暫收可抵繳)
							acNegCom.addL5708LoanBorTxHcodeNormal(acDetail, titaVo);

							this.txBuffer.addAllAcDetailList(acDetailList);

							/* 產生會計分錄 */
							acDetailCom.setTxBuffer(this.txBuffer);
							acDetailCom.run(titaVo);

						}
					}
				}

			} else {
				// E2003 查無資料
				throw new LogicException(titaVo, "E2003", "最大債權撥付資料檔");
			}
		} else {
			// 訂正
			// NegTrans的異動已經寫在 NegApprCom.InsUpdNegApprO1 內了
			// L5708 最大債權撥付入帳訂正產生放款交易明細(新壽攤分款、結清退還款轉入客戶暫收可抵繳)
			acNegCom.addL5708LoanBorTxHcodeErase(titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}