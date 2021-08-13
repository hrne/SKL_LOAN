package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R33")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R33 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R33.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service iJcicZ570Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r33 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ570 iJcicZ570 = new JcicZ570();
		iJcicZ570 = iJcicZ570Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ570 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r33TranKey", iJcicZ570.getTranKey());
			totaVo.putParam("L8r33CustId", iJcicZ570.getCustId());
			totaVo.putParam("L8r33SubmitKey", iJcicZ570.getSubmitKey());
			totaVo.putParam("L8r33ApplyDate", iJcicZ570.getApplyDate());
			totaVo.putParam("L8r33AdjudicateDate", iJcicZ570.getAdjudicateDate());
			totaVo.putParam("L8r33BankCount", iJcicZ570.getBankCount());
			totaVo.putParam("L8r33Bank1", iJcicZ570.getBank1());
			totaVo.putParam("L8r33Bank2", iJcicZ570.getBank2());
			totaVo.putParam("L8r33Bank3", iJcicZ570.getBank3());
			totaVo.putParam("L8r33Bank4", iJcicZ570.getBank4());
			totaVo.putParam("L8r33Bank5", iJcicZ570.getBank5());
			totaVo.putParam("L8r33Bank6", iJcicZ570.getBank6());
			totaVo.putParam("L8r33Bank7", iJcicZ570.getBank7());
			totaVo.putParam("L8r33Bank8", iJcicZ570.getBank8());
			totaVo.putParam("L8r33Bank9", iJcicZ570.getBank9());
			totaVo.putParam("L8r33Bank10", iJcicZ570.getBank10());
			totaVo.putParam("L8r33Bank11", iJcicZ570.getBank11());
			totaVo.putParam("L8r33Bank12", iJcicZ570.getBank12());
			totaVo.putParam("L8r33Bank13", iJcicZ570.getBank13());
			totaVo.putParam("L8r33Bank14", iJcicZ570.getBank14());
			totaVo.putParam("L8r33Bank15", iJcicZ570.getBank15());
			totaVo.putParam("L8r33Bank16", iJcicZ570.getBank16());
			totaVo.putParam("L8r33Bank17", iJcicZ570.getBank17());
			totaVo.putParam("L8r33Bank18", iJcicZ570.getBank18());
			totaVo.putParam("L8r33Bank19", iJcicZ570.getBank19());
			totaVo.putParam("L8r33Bank20", iJcicZ570.getBank20());
			totaVo.putParam("L8r33Bank21", iJcicZ570.getBank21());
			totaVo.putParam("L8r33Bank22", iJcicZ570.getBank22());
			totaVo.putParam("L8r33Bank23", iJcicZ570.getBank23());
			totaVo.putParam("L8r33Bank24", iJcicZ570.getBank24());
			totaVo.putParam("L8r33Bank25", iJcicZ570.getBank25());
			totaVo.putParam("L8r33Bank26", iJcicZ570.getBank26());
			totaVo.putParam("L8r33Bank27", iJcicZ570.getBank27());
			totaVo.putParam("L8r33Bank28", iJcicZ570.getBank28());
			totaVo.putParam("L8r33Bank29", iJcicZ570.getBank29());
			totaVo.putParam("L8r33Bank30", iJcicZ570.getBank30());
			totaVo.putParam("L8r33OutJcicTxtDate", iJcicZ570.getOutJcicTxtDate());			
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}