package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxPrinter;
import com.st1.itx.db.domain.TxPrinterId;
import com.st1.itx.db.service.TxPrinterService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("LC115")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC115 extends TradeBuffer {
	@Autowired
	public TxPrinterService txPrinterService;
	
	@Autowired
	public DataLog dataLog;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC115 ");
		this.totaVo.init(titaVo);
		
		TxPrinterId txPrinterId = new TxPrinterId();
		
		txPrinterId.setStanIp(titaVo.getParam("StanIp"));
		txPrinterId.setFileCode(titaVo.getParam("FileCode"));

		TxPrinter txPrinter = txPrinterService.findById(txPrinterId, titaVo);
		if (txPrinter == null) {
			txPrinter = new TxPrinter();
			
			txPrinter.setTxPrinterId(txPrinterId);
			txPrinter.setServerIp(titaVo.getParam("ServerIp"));
			txPrinter.setPrinter(titaVo.getParam("Printer"));
			try {
				txPrinterService.insert(txPrinter,titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else {
			TxPrinter txPrinter2 = (TxPrinter) dataLog.clone(txPrinter);
			txPrinter.setServerIp(titaVo.getParam("ServerIp"));
			txPrinter.setPrinter(titaVo.getParam("Printer"));
			try {
				txPrinter = txPrinterService.update2(txPrinter,titaVo);
				
				dataLog.setEnv(titaVo, txPrinter2, txPrinter);
				dataLog.exec("修改預設印表機設定 ");
				
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}