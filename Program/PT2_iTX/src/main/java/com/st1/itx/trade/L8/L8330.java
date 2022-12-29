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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Id;
import com.st1.itx.db.domain.JcicZ451Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ451LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ451Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8330")
@Scope("prototype")
/**
 * @author Fegie/ Mata
 * @version 1.0.0
 */
public class L8330 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ451Service sJcicZ451Service;
	@Autowired
	public JcicZ451LogService sJcicZ451LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8330 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		int iDelayYM = Integer.valueOf(titaVo.getParam("DelayYM").trim()) + 191100;
		String iDelayCode = titaVo.getParam("DelayCode").trim();
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		int sCovDelayYM = 0;// 延期繳款累計期數(「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款')
		int sDelayYM = 0;// 延期繳款累計期數(「延期繳款原因」為非'L')

		// JcicZ451
		JcicZ451 iJcicZ451 = new JcicZ451();
		JcicZ451Id iJcicZ451Id = new JcicZ451Id();
		iJcicZ451Id.setApplyDate(iApplyDate);
		iJcicZ451Id.setCourtCode(iCourtCode);
		iJcicZ451Id.setCustId(iCustId);
		iJcicZ451Id.setSubmitKey(iSubmitKey);
		iJcicZ451Id.setDelayYM(iDelayYM);
		JcicZ451 chJcicZ451 = new JcicZ451();
		JcicZ447 iJcicZ447 = new JcicZ447();
		JcicZ447Id iJcicZ447Id = new JcicZ447Id();
		iJcicZ447Id.setSubmitKey(iSubmitKey);
		iJcicZ447Id.setCustId(iCustId);
		iJcicZ447Id.setApplyDate(iApplyDate);
		iJcicZ447Id.setCourtCode(iCourtCode);
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setSubmitKey(iSubmitKey);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);

		// 檢核項目(D-60)
		if (!"4".equals(iTranKey_Tmp)) {

			if (!"D".equals(iTranKey)) {
				// 2 start
				// 需檢核「IDN+報送單位代號+調解申請日+受理調解機構代號+最大債權金融機構」是否曾報送過「'447':金融機構無擔保債務協議資料」，若不存在予以剔退處理。
				iJcicZ447 = sJcicZ447Service.findById(iJcicZ447Id, titaVo);
				if (iJcicZ447 == null || "D".equals(iJcicZ447.getTranKey())) {
					if ("A".equals(iTranKey)) {
						throw new LogicException(titaVo, "E0005",
								"「IDN+報送單位代號+調解申請日+受理調解機構代號+最大債權金融機構」未曾報送(447)前置調解金融機構無擔保債務協議資料.");
					} else {
						throw new LogicException(titaVo, "E0007",
								"「IDN+報送單位代號+調解申請日+受理調解機構代號+最大債權金融機構」未曾報送(447)前置調解金融機構無擔保債務協議資料.");
					}
				} // 2 end
				
				// 4 start 延期繳款累積期數(月份)不得超過6期
				if (!"L".equals(iDelayCode)) {
					Slice<JcicZ451> sJcicZ451 = sJcicZ451Service.otherEq(iSubmitKey, iCustId, iApplyDate+19110000, iCourtCode,iDelayYM, 0,  1, titaVo);
					if(sJcicZ451 != null) {
						throw new LogicException("E0005", "延期繳款累計期數(月份)不得超過6期.");
					}
				}	
				// 6.2 start 「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】，則不受上述檢核4的限制.
				if ("L".equals(iDelayCode)) {
					sCovDelayYM = 1;
				} else {
					sDelayYM = 1;
				}
				// @@@SQL-Function要改为：custRcSubCourtEq
				Slice<JcicZ451> sJcicZ451 = sJcicZ451Service.custIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ451 != null) {
					for (JcicZ451 xJcicZ451 : sJcicZ451) {
						if (!"D".equals(xJcicZ451.getTranKey())
								&& !titaVo.getParam("Ukey").equals(xJcicZ451.getUkey())) {
							if ("L".equals(xJcicZ451.getDelayCode())) {
								sCovDelayYM++;
							} else {
								sDelayYM++;
							}
						}
					}
					if (sDelayYM > 6) {
						if ("A".equals(iTranKey)) {
							throw new LogicException("E0005", "延期繳款累計期數(月份)不得超過6期.");
						} else {
							throw new LogicException("E0007", "延期繳款累計期數(月份)不得超過6期.");
						}
					} 
//					else if (sCovDelayYM > 6) {
//						if ("A".equals(iTranKey)) {
//							throw new LogicException("E0005", "「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】.");
//						} else {
//							throw new LogicException("E0007", "「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'【限累計申請最多6期】.");
//						}
//					}
				} // 4, 6.2 end
			}
			// 3 「延期繳款年月」不得小於「調解申請日」--->(前端檢核)

			// 5 start 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				}
			} // 5 end

			// 6.1「延期繳款原因」為'L:受嚴重特殊傳染性肺炎疫情影響繳款'***J
		}
		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ451
			chJcicZ451 = sJcicZ451Service.findById(iJcicZ451Id, titaVo);
			if (chJcicZ451 != null) {
				throw new LogicException("E0002", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ451.setJcicZ451Id(iJcicZ451Id);
			iJcicZ451.setTranKey(iTranKey);
			iJcicZ451.setDelayCode(iDelayCode);
			iJcicZ451.setUkey(iKey);
			try {
				sJcicZ451Service.insert(iJcicZ451, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ451 = sJcicZ451Service.ukeyFirst(iKey, titaVo);
			JcicZ451 uJcicZ451 = new JcicZ451();
			uJcicZ451 = sJcicZ451Service.holdById(iJcicZ451.getJcicZ451Id(), titaVo);
			if (uJcicZ451 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ451 oldJcicZ451 = (JcicZ451) iDataLog.clone(uJcicZ451);
			uJcicZ451.setDelayCode(iDelayCode);
			uJcicZ451.setTranKey(iTranKey);
			uJcicZ451.setOutJcicTxtDate(0);
			
			uJcicZ451.setActualFilingDate(0);
			uJcicZ451.setActualFilingMark("");
			
			try {
				sJcicZ451Service.update(uJcicZ451, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ451, uJcicZ451);
//			iDataLog.exec("L8330異動", uJcicZ451.getSubmitKey() + uJcicZ451.getCustId() + uJcicZ451.getApplyDate()
//					+ uJcicZ451.getCourtCode() + (uJcicZ451.getDelayYM() - 191100));
			iDataLog.exec("L8330異動", uJcicZ451.getUkey());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ451 = sJcicZ451Service.ukeyFirst(iKey, titaVo);
			JcicZ451 uJcicZ4512 = new JcicZ451();
			uJcicZ4512 = sJcicZ451Service.holdById(iJcicZ451.getJcicZ451Id(), titaVo);
			iJcicZ451 = sJcicZ451Service.findById(iJcicZ451Id);
			if (iJcicZ451 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ451 oldJcicZ4512 = (JcicZ451) iDataLog.clone(uJcicZ4512);
			uJcicZ4512.setDelayCode(iDelayCode);
			uJcicZ4512.setTranKey(iTranKey);
			uJcicZ4512.setOutJcicTxtDate(0);

			Slice<JcicZ451Log> dJcicLogZ451 = null;
			dJcicLogZ451 = sJcicZ451LogService.ukeyEq(iJcicZ451.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ451 == null || "A".equals(iTranKey) ) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ451Service.delete(iJcicZ451, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ451Log iJcicZ451Log = dJcicLogZ451.getContent().get(0);
				iJcicZ451.setDelayCode(iJcicZ451Log.getDelayCode());
				iJcicZ451.setTranKey(iJcicZ451Log.getTranKey());
				iJcicZ451.setOutJcicTxtDate(iJcicZ451Log.getOutJcicTxtDate());
				try {
					sJcicZ451Service.update(iJcicZ451, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}

			iDataLog.setEnv(titaVo, oldJcicZ4512, uJcicZ4512);
//			iDataLog.exec("L8330刪除", uJcicZ4512.getSubmitKey() + uJcicZ4512.getCustId() + uJcicZ4512.getApplyDate()
//					+ uJcicZ4512.getCourtCode() + (uJcicZ4512.getDelayYM() - 191100));
			iDataLog.exec("L8330刪除", uJcicZ4512.getUkey());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ451 = sJcicZ451Service.ukeyFirst(iKey, titaVo);
			JcicZ451 uJcicZ4513 = new JcicZ451();
			uJcicZ4513 = sJcicZ451Service.holdById(iJcicZ451.getJcicZ451Id(), titaVo);
			if (uJcicZ4513 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ451.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ451 oldJcicZ4513 = (JcicZ451) iDataLog.clone(uJcicZ4513);
			uJcicZ4513.setJcicZ451Id(iJcicZ451Id);
			uJcicZ4513.setTranKey(iTranKey);
			uJcicZ4513.setDelayCode(iDelayCode);
			uJcicZ4513.setUkey(iKey);

			try {
				sJcicZ451Service.update(uJcicZ4513, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ4513, uJcicZ4513);
//			iDataLog.exec("L8330修改", uJcicZ4513.getSubmitKey() + uJcicZ4513.getCustId() + uJcicZ4513.getApplyDate()
//					+ uJcicZ4513.getCourtCode() + (uJcicZ4513.getDelayYM() - 191100));
			iDataLog.exec("L8330修改", uJcicZ4513.getUkey());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}