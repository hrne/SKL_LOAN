package com.st1.itx.trade.L4;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L4454ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4454Report")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4454Report extends MakeReport {

	@Autowired
	public L4454ServiceImpl L4454ServiceImpl;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	String ENTDY = "";
	int reportCnt = 0;
	// 自訂表頭
//	@Override
//	public void printHeader() {
//		
//		
//		//明細起始列(自訂亦必須)
//		this.setBeginRow(10);
//		
//		//設定明細列數(自訂亦必須)
//		this.setMaxRows(35);
//	}

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> L4454List) throws LogicException {

		this.info("L4454Report exec");
		ENTDY = String.valueOf(Integer.parseInt(titaVo.getParam("ENTDY").toString()));
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4454", "存款不足明信片", "密", "A4", "L");

		this.info("L4454List-------->" + L4454List.size());
		int execCnt = 0;

		for (Map<String, String> tL4454Vo : L4454List) {
			execCnt++;
			if (execCnt >= 2 && execCnt != reportCnt) {
				this.newPage();
			}
			report(titaVo, tL4454Vo, txbuffer);
		}

		if (this.getNowPage() == 0) {
			this.print(1, 20, "*******    查無資料   ******");
		}

		long sno = this.close();

		this.toPdf(sno);
	}

	private void report(TitaVo titaVo, Map<String, String> tL4454Vo, TxBuffer txbuffer) throws LogicException {
		reportCnt++;

//		F1 戶號 F2 戶名
		String custNo = FormatUtil.pad9(tL4454Vo.get("CustNo"), 7);
		int intCustNo = parse.stringToInteger(tL4454Vo.get("CustNo"));
		CustMain tCustMain = custMainService.custNoFirst(intCustNo, intCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException("E0014", "CustMain"); // 檔案錯誤
		}
		String address = custNoticeCom.getCurrAddress(tCustMain, titaVo);
		String custName = tCustMain.getCustName();
		String zipCode1 = tCustMain.getCurrZip3();
		String zipCode2 = tCustMain.getCurrZip2();
		String virtAcct = "9510200" + custNo;

		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, tranNum(zipCode1) + tranNum(zipCode2));
		this.print(1, 1, "");
		this.print(1, 10, address);
		this.print(1, 10, custName);
		this.print(1, 1, "");
		this.print(1, 10, "戶號：" + custNo + "   " + custName);
		this.print(1, 10, "匯款帳號：" + tranNum(virtAcct));
		this.print(1, 10, "新光銀行城內分行代號：1030116");
		this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
	}

	private String tranNum(String data) {
		this.info("tranDate1 = " + data);
		if (data == null || data.equals("")) {
			return "";
		}
		String tranData = data;
		this.info("tranData = " + tranData);
		String result = "";

		int tranTemp = 0;
		int i = 0;
		char tmp;

		for (i = 0; i < tranData.length(); i++) {

			this.info("tranDate i = " + i);
			this.info("tranDate X = " + tranData.substring(i, i + 1));
			tmp = tranData.charAt(i);

			tranTemp = (int) tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

			this.info("tranDate XX= " + (char) tranTemp);
			result += (char) tranTemp;
		}

		return result;
	}
}