package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4603")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4603 extends TradeBuffer {
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	DateUtil dDateUtil;
	
	@Autowired
	public Parse parse;
	
	@Autowired
	public InsuRenewService insuRenewService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603 ");
		this.totaVo.init(titaVo);
		
		int iInsuEndMonth = 0;
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
//		 續保件
				if (t.getRenewCode() == 2 && t.getStatusCode() == 0) {
					lInsuRenew.add(t);
				} // if
			} // for
		} // if

		if (lInsuRenew.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");// 查無資料
		}
		
		if (titaVo.isHcodeNormal()) {
			for (InsuRenew t : lInsuRenew) {
				if ("Y".equals(t.getNotiTempFg())) {
					throw new LogicException("E0005", "已入通知，請先訂正此交易。");
				}
			} // for
		}
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "", "",
				titaVo.getParam("TLRNO"), "批次作業啟動請稍等", titaVo);
		MySpring.newTask("L4603p", this.txBuffer, titaVo); 
		this.addList(this.totaVo);
		return this.sendList();
	}
			
}