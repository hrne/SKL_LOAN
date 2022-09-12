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

/* DB容器 */
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;
import com.st1.itx.db.domain.JcicZ056Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ056LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ056Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8317")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8317 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
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

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		String iCaseStatus = titaVo.getParam("CaseStatus").trim();// 案件狀態
		int iClaimDate = 0;
		if ("E".equals(iCaseStatus)) {
			iClaimDate = Integer.valueOf(titaVo.getParam("ClaimDate1").trim());// 發文日期
		} else {
			iClaimDate = Integer.valueOf(titaVo.getParam("ClaimDate").trim());// 裁定日期
		}
		String iCourtCode = titaVo.getParam("CourtCode").trim();// 承審法院代碼
		int iYear = Integer.valueOf(titaVo.getParam("Year").trim()) + 1911;
		String iCourtDiv = titaVo.getParam("CourtDiv").trim();
		String iCourtCaseNo = titaVo.getParam("CourtCaseNo").trim();
		String iApprove = titaVo.getParam("Approve").trim();
		int iOutstandAmt = Integer.valueOf(titaVo.getParam("OutstandAmt").trim());
		int iSubAmt = Integer.valueOf(titaVo.getParam("SubAmt").trim());
		String iClaimStatus1 = titaVo.getParam("ClaimStatus1").trim();
		int iSaveDate = Integer.valueOf(titaVo.getParam("SaveDate").trim());
		String iClaimStatus2 = titaVo.getParam("ClaimStatus2").trim();
		int iSaveEndDate = Integer.valueOf(titaVo.getParam("SaveEndDate").trim());
		String iAdminName = titaVo.getParam("AdminName");
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

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
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {

				// 1 start 案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」
				if ("B".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.checkCaseStatus(iSubmitKey, iCustId,
							iClaimDate + 19110000, iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」 .");
						} else {
							throw new LogicException("E0007", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」 .");
						}
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if (!"D".equals(xJcicZ056.getTranKey()) && "A".equals(xJcicZ056.getCaseStatus())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」.");
							} else {
								throw new LogicException("E0007", "案件狀態未曾報送過「A:清算程序開始」 前，不能報送「B:清算程序終止(結)」.");
							}
						}
					}
				} // 1 end

				// 2 start 報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」
				// 或「C:清算程序開始同時終止」，且12欄「法院裁定免責確定」原填報為Y.
				if ("D".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.checkCaseStatus(iSubmitKey, iCustId,
							iClaimDate + 19110000, iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」 .");
						} else {
							throw new LogicException("E0007", "報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」 .");
						}
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if (!"D".equals(xJcicZ056.getTranKey())
									&& (("A".equals(xJcicZ056.getCaseStatus()) || "C".equals(xJcicZ056.getCaseStatus()))
											&& "Y".equals(xJcicZ056.getApprove()))) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005",
										"報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」，且12欄「法院裁定免責確定」原填報為Y.");
							} else {
								throw new LogicException("E0007",
										"報送案件狀態「D:清算撤消免責確定」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」，且12欄「法院裁定免責確定」原填報為Y.");
							}
						}
					}

				} // 2 end

				// 3.1 key值為「債務人IDN+報送單位代號+案件狀態+裁定日期+承審法院代碼」，不可重複，重複者予以剔退-case "1"檢核

				// 3.2 若非key值欄位資料需要更新，請以交易代碼'C'異動處理***

				// 8 start 報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」
				// 或「C:清算程序開始同時終止」.--->1014會議清和通知此檢核只查詢custId
				if ("H".equals(iCaseStatus)) {
					Slice<JcicZ056> sJcicZ056 = sJcicZ056Service.custIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ056 == null) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
						} else {
							throw new LogicException("E0007", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
						}
					} else {
						int flagCaseStatus = 0;
						for (JcicZ056 xJcicZ056 : sJcicZ056) {
							if (!"D".equals(xJcicZ056.getTranKey()) && ("A".equals(xJcicZ056.getCaseStatus())
									|| "C".equals(xJcicZ056.getCaseStatus()))) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
							} else {
								throw new LogicException("E0007", "報送案件狀態「H:清算復權」 前，需曾報送「A:清算程序開始」 或「C:清算程序開始同時終止」.");
							}
						}
					}

				} // 8 end

				// 4 start 案件狀態為「D:清算撤消免責確定」 時，除原必要填報項目外，第12欄「法院裁定免責確定」亦為必要填報項目，且必須填報N.
				if ("D".equals(iCaseStatus)) {
					if (!"N".equals(iApprove)) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005",
									"案件狀態為「D:清算撤消免責確定」 時，除原必要填報項目外，第12欄「法院裁定免責確定」亦為必要填報項目，且必須填報N.");
						} else {
							throw new LogicException("E0007",
									"案件狀態為「D:清算撤消免責確定」 時，除原必要填報項目外，第12欄「法院裁定免責確定」亦為必要填報項目，且必須填報N.");
						}
					}
				} // 4 end
			}

			// 5 裁定日期須小於等於報送日期--->(前端檢核)

			// 6 各金融機構清算資料報送不一致時，中心檢核事項***J

			// 7 各金融機構清算資料報送不一致時，中心檢核事項的比對邏輯***J

		} // 檢核條件 end

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
			JcicZ056 oldJcicZ056 = (JcicZ056) iDataLog.clone(uJcicZ056);
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
			
			uJcicZ056.setActualFilingDate(0);
			uJcicZ056.setActualFilingMark("");
			
			try {
				sJcicZ056Service.update(uJcicZ056, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ056, uJcicZ056);
			iDataLog.exec("L8317異動", uJcicZ056.getSubmitKey() + uJcicZ056.getCustId() + uJcicZ056.getCaseStatus()
					+ iClaimDate + uJcicZ056.getCourtCode());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ056 = sJcicZ056Service.ukeyFirst(iKey, titaVo);
			JcicZ056 uJcicZ0562 = new JcicZ056();
			uJcicZ0562 = sJcicZ056Service.holdById(iJcicZ056.getJcicZ056Id(), titaVo);
			iJcicZ056 = sJcicZ056Service.findById(iJcicZ056Id);
			if (iJcicZ056 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ056 oldJcicZ0562 = (JcicZ056) iDataLog.clone(uJcicZ0562);
			uJcicZ0562.setTranKey(iTranKey);
			uJcicZ0562.setYear(iYear);
			uJcicZ0562.setCourtDiv(iCourtDiv);
			uJcicZ0562.setCourtCaseNo(iCourtCaseNo);
			uJcicZ0562.setApprove(iApprove);
			uJcicZ0562.setOutstandAmt(iOutstandAmt);
			uJcicZ0562.setSubAmt(iSubAmt);
			uJcicZ0562.setClaimStatus1(iClaimStatus1);
			uJcicZ0562.setSaveDate(iSaveDate);
			uJcicZ0562.setClaimStatus2(iClaimStatus2);
			uJcicZ0562.setSaveEndDate(iSaveEndDate);
			uJcicZ0562.setAdminName(iAdminName);
			uJcicZ0562.setOutJcicTxtDate(0);

			Slice<JcicZ056Log> dJcicLogZ056 = null;
			dJcicLogZ056 = sJcicZ056LogService.ukeyEq(iJcicZ056.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ056 == null || "A".equals(iTranKey)) {
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
			iDataLog.setEnv(titaVo, oldJcicZ0562, uJcicZ0562);
			iDataLog.exec("L8317刪除", uJcicZ0562.getSubmitKey() + uJcicZ0562.getCustId() + uJcicZ0562.getCaseStatus()
					+ iClaimDate + uJcicZ0562.getCourtCode());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ056 = sJcicZ056Service.ukeyFirst(iKey, titaVo);
			JcicZ056 uJcicZ0563 = new JcicZ056();
			uJcicZ0563 = sJcicZ056Service.holdById(iJcicZ056.getJcicZ056Id(), titaVo);
			if (uJcicZ0563 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ056.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ056 oldJcicZ0563 = (JcicZ056) iDataLog.clone(uJcicZ0563);
			uJcicZ0563.setJcicZ056Id(iJcicZ056Id);
			uJcicZ0563.setTranKey(iTranKey);
			uJcicZ0563.setYear(iYear);
			uJcicZ0563.setCourtDiv(iCourtDiv);
			uJcicZ0563.setCourtCaseNo(iCourtCaseNo);
			uJcicZ0563.setApprove(iApprove);
			uJcicZ0563.setOutstandAmt(iOutstandAmt);
			uJcicZ0563.setSubAmt(iSubAmt);
			uJcicZ0563.setClaimStatus1(iClaimStatus1);
			uJcicZ0563.setSaveDate(iSaveDate);
			uJcicZ0563.setClaimStatus2(iClaimStatus2);
			uJcicZ0563.setSaveEndDate(iSaveEndDate);
			uJcicZ0563.setAdminName(iAdminName);
			uJcicZ0563.setUkey(iKey);

			try {
				sJcicZ056Service.update(uJcicZ0563, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0563, uJcicZ0563);
			iDataLog.exec("L8317修改", uJcicZ0563.getSubmitKey() + uJcicZ0563.getCustId() + uJcicZ0563.getCaseStatus()
					+ iClaimDate + uJcicZ0563.getCourtCode());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
