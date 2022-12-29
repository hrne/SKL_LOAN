package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogHistory;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.PostAuthLogHistoryService;
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
	public PostAuthLogHistoryService postAuthLogHistoryService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R30 ");
		this.totaVo.init(titaVo);

//		L4412調資料
		Long logNo = Long.parseLong(titaVo.getParam("RimLogNo"));
		int iAuthCreateDate = parse.stringToInteger(titaVo.getParam("RimAuthCreateDate"));
		String iAuthApplCode = titaVo.getParam("RimAuthApplCode");
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iPostDepCode = titaVo.getParam("RimPostDepCode");
		String iRepayAcct = titaVo.getParam("RimRepayAcct");
		String iAuthCode = titaVo.getParam("RimAuthCode");

		if (logNo != 0) {
			PostAuthLogHistory tPostAuthLogHistory = postAuthLogHistoryService.findById(logNo, titaVo);

			if (tPostAuthLogHistory != null) {
				this.totaVo.putParam("L4R30CustId", tPostAuthLogHistory.getCustId());
				this.totaVo.putParam("L4R30FacmNo", tPostAuthLogHistory.getFacmNo());
				this.totaVo.putParam("L4R30RepayAcctSeq", tPostAuthLogHistory.getRepayAcctSeq());
				this.totaVo.putParam("L4R30StampCode", tPostAuthLogHistory.getStampCode());
				this.totaVo.putParam("L4R30AuthErrorCode", tPostAuthLogHistory.getAuthErrorCode());
				this.totaVo.putParam("L4R30PostMediaCode", tPostAuthLogHistory.getPostMediaCode());
				this.totaVo.putParam("L4R30AmlRsp", tPostAuthLogHistory.getAmlRsp());
				this.totaVo.putParam("L4R30StampFinishDate", tPostAuthLogHistory.getStampFinishDate());
				this.totaVo.putParam("L4R30CalDate", tPostAuthLogHistory.getProcessDate());
				String ProcessTime = "";
				if (tPostAuthLogHistory.getProcessTime() == 0) {
					this.totaVo.putParam("L4R30CalTime", "");
				} else {
					ProcessTime = "" + tPostAuthLogHistory.getProcessTime();
					ProcessTime = ProcessTime.substring(0, 2) + ":" + ProcessTime.substring(2, 4) + ":"
							+ ProcessTime.substring(4, 6);
					this.totaVo.putParam("L4R30CalTime", ProcessTime);
				}

				String sCreateDate = parse.timeStampToStringDate(tPostAuthLogHistory.getCreateDate()).replace("/", "");
				String sLastUpdate = parse.timeStampToStringDate(tPostAuthLogHistory.getLastUpdate()).replace("/", "");

				this.totaVo.putParam("L4R30CreateEmpNo", tPostAuthLogHistory.getCreateEmpNo());
				this.totaVo.putParam("L4R30CreateDate", sCreateDate);
				this.totaVo.putParam("L4R30LastUpdateEmpNo", tPostAuthLogHistory.getLastUpdateEmpNo());
				this.totaVo.putParam("L4R30LastUpdate", sLastUpdate);
				this.totaVo.putParam("L4R30StampCancelDate", tPostAuthLogHistory.getStampCancelDate());
				this.totaVo.putParam("L4R30LimitAmt", tPostAuthLogHistory.getLimitAmt());
				this.totaVo.putParam("L4R30AuthApplCode", tPostAuthLogHistory.getAuthApplCode());

			} else {
				throw new LogicException("E0001", "PostAuthLogHistory");
			}

		} else {

			String wkAuthApplCode = "";
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
				this.totaVo.putParam("L4R30CalDate", tPostAuthLog.getProcessDate());
				String ProcessTime = "";
				if (tPostAuthLog.getProcessTime() == 0) {
					this.totaVo.putParam("L4R30CalTime", "");
				} else {
					ProcessTime = "" + tPostAuthLog.getProcessTime();
					ProcessTime = ProcessTime.substring(0, 2) + ":" + ProcessTime.substring(2, 4) + ":"
							+ ProcessTime.substring(4, 6);
					this.totaVo.putParam("L4R30CalTime", ProcessTime);
				}

				String sCreateDate = parse.timeStampToStringDate(tPostAuthLog.getCreateDate()).replace("/", "");
				String sLastUpdate = parse.timeStampToStringDate(tPostAuthLog.getLastUpdate()).replace("/", "");

				this.totaVo.putParam("L4R30CreateEmpNo", tPostAuthLog.getCreateEmpNo());
				this.totaVo.putParam("L4R30CreateDate", sCreateDate);
				this.totaVo.putParam("L4R30LastUpdateEmpNo", tPostAuthLog.getLastUpdateEmpNo());
				this.totaVo.putParam("L4R30LastUpdate", sLastUpdate);
				this.totaVo.putParam("L4R30StampCancelDate", tPostAuthLog.getStampCancelDate());
				this.totaVo.putParam("L4R30LimitAmt", tPostAuthLog.getLimitAmt());

				wkAuthApplCode = tPostAuthLog.getAuthApplCode();
				if (tPostAuthLog.getDeleteDate() > 0) {
					if ("00".equals(tPostAuthLog.getAuthErrorCode())) {
						wkAuthApplCode = "9";
					} else {
						wkAuthApplCode = "2";
					}
				}
				this.totaVo.putParam("L4R30AuthApplCode", wkAuthApplCode);
			} else {
				throw new LogicException("E0001", "PostAuthLog");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}