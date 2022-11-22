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
import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.domain.ClEvaId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.service.ClEvaService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R29")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R29 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClEvaService sClEvaService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R29 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iRimClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iRimClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iRimClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		int iRimEvaNo = parse.stringToInteger(titaVo.getParam("RimEvaNo"));

		// new ArrayList
		List<ClMain> lClMain = new ArrayList<ClMain>();

		// new table
		ClEva tClEva = new ClEva();

		// new PK
		ClEvaId tClEvaId = new ClEvaId();

		Slice<ClMain> slClMain = sClMainService.findClNo(iRimClCode1, iRimClCode2, iRimClNo, 0, Integer.MAX_VALUE,
				titaVo);
		lClMain = slClMain == null ? null : slClMain.getContent();

		if (lClMain == null) {
			throw new LogicException("E0001", "擔保品主檔");
		}

		tClEvaId = new ClEvaId();
		int evaNo = 0;
		// 回傳最後一筆序號
		if (iRimEvaNo == 0) {
			tClEva = new ClEva();
			tClEva = sClEvaService.ClNoFirst(iRimClCode1, iRimClCode2, iRimClNo, titaVo);
			if (tClEva != null) {
				evaNo = tClEva.getEvaNo() + 1;
			} else {
				evaNo = 1;
			}
		} else {

			tClEva = new ClEva();
			tClEvaId.setClCode1(iRimClCode1);
			tClEvaId.setClCode2(iRimClCode2);
			tClEvaId.setClNo(iRimClNo);
			tClEvaId.setEvaNo(iRimEvaNo);
			tClEva = sClEvaService.findById(tClEvaId, titaVo);
			if (tClEva != null) {
				if (iFunCd == 3) {
					evaNo = tClEva.getEvaNo() + 1;
				} else {
					evaNo = tClEva.getEvaNo();
				}
			}

		}

		if (tClEva == null) {
			if (iFunCd == 1) {
				tClEva = new ClEva();
			} else if (iFunCd == 2) {
				throw new LogicException("E0003", "擔保品重評資料檔");
			} else if (iFunCd == 4) {
				throw new LogicException("E0004", "擔保品重評資料檔");
			} else if (iFunCd == 5) {
				throw new LogicException("E0001", "擔保品重評資料檔");
			}
		}
		this.totaVo.putParam("L2r29ClCode1", tClEva.getClCode1());
		this.totaVo.putParam("L2r29ClCode2", tClEva.getClCode2());
		this.totaVo.putParam("L2r29ClNo", tClEva.getClNo());
		this.totaVo.putParam("L2r29EvaNo", evaNo);
		this.totaVo.putParam("L2r29EvaDate", tClEva.getEvaDate());
		this.totaVo.putParam("L2r29EvaAmt", tClEva.getEvaAmt());
		this.totaVo.putParam("L2r29EvaNetWorth", tClEva.getEvaNetWorth());
		this.totaVo.putParam("L2r29RentEvaValue", tClEva.getRentEvaValue());
		this.totaVo.putParam("L2r29EvaCompanyId", tClEva.getEvaCompanyId());
		this.totaVo.putParam("L2r29EvaCompanyName", tClEva.getEvaCompanyName());
		this.totaVo.putParam("L2r29EvaEmpno", tClEva.getEvaEmpno());
		this.totaVo.putParam("L2r29EvaReason", tClEva.getEvaReason());
		this.totaVo.putParam("L2r29OtherReason", tClEva.getOtherReason());

		this.addList(this.totaVo);
		return this.sendList();
	}
}