package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.domain.CdBonusId;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita FuncCode=9,1 Year=9,3 Month=9,2 #loop{times:20,i:0} PieceCode{i}=X,1
 * ProdNo{i}=X,5 AmtStartRange{i}=m,14 AmtEndRange{i}=m,14 Bonus{i}=m,14 #end
 * END=X,1
 */

@Service("L6751")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6751 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6751.class);

	/* DB服務注入 */
	@Autowired
	public CdBonusService sCdBonusService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6751 ");
		this.totaVo.init(titaVo);

		String iFuncCode = titaVo.getParam("FuncCode");
//		int iYear = this.parse.stringToInteger(titaVo.getParam("Year"));
//		int iMonth = this.parse.stringToInteger(titaVo.getParam("Month"));
//		int iFYear = iYear + 1911;
		
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth"))+191100;

		// 新增 - 每個條件都建一筆
		if (iFuncCode.equals("1") ||iFuncCode.equals("3")) {

			// 每個條件都建一筆
			moveCdBonus(iFuncCode, iWorkMonth, titaVo);

		} else if (iFuncCode.equals("2")) { // 修改 - 因為修改前後筆數不固定 , 所以先全部刪除再建立

			// 先全部刪除
			Slice<CdBonus> slCdBonus;
			slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, Integer.MAX_VALUE);
			List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

			if (lCdBonus == null || lCdBonus.size() == 0) {
				throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
			}

			// 如有找到資料
			for (CdBonus tCdBonus : lCdBonus) {

				CdBonus dCdBonus = new CdBonus();
				dCdBonus = sCdBonusService.holdById(new CdBonusId(iWorkMonth, tCdBonus.getConditionCode(), tCdBonus.getCondition()));

				if (dCdBonus != null) {
					try {
						sCdBonusService.delete(dCdBonus);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", tCdBonus.getCondition()); // 刪除資料不存在
				}

			}

			// 每個條件都建一筆
			moveCdBonus(iFuncCode, iWorkMonth, titaVo);

		} else if (iFuncCode.equals("4")) { // 刪除 - 每個條件一起刪除

			Slice<CdBonus> slCdBonus;
			slCdBonus = sCdBonusService.findYearMonth(iWorkMonth, iWorkMonth, this.index, Integer.MAX_VALUE);
			List<CdBonus> lCdBonus = slCdBonus == null ? null : slCdBonus.getContent();

			if (lCdBonus == null || lCdBonus.size() == 0) {
				throw new LogicException(titaVo, "E0001", "介紹人加碼獎勵津貼標準設定檔"); // 查無資料
			}

			// 如有找到資料
			for (CdBonus tCdBonus : lCdBonus) {

				CdBonus dCdBonus = new CdBonus();
				dCdBonus = sCdBonusService.holdById(new CdBonusId(iWorkMonth, tCdBonus.getConditionCode(), tCdBonus.getCondition()));

				if (dCdBonus != null) {
					try {
						sCdBonusService.delete(dCdBonus);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", tCdBonus.getCondition()); // 刪除資料不存在
				}

			}

		} else if (!(iFuncCode.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6751"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdBonus(String mFuncCode, int mWorkMonth, TitaVo titaVo) throws LogicException {

		String seq;
		String pieceCode;
		BigDecimal amtStartRange;
		BigDecimal amtEndRange;
		BigDecimal bonus;
		int i = 0;

		// 條件記號=1 (篩選條件-計件代碼)
		for (int j = 0; j <= 19; j++) {

			pieceCode = titaVo.getParam("PieceCode" + j);
			this.info("L6751 pieceCode : " + j + "-" + mWorkMonth + "-" + pieceCode);

			if (pieceCode.isEmpty()) {
				break;
			}
			movepieceCode(mWorkMonth, pieceCode, titaVo);

		} // end j

		
		// 條件記號=3 (金額級距)
		for (int l = 0; l <= 19; l++) {

			i++;
			seq = i + "";
			amtStartRange = this.parse.stringToBigDecimal(titaVo.getParam("AmtStartRange" + l));
			amtEndRange = this.parse.stringToBigDecimal(titaVo.getParam("AmtEndRange" + l));
			bonus = this.parse.stringToBigDecimal(titaVo.getParam("Bonus" + l));

			this.info("L6751 amtStartRange : " + l + "-" + mWorkMonth + "-" + amtStartRange);

			if (amtStartRange.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}

			CdBonus tCdBonus = new CdBonus();
			CdBonusId tCdBonusId = new CdBonusId();
			tCdBonusId.setWorkMonth(mWorkMonth);
			tCdBonusId.setConditionCode(3);
			tCdBonusId.setCondition(seq); // 流水序號處理唯一
			tCdBonus.setCdBonusId(tCdBonusId);

			tCdBonus.setAmtStartRange(amtStartRange);
			tCdBonus.setAmtEndRange(amtEndRange);
			tCdBonus.setBonus(bonus);

			tCdBonus.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
			tCdBonus.setCreateEmpNo(titaVo.getTlrNo());
			tCdBonus.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
			tCdBonus.setLastUpdateEmpNo(titaVo.getTlrNo());

			try {
				sCdBonusService.insert(tCdBonus);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

		} // end l


	}

	// 條件記號=1 (篩選條件-計件代碼)
	private void movepieceCode(int aWorkMonth, String apieceCode, TitaVo titaVo) throws LogicException {

		CdBonus tCdBonus = new CdBonus();
		CdBonusId tCdBonusId = new CdBonusId();
		tCdBonusId.setWorkMonth(aWorkMonth);
		tCdBonusId.setConditionCode(1);
		tCdBonusId.setCondition(apieceCode);
		tCdBonus.setCdBonusId(tCdBonusId);

		tCdBonus.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tCdBonus.setCreateEmpNo(titaVo.getTlrNo());
		tCdBonus.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
		tCdBonus.setLastUpdateEmpNo(titaVo.getTlrNo());

		try {
			sCdBonusService.insert(tCdBonus);
		} catch (DBException e) {
			if (e.getErrorId() == 2) {
				throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
			} else {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

}