package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ040Log;

/*DB服務*/
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.springjpa.cm.L6932ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L8301ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8301")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8301 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public L8301ServiceImpl sL8301ServiceImpl;
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ040LogService sJcicZ040LogService;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public TxDataLogService sTxDataLogService;
	@Autowired
	public L6932ServiceImpl sL6932Service;

	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8301 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		int iRbDate = Integer.valueOf(titaVo.getParam("RbDate").trim());
		String iApplyType = titaVo.getParam("ApplyType").trim();
		String iRefBankId = titaVo.getParam("RefBankId").trim();
		String iNotBankId1 = titaVo.getParam("NotBankId1").trim();
		String iNotBankId2 = titaVo.getParam("NotBankId2").trim();
		String iNotBankId3 = titaVo.getParam("NotBankId3").trim();
		String iNotBankId4 = titaVo.getParam("NotBankId4").trim();
		String iNotBankId5 = titaVo.getParam("NotBankId5").trim();
		String iNotBankId6 = titaVo.getParam("NotBankId6").trim();
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		String[] sNotBankId = { iNotBankId1, iNotBankId2, iNotBankId3, iNotBankId4, iNotBankId5, iNotBankId6 }; // 未揭露債權機構代號
		List<String> iL8301SqlReturn = new ArrayList<>(); // NegFinAcct有效消債條例金融機構代號集合

		// JcicZ040, JcicZ046, NegFinAcct
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);
		JcicZ040 chJcicZ040 = new JcicZ040();

		// 檢核項目(D-3)
		if (!"4".equals(iTranKey_Tmp)) {
			// 1.3 start若交易代碼報送C異動，於進檔時檢查並無此筆資料，視為新增A，不予剔退
			if ("C".equals(iTranKey)) {
				JcicZ040 jJcicZ040 = sJcicZ040Service.ukeyFirst(titaVo.getParam("Ukey"), titaVo);
				if (jJcicZ040 == null) {
					iTranKey_Tmp = "1";
					iTranKey = "A";
				}
			}
			// 1.3 end

			// 1.4 檢核該債務人IDN需於45日內申請過債權人清冊***J

			// 1.5 檢核其第7欄止息基準日需等於協商申請日+25日(前端已檢核)

			// 1.6 檢核該債務人IDN報送本通知資料之記錄.已存在尚未報送結案-剔退***J
			// 1.6 結案原因為協商不成立或毀諾案件者，不能再度申請前置協商,結案原因為視同未請求協商案件，結案未滿180天-剔退.***J

			// 1.7 結案已經滿180天，再度申請前置協商，原結案資料不可再報送異動或刪除-->在L8030控制('46'結案通知資料表)***J

			// 1.8 債務人IDN是否於債權人清冊註明：曾參與銀行公會債務協商，且目前狀態為履約中或毀諾者-有則剔退***J

			// 1.9 檢核中心建檔日期，若大於協商申請日(iRcDate)+2個營業日則剔退***J

			// 1.10
			// start檢核債權機構代號，是否屬於有效消債條例金融機構代號--不是則剔退(NegFinAcct「債務協商債權機構帳戶檔」FinCode)
			if ("A".equals(iTranKey) || "C".equals(iTranKey)) {
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
							flagFind = 0;
							for (String xL8301SqlReturn : iL8301SqlReturn) {
								if (xNotBankId.equals(xL8301SqlReturn)) {
									flagFind = 1;
									break;
								}
							}
							if (flagFind == 0) {
								if ("A".equals(iTranKey)) {
									throw new LogicException(titaVo, "E0005",
											"債權機構代號" + xNotBankId + "不屬於有效消債條例金融機構代號");
								} else {
									throw new LogicException(titaVo, "E0007",
											"債權機構代號" + xNotBankId + "不屬於有效消債條例金融機構代號");
								}
							}
						}
					}
				}
			}
			// 1.10 end

			// 2.本中心於債務人提出協商申請日後第22日截止時，若尚未接獲各債權金融機構回報債權金額資料，本中心即將此部分揭露於Z99前前置協商相關作業提醒資訊.***J

			// 檢核項目end
		}

		switch (iTranKey_Tmp)

		{
		case "1":
			// 檢核是否重複，並寫入JcicZ040
			chJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
			if (chJcicZ040 != null) {
				throw new LogicException("E0002", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ040.setJcicZ040Id(iJcicZ040Id);
			iJcicZ040.setTranKey(iTranKey);
			iJcicZ040.setRbDate(iRbDate);
			iJcicZ040.setApplyType(iApplyType);
			iJcicZ040.setRefBankId(iRefBankId);
			iJcicZ040.setNotBankId1(iNotBankId1);
			iJcicZ040.setNotBankId2(iNotBankId2);
			iJcicZ040.setNotBankId3(iNotBankId3);
			iJcicZ040.setNotBankId4(iNotBankId4);
			iJcicZ040.setNotBankId5(iNotBankId5);
			iJcicZ040.setNotBankId6(iNotBankId6);
			iJcicZ040.setUkey(iKey);
			try {
				sJcicZ040Service.insert(iJcicZ040, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "前置協商受理申請暨請求回報債權通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ040 = sJcicZ040Service.ukeyFirst(iKey, titaVo);
			JcicZ040 uJcicZ040 = new JcicZ040();
			uJcicZ040 = sJcicZ040Service.holdById(iJcicZ040.getJcicZ040Id(), titaVo);
			if (uJcicZ040 == null) {
				throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate = iJcicZ040.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate);
			if (JcicDate == 0) {
				throw new LogicException("E0007", "無此更新資料");
			}

			JcicZ040 oldJcicZ040 = (JcicZ040) iDataLog.clone(uJcicZ040);
			uJcicZ040.setTranKey(iTranKey);
			uJcicZ040.setRbDate(iRbDate);
			uJcicZ040.setApplyType(iApplyType);
			uJcicZ040.setRefBankId(iRefBankId);
			uJcicZ040.setNotBankId1(iNotBankId1);
			uJcicZ040.setNotBankId2(iNotBankId2);
			uJcicZ040.setNotBankId3(iNotBankId3);
			uJcicZ040.setNotBankId4(iNotBankId4);
			uJcicZ040.setNotBankId5(iNotBankId5);
			uJcicZ040.setNotBankId6(iNotBankId6);
			uJcicZ040.setOutJcicTxtDate(0);
			try {
				sJcicZ040Service.update(uJcicZ040, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "前置協商受理申請暨請求回報債權通知資料");
			}
//			註解為給序號
//			List<Map<String,String>> list = sL6932Service.findDataLog(uJcicZ040.getSubmitKey()+uJcicZ040.getCustId()+uJcicZ040.getRcDate(),titaVo);
//			int number =list.size();
//			this.info("number" + number);
//			iDataLog.exec("L8301異動", uJcicZ040.getSubmitKey()+uJcicZ040.getCustId()+uJcicZ040.getRcDate()+number);
			this.info("進入6932 ================ L8301");
			this.info("UKey    ===== " + uJcicZ040.getUkey());

			iDataLog.setEnv(titaVo, oldJcicZ040, uJcicZ040);
			iDataLog.exec("L8301異動", uJcicZ040.getSubmitKey() + uJcicZ040.getCustId() + uJcicZ040.getRcDate());
			break;
		// 2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ040 = sJcicZ040Service.ukeyFirst(iKey, titaVo);
			JcicZ040 uJcicZ0402 = new JcicZ040();
			uJcicZ0402 = sJcicZ040Service.holdById(iJcicZ040.getJcicZ040Id(), titaVo);
			iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id);
			if (iJcicZ040 == null) {
				throw new LogicException("E0004", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate2 = iJcicZ040.getOutJcicTxtDate();
			this.info("JcicDate2    = " + JcicDate2);
			if (JcicDate2 != 0) {
				throw new LogicException("E0004", "刪除資料不存在");
			}
			JcicZ040 oldJcicZ0402 = (JcicZ040) iDataLog.clone(uJcicZ0402);
			uJcicZ0402.setTranKey(iTranKey);
			uJcicZ0402.setRbDate(iRbDate);
			uJcicZ0402.setApplyType(iApplyType);
			uJcicZ0402.setRefBankId(iRefBankId);
			uJcicZ0402.setNotBankId1(iNotBankId1);
			uJcicZ0402.setNotBankId2(iNotBankId2);
			uJcicZ0402.setNotBankId3(iNotBankId3);
			uJcicZ0402.setNotBankId4(iNotBankId4);
			uJcicZ0402.setNotBankId5(iNotBankId5);
			uJcicZ0402.setNotBankId6(iNotBankId6);
			uJcicZ0402.setOutJcicTxtDate(0);

			Slice<JcicZ040Log> dJcicLogZ040 = null;
			dJcicLogZ040 = sJcicZ040LogService.ukeyEq(iJcicZ040.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ040 == null || ("A".equals(iTranKey) && dJcicLogZ040 == null) ) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ040Service.delete(iJcicZ040, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ040Log iJcicZ040Log = dJcicLogZ040.getContent().get(0);
				iJcicZ040.setApplyType(iJcicZ040Log.getApplyType());
				iJcicZ040.setRefBankId(iJcicZ040Log.getRefBankId());
				iJcicZ040.setNotBankId1(iJcicZ040Log.getNotBankId1());
				iJcicZ040.setNotBankId2(iJcicZ040Log.getNotBankId2());
				iJcicZ040.setNotBankId3(iJcicZ040Log.getNotBankId3());
				iJcicZ040.setNotBankId4(iJcicZ040Log.getNotBankId4());
				iJcicZ040.setNotBankId5(iJcicZ040Log.getNotBankId5());
				iJcicZ040.setNotBankId6(iJcicZ040Log.getNotBankId6());
				iJcicZ040.setTranKey(iJcicZ040Log.getTranKey());
				iJcicZ040.setOutJcicTxtDate(iJcicZ040Log.getOutJcicTxtDate());
				try {
					sJcicZ040Service.update(iJcicZ040, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}

			iDataLog.setEnv(titaVo, oldJcicZ0402, uJcicZ0402);
			iDataLog.exec("L8301刪除", uJcicZ0402.getSubmitKey() + uJcicZ0402.getCustId() + uJcicZ0402.getRcDate());
//			List<Map<String,String>> list2 = sL6932Service.findDataLog(uJcicZ0402.getSubmitKey()+uJcicZ0402.getCustId()+uJcicZ0402.getRcDate(),titaVo);
//			int number2 =list2.size();
//			iDataLog.exec("L8301刪除", uJcicZ0402.getSubmitKey()+uJcicZ0402.getCustId()+uJcicZ0402.getRcDate()+number2);
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ040 = sJcicZ040Service.ukeyFirst(iKey, titaVo);
			JcicZ040 uJcicZ0403 = new JcicZ040();
			uJcicZ0403 = sJcicZ040Service.holdById(iJcicZ040.getJcicZ040Id(), titaVo);
			if (uJcicZ0403 == null) {
				throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ040.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}
			if ("R".equals(iTranKey)) {
				chJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
				if (chJcicZ040 != null) {
					throw new LogicException("E0002", "已有相同資料");
				}
			}

			JcicZ040 oldJcicZ0403 = (JcicZ040) iDataLog.clone(uJcicZ0403);
			uJcicZ0403.setJcicZ040Id(iJcicZ040Id);
			uJcicZ0403.setTranKey(iTranKey);
			uJcicZ0403.setRbDate(iRbDate);
			uJcicZ0403.setApplyType(iApplyType);
			uJcicZ0403.setRefBankId(iRefBankId);
			uJcicZ0403.setNotBankId1(iNotBankId1);
			uJcicZ0403.setNotBankId2(iNotBankId2);
			uJcicZ0403.setNotBankId3(iNotBankId3);
			uJcicZ0403.setNotBankId4(iNotBankId4);
			uJcicZ0403.setNotBankId5(iNotBankId5);
			uJcicZ0403.setNotBankId6(iNotBankId6);
			uJcicZ0403.setOutJcicTxtDate(0);
			try {
				sJcicZ040Service.update(uJcicZ0403, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "前置協商受理申請暨請求回報債權通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0403, uJcicZ0403);
			iDataLog.exec("L8301修改", uJcicZ0403.getSubmitKey() + uJcicZ0403.getCustId() + uJcicZ0403.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
