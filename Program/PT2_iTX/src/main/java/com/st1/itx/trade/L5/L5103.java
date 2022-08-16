package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5103")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5103 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnDocRecordService innDocRecordService;
	@Autowired
	public FacCloseService facCloseService;

	@Autowired
	public TxTellerService txTellerService;

	private InnDocRecord tInnDocRecord = new InnDocRecord();
	private InnDocRecordId tInnDocRecordId = new InnDocRecordId();
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5103 ");
		this.totaVo.init(titaVo);

		String cfBrNo = titaVo.getParam("L5103ConfirmBrNo");
		String cfGroupNo = titaVo.getParam("L5103ConfirmGroupNo");

		int rdate = parse.stringToInteger(titaVo.getParam("ReturnDate"));
		String rEmpNo = titaVo.getParam("ReturnEmpNo");
		String applCode = titaVo.getParam("ApplCode");
		String usageCode = titaVo.getParam("UsageCode");

		TxCom txCom = this.txBuffer.getTxCom();
		txCom.setConfirmBrNo(cfBrNo);
		this.info("cfGroupNo==" + cfGroupNo);
		txCom.setConfirmGroupNo(cfGroupNo);
		this.txBuffer.setTxCom(txCom);

//		1.登 2.放 3.審 4.審放
		switch (titaVo.getActFgI()) {
		case 1:
			String sApplEmpNo = titaVo.getParam("ApplEmpNo");
			String sKeeperEmpNo = titaVo.getParam("KeeperEmpNo");

			TxTeller t1txTeller = txTellerService.findById(sApplEmpNo, titaVo);
			TxTeller t2txTeller = txTellerService.findById(sKeeperEmpNo, titaVo);

//			若保管與借閱同單位改為2段式交易
			if (t1txTeller != null && t2txTeller != null) {
				if (t1txTeller.getGroupNo().equals(t2txTeller.getGroupNo())) {
					titaVo.put("RELCD", "2");
				}
			}
			// 登錄
			if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
				insertNormal(titaVo);
			}
			// 登錄 修正
			if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
				updateModify(titaVo);
			}

			// 登錄/歸還 訂正
			if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
				updateErase(titaVo);
			}

			break;
		case 2:
			// 放行及放行訂正
			if (titaVo.isActfgSuprele()) {
				// 一般放行
				if (titaVo.isHcodeNormal()) {
					tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
					tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
					tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

					tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

					if (tInnDocRecord != null) {
						tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
						if (rdate > 0) {
							rdate = rdate + 19110000;
							tInnDocRecord.setApplCode("2");
							tInnDocRecord.setReturnDate(rdate);
							tInnDocRecord.setReturnEmpNo(rEmpNo);
						}
						try {
							innDocRecordService.update(tInnDocRecord);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007",
									"L5103 isHcodeNormal 2 update " + e.getErrorMsg());
						}
//						清償歸檔需更新清償作業檔的銷號欄
						if ("2".equals(applCode) && "01".equals(usageCode)) {
							FacClose mFacClose = facCloseService.findFacmNoMaxCloseNoFirst(
									parse.stringToInteger(titaVo.getParam("CustNo")),
									parse.stringToInteger(titaVo.getParam("FacmNo")), titaVo);
							if (mFacClose != null) {
								FacClose tFacClose = facCloseService.holdById(mFacClose, titaVo);

								int wkYy = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(0, 3)); // 年
								int wkMm = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(3, 5)); // 月
								int wkDd = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(5, 7)); // 日
								String clsDate = wkYy + "/" + wkMm + "/" + wkDd;
								tFacClose.setClsNo(clsDate);
								try {
									facCloseService.update(tFacClose, titaVo);
								} catch (DBException e) {
									throw new LogicException(titaVo, "E0007",
											"L5103 isHcodeNormal 2 update " + e.getErrorMsg());
								}
							}
						}

					} else {
						throw new LogicException(titaVo, "E0003", "L5103 isHcodeNormal 2");
					}
				}
				// 放行訂正
				if (titaVo.isHcodeErase()) {

					tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
					tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
					tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

					tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

					if (tInnDocRecord != null) {
						tInnDocRecord.setTitaActFg("1");

						try {
							innDocRecordService.update(tInnDocRecord);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "L5103 isHcodeErase 2 update " + e.getErrorMsg());
						}
					} else {
						throw new LogicException(titaVo, "E0003", "L5103 isHcodeErase 2");
					}
				}
			}

			break;
		case 3:

			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

			tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

			if (tInnDocRecord == null) {
				throw new LogicException(titaVo, "E0003", "L5103 isHcodeErase 3");
			}

			// 審閱訂正
			if (titaVo.isHcodeErase()) {

				tInnDocRecord.setTitaActFg("2");
				if (tInnDocRecord.getReturnDate() > 0) {
					tInnDocRecord.setApplCode("1");
					tInnDocRecord.setReturnDate(0);
					tInnDocRecord.setReturnEmpNo("");
				}
				try {
					innDocRecordService.update(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 isHcodeErase 3 update " + e.getErrorMsg());
				}

			} else {// 一般審閱

				if ("3".equals(tInnDocRecord.getTitaActFg())) {
					throw new LogicException(titaVo, "E0005", "待放行");
				}

				tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
				if (rdate > 0) {
					rdate = rdate + 19110000;
					tInnDocRecord.setApplCode("2");
					tInnDocRecord.setReturnDate(rdate);
					tInnDocRecord.setReturnEmpNo(rEmpNo);
				}
				try {
					innDocRecordService.update(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 isHcodeErase 3 update " + e.getErrorMsg());
				}

			}

			break;
		case 4:
			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

			tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

			if (tInnDocRecord == null) {
				throw new LogicException(titaVo, "E0001", "L5103 isHcodeErase 4 ");
			}
			// 審閱放行訂正
			if (titaVo.isHcodeErase()) {

				tInnDocRecord.setTitaActFg("3");
				try {
					innDocRecordService.update(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 InnDocRecord update " + e.getErrorMsg());
				}

			} else {

				tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
				try {
					innDocRecordService.update(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 InnDocRecord update " + e.getErrorMsg());
				}

			}

			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	public void insertNormal(TitaVo titaVo) throws LogicException {

		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		InnDocRecord nInnDocRecord = innDocRecordService.findById(tInnDocRecordId, titaVo);

		if (nInnDocRecord != null) {
			if (("1").equals(nInnDocRecord.getTitaActFg())) {
				throw new LogicException(titaVo, "E0005", "待放行");
			}

			nInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
			nInnDocRecord.setApplCode(titaVo.getParam("ApplCode"));
			nInnDocRecord.setApplEmpNo(titaVo.getParam("ApplEmpNo"));
			nInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
			nInnDocRecord.setUsageCode(titaVo.getParam("UsageCode"));
			nInnDocRecord.setCopyCode(titaVo.getParam("CopyCode"));
			nInnDocRecord.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
			nInnDocRecord.setReturnDate(parse.stringToInteger(titaVo.getParam("ReturnDate")));
			nInnDocRecord.setReturnEmpNo(titaVo.getParam("ReturnEmpNo"));
			nInnDocRecord.setRemark(titaVo.getParam("Remark"));
			nInnDocRecord.setApplObj(titaVo.getParam("ApplObj"));

			try {
				innDocRecordService.update(nInnDocRecord);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L5103 InnDocRecord insert " + e.getErrorMsg());
			}

		} else {
			tInnDocRecord.setInnDocRecordId(tInnDocRecordId);

			tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
			tInnDocRecord.setApplCode(titaVo.getParam("ApplCode"));
			tInnDocRecord.setApplEmpNo(titaVo.getParam("ApplEmpNo"));
			tInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
			tInnDocRecord.setUsageCode(titaVo.getParam("UsageCode"));
			tInnDocRecord.setCopyCode(titaVo.getParam("CopyCode"));
			tInnDocRecord.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
			tInnDocRecord.setReturnDate(parse.stringToInteger(titaVo.getParam("ReturnDate")));
			tInnDocRecord.setReturnEmpNo(titaVo.getParam("ReturnEmpNo"));
			tInnDocRecord.setRemark(titaVo.getParam("Remark"));
			tInnDocRecord.setApplObj(titaVo.getParam("ApplObj"));

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTA" + i)) != 0) {
					tTempVo.putParam("OPTA" + i, titaVo.getParam("OPTA" + i));
					tTempVo.putParam("AMTA" + i, titaVo.getParam("AMTA" + i));
				}
			}

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTB" + i)) != 0) {
					tTempVo.putParam("OPTB" + i, titaVo.getParam("OPTB" + i));
					tTempVo.putParam("AMTB" + i, titaVo.getParam("AMTB" + i));
				}
			}

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTC" + i)) != 0) {
					tTempVo.putParam("OPTC" + i, titaVo.getParam("OPTC" + i));
					tTempVo.putParam("AMTC" + i, titaVo.getParam("AMTC" + i));
				}
			}
			tInnDocRecord.setJsonFields(tTempVo.getJsonString());

			try {
				innDocRecordService.insert(tInnDocRecord, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L5103 InnDocRecord insert " + e.getErrorMsg());
			}
		}

	}

	public void updateModify(TitaVo titaVo) throws LogicException {
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

		if (tInnDocRecord != null) {
			tInnDocRecord.setInnDocRecordId(tInnDocRecordId);

			tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
			tInnDocRecord.setApplCode(titaVo.getParam("ApplCode"));
			tInnDocRecord.setApplEmpNo(titaVo.getParam("ApplEmpNo"));
			tInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
			tInnDocRecord.setUsageCode(titaVo.getParam("UsageCode"));
			tInnDocRecord.setCopyCode(titaVo.getParam("CopyCode"));
			tInnDocRecord.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
			tInnDocRecord.setReturnDate(parse.stringToInteger(titaVo.getParam("ReturnDate")));
			tInnDocRecord.setReturnEmpNo(titaVo.getParam("ReturnEmpNo"));
			tInnDocRecord.setRemark(titaVo.getParam("Remark"));
			tInnDocRecord.setApplObj(titaVo.getParam("ApplObj"));

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTA" + i)) != 0) {
					tTempVo.putParam("OPTA" + i, titaVo.getParam("OPTA" + i));
					tTempVo.putParam("AMTA" + i, titaVo.getParam("AMTA" + i));
				}
			}

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTB" + i)) != 0) {
					tTempVo.putParam("OPTB" + i, titaVo.getParam("OPTB" + i));
					tTempVo.putParam("AMTB" + i, titaVo.getParam("AMTB" + i));
				}
			}

			for (int i = 1; i <= 25; i++) {
				if (Integer.parseInt(titaVo.getParam("OPTC" + i)) != 0) {
					tTempVo.putParam("OPTC" + i, titaVo.getParam("OPTC" + i));
					tTempVo.putParam("AMTC" + i, titaVo.getParam("AMTC" + i));
				}
			}
			tInnDocRecord.setJsonFields(tTempVo.getJsonString());

			try {
				innDocRecordService.update(tInnDocRecord, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L5103 updateModify " + e.getErrorMsg());
			}
		} else {
			throw new LogicException(titaVo, "E0003", "");// 修改資料不存在
		}
	}

	public void updateErase(TitaVo titaVo) throws LogicException {
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

		if (tInnDocRecord != null) {
			if (("1").equals(tInnDocRecord.getApplCode())) {// 申請登錄時才可刪除
				try {
					innDocRecordService.delete(tInnDocRecord, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0004", "L5103 deleteErase " + e.getErrorMsg());
				}

			} else {// 登錄歸還恢復至未點選歸還時
				tInnDocRecord.setInnDocRecordId(tInnDocRecordId);
				tInnDocRecord.setTitaActFg("4");
				tInnDocRecord.setApplCode("1");
				tInnDocRecord.setReturnDate(0);
				tInnDocRecord.setReturnEmpNo("");
				try {
					innDocRecordService.update(tInnDocRecord, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 updateModify " + e.getErrorMsg());
				}
			}

		} else {
			throw new LogicException(titaVo, "E0004", "");// 刪除資料不存在
		}

	}
}