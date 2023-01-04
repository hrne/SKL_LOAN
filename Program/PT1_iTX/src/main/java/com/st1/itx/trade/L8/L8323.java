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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Id;
import com.st1.itx.db.domain.JcicZ442Log;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ442LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ446Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L8323")
@Scope("prototype")
/**
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8323 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ442LogService sJcicZ442LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	@Autowired
	public Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8323 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iIsMaxMain = titaVo.getParam("IsMaxMain").trim();
		String iIsClaims = titaVo.getParam("IsClaims").trim();
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		int iGuarLoanCnt = Integer.valueOf(titaVo.getParam("GuarLoanCnt").trim());
		
		BigDecimal iCivil323ExpAmt = new BigDecimal("0");
		iCivil323ExpAmt = parse.stringToBigDecimal(titaVo.getParam("Civil323ExpAmt").trim());
		//int ixCivil323ExpAmt = iCivil323ExpAmt.intValue();
		this.info("iCivil323ExpAmt     = " + iCivil323ExpAmt);
		BigDecimal iCivil323CashAmt = new BigDecimal("0");
		iCivil323CashAmt = parse.stringToBigDecimal(titaVo.getParam("Civil323CashAmt").trim());
		//int ixCivil323CashAmt = iCivil323CashAmt.intValue();
		this.info("iCivil323CashAmt       = " + iCivil323CashAmt);
		BigDecimal iCivil323CreditAmt = new BigDecimal("0");
		iCivil323CreditAmt = parse.stringToBigDecimal(titaVo.getParam("Civil323CreditAmt").trim());
		//int ixCivil323CreditAmt = iCivil323CreditAmt.intValue();
		this.info("iCivil323CreditAmt    = " + iCivil323CreditAmt);
		BigDecimal iCivil323GuarAmt = new BigDecimal("0");
		iCivil323GuarAmt = parse.stringToBigDecimal(titaVo.getParam("Civil323GuarAmt").trim());
		//int ixCivil323GuarAmt = iCivil323GuarAmt.intValue();
		this.info("iCivil323GuarAmt      = " + iCivil323GuarAmt );
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
		int iGuarObliPrin = Integer.valueOf(titaVo.getParam("GuarObliPrin").trim());
		int iGuarObliInte = Integer.valueOf(titaVo.getParam("GuarObliInte").trim());
		int iGuarObliPena = Integer.valueOf(titaVo.getParam("GuarObliPena").trim());
		int iGuarObliOther = Integer.valueOf(titaVo.getParam("GuarObliOther").trim());
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ442, JcicZ446
		JcicZ442 iJcicZ442 = new JcicZ442();
		JcicZ442Id iJcicZ442Id = new JcicZ442Id();
		iJcicZ442Id.setSubmitKey(iSubmitKey);
		iJcicZ442Id.setCustId(iCustId);
		iJcicZ442Id.setApplyDate(iApplyDate);
		iJcicZ442Id.setCourtCode(iCourtCode);
		iJcicZ442Id.setMaxMainCode(iMaxMainCode);
		JcicZ442 chJcicZ442 = new JcicZ442();
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setSubmitKey(iSubmitKey);
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-47)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2
			// 若「IDN+調解申請日+受理調解機構代號+最大債權金融機構代號」未曾報送過「'440':前置調解受理申請暨請求回報債權通知資料」，予以剔退處理.***J

			// 3 第3欄「債權金融機構代號」若非屬Z41「受理申請暨請求回報債權」之應回報金融機構代號，予以剔退處理.***J

			// 4 第9欄「是否為最大債權金融機構報送」填報為Y時，頭筆資料「報送單位代號」需與第8欄「最大債權金融機構代號」一致，否則予以剔退.--->前端檢核

			// 5 start 除最大債權金融機構報送自行債權資料外，--->1014會議通知此除外條件忽略，全部要檢核
			// 「'440':前置調解受理申請暨請求回報債權通知資料」第12欄「協辦行是否需自行回報債權」填報為Y時，第9欄「是否為最大債權金融機構報送」需填報為N，反之亦然.
			iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
			if (iJcicZ440 != null) {
				if ("Y".equals(iJcicZ440.getReportYn())) {
					if (!"N".equals(iIsMaxMain)) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005",
									"(440)前置調解受理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
						} else {
							throw new LogicException("E0007",
									"(440)前置調解受理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
						}
					}
				} else if (!"Y".equals(iIsMaxMain)) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005",
								"(440)前置調解受理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
					} else {
						throw new LogicException("E0007",
								"(440)前置調解受理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
					}
				}
			}

			// 6 最大債權金融機構報送自行債權資料時，第9欄「是否為最大債權金融機構報送」需填報Y.***J

			// 7
			// 第10欄「是否為本金融機構債務人」填報'Y',且第11欄「有擔保債權筆數」填報'0'者，檢核本檔案格式[第12+13+14+15欄'依民法第323條計算之信用放款、現金卡放款、信用卡、保證債權本息餘額]之值需大於0，否則予以剔退.
			if ("Y".equals(iIsClaims) && iGuarLoanCnt == 0) {
				int ix = BigDecimal.ZERO.compareTo((iCivil323ExpAmt.add(iCivil323CashAmt).add(iCivil323CreditAmt).add(iCivil323GuarAmt)));
				this.info("ix   = " + ix);
				if (ix == 0 ) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005",
								"「是否為本金融機構債務人」填報'Y',且「有擔保債權筆數」填報'0'者，本檔案格式「依民法第323條計算之信用放款、現金卡放款、信用卡、保證債權本息餘額」之合計值需大於0.");
					} else {
						throw new LogicException("E0007",
								"「是否為本金融機構債務人」填報'Y',且「有擔保債權筆數」填報'0'者，本檔案格式「依民法第323條計算之信用放款、現金卡放款、信用卡、保證債權本息餘額」之合計值需大於0.");
					}
				}
			} // 7 end

			// 8 start檢核第11欄「有擔保債權筆數」需等於報送「'443':回報有擔保債權金額資料」之筆數.
			Slice<JcicZ443> sJcicZ443 = sJcicZ443Service.otherEq(iSubmitKey, iCustId, iApplyDate + 19110000, iCourtCode,
					iMaxMainCode, 0, Integer.MAX_VALUE, titaVo);
			if (sJcicZ443 == null) {
				if (iGuarLoanCnt != 0) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
					} else {
						throw new LogicException("E0007", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
					}
				}
			} else {
				int sGuarLoanCnt = 0;
				for (JcicZ443 xJcicZ443 : sJcicZ443) {
					if (!"D".equals(xJcicZ443.getTranKey())) {
						sGuarLoanCnt++;
					}
				}
				if (iGuarLoanCnt != sGuarLoanCnt) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
					} else {
						throw new LogicException("E0007", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
					}
				}
			}
			// 8 end

			// 9 檢核第17~20欄之金額合計需等於第12欄「依民法第323條計算之信用放款本息餘額」.--->(前端檢核)
			// 10 檢核第21~24欄之金額合計需等於第13欄「依民法第323條計算之現金卡放款本息餘額」.--->(前端檢核)
			// 11 檢核第25~28欄之金額合計需等於第14欄「依民法第323條計算之信用卡本息餘額」.--->(前端檢核)
			// 12 檢核第29~32欄之金額合計需等於第15欄「依民法第323條計算之保證債權本息餘額」.--->(前端檢核)

			// 13 start同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
				}
			}
			// 13 end

			// 檢核條件 end
		}

		switch (iTranKey_Tmp)

		{
		case "1":
			// 檢核是否重複
			chJcicZ442 = sJcicZ442Service.findById(iJcicZ442Id, titaVo);
			this.info("TEST===" + chJcicZ442);
			if (chJcicZ442 != null) {
				throw new LogicException("E0002", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ442.setJcicZ442Id(iJcicZ442Id);
			iJcicZ442.setTranKey(iTranKey);
			iJcicZ442.setUkey(iKey);
			iJcicZ442.setIsMaxMain(iIsMaxMain);
			iJcicZ442.setIsClaims(iIsClaims);
			iJcicZ442.setGuarLoanCnt(iGuarLoanCnt);
			iJcicZ442.setCivil323ExpAmt(iCivil323ExpAmt);
			iJcicZ442.setCivil323CashAmt(iCivil323CashAmt);
			iJcicZ442.setCivil323CreditAmt(iCivil323CreditAmt);
			iJcicZ442.setCivil323GuarAmt(iCivil323GuarAmt);
			iJcicZ442.setReceExpPrin(iReceExpPrin);
			iJcicZ442.setReceExpInte(iReceExpInte);
			iJcicZ442.setReceExpPena(iReceExpPena);
			iJcicZ442.setReceExpOther(iReceExpOther);
			iJcicZ442.setCashCardPrin(iCashCardPrin);
			iJcicZ442.setCashCardInte(iCashCardInte);
			iJcicZ442.setCashCardPena(iCashCardPena);
			iJcicZ442.setCashCardOther(iCashCardOther);
			iJcicZ442.setCreditCardPrin(iCreditCardPrin);
			iJcicZ442.setCreditCardInte(iCreditCardInte);
			iJcicZ442.setCreditCardPena(iCreditCardPena);
			iJcicZ442.setCreditCardOther(iCreditCardOther);
			iJcicZ442.setGuarObliPrin(iGuarObliPrin);
			iJcicZ442.setGuarObliInte(iGuarObliInte);
			iJcicZ442.setGuarObliPena(iGuarObliPena);
			iJcicZ442.setGuarObliOther(iGuarObliOther);
			try {
				sJcicZ442Service.insert(iJcicZ442, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ442 = sJcicZ442Service.ukeyFirst(iKey, titaVo);
			JcicZ442 uJcicZ442 = new JcicZ442();
			uJcicZ442 = sJcicZ442Service.holdById(iJcicZ442.getJcicZ442Id(), titaVo);
			if (uJcicZ442 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ442 oldJcicZ442 = (JcicZ442) iDataLog.clone(uJcicZ442);
			uJcicZ442.setTranKey(iTranKey);
			uJcicZ442.setIsMaxMain(iIsMaxMain);
			uJcicZ442.setIsClaims(iIsClaims);
			uJcicZ442.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ442.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ442.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ442.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ442.setCivil323GuarAmt(iCivil323GuarAmt);
			uJcicZ442.setReceExpPrin(iReceExpPrin);
			uJcicZ442.setReceExpInte(iReceExpInte);
			uJcicZ442.setReceExpPena(iReceExpPena);
			uJcicZ442.setReceExpOther(iReceExpOther);
			uJcicZ442.setCashCardPrin(iCashCardPrin);
			uJcicZ442.setCashCardInte(iCashCardInte);
			uJcicZ442.setCashCardPena(iCashCardPena);
			uJcicZ442.setCashCardOther(iCashCardOther);
			uJcicZ442.setCreditCardPrin(iCreditCardPrin);
			uJcicZ442.setCreditCardInte(iCreditCardInte);
			uJcicZ442.setCreditCardPena(iCreditCardPena);
			uJcicZ442.setCreditCardOther(iCreditCardOther);
			uJcicZ442.setGuarObliPrin(iGuarObliPrin);
			uJcicZ442.setGuarObliInte(iGuarObliInte);
			uJcicZ442.setGuarObliPena(iGuarObliPena);
			uJcicZ442.setGuarObliOther(iGuarObliOther);
			uJcicZ442.setOutJcicTxtDate(0);

			uJcicZ442.setActualFilingDate(0);
			uJcicZ442.setActualFilingMark("");

			try {
				sJcicZ442Service.update(uJcicZ442, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ442, uJcicZ442);
//			iDataLog.exec("L8323異動", uJcicZ442.getSubmitKey() + uJcicZ442.getCustId() + uJcicZ442.getApplyDate()
//					+ uJcicZ442.getCourtCode() + uJcicZ442.getMaxMainCode());
			iDataLog.exec("L8323異動", uJcicZ442.getUkey());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ442 = sJcicZ442Service.ukeyFirst(iKey, titaVo);
			JcicZ442 uJcicZ4422 = new JcicZ442();
			uJcicZ4422 = sJcicZ442Service.holdById(iJcicZ442.getJcicZ442Id(), titaVo);
			iJcicZ442 = sJcicZ442Service.findById(iJcicZ442Id);
			if (iJcicZ442 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ442 oldJcicZ4422 = (JcicZ442) iDataLog.clone(uJcicZ4422);
			uJcicZ4422.setTranKey(iTranKey);
			uJcicZ4422.setIsMaxMain(iIsMaxMain);
			uJcicZ4422.setIsClaims(iIsClaims);
			uJcicZ4422.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ4422.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ4422.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ4422.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ4422.setCivil323GuarAmt(iCivil323GuarAmt);
			uJcicZ4422.setReceExpPrin(iReceExpPrin);
			uJcicZ4422.setReceExpInte(iReceExpInte);
			uJcicZ4422.setReceExpPena(iReceExpPena);
			uJcicZ4422.setReceExpOther(iReceExpOther);
			uJcicZ4422.setCashCardPrin(iCashCardPrin);
			uJcicZ4422.setCashCardInte(iCashCardInte);
			uJcicZ4422.setCashCardPena(iCashCardPena);
			uJcicZ4422.setCashCardOther(iCashCardOther);
			uJcicZ4422.setCreditCardPrin(iCreditCardPrin);
			uJcicZ4422.setCreditCardInte(iCreditCardInte);
			uJcicZ4422.setCreditCardPena(iCreditCardPena);
			uJcicZ4422.setCreditCardOther(iCreditCardOther);
			uJcicZ4422.setGuarObliPrin(iGuarObliPrin);
			uJcicZ4422.setGuarObliInte(iGuarObliInte);
			uJcicZ4422.setGuarObliPena(iGuarObliPena);
			uJcicZ4422.setGuarObliOther(iGuarObliOther);
			uJcicZ4422.setOutJcicTxtDate(0);

			Slice<JcicZ442Log> dJcicLogZ442 = null;
			dJcicLogZ442 = sJcicZ442LogService.ukeyEq(iJcicZ442.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ442 == null || "A".equals(iTranKey)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ442Service.delete(iJcicZ442, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ442Log iJcicZ442Log = dJcicLogZ442.getContent().get(0);
				iJcicZ442.setIsMaxMain(iJcicZ442Log.getIsMaxMain());
				iJcicZ442.setIsClaims(iJcicZ442Log.getIsClaims());
				iJcicZ442.setGuarLoanCnt(iJcicZ442Log.getGuarLoanCnt());
				iJcicZ442.setCivil323ExpAmt(iJcicZ442Log.getCivil323ExpAmt());
				iJcicZ442.setCivil323CashAmt(iJcicZ442Log.getCivil323CashAmt());
				iJcicZ442.setCivil323CreditAmt(iJcicZ442Log.getCivil323CreditAmt());
				iJcicZ442.setCivil323GuarAmt(iJcicZ442Log.getCivil323GuarAmt());
				iJcicZ442.setReceExpPrin(iJcicZ442Log.getReceExpPrin());
				iJcicZ442.setReceExpInte(iJcicZ442Log.getReceExpInte());
				iJcicZ442.setReceExpPena(iJcicZ442Log.getReceExpPena());
				iJcicZ442.setReceExpOther(iJcicZ442Log.getReceExpOther());
				iJcicZ442.setCashCardPrin(iJcicZ442Log.getCashCardPrin());
				iJcicZ442.setCashCardInte(iJcicZ442Log.getCashCardInte());
				iJcicZ442.setCashCardPena(iJcicZ442Log.getCashCardPena());
				iJcicZ442.setCashCardOther(iJcicZ442Log.getCashCardOther());
				iJcicZ442.setCreditCardPrin(iJcicZ442Log.getCreditCardPrin());
				iJcicZ442.setCreditCardInte(iJcicZ442Log.getCreditCardInte());
				iJcicZ442.setCreditCardPena(iJcicZ442Log.getCreditCardPena());
				iJcicZ442.setCreditCardOther(iJcicZ442Log.getCreditCardOther());
				iJcicZ442.setGuarObliPrin(iJcicZ442Log.getGuarObliPrin());
				iJcicZ442.setGuarObliInte(iJcicZ442Log.getGuarObliInte());
				iJcicZ442.setGuarObliPena(iJcicZ442Log.getGuarObliPena());
				iJcicZ442.setGuarObliOther(iJcicZ442Log.getGuarObliOther());
				iJcicZ442.setTranKey(iJcicZ442Log.getTranKey());
				iJcicZ442.setOutJcicTxtDate(iJcicZ442Log.getOutJcicTxtDate());

				try {
					sJcicZ442Service.update(iJcicZ442, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ4422, uJcicZ4422);
//			iDataLog.exec("L8323刪除", uJcicZ4422.getSubmitKey() + uJcicZ4422.getCustId() + uJcicZ4422.getApplyDate()
//					+ uJcicZ4422.getCourtCode() + uJcicZ4422.getMaxMainCode());
			iDataLog.exec("L8323刪除", uJcicZ4422.getUkey());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ442 = sJcicZ442Service.ukeyFirst(iKey, titaVo);
			JcicZ442 uJcicZ4423 = new JcicZ442();
			uJcicZ4423 = sJcicZ442Service.holdById(iJcicZ442.getJcicZ442Id(), titaVo);
			if (uJcicZ4423 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ442.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ442 oldJcicZ4423 = (JcicZ442) iDataLog.clone(uJcicZ4423);
			uJcicZ4423.setJcicZ442Id(iJcicZ442Id);
			uJcicZ4423.setTranKey(iTranKey);
			uJcicZ4423.setIsMaxMain(iIsMaxMain);
			uJcicZ4423.setIsClaims(iIsClaims);
			uJcicZ4423.setGuarLoanCnt(iGuarLoanCnt);
			uJcicZ4423.setCivil323ExpAmt(iCivil323ExpAmt);
			uJcicZ4423.setCivil323CashAmt(iCivil323CashAmt);
			uJcicZ4423.setCivil323CreditAmt(iCivil323CreditAmt);
			uJcicZ4423.setCivil323GuarAmt(iCivil323GuarAmt);
			uJcicZ4423.setReceExpPrin(iReceExpPrin);
			uJcicZ4423.setReceExpInte(iReceExpInte);
			uJcicZ4423.setReceExpPena(iReceExpPena);
			uJcicZ4423.setReceExpOther(iReceExpOther);
			uJcicZ4423.setCashCardPrin(iCashCardPrin);
			uJcicZ4423.setCashCardInte(iCashCardInte);
			uJcicZ4423.setCashCardPena(iCashCardPena);
			uJcicZ4423.setCashCardOther(iCashCardOther);
			uJcicZ4423.setCreditCardPrin(iCreditCardPrin);
			uJcicZ4423.setCreditCardInte(iCreditCardInte);
			uJcicZ4423.setCreditCardPena(iCreditCardPena);
			uJcicZ4423.setCreditCardOther(iCreditCardOther);
			uJcicZ4423.setGuarObliPrin(iGuarObliPrin);
			uJcicZ4423.setGuarObliInte(iGuarObliInte);
			uJcicZ4423.setGuarObliPena(iGuarObliPena);
			uJcicZ4423.setGuarObliOther(iGuarObliOther);
			uJcicZ4423.setUkey(iKey);

			try {
				sJcicZ442Service.update(uJcicZ4423, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ4423, uJcicZ4423);
//			iDataLog.exec("L8323修改", uJcicZ4423.getSubmitKey() + uJcicZ4423.getCustId() + uJcicZ4423.getApplyDate()
//					+ uJcicZ4423.getCourtCode() + uJcicZ4423.getMaxMainCode());
			iDataLog.exec("L8323修改", uJcicZ4423.getUkey());
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
