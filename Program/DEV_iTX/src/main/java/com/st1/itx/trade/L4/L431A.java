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
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L431A")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L431A extends TradeBuffer {

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	int adjDate = 0;
	private int adjCode = 0;
	private int custNo = 0;
	private int facmNo = 0;
	private int bormNo = 0;
	private int curtEffDate = 0;
	private int txKind = 0;

	/**
	 * 0.按擬調利率調整 </br>
	 * 1.按目前利率調整 </br>
	 * 2.按輸入利率調整 </br>
	 */
	int btnIndex = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L431A ");
		this.totaVo.init(titaVo);

//		T(6A,#AdjMonth+#OOAdjCode+#OOCustNo+#OOFacmNo+#OOBormNo+#OOCurtEffDate)

		adjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		txKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		adjCode = parse.stringToInteger(titaVo.getParam("OOAdjCode"));
		custNo = parse.stringToInteger(titaVo.getParam("OOCustNo"));
		facmNo = parse.stringToInteger(titaVo.getParam("OOFacmNo"));
		bormNo = parse.stringToInteger(titaVo.getParam("OOBormNo"));
		curtEffDate = parse.stringToInteger(titaVo.getParam("OOCurtEffDate"));
		this.info("titaVo.getBtnIndex() ..." + titaVo.getBtnIndex());
		btnIndex = parse.stringToInteger(titaVo.getBtnIndex());
		String checkMsg = "";
		this.info("adjDate ..." + adjDate);
		this.info("txKind ..." + txKind);
		this.info("adjCode ..." + adjCode);
		this.info("custNo ..." + custNo);
		this.info("facmNo ..." + facmNo);
		this.info("bormNo ..." + bormNo);
		this.info("curtEffDate ..." + curtEffDate);
		this.info("btnIndex ..." + btnIndex);

		BatxRateChange tBatxRateChange = new BatxRateChange();
		BatxRateChangeId tBatxRateChangeId = new BatxRateChangeId();
		tBatxRateChangeId.setAdjDate(adjDate);
		tBatxRateChangeId.setCustNo(custNo);
		tBatxRateChangeId.setFacmNo(facmNo);
		tBatxRateChangeId.setBormNo(bormNo);

		tBatxRateChange = batxRateChangeService.holdById(tBatxRateChangeId);
		if (tBatxRateChange == null) {
			throw new LogicException("E0001", "L431A Couldn't Find CustNo : " + parse.IntegerToString(custNo, 7) + "-"
					+ parse.IntegerToString(facmNo, 3) + "-" + parse.IntegerToString(bormNo, 3));
		}

		if (tBatxRateChange.getConfirmFlag() == 1) {
			throw new LogicException("E0007", " 此筆資料已確認，請先訂正L4321。");
		}

		if (tBatxRateChange.getConfirmFlag() == 2) {
			throw new LogicException("E0007", " 此筆資料已確認放行，請先主管訂正L4321。");
		}

		// 選擇調整、取消調整
		switch (tBatxRateChange.getRateKeyInCode()) {
		case 0: // 0.未調整
			// 選擇調整
			switch (btnIndex) {
			case 0: // 0.按擬調利率調整
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getProposalRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
				break;
			case 1: // 1.按目前利率調整
				checkMsg = "按目前利率調整 ";
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getPresentRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
				break;

			case 2: // 2.按輸入利率調整
				checkMsg = "按輸入利率調整 ";
				tBatxRateChange.setAdjustedRate(BigDecimal.ZERO);
				tBatxRateChange.setRateKeyInCode(2); // 2.待輸入
				break;
			}
			checkMsg = check(checkMsg, tBatxRateChange, titaVo);
			if (!checkMsg.isEmpty()) {
				throw new LogicException("E0015", checkMsg); // 檢查錯誤
			}
			break;

		case 1: // 1.已調整
			// 取消調整
			tBatxRateChange.setAdjustedRate(BigDecimal.ZERO);
			tBatxRateChange.setRateKeyInCode(0); // 0.未調整
			tBatxRateChange.setConfirmFlag(0);
			// get tempVo
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tBatxRateChange.getJsonFields());
			// 放款利率變動檔生效日，利率未變動為零
			int txEffectDate = 0;
			if (tBatxRateChange.getAdjustedRate().compareTo(tBatxRateChange.getPresentRate()) != 0) {
				txEffectDate = tBatxRateChange.getCurtEffDate();
			}

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

			break;

		case 9: // 9.待處理(檢核有誤)
			// 重新處理
			if (tBatxRateChange.getAdjCode() == 1) {
				checkMsg = check(checkMsg, tBatxRateChange, titaVo);
				if (!checkMsg.isEmpty()) {
					throw new LogicException("E0015", checkMsg); // 檢查錯誤
				}
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getProposalRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
			} else {
				tBatxRateChange.setAdjustedRate(BigDecimal.ZERO);
				tBatxRateChange.setRateKeyInCode(0); // 0.未調整
			}
			break;
		}

		try {
			batxRateChangeService.update(tBatxRateChange);
		} catch (DBException e) {
			throw new LogicException("E0007", "L431A tBatxRateChange update " + e.getErrorMsg());
		}
		this.addList(this.totaVo);
		return this.sendList();
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
		nextAdjDate = tBatxRateChange.getPreNextAdjDate();
		tLoanBorMain.setNextAdjRateDate(nextAdjDate);

		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "BS430 LoanBorMain update " + e.getErrorMsg());
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
		tLoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId);
		try {
			loanRateChangeService.delete(tLoanRateChange, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0008", "BS430 loanRateChangeService delete " + e.getErrorMsg());
		}
	}

	// 處理本次生效日
	private void setLoanRateChange(BatxRateChange tBatxRateChange, TitaVo titaVo) throws LogicException {
		this.info("setLoanRateChange ...");
		// 更新記號 0:新增 1:更新 2.刪除
		int updateFg = 0;
		LoanRateChange tLoanRateChange = new LoanRateChange();
		LoanRateChangeId tLoanRateChangeId = new LoanRateChangeId();

		tLoanRateChangeId.setCustNo(tBatxRateChange.getCustNo());
		tLoanRateChangeId.setFacmNo(tBatxRateChange.getFacmNo());
		tLoanRateChangeId.setBormNo(tBatxRateChange.getBormNo());
		tLoanRateChangeId.setEffectDate(tBatxRateChange.getCurtEffDate());

		tLoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId, titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException("E0006", "BS430 loanRateChang hold " + tLoanRateChangeId);
		} else {
			// 取消確認時，整批更新需刪除
			if (tLoanRateChange.getStatus() == 1) {
				updateFg = 2;
			} else {
				updateFg = 1;
			}
		}
		// 更新
		if (updateFg == 1) {
			this.info("JsonFields ... " + tBatxRateChange.getJsonFields());
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tBatxRateChange.getJsonFields());
			tLoanRateChange.setRateIncr(parse.stringToBigDecimal(tTempVo.getParam("RateIncr")));
			tLoanRateChange.setIndividualIncr(parse.stringToBigDecimal(tTempVo.getParam("IndividualIncr")));
			tLoanRateChange.setFitRate(parse.stringToBigDecimal(tTempVo.getParam("FitRate")));
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
		// 取消確認時，生效起日＝目前生效日， 指標利率＝目前利率 - (利率加減碼or個別加減碼)
		effectDateS = tBatxRateChange.getPresEffDate();
		if ("Y".equals(tBatxRateChange.getIncrFlag())) { // 加減碼是否依合約
			baseRate = tBatxRateChange.getPresentRate().subtract(tBatxRateChange.getRateIncr());
		} else {
			baseRate = tBatxRateChange.getPresentRate().subtract(tBatxRateChange.getIndividualIncr());
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

	private String check(String checkMsg, BatxRateChange t, TitaVo titaVo) throws LogicException {
		LoanBorMain tLoanBorMain = loanBorMainService
				.holdById(new LoanBorMainId(t.getCustNo(), t.getFacmNo(), t.getBormNo()));
		if (tLoanBorMain == null) {
			throw new LogicException("E0006", "LoanBorMain ");
		}
		t.setPreNextAdjFreq(tLoanBorMain.getRateAdjFreq());
		t.setPrevIntDate(tLoanBorMain.getPrevPayIntDate());

		if ("3".equals(t.getRateCode()) && t.getPreNextAdjFreq() == 0) {
			checkMsg += "定期機動但無利率調整週期";
		}
		BigDecimal fitRate = t.getPresentRate();
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(t.getJsonFields());
		if (tTempVo.get("FitRate") != null) {
			fitRate = parse.stringToBigDecimal(tTempVo.get("FitRate"));
		}
		if (t.getAdjustedRate().compareTo(fitRate) != 0 && t.getPrevIntDate() > t.getCurtEffDate()) {
			checkMsg += "上次繳息日大於利率生效日";
		}
		t.setJsonFields(tTempVo.getJsonString());
		return checkMsg;
	}

}