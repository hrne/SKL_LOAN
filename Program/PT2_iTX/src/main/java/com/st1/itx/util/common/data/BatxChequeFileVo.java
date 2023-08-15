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
@Component("BatxChequeFileVo")
@Scope("prototype")
public class BatxChequeFileVo extends FileVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1750414087018588344L;
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

//				1	ChequeAcct	支票銀行帳號	X	9	0	9	
//				2	ChequeNo	支票號碼	    X	7	9	16	
//				3	ReturnCode	回傳碼	    X	1	16	17	"H.成功 C.抽/退票"
//				4	ChequeDateA	異動日期	    A	7	17	24	YYYMMDD
//				5	ChequeAmt	金額	        A	10	24	34	
//				6	ChequeDateB	到期日期	    A	7	34	41	YYYMMDD
//				7	EntryDate	入帳日期	    A	7	41	48	YYYMMDD 若為抽/退票，此欄位為空值

				if (thisLine.length() <= 47) {
					throw new LogicException("E0014", "來源檔字元長度不足");
				}

//				PK 支票號碼 支票帳號
				occursList.putParam("ChequeAcct", split(thisLine, 0, 9));
				occursList.putParam("ChequeNo", split(thisLine, 9, 16));
				occursList.putParam("ReturnCode", split(thisLine, 16, 17));
				occursList.putParam("ChequeDateA", split(thisLine, 17, 24));
				occursList.putParam("ChequeAmt", split(thisLine, 24, 34));
				occursList.putParam("ChequeDateB", split(thisLine, 34, 41));
				occursList.putParam("EntryDate", split(thisLine, 41, 48));

//				TK OccReturnCode OccAcctAmt

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
//			1.產生票據媒體檔
//			ChequeNo	支票號碼		X	7	0	7	
//			ChequeDate	支票日期		A	7	7	14	YYYMMDD
//			BankCode	銀行代號		X	7	14	21	
//			ChequeAcct	支票銀行帳號	X	9	21	30	
//			ChequeAmt	金額			A	10	30	40	
//			MediaDate	輸入日期		A	7	40	47	YYYMMDD 轉檔的日期
//			Teller		作業者		X	8	47	55	帶轉檔作業者(員編六碼+01)
//			UnitCode	部門代號		X	6	55	61	部門代號=來源單位
//			SrcCode		支票來源碼2		X	1	61	62	* ----->
//			SrcUnit		來源單位		X	6	62	68	
//			RecipeNo	傳票號碼		X	10	68	78	
//			EntryDate	作帳日期		A	7	78	85	YYYMMDD
//			CustNo		相關資訊		X	10	85	95	放款戶戶號

			String thisLine = occursList.get("ChequeNo") + "," + occursList.get("ChequeDate") + "," + occursList.get("BankCode") + "," + occursList.get("ChequeAcct") + ","
					+ occursList.get("ChequeAmt") + "," + occursList.get("MediaDate") + "," + occursList.get("Teller") + "," + occursList.get("UnitCode") + "," + occursList.get("SrcCode") + ","
					+ occursList.get("SrcUnit") + "," + occursList.get("RecipeNo") + "," + occursList.get("EntryDate") + "," + occursList.get("CustNo");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
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
				throw new LogicException("E0014", "BankRmtfFileVo程式有誤");
			}
		}

		return result;
	}
}
