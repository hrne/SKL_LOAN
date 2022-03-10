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
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
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
	public L4320ServiceImpl l4320BatchServiceImpl;

	private HashMap<tmpBorm, BigDecimal> loanBalTot = new HashMap<>();
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
	private int custNo;
	private int facmNo;
	private int bormNo;
	private Boolean flag = true;

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

		iCustType = parse.stringToInteger(titaVo.getParam("EntCode"));
		// 調整日期
		wkAdjDate = this.getTxBuffer().getTxCom().getTbsdy();

		// 本月月初日
		wkLastMonthDateS = this.getTxBuffer().getTxCom().getTmndy() / 100;
		wkLastMonthDateS = wkLastMonthDateS * 100 + 01;
		this.info("iEffectMonth = " + iEffectMonth + ", iBaseRateCode=" + iBaseRateCode);

//		正常
		if (titaVo.isHcodeNormal()) {
			try {
				execute(titaVo);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
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

		if (flag) {
			// Broadcast message Link to L4031-利率調整清單
			setSendMsg(titaVo);

			if (this.processCnt > 0) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4031",
						titaVo.getTlrNo(), sendMsg + "，筆數：" + this.processCnt, titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4320",
						titaVo.getTlrNo(), sendMsg + "，筆數：" + this.processCnt, titaVo);
			}
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4320", titaVo.getTlrNo(),
					sendMsg, titaVo);
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

	private void execute(TitaVo titaVo) throws LogicException {

//      抓取資料
		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l4320BatchServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			throw new LogicException("E0015", ", " + e.getMessage());
		}

//		合計帳戶餘額
		getTotalLoanBal(fnAllList);

//		額度下撥款的目前利率是否相同
		setFacRate(fnAllList);

//		運算全戶餘額及其他輸出欄位
		if (fnAllList != null && fnAllList.size() != 0) {
			for (Map<String, String> s : fnAllList) {
				this.info("fnAllList=" + s);
				/* 判斷新增或更新整批利率調整檔，已確認跳過 */
				checkMsg = "";
				warnMsg = "";
				// F0 戶號
				// F1 額度
				// F2 撥款
				custNo = parse.stringToInteger(s.get("F0"));
				facmNo = parse.stringToInteger(s.get("F1"));
				bormNo = parse.stringToInteger(s.get("F2"));
				BatxRateChange b = new BatxRateChange();
				BatxRateChangeId bId = new BatxRateChangeId();
				// 調整日期
				bId.setAdjDate(wkAdjDate);
				bId.setCustNo(custNo);
				bId.setFacmNo(facmNo);
				bId.setBormNo(bormNo);
				b = batxRateChangeService.holdById(bId, titaVo);
				// 已輸入利率跳過
				boolean isInsert = false;
				if (b == null) {
					isInsert = true;
					b = new BatxRateChange();
					b.setBatxRateChangeId(bId);
				} else {
					if (b.getRateKeyInCode() == 1) {
						continue;
					}
				}
				/* 設定各項值 */
				seBatxRateChange(b, s, titaVo);

				// commit per commitCnt
				processCnt++;
				if (processCnt % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				// 全戶餘額
				tmpBorm cust = new tmpBorm(custNo, 0, 0);
				b.setTotBalance(loanBalTot.get(cust));

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
						batxRateChangeService.update(b, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "BatxRateChange update error : " + e.getErrorMsg());
					}
				}
			}
		}
	}

	/* 設定各項值 */
	private void seBatxRateChange(BatxRateChange b, Map<String, String> s, TitaVo titaVo) throws LogicException {

		// 作業項目
		b.setTxKind(iTxKind);

		// F3 下次利率調整日期
		int nextAdjRateDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("F3")));
		b.setPreNextAdjDate(nextAdjRateDate); // 下次利率調整日期
		// F4 上次繳息日,繳息迄日
		// F27 撥款日期
		int prevIntDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("F4")));
		int drawdownDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("F27")));
		if (prevIntDate == 0) {
			b.setPrevIntDate(drawdownDate);
		} else {
			b.setPrevIntDate(prevIntDate);
		}
		// F5 利率調整週期
		int rateAdjFreq = parse.stringToInteger(s.get("F5"));
		b.setPreNextAdjFreq(rateAdjFreq);
		// F6 餘額
		// F7 撥款金額
		BigDecimal loanBal = parse.stringToBigDecimal(s.get("F6"));
		BigDecimal drawdownAmt = parse.stringToBigDecimal(s.get("F7"));
		b.setLoanBalance(loanBal);
		b.setDrawdownAmt(drawdownAmt);
		// 全戶餘額
		tmpBorm cust = new tmpBorm(custNo, 0, 0);
		b.setTotBalance(loanBalTot.get(cust));
		// F11 員工利率記號
		// F12 借戶利率檔是否依合約記號
		String incrFlag = s.get("F12");
		b.setIncrFlag(incrFlag);
		// F13 借戶利率檔指標利率代碼
		String baseRateCode = s.get("F13");
		b.setBaseRateCode(baseRateCode);
		// F15 借戶利率檔利率區分 共用代碼檔 1: 機動 2: 固動 3: 定期機動
		String rateCode = s.get("F15");
		b.setRateCode(rateCode);
		// F16 借戶利率檔商品代碼
		String prodNo = s.get("F16");
		b.setProdNo(prodNo);
		// F21 擔保品地區別
		String cityCode = s.get("F21");
		b.setCityCode(cityCode);
		// F20 擔保品地區別
		int cntCode = parse.stringToInteger(s.get("F20"));
		b.setCustCode(cntCode);
		// F22 擔保品鄉鎮別
		b.setAreaCode(s.get("F22"));
		// F23 地區別利率上限
		BigDecimal cityIntRateCeiling = parse.stringToBigDecimal(s.get("F23"));
		// F24 地區別利率下限
		BigDecimal cityIntRateFloor = parse.stringToBigDecimal(s.get("F24"));
		// F25 地區別利率加減碼
		BigDecimal cityIntRateIncr = parse.stringToBigDecimal(s.get("F25"));

		// F8 額度核准利率
