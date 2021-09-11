package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L440A")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L440A extends TradeBuffer {
	@Autowired
	DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public BankAuthActService bankAuthActService;
	
	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L440A ");
		this.totaVo.init(titaVo);
		txAmlCom.setTxBuffer(this.txBuffer);
		txToDoCom.setTxBuffer(this.txBuffer);

//		T(6A,#OOAuthCreateDate+#OOCustNo+#OORepayBank+#OORepayAcct+#OOCreateFlag+#CreateFlag)
//		AuthCreateDate,CustNo,RepayBank,RepayAcct,CreateFlag

		int funcCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		// 1.新增授權 2.再次授權 3.取消授權
		int createFlag = parse.stringToInteger(titaVo.getParam("CreateFlag"));
		int custNo = parse.stringToInteger(titaVo.getParam("OOCustNo"));
		String repayBank = titaVo.getParam("OORepayBank");
		String repayAcct = titaVo.getParam("OORepayAcct");

		this.info("funcCode : " + funcCode);
		this.info("createFlag : " + createFlag);
		this.info("OOAuthCreateDate : " + parse.stringToInteger(titaVo.getParam("OOAuthCreateDate")));
		this.info("OOrepayBank : " + repayBank);
		this.info("OOrepayAcct : " + repayAcct);
		this.info("OOcreateFlag : " + titaVo.getParam("OOCreateFlag"));

		AchAuthLogId tAchAuthLogId = new AchAuthLogId();
		tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("OOAuthCreateDate")));
		tAchAuthLogId.setCustNo(custNo);
		tAchAuthLogId.setRepayBank(repayBank);
		tAchAuthLogId.setRepayAcct(repayAcct);
		tAchAuthLogId.setCreateFlag(titaVo.getParam("OOCreateFlag"));

		AchAuthLog tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();

// // 1.新增授權 2.再次授權 3.取消授權
//		createFlag1 新增->更新
//		createFlag2 再次->新增
//		createFlag3 取消->新增一筆取消
		switch (createFlag) {
		case 1:
			tAchAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			checkAmlVo = txAmlCom.authAch(tAchAuthLog, titaVo);
			if (!"0".equals(checkAmlVo.getConfirmStatus())) {
				throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
			}
			tAchAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());
			try {
				achAuthLogService.update(tAchAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L440A AchAuthLog update " + e.getErrorMsg());
			}

			break;
		case 2:
			AchAuthLog t1AchAuthLog = new AchAuthLog();
			tAchAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			tAchAuthLogId.setCustNo(tAchAuthLog.getCustNo());
			tAchAuthLogId.setRepayBank(tAchAuthLog.getRepayBank());
			tAchAuthLogId.setRepayAcct(tAchAuthLog.getRepayAcct());
			tAchAuthLogId.setCreateFlag(tAchAuthLog.getCreateFlag());
			t1AchAuthLog.setAchAuthLogId(tAchAuthLogId);
			t1AchAuthLog.setFacmNo(tAchAuthLog.getFacmNo());
			t1AchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
			t1AchAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			t1AchAuthLog.setAuthMeth(tAchAuthLog.getAuthMeth());
			t1AchAuthLog.setLimitAmt(tAchAuthLog.getLimitAmt());
			t1AchAuthLog.setRelationCode(tAchAuthLog.getRelationCode());
			t1AchAuthLog.setRelAcctName(tAchAuthLog.getRelAcctName());
			t1AchAuthLog.setRelAcctBirthday(tAchAuthLog.getRelAcctBirthday());
			t1AchAuthLog.setRelAcctGender(tAchAuthLog.getRelAcctGender());
			t1AchAuthLog.setMediaCode("");
			t1AchAuthLog.setAuthStatus(""); // 再次授權
			checkAmlVo = txAmlCom.authAch(t1AchAuthLog, titaVo);
			if (!"0".equals(checkAmlVo.getConfirmStatus())) {
				throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
			}
			t1AchAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());
			try {
				achAuthLogService.insert(t1AchAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "授權資料同日不可重複 ");
			}
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("ACHP00");
			tTxToDoDetail.setCustNo(tAchAuthLog.getCustNo());
			tTxToDoDetail.setFacmNo(tAchAuthLog.getFacmNo());
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(tAchAuthLog.getRepayAcct(), 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);
			break;

		case 3:
			AchAuthLog t2AchAuthLog = new AchAuthLog();
			tAchAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			tAchAuthLogId.setCustNo(tAchAuthLog.getCustNo());
			tAchAuthLogId.setRepayBank(tAchAuthLog.getRepayBank());
			tAchAuthLogId.setRepayAcct(tAchAuthLog.getRepayAcct());
			tAchAuthLogId.setCreateFlag("D");
			t2AchAuthLog.setAchAuthLogId(tAchAuthLogId);
			t2AchAuthLog.setFacmNo(tAchAuthLog.getFacmNo());
			t2AchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
			t2AchAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			t2AchAuthLog.setAuthMeth(tAchAuthLog.getAuthMeth());
			t2AchAuthLog.setLimitAmt(tAchAuthLog.getLimitAmt());
			t2AchAuthLog.setAuthStatus(" ");
			t2AchAuthLog.setRelationCode(tAchAuthLog.getRelationCode());
			t2AchAuthLog.setRelAcctName(tAchAuthLog.getRelAcctName());
			t2AchAuthLog.setRelAcctBirthday(tAchAuthLog.getRelAcctBirthday());
			t2AchAuthLog.setRelAcctGender(tAchAuthLog.getRelAcctGender());
			t2AchAuthLog.setAmlRsp(tAchAuthLog.getAmlRsp());
			try {
				achAuthLogService.insert(t2AchAuthLog, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "授權資料同日不可重複 ");
			}
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("ACHP00");
			tTxToDoDetail.setCustNo(tAchAuthLog.getCustNo());
			tTxToDoDetail.setFacmNo(tAchAuthLog.getFacmNo());
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(tAchAuthLog.getRepayAcct(), 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);
		break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}