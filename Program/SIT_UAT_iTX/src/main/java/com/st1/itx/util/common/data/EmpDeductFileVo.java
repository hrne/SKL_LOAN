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
@Component("EmpDeductFileVo")
@Scope("prototype")
public class EmpDeductFileVo extends FileVo {

	private static final long serialVersionUID = 3989650146055767948L;
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

				if (input.length < 157) {
					throw new LogicException("E0014", "資料長度與規格不相符");
				}

				// 設定明細欄位的擷取位置
//				1	OccYearMonthA	業績年月		X	7	0	7	西元年/月YYYY/MM
//				2	OccUnit			單位別		X	6	7	13	10H400
//				3	OccUnknowA		?			A	10	13	23	0000000001?
//				4	OccUnknowB		?			A	2	23	25	35?
//				5	OccCustId		身分證統一編號	X	10	25	35	
//				6	OccReturnCode	失敗原因		A	2	35	37	16: 扣款失敗17: 扣款不足…..
//				7	OccUnknowC		?			X	7	37	44	XH 房貸、92 火險...........
//				8	OccUnknowD		?			X	15	44	59	
//				9	OccRepayAmt		扣款金額		A	10	59	69	0(負號打頭)
//				10	OccUnknowE		?			X	40	69	109	
//				11	OccUnknowF		?			X	1	109	110	Y
//				12	OccUnknowA		?			X	14	110	124	?
//				13	OccAcctAmt		實扣金額		A	10	124	134	0(負號打頭)
//				14	OccEntryDate	入帳日期		A	8	134	142	西元年月日YYYYMMDD
//				15	OccYearMonthB	業績年月		A	6	142	148	西元年月YYYYMM
//				16	OccProcessType	流程別		A	1	148	149	
//				17	OccRepayCode	扣款代碼		A	1	149	150	
//				18	OccCustNo		戶號			A	7	150	157	
//				19	OccAcctCode		科目			A	3	157	160	
//				20	OccUnknowG		?			X	42	160	202	

//				PK OccCustNo OccEntryDate OccUnknowC(前兩碼判斷還款類別) OccRepayAmt(第一碼為負號) 
				occursList.putParam("OccCustNo", split5(thisLine, 150, 157));
				occursList.putParam("OccEntryDate", split5(thisLine, 134, 142));
				occursList.putParam("OccUnknowC", split5(thisLine, 37, 39));
				occursList.putParam("OccRepayAmt", split5(thisLine, 60, 69));

//				TK OccReturnCode OccAcctAmt
				occursList.putParam("OccReturnCode", split5(thisLine, 35, 37));
				occursList.putParam("OccAcctAmt", split5(thisLine, 125, 134));

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
//			1	OccYearMonthA	業績年月		X	7	0	7	西元年/月YYYY/MM
//			2	OccUnit			單位別		X	6	7	13	10H400
//			3	OccUnknowA		?			A	10	13	23	0000000001?
//			4	OccCustId		身分證統一編號	X	10	23	33	
//			5	OccUnknowB		?			X	11	33	44	XH 房貸、92 火險………
//			6	OccUnknowC		?			X	11	44	55	空白
//			7	OccRepayAmt		扣款金額		A	10	55	65	0
//			8	OccUnknowD		?			X	40	65	105	空白
//			9	OccUnknowE		?			X	1	105	106	Y
//			10	OccEntryDate	入帳日期		A	8	106	114	西元年月日YYYYMMDD
//			11	OccYearMonthB	業績年月		A	6	114	120	西元年月YYYYMM
//			12	OccProcessType	流程別		A	1	120	121	
//			13	OccRepayCode	扣款代碼		A	1	121	122	
//			14	OccCustNo		戶號			A	7	122	129	
//			15	OccAcctCode		科目			A	3	129	132	
//			16	OccUnknowF		?			X	42	132	174	空白

			String thisLine = "" + occursList.get("OccYearMonthA") + occursList.get("OccUnit") + occursList.get("OccUnknowA") + occursList.get("OccCustId") + occursList.get("OccUnknowB")
					+ occursList.get("OccUnknowC") + occursList.get("OccRepayAmt") + occursList.get("OccUnknowD") + occursList.get("OccUnknowE") + occursList.get("OccEntryDate")
					+ occursList.get("OccYearMonthB") + occursList.get("OccProcessType") + occursList.get("OccRepayCode") + occursList.get("OccCustNo") + occursList.get("OccAcctCode")
					+ occursList.get("OccUnknowF");
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
