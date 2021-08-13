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
import com.st1.itx.db.service.springjpa.cm.L7005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

@Component("L7005Report")
@Scope("prototype")

public class L7005Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L7005Report.class);

	@Autowired
	public L7005ServiceImpl L7005ServiceImpl;

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
			List<HashMap<String, String>> L7005List = L7005ServiceImpl.findAll();
			if (L7005List.size() > 0) {
				genFile(titaVo, 0, L7005List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7005ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7005 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單E", "LNM34EP.csv", 2);

			// 標題列
			strContent = "資料時點(1~6),戶號(7~13),統編(14~23),額度編號(核准號碼)(24~26),撥款序號(27~29),會計科目(30~37),狀態(38~38),授信行業別(39~44),擔保品類別(45~46),擔保品地區別(47~49),商品利率代碼(50~51),企業戶/個人戶(52~52),資料時點是否符合減損客觀證據(53~53),產品別(54~55)";
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
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 會計科目
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
						strField = makeFile.fillStringR(strField, 2, ' ');
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
			this.info("L7005ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
