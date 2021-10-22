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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.domain.JcicZ443Log;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ443LogService;

/*DB服務*/
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
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ443LogService sJcicZ443LogService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
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
		BigDecimal iLoanAmt = new BigDecimal(titaVo.getParam("LoanAmt"));
		BigDecimal iCreditAmt = new BigDecimal(titaVo.getParam("CreditAmt"));
		BigDecimal iPrincipal = new BigDecimal(titaVo.getParam("Principal"));
		BigDecimal iInterest = new BigDecimal(titaVo.getParam("Interest"));
		BigDecimal iPenalty = new BigDecimal(titaVo.getParam("Penalty"));
		BigDecimal iOther = new BigDecimal(titaVo.getParam("Other"));
		BigDecimal iTerminalPayAmt = new BigDecimal(titaVo.getParam("TerminalPayAmt"));
		BigDecimal iLatestPayAmt = new BigDecimal(titaVo.getParam("LatestPayAmt"));
		int iFinalPayDay = Integer.valueOf(titaVo.getParam("FinalPayDay"));
		BigDecimal iNotyetacQuit = new BigDecimal(titaVo.getParam("NotyetacQuit"));
		int iMothPayDay = Integer.valueOf(titaVo.getParam("MothPayDay"));
		int iBeginDate = Integer.valueOf(titaVo.getParam("BeginDate"));
		int iEndDate = Integer.valueOf(titaVo.getParam("EndDate"));
		String iKey = "";
		int txDate = Integer.valueOf(titaVo.getEntDy());// 會計日 民國年YYYMMDD

		// JcicZ443, JcicZ440, JcicZ446
		JcicZ443 iJcicZ443 = new JcicZ443();
		JcicZ443Id iJcicZ443Id = new JcicZ443Id();
		iJcicZ443Id.setSubmitKey(iSubmitKey);
		iJcicZ443Id.setCustId(iCustId);
		iJcicZ443Id.setApplyDate(iApplyDate);
		iJcicZ443Id.setCourtCode(iCourtCode);
		iJcicZ443Id.setMaxMainCode(iMaxMainCode);
		iJcicZ443Id.setAccount(iAccount);
		JcicZ443 chJcicZ443 = new JcicZ443();
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

		// 檢核項目(D-49)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2 「IDN+最大債權金融機構+調解申請日+受理調解機構代號」若未曾報送過「'440':前置調解受理申請暨請求回報債權通知資料」，予以剔退處理.***
			// 3 第3欄「債權金融機構代號」若非屬Z41「受理申請暨請求回報債權」之應回報金融機構代號，予以剔退處理.***J
			// 4 檢核第14~17欄「本金、利息、違約金、其他費用」之金額合計需等於第13欄「授信餘額」.--->(前端檢核)

			// 5 start 第20欄「最後繳息日」不可大於資料報送日.
			if ("A".equals(iTranKey)) {
				if (iFinalPayDay > txDate) {
					throw new LogicException("E0005", "「最後繳息日」不可大於資料報送日.");
				}
			} // 5 end

			// 6 第23欄「契約起始年月」不可大於第24欄「契約截止年月」.--->(前端檢核)

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 7 除最大債權金融機構報送自行債權資料外，「'440':前置調解愛理申請暨請求回報債權通知資料」第12欄「協辦行是否需自行回報債權」填報為Y時，
				// 第9欄「是否為最大債權金融機構報送」需填報為N，反之亦然.
				iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
				if (iJcicZ440 != null) {
					if ("Y".equals(iJcicZ440.getReportYn())) {
						if (!"N".equals(iIsMaxMain)) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005",
										"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
							} else {
								throw new LogicException("E0007",
										"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
							}
						}
					} else if (!"Y".equals(iIsMaxMain)) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005",
									"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
						} else {
							throw new LogicException("E0007",
									"(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
						}
					}
				}

				// 8 最大債權金融機構報送自行債權資料時，第9欄「是否為最大債權金融機構報送」需填報Y.***J

				// 9 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動本檔案資料.
				iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
				if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
					if ("A".equals(iTranKey)) {
						throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
					} else {
						throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
					}
				}
			} // 9 end

			// 檢核條件 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ443 = sJcicZ443Service.findById(iJcicZ443Id, titaVo);
			this.info("TEST===" + chJcicZ443);
			if (chJcicZ443 != null) {
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
			} catch (DBException e) {
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
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ443, uJcicZ443);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ443 = sJcicZ443Service.findById(iJcicZ443Id);
			if (iJcicZ443 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ443Log> dJcicLogZ443 = null;
			dJcicLogZ443 = sJcicZ443LogService.ukeyEq(iJcicZ443.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ443 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ443Service.delete(iJcicZ443, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
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
				iJcicZ443.setTranKey(iJcicZ443Log.getTranKey());
				iJcicZ443.setOutJcicTxtDate(iJcicZ443Log.getOutJcicTxtDate());

				try {
					sJcicZ443Service.update(iJcicZ443, titaVo);
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
