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
@Component("AchDeductFileVo")
@Scope("prototype")
public class AchDeductFileVo extends FileVo {

	private static final long serialVersionUID = -2306301316157932699L;
	// 設定首筆筆數
	private final int headerCounts = 1;
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
		int LastIndex = lineList.size() - 1;

		int i = 0;
		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 頁首
			if (i < headerCounts) {

				if (thisLine.length() < 160) {
					throw new LogicException("E0014", "資料長度與規格不相符");
				}

				// 設定頁首欄位的擷取位置
//				1	HeadIndex		首錄別		X	3	0	3	BOF
//				2	HeadDataCode	資料代號		A	6	3	9	ACHR01
//				3	HeadProcessDate	處理日		A	8	9	17	民國YYYYMMDD
//				4	HeadProcessTime	處理時間		A	6	17	23	系統時間 HHMMSS
//				5	HeadSubmitUnit	發送單位代號	A	7	23	30	9990250
//				6	HeadReceiveUnit	接收單位		A	7	30	37	1030000
//				7	HeadNote		備用			X	123	37	160	
				this.put("HeadIndex", thisLine.substring(0, 3));
				this.put("HeadDataCode", thisLine.substring(3, 9));
				this.put("HeadProcessDate", thisLine.substring(9, 17));
				this.put("HeadProcessTime", thisLine.substring(17, 23));
				this.put("HeadSubmitUnit", thisLine.substring(23, 30));
				this.put("HeadReceiveUnit", thisLine.substring(30, 37));
				this.put("HeadNote", thisLine.substring(37, 160));

			}

			// 明細
			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
				OccursList occursList = new OccursList();

				if (thisLine.length() < 160) {
					throw new LogicException("E0014", "資料長度與規格不相符");
				}

				// 設定明細欄位的擷取位置
//				1	OccTransType		交易型態		X	1	0	1	R:退件
//				2	OccTransCate		交易類別		X	2	1	3	SD:代收案件
//				3	OccTransCode		交易代號		X	3	3	6	801
//				4	OccTransSeq			交易序號		A	6	6	12	從1開始，右靠左補0
//				5	OccSenderNo			提出行代號		A	7	12	19	8120012 台新 0060567 合作金庫 1030019 新光
//				6	OccSenderAcct		發動者帳號		X	14	19	33	扣款人帳號，右靠左補0
//				7	OccWdBankNo			提回行代號		A	7	33	40	1030116
//				8	OccCustAcctNo		收受者帳號		X	14	40	54	00116101001006
//				9	OccRepayAmt			金額			A	10	54	64	
//				10	OccReturnCode		退件理由代號	A	2	64	66	*** --------------->
//				11	OccIndicator		提示交換次序	X	1	66	67	B
//				12	OccSenderId			發動者統一編號	X	10	67	77	03458902 左靠右補空白
//				13	OccCustId			收受者統一編號	X	10	77	87	客戶檔編號對應之統一編號 左靠右補空白
//				14	OccCompanyCode		上市上櫃公司代號	X	6	87	93	2888 左靠右補空白
//				15	OccOTransDate		原提示交易日期	A	8	93	101	原提示行提出交易日期
//				16	OccOTransSeq		原提示交易序號	A	6	101	107	原提示行提出交易序號
//				17	OccOTransOrder		原提示交易次序	X	1	107	108	原提示行提出交易次序
//				18	OccCustSeq			用戶編號		X	20	108	128	戶號 or 授權編號 左靠右補空白
//				19	OccSenderRemarker	發動者專區		X	20	128	148	戶號7+額度3+入帳扣款別1+繳息迄日8 左靠右補空白
//				20	OccAbstract			存摺摘要		X	10	148	158	801 左靠右補空白
//				21	OccNote				備用			X	2	158	160	空白

//				PK
//				金額、發動者專區、returnCode為空者
//				OccRepayAmt			金額
//				OccSenderRemarker	發動者專區
//				OccReturnCode				
				occursList.putParam("OccSenderNo", split(thisLine, 12, 19));
				occursList.putParam("OccRepayAmt", split(thisLine, 54, 64));
				occursList.putParam("OccSenderRemarker", split(thisLine, 128, 148));
				occursList.putParam("OccReturnCode", split(thisLine, 64, 66));

				this.occursList.add(occursList);
			}

			// 頁尾
