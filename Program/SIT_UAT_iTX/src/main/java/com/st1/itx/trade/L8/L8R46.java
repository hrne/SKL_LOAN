package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R46")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R46 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R46.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ450Service sJcicZ450Service;
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
		this.info("active L8r46 ");
		this.totaVo.init(titaVo);
		this.info("L8r46rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String iPayDate = titaVo.getParam("RimPayDate").trim();
		int ApplyDate = Integer.parseInt(iApplyDate) + 19110000;
		int PayDate = Integer.parseInt(iPayDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ450Id lJcicZ450Id = new JcicZ450Id();
		lJcicZ450Id.setApplyDate(ApplyDate);
//		lJcicZ450Id.setBankId(iBankId);
		lJcicZ450Id.setCustId(iCustId);
		lJcicZ450Id.setPayDate(PayDate);
		lJcicZ450Id.setSubmitKey(RimSubmitKey);
		JcicZ450 JcicZ450VO = sJcicZ450Service.findById(lJcicZ450Id, titaVo);

		if (JcicZ450VO != null) {
			totaVo.putParam("L8r46TranKey", jcicCom.changeTranKey(JcicZ450VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r46CustId", JcicZ450VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r46SubmitKey", JcicZ450VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r46SubmitKeyX", jcicCom.FinCodeName(JcicZ450VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r46ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ450VO.getApplyDate()), 0));// 款項統一收付申請日
//			totaVo.putParam("L8r46BankId", JcicZ450VO.getBankId());// 異動債權金機構代號
//			totaVo.putParam("L8r46BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ450VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r46PayDate", jcicCom.DcToRoc(String.valueOf(JcicZ450VO.getPayDate()), 0));// 繳款日期
			totaVo.putParam("L8r46PayAmt", JcicZ450VO.getPayAmt());// 本次繳款金額
			totaVo.putParam("L8r46SumRepayActualAmt", JcicZ450VO.getSumRepayActualAmt());// 累計實際還款金額
			totaVo.putParam("L8r46SumRepayShouldAmt", JcicZ450VO.getSumRepayShouldAmt());// 截至目前累計應還款金額
			totaVo.putParam("L8r46PayStatus", JcicZ450VO.getPayStatus());// 債權結案註記
			totaVo.putParam("L8r46OutJcicTxtDate", JcicZ450VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R46insert");
			totaVo.putParam("L8r46TranKey", "A");// 交易代碼
			totaVo.putParam("L8r46CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r46SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r46SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r46ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r46BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r46BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r46PayDate", "");// 繳款日期
			totaVo.putParam("L8r46PayAmt", "");// 本次繳款金額
			totaVo.putParam("L8r46SumRepayActualAmt", "");// 累計實際還款金額
			totaVo.putParam("L8r46SumRepayShouldAmt", "");// 截至目前累計應還款金額
			totaVo.putParam("L8r46PayStatus", "");// 債權結案註記
			totaVo.putParam("L8r46OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}