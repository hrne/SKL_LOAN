package com.st1.itx.trade.L5;

import java.util.ArrayList;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegFinAcct;
/*DB服務*/
import com.st1.itx.db.service.NegFinAcctService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegReportCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R04")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R04 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	@Autowired
	public NegReportCom NegReportCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5R04");
		this.info("active L5R04 ");
		this.totaVo.init(titaVo);

		String RimFinCode = titaVo.getParam("RimFinCode").trim();// 債權機構
		NegFinAcct NegFinAcctVO = new NegFinAcct();
		NegFinAcctVO = sNegFinAcctService.findById(RimFinCode, titaVo);
		if (NegFinAcctVO != null) {
			totaVo.putParam("L5r04FinCode", NegFinAcctVO.getFinCode());
			totaVo.putParam("L5r04FinCodeX", NegFinAcctVO.getFinItem());
			totaVo.putParam("L5r04RemitBank", NegFinAcctVO.getRemitBank());
			totaVo.putParam("L5r04RemitAcct", NegFinAcctVO.getRemitAcct());
			totaVo.putParam("L5r04RemitAcct2", NegFinAcctVO.getRemitAcct2());
			totaVo.putParam("L5r04RemitAcct3", NegFinAcctVO.getRemitAcct3());
			totaVo.putParam("L5r04RemitAcct4", NegFinAcctVO.getRemitAcct4());
			totaVo.putParam("L5r04DataSendSection", NegFinAcctVO.getDataSendSection());
		} else {
			String RemitBank = RimFinCode;
			totaVo.putParam("L5r04FinCode", RimFinCode);
			totaVo.putParam("L5r04FinCodeX", "");
			totaVo.putParam("L5r04RemitBank", RemitBank);// 七碼
			totaVo.putParam("L5r04RemitAcct", "");
			totaVo.putParam("L5r04RemitAcct2", "");
			totaVo.putParam("L5r04RemitAcct3", "");
			totaVo.putParam("L5r04RemitAcct4", "");
			totaVo.putParam("L5r04DataSendSection", "");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}