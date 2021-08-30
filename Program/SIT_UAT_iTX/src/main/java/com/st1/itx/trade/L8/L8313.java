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
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
import com.st1.itx.db.domain.JcicZ052Log;
import com.st1.itx.db.service.JcicZ052LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ052Service;

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

@Service("L8313")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8313 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ052LogService sJcicZ052LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8313 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iBankCode1=titaVo.getParam("BankCode1");
		String iDataCode1=titaVo.getParam("DataCode1");
		String iBankCode2=titaVo.getParam("BankCode2");
		String iDataCode2=titaVo.getParam("DataCode2");
		String iBankCode3=titaVo.getParam("BankCode3");
		String iDataCode3=titaVo.getParam("DataCode3");
		String iBankCode4=titaVo.getParam("BankCode4");
		String iDataCode4=titaVo.getParam("DataCode4");
		String iBankCode5=titaVo.getParam("BankCode5");
		String iDataCode5=titaVo.getParam("DataCode5");
		int iChangePayDate=Integer.valueOf(titaVo.getParam("ChangePayDate"));
		String iKey = "";
		//JcicZ052
		JcicZ052 iJcicZ052 = new JcicZ052();
		JcicZ052Id iJcicZ052Id = new JcicZ052Id();
		iJcicZ052Id.setCustId(iCustId);//債務人IDN
		iJcicZ052Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ052Id.setRcDate(iRcDate);
		JcicZ052 chJcicZ052 = new JcicZ052();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ052
			chJcicZ052 = sJcicZ052Service.findById(iJcicZ052Id, titaVo);
			if (chJcicZ052!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ052.setJcicZ052Id(iJcicZ052Id);
			iJcicZ052.setTranKey(iTranKey);
			iJcicZ052.setBankCode1(iBankCode1);
			iJcicZ052.setDataCode1(iDataCode1);
			iJcicZ052.setBankCode2(iBankCode2);
			iJcicZ052.setDataCode2(iDataCode2);
			iJcicZ052.setBankCode3(iBankCode3);
			iJcicZ052.setDataCode3(iDataCode3);
			iJcicZ052.setBankCode4(iBankCode4);
			iJcicZ052.setDataCode4(iDataCode4);
			iJcicZ052.setBankCode5(iBankCode5);
			iJcicZ052.setDataCode5(iDataCode5);
			iJcicZ052.setChangePayDate(iChangePayDate);
			iJcicZ052.setUkey(iKey);
			try {
				sJcicZ052Service.insert(iJcicZ052, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ052 = sJcicZ052Service.ukeyFirst(iKey, titaVo);
			JcicZ052 uJcicZ052 = new JcicZ052();
			uJcicZ052 = sJcicZ052Service.holdById(iJcicZ052.getJcicZ052Id(), titaVo);
			if (uJcicZ052 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ052.setTranKey(iTranKey);
			uJcicZ052.setBankCode1(iBankCode1);
			uJcicZ052.setDataCode1(iDataCode1);
			uJcicZ052.setBankCode2(iBankCode2);
			uJcicZ052.setDataCode2(iDataCode2);
			uJcicZ052.setBankCode3(iBankCode3);
			uJcicZ052.setDataCode3(iDataCode3);
			uJcicZ052.setBankCode4(iBankCode4);
			uJcicZ052.setDataCode4(iDataCode4);
			uJcicZ052.setBankCode5(iBankCode5);
			uJcicZ052.setDataCode5(iDataCode5);
			uJcicZ052.setChangePayDate(iChangePayDate);
			uJcicZ052.setOutJcicTxtDate(0);
			JcicZ052 oldJcicZ052 = (JcicZ052) iDataLog.clone(uJcicZ052);
			try {
				sJcicZ052Service.update(uJcicZ052, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ052, uJcicZ052);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ052 = sJcicZ052Service.findById(iJcicZ052Id);
			if (iJcicZ052 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ052Log> dJcicLogZ052 = null;
			dJcicLogZ052 = sJcicZ052LogService.ukeyEq(iJcicZ052.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ052 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ052Service.delete(iJcicZ052, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ052Log iJcicZ052Log = dJcicLogZ052.getContent().get(0);
				iJcicZ052.setBankCode1(iJcicZ052Log.getBankCode1());
				iJcicZ052.setDataCode1(iJcicZ052Log.getDataCode1());
				iJcicZ052.setBankCode2(iJcicZ052Log.getBankCode2());
				iJcicZ052.setDataCode2(iJcicZ052Log.getDataCode2());
				iJcicZ052.setBankCode3(iJcicZ052Log.getBankCode3());
				iJcicZ052.setDataCode3(iJcicZ052Log.getDataCode3());
				iJcicZ052.setBankCode4(iJcicZ052Log.getBankCode4());
				iJcicZ052.setDataCode4(iJcicZ052Log.getDataCode4());
				iJcicZ052.setBankCode5(iJcicZ052Log.getBankCode5());
				iJcicZ052.setDataCode5(iJcicZ052Log.getDataCode5());
				iJcicZ052.setChangePayDate(iJcicZ052Log.getChangePayDate());
				iJcicZ052.setTranKey(iJcicZ052Log.getTranKey());
				iJcicZ052.setOutJcicTxtDate(iJcicZ052Log.getOutJcicTxtDate());
				try {
					sJcicZ052Service.update(iJcicZ052, titaVo);
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
