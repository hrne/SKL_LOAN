package com.st1.itx.trade.L8;

import java.util.ArrayList;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ044Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* DebtCode=X,2<br>
* NonGageAmt=9,9<br>
* Period=9,3<br>
* Rate=9,2.2<br>
* MonthPayAmt=9,9<br>
* ReceYearIncome=9,9<br>
* ReceYear=9,4<br>
* ReceYear2Income=9,9<br>
* ReceYear2=9,4<br>
* CurrentMonthIncome=9,9<br>
* LivingCost=9,9<br>
* CompName=X,40<br>
* CompId=9,8<br>
* CarCnt=9,2<br>
* HouseCnt=9,2<br>
* LandCnt=9,2<br>
* ChildCnt=9,2<br>
* ChildRate=9,4.1<br>
* ParentCnt=9,2<br>
* ParentRate=9,4.1<br>
* MouthCnt=9,2<br>
* MouthRate=9,4.1<br>
* GradeType=X,2<br>
* PayLastAmt=9,9<br>
* Period2=9,3<br>
* Rate2=9,2.2<br>
* MonthPayAmt2=9,9<br>
* PayLastAmt2=9,9<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8305")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8305 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8305.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public JcicCom jcicCom;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L8305");
		this.info("active L8305 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		String DebtCode=titaVo.getParam("DebtCode").trim();//負債主因
		String NonGageAmt=titaVo.getParam("NonGageAmt").trim();//無擔保金融債務協商總金額
		String Period=titaVo.getParam("Period").trim();//期數
		String Rate=titaVo.getParam("Rate").trim();//利率
		String MonthPayAmt=titaVo.getParam("MonthPayAmt").trim();//協商方案估計月付金
		String ReceYearIncome=titaVo.getParam("ReceYearIncome").trim();//最近年度綜合所得總額
		String ReceYear=titaVo.getParam("ReceYear").trim();//最近年度別
		String ReceYear2Income=titaVo.getParam("ReceYear2Income").trim();//前二年度綜合所得總額
		String ReceYear2=titaVo.getParam("ReceYear2").trim();//前二年度別
		String CurrentMonthIncome=titaVo.getParam("CurrentMonthIncome").trim();//目前每月收入
		String LivingCost=titaVo.getParam("LivingCost").trim();//生活支出總額
		String CompName=titaVo.getParam("CompName").trim();//目前主要所得來源公司名稱
		String CompId=titaVo.getParam("CompId").trim();//目前主要所得公司統編
		String CarCnt=titaVo.getParam("CarCnt").trim();//債務人名下車輛數量
		String HouseCnt=titaVo.getParam("HouseCnt").trim();//債務人名下建物筆數
		String LandCnt=titaVo.getParam("LandCnt").trim();//債務人名下土地筆數
		String ChildCnt=titaVo.getParam("ChildCnt").trim();//撫養子女數
		String ChildRate=titaVo.getParam("ChildRate").trim();//母養子女責任比率
		String ParentCnt=titaVo.getParam("ParentCnt").trim();//撫養父母人數
		String ParentRate=titaVo.getParam("ParentRate").trim();//撫養父母責任比率
		String MouthCnt=titaVo.getParam("MouthCnt").trim();//其他法定撫養人數
		String MouthRate=titaVo.getParam("MouthRate").trim();//其他法定撫養人之責任比率
		String GradeType=titaVo.getParam("GradeType").trim();//屬二階段還款方案之階段註記
		String PayLastAmt=titaVo.getParam("PayLastAmt").trim();//第一階段最後一期應繳金額
		String Period2=titaVo.getParam("Period2").trim();//第二段期數
		String Rate2=titaVo.getParam("Rate2").trim();//第二階段利率
		String MonthPayAmt2=titaVo.getParam("MonthPayAmt2").trim();//第二階段協商方案估計月付金
		String PayLastAmt2=titaVo.getParam("PayLastAmt2").trim();//第二階段最後一期應繳金額

		if(ReceYear2!=null && ReceYear2.length()!=0) {
			if(ReceYear2.length()==3) {
				ReceYear2=String.valueOf((Integer.parseInt(ReceYear2)+1911));
			}else {
				ReceYear2="";
			}
		}else {
			ReceYear2="";
		}
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ044 tJcicZ044 = new JcicZ044();
		JcicZ044Id tJcicZ044Id = new JcicZ044Id();

		tJcicZ044Id.setCustId(CustId);//債務人IDN
		tJcicZ044Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ044Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		
		tJcicZ044.setJcicZ044Id(tJcicZ044Id);

		tJcicZ044.setTranKey(TranKey);
		tJcicZ044.setDebtCode(DebtCode);//負債主因
		tJcicZ044.setNonGageAmt(parse.stringToInteger(NonGageAmt));//無擔保金融債務協商總金額
		tJcicZ044.setPeriod(parse.stringToInteger(Period));//期數
		tJcicZ044.setRate(parse.stringToBigDecimal(Rate));//利率
		tJcicZ044.setMonthPayAmt(parse.stringToInteger(MonthPayAmt));//協商方案估計月付金
		tJcicZ044.setReceYearIncome(parse.stringToInteger(ReceYearIncome));//最近年度綜合所得總額
		tJcicZ044.setReceYear(Integer.parseInt(jcicCom.RocTurnDc(ReceYear,3)));//最近年度別
		tJcicZ044.setReceYear2Income(parse.stringToInteger(ReceYear2Income));//前二年度綜合所得總額
		tJcicZ044.setReceYear2(Integer.parseInt(jcicCom.RocTurnDc(ReceYear2,3)));//前二年度別
		tJcicZ044.setCurrentMonthIncome(parse.stringToInteger(CurrentMonthIncome));//目前每月收入
		tJcicZ044.setLivingCost(parse.stringToInteger(LivingCost));//生活支出總額
		tJcicZ044.setCompName(CompName);//目前主要所得來源公司名稱
		tJcicZ044.setCompId(CompId);//目前主要所得公司統編
		tJcicZ044.setCarCnt(parse.stringToInteger(CarCnt));//債務人名下車輛數量
		tJcicZ044.setHouseCnt(parse.stringToInteger(HouseCnt));//債務人名下建物筆數
		tJcicZ044.setLandCnt(parse.stringToInteger(LandCnt));//債務人名下土地筆數
		tJcicZ044.setChildCnt(parse.stringToInteger(ChildCnt));//撫養子女數
		tJcicZ044.setChildRate(parse.stringToBigDecimal(ChildRate));//母養子女責任比率
		tJcicZ044.setParentCnt(parse.stringToInteger(ParentCnt));//撫養父母人數
		tJcicZ044.setParentRate(parse.stringToBigDecimal(ParentRate));//撫養父母責任比率
		tJcicZ044.setMouthCnt(parse.stringToInteger(MouthCnt));//其他法定撫養人數
		tJcicZ044.setMouthRate(parse.stringToBigDecimal(MouthRate));//其他法定撫養人之責任比率
		tJcicZ044.setGradeType(GradeType);//屬二階段還款方案之階段註記
		tJcicZ044.setPayLastAmt(parse.stringToInteger(PayLastAmt));//第一階段最後一期應繳金額
		tJcicZ044.setPeriod2(parse.stringToInteger(Period2));//第二段期數
		tJcicZ044.setRate2(parse.stringToBigDecimal(Rate2));//第二階段利率
		tJcicZ044.setMonthPayAmt2(parse.stringToInteger(MonthPayAmt2));//第二階段協商方案估計月付金
		tJcicZ044.setPayLastAmt2(parse.stringToInteger(PayLastAmt2));//第二階段最後一期應繳金額
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ044.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ044.setOutJcicTxtDate(0);
		}
		JcicZ044 tJcicZ044VO=sJcicZ044Service.holdById(tJcicZ044Id, titaVo);
		JcicZ044 OrgJcicZ044 = null;
		if(tJcicZ044VO!=null) {
			OrgJcicZ044 = (JcicZ044) dataLog.clone(tJcicZ044VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ044VO,tJcicZ044VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ044.getTranKey())) {
					
				}else {
					//刷主管卡後始可刪除
					// 交易需主管核可
					if (!titaVo.getHsupCode().equals("1")) {
						//titaVo.getSupCode();
						sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
				}
				//刪除
				try {
					sJcicZ044Service.delete(tJcicZ044VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ044.getJcicZ044Id().getCustId(),tJcicZ044.getJcicZ044Id().getRcDate(),tJcicZ044.getJcicZ044Id().getSubmitKey(), titaVo);//資料檢核
			jcicCom.checkJcicZ048(tJcicZ044.getJcicZ044Id().getCustId(),tJcicZ044.getJcicZ044Id().getRcDate(),tJcicZ044.getJcicZ044Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ044VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ044.setCreateDate(tJcicZ044VO.getCreateDate());
				tJcicZ044.setCreateEmpNo(tJcicZ044VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ044.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ044.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ044.setCreateDate(OrgJcicZ044.getCreateDate());
				tJcicZ044.setCreateEmpNo(OrgJcicZ044.getCreateEmpNo());
				try {
					tJcicZ044 = sJcicZ044Service.update2(tJcicZ044, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ044, tJcicZ044);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ044.setTranKey(TranKey);
				try {
					sJcicZ044Service.insert(tJcicZ044, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	
}