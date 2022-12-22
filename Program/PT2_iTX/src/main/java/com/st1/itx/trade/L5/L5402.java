package com.st1.itx.trade.L5;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.report.ReportUtil;

/**
 * 年度業績目標更新<BR>
 * 2022-12-22 Wei refactor<BR>
 * 1. 解掉巢狀while(true)+switch造成的業務邏輯解讀困難<BR>
 * 2. User不同意增加部室、區部的代碼欄位<BR>
 * 
 * @author Fegie
 * @version 1.0.0
 */
@Service("L5402")
@Scope("prototype")
public class L5402 extends TradeBuffer {

	@Autowired
	private DateUtil iDateUtil;

	@Autowired
	private PfBsOfficerService sPfBsOfficerService;

	@Autowired
	private CdBcmService sCdBcmService;

	@Autowired
	private CdEmpService sCdEmpService;

	@Autowired
	private DataLog dataLog;

	@Autowired
	private MakeExcel makeExcel;

	@Autowired
	private ReportUtil rptUtil;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int bcYear = 0;

	private List<PfBsOfficer> listPfBsOfficer;

	private int deptIndex = 0; // 服務部室-欄位序號
	private int distIndex = 0; // 服務區部-欄位序號
	private int bsNameIndex = 0; // 專員-欄位序號
	private int empNoIndex = 0; // 員編-欄位序號
	private int workMonth1Index = 0; // 第1工作月-欄位序號
	private int workMonth2Index = 0; // 第2工作月-欄位序號
	private int workMonth3Index = 0; // 第3工作月-欄位序號
	private int workMonth4Index = 0; // 第4工作月-欄位序號
	private int workMonth5Index = 0; // 第5工作月-欄位序號
	private int workMonth6Index = 0; // 第6工作月-欄位序號
	private int workMonth7Index = 0; // 第7工作月-欄位序號
	private int workMonth8Index = 0; // 第8工作月-欄位序號
	private int workMonth9Index = 0; // 第9工作月-欄位序號
	private int workMonth10Index = 0; // 第10工作月-欄位序號
	private int workMonth11Index = 0; // 第11工作月-欄位序號
	private int workMonth12Index = 0; // 第12工作月-欄位序號
	private int workMonth13Index = 0; // 第13工作月-欄位序號
	private int endRowIndex = 0; // 結束行-行序號(目前以"部專合計")

	private String deptCode = ""; // 部室代號
	private String deptItem = ""; // 部室中文
	private String distCode = ""; // 區部代號
	private String distItem = ""; // 區部中文
	private String empNo = ""; // 員編
	private String empName = ""; // 員工姓名
	private String areaCode = "";
	private String areaItem = "";

	private BigDecimal tenThousand = new BigDecimal("10000");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5402 ");
		this.totaVo.init(titaVo);

		iDateUtil.init();

		String iYear = titaVo.getParam("UploadYear");

		bcYear = (Integer.parseInt(iYear) + 1911 * 100);

		String iFileName = inFolder + iDateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

//		int iUploadFg = Integer.parseInt(titaVo.getParam("UploadFg"));

		this.info("iFileName = " + iFileName);

		if (iFileName.equals("")) {
			throw new LogicException(titaVo, "E0014", "上傳檔案為空，請選擇 .xls之Excel檔案");
		} else {
			if (!iFileName.contains(".xls")) {
				throw new LogicException(titaVo, "E0014", "上傳檔案類型錯誤，請選擇 .xls之Excel檔案");
			}
		}

		makeExcel.openExcel(iFileName, 1);

		// 檢核必須存在的欄位
		checkColumn(titaVo);

		// 檢核並取得明細資料
		checkAndGetDetail(titaVo);

		// 寫入資料
		modifyData(titaVo);

		this.info(titaVo.getParam("FILENA").trim() + "資料新增成功");

