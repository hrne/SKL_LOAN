package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * ApplCode=9,1<br>
 * ApplSeq=9,3<br>
 * ApplEmpNo=X,6<br>
 * KeeperEmpNo=X,6<br>
 * UsageCode=9,2<br>
 * CopyCode=9,1<br>
 * ApplDate=9,7<br>
 * ReturnDate=9,7<br>
 * ReturnEmpNo=X,6<br>
 * Remark=X,60<br>
 * ApplObj=9,1<br>
 * END=X,1<br>
 */

@Service("L5103")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5103 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5103.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnDocRecordService innDocRecordService;

	@Autowired
	public CdEmpService cdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5103 ");
		this.totaVo.init(titaVo);

//		#L5103ConfirmBrNo
//		#L5103ConfirmGroupNo

		String cfBrNo = titaVo.getParam("L5103ConfirmBrNo");
		String cfGroupNo = titaVo.getParam("L5103ConfirmGroupNo");

		int rdate = parse.stringToInteger(titaVo.getParam("ReturnDate"));
		String rEmpNo = titaVo.getParam("ReturnEmpNo");

		TxCom txCom = this.txBuffer.getTxCom();
		txCom.setConfirmBrNo(cfBrNo);
		txCom.setConfirmGroupNo(cfGroupNo);
		this.txBuffer.setTxCom(txCom);

		InnDocRecord tInnDocRecord = new InnDocRecord();
		InnDocRecordId tInnDocRecordId = new InnDocRecordId();

//		1.登 2.放 3.審 4.審放
		switch (titaVo.getActFgI()) {
		case 1:
			String sApplEmpNo = titaVo.getParam("ApplEmpNo");
			String sKeeperEmpNo = titaVo.getParam("KeeperEmpNo");

			CdEmp t1cdEmp = cdEmpService.findById(sApplEmpNo, titaVo);
			CdEmp t2cdEmp = cdEmpService.findById(sKeeperEmpNo, titaVo);

//			若保管與借閱同單位改為2段式交易
			if (t1cdEmp != null && t2cdEmp != null) {
				if (t1cdEmp.getCenterCode().equals(t2cdEmp.getCenterCode())) {
					titaVo.put("RELCD", "2");
				}
			}
			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

			InnDocRecord nInnDocRecord = innDocRecordService.findById(tInnDocRecordId, titaVo);

			if (nInnDocRecord != null) {
				if ("1".equals(tInnDocRecord.getTitaActFg())) {
					throw new LogicException(titaVo, "E0005", "待放行");
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

				try {
					innDocRecordService.insert(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L5103 InnDocRecord insert " + e.getErrorMsg());
				}
			}
			break;
		case 2:
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
					throw new LogicException(titaVo, "E0007", "L5103 InnDocRecord update " + e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0001", "L5103 InnDocRecord 無此資料 ");
			}
			break;
		case 3:
			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

			tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

			if (tInnDocRecord != null) {
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
					throw new LogicException(titaVo, "E0007", "L5103 InnDocRecord update " + e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0001", "L5103 InnDocRecord 無此資料 ");
			}
			break;
		case 4:
			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

			tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId);

			if (tInnDocRecord != null) {
				tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
				try {
					innDocRecordService.update(tInnDocRecord);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L5103 InnDocRecord update " + e.getErrorMsg());
				}
			} else {
				throw new LogicException(titaVo, "E0001", "L5103 InnDocRecord 無此資料 ");
			}
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}