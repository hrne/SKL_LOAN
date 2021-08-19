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

/* DB容器 */
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
import com.st1.itx.db.domain.JcicZ440Log;
import com.st1.itx.db.service.JcicZ440LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ440Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8322")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8322 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ440LogService sJcicZ440LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

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
		// JcicZ440
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setSubmitKey(iSubmitKey);
		JcicZ440 chJcicZ440 = new JcicZ440();

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
			JcicZ440 oldJcicZ440 = (JcicZ440) iDataLog.clone(uJcicZ440);
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
}