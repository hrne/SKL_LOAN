package com.st1.itx.util.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 共用套印存入憑條
 * 
 * @author yu-heng
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class FormCom extends MakeReport {

	@Autowired
	WebClient webClient;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
	}

	@Override
	public void printHeader() {
	}

	public long exec(TitaVo titaVo) throws LogicException {

		String tran = titaVo.getTxcd();

		this.info("titaVo = " + titaVo);
		this.openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), tran, "存入憑條", "cm,20,9.31333", "P");

		// X軸 ,Y軸
		setFont(1, 14);

		String date = titaVo.getParam("fmEntryDate");


		// 中華民國
		printCm(15.4, 0.9, date.substring(0, 3));
		printCm(16.7, 0.9, date.substring(3, 5));
		printCm(18.1, 0.9, date.substring(5, 7));

		String account = titaVo.getParam("fmAccount");
		// 存入帳號 1.5 +0.6... / 3
		for (int i = 0; i < 13; i++) {
			double x = 9.9 - (i * 0.615);
			int ii = 14 - 1 - i;
			printCm(x, 2.8, account.substring(ii, ii + 1));
		}

		String amtX = titaVo.getParam("fmAmt");
		// 新台幣
		for (int i = 0; i < amtX.length(); i++) {
			double x = 18.5 - (i * 0.8);
			int ii = amtX.length() - 1 - i;
			String s = amtX.substring(ii, ii + 1);
			printCm(x, 2.8, s);

		}

		String custName = titaVo.getParam("fmCustName");
		// 戶名
		printCm(2.8, 4, custName);

		String custNo = FormatUtil.pad9(titaVo.getParam("fmCustNo"), 7);
		// 戶號
		printCm(2.8, 5, custNo);
		long sno = this.close();

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), titaVo.getTxCode() + "存入憑條已完成", titaVo);
		return sno;
	}

//	private String toChinese(String s) {
//		String rs = "";
//
//		if ("0".equals(s)) {
//			rs = "０";
//		} else if ("1".equals(s)) {
//			rs = "１";
//		} else if ("2".equals(s)) {
//			rs = "２";
//		} else if ("3".equals(s)) {
//			rs = "３";
//		} else if ("4".equals(s)) {
//			rs = "４";
//		} else if ("5".equals(s)) {
//			rs = "５";
//		} else if ("6".equals(s)) {
//			rs = "６";
//		} else if ("7".equals(s)) {
//			rs = "７";
//		} else if ("8".equals(s)) {
//			rs = "８";
//		} else if ("9".equals(s)) {
//			rs = "９";
//		}
//		return rs;
//	}

//	private String toChinese2(String s) {
//		String rs = "";
//
//		if ("0".equals(s)) {
//			rs = "零";
//		} else if ("1".equals(s)) {
//			rs = "壹";
//		} else if ("2".equals(s)) {
//			rs = "貳";
//		} else if ("3".equals(s)) {
//			rs = "參";
//		} else if ("4".equals(s)) {
//			rs = "肆";
//		} else if ("5".equals(s)) {
//			rs = "伍";
//		} else if ("6".equals(s)) {
//			rs = "陸";
//		} else if ("7".equals(s)) {
//			rs = "柒";
//		} else if ("8".equals(s)) {
//			rs = "捌";
//		} else if ("9".equals(s)) {
//			rs = "玖";
//		}
//		return rs;
//	}
}
