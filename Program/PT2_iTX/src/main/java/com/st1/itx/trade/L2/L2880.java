package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.service.TxToDoDetailReserveService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2880")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2880 extends TradeBuffer {

	@Autowired
	L2880Report l2880report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	private TxToDoDetailReserveService txToDoDetailReserveService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2880 ");
		this.totaVo.init(titaVo);

		this.info("L2880 titaVo.getTxcd() = " + titaVo.getTxcd());

		int custno = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmno = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int bormno = parse.stringToInteger(titaVo.getParam("BormNo"));

		txToDoCom.setTxBuffer(this.getTxBuffer());

		TempVo tTempVo = new TempVo();

		TxToDoDetailReserve tTxToDoDetailReserve = txToDoDetailReserveService.FindL2880First("L2880", custno, facmno, bormno, titaVo);

		if (tTxToDoDetailReserve != null) {

			// 抓舊資訊
			tTempVo = tTempVo.getVo(tTxToDoDetailReserve.getProcessNote());

			// 刪除舊的
			txToDoCom.delReserveByTxNo("L2880", tTxToDoDetailReserve.getTitaEntdy(), tTxToDoDetailReserve.getTitaKinbr(), tTxToDoDetailReserve.getTitaTlrNo(), "" + tTxToDoDetailReserve.getTitaTxtNo(),
					titaVo);
		}
		// 寫檔入TxToDoDetailReserve
		tTxToDoDetailReserve = new TxToDoDetailReserve();
		tTxToDoDetailReserve.setItemCode("L2880");
		tTxToDoDetailReserve.setCustNo(custno);
		tTxToDoDetailReserve.setFacmNo(facmno);
		tTxToDoDetailReserve.setBormNo(bormno);

		tTempVo.putParam("LastPrintDate", this.txBuffer.getMgBizDate().getTbsDy());
		tTempVo.putParam("TlrNo", titaVo.getTlrNo());
		if (parse.stringToInteger(titaVo.getParam("Reason")) == 1) {
			if (titaVo.get("ExpectedRate") != null) {
				tTempVo.putParam("LastExpectedRate", titaVo.getParam("ExpectedRate"));
			}
		}

		tTxToDoDetailReserve.setProcessNote(tTempVo.getJsonString());

		List<TxToDoDetailReserve> lTxToDoDetailReserve = new ArrayList<TxToDoDetailReserve>();

		lTxToDoDetailReserve.add(tTxToDoDetailReserve);

		txToDoCom.addReserve(lTxToDoDetailReserve, titaVo);

		String parentTranCode = titaVo.getTxcd();

		l2880report.setParentTranCode(parentTranCode);

		l2880report.exec(titaVo, this.getTxBuffer());

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO") + "L2880", "L2880個人房貸調整案已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}