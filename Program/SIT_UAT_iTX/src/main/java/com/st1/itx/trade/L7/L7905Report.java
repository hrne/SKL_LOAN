package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L7905ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7905Report")
@Scope("prototype")

public class L7905Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L7905Report.class);

	@Autowired
	public L7905ServiceImpl L7905ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
		try {
			List<HashMap<String, String>> L7905List = L7905ServiceImpl.findAll();
			if (L7905List.size() > 0) {
				// genFile(titaVo, 0, L7905List);
				genExcel(titaVo, 0, L7905List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7905ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7905 genExcel");
		// 自訂標題 inf
		String inf = "戶號;借款人ID / 統編;額度編號;核准號碼;撥款序號;企業戶/個人戶;戶況;轉催收款日期;原始認列時時信用評等;原始認列時信用評等模型;財務報導日時信用評等;財務報導日時信用評等模型;逾期繳款天數;債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上;個人消費性放款逾期超逾90天(含)以上;債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議);個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議);債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務;內部違約機率降至D評等";
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("-----------------" + L7List);
			// this.info("----------------- L7List.size()=" + L7List.size());

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7905", "IFRS9 資料欄位清單7", "IFRS9_07");

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 輸出內容
			for (HashMap<String, String> tL7Vo : L7List) {
				for (int j = 1; j <= tL7Vo.size(); j++) {
					if (tL7Vo.get(txt1[j - 1]) == null) {
						makeExcel.setValue(i, j, "");
					} else {
						makeExcel.setValue(i, j, tL7Vo.get(txt1[j - 1]));
					}
				}
				i++;
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7905ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7905 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IFRS9 資料欄位清單7", "IFRS9_07.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID / 統編(8~17),額度編號(18~20),核准號碼(21~27),撥款序號(28~30),企業戶/個人戶(31~31),戶況(32~32),轉催收款日期(33~40),原始認列時時信用評等(41~41),原始認列時信用評等模型(42~42),財務報導日時信用評等(43~43),財務報導日時信用評等模型(44~44),逾期繳款天數(45~47),債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上(48~48),個人消費性放款逾期超逾90天(含)以上(49~49),債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議)(50~50),個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議)(51~51),債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務(52~52),內部違約機率降至D評等(53~53)";
			makeFile.put(strContent);

			// 欄位內容
			this.info("-----------------" + L7List);
			for (HashMap<String, String> tL7Vo : L7List) {
				strContent = "";
				// this.info("-----------------" + tL7Vo);
				// this.info("--------tL7Vo.size=" + tL7Vo.size());
				for (int j = 1; j <= tL7Vo.size(); j++) {
					// this.info("--------------j=" + j);
					String strField = "";
					if (tL7Vo.get(txt1[j - 1]) == null) {
						strField = "";
					} else {
						strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
					}

					// 格式處理
					// this.info("--------------strField=" + strField);
					if (j == 1) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 戶號
					if (j == 2) {
						strField = makeFile.fillStringR(strField, 10, ' ');
					} // 借款人ID / 統編
					if (j == 3) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 額度編號
					if (j == 4) {
						strField = makeFile.fillStringL(strField, 7, '0');
					} // 核准號碼
					if (j == 5) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 撥款序號
					if (j == 6) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶 1=企業戶、2=個人戶
						// 自然人採用企金自然人評等模型者，應歸類為企業戶
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 戶況 1=正常 2=催收
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉催收款日期(抓取最近一次的轉催收日期)
					if (j == 9) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 原始認列時時信用評等(資料來源：與eloan系統對接，得以空值提供)
					if (j == 10) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 原始認列時信用評等模型(資料來源：與eloan系統對接)
					if (j == 11) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 財務報導日時信用評等(資料來源：與eloan系統對接，得以空值提供)
					if (j == 12) {
						strField = makeFile.fillStringR(strField, 1, ' ');
					} // 財務報導日時信用評等模型(資料來源：與eloan系統對接)
					if (j == 13) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 逾期繳款天數
					if (j == 14) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 為IAS39減損客觀條件1
						// 債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上 1=是 2=否
					if (j == 15) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 為IAS39減損客觀條件2
						// 個人消費性放款逾期超逾90天(含)以上 1=是 2=否
					if (j == 16) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 為IAS39減損客觀條件3
						// 債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議) 1=是 2=否
					if (j == 17) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 為IAS39減損客觀條件4
						// 個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議) 1=是 2=否
					if (j == 18) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 為IAS39減損客觀條件5
						// 債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務 1=是 2=否
					if (j == 19) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 內部違約機率降至D評等(資料來源：與eloan系統對接)

					strContent = strContent + strField;
					if (j != tL7Vo.size()) {
						strContent = strContent + ",";
					}
				}
				makeFile.put(strContent);
			}

			long sno = makeFile.close();
			makeFile.toFile(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7905ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
