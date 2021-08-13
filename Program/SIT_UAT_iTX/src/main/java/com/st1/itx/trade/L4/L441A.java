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
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L441A")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L441A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L441A.class);

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public BankAuthActService bankAuthActService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L441A ");
		this.totaVo.init(titaVo);

//		tita 1申請 2終止
		String authApplCode = titaVo.getParam("AuthApplCode");

//		#OOAuthCreateDate+#OOAuthApplCode+#OOCustNo+#OOPostDepCode+#OORepayAcct+#OOAuthCode

		PostAuthLog tPostAuthLog = new PostAuthLog();
		PostAuthLogId tPostAuthLogId = new PostAuthLogId();
		tPostAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("OOAuthCreateDate")));
		tPostAuthLogId.setAuthApplCode(titaVo.getParam("OOAuthApplCode"));
		tPostAuthLogId.setCustNo(parse.stringToInteger(titaVo.getParam("OOCustNo")));
		tPostAuthLogId.setPostDepCode(titaVo.getParam("OOPostDepCode"));
		tPostAuthLogId.setRepayAcct(titaVo.getParam("OORepayAcct"));
		tPostAuthLogId.setAuthCode(titaVo.getParam("OOAuthCode"));

		tPostAuthLog = postAuthLogService.holdById(tPostAuthLogId, titaVo);

//		Aml
		txAmlCom.setTxBuffer(this.txBuffer);
		CheckAmlVo checkAmlVo = txAmlCom.authPost(tPostAuthLog, titaVo);

		tPostAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());
		
//		1申請 2終止
		if ("1".equals(authApplCode)) {
//					tPostAuthLog.setAuthApplCode("1");
			tPostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			try {
				postAuthLogService.update(tPostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L441A tPostAuthLog update " + e.getErrorMsg());
			}
		} else if ("2".equals(authApplCode)) {
			PostAuthLogId t2PostAuthLogId = new PostAuthLogId();
			PostAuthLog t2PostAuthLog = new PostAuthLog();

			t2PostAuthLogId.setAuthApplCode(tPostAuthLog.getAuthApplCode());
			t2PostAuthLogId.setAuthCode(tPostAuthLog.getPostAuthLogId().getAuthCode());
			t2PostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			t2PostAuthLogId.setCustNo(tPostAuthLog.getPostAuthLogId().getCustNo());
			t2PostAuthLogId.setPostDepCode(tPostAuthLog.getPostAuthLogId().getPostDepCode());
			t2PostAuthLogId.setRepayAcct(tPostAuthLog.getPostAuthLogId().getRepayAcct());

			t2PostAuthLog.setPostAuthLogId(t2PostAuthLogId);

			t2PostAuthLog.setAuthCode(tPostAuthLog.getAuthCode());
			t2PostAuthLog.setFacmNo(tPostAuthLog.getFacmNo());
			t2PostAuthLog.setCustId(tPostAuthLog.getCustId());
			t2PostAuthLog.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
			t2PostAuthLog.setProcessDate(tPostAuthLog.getProcessDate());
			t2PostAuthLog.setAuthErrorCode(" ");
			t2PostAuthLog.setRelationCode(tPostAuthLog.getRelationCode());
			t2PostAuthLog.setRelAcctName(tPostAuthLog.getRelAcctName());
			t2PostAuthLog.setRelAcctBirthday(tPostAuthLog.getRelAcctBirthday());
			t2PostAuthLog.setRelAcctGender(tPostAuthLog.getRelAcctGender());
			t2PostAuthLog.setAmlRsp(tPostAuthLog.getAmlRsp());

			try {
				postAuthLogService.insert(t2PostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L441A tPostAuthLog insert " + e.getErrorMsg());
			}
			
			BankAuthAct tBankAuthAct = new BankAuthAct();
			BankAuthActId tBankAuthActId = new BankAuthActId();
			tBankAuthActId.setCustNo(tPostAuthLog.getCustNo());
			tBankAuthActId.setFacmNo(tPostAuthLog.getFacmNo());
			tBankAuthActId.setAuthType("01");

			tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

			if (tBankAuthAct != null && "9".equals(tBankAuthAct.getStatus())) {
				tBankAuthAct.setStatus(" ");
				try {
					bankAuthActService.update(tBankAuthAct, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
			}
			tBankAuthActId.setAuthType("02");

			tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);
			if (tBankAuthAct != null && "9".equals(tBankAuthAct.getStatus())) {
				tBankAuthAct.setStatus(" ");
				try {
					bankAuthActService.update(tBankAuthAct, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
			}
			
		} else if ("3".equals(authApplCode)) {
			PostAuthLogId t2PostAuthLogId = new PostAuthLogId();
			PostAuthLog t2PostAuthLog = new PostAuthLog();

			t2PostAuthLogId.setAuthApplCode("2");
			t2PostAuthLogId.setAuthCode(tPostAuthLog.getPostAuthLogId().getAuthCode());
			t2PostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			t2PostAuthLogId.setCustNo(tPostAuthLog.getPostAuthLogId().getCustNo());
			t2PostAuthLogId.setPostDepCode(tPostAuthLog.getPostAuthLogId().getPostDepCode());
			t2PostAuthLogId.setRepayAcct(tPostAuthLog.getPostAuthLogId().getRepayAcct());

			t2PostAuthLog.setPostAuthLogId(t2PostAuthLogId);

			t2PostAuthLog.setAuthCode(tPostAuthLog.getAuthCode());
			t2PostAuthLog.setFacmNo(tPostAuthLog.getFacmNo());
			t2PostAuthLog.setCustId(tPostAuthLog.getCustId());
			t2PostAuthLog.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
			t2PostAuthLog.setProcessDate(tPostAuthLog.getProcessDate());
			t2PostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			t2PostAuthLog.setAuthErrorCode(" ");
			t2PostAuthLog.setRelationCode(tPostAuthLog.getRelationCode());
			t2PostAuthLog.setRelAcctName(tPostAuthLog.getRelAcctName());
			t2PostAuthLog.setRelAcctBirthday(tPostAuthLog.getRelAcctBirthday());
			t2PostAuthLog.setRelAcctGender(tPostAuthLog.getRelAcctGender());
			t2PostAuthLog.setAmlRsp(tPostAuthLog.getAmlRsp());

			try {
				postAuthLogService.insert(t2PostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L441A tPostAuthLog insert " + e.getErrorMsg());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}