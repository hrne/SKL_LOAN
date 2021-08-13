package com.st1.itx.util.common.data;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.OccursList;

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
//				經紀人代號  
//				保單號碼    
//				批號        
//				險別        
//				簽單日期    
//				被保險人    
//				被保險人地址
//				被保險人電話
//				起保日期    
//				到期日期    
//				險種        
//				保費        
//				佣金率      
//				佣金    
//				合計保費
//				合計佣金
//				收件號碼
//				收費日期
//				佣金日期
//				借款人戶號
//				額度編號  

				// 設定明細欄位的擷取位置

				String[] thisColumn = thisLine.split("[|]");

				if (thisColumn.length >= 1 && thisColumn != null) {
					occursList.putParam("IndexCode", thisColumn[0]);
					occursList.putParam("InsuNo", thisColumn[1]);
					occursList.putParam("BatxNo", thisColumn[2]);
					occursList.putParam("InsuKind", thisColumn[3]);
					occursList.putParam("SignDate", thisColumn[4]);
					occursList.putParam("InsuredName", thisColumn[5]);
					occursList.putParam("InsuredAddress", thisColumn[6]);
					occursList.putParam("InsuredTeleNo", thisColumn[7]);
					occursList.putParam("InsuStartDate", thisColumn[8]);
					occursList.putParam("InsuEndDate", thisColumn[9]);
					occursList.putParam("InsuType", thisColumn[10]);
					occursList.putParam("InsuFee", thisColumn[11]);
					occursList.putParam("InsuCommRate", thisColumn[12]);
					occursList.putParam("InsuComm", thisColumn[13]);
					occursList.putParam("TotalFee", thisColumn[14]);
					occursList.putParam("TotalComm", thisColumn[15]);
					occursList.putParam("CaseNo", thisColumn[16]);
					occursList.putParam("AcDate", thisColumn[17]);
					occursList.putParam("CommDate", thisColumn[18]);
					occursList.putParam("CustNo", thisColumn[19]);
					occursList.putParam("FacmNo", thisColumn[20]);
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

			String thisLine = occursList.get("SalesId") + "," + occursList.get("FireInsuMonth") + "," + occursList.get("ColumnA") + "," + occursList.get("TotCommA") + "," + occursList.get("TotCommB")
					+ "," + occursList.get("ColumnB") + "," + occursList.get("ColumnC") + "," + occursList.get("ColumnD") + "," + occursList.get("ColumnE") + "," + occursList.get("Count") + ","
					+ occursList.get("TotFee") + "," + occursList.get("TotCommC") + "," + occursList.get("ColumnF") + "," + occursList.get("ColumnG") + "," + occursList.get("ColumnH");
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
