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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R23")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R23 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R23.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ051Service sJcicZ051Service;
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
		this.info("active L8R23 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		String iDelayYM = titaVo.getParam("RimDelayYM").trim();// 延期繳款年月


		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;

		this.info("L8R23 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "],iDelayYM=[" + iDelayYM + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ051Id JcicZ051IdVO = new JcicZ051Id();
		JcicZ051IdVO.setCustId(iCustId);
		JcicZ051IdVO.setDelayYM(Integer.parseInt(jcicCom.RocTurnDc(iDelayYM,1)));
		JcicZ051IdVO.setRcDate(iDcRcDate);
		JcicZ051IdVO.setSubmitKey(RimSubmitKey);
		JcicZ051 JcicZ051VO = sJcicZ051Service.findById(JcicZ051IdVO, titaVo);
		if (JcicZ051VO != null) {
			totaVo.putParam("L8r23TranKey", jcicCom.changeTranKey(JcicZ051VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r23CustId", JcicZ051VO.getCustId());// 身分證字號
			totaVo.putParam("L8r23RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ051VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r23SubmitKey", JcicZ051VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r23OutJcicTxtDate", JcicZ051VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			//totaVo.putParam("L8r23DelayYM", jcicCom.DcToRoc(String.valueOf(JcicZ051VO.getDelayYM()), 1));// 延期繳款年月
			totaVo.putParam("L8r23DelayYM", JcicZ051VO.getDelayYM());// 延期繳款年月
			
			totaVo.putParam("L8r23SubmitKeyX", jcicCom.FinCodeName(JcicZ051VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE

			totaVo.putParam("L8r23DelayCode", JcicZ051VO.getDelayCode());// 延期繳款原因
			totaVo.putParam("L8r23DelayDesc", JcicZ051VO.getDelayDesc());// 延期繳款案情說明
		} else {
			// 新增
			totaVo.putParam("L8r23TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r23CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r23RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r23SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r23OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r23DelayYM", "");// 延期繳款年月
			totaVo.putParam("L8r23SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r23DelayCode", "");// 延期繳款原因
			totaVo.putParam("L8r23DelayDesc", "");// 延期繳款案情說明
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r23");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}