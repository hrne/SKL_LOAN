package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L9714p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9714p extends TradeBuffer {

	@Autowired
	L9714Report lL9714Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	private CustNoticeCom custNoticeCom;
	

	@Autowired
	private Parse parse;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9714p");
		this.totaVo.init(titaVo);

		this.info("L9714p titaVo.getTxcd() = " + titaVo.getTxcd());
		
		int iAcDate = Integer.parseInt(titaVo.getEntDy());
		
		String parentTranCode = titaVo.getTxcd();
		
		String content = "";
		
		int custNo = 0;
		int facmNo = 0;

		if (titaVo.get("CustNo") != null) {
			custNo = parse.stringToInteger(titaVo.get("CustNo"));
		}
		if (titaVo.get("FacmNo") != null) {
			facmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		}
		String tran = titaVo.getTxCode().isEmpty() ? "L9706" : titaVo.getTxCode();

		if (!custNoticeCom.checkIsLetterSendable(titaVo.get("CUSTNO"), custNo, facmNo, tran, titaVo)) {
			throw new LogicException("E0005", "無郵件寄號");
		}
		
		lL9714Report.setParentTranCode(parentTranCode);

		boolean isFinish = lL9714Report.exec(titaVo);
		if (isFinish) {
			content = "L9714繳息證明單已完成";
		} else {
			content = "L9714繳息證明單查無資料";
		}
		
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9714", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf,
				content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}