		totaVo.putParam("OSuccessFlag", 1);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkColumn(TitaVo titaVo) throws LogicException {
		int headerRow = 3;
		for (int column = 0; column <= 18; column++) {
			String columnName = getExcelValueInString(headerRow, column);
			setColumnIndex(columnName, column);
		}
		// 目前資料大約總共35行
		// 若搜到1000行都沒有專員合計就當作此檔案不是我們要的檔案
		for (int row = 0; row <= 1000; row++) {
			String value = getExcelValueInString(row, 1);
			if (value.equals("部專合計")) {
				endRowIndex = row;
			}
		}
		// 這些欄位必須存在
		checkColumnIndex(titaVo);
	}

	private String getExcelValueInString(int row, int col) throws LogicException {
		Object value = makeExcel.getValue(row, col);
		return value == null ? "" : value.toString().trim();
	}

	private void setColumnIndex(String columnName, int column) {
		// 原本ST1-嘉榮設計部室跟區部要請User增加欄位輸入代號
		// User不同意,強調不會打錯字
		switch (columnName) {
		case "服務部室":
			deptIndex = column;
			break;
		case "服務區部":
			distIndex = column;
			break;
		case "專員":
			bsNameIndex = column;
			break;
		case "員編":
			empNoIndex = column;
			break;
		case "第1工作月":
			workMonth1Index = column;
			break;
		case "第2工作月":
			workMonth2Index = column;
			break;
		case "第3工作月":
			workMonth3Index = column;
			break;
		case "第4工作月":
			workMonth4Index = column;
			break;
		case "第5工作月":
			workMonth5Index = column;
			break;
		case "第6工作月":
			workMonth6Index = column;
			break;
		case "第7工作月":
			workMonth7Index = column;
			break;
		case "第8工作月":
			workMonth8Index = column;
			break;
		case "第9工作月":
			workMonth9Index = column;
			break;
		case "第10工作月":
			workMonth10Index = column;
			break;
		case "第11工作月":
			workMonth11Index = column;
			break;
		case "第12工作月":
			workMonth12Index = column;
			break;
		case "第13工作月":
			workMonth13Index = column;
			break;
		default:
			break;
		}
	}

	private void checkColumnIndex(TitaVo titaVo) throws LogicException {
		if (deptIndex == 0) {
			throw new LogicException(titaVo, "E0014", "服務部室，此欄位必須存在");
		}
		if (distIndex == 0) {
			throw new LogicException(titaVo, "E0014", "服務區部，此欄位必須存在");
		}
		if (bsNameIndex == 0) {
			throw new LogicException(titaVo, "E0014", "專員，此欄位必須存在");
		}
		if (empNoIndex == 0) {
			throw new LogicException(titaVo, "E0014", "員編，此欄位必須存在");
		}
		if (workMonth1Index == 0) {
			throw new LogicException(titaVo, "E0014", "第1工作月，此欄位必須存在");
		}
		if (workMonth2Index == 0) {
			throw new LogicException(titaVo, "E0014", "第2工作月，此欄位必須存在");
		}
		if (workMonth3Index == 0) {
			throw new LogicException(titaVo, "E0014", "第3工作月，此欄位必須存在");
		}
		if (workMonth4Index == 0) {
			throw new LogicException(titaVo, "E0014", "第4工作月，此欄位必須存在");
		}
		if (workMonth5Index == 0) {
			throw new LogicException(titaVo, "E0014", "第5工作月，此欄位必須存在");
		}
		if (workMonth6Index == 0) {
			throw new LogicException(titaVo, "E0014", "第6工作月，此欄位必須存在");
		}
		if (workMonth7Index == 0) {
			throw new LogicException(titaVo, "E0014", "第7工作月，此欄位必須存在");
		}
		if (workMonth8Index == 0) {
			throw new LogicException(titaVo, "E0014", "第8工作月，此欄位必須存在");
		}
		if (workMonth9Index == 0) {
			throw new LogicException(titaVo, "E0014", "第9工作月，此欄位必須存在");
		}
		if (workMonth10Index == 0) {
			throw new LogicException(titaVo, "E0014", "第10工作月，此欄位必須存在");
		}
		if (workMonth11Index == 0) {
			throw new LogicException(titaVo, "E0014", "第11工作月，此欄位必須存在");
		}
		if (workMonth12Index == 0) {
			throw new LogicException(titaVo, "E0014", "第12工作月，此欄位必須存在");
		}
		if (workMonth13Index == 0) {
			throw new LogicException(titaVo, "E0014", "第13工作月，此欄位必須存在");
		}
		if (endRowIndex == 0) {
			throw new LogicException(titaVo, "E0014", "部專合計，此欄位必須存在");
		}
	}

