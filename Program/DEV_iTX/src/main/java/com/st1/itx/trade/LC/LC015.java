package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.domain.TxPrinter;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.TxPrinterService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC015")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC015 extends TradeBuffer {
	@Autowired
	public TxPrinterService txPrinterService;

	@Autowired
	public TxFileService txFileService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC015 ");
		this.totaVo.init(titaVo);

		Slice<TxPrinter> slTxPrinter = txPrinterService.findByStanIp(titaVo.getParam("StanIp"), 0, Integer.MAX_VALUE,
				titaVo);

		List<TxPrinter> lTxPrinter = slTxPrinter == null ? null : slTxPrinter.getContent();

		if (lTxPrinter == null) {
			throw new LogicException(titaVo, "E0001", "印表機設定");
		}

		for (TxPrinter txPrinter : lTxPrinter) {
			if (txPrinter.getServerIp().trim().isEmpty()) {
				continue;
			}
			OccursList occursList = new OccursList();
			occursList.putParam("OFileCode", txPrinter.getFileCode());

			TxFile txFile = txFileService.findByFileCodeFirst(txPrinter.getFileCode(), titaVo);
			if (txFile == null) {
				occursList.putParam("OFileItem", "");
			} else {
				occursList.putParam("OFileItem", txFile.getFileItem());
			}

			occursList.putParam("OServerIp", txPrinter.getServerIp());
			occursList.putParam("OPrinter", txPrinter.getPrinter());
			
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}