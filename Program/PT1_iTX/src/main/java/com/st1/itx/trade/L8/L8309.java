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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ048LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ048Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8309")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8309 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ048LogService sJcicZ048LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8309 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iCustRegAddr = titaVo.getParam("CustRegAddr").trim();
		String iCustComAddr = titaVo.getParam("CustComAddr").trim();
		String iCustRegTelNo = titaVo.getParam("CustRegTelNo").trim();
		String iCustComTelNo = titaVo.getParam("CustComTelNo").trim();
		String iCustMobilNo = titaVo.getParam("CustMobilNo").trim();

		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ048
		JcicZ048 iJcicZ048 = new JcicZ048();
		JcicZ048Id iJcicZ048Id = new JcicZ048Id();
		iJcicZ048Id.setCustId(iCustId);// 債務人IDN
		iJcicZ048Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ048Id.setRcDate(iRcDate);
		JcicZ048 chJcicZ048 = new JcicZ048();

		// 無檢核項目 removed by Fegie 2021/10/25

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ048
			chJcicZ048 = sJcicZ048Service.findById(iJcicZ048Id, titaVo);
			if (chJcicZ048 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ048.setJcicZ048Id(iJcicZ048Id);
			iJcicZ048.setTranKey(iTranKey);
			iJcicZ048.setCustRegAddr(iCustRegAddr);
			iJcicZ048.setCustComAddr(iCustComAddr);
			iJcicZ048.setCustRegTelNo(iCustRegTelNo);
			iJcicZ048.setCustComTelNo(iCustComTelNo);
			iJcicZ048.setCustMobilNo(iCustMobilNo);
			iJcicZ048.setUkey(iKey);
			try {
				sJcicZ048Service.insert(iJcicZ048, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ048 = sJcicZ048Service.ukeyFirst(iKey, titaVo);
			JcicZ048 uJcicZ048 = new JcicZ048();
			uJcicZ048 = sJcicZ048Service.holdById(iJcicZ048.getJcicZ048Id(), titaVo);
			if (uJcicZ048 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ048 oldJcicZ048 = (JcicZ048) iDataLog.clone(uJcicZ048);
			uJcicZ048.setTranKey(iTranKey);
			uJcicZ048.setCustRegAddr(iCustRegAddr);
			uJcicZ048.setCustComAddr(iCustComAddr);
			uJcicZ048.setCustRegTelNo(iCustRegTelNo);
			uJcicZ048.setCustComTelNo(iCustComTelNo);
			uJcicZ048.setCustMobilNo(iCustMobilNo);
			uJcicZ048.setOutJcicTxtDate(0);
			
			uJcicZ048.setActualFilingDate(0);
			uJcicZ048.setActualFilingMark("");
			
			try {
				sJcicZ048Service.update(uJcicZ048, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ048, uJcicZ048);
			iDataLog.exec("L8309異動", uJcicZ048.getSubmitKey() + uJcicZ048.getCustId() + uJcicZ048.getRcDate());
			break;
		// 2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ048 = sJcicZ048Service.ukeyFirst(iKey, titaVo);
			JcicZ048 uJcicZ0482 = new JcicZ048();
			uJcicZ0482 = sJcicZ048Service.holdById(iJcicZ048.getJcicZ048Id(), titaVo);
			iJcicZ048 = sJcicZ048Service.findById(iJcicZ048Id);
			if (iJcicZ048 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ048 oldJcicZ0482 = (JcicZ048) iDataLog.clone(uJcicZ0482);
			uJcicZ0482.setTranKey(iTranKey);
			uJcicZ0482.setCustRegAddr(iCustRegAddr);
			uJcicZ0482.setCustComAddr(iCustComAddr);
			uJcicZ0482.setCustRegTelNo(iCustRegTelNo);
			uJcicZ0482.setCustComTelNo(iCustComTelNo);
			uJcicZ0482.setCustMobilNo(iCustMobilNo);
			uJcicZ0482.setOutJcicTxtDate(0);

			Slice<JcicZ048Log> dJcicLogZ048 = null;
			dJcicLogZ048 = sJcicZ048LogService.ukeyEq(iJcicZ048.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ048 == null || ("A".equals(iTranKey) && dJcicLogZ048 == null)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ048Service.delete(iJcicZ048, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ048Log iJcicZ048Log = dJcicLogZ048.getContent().get(0);
				iJcicZ048.setCustRegAddr(iJcicZ048Log.getCustRegAddr());
				iJcicZ048.setCustComAddr(iJcicZ048Log.getCustComAddr());
				iJcicZ048.setCustRegTelNo(iJcicZ048Log.getCustRegTelNo());
				iJcicZ048.setCustComTelNo(iJcicZ048Log.getCustComTelNo());
				iJcicZ048.setCustMobilNo(iJcicZ048Log.getCustMobilNo());
				iJcicZ048.setTranKey(iJcicZ048Log.getTranKey());
				iJcicZ048.setOutJcicTxtDate(iJcicZ048Log.getOutJcicTxtDate());
				try {
					sJcicZ048Service.update(iJcicZ048, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0482, uJcicZ0482);
			iDataLog.exec("L8309刪除", uJcicZ0482.getSubmitKey() + uJcicZ0482.getCustId() + uJcicZ0482.getRcDate());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ048 = sJcicZ048Service.ukeyFirst(iKey, titaVo);
			JcicZ048 uJcicZ0483 = new JcicZ048();
			uJcicZ0483 = sJcicZ048Service.holdById(iJcicZ048.getJcicZ048Id(), titaVo);
			if (uJcicZ0483 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ048.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ048 oldJcicZ0483 = (JcicZ048) iDataLog.clone(uJcicZ0483);
			uJcicZ0483.setJcicZ048Id(iJcicZ048Id);
			uJcicZ0483.setTranKey(iTranKey);
			uJcicZ0483.setCustRegAddr(iCustRegAddr);
			uJcicZ0483.setCustComAddr(iCustComAddr);
			uJcicZ0483.setCustRegTelNo(iCustRegTelNo);
			uJcicZ0483.setCustComTelNo(iCustComTelNo);
			uJcicZ0483.setCustMobilNo(iCustMobilNo);
			uJcicZ0483.setUkey(iKey);

			try {
				sJcicZ048Service.update(uJcicZ0483, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0483, uJcicZ0483);
			iDataLog.exec("L8309修改", uJcicZ0483.getSubmitKey() + uJcicZ0483.getCustId() + uJcicZ0483.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
