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
import com.st1.itx.db.service.springjpa.cm.L7201ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7201Report")
@Scope("prototype")

public class L7201Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L7201Report.class);

	@Autowired
	public L7201ServiceImpl L7201ServiceImpl;

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
		int fg = 1;
		for (fg = 1; fg <= 2; fg++) {
			try {
//				List<HashMap<String, String>> L7201List = L7201ServiceImpl.findAll(fg);
//				if(L7201List.size() > 0){
//					genExcel(titaVo, fg, L7201List);
//				}
				genExcel(titaVo, fg);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L7201ServiceImpl.findAll (" + fg + ") error = " + errors.toString());
			}
		}
	}

	private void genExcel(TitaVo titaVo, int fg) throws LogicException {
		this.info("=========== L7201 genExcel: " + fg);
		// 自訂標題 inf
		String inf = "";
		String txt = "";

		if (fg == 1) {
			inf = "戶號;額度; ;核准日期;初貸日期;到期日(額度);貸款期間; ; ;動支期限;循環動用期限;核准額度;放款餘額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;會計帳冊;信用風險轉換係數;表外曝險金額;借方：備忘分錄會計科目;貸方：備忘分錄會計科目; ";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21";
		}
		if (fg == 2) {
			inf = "戶號;額度; ;核准日期;初貸日期;到期日(額度);貸款期間; ; ;動支期限;循環動用期限;核准額度;放款餘額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;會計帳冊;信用風險轉換係數;表外曝險金額; ";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		}

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> L7List = L7201ServiceImpl.findAll(fg);
			this.info("-----------------" + L7List);

			if (fg == 1) {
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7201", "表外放款承諾資料產出", "L7201表外明細", "LNWLCTP");
			}
			// 第2個頁籤
			if (fg == 2) {
				makeExcel.newSheet("LNWLCAP已核撥");
			}

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;

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

			// 最後一個頁籤才close EXCEL
			if (fg == 2) {
				long sno = makeExcel.close();
				this.info("L7201Report.genExcel sno = " + sno);
				makeExcel.toExcel(sno);
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7201Report.genExcel error = " + errors.toString());
		}
	}

	// 以下未使用
	private void genExcel(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7201 genExcel: " + fg);
		// 自訂標題 inf
		String inf = "";
		String txt = "";

		if (fg == 1) {
			inf = "戶號;額度; ;核准日期;初貸日期;到期日(額度);貸款期間; ; ;動支期限;循環動用期限;核准額度;放款餘額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;會計帳冊;信用風險轉換係數;表外曝險金額;借方：備忘分錄會計科目;貸方：備忘分錄會計科目; ";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21";
		}
		if (fg == 2) {
			inf = "戶號;額度; ;核准日期;初貸日期;到期日(額度);貸款期間; ; ;動支期限;循環動用期限;核准額度;放款餘額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;會計帳冊;信用風險轉換係數;表外曝險金額; ";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19";
		}

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

			if (fg == 1) {
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L7201", "表外放款承諾資料產出", "表外明細LNWLCTP", "LNWLCTP");
			}
			// if (fg == 2) { makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
			// "L7201", "表外放款承諾資料產出", "表外明細LNWLCAP已核撥", "LNWLCAP已核撥"); }
			if (fg == 2) {
				makeExcel.newSheet("LNWLCAP已核撥");
				this.info("newSheet");
				// makeExcel.setSheet("L7201", "LNWLCAP已核撥");
				this.info("setSheet");
			}

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
			this.info("L7201ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
