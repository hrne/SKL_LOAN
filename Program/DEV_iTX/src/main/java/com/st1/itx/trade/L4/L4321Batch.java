package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.trade.L4.L4321Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4321Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4321Batch extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L4321Report l4321Report;

	@Autowired
	public CdBaseRateService cdBaseRateService;

//	寄送筆數
	private int commitCnt = 200;

	private int iAdjDate = 0;
	private int iTxKind = 0;
	private int iCustType = 0;
	private int iConfirmFlag = 0;
	private int processCnt = 0;
	private TempVo tTempVo = new TempVo();
	private String sendMsg = "";
	private int custType1 = 0;
	private int custType2 = 0;
	private int wkConfirmFlag = 0;
	private Boolean flag = true;
	private BigDecimal rateIncr = BigDecimal.ZERO;
	private BigDecimal individualIncr = BigDecimal.ZERO;

//	輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//	客戶檔 0:個金1:企金2:企金自然人

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4321Batch ");
		this.totaVo.init(titaVo);
		this.iAdjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;

		try {
			execute(titaVo);
		} catch (LogicException e) {
			sendMsg = e.getErrorMsg();
			flag = false;
		}

		// 送出通知訊息
		sendMessage(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execute(TitaVo titaVo) throws LogicException {
		// 設定確認記號、資料確認記號( 確認記號 0.未確認 1.確認未放行 2.已確認放行)
		// 經辦登錄

		this.info("isActfgEntry ... " + titaVo.isActfgEntry());
		this.info("isHcodeNormal ... " + titaVo.isHcodeNormal());

		if (titaVo.isActfgEntry()) {
			if (titaVo.isHcodeNormal()) {
				this.iConfirmFlag = 1;
				this.wkConfirmFlag = 0;

			} else {
				this.iConfirmFlag = 0;
				this.wkConfirmFlag = 1;
			}
		} else
		// 主管放行
		{
			if (titaVo.isHcodeNormal()) {
				this.iConfirmFlag = 2;
				this.wkConfirmFlag = 1;
			} else {
				this.iConfirmFlag = 1;
				this.wkConfirmFlag = 2;
			}
		}

		this.info("iConfirmFlag ... " + iConfirmFlag);
		this.info("wkConfirmFlag ... " + wkConfirmFlag);

		// 戶別 CustType 1:個金;2:企金（含企金自然人）=> 客戶檔 0:個金1:企金2:企金自然人
		if (this.iCustType == 2) {
			this.custType1 = 1;
			this.custType2 = 2;
		}

		// 處理更新
		processUpdate(titaVo);

		// 產出確認清單
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			l4321Report.exec(titaVo);
			this.batchTransaction.commit();
		}
	}

	private void sendMessage(TitaVo titaVo) throws LogicException {
		if (flag) {
			// 設定訊息
			if (iTxKind <= 3) {
				if (this.iCustType == 1) {
					sendMsg = "個金，" + sendMsg;
				} else {
					sendMsg = "企金，" + sendMsg;
				}
			}

			switch (this.iTxKind) {
			case 1:
				sendMsg = sendMsg + "，定期機動利率變動資料";
				break;
			case 2:
				sendMsg = sendMsg + "，指數型利率變動資料";
				break;
			case 3:
				sendMsg = sendMsg + "，機動利率變動資料";
				break;
			case 4:
				sendMsg = sendMsg + "，員工利率變動資料";
				break;
			case 5:
				sendMsg = sendMsg + "，按商品別利率變動資料";
				break;
			default:
				break;
			}

			if (titaVo.isHcodeNormal()) {
				sendMsg = sendMsg + "，完成確認，筆數：" + this.processCnt;
			} else {
				sendMsg = sendMsg + "，取消確認，筆數：" + this.processCnt;
			}

			if (this.processCnt > 0 && titaVo.isHcodeNormal()) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
						titaVo.getEmpNot() + "L4321", sendMsg, titaVo);
//				提醒原櫃員執行列印對帳單交易
//				主管放行
				if (!titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
					this.info("OrgTlr ..." + titaVo.getOrgTlr());
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getOrgTlr(), "Y", "L4721", "",
							sendMsg + "，主管已完成確認，需列印利率變動對帳單", titaVo);
				}
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", sendMsg,
						titaVo);
