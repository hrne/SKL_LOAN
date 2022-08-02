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
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.domain.JcicZ443Log;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ443LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.service.JcicZ446Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


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
	public CustMainService sCustMainService;
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
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iAccount = titaVo.getParam("Account").trim();
		String iIsMaxMain = titaVo.getParam("IsMaxMain").trim();
		String iGuarantyType = titaVo.getParam("GuarantyType").trim();
		BigDecimal iLoanAmt = new BigDecimal(titaVo.getParam("LoanAmt").trim());
		BigDecimal iCreditAmt = new BigDecimal(titaVo.getParam("CreditAmt").trim());
		BigDecimal iPrincipal = new BigDecimal(titaVo.getParam("Principal").trim());
		BigDecimal iInterest = new BigDecimal(titaVo.getParam("Interest").trim());
		BigDecimal iPenalty = new BigDecimal(titaVo.getParam("Penalty").trim());
		BigDecimal iOther = new BigDecimal(titaVo.getParam("Other").trim());
		BigDecimal iTerminalPayAmt = new BigDecimal(titaVo.getParam("TerminalPayAmt").trim());
		BigDecimal iLatestPayAmt = new BigDecimal(titaVo.getParam("LatestPayAmt").trim());
		int iFinalPayDay = Integer.valueOf(titaVo.getParam("FinalPayDay").trim());
		BigDecimal iNotyetacQuit = new BigDecimal(titaVo.getParam("NotyetacQuit").trim());
		int iMothPayDay = Integer.valueOf(titaVo.getParam("MothPayDay").trim());
		int iBeginDate = Integer.valueOf(titaVo.getParam("BeginDate").trim())+191100;
		int iEndDate = Integer.valueOf(titaVo.getParam("EndDate").trim())+191100;
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
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
			// 5 第20欄「最後繳息日」不可大於資料報送日.--->(前端檢核)
			// 6 第23欄「契約起始年月」不可大於第24欄「契約截止年月」.--->(前端檢核)

			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 7 除最大債權金融機構報送自行債權資料外，「'440':前置調解愛理申請暨請求回報債權通知資料」第12欄「協辦行是否需自行回報債權」填報為Y時，
				// 第9欄「是否為最大債權金融機構報送」需填報為N，反之亦然.
				iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
				if (iJcicZ440 != null) {
					if ("Y".equals(iJcicZ440.getReportYn())) {
						if (!"N".equals(iIsMaxMain)) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
							} else {
								throw new LogicException("E0007", "(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為Y時，本檔案「是否為最大債權金融機構報送」需填報為N.");
							}
						}
					} else if (!"Y".equals(iIsMaxMain)) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
						} else {
							throw new LogicException("E0007", "(440)前置調解愛理申請暨請求回報債權通知資料之「協辦行是否需自行回報債權」填報為N時，本檔案「是否為最大債權金融機構報送」需填報為Y");
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
			JcicZ443 oldJcicZ443 = (JcicZ443) iDataLog.clone(uJcicZ443);
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
			try {
				sJcicZ443Service.update(uJcicZ443, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ443, uJcicZ443);
			iDataLog.exec("L8324異動",uJcicZ443.getSubmitKey()+uJcicZ443.getCustId()+uJcicZ443.getApplyDate()+uJcicZ443.getCourtCode()+uJcicZ443.getMaxMainCode());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ443 = sJcicZ443Service.ukeyFirst(iKey, titaVo);
			JcicZ443 uJcicZ4432 = new JcicZ443();
			uJcicZ4432 = sJcicZ443Service.holdById(iJcicZ443.getJcicZ443Id(), titaVo);
			iJcicZ443 = sJcicZ443Service.findById(iJcicZ443Id);
			if (iJcicZ443 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ443 oldJcicZ4432 = (JcicZ443) iDataLog.clone(uJcicZ4432);
			uJcicZ4432.setTranKey(iTranKey);
			uJcicZ4432.setIsMaxMain(iIsMaxMain);
			uJcicZ4432.setGuarantyType(iGuarantyType);
			uJcicZ4432.setLoanAmt(iLoanAmt);
			uJcicZ4432.setCreditAmt(iCreditAmt);
			uJcicZ4432.setPrincipal(iPrincipal);
			uJcicZ4432.setInterest(iInterest);
			uJcicZ4432.setPenalty(iPenalty);
			uJcicZ4432.setOther(iOther);
			uJcicZ4432.setTerminalPayAmt(iTerminalPayAmt);
			uJcicZ4432.setLatestPayAmt(iLatestPayAmt);
			uJcicZ4432.setFinalPayDay(iFinalPayDay);
			uJcicZ4432.setNotyetacQuit(iNotyetacQuit);
			uJcicZ4432.setMothPayDay(iMothPayDay);
			uJcicZ4432.setBeginDate(iBeginDate);
			uJcicZ4432.setEndDate(iEndDate);
			uJcicZ4432.setOutJcicTxtDate(0);
			
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
			iDataLog.setEnv(titaVo, oldJcicZ4432, uJcicZ4432);
			iDataLog.exec("L8324刪除",uJcicZ4432.getSubmitKey()+uJcicZ4432.getCustId()+uJcicZ4432.getApplyDate()+uJcicZ4432.getCourtCode()+uJcicZ4432.getMaxMainCode());
			default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
