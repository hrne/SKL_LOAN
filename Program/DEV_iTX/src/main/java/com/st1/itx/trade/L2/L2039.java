package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClEvaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * END=X,1<br>
 */

@Service("L2039")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2039 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClEvaService sClEvaService;

	@Autowired
	public ClBuildingService sClBuildingService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2039 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 153 * 300 = 45900

		// 取tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// 宣告list ClEva
		List<ClEva> lClEva = new ArrayList<ClEva>();
		Slice<ClEva> slClEva = null;
		slClEva = sClEvaService.findClNo(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
		lClEva = slClEva == null ? null : slClEva.getContent();

		if (slClEva != null && slClEva.hasNext()) {

			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		if (lClEva == null || lClEva.size() == 0) {
			throw new LogicException("E0001", "擔保品重評資料檔"); // 查無資料
		}

		for (ClEva tClEva : lClEva) {
			// new occurs
			OccursList occursList = new OccursList();

			occursList.putParam("OOClCode1", tClEva.getClCode1());
			occursList.putParam("OOClCode2", tClEva.getClCode2());
			occursList.putParam("OOClNo", tClEva.getClNo());
			occursList.putParam("OOEvaNo", tClEva.getEvaNo());
			occursList.putParam("OOEvaDate", tClEva.getEvaDate());
			occursList.putParam("OOEvaAmt", tClEva.getEvaAmt());
			occursList.putParam("OOEvaNetWorth", tClEva.getEvaNetWorth());
			occursList.putParam("OORentEvaValue", tClEva.getRentEvaValue());
			occursList.putParam("OOEvaCompanyId", tClEva.getEvaCompanyId());
			occursList.putParam("OOEvaCompanyName", tClEva.getEvaCompanyName());
			occursList.putParam("OOEvaEmpno", tClEva.getEvaEmpno());
			occursList.putParam("OOEvaReason", tClEva.getEvaReason());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		ClBuildingId clBuildingId = new ClBuildingId();
		clBuildingId.setClCode1(iClCode1);
		clBuildingId.setClCode2(iClCode2);
		clBuildingId.setClNo(iClNo);
		ClBuilding tClBuilding = new ClBuilding();
		tClBuilding = sClBuildingService.findById(clBuildingId, titaVo);

		if (tClBuilding != null) {
			this.totaVo.putParam("BdLocation", tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1() + "-" + tClBuilding.getBdNo2());
		} else {
			this.totaVo.putParam("BdLocation", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}