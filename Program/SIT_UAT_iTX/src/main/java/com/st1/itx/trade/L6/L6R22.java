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
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimYear=9,3
 * RimMonth=9,2
 */
@Service("L6R22") // 尋找介紹人加碼獎勵津貼標準設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R22 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R22.class);

	/* DB服務注入 */
	@Autowired
	public CdBonusService sCdBonusService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R22 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");

		String iRimWorkMonth = titaVo.getParam("RimWorkMonth");
		this.info("L6R22 1 iRimWorkMonth : " + iRimWorkMonth);
		for (int i = 0; iRimWorkMonth.length() < 5; i++) {
			iRimWorkMonth = 0 + iRimWorkMonth;
			this.info("L6R22 1 iRimWorkMonth" + i + " : " + iRimWorkMonth);
		}

		String iRimYear = iRimWorkMonth.substring(0, 3);
		String iRimMonth = iRimWorkMonth.substring(3, 5);
		this.info("L6R22 1 iRimYear : " + iRimYear + "iRimMonth:" + iRimMonth);

		int iWorkYear = this.parse.stringToInteger(iRimYear) + 1911;
		String WorkMonth = iWorkYear + iRimMonth;
		int iWorkMonth = this.parse.stringToInteger(WorkMonth);

		// FuncCode=複製用記號
		int iRimMark = 0;
		if (titaVo.getParam("RimMark") != "" || titaVo.getParam("RimMark") != null) {
			iRimMark = this.parse.stringToInteger(titaVo.getParam("RimMark"));
		}

		this.info("L6R22 1 iWorkMonth : " + iWorkMonth);

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R22"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R22"); // 功能選擇錯誤
		}

		// 清值
		this.totaVo.putParam("L6R22Year", 0);
		this.totaVo.putParam("L6R22Month", 0);
		for (int i = 0; i <= 19; i++) {
			this.totaVo.putParam("L6R22PieceCode" + i, "");
			this.totaVo.putParam("L6R22AmtStartRange" + i, 0);
			this.totaVo.putParam("L6R22AmtEndRange" + i, 0);
			this.totaVo.putParam("L6R22Bonus" + i, 0);
		}

		// 檢查介紹人加碼獎勵津貼標準設定檔是否己存在
		Slice<CdBonus> slCdBonus;
		slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, Integer.MAX_VALUE, titaVo);
		List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

		if (lCdBonus == null || lCdBonus.size() == 0) {
			if (iRimTxCode.equals("L6751") && (iRimFuncCode == 1 || iRimFuncCode == 3)) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
			}
		}

		/* 如有找到資料 */
		if (lCdBonus != null) {
			if (iRimTxCode.equals("L6751") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimYear + "/" + iRimMonth); // 新增資料已存在
			}
			// FuncCode=複製時
			if (iRimTxCode.equals("L6751") && iRimFuncCode == 3 && iRimMark == 1) {
				throw new LogicException(titaVo, "E0002", iRimYear + "/" + iRimMonth); // 新增資料已存在
			}
		}

		this.totaVo.putParam("L6R22Year", iRimYear);
		this.totaVo.putParam("L6R22Month", iRimMonth);

		// 查詢條件記號=1 (篩選條件-計件代碼)
		Slice<CdBonus> alCdBonus;
		alCdBonus = sCdBonusService.findCondition(iWorkMonth, 1, "0", "Z", this.index, Integer.MAX_VALUE, titaVo);
		List<CdBonus> laCdBonus = alCdBonus == null ? null : alCdBonus.getContent();

		/* 如有找到資料 */
		if (laCdBonus != null) {
			int i = 0;
			for (CdBonus tCdBonus : laCdBonus) {
				this.info("L6R22 C1 i : " + i + " ,ConditionCode : " + tCdBonus.getConditionCode() + " - " + tCdBonus.getCondition());
				this.totaVo.putParam("L6R22PieceCode" + i, tCdBonus.getCondition());
				i++;
			}
		}

		// 查詢條件記號=3 (金額級距)
		Slice<CdBonus> clCdBonus;
		clCdBonus = sCdBonusService.findCondition(iWorkMonth, 3, "00", "99", this.index, Integer.MAX_VALUE, titaVo);
		List<CdBonus> lcCdBonus = clCdBonus == null ? null : clCdBonus.getContent();

		/* 如有找到資料 */
		if (lcCdBonus != null) {
			int i = 0;
			for (CdBonus tCdBonus : lcCdBonus) {
				this.info("L6R22 C3 i : " + i + " ,ConditionCode : " + tCdBonus.getConditionCode() + " - " + tCdBonus.getAmtStartRange());
				this.totaVo.putParam("L6R22AmtStartRange" + i, tCdBonus.getAmtStartRange());
				this.totaVo.putParam("L6R22AmtEndRange" + i, tCdBonus.getAmtEndRange());
				this.totaVo.putParam("L6R22Bonus" + i, tCdBonus.getBonus());
				i++;
			}
		}

		/* 將每筆資料放入Tota的OcList */
		this.addList(this.totaVo);
		return this.sendList();
	}

}
