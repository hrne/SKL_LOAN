package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.InnReCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L9734
 * 
 * @author
 * @version 1.0.0
 */
@Service("L9734")
@Scope("prototype")
public class L9734 extends TradeBuffer {

	String txcd = "L9734";

	@Autowired
	InnReCheckService sInnReCheckService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		int count = 0;

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();

		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		for (int i = 1; i <= totalItem; i++) {

			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				count++;
			}
		}

		this.info("count = " + count);
		if (count == 0) {
			throw new LogicException(titaVo, "E0019", "請勾選報表項目");
		} else {
			checkInnReCheckUpdate(tbsdy, titaVo);
		}

		MySpring.newTask(txcd + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 產表前更新最新(當前日期)InnReCheck資料
	private void checkInnReCheckUpdate(int tbsdyf, TitaVo titaVo) {
		this.info("checkInnReCheckUpdate ...");
		String empNo = titaVo.getTlrNo();

		sInnReCheckService.Usp_L5_InnReCheck_Upd(tbsdyf, empNo, "", titaVo);

	}
}