//			Footer
//			1	FootIndex			尾錄別		X	3	0	3	EOF
//			2	FootDataCode		資料代號		X	6	3	9	ACHR01
//			3	FootProgressDate	處理日期		A	8	9	17	製作媒體日 民國YYYYMMDD
//			4	FootSenderUnit		發送單位代號	A	7	17	24	9990250
//			5	FootReceiveUnit		接收單位代號	A	7	24	31	1030000
//			6	FootTotCnt			總筆數		A	8	31	39	靠右左補0
//			7	FootTotAmt			總金額		A	16	39	55	靠右左補0
//			8	FootYesterday		前一營業日日期	A	8	55	63	民國YYYYMMDD
//			9	FootNote			備用			X	97	63	160	空白

			if (i > (LastIndex - footerCounts)) {
				if (thisLine.length() < 160) {
					throw new LogicException("E0014", "尾錄長度與規格不相符，長度未達160");
				}
				// 設定頁尾欄位的擷取位置s
				this.put("FootTotCnt", thisLine.substring(31, 39));
				this.put("FootTotAmt", thisLine.substring(39, 55));
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
//			1	HeadIndex		首錄別		X	3	0	3	BOF
//			2	HeadDataCode	資料代號		A	6	3	9	ACHR01
//			3	HeadProcessDate	處理日		A	8	9	17	民國YYYYMMDD
//			4	HeadProcessTime	處理時間		A	6	17	23	系統時間 HHMMSS
//			5	HeadSubmitUnit	發送單位代號	A	7	23	30	9990250
//			6	HeadReceiveUnit	接收單位		A	7	30	37	1030000
//			7	HeadNote		備用			X	123	37	160	

			String thisLine = "" + this.get("HeadIndex") + this.get("HeadDataCode") + this.get("HeadProcessDate")
					+ this.get("HeadProcessTime") + this.get("HeadSubmitUnit") + this.get("HeadReceiveUnit")
					+ this.get("HeadNote");
			result.add(thisLine);
		}

		// 組明細
		for (OccursList occursList : occursList) {
			// 明細資料的單筆資料的欄位組合
//			1	OccTransType		交易型態		X	1	0	1	N:提示
//			2	OccTransCate		交易類別		X	2	1	3	SD
//			3	OccTransCode		交易代號		X	3	3	6	801
//			4	OccTransSeq			交易序號		A	6	6	12	
//			5	OccSenderNo			提出行代號		A	7	12	19	1030116
//			6	OccSenderAcct		發動者帳號		X	14	19	33	00116101001006
//			7	OccWdBankNo			提回行代號		A	7	33	40	8120012 台新 0060567 合作金庫 1030019 新光
//			8	OccCustAcctNo		收受者帳號		X	14	40	54	
//			9	OccRepayAmt			金額			A	10	54	64	
//			10	OccReturnCode		退件理由代號	A	2	64	66	空白
//			11	OccIndicator		提示交換次序	X	1	66	67	B
//			12	OccSenderId			發動者統一編號	X	10	67	77	03458902 左靠右補空白
//			13	OccCustId			收受者統一編號	X	10	77	87	客戶檔編號對應之統一編號 左靠右補空白
//			14	OccCompanyCode		上市上櫃公司代號	X	6	87	93	2888 左靠右補空白
//			15	OccOTransDate		原提示交易日期	A	8	93	101	空白
//			16	OccOTransSeq		原提示交易序號	A	6	101	107	空白
//			17	OccOTransOrder		原提示交易次序	X	1	107	108	空白
//			18	OccCustSeq			用戶編號		X	20	108	128	戶號 or 授權編號 左靠右補空白
//			19	OccSenderRemarker	發動者專區		X	20	128	148	戶號7+額度3+入帳扣款別1+繳息迄日8 左靠右補空白
//			20	OccAbstract			存摺摘要		X	10	148	158	801 左靠右補空白
//			21	OccNote				備用			X	2	158	160	空白

			String thisLine = "" + occursList.get("OccTransType") + occursList.get("OccTransCate")
					+ occursList.get("OccTransCode") + occursList.get("OccTransSeq") + occursList.get("OccSenderNo")
					+ occursList.get("OccSenderAcct") + occursList.get("OccWdBankNo") + occursList.get("OccCustAcctNo")
					+ occursList.get("OccRepayAmt") + occursList.get("OccReturnCode") + occursList.get("OccIndicator")
					+ occursList.get("OccSenderId") + occursList.get("OccCustId") + occursList.get("OccCompanyCode")
					+ occursList.get("OccOTransDate") + occursList.get("OccOTransSeq")
					+ occursList.get("OccOTransOrder") + occursList.get("OccCustSeq")
					+ occursList.get("OccSenderRemarker") + occursList.get("OccAbstract") + occursList.get("OccNote");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//			1	FootIndex			尾錄別		X	3	0	3	EOF
//			2	FootDataCode		資料代號		X	6	3	9	ACHP01
//			3	FootProgressDate	處理日期		A	8	9	17	製作媒體日 民國YYYYMMDD
//			4	FootSenderUnit		發送單位代號	A	7	17	24	1030000
//			5	FootReceiveUnit		接收單位代號	A	7	24	31	9990250
//			6	FootTotCnt			總筆數		A	8	31	39	靠右左補0
//			7	FootTotAmt			總金額		A	16	39	55	靠右左補0
//			8	FootNote			備用			X	105	55	160	空白

			String thisLine = "" + this.get("FootIndex") + this.get("FootDataCode") + this.get("FootProgressDate")
					+ this.get("FootSenderUnit") + this.get("FootReceiveUnit") + this.get("FootTotCnt")
					+ this.get("FootTotAmt") + this.get("FootNote");

			result.add(thisLine);
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
