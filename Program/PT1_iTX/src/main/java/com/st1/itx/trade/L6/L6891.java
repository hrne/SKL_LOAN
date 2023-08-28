package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdRuleCode;
import com.st1.itx.db.service.CdRuleCodeService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.menu.MenuBuilder;
import com.st1.itx.util.parse.Parse;

@Service("L6891")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L6891 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	CdRuleCodeService sCdRuleCodeService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public DataLog iDataLog;

	@Autowired
	MenuBuilder menuBuilder;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6891 ");

		this.totaVo.init(titaVo);
		String iRuleCode = titaVo.getParam("RuleCode");
		String iRuleCodeItem = titaVo.getParam("RuleCodeItem");
		String iRmkItem = titaVo.getParam("RmkItem");
		int iRuleStDate = parse.stringToInteger(titaVo.getParam("RuleStDate"));
		int iRuleEdDate = parse.stringToInteger(titaVo.getParam("RuleEdDate"));
		String iEnableMark = titaVo.getParam("EnableMark");
		int iFunCode = parse.stringToInteger(titaVo.getParam("FunCode"));

//		CdRuleCodeId iCdRuleCodeId = new CdRuleCodeId();
//		iCdRuleCodeId.setRuleCode(iRuleCode);
//		iCdRuleCodeId.setRuleStDate(iRuleStDate);

		CdRuleCode resultList = null;

		resultList = sCdRuleCodeService.findById(iRuleCode, titaVo);
		if (iFunCode == 1) {
			if (resultList != null) {
				throw new LogicException("E0002", "管制代碼檔");
			}
		} else {
			if (resultList == null) {
				throw new LogicException("E0002", "管制代碼檔");
			}
		}

		switch (iFunCode) {
		case 1:
			this.info("resultList    = " + resultList);
			if (resultList != null) {
				throw new LogicException("E0002", "管制代碼檔");
			}
			
			CdRuleCode txCdRuleCode = new CdRuleCode();
//			CdRuleCodeId txCdRuleCodeId = new CdRuleCodeId();
//			txCdRuleCodeId.setRuleCode(iRuleCode);
//			txCdRuleCodeId.setRuleStDate(iRuleStDate);
//			txCdRuleCode.setCdRuleCodeId(iCdRuleCodeId);
			txCdRuleCode.setRuleCode(iRuleCode);
			txCdRuleCode.setRuleStDate(iRuleStDate);
			txCdRuleCode.setRuleCodeItem(iRuleCodeItem);
			txCdRuleCode.setRmkItem(iRmkItem);
			txCdRuleCode.setRuleEdDate(iRuleEdDate);
			txCdRuleCode.setEnableMark(iEnableMark);

			try {
				sCdRuleCodeService.insert(txCdRuleCode, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L5101 InnFundApl insert " + e.getErrorMsg());
			}

			break;
		case 2:
			CdRuleCode uxCdRuleCode = new CdRuleCode();
			uxCdRuleCode = sCdRuleCodeService.findById(iRuleCode, titaVo);
			if (uxCdRuleCode == null) {
				throw new LogicException("E0007", "無此更新資料");
			}

			CdRuleCode oldCdRuleCode = (CdRuleCode) iDataLog.clone(resultList);
			uxCdRuleCode.setRuleCode(iRuleCode);
			uxCdRuleCode.setRuleStDate(iRuleStDate);

			uxCdRuleCode.setRuleCodeItem(iRuleCodeItem);
			uxCdRuleCode.setRmkItem(iRmkItem);
			uxCdRuleCode.setRuleEdDate(iRuleEdDate);
			uxCdRuleCode.setEnableMark(iEnableMark);

			try {
				sCdRuleCodeService.update(uxCdRuleCode, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L5101 InnFundApl insert " + e.getErrorMsg());
			}

			iDataLog.setEnv(titaVo, oldCdRuleCode, uxCdRuleCode);
			iDataLog.exec("L6891異動", uxCdRuleCode.getRuleCode() + " " + uxCdRuleCode.getRuleStDate());

			break;
		case 4:
			CdRuleCode uxCdRuleCode2 = new CdRuleCode();
			uxCdRuleCode2 = sCdRuleCodeService.findById(iRuleCode, titaVo);
			if (uxCdRuleCode2 == null) {
				throw new LogicException("E0007", "無此更新資料");
			}

			CdRuleCode oldCdRuleCode2 = (CdRuleCode) iDataLog.clone(resultList);
			uxCdRuleCode2.setRuleCode(iRuleCode);
			uxCdRuleCode2.setRuleStDate(iRuleStDate);

			uxCdRuleCode2.setRuleCodeItem(iRuleCodeItem);
			uxCdRuleCode2.setRmkItem(iRmkItem);
			uxCdRuleCode2.setRuleEdDate(iRuleEdDate);
			uxCdRuleCode2.setEnableMark(iEnableMark);

			try {
				sCdRuleCodeService.delete(uxCdRuleCode2, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L5101 InnFundApl insert " + e.getErrorMsg());
			}

			iDataLog.setEnv(titaVo, oldCdRuleCode2, uxCdRuleCode2);
			iDataLog.exec("L6891刪除", uxCdRuleCode2.getRuleCode() + " " + uxCdRuleCode2.getRuleStDate());

			break;

		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
