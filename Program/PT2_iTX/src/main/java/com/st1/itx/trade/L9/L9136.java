package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9136ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9136")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9136 extends TradeBuffer {

	@Autowired
	L9136Report L9136Report;

	@Autowired
	L9136ServiceImpl l9136ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9136p ");

		this.totaVo.init(titaVo);

		this.info("L9136p titaVo.getTxcd() = " + titaVo.getTxcd());

		String infoNotification = "";

		String parentTranCode = titaVo.getTxcd();

		L9136Report.setParentTranCode(parentTranCode);

		List<Map<String, String>> l9136List = null;
		List<Map<String, String>> l9136List2 = null;

		// 帳務日(西元)
//		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		int acDateStart = 0;
		int acDateEnd = 0;
		if ("L9130".equals(titaVo.getTxcd().toString())) {
			acDateStart = Integer.valueOf(titaVo.getParam("AcDate"));
			acDateEnd = Integer.valueOf(titaVo.getParam("AcDate"));
		} else {
			acDateStart = Integer.valueOf(titaVo.getParam("sAcDate"));
			acDateEnd = Integer.valueOf(titaVo.getParam("eAcDate"));
		}

		this.info("acDateStart~acDateStart =" + acDateStart + "~" + acDateEnd);

		try

		{

			l9136List = l9136ServiceImpl.findAll(titaVo);
			l9136List2 = l9136ServiceImpl.findAll2(titaVo);

		} catch (Exception e) {

			this.info("L9136ServiceImpl.findAll error = " + e.toString());

		}

		if ((l9136List != null && !l9136List.isEmpty()) || (l9136List2 != null && !l9136List2.isEmpty())) {

			this.info("active L9136report data detail");
			L9136Report.exec(titaVo, l9136List, l9136List2, acDateStart, acDateEnd);
			infoNotification = "L9136 檔案資料變更日報表";

		} else {

			infoNotification = "L9136 查無資料";

		}
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L9136", infoNotification, titaVo);

		this.addList(this.totaVo);

		return this.sendList();
	}

}