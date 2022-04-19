package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R40")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R40 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ442Service iJcicZ442Service;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r40 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ442 iJcicZ442 = new JcicZ442();
		iJcicZ442 = iJcicZ442Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ442 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r40TranKey", iJcicZ442.getTranKey());
			totaVo.putParam("L8r40CustId", iJcicZ442.getCustId());
			totaVo.putParam("L8r40SubmitKey", iJcicZ442.getSubmitKey());
			totaVo.putParam("L8r40ApplyDate", iJcicZ442.getApplyDate());
			totaVo.putParam("L8r40CourtCode", iJcicZ442.getCourtCode());// 異動債權金機構代號
			totaVo.putParam("L8r40MaxMainCode", iJcicZ442.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r40IsMaxMain", iJcicZ442.getIsMaxMain()); // 是否為最大債權金融機構報送
			totaVo.putParam("L8r40IsClaims", iJcicZ442.getIsClaims()); // 是否為本金融機構債務人
			totaVo.putParam("L8r40GuarLoanCnt", iJcicZ442.getGuarLoanCnt()); // 本金融機構有擔保債權筆數
			totaVo.putParam("L8r40Civil323ExpAmt", iJcicZ442.getCivil323ExpAmt()); // 依民法第323條計算之信用放款本息餘額
			totaVo.putParam("L8r40Civil323CashAmt", iJcicZ442.getCivil323CashAmt()); // 依民法第323條計算之現金卡放款本息餘額
			totaVo.putParam("L8r40Civil323CreditAmt", iJcicZ442.getCivil323CreditAmt()); // 依民法第323條計算之信用卡本息餘額
			totaVo.putParam("L8r40Civil323GuarAmt", iJcicZ442.getCivil323GuarAmt()); // 依民法第323條計算之保證債權本息餘額
			totaVo.putParam("L8r40ReceExpPrin", iJcicZ442.getReceExpPrin()); // 信用放款本金
			totaVo.putParam("L8r40ReceExpInte", iJcicZ442.getReceExpInte()); // 信用放款利息
			totaVo.putParam("L8r40ReceExpPena", iJcicZ442.getReceExpPena()); // 信用放款違約金
			totaVo.putParam("L8r40ReceExpOther", iJcicZ442.getReceExpOther()); // 信用放款其他費用
			totaVo.putParam("L8r40CashCardPrin", iJcicZ442.getCashCardPrin()); // 現金卡本金
			totaVo.putParam("L8r40CashCardInte", iJcicZ442.getCashCardInte()); // 現金卡利息
			totaVo.putParam("L8r40CashCardPena", iJcicZ442.getCashCardPena()); // 現金卡違約金
			totaVo.putParam("L8r40CashCardOther", iJcicZ442.getCashCardOther()); // 現金卡其他費用
			totaVo.putParam("L8r40CreditCardPrin", iJcicZ442.getCreditCardPrin()); // 信用卡本金
			totaVo.putParam("L8r40CreditCardInte", iJcicZ442.getCreditCardInte()); // 信用卡利息
			totaVo.putParam("L8r40CreditCardPena", iJcicZ442.getCreditCardPena()); // 信用卡違約金
			totaVo.putParam("L8r40CreditCardOther", iJcicZ442.getCreditCardOther()); // 信用卡其他費用
			totaVo.putParam("L8r40GuarObliPrin", iJcicZ442.getGuarObliPrin()); // 保證債權本金
			totaVo.putParam("L8r40GuarObliInte", iJcicZ442.getGuarObliInte()); // 保證債權利息
			totaVo.putParam("L8r40GuarObliPena", iJcicZ442.getGuarObliPena()); // 保證債權其他費用
			totaVo.putParam("L8r40GuarObliOther", iJcicZ442.getGuarObliOther()); // 信用卡其他費用
			totaVo.putParam("L8r40OutJcicTxtDate", iJcicZ442.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}