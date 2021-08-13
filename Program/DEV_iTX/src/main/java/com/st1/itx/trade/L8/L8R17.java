package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R17")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R17 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R17.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;

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
		this.info("active L8R17 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R17 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ047Id JcicZ047IdVO = new JcicZ047Id();
		JcicZ047IdVO.setCustId(iCustId);
		JcicZ047IdVO.setRcDate(iDcRcDate);
		JcicZ047IdVO.setSubmitKey(RimSubmitKey);
		JcicZ047 JcicZ047VO = sJcicZ047Service.findById(JcicZ047IdVO, titaVo);
		if (JcicZ047VO != null) {
			totaVo.putParam("L8r17TranKey", jcicCom.changeTranKey(JcicZ047VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除]
			totaVo.putParam("L8r17CustId", JcicZ047VO.getCustId());// 身分證字號
			totaVo.putParam("L8r17RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ047VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r17SubmitKey", JcicZ047VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r17OutJcicTxtDate", JcicZ047VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r17SubmitKeyX", jcicCom.FinCodeName(JcicZ047VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r17Period", JcicZ047VO.getPeriod());// 期數
			totaVo.putParam("L8r17Rate", JcicZ047VO.getRate());// 利率
			totaVo.putParam("L8r17ExpLoanAmt", JcicZ047VO.getExpLoanAmt());// 信用貸款債務簽約總金額
			totaVo.putParam("L8r17Civil323ExpAmt", JcicZ047VO.getCivil323ExpAmt());// 依民法第323條計算之信用貸款債務總金額
			totaVo.putParam("L8r17CashCardAmt", JcicZ047VO.getCashCardAmt());// 現金卡債務簽約總金額
			totaVo.putParam("L8r17Civil323CashAmt", JcicZ047VO.getCivil323CashAmt());// 依民法第323條計算之現金卡債務總金額
			totaVo.putParam("L8r17CreditCardAmt", JcicZ047VO.getCreditCardAmt());// 信用卡債務簽約總金額
			totaVo.putParam("L8r17Civil323CreditAmt", JcicZ047VO.getCivil323CreditAmt());// 依民法第323條計算之信用卡債務總金額
			totaVo.putParam("L8r17TotalAmt", JcicZ047VO.getTotalAmt());// 簽約總債務金額
			totaVo.putParam("L8r17Civil323Amt", JcicZ047VO.getCivil323Amt());// 依民法第323條計算之債務總金額
			totaVo.putParam("L8r17InterviewDate", JcicZ047VO.getInterviewDate());// 面談日期
			totaVo.putParam("L8r17PassDate", JcicZ047VO.getPassDate());// 協議完成日
			totaVo.putParam("L8r17LimitDate", JcicZ047VO.getLimitDate());// 前置協商註記訊息揭露期限
			totaVo.putParam("L8r17SignDate", JcicZ047VO.getSignDate());// 簽約完成日期
			totaVo.putParam("L8r17MonthPayAmt", JcicZ047VO.getMonthPayAmt());// 月付金
			totaVo.putParam("L8r17FirstPayDate", JcicZ047VO.getFirstPayDate());// 首期應繳款日
			totaVo.putParam("L8r17GradeType", JcicZ047VO.getGradeType());// 屬二階段還款方案之階段註記
			totaVo.putParam("L8r17PayLastAmt", JcicZ047VO.getPayLastAmt());// 第一階段最後一期應繳金額
			totaVo.putParam("L8r17Period2", JcicZ047VO.getPeriod2());// 第二段期數
			totaVo.putParam("L8r17Rate2", JcicZ047VO.getRate2());// 第二階段利率
			totaVo.putParam("L8r17MonthPayAmt2", JcicZ047VO.getMonthPayAmt2());// 第二階段協商方案估計月付金
			totaVo.putParam("L8r17PayLastAmt2", JcicZ047VO.getPayLastAmt2());// 第二階段最後一期應繳金額
			totaVo.putParam("L8r17PayAccount", JcicZ047VO.getPayAccount());// 繳款帳號
			totaVo.putParam("L8r17PostAddr", JcicZ047VO.getPostAddr());// 最大債權金融機構聲請狀送達地址
		} else {
			// 新增
			totaVo.putParam("L8r17TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r17CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r17RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r17SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r17OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			// 以上為KEY VALUE
			totaVo.putParam("L8r17Period", "");// 期數
			totaVo.putParam("L8r17Rate", "");// 利率
			totaVo.putParam("L8r17ExpLoanAmt", "");// 信用貸款債務簽約總金額
			totaVo.putParam("L8r17Civil323ExpAmt", "");// 依民法第323條計算之信用貸款債務總金額
			totaVo.putParam("L8r17CashCardAmt", "");// 現金卡債務簽約總金額
			totaVo.putParam("L8r17Civil323CashAmt", "");// 依民法第323條計算之現金卡債務總金額
			totaVo.putParam("L8r17CreditCardAmt", "");// 信用卡債務簽約總金額
			totaVo.putParam("L8r17Civil323CreditAmt", "");// 依民法第323條計算之信用卡債務總金額
			totaVo.putParam("L8r17TotalAmt", "");// 簽約總債務金額
			totaVo.putParam("L8r17Civil323Amt", "");// 依民法第323條計算之債務總金額
			totaVo.putParam("L8r17InterviewDate", "");// 面談日期
			totaVo.putParam("L8r17PassDate", "");// 協議完成日
			totaVo.putParam("L8r17LimitDate", "");// 前置協商註記訊息揭露期限
			totaVo.putParam("L8r17SignDate", "");// 簽約完成日期
			totaVo.putParam("L8r17MonthPayAmt", "");// 月付金
			totaVo.putParam("L8r17FirstPayDate", "");// 首期應繳款日
			totaVo.putParam("L8r17GradeType", "");// 屬二階段還款方案之階段註記
			totaVo.putParam("L8r17PayLastAmt", "");// 第一階段最後一期應繳金額
			totaVo.putParam("L8r17Period2", "");// 第二段期數
			totaVo.putParam("L8r17Rate2", "");// 第二階段利率
			totaVo.putParam("L8r17MonthPayAmt2", "");// 第二階段協商方案估計月付金
			totaVo.putParam("L8r17PayLastAmt2", "");// 第二階段最後一期應繳金額
			totaVo.putParam("L8r17PayAccount", "");// 繳款帳號
			totaVo.putParam("L8r17PostAddr", "");// 最大債權金融機構聲請狀送達地址
			
			totaVo.putParam("L8r17SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號

			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r17");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}