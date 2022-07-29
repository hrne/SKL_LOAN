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
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;
import com.st1.itx.db.domain.JcicZ049Log;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.service.JcicZ049LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ049Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8310")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8310 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ049Service sJcicZ049Service;
	@Autowired
	public JcicZ049LogService sJcicZ049LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8310 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		int iClaimStatus = Integer.valueOf(titaVo.getParam("ClaimStatus").trim());
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		int iYear = Integer.valueOf(titaVo.getParam("Year").trim())+1911;
		String iCourtDiv = titaVo.getParam("CourtDiv").trim();
		String iCourtCaseNo = titaVo.getParam("CourtCaseNo").trim();
		String iApprove = titaVo.getParam("Approve").trim();
		int iClaimDate = Integer.valueOf(titaVo.getParam("ClaimDate").trim());
		String iKey = "";
		// JcicZ049, JcicZ047
		JcicZ049 iJcicZ049 = new JcicZ049();
		JcicZ049Id iJcicZ049Id = new JcicZ049Id();
		iJcicZ049Id.setCustId(iCustId);// 債務人IDN
		iJcicZ049Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ049Id.setRcDate(iRcDate);
		JcicZ049 chJcicZ049 = new JcicZ049();
		JcicZ047 iJcicZ047 = new JcicZ047();
		JcicZ047Id iJcicZ047Id = new JcicZ047Id();
		iJcicZ047Id.setCustId(iCustId);// 債務人IDN
		iJcicZ047Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ047Id.setRcDate(iRcDate);

		// 檢核項目(D-22)
		if (!"4".equals(iTranKey_Tmp)) {
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				// 3.1 start 完整key值未曾報送過'47':金融機構無擔保債務協議資料檔案則予以剔退
				iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
				if (iJcicZ047 == null) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "需先報送過(47)金融機構無擔保債務協議資料.");
					} else {
						throw new LogicException("E0007", "需先報送過(47)金融機構無擔保債務協議資料.");
					}
				} else { // 3.2 start '47':金融機構無擔保債務協議資料檔案第19欄「簽約完成日期」空白則予以剔退
					if ("D".equals(iJcicZ047.getTranKey()) || iJcicZ047.getSignDate() == 0) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "需先報送過(47)金融機構無擔保債務協議資料，且「簽約完成日期」不可空白.");
						} else {
							throw new LogicException("E0007", "需先報送過(47)金融機構無擔保債務協議資料，且「簽約完成日期」不可空白.");
						}
					}
				}
				// 3 end

				// 4 第7欄案件進度填報2:最大債權金融機構接獲法院裁定書，則第9-14欄承審法院相關內容必須有值，不能空白，否則則予以剔退--->前端檢核
				// 5.1 法院裁定日期需大於遞狀日期--->前端檢核

				// 5.2 start遞狀日期需大於或等於簽約日期
				iJcicZ047 = sJcicZ047Service.findById(iJcicZ047Id, titaVo);
				if (iJcicZ047 != null && iApplyDate < iJcicZ047.getSignDate()) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "遞狀日期需大於或等於(47)金融機構無擔保債務協議資料之「簽約完成日期」.");
					} else {
						throw new LogicException("E0007", "遞狀日期需大於或等於(47)金融機構無擔保債務協議資料之「簽約完成日期」.");
					}
				} // 5.2 end
			}
			// 檢核項目end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ049
			chJcicZ049 = sJcicZ049Service.findById(iJcicZ049Id, titaVo);
			if (chJcicZ049 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ049.setJcicZ049Id(iJcicZ049Id);
			iJcicZ049.setTranKey(iTranKey);
			iJcicZ049.setClaimStatus(iClaimStatus);
			iJcicZ049.setApplyDate(iApplyDate);
			iJcicZ049.setCourtCode(iCourtCode);
			iJcicZ049.setYear(iYear);
			iJcicZ049.setCourtDiv(iCourtDiv);
			iJcicZ049.setCourtCaseNo(iCourtCaseNo);
			iJcicZ049.setApprove(iApprove);
			iJcicZ049.setClaimDate(iClaimDate);
			iJcicZ049.setUkey(iKey);
			try {
				sJcicZ049Service.insert(iJcicZ049, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ049 = sJcicZ049Service.ukeyFirst(iKey, titaVo);
			JcicZ049 uJcicZ049 = new JcicZ049();
			uJcicZ049 = sJcicZ049Service.holdById(iJcicZ049.getJcicZ049Id(), titaVo);
			if (uJcicZ049 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ049 oldJcicZ049 = (JcicZ049) iDataLog.clone(uJcicZ049);
			uJcicZ049.setTranKey(iTranKey);
			uJcicZ049.setClaimStatus(iClaimStatus);
			uJcicZ049.setApplyDate(iApplyDate);
			uJcicZ049.setCourtCode(iCourtCode);
			uJcicZ049.setYear(iYear);
			uJcicZ049.setCourtDiv(iCourtDiv);
			uJcicZ049.setCourtCaseNo(iCourtCaseNo);
			uJcicZ049.setApprove(iApprove);
			uJcicZ049.setClaimDate(iClaimDate);
			uJcicZ049.setOutJcicTxtDate(0);
			try {
				sJcicZ049Service.update(uJcicZ049, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ049, uJcicZ049);
			iDataLog.exec("L8310異動", uJcicZ049.getSubmitKey()+uJcicZ049.getCustId()+uJcicZ049.getRcDate());
			break;
			//2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ049 = sJcicZ049Service.ukeyFirst(iKey, titaVo);
			JcicZ049 uJcicZ0492 = new JcicZ049();
			uJcicZ0492 = sJcicZ049Service.holdById(iJcicZ049.getJcicZ049Id(), titaVo);
			iJcicZ049 = sJcicZ049Service.findById(iJcicZ049Id);
			if (iJcicZ049 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ049 oldJcicZ0492 = (JcicZ049) iDataLog.clone(uJcicZ0492);
			uJcicZ0492.setTranKey(iTranKey);
			uJcicZ0492.setClaimStatus(iClaimStatus);
			uJcicZ0492.setApplyDate(iApplyDate);
			uJcicZ0492.setCourtCode(iCourtCode);
			uJcicZ0492.setYear(iYear);
			uJcicZ0492.setCourtDiv(iCourtDiv);
			uJcicZ0492.setCourtCaseNo(iCourtCaseNo);
			uJcicZ0492.setApprove(iApprove);
			uJcicZ0492.setClaimDate(iClaimDate);
			uJcicZ0492.setOutJcicTxtDate(0);
			
			Slice<JcicZ049Log> dJcicLogZ049 = null;
			dJcicLogZ049 = sJcicZ049LogService.ukeyEq(iJcicZ049.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ049 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ049Service.delete(iJcicZ049, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ049Log iJcicZ049Log = dJcicLogZ049.getContent().get(0);
				iJcicZ049.setClaimStatus(iJcicZ049Log.getClaimStatus());
				iJcicZ049.setApplyDate(iJcicZ049Log.getApplyDate());
				iJcicZ049.setCourtCode(iJcicZ049Log.getCourtCode());
				iJcicZ049.setYear(iJcicZ049Log.getYear());
				iJcicZ049.setCourtDiv(iJcicZ049Log.getCourtDiv());
				iJcicZ049.setCourtCaseNo(iJcicZ049Log.getCourtCaseNo());
				iJcicZ049.setApprove(iJcicZ049Log.getApprove());
				iJcicZ049.setClaimDate(iJcicZ049Log.getClaimDate());
				iJcicZ049.setTranKey(iJcicZ049Log.getTranKey());
				iJcicZ049.setOutJcicTxtDate(iJcicZ049Log.getOutJcicTxtDate());
				try {
					sJcicZ049Service.update(iJcicZ049, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0492, uJcicZ0492);
			iDataLog.exec("L8310刪除", uJcicZ0492.getSubmitKey()+uJcicZ0492.getCustId()+uJcicZ0492.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
