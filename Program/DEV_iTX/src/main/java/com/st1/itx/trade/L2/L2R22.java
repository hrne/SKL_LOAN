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
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R22")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R22 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R22.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;
	@Autowired
	public ClFacService sClFacService;
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R22 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		int wkCustNo = 0;
		String wkCustId = "";
		// new table ClMain
		ClMain tClMain = new ClMain();
		// new pk
		ClMainId ClMainId = new ClMainId();

		// 塞pk
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		tClMain = sClMainService.findById(ClMainId, titaVo);

		if (tClMain == null) {

			switch (iFunCd) {
			case 1: {
				// 若為新增且資料不存在，存空值到totaVo
				tClMain = new ClMain();
				break;
			}
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "L2R22(tClMain)");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "L2R22(tClMain)");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "擔保品號碼不存在擔保品主檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "L2R22(tClMain)");
			}

		} else {
			if (iFunCd == 1) {
				// 若為新增，但資料已存在，拋錯
				throw new LogicException("E0002", "L2R22(tClMain)");
			} else if (iFunCd == 4) {
				// 若為刪除,額度與關聯檔有資料不可刪除,拋錯
				Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
				List<ClFac> lClFac = slClFac == null ? null : new ArrayList<ClFac>(slClFac.getContent());
				if (lClFac != null && lClFac.size() > 0) {
					throw new LogicException("E2035", "L2R22(ClFac)");
				}
			}
		}
		CustMain tCustMain = sCustMainService.findById(tClMain.getCustUKey(), titaVo);
		if (tCustMain != null) {
			wkCustId = tCustMain.getCustId();
			wkCustNo = tCustMain.getCustNo();
		}
		this.totaVo.putParam("L2r22CustId", wkCustId);
		this.totaVo.putParam("L2r22CustNo", wkCustNo);
		this.totaVo.putParam("L2r22ClCode1", tClMain.getClCode1());
		this.totaVo.putParam("L2r22ClCode2", tClMain.getClCode2());
		this.totaVo.putParam("L2r22ClNo", tClMain.getClNo());
		this.totaVo.putParam("L2r22ClTypeCode", tClMain.getClTypeCode());
		this.totaVo.putParam("L2r22CityCode", tClMain.getCityCode());
		this.totaVo.putParam("L2r22ClStatus", tClMain.getClStatus());
		this.totaVo.putParam("L2r22EvaDate", tClMain.getEvaDate());
		this.totaVo.putParam("L2r22EvaAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("L2r22Synd", tClMain.getSynd());
		this.totaVo.putParam("L2r22SyndCode", tClMain.getSyndCode());
		this.totaVo.putParam("L2r22DispPrice", tClMain.getDispPrice());
		this.totaVo.putParam("L2r22DispDate", tClMain.getDispDate());

		this.addList(this.totaVo);
		return this.sendList();
	}
}