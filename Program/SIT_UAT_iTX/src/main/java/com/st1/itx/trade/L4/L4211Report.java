package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4211AServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
//import com.st1.itx.util.parse.Parse;

@Component("L4211Report")
@Scope("prototype")
public class L4211Report extends MakeReport {
//	@Autowired
//	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public L4211AServiceImpl l4211ARServiceImpl;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	// 每頁筆數
	private int pageIndex = 38;
	// 每頁筆數
	private int reportkind = 0;
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");
		
		if(reportkind == 1) {
		  printHeaderP();
		} else if(reportkind == 2){
		  printHeaderP1();
		} else {
		  printHeaderP2();
		}
		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	int allsum = 0;
	int allsum2 = 0;
	int allsum3 = 0;
	int allsum4 = 0;
	int allsum5 = 0;
	int allsum6 = 0;
	int allsum7 = 0;
	int allsum8 = 0;
	int allsum9 = 0;
	int allsum10 = 0;
	
	int totalsum = 0;
	int totalsum2 = 0;
	int totalsum3 = 0;
	int totalsum4 = 0;
	int totalsum5 = 0;
	int totalsum6 = 0;
	int totalsum7 = 0;
	int totalsum8 = 0;
	int totalsum9 = 0;
	int totalsum10 = 0;
	
	int transferamt = 0;
	int makeferamt = 0;
	int principal = 0;
	int interest = 0;
	int payment = 0;
	int damages = 0;
	int temporaryloan = 0;
	int collection = 0;
	int shortpayment = 0;
	int others = 0;

	String acdate = "";
	String year = "";
	String month = "";
	String date = "";
	public void printHeaderP() {
		this.setFont(1, 8);
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + "L4211A");
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//			月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + "L4211A");
		this.print(-3, 82, "匯款總傳票明細表 ----(									)", "C");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");
		