//				提醒原櫃員執行列印對帳單交易
//				主管放行
				if (!titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
					this.info("OrgTlr ..." + titaVo.getOrgTlr());
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getOrgTlr(), "Y", "L4721", "",
							sendMsg + "，主管已完成確認，需列印利率變動對帳單", titaVo);
				}
			}
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", "", sendMsg, titaVo);
		}
	}

	private void processUpdate(TitaVo titaVo) throws LogicException {
		this.info("processUpdate...");
		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();
		Slice<BatxRateChange> sBatxRateChange = batxRateChangeService.findL4321Report(this.iAdjDate, this.iAdjDate,
				custType1, custType2, iTxKind, this.wkConfirmFlag, this.index, this.limit, titaVo);
		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();

		if (lBatxRateChange != null && lBatxRateChange.size() != 0) {
			for (BatxRateChange tBatxRateChange : lBatxRateChange) {
				// 未輸入利率=>不處理
				// 是否輸入利率 0.未調整 1.已調整 2.待輸入 9.待處理(檢核有誤)
				if (tBatxRateChange.getRateKeyInCode() != 1) {
					this.info(tBatxRateChange.getCustNo() + "  continue...");
					continue;
				}

				// commit per commitCnt
				this.processCnt++;
				if (this.processCnt % commitCnt == 0) {
					this.batchTransaction.commit();
				}
				BigDecimal fitRate = tBatxRateChange.getPresentRate();
				if (tTempVo.get("FitRate") != null) {
					fitRate = parse.stringToBigDecimal(tTempVo.get("FitRate"));
				}
				// 放款利率變動檔生效日，利率未變動為零
				int txEffectDate = 0;
				if (tBatxRateChange.getAdjustedRate().compareTo(fitRate) != 0) {
					txEffectDate = tBatxRateChange.getCurtEffDate();
				}
				// 經辦更新
				if (titaVo.isActfgEntry()) {
					// get tempVo
					TempVo tTempVo = new TempVo();
					tTempVo = tTempVo.getVo(tBatxRateChange.getJsonFields());

					// 更新撥款主檔
					updateBorm(tBatxRateChange, titaVo);

					// 處理本次生效日
					if (txEffectDate > 0) {
						setLoanRateChange(tBatxRateChange, titaVo);
					}
					// 處理預調利率
					setNextAdjRateChange(tBatxRateChange, titaVo);

					// 處理階梯式利率
					setStepRateChange(tBatxRateChange, titaVo);
				}
				// 主管放行
				if (titaVo.isActfgSuprele()) {
					CdBaseRate tCdBaseRate = new CdBaseRate();
					CdBaseRateId tCdBaseRateId = new CdBaseRateId();

					tCdBaseRateId.setBaseRateCode(tBatxRateChange.getBaseRateCode());
					tCdBaseRateId.setCurrencyCode("TWD");
					tCdBaseRateId.setEffectDate(tBatxRateChange.getCurtEffDate());

					tCdBaseRate = cdBaseRateService.holdById(tCdBaseRateId, titaVo);
					if (tCdBaseRate != null) {
//						0:已放行  1:已生效不可刪除 2:未放行
//						一般
						if (titaVo.isHcodeNormal()) {
							tCdBaseRate.setEffectFlag(1);
						}
//						訂正
						else {
							tCdBaseRate.setEffectFlag(0);
						}
					} else {
						this.info("查無此指標利率檔...");
					}
				}

				// 更新確認記號、放款利率變動檔生效日
				if (tBatxRateChange.getConfirmFlag() != this.iConfirmFlag) {
					tBatxRateChange = batxRateChangeService.holdById(tBatxRateChange, titaVo);
					if (this.iConfirmFlag == 0) {
						tBatxRateChange.setTxEffectDate(0);
					} else {
						tBatxRateChange.setTxEffectDate(txEffectDate);						
					}
					tBatxRateChange.setConfirmFlag(this.iConfirmFlag);
					try {
						batxRateChangeService.update(tBatxRateChange, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "BatxRateChange update is error : " + e.getErrorMsg());
					}
				}
			}
		}

		this.info("processUpdate End...");
	}

	// 更新撥款主檔
	private void updateBorm(BatxRateChange tBatxRateChange, TitaVo titaVo) throws LogicException {
		this.info("updateBorm ...");

		// 定期機動 => 更新下次利率調整日期

		if (!"3".equals(tBatxRateChange.getRateCode())) {
			return;
		}

		int nextAdjDate = 0; // 下次利率調整日期
		LoanBorMain tLoanBorMain = new LoanBorMain();
		LoanBorMainId tLoanBorMainId = new LoanBorMainId();
		tLoanBorMainId.setCustNo(tBatxRateChange.getCustNo());
		tLoanBorMainId.setFacmNo(tBatxRateChange.getFacmNo());
		tLoanBorMainId.setBormNo(tBatxRateChange.getBormNo());
		tLoanBorMain = loanBorMainService.holdById(tLoanBorMainId);
		if (tLoanBorMain == null) {
			throw new LogicException("E0006", "BS430 LoanBorMain " + tLoanBorMainId);
		}
		if (titaVo.isHcodeNormal()) {
			dateUtil.init();
			dateUtil.setDate_1(tBatxRateChange.getCurtEffDate());
			dateUtil.setMons(tLoanBorMain.getRateAdjFreq()); // 調整周期(單位固定為月)
			nextAdjDate = dateUtil.getCalenderDay();
		} else {
			nextAdjDate = tBatxRateChange.getPreNextAdjDate();
		}
		tLoanBorMain.setNextAdjRateDate(nextAdjDate);

		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "BS430 LoanBorMain update " + e.getErrorMsg());
		}
	}

	// 處理本次生效日
	private void setLoanRateChange(BatxRateChange tBatxRateChange, TitaVo titaVo) throws LogicException {
		this.info("setLoanRateChange ...");
		// 更新記號 0:新增 1:更新 2.刪除
		int updateFg = 0;
		rateIncr = BigDecimal.ZERO;
		individualIncr = BigDecimal.ZERO;
		if (tBatxRateChange.getIncrFlag().equals("Y")) {
			rateIncr = tBatxRateChange.getAdjustedRate().subtract(tBatxRateChange.getCurrBaseRate());
		} else {
			rateIncr = tBatxRateChange.getRateIncr();
			individualIncr = tBatxRateChange.getAdjustedRate().subtract(tBatxRateChange.getCurrBaseRate());
		}
		LoanRateChange tLoanRateChange = new LoanRateChange();
		LoanRateChangeId tLoanRateChangeId = new LoanRateChangeId();

		tLoanRateChangeId.setCustNo(tBatxRateChange.getCustNo());
		tLoanRateChangeId.setFacmNo(tBatxRateChange.getFacmNo());
		tLoanRateChangeId.setBormNo(tBatxRateChange.getBormNo());
		tLoanRateChangeId.setEffectDate(tBatxRateChange.getCurtEffDate());

		tLoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId, titaVo);
		if (tLoanRateChange == null) {
			// 取消確認時，需利率變動檔需存在
			if (this.iConfirmFlag == 0) {
				throw new LogicException("E0006", "BS430 loanRateChang hold " + tLoanRateChangeId);
			}
			updateFg = 0;
			tLoanRateChange = new LoanRateChange();
		} else {
			// 取消確認時，整批更新需刪除
			if (this.iConfirmFlag == 0 && tLoanRateChange.getStatus() == 1) {
				updateFg = 2;
			} else {
				updateFg = 1;
			}
		}
		// 新增
		if (updateFg == 0) {
			tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
			tLoanRateChange.setStatus(1); // 整批更新
			tLoanRateChange.setRateCode(tBatxRateChange.getRateCode());
			tLoanRateChange.setProdNo(tBatxRateChange.getProdNo());
			tLoanRateChange.setBaseRateCode(tBatxRateChange.getBaseRateCode());
			tLoanRateChange.setIncrFlag(tBatxRateChange.getIncrFlag());
			tLoanRateChange.setRateIncr(rateIncr);
			tLoanRateChange.setIndividualIncr(individualIncr);
			tLoanRateChange.setFitRate(tBatxRateChange.getAdjustedRate());
			tLoanRateChange.setRemark("");
			tLoanRateChange.setAcDate(this.getTxBuffer().getTxCom().getTbsdy());
			tLoanRateChange.setTellerNo(this.getTxBuffer().getTxCom().getRelTlr());
			tLoanRateChange.setTxtNo(parse.IntegerToString(this.getTxBuffer().getTxCom().getRelTno(), 8));
			try {
				loanRateChangeService.insert(tLoanRateChange, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "BS430 loanRateChangeService insert " + e.getErrorMsg());
			}
		}
		// 更新
		if (updateFg == 1) {
			if (this.iConfirmFlag == 1) {
				this.tTempVo.putParam("RateIncr", tLoanRateChange.getRateIncr());
				this.tTempVo.putParam("IndividualIncr", tLoanRateChange.getIndividualIncr());
				this.tTempVo.putParam("FitRate", tLoanRateChange.getFitRate());
				tLoanRateChange.setRateIncr(rateIncr);
				tLoanRateChange.setIndividualIncr(individualIncr);
				tLoanRateChange.setFitRate(tBatxRateChange.getAdjustedRate());
				tBatxRateChange.setJsonFields(tTempVo.getJsonString());
				tLoanRateChange.setRemark("");
				tLoanRateChange.setStatus(0);
			} else {
				this.info("JsonFields ... " + tBatxRateChange.getJsonFields());

				tTempVo = tTempVo.getVo(tBatxRateChange.getJsonFields());
				tLoanRateChange.setRateIncr(parse.stringToBigDecimal(this.tTempVo.getParam("RateIncr")));
				tLoanRateChange.setIndividualIncr(parse.stringToBigDecimal(this.tTempVo.getParam("IndividualIncr")));
				tLoanRateChange.setFitRate(parse.stringToBigDecimal(this.tTempVo.getParam("FitRate")));
			}
			try {
				loanRateChangeService.update(tLoanRateChange, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "BS430 loanRateChangeService update " + e.getErrorMsg());
			}
		}
		// 刪除
		if (updateFg == 2) {
			try {
				loanRateChangeService.delete(tLoanRateChange, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "BS430 loanRateChangeService delete " + e.getErrorMsg());
			}
		}

	}

	// 處理預調利率
	private void setNextAdjRateChange(BatxRateChange tBatxRateChange, TitaVo titaVo) throws LogicException {
		this.info("setNextAdjRateChange ...");
		// 預調週期為零不處理
		if (tBatxRateChange.getTxRateAdjFreq() == 0) {
			this.info("return ...");
			return;
		}
		// 預調利率生效日
		dateUtil.init();
		dateUtil.setDate_1(tBatxRateChange.getCurtEffDate());
		dateUtil.setMons(tBatxRateChange.getTxRateAdjFreq()); // 調整周期(單位固定為月)
		int txRateAdjDate = dateUtil.getCalenderDay();
		LoanRateChange tLoanRateChange = new LoanRateChange();
		LoanRateChangeId tLoanRateChangeId = new LoanRateChangeId();

		tLoanRateChangeId.setCustNo(tBatxRateChange.getCustNo());
		tLoanRateChangeId.setFacmNo(tBatxRateChange.getFacmNo());
		tLoanRateChangeId.setBormNo(tBatxRateChange.getBormNo());
		tLoanRateChangeId.setEffectDate(txRateAdjDate);
		if (this.iConfirmFlag == 1) {
			tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
			tLoanRateChange.setStatus(1); // 整批更新
			tLoanRateChange.setRateCode(tBatxRateChange.getRateCode());
			tLoanRateChange.setProdNo(tBatxRateChange.getProdNo());
			tLoanRateChange.setBaseRateCode(tBatxRateChange.getBaseRateCode());
			tLoanRateChange.setIncrFlag(tBatxRateChange.getIncrFlag());
			tLoanRateChange.setRateIncr(rateIncr);
			tLoanRateChange.setIndividualIncr(individualIncr);
			tLoanRateChange.setFitRate(tBatxRateChange.getAdjustedRate());
			tLoanRateChange.setRemark("預調利率");
			tLoanRateChange.setAcDate(this.getTxBuffer().getTxCom().getTbsdy());
			tLoanRateChange.setTellerNo(this.getTxBuffer().getTxCom().getRelTlr());
			tLoanRateChange.setTxtNo(parse.IntegerToString(this.getTxBuffer().getTxCom().getRelTno(), 8));
			try {
				loanRateChangeService.insert(tLoanRateChange, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "BS430 loanRateChangeService insert " + e.getErrorMsg());
			}
		} else {
			tLoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId);
			try {
				loanRateChangeService.delete(tLoanRateChange, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "BS430 loanRateChangeService delete " + e.getErrorMsg());
			}
		}
	}

	// 處理階梯式利率
	private void setStepRateChange(BatxRateChange tBatxRateChange, TitaVo titaVo) throws LogicException {
		this.info("setStepRateChange ...");
//		排除欲調利率
		if ("99".equals(tBatxRateChange.getBaseRateCode())) {
			this.info("return ...");
			return;
		}
		// 生效起日
		int effectDateS = 0;
		// 指標利率
		BigDecimal baseRate = BigDecimal.ZERO;
		// 確認時，生效起日＝本次生效日， 適用利率＝指標利率(本次指標率) + (利率加減碼or個別加減碼)
		if (this.iConfirmFlag == 1) {
			effectDateS = tBatxRateChange.getCurtEffDate();
			baseRate = tBatxRateChange.getCurrBaseRate();
		} else
		// 取消確認時，生效起日＝目前生效日， 指標利率＝目前利率 - (利率加減碼or個別加減碼)
		{
			effectDateS = tBatxRateChange.getPresEffDate();
			if ("Y".equals(tBatxRateChange.getIncrFlag())) { // 加減碼是否依合約
				baseRate = tBatxRateChange.getPresentRate().subtract(tBatxRateChange.getRateIncr());
			} else {
				baseRate = tBatxRateChange.getPresentRate().subtract(tBatxRateChange.getIndividualIncr());
			}
		}

		// 讀取生效日之後的利率變動檔
		Slice<LoanRateChange> sLoanRateChange = loanRateChangeService.rateChangeBormNoEq(tBatxRateChange.getCustNo(),
				tBatxRateChange.getFacmNo(), tBatxRateChange.getBormNo(), effectDateS + 19110000 + 1, this.index,
				this.limit);
		List<LoanRateChange> lLoanRateChange = sLoanRateChange == null ? null : sLoanRateChange.getContent();

		if (lLoanRateChange != null && lLoanRateChange.size() != 0) {
			for (LoanRateChange t : lLoanRateChange) {
				// 指標利率相同且利率區分不為固定時更新
				if (t.getBaseRateCode().equals(tBatxRateChange.getBaseRateCode()) && !"2".equals(t.getRateCode())) {
					LoanRateChange tLoanRateChange = loanRateChangeService.holdById(t.getLoanRateChangeId(), titaVo);
					// 更新適用利率
					if ("Y".equals(tLoanRateChange.getIncrFlag())) { // 加減碼是否依合約
						tLoanRateChange.setFitRate(baseRate.add(tLoanRateChange.getRateIncr()));
					} else {
						tLoanRateChange.setFitRate(baseRate.add(tLoanRateChange.getIndividualIncr()));
					}
					try {
						loanRateChangeService.update(tLoanRateChange, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "BS430 loanRateChangeService update " + e.getErrorMsg());
					}
				}
			}
		}
	}
}