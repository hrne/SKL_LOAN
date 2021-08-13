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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R22")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R22 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R22.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ050Service sJcicZ050Service;
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
		this.info("active L8R22 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號
		
		String iPayDate = titaVo.getParam("RimPayDate").trim();//繳款日期
		
		int iDcRcDate=Integer.parseInt(iRcDate)+19110000;
		int iDcPayDate=Integer.parseInt(iPayDate)+19110000;
		this.info("L8R22 RimCustId=["+iCustId+"],iRcDate=["+iRcDate+"],iSubmitKey=["+RimSubmitKey+"],iDcPayDate=["+iDcPayDate+"]");
		//查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ050Id JcicZ050IdVO=new JcicZ050Id();
		JcicZ050IdVO.setCustId(iCustId);
		JcicZ050IdVO.setPayDate(iDcPayDate);
		JcicZ050IdVO.setRcDate(iDcRcDate);
		JcicZ050IdVO.setSubmitKey(RimSubmitKey);
		JcicZ050 JcicZ050VO=sJcicZ050Service.findById(JcicZ050IdVO,titaVo);
		if(JcicZ050VO!=null) {
			totaVo.putParam("L8r22TranKey", jcicCom.changeTranKey(JcicZ050VO.getTranKey()));//[交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r22CustId",JcicZ050VO.getCustId());//身分證字號
			totaVo.putParam("L8r22RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ050VO.getRcDate()), 0));//協商申請日
			totaVo.putParam("L8r22SubmitKey",JcicZ050VO.getSubmitKey());//報送單位代號
			totaVo.putParam("L8r22OutJcicTxtDate",JcicZ050VO.getOutJcicTxtDate());//轉出JCIC文字檔日期

			totaVo.putParam("L8r22PayDate",jcicCom.DcToRoc(String.valueOf(JcicZ050VO.getPayDate()), 0));//繳款日期
			totaVo.putParam("L8r22SubmitKeyX", jcicCom.FinCodeName(JcicZ050VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			//以上為KEY VALUE
			
			totaVo.putParam("L8r22PayAmt",JcicZ050VO.getPayAmt());//本次繳款金額
			totaVo.putParam("L8r22SumRepayActualAmt",JcicZ050VO.getSumRepayActualAmt());//累計實際還款金額
			totaVo.putParam("L8r22SumRepayShouldAmt",JcicZ050VO.getSumRepayShouldAmt());//累計應還款金額
			totaVo.putParam("L8r22Status",JcicZ050VO.getStatus());//債權結案註記
			totaVo.putParam("L8r22SecondRepayYM",jcicCom.DcToRoc(String.valueOf(JcicZ050VO.getSecondRepayYM()), 1));//進入第二階梯還款年月
		}else {
			//新增
			totaVo.putParam("L8r22TranKey", "A");//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r22CustId",iCustId);//身分證字號
			totaVo.putParam("L8r22RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r22SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r22OutJcicTxtDate","");//轉出JCIC文字檔日期
			
			totaVo.putParam("L8r22PayDate",iRcDate);//繳款日期
			totaVo.putParam("L8r22SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			//以上為KEY VALUE
			totaVo.putParam("L8r22PayAmt","");//本次繳款金額
			totaVo.putParam("L8r22SumRepayActualAmt","");//累計實際還款金額
			totaVo.putParam("L8r22SumRepayShouldAmt","");//累計應還款金額
			totaVo.putParam("L8r22Status","");//債權結案註記
			totaVo.putParam("L8r22SecondRepayYM","");//進入第二階梯還款年月
			//throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r22");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}