//		BigDecimal approveRateFac = parse.stringToBigDecimal(s.get("F8"));
		// F9 額度加碼利率
//		BigDecimal rateIncrFac = parse.stringToBigDecimal(s.get("F9"));
		// F10 額度個人加碼利率(未用)

		// F17 借戶利率檔加碼利率
		BigDecimal rateIncr = parse.stringToBigDecimal(s.get("F17"));
		b.setRateIncr(rateIncr);
		b.setContrRateIncr(rateIncr);

		// F18 借戶利率檔個人加碼利率
		BigDecimal individualIncr = parse.stringToBigDecimal(s.get("F18"));
		b.setIndividualIncr(individualIncr);

		// F14 借戶利率檔適用利率
		BigDecimal fitRate = parse.stringToBigDecimal(s.get("F14"));

		// F19 借戶利率檔生效日
		int effectDatePresent = StaticTool.bcToRoc(parse.stringToInteger(s.get("F19")));

		// 本次生效日
		int effDateCurt = 0;

		// 機動非指數=>抓出的為預調利率(本次生效日)，需抓生效月份前的資料為目前利率與目前生效日
		// F28 // 機動非指數目前利率
		// F29 // 機動非指數目前生效日
		if (iTxKind == 3) {
			effDateCurt = effectDatePresent;
			if (parse.stringToInteger(s.get("F29")) > 0) {
				fitRate = parse.stringToBigDecimal(s.get("F28"));
				effectDatePresent = StaticTool.bcToRoc(parse.stringToInteger(s.get("F29")));
				effDateCurt = StaticTool.bcToRoc(parse.stringToInteger(s.get("F19")));
			}
		}

		// 目前利率 = 借戶利率檔適用利率
		b.setPresentRate(fitRate);

		// 目前生效日 = 借戶利率檔生效日
		b.setPresEffDate(effectDatePresent);

		// 合約利率=生效之指標利率+目前之合約加減碼； 自訂利率 =>0
		BigDecimal contrBaseRate = BigDecimal.ZERO;
		BigDecimal contractRate = BigDecimal.ZERO;
		if (!"99".equals(baseRateCode)) {
			contrBaseRate = iBaseRate;
			contractRate = contrBaseRate.add(rateIncr);
		}
		b.setContrBaseRate(contrBaseRate);
		b.setContractRate(contractRate);

		// F26 下次繳息日,下次應繳日
		int nextPayIntDate = StaticTool.bcToRoc(parse.stringToInteger(s.get("F26")));
		// 本次指標利率
		b.setCurrBaseRate(iBaseRate);
		// 調整記號 1.批次自動調整 2.按地區別自動調整 3.人工調整(未調整) 9.上次繳息日大於利率生效日
		int adjCode = 0;
		// 本次利率
		BigDecimal rateCurt = BigDecimal.ZERO;

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
//		                  處理: 1.逾一期              => 3.人工調整
//		                2.利率按合約 Y  => 1.批次自動調整
//		                3.利率按合約 N  => 2.按地區別自動調整  (有地區別)
//		                               => 3.人工調整(無地區別)		     
		case 1:
			// 本次生效日 = 主檔下次利率調整日
			effDateCurt = nextAdjRateDate;
			// 利率按合約 Y ， 本次利率 = 本次指標利率 + 目前合約加減碼
			// 利率按合約 N && 有地區別， 本次利率 = 目前利率 + 地區別加減碼，依地區別利率上、下限調整，不可超過合約加碼利率
			// 利率按合約 N && 無地區別， 本次利率 = 本次指標利率 +借戶利率檔個人加碼利率
			if ("Y".equals(incrFlag)) {
				adjCode = 1;
				rateCurt = iBaseRate.add(rateIncr);
			} else if ("".equals(cityCode.trim())) {
				adjCode = 3;
				rateCurt = iBaseRate.add(individualIncr);
			} else {
				adjCode = 2;
				// 本次利率 = 目前利率 + 地區別加減碼
				rateCurt = fitRate.add(cityIntRateIncr);
				warnMsg += "地區別加減碼:" + cityIntRateIncr ;			
				// 依地區別利率上、下限調整
				if (rateCurt.compareTo(cityIntRateCeiling) > 0) {
					rateCurt = cityIntRateCeiling;
					warnMsg += "達地區別上限 ";
				}
				if (rateCurt.compareTo(cityIntRateFloor) < 0) {
					rateCurt = cityIntRateFloor;
					warnMsg += "達地區別下限 ";
				}
			}
			
			if (rateIncr.compareTo(BigDecimal.ZERO) > 0 && contractRate.compareTo(rateCurt) < 0) {
				rateCurt = contractRate;
				warnMsg += "達合約利率上限 ";
			}

			// 逾一期 => 下次繳息日,下次應繳日 < 上次月初日
			if (adjCode > 1 && b.getOvduTerm() >= 1) {
				adjCode = 3;
				warnMsg += "逾 " + dateUtil.getMons() + " 期 ";
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
			// 本次利率 = 本次指標利率 + 目前合約加減碼
			rateCurt = iBaseRate.add(rateIncr);
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
			// 本次利率 = 原利率 + 地區別利率
			rateCurt = fitRate.add(cityIntRateIncr);
			// 依地區別利率上、下限調整
			if (rateCurt.compareTo(cityIntRateCeiling) > 0) {
				rateCurt = cityIntRateCeiling;
			}
			if (rateCurt.compareTo(cityIntRateFloor) < 0) {
				rateCurt = cityIntRateFloor;
			}
			// 3.人工調整
			adjCode = 3;
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
				rateCurt = iRate;
			} else {
				rateCurt = fitRate.add(iRateIncr);
			}
			// 1.自動調整
			adjCode = 1;
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
				rateCurt = iRate;
			} else {
				rateCurt = fitRate.add(iRateIncr);
			}
			// 3.人工調整
			adjCode = 3;
			break;
		default:
			break;
		}
		// 本次生效日
		b.setCurtEffDate(effDateCurt);
		// 擬調利率
		b.setProposalRate(rateCurt);

		/* 檢核有誤 */
		int errorFlag = 0;

		if ("3".equals(rateCode) && rateAdjFreq == 0) {
			errorFlag = 1;
			checkMsg += "定期機動但無利率調整週期";
		}

		// 若利率變動且大於利率生效日
		if (adjCode == 1) {
			if (rateCurt.compareTo(fitRate) != 0 && prevIntDate > effDateCurt) {
				errorFlag = 1;
				checkMsg += "上次繳息日大於利率生效日";
			}
		}

		tmpBorm facm = new tmpBorm(custNo, facmNo, 0);
		if (facRateFlag.get(facm) == 1) {
			warnMsg += "額度下撥款的目前利率不同"; // warning only
		}
		// 預調週期
		b.setTxRateAdjFreq(iNextAdjPeriod);

