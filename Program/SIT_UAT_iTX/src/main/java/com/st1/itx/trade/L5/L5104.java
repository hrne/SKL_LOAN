package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5904ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * StartDate=9,7<br>
 * EndDate=9,7<br>
 * reportFlag=9,1<br>
 * END=X,1<br>
 */

@Service("L5104")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5104 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5104.class);

	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TotaVo totaVoA;

	@Autowired
	public TotaVo totaVoB;

	@Autowired
	public TotaVo totaVoC;

	@Autowired
	public L5904ServiceImpl l5904ServiceImpl;

	private int reportFlag = 0;
	private int startDate = 0;
	private int endDate = 0;

	private int reportACnt = 0;
	private int reportBCnt = 0;
	private int reportCCnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5104 ");
		this.totaVo.init(titaVo);

//		 1:未歸還月報表;
//	     2:法拍件月報表;
//	     3:件數統計表;
//	     4:全部
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		reportFlag = parse.stringToInteger(titaVo.getParam("ReportFlag"));
		startDate = parse.stringToInteger(titaVo.getParam("StartDate")) + 19110000;
		endDate = parse.stringToInteger(titaVo.getParam("EndDate")) + 19110000;

		switch (reportFlag) {
		case 1:
			setReportA(titaVo);

			if (reportACnt == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料 ");
			}
			break;
		case 2:
			setReportB(titaVo);

			if (reportBCnt == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料 ");
			}
			break;
		case 3:
			setReportC(titaVo);

			if (reportCCnt == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料 ");
			}
			break;
		case 4:
			setReportA(titaVo);
			setReportB(titaVo);
			setReportC(titaVo);

			if (reportACnt + reportBCnt + reportCCnt == 0) {
				throw new LogicException(titaVo, "E0001", "查無資料 ");
			}
			break;
		}

		totaVoA.putParam("BackACnt", reportACnt);
		totaVoA.putParam("MSGID", "L514A");
		this.addList(totaVoA);

		totaVoB.putParam("BackBCnt", reportBCnt);
		totaVoB.putParam("MSGID", "L514B");
		this.addList(totaVoB);

		totaVoC.putParam("BackCCnt", reportCCnt);
		totaVoC.putParam("MSGID", "L514C");
		this.addList(totaVoC);

//		this.addList(this.totaVo);
		return this.sendList();
	}

//	 1:未歸還月報表;
	private void setReportA(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l5904ServiceImpl.findAll(startDate, endDate, "00", this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5904ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursListReport = new OccursList();
				occursListReport.putParam("ReportACustNo", result.get("F0"));
				occursListReport.putParam("ReportAFacmNo", result.get("F1"));
				occursListReport.putParam("ReportAUsageCode", result.get("F9"));
				occursListReport.putParam("ReportAApplEmpNo", result.get("F5"));
				occursListReport.putParam("ReportAKeeperEmpNo", result.get("F4"));

				totaVoA.addOccursList(occursListReport);
			}
			reportACnt = resultList.size();
		}
	}

//  2:法拍件月報表;
	private void setReportB(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l5904ServiceImpl.findAll(startDate, endDate, "02", this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5904ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursListReport = new OccursList();
				occursListReport.putParam("ReportBCustNo", result.get("F0"));
				occursListReport.putParam("ReportBFacmNo", result.get("F1"));
				occursListReport.putParam("ReportBApplEmpNo", result.get("F5"));
				occursListReport.putParam("ReportBKeeperEmpNo", result.get("F4"));

				totaVoB.addOccursList(occursListReport);
			}

			reportBCnt = resultList.size();
		}

	}

//  3:件數統計表;
	private void setReportC(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		try {
			// *** 折返控制相關 ***
			resultList = l5904ServiceImpl.findAll(startDate, endDate, "00", this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5904ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {
			int cnt1 = 0;
			int cnt2 = 0;
			int cnt3 = 0;
			int cnt4 = 0;
			int cnt5 = 0;
			int cnt6 = 0;
			int cnt7 = 0;
			int cnt8 = 0;
			for (Map<String, String> result : resultList) {
				if ("01".equals(result.get("F9"))) {
					cnt1 = cnt1 + 1;
				}
				if ("02".equals(result.get("F9"))) {
					cnt2 = cnt2 + 1;
				}
				if ("03".equals(result.get("F9"))) {
					cnt3 = cnt3 + 1;

				}
				if ("04".equals(result.get("F9"))) {
					cnt4 = cnt4 + 1;

				}
				if ("05".equals(result.get("F9"))) {
					cnt5 = cnt5 + 1;

				}
				if ("06".equals(result.get("F9"))) {
					cnt6 = cnt6 + 1;

				}
				if ("07".equals(result.get("F9"))) {
					cnt7 = cnt7 + 1;

				}
				if ("08".equals(result.get("F9"))) {
					cnt8 = cnt8 + 1;

				}
			}

			setUsageCnt(cnt1, "01");
			setUsageCnt(cnt2, "02");
			setUsageCnt(cnt3, "03");
			setUsageCnt(cnt4, "04");
			setUsageCnt(cnt5, "05");
			setUsageCnt(cnt6, "06");
			setUsageCnt(cnt7, "07");
			setUsageCnt(cnt8, "08");

			reportCCnt = resultList.size();
		}
	}

	private void setUsageCnt(int cnt, String usageCode) {
		OccursList occursListReport = new OccursList();

		if (cnt > 0) {
			occursListReport.putParam("ReportCUsageCode", usageCode);
			occursListReport.putParam("ReportCUsageCnt", cnt);

			totaVoC.addOccursList(occursListReport);
		}
	}
}