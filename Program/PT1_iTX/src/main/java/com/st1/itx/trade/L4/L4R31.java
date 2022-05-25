package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.service.AchAuthLogHistoryService;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R31")
@Scope("prototype")

public class L4R31 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public AchAuthLogHistoryService achAuthLogHistoryService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R31 ");
		this.totaVo.init(titaVo);

//		L4412調資料

		Long logNo = Long.parseLong(titaVo.getParam("RimLogNo"));
		int iAuthCreateDate = parse.stringToInteger(titaVo.getParam("RimAuthCreateDate"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iRepayBank = titaVo.getParam("RimRepayBank");
		String iRepayAcct = titaVo.getParam("RimRepayAcct");
		String iCreateFlag = titaVo.getParam("RimCreateFlag");

		if (logNo != 0) {
			AchAuthLogHistory tAchAuthLogHistory = achAuthLogHistoryService.findById(logNo, titaVo);

			if (tAchAuthLogHistory != null) {
				this.totaVo.putParam("L4R31FacmNo", tAchAuthLogHistory.getFacmNo());
				this.totaVo.putParam("L4R31LimitAmt", tAchAuthLogHistory.getLimitAmt());
				this.totaVo.putParam("L4R31AuthStatus", tAchAuthLogHistory.getAuthStatus());
				this.totaVo.putParam("L4R31MediaCode", tAchAuthLogHistory.getMediaCode());
				this.totaVo.putParam("L4R31AmlRsp", tAchAuthLogHistory.getAmlRsp());
				this.totaVo.putParam("L4R31StampFinishDate", tAchAuthLogHistory.getStampFinishDate());
				this.totaVo.putParam("L4R31AuthType", tAchAuthLogHistory.getAuthMeth());
				this.totaVo.putParam("L4R31CalDate", tAchAuthLogHistory.getProcessDate());
				String ProcessTime = "";
				if (tAchAuthLogHistory.getProcessTime() == 0) {
					this.totaVo.putParam("L4R31CalTime", tAchAuthLogHistory.getProcessTime());
				} else {
					ProcessTime = "" + tAchAuthLogHistory.getProcessTime();
					ProcessTime = ProcessTime.substring(0, 2) + ":" + ProcessTime.substring(2, 4) + ":"
							+ ProcessTime.substring(4, 6);
					this.totaVo.putParam("L4R31CalTime", ProcessTime);
				}

			} else {
				throw new LogicException("E0001", "AchAuthLogHistory");
			}

		} else {
			AchAuthLog tAchAuthLog = new AchAuthLog();
			AchAuthLogId tAchAuthLogId = new AchAuthLogId();
			tAchAuthLogId.setAuthCreateDate(iAuthCreateDate);
			tAchAuthLogId.setCustNo(iCustNo);
			tAchAuthLogId.setRepayBank(iRepayBank);
			tAchAuthLogId.setRepayAcct(iRepayAcct);
			tAchAuthLogId.setCreateFlag(iCreateFlag);

			tAchAuthLog = achAuthLogService.findById(tAchAuthLogId, titaVo);

			if (tAchAuthLog != null) {
				this.totaVo.putParam("L4R31FacmNo", tAchAuthLog.getFacmNo());
				this.totaVo.putParam("L4R31LimitAmt", tAchAuthLog.getLimitAmt());
				this.totaVo.putParam("L4R31AuthStatus", tAchAuthLog.getAuthStatus());
				this.totaVo.putParam("L4R31MediaCode", tAchAuthLog.getMediaCode());
				this.totaVo.putParam("L4R31AmlRsp", tAchAuthLog.getAmlRsp());
				this.totaVo.putParam("L4R31StampFinishDate", tAchAuthLog.getStampFinishDate());
				this.totaVo.putParam("L4R31AuthType", tAchAuthLog.getAuthMeth());
				this.totaVo.putParam("L4R31CalDate", tAchAuthLog.getProcessDate());
				String ProcessTime = "";
				if (tAchAuthLog.getProcessTime() == 0) {
					this.totaVo.putParam("L4R31CalTime", tAchAuthLog.getProcessTime());
				} else {
					ProcessTime = "" + tAchAuthLog.getProcessTime();
					ProcessTime = ProcessTime.substring(0, 2) + ":" + ProcessTime.substring(2, 4) + ":"
							+ ProcessTime.substring(4, 6);
					this.totaVo.putParam("L4R31CalTime", ProcessTime);
				}

			} else {
				throw new LogicException("E0001", "AchAuthLog");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}