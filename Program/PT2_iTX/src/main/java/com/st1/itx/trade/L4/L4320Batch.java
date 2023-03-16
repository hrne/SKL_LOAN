package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.BatxBaseRateChange;
import com.st1.itx.db.domain.BatxBaseRateChangeId;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.BatxBaseRateChangeService;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.springjpa.cm.L4320ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.StaticTool;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4320Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4320Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public WebClient webClient;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public ClMainService clMainService;

	@Autowired
	public FacProdService facProdService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	private BatxBaseRateChangeService sBatxBaseRateChangeService;

	@Autowired
	private LoanRateChangeService sLoanRateChangeService;

	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Autowired
	public CdCommService cdCommService;

	@Autowired
	public L4320ServiceImpl l4320BatchServiceImpl;

	@Autowired
	public L4320Report l4320Report;

	private HashMap<tmpBorm, BigDecimal> facRate = new HashMap<>();
	private HashMap<tmpBorm, Integer> facRateFlag = new HashMap<>();

	private int iTxKind = 0;
	private int iEffectDate = 0;
	private int iEffectMonth = 0;
	private int iNextAdjPeriod = 0;
	private String iBaseRateCode;
	private BigDecimal iRateIncr = BigDecimal.ZERO;
	private BigDecimal iRate = BigDecimal.ZERO;
	private BigDecimal iBaseRate = BigDecimal.ZERO;
	private int iCustType = 0;

	private String checkMsg = "";
	private String warnMsg = "";
	private String sendMsg = "";
	private int wkAdjDate = 0;
	private int wkLastMonthDateS = 0;
	private int commitCnt = 200;
	private int processCnt = 0;
	private int custNo = 0;
	private int facmNo = 0;
	private int bormNo = 0;
	private Boolean flag = true;
	private TempVo subsidyRateVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4320Batch ");
		this.totaVo.init(titaVo);
		// 作業項目
		iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));

		// 生效日期
		iEffectDate = parse.stringToInteger(titaVo.getParam("EffectDate"));

		// 生效月份
		iEffectMonth = parse.stringToInteger(titaVo.getParam("EffectMonth"));

		// 指標利率種類
		iBaseRateCode = titaVo.getParam("BaseRateCode");

		// 指標利率
		iBaseRate = parse.stringToBigDecimal(titaVo.getParam("BaseRate"));

		// 批次加減碼
		iRateIncr = parse.stringToBigDecimal(titaVo.getParam("RateIncr"));

		// 批次利率
		iRate = parse.stringToBigDecimal(titaVo.getParam("Rate"));

		// 預調週期
		iNextAdjPeriod = parse.stringToInteger(titaVo.getParam("NextAdjPeriod"));

		// 戶別
		iCustType = parse.stringToInteger(titaVo.getParam("CustType"));

		// 調整日期
		wkAdjDate = this.getTxBuffer().getTxCom().getTbsdy();

		// 本月月初日
		wkLastMonthDateS = this.getTxBuffer().getTxCom().getTmndy() / 100;
		wkLastMonthDateS = wkLastMonthDateS * 100 + 01;
		this.info("iEffectMonth = " + iEffectMonth + ", iBaseRateCode=" + iBaseRateCode);

//		正常
		if (titaVo.isHcodeNormal()) {
			// 政府補貼利率(郵局指標利率)
			if ("02".equals(iBaseRateCode)) {
				CdComm tCdComm = cdCommService.CdTypeDescFirst("01", "01", 0, iEffectDate + 19110000, titaVo);
				if (tCdComm != null) {
					subsidyRateVo = subsidyRateVo.getVo(tCdComm.getJsonFields());
				}
			}
			// 2022-05-13 ST1 Wei 新增
			// 若作業項目為1:定期機動調整
			// 且 指標利率生效日=本次利率調整生效月份
			// 調整 所有受此指標利率影響的借戶利率
			if (iTxKind == 1 && (iEffectDate / 100) == iEffectMonth) {
				doBaseRateChange(titaVo);
			}
			// 整批利率調整
			try {
				execute(0, titaVo); // 0: 整批利率調整
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}
			// 定期機動檢核件
			if (flag && iTxKind == 1) {
				facRate = new HashMap<>();
				facRateFlag = new HashMap<>();
				try {
					execute(9, titaVo); // 9:定期機動檢核件
				} catch (LogicException e) {
					sendMsg = e.getErrorMsg();
					flag = false;
				}
			}
		}
