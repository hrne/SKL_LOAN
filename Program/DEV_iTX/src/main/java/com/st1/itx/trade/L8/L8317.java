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
/* DB容器 */
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;
import com.st1.itx.db.domain.JcicZ056Log;
import com.st1.itx.db.service.JcicZ056LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ056Service;

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

@Service("L8317")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8317 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ056Service sJcicZ056Service;
	@Autowired
	public JcicZ056LogService sJcicZ056LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8317 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		String iCaseStatus = titaVo.getParam("CaseStatus");// 案件狀態
		int iClaimDate = Integer.valueOf(titaVo.getParam("ClaimDate"));// 裁定日期
		String iCourtCode = titaVo.getParam("CourtCode");// 承審法院代碼
		int iYear = Integer.valueOf(titaVo.getParam("Year"));
		String iCourtDiv = titaVo.getParam("CourtDiv");
		String iCourtCaseNo = titaVo.getParam("CourtCaseNo");
		String iApprove = titaVo.getParam("Approve");
		int iOutstandAmt = Integer.valueOf(titaVo.getParam("OutstandAmt"));
		int iSubAmt = Integer.valueOf(titaVo.getParam("SubAmt"));
		String iClaimStatus1 = titaVo.getParam("ClaimStatus1");
		int iSaveDate = Integer.valueOf(titaVo.getParam("SaveDate"));
		String iClaimStatus2 = titaVo.getParam("ClaimStatus2");
		int iSaveEndDate = Integer.valueOf(titaVo.getParam("SaveEndDate"));
		String iAdminName = titaVo.getParam("AdminName");
		String iKey = "";
		int txDate = Integer.valueOf(titaVo.getEntDy());// 營業日 民國YYYMMDD

		// JcicZ056
		JcicZ056 iJcicZ056 = new JcicZ056();
		JcicZ056Id iJcicZ056Id = new JcicZ056Id();
		iJcicZ056Id.setCustId(iCustId);// 債務人IDN
		iJcicZ056Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ056Id.setCaseStatus(iCaseStatus);// 案件狀態
		iJcicZ056Id.setClaimDate(iClaimDate);// 裁定日期
		iJcicZ056Id.setCourtCode(iCourtCode);// 承審法院代碼
		JcicZ056 chJcicZ056 = new JcicZ056();

		// 檢核項目(D-68)
		if (!"4".equals(iTranKey_Tmp)) {

			if ("A".equals(iTranKey)) {
				// 1 start 案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」
				if ("B".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.checkCaseStatus(iSubmitKey, iCustId, iClaimDate + 19110000,
							iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						throw new LogicException("E0005", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」 .");
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if ("A".equals(xJcicZ056.getCaseStatus())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							throw new LogicException("E0005", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」.");
						}
					}
				} // 1 end

				// 2 start 報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」
				// 或「C:清算程序開始同時終止」，且12欄「法院裁定免責確定」原填報為Y.
				if ("D".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.checkCaseStatus(iSubmitKey, iCustId, iClaimDate + 19110000,
							iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						throw new LogicException("E0005", "報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」 .");
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if (("A".equals(xJcicZ056.getCaseStatus()) || "C".equals(xJcicZ056.getCaseStatus()))
									&& "Y".equals(xJcicZ056.getApprove())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							throw new LogicException("E0005",
									"報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」，且12欄「法院裁定免責確定」原填報為Y.");
						}
					}

				} // 2 end

				// 3.1 start key值為「債務人IDN+報送單位代號+案件狀態+裁定日期+承審法院代碼」，不可重複，重複者予以剔退
				JcicZ056 jJcicZ056 = sJcicZ056Service.findById(iJcicZ056Id, titaVo);
				if (jJcicZ056 != null) {
					throw new LogicException("E0005", "key值「債務人IDN+報送單位代號+案件狀態+裁定日期+承審法院代碼」，不可重複.");
				}

				// 3.2 若非key值欄位資料需要更新，請以交易代碼'C'異動處理***

				// 5 start 裁定日期須小於等於報送日期
				if (iClaimDate > txDate) {
					throw new LogicException("E0005", "「裁定日期/發文日期」 須小於等於報送日期.");
				}

				// 8 start 報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.
				if ("H".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.checkCaseStatus(iSubmitKey, iCustId, iClaimDate + 19110000,
							iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						throw new LogicException("E0005", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if ("A".equals(xJcicZ056.getCaseStatus()) || "C".equals(xJcicZ056.getCaseStatus())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							throw new LogicException("E0005", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
						}
					}

				} // 8 end
			}

			// 4 start 案件狀態為「D:清算撤消免責確定」 時，除原必要填報項目外，第12欄「法院裁定免責確定」亦為必要填報項目，且必須填報N.
			if ("D".equals(iCaseStatus)) {
				if (!"N".equals(iApprove)) {
					throw new LogicException("E0005", "案件狀態為「D:清算撤消免責確定」 時，除原必要填報項目外，第12欄「法院裁定免責確定」亦為必要填報項目，且必須填報N.");
				}
			}
		}

		// 6 各金融機構清算資料報送不一致時，中心檢核事項***J

		// 7 各金融機構清算資料報送不一致時，中心檢核事項的比對邏輯***J

		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ056
			chJcicZ056 = sJcicZ056Service.findById(iJcicZ056Id, titaVo);
			if (chJcicZ056 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ056.setJcicZ056Id(iJcicZ056Id);
			iJcicZ056.setTranKey(iTranKey);
			iJcicZ056.setYear(iYear);
			iJcicZ056.setCourtDiv(iCourtDiv);
			iJcicZ056.setCourtCaseNo(iCourtCaseNo);
			iJcicZ056.setApprove(iApprove);
			iJcicZ056.setOutstandAmt(iOutstandAmt);
			iJcicZ056.setSubAmt(iSubAmt);
			iJcicZ056.setClaimStatus1(iClaimStatus1);
			iJcicZ056.setSaveDate(iSaveDate);
			iJcicZ056.setClaimStatus2(iClaimStatus2);
			iJcicZ056.setSaveEndDate(iSaveEndDate);
			iJcicZ056.setAdminName(iAdminName);
			iJcicZ056.setUkey(iKey);
			try {
				sJcicZ056Service.insert(iJcicZ056, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ056 = sJcicZ056Service.ukeyFirst(iKey, titaVo);
			JcicZ056 uJcicZ056 = new JcicZ056();
			uJcicZ056 = sJcicZ056Service.holdById(iJcicZ056.getJcicZ056Id(), titaVo);
			if (uJcicZ056 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ056.setTranKey(iTranKey);
			uJcicZ056.setYear(iYear);
			uJcicZ056.setCourtDiv(iCourtDiv);
			uJcicZ056.setCourtCaseNo(iCourtCaseNo);
			uJcicZ056.setApprove(iApprove);
			uJcicZ056.setOutstandAmt(iOutstandAmt);
			uJcicZ056.setSubAmt(iSubAmt);
			uJcicZ056.setClaimStatus1(iClaimStatus1);
			uJcicZ056.setSaveDate(iSaveDate);
			uJcicZ056.setClaimStatus2(iClaimStatus2);
			uJcicZ056.setSaveEndDate(iSaveEndDate);
			uJcicZ056.setAdminName(iAdminName);
			uJcicZ056.setOutJcicTxtDate(0);
			JcicZ056 oldJcicZ056 = (JcicZ056) iDataLog.clone(uJcicZ056);
			try {
				sJcicZ056Service.update(uJcicZ056, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ056, uJcicZ056);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ056 = sJcicZ056Service.findById(iJcicZ056Id);
			if (iJcicZ056 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ056Log> dJcicLogZ056 = null;
			dJcicLogZ056 = sJcicZ056LogService.ukeyEq(iJcicZ056.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ056 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ056Service.delete(iJcicZ056, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ056Log iJcicZ056Log = dJcicLogZ056.getContent().get(0);
				iJcicZ056.setYear(iJcicZ056Log.getYear());
				iJcicZ056.setCourtDiv(iJcicZ056Log.getCourtDiv());
				iJcicZ056.setCourtCaseNo(iJcicZ056Log.getCourtCaseNo());
				iJcicZ056.setApprove(iJcicZ056Log.getApprove());
				iJcicZ056.setOutstandAmt(iJcicZ056Log.getOutstandAmt());
				iJcicZ056.setSubAmt(iJcicZ056Log.getSubAmt());
				iJcicZ056.setClaimStatus1(iJcicZ056Log.getClaimStatus1());
				iJcicZ056.setSaveDate(iJcicZ056Log.getSaveDate());
				iJcicZ056.setClaimStatus2(iJcicZ056Log.getClaimStatus2());
				iJcicZ056.setSaveEndDate(iJcicZ056Log.getSaveEndDate());
				iJcicZ056.setAdminName(iJcicZ056Log.getAdminName());

				iJcicZ056.setTranKey(iJcicZ056Log.getTranKey());
				iJcicZ056.setOutJcicTxtDate(iJcicZ056Log.getOutJcicTxtDate());
				try {
					sJcicZ056Service.update(iJcicZ056, titaVo);
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
