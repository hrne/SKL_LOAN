package com.st1.itx.trade.L2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2980Report")
@Scope("prototype")

public class L2980Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	@Override
	public void printHeader() {

		this.info("L2980Report.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(4);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void printHeaderP() {
		this.setCharSpaces(0);
		this.setFontSize(12);
		this.print(-3, 10, "個人房貸調整案");
		this.print(-3, 80, "機密等級：機密");

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		this.info("L2980Report exec");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2980", "個人房貸調整案", "密", "A4", "P");

		this.print(1, 53, "借款人戶號 . . .  " + titaVo.getParam("CustNo") + "-" + titaVo.getParam("FacmNo"));
		this.print(3, 10, "身分證字號 . . .  " + titaVo.getParam("CustId"));
		this.print(0, 53, "客戶別 . . . . .  " + titaVo.getParam("CustTypeCode") + "    " + titaVo.getParam("CustTypeCodeX"));
		this.print(1, 10, "中文姓名 . . . .  " + titaVo.getParam("CustName"));
		this.print(0, 53, "繳款方式 . . . .  " + titaVo.getParam("RepayCode") + "    " + titaVo.getParam("RepayCodeX"));
		this.print(1, 10, "配偶姓名 . . . .  " + titaVo.getParam("SpouseName") + "    " +titaVo.getParam("SpouseId"));
		this.print(0, 53, "扣款銀行 . . . .  " + titaVo.getParam("RepayBank") + "    " + titaVo.getParam("RepayBankX"));
		this.print(1, 10, "電子信箱 . . . .  " + titaVo.getParam("Email"));
		this.print(2, 10, "基本利率代碼 . .  " + titaVo.getParam("ProdNo") + "  " + titaVo.getParam("ProdNoX"));
		this.print(1, 10, "貸款期間 . . . .  " + titaVo.getParam("LoanTermYy") + "  年  " + titaVo.getParam("LoanTermMm") + "  月");
		this.print(0, 53, "利率區分 . . . .  " + titaVo.getParam("RateCode") + "  " + titaVo.getParam("RateCodeX"));
		this.print(1, 10, "核准額度 . . . .  ");
		this.print(0, 43, titaVo.getParam("ApplAmtX"), "R");

		this.print(0, 53, "首次撥款日 . . .  " + DateFormat(titaVo.getParam("FirstDrawdownDate")));
		this.print(1, 10, "目前餘額 . . . .  ");
		this.print(0, 43, titaVo.getParam("LoanBalX"), "R");
		this.print(0, 53, "核准利率 . . . .  ");
		this.print(0, 83, titaVo.getParam("ApproveRate"), "R");

		this.print(1, 10, "違約還款月數 . .  " + titaVo.getParam("BreachDecreaseMonth"));
		this.print(0, 53, "目前利率 . . . .  ");
		this.print(0, 83, titaVo.getParam("StoreRate"), "R");
		this.print(0, 85, DateFormat(titaVo.getParam("DrawdownDate")));
		this.print(1, 10, "違約率－金額 . .  ");
		this.print(0, 34, titaVo.getParam("BreachPercent"), "R");
		this.print(0, 53, "下次調整利率 . .  ");
		this.print(0, 83, titaVo.getParam("NextAdjRate"), "R");
		this.print(0, 85, DateFormat(titaVo.getParam("NextAdjRateDate")));
		this.print(1, 10, "利率加減碼 . . .  ");
		this.print(0, 34, titaVo.getParam("RateIncr"), "R");
		this.print(2, 10, "押品地址 . . . .  " + titaVo.getParam("BdLocation"));
		this.print(1, 10, "押品提供人 . . .  " + titaVo.getParam("OwnerName") + "  " + titaVo.getParam("OwnerId"));
		this.print(1, 10, "主建物（坪） . .  " + titaVo.getParam("FloorArea"));
		this.print(1, 10, "公設（坪） . . .  " + titaVo.getParam("Area"));
		this.print(1, 10, "車位（坪） . . .  " + titaVo.getParam("ParkingArea"));

		String StatusName = "";
		switch (titaVo.getParam("Status")) {
		case "1":
			StatusName = "完全正常";
			break;
		case "2":
			StatusName = "非常正常";
			break;
		case "3":
			StatusName = "正常";
			break;
		case "4":
			StatusName = "不正常";
			break;
		default:
			break;
		}
		this.print(2, 10, "繳息狀況 . . . .  " + titaVo.getParam("Status") + "  " + StatusName);

		int Reason = parse.stringToInteger(titaVo.getParam("Reason"));
		switch (Reason) {
		case 1:
			this.print(1, 10, "調整原因 . . . .  1  調降利率");
			this.print(1, 10, "客戶要求利率 . .");
			this.print(0, 40, titaVo.getParam("CustomerRate"), "R");
			this.print(1, 10, "擬調利率 . . . .");
			this.print(0, 40, titaVo.getParam("ExpectedRate"), "R");
			this.print(1, 10, "備註 . . . . . .  " + titaVo.getParam("ReMark"));
			break;
		case 2:
			this.print(1, 10, "調整原因 . . . .  2  變更還款方式");
			this.print(3, 10, "備註 . . . . . .  " + titaVo.getParam("ReMark"));
			break;
		case 3:
			this.print(1, 10, "調整原因 . . . .  3  延長寬限期 " + titaVo.getParam("GracePeriod"));
			this.print(3, 10, "備註 . . . . . .  " + titaVo.getParam("ReMark"));
			break;
		case 4:
			this.print(1, 10, "調整原因 . . . .  4  縮短年限 " + titaVo.getParam("ShortenYear"));
			this.print(3, 10, "備註 . . . . . .  " + titaVo.getParam("ReMark"));
			break;
		case 5:
			this.print(1, 10, "調整原因 . . . .  5  延長年限 " + titaVo.getParam("ExtendYear"));
			this.print(3, 10, "備註 . . . . . .  " + titaVo.getParam("ReMark"));
			break;
		default:
			break;
		}

		this.print(10, 16, "職業：  _____________ ");
		this.print(1, 16, "收入：  _____________ ");
		this.print(1, 16, "地區：  _____________ ");

		this.print(10, 10, "◎除利率調整外，有年期變更者，均須附申請書（借款人親自簽名、簽章）");
		this.print(5, 10, "經                      經                          協");
		this.print(1, 10, "辦                      理                          理");
		long sno = this.close();
		this.toPdf(sno);
	}

	public String DateFormat(String date) {
		String temp = "";
		if (date.length() == 7) {
			temp = date.substring(0, 3) + "/" + date.substring(3, 5) + "/" + date.substring(5, 7);
		} else {
			temp = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 6);
		}

		return temp;
	}

}
