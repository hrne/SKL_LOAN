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
import com.st1.itx.db.domain.CdBudget;
import com.st1.itx.db.service.CdBudgetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimYear=9,3
 */
@Service("L6R16") // 尋找利息收入預算檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R16 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R16.class);

	/* DB服務注入 */
	@Autowired
	public CdBudgetService sCdBudgetService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R16 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		this.info("L6R16 1 iRimFuncCode : " + iRimFuncCode);
		int iRimYear = this.parse.stringToInteger(titaVo.getParam("RimYear"));
		this.info("L6R16 1 iRimYear : " + iRimYear);
		int iRimFYear = iRimYear + 1911;
		this.info("L6R16 1 iRimFYear : " + iRimFYear);

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R16"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R16"); // 功能選擇錯誤
		}

		// 查詢利息收入預算檔
		Slice<CdBudget> slCdBudget;
		slCdBudget = sCdBudgetService.findYearMonth(iRimFYear, iRimFYear, 00, 99, this.index, Integer.MAX_VALUE, titaVo);
		List<CdBudget> lCdBudget = slCdBudget == null ? null : slCdBudget.getContent();

		if (lCdBudget == null || lCdBudget.size() == 0) {
			if (iRimTxCode.equals("L6708") && iRimFuncCode == 1) {
				this.totaVo.putParam("L6R16Year", 0);
				for (int i = 1; i <= 12; i++) {
					this.totaVo.putParam("L6R16Month" + i, 0);
					this.totaVo.putParam("L6R16Budget" + i, 0);
				}
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "利息收入預算檔"); // 查無資料
			}
		}

		/* 如有找到資料 */
		if (lCdBudget != null) {
			if (iRimTxCode.equals("L6708") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimYear")); // 新增資料已存在
			}
		}

		this.totaVo.putParam("L6R16Year", iRimYear);

		for (CdBudget tCdBudget : lCdBudget) {

			this.totaVo.putParam("L6R16Month" + tCdBudget.getMonth(), tCdBudget.getMonth());
			this.totaVo.putParam("L6R16Budget" + tCdBudget.getMonth(), tCdBudget.getBudget());

			this.info("L6R16 Month : " + tCdBudget.getMonth() + " Budget : " + tCdBudget.getBudget());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}