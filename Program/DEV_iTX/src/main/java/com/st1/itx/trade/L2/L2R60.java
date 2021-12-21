package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L2R60")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R60 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R60.class);

	@Autowired
	public CdLandOfficeService cdLandOfficeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R60 ");
		this.totaVo.init(titaVo);

		String iLandOfficeCode = titaVo.getParam("RimLandOfficeCode");

		String s = "";
		String outs = "";

		Slice<CdLandOffice> slCdLandOffice = null;
		List<CdLandOffice> lCdLandOffice = new ArrayList<CdLandOffice>();

		slCdLandOffice = cdLandOfficeService.findLandOfficeCode(iLandOfficeCode, 0, Integer.MAX_VALUE, titaVo);
		lCdLandOffice = slCdLandOffice == null ? null : new ArrayList<CdLandOffice>(slCdLandOffice.getContent());

		if (lCdLandOffice != null) {
			for (CdLandOffice t : lCdLandOffice) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += t.getRecWord() + ":" + t.getRecWordItem().trim();
			}

			outs = s;
		}

		this.totaVo.putParam("HelpDesc", outs);

		this.addList(this.totaVo);
		return this.sendList();
	}
}