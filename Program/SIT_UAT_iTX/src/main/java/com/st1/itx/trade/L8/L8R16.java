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
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R16")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R16 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R16.class);

	/* DB服務注入 */
	@Autowired
	public JcicZ046Service sJcicZ046Service;
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
		this.info("active L8R16 ");
		this.info("L8R16 titaVo=[" + titaVo + "]");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		String RimCloseDate = titaVo.getParam("RimCloseDate").trim();// 結案日期

		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		int iCloseDate = Integer.parseInt(RimCloseDate) + 19110000;
		this.info("L8R16 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "],iCloseDate=[" + iCloseDate + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ046Id JcicZ046IdVO = new JcicZ046Id();
		JcicZ046IdVO.setCloseDate(iCloseDate);
		JcicZ046IdVO.setCustId(iCustId);
		JcicZ046IdVO.setRcDate(iDcRcDate);
		JcicZ046IdVO.setSubmitKey(RimSubmitKey);
		JcicZ046 JcicZ046VO = sJcicZ046Service.findById(JcicZ046IdVO, titaVo);
		if (JcicZ046VO != null) {
			totaVo.putParam("L8r16TranKey", jcicCom.changeTranKey(JcicZ046VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除]
			totaVo.putParam("L8r16CustId", JcicZ046VO.getCustId());// 身分證字號
			totaVo.putParam("L8r16RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ046VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r16SubmitKey", JcicZ046VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r16OutJcicTxtDate", JcicZ046VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
			totaVo.putParam("L8r16CloseDate", jcicCom.DcToRoc(String.valueOf(JcicZ046VO.getCloseDate()), 0));// 結案日期
			// 以上為KEY VALUE
			totaVo.putParam("L8r16CloseCode", JcicZ046VO.getCloseCode());// 結案原因代號
			totaVo.putParam("L8r16BreakCode", JcicZ046VO.getBreakCode());// 毀諾原因代號
			
			totaVo.putParam("L8r16SubmitKeyX", jcicCom.FinCodeName(JcicZ046VO.getSubmitKey(), 0, titaVo));// 報送單位代號
		} else {
			// 新增
			totaVo.putParam("L8r16TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r16CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r16RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r16SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r16OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			totaVo.putParam("L8r16CloseDate", iRcDate);// 結案日期

			// 以上為KEY VALUE
			totaVo.putParam("L8r16CloseCode", "  ");// 結案原因代號
			totaVo.putParam("L8r16BreakCode", "  ");// 毀諾原因代號
			
			totaVo.putParam("L8r16SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r16");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}