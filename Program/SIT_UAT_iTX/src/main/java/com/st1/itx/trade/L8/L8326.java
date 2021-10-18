package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.domain.JcicZ446Log;
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.service.JcicZ446LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.service.JcicZ451Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * TranKey=X,1<br>
 * CustId=X,10<br>
 * SubmitKey=X,10<br>
 * RcDate=9,7<br>
 * ChangePayDate=9,7<br>
 * ClosedDate=9,7<br>
 * ClosedResult=9,1<br>
 * OutJcicTxtDate=9,7<br>
 */

@Service("L8326")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8326 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ446LogService sJcicZ446LogService;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ451Service sJcicZ451Service;

	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8326 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		String iCloseCode = titaVo.getParam("CloseCode");
		int iCloseDate = Integer.valueOf(titaVo.getParam("CloseDate"));
		String iKey = "";
		int txDate = Integer.valueOf(titaVo.getEntDy());// 會計日 民國年YYYMMDD
		String[] acceptCloseCode = { "00", "01", "90", "99" };// 報送「'447':金融機構無擔保債務協議資料」後，可接受的「結案原因代號」

		// JcicZ446
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setSubmitKey(iSubmitKey);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		JcicZ446 chJcicZ446 = new JcicZ446();
		JcicZ440 iJcicZ440 = new JcicZ440();
		JcicZ440Id iJcicZ440Id = new JcicZ440Id();
		iJcicZ440Id.setSubmitKey(iSubmitKey);
		iJcicZ440Id.setCustId(iCustId);
		iJcicZ440Id.setApplyDate(iApplyDate);
		iJcicZ440Id.setCourtCode(iCourtCode);
		JcicZ447 iJcicZ447 = new JcicZ447();
		JcicZ447Id iJcicZ447Id = new JcicZ447Id();
		iJcicZ447Id.setSubmitKey(iSubmitKey);
		iJcicZ447Id.setCustId(iCustId);
		iJcicZ447Id.setApplyDate(iApplyDate);
		iJcicZ447Id.setCourtCode(iCourtCode);
		JcicZ451 iJcicZ451 = new JcicZ451();

		// 檢核項目(D-51)
		if (!"4".equals(iTranKey_Tmp)) {

			// 1.2
			// 「IDN+報送單位代號+調解申請日+受理調解機構代號」若未曾報送過「'440':前置調解受理申請暨請求回報債權通知資料」，予以剔退處理.(交易代碼為'X'者不檢核)
			if (!"X".equals(iTranKey)) {
				iJcicZ440 = sJcicZ440Service.findById(iJcicZ440Id, titaVo);
				if (iJcicZ440 == null) {
					throw new LogicException("E0005", "請先報送(440)前置調解受理申請暨請求回報債權通知資料.");
				}
			} // 1.2 end

			// 1.3.2 結案日期不可晚於報送本檔案日期.
			if ("A".equals(iTranKey)) {
				if (iCloseDate > txDate) {
					throw new LogicException("E0005", "[結案日期]不可晚於報送本檔案日期.");
				}
			} // 1.3.2 end
			
			// 1.3.1 結案日期不可早於調解申請日.
			if(iCloseDate < iApplyDate) {
				throw new LogicException("E0005", "[結案日期]不可早於[調解申請日].");
			}// 1.3.1 end

			// 1.4
			// 同一KEY值報送「'447':金融機構無擔保債務協議資料」後，若再報送本檔案資料時，結案理由代碼僅能報送'00','01','90'及'99'，其餘結案理由皆以剔退處理.
			iJcicZ447 = sJcicZ447Service.findById(iJcicZ447Id, titaVo);
			if (iJcicZ447 != null) {
				if (!"D".equals(iJcicZ447.getTranKey()) && (!Arrays.stream(acceptCloseCode).anyMatch(iCloseCode::equals))) { 
				throw new LogicException("E0005", "已報送過(447)前置調解金融機構無擔保債務協議資料，本檔案[結案原因代號]僅能報送'00','01','90'及'99'.");
				}
			} // 1.4 end

			// 1.5 檢核同一KEY值於'451':延期繳款期間不可報送「結案原因代號」 為'00'之本檔案資料
			if ("00".equals(iCloseCode) && ("A".equals(iTranKey))) {
				//@@@SQL-Function需改為custRcSubCourtEq
				Slice<JcicZ451> sJcicZ451 = sJcicZ451Service.otherEq(iSubmitKey, iCustId, iApplyDate, iCourtCode, txDate, 0, Integer.MAX_VALUE, titaVo);
				if (sJcicZ451 != null) {
					int sDelayYM = 0;
					for(JcicZ451 xJcicZ451 : sJcicZ451) {
						if(!"D".equals(xJcicZ451.getTranKey()) && xJcicZ451.getDelayYM() > sDelayYM) {
							sDelayYM = xJcicZ451.getDelayYM();
						}
					}
					int formateDelayYM = Integer.parseInt(sDelayYM + "31");
					if (txDate <= formateDelayYM) {
						throw new LogicException("E0005",
								"於(451)前置調解延期繳款期間(" + iJcicZ451.getDelayYM() + "前)不可報送「結案原因代號」 為'00'之本檔案資料.");
					}
				}
			} // 1.5 end

			// 2
			// 各資料檔案格式若第2欄「交易代碼」無「'D'刪除」功能者，如有資料key值報送錯誤情形者，需以本檔案格式報送「結案原因代號'97':資料key值報送錯誤，本行結案」至本中心，並重新報送更正後資料.***J

			// 檢核條件 end
		}

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			this.info("TEST===" + chJcicZ446);
			if (chJcicZ446 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ446.setJcicZ446Id(iJcicZ446Id);
			iJcicZ446.setTranKey(iTranKey);
			iJcicZ446.setUkey(iKey);
			iJcicZ446.setCloseCode(iCloseCode);
			iJcicZ446.setCloseDate(iCloseDate);
			try {
				sJcicZ446Service.insert(iJcicZ446, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ446 = sJcicZ446Service.ukeyFirst(iKey, titaVo);
			JcicZ446 uJcicZ446 = new JcicZ446();
			uJcicZ446 = sJcicZ446Service.holdById(iJcicZ446.getJcicZ446Id(), titaVo);
			if (uJcicZ446 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ446.setTranKey(iTranKey);
			uJcicZ446.setCloseCode(iCloseCode);
			uJcicZ446.setCloseDate(iCloseDate);
			uJcicZ446.setOutJcicTxtDate(0);
			JcicZ446 oldJcicZ446 = (JcicZ446) iDataLog.clone(uJcicZ446);
			try {
				sJcicZ446Service.update(uJcicZ446, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ446, uJcicZ446);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id);
			if (iJcicZ446 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ446Log> dJcicLogZ446 = null;
			dJcicLogZ446 = sJcicZ446LogService.ukeyEq(iJcicZ446.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ446 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ446Service.delete(iJcicZ446, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ446Log iJcicZ446Log = dJcicLogZ446.getContent().get(0);
				iJcicZ446.setCloseCode(iJcicZ446Log.getCloseCode());
				iJcicZ446.setCloseDate(iJcicZ446Log.getCloseDate());
				iJcicZ446.setOutJcicTxtDate(iJcicZ446Log.getOutJcicTxtDate());
				try {
					sJcicZ446Service.update(iJcicZ446, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			}
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
