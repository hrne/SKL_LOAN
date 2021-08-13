package com.st1.itx.trade.L4;

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
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
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
	private static final Logger logger = LoggerFactory.getLogger(L440A.class);
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L440A ");
		this.totaVo.init(titaVo);

//		T(6A,#OOAuthCreateDate+#OOCustNo+#OORepayBank+#OORepayAcct+#OOCreateFlag+#CreateFlag)
//		AuthCreateDate,CustNo,RepayBank,RepayAcct,CreateFlag

		int funcCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
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

		AchAuthLog tAchAuthLog = new AchAuthLog();
		AchAuthLogId tAchAuthLogId = new AchAuthLogId();
		tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("OOAuthCreateDate")));
		tAchAuthLogId.setCustNo(custNo);
		tAchAuthLogId.setRepayBank(repayBank);
		tAchAuthLogId.setRepayAcct(repayAcct);
		tAchAuthLogId.setCreateFlag(titaVo.getParam("OOCreateFlag"));

		tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);

//		Aml
		txAmlCom.setTxBuffer(this.txBuffer);
		CheckAmlVo checkAmlVo = txAmlCom.authAch(tAchAuthLog, titaVo);

		tAchAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());

		if (funcCode == 3) {
			tAchAuthLog.setPropDate(0);
			tAchAuthLog.setProcessDate(0);
			tAchAuthLog.setMediaCode("");
			try {
				achAuthLogService.update(tAchAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L440A AchAuthLog update " + e.getErrorMsg());
			}

			if (!"0".equals(checkAmlVo.getConfirmStatus())) {
				throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
			}
		} else {

//		createFlag1 新增->更新
//		createFlag2 再次->新增
//		createFlag3 取消->新增一筆取消
			if (createFlag == 1) {
				tAchAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
				try {
					achAuthLogService.update(tAchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L440A AchAuthLog update " + e.getErrorMsg());
				}

				if (!"0".equals(checkAmlVo.getConfirmStatus())) {
					throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
				}
			} else if (createFlag == 2) {
				AchAuthLog t2AchAuthLog = new AchAuthLog();
				AchAuthLogId achAuthLogId = new AchAuthLogId();

				achAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
				achAuthLogId.setCustNo(tAchAuthLog.getCustNo());
				achAuthLogId.setRepayBank(tAchAuthLog.getRepayBank());
				achAuthLogId.setRepayAcct(tAchAuthLog.getRepayAcct());
				achAuthLogId.setCreateFlag(tAchAuthLog.getCreateFlag());
				t2AchAuthLog.setAchAuthLogId(achAuthLogId);

				t2AchAuthLog.setFacmNo(tAchAuthLog.getFacmNo());
				t2AchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
				t2AchAuthLog.setAuthMeth(tAchAuthLog.getAuthMeth());
				t2AchAuthLog.setLimitAmt(tAchAuthLog.getLimitAmt());
				t2AchAuthLog.setAuthStatus(" ");
				t2AchAuthLog.setRelationCode(tAchAuthLog.getRelationCode());
				t2AchAuthLog.setRelAcctName(tAchAuthLog.getRelAcctName());
				t2AchAuthLog.setRelAcctBirthday(tAchAuthLog.getRelAcctBirthday());
				t2AchAuthLog.setRelAcctGender(tAchAuthLog.getRelAcctGender());
				t2AchAuthLog.setAmlRsp(tAchAuthLog.getAmlRsp());

				try {
					achAuthLogService.insert(t2AchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L440A AchAuthLog insert " + e.getErrorMsg());
				}

//			錯誤不會更新狀態，故再次產出時，改為未授權

				BankAuthAct tBankAuthAct = new BankAuthAct();
				BankAuthActId tBankAuthActId = new BankAuthActId();
				tBankAuthActId.setCustNo(tAchAuthLog.getCustNo());
				tBankAuthActId.setFacmNo(tAchAuthLog.getFacmNo());
				tBankAuthActId.setAuthType("00");

				tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

				if (tBankAuthAct != null && "9".equals(tBankAuthAct.getStatus())) {
					tBankAuthAct.setStatus(" ");
					try {
						bankAuthActService.update(tBankAuthAct, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

			} else if (createFlag == 3) {
				AchAuthLog t2AchAuthLog = new AchAuthLog();
				AchAuthLogId achAuthLogId = new AchAuthLogId();

				achAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
				achAuthLogId.setCustNo(tAchAuthLog.getCustNo());
				achAuthLogId.setRepayBank(tAchAuthLog.getRepayBank());
				achAuthLogId.setRepayAcct(tAchAuthLog.getRepayAcct());
				achAuthLogId.setCreateFlag("D");
				t2AchAuthLog.setAchAuthLogId(achAuthLogId);

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
					achAuthLogService.insert(t2AchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "L440A AchAuthLog insert " + e.getErrorMsg());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}