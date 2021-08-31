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
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.domain.JcicZ046Log;
import com.st1.itx.db.service.JcicZ046LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ046Service;

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

@Service("L8307")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8307 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ046LogService sJcicZ046LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8307 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
		String iCloseCode =titaVo.getParam("CloseCode"); 
		String iBreakCode =titaVo.getParam("BreakCode");
		String iKey = "";
		//JcicZ046
		JcicZ046 iJcicZ046 = new JcicZ046();
		JcicZ046Id iJcicZ046Id = new JcicZ046Id();
		iJcicZ046Id.setCustId(iCustId);//債務人IDN
		iJcicZ046Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ046Id.setRcDate(iRcDate);
		iJcicZ046Id.setCloseDate(iCloseDate);
		JcicZ046 chJcicZ046 = new JcicZ046();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ046
			chJcicZ046 = sJcicZ046Service.findById(iJcicZ046Id, titaVo);
			if (chJcicZ046!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ046.setJcicZ046Id(iJcicZ046Id);
			iJcicZ046.setTranKey(iTranKey);
			iJcicZ046.setCloseCode(iCloseCode); 
			iJcicZ046.setBreakCode(iBreakCode);
			iJcicZ046.setUkey(iKey);
			try {
				sJcicZ046Service.insert(iJcicZ046, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ046 = sJcicZ046Service.ukeyFirst(iKey, titaVo);
			JcicZ046 uJcicZ046 = new JcicZ046();
			uJcicZ046 = sJcicZ046Service.holdById(iJcicZ046.getJcicZ046Id(), titaVo);
			if (uJcicZ046 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ046.setTranKey(iTranKey);
			uJcicZ046.setCloseCode(iCloseCode); 
			uJcicZ046.setBreakCode(iBreakCode);
			uJcicZ046.setOutJcicTxtDate(0);
			JcicZ046 oldJcicZ046 = (JcicZ046) iDataLog.clone(uJcicZ046);
			try {
				sJcicZ046Service.update(uJcicZ046, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ046, uJcicZ046);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ046 = sJcicZ046Service.findById(iJcicZ046Id);
			if (iJcicZ046 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ046Log> dJcicLogZ046 = null;
			dJcicLogZ046 = sJcicZ046LogService.ukeyEq(iJcicZ046.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ046 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ046Service.delete(iJcicZ046, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ046Log iJcicZ046Log = dJcicLogZ046.getContent().get(0);
				iJcicZ046.setCloseCode(iJcicZ046Log.getCloseCode()); 
				iJcicZ046.setBreakCode(iJcicZ046Log.getBreakCode());
				iJcicZ046.setTranKey(iJcicZ046Log.getTranKey());
				iJcicZ046.setOutJcicTxtDate(iJcicZ046Log.getOutJcicTxtDate());
				try {
					sJcicZ046Service.update(iJcicZ046, titaVo);
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
