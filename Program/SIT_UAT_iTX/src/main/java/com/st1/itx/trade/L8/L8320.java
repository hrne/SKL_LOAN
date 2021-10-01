package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.domain.JcicZ062Log;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.service.JcicZ062LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.JcicZ063Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

@Service("L8320")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8320 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ062LogService sJcicZ062LogService;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	@Autowired
	public DateUtil iDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8320 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		int iCompletePeriod = Integer.valueOf(titaVo.getParam("CompletePeriod"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iExpBalanceAmt = Integer.valueOf(titaVo.getParam("ExpBalanceAmt"));
		int iCashBalanceAmt = Integer.valueOf(titaVo.getParam("CashBalanceAmt"));
		int iCreditBalanceAmt = Integer.valueOf(titaVo.getParam("CreditBalanceAmt"));
		BigDecimal iChaRepayAmt = new BigDecimal(titaVo.getParam("ChaRepayAmt"));
		int iChaRepayAgreeDate = Integer.valueOf(titaVo.getParam("ChaRepayAgreeDate"));
		int iChaRepayViewDate = Integer.valueOf(titaVo.getParam("ChaRepayViewDate"));
		int iChaRepayEndDate = Integer.valueOf(titaVo.getParam("ChaRepayEndDate"));
		int iChaRepayFirstDate = Integer.valueOf(titaVo.getParam("ChaRepayFirstDate"));
		String iPayAccount = titaVo.getParam("PayAccount");
		String iPostAddr = titaVo.getParam("PostAddr");
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		String iGradeType = titaVo.getParam("GradeType");
		int iPeriod2 = Integer.valueOf(titaVo.getParam("Period2"));
		BigDecimal iRate2 = new BigDecimal(titaVo.getParam("Rate2"));
		int iMonthPayAmt2 = Integer.valueOf(titaVo.getParam("MonthPayAmt2"));
		String iKey = "";
		// JcicZ062, JcicZ060,
		JcicZ062 iJcicZ062 = new JcicZ062();
		JcicZ062Id iJcicZ062Id = new JcicZ062Id();
		iJcicZ062Id.setSubmitKey(iSubmitKey);
		iJcicZ062Id.setCustId(iCustId);
		iJcicZ062Id.setRcDate(iRcDate);
		iJcicZ062Id.setChangePayDate(iChangePayDate);
		JcicZ062 chJcicZ062 = new JcicZ062();
		JcicZ060 iJcicZ060 = new JcicZ060();
		JcicZ060 jJcicZ060 = new JcicZ060();
		JcicZ060Id iJcicZ060Id = new JcicZ060Id();
		iJcicZ060Id.setSubmitKey(iSubmitKey);
		iJcicZ060Id.setCustId(iCustId);
		iJcicZ060Id.setRcDate(iRcDate);
		iJcicZ060Id.setChangePayDate(iChangePayDate);
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);
		iJcicZ047Id.setRcDate(iRcDate);
		iJcicZ047Id.setSubmitKey(iSubmitKey);
		JcicZ063 iJcicZ063 = new JcicZ063();
		JcicZ063Id iJcicZ063Id = new JcicZ063Id();
		iJcicZ063Id.setCustId(iCustId);
		iJcicZ063Id.setRcDate(iRcDate);
		iJcicZ063Id.setSubmitKey(iSubmitKey);
		iJcicZ063Id.setChangePayDate(iChangePayDate);
		JcicZ062 jJcicZ062 = new JcicZ062();

		BigDecimal xMonthPayAmt = BigDecimal.ZERO;// 「月付金」

		// Date計算
		int txDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;// 營業日 放acdate
		int iTxDate = DealDate(txDate, -3);// 報送日前3日

		// 檢核項目(D-34)
		if (!"4".equals(iTranKey_Tmp)) {
			// 1.2 start
			// 檢核KEY值(IDN+報送單位代號+協商申請日+申請變更還款條件日)是否存在「'60':前置協商受理申請變更還款暨請求回報剩餘債權通知資料」.

			// 1.3 start 本檔案報送日需大於等於「'60':前置協商受理申請變更還款暨請求回報剩餘債權通知」資料報送日+3個營業日.
			if ("1".equals(iTranKey_Tmp)) {
				iJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id, titaVo);
				if (iJcicZ060 == null) {
					throw new LogicException("E0005",
							"KEY值(IDN+報送單位代號+原前置協商申請日+申請變更還款條件日)未曾報送過「'60':前置協商受理申請變更還款暨請求回報剩餘債權通知」.");
				} else {
					if (iDateUtil.getDayOfWeek(iTxDate) > 4) {
						iTxDate = DealDate(iTxDate, -2);// 報送日前3個營業日，若為週四，再減2天
					}
					if (TimestampToDate(iJcicZ060.getCreateDate()) >= iTxDate) {
						// throw new LogicException("E0005",
						// "本檔案報送日需大於等於「'60':前置協商受理申請變更還款暨請求回報剩餘債權通知」資料報送日+3個營業日.");
					}
				}
				// 1.2, 1.3 end

				// 1.4檢核所有債權金融機構均已回報(或由最大債權金融機構代為回報)'61':回報協商剩餘債權金額資料，
				// 且第14欄「是否同意債務人申請變更還款條件方案」皆回報為'Y'，否則予以剔退.***J
			}

			// 1.5 檢核第11-13欄若與所有協商剩餘金融機構回報'61'之回報協商剩餘債權金額不同時，則予剔退***J

			// 1.6 檢核第14欄「變更還款條件簽約總債務金額」需為第11-13欄信用貸款、現金卡、信用卡之「協商剩餘債務簽約餘額」合計，否則予以剔退.前端已設為自動填入數字。

			// 1.7 start 變更還款條件簽約完成日期需大於或等於變更還款條件協議完成日期
			if (iChaRepayEndDate < iChaRepayAgreeDate) {
				// throw new LogicException("E0005", "變更還款條件簽約完成日期需大於或等於變更還款條件協議完成日期.");
			}
			// 1.7 end

			// 1.8 start 若第22欄「屬階梯式還款註記」填報'Y'者，第9欄第一階梯「期數」需填報固定值 '012'或'024'，否則予以剔退.
			if ("Y".equals(iGradeType)) {
				if (iPeriod != 12 && iPeriod != 24) {
					throw new LogicException("E0005", "「屬階梯式還款註記」填報'Y'，第一階梯「期數」需填報固定值 '012'或'024'.");
				} // 1.8 end

				// 1.9 start 若第22欄「屬階梯式還款註記」填報'Y'者，第10欄第一階梯「利率」需填報固定值 '00.00'，否則予以剔退.
				if (iRate.compareTo(BigDecimal.ZERO) != 0) {
					throw new LogicException("E0005", "「屬階梯式還款註記」填報'Y'，第一階梯「利率」需填報固定值 '00.00'.");
				}
			} // 1.9 end

			// 1.10.一.(一) start 第22欄「屬階梯式還款註記」空白，第10欄「利率」不等於0：第21欄「月付金」>=
			// 變更還款條件簽約總債務金額×{[(1+月利率)^第一階梯期數]×(月利率)}÷{[(1+月利率)^第一階梯期數]-1}
			if (iGradeType.trim().isEmpty()) {
				if (iRate.compareTo(BigDecimal.ZERO) != 0) {
					xMonthPayAmt = MonthPay1011(iChaRepayAmt, iRate, iPeriod);
					if (new BigDecimal(iMonthPayAmt).compareTo(xMonthPayAmt) < 0) {
						throw new LogicException("E0005",
								"「屬階梯式還款註記」空白且第一階梯「利率」不等於0時，「月付金」應大於等於：變更還款條件簽約總債務金額×{[(1+月利率)^第一階梯期數]×(月利率)}÷{[(1+月利率)^第一階梯期數]-1}.");
					} // 1.10.一.(一) end

					// 1.10.一.(二) start 第22欄「屬階梯式還款註記」空白，第10欄「利率」等於0：第21欄「月付金」>=
					// 變更還款條件簽約總債務金額÷第一階梯期數
				} else {
					if (iPeriod == 0) {
						xMonthPayAmt = iChaRepayAmt;
					} else {
						xMonthPayAmt = MonthPay1012(iChaRepayAmt, new BigDecimal(iPeriod));
					}
					if (new BigDecimal(iMonthPayAmt).compareTo(xMonthPayAmt) < 0) {
						throw new LogicException("E0005", "「屬階梯式還款註記」空白且第一階梯「利率」等於0時，「月付金」應大於等於：變更還款條件簽約總債務金額÷第一階梯期數.");
					} // 1.10.一.(二) end
				}
			} else if ("Y".equals(iGradeType)) {
				// 1.11 start 若第22欄「屬階梯式還款註記」填報'Y'者，第23-25欄二階梯期數、利率、月付金為必填，否則予以剔退。
				if (iPeriod2 == 0 || iRate2 == null || iMonthPayAmt2 == 0) {
					throw new LogicException("E0005", "「屬階梯式還款註記」填報'Y'，第二階梯期數、利率、月付金為必要填報欄位，不能為空.");
				} // 1.11 end

				// 1.10.二.(一)第22欄「屬階梯式還款註記」填報'Y'者，第21欄「月付金」不作檢核.
				// 1.10.二.(二).1 start 第24欄「第二階梯利率」不等於0：第25欄「第二階梯月付金」>=
				// [變更還款條件簽約總債務金額-(月付金×第一階梯期數)]×{[(1+第二階梯月利率)^第二階梯期數]×(第二階梯月利率)}÷{[(1+第二階梯月利率)^第二階梯期數]-1}
				if (iRate2.compareTo(BigDecimal.ZERO) != 0) {
					xMonthPayAmt = MonthPay10221(iChaRepayAmt, iRate2, iPeriod2, iMonthPayAmt, iPeriod);
					if (new BigDecimal(iMonthPayAmt2).compareTo(xMonthPayAmt) < 0) {
						throw new LogicException("E0005",
								"「屬階梯式還款註記」填報'Y'，第二階梯利率不等於0，則「第二階梯月付金」應大於等於：[變更還款條件簽約總債務金額-(月付金×第一階梯期數)]×{[(1+第二階梯月利率)^第二階梯期數]×(第二階梯月利率)}÷{[(1+第二階梯月利率)^第二階梯期數]-1}.");
					} // 1.10.二.(二).1 end

					// 1.10.二.(二).2 start 第24欄「第二階梯利率」等於0：第25欄「第二階梯月付金」>=
					// [變更還款條件簽約總債務金額-(月付金×第一階梯期數)]÷第二階梯期數.
				} else {
					xMonthPayAmt = MonthPay10222(iChaRepayAmt, new BigDecimal(iPeriod2), iMonthPayAmt, iPeriod);
					if (new BigDecimal(iMonthPayAmt2).compareTo(xMonthPayAmt) < 0) {
						throw new LogicException("E0005",
								"「屬階梯式還款註記」填報'Y'，第二階梯利率不等於0，則「第二階梯月付金」應大於等於：[變更還款條件簽約總債務金額-(月付金×第一階梯期數)]×{[(1+第二階梯月利率)^第二階梯期數]×(第二階梯月利率)}÷{[(1+第二階梯月利率)^第二階梯期數]-1}.");
					} // 1.10.二.(二).2 end

				}
			}

			// 1.12 start檢核第8欄「變更還款條件已履約期數」與第9欄「第一階梯期數」及第23欄「第二階梯期數」和不得大於180，否則予以剔退。
			if ((iCompletePeriod + iPeriod + iPeriod2) > 180) {
				throw new LogicException("E0005", "「變更還款條件已履約期數」與「第一階梯期數」及「第二階梯期數」和不得大於180.");
			}
			// 1.12 end

			// 1.13
			// start第24欄「第二階梯利率」須與「'47':無擔保債務協議資料」第8欄「利率」相同或前次報送'62'且未報'63'結案者，第10欄「第一階梯利率」(若前次變更還款條件屬階梯式還款者，則為前次報送'62'第24欄「第二階梯利率」)，否則予以剔退
			iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
			if ((iJcicZ047 == null) || ((iJcicZ047 != null) && (iRate2.compareTo(iJcicZ047.getRate()) != 0))) {
				iJcicZ063 = sJcicZ063Service.findById(iJcicZ063Id, titaVo);
				if (iJcicZ063 != null) {
					throw new LogicException("E0005", "「第二階梯利率」須與「'47':無擔保債務協議資料」之「利率」相同.");
				} else {
					jJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id, titaVo);
					if (jJcicZ062 == null) {
						throw new LogicException("E0005", "「第二階梯利率」須與「'47':無擔保債務協議資料」之「利率」相同.");
					} else {
						if (("Y".equals(jJcicZ062.getGradeType())) && (iRate2.compareTo(jJcicZ062.getRate2()) != 0)) {
							throw new LogicException("E0005",
									"「第二階梯利率」須與「'47':無擔保債務協議資料」之「利率」相同或前次報送'62'且未報'63'結案者「第二階梯利率」(前次變更還款條件屬階梯式還款者)相同.");
						} else if ((!"Y".equals(jJcicZ062.getGradeType()))
								&& (iRate2.compareTo(jJcicZ062.getRate()) != 0)) {
							throw new LogicException("E0005",
									"「第二階梯利率」須與「'47':無擔保債務協議資料」之「利率」相同或前次報送'62'且未報'63'結案者「第一階梯利率」相同.");
						}
					}
				}
			} // 1.13 end

			// 1.14
			// 第17欄「變更還款條件簽約完成日」有值後，同一筆key值資料即不得報送'63'結案，否則予以剔退.***與1.17和「L8321(JcicZ063)檢核1.5是同一檢核，併同報送相關.***

			// 1.15 start第18欄「變更還款條件首期應繳款日」需大於或等於第17欄「變更還款條件簽約完成日」，否則予以剔退.
			if (iChaRepayFirstDate < iChaRepayEndDate) {
				throw new LogicException("E0005", "「變更還款條件首期應繳款日」需大於或等於「變更還款條件簽約完成日」.");
			}
			// 1.15 end

			// 1.16 start第18欄「變更還款條件首期應繳款日」需為「'60':前置協商受理變更還款申請暨請求回報債權通知資料」報送日之次月10日，否則予以剔退.
			jJcicZ060 = sJcicZ060Service.findById(iJcicZ060Id, titaVo);
			if (jJcicZ060 != null) {
				this.info("NextMonth10Date(TimestampToDate(iJcicZ060.getCreateDate()))++"
						+ NextMonth10Date(TimestampToDate(iJcicZ060.getCreateDate())));
				if (iChaRepayFirstDate != NextMonth10Date(TimestampToDate(iJcicZ060.getCreateDate()))) {
					throw new LogicException("E0005", "「變更還款條件首期應繳款日」需為「'60':前置協商受理變更還款申請暨請求回報債權通知資料」報送日之次月10日.");
				}
			} // 1.16 end

			// 1.17
			// 若為第二次(含)以上之變更還款條件案件，報送本檔案格式資料且第17欄「變更還款條件簽約完成日」有值時，應併同報送前一筆「簽約完成日」有值之變更還款條件案件之「'63':結案通知資料」，且結案原因代碼為「'C':更新變更還款條件」，否則予以剔退。***

			// 2
			// 本中心以第15欄有值且第17欄為空白時為產出增補約據之依據，卷煙廠於接獲報送'62'次日，將檔名為BBBYYYMMDD+(10碼債務人IDN)B.pdf(BBB為最大債權金融機構代碼，YYYYMMDD為報送日期)，
			// 按身份證排序，並自動將當日產生之pdf壓縮成BBBYYYMMDDB.zip傳輸至最大債權金融機構DTO報送帳號，最大債權金融機構次日即可下載協議書等相關文件.***J

			// 檢核項目 end
		}
		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ062
			chJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id, titaVo);
			if (chJcicZ062 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ062.setJcicZ062Id(iJcicZ062Id);
			iJcicZ062.setTranKey(iTranKey);
			iJcicZ062.setCompletePeriod(iCompletePeriod);
			iJcicZ062.setPeriod(iPeriod);
			iJcicZ062.setRate(iRate);
			iJcicZ062.setExpBalanceAmt(iExpBalanceAmt);
			iJcicZ062.setCashBalanceAmt(iCashBalanceAmt);
			iJcicZ062.setCreditBalanceAmt(iCreditBalanceAmt);
			iJcicZ062.setChaRepayAmt(iChaRepayAmt);
			iJcicZ062.setChaRepayAgreeDate(iChaRepayAgreeDate);
			iJcicZ062.setChaRepayViewDate(iChaRepayViewDate);
			iJcicZ062.setChaRepayEndDate(iChaRepayEndDate);
			iJcicZ062.setChaRepayFirstDate(iChaRepayFirstDate);
			iJcicZ062.setPayAccount(iPayAccount);
			iJcicZ062.setPostAddr(iPostAddr);
			iJcicZ062.setMonthPayAmt(iMonthPayAmt);
			iJcicZ062.setGradeType(iGradeType);
			iJcicZ062.setPeriod2(iPeriod2);
			iJcicZ062.setRate2(iRate2);
			iJcicZ062.setMonthPayAmt2(iMonthPayAmt2);
			iJcicZ062.setUkey(iKey);
			try {
				sJcicZ062Service.insert(iJcicZ062, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ062 = sJcicZ062Service.ukeyFirst(iKey, titaVo);
			JcicZ062 uJcicZ062 = new JcicZ062();
			uJcicZ062 = sJcicZ062Service.holdById(iJcicZ062.getJcicZ062Id(), titaVo);
			if (uJcicZ062 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ062.setCompletePeriod(iCompletePeriod);
			uJcicZ062.setPeriod(iPeriod);
			uJcicZ062.setRate(iRate);
			uJcicZ062.setExpBalanceAmt(iExpBalanceAmt);
			uJcicZ062.setCashBalanceAmt(iCashBalanceAmt);
			uJcicZ062.setCreditBalanceAmt(iCreditBalanceAmt);
			uJcicZ062.setChaRepayAmt(iChaRepayAmt);
			uJcicZ062.setChaRepayAgreeDate(iChaRepayAgreeDate);
			uJcicZ062.setChaRepayViewDate(iChaRepayViewDate);
			uJcicZ062.setChaRepayEndDate(iChaRepayEndDate);
			uJcicZ062.setChaRepayFirstDate(iChaRepayFirstDate);
			uJcicZ062.setPayAccount(iPayAccount);
			uJcicZ062.setPostAddr(iPostAddr);
			uJcicZ062.setMonthPayAmt(iMonthPayAmt);
			uJcicZ062.setGradeType(iGradeType);
			uJcicZ062.setPeriod2(iPeriod2);
			uJcicZ062.setRate2(iRate2);
			uJcicZ062.setMonthPayAmt2(iMonthPayAmt2);
			uJcicZ062.setTranKey(iTranKey);
			uJcicZ062.setOutJcicTxtDate(0);
			JcicZ062 oldJcicZ062 = (JcicZ062) iDataLog.clone(uJcicZ062);
			try {
				sJcicZ062Service.update(uJcicZ062, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ062, uJcicZ062);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ062 = sJcicZ062Service.findById(iJcicZ062Id);
			if (iJcicZ062 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ062Log> dJcicLogZ062 = null;
			dJcicLogZ062 = sJcicZ062LogService.ukeyEq(iJcicZ062.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ062 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ062Service.delete(iJcicZ062, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ062Log iJcicZ062Log = dJcicLogZ062.getContent().get(0);
				iJcicZ062.setCompletePeriod(iJcicZ062Log.getCompletePeriod());
				iJcicZ062.setPeriod(iJcicZ062Log.getPeriod());
				iJcicZ062.setRate(iJcicZ062Log.getRate());
				iJcicZ062.setExpBalanceAmt(iJcicZ062Log.getExpBalanceAmt());
				iJcicZ062.setCashBalanceAmt(iJcicZ062Log.getCashBalanceAmt());
				iJcicZ062.setCreditBalanceAmt(iJcicZ062Log.getCreditBalanceAmt());
				iJcicZ062.setChaRepayAmt(iJcicZ062Log.getChaRepayAmt());
				iJcicZ062.setChaRepayAgreeDate(iJcicZ062Log.getChaRepayAgreeDate());
				iJcicZ062.setChaRepayViewDate(iJcicZ062Log.getChaRepayViewDate());
				iJcicZ062.setChaRepayEndDate(iJcicZ062Log.getChaRepayEndDate());
				iJcicZ062.setChaRepayFirstDate(iJcicZ062Log.getChaRepayFirstDate());
				iJcicZ062.setPayAccount(iJcicZ062Log.getPayAccount());
				iJcicZ062.setPostAddr(iJcicZ062Log.getPostAddr());
				iJcicZ062.setMonthPayAmt(iJcicZ062Log.getMonthPayAmt());
				iJcicZ062.setGradeType(iJcicZ062Log.getGradeType());
				iJcicZ062.setPeriod2(iJcicZ062Log.getPeriod2());
				iJcicZ062.setRate2(iJcicZ062Log.getRate2());
				iJcicZ062.setMonthPayAmt2(iJcicZ062Log.getMonthPayAmt2());
				iJcicZ062.setTranKey(iJcicZ062Log.getTranKey());
				iJcicZ062.setOutJcicTxtDate(iJcicZ062Log.getOutJcicTxtDate());
				try {
					sJcicZ062Service.update(iJcicZ062, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 計算：指定日期txDate加減天數iDays
	private int DealDate(int txDate, int iDays) throws LogicException {
		int retxdate = 0;
		iDateUtil.init();
		iDateUtil.setDate_1(txDate);
		iDateUtil.setDays(iDays);

		retxdate = iDateUtil.getCalenderDay();
		return retxdate;
	}

	// 轉換：Sql.Timestamp(創建日期)轉int(西元年YYYYMMDD)
	private int TimestampToDate(Timestamp ts) throws LogicException {
		int reTimestampToDate = 0;
		String tsStr = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			tsStr = dateFormat.format(ts);
			System.out.println(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		reTimestampToDate = Integer.valueOf(tsStr);
		return reTimestampToDate;
	}

	// 計算：指定日期txDate的下月10日
	private int NextMonth10Date(int txDate) throws LogicException {
		int retxdate = 0;
		iDateUtil.init();
		iDateUtil.setDate_1(txDate);
		iDateUtil.setMons(1);
		retxdate = iDateUtil.getCalenderDay();
		String retxdateStr = String.valueOf(retxdate);
		retxdateStr = retxdateStr.substring(0, retxdateStr.length() - 2) + "10";

		return Integer.valueOf(retxdateStr) - 19110000;// 轉成民國年YYYMMDD
	}

	// 計算月付金：1.10.一.(一):「屬階梯式還款註記」空白且第一階梯「利率」不等於0
	private BigDecimal MonthPay1011(BigDecimal iChaRepayAmt, BigDecimal iRate, int iPeriod) {
		BigDecimal xMonRate = iRate.divide(new BigDecimal(12 * 100), 10, BigDecimal.ROUND_HALF_UP);// 月利率=第一階梯利率/12
		BigDecimal NUMONE = BigDecimal.ONE;// 常數1
		return iChaRepayAmt.multiply((((NUMONE.add(xMonRate)).pow(iPeriod)).multiply(xMonRate))
				.divide(((NUMONE.add(xMonRate)).pow(iPeriod)).subtract(NUMONE), 10, BigDecimal.ROUND_HALF_UP));
	}

	// 計算月付金：1.10.一.(一):「屬階梯式還款註記」空白且第一階梯「利率」等於0
	private BigDecimal MonthPay1012(BigDecimal iChaRepayAmt, BigDecimal iPeriod) {
		return iChaRepayAmt.divide(iPeriod, 10, BigDecimal.ROUND_HALF_UP);
	}

	// 計算月付金：1.10.二.(二).1:「屬階梯式還款註記」為'Y'且第二階梯「利率」不等於0
	private BigDecimal MonthPay10221(BigDecimal iChaRepayAmt, BigDecimal iRate2, int iPeriod2, int iMonthPayAmt,
			int iPeriod) {
		BigDecimal xMonRate = iRate2.divide(new BigDecimal(12 * 100), 10, BigDecimal.ROUND_HALF_UP);// 月利率=第一階梯利率/12
		BigDecimal NUMONE = BigDecimal.ONE;// 常數1
		return ((iChaRepayAmt.subtract(new BigDecimal(iMonthPayAmt * iPeriod)))
				.multiply((NUMONE.add(xMonRate)).pow(iPeriod2)).multiply(xMonRate))
						.divide(((NUMONE.add(xMonRate)).pow(iPeriod2)).subtract(NUMONE), 10, BigDecimal.ROUND_HALF_UP);
	}

	// 計算月付金：1.10.二.(二).2:「屬階梯式還款註記」為'Y'且第二階梯「利率」等於0
	private BigDecimal MonthPay10222(BigDecimal iChaRepayAmt, BigDecimal iPeriod2, int iMonthPayAmt, int iPeriod) {
		return (iChaRepayAmt.subtract(new BigDecimal(iMonthPayAmt * iPeriod))).divide(iPeriod2, 10,
				BigDecimal.ROUND_HALF_UP);
	}

}