//		訂正
		else {
			try {
				deleteBatxRate(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}
		}

		// 指標利率檔生效記號
		if (!iBaseRateCode.trim().isEmpty()) {
			CdBaseRate tCdBaseRate = new CdBaseRate();
			CdBaseRateId tCdBaseRateId = new CdBaseRateId();
			tCdBaseRateId.setBaseRateCode(iBaseRateCode);
			tCdBaseRateId.setCurrencyCode("TWD");
			tCdBaseRateId.setEffectDate(iEffectDate);
			tCdBaseRate = cdBaseRateService.findById(tCdBaseRateId, titaVo);
			if (tCdBaseRate != null) {
				if (titaVo.isHcodeNormal()) {
					tCdBaseRate.setEffectFlag(1); // 1:已生效
				} else {
					if (titaVo.get("EffectFlag") != null) {
						tCdBaseRate.setEffectFlag(parse.stringToInteger(titaVo.getParam("EffectFlag")));
					}
				}
			}
		}
		// 產出清單
		if (flag && titaVo.isHcodeNormal() && this.processCnt > 0) {
			this.info("產出清單");
			this.batchTransaction.commit();
			l4320Report.exec(titaVo);
		}

		if (flag) {
			// Broadcast message Link to L4031-利率調整清單
			setSendMsg(titaVo);

			if (this.processCnt > 0) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4031",
						titaVo.getTlrNo(), sendMsg + "，筆數：" + this.processCnt, titaVo);
				if (titaVo.isHcodeNormal()) {
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
							titaVo.getTlrNo() + "L4320", sendMsg, titaVo);
				}
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "",
						sendMsg + "，筆數：" + this.processCnt, titaVo);
			}
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", sendMsg, titaVo);
		}

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 設定訊息內容
	private void setSendMsg(TitaVo titaVo) {
		switch (iTxKind) {
		case 1:
			sendMsg = "定期機動指數利率變動資料";
			break;
		case 2:
			sendMsg = "機動指數利率變動資料";
			break;
		case 3:
			sendMsg = "機動非指數利率變動資料";
			break;
		case 4:
			sendMsg = "員工利率變動資料";
			break;
		case 5:
			sendMsg = "按商品別利率變動資料";
			break;
		default:
			break;
		}

		if (titaVo.isHcodeNormal()) {
			sendMsg += "已產生";
		} else {
			sendMsg += "已訂正";
		}

		if (iTxKind <= 3) {
			if (this.iCustType == 1) {
				sendMsg = "個金，" + sendMsg;
			} else {
				sendMsg = "企金，" + sendMsg;
			}
		}
	}

	private void execute(int iAdjCode, TitaVo titaVo) throws LogicException {

//      抓取資料
		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l4320BatchServiceImpl.findAll(iAdjCode, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage()); // 檢查錯誤
		}

		// 檢查未放行
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				if ("1".equals(s.get("ActFg"))) {
					flag = false;
					sendMsg += " 戶號：" + s.get("CustNo");
				}
			}
		}
		if (!flag) {
			throw new LogicException("E0015", sendMsg + " 資料待放行中"); // 檢查錯誤
		}

//		額度下撥款的目前利率是否相同
		setFacRate(fnAllList);

//		運算輸出欄位
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				this.info("fnAllList=" + s);
				/* 判斷新增或更新整批利率調整檔，已確認跳過 */
				checkMsg = "";
				warnMsg = "";
				// F0 戶號
				// F1 額度
				// F2 撥款
				custNo = parse.stringToInteger(s.get("CustNo"));
				facmNo = parse.stringToInteger(s.get("FacmNo"));
				bormNo = parse.stringToInteger(s.get("BormNo"));
				BatxRateChange b = new BatxRateChange();
				BatxRateChangeId bId = new BatxRateChangeId();
				// 調整日期
				bId.setAdjDate(wkAdjDate);
				bId.setCustNo(custNo);
				bId.setFacmNo(facmNo);
				bId.setBormNo(bormNo);
				this.info("bId=" + bId.toString());
				b = batxRateChangeService.holdById(bId, titaVo);
				// 已確認跳過
				boolean isInsert = false;
				if (b == null) {
					isInsert = true;
					b = new BatxRateChange();
					b.setBatxRateChangeId(bId);
				} else {
					if (b.getConfirmFlag() >= 1) {
						continue;
					}
				}
				/* 設定各項值 */
				seBatxRateChange(iAdjCode, b, s, titaVo);

				// commit per commitCnt
				this.processCnt++;
				if (this.processCnt % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				/* 新增或更新整批利率調整檔 */
				if (isInsert) {
					try {
						b.setTitaTlrNo(titaVo.getTlrNo());
						b.setTitaTxtNo(titaVo.getTxtNo());
						batxRateChangeService.insert(b, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", ", BatxRateChange insert is error : " + e.getErrorMsg());
					}
				} else {
					try {
						b.setTitaTlrNo(titaVo.getTlrNo());
						b.setTitaTxtNo(titaVo.getTxtNo());
						batxRateChangeService.update(b, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "BatxRateChange update error : " + e.getErrorMsg());
					}
				}
			}
		}
	}

	/* 設定各項值 */
	private void seBatxRateChange(int iAdjCode, BatxRateChange b, Map<String, String> s, TitaVo titaVo)
			throws LogicException {
		this.info("seBatxRateChange ... iAdjCode=" + iAdjCode);
		TempVo tTempVo = new TempVo();
		// 作業項目
		b.setTxKind(iTxKind);

		// F4 上次繳息日,繳息迄日
		// DrawdownDate 撥款日期
		int prevIntDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("PrevPayIntDate")));
		int drawdownDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("DrawdownDate")));
		if (prevIntDate == 0) {
			b.setPrevIntDate(drawdownDate);
		} else {
			b.setPrevIntDate(prevIntDate);
		}
		// 入帳日
		int entryDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("EntryDate")));
		// F6 餘額
		// F7 撥款金額
		BigDecimal loanBal = parse.stringToBigDecimal(s.get("LoanBal"));
		BigDecimal drawdownAmt = parse.stringToBigDecimal(s.get("DrawdownAmt"));
		b.setLoanBalance(loanBal);
		b.setDrawdownAmt(drawdownAmt);
		// 全戶餘額
		b.setTotBalance(parse.stringToBigDecimal(s.get("TotBalance")));
		// F11 員工利率記號
		// F12 借戶利率檔是否依合約記號
		String incrFlag = s.get("IncrFlag");
		b.setIncrFlag(incrFlag);
		// F13 借戶利率檔指標利率代碼
		String baseRateCode = s.get("BaseRateCode");
		b.setBaseRateCode(baseRateCode);
		// F15 借戶利率檔利率區分 共用代碼檔 1: 機動 2: 固動 3: 定期機動
		String rateCode = s.get("RateCode");
		b.setRateCode(rateCode);
		// F16 借戶利率檔商品代碼
		String prodNo = s.get("ProdNo");
		b.setProdNo(prodNo);
		// F20 企金別
		int entCode = parse.stringToInteger(s.get("EntCode"));
		b.setCustCode(entCode);
		// F21 擔保品地區別
		String cityCode = s.get("CityCode");
		b.setCityCode(cityCode);
		// F22 擔保品鄉鎮別
		b.setAreaCode(s.get("AreaCode"));
		// F23 地區別利率上限
		BigDecimal cityRateCeiling = parse.stringToBigDecimal(s.get("CityRateCeiling"));
		// F24 地區別利率下限
		BigDecimal cityRateFloor = parse.stringToBigDecimal(s.get("CityRateFloor"));
		// IntRateIncr 地區別利率加減碼
		BigDecimal cityRateIncr = parse.stringToBigDecimal(s.get("CityRateIncr"));

		// F8 額度核准利率
