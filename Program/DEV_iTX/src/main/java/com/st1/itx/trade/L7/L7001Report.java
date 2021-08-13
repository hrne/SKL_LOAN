package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L7001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;

@Component("L7001Report")
@Scope("prototype")

public class L7001Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L7001Report.class);

	@Autowired
	public L7001ServiceImpl L7001ServiceImpl;

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
			List<HashMap<String, String>> L7001List = L7001ServiceImpl.findAll();
			if (L7001List.size() > 0) {
				genFile(titaVo, 0, L7001List);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L7001ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, int fg, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== L7001 genFile");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "IAS39 資料欄位清單A", "LNM34AP.csv", 2);

			// 標題列
			strContent = "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),核准號碼(21~27),撥款序號(28~30),會計科目(8碼)(31~38),戶況(39~39),初貸日期(40~47),撥款日期(48~55),到期日(額度)(56~63),到期日(撥款)(64~71),核准金額(72~82),撥款金額(83~93),帳管費(94~98),本金餘額(撥款)(99~109),應收利息(110~120),法拍及火險費用(121~128),利率(撥款)(129~136),逾期繳款天數(137~140),轉催收款日期(141~148),轉銷呆帳日期(149~156),轉銷呆帳金額(157~167),符合減損客觀證據之條件(168~168),初貸時約定還本寬限期(169~171),核准利率(172~179),契約當時還款方式(180~180),契約當時利率調整方式(181~181),契約約定當時還本週期(182~183),契約約定當時繳息週期(184~185),授信行業別(186~191),擔保品類別(192~193),擔保品地區別(194~196),商品利率代碼(197~198),企業戶/個人戶(199~199),五類資產分類(200~200),產品別(201~202),原始鑑價金額(203~213),首次應繳日(214~221),總期數(222~224),協議前之額度編號(225~227),協議前之撥款序號(228~230)";
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
						strField = makeFile.fillStringR(strField, 8, ' ');
					} // 會計科目(8碼)
					if (j == 7) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 戶況
					if (j == 8) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 初貸日期
					if (j == 9) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 撥款日期
					if (j == 10) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 到期日(額度)
					if (j == 11) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 到期日(撥款)
					if (j == 12) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 核准金額
					if (j == 13) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 撥款金額
					if (j == 14) {
						strField = makeFile.fillStringL(strField, 5, '0');
					} // 帳管費
					if (j == 15) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 本金餘額(撥款)
					if (j == 16) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 應收利息
					if (j == 17) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 法拍及火險費用
					if (j == 18) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 利率(撥款)
					if (j == 19) {
						strField = makeFile.fillStringL(strField, 4, '0');
					} // 逾期繳款天數
					if (j == 20) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉催收款日期
					if (j == 21) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 轉銷呆帳日期
					if (j == 22) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 轉銷呆帳金額
					if (j == 23) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 符合減損客觀證據之條件
					if (j == 24) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 初貸時約定還本寬限期
					if (j == 25) {
						DecimalFormat formatter = new DecimalFormat("0.000000");
						strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)) / 100);
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 核准利率
					if (j == 26) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時還款方式
					if (j == 27) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 契約當時利率調整方式
					if (j == 28) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時還本週期
					if (j == 29) {
						strField = makeFile.fillStringL(strField, 2, '0');
					} // 契約約定當時繳息週期
					if (j == 30) {
						strField = makeFile.fillStringR(strField, 6, ' ');
					} // 授信行業別
					if (j == 31) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 擔保品類別
					if (j == 32) {
						strField = makeFile.fillStringR(strField, 3, ' ');
					} // 擔保品地區別
					if (j == 33) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 商品利率代碼
					if (j == 34) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 企業戶/個人戶
					if (j == 35) {
						strField = makeFile.fillStringL(strField, 1, '0');
					} // 五類資產分類
					if (j == 36) {
						strField = makeFile.fillStringR(strField, 2, ' ');
					} // 產品別
					if (j == 37) {
						strField = makeFile.fillStringL(strField, 11, '0');
					} // 原始鑑價金額
					if (j == 38) {
						strField = makeFile.fillStringL(strField, 8, '0');
					} // 首次應繳日
					if (j == 39) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 總期數
					if (j == 40) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 協議前之額度編號
					if (j == 41) {
						strField = makeFile.fillStringL(strField, 3, '0');
					} // 協議前之撥款序號

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
			this.info("L7001ServiceImpl.genFile error = " + errors.toString());
		}
	}
}
