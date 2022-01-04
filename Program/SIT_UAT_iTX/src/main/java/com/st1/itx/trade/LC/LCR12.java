package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeReport;

@Service("LCR12")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LCR12 extends TradeBuffer {
	@Autowired
	public MakeReport makeReport;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR12 ");
		this.totaVo.init(titaVo);

		long reportno = Long.valueOf(titaVo.get("ReportNo").trim());
//		String printer = titaVo.get("Printer");
		int pageno = Integer.valueOf(titaVo.get("PageNo").trim());

		HashMap<String, Object> p = makeReport.toPrint(reportno, pageno, titaVo.getParam("IP"));

		String morepage = p.get("morePage").toString();
		String printJson = p.get("printJson").toString();

		this.totaVo.putParam("ServerIp", p.get("ServerIp"));
		this.totaVo.putParam("Printer", p.get("Printer"));
		this.totaVo.putParam("PrintJson", printJson);

		if ("1".equals(morepage)) {
			titaVo.put("PageNo", String.valueOf(pageno++));
			this.totaVo.setMsgEndToAuto();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}