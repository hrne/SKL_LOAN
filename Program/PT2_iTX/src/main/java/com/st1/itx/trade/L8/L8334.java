package com.st1.itx.trade.L8;

import java.math.BigDecimal;
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
import com.st1.itx.db.domain.CustMain;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ572LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ572Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8334")
@Scope("prototype")
/**
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8334 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ572Service sJcicZ572Service;
	@Autowired
	public JcicZ572LogService sJcicZ572LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8334 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		int iStartDate = Integer.valueOf(titaVo.getParam("StartDate").trim());
		int iPayDate = Integer.valueOf(titaVo.getParam("PayDate").trim());
		String iBankId = titaVo.getParam("BankId").trim();
		int iAllotAmt = Integer.valueOf(titaVo.getParam("AllotAmt").trim());
		BigDecimal iOwnPercentage = new BigDecimal(titaVo.getParam("OwnPercentage").trim());
		String iKey = "";

		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);

		// JcicZ572
		JcicZ572 iJcicZ572 = new JcicZ572();
		JcicZ572Id iJcicZ572Id = new JcicZ572Id();
		iJcicZ572Id.setApplyDate(iApplyDate);
		iJcicZ572Id.setBankId(iBankId);
		iJcicZ572Id.setCustId(iCustId);
		iJcicZ572Id.setSubmitKey(iSubmitKey);
		iJcicZ572Id.setPayDate(iPayDate);
		JcicZ572 chJcicZ572 = new JcicZ572();

		// 檢核項目(D-74)
		if (!"4".equals(iTranKey_Tmp)) {

			// 二 key值為「債務人IDN+報送單位代號+申請日期+本分配表首繳日+債權金融機構代號」，不可重複，重複者予以剔退--->檢核在case "1"

			// 三
			// 同一更生款項統一收付案件，若其一相關債權金融機構('571'第7欄填報為Y者),'571'檔案資料第8欄「債務人是否仍依更生方案正常還款予本金融機構」填報為N，則本檔案資料予以剔退處裡.***J

			// 四 同一更生款項統一收付案件，最大債權金融機構報送本檔案資料需等於填 報'571'第7欄「是否為更生債權人」為Y者筆數一致.***J

			// 五
			// 本檔案資料第9欄「參與分配債權金額」應與該金融機構所報最新一筆'571'檔案第10欄「參與分配債權金額」一致，否則予以剔退.--->1014會議通知不需檢核

			// 六 同一更生款項統一收付案件，各金融機構債權比例加總應為100%，本中心檢核...***J

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ572
			chJcicZ572 = sJcicZ572Service.findById(iJcicZ572Id, titaVo);
			if (chJcicZ572 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ572.setJcicZ572Id(iJcicZ572Id);
			iJcicZ572.setTranKey(iTranKey);
			iJcicZ572.setStartDate(iStartDate);
			iJcicZ572.setAllotAmt(iAllotAmt);
			iJcicZ572.setOwnPercentage(iOwnPercentage);
			iJcicZ572.setUkey(iKey);
			try {
				sJcicZ572Service.insert(iJcicZ572, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ572 = sJcicZ572Service.ukeyFirst(iKey, titaVo);
			JcicZ572 uJcicZ572 = new JcicZ572();
			uJcicZ572 = sJcicZ572Service.holdById(iJcicZ572.getJcicZ572Id(), titaVo);
			if (uJcicZ572 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ572 oldJcicZ572 = (JcicZ572) iDataLog.clone(uJcicZ572);
			uJcicZ572.setStartDate(iStartDate);
			uJcicZ572.setAllotAmt(iAllotAmt);
			uJcicZ572.setOwnPercentage(iOwnPercentage);
			uJcicZ572.setTranKey(iTranKey);
			uJcicZ572.setOutJcicTxtDate(0);
			try {
				sJcicZ572Service.update(uJcicZ572, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ572, uJcicZ572);
			iDataLog.exec("L8334異動", uJcicZ572.getSubmitKey() + uJcicZ572.getCustId() + uJcicZ572.getApplyDate() + uJcicZ572.getPayDate() + uJcicZ572.getBankId());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ572 = sJcicZ572Service.ukeyFirst(iKey, titaVo);
			JcicZ572 uJcicZ5722 = new JcicZ572();
			uJcicZ5722 = sJcicZ572Service.holdById(iJcicZ572.getJcicZ572Id(), titaVo);
			iJcicZ572 = sJcicZ572Service.findById(iJcicZ572Id);
			if (iJcicZ572 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			JcicZ572 oldJcicZ5722 = (JcicZ572) iDataLog.clone(uJcicZ5722);
			uJcicZ5722.setStartDate(iStartDate);
			uJcicZ5722.setAllotAmt(iAllotAmt);
			uJcicZ5722.setOwnPercentage(iOwnPercentage);
			uJcicZ5722.setTranKey(iTranKey);
			uJcicZ5722.setOutJcicTxtDate(0);

			Slice<JcicZ572Log> dJcicLogZ572 = null;
			dJcicLogZ572 = sJcicZ572LogService.ukeyEq(iJcicZ572.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ572 == null || ("A".equals(iTranKey) && dJcicLogZ572 == null)) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ572Service.delete(iJcicZ572, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ572Log iJcicZ572Log = dJcicLogZ572.getContent().get(0);
				iJcicZ572.setStartDate(iJcicZ572Log.getStartDate());
				iJcicZ572.setAllotAmt(iJcicZ572Log.getAllotAmt());
				iJcicZ572.setOwnPercentage(iJcicZ572Log.getOwnPercentage());
				iJcicZ572.setTranKey(iJcicZ572Log.getTranKey());
				iJcicZ572.setOutJcicTxtDate(iJcicZ572Log.getOutJcicTxtDate());
				try {
					sJcicZ572Service.update(iJcicZ572, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}

			iDataLog.setEnv(titaVo, oldJcicZ5722, uJcicZ5722);
			iDataLog.exec("L8334刪除", uJcicZ5722.getSubmitKey() + uJcicZ5722.getCustId() + uJcicZ5722.getApplyDate() + uJcicZ5722.getPayDate() + uJcicZ5722.getBankId());
			break;
		// 修改
		case "7":
			iKey = titaVo.getParam("Ukey");
			iJcicZ572 = sJcicZ572Service.ukeyFirst(iKey, titaVo);
			JcicZ572 uJcicZ5723 = new JcicZ572();
			uJcicZ5723 = sJcicZ572Service.holdById(iJcicZ572.getJcicZ572Id(), titaVo);
			if (uJcicZ5723 == null) {
				throw new LogicException("E0007", "更生債權金額異動通知資料");
			}
			// 2022/7/6新增錯誤判斷
			int JcicDate3 = iJcicZ572.getOutJcicTxtDate();
			this.info("JcicDate    = " + JcicDate3);
			if (JcicDate3 != 0) {
				throw new LogicException("E0007", "無此修改資料");
			}

			JcicZ572 oldJcicZ5723 = (JcicZ572) iDataLog.clone(uJcicZ5723);
			uJcicZ5723.setJcicZ572Id(iJcicZ572Id);
			uJcicZ5723.setTranKey(iTranKey);
			uJcicZ5723.setStartDate(iStartDate);
			uJcicZ5723.setAllotAmt(iAllotAmt);
			uJcicZ5723.setOwnPercentage(iOwnPercentage);
			uJcicZ5723.setUkey(iKey);

			try {
				sJcicZ572Service.update(uJcicZ5723, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			iDataLog.setEnv(titaVo, oldJcicZ5723, uJcicZ5723);
			iDataLog.exec("L8334修改", uJcicZ5723.getSubmitKey() + uJcicZ5723.getCustId() + uJcicZ5723.getApplyDate() + uJcicZ5723.getPayDate() + uJcicZ5723.getBankId());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}