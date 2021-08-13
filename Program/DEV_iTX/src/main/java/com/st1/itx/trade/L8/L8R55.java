package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R55")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R55 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R55.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service iJcicZ570Service;
	@Autowired
	public JcicZ571Service iJcicZ571Service;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r55 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		String iChainCd = titaVo.getParam("RimChainCd");
		switch(iChainCd) {
		case "L8332":
			JcicZ570 iJcicZ570 = new JcicZ570();
			iJcicZ570 = iJcicZ570Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ570 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ570.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ570.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ570.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
			}
		break;
		case "L8333":
			JcicZ571 iJcicZ571 = new JcicZ571();
			iJcicZ571 = iJcicZ571Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ571 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ571.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ571.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ571.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ571.getBankId());
				totaVo.putParam("L8r55PayDate", "");
			}
		break;
		case "L8334":
			JcicZ572 iJcicZ572 = new JcicZ572();
			iJcicZ572 = iJcicZ572Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ572 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ572.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ572.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ572.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ572.getBankId());
				totaVo.putParam("L8r55PayDate", iJcicZ572.getPayDate());
			}
		break;
		case "L8335":
			JcicZ573 iJcicZ573 = new JcicZ573();
			iJcicZ573 = iJcicZ573Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ573 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ573.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ573.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ573.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", iJcicZ573.getPayDate());
			}
		break;
		case "L8336":
			JcicZ574 iJcicZ574 = new JcicZ574();
			iJcicZ574 = iJcicZ574Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ574 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ574.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ574.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ574.getApplyDate());
				totaVo.putParam("L8r55BankId", "");
				totaVo.putParam("L8r55PayDate", "");
			}
		break;
		case "L8337":
			JcicZ575 iJcicZ575 = new JcicZ575();
			iJcicZ575 = iJcicZ575Service.ukeyFirst(iUkey, titaVo);
			
			if (iJcicZ575 == null) {
				throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
			}else {
				totaVo.putParam("L8r55CustId", iJcicZ575.getCustId());
				totaVo.putParam("L8r55SubmitKey", iJcicZ575.getSubmitKey());
				totaVo.putParam("L8r55ApplyDate", iJcicZ575.getApplyDate());
				totaVo.putParam("L8r55BankId", iJcicZ575.getBankId());
				totaVo.putParam("L8r55PayDate", "");
			}
		break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}