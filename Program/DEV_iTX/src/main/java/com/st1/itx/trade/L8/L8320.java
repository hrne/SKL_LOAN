package com.st1.itx.trade.L8;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.domain.JcicZ062Log;
import com.st1.itx.db.service.JcicZ062LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ062Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


@Service("L8320")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8320 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ062LogService sJcicZ062LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8320 ");
		this.totaVo.init(titaVo);
			
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		int iCompletePeriod = Integer.valueOf(titaVo.getParam("CompletePeriod"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iExpBalanceAmt = Integer.valueOf(titaVo.getParam("ExpBalanceAmt"));
		int iCashBalanceAmt = Integer.valueOf(titaVo.getParam("CashBalanceAmt"));
		int iCreditBalanceAmt = Integer.valueOf(titaVo.getParam("CreditBalanceAmt"));
		BigDecimal iChaRepayAmt = new BigDecimal(titaVo.getParam("ChaRepayAmt"));
		int iChaRepayAgreeDate = Integer.valueOf(titaVo.getParam("ChaRepayAgreeDate"));
		int iChaRepayViewDate = Integer.valueOf(titaVo.getParam("ChaRepayViewDate"));
		int iChaRepayEndDate = Integer.valueOf(titaVo.getParam("ChaRepayEndDate"));
		int iChaRepayFirstDate = Integer.valueOf(titaVo.getParam("ChaRepayFirstDate"));
		String iPayAccount = titaVo.getParam("PayAccount");
		String iPostAddr = titaVo.getParam("PostAddr");
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		String iGradeType = titaVo.getParam("GradeType");
		int iPeriod2 = Integer.valueOf(titaVo.getParam("Period2"));
		BigDecimal iRate2 = new BigDecimal(titaVo.getParam("Rate2"));
		int iMonthPayAmt2 = Integer.valueOf(titaVo.getParam("MonthPayAmt2"));	
		String iKey = "";
		//JcicZ062
		JcicZ062 iJcicZ062 = new JcicZ062();
		JcicZ062Id iJcicZ062Id = new JcicZ062Id();
		iJcicZ062Id.setSubmitKey(iSubmitKey);
		iJcicZ062Id.setCustId(iCustId);		
		iJcicZ062Id.setRcDate(iRcDate);
		iJcicZ062Id.setChangePayDate(iChangePayDate);
		JcicZ062 chJcicZ062 = new JcicZ062();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ062
			chJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id, titaVo);
			if (chJcicZ062 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ062.setJcicZ062Id(iJcicZ062Id);
			iJcicZ062.setTranKey(iTranKey);
			iJcicZ062.setCompletePeriod(iCompletePeriod);
			iJcicZ062.setPeriod(iPeriod);
			iJcicZ062.setRate(iRate);
			iJcicZ062.setExpBalanceAmt(iExpBalanceAmt);
			iJcicZ062.setCashBalanceAmt(iCashBalanceAmt);
			iJcicZ062.setCreditBalanceAmt(iCreditBalanceAmt);
			iJcicZ062.setChaRepayAmt(iChaRepayAmt);
			iJcicZ062.setChaRepayAgreeDate(iChaRepayAgreeDate);
			iJcicZ062.setChaRepayViewDate(iChaRepayViewDate);
			iJcicZ062.setChaRepayEndDate(iChaRepayEndDate);
			iJcicZ062.setChaRepayFirstDate(iChaRepayFirstDate);
			iJcicZ062.setPayAccount(iPayAccount);
			iJcicZ062.setPostAddr(iPostAddr);
			iJcicZ062.setMonthPayAmt(iMonthPayAmt);
			iJcicZ062.setGradeType(iGradeType);
			iJcicZ062.setPeriod2(iPeriod2);
			iJcicZ062.setRate2(iRate2);
			iJcicZ062.setMonthPayAmt2(iMonthPayAmt2);
			iJcicZ062.setUkey(iKey);
			try {
				sJcicZ062Service.insert(iJcicZ062, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ062 = sJcicZ062Service.ukeyFirst(iKey, titaVo);
			JcicZ062 uJcicZ062 = new JcicZ062();
			uJcicZ062 = sJcicZ062Service.holdById(iJcicZ062.getJcicZ062Id(), titaVo);
			if (uJcicZ062 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ062.setCompletePeriod(iCompletePeriod);
			uJcicZ062.setPeriod(iPeriod);
			uJcicZ062.setRate(iRate);
			uJcicZ062.setExpBalanceAmt(iExpBalanceAmt);
			uJcicZ062.setCashBalanceAmt(iCashBalanceAmt);
			uJcicZ062.setCreditBalanceAmt(iCreditBalanceAmt);
			uJcicZ062.setChaRepayAmt(iChaRepayAmt);
			uJcicZ062.setChaRepayAgreeDate(iChaRepayAgreeDate);
			uJcicZ062.setChaRepayViewDate(iChaRepayViewDate);
			uJcicZ062.setChaRepayEndDate(iChaRepayEndDate);
			uJcicZ062.setChaRepayFirstDate(iChaRepayFirstDate);
			uJcicZ062.setPayAccount(iPayAccount);
			uJcicZ062.setPostAddr(iPostAddr);
			uJcicZ062.setMonthPayAmt(iMonthPayAmt);
			uJcicZ062.setGradeType(iGradeType);
			uJcicZ062.setPeriod2(iPeriod2);
			uJcicZ062.setRate2(iRate2);
			uJcicZ062.setMonthPayAmt2(iMonthPayAmt2);
			uJcicZ062.setTranKey(iTranKey);
			uJcicZ062.setOutJcicTxtDate(0);
			JcicZ062 oldJcicZ062 = (JcicZ062) iDataLog.clone(uJcicZ062);
			try {
				sJcicZ062Service.update(uJcicZ062, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ062, uJcicZ062);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id);
			if (iJcicZ062 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ062Log> dJcicLogZ062 = null;
			dJcicLogZ062 = sJcicZ062LogService.ukeyEq(iJcicZ062.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ062 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ062Service.delete(iJcicZ062, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ062Log iJcicZ062Log = dJcicLogZ062.getContent().get(0);				
				iJcicZ062.setCompletePeriod(iJcicZ062Log.getCompletePeriod());
				iJcicZ062.setPeriod(iJcicZ062Log.getPeriod());
				iJcicZ062.setRate(iJcicZ062Log.getRate());
				iJcicZ062.setExpBalanceAmt(iJcicZ062Log.getExpBalanceAmt());
				iJcicZ062.setCashBalanceAmt(iJcicZ062Log.getCashBalanceAmt());
				iJcicZ062.setCreditBalanceAmt(iJcicZ062Log.getCreditBalanceAmt());
				iJcicZ062.setChaRepayAmt(iJcicZ062Log.getChaRepayAmt());
				iJcicZ062.setChaRepayAgreeDate(iJcicZ062Log.getChaRepayAgreeDate());
				iJcicZ062.setChaRepayViewDate(iJcicZ062Log.getChaRepayViewDate());
				iJcicZ062.setChaRepayEndDate(iJcicZ062Log.getChaRepayEndDate());
				iJcicZ062.setChaRepayFirstDate(iJcicZ062Log.getChaRepayFirstDate());
				iJcicZ062.setPayAccount(iJcicZ062Log.getPayAccount());
				iJcicZ062.setPostAddr(iJcicZ062Log.getPostAddr());
				iJcicZ062.setMonthPayAmt(iJcicZ062Log.getMonthPayAmt());
				iJcicZ062.setGradeType(iJcicZ062Log.getGradeType());
				iJcicZ062.setPeriod2(iJcicZ062Log.getPeriod2());
				iJcicZ062.setRate2(iJcicZ062Log.getRate2());
				iJcicZ062.setMonthPayAmt2(iJcicZ062Log.getMonthPayAmt2());
				iJcicZ062.setTranKey(iJcicZ062Log.getTranKey());
				iJcicZ062.setOutJcicTxtDate(iJcicZ062Log.getOutJcicTxtDate());
				try {
					sJcicZ062Service.update(iJcicZ062, titaVo);
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