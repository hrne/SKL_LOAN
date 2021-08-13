package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.BankAuthActComServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RimCustId=X,10<br>
 * RimPostDepCode=X,1<br>
 * RimCustNo=9,7<br>
 */

@Service("L4R01")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R01 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R01.class);

	/* DB服務注入 */
	@Autowired
	public BankAuthActComServiceImpl bankAuthActComServiceImpl;

	@Autowired
	public Parse parse;

	private String acctSeq = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R01 ");
		this.totaVo.init(titaVo);

		this.info("L4R01 titaVo = " + titaVo.toString());
		// CustId=X,10
		int rimCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo").trim());
		String rimDepCode = titaVo.getParam("RimPostDepCode").trim();
		int seq = 0;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = bankAuthActComServiceImpl.getAcctSeq(rimCustNo, rimDepCode, titaVo);
		} catch (Exception e) {
			this.error("bankAuthActComServiceImpl error..." + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

//		同戶號、同帳號，計算筆數
		if (resultList != null && resultList.size() > 0) {
			seq = parse.stringToInteger(resultList.get(0).get("F1"));
		}

//		第一筆=空白，第二筆之後=01起編
		if (seq >= 1) {
			acctSeq = FormatUtil.pad9("" + seq, 2);
		}

		this.totaVo.putParam("L4r01RepayAcctSeq", acctSeq);

		this.addList(this.totaVo);
		this.info("After AddList = " + this.toString());
		return this.sendList();
	}
}