package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R32")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R32 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R32.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ063Service sJcicZ063Service;
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
		this.info("active L8R32 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號
		String iChangePayDate = titaVo.getParam("RimChangePayDate").trim();// 申請變更還款條件日
		String iClosedDate = titaVo.getParam("RimClosedDate").trim();// 變更還款條件結案日期
		int RcDate = Integer.parseInt(iRcDate) + 19110000;
		int ChangePayDate = Integer.parseInt(iChangePayDate) + 19110000;
		int ClosedDate = Integer.parseInt(iClosedDate) + 19110000;
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		StringBuffer sbLogger = new StringBuffer();
		sbLogger.append("\r\n L8R32");
		sbLogger.append("iCustId=[" + iCustId + "]\r\n");
		sbLogger.append("iRcDate=[" + iRcDate + "]\r\n");
		sbLogger.append("RimSubmitKey=[" + RimSubmitKey + "]\r\n");
		sbLogger.append("iChangePayDate=[" + iChangePayDate + "]\r\n");
		sbLogger.append("iClosedDate=[" + iClosedDate + "]\r\n");
		this.info(sbLogger.toString());
		JcicZ063Id JcicZ063IdVO = new JcicZ063Id();
		JcicZ063IdVO.setChangePayDate(ChangePayDate);
//		JcicZ063IdVO.setClosedDate(ClosedDate);
		JcicZ063IdVO.setCustId(iCustId);
		JcicZ063IdVO.setRcDate(RcDate);
		JcicZ063IdVO.setSubmitKey(RimSubmitKey);
		JcicZ063 JcicZ063VO = sJcicZ063Service.findById(JcicZ063IdVO, titaVo);
		if (JcicZ063VO != null) {
			totaVo.putParam("L8r32TranKey", jcicCom.changeTranKey(JcicZ063VO.getTranKey()));// 交易代碼
			totaVo.putParam("L8r32CustId", JcicZ063VO.getCustId());// 債務人IDN
			totaVo.putParam("L8r32SubmitKey", JcicZ063VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r32SubmitKeyX", jcicCom.FinCodeName(JcicZ063VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r32RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ063VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r32ChangePayDate", jcicCom.DcToRoc(String.valueOf(JcicZ063VO.getChangePayDate()), 0));// 申請變更還款條件日
			totaVo.putParam("L8r32ClosedDate", jcicCom.DcToRoc(String.valueOf(JcicZ063VO.getClosedDate()), 0));// 變更還款條件結案日期
			totaVo.putParam("L8r32ClosedResult", JcicZ063VO.getClosedResult());// 結案原因
			totaVo.putParam("L8r32OutJcicTxtDate", JcicZ063VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期
		} else {
			// 新增
			totaVo.putParam("L8r32TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r32CustId", iCustId);// 債務人IDN
			totaVo.putParam("L8r32RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r32SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r32SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r32ChangePayDate", "");// 申請變更還款條件日
			totaVo.putParam("L8r32ClosedDate", "");// 變更還款條件結案日期
			totaVo.putParam("L8r32ClosedResult", "");// 結案原因
			totaVo.putParam("L8r32OutJcicTxtDate", "");// 轉出JCIC文字檔日期
			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r32");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}