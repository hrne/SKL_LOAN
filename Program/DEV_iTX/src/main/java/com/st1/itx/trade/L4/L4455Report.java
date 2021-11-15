package com.st1.itx.trade.L4;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4455Report")
@Scope("prototype")

public class L4455Report extends MakeReport {


	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	CustNoticeCom custNoticeCom;
	
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	String ENTDY = "";
	int reportCnt = 0;
	// 自訂表頭
	private int acdate = 0;
	private String year = "";
	private String month = "";
	private String date = "";
    // 每頁筆數
	private int pageIndex = 38;
	
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		printHeaderP();
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void printHeaderP() {
		this.setFont(1,9);
		this.print(-1, 185, "機密等級：密");
		this.print(-2, 1, "程式ID：" + "L4455Report");
		this.print(-2, 95, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 203, "日　期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 1, "報　表：" + "L4455Report");
		this.print(-3, 95, "ACH 扣款總傳票明細表", "C");
		this.print(-3, 203, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 185, "頁　數：");
		this.print(-4, 200, ""+this.getNowPage(),"R");
		this.print(-5, 3, "批次號碼：");
		this.print(-5, 95, " 年    月    日", "C");
		
		if(String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}
		
		this.print(-5, 84, year);
		this.print(-5, 91, month);
		this.print(-5, 98, date);
		
		this.print(-5, 185, "單　位：元");
		this.print(-6, 3, "扣款日期：");
		this.print(-6, 35, "扣款銀行：");
		this.print(-8, 1, "戶號              戶名         扣款金額         作帳金額             計息起迄日         本金         利息         暫付款         違約金         暫收借         暫收貸         短繳");
		this.print(-9, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4455Report exec");
		acdate = parse.stringToInteger(titaVo.getParam("AcDate"));
		
		List<Map<String, String>> L4455List = new ArrayList<Map<String, String>>();
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4455", "銀行扣款總傳票明細表", "", "A4", "L");


		if (L4455List.size() > 0) {
			int i = 0, pageCnt = 0;
			for (int j = 1; j <= L4455List.size(); j++) {
				i = j - 1;
				
//				每頁筆數相加
				pageCnt++;

				DecimalFormat df1 = new DecimalFormat("#,##0");

//				1.每筆先印出明細
				this.print(1, 1,"                                                                                                                                                                               ");
				this.print(0, 1, "1234567");// 戶號
				this.print(0, 9, "-");
				this.print(0, 10, "001");// 額度
				this.print(0, 13, "-");
				this.print(0, 14, "001");// 撥款
				
				this.print(0, 19, "測試者");// 戶名
				this.print(0, 45, "17,890","R");// 扣款金額
				this.print(0, 64, "17,890","R");// 作帳金額
				this.print(0, 67, "105/11/10- 105/12/10");// 計息起訖日
				this.print(0, 105, "17,890","R");// 本金
				this.print(0, 119, "17,890","R");// 利息
				this.print(0, 136, "17,890","R");// 暫付款
				this.print(0, 153, "17,890","R");// 違約金
				this.print(0, 170, "17,890","R");// 暫收借
				this.print(0, 186, "17,890","R");// 暫收貸				
				this.print(0, 201, "17,890","R");// 短繳		
				
				
				
				if (j != L4455List.size()) {
////					年月不同則跳頁，並且累計歸零
//					repayBank = occursList.get(j).get("OORepayBank");
//					if (!occursList.get(i).get("OORepayBank").equals(repayBank)) {
//						this.info("RepayBank Not Match...");
//						pageCnt = 0;
//						this.newPage();
//						continue;
//					}
//					每頁第42筆 跳頁 
					if (pageCnt == 42) {
						this.print(1, 70, "=====續下頁=====", "C");
//
						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
//					扣除總計合計的行數 +1 
					this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 1,"                                                                                                                                                                               ");
					this.print(0, 1, "擔保放款      小計");
					
					
					
					this.print(2, 1,"                                                                                                                                                                               ");
					this.print(0, 1, "合計");
					this.print(pageIndex - pageCnt - 2, 95, "=====報表結束=====", "C");
				}
				
			
			} // for
			
		} else {
			this.print(1, 20, "*******    查無資料   ******");
		}

		long sno = this.close();

		this.toPdf(sno);
	}
	
	
}