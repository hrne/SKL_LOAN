package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R44")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R44 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R44.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ447Service sJcicZ447Service;
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
		this.info("active L8r44 ");
		this.totaVo.init(titaVo);
		this.info("L8r44rimstart");
		String CustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String BankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ447Id lJcicZ447Id = new JcicZ447Id();
		lJcicZ447Id.setApplyDate(iApplyDate);
//		lJcicZ447Id.setBankId(BankId);
		lJcicZ447Id.setCustId(CustId);
		lJcicZ447Id.setSubmitKey(RimSubmitKey);
		JcicZ447 JcicZ447VO = sJcicZ447Service.findById(lJcicZ447Id, titaVo);
		if (JcicZ447VO != null) {
			this.info("L8R44update");
			totaVo.putParam("L8r44TranKey", jcicCom.changeTranKey(JcicZ447VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r44CustId", JcicZ447VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r44SubmitKey", JcicZ447VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r44SubmitKeyX", jcicCom.FinCodeName(JcicZ447VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r44ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ447VO.getApplyDate()), 0));// 款項統一收付申請日
//			totaVo.putParam("L8r44BankId", JcicZ447VO.getBankId());// 異動債權金機構代號
//			totaVo.putParam("L8r44BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ447VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r44Civil323Amt", JcicZ447VO.getCivil323Amt());// 依民法第323條計算之債務總金額
			totaVo.putParam("L8r44TotalAmt", JcicZ447VO.getTotalAmt());// 簽約總債務金額
			totaVo.putParam("L8r44SignDate", JcicZ447VO.getSignDate());// 簽約完成日期
			totaVo.putParam("L8r44FirstPayDate", JcicZ447VO.getFirstPayDate());// 首期應繳款日
			totaVo.putParam("L8r44Period", JcicZ447VO.getPeriod());// 期數
			totaVo.putParam("L8r44Rate", JcicZ447VO.getRate());// 利率
			totaVo.putParam("L8r44MonthPayAmt", JcicZ447VO.getMonthPayAmt());// 月付金
			totaVo.putParam("L8r44PayAccount", JcicZ447VO.getPayAccount());// 繳款帳號
			totaVo.putParam("L8r44OutJcicTxtDate", JcicZ447VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R44insert");
			totaVo.putParam("L8r44TranKey", "A");// 交易代碼
			totaVo.putParam("L8r44CustId", CustId);// 債務人IDN
			totaVo.putParam("L8r44SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r44SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r44ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r44BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r44BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r44Civil323Amt", "");// 依民法第323條計算之債務總金額
			totaVo.putParam("L8r44TotalAmt", "");// 簽約總債務金額
			totaVo.putParam("L8r44SignDate", "");// 簽約完成日期
			totaVo.putParam("L8r44FirstPayDate", "");// 首期應繳款日
			totaVo.putParam("L8r44Period", "");// 期數
			totaVo.putParam("L8r44Rate", "");// 利率
			totaVo.putParam("L8r44MonthPayAmt", "");// 月付金
			totaVo.putParam("L8r44PayAccount", "");// 繳款帳號
			totaVo.putParam("L8r44OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}