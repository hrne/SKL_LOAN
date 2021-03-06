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
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;

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

	@Autowired
	CdEmpService cdEmpService;
	
	@Autowired
	TxDataLogService sTxDataLogService;

	@Autowired
	Parse parse;

	boolean first = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6041 ");
		this.totaVo.init(titaVo);

		String iTlrNo = titaVo.getParam("TlrNo");
		String iBrNo = titaVo.getParam("BrNo");
		String iGroupNoS = titaVo.getParam("GroupNo");
		String iGroupNoE = "";
		if (!iGroupNoS.isEmpty()) {
			iGroupNoE = titaVo.getParam("GroupNo");
		} else {
			iGroupNoS = " ";
			iGroupNoE = "Z";
		}
		int iLevelFgS = Integer.parseInt(titaVo.getParam("LevelFg"));
		int iLevelFgE = Integer.parseInt(titaVo.getParam("LevelFg"));
		if (iLevelFgS == 0) {
			iLevelFgS = 1;
			iLevelFgE = 3;
		}

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		Slice<TxTeller> slTxTeller = null;

		if (iBrNo.isEmpty()) {
			slTxTeller = txTellerService.findByTlrNo(iTlrNo + "%", this.index, this.limit, titaVo);
		} else if (!iTlrNo.isEmpty()) {
			slTxTeller = txTellerService.findByL6041(iBrNo, iTlrNo + "%", this.index, this.limit, titaVo);
		} else {
			slTxTeller = txTellerService.findByGroupNo(iBrNo, iGroupNoS, iGroupNoE, iLevelFgS, iLevelFgE, this.index,
					this.limit, titaVo);
		}

		List<TxTeller> lTxTeller = slTxTeller == null ? null : slTxTeller.getContent();

		if (lTxTeller == null) {
			throw new LogicException("E0001", "");
		} else {
			CdBranch cdBranch = null;
			CdBranchGroup cdBranchGroup = null;
			for (TxTeller tTxTeller : lTxTeller) {
				if (first) {
					cdBranch = cdBranchService.findById(tTxTeller.getBrNo(), titaVo);
					first = false;
				}

				CdEmp cdEmp = cdEmpService.findById(tTxTeller.getTlrNo(), titaVo);
				if (cdEmp == null) {
					continue;
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OTlrNo", tTxTeller.getTlrNo());
				occursList.putParam("OTlrItem", cdEmp.getFullname());
				occursList.putParam("OBrNo", tTxTeller.getBrNo());
				occursList.putParam("OBrItem", cdBranch.getBranchItem());
				occursList.putParam("OGroupNo", tTxTeller.getGroupNo());
				String DateTime = this.parse.timeStampToString(tTxTeller.getMntDate());

				occursList.putParam("OLastUpdate", DateTime);

				String iEmpNo = tTxTeller.getMntEmpNo();
				if (!iEmpNo.isEmpty() || iEmpNo.length() > 0) {
					CdEmp tCdEmp = cdEmpService.findById(iEmpNo, titaVo);
					if (tCdEmp != null) {
						iEmpNo = iEmpNo + " " + tCdEmp.getFullname();
					}
				}
				occursList.putParam("OLastUpdateEmpNo", iEmpNo);

				cdBranchGroup = cdBranchGroupService.findById(new CdBranchGroupId(cdBranch.getBranchNo(), tTxTeller.getGroupNo()),
						titaVo);

				if (cdBranchGroup != null) {
					occursList.putParam("OGroupItem", cdBranchGroup.getGroupItem());

				} else {
					occursList.putParam("OGroupItem", "");
				}
				
				//若有歷程就顯示，無則不顯示
				Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByTranNo("L6401", "CODE:" +  tTxTeller.getTlrNo(), 0,
						1, titaVo);
				List<TxDataLog> lTxDataLog = slTxDataLog != null ? slTxDataLog.getContent() : null;
				occursList.putParam("OHasHistory", lTxDataLog != null && !lTxDataLog.isEmpty() ? "Y" : "N");
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