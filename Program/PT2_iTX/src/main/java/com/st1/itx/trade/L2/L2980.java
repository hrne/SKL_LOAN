package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.service.TxToDoDetailReserveService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.http.WebClient;

@Service("L2980")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2980 extends TradeBuffer {

	@Autowired
	L2980Report l2980report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	private TxToDoDetailReserveService txToDoDetailReserveService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2980 ");
		this.totaVo.init(titaVo);

		this.info("L2980 titaVo.getTxcd() = " + titaVo.getTxcd());

		int custno = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmno = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int bormno = parse.stringToInteger(titaVo.getParam("BormNo"));

		TempVo tTempVo = new TempVo();

		TxToDoDetailReserve tTxToDoDetailReserve = txToDoDetailReserveService.FindL2980First("L2980", custno, facmno,
				bormno, titaVo);

		if (tTxToDoDetailReserve != null) {
			// 更新 TxToDoDetailReserve
			tTxToDoDetailReserve.setDataDate(this.txBuffer.getMgBizDate().getTbsDy());
			tTxToDoDetailReserve.setTitaEntdy(titaVo.getEntDyI());
			tTxToDoDetailReserve.setTitaKinbr(titaVo.getKinbr());
			tTxToDoDetailReserve.setTitaTlrNo(titaVo.getTlrNo());
			tTxToDoDetailReserve.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));

			tTempVo = tTempVo.getVo(tTxToDoDetailReserve.getProcessNote());
			tTempVo.putParam("LastPrintDate", this.txBuffer.getMgBizDate().getTbsDy());
			tTempVo.putParam("TlrNo", titaVo.getTlrNo());
			if (parse.stringToInteger(titaVo.getParam("Reason")) == 1) {
				if (titaVo.get("ExpectedRate") != null) {
					tTempVo.putParam("LastExpectedRate", titaVo.getParam("ExpectedRate"));
				}
			}

			tTxToDoDetailReserve.setProcessNote(tTempVo.getJsonString());
			try {
				txToDoDetailReserveService.update(tTxToDoDetailReserve, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0005", "TxToDoDetailReserve insert " + e.getErrorMsg());
			}
		} else {
			// 寫檔入TxToDoDetailReserve
			tTxToDoDetailReserve = new TxToDoDetailReserve();
			tTxToDoDetailReserve.setItemCode("L2980");
			tTxToDoDetailReserve.setCustNo(custno);
			tTxToDoDetailReserve.setFacmNo(facmno);
			tTxToDoDetailReserve.setBormNo(bormno);
			tTxToDoDetailReserve.setDtlValue("");
			tTxToDoDetailReserve.setStatus(2);// 2.已處理
			tTxToDoDetailReserve.setDataDate(this.txBuffer.getMgBizDate().getTbsDy());
			tTxToDoDetailReserve.setTitaEntdy(titaVo.getEntDyI());
			tTxToDoDetailReserve.setTitaKinbr(titaVo.getKinbr());
			tTxToDoDetailReserve.setTitaTlrNo(titaVo.getTlrNo());
			tTxToDoDetailReserve.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));

			tTempVo.putParam("LastPrintDate", this.txBuffer.getMgBizDate().getTbsDy());
			tTempVo.putParam("TlrNo", titaVo.getTlrNo());
			if (parse.stringToInteger(titaVo.getParam("Reason")) == 1) {
				if (titaVo.get("ExpectedRate") != null) {
					tTempVo.putParam("LastExpectedRate", titaVo.getParam("ExpectedRate"));
				}
			}

			tTxToDoDetailReserve.setProcessNote(tTempVo.getJsonString());

			try {
				txToDoDetailReserveService.insert(tTxToDoDetailReserve, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0005", "TxToDoDetailReserve insert " + e.getErrorMsg());
			}

		}

		String parentTranCode = titaVo.getTxcd();

		l2980report.setParentTranCode(parentTranCode);

		l2980report.exec(titaVo, this.getTxBuffer());

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L2980", "L2980個人房貸調整案已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}