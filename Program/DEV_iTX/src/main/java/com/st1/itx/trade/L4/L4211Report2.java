package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4211BServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
//import com.st1.itx.util.parse.Parse;

@Component("L4211Report2")
@Scope("prototype")
public class L4211Report2 extends MakeReport {
	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public L4211BServiceImpl l4211BRServiceImpl;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

//		每頁筆數
	private int pageIndex = 45;

	private String ReconCode = "";
	private String entrydate = "";
	private String year = "";
	private String month = "";
	private String date = "";
	
	private BigDecimal falseamt = new BigDecimal("0");
	private BigDecimal falseamt234 = new BigDecimal("0");
	private BigDecimal amt5 = new BigDecimal("0");
	private BigDecimal amt6 = new BigDecimal("0");
	private BigDecimal amt7 = new BigDecimal("0");
	private BigDecimal amt = new BigDecimal("0");
	
	private int falsetimes = 0;
	private int falsetimes234 = 0;
	private int times5 = 0;
	private int times6 = 0;
	private int times7 = 0;
	private int times = 0;
	
	
	private BigDecimal totfalseamt = new BigDecimal("0");
	private BigDecimal totfalseamt234 = new BigDecimal("0");
	private BigDecimal totamt5 = new BigDecimal("0");
	private BigDecimal totamt6 = new BigDecimal("0");
	private BigDecimal totamt7 = new BigDecimal("0");
	private BigDecimal totamt = new BigDecimal("0");
	
	private int totfalsetimes = 0;
	private int totfalsetimes234 = 0;
	private int tottimes5 = 0;
	private int tottimes6 = 0;
	private int tottimes7 = 0;
	private int totaltimes = 0;
	
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(10);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.setFont(1, 8);
		this.print(-1, 148, "機密等級：密");
		this.print(-2, 3, "程式 ID：" + "L4211B");
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//			月/日/年(西元後兩碼)
		this.print(-2, 165, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3, "報  表 ：" + "L4211B");
		this.print(-3, 83, "匯款轉帳失敗表 ----(									)", "C");
		
		if (ReconCode.equals("P03")) {
			this.print(-3, 90, "A7");
		} else {
			this.print(-3, 90, ReconCode);// 存摺代號(表頭)A1~A7 (P03銀行存款－新光匯款轉帳)
		}
		
		this.print(-3, 165, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 148, "頁    數：" + this.getNowPage());
		this.print(-5, 64, "入帳日期 :           ---");
		
		if(String.valueOf(entrydate).length() == 7) {
			year = String.valueOf(entrydate).substring(0, 3);
			month = String.valueOf(entrydate).substring(3, 5);
			date = String.valueOf(entrydate).substring(5, 7);
		} else {
			year = String.valueOf(entrydate).substring(0, 2);
			month = String.valueOf(entrydate).substring(2, 4);
			date = String.valueOf(entrydate).substring(4, 6);
		}
		
		this.print(-5, 74, year + "/" + month + "/" + date);
		this.print(-5, 88, year + "/" + month + "/" + date);
		
