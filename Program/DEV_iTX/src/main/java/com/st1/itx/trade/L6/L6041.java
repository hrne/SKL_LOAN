package com.st1.itx.trade.L6;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;

@Service("L6041")
@Scope("prototype")
/**
 * 使用者資料查詢
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6041 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	CdBranchService cdBranchService;

	@Autowired
	CdBranchGroupService cdBranchGroupService;
	
	boolean first = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6041 ");
		this.totaVo.init(titaVo);

		String iTlrNo = titaVo.getParam("TlrNo");
		String iBrNo = titaVo.getParam("BrNo");

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TxTeller> slTxTeller = txTellerService.findByL6041(iBrNo, iTlrNo + "%", this.index, this.limit, titaVo);
		List<TxTeller> lTxTeller = slTxTeller == null ? null : slTxTeller.getContent();

		if (lTxTeller == null) {
			throw new LogicException("E0001", "");
		} else {
			CdBranch cdBranch = null;
			CdBranchGroup cdBranchGroup = null;
			for (TxTeller tTxTeller : lTxTeller) {
				if (first) {
					cdBranch = cdBranchService.findById(iBrNo, titaVo);
					first = false;
				}
				
				
				
				OccursList occursList = new OccursList();
				occursList.putParam("OTlrNo", tTxTeller.getTlrNo());
				occursList.putParam("OTlrItem", tTxTeller.getTlrItem());
				occursList.putParam("OBrNo", tTxTeller.getBrNo());
				occursList.putParam("OBrItem", cdBranch.getBranchItem());
				occursList.putParam("OGroupNo", tTxTeller.getGroupNo());
				
				cdBranchGroup = cdBranchGroupService.findById(new CdBranchGroupId(iBrNo, tTxTeller.getGroupNo()),titaVo);
				
				if(cdBranchGroup!=null) {
				occursList.putParam("OGroupItem", cdBranchGroup.getGroupItem());
					
				}
				
//				occursList.putParam("OAuthNo", tTxTeller.getAuthNo());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxTeller != null && slTxTeller.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}