	private void checkAndGetDetail(TitaVo titaVo) throws LogicException {
		String deptName = "";
		listPfBsOfficer = new ArrayList<>();
		for (int detailRowIndex = 5; detailRowIndex < endRowIndex; detailRowIndex++) {
			empNo = getExcelValueInString(detailRowIndex, empNoIndex);
			if (empNo == null || empNo.isEmpty()) {
				continue;
			}
			String tempDeptName = getExcelValueInString(detailRowIndex, deptIndex);
			String distName = getExcelValueInString(detailRowIndex, distIndex);
			if (tempDeptName != null && !tempDeptName.isEmpty()) {
				deptName = tempDeptName;
			}
			if (distName == null || distName.isEmpty()) {
				continue;
			}
			// 檢核明細
			checkDetail(deptName, distName, empNo, titaVo);

			setAreaCenter();

			addPfBsOfficer(1, getAmt(detailRowIndex, workMonth1Index));
			addPfBsOfficer(2, getAmt(detailRowIndex, workMonth2Index));
			addPfBsOfficer(3, getAmt(detailRowIndex, workMonth3Index));
			addPfBsOfficer(4, getAmt(detailRowIndex, workMonth4Index));
			addPfBsOfficer(5, getAmt(detailRowIndex, workMonth5Index));
			addPfBsOfficer(6, getAmt(detailRowIndex, workMonth6Index));
			addPfBsOfficer(7, getAmt(detailRowIndex, workMonth7Index));
			addPfBsOfficer(8, getAmt(detailRowIndex, workMonth8Index));
			addPfBsOfficer(9, getAmt(detailRowIndex, workMonth9Index));
			addPfBsOfficer(10, getAmt(detailRowIndex, workMonth10Index));
			addPfBsOfficer(11, getAmt(detailRowIndex, workMonth11Index));
			addPfBsOfficer(12, getAmt(detailRowIndex, workMonth12Index));
			addPfBsOfficer(13, getAmt(detailRowIndex, workMonth13Index));
		}
	}

	private void checkDetail(String deptName, String distName, String empNo, TitaVo titaVo) throws LogicException {
		switch (deptName) {
		case "營管部":
			deptCode = "A0B000";
			deptItem = "營業管理部";
			break;
		case "營推部":
			deptCode = "A0F000";
			deptItem = "營業推展部";
			break;
		case "業推部":
			deptCode = "A0E000";
			deptItem = "業務推展部";
			break;
		case "業開部":
			deptCode = "A0M000";
			deptItem = "業務開發部";
			break;
		default:
			throw new LogicException(titaVo, "E0014", "服務部室，查無對應代碼(" + deptName + ")，限輸入營管部、營推部、業推部、業開部");
		}
		if (!distName.equals("部專")) {
			CdBcm tCdBcm = sCdBcmService.distItemFirst(distName, titaVo);
			if (tCdBcm == null || tCdBcm.getDistCode() == null) {
				throw new LogicException(titaVo, "E0014", "服務區部，查無對應代碼(" + distName + ")");
			} else {
				distCode = tCdBcm.getDistCode();
				distItem = tCdBcm.getDistItem();
			}
		} else {
			distItem = "房貸部專";
		}

		CdEmp tCdEmp = sCdEmpService.findById(empNo, titaVo);
		if (tCdEmp == null || tCdEmp.getEmployeeNo() == null) {
			throw new LogicException(titaVo, "E0014", "員編，查無員工資料(" + empNo + ")");
		} else {
			empName = tCdEmp.getFullname();
		}
	}

