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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ047Log;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ047LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ047Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8308")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8308 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ047LogService sJcicZ047LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8308 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iCivil323ExpAmt = Integer.valueOf(titaVo.getParam("Civil323ExpAmt"));
		int iExpLoanAmt = Integer.valueOf(titaVo.getParam("ExpLoanAmt"));
		int iCivil323CashAmt = Integer.valueOf(titaVo.getParam("Civil323CashAmt"));
		int iCashCardAmt = Integer.valueOf(titaVo.getParam("CashCardAmt"));
		int iCivil323CreditAmt = Integer.valueOf(titaVo.getParam("Civil323CreditAmt"));
		int iCreditCardAmt = Integer.valueOf(titaVo.getParam("CreditCardAmt"));
		BigDecimal iCivil323Amt = new BigDecimal(titaVo.getParam("Civil323Amt"));
		BigDecimal iTotalAmt = new BigDecimal(titaVo.getParam("TotalAmt"));
		int iPassDate = Integer.valueOf(titaVo.getParam("PassDate"));
		int iInterviewDate = Integer.valueOf(titaVo.getParam("InterviewDate"));
		int iSignDate = Integer.valueOf(titaVo.getParam("SignDate"));
		int iLimitDate = Integer.valueOf(titaVo.getParam("LimitDate"));
		int iFirstPayDate = Integer.valueOf(titaVo.getParam("FirstPayDate"));
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		String iPayAccount = String.valueOf(titaVo.getParam("PayAccount"));
		String iPostAddr = String.valueOf(titaVo.getParam("PostAddr"));
		String iGradeType = String.valueOf(titaVo.getParam("GradeType"));
		int iPayLastAmt = Integer.valueOf(titaVo.getParam("PayLastAmt"));
		int iPeriod2 = Integer.valueOf(titaVo.getParam("Period2"));
		BigDecimal iRate2 = new BigDecimal(titaVo.getParam("Rate2"));
		int iMonthPayAmt2 = Integer.valueOf(titaVo.getParam("MonthPayAmt2"));
		int iPayLastAmt2 = Integer.valueOf(titaVo.getParam("PayLastAmt2"));

		String iKey = "";
		// JcicZ047, JcicZ044
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);// 債務人IDN
		iJcicZ047Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ047Id.setRcDate(iRcDate);
		JcicZ047 chJcicZ047 = new JcicZ047();
		JcicZ044 iJcicZ044 = new JcicZ044();
		JcicZ044Id iJcicZ044Id = new JcicZ044Id();
		iJcicZ044Id.setCustId(iCustId);// 債務人IDN
		iJcicZ044Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ044Id.setRcDate(iRcDate);

		// 檢核項目(D-19)
		if (!"4".equals(iTranKey_Tmp)) {

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 1.3 start 需檢核最大債權金融機構是否有報送過'44':請求同意債務清償方案通知資料，若無報送則予以剔退
				iJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id, titaVo);
				if (iJcicZ044 == null) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "最大債權金融機構未曾報送過(44)請求同意債務清償方案通知資料.");
					} else {
						throw new LogicException("E0007", "最大債權金融機構未曾報送過(44)請求同意債務清償方案通知資料.");
					}
				} // 1.3 end

				// 1.4 start 第7欄期數，第8欄利率需與最近一次'44':請求同意債務清償方案通知資料"對應值一致
				else if (iPeriod != iJcicZ044.getPeriod() || iRate.compareTo(iJcicZ044.getRate()) != 0) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "期數，利率需與(44)請求同意債務清償方案通知資料最近一筆報送的資料對應值一致.");
					} else {
						throw new LogicException("E0007", "期數，利率需與(44)請求同意債務清償方案通知資料最近一筆報送的資料對應值一致.");
					}
				} // 1.4 end
			}

			// 1.5 檢核第9~14欄若與所有原債權金融機構回報'42':「回報無擔保債權金額資料」債權金額不同時，則予剔退.***J

