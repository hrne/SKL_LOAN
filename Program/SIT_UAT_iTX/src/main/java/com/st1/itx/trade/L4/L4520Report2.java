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
import com.st1.itx.db.service.springjpa.cm.L4520ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4520Report2")
@Scope("prototype")

public class L4520Report2 extends MakeReport {


	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public L4520ServiceImpl l4520ServiceImpl;
	
	@Autowired
	public EmpDeductMediaService empDeductMediaService;
	

	@Autowired
	public CustMainService custMainService;
	
	private int acdate = 0;
	private String BatchNo = "";
	private String ProcCode = "";
	private String PerfMonth = "";
	
	private String year = "";
	private String month = "";
	private String date = "";
//	每頁筆數
	private int pageIndex = 40;
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");
		
		
		printHeaderP();	 
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}
	
	public void printHeaderP() {
		this.setFont(1,8);
		this.print(-1, 138, "機密等級：密");
		this.print(-2, 3, "程式ID：" + "L4520Report2");
		this.print(-2, 80, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-2, 155,"日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-3, 3,  "報　表：" + "L4520Report2");
		this.print(-3, 80, "員工扣薪總傳票明細表", "C");
		this.print(-3, 155,"時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-4, 80, "批次號碼：" + BatchNo, "C");
		this.print(-4, 148,"頁    數：" + this.getNowPage(), "R");
		this.print(-5, 80, "會計日期：     年    月   日", "C");
		
		if(String.valueOf(acdate).length() == 7) {
			year = String.valueOf(acdate).substring(0, 3);
			month = String.valueOf(acdate).substring(3, 5);
			date = String.valueOf(acdate).substring(5, 7);
		} else {
			year = String.valueOf(acdate).substring(0, 2);
			month = String.valueOf(acdate).substring(2, 4);
			date = String.valueOf(acdate).substring(4, 6);
		}
		
		this.print(-5, 77, year);
		this.print(-5, 84, month);
		this.print(-5, 89, date);
		
		this.print(-5, 149,"單    位：元","R");
		this.print(-6, 2, "業績年月：" + PerfMonth);
		this.print(-6, 18,"流程別：" + ProcCode);
		this.print(-6, 32,"部室別：" );
		this.print(-7, 2, "扣款代碼：" );
		this.print(-7, 32,"業務科目：" );
		this.print(-9, 1, "  戶號    計息起日   計息迄日    姓名          扣款金額          本金          利息          違約金          暫收借          暫收貸      短繳      單位     員工    身分證號碼");
		this.print(-10, 0, "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	
	public void exec(TitaVo titaVo1) throws LogicException {

		
		titaVo = titaVo1;
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值	
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;
		long sno = 0;
		ProcCode = titaVo.getParam("ProcCode");
		acdate = parse.stringToInteger(titaVo.getParam("AcDate"));
		PerfMonth = titaVo.getParam("PerfMonth");
		this.info("L4520Report2 exec");
		List<Map<String, String>> fnAllList = new ArrayList<>();
		
		try {
			fnAllList = l4520ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4510ServiceImpl.findAll error = " + errors.toString());
		}
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "員工扣薪總傳票明細表", "", "A4", "L");
	
		if (fnAllList.size() > 0) {
			
			DecimalFormat df1 = new DecimalFormat("#,##0");
			int total = 0, i = 0, pageCnt = 0;
			
			BigDecimal sumA1 = BigDecimal.ZERO;
			BigDecimal sumA2 = BigDecimal.ZERO;
			BigDecimal sumA3 = BigDecimal.ZERO;
			BigDecimal sumA4 = BigDecimal.ZERO;
			BigDecimal sumA5 = BigDecimal.ZERO;
			BigDecimal sumA6 = BigDecimal.ZERO;
			BigDecimal sumA7 = BigDecimal.ZERO;
			
			for(int j = 1; j <= fnAllList.size(); j++) {
//			1.每筆先印出明細
		      i = j - 1;
		      BatchNo = fnAllList.get(i).get("F0");
		      
			  this.print(1, 1,"                                                                                                                                                                               ");
	  		  this.print(0, 1, FormatUtil.pad9(fnAllList.get(i).get("F1"), 7));// 戶號
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F2")) != 0 ) {
	  		    this.print(0, 11, String.valueOf(parse.stringToInteger(fnAllList.get(i).get("F2"))-191100)); // 計息起日
//	  		  this.print(0, 9, "109/05/21"); // 計息起日
	  		  }
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F3")) != 0 ) {
	  		    this.print(0, 19, String.valueOf(parse.stringToInteger(fnAllList.get(i).get("F3"))-191100)); // 計息迄日
//	  		  this.print(0, 19, "109/05/21"); // 計息起日
	  		  }	  		  
	  		  this.print(0, 30, fnAllList.get(i).get("F4")); // 姓名
	  		  
//	  		  this.print(0, 53, df1.format(31732),"R"); // 扣款金額
//	  		  this.print(0, 66, df1.format(31732),"R"); // 本金
//	  		  this.print(0, 80, df1.format(31732),"R"); // 利息
//	  		  this.print(0, 95, df1.format(31732),"R"); // 違約金
//	  		  this.print(0, 110, df1.format(31732),"R"); // 暫收借
//	  		  this.print(0, 126, df1.format(31732),"R"); // 暫收貸
//	  		  this.print(0, 136, df1.format(31732),"R"); // 短繳
	  		
	  		  
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F6")) > 0) {
	  			this.print(0, 53, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F6"))),"R"); // 扣款金額
	  			sumA1 = sumA1.add(parse.stringToBigDecimal(fnAllList.get(i).get("F6")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F7")) > 0) {
	  			this.print(0, 66, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F7"))),"R"); // 本金
	  			sumA2 = sumA2.add(parse.stringToBigDecimal(fnAllList.get(i).get("F7")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F8")) > 0) {
	  			this.print(0, 80, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F8"))),"R"); // 利息
	  			sumA3 = sumA3.add(parse.stringToBigDecimal(fnAllList.get(i).get("F8")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F9")) > 0) {
	  			this.print(0, 95, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F9"))),"R"); // 違約金
	  			sumA4 = sumA4.add(parse.stringToBigDecimal(fnAllList.get(i).get("F9")));
	  		  }
	  		 
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F10")) > 0) {
	  			this.print(0, 110, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F10"))),"R"); // 暫收借
	  			sumA5 = sumA5.add(parse.stringToBigDecimal(fnAllList.get(i).get("F10")));
	  		  }
	  		 
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F10")) < 0) {
	  			this.print(0, 126, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F10"))),"R"); // 暫收貸
	  			sumA6 = sumA6.add(parse.stringToBigDecimal(fnAllList.get(i).get("F10")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F11")) > 0) {
	  			this.print(0, 136, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F11"))),"R"); // 短繳
	  			sumA7 = sumA7.add(parse.stringToBigDecimal(fnAllList.get(i).get("F11")));
	  		  }
	  		                    
	  		 
	  		  this.print(0, 141, fnAllList.get(i).get("F12")); // 單位
	  		  this.print(0, 149, fnAllList.get(i).get("F13")); // 員工
	  		  this.print(0, 157, fnAllList.get(i).get("F14")); // 身分證號碼
	  		
//			  每頁筆數相加
	  		  pageCnt++;
//			  全部筆數統計
			  total++;	
	  		  if (j != fnAllList.size()) {
	  			
	  			if (!fnAllList.get(i).get("F0").equals(fnAllList.get(j).get("F0"))){
	  				this.info("BatchNo Not Match...");
	  				
//					扣除合計的行數
					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
	  				pageCnt = 0;
	  				BatchNo = fnAllList.get(j).get("F0");
					this.newPage();
					continue;
	  			}
	  			  
	  			  
//				每頁第20筆 跳頁 
				if (pageCnt == 20) {
					this.print(1, 80, "=====續下頁=====", "C");

					pageCnt = 0;
					this.newPage();
					continue;
				}
	  			
			  } else {
				  if (total == fnAllList.size()) {
					    this.print(1, 0, "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 1, "總　計：               件                                                                                              ");
						this.print(0, 20, total+ "");
						
						if(sumA1.intValue() != 0) {
							this.print(0, 53, df1.format(sumA1),"R"); // 扣款金額							
						}
						
						if(sumA2.intValue() != 0) {
					  		this.print(0, 66, df1.format(sumA2),"R"); // 本金						
						}
						if(sumA3.intValue() != 0) {
					  		this.print(0, 80, df1.format(sumA3),"R"); // 利息						
						}
						if(sumA4.intValue() != 0) {
							this.print(0, 95, df1.format(sumA4),"R"); // 違約金							
						}
						if(sumA5.intValue() != 0) {
							this.print(0, 110, df1.format(sumA5),"R"); // 暫收借						
						}
						if(sumA6.intValue() != 0) {
							this.print(0, 126, df1.format(sumA6),"R"); // 暫收貸							
						}
						if(sumA7.intValue() != 0) {
							this.print(0, 136, df1.format(sumA7),"R"); // 短繳						
						}
						this.print(1, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
//						扣除總計合計的行數 +1 
						this.print(pageIndex - pageCnt - 2, 80, "=====報表結束=====", "C");
						this.print(2, 80, "課長：                      製表人：", "C");
				  } 
				  
			  } // else 


			} // if
		sno = this.close();
		this.toPdf(sno);
		}
		
	}


	private String formatDate(int date) {
		String result = "";

		if (date > 19110000) {
			date = date - 19110000;
		}
		result = FormatUtil.pad9("" + date, 7);

		result = result.substring(0, 3) + "/" + result.substring(3, 5) + "/" + result.substring(5);

		return result;
	}
		

}