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

/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ040Log;
import com.st1.itx.db.service.JcicZ040LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;

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

@Service("L8301")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8301 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ040LogService sJcicZ040LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8301 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iRbDate=Integer.valueOf(titaVo.getParam("RbDate"));
		String iApplyType =titaVo.getParam("ApplyType");
		String iRefBankId = titaVo.getParam("RefBankId");
		String iNotBankId1 = titaVo.getParam("NotBankId1");
		String iNotBankId2 = titaVo.getParam("NotBankId2");
		String iNotBankId3 = titaVo.getParam("NotBankId3");		
		String iNotBankId4 = titaVo.getParam("NotBankId4");
		String iNotBankId5 = titaVo.getParam("NotBankId5");
		String iNotBankId6 = titaVo.getParam("NotBankId6");
		String iKey = "";
		//JcicZ040
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);//債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);
		JcicZ040 chJcicZ040 = new JcicZ040();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ040
			chJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
			if (chJcicZ040!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ040.setJcicZ040Id(iJcicZ040Id);
			iJcicZ040.setTranKey(iTranKey);
			iJcicZ040.setRbDate(iRbDate);
			iJcicZ040.setApplyType(iApplyType);
			iJcicZ040.setRefBankId(iRefBankId);
			iJcicZ040.setNotBankId1(iNotBankId1);
			iJcicZ040.setNotBankId2(iNotBankId2);
			iJcicZ040.setNotBankId3(iNotBankId3);
			iJcicZ040.setNotBankId4(iNotBankId4);
			iJcicZ040.setNotBankId5(iNotBankId5);
			iJcicZ040.setNotBankId6(iNotBankId6);
			iJcicZ040.setUkey(iKey);
			try {
				sJcicZ040Service.insert(iJcicZ040, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ040 = sJcicZ040Service.ukeyFirst(iKey, titaVo);
			JcicZ040 uJcicZ040 = new JcicZ040();
			uJcicZ040 = sJcicZ040Service.holdById(iJcicZ040.getJcicZ040Id(), titaVo);
			if (uJcicZ040 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ040.setTranKey(iTranKey);
			uJcicZ040.setRbDate(iRbDate);
			uJcicZ040.setApplyType(iApplyType);
			uJcicZ040.setRefBankId(iRefBankId);
			uJcicZ040.setNotBankId1(iNotBankId1);
			uJcicZ040.setNotBankId2(iNotBankId2);
			uJcicZ040.setNotBankId3(iNotBankId3);
			uJcicZ040.setNotBankId4(iNotBankId4);
			uJcicZ040.setNotBankId5(iNotBankId5);
			uJcicZ040.setNotBankId6(iNotBankId6);
			uJcicZ040.setOutJcicTxtDate(0);
			JcicZ040 oldJcicZ040 = (JcicZ040) iDataLog.clone(uJcicZ040);
			try {
				sJcicZ040Service.update(uJcicZ040, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ040, uJcicZ040);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id);
			if (iJcicZ040 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ040Log> dJcicLogZ040 = null;
			dJcicLogZ040 = sJcicZ040LogService.ukeyEq(iJcicZ040.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ040 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ040Service.delete(iJcicZ040, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ040Log iJcicZ040Log = dJcicLogZ040.getContent().get(0);
				iJcicZ040.setApplyType(iJcicZ040Log.getApplyType());
				iJcicZ040.setRefBankId(iJcicZ040Log.getRefBankId());
				iJcicZ040.setNotBankId1(iJcicZ040Log.getNotBankId1());
				iJcicZ040.setNotBankId2(iJcicZ040Log.getNotBankId2());
				iJcicZ040.setNotBankId3(iJcicZ040Log.getNotBankId3());
				iJcicZ040.setNotBankId4(iJcicZ040Log.getNotBankId4());
				iJcicZ040.setNotBankId5(iJcicZ040Log.getNotBankId5());
				iJcicZ040.setNotBankId6(iJcicZ040Log.getNotBankId6());
				iJcicZ040.setTranKey(iJcicZ040Log.getTranKey());
				iJcicZ040.setOutJcicTxtDate(iJcicZ040Log.getOutJcicTxtDate());
				try {
					sJcicZ040Service.update(iJcicZ040, titaVo);
				}catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}
