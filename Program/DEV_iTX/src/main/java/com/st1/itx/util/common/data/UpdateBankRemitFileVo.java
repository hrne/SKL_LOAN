package com.st1.itx.util.common.data;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Component("UpdateBankRemitFileVo")
@Scope("prototype")
public class UpdateBankRemitFileVo extends FileVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150921249495041870L;
	// 設定首筆筆數
	private final int headerCounts = 0;
	// 設定尾筆筆數
	private final int footerCounts = 1;

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	/*
	 * 從檔案讀取到的資料，依照此方法中的定義，擷取欄位值
	 * 
	 * @param lineList 放入FileCom的intputTxt取得的ArrayList&lt;String&gt;
	 */
	@Autowired
	public Parse parse;

	@Override
	public void setValueFromFile(ArrayList<String> lineList) throws LogicException {

		// 總行數
		int LastIndex = lineList.size();

		int i = 0;
		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 頁首
			if (i < headerCounts) {
				// 設定頁首欄位的擷取位置
				// 無首筆
			}

			// 明細
			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
				OccursList occursList = new OccursList();

// 設定明細欄位的擷取位置
//				1	OccSeq請款序號				0-12    X(12)
//				2	OccNum付款號碼				12-24   X(12)
//				3	OccPayStatus付款狀況碼			24-25   X(1)
//				4	OccPayModifyDate付款狀況異動日		25-32   X(7)
//				5	OccRelNum相關號碼	 			32-47   X(15)
//				6	OccPayName受款人	 	        47-87   X(40)
//				7	OccChequeNum支票號碼	    	    87-96   X(9)
//				8	OccReceiveDate兌領日期	 	        96-103  X(7)
//				9	OccPaymentDate開票日期/匯款日期	 	103-110 X(7)

				if (thisLine.length() < 110) {
					throw new LogicException("E0014", "資料長度與規格不相符");
				}

				occursList.putParam("OccSeq", split(thisLine, 0, 12));
				occursList.putParam("OccNum", split(thisLine, 12, 24));
				occursList.putParam("OccPayStatus", split(thisLine, 24, 25));
				occursList.putParam("OccPayModifyDate", split(thisLine, 25, 32));
				occursList.putParam("OccRelNum", split(thisLine, 32, 47));
				occursList.putParam("OccPayName", split(thisLine, 47, 87));
				occursList.putParam("OccChequeNum", split(thisLine, 87, 96));
				occursList.putParam("OccReceiveDate", split(thisLine, 96, 103));
				occursList.putParam("OccPaymentDate", split(thisLine, 103, 110));

				this.occursList.add(occursList);
			}

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
				// 無頁尾
			}

			i++;
		}

	}

	/*
	 * 把目前FileVo內的欄位資料轉成ArrayList&lt;String&gt;
	 * 
	 * @return ArrayList&lt;String&gt; 可放入FileCom的outputTxt
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
//			1	OccSeq請款序號				0-12    X(12)
//			2	OccNum付款號碼				12-24   X(12)
//			3	OccPayStatus付款狀況碼			24-25   X(1)
//			4	OccPayModifyDate付款狀況異動日		25-32   X(7)
//			5	OccRelNum相關號碼	 			32-47   X(15)
//			6	OccPayName受款人	 	        47-87   X(40)
//			7	OccChequeNum支票號碼	    	    87-96   X(9)
//			8	OccReceiveDate兌領日期	 	        96-103  X(7)
//			9	OccPaymentDate開票日期/匯款日期	 	103-110 X(7)

			String thisLine = "" + occursList.get("OccSeq") + occursList.get("OccNum") + occursList.get("OccPayStatus") + occursList.get("OccPayModifyDate") + occursList.get("OccRelNum")
					+ occursList.get("OccPayName") + occursList.get("OccChequeNum") + occursList.get("OccReceiveDate") + occursList.get("OccPaymentDate");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
			// 無頁尾
		}
		return result;
	}

	public ArrayList<OccursList> getOccursList() {
		return occursList;
	}

	public void setOccursList(ArrayList<OccursList> occursList) {
		this.occursList = occursList;
	}

	private String split(String line, int startIndex, int endIndex) throws LogicException {

		byte[] input = line.getBytes();

		int inputLength = input.length;

		int resultLength = endIndex - startIndex;

		String result = "";

		if (startIndex >= 0 && resultLength > 0 && endIndex <= inputLength) {
			byte[] resultBytes = new byte[resultLength];
			System.arraycopy(input, startIndex, resultBytes, 0, resultLength);
			result = new String(resultBytes);
		} else {
			if (endIndex > inputLength) {
				throw new LogicException("E0014", "來源檔字元長度不足");
			} else if (startIndex < 0 || resultLength == 0) {
				throw new LogicException("E0014", "UpdateBankRemitFileVo程式有誤");
			}
		}

		return result;
	}
}
