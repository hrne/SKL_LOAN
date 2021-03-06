package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.domain.ClOwnerRelation;
import com.st1.itx.db.domain.ClOwnerRelationId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.ClOwnerRelationService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2412")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2412 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClMovablesService sClMovablesService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public FacMainService sFacMainService;
	@Autowired
	public FacCaseApplService sFacCaseApplService;

	@Autowired
	public ClOwnerRelationService sClOwnerRelationService;

	@Autowired
	public DataLog dataLog;

	/* 自動取號 */
	@Autowired
	GSeqCom gGSeqCom;

	@Autowired
	public ClFacCom clFacCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private int iFunCd;
	private int iClCode1;
	private int iClCode2;
	private int iClNo;
	// 核准號碼
	private int iApplNo;
	private ClMainId clMainId = new ClMainId();
	private ClMovablesId clMovablesId = new ClMovablesId();
	private ClMain tClMain;
	private ClMovables tClMovables;
	private String finalClNo;

	private boolean isEloan = false;
	private FacMain tFacMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2412 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// tita
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

		// 塞pk
		clMainId.setClCode1(iClCode1);
		clMainId.setClCode2(iClCode2);
		clMainId.setClNo(iClNo);

		// 塞pk
		clMovablesId.setClCode1(iClCode1);
		clMovablesId.setClCode2(iClCode2);
		clMovablesId.setClNo(iClNo);

		if (isEloan && iFunCd == 1) {
			if (iClNo > 0) {
				tClMovables = sClMovablesService.findById(clMovablesId, titaVo);
				if(tClMovables != null) {
					iFunCd = 2;					
				}
			} else {
				int ClNo = uniqueCheck(titaVo);
				if (ClNo > 0) {
					iFunCd = 2;
					iClNo = ClNo;
				}
			}
		}

		if (iApplNo > 0) { // 核准編號大於0才去做
			tFacMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException("E2019", "核准號碼 = " + iApplNo);
			} else {
//				ELOAN:擔保品案件在上送的時候，會回寫額度設定日，傳的日期就是上送當下的日期
				if (isEloan) {
					FacMain updFacMain = sFacMainService.holdById(tFacMain, titaVo);
					if (updFacMain != null) {
						updFacMain.setSettingDate(this.txBuffer.getTxCom().getTbsdy());
						try {
							sFacMainService.update(updFacMain, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "額度主檔" + e.getErrorMsg());
						}
					}

				}
			}
		}

		// 測試該擔保品代號是否存在擔保品主檔
		if (iClNo > 0) {
			tClMain = sClMainService.findById(clMainId);
		} else {
			tClMain = null;
		}

		if (tClMain == null) {
			if (iFunCd == 1) {

				this.info("新增時取號");

				String clCode = StringUtils.leftPad(String.valueOf(iClCode1), 2, "0")
						+ StringUtils.leftPad(String.valueOf(iClCode2), 2, "0");

				iClNo = gGSeqCom.getSeqNo(0, 0, "L2", clCode, 9999999, titaVo);

				finalClNo = StringUtils.leftPad(String.valueOf(iClNo), 7, "0");

				// new table PK
				clMainId = new ClMainId();
				clMovablesId = new ClMovablesId();

				// 塞pk
				clMainId.setClCode1(iClCode1);
				clMainId.setClCode2(iClCode2);
				clMainId.setClNo(iClNo);
				tClMain = new ClMain();
				// 搬值

				setClMain(titaVo);
				// 塞pk
				clMovablesId.setClCode1(iClCode1);
				clMovablesId.setClCode2(iClCode2);
				clMovablesId.setClNo(iClNo);
				tClMovables = new ClMovables();
				// 搬值
				setClMovables(titaVo);

				try {
					sClMainService.insert(tClMain);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品主檔");
				}
				try {
					sClMovablesService.insert(tClMovables);
				} catch (DBException e) {
					throw new LogicException("E0005", "擔保品動產檔");
				}

				if (iApplNo > 0) {
					List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
					String iOwnerId = titaVo.getParam("OwnerId");

					CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
					if (custMain != null) {
						String custUKey = custMain.getCustUKey().trim();
						String relCode = titaVo.getParam("OwnerRelCode").trim();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("OwnerCustUKey", custUKey);
						map.put("OwnerRelCode", relCode);
						ownerMap.add(map);
					}
					clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);

				} // if

			} else if (iFunCd == 2) {
				throw new LogicException("E0003", "擔保品主檔");
			} else if (iFunCd == 4) {
				throw new LogicException("E0004", "擔保品主檔");
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException("E0002", "擔保品主檔");
			} else if (iFunCd == 2) {

				tClMain = sClMainService.holdById(clMainId);
				tClMovables = sClMovablesService.holdById(clMovablesId);

				// 變更前
				ClMain beforeClMain = (ClMain) dataLog.clone(tClMain);
				ClMovables beforeClMovables = (ClMovables) dataLog.clone(tClMovables);
				setClMain(titaVo);
				setClMovables(titaVo);
				try {
					// 修改
					tClMain = sClMainService.update2(tClMain);
				} catch (DBException e) {
					// 修改時失敗
					throw new LogicException("E0007", "擔保品主檔");
				}
				try {
					// 修改
					tClMovables = sClMovablesService.update2(tClMovables);
				} catch (DBException e) {
					// 修改時失敗
					throw new LogicException("E0007", "擔保品動產檔");
				}

				// 紀錄變更前變更後 擔保品主檔
				dataLog.setEnv(titaVo, beforeClMain, tClMain);
				dataLog.exec("修改擔保品主檔資料");

				// 紀錄變更前變更後 動產檔
				dataLog.setEnv(titaVo, beforeClMovables, tClMovables);
				dataLog.exec("修改擔保品動產檔資料");

				if (iApplNo > 0) {
					List<HashMap<String, String>> ownerMap = new ArrayList<HashMap<String, String>>();
					String iOwnerId = titaVo.getParam("OwnerId");

					CustMain custMain = sCustMainService.custIdFirst(iOwnerId, titaVo);
					if (custMain != null) {
						String custUKey = custMain.getCustUKey().trim();
						String relCode = titaVo.getParam("OwnerRelCode").trim();

						FacMain facMain = sFacMainService.facmApplNoFirst(iApplNo, titaVo);
						if (facMain == null) {
							throw new LogicException(titaVo, "E0001", "核准號碼:" + iApplNo);
						}

						ClOwnerRelationId clOwnerRelationId = new ClOwnerRelationId();
						clOwnerRelationId.setCreditSysNo(facMain.getCreditSysNo());
						clOwnerRelationId.setCustNo(facMain.getCustNo());
						clOwnerRelationId.setOwnerCustUKey(custUKey);

						ClOwnerRelation clOwnerRelation = sClOwnerRelationService.holdById(clOwnerRelationId, titaVo);

						if (clOwnerRelation == null) {
							clOwnerRelation = new ClOwnerRelation();
							clOwnerRelation.setClOwnerRelationId(clOwnerRelationId);
							clOwnerRelation.setOwnerRelCode(relCode);
							try {
								sClOwnerRelationService.insert(clOwnerRelation, titaVo);
							} catch (DBException e) {
								throw new LogicException("E0005", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
							}
						} else {
							clOwnerRelation.setOwnerRelCode(relCode);
							try {
								sClOwnerRelationService.update(clOwnerRelation, titaVo);
							} catch (DBException e) {
								throw new LogicException("E0007", "擔保品所有權人與授信戶關係檔" + e.getErrorMsg());
							}
						} // else

					} // if

					if(this.isEloan) { // eloan 檢核不同核准號碼要新增額度關聯 2022.3.10
						ClFacId clFacId = new ClFacId();
						clFacId.setClCode1(iClCode1);
						clFacId.setClCode2(iClCode2);
						clFacId.setClNo(iClNo);
						clFacId.setApproveNo(iApplNo);	
						ClFac clFac = sClFacService.findById(clFacId, titaVo);
						if (clFac == null) {
							clFacCom.insertClFac(titaVo, iClCode1, iClCode2, iClNo, iApplNo, ownerMap);
						}
					} 
					
				} // if

			} else if (iFunCd == 4) {
				/* 刪除 */
				try {
					tClMovables = sClMovablesService.holdById(new ClMovablesId(iClCode1, iClCode2, iClNo));

					if (tClMovables != null) {
						sClMovablesService.delete(tClMovables);
					}
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品動產檔");
				}
				/* 刪除 */
				try {
					tClMain = sClMainService.holdById(clMainId);

					if (tClMain != null) {
						sClMainService.delete(tClMain);
					} else {
						throw new LogicException("E0004", "擔保品主檔");
					}
				} catch (DBException e) {
					throw new LogicException("E0008", "擔保品主檔");
				}
			}

		}
		this.totaVo.putParam("OClNo", finalClNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	/*
	 * 擔保品的唯一性規則
	 * 
	 * 300機器設備、350工具 =>廠牌+規格+所有權人ID
	 * 
	 * 320船舶、330漁船 =>船名
	 * 
	 * 310車輛 =>牌照號碼
	 * 
	 * 340航空器 =>引擎號碼
	 * 
	 */
	private int uniqueCheck(TitaVo titaVo) throws LogicException {
		int clNo = 0;

		String clTypeCode = titaVo.getParam("ClTypeCode");

		if ("300".equals(clTypeCode) || "350".equals(clTypeCode)) {
			Slice<ClMovables> sClMovables = sClMovablesService.findUnique1(titaVo.getParam("ProductBrand"),
					titaVo.getParam("ProductSpec"), titaVo.getParam("OwnerId"), 0, Integer.MAX_VALUE);
			List<ClMovables> lClMovables = sClMovables == null ? null : sClMovables.getContent();
			if (lClMovables != null) {
				for (ClMovables clMovables : lClMovables) {
					if (clMovables.getClCode1() == iClCode1 && clMovables.getClCode2() == iClCode2) {
						clNo = clMovables.getClNo();
					} else {
						throw new LogicException("E0012", "300機器設備、350工具 =>廠牌+規格+所有權人ID");
					}

				}
			}
		} else if ("320".equals(clTypeCode) || "330".equals(clTypeCode)) {
			Slice<ClMovables> sClMovables = sClMovablesService.findUnique2(titaVo.getParam("ProductBrand"), 0,
					Integer.MAX_VALUE);
			List<ClMovables> lClMovables = sClMovables == null ? null : sClMovables.getContent();
			if (lClMovables != null) {
				for (ClMovables clMovables : lClMovables) {
					if (clMovables.getClCode1() == iClCode1 && clMovables.getClCode2() == iClCode2) {
						clNo = clMovables.getClNo();
					} else {
						throw new LogicException("E0012", "320船舶、330漁船 =>船名");
					}

				}
			}

		} else if ("310".equals(clTypeCode)) {
			Slice<ClMovables> sClMovables = sClMovablesService.findUnique3(titaVo.getParam("LicenseNo"), 0,
					Integer.MAX_VALUE);
			List<ClMovables> lClMovables = sClMovables == null ? null : sClMovables.getContent();
			if (lClMovables != null) {
				for (ClMovables clMovables : lClMovables) {
					if (clMovables.getClCode1() == iClCode1 && clMovables.getClCode2() == iClCode2) {
						clNo = clMovables.getClNo();
					} else {
						throw new LogicException("E0012", "310車輛 =>牌照號碼");
					}

				}
			}

		} else if ("340".equals(clTypeCode)) {
			Slice<ClMovables> sClMovables = sClMovablesService.findUnique4(titaVo.getParam("EngineSN"), 0,
					Integer.MAX_VALUE);
			List<ClMovables> lClMovables = sClMovables == null ? null : sClMovables.getContent();
			if (lClMovables != null) {
				for (ClMovables clMovables : lClMovables) {
					if (clMovables.getClCode1() == iClCode1 && clMovables.getClCode2() == iClCode2) {
						clNo = clMovables.getClNo();
					} else {
						throw new LogicException("E0012", "340航空器 =>引擎號碼");
					}

				}
			}

		}

		return clNo;
	}

	private void setClMain(TitaVo titaVo) throws LogicException {
		tClMain.setClMainId(clMainId);
		tClMain.setClCode1(iClCode1);
		tClMain.setClCode2(iClCode2);
		tClMain.setClNo(iClNo);
		tClMain.setClTypeCode(titaVo.getParam("ClTypeCode"));
		tClMain.setCityCode(titaVo.getParam("CityCode"));/* 地區別 */
		tClMain.setClStatus(titaVo.getParam("ClStatus"));
		tClMain.setEvaDate(parse.stringToInteger(titaVo.getParam("EvaDate")));
		tClMain.setEvaAmt(parse.stringToBigDecimal(titaVo.getParam("EvaAmt")));
		tClMain.setSynd(titaVo.getParam("Synd"));
		tClMain.setSyndCode(titaVo.getParam("SyndCode"));
		tClMain.setDispPrice(parse.stringToBigDecimal(titaVo.getParam("DispPrice")));
		tClMain.setDispDate(parse.stringToInteger(titaVo.getParam("DispDate")));

		// 計算可分配金額
		BigDecimal shareTotal = new BigDecimal(0);

		// 鑑估總值
		BigDecimal evaAmt = new BigDecimal(titaVo.getParam("EvaAmt"));

		// 貸放成數
		BigDecimal loanToValue = new BigDecimal(titaVo.getParam("LoanToValue"));

		this.info("L2411 evaAmt = " + evaAmt.toString());
		this.info("L2411 loanToValue = " + loanToValue.toString());

//		"1.若""評估淨值""有值取""評估淨值""否則取""鑑估總值"")*貸放成數(四捨五入至個位數)
//		2.若設定金額低於可分配金額則為設定金額
//		3.擔保品塗銷/解除設定時(該筆擔保品的可分配金額設為零)"

		shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
		if (parse.stringToBigDecimal(titaVo.getParam("SettingAmt")).compareTo(shareTotal) < 0) {
			shareTotal = parse.stringToBigDecimal(titaVo.getParam("SettingAmt"));
		}

		if ("1".equals(titaVo.getParam("ClStat")) || "2".equals(titaVo.getParam("SettingStat"))) {
			tClMain.setShareTotal(BigDecimal.ZERO);
		} else {
			tClMain.setShareTotal(shareTotal);
		}
	}

	private void setClMovables(TitaVo titaVo) throws LogicException {
		tClMovables.setClMovablesId(clMovablesId);
		tClMovables.setClCode1(iClCode1);
		tClMovables.setClCode2(iClCode2);
		tClMovables.setClNo(iClNo);
		CustMain custMain = sCustMainService.custIdFirst(titaVo.getParam("OwnerId"), titaVo);
		if (custMain == null) {
			String Ukey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			custMain = new CustMain();
			custMain.setCustUKey(Ukey);
			custMain.setCustId(titaVo.getParam("OwnerId"));
			custMain.setCustName(titaVo.getParam("OwnerName"));
			custMain.setDataStatus(1);
			custMain.setTypeCode(2);
			if (titaVo.getParam("OwnerId").length() == 8) {
				custMain.setCuscCd("2");
			} else {
				custMain.setCuscCd("1");
			}
			try {
				sCustMainService.insert(custMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶資料主檔");
			}
		}
		tClMovables.setOwnerCustUKey(custMain.getCustUKey());

		tClMovables.setServiceLife(parse.stringToInteger(titaVo.getParam("ServiceLife")));
		tClMovables.setProductSpec(titaVo.getParam("ProductSpec"));
		tClMovables.setProductType(titaVo.getParam("ProductType"));
		tClMovables.setProductBrand(titaVo.getParam("ProductBrand"));
		tClMovables.setProductCC(titaVo.getParam("ProductCC"));
		tClMovables.setProductColor(titaVo.getParam("ProductColor"));
		tClMovables.setEngineSN(titaVo.getParam("EngineSN"));
		tClMovables.setLicenseNo(titaVo.getParam("LicenseNo"));
		tClMovables.setLicenseTypeCode(titaVo.getParam("LicenseTypeCode"));
		tClMovables.setLicenseUsageCode(titaVo.getParam("LicenseUsageCode"));
		tClMovables.setLiceneIssueDate(parse.stringToInteger(titaVo.getParam("LiceneIssueDate")));
		tClMovables.setMfgYearMonth(parse.stringToInteger(titaVo.getParam("MfgYearMonth")) + 191100);
		tClMovables.setVehicleTypeCode(titaVo.getParam("VehicleTypeCode"));
		tClMovables.setVehicleStyleCode(titaVo.getParam("VehicleStyleCode"));
		tClMovables.setVehicleOfficeCode(titaVo.getParam("VehicleOfficeCode"));
		tClMovables.setCurrency(titaVo.getParam("Currency"));
		tClMovables.setExchangeRate(parse.stringToBigDecimal(titaVo.getParam("ExchangeRate")));
		tClMovables.setInsurance(titaVo.getParam("Insurance"));
		tClMovables.setLoanToValue(parse.stringToBigDecimal(titaVo.getParam("LoanToValue")));
		tClMovables.setScrapValue(parse.stringToBigDecimal(titaVo.getParam("ScrapValue")));
		tClMovables.setMtgCode(titaVo.getParam("MtgCode"));
		tClMovables.setMtgCheck(titaVo.getParam("MtgCheck"));
		tClMovables.setMtgLoan(titaVo.getParam("MtgLoan"));
		tClMovables.setMtgPledge(titaVo.getParam("MtgPledge"));
		tClMovables.setSettingStat(titaVo.getParam("SettingStat"));
		tClMovables.setClStat(titaVo.getParam("ClStat"));
		tClMovables.setSettingAmt(parse.stringToBigDecimal(titaVo.getParam("SettingAmt")));
		tClMovables.setReceiptNo(titaVo.getParam("ReceiptNo"));
		tClMovables.setMtgNo(titaVo.getParam("MtgNo"));
		tClMovables.setReceivedDate(parse.stringToInteger(titaVo.getParam("ReceivedDate")));
		tClMovables.setMortgageIssueStartDate(parse.stringToInteger(titaVo.getParam("MortgageIssueStartDate")));
		tClMovables.setMortgageIssueEndDate(parse.stringToInteger(titaVo.getParam("MortgageIssueEndDate")));
		tClMovables.setRemark(titaVo.getParam("Remark"));
	}
}