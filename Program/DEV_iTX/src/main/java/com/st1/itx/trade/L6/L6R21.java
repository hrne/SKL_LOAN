package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;
import com.st1.itx.db.service.CdPerformanceService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimPieceCode=X,1
 */
@Service("L6R21") // 尋找業績件數及金額核算標準設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R21 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R21.class);

	/* DB服務注入 */
	@Autowired
	public CdPerformanceService sCdPerformanceService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R21 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimPieceCode = titaVo.getParam("RimPieceCode");
		int iRimWorkMonth = Integer.valueOf(titaVo.getParam("RimWorkMonth"))+191100;
		
		this.info("調RIM工作月=="+iRimWorkMonth);
		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R21"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R21"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdPerformance(new CdPerformance());
		
		CdPerformanceId iCdPerformanceId = new CdPerformanceId();
		iCdPerformanceId.setWorkMonth(iRimWorkMonth);
		iCdPerformanceId.setPieceCode(iRimPieceCode);
		// 查詢業績件數及金額核算標準設定檔
		CdPerformance tCdPerformance = sCdPerformanceService.findById(iCdPerformanceId, titaVo);

		/* 如有找到資料 */
		if (tCdPerformance != null) {
			if (iRimTxCode.equals("L6754") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimPieceCode")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdPerformance(tCdPerformance);
			}
		} else {
			if (iRimTxCode.equals("L6754") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "業績件數及金額核算標準設定檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 業績件數及金額核算標準設定檔
	private void moveTotaCdPerformance(CdPerformance mCdPerformance) throws LogicException {

		this.totaVo.putParam("L6R21PieceCode", mCdPerformance.getPieceCode());
		this.totaVo.putParam("L6R21UnitCnt", mCdPerformance.getUnitCnt());
		this.totaVo.putParam("L6R21UnitAmtCond", mCdPerformance.getUnitAmtCond());
		this.totaVo.putParam("L6R21UnitPercent", mCdPerformance.getUnitPercent());
		this.totaVo.putParam("L6R21IntrodPerccent", mCdPerformance.getIntrodPerccent());
		this.totaVo.putParam("L6R21IntrodAmtCond", mCdPerformance.getIntrodAmtCond());
		this.totaVo.putParam("L6R21IntrodPfEqBase", mCdPerformance.getIntrodPfEqBase());
		this.totaVo.putParam("L6R21IntrodPfEqAmt", mCdPerformance.getIntrodPfEqAmt());
		this.totaVo.putParam("L6R21IntrodRewardBase", mCdPerformance.getIntrodRewardBase());
		this.totaVo.putParam("L6R21IntrodReward", mCdPerformance.getIntrodReward());
		this.totaVo.putParam("L6R21BsOffrCnt", mCdPerformance.getBsOffrCnt());
		this.totaVo.putParam("L6R21BsOffrCntLimit", mCdPerformance.getBsOffrCntLimit());
		this.totaVo.putParam("L6R21BsOffrAmtCond", mCdPerformance.getBsOffrAmtCond());
		this.totaVo.putParam("L6R21BsOffrCntAmt", mCdPerformance.getBsOffrCntAmt());
		this.totaVo.putParam("L6R21BsOffrPerccent", mCdPerformance.getBsOffrPerccent());

	}

}