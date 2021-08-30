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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
import com.st1.itx.db.domain.JcicZ051Log;
import com.st1.itx.db.service.JcicZ051LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ051Service;

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

@Service("L8312")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8312 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ051LogService sJcicZ051LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8312 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iDelayYM = Integer.valueOf(titaVo.getParam("DelayYM"));
		String iDelayCode =titaVo.getParam("DelayCode");
		String iDelayDesc =titaVo.getParam("DelayDesc");
		String iKey = "";
		//JcicZ051
		JcicZ051 iJcicZ051 = new JcicZ051();
		JcicZ051Id iJcicZ051Id = new JcicZ051Id();
		iJcicZ051Id.setCustId(iCustId);//債務人IDN
		iJcicZ051Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ051Id.setRcDate(iRcDate);
		iJcicZ051Id.setDelayYM(iDelayYM);
		JcicZ051 chJcicZ051 = new JcicZ051();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ051
			chJcicZ051 = sJcicZ051Service.findById(iJcicZ051Id, titaVo);
			if (chJcicZ051!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ051.setJcicZ051Id(iJcicZ051Id);
			iJcicZ051.setTranKey(iTranKey);
			iJcicZ051.setDelayCode(iDelayCode);
			iJcicZ051.setDelayDesc(iDelayDesc);
			iJcicZ051.setUkey(iKey);
			try {
				sJcicZ051Service.insert(iJcicZ051, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ051 = sJcicZ051Service.ukeyFirst(iKey, titaVo);
			JcicZ051 uJcicZ051 = new JcicZ051();
			uJcicZ051 = sJcicZ051Service.holdById(iJcicZ051.getJcicZ051Id(), titaVo);
			if (uJcicZ051 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ051.setTranKey(iTranKey);
			uJcicZ051.setDelayCode(iDelayCode);
			uJcicZ051.setDelayDesc(iDelayDesc);
			uJcicZ051.setOutJcicTxtDate(0);
			JcicZ051 oldJcicZ051 = (JcicZ051) iDataLog.clone(uJcicZ051);
			try {
				sJcicZ051Service.update(uJcicZ051, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ051, uJcicZ051);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ051 = sJcicZ051Service.findById(iJcicZ051Id);
			if (iJcicZ051 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ051Log> dJcicLogZ051 = null;
			dJcicLogZ051 = sJcicZ051LogService.ukeyEq(iJcicZ051.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ051 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ051Service.delete(iJcicZ051, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ051Log iJcicZ051Log = dJcicLogZ051.getContent().get(0);
				iJcicZ051.setDelayCode(iJcicZ051Log.getDelayCode());
				iJcicZ051.setDelayDesc(iJcicZ051Log.getDelayDesc());
				iJcicZ051.setTranKey(iJcicZ051Log.getTranKey());
				iJcicZ051.setOutJcicTxtDate(iJcicZ051Log.getOutJcicTxtDate());
				try {
					sJcicZ051Service.update(iJcicZ051, titaVo);
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
