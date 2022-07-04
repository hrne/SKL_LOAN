package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM062ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM065ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM065Report extends MakeReport {

	@Autowired
	LM065ServiceImpl lm065ServiceImpl;

	@Autowired
	LM062ServiceImpl lm062ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		this.info("LM065Report exec");

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String txCD = "LM065";
		String itemName = "04-個金100萬以上小於2000萬";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, itemName + "-" + iYearMonth,
				txCD + "_" + itemName + "-" + iYearMonth, "LM065_底稿_個金100萬以上小於2000萬.xls", "簡表");
		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 15);
		makeExcel.setWidth(5, 15);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 20);
		makeExcel.setWidth(9, 20);
		makeExcel.setWidth(10, 15);
		makeExcel.setWidth(11, 15);
		makeExcel.setWidth(12, 15);
		makeExcel.setWidth(13, 15);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 12, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = lm065ServiceImpl.findAll(titaVo, yearMonth);

			// 共用LM062Impl
			fnAllList2 = lm062ServiceImpl.findList(titaVo, yearMonth, 4);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM065ServiceImpl.findAll error = " + errors.toString());

		}

		// 資料比數是否大於0筆
		if (fnAllList.size() > 0) {

			// 初始列
			int row = 2;
			String tempNo = "";
			String fdnm = "";

			// 總計
			BigDecimal tot = new BigDecimal("0");

			// 撥款日期
			int day = 0;
			int lday = 0;
			String date = "";

			for (Map<String, String> tLDVo : fnAllList) {

				row++;

				for (int col = 0; col < tLDVo.size(); col++) {

					fdnm = "F" + String.valueOf(col);
					// 實際EXCEL 欄位分配
					int i = col + 2;
					switch (i) {

					case 2:
						// B欄 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
									: Integer.valueOf(tLDVo.get(fdnm)), "R");
						} else {
							makeExcel.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// C欄 額度
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: Integer.valueOf(tLDVo.get(fdnm)),
								"C");
						break;
					case 4:
						// D欄 戶名
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// E欄 區域中心名稱
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// F欄 覆審單位
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "(空白)"
								: tLDVo.get(fdnm).equals("") ? "(空白)" : " ", "C");
						break;

					case 7:
						// G欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");

						break;

					case 8:
						// H欄 撥款日期
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0
								: Integer.parseInt(tLDVo.get(fdnm)) - 19110000, "C");

						// 與前一筆是否相同
						if (!date.equals(tLDVo.get(fdnm))) {
							date = tLDVo.get(fdnm);
							if (lday == 0) {
								// 第一次賦值
								lday = Integer.parseInt(tLDVo.get(fdnm));
							} else {
								if (lday > Integer.parseInt(tLDVo.get(fdnm))) {
									lday = Integer.parseInt(tLDVo.get(fdnm));
								}
							}
						}
						break;
					case 9:
						// I欄 放款餘額

						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						// 所有金額總計
						tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						break;
					case 10:
						// J欄 全戶餘額
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO
										: new BigDecimal(tLDVo.get(fdnm)),
								"#,##0", "R");
						break;
					case 11:
						// K欄 展期記號
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 12:
						// L欄 應覆審案件
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 13:
						// M欄 評等
						makeExcel.setValue(row, i,
								tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0")
										? ""
										: tLDVo.get(fdnm),
								"C");
						break;
					default:

						break;
					}
				}
			}

			makeExcel.setValue(row + 1, 2, "總計", "L");
			makeExcel.setValue(row + 1, 8, lday - 19110000, "C");
			makeExcel.setValue(row + 1, 9, tot, "#,##0", "R");

		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

		// 設定高度
		makeExcel.setHeight(3, 30);

		dataList(fnAllList2, itemName);

		makeExcel.close();

	}
	
public void dataList(List<Map<String, String>> list,String sheetName) throws LogicException {
		
		makeExcel.setSheet(sheetName);
		
		int row = 2;
		
		
		for(Map<String, String> r:list) {
			
			int conditionCode = Integer.valueOf(r.get("F0"));
			String branchNo = r.get("F1");
			int custNo = Integer.valueOf(r.get("F2"));
			int facmNo = Integer.valueOf(r.get("F3"));
			int bormNo = Integer.valueOf(r.get("F4"));
			String custName = r.get("F5");
			int drawdownDate = Integer.valueOf(r.get("F6"));
			int loanBal = Integer.valueOf(r.get("F7"));
			int maturityDate = Integer.valueOf(r.get("F8"));
			int clcode1 = Integer.valueOf(r.get("F9"));
			int clcode2 = Integer.valueOf(r.get("F10"));
			int clno = Integer.valueOf(r.get("F11"));
			String cityShort = r.get("F12");
			String areaShort = r.get("F13");
			String part1 = r.get("F14");
			String part2 = r.get("F15");
			String location = r.get("F16");
			String cityName = r.get("F17");
			String unit = r.get("F18");
			String sampleNum = r.get("F19");
			String sampleType = r.get("F20");
			int rechYM = Integer.valueOf(r.get("F21"));
			String usageItem = r.get("F22");
			String remark = r.get("F23");
			
			//條件代碼
			makeExcel.setValue(row,1,conditionCode,"R");
			//營業單位
			makeExcel.setValue(row,2,branchNo,"R");
			//戶號
			makeExcel.setValue(row,3,custNo,"#######","R");
			//額度
			makeExcel.setValue(row,4,facmNo,"R");
			//撥款
			makeExcel.setValue(row,5,bormNo,"R");
			//戶名
			makeExcel.setValue(row,6,custName,"L");

			//撥款日期
			makeExcel.setValue(row,7,drawdownDate,"","R");
			//放款餘額
			makeExcel.setValue(row,8,loanBal,"#,##0","R");
			//到期日
			makeExcel.setValue(row,9,maturityDate,"R");
			//押品1
			makeExcel.setValue(row,10,clcode1,"R");
			//押品2
			makeExcel.setValue(row,11,clcode2,"R");
			//押品號碼
			makeExcel.setValue(row,12,clno,"R");
			//縣市
			makeExcel.setValue(row,13,cityShort,"L");
			//鄉鎮區
			makeExcel.setValue(row,14,areaShort,"L");
			//段
			makeExcel.setValue(row,15,part1,"L");
			//小段
			makeExcel.setValue(row,16,part2,"L");
			//門牌號碼
			makeExcel.setValue(row,17,location,"L");
			//地區別
			makeExcel.setValue(row,18,cityName,"L");
			//區域中心
			makeExcel.setValue(row,19,unit,"L");
			//抽樣總戶數
			makeExcel.setValue(row,20,sampleNum,"R");
			//抽樣別
			makeExcel.setValue(row,21,sampleType,"L");
			//覆審月份
			makeExcel.setValue(row,22,rechYM,"R");
			//增貸案件
			makeExcel.setValue(row,23,usageItem,"L");
			//資料說明(備註)
			makeExcel.setValue(row,24,remark,"L");

			row++;
		}
		
	}

}
