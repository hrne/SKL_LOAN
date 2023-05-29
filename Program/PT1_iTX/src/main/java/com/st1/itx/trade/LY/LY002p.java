package com.st1.itx.trade.LY;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("LY002p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY002p extends TradeBuffer {

	@Autowired
	LY002Report lY002Report;

	@Autowired
	LY002Report2 lY002Report2;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active lY002p");
		this.totaVo.init(titaVo);

		this.info("LY002p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		// 1 舊版報表，2新版報表
		// 報表項目
		int reportVer = Integer.valueOf(titaVo.getParam("ReportCode"));

		lY002Report.setParentTranCode(parentTranCode);

		String noticeText = reportVer == 1 ? "LY002 非RBC_表14-1_會計部年度檢查報表" : "LY002 A141重要放款餘額明細表";

		boolean isFinish = reportVer == 1 ? lY002Report.exec(titaVo) : lY002Report2.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), noticeText + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), noticeText + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}