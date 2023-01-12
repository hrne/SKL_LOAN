package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.parse.Parse;

@Service("L4412")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4412 extends TradeBuffer {

	@Autowired
	public BankAuthActCom bankAuthActCom;

	@Autowired
	CdEmpService sCdEmpService;

	@Autowired
	BankAuthActService sBankAuthActService;
	@Autowired
	PostAuthLogService sPostAuthLogService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4412 ");
		this.totaVo.init(titaVo);
		bankAuthActCom.setTxBuffer(this.getTxBuffer());

		// tita
		String iFunCode = titaVo.getParam("FuncCode");
		String iAuthApplCode = titaVo.getParam("AuthApplCode");

		this.info("iFunCode = " + iFunCode);
		this.info("iAuthApplCode = " + iAuthApplCode);

		CdEmp cdEmp = sCdEmpService.findById(titaVo.getTlrNo(), titaVo);

		// FunCode 1 新增
		if ("1".equals(iFunCode)) {
			if ("1".equals(iAuthApplCode)) {

				bankAuthActCom.add("A", titaVo);
			} else {
				bankAuthActCom.add("D", titaVo);
			}

			this.totaVo.putParam("CreateEmpNo", titaVo.getTlrNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("CreateDate", titaVo.getCalDy());
			this.totaVo.putParam("LastUpdateEmpNo", titaVo.getTlrNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("LastUpdate", titaVo.getCalDy());

			// FunCode 2 修改
		} else if ("2".equals(iFunCode)) {

			if ("".equals(titaVo.getParam("AuthErrorCode").trim())) {
				bankAuthActCom.update(titaVo);
			} else {
				if ("1".equals(iAuthApplCode)) {
					bankAuthActCom.mntPostAuth("0", titaVo);
				} else {
					bankAuthActCom.mntPostAuth("1", titaVo);
				}
			}

			PostAuthLogId tPostAuthLogId = new PostAuthLogId();
			tPostAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("AuthCreateDate")));
			tPostAuthLogId.setAuthApplCode("1");
			tPostAuthLogId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tPostAuthLogId.setPostDepCode(titaVo.getParam("PostDepCode"));
			tPostAuthLogId.setRepayAcct(titaVo.getParam("RepayAcct"));
			tPostAuthLogId.setAuthCode(titaVo.getParam("AuthCode"));
			PostAuthLog tPostAuthLog = sPostAuthLogService.findById(tPostAuthLogId, titaVo);

			if (tPostAuthLog == null) {
				throw new LogicException("E0003", "郵局授權記錄檔");
			}

			cdEmp = sCdEmpService.findById(tPostAuthLog.getCreateEmpNo(), titaVo);

			if (cdEmp == null)
				throw new LogicException("E0001", "員工資料檔 " + tPostAuthLog.getCreateEmpNo());

			this.totaVo.putParam("CreateEmpNo", tPostAuthLog.getCreateEmpNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("CreateDate",
					parse.timeStampToStringDate(tPostAuthLog.getCreateDate()).replace("/", ""));

			cdEmp = sCdEmpService.findById(tPostAuthLog.getLastUpdateEmpNo(), titaVo);

			if (cdEmp == null)
				throw new LogicException("E0001", "員工資料檔 " + tPostAuthLog.getLastUpdateEmpNo());

			this.totaVo.putParam("LastUpdateEmpNo", tPostAuthLog.getLastUpdateEmpNo() + " " + cdEmp.getFullname());
			this.totaVo.putParam("LastUpdate",
					parse.timeStampToStringDate(tPostAuthLog.getLastUpdate()).replace("/", ""));

			// FunCode 4 刪除
		} else if ("4".equals(iFunCode)) {
			if (iAuthApplCode.equals("2")) {
				bankAuthActCom.del("D", titaVo);
			} else {
				bankAuthActCom.del("A", titaVo);
			}

			// 原樣奉還

			this.totaVo.putParam("CreateEmpNo", titaVo.getParam("CreateEmpNo"));
			this.totaVo.putParam("CreateDate", titaVo.getParam("CreateDate"));
			this.totaVo.putParam("LastUpdateEmpNo", titaVo.getParam("LastUpdateEmpNo"));
			this.totaVo.putParam("LastUpdate", titaVo.getParam("LastUpdate"));

		} else {

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}