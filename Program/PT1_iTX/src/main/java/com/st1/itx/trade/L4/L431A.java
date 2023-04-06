package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
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
		case 9: // 9:待處理(檢核有誤)
			// 選擇調整
			switch (btnIndex) {
			case 0: // 0.按擬調利率調整
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getProposalRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
				break;
			case 1: // 1.按目前利率調整
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getPresentRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
				break;

			case 2: // 2.按輸入利率調整
				tBatxRateChange.setAdjustedRate(BigDecimal.ZERO);
				tBatxRateChange.setRateKeyInCode(2); // 2.待輸入
				break;
			case 3: // 3.按合約利率調整
				tBatxRateChange.setAdjustedRate(tBatxRateChange.getContractRate());
				tBatxRateChange.setRateKeyInCode(1); // 1.已調整
				break;
			}
			checkMsg = check("", tBatxRateChange, titaVo);
			if (!checkMsg.isEmpty()) {
				throw new LogicException("E0015", checkMsg); // 檢查錯誤
			}
			break;

		case 1: // 1.已調整
		case 2: // 2.輸入利率
			// 取消調整
			tBatxRateChange.setAdjustedRate(BigDecimal.ZERO);
			tBatxRateChange.setRateKeyInCode(0); // 0.未調整
			break;
		}

		// 加碼值，自訂利率時：0、指標利率時：擬調利率(已調整時為調整後利率)減合約指標利率
		if ("99".equals(tBatxRateChange.getBaseRateCode())) {
			tBatxRateChange.setRateIncr(BigDecimal.ZERO);
		} else {
			if (tBatxRateChange.getRateKeyInCode() == 1) {
				tBatxRateChange
						.setRateIncr(tBatxRateChange.getAdjustedRate().subtract(tBatxRateChange.getContrBaseRate()));
			} else {
				tBatxRateChange
						.setRateIncr(tBatxRateChange.getProposalRate().subtract(tBatxRateChange.getContrBaseRate()));
			}
		}

		try {
			batxRateChangeService.update(tBatxRateChange);
		} catch (DBException e) {
			throw new LogicException("E0007", "L431A tBatxRateChange update " + e.getErrorMsg());
		}
		this.addList(this.totaVo);
		return this.sendList();
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
		if (t.getAdjustedRate().compareTo(t.getPresentRate()) != 0 && t.getPrevIntDate() > t.getCurtEffDate()) {
			checkMsg += "上次繳息日大於利率生效日";
		}
		return checkMsg;
	}

}