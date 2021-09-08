package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R24")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R24 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R24 ");
		this.totaVo.init(titaVo);

		int funcCode = parse.stringToInteger(titaVo.get("RimFuncCode"));
		int authCreateDate = 19110000 + parse.stringToInteger(titaVo.get("RimAuthCreateDate"));
		int custNo = parse.stringToInteger(titaVo.get("RimCustNo"));
		String bankNo = FormatUtil.pad9(titaVo.getParam("RimBankNo"), 3);
		String acctNo = FormatUtil.pad9(titaVo.getParam("RimAcctNo"), 14);
		int result = 1;

		if ("700".equals(bankNo)) {
			String postDepCode = titaVo.getParam("RimPostDepCode");
			String authApplCode = titaVo.getParam("RimAuthApplCode");

			PostAuthLogId tPostAuthLogId = new PostAuthLogId();
			tPostAuthLogId.setAuthCreateDate(authCreateDate);
			tPostAuthLogId.setCustNo(custNo);
			tPostAuthLogId.setRepayAcct(acctNo);
			tPostAuthLogId.setPostDepCode(postDepCode);
			if ("9".equals(authApplCode)) {
				authApplCode = "1";
			}
			tPostAuthLogId.setAuthApplCode(authApplCode);
			tPostAuthLogId.setAuthCode("1");

			PostAuthLog tPostAuthLog = postAuthLogService.findById(tPostAuthLogId, titaVo);

			if (tPostAuthLog == null) {
				this.info("funcCode ... " + funcCode);
				switch (funcCode) {
				case 2:
					throw new LogicException(titaVo, "E0003", "");
				case 4:
					throw new LogicException(titaVo, "E0004", "");
				case 5:
					throw new LogicException(titaVo, "E0001", "");
				default:
					break;
				}
			}
		} else {

			String createFlag = titaVo.getParam("RimCreateFlag");
			if ("Z".equals(createFlag)) {
				createFlag = "A";
			}

			AchAuthLogId tAchAuthLogId = new AchAuthLogId();
			tAchAuthLogId.setAuthCreateDate(authCreateDate);
			tAchAuthLogId.setCustNo(custNo);
			tAchAuthLogId.setRepayAcct(acctNo);
			tAchAuthLogId.setRepayBank(bankNo);
			tAchAuthLogId.setCreateFlag(createFlag);

			AchAuthLog tAchAuthLog = achAuthLogService.findById(tAchAuthLogId, titaVo);

			if (tAchAuthLog == null) {
				this.info("funcCode ... " + funcCode);

				switch (funcCode) {
				case 2:
					throw new LogicException(titaVo, "E0003", "");
				case 4:
					throw new LogicException(titaVo, "E0004", "");
				case 5:
					throw new LogicException(titaVo, "E0001", "");
				default:
					break;
				}
			}
		}

		totaVo.putParam("L4r24CheckCode", result);

		this.addList(this.totaVo);
		return this.sendList();
	}
}