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
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R45")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R45 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R45.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ448Service sJcicZ448Service;
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
		this.info("active L8r45 ");
		this.totaVo.init(titaVo);
		this.info("L8r45rimstart");
		String CustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String BankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String MaxMainCode = titaVo.getParam("RimMaxMainCode").trim();
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年

		JcicZ448Id lJcicZ448Id = new JcicZ448Id();
		lJcicZ448Id.setApplyDate(iApplyDate);
		lJcicZ448Id.setBankId(BankId);
		lJcicZ448Id.setCustId(CustId);
		lJcicZ448Id.setMaxMainCode(MaxMainCode);
		lJcicZ448Id.setSubmitKey(RimSubmitKey);
		JcicZ448 JcicZ448VO = sJcicZ448Service.findById(lJcicZ448Id, titaVo);
		if (JcicZ448VO != null) {
			totaVo.putParam("L8r45TranKey", jcicCom.changeTranKey(JcicZ448VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r45CustId", JcicZ448VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r45SubmitKey", JcicZ448VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r45SubmitKeyX", jcicCom.FinCodeName(JcicZ448VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r45ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ448VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r45BankId", JcicZ448VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r45BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ448VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r45MaxMainCode", JcicZ448VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r45MaxMainCodeX", jcicCom.FinCodeName(JcicZ448VO.getMaxMainCode(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r45SignPrin", JcicZ448VO.getSignPrin());// 簽約金額-本金
			totaVo.putParam("L8r45SignOther", JcicZ448VO.getSignOther());// 簽約金額-利息、違約金及其他費用
			totaVo.putParam("L8r45OwnPercentage", JcicZ448VO.getOwnPercentage());// 債權比例
			totaVo.putParam("L8r45AcQuitAmt", JcicZ448VO.getAcQuitAmt());// 每月清償金額
			totaVo.putParam("L8r45OutJcicTxtDate", JcicZ448VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R45insert");
			totaVo.putParam("L8r45TranKey", "A");// 交易代碼
			totaVo.putParam("L8r45CustId", CustId);// 債務人IDN
			totaVo.putParam("L8r45SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r45SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r45ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r45BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r45BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r45MaxMainCode", "");// 最大債權金融機構代號
			totaVo.putParam("L8r45MaxMainCodeX", "");// 最大債權金融機構代號
			totaVo.putParam("L8r45SignPrin", "");// 簽約金額-本金
			totaVo.putParam("L8r45SignOther", "");// 簽約金額-利息、違約金及其他費用
			totaVo.putParam("L8r45OwnPercentage", "");// 債權比例
			totaVo.putParam("L8r45AcQuitAmt", "");// 每月清償金額
			totaVo.putParam("L8r45OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}