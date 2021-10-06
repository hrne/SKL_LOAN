package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Service
@Scope("prototype")

public class LM052Report extends MakeReport {

	@Autowired
	LM052ServiceImpl lM052ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
		calMonthDate.set(iYear, iMonth - 1, 0);

		int lyymm = (Integer.valueOf(dateFormat.format(calMonthDate.getTime()))-19110000)/100 ;

		this.info("LM052Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類-會計部備呆計提", "LM052_放款資產分類-會計部備呆計提",
				"LM052_底稿_放款資產分類-會計部備呆計提.xlsx", "備呆總表");

		String formTitle = "";

		formTitle = (iYear - 1911) + "年 " + String.format("%02d", iMonth) + "    放款資產品質分類";
		makeExcel.setValue(1, 1, formTitle);

		formTitle = lyymm + "\n" + "放款總額";
		makeExcel.setValue(15, 3, formTitle, "C");

		List<Map<String, String>> lM052List = null;

		for (int formNum = 1; formNum <= 4; formNum++) {

			try {

				lM052List = lM052ServiceImpl.findAll(titaVo, formNum);

			} catch (Exception e) {

				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM052ServiceImpl.findAll error = " + errors.toString());

			}

			exportExcel(lM052List, formNum);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/*
	 * 應收利息提列2%：用MonthlyFacBal的應收利息加總、前者*0.02
	 * 
	 * 五類資產評估合計：M13+M14 1-5類總額(含應收息)提列1%：F13+L14 *0.01 無擔保案件總金額：F11
	 * 
	 * 法定備抵損失提撥(含應收息1%)：特定和非特定資產的提存金額 (G28+L28)+ 應收利息提列2%(L14)
	 * 
	 * 
	 * 問題： B30的 第三項 最後面金額 本月預期損失金額 30.89百萬元 (從IFRS9) 第四項 的4月份 十計提列 638.283百萬元
	 * 
	 * 
	 */
	private void exportExcel(List<Map<String, String>> LDList, int formNum) throws LogicException {

	
		
		BigDecimal amt = BigDecimal.ZERO;

		if (LDList.size() > 0) {
			
			int row = 0;
			int col = 0;
			
			for (Map<String, String> tLDVo : LDList) {

				switch (formNum) {
				case 1:
					row = tLDVo.get("F0").equals("11") ? 4: 
						  tLDVo.get("F0").equals("12") ? 5:
						  tLDVo.get("F0").equals("21") ? 6:
						  tLDVo.get("F0").equals("22") ? 7:
						  tLDVo.get("F0").equals("23") ? 8:
						  tLDVo.get("F0").equals("3") ? 9:
						  tLDVo.get("F0").equals("4") ? 10:
						  tLDVo.get("F0").equals("5") ? 11:
						  tLDVo.get("F0").equals("6") ? 12 : 14;
					
					col = tLDVo.get("F1").equals("00A") ? 3:
						  tLDVo.get("F1").equals("201") ? 4:
						  tLDVo.get("F0").equals("6") && tLDVo.get("F1").equals("999") ? 6 : 12;
					
					
					
					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
					
					break;
				case 2:

						row = tLDVo.get("F0").equals("1") ? 17 : 
							  tLDVo.get("F0").equals("21") ? 18 :
							  tLDVo.get("F0").equals("22") ? 19 :
							  tLDVo.get("F0").equals("23") ? 20 :
							  tLDVo.get("F0").equals("3") ? 21 :
							  tLDVo.get("F0").equals("4") ? 22 :
							  tLDVo.get("F0").equals("5") ? 23 : 24;			  
						
						col = 3 ;
						
						amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));

					break;
				case 3:
					
					row = tLDVo.get("F0").equals("1") ? 16 : 
						  tLDVo.get("F0").equals("2") ? 17 : 18;
						  					
					col = tLDVo.get("F1").equals("310") ? 9 :
						  tLDVo.get("F1").equals("320") ? 8 : 7 ; 
					
					
					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
					
					break;

				case 4:
					row = 27;
						  					
					col = tLDVo.get("F0").equals("S1") ? 7 :
						  tLDVo.get("F0").equals("S2") ? 8 :
						  tLDVo.get("F0").equals("NS1") ? 9 :
						  tLDVo.get("F0").equals("NS2") ? 11 : 12;
						  
					
					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
					
					
					break;
					

				case 5:
					row = 27;
						  					
					col = 15;
					
					amt = tLDVo.get("F0").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F0"));
					
					
					break;
				}
				
				
				makeExcel.setValue( row , col , amt , "#,##0");

			}
			
			//C13 D13 E13
			makeExcel.formulaCaculate(13,3);
			makeExcel.formulaCaculate(13,4);
			makeExcel.formulaCaculate(13,5);
			
			//F4~F11
			for(int r = 4 ;r<=11; r++) {
				makeExcel.formulaCaculate(r,6);				
			}
			
			//F13
			makeExcel.formulaCaculate(13,6);				
			
			//J4~M13 M14
			for(int r = 4 ;r<=13; r++) {
				for(int c = 10;c<=13;c++) {					
					makeExcel.formulaCaculate(r,c);				
				}
			}
			
			makeExcel.formulaCaculate(14,13);			
			
			//C25 D17~D25
			makeExcel.formulaCaculate(25,3);
			for(int r = 17 ;r<=25; r++) {
				makeExcel.formulaCaculate(r,4);				
			}
			
			//G19 H19 I19 G20
			makeExcel.formulaCaculate(19,7);
			makeExcel.formulaCaculate(19,8);
			makeExcel.formulaCaculate(19,9);
			makeExcel.formulaCaculate(20,7);
			
			//M16~18
			makeExcel.formulaCaculate(16,13);
			makeExcel.formulaCaculate(17,13);
			makeExcel.formulaCaculate(18,13);

			//G28 L28 M24
			makeExcel.formulaCaculate(28,7);
			makeExcel.formulaCaculate(28,12);
			makeExcel.formulaCaculate(24,13);
			
			//B30
			makeExcel.formulaCaculate(30,2);
			
			//set height row 1,4~13,23,24
			makeExcel.setHeight(1, 40);
			
			for(int r = 4 ; r <= 13 ; r++) {
				makeExcel.setHeight(r, 40);	
			}
			
			makeExcel.setHeight(23, 20);
			makeExcel.setHeight(24, 40);
			
		
		}

	}


}