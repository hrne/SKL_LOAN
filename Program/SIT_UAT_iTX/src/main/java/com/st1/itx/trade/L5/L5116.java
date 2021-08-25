package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.GraceCondition;
import com.st1.itx.db.domain.GraceConditionId;
import com.st1.itx.db.service.GraceConditionService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

/* DB容器 */

@Component("L5116")
@Scope("prototype")

/**
 * 寬限條件控管維護
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5116 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public GraceConditionService iGraceConditionService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5116 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iFunctioncd = titaVo.getParam("FunctionCode");
		String iActUse = titaVo.getParam("ActUse");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		GraceCondition iGraceCondition = new GraceCondition();
		GraceConditionId iGraceConditionId = new GraceConditionId();
		iGraceConditionId.setCustNo(iCustNo);
		iGraceConditionId.setFacmNo(iFacmNo);
		switch (iFunctioncd) {
		case "1":
			iGraceCondition = iGraceConditionService.findById(iGraceConditionId, titaVo);
			if (iGraceCondition == null) {
				iGraceCondition = new GraceCondition();
				iGraceCondition.setGraceConditionId(iGraceConditionId);
				iGraceCondition.setActUse(iActUse);
				try {
					iGraceConditionService.insert(iGraceCondition, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0005", "新增資料已存在");
			}
			break;
		case "2":
			iGraceCondition = iGraceConditionService.holdById(iGraceConditionId, titaVo);
			if (iGraceCondition == null) {
				throw new LogicException(titaVo, "E0003", "修改資料不存在");
			} else {
				GraceCondition oiGraceCondition = (GraceCondition) iDataLog.clone(iGraceCondition);
				iGraceCondition.setActUse(iActUse);
				try {
					iGraceConditionService.update(iGraceCondition, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", e.getErrorMsg());
				}
				// 紀錄變更前變更後
				iDataLog.setEnv(titaVo, oiGraceCondition, iGraceCondition);
				iDataLog.exec();
			}
			break;
		case "4":
			iGraceCondition = iGraceConditionService.holdById(iGraceConditionId, titaVo);
			if (iGraceCondition == null) {
				throw new LogicException(titaVo, "E0008", "刪除資料不存在");
			} else {
				try {
					iGraceConditionService.delete(iGraceCondition, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", e.getErrorMsg());
				}
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}
