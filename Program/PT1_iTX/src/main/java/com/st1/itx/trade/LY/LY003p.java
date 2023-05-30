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

@Service("LY003p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY003p extends TradeBuffer {

	@Autowired
	LY003Report lY003Report;

	@Autowired
	LY003Report2 lY003Report2;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LY003p");
		this.totaVo.init(titaVo);

		this.info("LY003p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		// 1 舊版報表，2新版報表
		// 報表項目
		int reportVer = Integer.valueOf(titaVo.getParam("ReportCode"));
		String noticeText = "";
		boolean isFinish = true;
		if (reportVer == 1) {
			lY003Report.setParentTranCode(parentTranCode);
			noticeText = "LY003 -非RBC_表14-2_會計部年度檢查報表";
			isFinish = lY003Report.exec(titaVo);
		} else {
			lY003Report2.setParentTranCode(parentTranCode);
			noticeText = "LY003 A142放款餘額彙總表";
			isFinish = lY003Report2.exec(titaVo);
		}

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