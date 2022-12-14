package com.st1.itx.trade.L4;

import java.util.Map;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4454R3ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L4454Report3")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4454Report3 extends MakeReport {

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public L4454R3ServiceImpl l4454R3ServiceImpl;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("一年內新貸件扣款失敗表 Excel Start...");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4454";
		String fileItem = "一年內新貸件扣款失敗表";
		String fileName = "一年內新貸件扣款失敗表";
		String defaultExcel = "一年內新貸件扣款失敗表-底稿.xlsx";
		String defaultSheet = "LAW7U1Pqp";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4454", "一年內新貸件扣款失敗表", "一年內新貸件扣款失敗表", "一年內新貸件扣款失敗表-底稿.xlsx", "LAW7U1Pqp");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			fnAllList = l4454R3ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l4454R3ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";
//			從第幾列開始(表頭位置)
			int row = 1;

			for (Map<String, String> tLDVo : fnAllList) {
				this.info("tLDVo-------->" + tLDVo.toString());
				row++;

//				i = 欄數(Columns)
				for (int i = 0; i < tLDVo.size(); i++) {

//					預設每個欄位名稱為F1~Fn
					fdnm = "F" + String.valueOf(i);

//					設定每欄之Format
					switch (i) {
//					0	1	2	3	4	5	6	7	8		9       10		
//					 帳號	戶號	額度  撥款  戶名	金額	電話	戶名	已繳月份	撥款日	專員
					case 5:
						// 金額
//						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "#,##0");
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "R");
						break;
					case 8:
					case 9:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					default:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					}
				}
			}
		} else {
			makeExcel.setValue(2, 1, "查無資料");
		}

		makeExcel.close();
//		makeExcel.toExcel(sno);
	}
}