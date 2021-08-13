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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Id;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R42")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R42 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R42.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ444Service sJcicZ444Service;
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
		this.info("active L8r42 ");
		this.totaVo.init(titaVo);
		this.info("L8r42rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("RimApplyDate").trim();
		String BankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		int iApplyDate = Integer.parseInt(ApplyDate) + 19110000;
		
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ444Id lJcicZ444Id = new JcicZ444Id();
		lJcicZ444Id.setCustId(iCustId);
		lJcicZ444Id.setApplyDate(iApplyDate);
		lJcicZ444Id.setSubmitKey(RimSubmitKey);
		lJcicZ444Id.setBankId(BankId);
		JcicZ444 JcicZ444VO = sJcicZ444Service.findById(lJcicZ444Id, titaVo);
		if (JcicZ444VO != null) {
			totaVo.putParam("L8r42TranKey", jcicCom.changeTranKey(JcicZ444VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r42CustId", JcicZ444VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r42SubmitKey", JcicZ444VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r42SubmitKeyX", jcicCom.FinCodeName(JcicZ444VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r42ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ444VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r42BankId", JcicZ444VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r42BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ444VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r42CustRegAddr", JcicZ444VO.getCustRegAddr());// 債務人戶籍之郵遞區號及地址
			totaVo.putParam("L8r42CustComAddr", JcicZ444VO.getCustComAddr());// 債務人通訊地之郵遞區號及地址
			totaVo.putParam("L8r42CustRegTelNo", JcicZ444VO.getCustRegTelNo());// 債務人戶籍電話
			totaVo.putParam("L8r42CustComTelNo", JcicZ444VO.getCustComTelNo());// 債務人通訊電話
			totaVo.putParam("L8r42CustMobilNo", JcicZ444VO.getCustMobilNo());// 債務人行動電話
			totaVo.putParam("L8r42OutJcicTxtDate", JcicZ444VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R42insert");
			totaVo.putParam("L8r42TranKey", "A");// 交易代碼
			totaVo.putParam("L8r42CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r42SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r42SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r42ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r42BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r42BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r42CustRegAddr", "");// 債務人戶籍之郵遞區號及地址
			totaVo.putParam("L8r42CustComAddr", "");// 債務人通訊地之郵遞區號及地址
			totaVo.putParam("L8r42CustRegTelNo", "");// 債務人戶籍電話
			totaVo.putParam("L8r42CustComTelNo", "");// 債務人通訊電話
			totaVo.putParam("L8r42CustMobilNo", "");// 債務人行動電話
			totaVo.putParam("L8r42OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}