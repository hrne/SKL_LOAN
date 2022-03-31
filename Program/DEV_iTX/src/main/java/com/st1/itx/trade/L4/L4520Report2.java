package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
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
	
	
	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;
	
	@Autowired
	public CustMainService custMainService;
	
	private int acdate = 0;
	private String BatchNo = "";
	private String ProcCode = "";
	private String RepayCode = "";
	private String RepayCodeX = "";
	
	private String AcctCode = "";
	private String AcctCodeX = "";
	
	private String PerfMonth = "";
	
	private String year = "";
	private String month = "";
	private String date = "";
//	每頁筆數
	private int pageIndex = 40;
	
	private List<CdCode> lCdCode = null;
	private List<CdCode> lCdCode2 = null;
	
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
		
		
		for(CdCode tCdCode :lCdCode) {
			if(RepayCode.equals(tCdCode.getCode())) {
				RepayCodeX = tCdCode.getItem();
			}
		}
		this.print(-7, 2, "扣款代碼：" + RepayCode + " " + RepayCodeX);
		
		for(CdCode tCdCode :lCdCode2) {
			if(AcctCode.equals(tCdCode.getCode())) {
				AcctCodeX = tCdCode.getItem();
			}
		}
		
		this.print(-7, 32,"業務科目：" + AcctCodeX);
		this.print(-9, 1, "  戶號    計息起日   計息迄日    姓名          扣款金額         本金         利息       違約金      暫收借      暫收貸      短繳   帳管費及其他  單位     員工    身分證號碼  ");
		this.print(-10, 0, "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	
	public void exec(TitaVo titaVo1, List<Map<String, String>> fnAllList) throws LogicException {

		
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
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "員工扣薪總傳票明細表", "", "A4", "L");
	
		
		Slice<CdCode> slCdCode = sCdCodeDefService.defItemEq("PerfRepayCode", "%", this.index, this.limit, titaVo);
		
		lCdCode = slCdCode == null ? null : slCdCode.getContent();
		
		Slice<CdCode> slCdCode2 = sCdCodeDefService.defItemEq("AcctCode", "%", this.index, this.limit, titaVo);
		
		lCdCode2 = slCdCode2 == null ? null : slCdCode2.getContent();
		
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
			BigDecimal sumA8 = BigDecimal.ZERO;
			
			for(int j = 1; j <= fnAllList.size(); j++) {
//			1.每筆先印出明細
		      i = j - 1;
		      BatchNo = fnAllList.get(i).get("BaTxNo");
		      RepayCode = fnAllList.get(i).get("RepayCode");
		      AcctCode = fnAllList.get(i).get("AcctCode");
		      ProcCode = fnAllList.get(i).get("ProcCode");
			  this.print(1, 1,"                                                                                                                                                                               ");
	  		  this.print(0, 1, FormatUtil.pad9(fnAllList.get(i).get("CustNo"), 7));// 戶號
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("IntStartDate")) != 0 ) {
	  		    this.print(0, 11, String.valueOf(parse.stringToInteger(fnAllList.get(i).get("IntStartDate"))-19110000)); // 計息起日
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("IntEndDate")) != 0 ) {
	  		    this.print(0, 21, String.valueOf(parse.stringToInteger(fnAllList.get(i).get("IntEndDate"))-19110000)); // 計息迄日
	  		  }	  		
	  		  
	  		  this.print(0, 30, fnAllList.get(i).get("Fullname")); // 姓名
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("TxAmt")) > 0) {
	  			this.print(0, 53, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("TxAmt"))),"R"); // 扣款金額
	  			sumA1 = sumA1.add(parse.stringToBigDecimal(fnAllList.get(i).get("TxAmt")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("Principal")) > 0) {
	  			this.print(0, 65, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("Principal"))),"R"); // 本金
	  			sumA2 = sumA2.add(parse.stringToBigDecimal(fnAllList.get(i).get("Principal")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("Interest")) > 0) {
	  			this.print(0, 78, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("Interest"))),"R"); // 利息
	  			sumA3 = sumA3.add(parse.stringToBigDecimal(fnAllList.get(i).get("Interest")));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("BreachAmt")) > 0) {
	  			this.print(0, 90, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("BreachAmt"))),"R"); // 違約金
	  			sumA4 = sumA4.add(parse.stringToBigDecimal(fnAllList.get(i).get("BreachAmt")));
	  		  }
	  		 
	  		  if(parse.stringToInteger(fnAllList.get(i).get("TempDr")) > 0) {
	  			this.print(0, 102, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("TempAmt"))),"R"); // 暫收借
	  			sumA5 = sumA5.add(parse.stringToBigDecimal(fnAllList.get(i).get("TempAmt")));
	  		  }
	  		 
	  		  if(parse.stringToInteger(fnAllList.get(i).get("TempCr")) > 0) {
	  			this.print(0, 113, df1.format(new BigDecimal("0").subtract(parse.stringToBigDecimal(fnAllList.get(i).get("TempAmt")))),"R"); // 暫收貸
	  			sumA6 = sumA6.add(new BigDecimal("0").subtract(parse.stringToBigDecimal(fnAllList.get(i).get("TempAmt"))));
	  		  }
	  		  
	  		  if(parse.stringToInteger(fnAllList.get(i).get("Shortfall")) > 0) {
	  			this.print(0, 123, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("Shortfall"))),"R"); // 短繳
	  			sumA7 = sumA7.add(parse.stringToBigDecimal(fnAllList.get(i).get("Shortfall")));
	  		  }
	  		                    
		  	  if(parse.stringToInteger(fnAllList.get(i).get("OtherAmt")) > 0) {
		  		this.print(0, 132, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("OtherAmt"))),"R"); // 帳管費及其他
		  		sumA8 = sumA8.add(parse.stringToBigDecimal(fnAllList.get(i).get("OtherAmt")));
		  	  }
		  	  
	  		  this.print(0, 139, fnAllList.get(i).get("CenterCode")); // 單位
	  		  this.print(0, 147, fnAllList.get(i).get("EmployeeNo")); // 員工
	  		  this.print(0, 155, fnAllList.get(i).get("CustId")); // 身分證號碼
	  		
