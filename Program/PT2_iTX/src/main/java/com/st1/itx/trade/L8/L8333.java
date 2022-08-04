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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.service.JcicZ571LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.service.JcicZ575Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8333")
@Scope("prototype")
/**
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8333 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ570Service sJcicZ570Service;
	@Autowired
	public JcicZ575Service sJcicZ575Service;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8333 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iBankId = titaVo.getParam("BankId").trim();
		String iOwnerYn = titaVo.getParam("OwnerYn").trim();
		String iPayYn = titaVo.getParam("PayYn").trim();
		int iOwnerAmt = Integer.valueOf(titaVo.getParam("OwnerAmt").trim());
		int iAllotAmt = Integer.valueOf(titaVo.getParam("AllotAmt").trim());
		int iUnallotAmt = Integer.valueOf(titaVo.getParam("UnallotAmt").trim());
		String iKey = "";
		
		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
		// JcicZ571
		JcicZ571 iJcicZ571 = new JcicZ571();
		JcicZ571Id iJcicZ571Id = new JcicZ571Id();
		iJcicZ571Id.setApplyDate(iApplyDate);
		iJcicZ571Id.setBankId(iBankId);
		iJcicZ571Id.setCustId(iCustId);
		iJcicZ571Id.setSubmitKey(iSubmitKey);
		JcicZ571 chJcicZ571 = new JcicZ571();
		JcicZ570 iJcicZ570 = new JcicZ570();
		JcicZ570Id iJcicZ570Id = new JcicZ570Id();
		iJcicZ570Id.setApplyDate(iApplyDate);
		iJcicZ570Id.setCustId(iCustId);
		iJcicZ570Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-73)
		if (!"4".equals(iTranKey_Tmp)) {

			// 二 key值為「債務人IDN+報送單位代號+申請日期+受理款項統一收付之債權金融機構代號」，不可重複，重複者予以剔退--->檢核在case "1"

			// 三 start 同一更生款項統一收付案件若未曾報送'570'檔案資料，則予以剔退處理
			iJcicZ570 = sJcicZ570Service.findById(iJcicZ570Id, titaVo);
			if (iJcicZ570 == null || "D".equals(iJcicZ570.getTranKey())) {
				if ("A".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一更生款項統一收付案件未曾報送(570)受理更生款項統一收付通知資料");
				} else {
					throw new LogicException(titaVo, "E0007", "同一更生款項統一收付案件未曾報送(570)受理更生款項統一收付通知資料");
				}
			}
			// 三end

			// 四 檢核金融機構報送本檔案資料日期，若超逾同一更生款項統一收付案件'575'檔案資料建檔日+7個工作日，且最大債權金融機構未曾報送'575'***J
			// 債務人通報債權金額異動檔者，予以剔退處理 處理***J
			// 五 檢核金融機構報送本檔案資料日期，若超逾同一更生款項統一收付案件最大債權金融機構報送'575'資料建檔日+3個工作日者，予以剔退處理***J

			// 六 第7欄「是否為更生債權人」填報為'Y'者，第8-11欄為必要填報欄位--->(前端檢核)
			// 七 檢核第10、11欄位金額加總為第9欄「更生債權總金額」--->(前端檢核)

			// 檢核項目 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ571
			chJcicZ571 = sJcicZ571Service.findById(iJcicZ571Id, titaVo);
			if (chJcicZ571 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ571.setJcicZ571Id(iJcicZ571Id);
			iJcicZ571.setTranKey(iTranKey);
			iJcicZ571.setOwnerYn(iOwnerYn);
			iJcicZ571.setPayYn(iPayYn);
			iJcicZ571.setOwnerAmt(iOwnerAmt);
			iJcicZ571.setAllotAmt(iAllotAmt);
			iJcicZ571.setUnallotAmt(iUnallotAmt);
			iJcicZ571.setUkey(iKey);
			try {
				sJcicZ571Service.insert(iJcicZ571, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ571 = sJcicZ571Service.ukeyFirst(iKey, titaVo);
			JcicZ571 uJcicZ571 = new JcicZ571();
			uJcicZ571 = sJcicZ571Service.holdById(iJcicZ571.getJcicZ571Id(), titaVo);
			if (uJcicZ571 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ571 oldJcicZ571 = (JcicZ571) iDataLog.clone(uJcicZ571);
			uJcicZ571.setTranKey(iTranKey);
			uJcicZ571.setOwnerYn(iOwnerYn);
			uJcicZ571.setPayYn(iPayYn);
			uJcicZ571.setOwnerAmt(iOwnerAmt);
			uJcicZ571.setAllotAmt(iAllotAmt);
			uJcicZ571.setUnallotAmt(iUnallotAmt);
			uJcicZ571.setOutJcicTxtDate(0);
			try {
				sJcicZ571Service.update(uJcicZ571, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ571, uJcicZ571);
			iDataLog.exec("L8333異動",uJcicZ571.getSubmitKey()+uJcicZ571.getCustId()+uJcicZ571.getApplyDate()+uJcicZ571.getBankId());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ571 = sJcicZ571Service.ukeyFirst(iKey, titaVo);
			JcicZ571 uJcicZ5712 = new JcicZ571();
			uJcicZ5712 = sJcicZ571Service.holdById(iJcicZ571.getJcicZ571Id(), titaVo);
			iJcicZ571 = sJcicZ571Service.findById(iJcicZ571Id);
			if (iJcicZ571 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ571 oldJcicZ5712 = (JcicZ571) iDataLog.clone(uJcicZ5712);
			uJcicZ5712.setTranKey(iTranKey);
			uJcicZ5712.setOwnerYn(iOwnerYn);
			uJcicZ5712.setPayYn(iPayYn);
			uJcicZ5712.setOwnerAmt(iOwnerAmt);
			uJcicZ5712.setAllotAmt(iAllotAmt);
			uJcicZ5712.setUnallotAmt(iUnallotAmt);
			uJcicZ5712.setOutJcicTxtDate(0);
			
			Slice<JcicZ571Log> dJcicLogZ571 = null;
			dJcicLogZ571 = sJcicZ571LogService.ukeyEq(iJcicZ571.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ571 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ571Service.delete(iJcicZ571, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ571Log iJcicZ571Log = dJcicLogZ571.getContent().get(0);
				iJcicZ571.setOwnerYn(iJcicZ571Log.getOwnerYn());
				iJcicZ571.setPayYn(iJcicZ571Log.getPayYn());
				iJcicZ571.setOwnerAmt(iJcicZ571Log.getOwnerAmt());
				iJcicZ571.setAllotAmt(iJcicZ571Log.getAllotAmt());
				iJcicZ571.setUnallotAmt(iJcicZ571Log.getUnallotAmt());
				iJcicZ571.setTranKey(iJcicZ571Log.getTranKey());
				iJcicZ571.setOutJcicTxtDate(iJcicZ571Log.getOutJcicTxtDate());
				try {
					sJcicZ571Service.update(iJcicZ571, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			
			iDataLog.setEnv(titaVo, oldJcicZ5712, uJcicZ5712);
			iDataLog.exec("L8333刪除",uJcicZ5712.getSubmitKey()+uJcicZ5712.getCustId()+uJcicZ5712.getApplyDate()+uJcicZ5712.getBankId());
		default:
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
