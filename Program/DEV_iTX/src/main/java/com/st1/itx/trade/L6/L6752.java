package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 Year=9,3 #loop{times:13,i:1} Month=9,2 StartDate=9,7
 * EndDate=9,7 #end END=X,1
 */

@Service("L6752")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6752 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdWorkMonthService sCdWorkMonthService;

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
		this.info("active L6752 ");
		this.totaVo.init(titaVo);

		// 功能 1:新增 2:修改 4:刪除 5:查詢
		String FuncCode = titaVo.getParam("FuncCode");
		int iYear = this.parse.stringToInteger(titaVo.getParam("Year"));
		this.info("L6752 iYear : " + iYear);
		int iFYear = iYear + 1911;
		this.info("L6752 iFYear : " + iFYear);

		// 新增
		if (FuncCode.equals("1")) {

			for (int i = 1; i <= 13; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));
				int iStartDate = this.parse.stringToInteger(titaVo.getParam("StartDate" + i));
				int iEndDate = this.parse.stringToInteger(titaVo.getParam("EndDate" + i));
				int iBonusDate = this.parse.stringToInteger(titaVo.getParam("BonusDate" + i)); 

				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdWorkMonth tCdWorkMonth = new CdWorkMonth();
				CdWorkMonthId tCdWorkMonthId = new CdWorkMonthId();
				tCdWorkMonthId.setYear(iFYear);
				tCdWorkMonthId.setMonth(iMonth);
				tCdWorkMonth.setCdWorkMonthId(tCdWorkMonthId);

				tCdWorkMonth.setYear(iFYear);
				tCdWorkMonth.setMonth(iMonth);
				tCdWorkMonth.setStartDate(iStartDate);
				tCdWorkMonth.setEndDate(iEndDate);
				tCdWorkMonth.setBonusDate(iBonusDate);
				tCdWorkMonth.setCreateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdWorkMonth.setCreateEmpNo(titaVo.getTlrNo());
				tCdWorkMonth.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdWorkMonth.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					sCdWorkMonthService.insert(tCdWorkMonth, titaVo);
				} catch (DBException e) {
					if (e.getErrorId() == 2) {
						throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
					} else {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				}
			}

		} else if (FuncCode.equals("2")) {

			for (int i = 1; i <= 13; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));
				int iStartDate = this.parse.stringToInteger(titaVo.getParam("StartDate" + i));
				int iEndDate = this.parse.stringToInteger(titaVo.getParam("EndDate" + i));
				int iBonusDate = this.parse.stringToInteger(titaVo.getParam("BonusDate" + i));
				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdWorkMonth tCdWorkMonth = new CdWorkMonth();
				tCdWorkMonth = sCdWorkMonthService.holdById(new CdWorkMonthId(iFYear, iMonth));
				if (tCdWorkMonth == null) {
					throw new LogicException(titaVo, "E0003", titaVo.getParam("Year") + titaVo.getParam("Month" + i)); // 修改資料不存在
				}

				CdWorkMonth tCdWorkMonth2 = (CdWorkMonth) dataLog.clone(tCdWorkMonth); ////
				try {
					tCdWorkMonth.setStartDate(iStartDate);
					tCdWorkMonth.setEndDate(iEndDate);
					tCdWorkMonth.setBonusDate(iBonusDate);
					tCdWorkMonth.setLastUpdate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
					tCdWorkMonth.setLastUpdateEmpNo(titaVo.getTlrNo());

					tCdWorkMonth = sCdWorkMonthService.update2(tCdWorkMonth, titaVo); ////
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}
				dataLog.setEnv(titaVo, tCdWorkMonth2, tCdWorkMonth); ////
				dataLog.exec("修改放款業績工作月"); ////
			}

		} else if (FuncCode.equals("4")) {

			for (int i = 1; i <= 13; i++) {

				int iMonth = this.parse.stringToInteger(titaVo.getParam("Month" + i));

				// 若該筆無資料就離開迴圈
				if (iMonth == 0) {
					break;
				}

				CdWorkMonth tCdWorkMonth = new CdWorkMonth();
				tCdWorkMonth = sCdWorkMonthService.holdById(new CdWorkMonthId(iFYear, iMonth));
				if (tCdWorkMonth != null) {
					try {
						sCdWorkMonthService.delete(tCdWorkMonth);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0004", titaVo.getParam("Year") + titaVo.getParam("Month" + i)); // 刪除資料不存在
				}
				dataLog.setEnv(titaVo, tCdWorkMonth, tCdWorkMonth); ////
				dataLog.exec("刪除放款業績工作月"); ////
			}

		} else if (!(FuncCode.equals("5"))) {
			throw new LogicException(titaVo, "E0010", "L6752"); // 功能選擇錯誤
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
