package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

@Service("L5108")
@Scope("prototype")
/**
 * 
 * 
 * @author ST1
 * @version 1.0.0
 */
public class L5108 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnDocRecordService innDocRecordService;

	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	public DataLog dataLog;

	@Autowired
	CdCodeService sCdCodeService;

	private InnDocRecord tInnDocRecord = new InnDocRecord();
	private InnDocRecordId tInnDocRecordId = new InnDocRecordId();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// 檔案借閱內容維護-限管理人
		this.info("active L5108 ");
		this.totaVo.init(titaVo);

		String iteller = titaVo.getParam("TLRNO");
		
		CdCode tCdCode = sCdCodeService.getItemFirst(5, "InnDocKeeper", iteller, titaVo);
		if (tCdCode == null) {
			throw new LogicException(titaVo, "E0015", "非檔案借閱管理人，不可維護本交易");
		}
		if("N".equals(tCdCode.getEnable())) {
			throw new LogicException(titaVo, "E0015", "該檔案借閱管理人已停用，不可維護本交易");
		}
		
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		InnDocRecord nInnDocRecord = innDocRecordService.findById(tInnDocRecordId, titaVo);
		if (nInnDocRecord == null) {
			throw new LogicException(titaVo, "E0003", "檔案借閱檔");
		}
		InnDocRecord beforeInnDocRecord = (InnDocRecord) dataLog.clone(nInnDocRecord);

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId, titaVo);
		if (tInnDocRecord == null) {
			throw new LogicException(titaVo, "E0003", "檔案借閱檔");
		}

		tInnDocRecord.setFacmNoMemo(titaVo.getParam("FacmNoMemo"));
		tInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
		tInnDocRecord.setRemark(titaVo.getParam("Remark"));

		try {
			innDocRecordService.update(tInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "InnDocRecord " + e.getErrorMsg());
		}
		dataLog.setEnv(titaVo, beforeInnDocRecord, tInnDocRecord);
		dataLog.exec("修改檔案借閱檔");

		this.addList(this.totaVo);
		return this.sendList();

	}

}