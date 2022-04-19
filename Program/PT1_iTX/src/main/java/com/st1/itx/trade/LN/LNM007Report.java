package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM007Report")
@Scope("prototype")

public class LNM007Report extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(LNM007Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM007ServiceImpl lNM007ServiceImpl;

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
		// 清單7：放款與應收帳款-stage轉換用
		try {
			genExcel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM007ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo) throws LogicException {
		this.info("=========== LNM007 genExcel: ");
		this.info("LNM007 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單7：放款與應收帳款-stage轉換用
		inf = "戶號;借款人ID / 統編;額度編號;核准號碼;撥款序號;企業戶/個人戶;戶況;轉催收款日期;原始認列時時信用評等;原始認列時信用評等模型;財務報導日時信用評等;財務報導日時信用評等模型;逾期繳款天數;債務人屬企業戶，且其歸戶下任一債務逾期90天(含)以上;個人消費性放款逾期超逾90天(含)以上;債務人屬企業戶，且其歸戶任一債務已經本行轉催收款、或符合列報逾期放款條件、或本行對該債務讓步(如協議);個人消費性放款已經本行轉催收、或符合列報逾期放款條件、或本行對該債務讓步(如協議);債務人申請重組、破產或其他等程序，而進行該等程序可能使債務人免除或延遲償還債務;內部違約機率降至D評等";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> LNList = null;
			LNList = lNM007ServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM007", "清單7：放款與應收帳款-stage轉換用", "清單7：放款與應收帳款-stage轉換用");

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (LNList != null) {
				this.info("--------LNList.size=" + LNList.size());
				for (HashMap<String, String> tLBVo : LNList) {
					for (int j = 1; j <= tLBVo.size(); j++) {
						if (tLBVo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tLBVo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			// 設定欄位寬度
			if (LNList != null) {
				makeExcel.setWidth(1, 9);
				makeExcel.setWidth(2, 12);
				makeExcel.setWidth(3, 5);
				makeExcel.setWidth(4, 9);
				makeExcel.setWidth(5, 5);
				makeExcel.setWidth(6, 3);
				makeExcel.setWidth(7, 3);
				makeExcel.setWidth(8, 10);
				makeExcel.setWidth(9, 3);
				makeExcel.setWidth(10, 3);
				makeExcel.setWidth(11, 3);
				makeExcel.setWidth(12, 3);
				makeExcel.setWidth(13, 5);
				makeExcel.setWidth(14, 3);
				makeExcel.setWidth(15, 3);
				makeExcel.setWidth(16, 3);
				makeExcel.setWidth(17, 3);
				makeExcel.setWidth(18, 3);
				makeExcel.setWidth(19, 3);
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM007ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