//		BigDecimal approveRateFac = parse.stringToBigDecimal(s.get("FacApproveRate"));
		// F9 額度加碼利率
//		BigDecimal rateIncrFac = parse.stringToBigDecimal(s.get("FacRateIncr"));
		// F10 額度個人加碼利率(未用)
//		BigDecimal individualIncr = parse.stringToBigDecimal(s.get("FacIndividualIncr"));

		// F17 借戶利率檔加碼利率
		BigDecimal rateIncr = parse.stringToBigDecimal(s.get("RateIncr"));
		b.setContrRateIncr(rateIncr);

		// F18 借戶利率檔個人加碼利率
		BigDecimal individualIncr = parse.stringToBigDecimal(s.get("IndividualIncr"));
		b.setIndividualIncr(individualIncr);

		// F14 借戶利率檔適用利率
		BigDecimal presentRate = parse.stringToBigDecimal(s.get("FitRate"));

		// F19 借戶利率檔生效日
		int presentEffectDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("EffectDate")));

		// 本次生效日
		int effDateCurt = 0;

		// 機動非指數=>抓出的為預調利率(本次生效日)，需抓生效月份前的資料為目前利率與目前生效日
		if (iTxKind == 3) {
			effDateCurt = presentEffectDate;
			presentRate = parse.stringToBigDecimal(s.get("PresentRate"));
			presentEffectDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("PresEffDate")));
		}

		// 目前利率 = 借戶利率檔適用利率
		b.setPresentRate(presentRate);

		// 目前生效日 = 借戶利率檔生效日
		b.setPresEffDate(presentEffectDate);

		// 合約利率=生效之指標利率+目前之合約加減碼； 自訂利率 =>0
		BigDecimal contrBaseRate = BigDecimal.ZERO;
		BigDecimal contractRate = BigDecimal.ZERO;
		if (!"99".equals(baseRateCode)) {
			contrBaseRate = iBaseRate;
			contractRate = contrBaseRate.add(rateIncr);
		}
		b.setContrBaseRate(contrBaseRate);
		b.setContractRate(contractRate);

		// NextPayIntDate 下次繳息日,下次應繳日
		int nextPayIntDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("NextPayIntDate")));
		// MaturityDate 到期日期
		int maturityDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("MaturityDate")));
		// 利率調整週期
		int rateAdjFreq = parse.stringToInteger(s.get("RateAdjFreq"));
		b.setPreNextAdjFreq(rateAdjFreq);
		// 主檔下次利率調整日期
		int nextAdjRateDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("NextAdjRateDate")));

		// 預定下次利率調整日期
		// 1). 下次調整日月份>=到期日月份，則下次調整日為到期日
		// 2). 首次利率調整日為月底日取首次利率調整日/本次利率調整日/首撥日/到期日的最大DD的相對日為下次利率調整日
		int preNextAdjDate = 0;
		if (iTxKind == 1) {
			if (iAdjCode == 9 || nextAdjRateDate == maturityDate) {
				preNextAdjDate = nextAdjRateDate;
			} else {
				// 預定下次利率調整日 = 主檔下次利率調整日期 + 利率調整週期
				dateUtil.init();
				dateUtil.setDate_1(nextAdjRateDate);
				dateUtil.setMons(rateAdjFreq); // 調整周期(單位固定為月)
				preNextAdjDate = dateUtil.getCalenderDay();
				int preDD = preNextAdjDate % 100;
				// 預定下次利率調整日月底日
				dateUtil.init();
				dateUtil.setDate_1(preNextAdjDate);
				int preNextAdjDateMonLimit = dateUtil.getMonLimit();

				// 首次利率調整日是否為月底日
				int firstAdjRateDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("FirstAdjRateDate")));
				dateUtil.init();
				dateUtil.setDate_1(firstAdjRateDate);
				// 首次利率調整日為月底日取首次利率調整日/本次利率調整日/首撥日/到期日的最大DD
				int maxDD;
				if (firstAdjRateDate % 100 == dateUtil.getMonLimit()) {
					maxDD = Math.max(firstAdjRateDate % 100, nextAdjRateDate);
					int firstDrawdownDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("FirstDrawdownDate")));
					maxDD = Math.max(maxDD, firstDrawdownDate % 100);
					maxDD = Math.max(maxDD, maturityDate % 100);
					// 不可大於月底日
					if (maxDD > preNextAdjDateMonLimit) {
						maxDD = preNextAdjDateMonLimit;
					}
					preNextAdjDate = (preNextAdjDate / 100) * 100 + maxDD;
				}
				// 下次調整日月份>=到期日月份，則下次調整日為到期日
				if (preNextAdjDate / 100 >= maturityDate / 100) {
					if (preNextAdjDate > maturityDate) {
						warnMsg += ", 下次利率調整日+ 調整周期 > 到期日";
					} else if (preNextAdjDate == maturityDate) {
						warnMsg += ", 下次利率調整日+ 調整周期 = 到期日";
					} else {
						warnMsg += ", 下次利率調整月份 + 調整周期 = 到期日月份"; // 不及通知客戶，故不調整
					}
					preNextAdjDate = maturityDate;
				} else if (preNextAdjDate % 100 > preDD) {
					warnMsg += ", 下次利率調整日>相對日";
				}
			}
		}
		b.setPreNextAdjDate(preNextAdjDate);

		// 本次指標利率
		b.setCurrBaseRate(iBaseRate);

		// 調整記號 1.批次自動調整 2.按地區別自動調整 3.人工調整(未調整) 4.批次自動調整(提醒建) 9.上次繳息日大於利率生效日
		int adjCode = 0;
		// 擬調利率
		BigDecimal rateProp = BigDecimal.ZERO;

		// 逾期期數
		b.setOvduTerm(0);
		if (nextPayIntDate < wkLastMonthDateS) {
			dateUtil.init();
			dateUtil.setDate_1(nextPayIntDate);
			dateUtil.setDate_2(wkLastMonthDateS);
			dateUtil.dateDiff();
			b.setOvduTerm(dateUtil.getMons());
		}

		/* 依作業項目設定 */
