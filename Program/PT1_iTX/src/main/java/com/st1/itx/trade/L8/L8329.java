package com.st1.itx.trade.L8;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.domain.JcicZ450Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ450LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ450Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8329")
@Scope("prototype")
/**
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8329 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ450Service sJcicZ450Service;
	@Autowired
	public JcicZ450LogService sJcicZ450LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8329 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate").trim());
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		int iPayAmt = Integer.valueOf(titaVo.getParam("PayAmt").trim());
		int iSumRepayActualAmt = Integer.valueOf(titaVo.getParam("SumRepayActualAmt").trim());
		int iSumRepayShouldAmt = Integer.valueOf(titaVo.getParam("SumRepayShouldAmt").trim());
		String iPayStatus = titaVo.getParam("PayStatus");
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ450, JcicZ446, JcicZ447
		JcicZ450 iJcicZ450 = new JcicZ450();
		JcicZ450Id iJcicZ450Id = new JcicZ450Id();
		iJcicZ450Id.setSubmitKey(iSubmitKey);
		iJcicZ450Id.setCustId(iCustId);
		iJcicZ450Id.setApplyDate(iApplyDate);
		iJcicZ450Id.setCourtCode(iCourtCode);
		iJcicZ450Id.setPayDate(iPayDate);
		JcicZ450 chJcicZ450 = new JcicZ450();
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

		// 檢核項目(D-58)
		if (!"4".equals(iTranKey_Tmp)) {

			if (!"D".equals(iTranKey)) {
				// 2.1 start
				// 需檢核「IDN+報送單位代號+調解申請日+受理調解機構代號」是否曾報送過「'447':金融機構無擔保債務協議資料」，若不存在予以剔退處理。
				iJcicZ447 = sJcicZ447Service.findById(iJcicZ447Id, titaVo);
				if (iJcicZ447 == null || "D".equals(iJcicZ447.getTranKey())) {
					if ("A".equals(iTranKey)) {
						throw new LogicException(titaVo, "E0005", "「IDN+報送單位代號+調解申請日+受理調解機構代號」未曾報送(447)前置調解金融機構無擔保債務協議資料.");
					} else {
						throw new LogicException(titaVo, "E0007", "「IDN+報送單位代號+調解申請日+受理調解機構代號」未曾報送(447)前置調解金融機構無擔保債務協議資料.");
					}
				} // 2.1 end

				// 3 start 累計實際還款金額不等於該IDN所有已報送本檔案資料繳款金額之合計(含本次繳款金額)
				Slice<JcicZ450> sJcicZ450 = sJcicZ450Service.custIdEq(iCustId, 0, Integer.MAX_VALUE, titaVo);
				int sPayAmt = 0;// IND所有已報送之繳款金額合計
				if (sJcicZ450 == null) {
					if (iPayAmt != iSumRepayActualAmt) {
						if ("A".equals(iTranKey)) {
							throw new LogicException(titaVo, "E0005", "[累計繳款金額]不等於該IND所有已報送之[本次繳款金額]合計(含今日).");
						} else {
							throw new LogicException(titaVo, "E0007", "[累計繳款金額]不等於該IND所有已報送之[本次繳款金額]合計(含今日).");
						}
					}
				} else {
					for (JcicZ450 xJcicZ450 : sJcicZ450) {
						if (!"D".equals(xJcicZ450.getTranKey()) && !titaVo.getParam("Ukey").equals(xJcicZ450.getUkey())) {
							sPayAmt += xJcicZ450.getPayAmt();
						}
					}
				}
				if ((sPayAmt + iPayAmt) != iSumRepayActualAmt) {
					if ("A".equals(iTranKey)) {
						throw new LogicException(titaVo, "E0005", "[累計繳款金額]不等於該IND所有已報送之[本次繳款金額]合計(含今日).");
					} else {
						throw new LogicException(titaVo, "E0007", "[累計繳款金額]不等於該IND所有已報送之[本次繳款金額]合計(含今日).");
					}
				} // 3 end
			}

			// 4 「繳款日期」不得大於資料報送日期--->前端檢核

			// 2.2 start 需檢核「IDN+報送單位代號+調解申請日+受理調解機構代號」是否已報送結案，已報送予以剔退處理。
			// 5 start 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				}
			} // 5 end
		}

		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ450
			chJcicZ450 = sJcicZ450Service.findById(iJcicZ450Id, titaVo);
			if (chJcicZ450 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ450.setJcicZ450Id(iJcicZ450Id);
			iJcicZ450.setTranKey(iTranKey);
			iJcicZ450.setPayAmt(iPayAmt);
			iJcicZ450.setSumRepayActualAmt(iSumRepayActualAmt);
			iJcicZ450.setSumRepayShouldAmt(iSumRepayShouldAmt);
			iJcicZ450.setPayStatus(iPayStatus);
			iJcicZ450.setUkey(iKey);
			try {
				sJcicZ450Service.insert(iJcicZ450, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ450 = sJcicZ450Service.ukeyFirst(iKey, titaVo);
			JcicZ450 uJcicZ450 = new JcicZ450();
			uJcicZ450 = sJcicZ450Service.holdById(iJcicZ450.getJcicZ450Id(), titaVo);
			if (uJcicZ450 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ450 oldJcicZ450 = (JcicZ450) iDataLog.clone(uJcicZ450);
			uJcicZ450.setPayAmt(iPayAmt);
			uJcicZ450.setSumRepayActualAmt(iSumRepayActualAmt);
			uJcicZ450.setSumRepayShouldAmt(iSumRepayShouldAmt);
			uJcicZ450.setPayStatus(iPayStatus);
			uJcicZ450.setTranKey(iTranKey);
			uJcicZ450.setOutJcicTxtDate(0);
			try {
				sJcicZ450Service.update(iJcicZ450, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ450, uJcicZ450);
			iDataLog.exec("L8329異動", uJcicZ450.getSubmitKey() + uJcicZ450.getCustId() + uJcicZ450.getApplyDate() + uJcicZ450.getCourtCode() + uJcicZ450.getPayDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ450 = sJcicZ450Service.ukeyFirst(iKey, titaVo);
			JcicZ450 uJcicZ4502 = new JcicZ450();
			uJcicZ4502 = sJcicZ450Service.holdById(iJcicZ450.getJcicZ450Id(), titaVo);
			iJcicZ450 = sJcicZ450Service.findById(iJcicZ450Id);
			if (iJcicZ450 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ450 oldJcicZ4502 = (JcicZ450) iDataLog.clone(uJcicZ4502);
			uJcicZ4502.setPayAmt(iPayAmt);
			uJcicZ4502.setSumRepayActualAmt(iSumRepayActualAmt);
			uJcicZ4502.setSumRepayShouldAmt(iSumRepayShouldAmt);
			uJcicZ4502.setPayStatus(iPayStatus);
			uJcicZ4502.setTranKey(iTranKey);
			uJcicZ4502.setOutJcicTxtDate(0);

			Slice<JcicZ450Log> dJcicLogZ450 = null;
			dJcicLogZ450 = sJcicZ450LogService.ukeyEq(iJcicZ450.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ450 == null || ("A".equals(iTranKey) && dJcicLogZ450 == null)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ450Service.delete(iJcicZ450, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ450Log iJcicZ450Log = dJcicLogZ450.getContent().get(0);
				iJcicZ450.setPayAmt(iJcicZ450Log.getPayAmt());
				iJcicZ450.setSumRepayActualAmt(iJcicZ450Log.getSumRepayActualAmt());
				iJcicZ450.setSumRepayShouldAmt(iJcicZ450Log.getSumRepayShouldAmt());
				iJcicZ450.setPayStatus(iJcicZ450Log.getPayStatus());
				iJcicZ450.setTranKey(iJcicZ450Log.getTranKey());
				iJcicZ450.setOutJcicTxtDate(iJcicZ450Log.getOutJcicTxtDate());
				try {
					sJcicZ450Service.update(iJcicZ450, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ4502, uJcicZ4502);
			iDataLog.exec("L8329刪除", uJcicZ4502.getSubmitKey() + uJcicZ4502.getCustId() + uJcicZ4502.getApplyDate() + uJcicZ4502.getCourtCode() + uJcicZ4502.getPayDate());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ450 = sJcicZ450Service.ukeyFirst(iKey, titaVo);
			JcicZ450 uJcicZ4503 = new JcicZ450();
			uJcicZ4503 = sJcicZ450Service.holdById(iJcicZ450.getJcicZ450Id(), titaVo);
			if (uJcicZ4503 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ450.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ450 oldJcicZ4503 = (JcicZ450) iDataLog.clone(uJcicZ4503);
			uJcicZ4503.setJcicZ450Id(iJcicZ450Id);
			uJcicZ4503.setTranKey(iTranKey);
			uJcicZ4503.setPayAmt(iPayAmt);
			uJcicZ4503.setSumRepayActualAmt(iSumRepayActualAmt);
			uJcicZ4503.setSumRepayShouldAmt(iSumRepayShouldAmt);
			uJcicZ4503.setPayStatus(iPayStatus);
			uJcicZ4503.setUkey(iKey);

			try {
				sJcicZ450Service.update(uJcicZ4503, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ4503, uJcicZ4503);
			iDataLog.exec("L8329修改", uJcicZ4503.getSubmitKey() + uJcicZ4503.getCustId() + uJcicZ4503.getApplyDate() + uJcicZ4503.getCourtCode() + uJcicZ4503.getPayDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}