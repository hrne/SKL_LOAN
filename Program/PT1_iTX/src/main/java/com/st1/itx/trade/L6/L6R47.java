package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R47")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6R47 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R47.class);

	@Autowired
	public CdLandOfficeService cdLandOfficeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R47 ");
		this.totaVo.init(titaVo);
		initset();

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		String iLandOfficeCode = titaVo.getParam("RimLandOfficeCode");
		String iRecWord = titaVo.getParam("RimRecWord");

		CdLandOfficeId cdLandOfficeId = new CdLandOfficeId();
		cdLandOfficeId.setLandOfficeCode(iLandOfficeCode);
		cdLandOfficeId.setRecWord(iRecWord);
		CdLandOffice tCdLandOffice = cdLandOfficeService.findById(cdLandOfficeId, titaVo);
		if (tCdLandOffice == null) {
			switch (iFunCd) {
			case 1:
				break;
			case 2:
			case 4:
			case 5:
				throw new LogicException(titaVo, "E2003",
						"不存在地政收件字檔" + "地政所代號 = " + iLandOfficeCode + " 收件字代號 = " + iRecWord); // 查無資料
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException(titaVo, "E0002",
						"地政收件字檔" + "地政所代號 = " + iLandOfficeCode + " 收件字代號 =" + iRecWord); // 新增資料已存在
			}

			this.totaVo.putParam("L6r47LandOfficeCode", tCdLandOffice.getLandOfficeCode());
			this.totaVo.putParam("L6r47RecWord", tCdLandOffice.getRecWord());
			this.totaVo.putParam("L6r47RecWordItem", tCdLandOffice.getRecWordItem());

		}

		this.info("end ... ");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void initset() throws LogicException {
		this.info("initset ... ");

		this.totaVo.putParam("L6r47LandOfficeCode", " ");
		this.totaVo.putParam("L6r47RecWord", " ");
		this.totaVo.putParam("L6r47RecWordItem", " ");
	}
}