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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.domain.JcicZ042Log;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.service.CustMainService;
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

@Service("L8303")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8303 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
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

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iIsClaims = titaVo.getParam("IsClaims").trim();
		int iGuarLoanCnt = Integer.valueOf(titaVo.getParam("GuarLoanCnt").trim());
		int iExpLoanAmt = Integer.valueOf(titaVo.getParam("ExpLoanAmt").trim());
		int iCivil323ExpAmt = Integer.valueOf(titaVo.getParam("Civil323ExpAmt").trim());
		int iReceExpAmt = Integer.valueOf(titaVo.getParam("ReceExpAmt").trim());
		int iCashCardAmt = Integer.valueOf(titaVo.getParam("CashCardAmt").trim());
		int iCivil323CashAmt = Integer.valueOf(titaVo.getParam("Civil323CashAmt").trim());
		int iReceCashAmt = Integer.valueOf(titaVo.getParam("ReceCashAmt").trim());
		int iCreditCardAmt = Integer.valueOf(titaVo.getParam("CreditCardAmt").trim());
		int iCivil323CreditAmt = Integer.valueOf(titaVo.getParam("Civil323CreditAmt").trim());
		int iReceCreditAmt = Integer.valueOf(titaVo.getParam("ReceCreditAmt").trim());
		int iReceExpPrin = Integer.valueOf(titaVo.getParam("ReceExpPrin").trim());
		int iReceExpInte = Integer.valueOf(titaVo.getParam("ReceExpInte").trim());
		int iReceExpPena = Integer.valueOf(titaVo.getParam("ReceExpPena").trim());
		int iReceExpOther = Integer.valueOf(titaVo.getParam("ReceExpOther").trim());
		int iCashCardPrin = Integer.valueOf(titaVo.getParam("CashCardPrin").trim());
		int iCashCardInte = Integer.valueOf(titaVo.getParam("CashCardInte").trim());
		int iCashCardPena = Integer.valueOf(titaVo.getParam("CashCardPena").trim());
		int iCashCardOther = Integer.valueOf(titaVo.getParam("CashCardOther").trim());
		int iCreditCardPrin = Integer.valueOf(titaVo.getParam("CreditCardPrin").trim());
		int iCreditCardInte = Integer.valueOf(titaVo.getParam("CreditCardInte").trim());
		int iCreditCardPena = Integer.valueOf(titaVo.getParam("CreditCardPena").trim());
		int iCreditCardOther = Integer.valueOf(titaVo.getParam("CreditCardOther").trim());
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		int sTotalAmt = iExpLoanAmt + iCivil323ExpAmt + iCashCardAmt + iCivil323CashAmt + iCreditCardAmt
				+ iCivil323CreditAmt;// 信用貸款+現金卡放款+信用卡 本息餘額
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
		
		JcicZ045 ixJcicZ045 = new JcicZ045();
		JcicZ045Id ixJcicZ045Id = new JcicZ045Id();
		ixJcicZ045Id.setCustId(iCustId);// 債務人IDN
		ixJcicZ045Id.setSubmitKey(iSubmitKey);// 報送單位代號
		ixJcicZ045Id.setRcDate(iRcDate+19110000);
		ixJcicZ045Id.setMaxMainCode(iMaxMainCode);
		
		// Date計算
		int txDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;// 營業日 放acdate
		int tDate = Integer.valueOf(titaVo.getCalDy()) + 19110000;// 日曆日
		this.info("tDate  " + tDate); // 20220707
		this.info("iRcDate  " + iRcDate); // 1110201
		this.info("txDate  " + txDate); // 20220105
		// int iDays25 = Dealdate(txDate, -25);// 報送日前25天
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
			this.info("iIsClaims    =" + iIsClaims);
			if ("Y".equals(iIsClaims)) {
				
				this.info("iJcicZ045Id   = " + ixJcicZ045Id);
				iJcicZ045 = sJcicZ045Service.findById(ixJcicZ045Id, titaVo);
				this.info("iJcicZ045   = " + iJcicZ045);
				if(iJcicZ045 == null ) {
					throw new LogicException("E0005", "本金融機構債務人必須先填報(45)回報是否同意債務清償方案資料.");
				}
				ixJcicZ045 = sJcicZ045Service.otherFirst(iSubmitKey, iCustId, iRcDate+19110000, iMaxMainCode, titaVo);
				if(ixJcicZ045 == null) {
					throw new LogicException("E0005", "本金融機構債務人必須先填報(45)回報是否同意債務清償方案資料.");
				}
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
			Slice<JcicZ043> sJcicZ043 = sJcicZ043Service.coutCollaterals(iCustId, iRcDate + 19110000, iSubmitKey,
					iMaxMainCode, 0, Integer.MAX_VALUE, titaVo);
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
			
			uJcicZ042.setActualFilingDate(0);
			uJcicZ042.setActualFilingMark("");
			
			try {
				sJcicZ042Service.update(uJcicZ042, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			this.info("進入6932 ================ L8303");
			this.info("UKey    ===== " + uJcicZ042.getUkey());

			iDataLog.setEnv(titaVo, oldJcicZ042, uJcicZ042);
//			iDataLog.exec("L8303異動", uJcicZ042.getSubmitKey() + uJcicZ042.getCustId() + uJcicZ042.getRcDate());
			iDataLog.exec("L8303異動", uJcicZ042.getUkey());
			break;
		// 2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ042 = sJcicZ042Service.ukeyFirst(iKey, titaVo);
			JcicZ042 uJcicZ0422 = new JcicZ042();
			uJcicZ0422 = sJcicZ042Service.holdById(iJcicZ042.getJcicZ042Id(), titaVo);
			iJcicZ042 = sJcicZ042Service.findById(iJcicZ042Id);
			if (iJcicZ042 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ042 oldJcicZ0422 = (JcicZ042) iDataLog.clone(uJcicZ0422);
			uJcicZ0422.setTranKey(iTranKey);
			uJcicZ0422.setIsClaims(iIsClaims);
			uJcicZ0422.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ0422.setExpLoanAmt(iExpLoanAmt);
			uJcicZ0422.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ0422.setReceExpAmt(iReceExpAmt);
			uJcicZ0422.setCashCardAmt(iCashCardAmt);
			uJcicZ0422.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ0422.setReceCashAmt(iReceCashAmt);
			uJcicZ0422.setCreditCardAmt(iCreditCardAmt);
			uJcicZ0422.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ0422.setReceCreditAmt(iReceCreditAmt);
			uJcicZ0422.setReceExpPrin(iReceExpPrin);
			uJcicZ0422.setReceExpInte(iReceExpInte);
			uJcicZ0422.setReceExpPena(iReceExpPena);
			uJcicZ0422.setReceExpOther(iReceExpOther);
			uJcicZ0422.setCashCardPrin(iCashCardPrin);
			uJcicZ0422.setCashCardInte(iCashCardInte);
			uJcicZ0422.setCashCardPena(iCashCardPena);
			uJcicZ0422.setCashCardOther(iCashCardOther);
			uJcicZ0422.setCreditCardPrin(iCreditCardPrin);
			uJcicZ0422.setCreditCardInte(iCreditCardInte);
			uJcicZ0422.setCreditCardPena(iCreditCardPena);
			uJcicZ0422.setCreditCardOther(iCreditCardOther);
			uJcicZ0422.setOutJcicTxtDate(0);

			
			Slice<JcicZ042Log> dJcicLogZ042 = null;
			dJcicLogZ042 = sJcicZ042LogService.ukeyEq(iJcicZ042.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ042 == null || "A".equals(iTranKey)) {
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
			iDataLog.setEnv(titaVo, oldJcicZ0422, uJcicZ0422);
//			iDataLog.exec("L8303刪除", uJcicZ0422.getSubmitKey() + uJcicZ0422.getCustId() + uJcicZ0422.getRcDate());
			iDataLog.exec("L8303刪除", uJcicZ0422.getUkey());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ042 = sJcicZ042Service.ukeyFirst(iKey, titaVo);
			JcicZ042 uJcicZ0423 = new JcicZ042();
			uJcicZ0423 = sJcicZ042Service.holdById(iJcicZ042.getJcicZ042Id(), titaVo);
			if (uJcicZ0423 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ042.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ042 oldJcicZ0423 = (JcicZ042) iDataLog.clone(uJcicZ0423);
			uJcicZ0423.setJcicZ042Id(iJcicZ042Id);
			uJcicZ0423.setTranKey(iTranKey);
			uJcicZ0423.setIsClaims(iIsClaims);
			uJcicZ0423.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ0423.setExpLoanAmt(iExpLoanAmt);
			uJcicZ0423.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ0423.setReceExpAmt(iReceExpAmt);
			uJcicZ0423.setCashCardAmt(iCashCardAmt);
			uJcicZ0423.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ0423.setReceCashAmt(iReceCashAmt);
			uJcicZ0423.setCreditCardAmt(iCreditCardAmt);
			uJcicZ0423.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ0423.setReceCreditAmt(iReceCreditAmt);
			uJcicZ0423.setReceExpPrin(iReceExpPrin);
			uJcicZ0423.setReceExpInte(iReceExpInte);
			uJcicZ0423.setReceExpPena(iReceExpPena);
			uJcicZ0423.setReceExpOther(iReceExpOther);
			uJcicZ0423.setCashCardPrin(iCashCardPrin);
			uJcicZ0423.setCashCardInte(iCashCardInte);
			uJcicZ0423.setCashCardPena(iCashCardPena);
			uJcicZ0423.setCashCardOther(iCashCardOther);
			uJcicZ0423.setCreditCardPrin(iCreditCardPrin);
			uJcicZ0423.setCreditCardInte(iCreditCardInte);
			uJcicZ0423.setCreditCardPena(iCreditCardPena);
			uJcicZ0423.setCreditCardOther(iCreditCardOther);
			uJcicZ0423.setUkey(iKey);
			try {
				sJcicZ042Service.update(uJcicZ0423, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0423, uJcicZ0423);
//			iDataLog.exec("L8303修改", uJcicZ0423.getSubmitKey() + uJcicZ0423.getCustId() + uJcicZ0423.getRcDate());
			iDataLog.exec("L8303修改", uJcicZ0423.getUkey());
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
