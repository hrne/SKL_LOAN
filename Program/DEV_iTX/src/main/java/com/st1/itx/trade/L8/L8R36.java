package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R36")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R36 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R36.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ573Service iJcicZ573Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R36 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ573 iJcicZ573 = new JcicZ573();
		iJcicZ573 = iJcicZ573Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ573 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r36TranKey",iJcicZ573.getTranKey());
			totaVo.putParam("L8r36CustId" ,iJcicZ573.getCustId());
			totaVo.putParam("L8r36SubmitKey",iJcicZ573.getSubmitKey());
			totaVo.putParam("L8r36ApplyDate",iJcicZ573.getApplyDate());
			totaVo.putParam("L8r36PayDate",iJcicZ573.getPayDate());
			totaVo.putParam("L8r36PayAmt",iJcicZ573.getPayAmt());
			totaVo.putParam("L8r36TotalPayAmt",iJcicZ573.getTotalPayAmt());
			totaVo.putParam("L8r36OutJcicTxtDate", iJcicZ573.getOutJcicTxtDate());
			totaVo.putParam("L8r36TranKey", iJcicZ573.getTranKey());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}