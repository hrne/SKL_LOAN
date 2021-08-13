package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimApplNo=9,7
 */
/**
 * L2R11 以核准編號尋找擔保品
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R11")
@Scope("prototype")
public class L2R11 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R11.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService clMainService;
	@Autowired
	public ClFacService clFacService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R11 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		for (int i = 1; i <= 10; i++) {
			this.totaVo.putParam("OClKey" + i, "");
			this.totaVo.putParam("OEvaAmt" + i, 0);
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 10;

		Slice<ClFac> lClFac = clFacService.approveNoEq(iApplNo, this.index, this.limit, titaVo);
		if (!(lClFac == null || lClFac.isEmpty())) {
			int i = 1;
			for (ClFac cl : lClFac.getContent()) {
				ClMain tClMain = clMainService.findById(new ClMainId(cl.getClCode1(), cl.getClCode2(), cl.getClNo()),
						titaVo);
				if (tClMain != null) {
					this.totaVo.putParam("OClKey" + i,
							cl.getClCode1() + "-" + this.parse.IntegerToString(cl.getClCode2(), 2) + "-"
									+ this.parse.IntegerToString(cl.getClNo(), 7));
					this.totaVo.putParam("OEvaAmt" + i, tClMain.getEvaAmt());
					i++;
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}