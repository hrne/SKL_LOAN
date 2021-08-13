package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R31")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R31 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R31.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ062Service sJcicZ062Service;
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
		this.info("active L8R31 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iChangePayDate = titaVo.getParam("RimChangePayDate").trim();// 申請變更還款條件日

		int RcDate = Integer.parseInt(iRcDate) + 19110000;
		int ChangePayDate = Integer.parseInt(iChangePayDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		StringBuffer sbLogger = new StringBuffer();
		sbLogger.append("\r\n L8R31");
		sbLogger.append("iCustId=[" + iCustId + "]\r\n");
		sbLogger.append("iRcDate=[" + iRcDate + "]\r\n");
		sbLogger.append("RimSubmitKey=[" + RimSubmitKey + "]\r\n");
		sbLogger.append("iChangePayDate=[" + iChangePayDate + "]\r\n");
		this.info(sbLogger.toString());
		JcicZ062Id JcicZ062IdVO = new JcicZ062Id();
		JcicZ062IdVO.setChangePayDate(ChangePayDate);
		JcicZ062IdVO.setCustId(iCustId);
		JcicZ062IdVO.setRcDate(RcDate);
		JcicZ062IdVO.setSubmitKey(RimSubmitKey);
		JcicZ062 JcicZ062VO = sJcicZ062Service.findById(JcicZ062IdVO, titaVo);

		if (JcicZ062VO != null) {
			totaVo.putParam("L8r31TranKey", jcicCom.changeTranKey(JcicZ062VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r31CustId", JcicZ062VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r31SubmitKey", JcicZ062VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r31SubmitKeyX", jcicCom.FinCodeName(JcicZ062VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r31RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ062VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r31ChangePayDate", jcicCom.DcToRoc(String.valueOf(JcicZ062VO.getChangePayDate()), 0));// 申請變更還款條件日
			totaVo.putParam("L8r31CompletePeriod", JcicZ062VO.getCompletePeriod());// 變更還款條件已履約期數
			totaVo.putParam("L8r31Period", JcicZ062VO.getPeriod());// (第一階梯)期數
			totaVo.putParam("L8r31Rate", JcicZ062VO.getRate());// (第一階梯)利率
			totaVo.putParam("L8r31ExpBalanceAmt", JcicZ062VO.getExpBalanceAmt());// 信用貸款協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CashBalanceAmt", JcicZ062VO.getCashBalanceAmt());// 現金卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CreditBalanceAmt", JcicZ062VO.getCreditBalanceAmt());// 信用卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31ChaRepayAmt", JcicZ062VO.getChaRepayAmt());// 變更還款條件簽約總債務金額
			totaVo.putParam("L8r31ChaRepayAgreeDate", JcicZ062VO.getChaRepayAgreeDate());// 變更還款條件協議完成日
			totaVo.putParam("L8r31ChaRepayViewDate", JcicZ062VO.getChaRepayViewDate());// 變更還款條件面談日期
			totaVo.putParam("L8r31ChaRepayEndDate", JcicZ062VO.getChaRepayEndDate());// 變更還款條件簽約完成日期
			totaVo.putParam("L8r31ChaRepayFirstDate", JcicZ062VO.getChaRepayFirstDate());// 變更還款條件首期應繳款日
			totaVo.putParam("L8r31PayAccount", JcicZ062VO.getPayAccount());// 繳款帳號
			totaVo.putParam("L8r31PostAddr", JcicZ062VO.getPostAddr());// 最大債權金融機構聲請狀送達地址
			totaVo.putParam("L8r31MonthPayAmt", JcicZ062VO.getMonthPayAmt());// 月付金
			totaVo.putParam("L8r31GradeType", JcicZ062VO.getGradeType());// 屬階梯式還款註記
			totaVo.putParam("L8r31Period2", JcicZ062VO.getPeriod2());// 第二階梯期數
			totaVo.putParam("L8r31Rate2", JcicZ062VO.getRate2());// 第二階梯利率
			totaVo.putParam("L8r31MonthPayAmt2", JcicZ062VO.getMonthPayAmt2());// 第二階段月付金
			totaVo.putParam("L8r31OutJcicTxtDate", JcicZ062VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
		} else {
			// 新增
			totaVo.putParam("L8r31TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r31CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r31RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r31SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r31SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r31ChangePayDate", "");// 申請變更還款條件日

			totaVo.putParam("L8r31CompletePeriod", "");// 變更還款條件已履約期數
			totaVo.putParam("L8r31Period", "");// (第一階梯)期數
			totaVo.putParam("L8r31Rate", "");// (第一階梯)利率
			totaVo.putParam("L8r31ExpBalanceAmt", "");// 信用貸款協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CashBalanceAmt", "");// 現金卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CreditBalanceAmt", "");// 信用卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31ChaRepayAmt", "");// 變更還款條件簽約總債務金額
			totaVo.putParam("L8r31ChaRepayAgreeDate", "");// 變更還款條件協議完成日
			totaVo.putParam("L8r31ChaRepayViewDate", "");// 變更還款條件面談日期
			totaVo.putParam("L8r31ChaRepayEndDate", "");// 變更還款條件簽約完成日期
			totaVo.putParam("L8r31ChaRepayFirstDate", "");// 變更還款條件首期應繳款日
			totaVo.putParam("L8r31PayAccount", "");// 繳款帳號
			totaVo.putParam("L8r31PostAddr", "");// 最大債權金融機構聲請狀送達地址
			totaVo.putParam("L8r31MonthPayAmt", "");// 月付金
			totaVo.putParam("L8r31GradeType", "");// 屬階梯式還款註記
			totaVo.putParam("L8r31Period2", "");// 第二階梯期數
			totaVo.putParam("L8r31Rate2", "");// 第二階梯利率
			totaVo.putParam("L8r31MonthPayAmt2", "");// 第二階段月付金
			totaVo.putParam("L8r31OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r31");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}