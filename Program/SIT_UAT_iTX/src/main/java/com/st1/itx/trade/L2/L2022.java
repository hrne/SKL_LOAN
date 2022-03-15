package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.db.service.FacShareRelationService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.db.service.springjpa.cm.L2022ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2022")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2022 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public FacShareApplService sFacShareApplService;

	@Autowired
	public GuarantorService sGuarantorService;

	@Autowired
	public ClFacService sClFacService;

	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;

	@Autowired
	public ClLandOwnerService sClLandOwnerService;

	@Autowired
	public ClMovablesService sClMovablesService;

	@Autowired
	public ClStockService sClStockService;

	@Autowired
	public ClOtherService sClOtherService;

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	@Autowired
	public FacRelationService sFacRelationService;

	@Autowired
	public FacShareRelationService sFacShareRelationService;

	@Autowired
	public CdGuarantorService sCdGuarantorService;

	@Autowired
	public CdCodeService sCdCodeService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	/* DB服務注入 */
	@Autowired
	public L2022ServiceImpl sL2022ServiceImpl;


	HashMap<String, String> owners = new HashMap<String, String>();

	String mainRel = "";
	String mainCustId = "";
	int mainCustNo = 0;
	String mainCustName = "";
	int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2022 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 50; // 9 * 15 * 376 = 50760 1次最多9筆occurs


		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2022ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2022ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0001", "L2022");

		}
		
		List<LinkedHashMap<String, String>> chkOccursList = null;
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				if(parse.stringToInteger(result.get("CreditSysNo")) == 0) {
					occursList.putParam("oCreditSysNo", "");
				} else {
					occursList.putParam("oCreditSysNo", result.get("CreditSysNo"));
				}
				occursList.putParam("oApplNo", result.get("ApplNo"));
				occursList.putParam("oCustNo", result.get("CustNo"));
				occursList.putParam("oFacmNo", result.get("FacmNo"));
				occursList.putParam("oCustName", result.get("CustName"));
				occursList.putParam("oUKey", result.get("UKey"));
				occursList.putParam("oId", result.get("Id"));
				occursList.putParam("oName", result.get("Name"));
				occursList.putParam("oType", result.get("Type"));
				occursList.putParam("oRelation", result.get("Relation"));
				occursList.putParam("oClCode1", result.get("ClCode1"));
				occursList.putParam("oClCode2", result.get("ClCode2"));
				occursList.putParam("oClNo", result.get("ClNo"));
				occursList.putParam("oModify", result.get("Modify"));

				this.totaVo.addOccursList(occursList);
				
			}
			
			chkOccursList = this.totaVo.getOccursList();

			if (resultList.size() >= this.limit ) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}
		
		
		
		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException("E0001", ""); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

		
