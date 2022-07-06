package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
//import java.util.Calendar;
//import java.util.Date;
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
import com.st1.itx.db.domain.CdCode;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
import com.st1.itx.db.domain.JcicZ440Log;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.JcicZ440LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.springjpa.cm.L8301ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8322")
@Scope("prototype")
/**
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8322 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public L8301ServiceImpl sL8301ServiceImpl;// 有效消債條例金融機構代號檢核
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ440LogService sJcicZ440LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8322 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		int iAgreeDate = Integer.valueOf(titaVo.getParam("AgreeDate"));
		int iStartDate = Integer.valueOf(titaVo.getParam("StartDate"));
		int iRemindDate = Integer.valueOf(titaVo.getParam("RemindDate"));
		String iApplyType = titaVo.getParam("ApplyType");
		String iReportYn = titaVo.getParam("ReportYn");
		String iNotBankId1 = titaVo.getParam("NotBankId1");
		String iNotBankId2 = titaVo.getParam("NotBankId2");
		String iNotBankId3 = titaVo.getParam("NotBankId3");
		String iNotBankId4 = titaVo.getParam("NotBankId4");
		String iNotBankId5 = titaVo.getParam("NotBankId5");
		String iNotBankId6 = titaVo.getParam("NotBankId6");
		String iKey = "";
		String[] sNotBankId = { iNotBankId1, iNotBankId2, iNotBankId3, iNotBankId4, iNotBankId5, iNotBankId6 };// 未揭露債權機構代號集合
		List<String> iL8301SqlReturn = new ArrayList<>(); // NegFinAcct有效消債條例金融機構代號集合

		// JcicZ440
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setSubmitKey(iSubmitKey);
		JcicZ440 chJcicZ440 = new JcicZ440();
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-44)
		if (!"4".equals(iTranKey_Tmp)) {
			if ("A".equals(iTranKey)) {
				// 2 IDN+調解申請日，不能重複，若有重複，剔退處理.
				Slice<JcicZ440> sJcicZ440 = sJcicZ440Service.custRcEq(iCustId, iApplyDate + 19110000, 0,
						Integer.MAX_VALUE, titaVo);
				if (sJcicZ440 != null) {
					throw new LogicException("E0005", "IDN+調解申請日，不能重複.");
				}
			}

			// 3 第5欄「調解申請日」不得大於「資料報送日」，否則予以剔退.--->(前端檢核)
			// 4 第8欄「同意書取得日期」不得大於「資料報送日」，否則予以剔退.--->(前端檢核)
			// 5 第9欄「首次調解日」小於等於第8欄「同意書取得日期」時，第12欄「協辦行是否需自行回報債權」需填報為'N'，否則予以剔退.--->(前端檢核)
			// 6 start 檢核第13~18「未揭露債權機構代號」，若不屬於有效消債條例金融機構代號，則予剔退。
			try {
				iL8301SqlReturn = sL8301ServiceImpl.findData(this.index, this.limit, titaVo);
			} catch (Exception e) {
				// E5004 讀取DB語法發生問題
				this.info("NegFinAcct ErrorForSql=" + e);
				throw new LogicException(titaVo, "E5004", "");
			}
			if (iL8301SqlReturn != null) {
				int flagFind = 0;
				for (String xNotBankId : sNotBankId) {
					if (!xNotBankId.trim().isEmpty()) {
						for (String xL8301SqlReturn : iL8301SqlReturn) {
							if (xNotBankId.equals(xL8301SqlReturn)) {
								flagFind = 1;
								break;
							}
						}
						if (flagFind == 0) {
							if ("A".equals(iTranKey)) {
								throw new LogicException(titaVo, "E0005", "未揭露債權機構代號" + xNotBankId + "不屬於有效消債條例金融機構代號");
							} else {
								throw new LogicException(titaVo, "E0007", "未揭露債權機構代號" + xNotBankId + "不屬於有效消債條例金融機構代號");
							}
						}
						flagFind = 0;
					}
				}
			} // 6 end

			// 7 start 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動本檔案資料.");
				}
			}
			// 7 end

			// 檢核條件 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ440
			chJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
			if (chJcicZ440 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ440.setJcicZ440Id(iJcicZ440Id);
			iJcicZ440.setTranKey(iTranKey);
			iJcicZ440.setAgreeDate(iAgreeDate);
			iJcicZ440.setStartDate(iStartDate);
			iJcicZ440.setRemindDate(iRemindDate);
			iJcicZ440.setApplyType(iApplyType);
			iJcicZ440.setReportYn(iReportYn);
			iJcicZ440.setNotBankId1(iNotBankId1);
			iJcicZ440.setNotBankId2(iNotBankId2);
			iJcicZ440.setNotBankId3(iNotBankId3);
			iJcicZ440.setNotBankId4(iNotBankId4);
			iJcicZ440.setNotBankId5(iNotBankId5);
			iJcicZ440.setNotBankId6(iNotBankId6);
			iJcicZ440.setUkey(iKey);
			try {
				sJcicZ440Service.insert(iJcicZ440, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ440 = sJcicZ440Service.ukeyFirst(iKey, titaVo);
			JcicZ440 uJcicZ440 = new JcicZ440();
			uJcicZ440 = sJcicZ440Service.holdById(iJcicZ440.getJcicZ440Id(), titaVo);
			if (uJcicZ440 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ440 oldJcicZ440 = (JcicZ440) iDataLog.clone(uJcicZ440);
			uJcicZ440.setAgreeDate(iAgreeDate);
			uJcicZ440.setStartDate(iStartDate);
			uJcicZ440.setRemindDate(iRemindDate);
			uJcicZ440.setApplyType(iApplyType);
			uJcicZ440.setReportYn(iReportYn);
			uJcicZ440.setNotBankId1(iNotBankId1);
			uJcicZ440.setNotBankId2(iNotBankId2);
			uJcicZ440.setNotBankId3(iNotBankId3);
			uJcicZ440.setNotBankId4(iNotBankId4);
			uJcicZ440.setNotBankId5(iNotBankId5);
			iJcicZ440.setNotBankId6(iNotBankId6);
			uJcicZ440.setTranKey(iTranKey);
			uJcicZ440.setOutJcicTxtDate(0);
			try {
				sJcicZ440Service.update(uJcicZ440, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ440, uJcicZ440);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id);
			if (iJcicZ440 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ440Log> dJcicLogZ440 = null;
			dJcicLogZ440 = sJcicZ440LogService.ukeyEq(iJcicZ440.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ440 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ440Service.delete(iJcicZ440, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ440Log iJcicZ440Log = dJcicLogZ440.getContent().get(0);
				iJcicZ440.setAgreeDate(iJcicZ440Log.getAgreeDate());
				iJcicZ440.setStartDate(iJcicZ440Log.getStartDate());
				iJcicZ440.setRemindDate(iJcicZ440Log.getRemindDate());
				iJcicZ440.setApplyType(iJcicZ440Log.getApplyType());
				iJcicZ440.setReportYn(iJcicZ440Log.getReportYn());
				iJcicZ440.setNotBankId1(iJcicZ440Log.getNotBankId1());
				iJcicZ440.setNotBankId2(iJcicZ440Log.getNotBankId2());
				iJcicZ440.setNotBankId3(iJcicZ440Log.getNotBankId3());
				iJcicZ440.setNotBankId4(iJcicZ440Log.getNotBankId4());
				iJcicZ440.setNotBankId5(iJcicZ440Log.getNotBankId5());
				iJcicZ440.setNotBankId6(iJcicZ440Log.getNotBankId6());
				iJcicZ440.setTranKey(iJcicZ440Log.getTranKey());
				iJcicZ440.setOutJcicTxtDate(iJcicZ440Log.getOutJcicTxtDate());
				try {
					sJcicZ440Service.update(iJcicZ440, titaVo);
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

	public String dealBankName(String iCourtCode, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", iCourtCode, titaVo);
		String JcicBankName = "";// 80碼長度
		if (tCdCode != null) {
			JcicBankName = tCdCode.getItem();
		}
		return JcicBankName;
	}
}