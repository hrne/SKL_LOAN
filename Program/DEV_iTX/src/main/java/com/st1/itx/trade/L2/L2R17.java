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
import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.service.ClNoMapService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R17")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R17 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClNoMapService sClNoMapService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R17 ");
		this.totaVo.init(titaVo);

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));

		List<ClNoMap> lClNoMap = new ArrayList<ClNoMap>();

		Slice<ClNoMap> sClNoMap = sClNoMapService.findNewClNo(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);

		lClNoMap = sClNoMap == null ? null : sClNoMap.getContent();

		this.totaVo.putParam("L2r17GdrId1", "0");
		this.totaVo.putParam("L2r17GdrId2", "00");
		this.totaVo.putParam("L2r17GdrNum", "0000000");
		this.totaVo.putParam("L2r17LgtSeq", "0");

		if (lClNoMap != null) {
			for (ClNoMap tClNoMap : lClNoMap) {
				if (tClNoMap.getTfStatus() == 1 || tClNoMap.getTfStatus() == 3) {
					this.totaVo.putParam("L2r17GdrId1", tClNoMap.getGdrId1());
					this.totaVo.putParam("L2r17GdrId2", tClNoMap.getGdrId2());
					this.totaVo.putParam("L2r17GdrNum", tClNoMap.getGdrNum());
					this.totaVo.putParam("L2r17LgtSeq", tClNoMap.getLgtSeq());
				}
			}

		}

		this.addList(this.totaVo);
		return this.sendList();

	}
}