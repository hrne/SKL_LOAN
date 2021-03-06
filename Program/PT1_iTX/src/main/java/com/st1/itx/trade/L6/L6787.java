package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.CdBonusCoId;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.L6087ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;

@Service("L6787")
@Scope("prototype")
public class L6787 extends TradeBuffer {

	/* DB服務注入 */
	/* 轉換工具 */

	@Autowired
	public CdBonusCoService iCdBonusCoService;
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	@Autowired
	public L6087ServiceImpl iL6087ServiceImpl;
	@Autowired
	SendRsp iSendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6787 ");
		this.totaVo.init(titaVo);

		int iFuncd = Integer.valueOf(titaVo.getParam("FuncCode"));
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		int iEntdy = titaVo.getEntDyI() + 19110000;
		int iConditionCode = 0;
		int nWorkMonth = 0; // 當前工作月
		int maxWorkMonth = 0; // 生效中工作月(CdBonusCo中<=當前工作月中之最大值)
		List<Map<String, String>> wCdBonusCo = new ArrayList<Map<String, String>>();
		// 判斷是否為已生效工作月
		// 取當前工作月
		CdWorkMonth iCdWorkMonth = new CdWorkMonth();
		iCdWorkMonth = iCdWorkMonthService.findDateFirst(iEntdy, iEntdy, titaVo);
		if (iCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "查無工作月資料"); // 查無資料
		}
		String iYear = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getYear() - 1911), 3, "0");
		String iMonth = StringUtils.leftPad(String.valueOf(iCdWorkMonth.getMonth()), 2, "0");
		nWorkMonth = Integer.valueOf(iYear + iMonth) + 191100;

		try {
			wCdBonusCo = iL6087ServiceImpl.findAllData(Integer.valueOf(nWorkMonth), titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E5004", "");
		}
		if (wCdBonusCo == null) {
			throw new LogicException(titaVo, "E0001", "查無對應工作月");
		}
		for (Map<String, String> wwCdBonusCo : wCdBonusCo) {
			if (wwCdBonusCo.get("Sort").equals("1")) {
				maxWorkMonth = Integer.valueOf(wwCdBonusCo.get("WorkMonth"));
			}
		}

		// 修改生效中工作月需主管授權
		if (maxWorkMonth == iWorkMonth) {
			if (!titaVo.getHsupCode().equals("1")) {
				iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}
		}

		// 有值計件代碼list
		ArrayList<String> iPieceCodeList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			String iPieceCode = titaVo.getParam("PieceCode" + i);
			if (iPieceCodeList.contains(iPieceCode)) {
				continue;
			} else {
				if (!iPieceCode.equals("")) {
					iPieceCodeList.add(iPieceCode);
				}
			}
		}
		this.info("計件代碼list===" + iPieceCodeList);
		// 協辦等級list
		ArrayList<Integer> iClassPassCodeList = new ArrayList<>();
		iClassPassCodeList.add(1);
		iClassPassCodeList.add(2);
		iClassPassCodeList.add(3);

		BigDecimal iConditionAmt = new BigDecimal(titaVo.getParam("ConditionAmt"));
		BigDecimal iPrizeAmt = new BigDecimal(titaVo.getParam("PrizeAmt"));
		if (iFuncd == 4) { // 刪除
			// 計件代碼處理區塊
			iConditionCode = 1;
			for (String iCondition : iPieceCodeList) {
				// init
				CdBonusCo iCdBonusCo = new CdBonusCo();
				CdBonusCoId iCdBonusCoId = new CdBonusCoId();
				iCdBonusCoId.setWorkMonth(iWorkMonth);
				iCdBonusCoId.setConditionCode(iConditionCode);
				iCdBonusCoId.setCondition(iCondition);
				iCdBonusCo = iCdBonusCoService.holdById(iCdBonusCoId, titaVo);
				if (iCdBonusCo == null) {
					throw new LogicException(titaVo, "E2007", "計件代碼: " + iCondition + "查無資料"); // 刪除資料時發生錯誤
				}
				try {
					iCdBonusCoService.delete(iCdBonusCo, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時發生錯誤
				}
			}
			// 協辦等級處理區塊
			iConditionCode = 2;
			for (int j = 1; j < 4; j++) {
				CdBonusCo aCdBonusCo = new CdBonusCo();
				CdBonusCoId aCdBonusCoId = new CdBonusCoId();
				aCdBonusCoId.setConditionCode(iConditionCode);
				aCdBonusCoId.setWorkMonth(iWorkMonth);
				aCdBonusCoId.setCondition(String.valueOf(j));
				aCdBonusCo = iCdBonusCoService.holdById(aCdBonusCoId, titaVo);
				if (aCdBonusCo == null) {
					throw new LogicException(titaVo, "E2007", "協辦等級查無相同資料"); // 刪除資料時發生錯誤
				}
				try {
					iCdBonusCoService.delete(aCdBonusCo, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時發生錯誤
				}
			}
			// 專業獎金處理區塊
			iConditionCode = 3;
			CdBonusCo dCdBonusCo = new CdBonusCo();
			CdBonusCoId dCdBonusCoId = new CdBonusCoId();
			dCdBonusCoId.setConditionCode(iConditionCode);
			dCdBonusCoId.setWorkMonth(iWorkMonth);
			dCdBonusCoId.setCondition(" ");
			dCdBonusCo = iCdBonusCoService.holdById(dCdBonusCoId, titaVo);
			if (dCdBonusCo == null) {
				throw new LogicException(titaVo, "E2007", "協辦等級查無相同資料"); // 刪除資料時發生錯誤
			}
			try {
				iCdBonusCoService.delete(dCdBonusCo, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時發生錯誤
			}
		} else {
			if (iFuncd == 2) { // 先刪除後新增
				CdBonusCo delCdBonusCo = new CdBonusCo();
				Slice<CdBonusCo> slCdBonusCo = null;
				CdBonusCoId delCdBonusCoId = new CdBonusCoId();
				slCdBonusCo = iCdBonusCoService.findYearMonth(iWorkMonth, iWorkMonth, this.index, this.limit, titaVo);

				for (CdBonusCo xxCdBonus : slCdBonusCo) {
					delCdBonusCoId = xxCdBonus.getCdBonusCoId();
					delCdBonusCo = iCdBonusCoService.holdById(delCdBonusCoId, titaVo);
					try {
						iCdBonusCoService.delete(delCdBonusCo, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時發生錯誤
					}
				}
			}
			// 新增
			// 計件代碼處理區塊
			iConditionCode = 1;
			for (String bCondition : iPieceCodeList) {
				// init
				CdBonusCo bCdBonusCo = new CdBonusCo();
				CdBonusCoId bCdBonusCoId = new CdBonusCoId();
				bCdBonusCoId.setWorkMonth(iWorkMonth);
				bCdBonusCoId.setConditionCode(iConditionCode);
				bCdBonusCoId.setCondition(bCondition);
				bCdBonusCo = iCdBonusCoService.findById(bCdBonusCoId, titaVo);
				if (bCdBonusCo != null) {
					throw new LogicException(titaVo, "E0005", "計件代碼: " + bCondition + "已有相同工作月資料"); // 新增資料時發生錯誤
				}
				// init
				bCdBonusCo = new CdBonusCo();
				bCdBonusCo.setCdBonusCoId(bCdBonusCoId);
				bCdBonusCo.setConditionAmt(iConditionAmt);
				try {
					iCdBonusCoService.insert(bCdBonusCo, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時發生錯誤
				}
			}
			// 協辦等級處理區塊
			iConditionCode = 2;
			for (int j = 1; j < 4; j++) {
				CdBonusCo zCdBonusCo = new CdBonusCo();
				CdBonusCoId zCdBonusCoId = new CdBonusCoId();
				BigDecimal iBonus = new BigDecimal(titaVo.getParam("Bonus" + String.valueOf(j - 1)));
				BigDecimal iClassPassBonus = new BigDecimal(titaVo.getParam("ClassPassBonus" + String.valueOf(j - 1)));
				zCdBonusCoId.setConditionCode(iConditionCode);
				zCdBonusCoId.setWorkMonth(iWorkMonth);
				zCdBonusCoId.setCondition(String.valueOf(j));
				zCdBonusCo = iCdBonusCoService.findById(zCdBonusCoId, titaVo);
				if (zCdBonusCo != null) {
					throw new LogicException(titaVo, "E0005", "協辦等級已有相同工作月資料"); // 新增資料時發生錯誤
				}
				// init
				zCdBonusCo = new CdBonusCo();
				zCdBonusCo.setCdBonusCoId(zCdBonusCoId);
				zCdBonusCo.setBonus(iBonus);
				zCdBonusCo.setClassPassBonus(iClassPassBonus);
				try {
					iCdBonusCoService.insert(zCdBonusCo, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時發生錯誤
				}
			}
			// 專業獎勵金
			iConditionCode = 3;
			CdBonusCo bCdBonusCo = new CdBonusCo();
			CdBonusCoId bCdBonusCoId = new CdBonusCoId();
			bCdBonusCoId.setConditionCode(3);
			bCdBonusCoId.setCondition(" ");
			bCdBonusCoId.setWorkMonth(iWorkMonth);
			bCdBonusCo.setCdBonusCoId(bCdBonusCoId);
			bCdBonusCo.setBonus(iPrizeAmt);
			try {
				iCdBonusCoService.insert(bCdBonusCo, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時發生錯誤
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}