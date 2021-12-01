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
import com.st1.itx.db.service.springjpa.cm.LNM34APServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component("lNM34APReport")
@Scope("prototype")

public class LNM34APReport extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	@Autowired
	public LNM34APServiceImpl lNM34APServiceImpl;

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
		// LNM34AP 資料欄位清單A
		try {
			this.info("---------- LNM34APReport exec titaVo: " + titaVo);
			List<Map<String, String>> LNM34APList = lNM34APServiceImpl.findAll(titaVo);
			genFile(titaVo, LNM34APList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM34APServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> L7List) throws LogicException {
		this.info("=========== LNM34AP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;" + "F25;F26;F27;F28;F29;F30;F31;F32;F33;F34;F35;F36;F37;F38;F39;F40";
		String txt1[] = txt.split(";");

		try {
			DecimalFormat formatter = new DecimalFormat("0");
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNM34AP", "IAS39 資料欄位清單A", "LNM34AP.csv", 2);

			// 標題列
			// strContent =
			// "戶號(1~7),借款人ID/統編(8~17),額度編號(18~20),核准號碼(21~27),撥款序號(28~30),會計科目(8碼)(31~38),戶況(39~39),初貸日期(40~47),撥款日期(48~55),到期日(額度)(56~63),到期日(撥款)(64~71),核准金額(72~82),撥款金額(83~93),帳管費(94~98),本金餘額(撥款)(99~109),應收利息(110~120),法拍及火險費用(121~128),利率(撥款)(129~136),逾期繳款天數(137~140),轉催收款日期(141~148),轉銷呆帳日期(149~156),轉銷呆帳金額(157~167),符合減損客觀證據之條件(168~168),初貸時約定還本寬限期(169~171),核准利率(172~179),契約當時還款方式(180~180),契約當時利率調整方式(181~181),契約約定當時還本週期(182~183),契約約定當時繳息週期(184~185),授信行業別(186~191),擔保品類別(192~193),擔保品地區別(194~196),商品利率代碼(197~198),企業戶/個人戶(199~199),五類資產分類(200~200),產品別(201~202),原始鑑價金額(203~213),首次應繳日(214~221),總期數(222~224),協議前之額度編號(225~227),協議前之撥款序號(228~230)";
			// makeFile.put(strContent);

			// 欄位內容
			if (L7List.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= 41; j++) {
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
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 戶號
						case 2:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break; // 借款人ID / 統編
						case 3:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 額度編號
						case 4:
							strField = makeFile.fillStringL(strField, 7, '0');
							break; // 核准號碼
						case 5:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 撥款序號
						case 6:
							break; // 會計科目(舊:8碼/新:11碼) (直接搬值)
						case 7:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 戶況
						case 8:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 初貸日期
						case 9:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 撥款日期
						case 10:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 到期日(額度)
						case 11:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 到期日(撥款)
						case 12:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 核准金額
						case 13:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 撥款金額
						case 14:
							strField = makeFile.fillStringL(strField, 5, '0');
							break; // 帳管費
						case 15:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 本金餘額(撥款)
						case 16:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 應收利息
						case 17:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 法拍及火險費用
						case 18:
							formatter.applyPattern("0.000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 利率(撥款)
						case 19:
							strField = makeFile.fillStringL(strField, 4, '0');
							break; // 逾期繳款天數
						case 20:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉催收款日期
						case 21:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 轉銷呆帳日期
						case 22:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 轉銷呆帳金額
						case 23:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 符合減損客觀證據之條件
						case 24:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 初貸時約定還本寬限期
						case 25:
							formatter.applyPattern("0.000000");
							strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 核准利率
						case 26:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 契約當時還款方式
						case 27:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 契約當時利率調整方式
						case 28:
							strField = makeFile.fillStringL(strField, 2, '0');
							break; // 契約約定當時還本週期
						case 29:
							strField = makeFile.fillStringL(strField, 2, '0');
							break; // 契約約定當時繳息週期
						case 30:
							strField = makeFile.fillStringR(strField, 6, ' ');
							break; // 授信行業別
						case 31:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 擔保品類別
						case 32:
							strField = makeFile.fillStringR(strField, 3, ' ');
							break; // 擔保品地區別(擔保品郵遞區號)
						case 33:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 商品利率代碼
						case 34:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 企業戶/個人戶
						case 35:
							strField = makeFile.fillStringL(strField, 1, '0');
							break; // 五類資產分類
						case 36:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break; // 產品別
						case 37:
							strField = makeFile.fillStringL(strField, 11, '0');
							break; // 原始鑑價金額
						case 38:
							strField = makeFile.fillStringL(strField, 8, '0');
							break; // 首次應繳日
						case 39:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 總期數
						case 40:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 協議前之額度編號
						case 41:
							strField = makeFile.fillStringL(strField, 3, '0');
							break; // 協議前之撥款序號
						}
						strContent = strContent + strField;
						if (j != tL7Vo.size()) {
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
			this.info("LNM34APServiceImpl.genFile error = " + errors.toString());
		}
	}
}