//		調整記號
		b.setAdjCode(adjCode);

		/* 設定 jsonFields 欄 */
		TempVo tTempVo = new TempVo();
		// 檢核訊息
		tTempVo.putParam("CheckMsg", checkMsg);
		// 緊告訊息
		tTempVo.putParam("warnMsg", warnMsg);

		// 目前利率 <> 借戶利率檔適用利率(預調利率)
		if (fitRate.compareTo(parse.stringToBigDecimal(s.get("F14"))) != 0) {
			tTempVo.putParam("FitRate", s.get("F14"));
		}

		b.setJsonFields(tTempVo.getJsonString());

		/* 設定調整後利率 */
		// 利率輸入記號 0.未調整 1.已調整 9.待處理(檢核有誤)
		if ((adjCode == 1) && errorFlag == 0) {
			b.setAdjustedRate(rateCurt); // 調整後利率
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
				if (tBatxRateChange.getConfirmFlag() == 1) {
					throw new LogicException("E0008", ", 該筆已確認，請先訂正L4321 ");
				} else {
					try {
						batxRateChangeService.delete(tBatxRateChange, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0008", ", 刪除錯誤 " + e.getErrorMsg());
					}
				}
			}
			processCnt = lBatxRateChange.size();
		}
	}

//	暫時紀錄戶號額度
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

	private void getTotalLoanBal(List<Map<String, String>> fnAllList) throws LogicException {
		if (fnAllList != null && fnAllList.size() != 0) {
			int i = 0;
			for (int j = 1; j <= fnAllList.size(); j++) {
				i = j - 1;

				int custNo = parse.stringToInteger(fnAllList.get(i).get("F0"));

				tmpBorm cust = new tmpBorm(custNo, 0, 0);

				if (loanBalTot.containsKey(cust)) {
					loanBalTot.put(cust,
							loanBalTot.get(cust).add(parse.stringToBigDecimal(fnAllList.get(i).get("F6"))));
				} else {
					loanBalTot.put(cust, parse.stringToBigDecimal(fnAllList.get(i).get("F6")));
				}
			}
		}
	}

	private void setFacRate(List<Map<String, String>> fnAllList) throws LogicException {

		BigDecimal fitRate;
		if (fnAllList != null && fnAllList.size() != 0) {
			int i = 0;
			for (int j = 1; j <= fnAllList.size(); j++) {
				i = j - 1;

				int custNo = parse.stringToInteger(fnAllList.get(i).get("F0"));
				int facmNo = parse.stringToInteger(fnAllList.get(i).get("F1"));

				tmpBorm facm = new tmpBorm(custNo, facmNo, 0);

				if (iTxKind == 3) {
					fitRate = parse.stringToBigDecimal(fnAllList.get(i).get("F29"));
				} else {
					fitRate = parse.stringToBigDecimal(fnAllList.get(i).get("F14"));
				}

				if (!facRateFlag.containsKey(facm)) {
					facRateFlag.put(facm, 0);
				}

				if (facRate.containsKey(facm)) {
					if (facRate.get(facm).compareTo(fitRate) != 0) {
						facRateFlag.put(facm, 1);
					}
				} else {
					facRate.put(facm, fitRate);
				}
			}
		}
	}

}