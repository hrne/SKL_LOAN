package com.st1.itx.trade.LD;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD004ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.ConvertUpMoney;
import com.st1.itx.util.format.StringCut;

@Component
@Scope("prototype")

public class LD004Report extends MakeReport {

	@Autowired
	LD004ServiceImpl lD004ServiceImpl;

	// 自訂表頭
	@Override
	public void printHeader() {

		// 明細起始列(自訂亦必須)
		this.setBeginRow(1);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(25);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {
		// 讀取VAR參數
		this.setFontSize(16);
		this.setCharSpaces(0);
		List<Map<String, String>> listLD004 = null;
		try {
			listLD004 = lD004ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lD004ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		makeReport(titaVo, listLD004);

		return true;
	}

	public void makeReport(TitaVo titaVo, List<Map<String, String>> listLD004) throws LogicException {

		String formName = "1".equals(titaVo.getParam("inputOption")) ? "還本收據" : "繳息收據";

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode("LD004").setRptItem("企金戶還本收據及繳息收據(" + formName + ")").setSecurity("").setRptSize("A4")
				.setPageOrientation("L").build();

		this.open(titaVo, reportVo);

		if (listLD004 != null && !listLD004.isEmpty()) {

			Boolean firstPage = true;
			for (Map<String, String> rowLD004 : listLD004) {

				if (firstPage) {
					newPage();
				} else {
					firstPage = false;
				}

				/*
				 * F0 = 傳票號碼 F1 = 登錄交易序號 F2 = 科目代號 F3 = 業務科目名稱 F4 = 彙總別 F5 = 入帳日期 F6 = 戶號 F7 =
				 * 額度編號 F8 = 撥款序號 F9 = 戶名/公司名稱 F10 = 記帳金額 F11 = 計息起日 F12 = 計息迄日
				 */
				String txtNo = rowLD004.get("F2").substring(0, 1);

				this.info("EntryDate ... = " + rowLD004.get("F5"));
				this.info("IntStartDate ... = " + rowLD004.get("F11"));
				this.info("IntEndDate ... = " + rowLD004.get("F12"));

				this.print(1, 33, "新光人壽保險股份有限公司");
				this.print(1, 2, "傳票號碼　.....　");
				this.print(0, 19, rowLD004.get("F0"), "L");

				if (titaVo.getParam("inputOption").equals("1")) {
					this.print(0, 41, "還本收據");
				} else {
					this.print(0, 41, "繳息收據");
				}

				this.print(1, 2, "交易序號　.....　");
				this.print(0, 26, padStart(rowLD004.get("F1").toString(), 7, "0"), "R");

				this.print(0, 56, "科目：　");
				this.print(0, 64, rowLD004.get("F2"));
				this.print(1, 40, showDate(rowLD004.get("F14").toString(), 1));

				// 輸出用的業務科目中文名稱要做 wrap
				// 此外, 如果前面沒有相關關鍵字, 加上標註

				// 這個加標註的部分是此支程式前一位負責人就有寫的
				// 看樣張不太確定
				// 如果未來有需求, 再修改這部分
				// xiangwei 20211027

				String acChineseName = rowLD004.get("F3");
				String prefix = "1".equals(txtNo) ? "擔保放款－" : "利息收入－";

				if (!acChineseName.contains(prefix)) {
					acChineseName = prefix + acChineseName;
				}

				// wrap
				// 九個中文字寬

				ArrayList<String> acChineseNameWrap = longStringWrap(acChineseName, 18);

				if (acChineseNameWrap.size() >= 1) {
					this.print(0, 64, acChineseNameWrap.get(0));
				}

				this.print(1, 3, "現金　　電匯　　票據　　調整");

				if (acChineseNameWrap.size() >= 2) {
					this.print(0, 64, acChineseNameWrap.get(1));
				}

				this.print(1, 1, "┌──────────┬──────────────────┬─────────┐");
				this.print(1, 1, "│　　　　子目　　　　│　　摘（　　　　　　入帳）要　　　　│　　　 金額 　　　│");
				this.print(0, 39, showDate(rowLD004.get("F5").toString(), 1), "C");
				this.print(1, 1, "├──────────┼──────────────────┼─────────┤");
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (!rowLD004.get("F16").equals("11") && !titaVo.getParam("inputOption").equals("1")) // 20210916 從現有樣張上推測
																									// 還本收據不出此字樣
				{
					this.print(0, 55, "非支票");
				}
				this.print(1, 1, "│　　　　　　　　　　│　戶號：　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(0, 13, rowLD004.get("F15"), "C");
				this.print(0, 34,
						padStart(rowLD004.get("F6").toString(), 7, "0") + "-"
								+ padStart(rowLD004.get("F7").toString(), 3, "0") + "-"
								+ padStart(rowLD004.get("F8").toString(), 3, "0"));

				this.print(0, 63, "$");

				BigDecimal f10 = new BigDecimal(rowLD004.get("F10").toString());
				DecimalFormat df1 = new DecimalFormat("#,##0");
				this.print(0, 80, df1.format(f10), "R");

				// 戶名固定做wrap

				ArrayList<String> custNameWrap = longStringWrap(rowLD004.get("F9"), 20);

				this.print(1, 1, "│　　　　　　　　　　│　戶名：　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (custNameWrap.size() >= 1) {
					this.print(0, 34, custNameWrap.get(0));
				}
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (custNameWrap.size() >= 2) {
					this.print(0, 34, custNameWrap.get(1));
				}
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (custNameWrap.size() >= 3) {
					this.print(0, 34, custNameWrap.get(2));
				}
				// 先輸出到最大三十字，如果將來有更長的戶名需要輸出，
				// 這裡可以考慮改成for迴圈

				this.print(1, 1, "│　　　　　　　　　　│　計算時間：　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(0, 37,
						showDate(rowLD004.get("F11").toString(), 1) + " - " + showDate(rowLD004.get("F12").toString(), 1));
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(1, 1, "└──────────┴──────────────────┴─────────┘");
				this.print(1, 52, "新台幣　" + ConvertUpMoney.toChinese(rowLD004.get("F10")), "R");
				String tmp = rowLD004.get("F13");
				String TLRNO = titaVo.getParam("TLRNO");
				if (rowLD004.get("F13") == null || rowLD004.get("F13") == "") {
					tmp = TLRNO;
				}
				this.print(0, 66, "製票：　" + tmp);
				this.print(1, 1, "＊此聯由繳款人收執、無收款印章無效");

			} // for

		} else {
			// 本日無資料
			this.print(1, 1, "本日無資料");

			this.print(1, 33, "新光人壽保險股份有限公司");
			this.print(1, 2, "傳票號碼　.....　");
			this.print(1, 2, "交易序號　.....　");

			this.print(0, 52, "科目：　");

			this.print(1, 3, "現金　　電匯　　票據　　調整");
			this.print(1, 1, "┌──────────┬──────────────────┬─────────┐");
			this.print(1, 1, "│　　　　子目　　　　│　　摘（　　　　　　入帳）要　　　　│　　　 金額 　　　│");
			this.print(1, 1, "├──────────┼──────────────────┼─────────┤");
			this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "│　　　　　　　　　　│　戶號：　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(0, 38, "-");
			this.print(0, 42, "-");
			this.print(0, 63, "$");
			this.print(1, 1, "│　　　　　　　　　　│　戶名：　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "│　　　　　　　　　　│　計算時間：　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(0, 43, "-");
			this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
			this.print(1, 1, "└──────────┴──────────────────┴─────────┘");
			this.print(1, 52, "新台幣　");
			this.print(0, 66, "製票：");
			this.print(1, 1, "＊此聯由繳款人收執、無收款印章無效");
		}

		this.close();
		// this.toPdf(sno);

	}

	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
		if (iType == 1) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);
			} else {
				return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月" + rocdatex.substring(4, 6) + "日";
			} else {
				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月" + rocdatex.substring(5, 7) + "日";
			}
		} else {
			return rocdatex;
		}
	}

	private ArrayList<String> longStringWrap(String s, int lengthPerPart) {
		this.info("LD004 longStringWrap is called.");
		this.info("string: " + s);
		this.info("lengthPerPart: " + lengthPerPart);

		ArrayList<String> result = new ArrayList<String>();

		if (s == null || s.isEmpty()) {
			// 如果輸入有誤, 回傳空list
			return result;
		} else if (lengthPerPart <= 0 || mixedLength(s) <= lengthPerPart) {
			// 如果不需要切, 直接回傳
			result.add(s);
			return result;
		}

		// 開始切

		int cutPos = 0;

		while (cutPos < mixedLength(s)) {
			String cut = StringCut.stringCut(s, cutPos, cutPos + lengthPerPart);
			cutPos += mixedLength(cut);
			result.add(cut);
		}

		return result;
	}

	private int mixedLength(String s) {
		try {
			return (s.getBytes("UTF-8").length - s.length()) / 2 + s.length();
		} catch (UnsupportedEncodingException e) {
			this.error("LD004 MixedLength() failed to encode!");
			this.error("Original string: " + s);
			return 0;
		}
	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

}