//			  每頁筆數相加
	  		  pageCnt++;
//			  全部筆數統計
			  total++;	
	  		  if (j != fnAllList.size()) {
	  			// 批號 或 扣款代碼 或 流程別 或 科目 不同
	  			if (!fnAllList.get(i).get("BaTxNo").equals(fnAllList.get(j).get("BaTxNo")) 
	  					|| !fnAllList.get(i).get("RepayCode").equals(fnAllList.get(j).get("RepayCode"))
	  					|| !fnAllList.get(i).get("AcctCode").equals(fnAllList.get(j).get("AcctCode")) 
	  					|| !fnAllList.get(i).get("ProcCode").equals(fnAllList.get(j).get("ProcCode"))){
	  				this.info("BatchNo or RepayCode or AcctCode or ProcCode Not Match...");
	  				
//					扣除合計的行數
					this.print(pageIndex - pageCnt - 2, 80, "=====續下頁=====", "C");
	  				pageCnt = 0;
	  				BatchNo = fnAllList.get(j).get("BaTxNo");	
	  				RepayCode = fnAllList.get(j).get("RepayCode");
	  		        AcctCode = fnAllList.get(j).get("AcctCode");
	  		        ProcCode = fnAllList.get(j).get("ProcCode");
					this.newPage();
					continue;
	  			}
	  			  
	  			  
//				每頁第38筆 跳頁 
				if (pageCnt >= 38) {
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
					  		this.print(0, 65, df1.format(sumA2),"R"); // 本金						
						}
						
						if(sumA3.intValue() != 0) {
					  		this.print(0, 78, df1.format(sumA3),"R"); // 利息						
						}
						
						if(sumA4.intValue() != 0) {
							this.print(0, 90, df1.format(sumA4),"R"); // 違約金							
						}
						
						if(sumA5.intValue() != 0) {
							this.print(0, 102, df1.format(sumA5),"R"); // 暫收借						
						}
						
						if(sumA6.intValue() != 0) {
							this.print(0, 113, df1.format(sumA6),"R"); // 暫收貸							
						}
						
						if(sumA7.intValue() != 0) {
							this.print(0, 123, df1.format(sumA7),"R"); // 短繳						
						}
						
						if(sumA8.intValue() != 0) {
							this.print(0, 132, df1.format(sumA8),"R"); // 帳管費及其他				
						}
						this.print(1, 0, "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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

}
