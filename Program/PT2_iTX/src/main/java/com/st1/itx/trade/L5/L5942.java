package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfReward;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5942")
@Scope("prototype")

/**
 * 晤談人員明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5942 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5942.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public PfRewardService iPfRewardService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		Slice<PfReward> iPfReward = iPfRewardService.findByCustNo(iCustNo, this.index, this.limit, titaVo);
		this.info("Return Value=" + iPfReward);
		if (iPfReward != null) {
			for (PfReward rePfReward : iPfReward) {
				OccursList occurslist = new OccursList();
				occurslist.putParam("OOCustNo", rePfReward.getCustNo());
				occurslist.putParam("OOFacmNo", rePfReward.getFacmNo());
				occurslist.putParam("OOInterviewerA", rePfReward.getInterviewerA());
				occurslist.putParam("OOInterviewerB", rePfReward.getInterviewerB());
				occurslist.putParam("OOKeyiner", rePfReward.getCreateEmpNo());
				occurslist.putParam("OOCoorgnizer", rePfReward.getCoorgnizer());
				this.totaVo.addOccursList(occurslist);
			}
		} else {
			throw new LogicException(titaVo, "E0001", ""); // 查無資料時
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
