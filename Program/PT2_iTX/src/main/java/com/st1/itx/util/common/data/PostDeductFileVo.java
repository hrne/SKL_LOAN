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
@Component("PostDeductFileVo")
@Scope("prototype")
public class PostDeductFileVo extends FileVo {

	private static final long serialVersionUID = -7983780509714917148L;
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
//				1	OccIndex		資料別		0-1		9(1)	固定值為1
//				2	OccDepCode		帳戶別		1-2		X(1)	存簿：P   劃撥：G
//				3	OccOrgCode		委託機構代號	2-5		X(3)	大寫英數字
//				4	OccDistCode		區處代號		5-9		X(4)	空白；另申請區處代號者，依申請
//				5	OccTxDate		轉帳日期		9-16	9(7)	民國年月日YYYMMDD
//				6	OccStampInd		核印註記		16-17	X(1)	固定值為S
//				7	OccPostNote1	郵局使用欄1		17-19	X(2)	空白
//				8	OccRepayAcctNo	儲金帳號		19-33	9(14)	存簿：局帳號計14碼 劃撥：000000+8碼帳號
//				9	OccKeepColA		保留欄		33-43	X(10)	空白
//				10	OccRepayAmt		繳費金額		43-54	9(11,2)	右靠左補0，含兩位角分位
//				11	OccCustMemo		用戶編號		54-74	X(20)	右靠左補空，大寫英數字，不得填寫中文 (扣款人ID+郵局存款別(POSCDE)+戶號)
//						OccCustId				54-64			扣款人ID
//						OccPostDepCode			64-65			郵局存款別(POSCDE)
//						OccCustNo				65-72			戶號
//						OccRepayAcctNoSeq		72-74			預計為帳號碼
//				12	OccPrtCustNo	列印用戶編號記號	74-75	X(1)	"本欄為1時：存簿存摺列印用戶編號(65-74)劃撥詳情單列印用戶編號(65-74)"
//				13	OccPostNote2	郵局使用欄2		75-76	X(1)	空白
//				14	OccMaskFlag		隱碼註記		76-77	X(1)	本欄為1時，存簿存摺或劃撥詳情單列印之用戶編號，第68-71欄位資料採隱碼處理
//				15	OccChgFlag		變更存簿局號記號	77-78	X(1)	初始值為空白，回送“＊”，表示已改局號
//				16	OccReturnCode	狀況代號		78-80	X(2)	初始值為空白，回送資料空白者，為扣費成功；有資料者為退件，請參閱「媒體資料不符代號一覽表」
//				17	OccRepayMonth	繳費月份		80-85	9(5)	民國年月YYYMM
//				18	OccPostNote3	郵局使用欄3		85-90	X(5)	空白
//				19	OccRemark		委託機構使用欄	90-110	X(20)	不得填寫中文 (預計為計息迄日+額度編號+入帳扣款別)
//						OccIntEndDate			90-98			計息迄日
//						OccFacmNo				98-101			額度編號
//						OccRepayType			101-102			入帳扣款別 1:期款2:火險
//				20	OccKeepColB		保留欄		110-120	X(10)	空白
//				OccReturnCode	狀況代號
//				03-已終止代繳 
//				06-凍結警示戶 
//				07-支票專戶 
//				08-帳號錯誤 
//				09-終止戶 
//				10-身分證不符
//				11-轉出戶 
//				12-拒絕往來戶 
//				13-無此編號 
//				14-編號已存在 
//				16-管制帳戶 
//				17-掛失戶 
//				18-異常帳戶 
//				19-編號非英數 
//				91-期限未扣款 
//				98-其他
//				PK 繳費金額 扣款人ID 郵局存款別(POSCDE) 戶號
//				OccRepayAmt			繳費金額				43-54
//				OccCustId			扣款人ID				54-64
//				OccPostDepCode		郵局存款別(POSCDE)		64-65
//				OccCustNo			戶號					65-72
//				OccRepayAcctNoSeq	預計為帳號碼			72-74
//				OccIntEndDate		預計為計息迄日			90-98
//				OccFacmNo			預計為額度編號			98-101
//				OccRepayType		預計為入帳扣款別 1:期款2:火險	101-102
//				OccReturnCode		狀況代號				78-80
//				OccDistCode			區處代號				5-9

