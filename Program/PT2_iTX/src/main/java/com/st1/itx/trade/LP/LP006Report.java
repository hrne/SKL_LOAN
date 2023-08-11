package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.service.springjpa.cm.LP006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LP006Report extends MakeReport {

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	private PfCoOfficerLogService pfCoOfficerLogService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	LP006ServiceImpl LP006ServiceImpl;

	@Autowired
	private Parse parse;
	
	@Autowired
	public WebClient webClient;

	@Autowired
	private DateUtil dateUtil;

	List<Map<String, String>> fnAllList = new ArrayList<>();

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LP006Report exec ...");

		// 年度
		int workSeasonYY = parse.stringToInteger(titaVo.get("WorkSeasonYY"));
		int workSeasonSS = parse.stringToInteger(titaVo.get("WorkSeasonSS"));
		int pfYear = workSeasonYY + 1911;

		int pfMonths = 0;

		// 判斷季度
		if (workSeasonSS == 1) {
			pfMonths = 1;
		} else if (workSeasonSS == 2) {
			pfMonths = 4;
		} else if (workSeasonSS == 3) {
			pfMonths = 7;
		} else {
			pfMonths = 10;
		}

		// 起日(季初日)
		int effectiveDateS = 0;
		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findById(new CdWorkMonthId(pfYear, pfMonths), titaVo);
		if (tCdWorkMonth != null) {
			effectiveDateS = tCdWorkMonth.getStartDate() + 19110000;
		}

		// 止日(下季起日)
		int effectiveDateE = 0;
		if (workSeasonSS == 1) {
			pfMonths = 4;
		} else if (workSeasonSS == 2) {
			pfMonths = 7;
		} else if (workSeasonSS == 3) {
			pfMonths = 10;
		} else {
			pfYear = pfYear + 1;
			pfMonths = 1;
		}

		tCdWorkMonth = sCdWorkMonthService.findById(new CdWorkMonthId(pfYear, pfMonths), titaVo);
		if (tCdWorkMonth != null) {
			effectiveDateE = tCdWorkMonth.getStartDate() + 19110000;
		}

		// 上次日期列印時間
		String updateDate = "2000-01-01 00:00:00";
		PfCoOfficerLog tPfCoOfficerLog = pfCoOfficerLogService.findEmpEffectiveDateFirst("LP006", effectiveDateS,
				titaVo);
		if (tPfCoOfficerLog != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			updateDate = df.format(tPfCoOfficerLog.getUpdateDate());
		}
		try {
			fnAllList = LP006ServiceImpl.findAll(effectiveDateS, effectiveDateE, updateDate, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP006ServiceImpl.findAll error = " + errors.toString());
		}
		
		exportExcel(titaVo);

		makeExcel.close();

		// 將產生房貸協辦人員異動名單新增至歷程檔
		tPfCoOfficerLog = new PfCoOfficerLog();
		tPfCoOfficerLog.setEmpNo("LP006");
		tPfCoOfficerLog.setEffectiveDate(effectiveDateE);
		tPfCoOfficerLog.setUpdateTlrNo(titaVo.getTlrNo());
		tPfCoOfficerLog
				.setUpdateDate(parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));

		try {
			pfCoOfficerLogService.insert(tPfCoOfficerLog, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "新增歷程資料時發生錯誤");
		}

	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		int reportDate = titaVo.getEntDyI() + 19110000;
		String fileNm = titaVo.get("WorkSeasonYY") + "年第" + titaVo.get("WorkSeasonSS") + "季房貸協辦人員異動名單";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(titaVo.getBrno()).setRptCode("LP006")
				.setRptItem(fileNm).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileNm);

		printExcelHeader();

		int row = 2;
		if (fnAllList == null || fnAllList.size() == 0) {
			makeExcel.setValue(row, 1, "無資料");
		} else {
			for (Map<String, String> t : fnAllList) {
				this.info("t-------->" + t.toString());
				makeExcel.setValue(row, 1, row - 1); // 1, "序號"
				makeExcel.setValue(row, 2, t.get("DeptItem")); // 2, "部室"
				makeExcel.setValue(row, 3, t.get("DistItem")); // 3, "區部"
				makeExcel.setValue(row, 4, t.get("AreaItem")); // 4, "單位"
				makeExcel.setValue(row, 5, t.get("Fullname")); // 5, "姓名"
				makeExcel.setValue(row, 6, t.get("EmpNo")); // 6, "員工代號"
				if (t.get("EmpClass").trim().isEmpty() || t.get("LastEmpClass").trim().isEmpty()
						|| t.get("EmpClass").equals(t.get("LastEmpClass"))) {
					makeExcel.setValue(row, 7, ""); // 7, "考核前職級"
					makeExcel.setValue(row, 8, ""); // 8, "考核後職級"
				} else {
					makeExcel.setValue(row, 7, t.get("LastEmpClass")); // 7, "考核前職級"
					makeExcel.setValue(row, 8, t.get("EmpClass")); // 8, "考核後職級"

				}
				String changeReason = "";
				switch (t.get("FunctionCode")) {
				case "1":
					changeReason = "新增：生效日 " + dateCvt(t.get("EffectiveDate"));
					break;
				case "2":
					changeReason = "修改 ";
					if (!t.get("IneffectiveDate").equals(t.get("LastIneffectiveDate"))) {
						changeReason += ", 停效日:" + dateCvt(t.get("IneffectiveDate"));
					}
					if (!t.get("EmpClass").equals(t.get("LastEmpClass"))) {
						changeReason += ", 協辦等級:" + t.get("EmpClass");
					}
					if (!t.get("ClassPass").equals(t.get("LastClassPass"))) {
						changeReason += ", 初階授信通過:" + t.get("EmpClass");
					}
					if (!t.get("DeptItem").equals(t.get("LastDeptItem"))) {
						changeReason += ", 部室:" + t.get("DeptItem");
					}
					if (!t.get("DistItem").equals(t.get("LastDistItem"))) {
						changeReason += ", 區部:" + t.get("DistItem");
					}
					if (!t.get("AreaItem").equals(t.get("LastAreaItem"))) {
						changeReason += ", 單位:" + t.get("AreaItem");
					}
					break;
				case "4":
					changeReason = "刪除：生效日 " + dateCvt(t.get("EffectiveDate"));
					break;
				case "6":
					changeReason = "離職異動：離退日 " + dateCvt(t.get("IneffectiveDate"));
					break;
				case "7":
					changeReason = "調職異動：調職日 " + dateCvt(t.get("EffectiveDate"));
					break;
				case "8":
					changeReason = "考核職級異動：考核生效日" + dateCvt(t.get("EffectiveDate"));
					break;
				}
				makeExcel.setValue(row, 9, changeReason); // 9, "異動原因"
				row++;
			}
		}
		makeExcel.close();
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "LP006", fileNm, titaVo);

	}

	private String dateCvt(String iDate) throws LogicException {
		String yyyymmdd = parse.IntegerToString((parse.stringToInteger(iDate) - 19110000), 7);
		return yyyymmdd.substring(0, 3) + "/" + yyyymmdd.substring(3, 5) + "/" + yyyymmdd.substring(5, 7);
	}

	private void printExcelHeader() throws LogicException {
		makeExcel.setValue(1, 1, "序號");
		makeExcel.setWidth(1, 8);

		makeExcel.setValue(1, 2, "部室");
		makeExcel.setWidth(2, 20);

		makeExcel.setValue(1, 3, "區部");
		makeExcel.setWidth(3, 20);

		makeExcel.setValue(1, 4, "單位");
		makeExcel.setWidth(4, 20);

		makeExcel.setValue(1, 5, "姓名");
		makeExcel.setWidth(5, 20);

		makeExcel.setValue(1, 6, "員工代號");
		makeExcel.setWidth(6, 20);

		makeExcel.setValue(1, 7, "考核前職級");
		makeExcel.setWidth(7, 20);

		makeExcel.setValue(1, 8, "考核後職級");
		makeExcel.setWidth(8, 20);

		makeExcel.setValue(1, 9, "異動原因");
		makeExcel.setWidth(9, 40);
	}
}
