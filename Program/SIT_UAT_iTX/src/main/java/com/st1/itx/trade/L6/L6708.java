package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBudget;
import com.st1.itx.db.domain.CdBudgetId;
import com.st1.itx.db.service.CdBudgetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 Year=9,3 #loop{times:12,i:1} Month=9,2 Budget=m,14 #end
 * END=X,1
 */

@Service("L6708")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6708 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBudgetService sCdBudgetService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6708 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String FuncCode = titaVo.getParam("FuncCode");
		int iYear = this.parse.stringToInteger(titaVo.getParam("Year"));
		this.info("L6708 iYear : " + iYear);
		int iFYear = iYear + 1911;
		this.info("L6708 iFYear : " + iFYear);

		// 新增
		if (FuncCode.equals("1")) {

			for (int i = 1; i <= 12; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));

				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdBudget tCdBudget = new CdBudget();
				CdBudgetId tCdBudgetId = new CdBudgetId();
				tCdBudgetId.setYear(iFYear);
				tCdBudgetId.setMonth(iMonth);
				tCdBudget.setCdBudgetId(tCdBudgetId);

				tCdBudget.setYear(iFYear);
				tCdBudget.setMonth(iMonth);
				tCdBudget.setBudget(this.parse.stringToBigDecimal(titaVo.getParam("Budget" + i)));

				tCdBudget.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdBudget.setCreateEmpNo(titaVo.getTlrNo());
				tCdBudget.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdBudget.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sCdBudgetService.insert(tCdBudget, titaVo);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", iFYear + "-" + iMonth); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (FuncCode.equals("2")) {

			for (int i = 1; i <= 12; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));

				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdBudget tCdBudget = new CdBudget();
				tCdBudget = sCdBudgetService.holdById(new CdBudgetId(iFYear, iMonth));
				if (tCdBudget == null) {
					throw new LogicException(titaVo, "E0003", iFYear + "-" + iMonth); // 修改資料不存在
				}

				CdBudget tCdBudget2 = (CdBudget) dataLog.clone(tCdBudget); ////
				try {
					tCdBudget.setBudget(this.parse.stringToBigDecimal(titaVo.getParam("Budget" + i)));
					tCdBudget.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
					tCdBudget.setLastUpdateEmpNo(titaVo.getTlrNo());

					tCdBudget = sCdBudgetService.update2(tCdBudget, titaVo); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tCdBudget2, tCdBudget); ////
				dataLog.exec("修改利息收入預算數"); ////
			}

		} else if (FuncCode.equals("4")) {

			for (int i = 1; i <= 12; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));

				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdBudget tCdBudget = new CdBudget();
				tCdBudget = sCdBudgetService.holdById(new CdBudgetId(iFYear, iMonth));
				if (tCdBudget != null) {
					try {
						sCdBudgetService.delete(tCdBudget);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", iFYear + "-" + iMonth); // 刪除資料不存在
				}
			}

		} else if (!(FuncCode.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6708"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}