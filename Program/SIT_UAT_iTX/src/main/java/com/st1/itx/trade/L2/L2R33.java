package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R33")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R33 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R33.class);

	/* DB服務注入 */
	@Autowired
	public ClImmService sClImmService;

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R33 ");
		this.totaVo.init(titaVo);

		// tita
		// 擔保品代號1
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		// 擔保品代號2
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		// 功能 1新增
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// 擔保品編號
		int ClNo = 1;
		// new ArrayList
		List<ClMain> lClMain = new ArrayList<ClMain>();
		// new table
		ClMain tClMain = new ClMain();
		ClMain tClMainnew = new ClMain();
		// new PK
		ClMainId ClMainId = new ClMainId();
		// 新增時 自動取號
		if (iFunCd == 1) {
			tClMain = sClMainService.lastClNoFirst(iClCode1, iClCode2, titaVo);
			if (tClMain != null) {
				ClNo = tClMain.getClNo() + 1;
			}
			this.info("擔保品編號 = " + ClNo);

			this.totaVo.putParam("L2r33ColNo", ClNo);

			ClMainId.setClCode1(iClCode1);
			ClMainId.setClCode2(iClCode2);
			ClMainId.setClNo(ClNo);

			tClMainnew.setClMainId(ClMainId);
			tClMainnew.setClCode1(iClCode1);
			tClMainnew.setClCode2(iClCode2);
			tClMainnew.setClNo(ClNo);

			try {
				sClMainService.insert(tClMainnew);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}