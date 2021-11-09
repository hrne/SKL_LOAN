package com.st1.itx.util.common.data;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.OccursList;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */

@Component("InsuCommFileVo")
@Scope("prototype")

public class InsuCommFileVo extends FileVo {

	private static final long serialVersionUID = 299883955044784188L;

	// 設定首筆筆數
	private final int headerCounts = 0;
	// 設定尾筆筆數
	private final int footerCounts = 0;

	private BigDecimal commRate11 = new BigDecimal("0.11");
	private BigDecimal commRate12 = new BigDecimal("0.65");
	private BigDecimal commRate13 = new BigDecimal("0.65");
	private BigDecimal commRate15 = new BigDecimal("0.04");
//	【CMT04險別】=1
//			Case1 【險種CMT11】=11→【應領金額CMT20】=【保費CMT12】*【佣金率(0.11)】
//			Case2 【險種CMT11】=12、13→【應領金額CMT20】=【佣金CMT13】*【佣金率(0.65)】
//			Case3【險種CMT11】=15→【應領金額CMT20】=【保費CMT12】*【佣金率(0.04)】
//			不符合Case1、Case2、Case3的則為0
//   
//	 佣金率
//			險種     新佣金率
//			11	   	0.11   
//			15 	   	0.04   
//			12	   	0.65   
//			13      0.65  
//

	@Autowired
	public Parse parse;

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	/**
	 * 從檔案讀取到的資料，依照此方法中的定義，擷取欄位值
	 * 
	 * @param lineList 放入FileCom的intputTxt取得的ArrayList
	 */
	@Override
	public void setValueFromFile(ArrayList<String> lineList) {

		// 總行數
		int LastIndex = lineList.size() - 1;

		int i = 0;
		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 頁首
			if (i < headerCounts) {
				// 設定頁首欄位的擷取位置
			}

			// 明細
			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
				OccursList occursList = new OccursList();

//  0.經紀人代號  
//  1.保單號碼    
//  2.批號        
//  3.險別                  【CMT04險別】
//  4.簽單日期    
//  5.被保險人    
//  6.被保險人地址
//  7.被保險人電話
//  8.起保日期    
//  9.到期日期    
// 10.險種                  【險種CMT11】
// 11.保費                  【保費CMT12】
// 12.佣金率      
// 13.佣金                  【佣金CMT13】
// 14.合計保費
// 15.合計佣金
// 16.收件號碼
// 17.收費日期
// 18.佣金日期
// 19.借款人戶號
// 20.額度編號 
// ---------------------------------------------------------
// 應領金額 DueAmt  【應領金額CMT20】
//

				// 設定明細欄位的擷取位置

				String[] thisColumn = thisLine.split("[|]");
//				【CMT04險別】=1
//				Case1 【險種CMT11】=11→【應領金額CMT20】=【保費CMT12】*【佣金率(0.11)】
//				Case2 【險種CMT11】=12、13→【應領金額CMT20】=【佣金CMT13】*【佣金率(0.65)】
//				Case3【險種CMT11】=15→【應領金額CMT20】=【保費CMT12】*【佣金率(0.04)】
//				不符合Case1、Case2、Case3的則為0

				if (thisColumn.length >= 1 && thisColumn != null) {
					occursList.putParam("IndexCode", thisColumn[0].trim());
					occursList.putParam("InsuNo", thisColumn[1].trim());
					occursList.putParam("BatxNo", thisColumn[2].trim());
					occursList.putParam("InsuKind", thisColumn[3].trim()); // 【CMT04險別】
					occursList.putParam("SignDate", thisColumn[4].trim());
					occursList.putParam("InsuredName", thisColumn[5].trim());
					occursList.putParam("InsuredAddress", thisColumn[6].trim());
					occursList.putParam("InsuredTeleNo", thisColumn[7].trim());
					occursList.putParam("InsuStartDate", thisColumn[8].trim());
					occursList.putParam("InsuEndDate", thisColumn[9].trim());
					occursList.putParam("InsuType", thisColumn[10].trim()); // 【險種CMT11】
					occursList.putParam("InsuFee", thisColumn[11].trim()); // 【保費CMT12】
					occursList.putParam("InsuCommRate", thisColumn[12].trim());
					occursList.putParam("InsuComm", thisColumn[13].trim()); // 【佣金CMT13】
					occursList.putParam("TotalFee", thisColumn[14].trim());
					occursList.putParam("TotalComm", thisColumn[15].trim());
					occursList.putParam("CaseNo", thisColumn[16].trim());
					occursList.putParam("AcDate", thisColumn[17].trim());
					occursList.putParam("CommDate", thisColumn[18].trim());
					occursList.putParam("CustNo", thisColumn[19].trim());
					occursList.putParam("FacmNo", thisColumn[20].trim());
//					【CMT04險別】=1
//					Case1 【險種CMT11】=11→【應領金額CMT20】=【保費CMT12】*【佣金率(0.11)】
//					Case2 【險種CMT11】=12、13→【應領金額CMT20】=【佣金CMT13】*【佣金率(0.65)】
//					Case3【險種CMT11】=15→【應領金額CMT20】=【保費CMT12】*【佣金率(0.04)】
//					不符合Case1、Case2、Case3的則為0
					String commBase = "0";
					BigDecimal commRate = BigDecimal.ZERO;
					if ("1".equals(thisColumn[3].trim())) {
						switch (thisColumn[10].trim()) {
						case "11":
							commBase = thisColumn[11].trim();
							commRate = commRate11;
							break;
						case "12":
							commBase = thisColumn[13].trim();
							commRate = commRate12;
							break;
						case "13":
							commBase = thisColumn[13].trim();
							commRate = commRate13;
							break;
						case "15":
							commBase = thisColumn[11].trim();
							commRate = commRate15;
							break;

						}
					}
					occursList.putParam("CommBase", commBase);
					occursList.putParam("CommRate", commRate);
					this.info("commBase) = " + commBase + ",commRate=" +commRate);
				}

				this.occursList.add(occursList);
			}

