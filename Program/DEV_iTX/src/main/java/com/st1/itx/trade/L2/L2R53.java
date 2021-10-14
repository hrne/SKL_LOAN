package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
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

@Service("L2R53")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R53 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public PostAuthLogService postAuthLogService;
	@Autowired
	public AchAuthLogService achAuthLogService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	String Help = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R53 ");
		this.totaVo.init(titaVo);

		// TITA
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iRepayBank = titaVo.getParam("RimRepayBank");

//		授權狀態
//		郵局 00:成功
//		一般銀行 0:成功授權
		List<String> wkAuthStatus = new ArrayList<String>();
//		新增或取消記號 
//		郵局 0:申請
//		一般銀行 A:新增授權
		List<String> CreateFlag = new ArrayList<String>();

		String wkOldRepayAcct = "";
		Boolean deleteFg = true;
		if ("700".equals(iRepayBank)) {// 郵局

			wkAuthStatus.add("00");
			CreateFlag.add("1");
			CreateFlag.add("2");
			Slice<PostAuthLog> slPostAuthLog = null;
			slPostAuthLog = postAuthLogService.custNoAuthErrorCodeEq(iCustNo, CreateFlag, wkAuthStatus, 0,
					Integer.MAX_VALUE, titaVo);

			List<PostAuthLog> lPostAuthLog = slPostAuthLog == null ? null : slPostAuthLog.getContent();

			if (lPostAuthLog == null || lPostAuthLog.size() == 0) {

				this.info("lPostAuthLog =  null");
			} else {
				this.info("lPostAuthLog =  " + lPostAuthLog);
				int listSize = lPostAuthLog.size();
				int i = 1;
				for (PostAuthLog t : lPostAuthLog) {
					deleteFg = false;
					if (!wkOldRepayAcct.equals(t.getRepayAcct())) {
						wkOldRepayAcct = t.getRepayAcct();
						deleteFg = true;
						// 判斷第一筆是否為D D不寫入HELP
						if ("2".equals(t.getAuthApplCode())) {
							deleteFg = false;
						}
					}
					if (deleteFg) {
						String wkRepayAcct = t.getRepayAcct();
						Help = Help + FormatUtil.pad9(wkRepayAcct, 14) + ":" + "";
						if (i < listSize) {
							Help = Help + ";";
							this.info("Help = " + Help);
						}

					}
					if (i == listSize) {
						Help = Help + "" + ":" + "";
						this.info("Help = " + Help);
					}
					i++;

				}

			}
		} else {// 一般銀行
			wkAuthStatus.add("0");
			CreateFlag.add("A");
			CreateFlag.add("D");
			Slice<AchAuthLog> slAchAuthLog = null;
			slAchAuthLog = achAuthLogService.custNoRepayBankEq(iCustNo, iRepayBank, wkAuthStatus, CreateFlag, 0,
					Integer.MAX_VALUE, titaVo);
			List<AchAuthLog> lAchAuthLog = slAchAuthLog == null ? null : slAchAuthLog.getContent();
			if (lAchAuthLog == null || lAchAuthLog.size() == 0) {
				this.info("lAchAuthLog =  null");

			} else {
				this.info("lAchAuthLog = " + lAchAuthLog);
				int listSize = lAchAuthLog.size();
				int i = 1;
				for (AchAuthLog t : lAchAuthLog) {

					if (!wkOldRepayAcct.equals(t.getRepayAcct())) {
						wkOldRepayAcct = t.getRepayAcct();
						deleteFg = true;
						// 判斷第一筆是否為D D不寫入HELP
						if ("D".equals(t.getCreateFlag())) {
							deleteFg = false;
						}
					}
					if (deleteFg) {
						String wkRepayAcct = t.getRepayAcct();
						Help = Help + FormatUtil.pad9(wkRepayAcct, 14) + ":" + "";
						if (i < listSize) {
							Help = Help + ";";
							this.info("Help = " + Help);
						}

					}
					if (i == listSize) {
						Help = Help + "" + ":" + "";
						this.info("Help = " + Help);
					}
					i++;
				}

			}
		}

		this.totaVo.putParam("L2r53Help1", Help);
		this.addList(this.totaVo);
		return this.sendList();
	}
}