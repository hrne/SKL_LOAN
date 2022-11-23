package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.springjpa.cm.L2075ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2075")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2075 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	@Autowired
	public ClOtherRightsService ClOtherRightsService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdLandOfficeService cdLandOfficeService;
	@Autowired
	public L2075ServiceImpl l2075ServiceImpl;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	public LoanCom loanCom;

	private OccursList occursList;
	int custNo = 0;
	int facmNo = 0;
	int closeNo = 0;
	int clCode1 = 0;
	int clCode2 = 0;
	int clNo = 0;
	int entryDate = 0;
	int applDate = 0;
	int closeDate = 0;
	String funCode = "";
	boolean isAllClose = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2075 ");
		this.totaVo.init(titaVo);

		// wk
		int wkCloseNo = 1;
		// new PK
		FacCloseId tFacCloseId = new FacCloseId();
		// new table
		FacClose tFacCloseMaxCloseNo = new FacClose();
		CustMain tCustMain = new CustMain();
		List<ClFac> lClFac = new ArrayList<ClFac>();
		List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();
		// new ArrayList
		List<FacClose> lFacClose = new ArrayList<FacClose>();
		Slice<FacClose> slFacClose = null;
//		long pdfSnoF = 0L ;
		this.index = 0;
		this.limit = 200;
		// tita
		int iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		int iType = parse.stringToInteger(titaVo.getParam("Type"));
		int iSearch = parse.stringToInteger(titaVo.getParam("Search"));

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l2075ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}
		this.info("resultList = " + resultList);
		this.info("resultList.size() = " + resultList.size());
		if (resultList != null && resultList.size() != 0) {

			int i = 1;
			for (Map<String, String> result : resultList) {
				// funcd1 勾選資料
				this.info("result = " + result);
				custNo = parse.stringToInteger(result.get("CustNo"));
				facmNo = parse.stringToInteger(result.get("FacmNo"));
				closeNo = parse.stringToInteger(result.get("MAXNO"));
				clCode1 = parse.stringToInteger(result.get("ClCode1"));
				clCode2 = parse.stringToInteger(result.get("ClCode2"));
				clNo = parse.stringToInteger(result.get("ClNo"));
				i++;
				int facmNoS = facmNo;
				int facmNoE = 999;
				if (facmNoS > 0) {
					facmNoE = facmNoS;
				}
				FacClose tFacClose = sFacCloseService.findById(new FacCloseId(custNo, closeNo), titaVo);
				entryDate = 0;
				applDate = 0;
				closeDate = 0;
				funCode = "";
				if (tFacClose != null) {
					entryDate = tFacClose.getEntryDate();
					applDate = tFacClose.getApplDate();
					closeDate = tFacClose.getCloseDate();
					funCode = tFacClose.getFunCode();
				}
				// 擔保品與額度關聯檔
				Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(custNo, facmNoS, facmNoE, 0, Integer.MAX_VALUE,
						titaVo);

				lClFac = slClFac == null ? null : slClFac.getContent();
				isAllClose = true;
				if (lClFac != null) {

					for (ClFac t2 : lClFac) {

						// 全部結案
						List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
						Slice<ClFac> slClFac2 = clFacService.clNoEq(t2.getClCode1(), t2.getClCode2(), t2.getClNo(), 0,
								Integer.MAX_VALUE, titaVo);
						l2ClFac = slClFac2 == null ? null : slClFac2.getContent();
						for (ClFac c : l2ClFac) {

							// 撥款主檔
							Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(),
									c.getFacmNo(), c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
							if (slLoanBorMain != null) {
								for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
									// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
									if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2
											|| tLoanBorMain.getStatus() == 4 || tLoanBorMain.getStatus() == 6
											|| tLoanBorMain.getStatus() == 8) {
										isAllClose = false;
										break;
									}
								}
							}
						}
					}
				}
				if (facmNoS == 0) {
					if (lClFac != null) {
						int clCode1 = 0;
						int clCode2 = 0;
						int clNo = 0;
						for (ClFac t3 : lClFac) {
							if (clCode1 != t3.getClCode1() || clCode2 != t3.getClCode2() || clNo != t3.getClNo()) {
								clCode1 = t3.getClCode1();
								clCode2 = t3.getClCode2();
								clNo = t3.getClNo();
							} else {
								continue;
							}
							Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findClNo(t3.getClCode1(),
									t3.getClCode2(), t3.getClNo(), 0, Integer.MAX_VALUE, titaVo);
							lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();

							if (lClOtherRights != null) {
								for (ClOtherRights tClOtherRights : lClOtherRights) {
									this.info("tClOtherRights = " + tClOtherRights);

									if (iType == 2 && iSearch == 1 && tClOtherRights.getReceiveFg() == 1) {
										this.info("已領取 ");
										continue;
									}

									moveOccursList(tClOtherRights, titaVo);
								}
							}
						}
					}
				} else {
					Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findClNo(clCode1, clCode2, clNo, 0,
							Integer.MAX_VALUE, titaVo);
					lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();

					if (lClOtherRights != null) {
						for (ClOtherRights tClOtherRights : lClOtherRights) {
							this.info("tClOtherRights = " + tClOtherRights);

							if (iType == 2 && iSearch == 1 && tClOtherRights.getReceiveFg() == 1) {
								this.info("已領取 ");
								continue;
							}
							moveOccursList(tClOtherRights, titaVo);
						}
					}
				}

			}
		} else {

			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		if (resultList != null && resultList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveOccursList(ClOtherRights tClOtherRights, TitaVo titaVo) {

		occursList = new OccursList();
		// wk
		String wkCityItem = "";
		String wkLandOfficeItem = "";
		String wkRecWordItem = "";
		occursList.putParam("OOTranDate", entryDate);
		occursList.putParam("OOApplDate", applDate);
		occursList.putParam("OOFunCode", funCode);
		occursList.putParam("OOCustNo", custNo);
		occursList.putParam("OOCloseNo", closeNo);
		occursList.putParam("OOCustName", loanCom.getCustNameByNo(custNo));
		occursList.putParam("OOFacmNo", facmNo);
		occursList.putParam("OOClCode1", tClOtherRights.getClCode1());
		occursList.putParam("OOClCode2", tClOtherRights.getClCode2());
		occursList.putParam("OOClNo", tClOtherRights.getClNo());
		occursList.putParam("OOSeq", tClOtherRights.getSeq());
		// 找縣市名稱
		if ("".equals(tClOtherRights.getOtherCity())) {
			CdCity tCdCity = cdCityService.findById(tClOtherRights.getCity(), titaVo);
			if (tCdCity != null) {
				wkCityItem = tCdCity.getCityItem();
			} else {
				CdCode tCdCode = cdCodeService.findById(new CdCodeId("ClOtherRightsCityCd", tClOtherRights.getCity()),
						titaVo);
				wkCityItem = tCdCode.getItem();
			}
		} else {
			wkCityItem = tClOtherRights.getOtherCity();
		}
		occursList.putParam("OOCity", wkCityItem);
		// 找地政所名稱
		if ("".equals(tClOtherRights.getOtherLandAdm())) {
			CdCode tCdCode = cdCodeService.findById(new CdCodeId("LandOfficeCode", tClOtherRights.getLandAdm()),
					titaVo);
			if (tCdCode != null) {
				wkLandOfficeItem = tCdCode.getItem();
			}
		} else {
			wkLandOfficeItem = tClOtherRights.getOtherLandAdm();
		}
		occursList.putParam("OOLandAdm", wkLandOfficeItem);
		occursList.putParam("OORecYear", tClOtherRights.getRecYear());
		// 找 收件字名稱
		if ("".equals(tClOtherRights.getOtherRecWord())) {
			CdLandOffice tCdLandOffice = cdLandOfficeService.findById(new CdLandOfficeId(tClOtherRights.getCity(),
					tClOtherRights.getLandAdm(), tClOtherRights.getRecWord()), titaVo);
			if (tCdLandOffice != null) {
				wkRecWordItem = tCdLandOffice.getRecWordItem();
			}
		} else {
			wkRecWordItem = tClOtherRights.getOtherRecWord();
		}
		occursList.putParam("OORecWord", wkRecWordItem);
		occursList.putParam("OORecNumber", tClOtherRights.getRecNumber());
		occursList.putParam("OORightsNote", tClOtherRights.getRightsNote());
		occursList.putParam("OOSecuredTotal", tClOtherRights.getSecuredTotal());
		if (isAllClose) {
			occursList.putParam("OOAllClose", "Y");
		} else {
			occursList.putParam("OOAllClose", "N");
		}

		this.info("occursList L2075" + occursList);
		this.totaVo.addOccursList(occursList);
	}
}