		if(String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}
		
		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);
		
		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		this.print(-7, 1,
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	  戶名    	     計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 帳管費及其他");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void printHeaderP1() {
		this.setFont(1, 8);
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + "L4211A");
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//			月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + "L4211A");
		this.print(-3, 77, "匯款總傳票明細表－以金額排序 ----(									)", "C");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");
		
		if(String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}
		
		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);
		
		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		this.print(-7, 1,
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	  戶名    	     計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 帳管費及其他");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public void printHeaderP2() {
		this.setFont(1, 8);
		this.print(-1, 150, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + "L4211A");
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//			月/日/年(西元後兩碼)
		this.print(-2, 167, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + "L4211A");
		this.print(-3, 82, "匯款明細表－依戶號 ----(									)", "C");
		this.print(-3, 167, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 150, "頁    數：" + this.getNowPage());
		this.print(-5, 2, "批次號碼 ....");
		this.print(-5, 81, "年    月   日", "C");
		
		if(String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}
		
		this.print(-5, 71, year);
		this.print(-5, 78, month);
		this.print(-5, 83, date);
		
		this.print(-5, 161, "單    位：元", "R");
		this.print(-6, 0, "");
		this.print(-7, 1,
				" 匯款日    匯款序號    匯款金額   作帳金額 戶號           	  戶名    	     計息起迄日     	    本金       利息     暫付款     違約金   	 暫收借    暫收貸    短繳  	 帳管費及其他");
		this.print(-8, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public void exec(TitaVo titaVo) throws LogicException {

		long sno = 0;

		acdate = titaVo.get("AcDate");
		List<Map<String, String>> fnAllList = new ArrayList<Map<String, String>>();

		try {
			fnAllList = l4211ARServiceImpl.findAll(titaVo, 1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}

		if(fnAllList == null) {	
				throw new LogicException("E2003", "查無資料"); // 查無資料
		}
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4211", "匯款總傳票明細表", "", "A4", "L");

		reportkind = 1;
		report1(fnAllList);
		
		
		try {
			fnAllList = l4211ARServiceImpl.findAll(titaVo, 2);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}
		
		reportkind = 2;
		newPage();
		report2(fnAllList);
		
		
		try {
			fnAllList = l4211ARServiceImpl.findAll(titaVo, 3);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}
		
		reportkind = 3;
		newPage();
		report3(fnAllList);
		

		sno = this.close();
		this.toPdf(sno);
	}

	
	private void report1(List<Map<String, String>> fnAllList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;
		
		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String df2 = formatAmt(tfnAllList.get("F4"), 0);
			String df3 = formatAmt(tfnAllList.get("F5"), 0);
			String df4 = formatAmt(tfnAllList.get("F10"), 0);
			String df5 = formatAmt(tfnAllList.get("F11"), 0);
			String df6 = formatAmt(tfnAllList.get("F12"), 0);
			String df7 = formatAmt(tfnAllList.get("F13"), 0);
			String df8 = formatAmt(tfnAllList.get("F14"), 0);
			String df9 = formatAmt(tfnAllList.get("F15"), 0);
			String df10 = formatAmt(tfnAllList.get("F16"), 0);
			String df11 = formatAmt(tfnAllList.get("F17"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("F4"));
			makeferamt = Integer.valueOf(tfnAllList.get("F5"));
			principal = Integer.valueOf(tfnAllList.get("F10"));
			interest = Integer.valueOf(tfnAllList.get("F11"));
			payment = Integer.valueOf(tfnAllList.get("F12"));
			damages = Integer.valueOf(tfnAllList.get("F13"));
			temporaryloan = Integer.valueOf(tfnAllList.get("F14"));
			collection = Integer.valueOf(tfnAllList.get("F15"));
			shortpayment = Integer.valueOf(tfnAllList.get("F16"));
			others = Integer.valueOf(tfnAllList.get("F17"));
			count++;
			
			
			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("F0")) || !msNum.equals(tfnAllList.get("F1"))) {
				
				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("F22");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					
					totalsum += allsum;
					totalsum2 += allsum2;
					totalsum3 += allsum3;
					totalsum4 += allsum4;
					totalsum5 += allsum5;
					totalsum6 += allsum6;
					totalsum7 += allsum7;
					totalsum8 += allsum8;
					totalsum9 += allsum9;
					totalsum10 += allsum10;
					
					allsum = 0;
					allsum2 = 0;
					allsum3 = 0;
					allsum4 = 0;
					allsum5 = 0;
					allsum6 = 0;
					allsum7 = 0;
					allsum8 = 0;
					allsum9 = 0;
					allsum10 = 0;

					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0 ;
					tround = 0;
				} // if
				
				// 頁面設置配置
				
				this.setFont(1, 8);
				String A17 = tfnAllList.get("F0");
				if (A17.equals("P03")) {
					this.print(-3, 90, "A7");
				} else {
					this.print(-3, 90, A17);// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("F0");
				msNum = tfnAllList.get("F1");
			} else { 
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("F22").toString())
							|| !txCode.equals(tfnAllList.get("F23").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("F22").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();
						
						this.print(1, 0, "");
						
						pageCnt = pageCnt + 2;
						
						totalsum += allsum;
						totalsum2 += allsum2;
						totalsum3 += allsum3;
						totalsum4 += allsum4;
						totalsum5 += allsum5;
						totalsum6 += allsum6;
						totalsum7 += allsum7;
						totalsum8 += allsum8;
						totalsum9 += allsum9;
						totalsum10 += allsum10;
						
						allsum = 0;
						allsum2 = 0;
						allsum3 = 0;
						allsum4 = 0;
						allsum5 = 0;
						allsum6 = 0;
						allsum7 = 0;
						allsum8 = 0;
						allsum9 = 0;
						allsum10 = 0;
					}

				}

				if (pageCnt >= 30) { // 超過30筆自動換頁 並印出當前的代碼
					
					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("F0").equals("P03")) {
						this.print(-3, 90, "A7");
					} else {
						this.print(-3, 90, tfnAllList.get("F0"));// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
					this.print(-8, 0, "");
				
				}
			} // else 

			npcount++;
			tround++;
			
//			每頁筆數相加
			pageCnt++;
			
			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("F22").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("F23").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("F2")), 1));
			
			if(!scode.equals(tfnAllList.get("F3"))) {  // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("F3"), "C");// 匯款序號		
				this.print(0, 29, df2, "R");// 匯款金額
				
				allsum += transferamt;
				
				scode = tfnAllList.get("F3");
				
			}
			
			this.print(0, 40, df3, "R");// 作帳金額
			this.print(0, 41, tfnAllList.get("F6"));// 戶號
			String name = tfnAllList.get("F7");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 57, name);
			this.print(0, 68, showRocDate(tfnAllList.get("F8"), 1));// 起日
			this.print(0, 77, "-");
			this.print(0, 78, showRocDate(tfnAllList.get("F9"), 1));// 迄日

			if (df5.equals("0")) {
				this.print(0, 106, "", "R"); // 利息
			} else {
				this.print(0, 106, df5, "R"); // 利息
			}
			if (df6.equals("0")) {
				this.print(0, 115, "", "R"); // 暫付款
			} else {
				this.print(0, 115, df6, "R"); // 暫付款
			}
			if (df7.equals("0")) {
				this.print(0, 125, "", "R"); // 違約金
			} else {
				this.print(0, 125, df7, "R"); // 違約金
			}
			if (df9.equals("0")) {
				this.print(0, 145, "", "R"); // 暫收貸
			} else {
				this.print(0, 145, df9, "R"); // 暫收貸
			}
			if (df10.equals("0")) {
				this.print(0, 155, "", "R"); // 短繳
			} else {
				this.print(0, 155, df10, "R"); // 短繳
			}
			if (df11.equals("0")) {
				this.print(0, 165, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 165, df11, "R"); // 帳管費及其他
			}
			if (df4.equals("0")) {
				this.print(0, 97, "", "R"); // 本金
			} else {
				this.print(0, 97, df4, "R"); // 本金
			}
			if (df8.equals("0")) {
				this.print(0, 137, "", "R"); // 暫收借
			} else {
				this.print(0, 137, df8, "R"); // 暫收借
			}

			allsum2 += makeferamt;
			allsum3 += principal;
			allsum4 += interest;
			allsum5 += payment;
			allsum6 += damages;
			allsum7 += temporaryloan;
			allsum8 += collection;
			allsum9 += shortpayment;
			allsum10 += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if("".equals(tfnAllList.get("F23"))) {
				  if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
					this.print(1, 2, "暫收款");
				  } else {
					this.print(1, 2, msCode);
				  }
				} else {
				  this.print(1, 2, tfnAllList.get("F23"));
				}
				this.print(0, 14, " 小計 ");
				
				atAll();

				
				totalsum += allsum;
				totalsum2 += allsum2;
				totalsum3 += allsum3;
				totalsum4 += allsum4;
				totalsum5 += allsum5;
				totalsum6 += allsum6;
				totalsum7 += allsum7;
				totalsum8 += allsum8;
				totalsum9 += allsum9;
				totalsum10 += allsum10;
				
				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");
				
				totalAll();
				pageCnt = pageCnt + 4;
				allsum = 0;
				allsum2 = 0;
				allsum3 = 0;
				allsum4 = 0;
				allsum5 = 0;
				allsum6 = 0;
				allsum7 = 0;
				allsum8 = 0;
				allsum9 = 0;
				allsum10 = 0;
				
				totalsum = 0;
				totalsum2 = 0;
				totalsum3 = 0;
				totalsum4 = 0;
				totalsum5 = 0;
				totalsum6 = 0;
				totalsum7 = 0;
				totalsum8 = 0;
				totalsum9 = 0;
				totalsum10 = 0;
				
				
				this.print(pageIndex - pageCnt - 2, 80, "=====報表結束=====", "C");
				this.print(2, 80, "　　　　　　　　　　　　　　　　　　　　課長：　　　　　　　　　　製表人：", "C");

				pageCnt = 0;
				npcount = 0 ;
				tround = 0;
			}

		} // for
	}
	
	private void report2(List<Map<String, String>> fnAllList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;
		
		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String df2 = formatAmt(tfnAllList.get("F4"), 0);
			String df3 = formatAmt(tfnAllList.get("F5"), 0);
			String df4 = formatAmt(tfnAllList.get("F10"), 0);
			String df5 = formatAmt(tfnAllList.get("F11"), 0);
			String df6 = formatAmt(tfnAllList.get("F12"), 0);
			String df7 = formatAmt(tfnAllList.get("F13"), 0);
			String df8 = formatAmt(tfnAllList.get("F14"), 0);
			String df9 = formatAmt(tfnAllList.get("F15"), 0);
			String df10 = formatAmt(tfnAllList.get("F16"), 0);
			String df11 = formatAmt(tfnAllList.get("F17"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("F4"));
			makeferamt = Integer.valueOf(tfnAllList.get("F5"));
			principal = Integer.valueOf(tfnAllList.get("F10"));
			interest = Integer.valueOf(tfnAllList.get("F11"));
			payment = Integer.valueOf(tfnAllList.get("F12"));
			damages = Integer.valueOf(tfnAllList.get("F13"));
			temporaryloan = Integer.valueOf(tfnAllList.get("F14"));
			collection = Integer.valueOf(tfnAllList.get("F15"));
			shortpayment = Integer.valueOf(tfnAllList.get("F16"));
			others = Integer.valueOf(tfnAllList.get("F17"));
			count++;
			
			
			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("F0")) || !msNum.equals(tfnAllList.get("F1"))) {
				
				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("F22");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					
					totalsum += allsum;
					totalsum2 += allsum2;
					totalsum3 += allsum3;
					totalsum4 += allsum4;
					totalsum5 += allsum5;
					totalsum6 += allsum6;
					totalsum7 += allsum7;
					totalsum8 += allsum8;
					totalsum9 += allsum9;
					totalsum10 += allsum10;
					
					allsum = 0;
					allsum2 = 0;
					allsum3 = 0;
					allsum4 = 0;
					allsum5 = 0;
					allsum6 = 0;
					allsum7 = 0;
					allsum8 = 0;
					allsum9 = 0;
					allsum10 = 0;

					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0 ;
					tround = 0;
				} // if
				
				// 頁面設置配置
				
				this.setFont(1, 8);
				String A17 = tfnAllList.get("F0");
				if (A17.equals("P03")) {
					this.print(-3, 90, "A7");
				} else {
					this.print(-3, 90, A17);// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("F0");
				msNum = tfnAllList.get("F1");
			} else { 
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("F22").toString())
							|| !txCode.equals(tfnAllList.get("F23").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("F22").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();
						
						this.print(1, 0, "");
						
						pageCnt = pageCnt + 2;
						
						totalsum += allsum;
						totalsum2 += allsum2;
						totalsum3 += allsum3;
						totalsum4 += allsum4;
						totalsum5 += allsum5;
						totalsum6 += allsum6;
						totalsum7 += allsum7;
						totalsum8 += allsum8;
						totalsum9 += allsum9;
						totalsum10 += allsum10;
						
						allsum = 0;
						allsum2 = 0;
						allsum3 = 0;
						allsum4 = 0;
						allsum5 = 0;
						allsum6 = 0;
						allsum7 = 0;
						allsum8 = 0;
						allsum9 = 0;
						allsum10 = 0;
					}

				}

				if (pageCnt >= 30) { // 超過40筆自動換頁 並印出當前的代碼
					
					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("F0").equals("P03")) {
						this.print(-3, 90, "A7");
					} else {
						this.print(-3, 90, tfnAllList.get("F0"));// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
					this.print(-8, 0, "");
				}
			} // else 

			npcount++;
			tround++;
			
