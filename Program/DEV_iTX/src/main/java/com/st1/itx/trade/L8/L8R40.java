package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Id;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R40")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R40 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R40.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicCom jcicCom;
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
		this.info("L8r40rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String iMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ442Id lJcicZ442Id = new JcicZ442Id();
		lJcicZ442Id.setApplyDate(iApplyDate);
		lJcicZ442Id.setCustId(iCustId);
//		lJcicZ442Id.setBankId(iBankId);
		lJcicZ442Id.setSubmitKey(RimSubmitKey);
		lJcicZ442Id.setMaxMainCode(iMaxMainCode);
		JcicZ442 JcicZ442VO = sJcicZ442Service.findById(lJcicZ442Id, titaVo);
		if (JcicZ442VO != null) {
			totaVo.putParam("L8r40TranKey", jcicCom.changeTranKey(JcicZ442VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r40CustId", JcicZ442VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r40SubmitKey", JcicZ442VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r40SubmitKeyX", jcicCom.FinCodeName(JcicZ442VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r40ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ442VO.getApplyDate()), 0));// 款項統一收付申請日
//			totaVo.putParam("L8r40BankId", JcicZ442VO.getBankId());// 受理調解機構代號
//			totaVo.putParam("L8r40BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ442VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r40MaxMainCode", JcicZ442VO.getMaxMainCode());// 最大債權金機構代號
			totaVo.putParam("L8r40MaxMainCodeX", jcicCom.FinCodeName(JcicZ442VO.getMaxMainCode(), 0, titaVo));// 最大債權金機構代號
			totaVo.putParam("L8r40IsMaxMain", JcicZ442VO.getIsMaxMain()); // 是否為最大債權金融機構報送
			totaVo.putParam("L8r40IsClaims", JcicZ442VO.getIsClaims()); // 是否為本金融機構債務人
			totaVo.putParam("L8r40GuarLoanCnt", JcicZ442VO.getGuarLoanCnt()); // 本金融機構有擔保債權筆數
			totaVo.putParam("L8r40Civil323ExpAmt", JcicZ442VO.getCivil323ExpAmt()); // 依民法第323條計算之信用放款本息餘額
			totaVo.putParam("L8r40Civil323CashAmt", JcicZ442VO.getCivil323CashAmt()); // 依民法第323條計算之現金卡放款本息餘額
			totaVo.putParam("L8r40Civil323CreditAmt", JcicZ442VO.getCivil323CreditAmt()); // 依民法第323條計算之信用卡本息餘額
			totaVo.putParam("L8r40Civil323GuarAmt", JcicZ442VO.getCivil323GuarAmt()); // 依民法第323條計算之保證債權本息餘額
			totaVo.putParam("L8r40ReceExpPrin", JcicZ442VO.getReceExpPrin()); // 信用放款本金
			totaVo.putParam("L8r40ReceExpInte", JcicZ442VO.getReceExpInte()); // 信用放款利息
			totaVo.putParam("L8r40ReceExpPena", JcicZ442VO.getReceExpPena()); // 信用放款違約金
			totaVo.putParam("L8r40ReceExpOther", JcicZ442VO.getReceExpOther()); // 信用放款其他費用
			totaVo.putParam("L8r40CashCardPrin", JcicZ442VO.getCashCardPrin()); // 現金卡本金
			totaVo.putParam("L8r40CashCardInte", JcicZ442VO.getCashCardInte()); // 現金卡利息
			totaVo.putParam("L8r40CashCardPena", JcicZ442VO.getCashCardPena()); // 現金卡違約金
			totaVo.putParam("L8r40CashCardOther", JcicZ442VO.getCashCardOther()); // 現金卡其他費用
			totaVo.putParam("L8r40CreditCardPrin", JcicZ442VO.getCreditCardPrin()); // 信用卡本金
			totaVo.putParam("L8r40CreditCardInte", JcicZ442VO.getCreditCardInte()); // 信用卡利息
			totaVo.putParam("L8r40CreditCardPena", JcicZ442VO.getCreditCardPena()); // 信用卡違約金
			totaVo.putParam("L8r40CreditCardOther", JcicZ442VO.getCreditCardOther()); // 信用卡其他費用
			totaVo.putParam("L8r40GuarObliPrin", JcicZ442VO.getGuarObliPrin()); // 保證債權本金
			totaVo.putParam("L8r40GuarObliInte", JcicZ442VO.getGuarObliInte()); // 保證債權利息
			totaVo.putParam("L8r40GuarObliPena", JcicZ442VO.getGuarObliPena()); // 保證債權違約金
			totaVo.putParam("L8r40GuarObliOther", JcicZ442VO.getGuarObliOther()); // 信用卡其他費用
			totaVo.putParam("L8r40OutJcicTxtDate", JcicZ442VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R40insert");
			totaVo.putParam("L8r40TranKey", "A");// 交易代碼
			totaVo.putParam("L8r40CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r40SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r40SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r40ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r40BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r40BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r40MaxMainCode", "");// 異動債權金機構代號
			totaVo.putParam("L8r40MaxMainCodeX", "");// 異動債權金機構代號
			totaVo.putParam("L8r40IsMaxMain", ""); // 是否為最大債權金融機構報送
			totaVo.putParam("L8r40IsClaims", ""); // 是否為本金融機構債務人
			totaVo.putParam("L8r40GuarLoanCnt", ""); // 本金融機構有擔保債權筆數
			totaVo.putParam("L8r40Civil323ExpAmt", ""); // 依民法第323條計算之信用放款本息餘額
			totaVo.putParam("L8r40Civil323CashAmt", ""); // 依民法第323條計算之現金卡放款本息餘額
			totaVo.putParam("L8r40Civil323CreditAmt", ""); // 依民法第323條計算之信用卡本息餘額
			totaVo.putParam("L8r40Civil323GuarAmt", ""); // 依民法第323條計算之保證債權本息餘額
			totaVo.putParam("L8r40ReceExpPrin", ""); // 信用放款本金
			totaVo.putParam("L8r40ReceExpInte", ""); // 信用放款利息
			totaVo.putParam("L8r40ReceExpPena", ""); // 信用放款違約金
			totaVo.putParam("L8r40ReceExpOther", ""); // 信用放款其他費用
			totaVo.putParam("L8r40CashCardPrin", ""); // 現金卡本金
			totaVo.putParam("L8r40CashCardInte", ""); // 現金卡利息
			totaVo.putParam("L8r40CashCardPena", ""); // 現金卡違約金
			totaVo.putParam("L8r40CashCardOther", ""); // 現金卡其他費用
			totaVo.putParam("L8r40CreditCardPrin", ""); // 信用卡本金
			totaVo.putParam("L8r40CreditCardInte", ""); // 信用卡利息
			totaVo.putParam("L8r40CreditCardPena", ""); // 信用卡違約金
			totaVo.putParam("L8r40CreditCardOther", ""); // 信用卡其他費用
			totaVo.putParam("L8r40GuarObliPrin", ""); // 保證債權本金
			totaVo.putParam("L8r40GuarObliInte", ""); // 保證債權利息
			totaVo.putParam("L8r40GuarObliPena", ""); // 保證債權違約金
			totaVo.putParam("L8r40GuarObliOther", ""); // 信用卡其他費用
			totaVo.putParam("L8r40OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}