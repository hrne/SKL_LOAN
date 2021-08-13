package com.st1.itx.util.common.data;

import java.io.UnsupportedEncodingException;
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
@Component("BankRmtfFileVo")
@Scope("prototype")
public class BankRmtfFileVo extends FileVo {

	private static final long serialVersionUID = 1887326441090463720L;
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

				byte[] input = null;
				try {
					input = thisLine.getBytes("Big5");
				} catch (UnsupportedEncodingException e) {
					throw new LogicException("E0014", "來源檔字碼非Big5 ");
				}

				if (input.length < 115) {
					throw new LogicException("E0014", "資料長度（" + input.length + "）與規格不相符（115）");
				}

				// 設定明細欄位的擷取位置
//				1	OccAcctNo		存摺帳號		0-13	A.13	
//				2 	OccEntryDate	入帳日期		13-21	A.8		西元年月日YYYYMMDD
//				3	OccRemark		摘要			21-31	X.10	中文
//				4	OccVirAcctNo	虛擬帳號		31-45	A.14	
//				5	OccWithDrawAmt	提款			45-58	A.13	含兩位角分位
//				6	OccDepositAmt	存款			58-71	A.13	含兩位角分位( p表負數)
//				8	OccBalance		結餘			71-84	A.13	含兩位角分位
//				9	OccBankCode		匯款銀行代碼	84-91	A.7	
//				10	OccNoteCode		摘要代號		91-95	A.4	
//				11	OccTrader		交易人資料		95-115	X.20	中文

//				
				occursList.putParam("OccAcctNo", split5(thisLine, 0, 13));
				occursList.putParam("OccEntryDate", split5(thisLine, 13, 21));
				occursList.putParam("OccRemark", split5(thisLine, 21, 31));
				occursList.putParam("OccVirAcctNo", split5(thisLine, 31, 45));
				occursList.putParam("OccWithDrawAmt", split5(thisLine, 45, 58));
				occursList.putParam("OccDepositAmt", split5(thisLine, 58, 71));
				occursList.putParam("OccBalance", split5(thisLine, 71, 84));
				occursList.putParam("OccBankCode", split5(thisLine, 84, 91));
				occursList.putParam("OccNoteCode", split5(thisLine, 91, 95));
				occursList.putParam("OccTrader", split5(thisLine, 95, 115));

				occursList.putParam("CheckFlag", "1");

//				occursList.putParam("OccAcctNo", thisLine.substring(0, 13));
//              occursList.putParam("OccEntryDate", thisLine.substring(13, 21));
//				occursList.putParam("OccRemark", thisLine.substring(21, 27));
//				occursList.putParam("OccVisAcctNo", thisLine.substring(27, 41));
//				occursList.putParam("OccWithDrawAmt", thisLine.substring(41, 54));
//				occursList.putParam("OccDepositAmt", thisLine.substring(54, 67));
//				occursList.putParam("OccBalance", thisLine.substring(67, 80));
//				occursList.putParam("OccBankCode", thisLine.substring(80, 87));
//				occursList.putParam("OccNoteCode", thisLine.substring(87, 91));
//				occursList.putParam("OccTrader", thisLine.substring(91));

				this.occursList.add(occursList);
			}

			// 頁尾
			// 無頁尾

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
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

//		 組明細
//		for (OccursList occursList : occursList) {
//			 明細資料的單筆資料的欄位組合
//		}
//
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
				throw new LogicException("XXXXX", "來源檔字元長度不足 inputLength : " + inputLength + ", endIndex : " + endIndex);
			} else if (startIndex < 0 || resultLength == 0) {
				throw new LogicException("XXXXX", "BankRmtfFileVo程式有誤");
			}
		}

		return result;
	}

	private String split5(String line, int startIndex, int endIndex) throws LogicException {

		byte[] input = null;
		try {
			input = line.getBytes("Big5");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new LogicException("XXXXX", "[BankRmtfFileVo]來源檔字碼非Big5 ");
		}

		int inputLength = input.length;

		int resultLength = endIndex - startIndex;

		String result = "";

		if (startIndex >= 0 && resultLength > 0 && endIndex <= inputLength) {
			byte[] resultBytes = new byte[resultLength];
			System.arraycopy(input, startIndex, resultBytes, 0, resultLength);
			try {
				String string5 = new String(resultBytes, "Big5");
				// 將big5 轉 utf-8
				byte[] utf8 = string5.getBytes("UTF-8");
				result = new String(utf8, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				throw new LogicException("E0014", "[BankRmtfFileVo]轉換Big5字碼錯誤");
			}
		} else {
			if (endIndex > inputLength) {
				throw new LogicException("E0014", "[BankRmtfFileVo]來源檔字元長度不足 inputLength : " + inputLength + ", endIndex : " + endIndex);
			} else if (startIndex < 0 || resultLength == 0) {
				throw new LogicException("E0014", "[BankRmtfFileVo]程式有誤");
			}
		}

		return result;
	}

}
