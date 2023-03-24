package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9723ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9723p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9723p extends TradeBuffer {

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	L9723ServiceImpl iL9723;

	@Autowired
	L9723Report l9723Report;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9723";
	String TXName = "有效客戶數明細表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);

		boolean isFinish = false;

		try {
			isFinish = l9723Report.exec(titaVo, iL9723.findData(titaVo));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.error("L9723ServiceImpl.findAll error = " + e.toString());
		}

		if (isFinish) {
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO") + TXCD, TXCD + TXName + "已完成", titaVo);

		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO") + TXCD, TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}