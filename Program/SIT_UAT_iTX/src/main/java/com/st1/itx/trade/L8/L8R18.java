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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R18")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R18 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R18.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ048Service sJcicZ048Service;
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
		this.info("active L8R18 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號


		int iDcRcDate = Integer.parseInt(iRcDate) + 19110000;
		this.info("L8R18 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + RimSubmitKey + "]");
		// 查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ048Id JcicZ048IdVO = new JcicZ048Id();
		JcicZ048IdVO.setCustId(iCustId);
		JcicZ048IdVO.setRcDate(iDcRcDate);
		JcicZ048IdVO.setSubmitKey(RimSubmitKey);
		JcicZ048 JcicZ048VO = sJcicZ048Service.findById(JcicZ048IdVO, titaVo);
		if (JcicZ048VO != null) {
			totaVo.putParam("L8r18TranKey", jcicCom.changeTranKey(JcicZ048VO.getTranKey()));// [交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r18CustId", JcicZ048VO.getCustId());// 身分證字號
			totaVo.putParam("L8r18RcDate", jcicCom.DcToRoc(String.valueOf(JcicZ048VO.getRcDate()), 0));// 協商申請日
			totaVo.putParam("L8r18SubmitKey", JcicZ048VO.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r18OutJcicTxtDate", JcicZ048VO.getOutJcicTxtDate());// 轉出JCIC文字檔日期

			totaVo.putParam("L8r18SubmitKeyX", jcicCom.FinCodeName(JcicZ048VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			// 以上為KEY VALUE
			totaVo.putParam("L8r18CustRegAddr", JcicZ048VO.getCustRegAddr());// 債務人戶籍之郵遞區號及地址
			totaVo.putParam("L8r18CustComAddr", JcicZ048VO.getCustComAddr());// 債務人通訊地之郵遞區號及地址
			totaVo.putParam("L8r18CustRegTelNo", JcicZ048VO.getCustRegTelNo());// 債務人戶籍電話
			totaVo.putParam("L8r18CustComTelNo", JcicZ048VO.getCustComTelNo());// 債務人通訊電話
			totaVo.putParam("L8r18CustMobilNo", JcicZ048VO.getCustMobilNo());// 債務人行動電話
		} else {
			// 新增
			totaVo.putParam("L8r18TranKey", "A");// [交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r18CustId", iCustId);// 身分證字號
			totaVo.putParam("L8r18RcDate", iRcDate);// 協商申請日
			totaVo.putParam("L8r18SubmitKey", jcicCom.getPreSubmitKey());// 報送單位代號
			totaVo.putParam("L8r18OutJcicTxtDate", "");// 轉出JCIC文字檔日期

			// 以上為KEY VALUE
			totaVo.putParam("L8r18CustRegAddr", "");// 債務人戶籍之郵遞區號及地址
			totaVo.putParam("L8r18CustComAddr", "");// 債務人通訊地之郵遞區號及地址
			totaVo.putParam("L8r18CustRegTelNo", "");// 債務人戶籍電話
			totaVo.putParam("L8r18CustComTelNo", "");// 債務人通訊電話
			totaVo.putParam("L8r18CustMobilNo", "");// 債務人行動電話
			
			totaVo.putParam("L8r18SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號

			// throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r18");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}