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
import com.st1.itx.db.domain.CdCl;
import com.st1.itx.db.service.CdClService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R48")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R48 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R48.class);

	/* DB服務注入 */
	@Autowired
	public CdClService sCdClService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R48 ");
		this.totaVo.init(titaVo);

		// tita參數
		// 擔保品代號1
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int wkClCode1St = 1;
		int wkClCode1Ed = 9;
		if (iClCode1 > 0) {
			wkClCode1St = iClCode1;
			wkClCode1Ed = iClCode1;
		}

		Slice<CdCl> slCdCl = sCdClService.clCode1Eq(wkClCode1St, wkClCode1Ed, 0, Integer.MAX_VALUE, titaVo);

		List<CdCl> lCdCl = slCdCl == null ? null : slCdCl.getContent();

		if (lCdCl == null || lCdCl.size() == 0) {

			throw new LogicException(titaVo, "E2003", "查無擔保品代號"); // 查無資料
		}

		String Help = "";

		int listSize = lCdCl.size();
		int i = 1;
		for (CdCl tlCdCl : lCdCl) {
			String tClCode2 = Integer.toString(tlCdCl.getClCode2());
			String tClCode2X = tlCdCl.getClItem();
			Help += FormatUtil.pad9(tClCode2, 2) + ": " + tClCode2X;
			if (i < listSize) {
				Help += ";";
			}
			i++;
		}

		this.totaVo.putParam("L2r48Help1", Help);

		this.addList(this.totaVo);
		return this.sendList();
	}
}