	private void setAreaCenter() {
		switch (deptCode) {
		case "A0B000":
		case "A0F000":
			areaCode = "10HC00";
			areaItem = "北部區域中心";
			break;
		case "A0E000":
			areaCode = "10HJ00";
			areaItem = "中部區域中心";
			break;
		case "A0M000":
			areaCode = "10HL00";
			areaItem = "南部區域中心";
			break;
		default:
			areaCode = "";
			areaItem = "";
			break;
		}
	}

	private void addPfBsOfficer(int i, BigDecimal amt) {
		PfBsOfficerId pfBsOfficerId = new PfBsOfficerId();
		pfBsOfficerId.setEmpNo(empNo);
		pfBsOfficerId.setWorkMonth(bcYear + i);
		PfBsOfficer pfBsOfficer = new PfBsOfficer();
		pfBsOfficer.setPfBsOfficerId(pfBsOfficerId);
		pfBsOfficer.setFullname(empName); // 員工姓名
		pfBsOfficer.setAreaCode(areaCode); // 區域中心
		pfBsOfficer.setAreaItem(areaItem); // 中心中文
		pfBsOfficer.setDeptCode(deptCode); // 部室代號
		pfBsOfficer.setDepItem(deptItem); // 部室中文
		pfBsOfficer.setDistCode(distCode); // 區部代號
		pfBsOfficer.setDistItem(distItem); // 區部中文
		pfBsOfficer.setGoalAmt(amt); // 目標金額
		listPfBsOfficer.add(pfBsOfficer);
	}

	private BigDecimal getAmt(int rowIndex, int columnIndex) throws LogicException {
		return rptUtil.getBigDecimal(getExcelValueInString(rowIndex, columnIndex)).multiply(tenThousand);
	}

	private void modifyData(TitaVo titaVo) throws LogicException {
		if (listPfBsOfficer == null || listPfBsOfficer.isEmpty()) {
			return;
		}
		for (PfBsOfficer pfBsOfficer : listPfBsOfficer) {
			PfBsOfficer tempPfBsOfficer = sPfBsOfficerService.holdById(pfBsOfficer, titaVo);
			if (tempPfBsOfficer == null) {
				try {
					sPfBsOfficerService.insert(pfBsOfficer, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增錯誤訊息
				}
			} else {
				// 變更前
				PfBsOfficer beforePfBsOfficer = (PfBsOfficer) dataLog.clone(tempPfBsOfficer);

				tempPfBsOfficer.setFullname(pfBsOfficer.getFullname()); // 員工姓名
				tempPfBsOfficer.setAreaCode(pfBsOfficer.getAreaCode()); // 區域中心
				tempPfBsOfficer.setAreaItem(pfBsOfficer.getAreaItem()); // 中心中文
				tempPfBsOfficer.setDeptCode(pfBsOfficer.getDeptCode()); // 部室代號
				tempPfBsOfficer.setDepItem(pfBsOfficer.getDepItem()); // 部室中文
				tempPfBsOfficer.setDistCode(pfBsOfficer.getDistCode()); // 區部代號
				tempPfBsOfficer.setDistItem(pfBsOfficer.getDistItem()); // 區部中文
				tempPfBsOfficer.setGoalAmt(pfBsOfficer.getGoalAmt()); // 目標金額
				try {
					tempPfBsOfficer = sPfBsOfficerService.update2(tempPfBsOfficer, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料更新錯誤
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforePfBsOfficer, tempPfBsOfficer);
				dataLog.exec();
			}
		}
	}
}