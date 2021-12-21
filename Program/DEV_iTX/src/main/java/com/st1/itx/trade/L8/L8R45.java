package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R45")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R45 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ448Service iJcicZ448Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r45 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ448 iJcicZ448 = new JcicZ448();
		iJcicZ448 = iJcicZ448Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ448 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r45TranKey", iJcicZ448.getTranKey());
			totaVo.putParam("L8r45CustId", iJcicZ448.getCustId());
			totaVo.putParam("L8r45SubmitKey", iJcicZ448.getSubmitKey());
			totaVo.putParam("L8r45ApplyDate", iJcicZ448.getApplyDate());
			totaVo.putParam("L8r45CourtCode", iJcicZ448.getCourtCode());
			totaVo.putParam("L8r45MaxMainCode", iJcicZ448.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r45SignPrin", iJcicZ448.getSignPrin());// 簽約金額-本金
			totaVo.putParam("L8r45SignOther", iJcicZ448.getSignOther());// 簽約金額-利息、違約金及其他費用
			totaVo.putParam("L8r45OwnPercentage", iJcicZ448.getOwnPercentage());// 債權比例
			totaVo.putParam("L8r45AcQuitAmt", iJcicZ448.getAcQuitAmt());// 每月清償金額
			totaVo.putParam("L8r45OutJcicTxtDate", iJcicZ448.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}