package com.st1.itx.trade.L8;
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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;
import com.st1.itx.db.domain.JcicZ061Log;
import com.st1.itx.db.service.JcicZ061LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ061Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8319")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8319 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ061LogService sJcicZ061LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8319 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		int iExpBalanceAmt = Integer.valueOf(titaVo.getParam("ExpBalanceAmt"));
		int iCashBalanceAmt = Integer.valueOf(titaVo.getParam("CashBalanceAmt"));
		int iCreditBalanceAmt = Integer.valueOf(titaVo.getParam("CreditBalanceAmt"));
		String iMaxMainNote = titaVo.getParam("MaxMainNote");
		String iIsGuarantor = titaVo.getParam("IsGuarantor");
		String iIsChangePayment = titaVo.getParam("IsChangePayment");
		String iKey = "";
		//JcicZ061
		JcicZ061 iJcicZ061 = new JcicZ061();
		JcicZ061Id iJcicZ061Id = new JcicZ061Id();
		iJcicZ061Id.setSubmitKey(iSubmitKey);
		iJcicZ061Id.setCustId(iCustId);		
		iJcicZ061Id.setRcDate(iRcDate);
		iJcicZ061Id.setChangePayDate(iChangePayDate);
		iJcicZ061Id.setMaxMainCode(iMaxMainCode);
		JcicZ061 chJcicZ061 = new JcicZ061();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複
			chJcicZ061 = sJcicZ061Service.findById(iJcicZ061Id, titaVo);
			if (chJcicZ061!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ061.setJcicZ061Id(iJcicZ061Id);
			iJcicZ061.setTranKey(iTranKey);
			iJcicZ061.setUkey(iKey);
			iJcicZ061.setExpBalanceAmt(iExpBalanceAmt);
			iJcicZ061.setCashBalanceAmt(iCashBalanceAmt);
			iJcicZ061.setCreditBalanceAmt(iCreditBalanceAmt);
			iJcicZ061.setMaxMainNote(iMaxMainNote);
			iJcicZ061.setIsGuarantor(iIsGuarantor);
			iJcicZ061.setIsChangePayment(iIsChangePayment);
			iJcicZ061.setUkey(iKey);
			try {
				sJcicZ061Service.insert(iJcicZ061, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ061 = sJcicZ061Service.ukeyFirst(iKey, titaVo);
			JcicZ061 uJcicZ061 = new JcicZ061();
			uJcicZ061 = sJcicZ061Service.holdById(iJcicZ061.getJcicZ061Id(), titaVo);
			if (uJcicZ061 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ061.setTranKey(iTranKey);
			uJcicZ061.setExpBalanceAmt(iExpBalanceAmt);
			uJcicZ061.setCashBalanceAmt(iCashBalanceAmt);
			uJcicZ061.setCreditBalanceAmt(iCreditBalanceAmt);
			uJcicZ061.setMaxMainNote(iMaxMainNote);
			uJcicZ061.setIsGuarantor(iIsGuarantor);
			uJcicZ061.setIsChangePayment(iIsChangePayment);
			uJcicZ061.setOutJcicTxtDate(0);
			JcicZ061 oldJcicZ061 = (JcicZ061) iDataLog.clone(uJcicZ061);
			try {
				sJcicZ061Service.update(uJcicZ061, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ061, uJcicZ061);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ061 = sJcicZ061Service.findById(iJcicZ061Id);
			if (iJcicZ061 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ061Log> dJcicLogZ061 = null;
			dJcicLogZ061 = sJcicZ061LogService.ukeyEq(iJcicZ061.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ061 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ061Service.delete(iJcicZ061, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ061Log iJcicZ061Log = dJcicLogZ061.getContent().get(0);				
				iJcicZ061.setExpBalanceAmt(iJcicZ061Log.getExpBalanceAmt());
				iJcicZ061.setCashBalanceAmt(iJcicZ061Log.getCashBalanceAmt());
				iJcicZ061.setCreditBalanceAmt(iJcicZ061Log.getCreditBalanceAmt());
				iJcicZ061.setMaxMainNote(iJcicZ061Log.getMaxMainNote());
				iJcicZ061.setIsGuarantor(iJcicZ061Log.getIsGuarantor());
				iJcicZ061.setIsChangePayment(iJcicZ061Log.getIsChangePayment());
				iJcicZ061.setTranKey(iJcicZ061Log.getTranKey());
				iJcicZ061.setOutJcicTxtDate(iJcicZ061Log.getOutJcicTxtDate());
				try {
					sJcicZ061Service.update(iJcicZ061, titaVo);
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