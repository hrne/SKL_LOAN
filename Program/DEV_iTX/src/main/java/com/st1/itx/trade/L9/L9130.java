package com.st1.itx.trade.L9;

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

/**
 * L9130 核心傳票媒體檔產生作業
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9130")
@Scope("prototype")
public class L9130 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9130.class);

	/* 報表服務注入 */
	@Autowired
	private L9130Report l9130Report;

	/* 報表服務注入 */
	@Autowired
	private L9130Report2022 l9130Report2022;

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
		int iAcDate = Integer.valueOf(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.valueOf(titaVo.getParam("BatchNo"));

		// 核心傳票媒體上傳序號 #MediaSeq=A,3,I
		int iMediaSeq = Integer.valueOf(titaVo.getParam("MediaSeq"));

		if (iAcDate != titaVo.getEntDyI()) {
			this.info("L9130 iAcDate = " + iAcDate);
			this.info("L9130 titaVo.getEntDyI() = " + titaVo.getEntDyI());

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130核心傳票媒體檔產生,傳入參數[會計日期]不等於系統會計日期", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iBatchNo == 0) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130核心傳票媒體檔產生,傳入參數[傳票批號]不得為0", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iBatchNo > 99) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130核心傳票媒體檔產生,傳入參數[傳票批號]不得大於99", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iMediaSeq == 0) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130核心傳票媒體檔產生,傳入參數[核心傳票媒體上傳序號]不得為0", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		if (iMediaSeq > 999) {
			this.info("L9130 iBatchNo = " + iBatchNo);

			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", titaVo.getTxcd(),
					titaVo.getTlrNo(), "L9130核心傳票媒體檔產生,傳入參數[核心傳票媒體上傳序號]不得大於999", titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

		doRpt(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				"L9130核心傳票媒體檔產生已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9130 doRpt started.");

		String parentTranCode = titaVo.getTxcd();

		l9130Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l9130Report.exec(titaVo);
		l9130Report2022.exec(titaVo);

		this.info("L9130 doRpt finished.");
	}
}