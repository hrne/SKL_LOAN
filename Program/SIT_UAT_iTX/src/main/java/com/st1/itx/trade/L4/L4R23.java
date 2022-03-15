package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R23")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R23 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R23.class);

	@Autowired
	public Parse parse;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R23 ");
		this.totaVo.init(titaVo);

		int custNo = parse.stringToInteger(titaVo.get("RimCustNo"));
		String bankNo = FormatUtil.pad9(titaVo.getParam("RimBankNo"), 3);
		String acctNo = FormatUtil.pad9(titaVo.getParam("RimAcctNo"), 14);
//		int facmNo = parse.stringToInteger(titaVo.get("RimFacmNo"));
		int result = 1;

		if ("700".equals(bankNo)) {
			String postDepCode = titaVo.getParam("RimPostDepCode");

			PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(custNo, postDepCode, acctNo, "1", titaVo);

			if (tPostAuthLog != null) {
				if (!"2".equals(tPostAuthLog.getAuthApplCode()) && "Y".equals(tPostAuthLog.getPostMediaCode())) {
					result = 0;
				}
			}
		} else {
			this.info("bankNo ... " + bankNo);
			this.info("acctNo ... " + acctNo);

			AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(custNo, bankNo, acctNo, titaVo);

			if (tAchAuthLog != null) {
//				A:新增(恢復授權) D:取消 Z:暫停授權
//				非A或找不到 = 未授權
				if (!"D".equals(tAchAuthLog.getCreateFlag()) && "Y".equals(tAchAuthLog.getMediaCode())) {
					result = 0;
				}
			}
		}

		totaVo.putParam("L4r23CheckCode", result);

		this.addList(this.totaVo);
		return this.sendList();
	}
}