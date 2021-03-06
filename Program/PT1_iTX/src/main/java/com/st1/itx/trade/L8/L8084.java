package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

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
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;
import com.st1.itx.db.service.TxAmlCreditService;

import com.st1.itx.db.domain.TxAmlNotice;
import com.st1.itx.db.service.TxAmlNoticeService;

import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdBranchGroupId;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdCodeService;

@Service("L8084")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L8084 extends TradeBuffer {

	@Autowired
	TxAmlCreditService txAmlCreditService;

	@Autowired
	TxAmlNoticeService txAmlNoticeService;
	@Autowired
	CdBranchGroupService cdBranchGroupService;

	@Autowired
	TxTellerService txTellerService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	Parse parse;

	boolean first = true;

	HashMap<String, String> tlrItems = new HashMap<String, String>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8082 ");
		this.totaVo.init(titaVo);

		int dataDt = parse.stringToInteger(titaVo.get("iDataDt")) + 19110000;
		String custKey = titaVo.get("iCustKey").trim();

		this.info("active L8082 key = " + dataDt + "/" + custKey);

		TxAmlCreditId txAmlCreditId = new TxAmlCreditId(dataDt, custKey);
		TxAmlCredit txAmlCredit = txAmlCreditService.findById(txAmlCreditId, titaVo);

		if (txAmlCredit == null) {
			throw new LogicException("E0001", dataDt + "/" + custKey);
		}

		this.totaVo.putParam("oDataDt", txAmlCredit.getDataDt());
		this.totaVo.putParam("oCustKey", txAmlCredit.getCustKey());
		this.totaVo.putParam("oReviewType", txAmlCredit.getReviewType());
		this.totaVo.putParam("oProcessType", txAmlCredit.getProcessType());

		this.totaVo.putParam("oIsStatus", txAmlCredit.getIsStatus());

		String isStatus = "";
		CdCodeId cdCodeId = new CdCodeId("AmlIsStatus", String.format("%02d", txAmlCredit.getIsStatus()));
		CdCode cdCode = cdCodeService.findById(cdCodeId, titaVo);
		if (cdCode != null) {
			isStatus = cdCode.getItem();
		}
		this.totaVo.putParam("oIsStatusX", isStatus);

		this.totaVo.putParam("oConfirmStatus", txAmlCredit.getWlfConfirmStatus());

		String ConfirmStatus = "";
		cdCodeId = new CdCodeId("AmlConfirmStatus", txAmlCredit.getWlfConfirmStatus());
		cdCode = cdCodeService.findById(cdCodeId, titaVo);
		if (cdCode != null) {
			ConfirmStatus = cdCode.getItem();
		}
		this.totaVo.putParam("oConfirmStatusX", ConfirmStatus);

		/*
		 * ?????????????????? titaVo.getReturnIndex() ???????????????0?????????????????????????????????
		 */
		this.index = titaVo.getReturnIndex();

		/* ????????????????????????????????? ??????500??? ???????????????????????? */
		this.limit = 500;

		Slice<TxAmlNotice> slTxAmlNotice = null;

		slTxAmlNotice = txAmlNoticeService.processAll(dataDt, custKey, this.index, this.limit, titaVo);

		List<TxAmlNotice> lTxAmlNotice = slTxAmlNotice == null ? null : slTxAmlNotice.getContent();

		if (lTxAmlNotice == null) {
			throw new LogicException("E0001", "");
		} else {
			CdBranchGroup cdBranchGroup = null;
			for (TxAmlNotice txAmlNotice : lTxAmlNotice) {

				if (first) {
					cdBranchGroup = cdBranchGroupService.findById(new CdBranchGroupId(txAmlNotice.getProcessBrNo(), txAmlNotice.getProcessGroupNo()), titaVo);
					first = false;
				}

				OccursList occursList = new OccursList();

				occursList.putParam("oProcessDate", txAmlNotice.getProcessDate());
				String groupNoX = "";
				if (cdBranchGroup != null) {
					groupNoX = cdBranchGroup.getGroupItem();
				}
//				if ("1".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("2".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("3".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("4".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("5".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("6".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("7".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("8".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("9".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				} else if ("A".equals(txAmlNotice.getProcessGroupNo())) {
//					groupNoX = cdBranchGroup.getGroupItem();
//				}
//				occursList.putParam("oProcessBrNo", txAmlNotice.getProcessBrNo());
				occursList.putParam("oProcessGroupNoX", groupNoX);
//				occursList.putParam("oProcessTlrNo", txAmlNotice.getProcessTlrNo());
				occursList.putParam("oProcessTlrItem", getTlrItem(txAmlNotice.getProcessTlrNo().trim(), titaVo));
				occursList.putParam("oProcessMobile", txAmlNotice.getProcessMobile());
				occursList.putParam("oProcessAddr", txAmlNotice.getProcessAddress());
				occursList.putParam("oProcessName", txAmlNotice.getProcessName());
				/* ?????????????????????Tota???OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* ????????????????????? ??????true ?????????????????????????????? ?????????????????? ???????????? ???????????????????????? */
		if (slTxAmlNotice != null && slTxAmlNotice.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// ????????????
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getTlrItem(String tlrNo, TitaVo titaVo) {
		String tlrItem = "";

		if ("".equals(tlrNo)) {
			return tlrItem;
		}

		if (tlrItems.size() > 0) {
			if (tlrItems.get(tlrNo) != null) {
				tlrItem = tlrItems.get(tlrNo).toString();
			}
		}

		if ("".equals(tlrItem)) {
			TxTeller txTeller = txTellerService.findById(tlrNo, titaVo);
			if (txTeller == null) {
				tlrItem = tlrNo;
			} else {
				tlrItem = txTeller.getTlrItem();
				tlrItems.put(tlrNo, tlrItem);
			}
		}
		return tlrItem;
	}
}