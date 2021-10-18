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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Id;
import com.st1.itx.db.domain.JcicZ442Log;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
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

@Service("L8323")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8323 extends TradeBuffer {
	/* DB服務注入 */
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8323 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iIsMaxMain = titaVo.getParam("IsMaxMain");
		String iIsClaims = titaVo.getParam("IsClaims");
		String iCourtCode = titaVo.getParam("CourtCode");
		String iMaxMainCode = titaVo.getParam("MaxMainCode");
		int iGuarLoanCnt = Integer.valueOf(titaVo.getParam("GuarLoanCnt"));
		int iCivil323ExpAmt = Integer.valueOf(titaVo.getParam("Civil323ExpAmt"));
		int iCivil323CashAmt = Integer.valueOf(titaVo.getParam("Civil323CashAmt"));
		int iCivil323CreditAmt = Integer.valueOf(titaVo.getParam("Civil323CreditAmt"));
		int iCivil323GuarAmt = Integer.valueOf(titaVo.getParam("Civil323GuarAmt"));
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
		int iGuarObliPrin = Integer.valueOf(titaVo.getParam("GuarObliPrin"));
		int iGuarObliInte = Integer.valueOf(titaVo.getParam("GuarObliInte"));
		int iGuarObliPena = Integer.valueOf(titaVo.getParam("GuarObliPena"));
		int iGuarObliOther = Integer.valueOf(titaVo.getParam("GuarObliOther"));
		String iKey = "";
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
			// 「'440':前置調解愛理申請暨請求回報債權通知資料」第12欄「協辦行是否需自行回報債權」填報為Y時，第9欄「是否為最大債權金融機構報送」需填報為N，反之亦然.
			iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
			if (iJcicZ440 != null) {
				if ("Y".equals(iJcicZ440.getReportYn())) {
					if (!"N".equals(iIsMaxMain)) {
						throw new LogicException("E0005",
								"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
					}
				} else if (!"Y".equals(iIsMaxMain)) {
					throw new LogicException("E0005",
							"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
				}
			}

			// 6 最大債權金融機構報送自行債權資料時，第9欄「是否為最大債權金融機構報送」需填報Y.***J

			// 7
			// 第10欄「是否為本金融機構債務人」填報'Y',且第11欄「有擔保債權筆數」填報'0'者，檢核本檔案格式[第12+13+14+15欄'依民法第323條計算之信用放款、現金卡放款、信用卡、保證債權本息余額]之值需大於0，否則予以剔退.
			if ("Y".equals(iIsClaims) && iGuarLoanCnt == 0) {
				if ((iCivil323ExpAmt + iCivil323CashAmt + iCivil323CreditAmt + iCivil323GuarAmt) <= 0) {
					throw new LogicException("E0005",
							"「是否為本金融機構債務人」填報'Y',且「有擔保債權筆數」填報'0'者，本檔案格式「依民法第323條計算之信用放款、現金卡放款、信用卡、保證債權本息余額」之合計值需大於0.");
				}
			} // 7 end

			// 8 start檢核第11欄「有擔保債權筆數」需等於報送「'443':回報有擔保債權金額資料」之筆數.
			Slice<JcicZ443> sJcicZ443 = sJcicZ443Service.otherEq(iSubmitKey, iCustId, iApplyDate + 19110000, iCourtCode,
					iMaxMainCode, 0, Integer.MAX_VALUE, titaVo);
			if (sJcicZ443 == null) {
				if (iGuarLoanCnt != 0) {
					throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
				}
			} else {
				int sGuarLoanCnt = 0;
				for (JcicZ443 xJcicZ443 : sJcicZ443) {
					if (!"D".equals(xJcicZ443.getTranKey())) {
						sGuarLoanCnt++;
					}
				}
				if (iGuarLoanCnt != sGuarLoanCnt) {
					throw new LogicException("E0005", "[有擔保債權筆數]需等於報送(443)前置調解回報有擔保債權金額資料之筆數.");
				}
			}
			// 8 end

			// 9 start 檢核第17~20欄之金額合計需等於第12欄「依民法第323條計算之信用放款本息餘額」.
			if ((iReceExpPrin + iReceExpInte + iReceExpPena + iReceExpOther) != iCivil323ExpAmt) {
				throw new LogicException("E0005", "「信用放款」本金、利息、違約金、其他費用之金額合計需等於「依民法第323條計算之信用放款本息餘額」.");
			} // 9 end

			// 10 start檢核第21~24欄之金額合計需等於第13欄「依民法第323條計算之現金卡放款本息餘額」.
			if ((iCashCardPrin + iCashCardInte + iCashCardPena + iCashCardOther) != iCivil323CashAmt) {
				throw new LogicException("E0005", "「現金卡」本金、利息、違約金、其他費用之金額合計需等於「依民法第323條計算之現金卡放款本息餘額」.");
			} // 10 end

			// 11 start檢核第25~28欄之金額合計需等於第14欄「依民法第323條計算之信用卡本息餘額」.
			if ((iCreditCardPrin + iCreditCardInte + iCreditCardPena + iCreditCardOther) != iCivil323CreditAmt) {
				throw new LogicException("E0005", "「信用卡」本金、利息、違約金、其他費用之金額合計需等於「依民法第323條計算之信用卡本息餘額」.");
			} // 11 end

			// 12 start檢核第29~32欄之金額合計需等於第15欄「依民法第323條計算之保證債權本息餘額」.
			if ((iGuarObliPrin + iGuarObliInte + iGuarObliPena + iGuarObliOther) != iCivil323GuarAmt) {
				throw new LogicException("E0005", "「保證債權」本金、利息、違約金、其他費用之金額合計需等於「依民法第323條計算之保證債權本息餘額」.");
			} // 12 end

			// 13 start同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動本檔案資料.
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
				if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
				}
			} // 13 end

			// 檢核條件 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ442 = sJcicZ442Service.findById(iJcicZ442Id, titaVo);
			this.info("TEST===" + chJcicZ442);
			if (chJcicZ442 != null) {
				throw new LogicException("E0005", "已有相同資料");
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
			JcicZ442 oldJcicZ442 = (JcicZ442) iDataLog.clone(uJcicZ442);
			try {
				sJcicZ442Service.update(uJcicZ442, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ442, uJcicZ442);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ442 = sJcicZ442Service.findById(iJcicZ442Id);
			if (iJcicZ442 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ442Log> dJcicLogZ442 = null;
			dJcicLogZ442 = sJcicZ442LogService.ukeyEq(iJcicZ442.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ442 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ442Service.delete(iJcicZ442, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ442Log iJcicZ442Log = dJcicLogZ442.getContent().get(0);
				iJcicZ442Log.setIsMaxMain(iJcicZ442Log.getIsMaxMain());
				iJcicZ442Log.setIsClaims(iJcicZ442Log.getIsClaims());
				iJcicZ442Log.setGuarLoanCnt(iJcicZ442Log.getGuarLoanCnt());
				iJcicZ442Log.setCivil323ExpAmt(iJcicZ442Log.getCivil323ExpAmt());
				iJcicZ442Log.setCivil323CashAmt(iJcicZ442Log.getCivil323CashAmt());
				iJcicZ442Log.setCivil323CreditAmt(iJcicZ442Log.getCivil323CreditAmt());
				iJcicZ442Log.setCivil323GuarAmt(iJcicZ442Log.getCivil323GuarAmt());
				iJcicZ442Log.setReceExpPrin(iJcicZ442Log.getReceExpPrin());
				iJcicZ442Log.setReceExpInte(iJcicZ442Log.getReceExpInte());
				iJcicZ442Log.setReceExpPena(iJcicZ442Log.getReceExpPena());
				iJcicZ442Log.setReceExpOther(iJcicZ442Log.getReceExpOther());
				iJcicZ442Log.setCashCardPrin(iJcicZ442Log.getCashCardPrin());
				iJcicZ442Log.setCashCardInte(iJcicZ442Log.getCashCardInte());
				iJcicZ442Log.setCashCardPena(iJcicZ442Log.getCashCardPena());
				iJcicZ442Log.setCashCardOther(iJcicZ442Log.getCashCardOther());
				iJcicZ442Log.setCreditCardPrin(iJcicZ442Log.getCreditCardPrin());
				iJcicZ442Log.setCreditCardInte(iJcicZ442Log.getCreditCardInte());
				iJcicZ442Log.setCreditCardPena(iJcicZ442Log.getCreditCardPena());
				iJcicZ442Log.setCreditCardOther(iJcicZ442Log.getCreditCardOther());
				iJcicZ442Log.setGuarObliPrin(iJcicZ442Log.getGuarObliPrin());
				iJcicZ442Log.setGuarObliInte(iJcicZ442Log.getGuarObliInte());
				iJcicZ442Log.setGuarObliPena(iJcicZ442Log.getGuarObliPena());
				iJcicZ442Log.setGuarObliOther(iJcicZ442Log.getGuarObliOther());
				iJcicZ442Log.setTranKey(iJcicZ442Log.getTranKey());
				iJcicZ442Log.setOutJcicTxtDate(iJcicZ442Log.getOutJcicTxtDate());

				try {
					sJcicZ442Service.update(iJcicZ442, titaVo);
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
}
