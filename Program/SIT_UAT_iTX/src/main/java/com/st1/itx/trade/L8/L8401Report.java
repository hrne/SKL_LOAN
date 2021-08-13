package com.st1.itx.trade.L8;

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
import com.st1.itx.db.service.springjpa.cm.L8401ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("L8401Report")
@Scope("prototype")

public class L8401Report extends MakeReport {
	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	private static final Logger logger = LoggerFactory.getLogger(L8401Report.class);

	@Autowired
	public L8401ServiceImpl L8401ServiceImpl;

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
		for (int fg = 1; fg <= 1; fg++) { // 總報表數
			try {
				genExcel(titaVo, fg);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L8401ServiceImpl.findAll (" + fg + ") error = " + errors.toString());
			}
		}
	}

	/**
	 * 製檔<br>
	 * 
	 * @param titaVo titaVo
	 * @param fg     fg=1: B204 聯徵授信餘額日報檔 未列在新系統需求書 fg=2: B071 現金卡資料日報檔 未列在新系統需求書
	 *               fg=3: B211 聯徵每日授信餘額變動資料檔
	 * @throws LogicException LogicException
	 */
	private void genExcel(TitaVo titaVo, int fg) throws LogicException {
		this.info("=========== L8401 genExcel: " + fg);
		this.info("L8401 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		if (fg == 1) {
			inf = "總行代號(1~4);分行代號(5~9);新增核准額度日期／清償日期／額度到期或解約日期(10~17);額度控制編碼／帳號(18~68);授信戶IDN/BAN(69~79);科目別(80~81);科目別註記(82~83);交易別(84~85);訂約金額(86~96);新增核准額度當日動撥／清償金額(97~107);本筆新增核准額度應計入DBR22倍規範之金額(108~118);1~7欄資料值相同之交易序號(119~120);空白(121~141)";
			txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12";
		}
		/*
		 * 未列在新系統需求書 if (fg == 2) { inf = ""; txt = ""; } if (fg == 3) { inf = ""; txt =
		 * "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17"; }
		 */

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> L8List = null;
			if (fg != 2) {
				L8List = L8401ServiceImpl.findAll(titaVo, fg);
			}

			this.info("-----------------" + L8List);

			String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			switch (fg) {
			case 1:
				makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B204", "聯徵授信餘額日報檔", "B204");
				break;
			/*
			 * 未列在新系統需求書 case 2 : makeExcel.open(titaVo, titaVo.getEntDyI(),
			 * titaVo.getKinbr(), "B071", "聯徵現金卡資料日報檔", "B071"); break; case 3 :
			 * makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B211",
			 * "聯徵每日授信餘額變動資料檔", "B211"); break;
			 */
			}

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (L8List != null) {
				this.info("--------L8List.size=" + L8List.size());
				for (HashMap<String, String> tL8Vo : L8List) {
					for (int j = 1; j <= tL8Vo.size(); j++) {
						if (tL8Vo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tL8Vo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			// 設定欄位寬度
			if (L8List != null) {
				switch (fg) {
				case 1:
					makeExcel.setWidth(1, 5);
					makeExcel.setWidth(2, 6);
					makeExcel.setWidth(3, 9);
					makeExcel.setWidth(4, 52);
					makeExcel.setWidth(5, 12);
					makeExcel.setWidth(6, 3);
					makeExcel.setWidth(7, 3);
					makeExcel.setWidth(8, 3);
					makeExcel.setWidth(9, 12);
					makeExcel.setWidth(10, 12);
					makeExcel.setWidth(11, 12);
					makeExcel.setWidth(12, 3);
					makeExcel.setWidth(13, 22);
					break;
				}
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L8401ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
