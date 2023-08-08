package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=9,1 CurrencyCode=X,3 BaseRateCode=X,2 EffectDate=9,7
 * BaseRate=m,2.4 Remark=x,80 END=X,1
 */

@Service("L6304")
@Scope("prototype")
/**
 *
 *
 * @author Mata
 * @version 1.0.0
 */
public class L6304 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdCommService sCdCommService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public SendRsp sendRsp;
	@Autowired
	WebClient webClient;

	TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6304 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("TranKey_Tmp"));
		int iEffectDate = titaVo.getParam("RimEffectDate").trim().length() == 0 ? 0
				: Integer.valueOf(titaVo.getParam("RimEffectDate").trim());

		this.info("iFuncCode = " + iFuncCode);
		this.info("iEffectDate = " + iEffectDate);

		CdCommId tCdCommId = new CdCommId();

		tCdCommId.setCdType("02");
		tCdCommId.setCdItem("01");
		tCdCommId.setEffectDate(iEffectDate);

		CdComm sCdComm = sCdCommService.findById(tCdCommId, titaVo);

		titaVo.keepOrgDataBase();// 保留原本記號

		switch (iFuncCode) {
		case 1: // 新增

			if (sCdComm != null) {
				throw new LogicException(titaVo, "E0002", ""); // 新增資料已存在
			}

			sCdComm = new CdComm();
			sCdComm.setCdCommId(tCdCommId);
			sCdComm = setCdcommRoutine(sCdComm, titaVo);

			sCdComm.setEnable("Y");
			sCdComm.setRemark("專案放款餘額");

			try {
				sCdCommService.insert(sCdComm, titaVo);

			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

			titaVo.setDataBaseOnMon();// 指定月報環境

			try {
				sCdCommService.insert(sCdComm, titaVo);

			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

			titaVo.setDataBaseOnOrg();// 還原原本的環境

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "新增資料成功", titaVo);

			break;

		case 2: // 修改

			CdComm uCdComm = new CdComm();

			CdComm oldCdComm = (CdComm) iDataLog.clone(sCdComm);

			uCdComm = setCdcommRoutine(sCdComm, titaVo);

			try {
				sCdCommService.update(uCdComm, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}

			this.info("onMon");
			titaVo.setDataBaseOnMon();// 指定月報環境

			try {
				sCdCommService.update(uCdComm, titaVo);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}

			titaVo.setDataBaseOnOrg();// 還原原本的環境

			iDataLog.setEnv(titaVo, oldCdComm, uCdComm);
			iDataLog.exec("修改專案放款");

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "修改資料成功", titaVo);

			break;

		case 4: // 刪除
			if (sCdComm == null) {
				throw new LogicException(titaVo, "E0007", "無此更新資料"); // 修改資料不存在
			}
			CdComm dCdComm = new CdComm();
			dCdComm.setCdCommId(tCdCommId);

			// 主管授權
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			CdComm oldDCdComm = (CdComm) iDataLog.clone(sCdComm);

			try {
				sCdCommService.delete(dCdComm);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
			
			this.info("onMon");
			titaVo.setDataBaseOnMon();// 指定月報環境

			try {
				sCdCommService.delete(dCdComm);

			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
			}

			titaVo.setDataBaseOnOrg();// 還原原本的環境

			iDataLog.setEnv(titaVo, oldDCdComm, dCdComm);
			iDataLog.exec("刪除專案放款");

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "刪除資料成功", titaVo);

			break;
		case 5: // 查詢
			this.addList(this.totaVo);
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private CdComm setCdcommRoutine(CdComm t, TitaVo titaVo) throws LogicException {

		int effectDate = parse.stringToInteger(titaVo.getParam("RimEffectDate").trim());

		t.setCdType("02");
		t.setCdItem("01");
		t.setEffectDate(effectDate);

		tTempVo.clear();

		tTempVo.putParam("YearMonth", (effectDate + 19110000) / 100);

		for (int i = 1; i <= 12; i++) {
			if (titaVo.get("ProdName" + i) == null || "".equals(titaVo.get("LoanBal" + i).trim())) {
				continue;
			}
			// 原放款主檔
			tTempVo.putParam("o" + titaVo.getParam("ColName" + i).trim(), titaVo.getParam("oLoanBal" + i));
			// 調整後放款主檔
			tTempVo.putParam(titaVo.getParam("ColName" + i).trim(), titaVo.getParam("LoanBal" + i));
			// 備註
			tTempVo.putParam("Remark" + i, titaVo.getParam("Remark" + i));
		}

		t.setJsonFields(tTempVo.getJsonString());

		return t;
	}
}
