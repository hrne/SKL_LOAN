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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R43")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8R43 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R43.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ446Service sJcicZ446Service;
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
		this.info("active L8r43 ");
		this.totaVo.init(titaVo);
		this.info("L8r43rimstart");
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iApplyDate = titaVo.getParam("RimApplyDate").trim();
		String iBankId = titaVo.getParam("RimBankId").trim(); // 異動債權金機構代號
		int ApplyDate = Integer.parseInt(iApplyDate) + 19110000;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		// 查資料的時候尚未改變 要自己轉民國年與西元年

		JcicZ446Id lJcicZ446Id = new JcicZ446Id();
		lJcicZ446Id.setApplyDate(ApplyDate);
		lJcicZ446Id.setBankId(iBankId);
		lJcicZ446Id.setCustId(iCustId);
		lJcicZ446Id.setSubmitKey(RimSubmitKey);
		JcicZ446 JcicZ446VO = sJcicZ446Service.findById(lJcicZ446Id, titaVo);
		if (JcicZ446VO != null) {
			totaVo.putParam("L8r43TranKey", jcicCom.changeTranKey(JcicZ446VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r43CustId", JcicZ446VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r43SubmitKey", JcicZ446VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r43SubmitKeyX", jcicCom.FinCodeName(JcicZ446VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r43ApplyDate", jcicCom.DcToRoc(String.valueOf(JcicZ446VO.getApplyDate()), 0));// 款項統一收付申請日
			totaVo.putParam("L8r43BankId", JcicZ446VO.getBankId());// 異動債權金機構代號
			totaVo.putParam("L8r43BankIdX", jcicCom.JcicCourtCodeAndZipCode(JcicZ446VO.getBankId(),titaVo));// 受理調解機構代號
			totaVo.putParam("L8r43CloseCode", JcicZ446VO.getCloseCode());// 結案原因代號
			totaVo.putParam("L8r43CloseDate", JcicZ446VO.getCloseDate());// 結案日期
			totaVo.putParam("L8r43OutJcicTxtDate", JcicZ446VO.getOutJcicTxtDate());// 轉JCIC文字檔日期
		} else {
			// 新增
			this.info("L8R43insert");
			totaVo.putParam("L8r43TranKey", "A");// 交易代碼
			totaVo.putParam("L8r43CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r43SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r43SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r43ApplyDate", "");// 款項統一收付申請日
			totaVo.putParam("L8r43BankId", "");// 異動債權金機構代號
			totaVo.putParam("L8r43BankIdX", "");// 異動債權金機構代號
			totaVo.putParam("L8r43CloseCode", "");// 結案原因代號
			totaVo.putParam("L8r43CloseDate", "");// 結案日期
			totaVo.putParam("L8r43OutJcicTxtDate", "");// 轉JCIC文字檔日期

		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}