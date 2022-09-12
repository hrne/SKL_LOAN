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

/* DB容器 */
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ454Id;
import com.st1.itx.db.domain.JcicZ454Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ454LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ454Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8331")
@Scope("prototype")
/**
 * @author Luisito
 * @version 1.0.0
 */
public class L8331 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ454Service sJcicZ454Service;
	@Autowired
	public JcicZ454LogService sJcicZ454LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8331 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iPayOffResult = titaVo.getParam("PayOffResult").trim();
		int iPayOffDate = Integer.valueOf(titaVo.getParam("PayOffDate").trim());
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ454
		JcicZ454 iJcicZ454 = new JcicZ454();
		JcicZ454Id iJcicZ454Id = new JcicZ454Id();
		iJcicZ454Id.setApplyDate(iApplyDate);
		iJcicZ454Id.setCourtCode(iCourtCode);
		iJcicZ454Id.setCustId(iCustId);
		iJcicZ454Id.setSubmitKey(iSubmitKey);
		iJcicZ454Id.setMaxMainCode(iMaxMainCode);
		JcicZ454 chJcicZ454 = new JcicZ454();
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setSubmitKey(iSubmitKey);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);

		// 檢核項目(D-62)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2
			// 需檢核「IDN+報送單位代號+調解申請日+受理調解機構代號+最大債權金融機構」是否存在「'448'」前置調解無擔保債務還款分配表資料--->1014會議通知不需檢核
			// 3 「單獨全數受清償日期」不得大於「資料報送日期」--->前端檢核

			// 4 start 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除本檔案資料.");
				}
			} // 4 end

		}
		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ454
			chJcicZ454 = sJcicZ454Service.findById(iJcicZ454Id, titaVo);
			if (chJcicZ454 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ454.setJcicZ454Id(iJcicZ454Id);
			iJcicZ454.setTranKey(iTranKey);
			iJcicZ454.setPayOffResult(iPayOffResult);
			iJcicZ454.setPayOffDate(iPayOffDate);
			iJcicZ454.setUkey(iKey);
			try {
				sJcicZ454Service.insert(iJcicZ454, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ454 = sJcicZ454Service.ukeyFirst(iKey, titaVo);
			JcicZ454 uJcicZ454 = new JcicZ454();
			uJcicZ454 = sJcicZ454Service.holdById(iJcicZ454.getJcicZ454Id(), titaVo);
			if (uJcicZ454 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ454 oldJcicZ454 = (JcicZ454) iDataLog.clone(uJcicZ454);
			uJcicZ454.setPayOffResult(iPayOffResult);
			uJcicZ454.setPayOffDate(iPayOffDate);
			uJcicZ454.setTranKey(iTranKey);
			uJcicZ454.setOutJcicTxtDate(0);
			
			uJcicZ454.setActualFilingDate(0);
			uJcicZ454.setActualFilingMark("");
			
			try {
				sJcicZ454Service.update(uJcicZ454, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ454, uJcicZ454);
			iDataLog.exec("L8331異動", uJcicZ454.getSubmitKey() + uJcicZ454.getCustId() + uJcicZ454.getApplyDate()
					+ uJcicZ454.getCourtCode() + uJcicZ454.getMaxMainCode());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ454 = sJcicZ454Service.ukeyFirst(iKey, titaVo);
			JcicZ454 uJcicZ4542 = new JcicZ454();
			uJcicZ4542 = sJcicZ454Service.holdById(iJcicZ454.getJcicZ454Id(), titaVo);
			iJcicZ454 = sJcicZ454Service.findById(iJcicZ454Id);
			if (iJcicZ454 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ454 oldJcicZ4542 = (JcicZ454) iDataLog.clone(uJcicZ4542);
			uJcicZ4542.setPayOffResult(iPayOffResult);
			uJcicZ4542.setPayOffDate(iPayOffDate);
			uJcicZ4542.setTranKey(iTranKey);
			uJcicZ4542.setOutJcicTxtDate(0);

			Slice<JcicZ454Log> dJcicLogZ454 = null;
			dJcicLogZ454 = sJcicZ454LogService.ukeyEq(iJcicZ454.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ454 == null || "A".equals(iTranKey)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ454Service.delete(iJcicZ454, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ454Log iJcicZ454Log = dJcicLogZ454.getContent().get(0);
				iJcicZ454.setPayOffResult(iJcicZ454Log.getPayOffResult());
				iJcicZ454.setPayOffDate(iJcicZ454Log.getPayOffDate());
				iJcicZ454.setTranKey(iJcicZ454Log.getTranKey());
				iJcicZ454.setOutJcicTxtDate(iJcicZ454Log.getOutJcicTxtDate());
				try {
					sJcicZ454Service.update(iJcicZ454, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}

			iDataLog.setEnv(titaVo, oldJcicZ4542, uJcicZ4542);
			iDataLog.exec("L8331刪除", uJcicZ4542.getSubmitKey() + uJcicZ4542.getCustId() + uJcicZ4542.getApplyDate()
					+ uJcicZ4542.getCourtCode() + uJcicZ4542.getMaxMainCode());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ454 = sJcicZ454Service.ukeyFirst(iKey, titaVo);
			JcicZ454 uJcicZ4543 = new JcicZ454();
			uJcicZ4543 = sJcicZ454Service.holdById(iJcicZ454.getJcicZ454Id(), titaVo);
			if (uJcicZ4543 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ454.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ454 oldJcicZ4543 = (JcicZ454) iDataLog.clone(uJcicZ4543);
			uJcicZ4543.setJcicZ454Id(iJcicZ454Id);
			uJcicZ4543.setTranKey(iTranKey);
			uJcicZ4543.setPayOffResult(iPayOffResult);
			uJcicZ4543.setPayOffDate(iPayOffDate);
			uJcicZ4543.setUkey(iKey);

			try {
				sJcicZ454Service.update(uJcicZ4543, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ4543, uJcicZ4543);
			iDataLog.exec("L8331異動", uJcicZ4543.getSubmitKey() + uJcicZ4543.getCustId() + uJcicZ4543.getApplyDate()
					+ uJcicZ4543.getCourtCode() + uJcicZ4543.getMaxMainCode());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}