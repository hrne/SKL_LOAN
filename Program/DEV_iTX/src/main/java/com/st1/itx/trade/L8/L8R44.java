package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R44")
@Scope("prototype")
/**
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R44 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ447Service iJcicZ447Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r44 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ447 iJcicZ447 = new JcicZ447();
		iJcicZ447 = iJcicZ447Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ447 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r44TranKey", iJcicZ447.getTranKey());// 交易代碼
			totaVo.putParam("L8r44CustId", iJcicZ447.getCustId());// 債務人IDN
			totaVo.putParam("L8r44SubmitKey", iJcicZ447.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r44ApplyDate", iJcicZ447.getApplyDate());// 調解申請日
			totaVo.putParam("L8r44CourtCode", iJcicZ447.getCourtCode());// 受理調解機構代號
			totaVo.putParam("L8r44Civil323Amt", iJcicZ447.getCivil323Amt());// 依民法第323條計算之債務總金額
			totaVo.putParam("L8r44TotalAmt", iJcicZ447.getTotalAmt());// 簽約總債務金額
			totaVo.putParam("L8r44SignDate", iJcicZ447.getSignDate());// 簽約完成日期
			totaVo.putParam("L8r44FirstPayDate", iJcicZ447.getFirstPayDate());// 首期應繳款日
			totaVo.putParam("L8r44Period", iJcicZ447.getPeriod());// 期數
			totaVo.putParam("L8r44Rate", iJcicZ447.getRate());// 利率
			totaVo.putParam("L8r44MonthPayAmt", iJcicZ447.getMonthPayAmt());// 月付金
			totaVo.putParam("L8r44PayAccount", iJcicZ447.getPayAccount());// 繳款帳號
			totaVo.putParam("L8r44OutJcicTxtDate", iJcicZ447.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}