				if (thisLine.length() < 110) {
					throw new LogicException("E0014", "資料長度與規格不相符");
				}

				occursList.putParam("OccRepayAmt", split(thisLine, 43, 54));

//				occursList.putParam("OccCustId", split(thisLine,54, 64));
//				occursList.putParam("OccPostDepCode", split(thisLine,64, 65));
//				occursList.putParam("OccCustNo", split(thisLine,65, 72));
//				occursList.putParam("OccRepayAcctNoSeq", split(thisLine,72, 74));

				occursList.putParam("OccCustMemo", split(thisLine, 54, 74));
				occursList.putParam("OccCustId", split(thisLine, 56, 66));
				occursList.putParam("OccPostDepCode", split(thisLine, 66, 67));
				occursList.putParam("OccCustNo", split(thisLine, 67, 74));

				occursList.putParam("OccIntEndDate", split(thisLine, 90, 98));
				occursList.putParam("OccFacmNo", split(thisLine, 98, 101));
				occursList.putParam("OccRepayType", split(thisLine, 101, 102));
				occursList.putParam("OccReturnCode", split(thisLine, 78, 80));

				occursList.putParam("OccRemark", split(thisLine, 90, 110));

				occursList.putParam("OccDistCode", split(thisLine, 5, 9));
				occursList.putParam("OccOrgCode", split(thisLine, 2, 5));

				occursList.putParam("OccTxDate", split(thisLine, 9, 16));

				this.occursList.add(occursList);
			}

			// 頁尾
