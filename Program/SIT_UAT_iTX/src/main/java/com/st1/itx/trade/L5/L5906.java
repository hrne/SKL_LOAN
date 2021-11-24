package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.GraceCondition;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.GraceConditionService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5906")
@Scope("prototype")
/**
 * 寬限條件控管繳息查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5906 extends TradeBuffer {

	@Autowired
	public GraceConditionService iGraceConditionService;

	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5906 ");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		this.totaVo.init(titaVo);
		int iCustNoS = Integer.valueOf(titaVo.getParam("CustNoS"));
		int iCustNoE = Integer.valueOf(titaVo.getParam("CustNoE"));
		Slice<GraceCondition> iGraceCondition = null;
		if (iCustNoS == 0 && iCustNoE == 0) {
			iGraceCondition = iGraceConditionService.findAll(this.index, this.limit, titaVo);
		} else {
			iGraceCondition = iGraceConditionService.custNoEq(iCustNoE, iCustNoS, this.index, this.limit, titaVo);
		}
		if (iGraceCondition != null && iGraceCondition.hasNext()) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		if (iGraceCondition == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			for (GraceCondition rGraceCondition : iGraceCondition) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", rGraceCondition.getCustNo());
				CustMain iCustMain = new CustMain();
				iCustMain = iCustMainService.custNoFirst(rGraceCondition.getCustNo(), rGraceCondition.getCustNo(), titaVo);
				if (iCustMain == null) {
					occursList.putParam("OOCustName", "");
				} else {
					occursList.putParam("OOCustName", iCustMain.getCustName());
				}
				occursList.putParam("OOFacmNo", rGraceCondition.getFacmNo());
				occursList.putParam("OOActUse", rGraceCondition.getActUse());
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}