package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CollRemindService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.util.data.DataLog;
/* DB容器 */
import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;

@Component("L5605")
@Scope("prototype")

/**
 * 提醒資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5605 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollRemindService iCollRemindService;
	@Autowired
	public CollListService iCollListService;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5605 start");
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iFunctionCd = titaVo.getParam("FunctionCd");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// 進主檔找是否有這筆資料
		CollListId iCollListId = new CollListId();
		iCollListId.setCustNo(iCustNo);
		iCollListId.setFacmNo(iFacmNo);
		CollList iCollList = iCollListService.findById(iCollListId, titaVo);
		if (iCollList == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 從找到的資料挖出擔保品戶號、額度
		int iClCustNo = iCollList.getClCustNo();
		int iClFacmNo = iCollList.getClFacmNo();
		// 用撈出的擔保品編號找全部相同擔保品的資料
		Slice<CollList> allCollList = iCollListService.findCl(iClCustNo, iClFacmNo, 0, Integer.MAX_VALUE, titaVo);
		if (allCollList == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 處理找出的資料(包含法催檔登錄和主檔更新)
		for (CollList iCollListVo : allCollList) {
			CollRemindId iCollRemindId = new CollRemindId();
			CollRemind iCollRemind = new CollRemind();
			iCollRemindId.setCaseCode(titaVo.getParam("CaseCode"));
			iCollRemindId.setCustNo(iCollListVo.getCustNo());
			iCollRemindId.setFacmNo(iCollListVo.getFacmNo());
			if (iFunctionCd.equals("1")) {
				iCollRemindId.setTitaTlrNo(titaVo.getTlrNo());
				iCollRemindId.setTitaTxtNo(titaVo.getTxtNo());
				iCollRemindId.setAcDate(Integer.valueOf(titaVo.getCalDy()));// 日曆日 放acdate
			} else {
				iCollRemindId.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				iCollRemindId.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				iCollRemindId.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));
			}
			iCollRemind.setCollRemindId(iCollRemindId);
			iCollRemind.setCondCode(titaVo.getParam("CondCode"));
			iCollRemind.setRemindDate(Integer.valueOf(titaVo.getParam("RemindDate")));
			iCollRemind.setEditDate(Integer.valueOf(titaVo.getParam("EditDate")));
			iCollRemind.setEditTime(titaVo.getParam("EditTime"));
			iCollRemind.setRemindCode(titaVo.getParam("RemindCode"));
			iCollRemind.setRemark(titaVo.getParam("Remark"));
			CollRemind tCollRemind = iCollRemindService.findById(iCollRemindId, titaVo);
			if (iFunctionCd.equals("1")) {
				if (tCollRemind == null) {
					try {
						iCollRemindService.insert(iCollRemind, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0002", "");
				}
			} else {
				if (tCollRemind != null) {
					CollRemind uCollRemind = new CollRemind();
					uCollRemind = iCollRemindService.holdById(iCollRemindId);
					CollRemind beforeCollRemind = (CollRemind) iDataLog.clone(uCollRemind);
					uCollRemind.setCondCode(titaVo.getParam("CondCode"));
					uCollRemind.setRemindDate(Integer.valueOf(titaVo.getParam("RemindDate")));
					uCollRemind.setEditDate(Integer.valueOf(titaVo.getParam("EditDate")));
					uCollRemind.setEditTime(titaVo.getParam("EditTime"));
					uCollRemind.setRemindCode(titaVo.getParam("RemindCode"));
					uCollRemind.setRemark(titaVo.getParam("Remark"));
					try {
						iCollRemindService.update(uCollRemind, titaVo);
						iDataLog.setEnv(titaVo, beforeCollRemind, uCollRemind);
						iDataLog.exec();		
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0003", "");
				}
			}

			// 更新list的上次作業日期和項目
			CollListId cCollListid = new CollListId();
			cCollListid.setCustNo(iCollListVo.getCustNo());
			cCollListid.setFacmNo(iCollListVo.getFacmNo());
			CollList neCollList = iCollListService.findById(cCollListid, titaVo);
			if (neCollList != null) {
				try {
					CollList upCollList = iCollListService.holdById(neCollList);
					upCollList.setTxCode("6"); // 上次作業項目
					upCollList.setTxDate(Integer.valueOf(titaVo.getCalDy())); // 上次作業日期
					iCollListService.update(upCollList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 主檔更新錯誤訊息
				}
			} else {
				throw new LogicException(titaVo, "E0003", ""); // 主檔無資料錯誤訊息
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
