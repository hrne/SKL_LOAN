package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R57")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R57 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	String Help = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R57 ");
		this.totaVo.init(titaVo);

		// TITA
		String iEnterpriseFg = titaVo.getParam("Rim1");

		List<String> wklEnterpriseFg = new ArrayList<String>();

		Boolean deleteFg = true;
		if ("1".equals(iEnterpriseFg)) {

			wklEnterpriseFg.add("Y");
		} else {
			wklEnterpriseFg.add("Y");
			wklEnterpriseFg.add("N");
			wklEnterpriseFg.add("");
		}

		Slice<FacProd> slFacProd = null;
		slFacProd = facProdService.fildentCode(wklEnterpriseFg, 0, Integer.MAX_VALUE, titaVo);

		List<FacProd> lFacProd = slFacProd == null ? null : slFacProd.getContent();

		if (lFacProd == null || lFacProd.size() == 0) {

			this.info("lFacProd =  null");
		} else {
			this.info("lFacProd =  " + lFacProd);
			int listSize = lFacProd.size();
			int i = 1;
			for (FacProd t : lFacProd) {
				String wkProdNo = t.getProdNo();
				String wkProdName = t.getProdName();
				Help = Help + wkProdNo + ":" + wkProdName;
				if (i < listSize) {
					Help = Help + ";";
					this.info("Help = " + Help);
				}

				i++;

			}

		}

		this.totaVo.putParam("L2r57Help1", Help);
		this.addList(this.totaVo);
		return this.sendList();
	}
}