//			Footer
//			1	資料別		0-1		9(1)	固定值為2
//			2	帳戶別		1-2		X(1)	空白
//			3	委託機構代號	2-5		X(3)	同明細
//			4	區處代號		5-9		X(4)	同明細
//			5	轉帳日期		9-16	9(7)	同明細
//			6	保留欄		16-19	X(3)	空白
//			7	總件數		19-26	9(7)	右靠左補0
//			8	總金額		26-39	9(13,2)	右靠左補0，含兩位角分位
//			9	保留欄		39-55	X(16)	空白
//			10	成功筆數		55-62	9(7)	初始值為0，回送時使用
//			11	成功金額		62-75	9(13,2)	初始值為0，回送時使用
//			12	保留欄		75-120	X(45)	空白

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
				this.put("FootTotCnt", thisLine.substring(19, 26));
				this.put("FootTotAmt", thisLine.substring(26, 39));
				this.put("FootSucsCnt", thisLine.substring(55, 62));
				this.put("FootSucsAmt", thisLine.substring(62, 75));
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
//			1	OccIndex		資料別		0-1		9(1)	固定值為1
//			2	OccDepCode		帳戶別		1-2		X(1)	存簿：P   劃撥：G
//			3	OccOrgCode		委託機構代號	2-5		X(3)	大寫英數字
//			4	OccDistCode		區處代號		5-9		X(4)	空白；另申請區處代號者，依申請
//			5	OccTxDate		轉帳日期		9-16	9(7)	民國年月日YYYMMDD
//			6	OccStampInd		核印註記		16-17	X(1)	固定值為S
//			7	OccPostNote1	郵局使用欄1		17-19	X(2)	空白
//			8	OccRepayAcctNo	儲金帳號		19-33	9(14)	存簿：局帳號計14碼 劃撥：000000+8碼帳號
//			9	OccKeepColA		保留欄		33-43	X(10)	空白
//			10	OccRepayAmt		繳費金額		43-54	9(11,2)	右靠左補0，含兩位角分位
//			11	OccCustMemo		用戶編號		54-74	X(20)	右靠左補空，大寫英數字，不得填寫中文 (扣款人ID+郵局存款別(POSCDE)+戶號)
//					OccCustId				54-64			扣款人ID
//					OccPostDepCode			64-65			郵局存款別(POSCDE)
//					OccCustNo				65-72			戶號
//					OccRepayAcctNoSeq		72-74			預計為帳號碼
//			12	OccPrtCustNo	列印用戶編號記號	74-75	X(1)	"本欄為1時：存簿存摺列印用戶編號(65-74)劃撥詳情單列印用戶編號(65-74)"
//			13	OccPostNote2	郵局使用欄2		75-76	X(1)	空白
//			14	OccMaskFlag		隱碼註記		76-77	X(1)	本欄為1時，存簿存摺或劃撥詳情單列印之用戶編號，第68-71欄位資料採隱碼處理
//			15	OccChgFlag		變更存簿局號記號	77-78	X(1)	初始值為空白，回送“＊”，表示已改局號
//			16	OccReturnCode	狀況代號		78-80	X(2)	初始值為空白，回送資料空白者，為扣費成功；有資料者為退件，請參閱「媒體資料不符代號一覽表」
//			17	OccRepayMonth	繳費月份		80-85	9(5)	民國年月YYYMM
//			18	OccPostNote3	郵局使用欄3		85-90	X(5)	空白
//			19	OccRemark		委託機構使用欄	90-110	X(20)	不得填寫中文 (預計為計息迄日+額度編號+入帳扣款別)
//					OccIntEndDate			90-98			計息迄日
//					OccFacmNo				98-101			額度編號
//					OccRepayType			101-102			入帳扣款別 1:期款2:火險
//			20	OccKeepColB		保留欄		110-120	X(10)	空白

			String thisLine = "" + occursList.get("OccIndex") + occursList.get("OccDepCode") + occursList.get("OccOrgCode") + occursList.get("OccDistCode") + occursList.get("OccTxDate")
					+ occursList.get("OccStampInd") + occursList.get("OccPostNote1") + occursList.get("OccRepayAcctNo") + occursList.get("OccKeepColA") + occursList.get("OccRepayAmt")
					+ occursList.get("OccCustMemo") + occursList.get("OccPrtCustNo") + occursList.get("OccPostNote2") + occursList.get("OccMaskFlag") + occursList.get("OccChgFlag")
					+ occursList.get("OccReturnCode") + occursList.get("OccRepayMonth") + occursList.get("OccPostNote3") + occursList.get("OccRemark") + occursList.get("OccKeepColB");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//			1	FootIndex		資料別		0-1		9(1)	固定值為2
//			2	FootDepCode		帳戶別		1-2		X(1)	空白
//			3	FootOrgCode		委託機構代號	2-5		X(3)	同明細
//			4	FootDistCode	區處代號		5-9		X(4)	同明細
//			5	FootTxDate		轉帳日期		9-16	9(7)	同明細
//			6	FootKeepColA	保留欄		16-19	X(3)	空白
//			7	FootTotCnt		總件數		19-26	9(7)	右靠左補0
//			8	FootTotAmt		總金額		26-39	9(13,2)	右靠左補0，含兩位角分位
//			9	FootKeepColB	保留欄		39-55	X(16)	空白
//			10	FootSucsCnt		成功筆數		55-62	9(7)	初始值為0，回送時使用
//			11	FootSucsAmt		成功金額		62-75	9(13,2)	初始值為0，回送時使用
//			12	FootKeepColC	保留欄		75-120	X(45)	空白

			String thisLine = "" + this.get("FootIndex") + this.get("FootDepCode") + this.get("FootOrgCode") + this.get("FootDistCode") + this.get("FootTxDate") + this.get("FootKeepColA")
					+ this.get("FootTotCnt") + this.get("FootTotAmt") + this.get("FootKeepColB") + this.get("FootSucsCnt") + this.get("FootSucsAmt") + this.get("FootKeepColC");
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
