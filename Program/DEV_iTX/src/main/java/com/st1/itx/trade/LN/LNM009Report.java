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
import com.st1.itx.db.service.springjpa.cm.LNM009ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM009Report")
@Scope("prototype")

public class LNM009Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LNM009Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM009ServiceImpl lNM009ServiceImpl;

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
		// 清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用
		try {
			genExcel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM009ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo) throws LogicException {
		this.info("=========== LNM009 genExcel: ");
		this.info("LNM009 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用
		inf = "戶號;借款人ID/統編;額度編號;核准號碼;核准日期;初貸日期;核准金額(台幣);帳管費(台幣);法拍及火險費用(台幣);核准利率;初貸時約定還本寬限期;契約當時還款方式;契約當時利率調整方式;契約約定當時還本週期;契約約定當時繳息週期;授信行業別;擔保品類別;擔保品地區別;商品利率代碼;企業戶/個人戶;產品別;原始鑑價金額;可動用餘額;該筆額度是否可循環動用;該筆額度是否為不可徹銷;合約期限;備忘分錄會計科目;記帳幣別;會計帳冊;交易幣別;報導日匯率;核准金額(交易幣);帳管費(交易幣);法拍及火險費用(交易幣)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;" + "F20;F21;F22;F23;F24;F25;F26;F27;F28;F29;F30;F31;F32;F33";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			List<HashMap<String, String>> LNList = null;
			LNList = lNM009ServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM009", "清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用", "清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用");

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
				makeExcel.setWidth(5,  10);
				makeExcel.setWidth(6,  10);
				makeExcel.setWidth(7,  15);
				makeExcel.setWidth(8,  15);
				makeExcel.setWidth(9,  15);
				makeExcel.setWidth(10, 11);
				makeExcel.setWidth(11, 5 );
				makeExcel.setWidth(12, 3 );
				makeExcel.setWidth(13, 3 );
				makeExcel.setWidth(14, 4 );
				makeExcel.setWidth(15, 4 );
				makeExcel.setWidth(16, 8 );
				makeExcel.setWidth(17, 4 );
				makeExcel.setWidth(18, 3 );
				makeExcel.setWidth(19, 4 );
				makeExcel.setWidth(20, 3 );
				makeExcel.setWidth(21, 3 );
				makeExcel.setWidth(22, 15);
				makeExcel.setWidth(23, 15);
				makeExcel.setWidth(24, 3 );
				makeExcel.setWidth(25, 3 );
				makeExcel.setWidth(26, 10);
				makeExcel.setWidth(27, 10);
				makeExcel.setWidth(28, 3 );
				makeExcel.setWidth(29, 3 );
				makeExcel.setWidth(30, 6 );
				makeExcel.setWidth(31, 15);
				makeExcel.setWidth(32, 15);
				makeExcel.setWidth(33, 15);
				makeExcel.setWidth(34, 15);
			}

			long sno = makeExcel.close();
			makeExcel.toExcel(sno);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM009ServiceImpl.genExcel error = " + errors.toString());
		}
	}
}
