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
/* DB容器 */
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Id;
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.service.JcicZ574LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ574Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8336")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */

public class L8336 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ574Service sJcicZ574Service;
	@Autowired
	public JcicZ574LogService sJcicZ574LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8336 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		int iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate").trim());
		String iCloseMark = titaVo.getParam("CloseMark").trim();
		String iPhoneNo = titaVo.getParam("PhoneNo").trim();
		String iKey = "";
		// JcicZ574
		JcicZ574 iJcicZ574 = new JcicZ574();
		JcicZ574Id iJcicZ574Id = new JcicZ574Id();
		iJcicZ574Id.setApplyDate(iApplyDate);
		iJcicZ574Id.setCustId(iCustId);
		iJcicZ574Id.setSubmitKey(iSubmitKey);
		JcicZ574 chJcicZ574 = new JcicZ574();

		// 檢核項目(D-77)
		if (!"4".equals(iTranKey_Tmp)) {
			// 二 key值為「債務人IDN+報送單位代號+申請日期」，不可重複，重複者予以剔退--->檢核在case "1"

			// 三 結案日期不可早於申請日期-->(前端檢核)，亦不可晚於報送本檔案日期--->前端檢核
		}
		// 檢核項目end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ574
			chJcicZ574 = sJcicZ574Service.findById(iJcicZ574Id, titaVo);
			if (chJcicZ574 != null) {
				throw new LogicException("E0002", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ574.setJcicZ574Id(iJcicZ574Id);
			iJcicZ574.setTranKey(iTranKey);
			iJcicZ574.setCloseDate(iCloseDate);
			iJcicZ574.setCloseMark(iCloseMark);
			iJcicZ574.setPhoneNo(iPhoneNo);
			iJcicZ574.setUkey(iKey);
			try {
				sJcicZ574Service.insert(iJcicZ574, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ574 = sJcicZ574Service.ukeyFirst(iKey, titaVo);
			JcicZ574 uJcicZ574 = new JcicZ574();
			uJcicZ574 = sJcicZ574Service.holdById(iJcicZ574.getJcicZ574Id(), titaVo);
			if (uJcicZ574 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ574 oldJcicZ574 = (JcicZ574) iDataLog.clone(uJcicZ574);
			uJcicZ574.setCloseDate(iCloseDate);
			uJcicZ574.setCloseMark(iCloseMark);
			uJcicZ574.setPhoneNo(iPhoneNo);
			uJcicZ574.setTranKey(iTranKey);
			uJcicZ574.setOutJcicTxtDate(0);
			try {
				sJcicZ574Service.update(uJcicZ574, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ574, uJcicZ574);
			iDataLog.exec("L8336異動",uJcicZ574.getCustId()+uJcicZ574.getSubmitKey()+uJcicZ574.getApplyDate());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ574 = sJcicZ574Service.ukeyFirst(iKey, titaVo);
			JcicZ574 uJcicZ5742 = new JcicZ574();
			uJcicZ5742 = sJcicZ574Service.holdById(iJcicZ574.getJcicZ574Id(), titaVo);
			iJcicZ574 = sJcicZ574Service.findById(iJcicZ574Id);
			if (iJcicZ574 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ574 oldJcicZ5742 = (JcicZ574) iDataLog.clone(uJcicZ5742);
			uJcicZ5742.setCloseDate(iCloseDate);
			uJcicZ5742.setCloseMark(iCloseMark);
			uJcicZ5742.setPhoneNo(iPhoneNo);
			uJcicZ5742.setTranKey(iTranKey);
			uJcicZ5742.setOutJcicTxtDate(0);
			
			Slice<JcicZ574Log> dJcicLogZ574 = null;
			dJcicLogZ574 = sJcicZ574LogService.ukeyEq(iJcicZ574.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ574 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ574Service.delete(iJcicZ574, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ574Log iJcicZ574Log = dJcicLogZ574.getContent().get(0);
				iJcicZ574.setCloseDate(iJcicZ574Log.getCloseDate());
				iJcicZ574.setCloseMark(iJcicZ574Log.getCloseMark());
				iJcicZ574.setPhoneNo(iJcicZ574Log.getPhoneNo());
				iJcicZ574.setTranKey(iJcicZ574Log.getTranKey());
				iJcicZ574.setOutJcicTxtDate(iJcicZ574Log.getOutJcicTxtDate());
				try {
					sJcicZ574Service.update(iJcicZ574, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			
			iDataLog.setEnv(titaVo, oldJcicZ5742, uJcicZ5742);
			iDataLog.exec("L8336刪除",uJcicZ5742.getCustId()+uJcicZ5742.getSubmitKey()+uJcicZ5742.getApplyDate());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}