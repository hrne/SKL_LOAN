package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R35")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R35 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R35.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r35 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ572 iJcicZ572 = new JcicZ572();
		iJcicZ572 = iJcicZ572Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ572 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r35TranKey", iJcicZ572.getTranKey());
			totaVo.putParam("L8r35CustId", iJcicZ572.getCustId());
			totaVo.putParam("L8r35SubmitKey", iJcicZ572.getSubmitKey());
			totaVo.putParam("L8r35ApplyDate", iJcicZ572.getApplyDate());
			totaVo.putParam("L8r35StartDate", iJcicZ572.getStartDate());// 生效日期 
			totaVo.putParam("L8r35PayDate", iJcicZ572.getPayDate());// 本分配表首繳日 
			totaVo.putParam("L8r35BankId",iJcicZ572.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r35AllotAmt", iJcicZ572.getAllotAmt());// 參與分配債權金額
			totaVo.putParam("L8r35OwnPercentage", iJcicZ572.getOwnPercentage());// 債權比例
			totaVo.putParam("L8r35OutJcicTxtDate", iJcicZ572.getOutJcicTxtDate());		
		}		
		this.addList(this.totaVo);
		return this.sendList();
	}
}