		this.print(-5, 159, "單    位：元", "R");
		this.print(-6, 64, "會計日期 :								---");
		this.print(-8, 1,
				"    匯款日    匯款序號  		          匯款金額     	 匯款銀行      戶號      戶名         	     連絡電話       	       會計日期         交易序號  	     備註 ");
		this.print(-9, 0,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo) throws LogicException {

		
		this.info("exec   =" + titaVo.toString());
		long sno = 0;

		List<Map<String, String>> fnAllList = new ArrayList<>();
		try {
			fnAllList = l4211BRServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4211ServiceImpl.findAll error = " + errors.toString());
		}

		if(fnAllList.size() != 0 ) {
		  this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4211", "匯款轉帳失敗表", "", "A4", "L");
		
		  entrydate = titaVo.get("EntryDate");
		
		  DecimalFormat df1 = new DecimalFormat("#,##0");
		
		  int i = 0,pageCnt = 0;
			
		  ReconCode = fnAllList.get(0).get("F0");
		  for (int j = 1 ; j <= fnAllList.size() ; j++) {
			i = j - 1;

			int lengthF7 = 10;
			if (fnAllList.get(i).get("F7").length() < 10) {
			  lengthF7 = fnAllList.get(i).get("F7").length();
			}
			
			if("1".equals(fnAllList.get(i).get("F12")) && !"00103".equals(fnAllList.get(i).get("F11"))) {  // 失敗明細
				
				ReconCode = fnAllList.get(i).get("F0");
				
				this.print(1, 3, showRocDate((fnAllList.get(i).get("F2")), 1)); // 匯款日
				this.print(0, 20, fnAllList.get(i).get("F3")); // 匯款序號
				this.print(0, 43, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F4"))), "R");// 匯款金額
				this.print(0, 50, fnAllList.get(i).get("F5")); // 匯款銀行
				this.print(0, 63, FormatUtil.pad9(fnAllList.get(i).get("F6"), 7)); // 戶號
				this.print(0, 73, fnAllList.get(i).get("F7").substring(0, lengthF7)); // 戶名
				this.print(0, 91, fnAllList.get(i).get("F8")); // 連絡電話
				this.print(0, 114,String.valueOf(Integer.valueOf(fnAllList.get(i).get("F9")) - 19110000)); // 會計日期
				this.print(0, 130, fnAllList.get(i).get("F10")); // 交易序號
				this.print(0, 145, fnAllList.get(i).get("F11")); // 備註
				pageCnt++;
				
				falsetimes++;
				falseamt = falseamt.add(parse.stringToBigDecimal(fnAllList.get(i).get("F4")));
				
				
			} else { // 待處理 2 3 4   // 單筆入帳 5 // 批次入帳 6 // 轉暫收 7 // 預先作業 ProcStsCode=1 && ProcCode=00103
				
				ReconCode = fnAllList.get(i).get("F0");
				
				String ProcStsCode = fnAllList.get(i).get("F12");
				if("2".equals(ProcStsCode) || "3".equals(ProcStsCode) ||"4".equals(ProcStsCode)){
					falsetimes234++;
					falseamt234 = falseamt234.add(parse.stringToBigDecimal(fnAllList.get(i).get("F4")));
				} else if("5".equals(ProcStsCode)) {
					times5++;
					amt5 = amt5.add(parse.stringToBigDecimal(fnAllList.get(i).get("F4")));
				} else if("6".equals(ProcStsCode)) {
					times6++;
					amt6 = amt6.add(parse.stringToBigDecimal(fnAllList.get(i).get("F4")));
				} else if("7".equals(ProcStsCode)) {
					times7++;
					amt7 = amt7.add(parse.stringToBigDecimal(fnAllList.get(i).get("F4")));
				} 
				
			} // else 
			
			
			if (j != fnAllList.size()) {
				
//				存摺號碼 不同則跳頁
				String sReconCode = fnAllList.get(j).get("F0");
				if (!fnAllList.get(i).get("F0").equals(sReconCode)){
					this.info("Not Match...");
					
					// 換頁科目合計		
					this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					this.print(1, 20,"失敗　　　　　　　　　　　　　　　　件");
					this.print(0, 3, showRocDate((fnAllList.get(i).get("F2")), 1));	
					
					amt(); 
					
					pageCnt = pageCnt + 6;
					this.print(pageIndex - pageCnt - 2, 85, "=====續下頁=====", "C");
					
					amttototal(); // 合計累加
					init(); // 歸0
					
					
					pageCnt = 0;
					ReconCode = fnAllList.get(j).get("F0");
					this.newPage();
					continue;
					
					
				}
				
				
//				每頁第42筆 跳頁 
				if (pageCnt >= 40) {
					this.print(pageIndex - pageCnt - 2, 85, "=====續下頁=====", "C");
//
					pageCnt = 0;
					this.newPage();
					continue;
				}
				
			} else {
//			3.若為最後一筆，則固定產出小計、總計、報表合計
//				扣除總計合計的行數 +1 
				this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				
				
				this.print(1, 20,"失敗　　　　　　　　　　　　　　　　件");
				this.print(0, 3, showRocDate((fnAllList.get(i).get("F2")), 1));	
				
				amt(); 
				this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				amttototal(); // 合計累加
				
				this.print(1, 20,"失敗　　　　　　　　　　　　　　　　件");
				totamt();
				this.print(1, 1, "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				pageCnt = pageCnt + 13;
				this.print(pageIndex - pageCnt - 2, 85, "=====報表結束=====", "C");
				this.print(2, 90, "　　　　　　　　　　　　　　　　　　　　課長：　　　　　　　　　　製表人：", "C");
			}

		  } // for
		
		sno = this.close();
		this.toPdf(sno);
		} else {
			throw new LogicException("E2003", "查無資料"); // 查無資料
		}
	}
	
	private void amt() {
		
		DecimalFormat df1 = new DecimalFormat("#,##0");
		
		if(falseamt.compareTo(new BigDecimal("0")) != 0) {
			this.print(0, 43, df1.format(falseamt), "R");// 匯款金額							
		}
		if(falsetimes != 0 ) {
		  this.print(0, 48, String.valueOf(falsetimes));// 次數
		}
		
		this.print(1, 20,"待處理　　　　　　　　　　　　　　　件");
		if(falseamt234.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(falseamt234), "R");// 匯款金額
		}
		if(falsetimes234 != 0 ) {
		  this.print(0, 48, String.valueOf(falsetimes234));// 次數
		}
		
		
		this.print(1, 20,"批次入帳　　　　　　　　　　　　　　件");
		if(amt6.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(amt6), "R");// 匯款金額
		}
		if(times6 != 0 ) {
		  this.print(0, 48, String.valueOf(times6));// 次數
		}
		
		
		this.print(1, 20,"單筆入帳　　　　　　　　　　　　　　件");
		if(amt5.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(amt5), "R");// 匯款金額
		}
		if(times5 != 0 ) {
		  this.print(0, 48, String.valueOf(times5));// 次數
		}
		
		
		this.print(1, 20,"轉暫收　　　　　　　　　　　　　　　件");
		if(amt7.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(amt7), "R");// 匯款金額
		}
		if(times7 != 0 ) {
		  this.print(0, 48, String.valueOf(times7));// 次數
		}
		
		
		amt = amt.add(falseamt).add(falseamt234).add(amt6).add(amt5).add(amt7);
		times = times + falsetimes + falsetimes234 + times6 + times5 + times7 ;
		
		this.print(1, 20,"合　　計　　　　　　　　　　　　　　件");
		if(amt.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(amt), "R");// 匯款金額
		}
		if(times != 0 ) {
		  this.print(0, 48, String.valueOf(times));// 次數
		}
		
	}
	
	private void totamt() {
		
		DecimalFormat df1 = new DecimalFormat("#,##0");
		
		if(totfalseamt.compareTo(new BigDecimal("0")) != 0) {
			this.print(0, 43, df1.format(totfalseamt), "R");// 匯款金額							
		}
		if(totfalsetimes != 0 ) {
		  this.print(0, 48, String.valueOf(totfalsetimes));// 次數
		}
		
		this.print(1, 20,"待處理　　　　　　　　　　　　　　　件");
		if(totfalseamt234.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(totfalseamt234), "R");// 匯款金額
		}
		if(totfalsetimes234 != 0 ) {
		  this.print(0, 48, String.valueOf(totfalsetimes234));// 次數
		}
		
		
		this.print(1, 20,"批次入帳　　　　　　　　　　　　　　件");
		if(totamt6.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(totamt6), "R");// 匯款金額
		}
		if(tottimes6 != 0 ) {
		  this.print(0, 48, String.valueOf(tottimes6));// 次數
		}
		
		
		this.print(1, 20,"單筆入帳　　　　　　　　　　　　　　件");
		if(totamt5.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(totamt5), "R");// 匯款金額
		}
		if(tottimes5 != 0 ) {
		  this.print(0, 48, String.valueOf(tottimes5));// 次數
		}
		
		
		this.print(1, 20,"轉暫收　　　　　　　　　　　　　　　件");
		if(totamt7.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(totamt7), "R");// 匯款金額
		}
		if(tottimes7 != 0 ) {
		  this.print(0, 48, String.valueOf(tottimes7));// 次數
		}
		
		
		totamt = totamt.add(totfalseamt).add(totfalseamt234).add(totamt6).add(totamt5).add(totamt7);
		totaltimes = totaltimes + totfalsetimes + totfalsetimes234 + tottimes6 + tottimes5 + tottimes7 ;
		
		this.print(1, 20,"總　　計　　　　　　　　　　　　　　件");
		if(totamt.compareTo(new BigDecimal("0")) != 0) {
		  this.print(0, 43, df1.format(totamt), "R");// 匯款金額
		}
		if(totaltimes != 0 ) {
		  this.print(0, 48, String.valueOf(totaltimes));// 次數
		}
		
	}

	private void init() {
		falseamt = new BigDecimal("0");
		falseamt234 = new BigDecimal("0");
		amt5 = new BigDecimal("0");
		amt6 = new BigDecimal("0");
		amt7 = new BigDecimal("0");
		amt = new BigDecimal("0");
				
		falsetimes = 0;
		falsetimes234 = 0;
		times5 = 0;
		times6 = 0;
		times7 = 0;
		
		times = 0;
	}
	
	private void amttototal() {
		
		totfalseamt = totfalseamt.add(falseamt);
		totfalseamt234 = totfalseamt234.add(falseamt234);
		totamt5 = totamt5.add(amt5);
		totamt6 = totamt6.add(amt6);
		totamt7 = totamt7.add(amt7);
							
		totfalsetimes += falsetimes;
		totfalsetimes234 += falsetimes234;
		tottimes5 += times5;
		tottimes6 += times6;
		tottimes7 += times7;
		
	}
	
}
