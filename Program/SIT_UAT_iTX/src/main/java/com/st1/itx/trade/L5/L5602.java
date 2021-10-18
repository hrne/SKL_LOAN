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
import com.st1.itx.db.service.CollMeetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
/* DB容器 */
import com.st1.itx.db.domain.CollMeet;
import com.st1.itx.db.domain.CollMeetId;

@Component("L5602")
@Scope("prototype")

/**
 * 面催明細資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5602 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CollMeetService iCollMeetService;
	@Autowired
	public CollListService iCollListService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));
		String iFuntionCd = titaVo.getParam("FunctionCd");
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
		Slice<CollList> allCollList = iCollListService.findCl(iClCustNo, iClFacmNo, 0,Integer.MAX_VALUE, titaVo);
		if (allCollList == null) {
			throw new LogicException(titaVo, "E0005", "");
		}
		// 處理找出的資料(包含面催檔登錄和主檔更新)
		for (CollList CollListVo : allCollList) {
			CollMeetId iCollMeetId = new CollMeetId();
			CollMeet iCollMeet = new CollMeet();
			iCollMeetId.setCaseCode(titaVo.getParam("CaseCode"));
			iCollMeetId.setCustNo(CollListVo.getCustNo());
			iCollMeetId.setFacmNo(CollListVo.getFacmNo());
			if (iFuntionCd.equals("1") || iFuntionCd.equals("3")) {
				iCollMeetId.setTitaTlrNo(titaVo.getTlrNo());
				iCollMeetId.setTitaTxtNo(titaVo.getTxtNo());
				iCollMeetId.setAcDate(Integer.valueOf(titaVo.getEntDy()));// 營業日 放acdate
			} else {
				iCollMeetId.setTitaTlrNo(titaVo.getParam("TitaTlrNo"));
				iCollMeetId.setTitaTxtNo(titaVo.getParam("TitaTxtNo"));
				iCollMeetId.setAcDate(Integer.valueOf(titaVo.getParam("TitaAcDate")));// 營業日 放acdate
			}
			iCollMeet.setCollMeetId(iCollMeetId);
			iCollMeet.setMeetDate(Integer.valueOf(titaVo.getParam("MeetDate")));
			iCollMeet.setMeetTime(titaVo.getParam("MeetTime"));
			iCollMeet.setContactCode(titaVo.getParam("ContactCode"));
			iCollMeet.setMeetPsnCode(titaVo.getParam("MeetPsnCode"));
			iCollMeet.setCollPsnCode(titaVo.getParam("AccCollPsnCode"));
			iCollMeet.setCollPsnName(titaVo.getParam("AccCollPsnName"));
			iCollMeet.setMeetPlaceCode(Integer.valueOf(titaVo.getParam("MeetPlaceCode")));
			iCollMeet.setMeetPlace(titaVo.getParam("MeetPlace"));
			iCollMeet.setRemark(titaVo.getParam("Remark"));
			CollMeet tCollMeet = iCollMeetService.findById(iCollMeetId, titaVo);
			if (iFuntionCd.equals("1") || iFuntionCd.equals("3")) {
				if (tCollMeet == null) {
					try {
						iCollMeetService.insert(iCollMeet, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0002", e.getErrorMsg());
					}
				}
			}
			if (iFuntionCd.equals("2")) {
				if (tCollMeet != null) {
					try {
						iCollMeetService.holdById(iCollMeetId);
						iCollMeet.setCreateDate(tCollMeet.getCreateDate());
						iCollMeet.setCreateEmpNo(tCollMeet.getCreateEmpNo());
						iCollMeetService.update(iCollMeet, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0003", "");
				}
			}
			if (iFuntionCd.equals("4")) {
				if (tCollMeet != null) {
					try {
						iCollMeetService.holdById(iCollMeetId);
						iCollMeetService.delete(iCollMeet, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg());
					}
				} else {
					throw new LogicException(titaVo, "E0004", "");
				}
			}
			// 更新list的上次作業日期和項目
			CollListId cCollListId = new CollListId();
			cCollListId.setCustNo(CollListVo.getCustNo());
			cCollListId.setFacmNo(CollListVo.getFacmNo());
			CollList neCollist = iCollListService.findById(cCollListId, titaVo);
			if (neCollist != null) {
				try {
					CollList upCollList = iCollListService.holdById(cCollListId, titaVo);
					upCollList.setTxCode("4"); // 上次作業項目
					upCollList.setTxDate(Integer.valueOf(titaVo.getEntDy()));
					iCollListService.update(upCollList, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0002", "");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}
