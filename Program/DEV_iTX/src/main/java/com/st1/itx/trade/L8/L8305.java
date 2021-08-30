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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.service.JcicZ044LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ044Service;

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

@Service("L8305")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8305 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ044LogService sJcicZ044LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8305 ");
		this.totaVo.init(titaVo);
		
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey=titaVo.getParam("TranKey"); //交易代碼
		String iCustId=titaVo.getParam("CustId");//債務人IDN
		String iSubmitKey=titaVo.getParam("SubmitKey");//報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iDebtCode = titaVo.getParam("DebtCode");
		int iNonGageAmt = Integer.valueOf(titaVo.getParam("NonGageAmt"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iMonthPayAmt= Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		int iReceYearIncome= Integer.valueOf(titaVo.getParam("ReceYearIncome"));
		int iReceYear= Integer.valueOf(titaVo.getParam("ReceYear"));
		int iReceYear2Income= Integer.valueOf(titaVo.getParam("ReceYear2Income"));
		int iReceYear2= Integer.valueOf(titaVo.getParam("ReceYear2"));
		int iCurrentMonthIncome= Integer.valueOf(titaVo.getParam("CurrentMonthIncome"));
		int iLivingCost= Integer.valueOf(titaVo.getParam("LivingCost"));
		String iCompName =titaVo.getParam("CompName");
		String iCompId =titaVo.getParam("CompId");
		int iCarCnt = Integer.valueOf(titaVo.getParam("CarCnt"));
		int iHouseCnt  = Integer.valueOf(titaVo.getParam("HouseCnt"));
		int iLandCnt = Integer.valueOf(titaVo.getParam("LandCnt"));
		int iChildCnt = Integer.valueOf(titaVo.getParam("ChildCnt"));
		BigDecimal iChildRate = new BigDecimal(titaVo.getParam("ChildRate"));
		int iParentCnt= Integer.valueOf(titaVo.getParam("ParentCnt"));
		BigDecimal iParentRate = new BigDecimal(titaVo.getParam("ParentRate"));
		int iMouthCnt= Integer.valueOf(titaVo.getParam("MouthCnt"));
		BigDecimal iMouthRate = new BigDecimal(titaVo.getParam("MouthRate"));
		String iGradeType = titaVo.getParam("GradeType");
		int iPayLastAmt= Integer.valueOf(titaVo.getParam("PayLastAmt"));
		int iPeriod2= Integer.valueOf(titaVo.getParam("MouthCnt"));
		BigDecimal iRate2 = new BigDecimal(titaVo.getParam("Rate2"));
		int iMonthPayAmt2= Integer.valueOf(titaVo.getParam("MonthPayAmt2"));
        int iPayLastAmt2= Integer.valueOf(titaVo.getParam("PayLastAmt2"));
		String iKey = "";
		//JcicZ044
		JcicZ044 iJcicZ044 = new JcicZ044();
		JcicZ044Id iJcicZ044Id = new JcicZ044Id();
		iJcicZ044Id.setCustId(iCustId);//債務人IDN
		iJcicZ044Id.setSubmitKey(iSubmitKey);//報送單位代號
		iJcicZ044Id.setRcDate(iRcDate);
		JcicZ044 chJcicZ044 = new JcicZ044();

		switch(iTranKey_Tmp) {
		case "1":
			//檢核是否重複，並寫入JcicZ044
			chJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id, titaVo);
			if (chJcicZ044!=null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");          
            iJcicZ044.setJcicZ044Id(iJcicZ044Id);
			iJcicZ044.setTranKey(iTranKey);
            iJcicZ044.setDebtCode(iDebtCode);
            iJcicZ044.setNonGageAmt(iNonGageAmt);
            iJcicZ044.setPeriod(iPeriod);
            iJcicZ044.setRate(iRate);
            iJcicZ044.setMonthPayAmt(iMonthPayAmt);
            iJcicZ044.setReceYearIncome(iReceYearIncome);
            iJcicZ044.setReceYear(iReceYear);
            iJcicZ044.setReceYear2Income(iReceYear2Income);
            iJcicZ044.setReceYear2(iReceYear2);
            iJcicZ044.setCurrentMonthIncome(iCurrentMonthIncome);
            iJcicZ044.setLivingCost(iLivingCost);
            iJcicZ044.setCompName(iCompName);
            iJcicZ044.setCompId(iCompId);
            iJcicZ044.setCarCnt(iCarCnt);
            iJcicZ044.setHouseCnt(iHouseCnt);
            iJcicZ044.setLandCnt(iLandCnt);
            iJcicZ044.setChildCnt(iChildCnt);
            iJcicZ044.setChildRate(iChildRate);
            iJcicZ044.setParentCnt(iParentCnt);
            iJcicZ044.setParentRate(iParentRate);
            iJcicZ044.setMouthCnt(iMouthCnt);
            iJcicZ044.setMouthRate(iMouthRate);
            iJcicZ044.setGradeType(iGradeType);
            iJcicZ044.setPayLastAmt(iPayLastAmt);
            iJcicZ044.setPeriod2(iPeriod2);
            iJcicZ044.setRate2(iRate2);
            iJcicZ044.setMonthPayAmt2(iMonthPayAmt2);
            iJcicZ044.setPayLastAmt2(iPayLastAmt2);
			iJcicZ044.setUkey(iKey);
			try {
				sJcicZ044Service.insert(iJcicZ044, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ044 = sJcicZ044Service.ukeyFirst(iKey, titaVo);
			JcicZ044 uJcicZ044 = new JcicZ044();
			uJcicZ044 = sJcicZ044Service.holdById(iJcicZ044.getJcicZ044Id(), titaVo);
			if (uJcicZ044 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}			
			uJcicZ044.setTranKey(iTranKey);
            uJcicZ044.setDebtCode(iDebtCode);
            uJcicZ044.setNonGageAmt(iNonGageAmt);
            uJcicZ044.setPeriod(iPeriod);
            uJcicZ044.setRate(iRate);
            uJcicZ044.setMonthPayAmt(iMonthPayAmt);
            uJcicZ044.setReceYearIncome(iReceYearIncome);
            uJcicZ044.setReceYear(iReceYear);
            uJcicZ044.setReceYear2Income(iReceYear2Income);
            uJcicZ044.setReceYear2(iReceYear2);
            uJcicZ044.setCurrentMonthIncome(iCurrentMonthIncome);
            uJcicZ044.setLivingCost(iLivingCost);
            uJcicZ044.setCompName(iCompName);
            uJcicZ044.setCompId(iCompId);
            uJcicZ044.setCarCnt(iCarCnt);
            uJcicZ044.setHouseCnt(iHouseCnt);
            uJcicZ044.setLandCnt(iLandCnt);
            uJcicZ044.setChildCnt(iChildCnt);
            uJcicZ044.setChildRate(iChildRate);
            uJcicZ044.setParentCnt(iParentCnt);
            uJcicZ044.setParentRate(iParentRate);
            uJcicZ044.setMouthCnt(iMouthCnt);
            uJcicZ044.setMouthRate(iMouthRate);
            uJcicZ044.setGradeType(iGradeType);
            uJcicZ044.setPayLastAmt(iPayLastAmt);
            uJcicZ044.setPeriod2(iPeriod2);
            uJcicZ044.setRate2(iRate2);
            uJcicZ044.setMonthPayAmt2(iMonthPayAmt2);
            uJcicZ044.setPayLastAmt2(iPayLastAmt2);
			uJcicZ044.setOutJcicTxtDate(0);
			JcicZ044 oldJcicZ044 = (JcicZ044) iDataLog.clone(uJcicZ044);
			try {
				sJcicZ044Service.update(uJcicZ044, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ044, uJcicZ044);
			iDataLog.exec();
			break;
		case "4": //需刷主管卡
			iJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id);
			if (iJcicZ044 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer,titaVo,"0004","");
			}
			Slice<JcicZ044Log> dJcicLogZ044 = null;
			dJcicLogZ044 = sJcicZ044LogService.ukeyEq(iJcicZ044.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ044 == null) {
				//尚未開始寫入log檔之資料，主檔資料可刪除
			try {
				sJcicZ044Service.delete(iJcicZ044, titaVo);
			}catch (DBException e) {
				throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}else {//已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
				//最近一筆之資料
				JcicZ044Log iJcicZ044Log = dJcicLogZ044.getContent().get(0);
                iJcicZ044.setDebtCode(iJcicZ044Log.getDebtCode());
                iJcicZ044.setNonGageAmt(iJcicZ044Log.getNonGageAmt());
                iJcicZ044.setPeriod(iJcicZ044Log.getPeriod());
                iJcicZ044.setRate(iJcicZ044Log.getRate());
                iJcicZ044.setMonthPayAmt(iJcicZ044Log.getMonthPayAmt());
                iJcicZ044.setReceYearIncome(iJcicZ044Log.getReceYearIncome());
                iJcicZ044.setReceYear(iJcicZ044Log.getReceYear());
                iJcicZ044.setReceYear2Income(iJcicZ044Log.getReceYear2Income());
                iJcicZ044.setReceYear2(iJcicZ044Log.getReceYear2());
                iJcicZ044.setCurrentMonthIncome(iJcicZ044Log.getCurrentMonthIncome());
                iJcicZ044.setLivingCost(iJcicZ044Log.getLivingCost());
                iJcicZ044.setCompName(iJcicZ044Log.getCompName());
                iJcicZ044.setCompId(iJcicZ044Log.getCompId());
                iJcicZ044.setCarCnt(iJcicZ044Log.getCarCnt());
                iJcicZ044.setHouseCnt(iJcicZ044Log.getHouseCnt());
                iJcicZ044.setLandCnt(iJcicZ044Log.getLandCnt());
                iJcicZ044.setChildCnt(iJcicZ044Log.getChildCnt());
                iJcicZ044.setChildRate(iJcicZ044Log.getChildRate());
                iJcicZ044.setParentCnt(iJcicZ044Log.getParentCnt());
                iJcicZ044.setParentRate(iJcicZ044Log.getParentRate());
                iJcicZ044.setMouthCnt(iJcicZ044Log.getMouthCnt());
                iJcicZ044.setMouthRate(iJcicZ044Log.getMouthRate());
                iJcicZ044.setGradeType(iJcicZ044Log.getGradeType());
                iJcicZ044.setPayLastAmt(iJcicZ044Log.getPayLastAmt());
                iJcicZ044.setPeriod2(iJcicZ044Log.getPeriod2());
                iJcicZ044.setRate2(iJcicZ044Log.getRate2());
                iJcicZ044.setMonthPayAmt2(iJcicZ044Log.getMonthPayAmt2());
                iJcicZ044.setPayLastAmt2(iJcicZ044Log.getPayLastAmt2());
				iJcicZ044.setTranKey(iJcicZ044Log.getTranKey());
				iJcicZ044.setOutJcicTxtDate(iJcicZ044Log.getOutJcicTxtDate());
				try {
					sJcicZ044Service.update(iJcicZ044, titaVo);
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
