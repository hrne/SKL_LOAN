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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R30")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R30 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R30.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ061Service sJcicZ061Service;
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
		this.info("active L8R30 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iChangePayDate = titaVo.getParam("RimChangePayDate").trim();// 申請變更還款條件日
		
		int RcDate = Integer.parseInt(iRcDate) + 19110000;
		int ChangePayDate = Integer.parseInt(iChangePayDate) + 19110000;

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		StringBuffer sbLogger = new StringBuffer();
		sbLogger.append("\r\n L8R30");
		sbLogger.append("iCustId=[" + iCustId + "]\r\n");
		sbLogger.append("iRcDate=[" + iRcDate + "]\r\n");
		sbLogger.append("RimSubmitKey=[" + RimSubmitKey + "]\r\n");
		sbLogger.append("iChangePayDate=[" + iChangePayDate + "]\r\n");
		this.info(sbLogger.toString());
		JcicZ061Id JcicZ061IdVO = new JcicZ061Id();
		JcicZ061IdVO.setChangePayDate(ChangePayDate);
		JcicZ061IdVO.setCustId(iCustId);
		JcicZ061IdVO.setRcDate(RcDate);
		JcicZ061IdVO.setSubmitKey(RimSubmitKey);
		JcicZ061 JcicZ061VO = sJcicZ061Service.findById(JcicZ061IdVO, titaVo);
		if (JcicZ061VO != null) {
			totaVo.putParam("L8r30TranKey", jcicCom.changeTranKey(JcicZ061VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r30CustId", JcicZ061VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r30SubmitKey", JcicZ061VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r30SubmitKeyX", jcicCom.FinCodeName(JcicZ061VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r30RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ061VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r30ChangePayDate", jcicCom.DcToRoc(String.valueOf(JcicZ061VO.getChangePayDate()), 0));// 申請變更還款條件日
			totaVo.putParam("L8r30MaxMainCode", JcicZ061VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r30MaxMainCodeX", jcicCom.FinCodeName(JcicZ061VO.getMaxMainCode(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r30ExpBalanceAmt", JcicZ061VO.getExpBalanceAmt());// 信用貸款協商剩餘債權餘額
			totaVo.putParam("L8r30CashBalanceAmt", JcicZ061VO.getCashBalanceAmt());// 現金卡協商剩餘債權餘額
			totaVo.putParam("L8r30CreditBalanceAmt", JcicZ061VO.getCreditBalanceAmt());// 信用卡協商剩餘債權餘額
			totaVo.putParam("L8r30MaxMainNote", JcicZ061VO.getMaxMainNote());// 最大債權金融機構報送註記
			totaVo.putParam("L8r30IsGuarantor", JcicZ061VO.getIsGuarantor());// 是否有保證人
			totaVo.putParam("L8r30IsChangePayment", JcicZ061VO.getIsChangePayment());// 是否同意債務人申請變更還款條件方案
			totaVo.putParam("L8r30OutJcicTxtDate", JcicZ061VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
		} else {
			// 新增
			totaVo.putParam("L8r30TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r30CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r30RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r30SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r30SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r30ChangePayDate", "");// 申請變更還款條件日

			totaVo.putParam("L8r30MaxMainCode",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r30MaxMainCodeX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r30ExpBalanceAmt", "");// 信用貸款協商剩餘債權餘額
			totaVo.putParam("L8r30CashBalanceAmt", "");// 現金卡協商剩餘債權餘額
			totaVo.putParam("L8r30CreditBalanceAmt", "");// 信用卡協商剩餘債權餘額
			totaVo.putParam("L8r30MaxMainNote", "");// 最大債權金融機構報送註記
			totaVo.putParam("L8r30IsGuarantor", "");// 是否有保證人
			totaVo.putParam("L8r30IsChangePayment", "");// 是否同意債務人申請變更還款條件方案
			totaVo.putParam("L8r30OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r30");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}