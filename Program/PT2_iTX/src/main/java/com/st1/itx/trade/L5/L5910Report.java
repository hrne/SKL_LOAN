package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.springjpa.cm.L5910ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("L5910Report")
@Scope("prototype")

public class L5910Report extends MakeReport {

	@Autowired
	public PfBsDetailService iPfBsDetailService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	public CdCodeService iCdCodeService;

	@Autowired
	public CdWorkMonthService iCdWorkMonthService;

	@Autowired
	public L5910ServiceImpl iL5910ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public long exec(TitaVo titaVo) throws LogicException {

		this.info("L5910Report start success");

		String iDrawDateFm = titaVo.getParam("DateFm");
		String iDrawDateTo = titaVo.getParam("DateTo");
		String iCDate = titaVo.getCalDy();

		List<String> header = new ArrayList<>();
		List<String> reportHeader = new ArrayList<>();
		header.addAll(Arrays.asList("工作月", "撥款日", "戶號", "額度", "撥款", "撥款金額", "放款餘額", "撥款利率", "目前利率", "核准利率", "商品代碼", "商品名稱", "利率加碼", "計件代碼", "押品地區", "房專姓名", "房貸部專", "撥款金額*撥款利率"));
		reportHeader.addAll(Arrays.asList("排行", "部  室", "姓 名", "平均利率"));
		// 抓工作月
		CdWorkMonth iCdWorkMonthSt = new CdWorkMonth();
		CdWorkMonth iCdWorkMonthEd = new CdWorkMonth();
		iCdWorkMonthSt = iCdWorkMonthService.findDateFirst(Integer.valueOf(iDrawDateFm) + 19110000, Integer.valueOf(iDrawDateFm) + 19110000, titaVo);
		if (iCdWorkMonthSt == null) {
			throw new LogicException(titaVo, "E0001", "工作月起");
		}
		iCdWorkMonthEd = iCdWorkMonthService.findDateFirst(Integer.valueOf(iDrawDateTo) + 19110000, Integer.valueOf(iDrawDateTo) + 19110000, titaVo);
		if (iCdWorkMonthEd == null) {
			throw new LogicException(titaVo, "E0001", "工作月迄");
		}
		String iYearSt = StringUtils.leftPad(String.valueOf(iCdWorkMonthSt.getYear() - 1911), 3, '0');
		String iMonthSt = StringUtils.leftPad(String.valueOf(iCdWorkMonthSt.getMonth()), 2, '0');
		String iYearEd = StringUtils.leftPad(String.valueOf(iCdWorkMonthEd.getYear() - 1911), 3, '0');
		String iMonthEd = StringUtils.leftPad(String.valueOf(iCdWorkMonthEd.getMonth()), 2, '0');
		List<Map<String, String>> t5910SqlReturn = new ArrayList<Map<String, String>>();
		List<Map<String, String>> d5910SqlReturn = new ArrayList<Map<String, String>>();
		List<Map<String, String>> x5910SqlReturn = new ArrayList<Map<String, String>>();
		String fileName = iYearSt + iMonthSt + "到" + iYearEd + iMonthEd + "工作月首撥加權平均利率排行-" + iCDate;

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5910", fileName, fileName);

		try {
			t5910SqlReturn = iL5910ServiceImpl.findDetail(Integer.valueOf(iDrawDateFm) + 19110000, Integer.valueOf(iDrawDateTo) + 19110000, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (t5910SqlReturn == null || t5910SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "查無明細資料");
		}
		try {
			d5910SqlReturn = iL5910ServiceImpl.findResult(Integer.valueOf(iDrawDateFm) + 19110000, Integer.valueOf(iDrawDateTo) + 19110000, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (d5910SqlReturn == null || d5910SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "查無報表資料");
		}
		try {
			x5910SqlReturn = iL5910ServiceImpl.findResultAdd(Integer.valueOf(iDrawDateFm) + 19110000, Integer.valueOf(iDrawDateTo) + 19110000, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (x5910SqlReturn == null || x5910SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "查無報表資料");
		}
		// 列數
		int row = 1;

		// 表頭列數
		int hcol = 0;

		// 明細
		makeExcel.setSheet("L5910", "明細");
		for (String content : header) {
			makeExcel.setFontType(1);
			switch (hcol + 1) {
			case 4:
				makeExcel.setColor("Blue");
				break;
			case 5:
				makeExcel.setBackGroundColor("Orange");
				makeExcel.setColor("White");
				break;
			case 7:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			case 8:
				makeExcel.setBackGroundColor("Orange");
				makeExcel.setColor("White");
				break;
			case 9:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			case 10:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			case 16:
				makeExcel.setBackGroundColor("Blue");
				makeExcel.setColor("Black");
				break;
			case 17:
				makeExcel.setBackGroundColor("Blue");
				break;
			default:
				makeExcel.setBackGroundColor("Grey");
				break;
			}
			makeExcel.setValue(row, hcol + 1, content, "C");
			hcol++;
		}
		for (Map<String, String> s5910SqlReturn : t5910SqlReturn) {
			row++;
			for (int col = 0; col < s5910SqlReturn.size(); col++) {
				switch (col) {
				// 處裡左右靠
				case 0: // 工作月
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5910SqlReturn.get("WorkMonth")) - 191100);
					break;
				case 1: // 撥款日
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5910SqlReturn.get("DrawdownDate")) - 19110000);
					break;
				case 2: // 戶號
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5910SqlReturn.get("CustNo")), "R");
					break;
				case 3: // 額度
//					makeExcel.setColor("Blue");
//					makeExcel.setValue(row, col + 1, Integer.valueOf(s5910SqlReturn.get("DrawdownAmt")),"#,##0");
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("FacmNo"), "C");
					break;
				case 4: // 撥款序號
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("BormNo"), "C");
					break;
				case 5: // 撥款金額
					makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("DrawdownAmt")), "#,##0");
					break;
				case 6: // 放款餘額
					makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("LoanBal")), "#,##0");
					break;
				case 7: // 撥款利率
					makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("LoanApproveRate")), "#0.0000", "C");
					break;
				case 8: // 目前利率
					makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("FitRate")), "#0.0000", "C");
					break;
				case 9: // 核准利率
					makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("FacApproveRate")), "#0.0000", "C");
					break;
				case 10: // 商品代號
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("ProdCode"), "C");
					break;
				case 11: // 商品名稱
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("ProdName"), "C");
					break;
				case 12: // 利率加碼
					if (("Y").equals(s5910SqlReturn.get("IncrFlag"))) {
						makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("LRCRateIncr")), "#0.0000", "C");
					} else {
						makeExcel.setValue(row, col + 1, getBigDecimal(s5910SqlReturn.get("IndividualIncr")), "#0.0000", "C");
					}
					break;
				case 13: // 計件代碼
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("PieceCode"), "C");
					break;
				case 14: // 押品地區
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("RegCityCode"), "C");
					break;
				case 15: // 房專姓名
					makeExcel.setValue(row, col + 1, s5910SqlReturn.get("Fullname"), "C");
					break;
				case 16: // 房貸部專

					switch (s5910SqlReturn.get("DeptCode")) {
					case "A0B000": // 營管
						makeExcel.setColor("Red");
						makeExcel.setValue(row, col + 1, 1, "C");
						break;
					case "A0F000": // 營推
						makeExcel.setColor("Red");
						makeExcel.setValue(row, col + 1, 2, "C");
						break;
					case "A0E000": // 業推
						makeExcel.setColor("Red");
						makeExcel.setValue(row, col + 1, 3, "C");
						break;
					case "A0M000": // 業開
						makeExcel.setColor("Red");
						makeExcel.setValue(row, col + 1, 4, "C");
						break;
					}
					break;
				case 17: // 撥款金額*撥款利率
					makeExcel.setValue(row, col + 1,
							getBigDecimal(s5910SqlReturn.get("DrawdownAmt")).multiply(getBigDecimal(s5910SqlReturn.get("LoanApproveRate"))).setScale(0, BigDecimal.ROUND_HALF_UP), "#,##0", "R");
					break;
				}
			}
		}
		makeExcel.setWidth(3, 14);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 18);
		makeExcel.setWidth(8, 14);
		makeExcel.setWidth(9, 14);
		makeExcel.setWidth(10, 14);
		makeExcel.setWidth(11, 14);
		makeExcel.setWidth(12, 18);
		makeExcel.setWidth(13, 14);
		makeExcel.setWidth(14, 14);
		makeExcel.setWidth(15, 14);
		makeExcel.setWidth(16, 14);
		makeExcel.setWidth(17, 14);
		makeExcel.setWidth(18, 18);
		// 報表
		// 列數
		row = 1;

		// 表頭列數
		hcol = 0;

		makeExcel.newSheet("報表");
		for (String content : reportHeader) {
			makeExcel.setValue(row, hcol + 1, content, "C");
			hcol++;
		}
		for (Map<String, String> a5910SqlReturn : d5910SqlReturn) {
			row++;
			for (int col = 0; col < a5910SqlReturn.size(); col++) {

				switch (col) {
				// 處裡左右靠
				case 0:
					makeExcel.setValue(row, col + 1, a5910SqlReturn.get("sort"), "C");
					break;
				case 1:
					makeExcel.setValue(row, col + 1, a5910SqlReturn.get("DepItem"), "C");
					break;
				case 2:
					makeExcel.setColor("Blue");
					makeExcel.setValue(row, col + 1, a5910SqlReturn.get("Fullname"), "C");
					break;
				case 3:
					makeExcel.setColor("Blue");
					makeExcel.setValue(row, col + 1, getBigDecimal(a5910SqlReturn.get("Divid")).setScale(4, BigDecimal.ROUND_HALF_UP), "#0.0000");
					break;
				}
			}
		}
		for (Map<String, String> b5910SqlReturn : x5910SqlReturn) {
			row++;
			for (int col = 0; col < b5910SqlReturn.size(); col++) {

				switch (col) {
				// 處裡左右靠
				case 0:
					makeExcel.setValue(row, col + 1, b5910SqlReturn.get("sort"), "C");
					break;
				case 1:
					switch (b5910SqlReturn.get("DepItem")) {
					case "業務開發部":
						makeExcel.setValue(row, col + 1, "業開部部專", "C");
						break;
					case "業務推展部":
						makeExcel.setValue(row, col + 1, "業推部部專", "C");
						break;
					case "營業推展部":
						makeExcel.setValue(row, col + 1, "營推部部專", "C");
						break;
					case "營業管理部":
						makeExcel.setValue(row, col + 1, "營管部部專", "C");
						break;
					}

					break;
				case 2:
					switch (b5910SqlReturn.get("DepItem")) {
					case "業務開發部":
						makeExcel.setColor("Blue");
						makeExcel.setValue(row, col + 1, "蔡丞耿", "C");
						break;
					case "業務推展部":
						makeExcel.setColor("Blue");
						makeExcel.setValue(row, col + 1, "涂國祥", "C");
						break;
					case "營業推展部":
						makeExcel.setColor("Blue");
						makeExcel.setValue(row, col + 1, "傅子榮", "C");
						break;
					case "營業管理部":
						makeExcel.setColor("Blue");
						makeExcel.setValue(row, col + 1, "黃國安", "C");
						break;
					}
					break;
				case 3:
					makeExcel.setColor("Blue");
					makeExcel.setValue(row, col + 1, getBigDecimal(b5910SqlReturn.get("Divid")).setScale(4, BigDecimal.ROUND_HALF_UP), "#0.0000");
					break;
				}
			}
			makeExcel.setWidth(4, 18);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return sno;
	}

}
