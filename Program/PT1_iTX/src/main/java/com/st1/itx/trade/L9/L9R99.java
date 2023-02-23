package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9739ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9741ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L9R99")
@Scope("prototype")
/**
 * L9R99 隨機報表檢查
 * 
 * @author
 * @version 1.0.0
 */
public class L9R99 extends TradeBuffer {

	@Autowired
	L9739ServiceImpl l9739ServiceImpl;

	@Autowired
	L9741ServiceImpl l9741ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	private Parse parse;

	String txcd = "L9R99";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);

		this.totaVo.init(titaVo);

		String tranCode = titaVo.getParam("tranNo");

		this.info("tranCode=" + tranCode);

		switch (tranCode) {
		case "L9739":
			try {
				l9739DataProcessing(titaVo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "L9741":
			l9741DataProcessing(titaVo);
			break;

		default:
			break;
		}

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

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void l9739DataProcessing(TitaVo titaVo) throws JSONException {

		List<Map<String, String>> listL9739 = null;

		try {

			listL9739 = l9739ServiceImpl.findStandard(titaVo, titaVo.getEntDyI() + 19110000);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		this.info("listL9739Title =" + listL9739.size());

		// 政府補貼利率前期
		BigDecimal lastRate = BigDecimal.ZERO;
		// 政府補貼利率當期
		BigDecimal thisRate = BigDecimal.ZERO;
		// 前期-當期
		BigDecimal diffRate = BigDecimal.ZERO;
		// 郵局利率
		BigDecimal basePostRate = BigDecimal.ZERO;
		// 商品加碼
		BigDecimal prodIncrRate = BigDecimal.ZERO;
		// 使用利率
		BigDecimal rate = BigDecimal.ZERO;

		JSONObject thisJS;
		JSONObject lastJS;

		for (int i = 0; i < listL9739.size(); i++) {
			int t = i + 1;

			this.totaVo.putParam("OOProdNo" + t, listL9739.get(i).get("ProdNo"));
			this.totaVo.putParam("OOProdName" + t, listL9739.get(i).get("ProdName"));
			this.totaVo.putParam("OOEffectDate" + t, Integer.valueOf(listL9739.get(i).get("EffectDate")) - 19110000);

			int seq = Integer.valueOf(listL9739.get(i).get("Seq"));

			this.info("seq =" + seq);
			
			prodIncrRate = new BigDecimal(listL9739.get(i).get("ProdIncr").toString());
			basePostRate = new BigDecimal(listL9739.get(i).get("BaseRate").toString());

			this.info("prodIncrRate =" + prodIncrRate);
			this.info("basePostRate =" + basePostRate);


			this.info("listL9739.get(i).get(\"JsonFields\").toString() =" + listL9739.get(i).get("JsonFields").toString());
			this.info("listL9739.get(i).get(\"lJsonFields\").toString() =" + listL9739.get(i).get("lJsonFields").toString());
			
			thisJS = new JSONObject(listL9739.get(i).get("JsonFields").toString());
			lastJS = new JSONObject(listL9739.get(i).get("lJsonFields").toString());

			thisRate = new BigDecimal(thisJS.get("SubsidyRate" + seq).toString());
			lastRate = new BigDecimal(lastJS.get("SubsidyRate" + seq).toString());

			this.info("thisRate =" + thisRate);
			this.info("lastRate =" + lastRate);

			diffRate = lastRate.subtract(thisRate);

			this.info("diffRate =" + diffRate);

			rate = basePostRate.add(prodIncrRate).add(diffRate).setScale(2, RoundingMode.HALF_DOWN);

			this.totaVo.putParam("OOFitRate" + t, rate);
//			this.info("EffectDate" + t + "=" + Integer.valueOf(listL9739.get(i).get("EffectDate")));
//			this.info("FitRate" + t + "=" + listL9739.get(i).get("FitRate"));
		}
	}

	private void l9741DataProcessing(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listL9741 = null;

		try {

			listL9741 = l9741ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		this.info("listL9741 =" + listL9741.size());

		int intInsuYearMonth = parse.stringToInteger(titaVo.getParam("InsuYearMonth"));
		int intCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int SearchOption = parse.stringToInteger(titaVo.getParam("SearchOption"));// 1:正常未繳;2:借支;3:催收未繳;9:全部

		this.info("intInsuYearMonth=" + intInsuYearMonth);
		this.info("intCustNo=" + intCustNo);
		this.info("SearchOption=" + SearchOption);

		if (listL9741.size() == 0) {
			this.totaVo.putParam("OOInsuYearMonth", intInsuYearMonth);
			this.totaVo.putParam("OOCustNo", "0000000");
			this.totaVo.putParam("OOSearchOption", SearchOption);
			this.addList(this.totaVo);

			throw new LogicException(titaVo, "E0001", "查無資料");
		} else {
			this.totaVo.putParam("OOInsuYearMonth", intInsuYearMonth);
			this.totaVo.putParam("OOCustNo", intCustNo);
			this.totaVo.putParam("OOSearchOption", SearchOption);
		}

	}
}