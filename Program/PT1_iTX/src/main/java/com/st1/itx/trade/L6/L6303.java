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
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=9,1 CurrencyCode=X,3 BaseRateCode=X,2 EffectDate=9,7
 * BaseRate=m,2.4 Remark=x,80 END=X,1
 */

@Service("L6303")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6303 extends TradeBuffer {

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

	TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6303 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));

		CdCommId tCdCommId = new CdCommId();
		int iEffectDate = Integer.valueOf(titaVo.getParam("EffectDate").trim()) + 19110000;

		tCdCommId.setCdType("01");
		tCdCommId.setCdItem("01");
		tCdCommId.setEffectDate(iEffectDate);
		CdComm sCdComm = sCdCommService.findById(tCdCommId, titaVo);
		this.info("iFuncCode = " + iFuncCode);
		switch (iFuncCode) {
		case 1: // 新增

			if (sCdComm != null) {
				throw new LogicException(titaVo, "E0002", ""); // 新增資料已存在
			}
			sCdComm = new CdComm();
			sCdComm = setCdcommRoutine(sCdComm, titaVo);
			sCdComm.setCdCommId(tCdCommId);
			try {
				sCdCommService.insert(sCdComm, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			CdComm cCdComm2 = sCdCommService.findById(tCdCommId, titaVo);
			CdComm uCdComm = new CdComm();
			uCdComm = sCdCommService.findById(tCdCommId, titaVo);
			if (cCdComm2 == null) {
				throw new LogicException(titaVo, "E0007", "無此更新資料"); // 修改資料不存在
			}
			CdComm oldCdComm = (CdComm) iDataLog.clone(uCdComm);

			sCdComm = setCdcommRoutine(uCdComm, titaVo);
			try {
				sCdCommService.update(uCdComm, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			iDataLog.setEnv(titaVo, oldCdComm, uCdComm); ////
			iDataLog.exec("修改政府優惠房屋貸款-補貼利率"); ////
			break;

		case 4: // 刪除
			CdComm cCdComm4 = sCdCommService.findById(tCdCommId, titaVo);
			CdComm uCdComm2 = new CdComm();
			uCdComm2 = sCdCommService.findById(tCdCommId, titaVo);
			if (cCdComm4 == null) {
				throw new LogicException(titaVo, "E0007", "無此更新資料"); // 修改資料不存在
			}
			
			// 主管授權
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
			
			CdComm oldCdComm2 = (CdComm) iDataLog.clone(uCdComm2);

			try {
				sCdCommService.delete(cCdComm4);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
			}

			iDataLog.setEnv(titaVo, oldCdComm2, cCdComm4);
			iDataLog.exec("刪除政府優惠房屋貸款-補貼利率");

			break;
		case 5: // 查詢
			this.addList(this.totaVo);
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private CdComm setCdcommRoutine(CdComm t, TitaVo titaVo) throws LogicException {

		t.setCdType("01");
		t.setCdItem("01");
		t.setEffectDate(parse.stringToInteger(titaVo.getParam("EffectDate")));
		t.setEnable("Y");
		t.setRemark(titaVo.getParam("Remark"));

		tTempVo.clear();
		for (int i = 1; i <= 10; i++) {
			if (titaVo.get("CodeTypeNo" + i) == null || "".equals(titaVo.get("CodeTypeNo" + i).trim())) {
				continue;
			}
			tTempVo.putParam("SubsidyRate" + titaVo.getParam("CodeTypeNo" + i), titaVo.getParam("JsonFieldsNo" + i));
		}
		t.setJsonFields(tTempVo.getJsonString());

		return t;
	}
}
