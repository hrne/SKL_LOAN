package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R30")
@Scope("prototype")

public class L4R30 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R30 ");
		this.totaVo.init(titaVo);

//		L4412調資料

		int iAuthCreateDate = parse.stringToInteger(titaVo.getParam("RimAuthCreateDate"));
		String iAuthApplCode = titaVo.getParam("RimAuthApplCode");
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iPostDepCode = titaVo.getParam("RimPostDepCode");
		String iRepayAcct = titaVo.getParam("RimRepayAcct");
		String iAuthCode = titaVo.getParam("RimAuthCode");

		PostAuthLog tPostAuthLog = new PostAuthLog();
		PostAuthLogId tPostAuthLogId = new PostAuthLogId();
		tPostAuthLogId.setAuthCreateDate(iAuthCreateDate);
		tPostAuthLogId.setAuthApplCode(iAuthApplCode);
		tPostAuthLogId.setCustNo(iCustNo);
		tPostAuthLogId.setPostDepCode(iPostDepCode);
		tPostAuthLogId.setRepayAcct(iRepayAcct);
		tPostAuthLogId.setAuthCode(iAuthCode);

		tPostAuthLog = postAuthLogService.findById(tPostAuthLogId, titaVo);

		if (tPostAuthLog != null) {
			this.totaVo.putParam("L4R30CustId", tPostAuthLog.getCustId());
			this.totaVo.putParam("L4R30FacmNo", tPostAuthLog.getFacmNo());
			this.totaVo.putParam("L4R30RepayAcctSeq", tPostAuthLog.getRepayAcctSeq());
			this.totaVo.putParam("L4R30StampCode", tPostAuthLog.getStampCode());
			this.totaVo.putParam("L4R30AuthErrorCode", tPostAuthLog.getAuthErrorCode());
			this.totaVo.putParam("L4R30PostMediaCode", tPostAuthLog.getPostMediaCode());
			this.totaVo.putParam("L4R30AmlRsp", tPostAuthLog.getAmlRsp());
			this.totaVo.putParam("L4R30StampFinishDate", tPostAuthLog.getStampFinishDate());
			this.totaVo.putParam("L4R30CalDate", "");
			this.totaVo.putParam("L4R30CalTime", "");

			String sCreateDate = parse.timeStampToStringDate(tPostAuthLog.getCreateDate()).replace("/", "");
			String sLastUpdate = parse.timeStampToStringDate(tPostAuthLog.getLastUpdate()).replace("/", "");

			if (tPostAuthLog.getProcessDateTime() != null) {
				String caldate = "";
				String caltime = "";
				caldate = parse.timeStampToStringDate(tPostAuthLog.getProcessDateTime()).replace("/", "");
				caltime = parse.timeStampToStringTime(tPostAuthLog.getProcessDateTime());
				this.totaVo.putParam("L4R30CalDate", caldate);
				this.totaVo.putParam("L4R30CalTime", caltime);
			}
			
			this.totaVo.putParam("L4R30CreateEmpNo", tPostAuthLog.getCreateEmpNo());
			this.totaVo.putParam("L4R30CreateDate", sCreateDate);
			this.totaVo.putParam("L4R30LastUpdateEmpNo", tPostAuthLog.getLastUpdateEmpNo());
			this.totaVo.putParam("L4R30LastUpdate", sLastUpdate);
			this.totaVo.putParam("L4R30StampCancelDate", tPostAuthLog.getStampCancelDate());
			this.totaVo.putParam("L4R30LimitAmt", tPostAuthLog.getLimitAmt());

		} else {
			throw new LogicException("E0001", "PostAuthLog");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}