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
import com.st1.itx.db.service.CustMainService;
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
	public CustMainService sCustMainService;
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
		FacClose tFacClose = new FacClose();
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


			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				resultList = l2075ServiceImpl.findAll(this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.info("Error ... " + e.getMessage());
			}
			this.info("resultList = " + resultList);
			this.info("resultList.size() = " + resultList.size());
			if (resultList != null && resultList.size() != 0) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (resultList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

				int i = 1;
				for (Map<String, String> result : resultList) {
					// funcd1 勾選資料
					OccursList occursList = new OccursList();

//				輸入入帳日期不顯示補領補發
					if (iEntryDate > 0) {
						if ("2".equals(result.get("FunCode")) || "3".equals(result.get("FunCode"))) {
							i++;
							continue;
						}
//					輸入申請日期不顯示請領
					} else if (iApplDate > 0) {
						if ("0".equals(result.get("FunCode")) || "1".equals(result.get("FunCode"))) {
							i++;
							continue;
						}
					}

					i++;
					int facmNoS = parse.stringToInteger(result.get("FacmNo"));
					int facmNoE = 999;
					if (facmNoS > 0) {
						facmNoE = facmNoS;
					}

					// 擔保品與額度關聯檔
					Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(
							parse.stringToInteger(result.get("CustNo")), facmNoS, facmNoE, 0, Integer.MAX_VALUE,
							titaVo);

					lClFac = slClFac == null ? null : slClFac.getContent();
					boolean isAllClose = true;
					if (lClFac != null) {

						for (ClFac t2 : lClFac) {

							// 全部結案
							List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
							Slice<ClFac> slClFac2 = clFacService.clNoEq(t2.getClCode1(), t2.getClCode2(), t2.getClNo(),
									0, Integer.MAX_VALUE, titaVo);
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
					int entryDate = parse.stringToInteger(result.get("EntryDate"));
					if (entryDate > 0) {
						entryDate = entryDate - 19110000;
					}
					int applDate = parse.stringToInteger(result.get("ApplDate"));
					if (applDate > 0) {
						applDate = applDate - 19110000;
					}
					int closeDate = parse.stringToInteger(result.get("CloseDate"));
					if (closeDate > 0) {
						closeDate = closeDate - 19110000;
					}

					Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findClNo(
							parse.stringToInteger(result.get("ClCode1")), parse.stringToInteger(result.get("ClCode2")),
							parse.stringToInteger(result.get("ClNo")), 0, Integer.MAX_VALUE, titaVo);
					lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();

					if (lClOtherRights != null) {
						this.info("lClOtherRights != null");
						for (ClOtherRights tClOtherRights : lClOtherRights) {

							// wk
							String wkCityItem = "";
							String wkLandOfficeItem = "";
							String wkRecWordItem = "";
							occursList.putParam("OOTranDate", entryDate);
							occursList.putParam("OOApplDate", applDate);
							occursList.putParam("OOFunCode", result.get("FunCode"));
							occursList.putParam("OOCustNo", result.get("CustNo"));
							occursList.putParam("OOCloseNo", result.get("CloseNo"));
							occursList.putParam("OOCustName",
									loanCom.getCustNameByNo(parse.stringToInteger(result.get("CustNo"))));
							occursList.putParam("OOFacmNo", result.get("FacmNo"));
							occursList.putParam("OOClCode1", tClOtherRights.getClCode1());
							occursList.putParam("OOClCode2", tClOtherRights.getClCode2());
							occursList.putParam("OOClNo", tClOtherRights.getClNo());
							occursList.putParam("OOSeq", tClOtherRights.getSeq());
							// 找縣市名稱
							if ("".equals(tClOtherRights.getOtherCity())) {
								CdCity tCdCity = cdCityService.findById(tClOtherRights.getCity(), titaVo);
								if (tCdCity != null) {
									wkCityItem = tCdCity.getCityItem();
								}
							} else {
								wkCityItem = tClOtherRights.getOtherCity();
							}
							occursList.putParam("OOCity", wkCityItem);
							// 找地政所名稱
							if ("".equals(tClOtherRights.getOtherLandAdm())) {
								CdCode tCdCode = cdCodeService
										.findById(new CdCodeId("LandOfficeCode", tClOtherRights.getLandAdm()), titaVo);
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
								CdLandOffice tCdLandOffice = cdLandOfficeService.findById(
										new CdLandOfficeId(tClOtherRights.getLandAdm(), tClOtherRights.getRecWord()),
										titaVo);
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

				}
			} else {

				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l2075ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
}