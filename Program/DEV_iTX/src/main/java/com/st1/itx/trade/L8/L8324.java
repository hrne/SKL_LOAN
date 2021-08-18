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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.domain.JcicZ443Log;
import com.st1.itx.db.service.JcicZ443LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ443Service;

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

@Service("L8324")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8324 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ443LogService sJcicZ443LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8324 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		String iAccount = titaVo.getParam("Account");
		String iIsMaxMain = titaVo.getParam("IsMaxMain");
		String iGuarantyType = titaVo.getParam("GuarantyType");
		BigDecimal iLoanAmt = new BigDecimal (titaVo.getParam("LoanAmt"));
		BigDecimal iCreditAmt = new BigDecimal (titaVo.getParam("CreditAmt"));
		BigDecimal iPrincipal = new BigDecimal (titaVo.getParam("Principal"));
		BigDecimal iInterest = new BigDecimal (titaVo.getParam("Interest"));
		BigDecimal iPenalty = new BigDecimal (titaVo.getParam("Penalty"));
		BigDecimal iOther = new BigDecimal (titaVo.getParam("Other"));
		BigDecimal iTerminalPayAmt = new BigDecimal (titaVo.getParam("TerminalPayAmt"));
		BigDecimal iLatestPayAmt = new BigDecimal (titaVo.getParam("LatestPayAmt"));
		int iFinalPayDay = Integer.valueOf(titaVo.getParam("FinalPayDay"));
		BigDecimal iNotyetacQuit = new BigDecimal (titaVo.getParam("NotyetacQuit"));
		int iMothPayDay = Integer.valueOf(titaVo.getParam("MothPayDay"));
		int iBeginDate = Integer.valueOf(titaVo.getParam("BeginDate"));
		int iEndDate = Integer.valueOf(titaVo.getParam("EndDate"));
		String iKey = "";
		//JcicZ443
		JcicZ443 iJcicZ443 = new JcicZ443();
		JcicZ443Id iJcicZ443Id = new JcicZ443Id();
		iJcicZ443Id.setSubmitKey(iSubmitKey);
		iJcicZ443Id.setCustId(iCustId);		
		iJcicZ443Id.setApplyDate(iApplyDate);
		iJcicZ443Id.setCourtCode(iCourtCode);
		iJcicZ443Id.setMaxMainCode(iMaxMainCode);
		iJcicZ443Id.setAccount(iAccount);
		JcicZ443 chJcicZ443 = new JcicZ443();

		switch(iTranKey_Tmp) {
		case "1":
		    //檢核是否重複
		    chJcicZ443 = sJcicZ443Service.findById(iJcicZ443Id, titaVo);
		    this.info("TEST==="+chJcicZ443);
		    if (chJcicZ443!=null) {
		        throw new LogicException("E0005", "已有相同資料");
		    }
		    
		    iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
		    iJcicZ443.setJcicZ443Id(iJcicZ443Id);
		    iJcicZ443.setTranKey(iTranKey);
		    iJcicZ443.setUkey(iKey);
		    iJcicZ443.setIsMaxMain(iIsMaxMain);
		    iJcicZ443.setGuarantyType(iGuarantyType);
		    iJcicZ443.setLoanAmt(iLoanAmt);
		    iJcicZ443.setCreditAmt(iCreditAmt);
		    iJcicZ443.setPrincipal(iPrincipal);
		    iJcicZ443.setInterest(iInterest);
		    iJcicZ443.setPenalty(iPenalty);
		    iJcicZ443.setOther(iOther);
		    iJcicZ443.setTerminalPayAmt(iTerminalPayAmt);
		    iJcicZ443.setLatestPayAmt(iLatestPayAmt);
		    iJcicZ443.setFinalPayDay(iFinalPayDay);
		    iJcicZ443.setNotyetacQuit(iNotyetacQuit);
		    iJcicZ443.setMothPayDay(iMothPayDay);
		    iJcicZ443.setBeginDate(iBeginDate);
		    iJcicZ443.setEndDate(iEndDate);
		    try {
		        sJcicZ443Service.insert(iJcicZ443, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    break;
		case "2":
		    iKey = titaVo.getParam("Ukey");
		    iJcicZ443 = sJcicZ443Service.ukeyFirst(iKey, titaVo);
		    JcicZ443 uJcicZ443 = new JcicZ443();
		    uJcicZ443 = sJcicZ443Service.holdById(iJcicZ443.getJcicZ443Id(), titaVo);
		    if (uJcicZ443 == null) {
		        throw new LogicException("E0007", "無此更新資料");
		    }
		    uJcicZ443.setTranKey(iTranKey);
		    uJcicZ443.setIsMaxMain(iIsMaxMain);
		    uJcicZ443.setGuarantyType(iGuarantyType);
		    uJcicZ443.setLoanAmt(iLoanAmt);
		    uJcicZ443.setCreditAmt(iCreditAmt);
		    uJcicZ443.setPrincipal(iPrincipal);
		    uJcicZ443.setInterest(iInterest);
		    uJcicZ443.setPenalty(iPenalty);
		    uJcicZ443.setOther(iOther);
		    uJcicZ443.setTerminalPayAmt(iTerminalPayAmt);
		    uJcicZ443.setLatestPayAmt(iLatestPayAmt);
		    uJcicZ443.setFinalPayDay(iFinalPayDay);
		    uJcicZ443.setNotyetacQuit(iNotyetacQuit);
		    uJcicZ443.setMothPayDay(iMothPayDay);
		    uJcicZ443.setBeginDate(iBeginDate);
		    uJcicZ443.setEndDate(iEndDate);
		    uJcicZ443.setOutJcicTxtDate(0);
		    JcicZ443 oldJcicZ443 = (JcicZ443) iDataLog.clone(uJcicZ443);
		    try {
		        sJcicZ443Service.update(uJcicZ443, titaVo);
		    }catch (DBException e) {
		        throw new LogicException("E0005", "更生債權金額異動通知資料");
		    }
		    iDataLog.setEnv(titaVo, oldJcicZ443, uJcicZ443);
		    iDataLog.exec();
		    break;
		case "4": //需刷主管卡
		    iJcicZ443 = sJcicZ443Service.findById(iJcicZ443Id);
		    if (iJcicZ443 == null) {
		        throw new LogicException("E0008", "");
		    }
		    if (!titaVo.getHsupCode().equals("1")) {
		        iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
		    }
		    Slice<JcicZ443Log> dJcicLogZ443 = null;
		    dJcicLogZ443 = sJcicZ443LogService.ukeyEq(iJcicZ443.getUkey(), 0, Integer.MAX_VALUE, titaVo);
		    if (dJcicLogZ443 == null) {
		        //尚未開始寫入log檔之資料，主檔資料可刪除
		        try {
		            sJcicZ443Service.delete(iJcicZ443, titaVo);
		        }catch (DBException e) {
		            throw new LogicException("E0008", "更生債權金額異動通知資料");
		        }
		    }else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
		        //最近一筆之資料
		        JcicZ443Log iJcicZ443Log = dJcicLogZ443.getContent().get(0);				
			    iJcicZ443.setIsMaxMain(iJcicZ443Log.getIsMaxMain());
			    iJcicZ443.setGuarantyType(iJcicZ443Log.getGuarantyType());
			    iJcicZ443.setLoanAmt(iJcicZ443Log.getLoanAmt());
			    iJcicZ443.setCreditAmt(iJcicZ443Log.getCreditAmt());
			    iJcicZ443.setPrincipal(iJcicZ443Log.getPrincipal());
			    iJcicZ443.setInterest(iJcicZ443Log.getInterest());
			    iJcicZ443.setPenalty(iJcicZ443Log.getPenalty());
			    iJcicZ443.setOther(iJcicZ443Log.getOther());
			    iJcicZ443.setTerminalPayAmt(iJcicZ443Log.getTerminalPayAmt());
			    iJcicZ443.setLatestPayAmt(iJcicZ443Log.getLatestPayAmt());
			    iJcicZ443.setFinalPayDay(iJcicZ443Log.getFinalPayDay());
			    iJcicZ443.setNotyetacQuit(iJcicZ443Log.getNotyetacQuit());
			    iJcicZ443.setMothPayDay(iJcicZ443Log.getMothPayDay());
			    iJcicZ443.setBeginDate(iJcicZ443Log.getBeginDate());
			    iJcicZ443.setEndDate(iJcicZ443Log.getEndDate());
		        
		        try {
		            sJcicZ443Service.update(iJcicZ443, titaVo);
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

