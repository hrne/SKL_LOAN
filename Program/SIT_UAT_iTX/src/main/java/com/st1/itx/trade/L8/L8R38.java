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
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R38")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R38 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R38.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ575Service sJcicZ575Service;
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
		this.info("active L8r38 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ575Id JcicZ575IdVo = new JcicZ575Id();
		JcicZ575IdVo.setCustId(iCustId);
		JcicZ575IdVo.setApplyDate(iApplyDate);
		JcicZ575IdVo.setSubmitKey(RimSubmitKey);
		JcicZ575IdVo.setBankId(iBankId);
		this.info("L8R38 JcicZ575IdVo="+JcicZ575IdVo.toString());
		JcicZ575 JcicZ575VO = sJcicZ575Service.findById(JcicZ575IdVo, titaVo);
		if (JcicZ575VO != null) {
			totaVo.putParam("L8r38TranKey", jcicCom.changeTranKey(JcicZ575VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r38CustId", JcicZ575VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r38SubmitKey", JcicZ575VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r38SubmitKeyX", jcicCom.FinCodeName(JcicZ575VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r38ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ575VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r38BankId", JcicZ575VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r38BankIdX", jcicCom.FinCodeName(JcicZ575VO.getBankId(), 0, titaVo));// 異動債權金機構代號
			totaVo.putParam("L8r38ModifyType", JcicZ575VO.getModifyType());// 債權異動類別
			totaVo.putParam("L8r38OutJcicTxtDate", JcicZ575VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			totaVo.putParam("L8r38TranKey", "A");// 交易代碼
			totaVo.putParam("L8r38CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r38SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r38SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r38ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r38BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r38BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r38ModifyType", "");// 債權異動類別
			totaVo.putParam("L8r38OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}