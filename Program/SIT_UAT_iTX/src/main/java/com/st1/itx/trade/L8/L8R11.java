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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R11")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R11 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R11.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ041Service sJcicZ041Service;

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
		this.info("Run L8R11");
		this.info("active L8R11 ");

		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R11 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "]");
		JcicZ041Id JcicZ041IdVO = new JcicZ041Id();
		JcicZ041IdVO.setCustId(iCustId);
		JcicZ041IdVO.setRcDate(iDcRcDate);
		JcicZ041IdVO.setSubmitKey(RimSubmitKey);
		JcicZ041 JcicZ041VO = sJcicZ041Service.findById(JcicZ041IdVO, titaVo);
		if (JcicZ041VO != null) {
			String SubmitKeyX=jcicCom.FinCodeName(JcicZ041VO.getSubmitKey(), 0, titaVo);
			totaVo.putParam("L8r11TranKey",jcicCom.changeTranKey(JcicZ041VO.getTranKey()));// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r11CustId", JcicZ041VO.getCustId());// 身分證字號
			totaVo.putParam("L8r11RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ041VO.getRcDate()), 0));// 協商申請日
			
			totaVo.putParam("L8r11SubmitKey", JcicZ041VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r11SubmitKeyX", SubmitKeyX);// 報送單位代號名稱
			totaVo.putParam("L8r11OutJcicTxtDate", JcicZ041VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
			totaVo.putParam("L8r11ScDate", JcicZ041VO.getScDate());// 停催日期
			totaVo.putParam("L8r11NegoStartDate", JcicZ041VO.getNegoStartDate());// 協商開始日
			totaVo.putParam("L8r11NonFinClaimAmt", JcicZ041VO.getNonFinClaimAmt());// 非金融機構債權金額
		} else {
			// 新增
			totaVo.putParam("L8r11TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r11CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r11RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r11SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r11SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號名稱
			totaVo.putParam("L8r11OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r11ScDate", "");// 停催日期
			totaVo.putParam("L8r11NegoStartDate", "");// 協商開始日
			totaVo.putParam("L8r11NonFinClaimAmt", "");// 非金融機構債權金額
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}