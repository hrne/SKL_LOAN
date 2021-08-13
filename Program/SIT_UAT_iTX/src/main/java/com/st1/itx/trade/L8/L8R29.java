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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R29")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R29 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R29.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ060Service sJcicZ060Service;
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
		this.info("active L8R29 ");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號
		String iChangePayDate=titaVo.getParam("RimChangePayDate").trim();//申請變更還款條件日
		
		int RcDate=Integer.parseInt(iRcDate)+19110000;
		int ChangePayDate=Integer.parseInt(iChangePayDate)+19110000;
		
		//查資料的時候尚未改變 要自己轉民國年與西元年
		StringBuffer sbLogger=new StringBuffer();
		sbLogger.append("\r\n L8R29");
		sbLogger.append("iCustId=["+iCustId+"]\r\n");
		sbLogger.append("iRcDate=["+iRcDate+"]\r\n");
		sbLogger.append("RimSubmitKey=["+RimSubmitKey+"]\r\n");
		sbLogger.append("iChangePayDate=["+iChangePayDate+"]\r\n");
		this.info(sbLogger.toString());
		JcicZ060Id JcicZ060IdVO=new JcicZ060Id();
		JcicZ060IdVO.setChangePayDate(ChangePayDate);
		JcicZ060IdVO.setCustId(iCustId);
		JcicZ060IdVO.setRcDate(RcDate);
		JcicZ060IdVO.setSubmitKey(RimSubmitKey);
		JcicZ060 JcicZ060VO=sJcicZ060Service.findById(JcicZ060IdVO,titaVo);
		if(JcicZ060VO!=null) {
			totaVo.putParam("L8r29TranKey", jcicCom.changeTranKey(JcicZ060VO.getTranKey()));//[交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r29CustId",JcicZ060VO.getCustId());//身分證字號
			totaVo.putParam("L8r29RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ060VO.getRcDate()), 0));//協商申請日
			totaVo.putParam("L8r29SubmitKey",JcicZ060VO.getSubmitKey());//報送單位代號
			totaVo.putParam("L8r29SubmitKeyX", jcicCom.FinCodeName(JcicZ060VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r29ChangePayDate",jcicCom.DcToRoc(String.valueOf(JcicZ060VO.getChangePayDate()), 0));//申請變更還款條件日
			totaVo.putParam("L8r29YM",jcicCom.DcToRoc(String.valueOf(JcicZ060VO.getYM()), 1));//已清分足月期付金年月
			totaVo.putParam("L8r29OutJcicTxtDate",JcicZ060VO.getOutJcicTxtDate());//轉出JCIC文字檔日期

			
		}else {
			//新增
			totaVo.putParam("L8r29TranKey", "A");//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r29CustId",iCustId);//身分證字號
			totaVo.putParam("L8r29RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r29SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r29SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r29ChangePayDate","");//申請變更還款條件日
			totaVo.putParam("L8r29OutJcicTxtDate","");//轉出JCIC文字檔日期

			//以上為KEY VALUE
			totaVo.putParam("L8r29YM","");//已清分足月期付金年月
			//throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r29");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}