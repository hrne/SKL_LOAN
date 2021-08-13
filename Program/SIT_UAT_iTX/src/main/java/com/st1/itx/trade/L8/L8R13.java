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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R13")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R13 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R13.class);
	
	/* DB服務注入 */
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public JcicCom jcicCom;
	
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R13 ");
		this.info("L8R13 titaVo=["+titaVo+"]");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號
		
		String RimMaxMainCode = titaVo.getParam("RimMaxMainCode").trim();//最大債權金融機構代號
		String RimAccount= titaVo.getParam("RimAccount").trim();//帳號
		int iDcRcDate=Integer.parseInt(iRcDate)+19110000;
		this.info("L8R13 RimCustId=["+iCustId+"],iRcDate=["+iRcDate+"],iSubmitKey=["+RimSubmitKey+"],RimMaxMainCode=["+RimMaxMainCode+"]");
		//查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ043Id JcicZ043IdVO=new JcicZ043Id();
		JcicZ043IdVO.setCustId(iCustId);
		JcicZ043IdVO.setMaxMainCode(RimMaxMainCode);
		JcicZ043IdVO.setRcDate(iDcRcDate);
		JcicZ043IdVO.setSubmitKey(RimSubmitKey);
		JcicZ043IdVO.setAccount(RimAccount);
		JcicZ043 JcicZ043VO=sJcicZ043Service.findById(JcicZ043IdVO,titaVo);
		
		if(JcicZ043VO!=null){
			totaVo.putParam("L8r13TranKey",jcicCom.changeTranKey(JcicZ043VO.getTranKey()));//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r13CustId",JcicZ043VO.getCustId());//身分證字號
			totaVo.putParam("L8r13RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ043VO.getRcDate()), 0));//協商申請日
			totaVo.putParam("L8r13SubmitKey",JcicZ043VO.getSubmitKey());//報送單位代號
			totaVo.putParam("L8r13OutJcicTxtDate",JcicZ043VO.getOutJcicTxtDate());//轉出JCIC文字檔日期
			
			totaVo.putParam("L8r13MaxMainCode",JcicZ043VO.getMaxMainCode());//最大債權金融機構代號
			totaVo.putParam("L8r13Account",JcicZ043VO.getAccount());//帳戶
			
			totaVo.putParam("L8r13SubmitKeyX", jcicCom.FinCodeName(JcicZ043VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r13MaxMainCodeX", jcicCom.FinCodeName(JcicZ043VO.getMaxMainCode(), 0, titaVo));// 最大債權金融機構代號
			//以上為KEY VALUE
			totaVo.putParam("L8r13CollateralType",JcicZ043VO.getCollateralType());//擔保品類別
			totaVo.putParam("L8r13OriginLoanAmt",JcicZ043VO.getOriginLoanAmt());//原借款金額
			totaVo.putParam("L8r13CreditBalance",JcicZ043VO.getCreditBalance());//授信餘額
			totaVo.putParam("L8r13PerPeriordAmt",JcicZ043VO.getPerPeriordAmt());//每期應付金額
			totaVo.putParam("L8r13LastPayAmt",JcicZ043VO.getLastPayAmt());//最近一起繳款金額
			totaVo.putParam("L8r13LastPayDate",JcicZ043VO.getLastPayDate());//最後繳息日
			totaVo.putParam("L8r13OutstandAmt",JcicZ043VO.getOutstandAmt());//已到期尚未償還金額
			totaVo.putParam("L8r13RepayPerMonDay",JcicZ043VO.getRepayPerMonDay());//每月應還款日
			totaVo.putParam("L8r13ContractStartYM",jcicCom.DcToRoc(String.valueOf(JcicZ043VO.getContractStartYM()), 1));//契約起始年月
			totaVo.putParam("L8r13ContractEndYM",jcicCom.DcToRoc(String.valueOf(JcicZ043VO.getContractEndYM()), 1));//契約截止年月
		}else {
			//新增
			totaVo.putParam("L8r13TranKey", "A");//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r13CustId",iCustId);//身分證字號
			totaVo.putParam("L8r13RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r13SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r13SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r13OutJcicTxtDate","");//轉出JCIC文字檔日期
			
			totaVo.putParam("L8r13MaxMainCode","");//最大債權金融機構代號
			totaVo.putParam("L8r13Account","");//帳戶
			
			
			totaVo.putParam("L8r13MaxMainCodeX", "");// 最大債權金融機構代號
			//以上為KEY VALUE
			totaVo.putParam("L8r13CollateralType","");//擔保品類別
			totaVo.putParam("L8r13OriginLoanAmt","");//原借款金額
			totaVo.putParam("L8r13CreditBalance","");//授信餘額
			totaVo.putParam("L8r13PerPeriordAmt","");//每期應付金額
			totaVo.putParam("L8r13LastPayAmt","");//最近一起繳款金額
			totaVo.putParam("L8r13LastPayDate","");//最後繳息日
			totaVo.putParam("L8r13OutstandAmt","");//已到期尚未償還金額
			totaVo.putParam("L8r13RepayPerMonDay","");//每月應還款日
			totaVo.putParam("L8r13ContractStartYM","");//契約起始年月
			totaVo.putParam("L8r13ContractEndYM","");//契約截止年月
			//throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r13");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}