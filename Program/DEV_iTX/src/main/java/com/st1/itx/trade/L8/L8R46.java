package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.tradeService.TradeBuffer;


@Service("L8R46")
@Scope("prototype")
/**
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R46 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ450Service iJcicZ450Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r46 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ450 iJcicZ450 = new JcicZ450();
		iJcicZ450 = iJcicZ450Service.ukeyFirst(iUkey, titaVo);
	
		if (iJcicZ450 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r46TranKey", iJcicZ450.getTranKey());// 交易代碼
			totaVo.putParam("L8r46CustId", iJcicZ450.getCustId());// 債務人IDN
			totaVo.putParam("L8r46SubmitKey", iJcicZ450.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r46ApplyDate", iJcicZ450.getApplyDate());
			totaVo.putParam("L8r46CourtCode", iJcicZ450.getCourtCode());
			totaVo.putParam("L8r46PayDate", iJcicZ450.getPayDate());
			totaVo.putParam("L8r46PayAmt",iJcicZ450.getPayAmt());
			totaVo.putParam("L8r46SumRepayActualAmt",iJcicZ450.getSumRepayActualAmt());
			totaVo.putParam("L8r46SumRepayShouldAmt",iJcicZ450.getSumRepayShouldAmt());
			totaVo.putParam("L8r46PayStatus",iJcicZ450.getPayStatus());
			totaVo.putParam("L8r46OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}