			// 頁尾
			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置
			}

			i++;
		}

	}

	/**
	 * 把目前FileVo內的欄位資料轉成ArrayList
	 * 
	 * @return ArrayList 可放入FileCom的outputTxt
	 */
	@Override
	public ArrayList<String> toFile() {
		ArrayList<String> result = new ArrayList<>();

		// 組頁首
		for (int i = 0; i < headerCounts; i++) {
			// 頁首的欄位組合
			// 無首筆
		}

		// 組明細
		for (OccursList occursList : occursList) {

			// 明細資料的單筆資料的欄位組合
//			SalesId			員工Id	X	10	
//			FireInsuMonth	火險年月	X	6	
//			ColumnA			A		X	1	0
//			TotCommA		總佣金	X	9	
//			TotCommB		總佣金	X	9	
//			ColumnB			B		X	1	0
//			ColumnC			C		X	1	0
//			ColumnD			D		X	1	0
//			ColumnE			E		X	1	0
//			Count			筆數		X	5	
//			TotFee			總保費	X	9	
//			TotCommC		總佣金	X	9	
//			ColumnF			F		X	1	0
//			ColumnG			G		X	1	0
//			ColumnH			H		X	1	0

			String thisLine = occursList.get("SalesId") + "," + occursList.get("FireInsuMonth") + ","
					+ occursList.get("ColumnA") + "," + occursList.get("TotCommA") + "," + occursList.get("TotCommB")
					+ "," + occursList.get("ColumnB") + "," + occursList.get("ColumnC") + ","
					+ occursList.get("ColumnD") + "," + occursList.get("ColumnE") + "," + occursList.get("Count") + ","
					+ occursList.get("TotFee") + "," + occursList.get("TotCommC") + "," + occursList.get("ColumnF")
					+ "," + occursList.get("ColumnG") + "," + occursList.get("ColumnH");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//			1	資料別		0-1		X(1)	固定值為2	

//			String thisLine = "" + this.get("FootNoteB");// 11 保留欄

//			result.add(thisLine);
		}
		return result;
	}

	public ArrayList<OccursList> getOccursList() {
		return occursList;
	}

	public void setOccursList(ArrayList<OccursList> occursList) {
		this.occursList = occursList;
	}

}
