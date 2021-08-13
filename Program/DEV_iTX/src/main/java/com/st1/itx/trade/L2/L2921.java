package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CaseNo=9,7<br>
 * CustNo=9,7<br>
 * CustId=X,10<br>
 * ApplNo=9,7<br>
 * TELLER=X,6<br>
 * FacmNo=9,3<br>
 * CloseCode=9,1<br>
 * END=X,1<br>
 */

@Service("L2921")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2921 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2921.class);

	/* DB服務注入 */
//	@Autowired
//	public TxTellerService txTellerService;
	
	@Autowired
	public CdEmpService sCdEmpService;
	
	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService sLoanNotYetService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2921 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 103 * 500 = 51500

		// tita
		// 案件編號
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 統編
		String iCustId = titaVo.getParam("CustId");
		// 核准編號
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));
		// 經辦
		String iTELLER = titaVo.getParam("Teller");
		// 額度編號
		int iFacmNoStartAt = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iFacmNoEndAt = 999;
		if (iFacmNoStartAt > 0) {
			iFacmNoEndAt = iFacmNoStartAt;
		}
		// 輸入核准編號時,參數使用
		String colsetflag = "%";

		int YetDate1 = parse.stringToInteger(titaVo.getParam("YetDate1"));
		int YetDate2 = parse.stringToInteger(titaVo.getParam("YetDate2"));
		int CloseDate1 = parse.stringToInteger(titaVo.getParam("CloseDate1"));
		int CloseDate2 = parse.stringToInteger(titaVo.getParam("CloseDate2"));
		
		if(YetDate1 == 0 ) {
			YetDate2 = 99999999; 
		} 
		
		if(CloseDate1 == 0 ) {
			CloseDate2 = 99999999;
		}
		
		// 銷號狀態 0:全部 1:已銷 2:未銷
		int iCloseCode = parse.stringToInteger(titaVo.getParam("CloseCode"));

		// new ArrayList
		List<FacMain> lFacMain = new ArrayList<FacMain>();
		List<LoanNotYet> lLoanNotYet = new ArrayList<LoanNotYet>();
		Slice<FacMain> slFacMain = null;
		
		// 調所有經辦號碼跟名稱
		List<CdEmp> lCdEmp = new ArrayList<CdEmp>();
		Slice<CdEmp> slCdEmp = null;
		String EmployeeNo = "";
		// new table
		CustMain tCustMain = new CustMain();

		// 處理邏輯 五擇一輸入 取出戶號,額度
		// 輸入案號
		if (iCaseNo > 0 || iApplNo > 0 || !iTELLER.isEmpty() || iCustNo > 0 || !iCustId.isEmpty()) {
			if (iCaseNo > 0) {
				slFacMain = sFacMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, iFacmNoStartAt, iFacmNoEndAt,
						this.index, this.limit, titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null) {
					throw new LogicException(titaVo, "E2003", "該案號" + iCaseNo + "查無額度主檔資料"); // 查無資料
				}
				// 核准編號
			} else if (iApplNo > 0) {
				slFacMain = sFacMainService.facmApplNoRange(iApplNo, iApplNo, iFacmNoStartAt, iFacmNoEndAt, colsetflag,
						this.index, this.limit, titaVo);

				lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null) {
					throw new LogicException(titaVo, "E2003", "該核准編號" + iApplNo + "查無額度主檔資料"); // 查無資料
				}
				// 經辦
			} else if (!iTELLER.isEmpty()) {
				slFacMain = sFacMainService.facmBusinessOfficerRange(iTELLER, iTELLER, iFacmNoStartAt, iFacmNoEndAt,
						this.index, this.limit, titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null) {
					throw new LogicException(titaVo, "E2003", "該經辦" + iTELLER + "查無額度主檔資料"); // 查無資料
				}
				// 戶號
			} else if (iCustNo > 0) {
				slFacMain = sFacMainService.facmCustNoRange(iCustNo, iCustNo, iFacmNoStartAt, iFacmNoEndAt, this.index,
						this.limit, titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null) {
					throw new LogicException(titaVo, "E2003", "該戶號" + iCustNo + "查無額度主檔資料"); // 查無資料
				}
				// 統編
			} else if (!iCustId.isEmpty()) {
				tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
				if (tCustMain == null) {
					throw new LogicException(titaVo, "E2003", "該統編" + iCustId + "查無客戶主檔資料"); // 查無資料
				}
				// 輸入統編查無戶號拋錯
				if (tCustMain.getCustNo() == 0) {
					throw new LogicException(titaVo, "E2003", "該統編無戶號"); // 查無資料
				}
				int custno = tCustMain.getCustNo();
				slFacMain = sFacMainService.facmCustNoRange(custno, custno, iFacmNoStartAt, iFacmNoEndAt, this.index,
						this.limit, titaVo);
				lFacMain = slFacMain == null ? null : slFacMain.getContent();
				if (lFacMain == null) {
					throw new LogicException(titaVo, "E2003", "該統編戶號" + iCustId + " " + custno + "查無額度主檔資料"); // 查無資料
				}
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (slFacMain != null && slFacMain.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (FacMain tFacMain : lFacMain) {
				this.info("L2921-tFacMain = " + tFacMain);
				tCustMain = new CustMain();
				int FacCustNo = tFacMain.getCustNo();
				int FacFacmNo = tFacMain.getFacmNo();
				tCustMain = sCustMainService.custNoFirst(FacCustNo, FacCustNo, titaVo);

				Slice<LoanNotYet> slLoanNotYet = sLoanNotYetService.notYetCustNoEq(FacCustNo, FacFacmNo, FacFacmNo,
						YetDate1, YetDate2, CloseDate1, CloseDate2, this.index, this.limit, titaVo);
				lLoanNotYet = slLoanNotYet == null ? null : slLoanNotYet.getContent();

				if (lLoanNotYet == null) {
//					throw new LogicException(titaVo, "E2003", "查無未齊件資料"); // 查無資料

					lLoanNotYet = new ArrayList<LoanNotYet>();
				}
				
				for (LoanNotYet tmpLoanNotYet : lLoanNotYet) {
					// new occurs
					OccursList occurslist = new OccursList();
					if (iCloseCode == 1) {
					  
						if (tmpLoanNotYet.getCloseDate() > 0) {
							occurslist.putParam("OOCaseNo", tFacMain.getCreditSysNo()); // 案號
							occurslist.putParam("OOCustNo", tCustMain.getCustNo()); // 戶號
                            occurslist.putParam("OOCustName", tCustMain.getCustName()); // 戶名
							occurslist.putParam("OOCustId", tCustMain.getCustId()); // 統編
							occurslist.putParam("OOApplNo", tFacMain.getApplNo()); // 核准號碼
							occurslist.putParam("OOFacmNo", tmpLoanNotYet.getFacmNo()); // 額度號碼
							occurslist.putParam("OOFirstDrawdownDate", tFacMain.getFirstDrawdownDate()); // 首撥日期
							occurslist.putParam("OOEmpNo", tFacMain.getBusinessOfficer()); // 經辦(房貸專員)
							EmployeeNo = tFacMain.getBusinessOfficer();
							slCdEmp = sCdEmpService.findEmployeeNo(EmployeeNo, EmployeeNo, this.index, this.limit, titaVo);
							lCdEmp = slCdEmp == null ? null : slCdEmp.getContent();
							
							if (lCdEmp == null) {
								occurslist.putParam("OOEmpName","          ");
							} else {
								for(CdEmp tCdEmp: lCdEmp) {
									occurslist.putParam("OOEmpName",tCdEmp.getFullname());
								}
							}

							occurslist.putParam("OONotYetCode", tmpLoanNotYet.getNotYetCode()); // 未齊件代碼
							occurslist.putParam("OONotYetItemX", tmpLoanNotYet.getNotYetItem()); // 未齊件說明
							occurslist.putParam("OOYetDate", tmpLoanNotYet.getYetDate()); // 齊件日期
							occurslist.putParam("OOCloseDate", tmpLoanNotYet.getCloseDate()); // 銷號日期
							occurslist.putParam("OOReMark", tmpLoanNotYet.getReMark()); // 備註
							this.info("已銷  =" + tmpLoanNotYet.getCloseDate());

							/* 將每筆資料放入Tota的OcList */
							this.totaVo.addOccursList(occurslist);
						}
                      
					} else if (iCloseCode == 2) {
						if (tmpLoanNotYet.getCloseDate() == 0) {
							occurslist.putParam("OOCaseNo", tFacMain.getCreditSysNo()); // 案號
							occurslist.putParam("OOCustNo", tCustMain.getCustNo()); // 戶號
                            occurslist.putParam("OOCustName", tCustMain.getCustName()); // 戶名
							occurslist.putParam("OOCustId", tCustMain.getCustId()); // 統編
							occurslist.putParam("OOApplNo", tFacMain.getApplNo()); // 核准號碼
							occurslist.putParam("OOFacmNo", tmpLoanNotYet.getFacmNo()); // 額度號碼
							occurslist.putParam("OOFirstDrawdownDate", tFacMain.getFirstDrawdownDate()); // 首撥日期
							occurslist.putParam("OOEmpNo", tFacMain.getBusinessOfficer()); // 經辦(房貸專員)				
							
							EmployeeNo = tFacMain.getBusinessOfficer();
							slCdEmp = sCdEmpService.findEmployeeNo(EmployeeNo, EmployeeNo, this.index, this.limit, titaVo);
							lCdEmp = slCdEmp == null ? null : slCdEmp.getContent();
							
							if (lCdEmp == null) {
								occurslist.putParam("OOEmpName","          ");
							} else {
								for(CdEmp tCdEmp: lCdEmp) {
									occurslist.putParam("OOEmpName",tCdEmp.getFullname());
								}
							}

							
							occurslist.putParam("OONotYetCode", tmpLoanNotYet.getNotYetCode()); // 未齊件代碼
							occurslist.putParam("OONotYetItemX", tmpLoanNotYet.getNotYetItem()); // 未齊件說明
							occurslist.putParam("OOYetDate", tmpLoanNotYet.getYetDate()); // 齊件日期
							occurslist.putParam("OOCloseDate", tmpLoanNotYet.getCloseDate()); // 銷號日期
							occurslist.putParam("OOReMark", tmpLoanNotYet.getReMark()); // 備註
							this.info("未銷  =" + tmpLoanNotYet.getCloseDate());

							/* 將每筆資料放入Tota的OcList */
							this.totaVo.addOccursList(occurslist);
						}
					} else if (iCloseCode == 0) {
						occurslist.putParam("OOCaseNo", tFacMain.getCreditSysNo()); // 案號
						occurslist.putParam("OOCustNo", tCustMain.getCustNo()); // 戶號
                        occurslist.putParam("OOCustName", tCustMain.getCustName()); // 戶名
						occurslist.putParam("OOCustId", tCustMain.getCustId()); // 統編
						occurslist.putParam("OOApplNo", tFacMain.getApplNo()); // 核准號碼
						occurslist.putParam("OOFacmNo", tmpLoanNotYet.getFacmNo()); // 額度號碼
						occurslist.putParam("OOFirstDrawdownDate", tFacMain.getFirstDrawdownDate()); // 首撥日期
						occurslist.putParam("OOEmpNo", tFacMain.getBusinessOfficer()); // 經辦(房貸專員)			
						
						EmployeeNo = tFacMain.getBusinessOfficer();
						slCdEmp = sCdEmpService.findEmployeeNo(EmployeeNo, EmployeeNo, this.index, this.limit, titaVo);
						lCdEmp = slCdEmp == null ? null : slCdEmp.getContent();
						
						if (lCdEmp == null) {
							occurslist.putParam("OOEmpName","          ");
						} else {
							for(CdEmp tCdEmp: lCdEmp) {
								occurslist.putParam("OOEmpName",tCdEmp.getFullname());
							}
						}

						occurslist.putParam("OONotYetCode", tmpLoanNotYet.getNotYetCode()); // 未齊件代碼
						occurslist.putParam("OONotYetItemX", tmpLoanNotYet.getNotYetItem()); // 未齊件說明
						occurslist.putParam("OOYetDate", tmpLoanNotYet.getYetDate()); // 齊件日期
						occurslist.putParam("OOCloseDate", tmpLoanNotYet.getCloseDate()); // 銷號日期
						occurslist.putParam("OOReMark", tmpLoanNotYet.getReMark()); // 備註
						this.info("全部  =" + tmpLoanNotYet.getCloseDate());

						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occurslist);
					}

				} // for
			}
		}

		if(this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E2003", "查無未齊件資料"); // 查無資料
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}