package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
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

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L441A ");
		this.totaVo.init(titaVo);
		txAmlCom.setTxBuffer(this.txBuffer);
		txToDoCom.setTxBuffer(this.txBuffer);

//		tita  1.新增授權 2.再次授權 3.取消授權
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
		CheckAmlVo checkAmlVo = new CheckAmlVo();
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();

		// 1.新增授權 2.再次授權 3.取消授權
		switch (authApplCode) {
		case "1":
			tPostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			checkAmlVo = txAmlCom.authPost(tPostAuthLog, titaVo);
			if (!"0".equals(checkAmlVo.getConfirmStatus())) {
				throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
			}
			tPostAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());
			try {
				postAuthLogService.update(tPostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "L441A tPostAuthLog update " + e.getErrorMsg());
			}
			break;

		case "2":
			PostAuthLog t1PostAuthLog = new PostAuthLog();
			tPostAuthLogId.setAuthApplCode(tPostAuthLog.getAuthApplCode());
			tPostAuthLogId.setAuthCode(tPostAuthLog.getPostAuthLogId().getAuthCode());
			tPostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			tPostAuthLogId.setCustNo(tPostAuthLog.getPostAuthLogId().getCustNo());
			tPostAuthLogId.setPostDepCode(tPostAuthLog.getPostAuthLogId().getPostDepCode());
			tPostAuthLogId.setRepayAcct(tPostAuthLog.getPostAuthLogId().getRepayAcct());
			t1PostAuthLog.setPostAuthLogId(tPostAuthLogId);
			t1PostAuthLog.setAuthCode(tPostAuthLog.getAuthCode());
			t1PostAuthLog.setFacmNo(tPostAuthLog.getFacmNo());
			t1PostAuthLog.setCustId(tPostAuthLog.getCustId());
			t1PostAuthLog.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
			t1PostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			t1PostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
			t1PostAuthLog.setProcessTime(dateUtil.getNowIntegerTime());
			t1PostAuthLog.setAuthErrorCode(""); // 再次授權
			t1PostAuthLog.setRelationCode(tPostAuthLog.getRelationCode());
			t1PostAuthLog.setRelAcctName(tPostAuthLog.getRelAcctName());
			t1PostAuthLog.setRelAcctBirthday(tPostAuthLog.getRelAcctBirthday());
			t1PostAuthLog.setRelAcctGender(tPostAuthLog.getRelAcctGender());
			checkAmlVo = txAmlCom.authPost(t1PostAuthLog, titaVo);
			if (!"0".equals(checkAmlVo.getConfirmStatus())) {
				throw new LogicException(titaVo, "E0006", "AML檢核不為正常");
			}
			t1PostAuthLog.setAmlRsp(checkAmlVo.getConfirmStatus());

			try {
				postAuthLogService.insert(t1PostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "授權資料同日不可重複 ");
			}
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("POSP00");
			tTxToDoDetail.setCustNo(tPostAuthLog.getCustNo());
			tTxToDoDetail.setFacmNo(tPostAuthLog.getFacmNo());
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(tPostAuthLog.getRepayAcct(), 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);
			break;

		case "3":
			PostAuthLog t2PostAuthLog = new PostAuthLog();
			tPostAuthLogId.setAuthApplCode("2");
			tPostAuthLogId.setAuthCode(tPostAuthLog.getPostAuthLogId().getAuthCode());
			tPostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			tPostAuthLogId.setCustNo(tPostAuthLog.getPostAuthLogId().getCustNo());
			tPostAuthLogId.setPostDepCode(tPostAuthLog.getPostAuthLogId().getPostDepCode());
			tPostAuthLogId.setRepayAcct(tPostAuthLog.getPostAuthLogId().getRepayAcct());
			t2PostAuthLog.setPostAuthLogId(tPostAuthLogId);
			t2PostAuthLog.setAuthCode(tPostAuthLog.getAuthCode());
			t2PostAuthLog.setFacmNo(tPostAuthLog.getFacmNo());
			t2PostAuthLog.setCustId(tPostAuthLog.getCustId());
			t2PostAuthLog.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
			t2PostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
			t2PostAuthLog.setProcessTime(dateUtil.getNowIntegerTime());
			t2PostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
			t2PostAuthLog.setAuthErrorCode(" ");
			t2PostAuthLog.setRelationCode(tPostAuthLog.getRelationCode());
			t2PostAuthLog.setRelAcctName(tPostAuthLog.getRelAcctName());
			t2PostAuthLog.setRelAcctBirthday(tPostAuthLog.getRelAcctBirthday());
			t2PostAuthLog.setRelAcctGender(tPostAuthLog.getRelAcctGender());
			t2PostAuthLog.setAmlRsp(tPostAuthLog.getAmlRsp());
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("POSP00");
			tTxToDoDetail.setCustNo(tPostAuthLog.getCustNo());
			tTxToDoDetail.setFacmNo(tPostAuthLog.getFacmNo());
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(tPostAuthLog.getRepayAcct(), 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

			try {
				postAuthLogService.insert(t2PostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "授權資料同日不可重複 ");
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}