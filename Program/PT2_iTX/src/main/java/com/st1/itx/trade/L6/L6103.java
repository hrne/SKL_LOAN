package com.st1.itx.trade.L6;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

import oracle.jdbc.datasource.OracleDataSource;

import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.TxTellerService;

@Service("L6103")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L6103 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	private TxBizDateService txBizDateService;

	@Autowired
	SendRsp sendRsp;

	@Autowired
	public DataLog dataLog;

	@Autowired
	private DataSource dataSourceDay;

	@Autowired
	private DataSource dataSourceMon;

	@Autowired
	private DataSource dataSourceHist;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6103 ");
		this.totaVo.init(titaVo);

		this.info("L6103 SupCode : " + titaVo.getHsupCode());
		try {
			this.info(dataSourceDay.getConnection().getMetaData().getUserName().toLowerCase(Locale.TAIWAN).indexOf("day") == -1 ? "no Day DataBase Connect" : "Day DataBase is Connect");
			this.info(dataSourceMon.getConnection().getMetaData().getUserName().toLowerCase(Locale.TAIWAN).indexOf("mon") == -1 ? "no Mon DataBase Connect" : "Mon DataBase is Connect");
			this.info(dataSourceHist.getConnection().getMetaData().getUserName().toLowerCase(Locale.TAIWAN).indexOf("hist") == -1 ? "no Hist DataBase Connect" : "Hist DataBase is Connect");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		// 交易需主管核可
//		if (!titaVo.getHsupCode().equals("1")) {
//			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
//		} else {
		toDo(titaVo);
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void toDo(TitaVo titaVo) throws LogicException {
		int iFUNCD = Integer.parseInt(titaVo.get("FUNCD").trim());

		TxTeller tTxTeller = txTellerService.holdById(titaVo.getTlrNo());

		if (tTxTeller == null) {
			throw new LogicException(titaVo, "E0003", "使用者:" + titaVo.getTlrNo());
		}

		/* 使用DB記號 0.onLine 1.onDay 2.onMon 3.onHist */
		if (iFUNCD == 0)
			titaVo.setDataBaseOnLine();
		else if (iFUNCD == 1) {
			throw new LogicException("EC001", "日報環境未開放!");
//			titaVo.setDataBaseOnDay();
		} else if (iFUNCD == 2)
			titaVo.setDataBaseOnMon();
		else if (iFUNCD == 3) {
			throw new LogicException("EC001", "歷史資料環境尚未開放!");
//			titaVo.setDataBaseOnHist();
		}
		TxBizDate txBizDate = txBizDateService.findById("ONLINE", titaVo);

		if (txBizDate == null)
			throw new LogicException("EC001", titaVo.getDataBase() + "TxBizDate");

		TxTeller tTxTeller2 = (TxTeller) dataLog.clone(tTxTeller); // 異動前資料
		tTxTeller.setReportDb(iFUNCD);
		tTxTeller.setEntdy(txBizDate.getTbsDy());

		try {
			// 改回onLine記號
			titaVo.setDataBaseOnLine();
			tTxTeller = txTellerService.update2(tTxTeller, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "使用者:" + titaVo.getTlrNo() + "/" + e.getErrorMsg());
		}
		dataLog.setEnv(titaVo, tTxTeller2, tTxTeller); ////
		dataLog.exec("報表查詢作業申請"); ////

	}
}