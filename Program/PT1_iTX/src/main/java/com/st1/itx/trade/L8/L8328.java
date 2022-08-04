package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.UUID;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;
import com.st1.itx.db.domain.JcicZ448Log;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ448LogService;
/*DB服務*/
import com.st1.itx.db.service.JcicZ448Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

@Service("L8328")
@Scope("prototype")
/**
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8328 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ448Service sJcicZ448Service;
	@Autowired
	public JcicZ448LogService sJcicZ448LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8328 ");
		this.totaVo.init(titaVo);

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();
		String iTranKey = titaVo.getParam("TranKey").trim();
		String iCustId = titaVo.getParam("CustId").trim();
		String iSubmitKey = titaVo.getParam("SubmitKey").trim();
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate").trim());
		String iCourtCode = titaVo.getParam("CourtCode").trim();
		String iMaxMainCode = titaVo.getParam("MaxMainCode").trim();
		int iSignPrin = Integer.valueOf(titaVo.getParam("SignPrin").trim());
		int iSignOther = Integer.valueOf(titaVo.getParam("SignOther").trim());
		BigDecimal iOwnPercentage = new BigDecimal(titaVo.getParam("OwnPercentage").trim());
		int iAcQuitAmt = Integer.valueOf(titaVo.getParam("AcQuitAmt").trim());
		String iKey = "";
		
		CustMain tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		int iCustNo = tCustMain == null ? 0 : tCustMain.getCustNo();
		titaVo.putParam("CustNo", iCustNo);
		this.info("CustNo   = " + iCustNo);
		
		// JcicZ448
		JcicZ448 iJcicZ448 = new JcicZ448();
		JcicZ448Id iJcicZ448Id = new JcicZ448Id();
		iJcicZ448Id.setApplyDate(iApplyDate);
		iJcicZ448Id.setCustId(iCustId);
		iJcicZ448Id.setSubmitKey(iSubmitKey);
		iJcicZ448Id.setCourtCode(iCourtCode);
		iJcicZ448Id.setMaxMainCode(iMaxMainCode);
		JcicZ448 chJcicZ448 = new JcicZ448();
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-56)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2
			// 檢核同一key值之「應回報債權金融機構」皆已回報「'442':回報無擔保債權金融資料」、「'443':回報有擔保債權金融資料」檔案資料，否則予以剔退。(交易代碼X者不檢核).***J

			// 3
			// 檢核第9欄「簽約金額-本金」需等於該債權金融機構報送之「'442':回報無擔保債權金融資料」之17+21+25+29欄位金額加總.(交易代碼X者不檢核)***J

			// 4 各債權金融機構「債權比例」加總需為100.00%，否則予以剔退.***J

			// 5 任一債權金融機構報送同一KEY值454單獨受償檔案資料後，本檔案資料不得異動、刪除或補件.***J

			// 6 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey) || "X".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007", "同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				}
			} // 6 end
		}
		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複，並寫入JcicZ448
			chJcicZ448 = sJcicZ448Service.findById(iJcicZ448Id, titaVo);
			if (chJcicZ448 != null) {
				throw new LogicException("E0005", "已有相同資料存在");
			}
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ448.setJcicZ448Id(iJcicZ448Id);
			iJcicZ448.setTranKey(iTranKey);
			iJcicZ448.setSignPrin(iSignPrin);
			iJcicZ448.setSignOther(iSignOther);
			iJcicZ448.setOwnPercentage(iOwnPercentage);
			iJcicZ448.setAcQuitAmt(iAcQuitAmt);
			iJcicZ448.setUkey(iKey);
			try {
				sJcicZ448Service.insert(iJcicZ448, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}

			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ448 = sJcicZ448Service.ukeyFirst(iKey, titaVo);
			JcicZ448 uJcicZ448 = new JcicZ448();
			uJcicZ448 = sJcicZ448Service.holdById(iJcicZ448.getJcicZ448Id(), titaVo);
			if (uJcicZ448 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			JcicZ448 oldJcicZ448 = (JcicZ448) iDataLog.clone(uJcicZ448);
			uJcicZ448.setSignPrin(iSignPrin);
			uJcicZ448.setSignOther(iSignOther);
			uJcicZ448.setOwnPercentage(iOwnPercentage);
			uJcicZ448.setAcQuitAmt(iAcQuitAmt);
			uJcicZ448.setTranKey(iTranKey);
			uJcicZ448.setOutJcicTxtDate(0);
			try {
				sJcicZ448Service.update(uJcicZ448, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ448, uJcicZ448);
			iDataLog.exec("L8328異動",uJcicZ448.getSubmitKey()+uJcicZ448.getCustId()+uJcicZ448.getApplyDate()+uJcicZ448.getCourtCode());
			break;
		case "4": // 需刷主管卡
			iKey = titaVo.getParam("Ukey");
			iJcicZ448 = sJcicZ448Service.ukeyFirst(iKey, titaVo);
			JcicZ448 uJcicZ4482 = new JcicZ448();
			uJcicZ4482 = sJcicZ448Service.holdById(iJcicZ448.getJcicZ448Id(), titaVo);
			iJcicZ448 = sJcicZ448Service.findById(iJcicZ448Id);
			if (iJcicZ448 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			JcicZ448 oldJcicZ4482 = (JcicZ448) iDataLog.clone(uJcicZ4482);
			uJcicZ4482.setSignPrin(iSignPrin);
			uJcicZ4482.setSignOther(iSignOther);
			uJcicZ4482.setOwnPercentage(iOwnPercentage);
			uJcicZ4482.setAcQuitAmt(iAcQuitAmt);
			uJcicZ4482.setTranKey(iTranKey);
			uJcicZ4482.setOutJcicTxtDate(0);
			
			Slice<JcicZ448Log> dJcicLogZ448 = null;
			dJcicLogZ448 = sJcicZ448LogService.ukeyEq(iJcicZ448.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ448 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ448Service.delete(iJcicZ448, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ448Log iJcicZ448Log = dJcicLogZ448.getContent().get(0);
				iJcicZ448.setSignPrin(iJcicZ448Log.getSignPrin());
				iJcicZ448.setSignOther(iJcicZ448Log.getSignOther());
				iJcicZ448.setOwnPercentage(iJcicZ448Log.getOwnPercentage());
				iJcicZ448.setAcQuitAmt(iJcicZ448Log.getAcQuitAmt());
				iJcicZ448.setTranKey(iJcicZ448Log.getTranKey());
				iJcicZ448.setOutJcicTxtDate(iJcicZ448Log.getOutJcicTxtDate());
				try {
					sJcicZ448Service.update(iJcicZ448, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
			iDataLog.setEnv(titaVo, oldJcicZ4482, uJcicZ4482);
			iDataLog.exec("L8328刪除",uJcicZ4482.getSubmitKey()+uJcicZ4482.getCustId()+uJcicZ4482.getApplyDate()+uJcicZ4482.getCourtCode());
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}