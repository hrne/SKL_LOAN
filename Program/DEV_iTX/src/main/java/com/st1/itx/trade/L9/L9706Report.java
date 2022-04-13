package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9706ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.ConvertUpMoney;

@Component("L9706Report")
@Scope("prototype")

public class L9706Report extends MakeReport {

	@Autowired
	L9706ServiceImpl l9706ServiceImpl;

	String iENTDAY = "";

	@Override
	public void printHeader() {

		this.setFontSize(13);
		this.info("L9706 exec" + this.titaVo.get("ENTDY"));
		this.print(-1, 19, "貸   款   餘   額   證   明   書             機密等級：普通");
		// 明細起始列(自訂亦必須)
		this.setBeginRow(2);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(45);
	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9706Report exec");

		int cnt = 0;

		iENTDAY = tranDate(titaVo.getParam("ENTDY"));

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9706", "貸款餘額證明書", "普通", "A4", "P");

		List<Map<String, String>> loanBTXList = null;

		try {
			loanBTXList = l9706ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L9706ServiceImpl.LoanBorTx error = " + e.toString());
		}

		this.info("L9706Report LoanBTXList =" + loanBTXList.toString());

		if (loanBTXList != null && loanBTXList.size() != 0) {
			for (Map<String, String> tL9706Vo : loanBTXList) {
				if (cnt == 2) {
					this.newPage();
					cnt = 0;
				}
				report1(tL9706Vo);
				cnt += 1;
			}
		} else {
			this.print(1, 1, "無資料");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 5, "查");
			this.print(0, 58, "君前於");
			this.print(1, 1, "");
			this.print(0, 37, "向本公司申借");
			this.print(0, 58, "購置住");
			this.print(1, 1, "");
			this.print(1, 1, "宅貸款金額");
			this.print(0, 12, "整");
			this.print(0, 62, "，");
			this.print(1, 1, "");
			this.print(1, 1, "戶號 ");
			this.print(0, 20, "截至");
			this.print(0, 62, iENTDAY, "R");
			this.print(0, 62, "止");
			this.print(1, 1, "");
			this.print(1, 1, "尚欠本金餘額");
			this.print(0, 14, "整");
			this.print(0, 62, "。");
			this.print(1, 1, "");
			this.print(1, 18, "此  致");
			this.print(1, 1, "");
			this.print(1, 9, "台  照");
			this.print(1, 1, "");
			this.print(1, 40, "新光人壽保險股份有限公司");
			this.print(1, 1, "");
			this.print(1, 4, "中 　 華 　 民 　 國  ");
			this.print(0, 64, iENTDAY, "R");
		}
		this.close();

		// 測試用
		//this.toPdf(sno);
		if (loanBTXList != null && loanBTXList.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	private void report1(Map<String, String> tL9706Vo) throws LogicException {

		String tmp = "";

		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 5, "查");

		this.print(0, 13, tL9706Vo.get("F0"));
		this.print(0, 58, "君前於");
		this.print(1, 1, "");
		this.print(1, 37, tranDate(tL9706Vo.get("F1")), "R");
		this.print(0, 37, "向本公司申借");

		tmp = "";
		if (!tL9706Vo.get("F2").equals("0")) {
			tmp = tmp + tL9706Vo.get("F2") + "年";
		}
		if (!tL9706Vo.get("F3").equals("0")) {
			tmp = tmp + tL9706Vo.get("F3") + "月";
		}
		if (!tL9706Vo.get("F4").equals("0")) {
			tmp = tmp + tL9706Vo.get("F4") + "天";
		}
		tmp = tmp + "期";

		this.print(0, 50, tmp);
		this.print(0, 58, "購置住");
		this.print(1, 1, "");
		this.print(1, 1, "宅貸款金額");
		tmp = ConvertUpMoney.toChinese(tL9706Vo.get("F5"));
		this.print(0, 12, tmp + "整");
		this.print(0, 62, "，");
		tmp = String.format("%07d", Integer.valueOf(tL9706Vo.get("F6"))) + " - " + String.format("%03d", Integer.valueOf(tL9706Vo.get("F7")));
		this.print(1, 1, "");
		this.print(1, 1, "戶號 ");
		this.print(0, 6, tmp);
		this.print(0, 20, "截至");
		this.print(0, 62, iENTDAY, "R");
		this.print(0, 62, "止");
		this.print(1, 1, "");
		this.print(1, 1, "尚欠本金餘額");
		tmp = ConvertUpMoney.toChinese(tL9706Vo.get("F8"));
		this.print(0, 14, tmp + "整");
		this.print(0, 62, "。");
		this.print(1, 1, "");
		this.print(1, 18, "此  致");
		this.print(1, 1, "");
		this.print(1, 9, "台  照");
		this.print(1, 1, "");
		this.print(1, 40, "新光人壽保險股份有限公司");
		this.print(1, 1, "");
		this.print(1, 4, "中 　 華 　 民 　 國  ");
		this.print(0, 64, iENTDAY, "R");
	}

	private String tranDate(String date) {
		this.info("tranDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}

		String rocdatex = String.valueOf(rocdate);
		this.info("tranDate2 = " + rocdatex);
		this.info("tranDate2 LEN = " + rocdatex.length());

		String rocdatexx = fullForm(rocdatex);
		rocdatex = "";
		int i = 0;
		if (rocdatexx.length() == 6) {
			rocdatex = rocdatexx.substring(0, 2) + "   年   ";
			i = 2;
		} else {
			rocdatex = rocdatexx.substring(0, 3) + "   年   ";
			i = 3;
		}
		if (rocdatexx.substring(i, i + 1).equals("０")) {
			rocdatex = rocdatex + " " + rocdatexx.substring(i + 1, i + 2) + "    月   ";
		} else {
			rocdatex = rocdatex + rocdatexx.substring(i, i + 2) + "   月   ";
		}
		i += 2;

		if (rocdatexx.substring(i, i + 1).equals("０")) {
			rocdatex = rocdatex + " " + rocdatexx.substring(i + 1, i + 2) + "    日";
		} else {
			rocdatex = rocdatex + rocdatexx.substring(i, i + 2) + "   日";
		}
		return rocdatex;
	}

	// 數字半型轉全型
	private String fullForm(String idata) {
		int tranTemp = 0;
		char tmp;
		String odata = "";

		for (int i = 0; i < idata.length(); i++) {

//			this.info("idata i = " + i);
//			this.info("idata X = " + idata.substring(i, i + 1));
			tmp = idata.charAt(i);

			tranTemp = (int) tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

//			this.info("tranDate XX= " + (char) tranTemp);
			odata += (char) tranTemp;
		}
		return odata;
	}

}
