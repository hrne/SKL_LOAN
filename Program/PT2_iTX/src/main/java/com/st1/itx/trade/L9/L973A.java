package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9739ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L973A")
@Scope("prototype")
/**
 * L9739 檢核政府優惠房貸利率脫鉤
 * 
 * @author
 * @version 1.0.0
 */
public class L973A extends TradeBuffer {

	
	@Autowired
	L9739ServiceImpl l9739ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txcd = "L973A";
	

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		
		this.totaVo.init(titaVo);

		// 帳務日(西元)
//		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
//		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月底日(西元)
//		int mfbsdsy = this.txBuffer.getTxCom().getLmndyf();

//		int iYearMonth = 0;
//
//		// 帳務日==月底日
//		if (tbsdy == mfbsdy) {
//			iYearMonth = mfbsdy / 100;
//		}
//		// 帳務日< 月底日 取上的月底
//		if (tbsdy < mfbsdy) {
//			iYearMonth = mfbsdsy / 100;
//		}

		
		List<Map<String, String>> listL9739Title = null;

		try {

			listL9739Title = l9739ServiceImpl.findStandard(titaVo, "0");

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		this.info("listL9739Title =" + listL9739Title.size());

		for (int i = 0; i < listL9739Title.size(); i++) {
			int t = i + 1;

			this.totaVo.putParam("OOProdNo" + t, listL9739Title.get(i).get("ProdNo"));
			this.totaVo.putParam("OOProdName" + t, listL9739Title.get(i).get("ProdName"));
			this.totaVo.putParam("OOEffectDate" + t,
					Integer.valueOf(listL9739Title.get(i).get("EffectDate")) - 19110000);
			this.totaVo.putParam("OOFitRate" + t, listL9739Title.get(i).get("FitRate"));
			this.info("EffectDate" + t + "=" + Integer.valueOf(listL9739Title.get(i).get("EffectDate")));
			this.info("FitRate" + t + "=" + listL9739Title.get(i).get("FitRate"));
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}