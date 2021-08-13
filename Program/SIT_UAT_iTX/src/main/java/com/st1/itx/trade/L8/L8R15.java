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
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R15")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R15 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R15.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ045Service sJcicZ045Service;
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
		this.info("active L8R15 ");
		this.info("L8R15 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		String RimMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();// 最大債權金融機構代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R15 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "],RimMaxMainCode=[" + RimMaxMainCode + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ045Id JcicZ045IdVO = new JcicZ045Id();
		JcicZ045IdVO.setCustId(iCustId);
		JcicZ045IdVO.setMaxMainCode(RimMaxMainCode);
		JcicZ045IdVO.setRcDate(iDcRcDate);
		JcicZ045IdVO.setSubmitKey(RimSubmitKey);

		JcicZ045 JcicZ045VO = sJcicZ045Service.findById(JcicZ045IdVO, titaVo);
		if (JcicZ045VO != null) {
			totaVo.putParam("L8r15TranKey", jcicCom.changeTranKey(JcicZ045VO.getTranKey()));// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r15CustId", JcicZ045VO.getCustId());// 身分證字號
			totaVo.putParam("L8r15RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ045VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r15SubmitKey", JcicZ045VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r15OutJcicTxtDate", JcicZ045VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r15MaxMainCode", JcicZ045VO.getMaxMainCode());// 最大債權金融機構代號
			
			totaVo.putParam("L8r15SubmitKeyX", jcicCom.FinCodeName(JcicZ045VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r15MaxMainCodeX", jcicCom.FinCodeName(JcicZ045VO.getMaxMainCode(), 0, titaVo));// 最大債權金融機構代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r15AgreeCode", JcicZ045VO.getAgreeCode());// 是否同意債務清償方案
		} else {
			// 新增
			totaVo.putParam("L8r15TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r15CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r15RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r15SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r15OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r15MaxMainCode", jcicCom.getPreSubmitKey());// 最大債權金融機構代號
			
			totaVo.putParam("L8r15SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r15MaxMainCodeX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 最大債權金融機構代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r15AgreeCode", "Y");// 是否同意債務清償方案
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r15");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}