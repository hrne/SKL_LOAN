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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
/* DB容器 */
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;
import com.st1.itx.db.domain.JcicZ447Log;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.service.JcicZ447LogService;

/*DB服務*/
import com.st1.itx.db.service.JcicZ447Service;

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

@Service("L8327")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8327 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ447LogService sJcicZ447LogService;
	@Autowired
	SendRsp iSendRsp;
	@Autowired
	DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8327 ");
		this.totaVo.init(titaVo);
		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp");
		String iTranKey = titaVo.getParam("TranKey");
		String iCustId = titaVo.getParam("CustId");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		int iApplyDate = Integer.valueOf(titaVo.getParam("ApplyDate"));
		String iCourtCode = titaVo.getParam("CourtCode");
		BigDecimal iCivil323Amt = new BigDecimal(titaVo.getParam("Civil323Amt"));
		int iSignDate = Integer.valueOf(titaVo.getParam("SignDate"));
		BigDecimal iTotalAmt = new BigDecimal(titaVo.getParam("TotalAmt"));
		int iFirstPayDate = Integer.valueOf(titaVo.getParam("FirstPayDate"));
		int iPeriod = Integer.valueOf(titaVo.getParam("Period"));
		BigDecimal iRate = new BigDecimal(titaVo.getParam("Rate"));
		int iMonthPayAmt = Integer.valueOf(titaVo.getParam("MonthPayAmt"));
		String iPayAccount = titaVo.getParam("PayAccount");
		String iKey = "";
		int txDate = Integer.valueOf(titaVo.getEntDy());// 會計日 民國年YYYMMDD

		// JcicZ447, JcicZ446
		JcicZ447 iJcicZ447 = new JcicZ447();
		JcicZ447Id iJcicZ447Id = new JcicZ447Id();
		iJcicZ447Id.setSubmitKey(iSubmitKey);
		iJcicZ447Id.setCustId(iCustId);
		iJcicZ447Id.setApplyDate(iApplyDate);
		iJcicZ447Id.setCourtCode(iCourtCode);
		JcicZ447 chJcicZ447 = new JcicZ447();
		JcicZ446 iJcicZ446 = new JcicZ446();
		JcicZ446Id iJcicZ446Id = new JcicZ446Id();
		iJcicZ446Id.setApplyDate(iApplyDate);
		iJcicZ446Id.setCourtCode(iCourtCode);
		iJcicZ446Id.setCustId(iCustId);
		iJcicZ446Id.setSubmitKey(iSubmitKey);

		// 檢核項目(D-55)
		if (!"4".equals(iTranKey_Tmp)) {

			// 2
			// 檢核第8欄「依民法第323條計算之債務總金額」需等於各金融機構回報之「'442':回報無擔保債權金融資料」檔案第[12+13+14+15]欄金額總和。(交易代碼X者不檢核).***J

			// 3 檢核第9欄「簽約總債權金額」需等於「'448':前置調解無擔保債務還款分配資料」檔案各金融機構第9+10欄金額總和.***J

			// 4 第10欄「簽約完成日期」不得大於「資料報送日」，否則予以剔退.
			if ("A".equals(iTranKey)) {
				if (iSignDate > txDate) {
					throw new LogicException("E0005", "「簽約完成日期」不得大於資料報送日.");
				}
			} // 4 end

			// 5 第11欄「首期應繳款日」不得小於第10欄「簽約完成日」，否則予以剔退.--->(前端檢核)

			// 6 同一key值報送446檔案結案後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.
			iJcicZ446 = sJcicZ446Service.findById(iJcicZ446Id, titaVo);
			if (iJcicZ446 != null && !"D".equals(iJcicZ446.getTranKey())) {
				if ("A".equals(iTranKey) || "X".equals(iTranKey)) {
					throw new LogicException(titaVo, "E0005",
							"同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				} else {
					throw new LogicException(titaVo, "E0007",
							"同一key值報送(446)前置調解結案通知資料後，且該結案資料未刪除前，不得新增、異動、刪除、補件本檔案資料.");
				}
			} // 6 end
		}
		// 檢核條件 end

		switch (iTranKey_Tmp) {
		case "1":
			// 檢核是否重複
			chJcicZ447 = sJcicZ447Service.findById(iJcicZ447Id, titaVo);
			this.info("TEST===" + chJcicZ447);
			if (chJcicZ447 != null) {
				throw new LogicException("E0005", "已有相同資料");
			}

			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			iJcicZ447.setJcicZ447Id(iJcicZ447Id);
			iJcicZ447.setTranKey(iTranKey);
			iJcicZ447.setUkey(iKey);
			iJcicZ447.setCivil323Amt(iCivil323Amt);
			iJcicZ447.setTotalAmt(iTotalAmt);
			iJcicZ447.setSignDate(iSignDate);
			iJcicZ447.setFirstPayDate(iFirstPayDate);
			iJcicZ447.setPeriod(iPeriod);
			iJcicZ447.setRate(iRate);
			iJcicZ447.setMonthPayAmt(iMonthPayAmt);
			iJcicZ447.setPayAccount(iPayAccount);
			try {
				sJcicZ447Service.insert(iJcicZ447, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			break;
		case "2":
			iKey = titaVo.getParam("Ukey");
			iJcicZ447 = sJcicZ447Service.ukeyFirst(iKey, titaVo);
			JcicZ447 uJcicZ447 = new JcicZ447();
			uJcicZ447 = sJcicZ447Service.holdById(iJcicZ447.getJcicZ447Id(), titaVo);
			if (uJcicZ447 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}
			uJcicZ447.setTranKey(iTranKey);
			uJcicZ447.setCivil323Amt(iCivil323Amt);
			uJcicZ447.setTotalAmt(iTotalAmt);
			uJcicZ447.setSignDate(iSignDate);
			uJcicZ447.setFirstPayDate(iFirstPayDate);
			uJcicZ447.setPeriod(iPeriod);
			uJcicZ447.setRate(iRate);
			uJcicZ447.setMonthPayAmt(iMonthPayAmt);
			uJcicZ447.setPayAccount(iPayAccount);
			uJcicZ447.setOutJcicTxtDate(0);
			JcicZ447 oldJcicZ447 = (JcicZ447) iDataLog.clone(uJcicZ447);
			try {
				sJcicZ447Service.update(uJcicZ447, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "更生債權金額異動通知資料");
			}
			iDataLog.setEnv(titaVo, oldJcicZ447, uJcicZ447);
			iDataLog.exec();
			break;
		case "4": // 需刷主管卡
			iJcicZ447 = sJcicZ447Service.findById(iJcicZ447Id);
			if (iJcicZ447 == null) {
				throw new LogicException("E0008", "");
			}
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			Slice<JcicZ447Log> dJcicLogZ447 = null;
			dJcicLogZ447 = sJcicZ447LogService.ukeyEq(iJcicZ447.getUkey(), 0, Integer.MAX_VALUE, titaVo);
			if (dJcicLogZ447 == null) {
				// 尚未開始寫入log檔之資料，主檔資料可刪除
				try {
					sJcicZ447Service.delete(iJcicZ447, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "更生債權金額異動通知資料");
				}
			} else {// 已開始寫入log檔之資料，主檔資料還原成最近一筆之內容
					// 最近一筆之資料
				JcicZ447Log iJcicZ447Log = dJcicLogZ447.getContent().get(0);
				iJcicZ447.setCivil323Amt(iJcicZ447Log.getCivil323Amt());
				iJcicZ447.setTotalAmt(iJcicZ447Log.getTotalAmt());
				iJcicZ447.setSignDate(iJcicZ447Log.getSignDate());
				iJcicZ447.setFirstPayDate(iJcicZ447Log.getFirstPayDate());
				iJcicZ447.setPeriod(iJcicZ447Log.getPeriod());
				iJcicZ447.setRate(iJcicZ447Log.getRate());
				iJcicZ447.setMonthPayAmt(iJcicZ447Log.getMonthPayAmt());
				iJcicZ447.setPayAccount(iJcicZ447Log.getPayAccount());
				iJcicZ447.setTranKey(iJcicZ447Log.getTranKey());
				iJcicZ447.setOutJcicTxtDate(iJcicZ447Log.getOutJcicTxtDate());
				try {
					sJcicZ447Service.update(iJcicZ447, titaVo);
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
