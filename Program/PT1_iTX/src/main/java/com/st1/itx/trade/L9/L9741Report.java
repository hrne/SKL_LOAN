package com.st1.itx.trade.L9;

import java.util.ArrayList;
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

	public int custNo = 0;
	public int facmNo = 0;

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public long exec(List<Map<String, String>> l9741List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		long cls = 0;

		String rptitem = "火險及地震險保費繳款通知單";

		String tran = titaVo.getTxCode().isEmpty() ? "L9741" : titaVo.getTxCode();

		this.custNo = 0;
		this.facmNo = 0;
		l9741List = iL9741ServiceImpl.findAll(titaVo);
		if (l9741List.size() > 0) {

			int count = 0;

			this.info("isOpen Report");
			this.info("l9741List=" + l9741List.toString());

			// 開啟報表
			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
					.setRptCode(tran).setRptItem(rptitem).setRptSize("A4").setSecurity("").setPageOrientation("P")
					.build();

			this.open(titaVo, reportVo);

			for (Map<String, String> r : l9741List) {
				int time = 0;
				time++;
				try {
					Thread.sleep(1 * 500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

				// 同戶號 同額度使用同一頁否則換新頁
//				if (this.custNo == custNo && this.facmNo == facmNo) {
//
//				} else {
				if (time > 0) {
					this.newPage(true);
				}
//					if (count > 0) {
//						this.info("...newPage");
//
//						this.newPage(true);
//
//					}

//				}

				this.custNo = custNo;
				this.facmNo = facmNo;

				exportData(l9741List, titaVo, txbuffer, count);

				count++;

			}

		} else {
			int standardFontSize = 13;
			this.info("no data");

			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
					.setRptCode(tran + "A").setRptItem(rptitem).setRptSize("inch,8.5,12").setSecurity("")
					.setPageOrientation("P").build();

			this.open(titaVo, reportVo);

			this.setFontSize(standardFontSize - 3);
			this.print(1, 15, "新光人壽保險股份有限公司 放款部放款服務課");
			this.print(1, 15, "105409 台北市松山區南京東路五段125號13樓");
			this.print(1, 15, "電話 (02) 2389 5858 放款服務課");
			this.print(1, 15, "www.skl.tw");
			this.print(1, 1, "");
			this.setFontSize(standardFontSize);
			this.print(1, 9, "親愛的房貸客戶您好：");
			this.print(1, 9, "承蒙您於本公司辦理房屋抵押貸款，謹致謝忱！");
			this.print(1, 9, "您的火險及地震險保單即將到期（內容請參照保費明細表），本公司將於保單到期日當月與房貸期款一併");
			this.print(1, 9, "扣繳，且先扣繳火險及地震險保費，再扣繳期款，並向新光產物保險（股）公司辦理續保手續。");
			this.print(1, 9, "一、採銀行（郵局）自動扣款者，敬請貴貸戶留意增加火險及地震險總保費金額。");
			this.print(1, 9, "二、自行轉匯款者，敬請貴貸戶務必與期款一併匯款。");
			this.print(1, 9, "謹先奉達，並頌安祺！");
			this.print(1, 9, "備註：");
			this.print(1, 9, "1.	依據財政部91.01.25台財保字第0910750050號函辦理，自91.04.01起推行「新住宅火險及地震險」方案");
			this.print(1, 9, " ，住宅火險自動涵蓋地震險，保險期間為一年期。");
			this.print(1, 9, "2.	依據台端所簽具之火險及地震保險續保切結書之內文，保險費與房貸期款一併繳交，如保險費有未全額");
			this.print(1, 9, " ，本公司得自所繳交期款中優先扣取。如欲自行投保者，請於原保單到期前15日且須於扣款繳交情形前將");
			this.print(1, 9, " 續期投保之保險單正本及收據副本交付公司收執。");
			this.print(1, 9, "                                                              新光人壽保險股份有險公司 敬啟");
			this.printCm(1, 4, "*******    查無資料    ******");

			this.info("查無資料 Return...");

		}

		// 關閉報表
		cls = this.close();

		if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"),
					titaVo.getTxCode().isEmpty() ? "L9741" : titaVo.getTxCode() + "火險及地震險保費繳款通知單已完成", titaVo);
		}

		return cls;

	}

	private void exportData(List<Map<String, String>> r, TitaVo titaVo, TxBuffer txbuffer, int c)
			throws LogicException {
		int standardFontSize = 13;
		this.info("exportData.count = 第" + (c + 1) + "筆");
		this.info("exportData.List = " + r.toString());

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		String currAddress = custNoticeCom.getCurrAddress(custMain, titaVo);

		// 列印地址
		this.setFontSize(standardFontSize - 3);
		this.print(1, 15, "新光人壽保險股份有限公司 放款部放款服務課");
		this.print(1, 15, "105409 台北市松山區南京東路五段125號13樓");
		this.print(1, 15, "電話 (02) 2389 5858 放款服務課");
		this.print(1, 15, "www.skl.tw");
		this.print(1, 1, "");

		this.setFontSize(standardFontSize);
		setFont(1, 11);
//		printCm(2, 2.5, "【限定本人拆閱，若無此人，請寄回本公司】");
//		printCm(2, 3.5, custMain.getCurrZip3().trim() + custMain.getCurrZip2().trim());
//		printCm(2, 4.5, currAddress);
		this.print(1, 17, "【限定本人拆閱，若無此人，請寄回本公司】");
		this.print(1, 1, "");
		this.print(1, 17, custMain.getCurrZip3().trim() + custMain.getCurrZip2().trim());
		this.print(1, 1, "");
		this.print(1, 17, currAddress);
		this.print(1, 1, "");
		this.print(1, 1, "");
		int nameLength = 20;
		if (custMain.getCustName().length() < 20) {
			nameLength = custMain.getCustName().length();
		}

//		printCm(3, 5.5, String.format("%07d", custNo) + "   " + custMain.getCustName().substring(0, nameLength));
		this.print(1, 17, String.format("%07d", custNo) + "   " + custMain.getCustName().substring(0, nameLength));

		// 印圖片時是設定座標(x,y)
		// 整個文件的最左上角是(0,0)
		this.printImage(18, 255, 35f, "ReportSklLogo.jpg");

		int top = 0;// 上下微調用
		double yy = 21;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數

		double y = top + yy;

		this.setFontSize(standardFontSize + 4);
		this.print(1, 25, "新光人壽保險股份有限公司");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.setFontSize(standardFontSize);
		this.print(1, 36, "火險及地震險保費 繳款通知單");
		this.print(1, 1, "");
		this.print(1, 1, "");

		this.setFontSize(standardFontSize - 2);
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");

		this.print(1, 9, "親愛的房貸客戶您好：");
		this.print(1, 9, "承蒙您於本公司辦理房屋抵押貸款，謹致謝忱！");
		this.print(1, 9, "您的火險及地震險保單即將到期（內容請參照保費明細表），本公司將於保單到期日當月與房貸期款一併");
		this.print(1, 9, "扣繳，且先扣繳火險及地震險保費，再扣繳期款，並向新光產物保險（股）公司辦理續保手續。");
		this.print(1, 9, "一、採銀行（郵局）自動扣款者，敬請貴貸戶留意增加火險及地震險總保費金額。");
		this.print(1, 9, "二、自行轉匯款者，敬請貴貸戶務必與期款一併匯款。");
		this.print(1, 9, "謹先奉達，並頌安祺！");
		this.print(1, 9, "備註：");
		this.print(1, 9, "1.	依據財政部91.01.25台財保字第0910750050號函辦理，自91.04.01起推行「新住宅火險及地震險」方案");
		this.print(1, 9, " ，住宅火險自動涵蓋地震險，保險期間為一年期。");
		this.print(1, 9, "2.	依據台端所簽具之火險及地震保險續保切結書之內文，保險費與房貸期款一併繳交，如保險費有未全額");
		this.print(1, 9, " ，本公司得自所繳交期款中優先扣取。如欲自行投保者，請於原保單到期前15日且須於扣款繳交情形前將");
		this.print(1, 9, " 續期投保之保險單正本及收據副本交付公司收執。");
		this.print(1, 9, "                                                              新光人壽保險股份有險公司 敬啟");

		this.setFontSize(standardFontSize);
		setFont(1, 16);

		printCm(10, 22, "火險及地震險保費明細表", "C");

		setFont(1, 13);

		y = top + yy + (++l) * h;
		printCm(3, y+2, "貸款戶號：" + String.format("%07d", Integer.valueOf(custNo)) + "-"
				+ String.format("%03d", Integer.valueOf(facmNo)));
		String repayCode = r.get(c).get("RepayCode");
		printCm(18, y+2, repayCodeX(repayCode), "R");

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y+2, "客戶名稱：" + custMain.getCustName().substring(0, nameLength));

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

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
		printCm(3, y+2, "押品地址：" + r.get(c).get("BdLocation"));

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y+2,
				"續保期間：" + tempDate1.substring(0, 3) + "/" + tempDate1.substring(3, 5) + "/" + tempDate1.substring(5, 7)
						+ " - " + tempDate2.substring(0, 3) + "/" + tempDate2.substring(3, 5) + "/"
						+ tempDate2.substring(5, 7));
		printCm(11, y+2, "火險保費：" + String.format("%,d", iFireInsuPrem));

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y+2, "火險保額：" + String.format("%,d", iFireInsuCovrg));
		printCm(11, y+2, "地震險保費：" + String.format("%,d", iEthqInsuPrem));

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		y = top + yy + (++l) * h;
		printCm(3, y+2, "地震險保額：" + String.format("%,d", iEthqInsuCovrg));
		printCm(11, y+2, "總保費：" + String.format("%,d", iTotInsuPrem) + " (應繳日：" + tempDate3.substring(0, 3) + "/"
				+ tempDate3.substring(3, 5) + "/" + tempDate3.substring(5, 7) + ")");

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		y = top + yy + (++l) * h;
		String payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(custNo));
		this.printCm(3, y+2, "匯款帳號：" + payIntAcct);

		y = top + yy + (++l) * h;
		printCm(2, y+2, " ");

		int iStatusCode = parse.stringToInteger(r.get(c).get("StatusCode"));

//		if (iStatusCode==1) {//借支時才印此行
		y = top + yy + (++l) * h;
		this.printCm(3, y+2, "台端有一火險保費，至今仍未入金，此費用請與房貸期款一併繳納，謝謝您的");
		y = top + yy + (++l) * h;
		this.printCm(3, y+2, "配合。");
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
