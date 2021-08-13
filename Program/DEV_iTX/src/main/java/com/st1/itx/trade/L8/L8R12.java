package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R12")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R12 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R12.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ042Service sJcicZ042Service;
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
		this.info("active L8R12 ");
		this.info("L8R12 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R12 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ042Id JcicZ042IdVO = new JcicZ042Id();
		JcicZ042IdVO.setCustId(iCustId);
		JcicZ042IdVO.setRcDate(iDcRcDate);
		JcicZ042IdVO.setSubmitKey(RimSubmitKey);
		JcicZ042 JcicZ042VO = sJcicZ042Service.findById(JcicZ042IdVO, titaVo);
		if (JcicZ042VO != null) {
			totaVo.putParam("L8r12TranKey", jcicCom.changeTranKey(JcicZ042VO.getTranKey()));// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r12CustId", JcicZ042VO.getCustId());// 身分證字號
			totaVo.putParam("L8r12RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ042VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r12SubmitKey", JcicZ042VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r12OutJcicTxtDate", JcicZ042VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r12MaxMainCode", JcicZ042VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r12IsClaims", JcicZ042VO.getIsClaims());// 是否為本金融機構債務人
			totaVo.putParam("L8r12GuarLoanCnt", JcicZ042VO.getGuarLoanCnt());// 本金融機構有擔保債權筆數
			totaVo.putParam("L8r12ExpLoanAmt", JcicZ042VO.getExpLoanAmt());// 信用貸款對內本息餘額
			totaVo.putParam("L8r12Civil323ExpAmt", JcicZ042VO.getCivil323ExpAmt());// 依民法第323條計算之信用貸款本息餘額
			totaVo.putParam("L8r12ReceExpAmt", JcicZ042VO.getReceExpAmt());// 信用貸款最近一期繳款金額
			totaVo.putParam("L8r12CashCardAmt", JcicZ042VO.getCashCardAmt());// 現金卡放款對內本息餘額
			totaVo.putParam("L8r12Civil323CashAmt", JcicZ042VO.getCivil323CashAmt());// 依民法第323條計算之現金卡放款本息餘額
			totaVo.putParam("L8r12ReceCashAmt", JcicZ042VO.getReceCashAmt());// 現金卡最近一期繳款金額
			totaVo.putParam("L8r12CreditCardAmt", JcicZ042VO.getCreditCardAmt());// 信用卡對內本息餘額
			totaVo.putParam("L8r12Civil323CreditAmt", JcicZ042VO.getCivil323CreditAmt());// 依民法第323條計算之信用卡本息餘額
			totaVo.putParam("L8r12ReceCreditAmt", JcicZ042VO.getReceCreditAmt());// 信用卡最近一期繳款金額
			totaVo.putParam("L8r12ReceExpPrin", JcicZ042VO.getReceExpPrin());// 信用貸款本金
			totaVo.putParam("L8r12ReceExpInte", JcicZ042VO.getReceExpInte());// 信用貸款利息
			totaVo.putParam("L8r12ReceExpPena", JcicZ042VO.getReceExpPena());// 信用貸款違約金
			totaVo.putParam("L8r12ReceExpOther", JcicZ042VO.getReceExpOther());// 信用貸款其他費用
			totaVo.putParam("L8r12CashCardPrin", JcicZ042VO.getCashCardPrin());// 現金卡本金
			totaVo.putParam("L8r12CashCardInte", JcicZ042VO.getCashCardInte());// 信金卡利息
			totaVo.putParam("L8r12CashCardPena", JcicZ042VO.getCashCardPena());// 信金卡違約金
			totaVo.putParam("L8r12CashCardOther", JcicZ042VO.getCashCardOther());// 現金卡其他費用
			totaVo.putParam("L8r12CreditCardPrin", JcicZ042VO.getCreditCardPrin());// 信用卡本金
			totaVo.putParam("L8r12CreditCardInte", JcicZ042VO.getCreditCardInte());// 信用卡利息
			totaVo.putParam("L8r12CreditCardPena", JcicZ042VO.getCreditCardPena());// 信用卡違約金
			totaVo.putParam("L8r12CreditCardOther", JcicZ042VO.getCreditCardOther());// 信用卡其他費用
			
			totaVo.putParam("L8r12SubmitKeyX", jcicCom.FinCodeName(JcicZ042VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r12MaxMainCodeX", jcicCom.FinCodeName(JcicZ042VO.getMaxMainCode(), 0, titaVo));// 最大債權金融機構代號
		} else {
			// 新增
			totaVo.putParam("L8r12TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r12CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r12RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r12SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r14SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r12OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r12MaxMainCode", "");// 最大債權金融機構代號
			totaVo.putParam("L8r12IsClaims", "N");// 是否為本金融機構債務人
			totaVo.putParam("L8r12GuarLoanCnt", "0");// 本金融機構有擔保債權筆數
			totaVo.putParam("L8r12ExpLoanAmt", "0");// 信用貸款對內本息餘額
			totaVo.putParam("L8r12Civil323ExpAmt", "0");// 依民法第323條計算之信用貸款本息餘額
			totaVo.putParam("L8r12ReceExpAmt", "0");// 信用貸款最近一期繳款金額
			totaVo.putParam("L8r12CashCardAmt", "0");// 現金卡放款對內本息餘額
			totaVo.putParam("L8r12Civil323CashAmt", "0");// 依民法第323條計算之現金卡放款本息餘額
			totaVo.putParam("L8r12ReceCashAmt", "0");// 現金卡最近一期繳款金額
			totaVo.putParam("L8r12CreditCardAmt", "0");// 信用卡對內本息餘額
			totaVo.putParam("L8r12Civil323CreditAmt", "0");// 依民法第323條計算之信用卡本息餘額
			totaVo.putParam("L8r12ReceCreditAmt", "0");// 信用卡最近一期繳款金額
			totaVo.putParam("L8r12ReceExpPrin", "0");// 信用貸款本金
			totaVo.putParam("L8r12ReceExpInte", "0");// 信用貸款利息
			totaVo.putParam("L8r12ReceExpPena", "0");// 信用貸款違約金
			totaVo.putParam("L8r12ReceExpOther", "0");// 信用貸款其他費用
			totaVo.putParam("L8r12CashCardPrin", "0");// 現金卡本金
			totaVo.putParam("L8r12CashCardInte", "0");// 信金卡利息
			totaVo.putParam("L8r12CashCardPena", "0");// 信金卡違約金
			totaVo.putParam("L8r12CashCardOther", "0");// 現金卡其他費用
			totaVo.putParam("L8r12CreditCardPrin", "0");// 信用卡本金
			totaVo.putParam("L8r12CreditCardInte", "0");// 信用卡利息
			totaVo.putParam("L8r12CreditCardPena", "0");// 信用卡違約金
			totaVo.putParam("L8r12CreditCardOther", "0");// 信用卡其他費用
			
			totaVo.putParam("L8r12SubmitKeyX", "");// 報送單位代號
			totaVo.putParam("L8r12MaxMainCodeX", "");// 最大
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r12");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}