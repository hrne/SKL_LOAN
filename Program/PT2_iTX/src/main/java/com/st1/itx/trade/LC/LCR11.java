package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxRecord;
import com.st1.itx.db.domain.TxRecordId;
import com.st1.itx.db.service.TxRecordService;

@Service("LCR11")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR11 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LCR11.class);

	@Autowired
	public TxRecordService txRecordService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR01 ");
		this.totaVo.init(titaVo);

		int entday = Integer.valueOf(titaVo.get("Entdy").trim()) + 19110000;
		String txno = titaVo.get("TxNo").trim();

		this.info("TxRecord key = " + entday + "/" + txno);

		TxRecordId tTxRecordId = new TxRecordId();

		tTxRecordId.setEntdy(entday);
		tTxRecordId.setTxNo(txno);

		TxRecord tTxRecord = txRecordService.findById(tTxRecordId);

		if (tTxRecord == null) {
			throw new LogicException(titaVo, "EC001", "帳務日:" + entday + ",交易序號:" + txno);
		} else {

			try {
				TitaVo tita2 = titaVo.getVo(tTxRecord.getTranData());
				tita2.put("OrgEntdy", String.valueOf(tTxRecord.getEntdy()));
				tita2.put("ORGKIN", tTxRecord.getBrNo());
				tita2.put("ORGTLR", tTxRecord.getTlrNo());
				tita2.put("ORGTNO", tTxRecord.getTxSeq());
				tita2.put("HCODE", "1");
				totaVo.setEcTitaVo(tita2);
			} catch (Throwable e) {
				throw new LogicException(titaVo, "EC002", "電文資料格式有誤");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}