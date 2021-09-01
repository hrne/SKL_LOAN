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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;
import com.st1.itx.db.domain.JcicZ043Log;
import com.st1.itx.db.service.JcicZ043LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ043Service;

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

@Service("L8304")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8304 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ043LogService sJcicZ043LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8304 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iMaxMainCode=titaVo.getParam("MaxMainCode");
		String iAccount=titaVo.getParam("Account");
		String iCollateralType=titaVo.getParam("CollateralType");
		BigDecimal iOriginLoanAmt= new BigDecimal(titaVo.getParam("OriginLoanAmt"));
		BigDecimal iCreditBalance=new BigDecimal(titaVo.getParam("CreditBalance"));
		BigDecimal iPerPeriordAmt=new BigDecimal(titaVo.getParam("PerPeriordAmt"));
		BigDecimal iLastPayAmt=new BigDecimal(titaVo.getParam("LastPayAmt"));
		int iLastPayDate=Integer.valueOf(titaVo.getParam("LastPayDate"));
		BigDecimal iOutstandAmt=new BigDecimal(titaVo.getParam("OutstandAmt"));
		int iRepayPerMonDay=Integer.valueOf(titaVo.getParam("RepayPerMonDay"));
		int iContractStartYM=Integer.valueOf(titaVo.getParam("ContractStartYM"));
		int iContractEndYM=Integer.valueOf(titaVo.getParam("ContractEndYM"));
		String iKey = "";
		//JcicZ043
		JcicZ043 iJcicZ043 = new JcicZ043();
		JcicZ043Id iJcicZ043Id = new JcicZ043Id();
		iJcicZ043Id.setCustId(iCustId);//債務人IDN
		iJcicZ043Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ043Id.setRcDate(iRcDate);
		iJcicZ043Id.setMaxMainCode(iMaxMainCode);
		iJcicZ043Id.setAccount(iAccount);
		JcicZ043 chJcicZ043 = new JcicZ043();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ043
			chJcicZ043 = sJcicZ043Service.findById(iJcicZ043Id, titaVo);
			if (chJcicZ043!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ043.setJcicZ043Id(iJcicZ043Id);
			iJcicZ043.setTranKey(iTranKey);
			iJcicZ043.setCollateralType(iCollateralType);
			iJcicZ043.setOriginLoanAmt(iOriginLoanAmt);
			iJcicZ043.setCreditBalance(iCreditBalance);
			iJcicZ043.setPerPeriordAmt(iPerPeriordAmt);
			iJcicZ043.setLastPayAmt(iLastPayAmt);
			iJcicZ043.setLastPayDate(iLastPayDate);
			iJcicZ043.setOutstandAmt(iOutstandAmt);
			iJcicZ043.setRepayPerMonDay(iRepayPerMonDay);
			iJcicZ043.setContractStartYM(iContractStartYM);
			iJcicZ043.setContractEndYM(iContractEndYM);
			iJcicZ043.setUkey(iKey);
			try {
				sJcicZ043Service.insert(iJcicZ043, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ043 = sJcicZ043Service.ukeyFirst(iKey, titaVo);
			JcicZ043 uJcicZ043 = new JcicZ043();
			uJcicZ043 = sJcicZ043Service.holdById(iJcicZ043.getJcicZ043Id(), titaVo);
			if (uJcicZ043 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ043.setTranKey(iTranKey);
			uJcicZ043.setCollateralType(iCollateralType);
			uJcicZ043.setOriginLoanAmt(iOriginLoanAmt);
			uJcicZ043.setCreditBalance(iCreditBalance);
			uJcicZ043.setPerPeriordAmt(iPerPeriordAmt);
			uJcicZ043.setLastPayAmt(iLastPayAmt);
			uJcicZ043.setLastPayDate(iLastPayDate);
			uJcicZ043.setOutstandAmt(iOutstandAmt);
			uJcicZ043.setRepayPerMonDay(iRepayPerMonDay);
			uJcicZ043.setContractStartYM(iContractStartYM);
			uJcicZ043.setContractEndYM(iContractEndYM);
			uJcicZ043.setOutJcicTxtDate(0);
			JcicZ043 oldJcicZ043 = (JcicZ043) iDataLog.clone(uJcicZ043);
			try {
				sJcicZ043Service.update(uJcicZ043, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ043, uJcicZ043);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ043 = sJcicZ043Service.findById(iJcicZ043Id);
			if (iJcicZ043 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ043Log> dJcicLogZ043 = null;
			dJcicLogZ043 = sJcicZ043LogService.ukeyEq(iJcicZ043.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ043 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ043Service.delete(iJcicZ043, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ043Log iJcicZ043Log = dJcicLogZ043.getContent().get(0);
				iJcicZ043.setCollateralType(iJcicZ043Log.getCollateralType());
				iJcicZ043.setOriginLoanAmt(iJcicZ043Log.getOriginLoanAmt());
				iJcicZ043.setCreditBalance(iJcicZ043Log.getCreditBalance());
				iJcicZ043.setPerPeriordAmt(iJcicZ043Log.getPerPeriordAmt());
				iJcicZ043.setLastPayAmt(iJcicZ043Log.getLastPayAmt());
				iJcicZ043.setLastPayDate(iJcicZ043Log.getLastPayDate());
				iJcicZ043.setOutstandAmt(iJcicZ043Log.getOutstandAmt());
				iJcicZ043.setRepayPerMonDay(iJcicZ043Log.getRepayPerMonDay());
				iJcicZ043.setContractStartYM(iJcicZ043Log.getContractStartYM());
				iJcicZ043.setContractEndYM(iJcicZ043Log.getContractEndYM());
				iJcicZ043.setTranKey(iJcicZ043Log.getTranKey());
				iJcicZ043.setOutJcicTxtDate(iJcicZ043Log.getOutJcicTxtDate());
				try {
					sJcicZ043Service.update(iJcicZ043, titaVo);
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
