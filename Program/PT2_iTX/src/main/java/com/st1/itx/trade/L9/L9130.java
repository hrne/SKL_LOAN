package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L9130 總帳傳票媒體檔產生作業
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9130")
@Scope("prototype")
public class L9130 extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	private L9130Report2022 l9130Report2022;

	@Autowired
	L9131 tranL9131;
	@Autowired
	L9132 tranL9132;
	@Autowired
	L9133 tranL9133;
	@Autowired
	L9134 tranL9134;
	@Autowired
	L9135 tranL9135;
	@Autowired
	L9136 tranL9136;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9130 ");
		this.totaVo.init(titaVo);

		// 取出tita值
		// 會計日期 #AcDate=D,7,I
		int iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		this.info("L9130 iAcDate = " + iAcDate);

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 總帳傳票媒體上傳序號 #MediaSeq=A,3,I
		int iMediaSeq = Integer.parseInt(titaVo.getParam("MediaSeq"));

		// 2022-01-13 智偉修改為不檢核,from 賴桑:L6102會產生不同日期的傳票
//		if (iAcDate != titaVo.getEntDyI()) {
//			this.info("L9130 iAcDate = " + iAcDate);
//			this.info("L9130 titaVo.getEntDyI() = " + titaVo.getEntDyI());
//
//			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
//					titaVo.getTlrNo(), "L9130總帳傳票媒體檔產生,傳入參數[會計日期]不等於系統會計日期", titaVo);
//
//			this.addList(this.totaVo);
//			return this.sendList();
//		}

		if (iBatchNo == 0) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130總帳傳票媒體檔產生,傳入參數[傳票批號]不得為0", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iBatchNo > 99) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130總帳傳票媒體檔產生,傳入參數[傳票批號]不得大於99", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iMediaSeq == 0) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130總帳傳票媒體檔產生,傳入參數[總帳傳票媒體上傳序號]不得為0", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iMediaSeq > 999) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130總帳傳票媒體檔產生,傳入參數[總帳傳票媒體上傳序號]不得大於999", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		doRpt(titaVo);

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9130", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
				"L9130總帳傳票媒體檔產生已完成", titaVo);

		try {
			tranL9131.run(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130產生L9131總帳日結單代傳票時發生錯誤 = " + errors.toString());
		}

		// 2022-05-13 ST1 Wei 新增:
		// from Linda : 珮瑜產生批號90~99時，只需上傳傳票，產生L9131報表，後面的報表不產
		if (iBatchNo >= 90 && iBatchNo <= 99) {
			this.addList(this.totaVo);
			return this.sendList();
		}

		try {
			tranL9132.run(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130產生L9132傳票媒體總表(總帳)時發生錯誤 = " + errors.toString());
		}

		String doL9133 = "N";

		if (titaVo.containsKey("DoL9133")) {
			this.info("titaVo.containsKey : DoL9133 , value = " + titaVo.getParam("DoL9133"));
			doL9133 = titaVo.getParam("DoL9133");
		}

		this.info("doL9133 = " + doL9133);

		if (doL9133.equals("Y")) {
			try {
				tranL9133.run(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("L9130產生L9133放款會計與主檔餘額檢核表時發生錯誤 = " + errors.toString());
			}
		}

		titaVo.putParam("StartDate", iAcDate);
		titaVo.putParam("EndDate", iAcDate);
		
		
		try {
			tranL9134.run(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130產生L9134暫收款傳票金額表時發生錯誤 = " + errors.toString());
		}
		//2022/08/03 Ted新增 L9135 L9136
		try {
			tranL9135.run(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130產生L9135銀行存款媒體明細表(總帳)時發生錯誤 = " + errors.toString());
		}
		
		try {
			tranL9136.run(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9130產生L9136檔案資料變更日報表時發生錯誤 = " + errors.toString());
		}


		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9130 doRpt started.");

//		String parentTranCode = titaVo.getTxcd();

//		l9130Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
//		l9130Report.exec(titaVo);
		l9130Report2022.exec(titaVo);

		this.info("L9130 doRpt finished.");
	}
}