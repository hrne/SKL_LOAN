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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ454Id;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R48")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R48 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R48.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ454Service sJcicZ454Service;
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
		this.info("active L8r48 ");
		this.totaVo.init(titaVo);
		this.info("L8r48rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String iMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();
		int ApplyDate = Integer.parseInt(iApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ454Id lJcicZ454Id = new JcicZ454Id();
		lJcicZ454Id.setApplyDate(ApplyDate);
		lJcicZ454Id.setBankId(iBankId);
		lJcicZ454Id.setCustId(iCustId);
		lJcicZ454Id.setMaxMainCode(iMaxMainCode);
		lJcicZ454Id.setSubmitKey(RimSubmitKey);
		JcicZ454 JcicZ454VO = sJcicZ454Service.findById(lJcicZ454Id, titaVo);
		if (JcicZ454VO != null) {
			totaVo.putParam("L8r48TranKey", jcicCom.changeTranKey(JcicZ454VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r48CustId", JcicZ454VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r48SubmitKey", JcicZ454VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r48SubmitKeyX", jcicCom.FinCodeName(JcicZ454VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r48ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ454VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r48BankId", JcicZ454VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r48BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ454VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r48MaxMainCode", JcicZ454VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r48MaxMainCodeX", jcicCom.FinCodeName(JcicZ454VO.getMaxMainCode(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r48PayOffResult", JcicZ454VO.getPayOffResult());// 單獨全數受清償原因
			totaVo.putParam("L8r48PayOffDate", JcicZ454VO.getPayOffDate());// 單獨全數受清償日期
			totaVo.putParam("L8r48OutJcicTxtDate", JcicZ454VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R48insert");
			totaVo.putParam("L8r48TranKey", "A");// 交易代碼
			totaVo.putParam("L8r48CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r48SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r48SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r48ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r48BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r48BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r48MaxMainCode", "");// 最大債權金融機構代號
			totaVo.putParam("L8r48MaxMainCodeX", "");// 最大債權金融機構代號
			totaVo.putParam("L8r48PayOffResult", "");// 單獨全數受清償原因
			totaVo.putParam("L8r48PayOffDate", "");// 單獨全數受清償日期
			totaVo.putParam("L8r48OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}