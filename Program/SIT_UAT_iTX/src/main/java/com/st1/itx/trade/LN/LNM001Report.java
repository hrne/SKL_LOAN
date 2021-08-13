package com.st1.itx.trade.LN;

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
import com.st1.itx.db.service.springjpa.cm.LNM001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM001Report")
@Scope("prototype")

public class LNM001Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LNM001Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM001ServiceImpl lNM001ServiceImpl;

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
		// 清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用
		try {
			genExcel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM001ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo) throws LogicException {
		this.info("=========== LNM001 genExcel: ");
		this.info("LNM001 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用
		inf = "戶號;借款人ID/統編;額度編號;核准號碼;撥款序號;會計科目(8碼);戶況;初貸日期;撥款日期;到期日(額度);到期日(撥款);核准金額;撥款金額;帳管費;本金餘額(撥款);應收利息;法拍及火險費用;利率(撥款);逾期繳款天數;轉催收款日期;轉銷呆帳日期;轉銷呆帳金額;初貸時約定還本寬限期;核准利率;契約當時還款方式;契約當時利率調整方式;契約約定當時還本週期;契約約定當時繳息週期;授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;五類資產分類;產品別;原始鑑價金額;首次應繳日;總期數;可動用餘額(台幣);該筆額度是否可循環動用;該筆額度是否為不可徹銷;暫收款金額(台幣);記帳幣別;會計帳冊;交易幣別;報導日匯率;核准金額(交易幣);撥款金額(交易幣);帳管費(交易幣);本金餘額(撥款_交易幣);應收利息(交易幣);法拍及火險費用(交易幣);可動用餘額(交易幣);暫收款金額(交易幣)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;"
				+ "F40;F41;F42;F43;F44;F45;F46;F47;F48;F49;F50;F51;F52;F53";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> LNList = null;
			LNList = lNM001ServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM001", "清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用", "清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用");

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
				makeExcel.setWidth(1,  9 );
				makeExcel.setWidth(2,  12);
				makeExcel.setWidth(3,  5 );
				makeExcel.setWidth(4,  9 );
				makeExcel.setWidth(5,  5 );
				makeExcel.setWidth(6,  10);
				makeExcel.setWidth(7,  3 );
				makeExcel.setWidth(8,  10);
				makeExcel.setWidth(9,  10);
				makeExcel.setWidth(10, 10);
				makeExcel.setWidth(11, 10);
				makeExcel.setWidth(12, 15);
				makeExcel.setWidth(13, 15);
				makeExcel.setWidth(14, 15);
				makeExcel.setWidth(15, 15);
				makeExcel.setWidth(16, 15);
				makeExcel.setWidth(17, 15);
				makeExcel.setWidth(18, 11);
				makeExcel.setWidth(19, 6 );
				makeExcel.setWidth(20, 10);
				makeExcel.setWidth(21, 10);
				makeExcel.setWidth(22, 15);
				makeExcel.setWidth(23, 5 );
				makeExcel.setWidth(24, 10);
				makeExcel.setWidth(25, 3 );
				makeExcel.setWidth(26, 3 );
				makeExcel.setWidth(27, 4 );
				makeExcel.setWidth(28, 4 );
				makeExcel.setWidth(29, 8 );
				makeExcel.setWidth(30, 4 );
				makeExcel.setWidth(31, 3 );
				makeExcel.setWidth(32, 4 );
				makeExcel.setWidth(33, 3 );
				makeExcel.setWidth(34, 3 );
				makeExcel.setWidth(35, 3 );
				makeExcel.setWidth(36, 15);
				makeExcel.setWidth(37, 10);
				makeExcel.setWidth(38, 5 );
				makeExcel.setWidth(39, 15);
				makeExcel.setWidth(40, 3 );
				makeExcel.setWidth(41, 3 );
				makeExcel.setWidth(42, 15);
				makeExcel.setWidth(43, 3 );
				makeExcel.setWidth(44, 3 );
				makeExcel.setWidth(45, 6 );
				makeExcel.setWidth(46, 11);
				makeExcel.setWidth(47, 15);
				makeExcel.setWidth(48, 15);
				makeExcel.setWidth(49, 15);
				makeExcel.setWidth(50, 15);
				makeExcel.setWidth(51, 15);
				makeExcel.setWidth(52, 15);
				makeExcel.setWidth(53, 15);
				makeExcel.setWidth(54, 15);
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM001ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
