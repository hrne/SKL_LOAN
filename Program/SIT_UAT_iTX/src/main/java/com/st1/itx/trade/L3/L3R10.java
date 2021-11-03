package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimCustNo=9,7
 * RimSyndNo=9,3
 */
/**
 * L3R10 查詢聯貸訂約檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R10")
@Scope("prototype")
public class L3R10 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3R10.class);

	/* DB服務注入 */
	@Autowired
	public LoanSyndService loanSyndService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;

	// work area
	private TitaVo titaVo = new TitaVo();
	LoanSynd tLoanSynd;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L3R10 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		int iSyndNo = this.parse.stringToInteger(titaVo.getParam("RimSyndNo"));

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L3R10"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L3R10"); // 功能選擇錯誤
		}

		initTotaLoanSynd();
		// 查尋聯貸訂約檔
		tLoanSynd = loanSyndService.findById(iSyndNo, titaVo);
		if (tLoanSynd != null) {
			if (iRimTxCode.equals("L2600")) { // 聯貸案訂約登錄
				if (iRimFuncCode == 1 || iRimFuncCode == 3) {
					throw new LogicException(titaVo, "E0002", "聯貸訂約檔  聯貸案序號 = " + iSyndNo); // 新增資料已存在
				}
			}
			moveTotaLoanSynd();
		} else {
			if (iRimTxCode.equals("L2600") && (iRimFuncCode == 1 || iRimFuncCode == 3)) {
				initTotaLoanSynd();
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "聯貸訂約檔  聯貸案序號 = " + iSyndNo); // 查詢資料不存在
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void initTotaLoanSynd() {

		this.totaVo.putParam("OSyndNo", 0);
		this.totaVo.putParam("OLeadingBank", "");
		this.totaVo.putParam("OAgentBank", "");
		this.totaVo.putParam("OSigningDate", 0);
		this.totaVo.putParam("OSyndTypeCodeFlag", "");
		this.totaVo.putParam("OPartRate", 0);
		this.totaVo.putParam("OCurrencyCode", "");
		this.totaVo.putParam("OSyndAmt", 0);
		this.totaVo.putParam("OPartAmt", 0);

	}

	private void moveTotaLoanSynd() throws LogicException {
		this.totaVo.putParam("OSyndNo", tLoanSynd.getSyndNo());
		this.totaVo.putParam("OLeadingBank", tLoanSynd.getLeadingBank());
		this.totaVo.putParam("OAgentBank", tLoanSynd.getAgentBank());
		this.totaVo.putParam("OSigningDate", tLoanSynd.getSigningDate());
		this.totaVo.putParam("OSyndTypeCodeFlag", tLoanSynd.getSyndTypeCodeFlag());
		this.totaVo.putParam("OPartRate", tLoanSynd.getPartRate());
		this.totaVo.putParam("OCurrencyCode", tLoanSynd.getCurrencyCode());
		this.totaVo.putParam("OSyndAmt", tLoanSynd.getSyndAmt());
		this.totaVo.putParam("OPartAmt", tLoanSynd.getPartAmt());

	}
}