package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R41")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R41 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R41.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ443Service sJcicZ443Service;
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
		this.info("active L8r41 ");
		this.totaVo.init(titaVo);
		this.info("L8r41rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String BankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String MaxMainCode = titaVo.getParam("RimMaxMainCode").trim();
		String Account= titaVo.getParam("RimAccount").trim();
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ443Id lJcicZ443Id = new JcicZ443Id();
		lJcicZ443Id.setCustId(iCustId);
		lJcicZ443Id.setApplyDate(iApplyDate);
		lJcicZ443Id.setSubmitKey(RimSubmitKey);
//		lJcicZ443Id.setBankId(BankId);
		lJcicZ443Id.setMaxMainCode(MaxMainCode);
		lJcicZ443Id.setAccount(Account);
		JcicZ443 JcicZ443VO = sJcicZ443Service.findById(lJcicZ443Id, titaVo);
		if (JcicZ443VO != null) {
			totaVo.putParam("L8r41TranKey", jcicCom.changeTranKey(JcicZ443VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r41CustId", JcicZ443VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r41SubmitKey", JcicZ443VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r41SubmitKeyX", jcicCom.FinCodeName(JcicZ443VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r41ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ443VO.getApplyDate()), 0));// 款項統一收付申請日
//			totaVo.putParam("L8r41BankId", JcicZ443VO.getBankId());// 異動債權金機構代號
//			totaVo.putParam("L8r41BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ443VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r41MaxMainCode", JcicZ443VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r41MaxMainCodeX", jcicCom.FinCodeName(JcicZ443VO.getMaxMainCode(), 0, titaVo));// 最大債權金融機構代號
			totaVo.putParam("L8r41IsMaxMain", JcicZ443VO.getIsMaxMain());//
			totaVo.putParam("L8r41Account", JcicZ443VO.getAccount());// 帳號
			totaVo.putParam("L8r41GuarantyType", JcicZ443VO.getGuarantyType());// 擔保品類別
			totaVo.putParam("L8r41LoanAmt", JcicZ443VO.getLoanAmt());// 原借款金額
			totaVo.putParam("L8r41CreditAmt", JcicZ443VO.getCreditAmt());// 授信餘額
			totaVo.putParam("L8r41Principal", JcicZ443VO.getPrincipal());// 本金
			totaVo.putParam("L8r41Interest", JcicZ443VO.getInterest());// 利息
			totaVo.putParam("L8r41Penalty", JcicZ443VO.getPenalty());// 違約金
			totaVo.putParam("L8r41Other", JcicZ443VO.getOther());// 其他費用
			totaVo.putParam("L8r41TerminalPayAmt", JcicZ443VO.getTerminalPayAmt());// 每期應付金額
			totaVo.putParam("L8r41LatestPayAmt", JcicZ443VO.getLatestPayAmt());// 最近一期繳款金額
			totaVo.putParam("L8r41FinalPayDay", JcicZ443VO.getFinalPayDay());// 最後繳息日
			totaVo.putParam("L8r41NotyetacQuit", JcicZ443VO.getNotyetacQuit());// 已到期尚未清償金額
			totaVo.putParam("L8r41MothPayDay", JcicZ443VO.getMothPayDay());// 每月應還款日
			totaVo.putParam("L8r41BeginDate", jcicCom.DcToRoc(String.valueOf(JcicZ443VO.getBeginDate()), 1));// 契約起始年月
			totaVo.putParam("L8r41EndDate", jcicCom.DcToRoc(String.valueOf(JcicZ443VO.getEndDate()), 1));// 契約截止年月
			totaVo.putParam("L8r41OutJcicTxtDate", JcicZ443VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R41insert");
			totaVo.putParam("L8r41TranKey", "A");// 交易代碼
			totaVo.putParam("L8r41CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r41SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r41SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r41ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r41BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r41BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r41MaxMainCode", "");// 最大債權金融機構代號
			totaVo.putParam("L8r41MaxMainCodeX", "");// 最大債權金融機構代號
			totaVo.putParam("L8r41IsMaxMain", "");//
			totaVo.putParam("L8r41Account", "");// 帳號
			totaVo.putParam("L8r41GuarantyType", "");// 擔保品類別
			totaVo.putParam("L8r41LoanAmt", "");// 原借款金額
			totaVo.putParam("L8r41CreditAmt", "");// 授信餘額
			totaVo.putParam("L8r41Principal", "");// 本金
			totaVo.putParam("L8r41Interest", "");// 利息
			totaVo.putParam("L8r41Penalty", "");// 違約金
			totaVo.putParam("L8r41Other", "");// 其他費用
			totaVo.putParam("L8r41TerminalPayAmt", "");// 每期應付金額
			totaVo.putParam("L8r41LatestPayAmt", "");// 最近一期繳款金額
			totaVo.putParam("L8r41FinalPayDay", "");// 最後繳息日
			totaVo.putParam("L8r41NotyetacQuit", "");// 已到期尚未清償金額
			totaVo.putParam("L8r41MothPayDay", "");// 每月應還款日
			totaVo.putParam("L8r41BeginDate", "");// 契約起始年月
			totaVo.putParam("L8r41EndDate", "");// 契約截止年月
			totaVo.putParam("L8r41OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}