//作業項目 
		switch (iTxKind) {
//
//1.定期機動調整
//		                  輸入: 1.生效月份:   
//		                2.利率種類:   自動顯示: 保單分紅利率: 1.4000 生效日期:109/10/01    (default 01)
//		                3.戶別種類:      
//		                  抓取: 1.撥款主檔的利率區分=3.定期機動，下次利率調整日為調整月份      
//		                        2.借戶利率檔的的利率區分=3.定期機動，指標利率種類=該指標利率種類抓，生效日期 <= 變動月份 			                                  
//		                  處理: //		                2.利率按合約 Y  => 1.批次自動調整
//		                3.利率按合約 N  => 2.按地區別自動調整  (有地區別)
//		                1.逾一期             => 3.人工調整               => 4.人工調整(依地區別)		     
		case 1:
			// 本次生效日 = 主檔下次利率調整日
			effDateCurt = nextAdjRateDate;
			// 利率按合約 Y ， 本次利率 = 本次指標利率 + 目前合約加減碼
			// 利率按合約 N && 有地區別， 本次利率 = 目前利率 + 地區別加減碼，依地區別利率上、下限調整，不可超過合約加碼利率
			// 利率按合約 N && 無地區別， 本次利率 = 本次指標利率 +借戶利率檔個人加碼利率
			if ("Y".equals(incrFlag)) {
				rateProp = iBaseRate.add(rateIncr);
			} else {
				rateProp = iBaseRate.add(individualIncr);
			}

			// 定期機動檢核提醒件
			if (iAdjCode == 9) {
				// 下次利率調整日小於調整月份列入[檢核提醒件]
				adjCode = 9;
				if (nextAdjRateDate < wkLastMonthDateS && nextAdjRateDate < maturityDate) {
					warnMsg += ", 下次利率調整日小於調整月份";
				}
			} else if (effDateCurt == maturityDate) {
				// 下次利率調整日為到期日者於整批利率調整時列入[檢核提醒件],不調整利率
				adjCode = 9;
				warnMsg += ", 下次利率調整日為到期日";
			} else if ("Y".equals(incrFlag)) {
				adjCode = 1; // 批次自動調整
			} else if ("".equals(cityCode.trim()) || this.iCustType == 2) {
				adjCode = 3; // 人工調整(按合約)
			} else {
				adjCode = 2; // 按地區別調整
				tTempVo.putParam("CityRateIncr", cityRateIncr);
				tTempVo.putParam("CityRateCeiling", cityRateCeiling);
				tTempVo.putParam("CityRateFloor", cityRateFloor);
				// 本次利率 = 目前利率 + 地區別加減碼
				rateProp = presentRate.add(cityRateIncr);
				String warn = "";
				// 依地區別利率上、下限調整
				if (rateProp.compareTo(cityRateCeiling) > 0) {
					rateProp = cityRateCeiling;
					warn = ", 高於地區上限，依地區上限利率調整";
				}
				if (rateProp.compareTo(cityRateFloor) < 0) {
					rateProp = cityRateFloor;
					warn = ", 低於地區下限，依地區下限利率調整";
				}
				if (rateIncr.compareTo(BigDecimal.ZERO) > 0 && contractRate.compareTo(rateProp) < 0) {
					rateProp = contractRate;
					warn = ", 高於合約利率，依合約利率調整";
				}
				warn += ", 地區別加減碼設定值:" + cityRateIncr;
				warnMsg += warn;
			}

			// 逾一期 => 下次繳息日,下次應繳日 < 上次月初日
			if (adjCode == 2 && b.getOvduTerm() >= 1) {
				adjCode = 4; // 人工調整(按地區別)
				warnMsg += ", 逾" + dateUtil.getMons() + " 期 ";
			}

			// 僅調整下次利率調整日
			if (rateProp.compareTo(presentRate) == 0) {
				warnMsg += ", 維持目前利率, 調整下次利率調整日";
			}

			break;

//2.機動指數利率調整
//		                  輸入: 1.利率種類 :      自動顯示 xxxx利率: 1.4000 生效日期:109/10/05    (default 02 : 不可輸入 01 , 99)				                  
//		               2.戶別種類 : 			                        
//		                  抓取: 1.撥款主檔的利率區分=1.機動      
//		               2.借戶利率檔的利率區分=1.機動,指標利率種類=該指標利率種類,生效日期 <= 調整日期 			                                    
//		                  處理: 1.無論是否逾期均自動調整   
		case 2:
			// 本次生效日 = 指標利率生效日
			effDateCurt = iEffectDate;
			// 加碼值 = 目前合約加減碼-政府補貼利率差額
			// 擬調利率 = 本次指標利率 + 加碼值
			BigDecimal subsidyRateDiff = BigDecimal.ZERO;
			if ("02".equals(iBaseRateCode)) {
				// 政府補貼利率差額(郵局指標利率)
				subsidyRateDiff = getSubsidyRateDiff(s.get("GovOfferFlag"), presentEffectDate, titaVo);
				if (!"N".equals(s.get("GovOfferFlag"))) {
					warnMsg += ", 政府補貼利率差額:" + subsidyRateDiff;
					tTempVo.putParam("SubsidyRateDiff", subsidyRateDiff);
				}
				rateIncr = rateIncr.subtract(subsidyRateDiff);
			}
			rateProp = iBaseRate.add(rateIncr);
			// 1.自動調整
			adjCode = 1;
			break;

//3.機動非指數利率調整
//
//		                  輸入: 生效月份 : default下個月 
//		                             戶別種類 :    
//		                             預調週期 : 6 個月    (default)            			                                    
//		                 抓取: 1.撥款主檔的利率區分=1.機動     
//		               2.借戶利率檔的利率區分=1.機動,指標利率種類=99，生效日期 = 調整月份，商品<>員工利率			                        
//		                 處理: 1.按地區別利率調整設定，計算擬調利率                      
//		               2.全歸 3.人工調整 
//		               3.確認後預調週期、預調利率，新增一筆借戶預調利率資料(生效日為借戶利率檔生效日期 + 預調週期)			             
		case 3:
			// 本次生效日(已放好)
			// 本次利率 = 原利率 + 地區別利率
			if ("".equals(cityCode.trim()) || this.iCustType == 2) {
				adjCode = 3; // 人工調整(按合約)
			} else {
				tTempVo.putParam("CityRateIncr", cityRateIncr);
				tTempVo.putParam("CityRateCeiling", cityRateCeiling);
				tTempVo.putParam("CityRateFloor", cityRateFloor);
				rateProp = presentRate.add(cityRateIncr);
				// 依地區別利率上、下限調整
				if (rateProp.compareTo(cityRateCeiling) > 0) {
					rateProp = cityRateCeiling;
					warnMsg += ", 高於地區上限 ";
				}
				if (rateProp.compareTo(cityRateFloor) < 0) {
					rateProp = cityRateFloor;
					warnMsg += ", 低於地區下限 ";
				}
				warnMsg += ", 地區別加減設定值:" + cityRateIncr;
				adjCode = 4; // 人工調整(按地區別)
			}
			break;
//4.員工利率調整
//		                  輸入: 生效日期   : 109/10/05 
//		                            批次加減碼 :      
//		                  抓取: 1.撥款主檔的利率區分=1.機動   
//		                2.借戶利率檔的利率區分=1.機動,指標利率種類=99,生效日期 <= 調整日期，商品=員工利率  				                                   
//		                  處理: 1.批次自動調整
		case 4:
			// 本次生效日 =生效日期
			effDateCurt = iEffectDate;
			// 本次利率 = 原利率 + 批次加減碼
			if (iRate.compareTo(BigDecimal.ZERO) > 0) {
				rateProp = iRate;
			} else {
				rateProp = presentRate.add(iRateIncr);
			}
			adjCode = 1; // 1.自動調整
			break;
//5.按商品別調整		     
//		                  輸入: 生效日期   : 109/10/05 			                  
//		                           商品: ----- ----- -----	(可輸入商品建代碼檔)  	
//			                    批次加減碼:
//		                  抓取: 1.借戶利率檔商品為輸入商品		                       
//		                  處理: 1.全歸 3.人工調整 
		case 5:
			// 本次生效日 =生效日期
			effDateCurt = iEffectDate;
			// 本次利率 = 目前利率 + 批次加減碼
			if (iRate.compareTo(BigDecimal.ZERO) > 0) {
				rateProp = iRate;
			} else {
				rateProp = presentRate.add(iRateIncr);
			}
			adjCode = 3; // 人工調整(按合約)
			break;
		default:
			break;
		}
		// 本次生效日
		b.setCurtEffDate(effDateCurt);
		// 擬調利率
		b.setProposalRate(rateProp);

		/* 檢核有誤 */
		int errorFlag = 0;

		if (adjCode <= 2 && "3".equals(rateCode) && rateAdjFreq == 0) {
			errorFlag = 1;
			checkMsg += ", 定期機動但無利率調整週期";
		}

		// 若利率變動且大於利率生效日
		if (adjCode <= 2 && rateProp.compareTo(presentRate) != 0 && prevIntDate > effDateCurt) {
			errorFlag = 1;
			checkMsg += ", 上次繳息日大於利率生效日, 入帳日:" + entryDate;
		}

		// 有錯誤轉為人工處理
		if (adjCode <= 2 && errorFlag == 1) {
			if (adjCode == 1) {
				adjCode = 3; // 有錯誤轉為人工處理
			} else {
				adjCode = 4; // 有錯誤轉為人工處理
			}
		}

		tmpBorm facm = new tmpBorm(custNo, facmNo, 0);
		if (facRateFlag.get(facm) == 1) {
			warnMsg += ", 額度下撥款的目前利率不同"; // warning only
		}
		// 預調週期
		b.setTxRateAdjFreq(iNextAdjPeriod);

		b.setAdjCode(adjCode);

		// 檢核訊息
		// 去起頭的逗號
		if (checkMsg.length() > 2 && checkMsg.startsWith(", ")) {
			String str = checkMsg.substring(2);
			checkMsg = str;
		}
		tTempVo.putParam("CheckMsg", checkMsg);
		// 緊告訊息
		if (warnMsg.length() > 2 && warnMsg.startsWith(", ")) {
			String str = warnMsg.substring(2);
			warnMsg = str;
		}
		tTempVo.putParam("WarnMsg", warnMsg);

		b.setJsonFields(tTempVo.getJsonString());

		/* 設定調整後利率 */
		// 利率輸入記號 0.未調整 1.已調整 9.待處理(檢核有誤)
		if ((adjCode == 1 || adjCode == 2) && errorFlag == 0) {
			b.setAdjustedRate(rateProp); // 調整後利率
			b.setRateKeyInCode(1); // 利率輸入記號 0.未調整 1.已調整 9.待處理(檢核有誤)
		} else {
			if (errorFlag == 0) {
				b.setAdjustedRate(BigDecimal.ZERO);
				b.setRateKeyInCode(0);
			} else {
				b.setAdjustedRate(BigDecimal.ZERO);
				b.setRateKeyInCode(9);
			}
		}
		// 加碼值：自訂利率時為0、指標利率時為擬調利率(已調整時為調整後利率)減合約指標利率
		if ("99".equals(baseRateCode)) {
			b.setRateIncr(BigDecimal.ZERO);
		} else {
			b.setRateIncr(b.getProposalRate().subtract(b.getContrBaseRate()));
		}
	}

