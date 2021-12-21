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

/* DB容器 */
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Id;
import com.st1.itx.db.domain.JcicZ055Log;
import com.st1.itx.db.service.JcicZ055LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ055Service;

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

@Service("L8316")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8316 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ055Service sJcicZ055Service;
	@Autowired
	public JcicZ055LogService sJcicZ055LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8316 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey"); // 交易代碼
		String iCustId = titaVo.getParam("CustId");// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey");// 報送單位代號
		String iCaseStatus = titaVo.getParam("CaseStatus");// 案件狀態
		int iClaimDate = Integer.valueOf(titaVo.getParam("ClaimDate"));// 裁定日期
		String iCourtCode = titaVo.getParam("CourtCode");// 承審法院代碼
		int iYear = Integer.valueOf(titaVo.getParam("Year")) + 1911;
		String iCourtDiv = titaVo.getParam("CourtDiv");
		String iCourtCaseNo = titaVo.getParam("CourtCaseNo");
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate"));
		int iPayEndDate = Integer.valueOf(titaVo.getParam("PayEndDate"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		String iIsImplement = titaVo.getParam("IsImplement");
		String iInspectName = titaVo.getParam("InspectName");
		int iOutstandAmt = Integer.valueOf(titaVo.getParam("OutstandAmt"));
		int iSubAmt = Integer.valueOf(titaVo.getParam("SubAmt"));
		String iClaimStatus1 = titaVo.getParam("ClaimStatus1");
		int iSaveDate = Integer.valueOf(titaVo.getParam("SaveDate"));
		String iClaimStatus2 = titaVo.getParam("ClaimStatus2");
		int iSaveEndDate = Integer.valueOf(titaVo.getParam("SaveEndDate"));
		String iKey = "";

		// JcicZ055
		JcicZ055 iJcicZ055 = new JcicZ055();
		JcicZ055Id iJcicZ055Id = new JcicZ055Id();
		iJcicZ055Id.setCustId(iCustId);// 債務人IDN
		iJcicZ055Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ055Id.setCaseStatus(iCaseStatus);// 案件狀態
		iJcicZ055Id.setClaimDate(iClaimDate);// 裁定日期
		iJcicZ055Id.setCourtCode(iCourtCode);// 承審法院代碼
		JcicZ055 chJcicZ055 = new JcicZ055();

		// 檢核項目(D-65)
		if (!"4".equals(iTranKey_Tmp)) {
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {

				// 1 start 案件狀態未曾報送過「1:更生程序開始」 前，不能報送「3:更生方案認可確定」
				if ("3".equals(iCaseStatus)) {
					Slice<JcicZ055> sJcicZ055 = sJcicZ055Service.checkCaseStatus(iSubmitKey, iCustId, iClaimDate + 19110000, iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ055 == null) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "案件狀態未曾報送過「1:更生程序開始」 前，不能報送「3:更生方案認可確定」 .");
						} else {
							throw new LogicException("E0007", "案件狀態未曾報送過「1:更生程序開始」 前，不能報送「3:更生方案認可確定」 .");
						}
					} else {
						int flagCaseStatus = 0;
						for (JcicZ055 xJcicZ055 : sJcicZ055) {
							if (!"D".equals(xJcicZ055.getTranKey()) && "1".equals(xJcicZ055.getCaseStatus())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "案件狀態未曾報送過「1:更生程序開始」 前，不能報送「3:更生方案認可確定」.");
							} else {
								throw new LogicException("E0007", "案件狀態未曾報送過「1:更生程序開始」 前，不能報送「3:更生方案認可確定」.");
							}
						}
					}
				} // 1 end

				// 2 start 案件狀態未曾報送過「3:更生方案認可確定」 前，不能報送「4:更生方案履行完畢」或「5:更生裁定免責確定」
				if ("4".equals(iCaseStatus) || "5".equals(iCaseStatus)) {
					Slice<JcicZ055> sJcicZ055 = sJcicZ055Service.checkCaseStatus(iSubmitKey, iCustId, iClaimDate + 19110000, iCourtCode, 0, Integer.MAX_VALUE, titaVo);
					if (sJcicZ055 == null) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "案件狀態未曾報送過「3:更生方案認可確定」 前，不能報送「4:更生方案履行完畢」或「5:更生裁定免責確定」.");
						} else {
							throw new LogicException("E0007", "案件狀態未曾報送過「3:更生方案認可確定」 前，不能報送「4:更生方案履行完畢」或「5:更生裁定免責確定」.");
						}
					} else {
						int flagCaseStatus = 0;
						for (JcicZ055 xJcicZ055 : sJcicZ055) {
							if (!"D".equals(xJcicZ055.getTranKey()) && "3".equals(xJcicZ055.getCaseStatus())) {
								flagCaseStatus = 1;
							}
						}
						if (flagCaseStatus == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException("E0005", "案件狀態未曾報送過「3:更生方案認可確定」 前，不能報送「4:更生方案履行完畢」或「5:更生裁定免責確定」.");
							} else {
								throw new LogicException("E0007", "案件狀態未曾報送過「3:更生方案認可確定」 前，不能報送「4:更生方案履行完畢」或「5:更生裁定免責確定」.");
							}
						}
					}
				} // 2 end
			}
		}

		// 3.1 key值為「債務人IDN+報送單位代號+案件狀態+裁定日期+承審法院代碼」，不可重複，重複者予以剔退-case "1"檢核
		// 3.2 若非key值欄位資料需要更新，請以交易代碼'C'異動處理***
		// 4 裁定日/撤回通知日/履行完畢日/發文日須小於等於報送日期--->前端檢核
		// 5 各金融機構更生資料報送不一致時，中心檢核事項***J
		// 6 各金融機構更生資料報送不一致時，中心檢核事項的比對邏輯***J

		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ055
			chJcicZ055 = sJcicZ055Service.findById(iJcicZ055Id, titaVo);
			if (chJcicZ055 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ055.setJcicZ055Id(iJcicZ055Id);
			iJcicZ055.setTranKey(iTranKey);
			iJcicZ055.setYear(iYear);
			iJcicZ055.setCourtDiv(iCourtDiv);
			iJcicZ055.setCourtCaseNo(iCourtCaseNo);
			iJcicZ055.setPayDate(iPayDate);
			iJcicZ055.setPayEndDate(iPayEndDate);
			iJcicZ055.setPeriod(iPeriod);
			iJcicZ055.setRate(iRate);
			iJcicZ055.setOutstandAmt(iOutstandAmt);
			iJcicZ055.setSubAmt(iSubAmt);
			iJcicZ055.setClaimStatus1(iClaimStatus1);
			iJcicZ055.setSaveDate(iSaveDate);
			iJcicZ055.setClaimStatus2(iClaimStatus2);
			iJcicZ055.setSaveEndDate(iSaveEndDate);
			iJcicZ055.setIsImplement(iIsImplement);
			iJcicZ055.setInspectName(iInspectName);

			iJcicZ055.setUkey(iKey);
			try {
				sJcicZ055Service.insert(iJcicZ055, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ055 = sJcicZ055Service.ukeyFirst(iKey, titaVo);
			JcicZ055 uJcicZ055 = new JcicZ055();
			uJcicZ055 = sJcicZ055Service.holdById(iJcicZ055.getJcicZ055Id(), titaVo);
			if (uJcicZ055 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ055.setTranKey(iTranKey);
			uJcicZ055.setYear(iYear);
			uJcicZ055.setCourtDiv(iCourtDiv);
			uJcicZ055.setCourtCaseNo(iCourtCaseNo);
			uJcicZ055.setCourtDiv(iCourtDiv);
			uJcicZ055.setCourtCaseNo(iCourtCaseNo);
			uJcicZ055.setPayDate(iPayDate);
			uJcicZ055.setPayEndDate(iPayEndDate);
			uJcicZ055.setPeriod(iPeriod);
			uJcicZ055.setRate(iRate);
			uJcicZ055.setOutstandAmt(iOutstandAmt);
			uJcicZ055.setSubAmt(iSubAmt);
			uJcicZ055.setClaimStatus1(iClaimStatus1);
			uJcicZ055.setSaveDate(iSaveDate);
			uJcicZ055.setClaimStatus2(iClaimStatus2);
			uJcicZ055.setSaveEndDate(iSaveEndDate);
			uJcicZ055.setIsImplement(iIsImplement);
			uJcicZ055.setInspectName(iInspectName);

			uJcicZ055.setOutJcicTxtDate(0);
			JcicZ055 oldJcicZ055 = (JcicZ055) iDataLog.clone(uJcicZ055);
			try {
				sJcicZ055Service.update(uJcicZ055, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ055, uJcicZ055);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ055 = sJcicZ055Service.findById(iJcicZ055Id);
			if (iJcicZ055 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ055Log> dJcicLogZ055 = null;
			dJcicLogZ055 = sJcicZ055LogService.ukeyEq(iJcicZ055.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ055 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ055Service.delete(iJcicZ055, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ055Log iJcicZ055Log = dJcicLogZ055.getContent().get(0);
				iJcicZ055.setYear(iJcicZ055Log.getYear());
				iJcicZ055.setCourtDiv(iJcicZ055Log.getCourtDiv());
				iJcicZ055.setCourtCaseNo(iJcicZ055Log.getCourtCaseNo());
				iJcicZ055.setCourtDiv(iJcicZ055Log.getCourtDiv());
				iJcicZ055.setCourtCaseNo(iJcicZ055Log.getCourtCaseNo());
				iJcicZ055.setPayDate(iJcicZ055Log.getPayDate());
				iJcicZ055.setPayEndDate(iJcicZ055Log.getPayEndDate());
				iJcicZ055.setPeriod(iJcicZ055Log.getPeriod());
				iJcicZ055.setRate(iJcicZ055Log.getRate());
				iJcicZ055.setOutstandAmt(iJcicZ055Log.getOutstandAmt());
				iJcicZ055.setSubAmt(iJcicZ055Log.getSubAmt());
				iJcicZ055.setClaimStatus1(iJcicZ055Log.getClaimStatus1());
				iJcicZ055.setSaveDate(iJcicZ055Log.getSaveDate());
				iJcicZ055.setClaimStatus2(iJcicZ055Log.getClaimStatus2());
				iJcicZ055.setSaveEndDate(iJcicZ055Log.getSaveEndDate());
				iJcicZ055.setIsImplement(iJcicZ055Log.getIsImplement());
				iJcicZ055.setInspectName(iJcicZ055Log.getInspectName());

				iJcicZ055.setTranKey(iJcicZ055Log.getTranKey());
				iJcicZ055.setOutJcicTxtDate(iJcicZ055Log.getOutJcicTxtDate());
				try {
					sJcicZ055Service.update(iJcicZ055, titaVo);
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
