package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimYear=9,3
 */
@Service("L6R19") // 尋找放款業績工作月對照檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R19 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R19.class);

	/* DB服務注入 */
	@Autowired
	public CdWorkMonthService sCdWorkMonthService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R19 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		this.info("L6R19 1 iRimFuncCode : " + iRimFuncCode);
		int iRimYear = this.parse.stringToInteger(titaVo.getParam("RimYear"));
		this.info("L6R19 1 iRimYear : " + iRimYear);
		int iRimFYear = iRimYear + 1911;
		this.info("L6R19 1 iRimFYear : " + iRimFYear);

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R19"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R19"); // 功能選擇錯誤
		}

		// 查詢放款業績工作月對照檔
		Slice<CdWorkMonth> slCdWorkMonth;
		slCdWorkMonth = sCdWorkMonthService.findYearMonth(iRimFYear, iRimFYear, 00, 99, this.index, Integer.MAX_VALUE, titaVo);
		List<CdWorkMonth> lCdWorkMonth = slCdWorkMonth == null ? null : slCdWorkMonth.getContent();

		if (lCdWorkMonth == null || lCdWorkMonth.size() == 0) {
			if (iRimTxCode.equals("L6752") && iRimFuncCode == 1) {
				this.totaVo.putParam("L6R19Year", 0);
				for (int i = 1; i <= 13; i++) {
					this.totaVo.putParam("L6R19Month" + i, 0);
					this.totaVo.putParam("L6R19StartDate" + i, 0);
					this.totaVo.putParam("L6R19EndDate" + i, 0);
				}
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "放款業績工作月對照檔"); // 查無資料
			}
		}

		/* 如有找到資料 */
		if (lCdWorkMonth != null) {
			if (iRimTxCode.equals("L6752") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimYear")); // 新增資料已存在
			}
		}

		this.totaVo.putParam("L6R19Year", iRimYear);

		for (CdWorkMonth tCdWorkMonth : lCdWorkMonth) {

			this.totaVo.putParam("L6R19Month" + tCdWorkMonth.getMonth(), tCdWorkMonth.getMonth());
			this.totaVo.putParam("L6R19StartDate" + tCdWorkMonth.getMonth(), tCdWorkMonth.getStartDate());
			this.totaVo.putParam("L6R19EndDate" + tCdWorkMonth.getMonth(), tCdWorkMonth.getEndDate());

			this.info("L6R19 Month : " + tCdWorkMonth.getMonth() + " SD : " + tCdWorkMonth.getStartDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}