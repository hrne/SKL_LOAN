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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.domain.JcicZ045Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.service.JcicZ045LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ045Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8306")
@Scope("prototype")
/**
 * @author Mata
 * @version 1.0.0
 */
public class L8306 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	public JcicZ045LogService sJcicZ045LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8306 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String iCustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		int iRcDate = Integer.valueOf(titaVo.getParam("RcDate").trim());
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		String iAgreeCode = String.valueOf(titaVo.getParam("AgreeCode").trim());

		String iKey = "";
		
		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
		// JcicZ045
		JcicZ045 iJcicZ045 = new JcicZ045();
		JcicZ045Id iJcicZ045Id = new JcicZ045Id();
		iJcicZ045Id.setCustId(iCustId);// 債務人IDN
		iJcicZ045Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ045Id.setRcDate(iRcDate);
		iJcicZ045Id.setMaxMainCode(iMaxMainCode);
		JcicZ045 chJcicZ045 = new JcicZ045();
		JcicZ044 iJcicZ044 = new JcicZ044();
		JcicZ044Id iJcicZ044Id = new JcicZ044Id();
		iJcicZ044Id.setCustId(iCustId);// 債務人IDN
		iJcicZ044Id.setSubmitKey(iSubmitKey);// 報送單位代號
		iJcicZ044Id.setRcDate(iRcDate);

		// 檢核項目(D-14)
		if (!"4".equals(iTranKey_Tmp)) {
			// 2 start 完整key值未曾報送過'44':請求同意債務清償方案通知資料則予以剔退
			iJcicZ044 = sJcicZ044Service.findById(iJcicZ044Id, titaVo);
			if (iJcicZ044 == null) {
				if ("A".equals(iTranKey)) {
					throw new LogicException("E0005", "未曾報送過(44)請求同意債務清償方案通知資料.");
				} else {
					throw new LogicException("E0007", "未曾報送過(44)請求同意債務清償方案通知資料.");
				}
			}
			// 2 end

			// 3 若各金融機構超過3天尚未回報是否同意，將揭露於Z99前置協商相關作業提醒資訊之「逾3日尚未回報是否同意債務清停方案提醒通知」.***J

			// 4 若各金融機構超過最大債權金融機構請求同意債務清償方案通知日30天後始回報是否同意債務清償方案，則予以剔退處理.***J

			// 檢核項目end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ045
			chJcicZ045 = sJcicZ045Service.findById(iJcicZ045Id, titaVo);
			if (chJcicZ045 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ045.setJcicZ045Id(iJcicZ045Id);
			iJcicZ045.setTranKey(iTranKey);
			iJcicZ045.setAgreeCode(iAgreeCode);
			iJcicZ045.setUkey(iKey);
			try {
				sJcicZ045Service.insert(iJcicZ045, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ045 = sJcicZ045Service.ukeyFirst(iKey, titaVo);
			JcicZ045 uJcicZ045 = new JcicZ045();
			uJcicZ045 = sJcicZ045Service.holdById(iJcicZ045.getJcicZ045Id(), titaVo);
			if (uJcicZ045 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ045 oldJcicZ045 = (JcicZ045) iDataLog.clone(uJcicZ045);
			uJcicZ045.setTranKey(iTranKey);
			uJcicZ045.setAgreeCode(iAgreeCode);
			uJcicZ045.setOutJcicTxtDate(0);
			try {
				sJcicZ045Service.update(uJcicZ045, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ045, uJcicZ045);
			iDataLog.exec("L8306異動", uJcicZ045.getSubmitKey()+uJcicZ045.getCustId()+uJcicZ045.getRcDate()+uJcicZ045.getMaxMainCode());
			break;
			// 2022/7/14 新增刪除必須也要在記錄檔l6932裡面
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ045 = sJcicZ045Service.ukeyFirst(iKey, titaVo);
			JcicZ045 uJcicZ0452 = new JcicZ045();
			uJcicZ0452 = sJcicZ045Service.holdById(iJcicZ045.getJcicZ045Id(), titaVo);
			iJcicZ045 = sJcicZ045Service.findById(iJcicZ045Id);
			if (iJcicZ045 == null) {
				throw new LogicException("E0006", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			JcicZ045 oldJcicZ0452 = (JcicZ045) iDataLog.clone(uJcicZ0452);
			uJcicZ0452.setTranKey(iTranKey);
			uJcicZ0452.setAgreeCode(iAgreeCode);
			uJcicZ0452.setOutJcicTxtDate(0);
			
			Slice<JcicZ045Log> dJcicLogZ045 = null;
			dJcicLogZ045 = sJcicZ045LogService.ukeyEq(iJcicZ045.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ045 == null|| ("A".equals(iTranKey) && dJcicLogZ045 == null) ) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ045Service.delete(iJcicZ045, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ045Log iJcicZ045Log = dJcicLogZ045.getContent().get(0);
				iJcicZ045.setAgreeCode(iJcicZ045Log.getAgreeCode());
				iJcicZ045.setTranKey(iJcicZ045Log.getTranKey());
				iJcicZ045.setOutJcicTxtDate(iJcicZ045Log.getOutJcicTxtDate());
				try {
					sJcicZ045Service.update(iJcicZ045, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0006", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ0452, uJcicZ0452);
			iDataLog.exec("L8306刪除", uJcicZ0452.getSubmitKey()+uJcicZ0452.getCustId()+uJcicZ0452.getRcDate()+uJcicZ0452.getMaxMainCode());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
