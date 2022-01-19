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
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
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

		this.totaVo.putParam("L3r10SyndNo", 0);
		this.totaVo.putParam("L3r10LeadingBank", "");
		this.totaVo.putParam("L3r10AgentBank", "");
		this.totaVo.putParam("L3r10SigningDate", 0);
		this.totaVo.putParam("L3r10SyndTypeCodeFlag", "");
		this.totaVo.putParam("L3r10PartRate", 0);
		this.totaVo.putParam("L3r10CurrencyCode", "");
		this.totaVo.putParam("L3r10SyndAmt", 0);
		this.totaVo.putParam("L3r10PartAmt", 0);

	}

	private void moveTotaLoanSynd() throws LogicException {
		this.totaVo.putParam("L3r10SyndNo", tLoanSynd.getSyndNo());
		this.totaVo.putParam("L3r10LeadingBank", tLoanSynd.getLeadingBank());
		this.totaVo.putParam("L3r10AgentBank", tLoanSynd.getAgentBank());
		this.totaVo.putParam("L3r10SigningDate", tLoanSynd.getSigningDate());
		this.totaVo.putParam("L3r10SyndTypeCodeFlag", tLoanSynd.getSyndTypeCodeFlag());
		this.totaVo.putParam("L3r10PartRate", tLoanSynd.getPartRate());
		this.totaVo.putParam("L3r10CurrencyCode", tLoanSynd.getCurrencyCode());
		this.totaVo.putParam("L3r10SyndAmt", tLoanSynd.getSyndAmt());
		this.totaVo.putParam("L3r10PartAmt", tLoanSynd.getPartAmt());

	}
}