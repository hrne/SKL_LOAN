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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R14")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R14 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R14.class);
	
	/* DB服務注入 */
	@Autowired
	public JcicZ044Service sJcicZ044Service;
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
		this.info("active L8R14 ");
		this.info("L8R14 titaVo=["+titaVo+"]");
		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();//身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();//協商申請日
		String RimSubmitKey = titaVo.getParam("RimSubmitKey").trim();//報送單位代號
		

		int iDcRcDate=Integer.parseInt(iRcDate)+19110000;
		this.info("L8R14 RimCustId=["+iCustId+"],iRcDate=["+iRcDate+"],iSubmitKey=["+RimSubmitKey+"]");
		//查資料的時候尚未改變 要自己轉民國年與西元年
		JcicZ044Id JcicZ044IdVO=new JcicZ044Id();
		JcicZ044IdVO.setCustId(iCustId);
		JcicZ044IdVO.setRcDate(iDcRcDate);
		JcicZ044IdVO.setSubmitKey(RimSubmitKey);
		JcicZ044 JcicZ044VO=sJcicZ044Service.findById(JcicZ044IdVO,titaVo);
		if(JcicZ044VO!=null) {
			totaVo.putParam("L8r14TranKey", jcicCom.changeTranKey(JcicZ044VO.getTranKey()));//[交易代碼 A:新增,C:異動,D:刪除,X:補件]
			totaVo.putParam("L8r14CustId",JcicZ044VO.getCustId());//身分證字號
			totaVo.putParam("L8r14RcDate",jcicCom.DcToRoc(String.valueOf(JcicZ044VO.getRcDate()), 0));//協商申請日
			totaVo.putParam("L8r14SubmitKey",JcicZ044VO.getSubmitKey());//報送單位代號
			totaVo.putParam("L8r14OutJcicTxtDate",JcicZ044VO.getOutJcicTxtDate());//轉出JCIC文字檔日期
			
			totaVo.putParam("L8r14SubmitKeyX", jcicCom.FinCodeName(JcicZ044VO.getSubmitKey(), 0, titaVo));// 報送單位代號
			//以上為KEY VALUE
			totaVo.putParam("L8r14DebtCode",JcicZ044VO.getDebtCode());//負債主因
			totaVo.putParam("L8r14NonGageAmt",JcicZ044VO.getNonGageAmt());//無擔保金融債務協商總金額
			totaVo.putParam("L8r14Period",JcicZ044VO.getPeriod());//期數
			totaVo.putParam("L8r14Rate",JcicZ044VO.getRate());//利率
			totaVo.putParam("L8r14MonthPayAmt",JcicZ044VO.getMonthPayAmt());//協商方案估計月付金
			totaVo.putParam("L8r14ReceYearIncome",JcicZ044VO.getReceYearIncome());//最近年度綜合所得總額

			totaVo.putParam("L8r14ReceYear",jcicCom.DcToRoc(String.valueOf(JcicZ044VO.getReceYear()),3));//最近年度別
			totaVo.putParam("L8r14ReceYear2Income",JcicZ044VO.getReceYear2Income());//前二年度綜合所得總額

			totaVo.putParam("L8r14ReceYear2",jcicCom.DcToRoc(String.valueOf(JcicZ044VO.getReceYear2()),3));//前二年度別
			totaVo.putParam("L8r14CurrentMonthIncome",JcicZ044VO.getCurrentMonthIncome());//目前每月收入
			totaVo.putParam("L8r14LivingCost",JcicZ044VO.getLivingCost());//生活支出總額
			totaVo.putParam("L8r14CompName",JcicZ044VO.getCompName());//目前主要所得來源公司名稱
			totaVo.putParam("L8r14CompId",JcicZ044VO.getCompId());//目前主要所得公司統編
			totaVo.putParam("L8r14CarCnt",JcicZ044VO.getCarCnt());//債務人名下車輛數量
			totaVo.putParam("L8r14HouseCnt",JcicZ044VO.getHouseCnt());//債務人名下建物筆數
			totaVo.putParam("L8r14LandCnt",JcicZ044VO.getLandCnt());//債務人名下土地筆數
			totaVo.putParam("L8r14ChildCnt",JcicZ044VO.getChildCnt());//撫養子女數
			totaVo.putParam("L8r14ChildRate",JcicZ044VO.getChildRate());//母養子女責任比率
			totaVo.putParam("L8r14ParentCnt",JcicZ044VO.getParentCnt());//撫養父母人數
			totaVo.putParam("L8r14ParentRate",JcicZ044VO.getParentRate());//撫養父母責任比率
			totaVo.putParam("L8r14MouthCnt",JcicZ044VO.getMouthCnt());//其他法定撫養人數
			totaVo.putParam("L8r14MouthRate",JcicZ044VO.getMouthRate());//其他法定撫養人之責任比率
			totaVo.putParam("L8r14GradeType",JcicZ044VO.getGradeType());//屬二階段還款方案之階段註記
			totaVo.putParam("L8r14PayLastAmt",JcicZ044VO.getPayLastAmt());//第一階段最後一期應繳金額
			totaVo.putParam("L8r14Period2",JcicZ044VO.getPeriod2());//第二段期數
			totaVo.putParam("L8r14Rate2",JcicZ044VO.getRate2());//第二階段利率
			totaVo.putParam("L8r14MonthPayAmt2",JcicZ044VO.getMonthPayAmt2());//第二階段協商方案估計月付金
			totaVo.putParam("L8r14PayLastAmt2",JcicZ044VO.getPayLastAmt2());//第二階段最後一期應繳金額
			
		}else {
			//新增
			totaVo.putParam("L8r14TranKey", "A");//[交易代碼 A:新增,C:異動]
			totaVo.putParam("L8r14CustId",iCustId);//身分證字號
			totaVo.putParam("L8r14RcDate",iRcDate);//協商申請日
			totaVo.putParam("L8r14SubmitKey",jcicCom.getPreSubmitKey());//報送單位代號
			totaVo.putParam("L8r14SubmitKeyX", jcicCom.FinCodeName(jcicCom.getPreSubmitKey(), 0, titaVo));// 報送單位代號
			totaVo.putParam("L8r14OutJcicTxtDate","");//轉出JCIC文字檔日期
			//以上為KEY VALUE
			totaVo.putParam("L8r14DebtCode","");//負債主因
			totaVo.putParam("L8r14NonGageAmt","");//無擔保金融債務協商總金額
			totaVo.putParam("L8r14Period","");//期數
			totaVo.putParam("L8r14Rate","");//利率
			totaVo.putParam("L8r14MonthPayAmt","");//協商方案估計月付金
			totaVo.putParam("L8r14ReceYearIncome","");//最近年度綜合所得總額
			totaVo.putParam("L8r14ReceYear","");//最近年度別
			totaVo.putParam("L8r14ReceYear2Income","");//前二年度綜合所得總額
			totaVo.putParam("L8r14ReceYear2","");//前二年度別
			totaVo.putParam("L8r14CurrentMonthIncome","");//目前每月收入
			totaVo.putParam("L8r14LivingCost","");//生活支出總額
			totaVo.putParam("L8r14CompName","");//目前主要所得來源公司名稱
			totaVo.putParam("L8r14CompId","");//目前主要所得公司統編
			totaVo.putParam("L8r14CarCnt","");//債務人名下車輛數量
			totaVo.putParam("L8r14HouseCnt","");//債務人名下建物筆數
			totaVo.putParam("L8r14LandCnt","");//債務人名下土地筆數
			totaVo.putParam("L8r14ChildCnt","");//撫養子女數
			totaVo.putParam("L8r14ChildRate","");//母養子女責任比率
			totaVo.putParam("L8r14ParentCnt","");//撫養父母人數
			totaVo.putParam("L8r14ParentRate","");//撫養父母責任比率
			totaVo.putParam("L8r14MouthCnt","");//其他法定撫養人數
			totaVo.putParam("L8r14MouthRate","");//其他法定撫養人之責任比率
			totaVo.putParam("L8r14GradeType","");//屬二階段還款方案之階段註記
			totaVo.putParam("L8r14PayLastAmt","");//第一階段最後一期應繳金額
			totaVo.putParam("L8r14Period2","");//第二段期數
			totaVo.putParam("L8r14Rate2","");//第二階段利率
			totaVo.putParam("L8r14MonthPayAmt2","");//第二階段協商方案估計月付金
			totaVo.putParam("L8r14PayLastAmt2","");//第二階段最後一期應繳金額
			
			
			//throw new LogicException(titaVo, "XXXXX", "資料庫抓取有誤L8r14");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}