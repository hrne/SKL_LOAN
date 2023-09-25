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
import com.st1.itx.eum.ContentName;
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
	private TxPrinterService txPrinterService;

	@Autowired
	private TxFileService txFileService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC015 ");
		this.totaVo.init(titaVo);

		// 2023-09-25 Wei 增加 from Lai:
		// 各環境產表都寫回Online,
		// 但是各環境在LC009查詢時,只能查到各自環境產製的報表
		String sourceEnv = getSourceEnv(titaVo.getDataBase());

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) titaVo.clone();
		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);
		
		Slice<TxPrinter> slTxPrinter = txPrinterService.findByStanIpAndSourceEnv(titaVo.getParam("StanIp"), sourceEnv, 0,
				Integer.MAX_VALUE, tmpTitaVo);

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

			TxFile txFile = txFileService.findByFileCodeFirst(txPrinter.getFileCode(), tmpTitaVo);
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

	private String getSourceEnv(String dataBase) {
		switch (dataBase) {
		case ContentName.onLine:
			return "O";
		case ContentName.onDay:
			return "D";
		case ContentName.onMon:
			return "M";
		case ContentName.onHist:
			return "H";
		default:
			return "O";
		}
	}
}