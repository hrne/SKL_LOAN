package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R40")
@Scope("prototype")
/**
 * 用擔保品編號和核准號碼查擔保品與額度關聯檔
 * 
 * @author St1ChihWei
 * @version 1.0.0
 */
public class L2R40 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R40.class);

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R40 ...");
		this.totaVo.init(titaVo);

		// tita參數
		// 擔保品代號1
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		// 擔保品代號2
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		// 擔保品編號
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		// 核准號碼
		int iApproveNo = parse.stringToInteger(titaVo.getParam("RimApproveNo"));

		this.info("L2R40 iClCode1   = " + iClCode1);
		this.info("L2R40 iClCode2   = " + iClCode2);
		this.info("L2R40 iClNo      = " + iClNo);
		this.info("L2R40 iApproveNo = " + iApproveNo);

		ClFac tClFac = sClFacService.findById(new ClFacId(iClCode1, iClCode2, iClNo, iApproveNo));

		// 錯誤處理
		if (tClFac == null) {
			throw new LogicException("E2020", "擔保品與額度關聯檔"); // 查無資料
		}

		// 存入tota
		this.totaVo.putParam("L2r40ShareAmt", tClFac.getShareAmt().toString());

		this.addList(this.totaVo);
		return this.sendList();
	}
}