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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Id;
import com.st1.itx.db.domain.JcicZ444Log;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ444LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.db.service.JcicZ446Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;


@Service("L8325")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8325 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ444Service sJcicZ444Service;
	@Autowired
	public JcicZ444LogService sJcicZ444LogService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8325 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iCustRegAddr = titaVo.getParam("CustRegAddr");
		String iCustComAddr = titaVo.getParam("CustComAddr");
		String iCustRegTelNo = titaVo.getParam("CustRegTelNo");
		String iCustComTelNo = titaVo.getParam("CustComTelNo");
		String iCustMobilNo = titaVo.getParam("CustMobilNo");
		String iKey = "";
		// JcicZ444, JcicZ440, JcicZ446
		JcicZ444 iJcicZ444 = new JcicZ444();
		JcicZ444Id iJcicZ444Id = new JcicZ444Id();
		iJcicZ444Id.setSubmitKey(iSubmitKey);
		iJcicZ444Id.setCustId(iCustId);
		iJcicZ444Id.setApplyDate(iApplyDate);
		iJcicZ444Id.setCourtCode(iCourtCode);
		JcicZ444 chJcicZ444 = new JcicZ444();
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setSubmitKey(iSubmitKey);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-51)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2
			// 「IDN+報送單位代號+調解申請日+受理調解機構代號」若未曾報送過「'440':前置調解受理申請暨請求回報債權通知資料」，予以剔退處理.(交易代碼為'X'者不檢核)
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
				iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
				if (iJcicZ440 == null) {
					if ("A".equals(iTranKey)) {
						throw new LogicException("E0005", "請先報送(440)前置調解受理申請暨請求回報債權通知資料.");
					} else {
						throw new LogicException("E0007", "請先報送(440)前置調解受理申請暨請求回報債權通知資料.");
					}
				}
			} // 2 end

			// 3 第10欄、第11欄及第12欄「債務人電話」之其中一欄，需為必要填報項目.
			// removed by Fegie 2021/10/25

			// 4 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey) || "X".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				}
			} // 4 end
		}
		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ444 = sJcicZ444Service.findById(iJcicZ444Id, titaVo);
			this.info("TEST===" + chJcicZ444);
			if (chJcicZ444 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ444.setJcicZ444Id(iJcicZ444Id);
			iJcicZ444.setTranKey(iTranKey);
			iJcicZ444.setUkey(iKey);
			iJcicZ444.setCustRegAddr(iCustRegAddr);
			iJcicZ444.setCustComAddr(iCustComAddr);
			iJcicZ444.setCustRegTelNo(iCustRegTelNo);
			iJcicZ444.setCustComTelNo(iCustComTelNo);
			iJcicZ444.setCustMobilNo(iCustMobilNo);
			try {
				sJcicZ444Service.insert(iJcicZ444, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ444 = sJcicZ444Service.ukeyFirst(iKey, titaVo);
			JcicZ444 uJcicZ444 = new JcicZ444();
			uJcicZ444 = sJcicZ444Service.holdById(iJcicZ444.getJcicZ444Id(), titaVo);
			if (uJcicZ444 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ444 oldJcicZ444 = (JcicZ444) iDataLog.clone(uJcicZ444);
			uJcicZ444.setTranKey(iTranKey);
			uJcicZ444.setCustRegAddr(iCustRegAddr);
			uJcicZ444.setCustComAddr(iCustComAddr);
			uJcicZ444.setCustRegTelNo(iCustRegTelNo);
			uJcicZ444.setCustComTelNo(iCustComTelNo);
			uJcicZ444.setCustMobilNo(iCustMobilNo);
			uJcicZ444.setOutJcicTxtDate(0);
			try {
				sJcicZ444Service.update(uJcicZ444, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ444, uJcicZ444);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ444 = sJcicZ444Service.ukeyFirst(iKey, titaVo);
			JcicZ444 uJcicZ4442 = new JcicZ444();
			uJcicZ4442 = sJcicZ444Service.holdById(iJcicZ444.getJcicZ444Id(), titaVo);
			iJcicZ444 = sJcicZ444Service.findById(iJcicZ444Id);
			if (iJcicZ444 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ444 oldJcicZ4442 = (JcicZ444) iDataLog.clone(uJcicZ4442);
			uJcicZ4442.setTranKey(iTranKey);
			uJcicZ4442.setCustRegAddr(iCustRegAddr);
			uJcicZ4442.setCustComAddr(iCustComAddr);
			uJcicZ4442.setCustRegTelNo(iCustRegTelNo);
			uJcicZ4442.setCustComTelNo(iCustComTelNo);
			uJcicZ4442.setCustMobilNo(iCustMobilNo);
			uJcicZ4442.setOutJcicTxtDate(0);
			
			Slice<JcicZ444Log> dJcicLogZ444 = null;
			dJcicLogZ444 = sJcicZ444LogService.ukeyEq(iJcicZ444.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ444 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ444Service.delete(iJcicZ444, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ444Log iJcicZ444Log = dJcicLogZ444.getContent().get(0);
				iJcicZ444.setCustRegAddr(iJcicZ444Log.getCustRegAddr());
				iJcicZ444.setCustComAddr(iJcicZ444Log.getCustComAddr());
				iJcicZ444.setCustRegTelNo(iJcicZ444Log.getCustRegTelNo());
				iJcicZ444.setCustComTelNo(iJcicZ444Log.getCustComTelNo());
				iJcicZ444.setCustMobilNo(iJcicZ444Log.getCustMobilNo());
				iJcicZ444.setOutJcicTxtDate(iJcicZ444Log.getOutJcicTxtDate());
				try {
					sJcicZ444Service.update(iJcicZ444, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ4442, uJcicZ4442);
			iDataLog.exec();
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
