package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.SendRsp;

@Service("LC899")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC899 extends TradeBuffer {
	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC899 >> " + titaVo.get("Prgm").toString().trim());
		this.totaVo.init(titaVo);
		if (titaVo.getEntDyI() != this.txBuffer.getTxBizDate().getTbsDy()) {
			throw new LogicException(titaVo, "E0015", "系統已換日請重新登入"); // 檢查錯誤
		}

		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0004", "此為異常處理機制，需與IT確認後才可執行");
		} else {
			String runmode = titaVo.getParam("RunMode").trim();
			String prgm = titaVo.get("Prgm").toString().trim();

			if ("1".equals(runmode)) {

				TradeBuffer x = (TradeBuffer) MySpring.getBean(prgm);
				x.setLoggerFg("1",
						"com.st1.itx.trade." + titaVo.getTxCode().substring(0, 2) + "." + titaVo.getTxCode());
				x.setTxBuffer(this.txBuffer);
				this.info(titaVo.toString());
				this.info(txBuffer.getTxCom().toString());
//		this.totaVoList.addAll(x.run(titaVo));
				x.run(titaVo);

				this.txBuffer = x.getTxBuffer();
			} else if ("2".equals(runmode)) {
				MySpring.newTask(prgm, this.txBuffer, titaVo);
			} else if ("3".equals(runmode)) {
				MySpring.newTask(prgm, this.txBuffer, titaVo, true);
			} else if ("4".equals(runmode)) {
				titaVo.setBatchJobId(prgm);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}