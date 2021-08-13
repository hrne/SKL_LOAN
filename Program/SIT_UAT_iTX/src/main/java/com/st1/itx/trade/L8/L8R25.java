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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R25")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R25 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R25.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ053Service sJcicZ053Service;
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
		this.info("active L8R25 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String RimMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();// 最大債權金融機構代號
		
		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R25 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "],RimMaxMainCode=[" + RimMaxMainCode + "]");

		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ053Id JcicZ053IdVO = new JcicZ053Id();
		JcicZ053IdVO.setCustId(iCustId);
		JcicZ053IdVO.setSubmitKey(RimSubmitKey);
		JcicZ053IdVO.setMaxMainCode(RimMaxMainCode);
		JcicZ053IdVO.setRcDate(iDcRcDate);
		
		JcicZ053 JcicZ053VO = sJcicZ053Service.findById(JcicZ053IdVO, titaVo);
		if (JcicZ053VO != null) {

			totaVo.putParam("L8r25TranKey", jcicCom.changeTranKey(JcicZ053VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r25CustId", JcicZ053VO.getCustId());// 身分證字號
			totaVo.putParam("L8r25RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ053VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r25SubmitKey", JcicZ053VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r25OutJcicTxtDate", JcicZ053VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r25MaxMainCode", JcicZ053VO.getMaxMainCode());// 最大債權金融機構代號
			
			totaVo.putParam("L8r25SubmitKeyX", jcicCom.FinCodeName(JcicZ053VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r25MaxMainCodeX", jcicCom.FinCodeName(JcicZ053VO.getMaxMainCode(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE

			totaVo.putParam("L8r25AgreeSend", JcicZ053VO.getAgreeSend());// 是否同意報送例外處理檔案格式
			totaVo.putParam("L8r25AgreeSendData1", JcicZ053VO.getAgreeSendData1());// 同意補報送檔案格式資料別1
			totaVo.putParam("L8r25AgreeSendData2", JcicZ053VO.getAgreeSendData2());// 同意補報送檔案格式資料別2
			totaVo.putParam("L8r25ChangePayDate", JcicZ053VO.getChangePayDate());// 申請變更還款條件日
		} else {
			// 新增
			totaVo.putParam("L8r25TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r25CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r25RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r25SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r25OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r25MaxMainCode", jcicCom.getPreSubmitKey());// 最大債權金融機構代號
			totaVo.putParam("L8r25SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r25MaxMainCodeX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 最大債權金融機構代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r25AgreeSend", "");// 是否同意報送例外處理檔案格式
			totaVo.putParam("L8r25AgreeSendData1", "");// 同意補報送檔案格式資料別1
			totaVo.putParam("L8r25AgreeSendData2", "");// 同意補報送檔案格式資料別2
			totaVo.putParam("L8r25ChangePayDate", "");// 申請變更還款條件日
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r25");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}