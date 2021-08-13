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
import com.st1.itx.db.service.springjpa.cm.LNM002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM002Report")
@Scope("prototype")

public class LNM002Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LNM002Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM002ServiceImpl lNM002ServiceImpl;

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
		// 清單2：台幣放款-計算原始有效利率用
		try {
			genExcel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM002ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo) throws LogicException {
		this.info("=========== LNM002 genExcel: ");
		this.info("LNM002 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單2：台幣放款-計算原始有效利率用
		inf = "戶號;借款人ID/統編;額度編號;撥款序號;貸放利率;利率調整方式;利率欄位生效日";
		txt = "F0;F1;F2;F3;F4;F5;F6";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> LNList = null;
			LNList = lNM002ServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM002", "清單2：台幣放款-計算原始有效利率用", "清單2：台幣放款-計算原始有效利率用");

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
				makeExcel.setWidth(4, 5);
				makeExcel.setWidth(5, 11);
				makeExcel.setWidth(6, 3);
				makeExcel.setWidth(7, 10);
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM002ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
