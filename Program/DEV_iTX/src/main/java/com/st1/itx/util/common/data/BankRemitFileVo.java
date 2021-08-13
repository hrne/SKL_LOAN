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
@Component("BankRemitFileVo")
@Scope("prototype")
public class BankRemitFileVo extends FileVo {

	private static final long serialVersionUID = -346902158071682791L;
	// 設定首筆筆數
	private final int headerCounts = 0;
	// 設定尾筆筆數
	private final int footerCounts = 0;

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

//		目前無此交易

		// 總行數
		int LastIndex = lineList.size() - 1;

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

//				PK OccCustNo OccEntryDate OccUnknowC(前兩碼判斷還款類別) OccRepayAmt(第一碼為負號) 
				occursList.putParam("OccCustNo", split(thisLine, 150, 157));
				occursList.putParam("OccEntryDate", split(thisLine, 134, 142));
				occursList.putParam("OccUnknowC", split(thisLine, 37, 39));
				occursList.putParam("OccRepayAmt", split(thisLine, 60, 69));

//				TK OccReturnCode OccAcctAmt
				occursList.putParam("OccReturnCode", split(thisLine, 35, 37));
				occursList.putParam("OccAcctAmt", split(thisLine, 125, 134));

				this.occursList.add(occursList);
			}

			// 頁尾
//			Footer
//			無頁尾

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
//				this.put("FootTotCnt", thisLine.substring(19, 26));
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
//			DataSeq		序號			4	0	4
//			AcctNo		帳號			X	14	4	18
//			Amount		金額			X	13	18	31
//			UnitCode	解付單位代號	X	7	31	38
//			RemitName	代償專戶		X	59	38	97
//			ColumnA	新光人壽保險股份有限公司─放款服務課	X	35	97	132
//			ColumnB		space		X	59	132	191
//			ColumnC		00174		X	5	191	196
//			RemitDate	匯款日期		X	8	196	204
//			BatchNo		批號			X	2	204	206

			String thisLine = "" + occursList.get("DataSeq") + occursList.get("AcctNo") + occursList.get("Amount") + occursList.get("UnitCode") + occursList.get("RemitName")
					+ occursList.get("ColumnA") + occursList.get("ColumnB") + occursList.get("ColumnC") + occursList.get("RemitDate") + occursList.get("BatchNo");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
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
				throw new LogicException("XXXXX", "來源檔字元長度不足");
			} else if (startIndex < 0 || resultLength == 0) {
				throw new LogicException("XXXXX", "BankRmtfFileVo程式有誤");
			}
		}

		return result;
	}
}
