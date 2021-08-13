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
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R38")
@Scope("prototype")
/**
 * 
 * 
 * @author St1ChihWei
 * @version 1.0.0
 */
public class L2R38 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R38.class);

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R38 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// tita參數
		// 擔保品代號1
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		// 擔保品代號2
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		// 擔保品編號
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));

		Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();

		if (lClFac == null || lClFac.size() == 0) {

			throw new LogicException(titaVo, "E2020", "查無額度與擔保品關聯檔資料"); // 查無資料
		}

		String approveNoHelp = "";

		int listSize = lClFac.size();
		int i = 1;
		for (ClFac tClFac : lClFac) {
			String tApproveNo = Integer.toString(tClFac.getApproveNo());
			approveNoHelp += FormatUtil.pad9(tApproveNo, 7) + ": ";
			if (i < listSize) {
				approveNoHelp += ";";
			}
			i++;
		}

		this.totaVo.putParam("L2r38ApproveNoHelp", approveNoHelp);

		this.addList(this.totaVo);
		return this.sendList();
	}
}