package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcAcctCheckDetailService;
import com.st1.itx.db.service.AcAcctCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9133")
@Scope("prototype")
/**
 * L9133 會計與主檔餘額檢核表
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9133 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	AcAcctCheckService sAcAcctCheckService;

	/* DB服務注入 */
	@Autowired
	AcAcctCheckDetailService sAcAcctCheckDetailService;

	/* 報表服務注入 */
	@Autowired
	L9133Report l9133Report;

	/* 報表服務注入 */
	@Autowired
	L9133Report2 l9133Report2;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	int iAcDate = 0;
	String empNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9133 ");
		this.totaVo.init(titaVo);

		// 取出tita值
		// 會計日期 #AcDate=D,7,I
		iAcDate = Integer.parseInt(titaVo.getParam("AcDate")) + 19110000;

		this.info("L9133 iAcDate = " + iAcDate);

		// 交易櫃員
		empNo = titaVo.getTlrNo();

		// 執行預存程式更新會計業務檢核檔
		sAcAcctCheckService.Usp_L6_AcAcctCheck_Upd(iAcDate, empNo, titaVo);

		doRpt(titaVo);

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9133", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "L9133會計與主檔餘額檢核表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9133 doRpt started.");

		String parentTranCode = titaVo.getTxcd();

		l9133Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		boolean isDiff = l9133Report.exec(titaVo);

		// 寫產檔記錄到TxFile
		long rptNo = l9133Report.close();

		// 產生PDF檔案
		l9133Report.toPdf(rptNo);

		// 若有差額才產生明細表
		if (isDiff) {
			this.info("l9133Report2 started.");

			// 執行預存程式更新會計業務檢核檔
			sAcAcctCheckDetailService.Usp_L6_AcAcctCheckDetail_Ins(iAcDate, empNo, titaVo);

			l9133Report2.setParentTranCode(parentTranCode);

			// 撈資料組報表
			l9133Report2.exec(titaVo);

			// 寫產檔記錄到TxFile
			long rptNo2 = l9133Report2.close();

			// 產生PDF檔案
			l9133Report2.toPdf(rptNo2);

			this.info("L9133 l9133Report2 finished.");
		}

		this.info("L9133 doRpt finished.");

	}
}