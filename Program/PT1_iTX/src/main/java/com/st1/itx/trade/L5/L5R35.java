	package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.GraceCondition;
import com.st1.itx.db.domain.GraceConditionId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GraceConditionService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R35")
@Scope("prototype")
/**
 *
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R35 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public GraceConditionService iGraceConditionService;
	@Autowired
	public FacMainService facMainService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);

		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("RimFacmNo"));
		int iRimFunCd = Integer.valueOf(titaVo.getParam("RimFunCd"));

		GraceConditionId iGraceConditionId = new GraceConditionId();
		GraceCondition iGraceCondition = new GraceCondition();

		FacMainId iFacMainId = new FacMainId();
		FacMain iFacMain = new FacMain();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		if(iRimFunCd ==1) {
			iFacMainId.setCustNo(iCustNo);
			iFacMainId.setFacmNo(iFacmNo);
			iFacMain = facMainService.findById(iFacMainId, titaVo);
			if(iFacMain == null) {
				throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
		}else {
			iGraceConditionId.setCustNo(iCustNo);
			iGraceConditionId.setFacmNo(iFacmNo);
			iGraceCondition = iGraceConditionService.findById(iGraceConditionId, titaVo);

			if (iGraceCondition == null) {
				throw new LogicException(titaVo, "E0001", "");
			} else {
				totaVo.putParam("L5R35ActUse", iGraceCondition.getActUse());
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}