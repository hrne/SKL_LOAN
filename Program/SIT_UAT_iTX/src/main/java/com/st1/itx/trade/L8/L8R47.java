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
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Id;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R47")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R47 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R47.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ451Service sJcicZ451Service;
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
		this.info("active L8r47 ");
		this.totaVo.init(titaVo);
		this.info("L8r47rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		String iDelayYM = titaVo.getParam("RimDelayYM").trim();// 延期繳款年月
		int ApplyDate = Integer.parseInt(iApplyDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ451Id lJcicZ451Id = new JcicZ451Id();
		lJcicZ451Id.setApplyDate(ApplyDate);
		lJcicZ451Id.setBankId(iBankId);
		lJcicZ451Id.setCustId(iCustId);
		lJcicZ451Id.setDelayYM(Integer.parseInt(jcicCom.RocTurnDc(iDelayYM,1)));
		lJcicZ451Id.setSubmitKey(RimSubmitKey);
		JcicZ451 JcicZ451VO = sJcicZ451Service.findById(lJcicZ451Id, titaVo);
		if (JcicZ451VO != null) {
			totaVo.putParam("L8r47TranKey", jcicCom.changeTranKey(JcicZ451VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r47CustId", JcicZ451VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r47SubmitKey", JcicZ451VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r47SubmitKeyX", jcicCom.FinCodeName(JcicZ451VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r47ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ451VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r47BankId", JcicZ451VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r47BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ451VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r47DelayYM", JcicZ451VO.getDelayYM());// 延期繳款年月
			totaVo.putParam("L8r47DelayCode", JcicZ451VO.getDelayCode());// 延期繳款原因
			totaVo.putParam("L8r47OutJcicTxtDate", JcicZ451VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R47insert");
			totaVo.putParam("L8r47TranKey", "A");// 交易代碼
			totaVo.putParam("L8r47CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r47SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r47SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r47ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r47BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r47BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r47DelayYM", "");// 延期繳款年月
			totaVo.putParam("L8r47DelayCode", "");// 延期繳款原因
			totaVo.putParam("L8r47OutJcicTxtDate", "");// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}