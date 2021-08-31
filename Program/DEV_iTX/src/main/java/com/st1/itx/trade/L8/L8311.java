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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
import com.st1.itx.db.domain.JcicZ050Log;
import com.st1.itx.db.service.JcicZ050LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ050Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,11<br>
* SubmitKey=X,11<br>
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

@Service("L8311")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8311 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ050LogService sJcicZ050LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8311 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iPayDate= Integer.valueOf(titaVo.getParam("PayDate"));
		int iPayAmt = Integer.valueOf(titaVo.getParam("PayAmt"));
		int iSumRepayActualAmt= Integer.valueOf(titaVo.getParam("SumRepayActualAmt"));
		int iSumRepayShouldAmt= Integer.valueOf(titaVo.getParam("SumRepayShouldAmt"));
		int iSecondRepayYM= Integer.valueOf(titaVo.getParam("SecondRepayYM"));
		String iStatus= titaVo.getParam("Status");
		String iKey = "";
		//JcicZ050
		JcicZ050 iJcicZ050 = new JcicZ050();
		JcicZ050Id iJcicZ050Id = new JcicZ050Id();
		iJcicZ050Id.setCustId(iCustId);//債務人IDN
		iJcicZ050Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ050Id.setRcDate(iRcDate);
		iJcicZ050Id.setPayDate(iPayDate);
		JcicZ050 chJcicZ050 = new JcicZ050();
		
		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ050
			chJcicZ050 = sJcicZ050Service.findById(iJcicZ050Id, titaVo);
			if (chJcicZ050!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ050.setJcicZ050Id(iJcicZ050Id);
			iJcicZ050.setTranKey(iTranKey);
			iJcicZ050.setPayAmt(iPayAmt);
			iJcicZ050.setSumRepayActualAmt(iSumRepayActualAmt);
			iJcicZ050.setSumRepayShouldAmt(iSumRepayShouldAmt);
			iJcicZ050.setStatus(iStatus);
			iJcicZ050.setSecondRepayYM(iSecondRepayYM);
			iJcicZ050.setUkey(iKey);
			try {
				sJcicZ050Service.insert(iJcicZ050, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ050 = sJcicZ050Service.ukeyFirst(iKey, titaVo);
			JcicZ050 uJcicZ050 = new JcicZ050();
			uJcicZ050 = sJcicZ050Service.holdById(iJcicZ050.getJcicZ050Id(), titaVo);
			if (uJcicZ050 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ050.setTranKey(iTranKey);
			uJcicZ050.setPayAmt(iPayAmt);
			uJcicZ050.setSumRepayActualAmt(iSumRepayActualAmt);
			uJcicZ050.setSumRepayShouldAmt(iSumRepayShouldAmt);
			uJcicZ050.setSecondRepayYM(iSecondRepayYM);
			uJcicZ050.setStatus(iStatus);
			uJcicZ050.setOutJcicTxtDate(0);
			JcicZ050 oldJcicZ050 = (JcicZ050) iDataLog.clone(uJcicZ050);
			try {
				sJcicZ050Service.update(uJcicZ050, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ050, uJcicZ050);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ050 = sJcicZ050Service.findById(iJcicZ050Id);
			if (iJcicZ050 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ050Log> dJcicLogZ050 = null;
			dJcicLogZ050 = sJcicZ050LogService.ukeyEq(iJcicZ050.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ050 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ050Service.delete(iJcicZ050, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ050Log iJcicZ050Log = dJcicLogZ050.getContent().get(0);
				iJcicZ050.setPayAmt(iJcicZ050Log.getPayAmt());
				iJcicZ050.setSumRepayActualAmt(iJcicZ050Log.getSumRepayActualAmt());
				iJcicZ050.setSumRepayShouldAmt(iJcicZ050Log.getSumRepayShouldAmt());
				iJcicZ050.setSecondRepayYM(iJcicZ050Log.getSecondRepayYM());
				iJcicZ050.setStatus(iJcicZ050Log.getStatus());
				iJcicZ050.setTranKey(iJcicZ050Log.getTranKey());
				iJcicZ050.setOutJcicTxtDate(iJcicZ050Log.getOutJcicTxtDate());
				try {
					sJcicZ050Service.update(iJcicZ050, titaVo);
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
