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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ052;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ048Service;
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
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ044LogService sJcicZ044LogService;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8305 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iDebtCode = titaVo.getParam("DebtCode");
		int iNonGageAmt = Integer.valueOf(titaVo.getParam("NonGageAmt"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		int iReceYearIncome = Integer.valueOf(titaVo.getParam("ReceYearIncome"));
		int iReceYear = Integer.valueOf(titaVo.getParam("ReceYear"));
		int iReceYear2Income = Integer.valueOf(titaVo.getParam("ReceYear2Income"));
		int iReceYear2 = Integer.valueOf(titaVo.getParam("ReceYear2"));
		int iCurrentMonthIncome = Integer.valueOf(titaVo.getParam("CurrentMonthIncome"));
		int iLivingCost = Integer.valueOf(titaVo.getParam("LivingCost"));
		String iCompName = titaVo.getParam("CompName");
		String iCompId = titaVo.getParam("CompId");
		int iCarCnt = Integer.valueOf(titaVo.getParam("CarCnt"));
		int iHouseCnt = Integer.valueOf(titaVo.getParam("HouseCnt"));
		int iLandCnt = Integer.valueOf(titaVo.getParam("LandCnt"));
		int iChildCnt = Integer.valueOf(titaVo.getParam("ChildCnt"));
		BigDecimal iChildRate = new BigDecimal(titaVo.getParam("ChildRate"));
		int iParentCnt = Integer.valueOf(titaVo.getParam("ParentCnt"));
		BigDecimal iParentRate = new BigDecimal(titaVo.getParam("ParentRate"));
		int iMouthCnt = Integer.valueOf(titaVo.getParam("MouthCnt"));
		BigDecimal iMouthRate = new BigDecimal(titaVo.getParam("MouthRate"));
		String iGradeType = titaVo.getParam("GradeType");
		int iPayLastAmt = Integer.valueOf(titaVo.getParam("PayLastAmt"));
		int iPeriod2 = Integer.valueOf(titaVo.getParam("Period2"));
		BigDecimal iRate2 = new BigDecimal(titaVo.getParam("Rate2"));
		int iMonthPayAmt2 = Integer.valueOf(titaVo.getParam("MonthPayAmt2"));
		int iPayLastAmt2 = Integer.valueOf(titaVo.getParam("PayLastAmt2"));
		String iKey = "";

		// JcicZ044, JcicZ040, JcicZ047, JcicZ048, JcicZ052
		JcicZ044 iJcicZ044 = new JcicZ044();
		JcicZ044Id iJcicZ044Id = new JcicZ044Id();
		iJcicZ044Id.setCustId(iCustId);// 債務人IDN
		iJcicZ044Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ044Id.setRcDate(iRcDate);
		JcicZ044 chJcicZ044 = new JcicZ044();
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);// 債務人IDN
		iJcicZ047Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ047Id.setRcDate(iRcDate);

		// 檢核項目(D-12)
		if (!"4".equals(iTranKey_Tmp)) {
			if ("A".equals(iTranKey)) {
				// 1.2 start 完整key值未曾報送過'40':「前置協商受理申請暨請求回報債權通知」則予以剔退
				iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
				if (iJcicZ040 == null) {
					throw new LogicException("E0005", "未曾報送過'40':「前置協商受理申請暨請求回報債權通知」.");
				} // 1.2 end
			}

			// 1.3 start 填報本表必須隨同填報'48':「債務人基本資料」，否則則予以剔退.***

			// 1.4 檢核全體債權金融機構均已回報債權後，最大債權金融機構才能報送本表('44':請求同意債務清償方案通知資料).***J

			// 1.5 start 此檔案新增後，於報送'47'「金融機構無擔保債務協議資料」前，僅能異動1次。但若最大債權機構啟動'52'後，將不受此限.
			if ("C".equals(iTranKey)) {
				Slice<JcicZ052> sJcicZ052 = sJcicZ052Service.otherEq(iSubmitKey, iCustId, iRcDate + 19110000, 0,
						Integer.MAX_VALUE, titaVo);
				if (sJcicZ052 == null) {
					iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
					if (iJcicZ047 == null) {
						Slice<JcicZ044Log> sJcicZ044Log = sJcicZ044LogService.ukeyEq(titaVo.getParam("Ukey"), 0,
								Integer.MAX_VALUE, titaVo);
						if (sJcicZ044Log != null) {
							if (sJcicZ044Log.getSize() > 1) {
								throw new LogicException("E0005", "報送'47':金融機構無擔保債務協議資料前，僅能異動1次.");
							}
						}
					}
				}
			} // 1.5 end

			// 1.6, 1.7, 1.8, 1.10 start 若第31欄「屬二階段還款方案之階段註記」填報1者(第一階段)，4條件
			if ("1".equals(iGradeType)) {
				// 6.第9欄還款期數需填報固定值72.
				if (iPeriod != 72) {
					throw new LogicException("E0005", "屬二階段還款方案之第一階段，還款期數需填報固定值72.");
				}
				// 7.第10欄利率需填報固定值00.00.
				if (iRate.compareTo(BigDecimal.ZERO) != 0) {
					throw new LogicException("E0005", "屬二階段還款方案之第一階段，利率需填報固定值00.00.");
				}
				// 8.第32欄「第一階段最後一期應繳金額」需等於第8欄「無擔保金融債務協商總金額」減第11欄「協商方案估計月付金」乘以71期.
				if (iPayLastAmt != (iNonGageAmt - iMonthPayAmt * 71)) {
					throw new LogicException("E0005",
							"屬二階段還款方案之第一階段，「第一階段最後一期應繳金額」需等於「無擔保金融債務協商總金額」減「協商方案估計月付金」乘以71期.");
				}
				// 1.10.第33欄(含)以後需空白(屬第二階段相關內容).
				if (iPeriod2 != 0 || (iRate2 != null && iRate2.compareTo(BigDecimal.ZERO) != 0) || iMonthPayAmt2 != 0
						|| iPayLastAmt2 != 0) {
					throw new LogicException("E0005", "屬二階段還款方案之第一階段，第二階段相關內容需空白.");
				}
			} // 1.6, 1.7, 1.8, 1.10 end

			// 1.9 start 若第31欄「屬二階段還款之階段註記」空白者，第32欄(含)以後需空白(屬第一/二階段相關內容).
			if ((iGradeType.trim().isEmpty() || "0".equals(iGradeType))
					&& (iPayLastAmt != 0 || iPeriod2 != 0 || (iRate2 != null && iRate2.compareTo(BigDecimal.ZERO) != 0)
							|| iMonthPayAmt2 != 0 || iPayLastAmt2 != 0)) {
				throw new LogicException("E0005", "未註記二階段還款方案，「第一階段最後一期應繳金額」及 二階段還款方案的相關內容需空白.");
			} // 1.9 end

			// 1.11 若第31欄「屬二階段還款方案之階段註記」填報2者(第二階段)，第33欄(含)以後第二階段相關內容不可空白
			if ("2".equals(iGradeType)
					&& (iPeriod2 == 0 || iRate2 == null || iMonthPayAmt2 == 0 || iPayLastAmt2 == 0)) {
				throw new LogicException("E0005", "屬二階段還款方案之第二階段，第二階段相關內容不可空白.");
			} // 1.11 end

			// 2.最大債權金融機構於報送「協商開始暨停催通知資料」後，若超過15日仍未報送本檔案格式者，本中心即將此部分揭露於Z99前前置協商相關作業提醒資訊.***J

			// 3.因協商不成立致須回報本資料檔案格式時，則須以交易代碼「'X'補件」報送之，本中心即可接受金融機構報送之前已因協商不成立而結案之案件資料.***J

			// 4.當本資料檔案格式交易代碼為「'X'補件」時，則第11欄「協商方案估計月付金」、第25~30欄「扶養人」相關，為非必要填報欄位.***J

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ044
			chJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id, titaVo);
			if (chJcicZ044 != null) {
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
			} catch (DBException e) {
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
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ044, uJcicZ044);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id);
			if (iJcicZ044 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ044Log> dJcicLogZ044 = null;
			dJcicLogZ044 = sJcicZ044LogService.ukeyEq(iJcicZ044.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ044 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ044Service.delete(iJcicZ044, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
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
				} catch (DBException e) {
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
