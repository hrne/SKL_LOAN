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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
import com.st1.itx.db.domain.JcicZ052Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.service.JcicZ052LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ052Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8313")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8313 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ052LogService sJcicZ052LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8313 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iBankCode1 = titaVo.getParam("BankCode1").trim();
		String iDataCode1 = titaVo.getParam("DataCode1").trim();
		String iBankCode2 = titaVo.getParam("BankCode2").trim();
		String iDataCode2 = titaVo.getParam("DataCode2").trim();
		String iBankCode3 = titaVo.getParam("BankCode3").trim();
		String iDataCode3 = titaVo.getParam("DataCode3").trim();
		String iBankCode4 = titaVo.getParam("BankCode4").trim();
		String iDataCode4 = titaVo.getParam("DataCode4").trim();
		String iBankCode5 = titaVo.getParam("BankCode5").trim();
		String iDataCode5 = titaVo.getParam("DataCode5").trim();
		int iChangePayDate = Integer.valueOf(titaVo.getParam("ChangePayDate"));
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ052, JcicZ040
		JcicZ052 iJcicZ052 = new JcicZ052();
		JcicZ052Id iJcicZ052Id = new JcicZ052Id();
		iJcicZ052Id.setCustId(iCustId);// 債務人IDN
		iJcicZ052Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ052Id.setRcDate(iRcDate);
		JcicZ052 chJcicZ052 = new JcicZ052();
		JcicZ040 iJcicZ040 = new JcicZ040();
		JcicZ040Id iJcicZ040Id = new JcicZ040Id();
		iJcicZ040Id.setCustId(iCustId);// 債務人IDN
		iJcicZ040Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ040Id.setRcDate(iRcDate);

		// 檢核項目(D-28)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 start 完整key值未曾報送過'40':前置協商受理申請暨請求回報債權通知則予以剔退
			iJcicZ040 = sJcicZ040Service.findById(iJcicZ040Id, titaVo);
			if (iJcicZ040 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				} else {
					throw new LogicException("E0007", "未曾報送過(40)前置協商受理申請暨請求回報債權通知資料.");
				}

			} // 2 end

			// 3 後續需檢核補報送金融機構需於最大債權金融機構報送本檔案格式後3個營業日內補送資料，予以剔退處理.***J

			// 4 第8,10,12,14,16欄「請求補報送資料檔案格式之資料別」僅限於"42", "43", "61"，其餘予以剔退處理.--->(前端檢核)

			// 檢核項目end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ052
			chJcicZ052 = sJcicZ052Service.findById(iJcicZ052Id, titaVo);
			if (chJcicZ052 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ052.setJcicZ052Id(iJcicZ052Id);
			iJcicZ052.setTranKey(iTranKey);
			iJcicZ052.setBankCode1(iBankCode1);
			iJcicZ052.setDataCode1(iDataCode1);
			iJcicZ052.setBankCode2(iBankCode2);
			iJcicZ052.setDataCode2(iDataCode2);
			iJcicZ052.setBankCode3(iBankCode3);
			iJcicZ052.setDataCode3(iDataCode3);
			iJcicZ052.setBankCode4(iBankCode4);
			iJcicZ052.setDataCode4(iDataCode4);
			iJcicZ052.setBankCode5(iBankCode5);
			iJcicZ052.setDataCode5(iDataCode5);
			iJcicZ052.setChangePayDate(iChangePayDate);
			iJcicZ052.setUkey(iKey);
			

			
			try {
				sJcicZ052Service.insert(iJcicZ052, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ052 = sJcicZ052Service.ukeyFirst(iKey, titaVo);
			JcicZ052 uJcicZ052 = new JcicZ052();
			uJcicZ052 = sJcicZ052Service.holdById(iJcicZ052.getJcicZ052Id(), titaVo);
			if (uJcicZ052 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ052 oldJcicZ052 = (JcicZ052) iDataLog.clone(uJcicZ052);
			uJcicZ052.setTranKey(iTranKey);
			uJcicZ052.setBankCode1(iBankCode1);
			uJcicZ052.setDataCode1(iDataCode1);
			uJcicZ052.setBankCode2(iBankCode2);
			uJcicZ052.setDataCode2(iDataCode2);
			uJcicZ052.setBankCode3(iBankCode3);
			uJcicZ052.setDataCode3(iDataCode3);
			uJcicZ052.setBankCode4(iBankCode4);
			uJcicZ052.setDataCode4(iDataCode4);
			uJcicZ052.setBankCode5(iBankCode5);
			uJcicZ052.setDataCode5(iDataCode5);
			uJcicZ052.setChangePayDate(iChangePayDate);
			uJcicZ052.setOutJcicTxtDate(0);
			
			uJcicZ052.setActualFilingDate(0);
			uJcicZ052.setActualFilingMark("");
			
			try {
				sJcicZ052Service.update(uJcicZ052, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ052, uJcicZ052);
			iDataLog.exec("L8313異動", uJcicZ052.getSubmitKey() + uJcicZ052.getCustId() + uJcicZ052.getRcDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ052 = sJcicZ052Service.ukeyFirst(iKey, titaVo);
			JcicZ052 uJcicZ0522 = new JcicZ052();
			uJcicZ0522 = sJcicZ052Service.holdById(iJcicZ052.getJcicZ052Id(), titaVo);
			iJcicZ052 = sJcicZ052Service.findById(iJcicZ052Id);
			if (iJcicZ052 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ052 oldJcicZ0522 = (JcicZ052) iDataLog.clone(uJcicZ0522);
			uJcicZ0522.setTranKey(iTranKey);
			uJcicZ0522.setBankCode1(iBankCode1);
			uJcicZ0522.setDataCode1(iDataCode1);
			uJcicZ0522.setBankCode2(iBankCode2);
			uJcicZ0522.setDataCode2(iDataCode2);
			uJcicZ0522.setBankCode3(iBankCode3);
			uJcicZ0522.setDataCode3(iDataCode3);
			uJcicZ0522.setBankCode4(iBankCode4);
			uJcicZ0522.setDataCode4(iDataCode4);
			uJcicZ0522.setBankCode5(iBankCode5);
			uJcicZ0522.setDataCode5(iDataCode5);
			uJcicZ0522.setChangePayDate(iChangePayDate);
			uJcicZ0522.setOutJcicTxtDate(0);

			Slice<JcicZ052Log> dJcicLogZ052 = null;
			dJcicLogZ052 = sJcicZ052LogService.ukeyEq(iJcicZ052.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ052 == null || ("A".equals(iTranKey) && dJcicLogZ052 == null)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ052Service.delete(iJcicZ052, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ052Log iJcicZ052Log = dJcicLogZ052.getContent().get(0);
				iJcicZ052.setBankCode1(iJcicZ052Log.getBankCode1());
				iJcicZ052.setDataCode1(iJcicZ052Log.getDataCode1());
				iJcicZ052.setBankCode2(iJcicZ052Log.getBankCode2());
				iJcicZ052.setDataCode2(iJcicZ052Log.getDataCode2());
				iJcicZ052.setBankCode3(iJcicZ052Log.getBankCode3());
				iJcicZ052.setDataCode3(iJcicZ052Log.getDataCode3());
				iJcicZ052.setBankCode4(iJcicZ052Log.getBankCode4());
				iJcicZ052.setDataCode4(iJcicZ052Log.getDataCode4());
				iJcicZ052.setBankCode5(iJcicZ052Log.getBankCode5());
				iJcicZ052.setDataCode5(iJcicZ052Log.getDataCode5());
				iJcicZ052.setChangePayDate(iJcicZ052Log.getChangePayDate());
				iJcicZ052.setTranKey(iJcicZ052Log.getTranKey());
				iJcicZ052.setOutJcicTxtDate(iJcicZ052Log.getOutJcicTxtDate());
				try {
					sJcicZ052Service.update(iJcicZ052, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0522, uJcicZ0522);
			iDataLog.exec("L8313刪除", uJcicZ0522.getSubmitKey() + uJcicZ0522.getCustId() + uJcicZ0522.getRcDate());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ052 = sJcicZ052Service.ukeyFirst(iKey, titaVo);
			JcicZ052 uJcicZ0523 = new JcicZ052();
			uJcicZ0523 = sJcicZ052Service.holdById(iJcicZ052.getJcicZ052Id(), titaVo);
			if (uJcicZ0523 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ052.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ052 oldJcicZ0523 = (JcicZ052) iDataLog.clone(uJcicZ0523);
			uJcicZ0523.setJcicZ052Id(iJcicZ052Id);
			uJcicZ0523.setTranKey(iTranKey);
			uJcicZ0523.setBankCode1(iBankCode1);
			uJcicZ0523.setDataCode1(iDataCode1);
			uJcicZ0523.setBankCode2(iBankCode2);
			uJcicZ0523.setDataCode2(iDataCode2);
			uJcicZ0523.setBankCode3(iBankCode3);
			uJcicZ0523.setDataCode3(iDataCode3);
			uJcicZ0523.setBankCode4(iBankCode4);
			uJcicZ0523.setDataCode4(iDataCode4);
			uJcicZ0523.setBankCode5(iBankCode5);
			uJcicZ0523.setDataCode5(iDataCode5);
			uJcicZ0523.setChangePayDate(iChangePayDate);
			uJcicZ0523.setUkey(iKey);

			try {
				sJcicZ052Service.update(uJcicZ0523, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ0523, uJcicZ0523);
			iDataLog.exec("L8313修改", uJcicZ0523.getSubmitKey() + uJcicZ0523.getCustId() + uJcicZ0523.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
