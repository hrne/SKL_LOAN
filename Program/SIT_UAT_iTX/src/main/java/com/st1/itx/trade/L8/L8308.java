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
import com.st1.itx.db.domain.JcicZ044;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ047Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* TranKey=X,3<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* Period=9,3<br>
* Rate=9,2.2<br>
* ExpLoanAmt=9,9<br>
* Civil323ExpAmt=9,9<br>
* CashCardAmt=9,9<br>
* Civil323CashAmt=9,9<br>
* CreditCardAmt=9,9<br>
* Civil323CreditAmt=9,9<br>
* TotalAmt=9,9<br>
* Civil323Amt=9,9<br>
* InterviewDate=9,7<br>
* PassDate=9,7<br>
* LimitDate=9,7<br>
* SignDate=9,7<br>
* MonthPayAmt=9,9<br>
* FirstPayDate=9,7<br>
* GradeType=X,2<br>
* PayLastAmt=9,9<br>
* Period2=9,3<br>
* Rate2=9,2.2<br>
* MonthPayAmt2=9,9<br>
* PayLastAmt2=9,9<br>
* PayAccount=X,80<br>
* PostAddr=X,150<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8308")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8308 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8308.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	
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
		this.info("Run L8308");
		this.info("active L8308 ");
		this.totaVo.init(titaVo);
		
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ047 tJcicZ047 = new JcicZ047();
		JcicZ047Id tJcicZ047Id = new JcicZ047Id();
		tJcicZ047Id.setCustId(CustId);//債務人IDN
		tJcicZ047Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ047Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		
		tJcicZ047.setJcicZ047Id(tJcicZ047Id);

		tJcicZ047.setTranKey(TranKey);
		tJcicZ047.setPeriod(parse.stringToInteger(titaVo.getParam("Period").trim()));//期數
		tJcicZ047.setRate(parse.stringToBigDecimal(titaVo.getParam("Rate").trim()));//利率
		tJcicZ047.setExpLoanAmt(parse.stringToInteger(titaVo.getParam("ExpLoanAmt").trim()));//信用貸款債務簽約總金額
		tJcicZ047.setCivil323ExpAmt(parse.stringToInteger(titaVo.getParam("Civil323ExpAmt").trim()));//依民法第323條計算之信用貸款債務總金額
		tJcicZ047.setCashCardAmt(parse.stringToInteger(titaVo.getParam("CashCardAmt").trim()));//現金卡債務簽約總金額
		tJcicZ047.setCivil323CashAmt(parse.stringToInteger(titaVo.getParam("Civil323CashAmt").trim()));//依民法第323條計算之現金卡債務總金額
		tJcicZ047.setCreditCardAmt(parse.stringToInteger(titaVo.getParam("CreditCardAmt").trim()));//信用卡債務簽約總金額
		tJcicZ047.setCivil323CreditAmt(parse.stringToInteger(titaVo.getParam("Civil323CreditAmt").trim()));//依民法第323條計算之信用卡債務總金額
		tJcicZ047.setTotalAmt(parse.stringToBigDecimal(titaVo.getParam("TotalAmt").trim()));//簽約總債務金額
		tJcicZ047.setCivil323Amt(parse.stringToBigDecimal(titaVo.getParam("Civil323Amt").trim()));//依民法第323條計算之債務總金額
		tJcicZ047.setInterviewDate(parse.stringToInteger(titaVo.getParam("InterviewDate").trim()));//面談日期
		tJcicZ047.setPassDate(parse.stringToInteger(titaVo.getParam("PassDate").trim()));//協議完成日
		tJcicZ047.setLimitDate(parse.stringToInteger(titaVo.getParam("LimitDate").trim()));//前置協商註記訊息揭露期限
		tJcicZ047.setSignDate(parse.stringToInteger(titaVo.getParam("SignDate").trim()));//簽約完成日期
		tJcicZ047.setMonthPayAmt(parse.stringToInteger(titaVo.getParam("MonthPayAmt").trim()));//月付金
		tJcicZ047.setFirstPayDate(parse.stringToInteger(titaVo.getParam("FirstPayDate").trim()));//首期應繳款日
		tJcicZ047.setGradeType(titaVo.getParam("GradeType").trim());//屬二階段還款方案之階段註記
		tJcicZ047.setPayLastAmt(parse.stringToInteger(titaVo.getParam("PayLastAmt").trim()));//第一階段最後一期應繳金額
		tJcicZ047.setPeriod2(parse.stringToInteger(titaVo.getParam("Period2").trim()));//第二段期數
		tJcicZ047.setRate2(parse.stringToBigDecimal(titaVo.getParam("Rate2").trim()));//第二階段利率
		tJcicZ047.setMonthPayAmt2(parse.stringToInteger(titaVo.getParam("MonthPayAmt2").trim()));//第二階段協商方案估計月付金
		tJcicZ047.setPayLastAmt2(parse.stringToInteger(titaVo.getParam("PayLastAmt2").trim()));//第二階段最後一期應繳金額
		tJcicZ047.setPayAccount(titaVo.getParam("PayAccount").trim());//繳款帳號
		tJcicZ047.setPostAddr(titaVo.getParam("PostAddr").trim());//最大債權金融機構聲請狀送達地址

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ047.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ047.setOutJcicTxtDate(0);
		}
		JcicZ047 tJcicZ047VO=sJcicZ047Service.holdById(tJcicZ047Id, titaVo);
		JcicZ047 OrgJcicZ047 = null;
		if(tJcicZ047VO!=null) {
			OrgJcicZ047 = (JcicZ047) dataLog.clone(tJcicZ047VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ047VO,tJcicZ047VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ047.getTranKey())) {
					
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
					sJcicZ047Service.delete(tJcicZ047VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			JcicZ044 JcicZ044Vo=jcicCom.checkJcicZ044(tJcicZ047.getJcicZ047Id().getCustId(),tJcicZ047.getJcicZ047Id().getRcDate(),tJcicZ047.getJcicZ047Id().getSubmitKey(), titaVo);//資料檢核
			jcicCom.checkJcicZ044RateAnedPeriod(JcicZ044Vo, tJcicZ047);
//			//檢核第9~14欄(依民法第323條計算之信用貸款債務總金額、信用貸款債務簽約總金額、依民法323條計算之現金卡債務總金額、現金卡簽約總金額、依民法323條計算之信用卡債務總金額、信用卡債務簽約總金額)若與所有原債權金融機構回報‘42’:回報無擔保債權金額資料債權金額不同時，則予以剔退。
//			jcicCom.checkValueJcicZ042(tJcicZ047,titaVo);
			
			if(tJcicZ047VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ047.setCreateDate(tJcicZ047VO.getCreateDate());
				tJcicZ047.setCreateEmpNo(tJcicZ047VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ047.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ047.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ047.setCreateDate(OrgJcicZ047.getCreateDate());
				tJcicZ047.setCreateEmpNo(OrgJcicZ047.getCreateEmpNo());
				try {
					tJcicZ047 = sJcicZ047Service.update2(tJcicZ047, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ047, tJcicZ047);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ047.setTranKey(TranKey);
				try {
					sJcicZ047Service.insert(tJcicZ047, titaVo);
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