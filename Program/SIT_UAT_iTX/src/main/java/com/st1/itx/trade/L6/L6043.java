
package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.service.CdBranchService;

@Service("L6043")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6043 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public TxAuthGroupService txAuthGroupService;

	@Autowired
	public CdBranchService cdBranchService;

	HashMap<String, String> branchItems = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6043 ");
		this.totaVo.init(titaVo);

		String iBranchNo = titaVo.getParam("BranchNo");
		String iAuthNo = titaVo.getParam("AuthNo") + "%";
		int iStatus1 = Integer.parseInt(titaVo.getParam("Status"));
		int iStatus2 = 0;
		if(iStatus1==9) {
			iStatus1=0;
			iStatus2=1;
		}
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxAuthGroup> slTxAuthGroup = null;

		if ("".equals(iBranchNo)) {
			slTxAuthGroup = txAuthGroupService.AuthNoLike(iAuthNo, iStatus1, iStatus2, this.index, this.limit);
		} else {
			slTxAuthGroup = txAuthGroupService.BranchAuthNo(iBranchNo, iAuthNo, iStatus1, iStatus2, this.index, this.limit);

		}
		List<TxAuthGroup> lTxAuthGroup = slTxAuthGroup == null ? null : slTxAuthGroup.getContent();

		if (lTxAuthGroup == null) {
			throw new LogicException("E0001", "");
		} else {
			for (TxAuthGroup tTxAuthGroup : lTxAuthGroup) {
				OccursList occursList = new OccursList();

				occursList.putParam("OAuthNo", tTxAuthGroup.getAuthNo());
				occursList.putParam("OAuthItem", tTxAuthGroup.getAuthItem());
				occursList.putParam("ODesc", tTxAuthGroup.getDesc());
				occursList.putParam("OBranchNo", tTxAuthGroup.getBranchNo());
				occursList.putParam("OBranchItem", getBranchItem(tTxAuthGroup.getBranchNo().trim(), titaVo));
				occursList.putParam("OLevelFg", tTxAuthGroup.getLevelFg());

				
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAuthGroup != null && slTxAuthGroup.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getBranchItem(String branchNo, TitaVo titaVo) {
		this.info("L6043.getBranchItem=" + branchNo);

		String branchItem = "";

		if ("".equals(branchNo)) {
			return branchItem;
		}

		if (branchItems.size() > 0) {
			if (branchItems.get(branchNo) != null) {
				branchItem = branchItems.get(branchNo).toString();
			}

		}

		if ("".equals(branchItem)) {
			CdBranch cdBranch = cdBranchService.findById(branchNo, titaVo);
			if (cdBranch == null) {
				branchItem = branchNo;
			} else {
				branchItem = cdBranch.getBranchItem();
				branchItems.put(branchNo, branchItem);
			}
		}

		return branchItem;
	}
}