package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimDataYearMonth=9,5
 */
@Service("L6R15") // 尋找現金流量預估資料檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R15 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R15.class);

	/* DB服務注入 */
	@Autowired
	public CdCashFlowService sCdCashFlowService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R15 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iRimDataYearMonth = this.parse.stringToInteger(titaVo.getParam("RimDataYearMonth"));
		this.info("L6R15 1 iRimDataYearMonth : " + iRimDataYearMonth);
		int iRimYearMonth = iRimDataYearMonth + 191100;
		this.info("L6R15 1 iRimYearMonth : " + iRimYearMonth);

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R15"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R15"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdCashFlow(new CdCashFlow());

		// 查詢現金流量預估資料檔
		CdCashFlow tCdCashFlow = sCdCashFlowService.findById(iRimYearMonth, titaVo);

		/* 如有找到資料 */
		if (tCdCashFlow != null) {
			if (iRimTxCode.equals("L6707") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimDataYearMonth")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdCashFlow(tCdCashFlow);
			}
		} else {
			if (iRimTxCode.equals("L6707") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "現金流量預估資料檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 現金流量預估資料檔
	private void moveTotaCdCashFlow(CdCashFlow mCdCashFlow) throws LogicException {

		int YearMonth = mCdCashFlow.getDataYearMonth();
		this.info("L6R15 1 mCdCashFlow.getDataYearMonth : " + YearMonth);
		if (YearMonth != 0) {
			YearMonth = YearMonth - 191100;
			this.totaVo.putParam("L6R15DataYearMonth", YearMonth);
		} else {
			this.totaVo.putParam("L6R15DataYearMonth", 0);
		}

		this.totaVo.putParam("L6R15InterestIncome", mCdCashFlow.getInterestIncome());
		this.totaVo.putParam("L6R15PrincipalAmortizeAmt", mCdCashFlow.getPrincipalAmortizeAmt());
		this.totaVo.putParam("L6R15PrepaymentAmt", mCdCashFlow.getPrepaymentAmt());
		this.totaVo.putParam("L6R15DuePaymentAmt", mCdCashFlow.getDuePaymentAmt());
		this.totaVo.putParam("L6R15ExtendAmt", mCdCashFlow.getExtendAmt());
		this.totaVo.putParam("L6R15LoanAmt", mCdCashFlow.getLoanAmt());
	}

}