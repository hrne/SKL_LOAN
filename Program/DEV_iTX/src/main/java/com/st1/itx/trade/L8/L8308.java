package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ047Log;
import com.st1.itx.db.service.JcicZ047LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ047Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* CaseStatus=X,1<br>
* ClaimDate=9,7<br>
* CourtCode=X,3<br>
* Year=9,3<br>
* CourtDiv=X,8<br>
* CourtCaseNo=X,80<br>
* Approve=X,1<br>
* OutstandAmt=9,9<br>
* ClaimStatus1=X,1<br>
* SaveDate=9,7<br>
* ClaimStatus2=X,1<br>
* SaveEndDate=9,7<br>
* SubAmt=9,9<br>
* AdminName=X,20<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8308")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8308 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ047LogService sJcicZ047LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8308 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate   = new BigDecimal (titaVo.getParam("Rate"));
		int iCivil323ExpAmt =Integer.valueOf(titaVo.getParam("Civil323ExpAmt"));
		int iExpLoanAmt=Integer.valueOf(titaVo.getParam("ExpLoanAmt"));
		int iCivil323CashAmt=Integer.valueOf(titaVo.getParam("Civil323CashAmt"));
		int iCashCardAmt=Integer.valueOf(titaVo.getParam("CashCardAmt"));
		int iCivil323CreditAmt =Integer.valueOf(titaVo.getParam("Civil323CreditAmt"));
		int iCreditCardAmt =Integer.valueOf(titaVo.getParam("CreditCardAmt"));
		BigDecimal iCivil323Amt =new BigDecimal (titaVo.getParam("Civil323Amt"));
		BigDecimal iTotalAmt =new BigDecimal (titaVo.getParam("TotalAmt"));
		int iPassDate =Integer.valueOf(titaVo.getParam("PassDate"));
		int iInterviewDate=Integer.valueOf(titaVo.getParam("InterviewDate")); 
		int iSignDate = Integer.valueOf(titaVo.getParam("SignDate")); 
		int iLimitDate = Integer.valueOf(titaVo.getParam("LimitDate")); 
		int iFirstPayDate  = Integer.valueOf(titaVo.getParam("FirstPayDate"));
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		String iPayAccount = String.valueOf(titaVo.getParam("PayAccount"));
		String iPostAddr = String.valueOf(titaVo.getParam("PostAddr"));
		String iGradeType = String.valueOf(titaVo.getParam("GradeType"));
		int iPayLastAmt = Integer.valueOf(titaVo.getParam("PayLastAmt"));
		int iPeriod2= Integer.valueOf(titaVo.getParam("PayLastAmt"));
		BigDecimal iRate2 =new BigDecimal (titaVo.getParam("Rate2"));
		int iMonthPayAmt2= Integer.valueOf(titaVo.getParam("MonthPayAmt2"));
		int iPayLastAmt2= Integer.valueOf(titaVo.getParam("PayLastAmt2"));

		String iKey = "";
		//JcicZ047
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);//債務人IDN
		iJcicZ047Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ047Id.setRcDate(iRcDate);
		JcicZ047 chJcicZ047 = new JcicZ047();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ047
			chJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
			if (chJcicZ047!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ047.setJcicZ047Id(iJcicZ047Id);
			iJcicZ047.setTranKey(iTranKey);
			iJcicZ047.setPeriod(iPeriod); 
			iJcicZ047.setRate(iRate);
			iJcicZ047.setCivil323ExpAmt(iCivil323ExpAmt);
			iJcicZ047.setExpLoanAmt(iExpLoanAmt);
			iJcicZ047.setCivil323CashAmt(iCivil323CashAmt);
			iJcicZ047.setCashCardAmt(iCashCardAmt);
			iJcicZ047.setCivil323CreditAmt(iCivil323CreditAmt);
			iJcicZ047.setCreditCardAmt(iCreditCardAmt);
			iJcicZ047.setCivil323Amt(iCivil323Amt);
			iJcicZ047.setTotalAmt(iTotalAmt);
			iJcicZ047.setPassDate(iPassDate);
			iJcicZ047.setInterviewDate(iInterviewDate);
			iJcicZ047.setSignDate(iSignDate);
			iJcicZ047.setLimitDate(iLimitDate);
			iJcicZ047.setFirstPayDate(iFirstPayDate);
			iJcicZ047.setMonthPayAmt(iMonthPayAmt);
			iJcicZ047.setPayAccount(iPayAccount);
			iJcicZ047.setPostAddr(iPostAddr);
			iJcicZ047.setGradeType(iGradeType);
			iJcicZ047.setPayLastAmt(iPayLastAmt);
			iJcicZ047.setPeriod2(iPeriod2);
			iJcicZ047.setRate2(iRate2);
			iJcicZ047.setMonthPayAmt2(iMonthPayAmt2);
			iJcicZ047.setPayLastAmt2(iPayLastAmt2);
			iJcicZ047.setUkey(iKey);
			try {
				sJcicZ047Service.insert(iJcicZ047, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ047 = sJcicZ047Service.ukeyFirst(iKey, titaVo);
			JcicZ047 uJcicZ047 = new JcicZ047();
			uJcicZ047 = sJcicZ047Service.holdById(iJcicZ047.getJcicZ047Id(), titaVo);
			if (uJcicZ047 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ047.setTranKey(iTranKey);
			uJcicZ047.setPeriod(iPeriod); 
			uJcicZ047.setRate(iRate);
			uJcicZ047.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ047.setExpLoanAmt(iExpLoanAmt);
			uJcicZ047.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ047.setCashCardAmt(iCashCardAmt);
			uJcicZ047.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ047.setCreditCardAmt(iCreditCardAmt);
			uJcicZ047.setCivil323Amt(iCivil323Amt);
			uJcicZ047.setTotalAmt(iTotalAmt);
			uJcicZ047.setPassDate(iPassDate);
			uJcicZ047.setInterviewDate(iInterviewDate);
			uJcicZ047.setSignDate(iSignDate);
			uJcicZ047.setLimitDate(iLimitDate);
			uJcicZ047.setFirstPayDate(iFirstPayDate);
			uJcicZ047.setMonthPayAmt(iMonthPayAmt);
			uJcicZ047.setPayAccount(iPayAccount);
			uJcicZ047.setPostAddr(iPostAddr);
			uJcicZ047.setGradeType(iGradeType);
			uJcicZ047.setPayLastAmt(iPayLastAmt);
			uJcicZ047.setPeriod2(iPeriod2);
			uJcicZ047.setRate2(iRate2);
			uJcicZ047.setMonthPayAmt2(iMonthPayAmt2);
			uJcicZ047.setPayLastAmt2(iPayLastAmt2);
			uJcicZ047.setOutJcicTxtDate(0);
			JcicZ047 oldJcicZ047 = (JcicZ047) iDataLog.clone(uJcicZ047);
			try {
				sJcicZ047Service.update(uJcicZ047, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ047, uJcicZ047);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id);
			if (iJcicZ047 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ047Log> dJcicLogZ047 = null;
			dJcicLogZ047 = sJcicZ047LogService.ukeyEq(iJcicZ047.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ047 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ047Service.delete(iJcicZ047, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ047Log iJcicZ047Log = dJcicLogZ047.getContent().get(0);
				iJcicZ047.setPeriod(iJcicZ047Log.getPeriod()); 
				iJcicZ047.setRate(iJcicZ047Log.getRate());
				iJcicZ047.setCivil323ExpAmt(iJcicZ047Log.getCivil323ExpAmt());
				iJcicZ047.setExpLoanAmt(iJcicZ047Log.getExpLoanAmt());
				iJcicZ047.setCivil323CashAmt(iJcicZ047Log.getCivil323CashAmt());
				iJcicZ047.setCashCardAmt(iJcicZ047Log.getCashCardAmt());
				iJcicZ047.setCivil323CreditAmt(iJcicZ047Log.getCivil323CreditAmt());
				iJcicZ047.setCreditCardAmt(iJcicZ047Log.getCreditCardAmt());
				iJcicZ047.setCivil323Amt(iJcicZ047Log.getCivil323Amt());
				iJcicZ047.setTotalAmt(iJcicZ047Log.getTotalAmt());
				iJcicZ047.setPassDate(iJcicZ047Log.getPassDate());
				iJcicZ047.setInterviewDate(iJcicZ047Log.getInterviewDate());
				iJcicZ047.setSignDate(iJcicZ047Log.getSignDate());
				iJcicZ047.setLimitDate(iJcicZ047Log.getLimitDate());
				iJcicZ047.setFirstPayDate(iJcicZ047Log.getFirstPayDate());
				iJcicZ047.setMonthPayAmt(iJcicZ047Log.getMonthPayAmt());
				iJcicZ047.setPayAccount(iJcicZ047Log.getPayAccount());
				iJcicZ047.setPostAddr(iJcicZ047Log.getPostAddr());
				iJcicZ047.setGradeType(iJcicZ047Log.getGradeType());
				iJcicZ047.setPayLastAmt(iJcicZ047Log.getPayLastAmt());
				iJcicZ047.setPeriod2(iJcicZ047Log.getPeriod2());
				iJcicZ047.setRate2(iJcicZ047Log.getRate2());
				iJcicZ047.setMonthPayAmt2(iJcicZ047Log.getMonthPayAmt2());
				iJcicZ047.setPayLastAmt2(iJcicZ047Log.getPayLastAmt2());
				iJcicZ047.setTranKey(iJcicZ047Log.getTranKey());
				iJcicZ047.setOutJcicTxtDate(iJcicZ047Log.getOutJcicTxtDate());
				try {
					sJcicZ047Service.update(iJcicZ047, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}
