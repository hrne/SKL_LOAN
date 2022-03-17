package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM34EPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lNM34EPReport")
@Scope("prototype")

public class LNM34EPReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34EPServiceImpl lNM34EPServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		// LNM34EP 資料欄位清單E
		this.info("---------- LNM34EPReport exec titaVo: " + titaVo);

		// 系統營業日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底營業日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月月底日
		int lmndyf = this.txBuffer.getTxCom().getLmndyf();

		int dataMonth = 0;

		if (tbsdyf == mfbsdyf) {
			// 今日為月底營業日:產本月報表
			dataMonth = tbsdyf / 100;
		} else {
			// 今日非月底營業日:產上月報表
			dataMonth = lmndyf / 100;
		}

		this.info("dataMonth= " + dataMonth);

		List<Map<String, String>> LNM34EPList = null;
		try {
			LNM34EPList = lNM34EPServiceImpl.findAll(dataMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34EPReport LNM34EPServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		try {
			// excel-CSV
			genFile(titaVo, LNM34EPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LNM34EPReport.genFile error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34EP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34EP", "IAS39 資料欄位清單E", "LNM34EP.csv", 2);

			// 標題列
			// strContent =
			// "資料時點(1~6),戶號(7~13),統編(14~23),額度編號(核准號碼)(24~26),撥款序號(27~29),會計科目(30~37),狀態(38~38),授信行業別(39~44),擔保品類別(45~46),擔保品地區別(47~49),商品利率代碼(50~51),企業戶/個人戶(52~52),資料時點是否符合減損客觀證據(53~53),產品別(54~55)";
			// makeFile.put(strContent);

			// 欄位內容
			// this.info("-----------------" + L7List);
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 14; j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，"); // csv檔: 逗號轉全形
						}

						// 格式處理(直接搬值，不刪除前後空白者)
						if (j == 6) { // 會計科目(舊:8碼/新:11碼)
						} else {
							strField = tL7Vo.get(txt1[j - 1]).trim();
						}

						// 格式處理
						if (j == 1) {
							strField = makeFile.fillStringL(strField, 6, '0');
						} // 資料時點
						if (j == 2) {
							strField = makeFile.fillStringL(strField, 7, '0');
						} // 戶號
						if (j == 3) {
							strField = makeFile.fillStringR(strField, 10, ' ');
						} // 統編
						if (j == 4) {
							strField = makeFile.fillStringL(strField, 3, '0');
						} // 額度編號(核准號碼)
						if (j == 5) {
							strField = makeFile.fillStringL(strField, 3, '0');
						} // 撥款序號
						if (j == 6) {
							// 直接搬值
							// strField = makeFile.fillStringR(strField, 8, ' ');
						} // 會計科目(舊:8碼/新:11碼) (直接搬值)
						if (j == 7) {
							strField = makeFile.fillStringL(strField, 1, '0');
						} // 狀態
						if (j == 8) {
							strField = makeFile.fillStringR(strField, 6, ' ');
						} // 授信行業別
						if (j == 9) {
							strField = makeFile.fillStringR(strField, 2, ' ');
						} // 擔保品類別
						if (j == 10) {
							strField = makeFile.fillStringR(strField, 3, ' ');
						} // 擔保品地區別
						if (j == 11) {
							strField = makeFile.fillStringR(strField, 5, ' ');
						} // 商品利率代碼
						if (j == 12) {
							strField = makeFile.fillStringL(strField, 1, '0');
						} // 企業戶/個人戶
						if (j == 13) {
							strField = makeFile.fillStringR(strField, 1, ' ');
						} // 資料時點是否符合減損客觀證據
						if (j == 14) {
							strField = makeFile.fillStringR(strField, 2, ' ');
						} // 產品別

						strContent = strContent + strField;
						if (j != 14) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM34EPServiceImpl.genFile error = " + errors.toString());
		}
	}
}
