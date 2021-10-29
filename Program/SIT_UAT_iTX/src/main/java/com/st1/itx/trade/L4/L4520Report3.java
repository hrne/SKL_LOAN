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
import com.st1.itx.db.service.springjpa.cm.L4520RServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4520Report3")
@Scope("prototype")

public class L4520Report3 extends MakeReport {


	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	public L4520RServiceImpl l4520RServiceImpl;
	
	@Autowired
	public EmpDeductMediaService empDeductMediaService;
	

	@Autowired
	public CustMainService custMainService;
	
	private int acdate = 0;
	private String BatchNo = "";
//	每頁筆數
	private int pageIndex = 40;
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");
		
		printHeaderP();
		
		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 3, "程式ID：" + "L4520Report3");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
		this.print(-1, 120, "日    期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim);
		this.print(-2, 3, "報　表：" + "L4520Report3");
		this.print(-2, 70, "火險費沖銷明細表（員工扣薪）", "C");
		this.print(-2, 120, "時    間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-3, 120, "頁　　數：" + this.getNowPage());
		this.print(-4, 1, " 會計日期：" + formatDate(acdate));
		this.print(-4, 38, "批次號碼：" + BatchNo);
		this.print(-6, 1, " 戶號     年月     原保單號碼         戶名        員工代號   身份證字號    交易序號           沖火險費           扣款金額           未沖金額");
		this.print(-7, 0, "---------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	public long exec(TitaVo titaVo1) throws LogicException {

		titaVo = titaVo1;
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值	
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;
		long sno = 0;
		
		acdate = parse.stringToInteger(titaVo.getParam("AcDate"));
		
		this.info("L4520Report3 exec");
		List<Map<String, String>> fnAllList = new ArrayList<>();
		
		try {
			fnAllList = l4520RServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4510ServiceImpl.findAll error = " + errors.toString());
		}
		
		
		if (fnAllList.size() > 0) {
			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4520", "火險費沖銷明細表（員工扣薪）", "", "A4", "L");
			
			DecimalFormat df1 = new DecimalFormat("#,##0");
			int total = 0, i = 0, pageCnt = 0;
			
			BigDecimal sumA1 = BigDecimal.ZERO;
			BigDecimal sumA2 = BigDecimal.ZERO;
			
			for(int j = 1; j <= fnAllList.size(); j++) {
//			1.每筆先印出明細
		      i = j - 1;
		      BatchNo = fnAllList.get(i).get("F0");
		      
			  this.print(1, 1,"                                                                                                                                                                               ");
	  		  this.print(0, 2, FormatUtil.pad9(fnAllList.get(i).get("F1"), 7));// 戶號
	  		  this.print(0, 11, String.valueOf(parse.stringToInteger(fnAllList.get(i).get("F2"))-191100)); // 年月
	  		  this.print(0, 19, fnAllList.get(i).get("F3")); // 原保單號碼
	  		  this.print(0, 36, fnAllList.get(i).get("F4")); // 戶名
	  		  this.print(0, 50, fnAllList.get(i).get("F5")); // 員工代號
	  		  this.print(0, 60, fnAllList.get(i).get("F6")); // 身份證字號
	  		  this.print(0, 73, fnAllList.get(i).get("F7")); // 交易序號
	  		  if(parse.stringToInteger(fnAllList.get(i).get("F9")) > 0) {
	  			  this.print(0, 98, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F8"))), "R");// 沖火險費
	  			  this.print(0, 116, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F8"))), "R");// 沖火險費
	  			  sumA1 = sumA1.add(parse.stringToBigDecimal(fnAllList.get(i).get("F8")));
	  			  sumA2 = sumA2.add(parse.stringToBigDecimal(fnAllList.get(i).get("F8")));
	  		  } else {
	  			  this.print(0, 116, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F8"))), "R");// 沖火險費
	  			  this.print(0, 135, df1.format(parse.stringToBigDecimal(fnAllList.get(i).get("F8"))), "R");// 沖火險費
	  			  sumA2 = sumA2.add(parse.stringToBigDecimal(fnAllList.get(i).get("F8")));
	  		  }
	  		                    
	  		  this.print(1, 0, "-------------------------------------------------------------------------------------------------------------------------------------------------");
			  
//			  每頁筆數相加
			  pageCnt = pageCnt + 2;
//			  全部筆數統計
			  total++;	
	  		  if (j != fnAllList.size()) {
	  			
	  			if (!fnAllList.get(i).get("F0").equals(fnAllList.get(j).get("F0"))){
	  				this.info("BatchNo Not Match...");
	  				
//					扣除合計的行數
					this.print(pageIndex - pageCnt - 2, 70, "=====續下頁=====", "C");
	  				pageCnt = 0;
	  				BatchNo = fnAllList.get(j).get("F0");
					this.newPage();
					continue;
	  			}
	  			  
	  			  
//				每頁第20筆 跳頁 
				if (pageCnt == 20) {
					this.print(1, 70, "=====續下頁=====", "C");

					pageCnt = 0;
					this.newPage();
					continue;
				}
	  			
			  } else {
				  if (total == fnAllList.size()) {
						this.print(1, 1, "   總　計：                                                                                                             ");
						this.print(0, 98, df1.format(sumA1), "R");// 沖火險費
						this.print(0, 116, df1.format(sumA2), "R");// 沖火險費
//						扣除總計合計的行數 +1 
						this.print(pageIndex - pageCnt - 2, 70, "=====報表結束=====", "C");
				  } 
				  
			  } // else 
			

			} // if
			sno = this.close();
			this.toPdf(sno);
		}
		return sno;
		
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
