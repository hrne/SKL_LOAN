package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.springjpa.cm.L6041ServiceImpl;
import com.st1.itx.db.domain.CdBranch;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.TxTellerAuthService;

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
	TxTellerAuthService sTxTellerAuthService;
	
	@Autowired
	L6041ServiceImpl iL6041ServiceImpl;

	@Autowired
	Parse parse;

	boolean first = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6041 ");
		this.totaVo.init(titaVo);
		String iGroupNoS = "";
		int iLevelFgS = 0;
		String iTlrNo = titaVo.getParam("TlrNo");
		String iBrNo = titaVo.getParam("BrNo");
		
		if(titaVo.getParam("GroupNo").equals("")) {
			iGroupNoS = "";
		}else {
			iGroupNoS = titaVo.getParam("GroupNo");
		}
		if(titaVo.getParam("LevelFg").equals("0")) {
			 iLevelFgS = 0;
		}else {
		     iLevelFgS = Integer.parseInt(titaVo.getParam("LevelFg"));
		}
		String iAuthNo = titaVo.getParam("AuthNo");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();
		

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
//		Slice<TxTeller> slTxTeller = null;	
//		if (iBrNo.isEmpty()) {
//			slTxTeller = txTellerService.findByTlrNo(iTlrNo + "%", this.index, this.limit, titaVo);
//		} else if (!iTlrNo.isEmpty()) {
//			slTxTeller = txTellerService.findByL6041(iBrNo, iTlrNo + "%", this.index, this.limit, titaVo);
//		} else {
//			slTxTeller = txTellerService.findByGroupNo(iBrNo, iGroupNoS, iGroupNoE, iLevelFgS, iLevelFgE, this.index,
//					this.limit, titaVo);
//		}
//		List<TxTeller> lTxTeller = slTxTeller == null ? null : slTxTeller.getContent();
		
		List<Map<String, String>> sL6041ServiceImpl =null;
		sL6041ServiceImpl = iL6041ServiceImpl.findByTrol(iAuthNo, iTlrNo, iBrNo , iGroupNoS , iLevelFgS ,0, Integer.MAX_VALUE, titaVo);

		if (sL6041ServiceImpl == null) {
			throw new LogicException("E0001", "");
		} else {
			CdBranch cdBranch = null;
			CdBranchGroup cdBranchGroup = null;
			for (Map<String, String> t : sL6041ServiceImpl) {

				if (first) {
					cdBranch = cdBranchService.findById(t.get("F1"), titaVo);
					first = false;
				}

				CdEmp cdEmp = cdEmpService.findById(t.get("F0"), titaVo);
				if (cdEmp == null) {
					continue;
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OTlrNo", t.get("F0"));
				occursList.putParam("OTlrItem", cdEmp.getFullname());
				occursList.putParam("OBrNo", t.get("F1"));
				occursList.putParam("OBrItem", cdBranch.getBranchItem());
				occursList.putParam("OGroupNo", t.get("F2"));
				String DateTime = this.parse.stringToStringDateTime(t.get("F3"));

				occursList.putParam("OLastUpdate", DateTime);

				String iEmpNo = t.get("F5");
				if (!iEmpNo.isEmpty() || iEmpNo.length() > 0) {
					CdEmp tCdEmp = cdEmpService.findById(iEmpNo, titaVo);
					if (tCdEmp != null) {
						iEmpNo = iEmpNo + " " + tCdEmp.getFullname();
					}
				}
				occursList.putParam("OLastUpdateEmpNo", iEmpNo);
				this.info("F2   = " +t.get("F2") );
				this.info("BranchNo" + cdBranch.getBranchNo());
				cdBranchGroup = cdBranchGroupService.findById(new CdBranchGroupId(cdBranch.getBranchNo(), t.get("F2")),
						titaVo);

				if (cdBranchGroup != null) {
					occursList.putParam("OGroupItem", cdBranchGroup.getGroupItem());

				} else {
					occursList.putParam("OGroupItem", "");
				}
				
				//若有歷程就顯示，無則不顯示
				Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByTranNo("L6401", "CODE:" +  t.get("F0"), 0,
						1, titaVo);
				List<TxDataLog> lTxDataLog = slTxDataLog != null ? slTxDataLog.getContent() : null;
				occursList.putParam("OHasHistory", lTxDataLog != null && !lTxDataLog.isEmpty() ? "Y" : "N");
				if(sL6041ServiceImpl.size() ==1) {
					occursList.putParam("OPermitory", "Y");
				}else {
					occursList.putParam("OPermitory", "N");
				}
//				occursList.putParam("OAuthNo", tTxTeller.getAuthNo());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}