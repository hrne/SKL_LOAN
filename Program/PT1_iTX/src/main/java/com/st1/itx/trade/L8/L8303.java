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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.domain.JcicZ042Log;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.service.JcicZ045Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;

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

@Service("L8303")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8303 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ042LogService sJcicZ042LogService;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public DateUtil iDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8303 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate"));
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		String iIsClaims = titaVo.getParam("IsClaims");
		int iGuarLoanCnt = Integer.valueOf(titaVo.getParam("GuarLoanCnt"));
		int iExpLoanAmt = Integer.valueOf(titaVo.getParam("ExpLoanAmt"));
		int iCivil323ExpAmt = Integer.valueOf(titaVo.getParam("Civil323ExpAmt"));
		int iReceExpAmt = Integer.valueOf(titaVo.getParam("ReceExpAmt"));
		int iCashCardAmt = Integer.valueOf(titaVo.getParam("CashCardAmt"));
		int iCivil323CashAmt = Integer.valueOf(titaVo.getParam("Civil323CashAmt"));
		int iReceCashAmt = Integer.valueOf(titaVo.getParam("ReceCashAmt"));
		int iCreditCardAmt = Integer.valueOf(titaVo.getParam("CreditCardAmt"));
		int iCivil323CreditAmt = Integer.valueOf(titaVo.getParam("Civil323CreditAmt"));
		int iReceCreditAmt = Integer.valueOf(titaVo.getParam("ReceCreditAmt"));
		int iReceExpPrin = Integer.valueOf(titaVo.getParam("ReceExpPrin"));
		int iReceExpInte = Integer.valueOf(titaVo.getParam("ReceExpInte"));
		int iReceExpPena = Integer.valueOf(titaVo.getParam("ReceExpPena"));
		int iReceExpOther = Integer.valueOf(titaVo.getParam("ReceExpOther"));
		int iCashCardPrin = Integer.valueOf(titaVo.getParam("CashCardPrin"));
		int iCashCardInte = Integer.valueOf(titaVo.getParam("CashCardInte"));
		int iCashCardPena = Integer.valueOf(titaVo.getParam("CashCardPena"));
		int iCashCardOther = Integer.valueOf(titaVo.getParam("CashCardOther"));
		int iCreditCardPrin = Integer.valueOf(titaVo.getParam("CreditCardPrin"));
		int iCreditCardInte = Integer.valueOf(titaVo.getParam("CreditCardInte"));
		int iCreditCardPena = Integer.valueOf(titaVo.getParam("CreditCardPena"));
		int iCreditCardOther = Integer.valueOf(titaVo.getParam("CreditCardOther"));
		String iKey = "";
		int sTotalAmt = iExpLoanAmt + iCivil323ExpAmt + iCashCardAmt + iCivil323CashAmt + iCreditCardAmt + iCivil323CreditAmt;// 信用貸款+現金卡放款+信用卡 本息餘額
		// JcicZ042, JcicZ040, JcicZ043, JcicZ045
		JcicZ042 iJcicZ042 = new JcicZ042();
		JcicZ042Id iJcicZ042Id = new JcicZ042Id();
		iJcicZ042Id.setCustId(iCustId);// 債務人IDN
		iJcicZ042Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ042Id.setRcDate(iRcDate);
		iJcicZ042Id.setMaxMainCode(iMaxMainCode);
		JcicZ042 chJcicZ042 = new JcicZ042();
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);
		JcicZ045 iJcicZ045 = new JcicZ045();
		JcicZ045Id iJcicZ045Id = new JcicZ045Id();
		iJcicZ045Id.setCustId(iCustId);// 債務人IDN
		iJcicZ045Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ045Id.setRcDate(iRcDate);
		// Date計算
		int txDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;// 營業日 放acdate
		int tDate = Integer.valueOf(titaVo.getCalDy()) + 19110000;// 日曆日
		this.info("tDate  " + tDate); //20220707
		this.info("iRcDate  " + iRcDate); //1110201
		this.info("txDate  " + txDate); //20220105
		//int iDays25 = Dealdate(txDate, -25);// 報送日前25天
		int iDays25 = Dealdate(tDate, -25);// 報送日前25天
		// 檢核項目(D-7)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 start 完整key值未曾報送過'40':前置協商受理申請暨請求回報債權通知則予以剔退
			iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
			if (iJcicZ040 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				} else {
					throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				}
			} // 2 end

			// 3 start 金融機構報送日大於協商申請日+25則予以剔退
			if ("A".equals(iTranKey)) {
				if (iDays25 > (iRcDate + 19110000)) {// iRcDate協商申請日為民國年
					throw new LogicException("E0005", "報送日不可大於協商申請日+25");
				}
			} // 3 end

			// 4 start 本金融機構債務人必須填報'45':回報是否同意債務清償方案資料
			if ("Y".equals(iIsClaims)) {
				iJcicZ045 = sJcicZ045Service.findById(iJcicZ045Id, titaVo);
//				if (iJcicZ045 == null) {
//					if ("A".equals(iTranKey)) {
//						throw new LogicException("E0005", "本金融機構債務人必須先填報(45)回報是否同意債務清償方案資料.");
//					} else {
//						throw new LogicException("E0007", "本金融機構債務人必須先填報(45)回報是否同意債務清償方案資料.");
//					}
//				}
				// 4 end

				// 5 start 本金融機構債務人+有擔保債權筆數0，則信用貸款+現金卡放款+信用卡 本息餘額應大於0
				if ((iGuarLoanCnt == 0) && (sTotalAmt <= 0)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "本金融機構債務人有擔保債權筆數為0，則[信用貸款本息餘額]+[現金卡放款本息餘額]+[信用卡本息餘額]合計應大於0.");
					} else {
						throw new LogicException("E0007", "本金融機構債務人有擔保債權筆數為0，則[信用貸款本息餘額]+[現金卡放款本息餘額]+[信用卡本息餘額]合計應大於0.");
					}
				}
			}
			// 5 end-->(前端已有檢核，但錯誤信息提示不明確)

			// 6 start 有擔保債權筆數需等於報送'43':回報有擔保債權金額資料之筆數
			Slice<JcicZ043> sJcicZ043 = sJcicZ043Service.coutCollaterals(iCustId, iRcDate + 19110000, iSubmitKey, iMaxMainCode, 0, Integer.MAX_VALUE, titaVo);
			if (sJcicZ043 == null) {
				if (iGuarLoanCnt != 0) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(43)回報有擔保債權金額資料之筆數.");
					} else {
						throw new LogicException("E0007", "[有擔保債權筆數]需等於報送(43)回報有擔保債權金額資料之筆數.");
					}
				}
			} else {
				int sGuarLoanCnt = 0;
				for (JcicZ043 xJcicZ043 : sJcicZ043) {
					if (!"D".equals(xJcicZ043.getTranKey())) {
						sGuarLoanCnt++;
					}
				}
				if (iGuarLoanCnt != sGuarLoanCnt) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(43)回報有擔保債權金額資料之筆數.");
					} else {
						throw new LogicException("E0007", "[有擔保債權筆數]需等於報送(43)回報有擔保債權金額資料之筆數.");
					}
				}
			}
			// 6 end

			// 7 非本金融機構債務人，有擔保債權筆數必須為0，信用貸款+現金卡放款+信用卡 本息餘額應等於0-->(前端檢核)
			// 8,9,10合計值檢核，前端已改為合計值自動顯示

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ042
			chJcicZ042 = sJcicZ042Service.findById(iJcicZ042Id, titaVo);
			if (chJcicZ042 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ042.setJcicZ042Id(iJcicZ042Id);
			iJcicZ042.setTranKey(iTranKey);
			iJcicZ042.setIsClaims(iIsClaims);
			iJcicZ042.setGuarLoanCnt(iGuarLoanCnt);
			iJcicZ042.setExpLoanAmt(iExpLoanAmt);
			iJcicZ042.setCivil323ExpAmt(iCivil323ExpAmt);
			iJcicZ042.setReceExpAmt(iReceExpAmt);
			iJcicZ042.setCashCardAmt(iCashCardAmt);
			iJcicZ042.setCivil323CashAmt(iCivil323CashAmt);
			iJcicZ042.setReceCashAmt(iReceCashAmt);
			iJcicZ042.setCreditCardAmt(iCreditCardAmt);
			iJcicZ042.setCivil323CreditAmt(iCivil323CreditAmt);
			iJcicZ042.setReceCreditAmt(iReceCreditAmt);
			iJcicZ042.setReceExpPrin(iReceExpPrin);
			iJcicZ042.setReceExpInte(iReceExpInte);
			iJcicZ042.setReceExpPena(iReceExpPena);
			iJcicZ042.setReceExpOther(iReceExpOther);
			iJcicZ042.setCashCardPrin(iCashCardPrin);
			iJcicZ042.setCashCardInte(iCashCardInte);
			iJcicZ042.setCashCardPena(iCashCardPena);
			iJcicZ042.setCashCardOther(iCashCardOther);
			iJcicZ042.setCreditCardPrin(iCreditCardPrin);
			iJcicZ042.setCreditCardInte(iCreditCardInte);
			iJcicZ042.setCreditCardPena(iCreditCardPena);
			iJcicZ042.setCreditCardOther(iCreditCardOther);
			iJcicZ042.setUkey(iKey);
			try {
				sJcicZ042Service.insert(iJcicZ042, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ042 = sJcicZ042Service.ukeyFirst(iKey, titaVo);
			JcicZ042 uJcicZ042 = new JcicZ042();
			uJcicZ042 = sJcicZ042Service.holdById(iJcicZ042.getJcicZ042Id(), titaVo);
			if (uJcicZ042 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ042 oldJcicZ042 = (JcicZ042) iDataLog.clone(uJcicZ042);
			uJcicZ042.setTranKey(iTranKey);
			uJcicZ042.setIsClaims(iIsClaims);
			uJcicZ042.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ042.setExpLoanAmt(iExpLoanAmt);
			uJcicZ042.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ042.setReceExpAmt(iReceExpAmt);
			uJcicZ042.setCashCardAmt(iCashCardAmt);
			uJcicZ042.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ042.setReceCashAmt(iReceCashAmt);
			uJcicZ042.setCreditCardAmt(iCreditCardAmt);
			uJcicZ042.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ042.setReceCreditAmt(iReceCreditAmt);
			uJcicZ042.setReceExpPrin(iReceExpPrin);
			uJcicZ042.setReceExpInte(iReceExpInte);
			uJcicZ042.setReceExpPena(iReceExpPena);
			uJcicZ042.setReceExpOther(iReceExpOther);
			uJcicZ042.setCashCardPrin(iCashCardPrin);
			uJcicZ042.setCashCardInte(iCashCardInte);
			uJcicZ042.setCashCardPena(iCashCardPena);
			uJcicZ042.setCashCardOther(iCashCardOther);
			uJcicZ042.setCreditCardPrin(iCreditCardPrin);
			uJcicZ042.setCreditCardInte(iCreditCardInte);
			uJcicZ042.setCreditCardPena(iCreditCardPena);
			uJcicZ042.setCreditCardOther(iCreditCardOther);
			uJcicZ042.setOutJcicTxtDate(0);
			try {
				sJcicZ042Service.update(uJcicZ042, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ042, uJcicZ042);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ042 = sJcicZ042Service.findById(iJcicZ042Id);
			if (iJcicZ042 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ042Log> dJcicLogZ042 = null;
			dJcicLogZ042 = sJcicZ042LogService.ukeyEq(iJcicZ042.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ042 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ042Service.delete(iJcicZ042, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ042Log iJcicZ042Log = dJcicLogZ042.getContent().get(0);
				iJcicZ042.setIsClaims(iJcicZ042Log.getIsClaims());
				iJcicZ042.setGuarLoanCnt(iJcicZ042Log.getGuarLoanCnt());
				iJcicZ042.setExpLoanAmt(iJcicZ042Log.getExpLoanAmt());
				iJcicZ042.setCivil323ExpAmt(iJcicZ042Log.getCivil323ExpAmt());
				iJcicZ042.setReceExpAmt(iJcicZ042Log.getReceExpAmt());
				iJcicZ042.setCashCardAmt(iJcicZ042Log.getCashCardAmt());
				iJcicZ042.setCivil323CashAmt(iJcicZ042Log.getCivil323CashAmt());
				iJcicZ042.setReceCashAmt(iJcicZ042Log.getReceCashAmt());
				iJcicZ042.setCreditCardAmt(iJcicZ042Log.getCreditCardAmt());
				iJcicZ042.setCivil323CreditAmt(iJcicZ042Log.getCivil323CreditAmt());
				iJcicZ042.setReceCreditAmt(iJcicZ042Log.getReceCreditAmt());
				iJcicZ042.setReceExpPrin(iJcicZ042Log.getReceExpPrin());
				iJcicZ042.setReceExpInte(iJcicZ042Log.getReceExpInte());
				iJcicZ042.setReceExpPena(iJcicZ042Log.getReceExpPena());
				iJcicZ042.setReceExpOther(iJcicZ042Log.getReceExpOther());
				iJcicZ042.setCashCardPrin(iJcicZ042Log.getCashCardPrin());
				iJcicZ042.setCashCardInte(iJcicZ042Log.getCashCardInte());
				iJcicZ042.setCashCardPena(iJcicZ042Log.getCashCardPena());
				iJcicZ042.setCashCardOther(iJcicZ042Log.getCashCardOther());
				iJcicZ042.setCreditCardPrin(iJcicZ042Log.getCreditCardPrin());
				iJcicZ042.setCreditCardInte(iJcicZ042Log.getCreditCardInte());
				iJcicZ042.setCreditCardPena(iJcicZ042Log.getCreditCardPena());
				iJcicZ042.setCreditCardOther(iJcicZ042Log.getCreditCardOther());
				iJcicZ042.setTranKey(iJcicZ042Log.getTranKey());
				iJcicZ042.setOutJcicTxtDate(iJcicZ042Log.getOutJcicTxtDate());
				try {
					sJcicZ042Service.update(iJcicZ042, titaVo);
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

	private int Dealdate(int txDate, int iDays) throws LogicException {
		int retxdate = 0;
		iDateUtil.init();
		iDateUtil.setDate_1(txDate);
		iDateUtil.setDays(iDays);
		retxdate = iDateUtil.getCalenderDay();

		return retxdate;
	}
}
