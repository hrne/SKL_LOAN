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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;
import com.st1.itx.db.domain.JcicZ049Log;
import com.st1.itx.db.service.JcicZ049LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ049Service;

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

@Service("L8310")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8310 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ049Service sJcicZ049Service;
	@Autowired
	public JcicZ049LogService sJcicZ049LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8310 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String iCustId=titaVo.getParam("CustId").trim();//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iClaimStatus = Integer.valueOf(titaVo.getParam("ClaimStatus"));
		int iApplyDate= Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode= titaVo.getParam("CourtCode");
		int iYear= Integer.valueOf(titaVo.getParam("Year"));
		String iCourtDiv= titaVo.getParam("CourtDiv");
		String iCourtCaseNo= titaVo.getParam("CourtCaseNo");
		String iApprove= titaVo.getParam("Approve");
		int iClaimDate= Integer.valueOf(titaVo.getParam("ClaimDate"));
		String iKey = "";
		//JcicZ049
		JcicZ049 iJcicZ049 = new JcicZ049();
		JcicZ049Id iJcicZ049Id = new JcicZ049Id();
		iJcicZ049Id.setCustId(iCustId);//債務人IDN
		iJcicZ049Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ049Id.setRcDate(iRcDate);
		JcicZ049 chJcicZ049 = new JcicZ049();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ049
			chJcicZ049 = sJcicZ049Service.findById(iJcicZ049Id, titaVo);
			if (chJcicZ049!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ049.setJcicZ049Id(iJcicZ049Id);
			iJcicZ049.setTranKey(iTranKey);
			iJcicZ049.setClaimStatus(iClaimStatus);
			iJcicZ049.setApplyDate(iApplyDate);
			iJcicZ049.setCourtCode(iCourtCode);
			iJcicZ049.setYear(iYear);
			iJcicZ049.setCourtDiv(iCourtDiv);
			iJcicZ049.setCourtCaseNo(iCourtCaseNo);
			iJcicZ049.setApprove(iApprove);
			iJcicZ049.setClaimDate(iClaimDate);
			iJcicZ049.setUkey(iKey);
			try {
				sJcicZ049Service.insert(iJcicZ049, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ049 = sJcicZ049Service.ukeyFirst(iKey, titaVo);
			JcicZ049 uJcicZ049 = new JcicZ049();
			uJcicZ049 = sJcicZ049Service.holdById(iJcicZ049.getJcicZ049Id(), titaVo);
			if (uJcicZ049 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ049.setTranKey(iTranKey);
			uJcicZ049.setClaimStatus(iClaimStatus);
			uJcicZ049.setApplyDate(iApplyDate);
			uJcicZ049.setCourtCode(iCourtCode);
			uJcicZ049.setYear(iYear);
			uJcicZ049.setCourtDiv(iCourtDiv);
			uJcicZ049.setCourtCaseNo(iCourtCaseNo);
			uJcicZ049.setApprove(iApprove);
			uJcicZ049.setClaimDate(iClaimDate);
			uJcicZ049.setOutJcicTxtDate(0);
			JcicZ049 oldJcicZ049 = (JcicZ049) iDataLog.clone(uJcicZ049);
			try {
				sJcicZ049Service.update(uJcicZ049, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ049, uJcicZ049);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ049 = sJcicZ049Service.findById(iJcicZ049Id);
			if (iJcicZ049 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ049Log> dJcicLogZ049 = null;
			dJcicLogZ049 = sJcicZ049LogService.ukeyEq(iJcicZ049.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ049 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ049Service.delete(iJcicZ049, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ049Log iJcicZ049Log = dJcicLogZ049.getContent().get(0);
				iJcicZ049.setClaimStatus(iJcicZ049Log.getClaimStatus());
				iJcicZ049.setApplyDate(iJcicZ049Log.getApplyDate());
				iJcicZ049.setCourtCode(iJcicZ049Log.getCourtCode());
				iJcicZ049.setYear(iJcicZ049Log.getYear());
				iJcicZ049.setCourtDiv(iJcicZ049Log.getCourtDiv());
				iJcicZ049.setCourtCaseNo(iJcicZ049Log.getCourtCaseNo());
				iJcicZ049.setApprove(iJcicZ049Log.getApprove());
				iJcicZ049.setClaimDate(iJcicZ049Log.getClaimDate());
				iJcicZ049.setTranKey(iJcicZ049Log.getTranKey());
				iJcicZ049.setOutJcicTxtDate(iJcicZ049Log.getOutJcicTxtDate());
				try {
					sJcicZ049Service.update(iJcicZ049, titaVo);
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
