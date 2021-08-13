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
import com.st1.itx.db.service.springjpa.cm.LNM004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM004Report")
@Scope("prototype")

public class LNM004Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LNM004Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM004ServiceImpl lNM004ServiceImpl;

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
		// 清單4：放款與AR-估計回收率用
		try {
			genExcel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM004ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo) throws LogicException {
		this.info("=========== LNM004 genExcel: ");
		this.info("LNM004 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單4：放款與AR-估計回收率用
		inf = "戶號;借款人ID/統編;額度編號;撥款序號;會計科目;案件狀態;初貸日期;貸放日期;到期日;核准金額(台幣);撥款金額(台幣);本金餘額(撥款)(台幣);應收利息(台幣);法拍及火險費用(台幣);逾期繳款天數;轉催收款日期;轉銷呆帳日期;轉銷呆帳金額;stage3發生日期;上述發生日期前之最近一次利率;上述發生日期時之本金餘額(台幣);上述發生日期時之應收利息(台幣);上述發生日期時之法拍及火險費用(台幣);stage3發生後第一年本金回收金額(台幣);stage3發生後第二年本金回收金額(台幣);stage3發生後第三年本金回收金額(台幣);stage3發生後第四年本金回收金額(台幣);stage3發生後第五年本金回收金額(台幣);stage3發生後第一年應收利息回收金額(台幣);stage3發生後第二年應收利息回收金額(台幣);stage3發生後第三年應收利息回收金額(台幣);stage3發生後第四年應收利息回收金額(台幣);stage3發生後第五年應收利息回收金額(台幣);stage3發生後第一年法拍及火險費用回收金額(台幣);stage3發生後第二年法拍及火險費用回收金額(台幣);stage3發生後第三年法拍及火險費用回收金額(台幣);stage3發生後第四年法拍及火險費用回收金額(台幣);stage3發生後第五年法拍及火險費用回收金額(台幣);授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;產品別";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;"
				+ "F40;F41;F42;F43";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> LNList = null;
			LNList = lNM004ServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM004", "清單4：放款與AR-估計回收率用", "清單4：放款與AR-估計回收率用");

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
				makeExcel.setWidth(4,  5 );
				makeExcel.setWidth(5,  10);
				makeExcel.setWidth(6,  3 );
				makeExcel.setWidth(7,  10);
				makeExcel.setWidth(8,  10);
				makeExcel.setWidth(9,  10);
				makeExcel.setWidth(10, 15);
				makeExcel.setWidth(11, 15);
				makeExcel.setWidth(12, 15);
				makeExcel.setWidth(13, 15);
				makeExcel.setWidth(14, 15);
				makeExcel.setWidth(15, 6 );
				makeExcel.setWidth(16, 10);
				makeExcel.setWidth(17, 10);
				makeExcel.setWidth(18, 15);
				makeExcel.setWidth(19, 10);
				makeExcel.setWidth(20, 11);
				makeExcel.setWidth(21, 15);
				makeExcel.setWidth(22, 15);
				makeExcel.setWidth(23, 15);
				makeExcel.setWidth(24, 15);
				makeExcel.setWidth(25, 15);
				makeExcel.setWidth(26, 15);
				makeExcel.setWidth(27, 15);
				makeExcel.setWidth(28, 15);
				makeExcel.setWidth(29, 15);
				makeExcel.setWidth(30, 15);
				makeExcel.setWidth(31, 15);
				makeExcel.setWidth(32, 15);
				makeExcel.setWidth(33, 15);
				makeExcel.setWidth(34, 15);
				makeExcel.setWidth(35, 15);
				makeExcel.setWidth(36, 15);
				makeExcel.setWidth(37, 15);
				makeExcel.setWidth(38, 15);
				makeExcel.setWidth(39, 8 );
				makeExcel.setWidth(40, 4 );
				makeExcel.setWidth(41, 3 );
				makeExcel.setWidth(42, 4 );
				makeExcel.setWidth(43, 3 );
				makeExcel.setWidth(44, 3 );
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM004ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
