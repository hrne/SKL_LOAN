package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;

@Service("LC109")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LC109 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxFileService txFileService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC109 = " + titaVo.getTlrNo() + "/" + this.txBuffer.getTxCom().getTlrDept());
		this.totaVo.init(titaVo);

		Long fileno = Long.valueOf(titaVo.get("FileNo").trim());

		TxFile tTxFile = txFileService.findById(fileno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC001", "輸出檔(TxFile)序號:" + fileno);
		}

		if (this.txBuffer.getTxCom().getTlrLevel() == 3) {
//			if ("".equals(tTxFile.getTlrNo())) {
			tTxFile.setTlrNo(titaVo.getTlrNo());
			tTxFile.setGroupNo(this.txBuffer.getTxCom().getTlrDept());
//			} else {
//				throw new LogicException(titaVo, "EC004", "櫃員" + tTxFile.getTlrNo() +"已簽核");
//			}
		} else if (this.txBuffer.getTxCom().getTlrLevel() < 3) {
			if ("".equals(tTxFile.getSupNo())) {
				tTxFile.setSupNo(titaVo.getTlrNo());
			} else {
				throw new LogicException(titaVo, "EC004", "主管" + tTxFile.getTlrNo() + "已簽核");
			}
		}

		try {
			txFileService.update(tTxFile);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0003", "輸出檔(TxFile)序號:" + fileno);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}