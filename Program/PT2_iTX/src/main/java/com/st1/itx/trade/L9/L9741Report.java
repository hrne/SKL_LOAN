package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L9741ServiceImpl;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L9705Report
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9741Report extends MakeReport {

	@Autowired
	public L9741ServiceImpl iL9741ServiceImpl;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	private Parse parse;

	@Autowired
	private WebClient webClient;

	@Autowired
	CustNoticeCom custNoticeCom;

	private int custNo = 0;
	private int facmNo = 0;
	/**
	 * 字體大小
	 * */
	private int FontSize = 13;

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void exec(List<Map<String, String>> l9741List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		String rptitem = "火險及地震險保費繳款通知單";

		String tran = titaVo.getTxCode().isEmpty() ? "L9741" : titaVo.getTxCode();

		this.custNo = 0;
		this.facmNo = 0;

		// 開啟報表
		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI()).setRptCode(tran)
				.setRptItem(rptitem).setRptSize("A4").setSecurity("").setPageOrientation("P").build();

		this.open(titaVo, reportVo);

		if (l9741List.size() > 0) {

			int count = 0;

			this.info("isOpen Report");
			this.info("l9741List=" + l9741List.toString());

			for (Map<String, String> r : l9741List) {

				int custNo = 0;
				int facmNo = 0;

				if (r.get("CustNo") != null) {
					custNo = parse.stringToInteger(r.get("CustNo"));
				}
				if (r.get("FacmNo") != null) {
					facmNo = parse.stringToInteger(r.get("FacmNo"));
				}

				this.info("L9741 custNo=" + custNo);
				this.info("L9741 facmNo=" + facmNo);
				
				if (count > 0) {
					this.newPage();
				}

				this.custNo = custNo;
				this.facmNo = facmNo;

				exportData(l9741List, titaVo, txbuffer, count);

				count++;

			}

		} else {
			// 列印地址		
			this.setFontSize(FontSize - 3);
			this.print(1, 15, "新光人壽保險股份有限公司 放款部放款服務課");
			this.print(1, 15, "105409 台北市松山區南京東路五段125號13樓");
			this.print(1, 15, "電話 (02) 2389 5858 放款服務課");
			this.print(1, 15, "www.skl.com.tw");
			this.print(1, 17, "【限定本人拆閱，若無此人，請寄回本公司】");
			
			this.setFontSize(FontSize);
			this.print(1, 17, "");
			this.print(1, 1, "");
			this.print(1, 17, "*******    查無資料    ******");
			this.print(1, 1, "");
			this.print(1, 17, "");
			this.print(1, 1, "");

			// 印圖片時是設定座標(x,y)
			// 整個文件的最左上角是(0,0)
			this.printImage(25, 220, 35f, "ReportSklLogo.jpg");

			this.setFontSize(FontSize + 4);
			this.print(1, 1, "");
			this.print(1,  this.getMidXAxis(), "新光人壽保險股份有限公司","C");
			this.print(4, 1, "");
			this.setFontSize(FontSize -1);
			this.print(2, 1, "");
			this.print(1,  this.getMidXAxis(), "火險及地震險保費 繳款通知單","C");
			this.print(1, 1, "");

			this.setFontSize(FontSize - 3);
			this.print(5, 1, "");

			this.print(1, 9, "親愛的房貸客戶您好：");
			this.print(1, 9, "承蒙您於本公司辦理房屋抵押貸款，謹致謝忱！");
			this.print(1, 9, "您的火險及地震險保單即將到期（內容請參照保費明細表），本公司將於保單到期日當月與房貸期款一併扣繳，且");
			this.print(1, 9, "先且先扣繳火險及地震險保費，再扣繳期款，並向新光產物保險（股）公司辦理續保手續。");
			this.print(1, 9, "一、採銀行（郵局）自動扣款者，敬請貴貸戶留意增加火險及地震險總保費金額。");
			this.print(1, 9, "二、自行轉匯款者，敬請貴貸戶務必與期款一併匯款。");
			this.print(1, 9, "謹先奉達，並頌安祺！");
			this.print(1, 9, "備註：");
			this.print(1, 9, "1.	依據財政部91.01.25台財保字第0910750050號函辦理，自91.04.01起推行「新住宅火險及地震險」方案，住宅");
			this.print(1, 9, " 火險自動涵蓋地震險，保險期間為一年期。");
			this.print(1, 9, "2.	依據台端所簽具之火險及地震保險續保切結書之內文，保險費與房貸期款一併繳交，如保險費有未全額，本公");
			this.print(1, 9, " 司得自所繳交期款中優先扣取。如欲自行投保者，請於原保單到期前15日且須於扣款繳交情形前將續期投保之保");
			this.print(1, 9, " 險單正本及收據副本交付公司收執。");
			this.print(1, 9, "                                                                    新光人壽保險股份有險公司 敬啟");

		
			setFont(1, 16);
			printCm(10 , 21, "火險及地震險保費明細表", "C");

			int top = 0;// 上下微調用
			double yy = 20;// 開始Y軸
			double h = 0.4;// 列高
			double l = 0;// 列數
			double y = top + yy;
			
			setFont(1, 13);
			y = top + yy + (++l) * h;
			printCm(3, y + 2, "貸款戶號：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			printCm(3, y + 2, "客戶名稱：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			printCm(3, y + 2, "押品地址：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			printCm(3, y + 2,"續保期間：");
			printCm(11, y + 2, "火險保費：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			printCm(3, y + 2, "火險保額：" );
			printCm(11, y + 2, "地震險保費：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			printCm(3, y + 2, "地震險保額：");
			printCm(11, y + 2, "總保費：" );

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

			y = top + yy + (++l) * h;
			
			this.printCm(3, y + 2, "匯款帳號：");

			y = top + yy + (++l) * h;
			printCm(2, y + 2, " ");

		}

		// 關閉報表
		this.close();

		if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"),
					titaVo.getTxCode().isEmpty() ? "L9741" : titaVo.getTxCode() + "火險及地震險保費繳款通知單已完成", titaVo);
		}

	}

	private void exportData(List<Map<String, String>> r, TitaVo titaVo, TxBuffer txbuffer, int c)
			throws LogicException {
		this.info("exportData.count = 第" + (c + 1) + "筆");
		this.info("exportData.List = " + r.toString());

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		String currAddress = custNoticeCom.getCurrAddress(custMain, titaVo);

		// 列印地址
		this.setFontSize(FontSize - 3);
		this.print(1, 15, "新光人壽保險股份有限公司 放款部放款服務課");
		this.print(1, 15, "105409 台北市松山區南京東路五段125號13樓");
		this.print(1, 15, "電話 (02) 2389 5858 放款服務課");
		this.print(1, 15, "www.skl.com.tw");
		this.print(1, 17, "【限定本人拆閱，若無此人，請寄回本公司】");
		
		this.setFontSize(FontSize);
		this.print(1, 17, custMain.getCurrZip3().trim() + custMain.getCurrZip2().trim());
		this.print(1, 17, currAddress);
		this.print(1, 1, "");
		int nameLength = 20;
		if (custMain.getCustName().length() < 20) {
			nameLength = custMain.getCustName().length();
		}

		this.print(1, 17, String.format("%07d", custNo) + "   " + custMain.getCustName().substring(0, nameLength));
		this.print(1, 1, "");

		// 印圖片時是設定座標(x,y)
		// 整個文件的最左上角是(0,0)
		this.printImage(25, 220, 35f, "ReportSklLogo.jpg");

		this.setFontSize(FontSize + 4);
		this.print(1, 1, "");
		this.print(1,  this.getMidXAxis(), "新光人壽保險股份有限公司","C");
		this.print(4, 1, "");
		
		this.setFontSize(FontSize -1);
		this.print(2, 1, "");
		this.print(1,  this.getMidXAxis(), "火險及地震險保費 繳款通知單","C");
		this.print(1, 1, "");

		this.setFontSize(FontSize - 3);
		this.print(5, 1, "");
		this.print(1, 9, "親愛的房貸客戶您好：");
		this.print(1, 9, "承蒙您於本公司辦理房屋抵押貸款，謹致謝忱！");
		this.print(1, 9, "您的火險及地震險保單即將到期（內容請參照保費明細表），本公司將於保單到期日當月與房貸期款一併扣繳，且");
		this.print(1, 9, "先且先扣繳火險及地震險保費，再扣繳期款，並向新光產物保險（股）公司辦理續保手續。");
		this.print(1, 9, "一、採銀行（郵局）自動扣款者，敬請貴貸戶留意增加火險及地震險總保費金額。");
		this.print(1, 9, "二、自行轉匯款者，敬請貴貸戶務必與期款一併匯款。");
		this.print(1, 9, "謹先奉達，並頌安祺！");
		this.print(1, 9, "備註：");
		this.print(1, 9, "1.	依據財政部91.01.25台財保字第0910750050號函辦理，自91.04.01起推行「新住宅火險及地震險」方案，住宅");
		this.print(1, 9, " 火險自動涵蓋地震險，保險期間為一年期。");
		this.print(1, 9, "2.	依據台端所簽具之火險及地震保險續保切結書之內文，保險費與房貸期款一併繳交，如保險費有未全額，本公");
		this.print(1, 9, " 司得自所繳交期款中優先扣取。如欲自行投保者，請於原保單到期前15日且須於扣款繳交情形前將續期投保之保");
		this.print(1, 9, " 險單正本及收據副本交付公司收執。");
		this.print(1, 9, "                                                                    新光人壽保險股份有險公司 敬啟");

	
		setFont(1, 16);
		printCm(10 , 21, "火險及地震險保費明細表", "C");

		int top = 0;// 上下微調用
		double yy = 20;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數
		double y = top + yy;
		
		setFont(1, 13);
		y = top + yy + (++l) * h;
		printCm(3, y + 2, "貸款戶號：" + String.format("%07d", Integer.valueOf(custNo)) + "-"
				+ String.format("%03d", Integer.valueOf(facmNo)));
		String repayCode = r.get(c).get("RepayCode");
		printCm(18, y + 2, repayCodeX(repayCode), "R");

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y + 2, "客戶名稱：" + custMain.getCustName().substring(0, nameLength));

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		int iFireInsuCovrg = parse.stringToInteger(r.get(c).get("FireInsuCovrg"));
		int iEthqInsuCovrg = parse.stringToInteger(r.get(c).get("EthqInsuCovrg"));
		int iFireInsuPrem = parse.stringToInteger(r.get(c).get("FireInsuPrem"));
		int iEthqInsuPrem = parse.stringToInteger(r.get(c).get("EthqInsuPrem"));
		int iTotInsuPrem = parse.stringToInteger(r.get(c).get("TotInsuPrem"));
		int iInsuStartDate = parse.stringToInteger(r.get(c).get("InsuStartDate")) - 19110000;
		int iInsuEndDate = parse.stringToInteger(r.get(c).get("InsuEndDate")) - 19110000;
		int iNextRepayDate = parse.stringToInteger(r.get(c).get("NextRepayDate")) - 19110000;
		String tempDate1 = String.valueOf(iInsuStartDate);
		String tempDate2 = String.valueOf(iInsuEndDate);
		String tempDate3 = String.valueOf(iNextRepayDate);

		y = top + yy + (++l) * h;
		printCm(3, y + 2, "押品地址：" + r.get(c).get("BdLocation"));

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y + 2,
				"續保期間：" + tempDate1.substring(0, 3) + "/" + tempDate1.substring(3, 5) + "/" + tempDate1.substring(5, 7)
						+ " - " + tempDate2.substring(0, 3) + "/" + tempDate2.substring(3, 5) + "/"
						+ tempDate2.substring(5, 7));
		printCm(11, y + 2, "火險保費：" + String.format("%,d", iFireInsuPrem));

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y + 2, "火險保額：" + String.format("%,d", iFireInsuCovrg));
		printCm(11, y + 2, "地震險保費：" + String.format("%,d", iEthqInsuPrem));

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y + 2, "地震險保額：" + String.format("%,d", iEthqInsuCovrg));
		printCm(11, y + 2, "總保費：" + String.format("%,d", iTotInsuPrem) + " (應繳日：" + tempDate3.substring(0, 3) + "/"
				+ tempDate3.substring(3, 5) + "/" + tempDate3.substring(5, 7) + ")");

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		y = top + yy + (++l) * h;
		String payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(custNo));
		this.printCm(3, y + 2, "匯款帳號：" + payIntAcct);

		y = top + yy + (++l) * h;
		printCm(2, y + 2, " ");

		int iStatusCode = parse.stringToInteger(r.get(c).get("StatusCode"));

//		if (iStatusCode==1) {//借支時才印此行
		y = top + yy + (++l) * h;
		this.printCm(3, y + 2, "台端有一火險保費，至今仍未入金，此費用請與房貸期款一併繳納，謝謝您的");
		y = top + yy + (++l) * h + 0.1;
		this.printCm(3, y + 2, "配合。");
//		}

	}

	private String repayCodeX(String repayCode) {
		String result = "";
		switch (repayCode) {
		case "1":
			result = "匯款轉帳";
			break;
		case "2":
			result = "銀行扣款";
			break;
		case "3":
			result = "員工扣薪";
			break;
		case "4":
			result = "支票繳款";
			break;
		default:
			break;
		}

		return result;
	}
}