//			// 1.6.1 start 第15欄「依民法第323條計算之債務總金額」需等於信用貸款+現金卡+信用卡依民法第323條計算之債務金額合計
//			if (iCivil323Amt.compareTo(new BigDecimal(iCivil323ExpAmt + iCivil323CashAmt + iCivil323CreditAmt)) != 0) {
//				throw new LogicException("E0005", "「依民法第323條計算之債務總金額」需等於依民法第323條計算之信用貸款+現金卡+信用卡債務金額合計.");
//			}
//			// 1.6.2 start 第16欄「簽約總債務金額」需等於信用貸款+現金卡+信用卡債務金額合計
//			if (iTotalAmt.compareTo(new BigDecimal(iExpLoanAmt + iCashCardAmt + iCreditCardAmt)) != 0) {
//				throw new LogicException("E0005", "「簽約總債務金額」需等於信用貸款+現金卡+信用卡債務金額合計.");
//			} // 1.6 end
//
//			// 1.7 start 簽約完成日期需大於或等於協議完成日期--->(前端檢核)
			// 1.7.1 start 簽約完成日期必定要大於協議日期
			if(iSignDate != 0 && iSignDate < iPassDate) {
				throw new LogicException("E0015", "簽約完成日期需大於協議日期");
			}
			
//			// 1.8 start 首期應繳款日需大於或等簽約完成日期--->(前端檢核)
//			
//			// 1.9, 1.11, 1.13 start 若第25欄「屬二階段還款方案之階段註記」填報1者(第一階段)，3條件
//			if ("1".equals(iGradeType)) {
//				// 1.9.第7欄期數需填報固定值072.
//				if (iPeriod != 72) {
//					throw new LogicException("E0005", "屬二階段還款方案之第一階段，還款期數需填報固定值'072'.");
//				}
//				// 1.11.第26欄「第一階段最後一期應繳金額」需等於第16欄「簽約總債務金額」減第22欄「月付金」乘以71期.
//				if (iTotalAmt.compareTo(new BigDecimal(iPayLastAmt + iMonthPayAmt * 71)) != 0) {
//					throw new LogicException("E0005", "屬二階段還款方案之第一階段，「第一階段最後一期應繳金額」需等於「簽約總債務金額」減「月付金」乘以71期.");
//				}
//				// 1.13.第27欄(含)以後需空白(屬第二階段相關內容).
//				if (iPeriod2 != 0 || (iRate2 != null && iRate2.compareTo(BigDecimal.ZERO) != 0) || iMonthPayAmt2 != 0
//						|| iPayLastAmt2 != 0) {
//					throw new LogicException("E0005", "屬二階段還款方案之第一階段，第二階段相關內容需空白.");
//				}
//			} // 1.9, 1.11, 1.13 end
//
//			// 1.10 start 若第25欄「屬二階段還款方案之階段註記」填報1或2者，第8欄利率需填報固定值00.00.
//			if (("1".equals(iGradeType) || "2".equals(iGradeType)) && iRate.compareTo(BigDecimal.ZERO) != 0) {
//				throw new LogicException("E0005", "屬二階段還款方案之第一/二階段，利率需填報固定值00.00.");
//			} // 1.10 end
//
//			// 1.12 start 若第25欄「屬二階段還款之階段註記」空白者，第26欄(含)以後需空白(屬第一/二階段相關內容).
//			if ((iGradeType.trim().isEmpty() || "0".equals(iGradeType))
//					&& (iPayLastAmt != 0 || iPeriod2 != 0 || (iRate2 != null && iRate2.compareTo(BigDecimal.ZERO) != 0)
//							|| iMonthPayAmt2 != 0 || iPayLastAmt2 != 0)) {
//				throw new LogicException("E0005", "未註記二階段還款方案，二階段還款方案的相關內容需空白.");
//			} // 1.12 end
//
//			// 1.14 若第25欄「屬二階段還款方案之階段註記」填報2者(第二階段)，第27欄(含)以後第二階段相關內容不可空白
//			if ("2".equals(iGradeType)
//					&& (iPeriod2 == 0 || iRate2 == null || iMonthPayAmt2 == 0 || iPayLastAmt2 == 0)) {
//				throw new LogicException("E0005", "屬二階段還款方案之第二階段，第二階段相關內容不可空白.");
//			} // 1.14 end

			// 2.本中心以第17欄「協議完成日」有值且第19欄「簽約完成日期」為空白時為產出協議書之依據，並於接獲報送'47'次日，將檔名為BBBYYYMMDD+(10碼債務人IDN).pdf按身份證排充電，並自動將當日產生之pdf壓縮成.zip傳輸至最大債權金融機構DTO報送帳號，最大債權金融機構次日即可下載協議書等相關文件。***

			// 檢核項目end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ047
			chJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
			if (chJcicZ047 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ047.setJcicZ047Id(iJcicZ047Id);
			iJcicZ047.setTranKey(iTranKey);
			iJcicZ047.setPeriod(iPeriod);
			iJcicZ047.setRate(iRate);
			iJcicZ047.setCivil323ExpAmt(iCivil323ExpAmt);
			iJcicZ047.setExpLoanAmt(iExpLoanAmt);
			iJcicZ047.setCivil323CashAmt(iCivil323CashAmt);
			iJcicZ047.setCashCardAmt(iCashCardAmt);
			iJcicZ047.setCivil323CreditAmt(iCivil323CreditAmt);
			iJcicZ047.setCreditCardAmt(iCreditCardAmt);
			iJcicZ047.setCivil323Amt(iCivil323Amt);
			iJcicZ047.setTotalAmt(iTotalAmt);
			iJcicZ047.setPassDate(iPassDate);
			iJcicZ047.setInterviewDate(iInterviewDate);
			iJcicZ047.setSignDate(iSignDate);
			iJcicZ047.setLimitDate(iLimitDate);
			iJcicZ047.setFirstPayDate(iFirstPayDate);
			iJcicZ047.setMonthPayAmt(iMonthPayAmt);
			iJcicZ047.setPayAccount(iPayAccount);
			iJcicZ047.setPostAddr(iPostAddr);
			iJcicZ047.setGradeType(iGradeType);
			iJcicZ047.setPayLastAmt(iPayLastAmt);
			iJcicZ047.setPeriod2(iPeriod2);
			iJcicZ047.setRate2(iRate2);
			iJcicZ047.setMonthPayAmt2(iMonthPayAmt2);
			iJcicZ047.setPayLastAmt2(iPayLastAmt2);
			iJcicZ047.setUkey(iKey);
			try {
				sJcicZ047Service.insert(iJcicZ047, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			String iUkey = titaVo.getParam("Ukey");
			this.info("iUkey     = " + iUkey);
			iKey = titaVo.getParam("Ukey");
			iJcicZ047 = sJcicZ047Service.ukeyFirst(iKey, titaVo);
			JcicZ047 uJcicZ047 = new JcicZ047();
			uJcicZ047 = sJcicZ047Service.holdById(iJcicZ047.getJcicZ047Id(), titaVo);
			if (uJcicZ047 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ047 oldJcicZ047 = (JcicZ047) iDataLog.clone(uJcicZ047);
			uJcicZ047.setTranKey(iTranKey);
			uJcicZ047.setPeriod(iPeriod);
			uJcicZ047.setRate(iRate);
			uJcicZ047.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ047.setExpLoanAmt(iExpLoanAmt);
			uJcicZ047.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ047.setCashCardAmt(iCashCardAmt);
			uJcicZ047.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ047.setCreditCardAmt(iCreditCardAmt);
			uJcicZ047.setCivil323Amt(iCivil323Amt);
			uJcicZ047.setTotalAmt(iTotalAmt);
			uJcicZ047.setPassDate(iPassDate);
			uJcicZ047.setInterviewDate(iInterviewDate);
			uJcicZ047.setSignDate(iSignDate);
			uJcicZ047.setLimitDate(iLimitDate);
			uJcicZ047.setFirstPayDate(iFirstPayDate);
			uJcicZ047.setMonthPayAmt(iMonthPayAmt);
			uJcicZ047.setPayAccount(iPayAccount);
			uJcicZ047.setPostAddr(iPostAddr);
			uJcicZ047.setGradeType(iGradeType);
			uJcicZ047.setPayLastAmt(iPayLastAmt);
			uJcicZ047.setPeriod2(iPeriod2);
			uJcicZ047.setRate2(iRate2);
			uJcicZ047.setMonthPayAmt2(iMonthPayAmt2);
			uJcicZ047.setPayLastAmt2(iPayLastAmt2);
			uJcicZ047.setOutJcicTxtDate(0);
			try {
				sJcicZ047Service.update(uJcicZ047, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ047, uJcicZ047);
			iDataLog.exec();
			break;
			//2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			String iUkey2 = titaVo.getParam("Ukey");
			this.info("iUkey     = " + iUkey2);
			iKey = titaVo.getParam("Ukey");
			iJcicZ047 = sJcicZ047Service.ukeyFirst(iKey, titaVo);
			JcicZ047 uJcicZ0472 = new JcicZ047();
			uJcicZ0472 = sJcicZ047Service.holdById(iJcicZ047.getJcicZ047Id(), titaVo);
			if (uJcicZ0472 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}

			iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id);
			if (iJcicZ047 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate2 = iJcicZ047.getOutJcicTxtDate();
			this.info("JcicDate2    = " + JcicDate2);
			if (JcicDate2 != 0) {
				throw new LogicException("E0004", "刪除資料不存在");
			}
			
			JcicZ047 oldJcicZ0472 = (JcicZ047) iDataLog.clone(uJcicZ0472);
			uJcicZ0472.setTranKey(iTranKey);
			uJcicZ0472.setPeriod(iPeriod);
			uJcicZ0472.setRate(iRate);
			uJcicZ0472.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ0472.setExpLoanAmt(iExpLoanAmt);
			uJcicZ0472.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ0472.setCashCardAmt(iCashCardAmt);
			uJcicZ0472.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ0472.setCreditCardAmt(iCreditCardAmt);
			uJcicZ0472.setCivil323Amt(iCivil323Amt);
			uJcicZ0472.setTotalAmt(iTotalAmt);
			uJcicZ0472.setPassDate(iPassDate);
			uJcicZ0472.setInterviewDate(iInterviewDate);
			uJcicZ0472.setSignDate(iSignDate);
			uJcicZ0472.setLimitDate(iLimitDate);
			uJcicZ0472.setFirstPayDate(iFirstPayDate);
			uJcicZ0472.setMonthPayAmt(iMonthPayAmt);
			uJcicZ0472.setPayAccount(iPayAccount);
			uJcicZ0472.setPostAddr(iPostAddr);
			uJcicZ0472.setGradeType(iGradeType);
			uJcicZ0472.setPayLastAmt(iPayLastAmt);
			uJcicZ0472.setPeriod2(iPeriod2);
			uJcicZ0472.setRate2(iRate2);
			uJcicZ0472.setMonthPayAmt2(iMonthPayAmt2);
			uJcicZ0472.setPayLastAmt2(iPayLastAmt2);
			uJcicZ0472.setOutJcicTxtDate(0);
			
			Slice<JcicZ047Log> dJcicLogZ047 = null;
			dJcicLogZ047 = sJcicZ047LogService.ukeyEq(iJcicZ047.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ047 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ047Service.delete(iJcicZ047, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ047Log iJcicZ047Log = dJcicLogZ047.getContent().get(0);
				iJcicZ047.setPeriod(iJcicZ047Log.getPeriod());
				iJcicZ047.setRate(iJcicZ047Log.getRate());
				iJcicZ047.setCivil323ExpAmt(iJcicZ047Log.getCivil323ExpAmt());
				iJcicZ047.setExpLoanAmt(iJcicZ047Log.getExpLoanAmt());
				iJcicZ047.setCivil323CashAmt(iJcicZ047Log.getCivil323CashAmt());
				iJcicZ047.setCashCardAmt(iJcicZ047Log.getCashCardAmt());
				iJcicZ047.setCivil323CreditAmt(iJcicZ047Log.getCivil323CreditAmt());
				iJcicZ047.setCreditCardAmt(iJcicZ047Log.getCreditCardAmt());
				iJcicZ047.setCivil323Amt(iJcicZ047Log.getCivil323Amt());
				iJcicZ047.setTotalAmt(iJcicZ047Log.getTotalAmt());
				iJcicZ047.setPassDate(iJcicZ047Log.getPassDate());
				iJcicZ047.setInterviewDate(iJcicZ047Log.getInterviewDate());
				iJcicZ047.setSignDate(iJcicZ047Log.getSignDate());
				iJcicZ047.setLimitDate(iJcicZ047Log.getLimitDate());
				iJcicZ047.setFirstPayDate(iJcicZ047Log.getFirstPayDate());
				iJcicZ047.setMonthPayAmt(iJcicZ047Log.getMonthPayAmt());
				iJcicZ047.setPayAccount(iJcicZ047Log.getPayAccount());
				iJcicZ047.setPostAddr(iJcicZ047Log.getPostAddr());
				iJcicZ047.setGradeType(iJcicZ047Log.getGradeType());
				iJcicZ047.setPayLastAmt(iJcicZ047Log.getPayLastAmt());
				iJcicZ047.setPeriod2(iJcicZ047Log.getPeriod2());
				iJcicZ047.setRate2(iJcicZ047Log.getRate2());
				iJcicZ047.setMonthPayAmt2(iJcicZ047Log.getMonthPayAmt2());
				iJcicZ047.setPayLastAmt2(iJcicZ047Log.getPayLastAmt2());
				iJcicZ047.setTranKey(iJcicZ047Log.getTranKey());
				iJcicZ047.setOutJcicTxtDate(iJcicZ047Log.getOutJcicTxtDate());
				try {
					sJcicZ047Service.update(iJcicZ047, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0472, uJcicZ0472);
			iDataLog.exec();
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