//		if (iCreditSysNo > 0) {
//			Slice<FacMain> slFacMain = sFacMainService.facmCreditSysNoRange(iCreditSysNo, iCreditSysNo, 0, 999, this.index, this.limit, titaVo);
//			List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
//
//			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//			if (slFacMain != null && slFacMain.hasNext()) {
//				titaVo.setReturnIndex(this.setIndexNext());
//				/* 手動折返 */
//				this.totaVo.setMsgEndToEnter();
//			}
//
//			if (lFacMain != null && lFacMain.size() > 0) {
//				for (FacMain tFacMain : lFacMain) {
//					facMainRelation(titaVo, tFacMain);
//					cnt++;
//				}
//			}
//
//			getfacRelation(titaVo, iCreditSysNo);
//
//		} else if (iCustNo > 0 && iFacmNo > 0) {
//			FacMainId facMainId = new FacMainId();
//			facMainId.setCustNo(iCustNo);
//			facMainId.setFacmNo(iFacmNo);
//			FacMain tFacMain = sFacMainService.findById(facMainId, titaVo);
//			if (tFacMain != null) {
//				cnt++;
//				facMainRelation(titaVo, tFacMain);
//
//				getfacRelation(titaVo, tFacMain.getCreditSysNo());
//			}
//
//		} else if (iCustNo > 0) {
//			Slice<FacMain> slFacMain = sFacMainService.CustNoAll(iCustNo, this.index, this.limit, titaVo);
//
//			List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
//
//			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//			if (slFacMain != null && slFacMain.hasNext()) {
//				titaVo.setReturnIndex(this.setIndexNext());
//				/* 手動折返 */
//				this.totaVo.setMsgEndToEnter();
//			}
//
//			int oCreditSysNo = 0;
//			if (lFacMain != null && lFacMain.size() > 0) {
//				for (FacMain tFacMain : lFacMain) {
//
//					cnt++;
//					facMainRelation(titaVo, tFacMain);
//
//					oCreditSysNo = tFacMain.getCreditSysNo();
//
//					if (oCreditSysNo > 0 && oCreditSysNo != tFacMain.getCreditSysNo()) {
//						getfacRelation(titaVo, tFacMain.getCreditSysNo());
//					}
//				}
////				getfacRelation(titaVo, oCreditSysNo);
//			}
//
//		} else if (iApplNo > 0) {
//			FacMain tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
//			if (tFacMain != null) {
//				cnt++;
//				facMainRelation(titaVo, tFacMain);
//				getfacRelation(titaVo, tFacMain.getCreditSysNo());
//
//			}
//		}
//
//		if (cnt == 0) {
//			throw new LogicException(titaVo, "E0001", "");
//		}
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//	private void findAllCdGuarantor() {
//		Slice<CdGuarantor> slCdGuarantor = sCdGuarantorService.findAll(0, Integer.MAX_VALUE);
//		List<CdGuarantor> lCdGuarantor = slCdGuarantor == null ? null : slCdGuarantor.getContent();
//
//		if (lCdGuarantor != null && lCdGuarantor.size() > 0) {
//			for (CdGuarantor cdGuarantor : lCdGuarantor) {
//				rels.put(cdGuarantor.getGuaRelCode(), cdGuarantor.getGuaRelItem());
//			}
//		}
//	}
//
//	private void findAllCdCode() {
//		Slice<CdCode> slCdCode = sCdCodeService.defCodeEq("FacRelationCode", "%", 0, Integer.MAX_VALUE);
//		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();
//
//		if (lCdCode != null && lCdCode.size() > 0) {
//			for (CdCode tCdCode : lCdCode) {
//				facRels.put(tCdCode.getCode(), tCdCode.getItem());
//			}
//		}
//	}
//
//	private String formatOut(int n, String f) {
//		String s = "";
//
//		if (n != 0) {
//			s = String.format(f, n);
//		}
//		return s;
//	}
//
//	// 額度相關資料
//	private void facMainRelation(TitaVo titaVo, FacMain tFacMain) {
//		CustMain tCustMain = sCustMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
//
//		mainRel = "";
//		mainCustId = tCustMain.getCustId();
//		mainCustNo = tCustMain.getCustNo();
//		mainCustName = tCustMain.getCustName();
//
//		if (tCustMain != null) {
//
//			if (tCustMain.getCustId().trim().length() > 8) {
//				mainRel = "授信戶本人";
//			} else {
//				mainRel = "授信戶企業";
//			}
//
//			// 借戶
//			OccursList occursList = new OccursList();
//
//			occursList.putParam("oCreditSysNo", formatOut(tFacMain.getCreditSysNo(), "%07d"));
//			occursList.putParam("oApplNo", formatOut(tFacMain.getApplNo(), "%07d"));
//			occursList.putParam("oCustNo", formatOut(tFacMain.getCustNo(), "%07d"));
//			occursList.putParam("oFacmNo", formatOut(tFacMain.getFacmNo(), "%03d"));
//			occursList.putParam("oCustName", mainCustName);
//			occursList.putParam("oUKey", tCustMain.getCustUKey());
//			occursList.putParam("oId", tCustMain.getCustId());
//			occursList.putParam("oName", tCustMain.getCustName());
//			occursList.putParam("oType", "授信戶");
//			occursList.putParam("oRelation", mainRel);
//			occursList.putParam("oClCode1", "");
//			occursList.putParam("oClCode2", "");
//			occursList.putParam("oClNo", "");
//			occursList.putParam("oModify", 0);
//
//			this.totaVo.addOccursList(occursList);
//		}
//
//		// 共同借款人
//		FacShareAppl tFacShareAppl = sFacShareApplService.findById(tFacMain.getApplNo(), titaVo);
//		if (tFacShareAppl != null) {
//			int MainApplNo = tFacShareAppl.getMainApplNo();
//			Slice<FacShareAppl> slFacShareAppl = sFacShareApplService.findMainApplNo(MainApplNo, 0, Integer.MAX_VALUE, titaVo);
//			List<FacShareAppl> lFacShareAppl = slFacShareAppl == null ? null : slFacShareAppl.getContent();
//
//			if (lFacShareAppl != null && lFacShareAppl.size() > 0) {
//				for (FacShareAppl tFacShareAppl2 : lFacShareAppl) {
//
//					if (tFacShareAppl2.getApplNo() != tFacShareAppl.getApplNo()) {
//						if (tFacShareAppl2.getCustNo() == mainCustNo) {
//							continue;
//						}
//
//						CustMain tCustMain2 = sCustMainService.custNoFirst(tFacShareAppl2.getCustNo(), tFacShareAppl2.getCustNo(), titaVo);
//						if (tCustMain2 == null) {
//							continue;
//						}
//
//						OccursList occursList = new OccursList();
//
//						occursList.putParam("oCreditSysNo", formatOut(tFacMain.getCreditSysNo(), "%07d"));
//						occursList.putParam("oApplNo", formatOut(tFacMain.getApplNo(), "%07d"));
//						occursList.putParam("oCustNo", formatOut(tFacMain.getCustNo(), "%07d"));
//						occursList.putParam("oFacmNo", formatOut(tFacMain.getFacmNo(), "%03d"));
//						occursList.putParam("oCustName", mainCustName);
//						occursList.putParam("oUKey", tCustMain2.getCustUKey());
//						occursList.putParam("oId", tCustMain2.getCustId());
//						occursList.putParam("oName", tCustMain2.getCustName());
//						occursList.putParam("oType", "共同借款人");
//						occursList.putParam("oClCode1", "");
//						occursList.putParam("oClCode2", "");
//						occursList.putParam("oClNo", "");
//
//						FacShareRelationId tFacShareRelationId = new FacShareRelationId();
//						tFacShareRelationId.setApplNo(tFacShareAppl.getApplNo());
//						tFacShareRelationId.setRelApplNo(tFacShareAppl2.getApplNo());
//						FacShareRelation tFacShareRelation = sFacShareRelationService.findById(tFacShareRelationId, titaVo);
//						if (tFacShareRelation == null || "".equals(tFacShareRelation.getRelCode())) {
//							occursList.putParam("oRelation", "**與授信戶關係未登錄**");
//							occursList.putParam("oModify", 1);
//						} else {
//							occursList.putParam("oRelation", rels.get(tFacShareRelation.getRelCode()));
//							occursList.putParam("oModify", 0);
//						}
//
//						this.totaVo.addOccursList(occursList);
//					}
//
//				}
//			}
//		}
//
//		// 保證人資料
//		Slice<Guarantor> slGuarantor = sGuarantorService.approveNoEq(tFacMain.getApplNo(), 0, Integer.MAX_VALUE, titaVo);
//		List<Guarantor> lGuarantor = slGuarantor == null ? null : slGuarantor.getContent();
//
//		if (lGuarantor != null && lGuarantor.size() > 0) {
//			for (Guarantor tGuarantor : lGuarantor) {
//				OccursList occursList = new OccursList();
//
//				occursList.putParam("oCreditSysNo", formatOut(tFacMain.getCreditSysNo(), "%07d"));
//				occursList.putParam("oApplNo", formatOut(tFacMain.getApplNo(), "%07d"));
//				occursList.putParam("oCustNo", formatOut(tFacMain.getCustNo(), "%07d"));
//				occursList.putParam("oFacmNo", formatOut(tFacMain.getFacmNo(), "%03d"));
//				occursList.putParam("oCustName", mainCustName);
//
//				CustMain tCustMain2 = sCustMainService.findById(tGuarantor.getGuaUKey(), titaVo);
//				if (tCustMain2 == null) {
//					continue;
//				}
//				occursList.putParam("oUKey", tCustMain2.getCustUKey());
//				occursList.putParam("oId", tCustMain2.getCustId());
//				occursList.putParam("oName", tCustMain2.getCustName());
//				occursList.putParam("oType", "保證人");
//				occursList.putParam("oRelation", rels.get(tGuarantor.getGuaRelCode()));
//				occursList.putParam("oClCode1", "");
//				occursList.putParam("oClCode2", "");
//				occursList.putParam("oClNo", "");
//				occursList.putParam("oModify", 0);
//
//				this.totaVo.addOccursList(occursList);
//			}
//		}
//
//		// 擔保品提供人資料
//		Slice<ClFac> slClFac = sClFacService.approveNoEq(tFacMain.getApplNo(), 0, Integer.MAX_VALUE, titaVo);
//		List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
//
//		if (lClFac != null && lClFac.size() > 0) {
//			for (ClFac tClFac : lClFac) {
//
//				int iClCode1 = tClFac.getClCode1();
//				int iClCode2 = tClFac.getClCode2();
//				int iClNo = tClFac.getClNo();
//
//				if (iClCode1 == 1) {
//					Slice<ClBuildingOwner> slClBuildingOwner = sClBuildingOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
//					List<ClBuildingOwner> lClBuildingOwner = slClBuildingOwner == null ? null : slClBuildingOwner.getContent();
//					if (lClBuildingOwner != null && lClBuildingOwner.size() > 0) {
//						for (ClBuildingOwner clBuildingOwner : lClBuildingOwner) {
//
//							putOwner(titaVo, tFacMain, clBuildingOwner.getOwnerCustUKey(), iClCode1, iClCode2, iClNo);
//
//						}
//					}
//				}
//
//				if (iClCode1 == 1 || iClCode1 == 2) {
//					Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
//					List<ClLandOwner> lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();
//					if (lClLandOwner != null && lClLandOwner.size() > 0) {
//						for (ClLandOwner clLandOwner : lClLandOwner) {
//							putOwner(titaVo, tFacMain, clLandOwner.getOwnerCustUKey(), iClCode1, iClCode2, iClNo);
//						}
//					}
//				}
//
//				if (iClCode1 == 3 || iClCode1 == 4) {
//					ClStockId clStockId = new ClStockId();
//					clStockId.setClCode1(iClCode1);
//					clStockId.setClCode2(iClCode2);
//					clStockId.setClNo(iClNo);
//					ClStock clStock = sClStockService.findById(clStockId, titaVo);
//					if (clStock != null) {
//						putOwner(titaVo, tFacMain, clStock.getOwnerCustUKey(), iClCode1, iClCode2, iClNo);
//					}
//				}
//
//				if (iClCode1 == 5) {
//					ClOtherId clOtherId = new ClOtherId();
//					clOtherId.setClCode1(iClCode1);
//					clOtherId.setClCode2(iClCode2);
//					clOtherId.setClNo(iClNo);
//					ClOther clOther = sClOtherService.findById(clOtherId, titaVo);
//					if (clOther != null) {
//						putOwner(titaVo, tFacMain, clOther.getOwnerCustUKey(), iClCode1, iClCode2, iClNo);
//					}
//				}
//
//				if (iClCode1 == 9) {
//					ClMovablesId clMovablesId = new ClMovablesId();
//					clMovablesId.setClCode1(iClCode1);
//					clMovablesId.setClCode2(iClCode2);
//					clMovablesId.setClNo(iClNo);
//					ClMovables clMovables = sClMovablesService.findById(clMovablesId, titaVo);
//					if (clMovables != null) {
//						putOwner(titaVo, tFacMain, clMovables.getOwnerCustUKey(), iClCode1, iClCode2, iClNo);
//					}
//				}
//
//			}
//		}
//	}
//
//	private void putOwner(TitaVo titaVo, FacMain tFacMain, String custUKey, int iClCode1, int iClCode2, int iClNo) {
//		owners.put(custUKey + "/" + iClCode1 + "-" + iClCode2 + "-" + iClNo, custUKey);
//
//		OccursList occursList = new OccursList();
//
//		occursList.putParam("oCreditSysNo", formatOut(tFacMain.getCreditSysNo(), "%07d"));
//		occursList.putParam("oApplNo", formatOut(tFacMain.getApplNo(), "%07d"));
//		occursList.putParam("oCustNo", formatOut(tFacMain.getCustNo(), "%07d"));
//		occursList.putParam("oFacmNo", formatOut(tFacMain.getFacmNo(), "%03d"));
//		occursList.putParam("oCustName", mainCustName);
//
//		CustMain tCustMain2 = sCustMainService.findById(custUKey, titaVo);
//		if (tCustMain2 == null) {
//			return;
//		}
//
//		occursList.putParam("oUKey", tCustMain2.getCustUKey());
//		occursList.putParam("oId", tCustMain2.getCustId());
//		occursList.putParam("oName", tCustMain2.getCustName());
//		occursList.putParam("oType", "擔保品提供人");
//
//		String relCodeX = "";
//
//		occursList.putParam("oModify", 0);
//		if (tFacMain.getCustNo() == tCustMain2.getCustNo()) {
//			relCodeX = mainRel;
//		} else {
//
//			ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
//			clOwnerRelationId.setCreditSysNo(tFacMain.getCreditSysNo());
//			clOwnerRelationId.setCustNo(tFacMain.getCustNo());
//			clOwnerRelationId.setOwnerCustUKey(custUKey);
//			ClOwnerRelation tClOwnerRelation = sClOwnerRelationService.findById(clOwnerRelationId, titaVo);
//
//			if (tClOwnerRelation != null) {
//				relCodeX = rels.get(tClOwnerRelation.getOwnerRelCode());
//			} else {
//				relCodeX = "**與授信戶關係未登錄**";
//				occursList.putParam("oModify", 1);
//			}
//		}
//
//		occursList.putParam("oRelation", relCodeX);
//
//		occursList.putParam("oClCode1", formatOut(iClCode1, "%01d"));
//		occursList.putParam("oClCode2", formatOut(iClCode2, "%02d"));
//		occursList.putParam("oClNo", formatOut(iClNo, "%07d"));
//
//		this.totaVo.addOccursList(occursList);
//	}
//
//	// 交易關係人相關資料
//	private void getfacRelation(TitaVo titaVo, int creditSysNo) {
//
//		if (creditSysNo == 0) {
//			return;
//		}
//
//		Slice<FacRelation> slFacRelation = sFacRelationService.CreditSysNoAll(creditSysNo, 0, Integer.MAX_VALUE, titaVo);
//		List<FacRelation> lFacRelation = slFacRelation == null ? null : slFacRelation.getContent();
//		if (lFacRelation != null && lFacRelation.size() > 0) {
//			for (FacRelation clFacRelation : lFacRelation) {
//				OccursList occursList = new OccursList();
//
//				occursList.putParam("oCreditSysNo", formatOut(creditSysNo, "%07d"));
//				occursList.putParam("oApplNo", "");
//				occursList.putParam("oCustNo", "");
//				occursList.putParam("oFacmNo", "");
//				occursList.putParam("oCustName", "");
//
//				CustMain tCustMain2 = sCustMainService.findById(clFacRelation.getCustUKey(), titaVo);
//				if (tCustMain2 == null) {
//					return;
//				}
//
//				cnt++;
//
//				occursList.putParam("oUKey", tCustMain2.getCustUKey());
//				occursList.putParam("oId", tCustMain2.getCustId());
//				occursList.putParam("oName", tCustMain2.getCustName());
//				occursList.putParam("oType", "交易關係人");
//
//				String facRelCodeX = facRels.get(clFacRelation.getFacRelationCode());
//
//				occursList.putParam("oRelation", facRelCodeX);
//
//				occursList.putParam("oClCode1", "");
//				occursList.putParam("oClCode2", "");
//				occursList.putParam("oClNo", "");
//				occursList.putParam("oModify", 0);
//
//				this.totaVo.addOccursList(occursList);
//			}
//		}
//	}
}