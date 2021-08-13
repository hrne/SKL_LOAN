package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ054Id;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R26")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R26 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R26.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ054Service sJcicZ054Service;
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
		this.info("active L8R26 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String RimMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();// 最大債權金融機構代號

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R26 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "],RimMaxMainCode=[" + RimMaxMainCode + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ054Id JcicZ054IdVO = new JcicZ054Id();
		JcicZ054IdVO.setCustId(iCustId);
		JcicZ054IdVO.setMaxMainCode(RimMaxMainCode);
		JcicZ054IdVO.setRcDate(iDcRcDate);
		JcicZ054IdVO.setSubmitKey(RimSubmitKey);
		JcicZ054 JcicZ054VO = sJcicZ054Service.findById(JcicZ054IdVO, titaVo);
		if (JcicZ054VO != null) {
			totaVo.putParam("L8r26TranKey", jcicCom.changeTranKey(JcicZ054VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r26CustId", JcicZ054VO.getCustId());// 身分證字號
			totaVo.putParam("L8r26RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ054VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r26SubmitKey", JcicZ054VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r26OutJcicTxtDate", JcicZ054VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r26MaxMainCode", JcicZ054VO.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r26SubmitKeyX", jcicCom.FinCodeName(JcicZ054VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r26MaxMainCodeX", jcicCom.FinCodeName(JcicZ054VO.getMaxMainCode(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE

			totaVo.putParam("L8r26PayOffResult", JcicZ054VO.getPayOffResult());// 單獨全數受清償原因
			totaVo.putParam("L8r26PayOffDate", JcicZ054VO.getPayOffDate());// 單獨全數受清償日期
		} else {
			// 新增
			totaVo.putParam("L8r26TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r26CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r26RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r26SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r26OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r26MaxMainCode", "");// 最大債權金融機構代號
			totaVo.putParam("L8r26SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r26MaxMainCodeX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 最大債權金融機構代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r26PayOffResult", "");// 單獨全數受清償原因
			totaVo.putParam("L8r26PayOffDate", "");// 單獨全數受清償日期

			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r26");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}