//	訂正(刪除)
	private void deleteBatxRate(TitaVo titaVo) throws LogicException {
		// 戶別 CustType 1:個金;2:企金（含企金自然人）=> 客戶檔 0:個金1:企金2:企金自然人
		this.info("deleteBatxRate...");
		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();
		Slice<BatxRateChange> sBatxRateChange = batxRateChangeService.findL4320Erase(titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();
		if (lBatxRateChange != null && lBatxRateChange.size() != 0) {
			for (BatxRateChange tBatxRateChange : lBatxRateChange) {
				batxRateChangeService.holdById(tBatxRateChange, titaVo);
				if (tBatxRateChange.getConfirmFlag() > 0) {
					throw new LogicException("E0008", ", 該筆已確認，請先訂正L4321 ");
				} else {
					try {
						batxRateChangeService.delete(tBatxRateChange, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", ", 刪除錯誤 " + e.getErrorMsg());
					}
				}
			}
			this.processCnt = lBatxRateChange.size();
		}
	}

	// 政府補貼利率差額
	private BigDecimal getSubsidyRateDiff(String govOfferFlag, int presEffDate, TitaVo titaVo) throws LogicException {
		if (!"N".equals(govOfferFlag)) {
			return BigDecimal.ZERO;
		}

		String jsFieldName = "SubsidyRate" + govOfferFlag;
		if (subsidyRateVo.get(jsFieldName) == null) {
			return BigDecimal.ZERO;
		}

		BigDecimal subsidyRateNow = parse.stringToBigDecimal(subsidyRateVo.get(jsFieldName));

		CdComm tCdComm = cdCommService.CdTypeDescFirst("01", "01", 0, presEffDate + 19110000, titaVo);
		if (tCdComm == null) {
			return BigDecimal.ZERO;
		}

		TempVo oldTempVo = new TempVo();
		oldTempVo.getVo(tCdComm.getJsonFields());
		if (oldTempVo.get(jsFieldName) == null) {
			return BigDecimal.ZERO;
		}

		BigDecimal subsidyRateOld = parse.stringToBigDecimal(oldTempVo.get(jsFieldName));

		return subsidyRateNow.subtract(subsidyRateOld);
	}

	// 暫時紀錄戶號額度
	private class tmpBorm {

		@Override
		public String toString() {
			return "tmpBorm [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
		}

		private int custNo = 0;
		private int facmNo = 0;
		private int bormNo = 0;

		public tmpBorm(int custNo, int facmNo, int bormNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setBormNo(bormNo);
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		public void setBormNo(int bormNo) {
			this.bormNo = bormNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + bormNo;
			result = prime * result + custNo;
			result = prime * result + facmNo;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpBorm other = (tmpBorm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (bormNo != other.bormNo)
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			return true;
		}

		private L4320Batch getEnclosingInstance() {
			return L4320Batch.this;
		}

	}

	private void setFacRate(List<Map<String, String>> fnAllList) throws LogicException {

		BigDecimal fitRate;
		if (fnAllList != null && fnAllList.size() != 0) {
			int i = 0;
			for (int j = 1; j <= fnAllList.size(); j++) {
				i = j - 1;

				int custNo = parse.stringToInteger(fnAllList.get(i).get("CustNo"));
				int facmNo = parse.stringToInteger(fnAllList.get(i).get("FacmNo"));

				tmpBorm facm = new tmpBorm(custNo, facmNo, 0);
				fitRate = parse.stringToBigDecimal(fnAllList.get(i).get("FitRate"));

				if (!facRateFlag.containsKey(facm)) {
					facRateFlag.put(facm, 0);
				}

				if (facRate.containsKey(facm)) {
					if (facRate.get(facm).compareTo(fitRate) != 0) {
						facRateFlag.put(facm, 1);
					}
				} else {
					facRate.put(facm, fitRate);
					this.info(facm.toString() + " facRate=" + facRate.get(facm));
				}
			}
		}
	}

	// 指標利率異動
	private void doBaseRateChange(TitaVo titaVo) throws LogicException {
		this.info("doBaseRateChange ...");
		// 指標利率代碼 iBaseRateCode
		// 客戶別 iCustType 1:個金 2:企金
		// 指標利率生效日 iEffectDate

		// 檢核:本次指標利率與前一筆是否有差異
		List<Map<String, String>> checkResultList = l4320BatchServiceImpl.checkBaseRateChange(iBaseRateCode,
				iEffectDate, titaVo);

		if (checkResultList == null || checkResultList.isEmpty()) {
			this.info("checkResultList is null.");
			return;
		}
		Map<String, String> checkResult = checkResultList.get(0);
		BigDecimal baseRate = parse.stringToBigDecimal(checkResult.get("BaseRate"));
		BigDecimal oriBaseRate = parse.stringToBigDecimal(checkResult.get("OriBaseRate"));
		String baseRateChangeFlag = checkResult.get("BaseRateChangeFlag");

		if (baseRateChangeFlag == null || !baseRateChangeFlag.equals("Y")) {
			this.info("baseRateChangeFlag != Y");
			return;
		}

		// 取得受影響的客戶名單
		List<Map<String, String>> baseRateChangeCustList = l4320BatchServiceImpl.getBaseRateChangeCust(iBaseRateCode,
				iCustType, iEffectDate, titaVo);

		if (baseRateChangeCustList == null || baseRateChangeCustList.isEmpty()) {
			this.info("baseRateChangeCustList is null.");
			return;
		}

		// 寫入BatxBaseRateChange
		setBatxBaseRateChangeAll(baseRate, oriBaseRate, baseRateChangeCustList, titaVo);

		// 更新放款利率檔資料
		updateLoanRateChangeAll(baseRate, baseRateChangeCustList, titaVo);
	}

	private void setBatxBaseRateChangeAll(BigDecimal baseRate, BigDecimal oriBaseRate,
			List<Map<String, String>> baseRateChangeCustList, TitaVo titaVo) throws LogicException {

		int adjDate = titaVo.getEntDyI() < 19110000 ? titaVo.getEntDyI() + 19110000 : titaVo.getEntDyI();

		boolean isExist;
		BatxBaseRateChangeId batxBaseRateChangeId;
		BatxBaseRateChange batxBaseRateChange;
		BigDecimal rateIncr;
		BigDecimal individualIncr;
		BigDecimal fitRate;
		BigDecimal newFitRate;
		TempVo tTempVo;

		for (Map<String, String> baseRateChangeCust : baseRateChangeCustList) {
			int custEffectDate = parse.stringToInteger(baseRateChangeCust.get("EffectDate"));

			batxBaseRateChangeId = new BatxBaseRateChangeId();
			batxBaseRateChangeId.setAdjDate(adjDate);
			batxBaseRateChangeId.setCustNo(parse.stringToInteger(baseRateChangeCust.get("CustNo")));
			batxBaseRateChangeId.setFacmNo(parse.stringToInteger(baseRateChangeCust.get("FacmNo")));
			batxBaseRateChangeId.setBormNo(parse.stringToInteger(baseRateChangeCust.get("BormNo")));

			batxBaseRateChange = sBatxBaseRateChangeService.holdById(batxBaseRateChangeId, titaVo);

			if (batxBaseRateChange == null) {
				isExist = false;
				batxBaseRateChange = new BatxBaseRateChange();
				batxBaseRateChange.setBatxBaseRateChangeId(batxBaseRateChangeId);
				batxBaseRateChange.setAdjDate(adjDate);
				batxBaseRateChange.setCustNo(parse.stringToInteger(baseRateChangeCust.get("CustNo")));
				batxBaseRateChange.setFacmNo(parse.stringToInteger(baseRateChangeCust.get("FacmNo")));
				batxBaseRateChange.setBormNo(parse.stringToInteger(baseRateChangeCust.get("BormNo")));
				batxBaseRateChange.setTitaTlrNo(titaVo.getTlrNo());
				batxBaseRateChange.setTitaTxtNo(titaVo.getTxtNo());
			} else {
				isExist = true;
				// 若已存在,且指標利率相同,跳過這筆
				if (batxBaseRateChange.getBaseRate().compareTo(baseRate) == 0) {
					continue;
				}
			}
			batxBaseRateChange.setProdNo(baseRateChangeCust.get("ProdNo"));
			batxBaseRateChange.setBaseRateCode(iBaseRateCode);
			batxBaseRateChange.setOriBaseRate(oriBaseRate);
			batxBaseRateChange.setBaseRateEffectDate(iEffectDate <= 19110000 ? iEffectDate + 19110000 : iEffectDate);
			batxBaseRateChange.setBaseRate(baseRate);

			// 原加碼利率
			rateIncr = parse.stringToBigDecimal(baseRateChangeCust.get("RateIncr"));
			// 原個別加碼利率
			individualIncr = parse.stringToBigDecimal(baseRateChangeCust.get("IndividualIncr"));
			// 原利率
			fitRate = parse.stringToBigDecimal(baseRateChangeCust.get("FitRate"));

			// 運算新的適用利率
			if ("Y".equals(baseRateChangeCust.get("IncrFlag"))) { // 加減碼是否依合約
				newFitRate = iBaseRate.add(rateIncr);
			} else {
				newFitRate = iBaseRate.add(individualIncr);
			}
			batxBaseRateChange.setFitRate(newFitRate);

			if (fitRate.compareTo(newFitRate) == 0) {
				// 若利率未變動，放款利率變動檔生效日擺0
				batxBaseRateChange.setTxEffectDate(0);
			} else {
				batxBaseRateChange.setTxEffectDate(custEffectDate);
			}

			tTempVo = new TempVo();
			tTempVo.putParam("RateIncr", rateIncr);
			tTempVo.putParam("IndividualIncr", individualIncr);
			tTempVo.putParam("FitRate", fitRate);
			tTempVo.putParam("EffectDate", custEffectDate);
			batxBaseRateChange.setJsonFields(tTempVo.getJsonString());

			if (isExist) {
				// update
				try {
					sBatxBaseRateChangeService.update(batxBaseRateChange, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "BatxBaseRateChange update error : " + e.getErrorMsg());
				}
			} else {
				// insert
				try {
					sBatxBaseRateChangeService.insert(batxBaseRateChange, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", ", BatxBaseRateChange insert error : " + e.getErrorMsg());
				}
			}
		}

	}

	private void updateLoanRateChangeAll(BigDecimal iBaseRate, List<Map<String, String>> baseRateChangeCustList,
			TitaVo titaVo) throws LogicException {
		int tmpCustNo;
		int tmpFacmNo;
		int tmpBormNo;
		int custEffectDate;
		BigDecimal newFitRate;

		for (Map<String, String> baseRateChangeCust : baseRateChangeCustList) {
			tmpCustNo = parse.stringToInteger(baseRateChangeCust.get("CustNo"));
			tmpFacmNo = parse.stringToInteger(baseRateChangeCust.get("FacmNo"));
			tmpBormNo = parse.stringToInteger(baseRateChangeCust.get("BormNo"));
			custEffectDate = parse.stringToInteger(baseRateChangeCust.get("EffectDate"));
			// 讀取生效日之後的利率變動檔
			Slice<LoanRateChange> sLoanRateChange = sLoanRateChangeService.rateChangeBormNoEq(tmpCustNo, tmpFacmNo,
					tmpBormNo, custEffectDate, 0, Integer.MAX_VALUE);
			List<LoanRateChange> lLoanRateChange = sLoanRateChange == null ? null : sLoanRateChange.getContent();

			if (lLoanRateChange != null && lLoanRateChange.size() != 0) {
				for (LoanRateChange t : lLoanRateChange) {
					// 指標利率相同
					if (t.getBaseRateCode().equals(iBaseRateCode) && !"2".equals(t.getRateCode())) {
						LoanRateChange tLoanRateChange = sLoanRateChangeService.holdById(t.getLoanRateChangeId(),
								titaVo);
						if ("Y".equals(tLoanRateChange.getIncrFlag())) { // 加減碼是否依合約
							newFitRate = iBaseRate.add(tLoanRateChange.getRateIncr());
						} else {
							newFitRate = iBaseRate.add(tLoanRateChange.getIndividualIncr());
						}
						if (t.getFitRate().compareTo(newFitRate) == 0) {
							this.info("此筆利率異動後相同,不更新.");
							continue;
						}
						tLoanRateChange.setFitRate(newFitRate);
						try {
							sLoanRateChangeService.update(tLoanRateChange, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "LoanRateChange update error : " + e.getErrorMsg());
						}
					}
				}
			}

		}
	}
}