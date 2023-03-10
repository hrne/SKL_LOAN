package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.LD006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LD006Report extends MakeReport {

	/* DB服務注入 */
	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	LD006ServiceImpl lD006ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lD006List = null;

		try {
			lD006List = lD006ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error("lD006ServiceImpl.findAll error = " + e.getMessage());
			e.printStackTrace();
		}

		exportExcel(titaVo, lD006List);

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lD006List) throws LogicException {
		this.info("exportExcel ... ");
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LD006";
		String fileItem = "三階放款明細統計";
		String fileName = "LD006三階放款明細統計";
		String defaultExcel = "LD006三階放款明細統計.xls";
		String defaultSheet = "三階放款明細統計";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD006", "三階放款明細統計", "LD006三階放款明細統計",
//				"LD006三階放款明細統計.xls", "三階放款明細統計");

		// 有標題列，從第二列開始塞值
		int row = 2;
		BigDecimal total = BigDecimal.ZERO;
		if (lD006List != null && !lD006List.isEmpty()) {
			for (Map<String, String> tLDVo : lD006List) {

				// query欄位數有變時, 這裡也須修改
				for (int i = 0; i < 24; i++) {

					// 查詢結果第一個欄位為F0
					String tmpValue = tLDVo.get("F" + i);

					// 寫入Excel時，A欄為1
					int col = i + 1;

					switch (i) {
					case 2: // 戶名
						makeExcel.setValue(row, col, StringCut.stringMask(tmpValue));
						break;
					case 10: // K欄:撥款金額
						total = total.add(getBigDecimal(tmpValue));
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue,
								"#,##0");
						break;
					case 11: // L, M, N欄: 部室/區部/單位代號
					case 12:
					case 13:
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue,
								"L");
						break;
					case 17: // 員工代號
						makeExcel.setValue(row, col, tmpValue);
						break;
					case 20: // U欄:區經理 by eric 2022.5.20
                   // 條件 : 介紹人本身為區經理,否則依序找上層主管直到找到區經理
                   // 區經理定義 : CdEmp(員工資料檔)的AgLevel(業務人員職等)第一碼為'H'為區經理
                   // AgLevel0  介紹人職等      
                   // AgLevel1  介紹人的第一層主管職等  AgentCode(業務員代號)=DirectorId(上層主管)
                   // AgLevel2  介紹人的第二層主管職等  AgentCode(業務員代號)=DirectorId(上層主管)
                   // AgLevel3  介紹人的第三層主管職等  AgentCode(業務員代號)=DirectorId(上層主管)


						String manager = "";
						if (!tLDVo.get("AgLevel0").isEmpty() && "H".equals(tLDVo.get("AgLevel0").substring(0, 1))) {
							manager = tLDVo.get("ItName");
						} else if (!tLDVo.get("AgLevel1").isEmpty()
								&& "H".equals(tLDVo.get("AgLevel1").substring(0, 1))) {
							manager = tLDVo.get("ManagerName1");
						} else if (!tLDVo.get("AgLevel2").isEmpty()
								&& "H".equals(tLDVo.get("AgLevel2").substring(0, 1))) {
							manager = tLDVo.get("ManagerName2");
						} else if (!tLDVo.get("AgLevel3").isEmpty()
								&& "H".equals(tLDVo.get("AgLevel3").substring(0, 1))) {
							manager = tLDVo.get("ManagerName3");
						}
						makeExcel.setValue(row, col, manager);
						break;
					case 21: // V欄:換算業績
					case 22: // W欄:業務報酬
					case 23: // X欄:業績金額
						makeExcel.setValue(row, col, getBigDecimal(tmpValue), "#,##0");
						break;
					default:
						makeExcel.setValue(row, col, parse.isNumeric(tmpValue) ? getBigDecimal(tmpValue) : tmpValue);
						break;
					}
				} // for

				row++;
			} // for
			makeExcel.setFormula(row, 11, total, "SUBTOTAL(9,K2:K" + (row - 1) + ")", "#,##0");
		} else {
			this.info("LD006Report exportExcel ... 本日無資料");
			makeExcel.setValue(row, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