//			每頁筆數相加
			pageCnt++;
			
			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("F22").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("F23").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("F2")), 1));
			
			if(!scode.equals(tfnAllList.get("F3"))) {  // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("F3"), "C");// 匯款序號		
				this.print(0, 29, df2, "R");// 匯款金額
				
				allsum += transferamt;
				
				scode = tfnAllList.get("F3");
				
			}
			
			this.print(0, 40, df3, "R");// 作帳金額
			this.print(0, 41, tfnAllList.get("F6"));// 戶號
			String name = tfnAllList.get("F7");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 57, name);
			this.print(0, 68, showRocDate(tfnAllList.get("F8"), 1));// 起日
			this.print(0, 77, "-");
			this.print(0, 78, showRocDate(tfnAllList.get("F9"), 1));// 迄日

			if (df5.equals("0")) {
				this.print(0, 106, "", "R"); // 利息
			} else {
				this.print(0, 106, df5, "R"); // 利息
			}
			if (df6.equals("0")) {
				this.print(0, 115, "", "R"); // 暫付款
			} else {
				this.print(0, 115, df6, "R"); // 暫付款
			}
			if (df7.equals("0")) {
				this.print(0, 125, "", "R"); // 違約金
			} else {
				this.print(0, 125, df7, "R"); // 違約金
			}
			if (df9.equals("0")) {
				this.print(0, 145, "", "R"); // 暫收貸
			} else {
				this.print(0, 145, df9, "R"); // 暫收貸
			}
			if (df10.equals("0")) {
				this.print(0, 155, "", "R"); // 短繳
			} else {
				this.print(0, 155, df10, "R"); // 短繳
			}
			if (df11.equals("0")) {
				this.print(0, 165, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 165, df11, "R"); // 帳管費及其他
			}
			if (df4.equals("0")) {
				this.print(0, 97, "", "R"); // 本金
			} else {
				this.print(0, 97, df4, "R"); // 本金
			}
			if (df8.equals("0")) {
				this.print(0, 137, "", "R"); // 暫收借
			} else {
				this.print(0, 137, df8, "R"); // 暫收借
			}

			allsum2 += makeferamt;
			allsum3 += principal;
			allsum4 += interest;
			allsum5 += payment;
			allsum6 += damages;
			allsum7 += temporaryloan;
			allsum8 += collection;
			allsum9 += shortpayment;
			allsum10 += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if("".equals(tfnAllList.get("F23"))) {
				  if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
					this.print(1, 2, "暫收款");
				  } else {
					this.print(1, 2, msCode);
				  }
				} else {
				  this.print(1, 2, tfnAllList.get("F23"));
				}
				this.print(0, 14, " 小計 ");
				
				atAll();

				
				totalsum += allsum;
				totalsum2 += allsum2;
				totalsum3 += allsum3;
				totalsum4 += allsum4;
				totalsum5 += allsum5;
				totalsum6 += allsum6;
				totalsum7 += allsum7;
				totalsum8 += allsum8;
				totalsum9 += allsum9;
				totalsum10 += allsum10;
				
				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");
				
				totalAll();
				pageCnt = pageCnt + 4;
				allsum = 0;
				allsum2 = 0;
				allsum3 = 0;
				allsum4 = 0;
				allsum5 = 0;
				allsum6 = 0;
				allsum7 = 0;
				allsum8 = 0;
				allsum9 = 0;
				allsum10 = 0;
				
				totalsum = 0;
				totalsum2 = 0;
				totalsum3 = 0;
				totalsum4 = 0;
				totalsum5 = 0;
				totalsum6 = 0;
				totalsum7 = 0;
				totalsum8 = 0;
				totalsum9 = 0;
				totalsum10 = 0;
				
				
				this.print(pageIndex - pageCnt - 2, 80, "=====報表結束=====", "C");
				this.print(2, 80, "　　　　　　　　　　　　　　　　　　　　課長：　　　　　　　　　　製表人：", "C");
			}

		} // for
	}
	
	private void report3(List<Map<String, String>> fnAllList) {
		String msCode = ""; // 代號
		String txCode = ""; // 代號名稱
		String msName = ""; // 表頭P號
		String msNum = ""; // 批次號碼
		int count = 0;
		int npcount = 0;
		int tround = 0;
		int pageCnt = 0;
		
		String scode = ""; // 暫存流水序號(相同的時候匯款金額判斷不用出現)
		for (Map<String, String> tfnAllList : fnAllList) {

			String df2 = formatAmt(tfnAllList.get("F4"), 0);
			String df3 = formatAmt(tfnAllList.get("F5"), 0);
			String df4 = formatAmt(tfnAllList.get("F10"), 0);
			String df5 = formatAmt(tfnAllList.get("F11"), 0);
			String df6 = formatAmt(tfnAllList.get("F12"), 0);
			String df7 = formatAmt(tfnAllList.get("F13"), 0);
			String df8 = formatAmt(tfnAllList.get("F14"), 0);
			String df9 = formatAmt(tfnAllList.get("F15"), 0);
			String df10 = formatAmt(tfnAllList.get("F16"), 0);
			String df11 = formatAmt(tfnAllList.get("F17"), 0);

			transferamt = Integer.valueOf(tfnAllList.get("F4"));
			makeferamt = Integer.valueOf(tfnAllList.get("F5"));
			principal = Integer.valueOf(tfnAllList.get("F10"));
			interest = Integer.valueOf(tfnAllList.get("F11"));
			payment = Integer.valueOf(tfnAllList.get("F12"));
			damages = Integer.valueOf(tfnAllList.get("F13"));
			temporaryloan = Integer.valueOf(tfnAllList.get("F14"));
			collection = Integer.valueOf(tfnAllList.get("F15"));
			shortpayment = Integer.valueOf(tfnAllList.get("F16"));
			others = Integer.valueOf(tfnAllList.get("F17"));
			count++;
			
			
			// 判斷當前的批號與批次號碼不同
			if (!msName.equals(tfnAllList.get("F0")) || !msNum.equals(tfnAllList.get("F1"))) {
				
				if (npcount > 0) { // 除當頁第一筆
					this.print(1, 0,
							"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					String aName = tfnAllList.get("F22");
					if (aName.equals("999") || msCode.equals("") || msCode.equals(" ")) {
						this.print(1, 2, "暫收款");
					} else {
						this.print(1, 2, msCode);
					}
					this.print(0, 14, " 小計 ");

					atAll();

					
					totalsum += allsum;
					totalsum2 += allsum2;
					totalsum3 += allsum3;
					totalsum4 += allsum4;
					totalsum5 += allsum5;
					totalsum6 += allsum6;
					totalsum7 += allsum7;
					totalsum8 += allsum8;
					totalsum9 += allsum9;
					totalsum10 += allsum10;
					
					allsum = 0;
					allsum2 = 0;
					allsum3 = 0;
					allsum4 = 0;
					allsum5 = 0;
					allsum6 = 0;
					allsum7 = 0;
					allsum8 = 0;
					allsum9 = 0;
					allsum10 = 0;

					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					npcount = 0 ;
					tround = 0;
				} // if
				
				// 頁面設置配置
				
				this.setFont(1, 8);
				String A17 = tfnAllList.get("F0");
				if (A17.equals("P03")) {
					this.print(-3, 90, "A7");
				} else {
					this.print(-3, 90, A17);// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
				}
				this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
				this.print(-8, 0, "");

				msName = tfnAllList.get("F0");
				msNum = tfnAllList.get("F1");
			} else { 
				// 當前的批號與批次號碼相同
				if (tround > 0) {
					// 判斷前一筆與當筆是否相同科目
					if (!msCode.equals(tfnAllList.get("F22").toString())
							|| !txCode.equals(tfnAllList.get("F23").toString())) {
						this.info("msCode       = " + msCode);
						this.info("22       = " + tfnAllList.get("F22").toString());

						this.print(1, 0,
								"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
							this.print(1, 2, "暫收款");
						} else {
							this.print(1, 2, msCode);
						}
						this.print(0, 14, " 小計 ");

						atAll();
						
						this.print(1, 0, "");
						
						pageCnt = pageCnt + 2;
						
						totalsum += allsum;
						totalsum2 += allsum2;
						totalsum3 += allsum3;
						totalsum4 += allsum4;
						totalsum5 += allsum5;
						totalsum6 += allsum6;
						totalsum7 += allsum7;
						totalsum8 += allsum8;
						totalsum9 += allsum9;
						totalsum10 += allsum10;
						
						allsum = 0;
						allsum2 = 0;
						allsum3 = 0;
						allsum4 = 0;
						allsum5 = 0;
						allsum6 = 0;
						allsum7 = 0;
						allsum8 = 0;
						allsum9 = 0;
						allsum10 = 0;
					}

				}

				if (pageCnt >= 30) { // 超過40筆自動換頁 並印出當前的代碼
					
					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
					pageCnt = 0;
					newPage();
					if (tfnAllList.get("F0").equals("P03")) {
						this.print(-3, 90, "A7");
					} else {
						this.print(-3, 90, tfnAllList.get("F0"));// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
					}
					this.print(-5, 15, tfnAllList.get("F1"));// 批次號碼(表頭)
					this.print(-8, 0, "");
				}
			} // else 

			npcount++;
			tround++;
			
//			每頁筆數相加
			pageCnt++;
			
			// 第一筆或相同的時候放入暫存 給下次一筆 比對使用
			msCode = tfnAllList.get("F22").toString();
			// 當前代碼對應中文 當下一筆不同時取用
			txCode = tfnAllList.get("F23").toString();

			// 報表邏輯及排序

			// 匯款日 * type = 1: yyy/mm/dd<BR>
			this.print(1, 2, showRocDate((tfnAllList.get("F2")), 1));
			
			if(!scode.equals(tfnAllList.get("F3"))) {  // 匯款序號不同 印匯款金額
				this.print(0, 16, tfnAllList.get("F3"), "C");// 匯款序號		
				this.print(0, 29, df2, "R");// 匯款金額
				
				allsum += transferamt;
				
				scode = tfnAllList.get("F3");
				
			}
			
			this.print(0, 40, df3, "R");// 作帳金額
			this.print(0, 41, tfnAllList.get("F6"));// 戶號
			String name = tfnAllList.get("F7");
			if (name.length() > 5) {// 戶名
				name = name.substring(0, 5);
			}
			this.print(0, 57, name);
			this.print(0, 68, showRocDate(tfnAllList.get("F8"), 1));// 起日
			this.print(0, 77, "-");
			this.print(0, 78, showRocDate(tfnAllList.get("F9"), 1));// 迄日

			if (df5.equals("0")) {
				this.print(0, 106, "", "R"); // 利息
			} else {
				this.print(0, 106, df5, "R"); // 利息
			}
			if (df6.equals("0")) {
				this.print(0, 115, "", "R"); // 暫付款
			} else {
				this.print(0, 115, df6, "R"); // 暫付款
			}
			if (df7.equals("0")) {
				this.print(0, 125, "", "R"); // 違約金
			} else {
				this.print(0, 125, df7, "R"); // 違約金
			}
			if (df9.equals("0")) {
				this.print(0, 145, "", "R"); // 暫收貸
			} else {
				this.print(0, 145, df9, "R"); // 暫收貸
			}
			if (df10.equals("0")) {
				this.print(0, 155, "", "R"); // 短繳
			} else {
				this.print(0, 155, df10, "R"); // 短繳
			}
			if (df11.equals("0")) {
				this.print(0, 165, "", "R"); // 帳管費及其他
			} else {
				this.print(0, 165, df11, "R"); // 帳管費及其他
			}
			if (df4.equals("0")) {
				this.print(0, 97, "", "R"); // 本金
			} else {
				this.print(0, 97, df4, "R"); // 本金
			}
			if (df8.equals("0")) {
				this.print(0, 137, "", "R"); // 暫收借
			} else {
				this.print(0, 137, df8, "R"); // 暫收借
			}

			allsum2 += makeferamt;
			allsum3 += principal;
			allsum4 += interest;
			allsum5 += payment;
			allsum6 += damages;
			allsum7 += temporaryloan;
			allsum8 += collection;
			allsum9 += shortpayment;
			allsum10 += others;

			// 最後一筆產出
			if (count == fnAllList.size()) {
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				if("".equals(tfnAllList.get("F23"))) {
				  if (msCode.equals("999") || msCode.equals("") || msCode.equals(" ")) {
					this.print(1, 2, "暫收款");
				  } else {
					this.print(1, 2, msCode);
				  }
				} else {
				  this.print(1, 2, tfnAllList.get("F23"));
				}
				this.print(0, 14, " 小計 ");
				
				atAll();

				
				totalsum += allsum;
				totalsum2 += allsum2;
				totalsum3 += allsum3;
				totalsum4 += allsum4;
				totalsum5 += allsum5;
				totalsum6 += allsum6;
				totalsum7 += allsum7;
				totalsum8 += allsum8;
				totalsum9 += allsum9;
				totalsum10 += allsum10;
				
				this.print(1, 0, "");
				this.print(1, 0,
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				this.print(1, 14, " 合計 ");
				
				totalAll();
				pageCnt = pageCnt + 4;
				allsum = 0;
				allsum2 = 0;
				allsum3 = 0;
				allsum4 = 0;
				allsum5 = 0;
				allsum6 = 0;
				allsum7 = 0;
				allsum8 = 0;
				allsum9 = 0;
				allsum10 = 0;
				
				totalsum = 0;
				totalsum2 = 0;
				totalsum3 = 0;
				totalsum4 = 0;
				totalsum5 = 0;
				totalsum6 = 0;
				totalsum7 = 0;
				totalsum8 = 0;
				totalsum9 = 0;
				totalsum10 = 0;
				
				
				this.print(pageIndex - pageCnt - 2, 80, "=====報表結束=====", "C");
				this.print(2, 80, "　　　　　　　　　　　　　　　　　　　　課長：　　　　　　　　　　製表人：", "C");
			}

		} // for
	}
	
	private void atAll() {
		
		if (allsum != 0) {
			this.print(0, 29, String.format("%,d", allsum), "R");
		} else {
			this.print(0, 29, "");
		}
		if (allsum2 != 0) {
			this.print(0, 40, String.format("%,d", allsum2), "R");
		} else {
			this.print(0, 40, "");
		}

		if (allsum3 != 0) {
			this.print(0, 97, String.format("%,d", allsum3), "R");
		} else {
			this.print(0, 97, "");
		}
		if (allsum5 != 0) {
			this.print(0, 117, String.format("%,d", allsum5), "R");
		} else {
			this.print(0, 117, "");
		}
		if (allsum6 != 0) {
			this.print(0, 125, String.format("%,d", allsum6), "R");
		} else {
			this.print(0, 125, "");
		}
		if (allsum7 != 0) {
			this.print(0, 137, String.format("%,d", allsum7), "R");
		} else {
			this.print(0, 137, "");
		}
		if (allsum9 != 0) {
			this.print(0, 154, String.format("%,d", allsum9), "R");
		} else {
			this.print(0, 154, "");
		}
		if (allsum10 != 0) {
			this.print(0, 165, String.format("%,d", allsum10), "R");
		} else {
			this.print(0, 165, "");
		}
		if (allsum4 != 0) {
			this.print(1, 106, String.format("%,d", allsum4), "R");
		} else {
			this.print(1, 106, "");
		}
		if (allsum8 != 0) {
			this.print(0, 145, String.format("%,d", allsum8), "R");
		} else {
			this.print(0, 145, "");
		}
		
	}
	
	private void totalAll() {
		
		if (totalsum != 0) {
			this.print(0, 29, String.format("%,d", totalsum), "R");
		} else {
			this.print(0, 29, "");
		}
		if (totalsum2 != 0) {
			this.print(0, 40, String.format("%,d", totalsum2), "R");
		} else {
			this.print(0, 40, "");
		}

		if (totalsum3 != 0) {
			this.print(0, 97, String.format("%,d", totalsum3), "R");
		} else {
			this.print(0, 97, "");
		}
		if (totalsum5 != 0) {
			this.print(0, 117, String.format("%,d", totalsum5), "R");
		} else {
			this.print(0, 117, "");
		}
		if (totalsum6 != 0) {
			this.print(0, 125, String.format("%,d", totalsum6), "R");
		} else {
			this.print(0, 125, "");
		}
		if (totalsum7 != 0) {
			this.print(0, 137, String.format("%,d", totalsum7), "R");
		} else {
			this.print(0, 137, "");
		}
		if (totalsum9 != 0) {
			this.print(0, 154, String.format("%,d", totalsum9), "R");
		} else {
			this.print(0, 154, "");
		}
		if (totalsum10 != 0) {
			this.print(0, 165, String.format("%,d", totalsum10), "R");
		} else {
			this.print(0, 165, "");
		}
		if (totalsum4 != 0) {
			this.print(1, 106, String.format("%,d", totalsum4), "R");
		} else {
			this.print(1, 106, "");
		}
		if (totalsum8 != 0) {
			this.print(0, 145, String.format("%,d", totalsum8), "R");
		} else {
			this.print(0, 145, "");
		}
		
	}

}
