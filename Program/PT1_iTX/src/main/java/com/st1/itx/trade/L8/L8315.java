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

/* DB容器 */
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ054Id;
import com.st1.itx.db.domain.JcicZ054Log;
import com.st1.itx.db.service.JcicZ054LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ054Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8315")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8315 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ054Service sJcicZ054Service;
	@Autowired
	public JcicZ054LogService sJcicZ054LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8315 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iPayOffResult = titaVo.getParam("PayOffResult").trim();
		int iPayOffDate = Integer.valueOf(titaVo.getParam("PayOffDate").trim());
		String iKey = "";
		// JcicZ054
		JcicZ054 iJcicZ054 = new JcicZ054();
		JcicZ054Id iJcicZ054Id = new JcicZ054Id();
		iJcicZ054Id.setCustId(iCustId);// 債務人IDN
		iJcicZ054Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ054Id.setRcDate(iRcDate);
		iJcicZ054Id.setMaxMainCode(iMaxMainCode);
		iJcicZ054Id.setPayOffDate(iPayOffDate);
		JcicZ054 chJcicZ054 = new JcicZ054();

		// 檢核項目(D-31)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2
			// KEY值(IDN+報送單位代號+協商申請日+最大債權金融機構代號)未曾報送過'47':金融機構無擔保債務協議資料，予以剔退處理.***1014會議(不做檢核)
			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ054
			chJcicZ054 = sJcicZ054Service.findById(iJcicZ054Id, titaVo);
			if (chJcicZ054 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ054.setJcicZ054Id(iJcicZ054Id);
			iJcicZ054.setTranKey(iTranKey);
			iJcicZ054.setPayOffResult(iPayOffResult);
			iJcicZ054.setUkey(iKey);
			try {
				sJcicZ054Service.insert(iJcicZ054, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ054 = sJcicZ054Service.ukeyFirst(iKey, titaVo);
			JcicZ054 uJcicZ054 = new JcicZ054();
			uJcicZ054 = sJcicZ054Service.holdById(iJcicZ054.getJcicZ054Id(), titaVo);
			if (uJcicZ054 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ054 oldJcicZ054 = (JcicZ054) iDataLog.clone(uJcicZ054);
			uJcicZ054.setTranKey(iTranKey);
			uJcicZ054.setPayOffResult(iPayOffResult);
			uJcicZ054.setOutJcicTxtDate(0);
			try {
				sJcicZ054Service.update(uJcicZ054, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ054, uJcicZ054);
			iDataLog.exec("L8315異動", uJcicZ054.getSubmitKey()+uJcicZ054.getCustId()+uJcicZ054.getRcDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ054 = sJcicZ054Service.ukeyFirst(iKey, titaVo);
			JcicZ054 uJcicZ0542 = new JcicZ054();
			uJcicZ0542 = sJcicZ054Service.holdById(iJcicZ054.getJcicZ054Id(), titaVo);
			iJcicZ054 = sJcicZ054Service.findById(iJcicZ054Id);
			if (iJcicZ054 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ054 oldJcicZ0542 = (JcicZ054) iDataLog.clone(uJcicZ0542);
			uJcicZ0542.setTranKey(iTranKey);
			uJcicZ0542.setPayOffResult(iPayOffResult);
			uJcicZ0542.setOutJcicTxtDate(0);
			
			Slice<JcicZ054Log> dJcicLogZ054 = null;
			dJcicLogZ054 = sJcicZ054LogService.ukeyEq(iJcicZ054.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ054 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ054Service.delete(iJcicZ054, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ054Log iJcicZ054Log = dJcicLogZ054.getContent().get(0);
				iJcicZ054.setPayOffResult(iJcicZ054Log.getPayOffResult());
				iJcicZ054.setTranKey(iJcicZ054Log.getTranKey());
				iJcicZ054.setOutJcicTxtDate(iJcicZ054Log.getOutJcicTxtDate());
				try {
					sJcicZ054Service.update(iJcicZ054, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0542, uJcicZ0542);
			iDataLog.exec("L8315刪除", uJcicZ0542.getSubmitKey()+uJcicZ0542.getCustId()+uJcicZ0542.getRcDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
