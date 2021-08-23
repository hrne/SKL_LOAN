package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD004ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.ConvertUpMoney;

@Component
@Scope("prototype")

public class LD004Report extends MakeReport {

	@Autowired
	LD004ServiceImpl lD004ServiceImpl;

	@Autowired
	MakeReport makeReport;

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
		List<Map<String, String>> LD004List = null;
		try {
			LD004List = lD004ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lD004ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		makeReport(titaVo, LD004List);

		return true;
	}

	public void makeReport(TitaVo titaVo, List<Map<String, String>> LD004List) throws LogicException {
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD004", "企金戶還本收據及繳息收據", "", "A4", "L");
		if (LD004List != null && LD004List.size() != 0) {
			for (Map<String, String> LD4Vo : LD004List) {
				/*
				 * F0 = 傳票號碼 F1 = 登錄交易序號 F2 = 科目代號 F3 = 業務科目名稱 F4 = 彙總別 F5 = 入帳日期 F6 = 戶號 F7 =
				 * 額度編號 F8 = 撥款序號 F9 = 戶名/公司名稱 F10 = 記帳金額 F11 = 計息起日 F12 = 計息迄日
				 */
				String An = LD4Vo.get("F2").substring(0, 1);

				this.info("EntryDate ... = " + LD4Vo.get("F5"));
				this.info("IntStartDate ... = " + LD4Vo.get("F11"));
				this.info("IntEndDate ... = " + LD4Vo.get("F12"));

				this.print(1, 33, "新光人壽保險股份有限公司");
				this.print(1, 2, "傳票號碼　.....　");
				this.print(0, 19, LD4Vo.get("F0"), "L");

				if ("1".equals(An)) {
					this.print(0, 41, "還本收據");
				} else {
					this.print(0, 41, "繳息收據");
				}

				this.print(1, 2, "交易序號　.....　");
				this.print(0, 26, padStart(LD4Vo.get("F1").toString(), 7, "0"), "R");

				this.print(0, 56, "科目：　");
				this.print(0, 64, LD4Vo.get("F2"));
				this.print(1, 40, showDate(LD4Vo.get("F14").toString(), 1));

				if ("1".equals(An)) {
					this.print(0, 64, "擔保放款" + "－");
				} else {
					this.print(0, 64, "利息收入" + "－");
				}

				if (LD4Vo.get("F3").toString().length() >= 4) {
					this.print(0, 74, LD4Vo.get("F3").substring(0, 4));
				} else {
					this.print(0, 74, LD4Vo.get("F3"));
				}

				this.print(1, 3, "現金　　電匯　　票據　　調整");

				if (LD4Vo.get("F3").toString().length() >= 4) {
					this.print(0, 64, LD4Vo.get("F3").toString().substring(4, LD4Vo.get("F3").toString().length()));
				}

				this.print(1, 1, "┌──────────┬──────────────────┬─────────┐");
				this.print(1, 1, "│　　　　子目　　　　│　　摘（　　　　　　入帳）要　　　　│　　　 金額 　　　│");
				this.print(0, 39, showDate(LD4Vo.get("F5").toString(), 1), "C");
				this.print(1, 1, "├──────────┼──────────────────┼─────────┤");
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (!LD4Vo.get("F16").equals("11"))
				{
					this.print(0, 55, "非支票");
				}
				this.print(1, 1, "│　　　　　　　　　　│　戶號：　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(0, 13, LD4Vo.get("F15"), "C");
				this.print(0, 34, padStart(LD4Vo.get("F6").toString(), 7, "0") + "-" + padStart(LD4Vo.get("F7").toString(), 3, "0") + "-" + padStart(LD4Vo.get("F8").toString(), 3, "0"));
//				this.print(0, 38, "-");
//				this.print(0, 39, padStart(LD4Vo.get("F7").toString(), 3, "0"));
//				this.print(0, 42, "-");
//				this.print(0, 43, padStart(LD4Vo.get("F8").toString(), 3, "0"));

				this.print(0, 63, "$");

				BigDecimal f10 = new BigDecimal(LD4Vo.get("F10").toString());
				DecimalFormat df1 = new DecimalFormat("#,##0");
				this.print(0, 80, df1.format(f10), "R");
				
				// 戶名太長時做wrap
				
				String[] custNameWrap = new String[(int)Math.ceil((double)LD4Vo.get("F9").length() / 10)];
				
				for (int i = 0; i < custNameWrap.length; i++)
				{
					if (i < custNameWrap.length - 1)
					{
						custNameWrap[i] = LD4Vo.get("F9").substring(10*i, 10*i+10);
					} else {
						custNameWrap[i] = LD4Vo.get("F9").substring(10*i);
					}
				}

				this.print(1, 1, "│　　　　　　　　　　│　戶名：　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(0, 34, custNameWrap[0]);
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (custNameWrap.length >= 2) { this.print(0, 34, custNameWrap[1]); }
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				if (custNameWrap.length >= 3) { this.print(0, 34, custNameWrap[2]); }
				// 先輸出到最大三十字，如果將來有更長的戶名需要輸出，
				// 這裡可以考慮改成for迴圈
				
				this.print(1, 1, "│　　　　　　　　　　│　計算時間：　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(0, 37, showDate(LD4Vo.get("F11").toString(), 1) + " - " + showDate(LD4Vo.get("F12").toString(), 1));
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(1, 1, "│　　　　　　　　　　│　　　　　　　　　　　　　　　　　　│　　　　　　　　　│");
				this.print(1, 1, "└──────────┴──────────────────┴─────────┘");
				this.print(1, 52, "新台幣　" + ConvertUpMoney.toChinese(LD4Vo.get("F10")), "R");
				String tmp = LD4Vo.get("F13");
				String TLRNO = titaVo.getParam("TLRNO");
				if (LD4Vo.get("F13") == null || LD4Vo.get("F13") == "") {
					tmp = TLRNO;
				}
				this.print(0, 66, "製票：　" + tmp);
				this.print(1, 1, "＊此聯由繳款人收執、無收款印章無效");

				newPage();

			} // for

		} else {
			// 本日無資料
//			for (Map<String, String> LD4Vo : LD004List) {
//			String An = LD4Vo.get("F2").substring(0, 1);
//			if ("1".equals(An)) {
//				this.print(0, 41, "還本收據");
//			} else {
//				this.print(0, 41, "繳息收據");
//			}
			this.print(1, 1, "本日無資料");
			this.print(1, 33, "新光人壽保險股份有限公司");
			this.print(1, 2, "傳票號碼　.....　");
			this.print(1, 2, "交易序號　.....　");

			this.print(0, 52, "科目：　");

//			if ("1".equals(An)) {
//				this.print(0, 60, "擔保放款" + "－");
//			} else {
//				this.print(0, 60, "利息收入" + "－");
//			}

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

		long sno = this.close();
		this.toPdf(sno);

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

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

}
