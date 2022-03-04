package com.st1.itx.util.common.data;

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
@Component("PostAuthFileVo")
@Scope("prototype")
public class PostAuthFileVo extends FileVo {

	private static final long serialVersionUID = -5842366114565250478L;

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
	public void setValueFromFile(ArrayList<String> lineList) {

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
//				1   OccDataClass    資料別		0-1		X(1)	固定值為1	
//				2   OccOrgCode  	委託機構代號	1-4		X(3)	大寫英數字	
//				3   OccNoteA    	保留欄		4-8		X(4)	空白	
//				4   OccMediaDate    媒體產生日期	8-16	X(8)	西元年月日YYYYMMDD	
//				5   OccBatchNo  	批號			16-19	9(3)	固定值為001 	
//				6   OccDataSeq  	流水號		19-25	9(6)	每批自000001序編	
//				7   AuthApplCode    申請代號		25-26	X(1)	"委託機構送件：1：申請2：終止"	"郵局回送「帳戶至郵局辦理終止」檔：3：郵局終止4：誤終止-已回復為申請"
//				8   PostDepCode 	帳戶別		26-27	X(1)	P：存簿    G：劃撥	
//				9   RepayAcct       儲金帳號		27-41	9(14)	存簿：局帳號計14碼   劃撥：000000+8碼帳號	
//				10  OccCustNo   	用戶編號		41-61	X(20)	"右靠左補空，大寫英數字，不得填寫中文由委託機構自行編給其客戶之編號"	
//				11  OccCustId   	統一證號		61-71	X(10)	左靠右補空白	
//				12  AuthErrorCode   狀況代號		71-73	X(2)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//				13  StampCode    	核對註記		73-74	X(1)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//				14  OccNoteB    	保留欄		74-100	X(26)	空白	
//				CustNo = ,AND PostDepCode = ,AND RepayAcct = ,AND AuthCode = 
// 用戶編號=	帳號碼(文字2位)+扣款人ID(10)+郵局存款別(1)+戶號(7)
				occursList.putParam("OccMediaDate", thisLine.substring(8, 16));
				occursList.putParam("OccDataSeq", thisLine.substring(19, 25));
				occursList.putParam("OccOrgCode", thisLine.substring(1, 4));
//				AuthApplCode		申請代號1：申請2：終止
				occursList.putParam("AuthApplCode", thisLine.substring(25, 26));
//				 戶號(7)
				occursList.putParam("CustNo", thisLine.substring(54, 61));
//				郵局存款別(1)
				occursList.putParam("PostDepCode", thisLine.substring(26, 27));			
//				儲金帳號(14)
				occursList.putParam("RepayAcct", thisLine.substring(27, 41));

//				StampCode		核印註記    
				occursList.putParam("StampCode", thisLine.substring(73, 74));
//				AuthErrorCode	狀況代號，授權狀態
				occursList.putParam("AuthErrorCode", thisLine.substring(71, 73));

				this.occursList.add(occursList);
			}
//			StampCode
//			1 局帳號不符
//			2 戶名不符
//			3 身分證號不符
//			4 印鑑不符
//			9 其他

//			AuthErrorCode
//			00-成功
//          03-已終止代繳
//          06-凍結警示戶
//          07-支票專戶
//          08-帳號錯誤
//          09-終止戶
//          10-身分證不符
//          11-轉出戶
//          12-拒絕往來戶
//          13-無此編號
//          14-編號已存在
//          16-管制帳戶
//          17-掛失戶
//          18-異常帳戶
//          19-編號非英數
//          91-期限未扣款
//          98-其他
//			      -尚未授權

			// 頁尾
//			Footer
//			1	FootDataClass   資料別		0-1		X(1)	固定值為2	
//			2	FootOrgCode 	委託機構代號	1-4		X(3)	同明細	
//			3	FootNoteA   	保留欄		4-8		X(4)	空白	
//			4	FootMediaDate   媒體產生日期	8-16	X(8)	同明細	
//			5	FootBatchNo 	批號			16-19	9(3)	同明細	
//			6	FootCreateFlag  建檔記號		19-20	X(1)	固定值B：委託機構送件	固定值F：郵局回送「帳戶至郵局辦理終止」檔	
//			7	FootDataCnt 	總筆數		20-26	9(6)	右靠左補0	
//			8	FootCreateDate  資料建檔日期	26-34	X(8)	初始值為空白，	郵局回送「帳戶至郵局辦理終止」檔：空白回送時使用	
//			9	FootErrorCnt    錯誤筆數		34-40	9(6)	初始值為0，回送時使用	
//			10	FootSuccsCnt    成功筆數		40-46	9(6)	初始值為0，回送時使用	
//			11	FootNoteB   	保留欄		46-100	X(54)		

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
				this.put("FootErrorCnt", thisLine.substring(34, 40));
				this.put("FootSuccsCnt", thisLine.substring(40, 46));
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
//			1	資料別		0-1		X(1)	固定值為1	
//			2	委託機構代號	1-4		X(3)	大寫英數字	
//			3	保留欄		4-8		X(4)	空白	
//			4	媒體產生日期	8-16	X(8)	西元年月日YYYYMMDD	
//			5	批號			16-19	9(3)	固定值為001 	
//			6	流水號		19-25	9(6)	每批自000001序編	
//			7	申請代號		25-26	X(1)	"委託機構送件：1：申請2：終止"	"郵局回送「帳戶至郵局辦理終止」檔：3：郵局終止4：誤終止-已回復為申請"
//			8	帳戶別		26-27	X(1)	P：存簿    G：劃撥	
//			9	儲金帳號		27-41	9(14)	存簿：局帳號計14碼   劃撥：000000+8碼帳號	
//			10	用戶編號		41-61	X(20)	"右靠左補空，大寫英數字，不得填寫中文由委託機構自行編給其客戶之編號"	
//			11	統一證號		61-71	X(10)	左靠右補空白	
//			12	狀況代號		71-73	X(2)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//			13	核對註記		73-75	X(1)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//			14	保留欄		75-100	X(26)	空白	

			String thisLine = "" + occursList.get("OccDataClass") // 1 資料別
					+ occursList.get("OccOrgCode") // 2 委託機構代號
					+ occursList.get("OccNoteA") // 3 保留欄
					+ occursList.get("OccMediaDate")// 4 媒體產生日期
					+ occursList.get("OccBatchNo")// 5 批號
					+ occursList.get("OccDataSeq") // 6 流水號
					+ occursList.get("OccApprCode") // 7 申請代號
					+ occursList.get("OccAcctType") // 8 帳戶別
					+ occursList.get("OccRepayAcct")// 9 儲金帳號
					+ occursList.get("OccCustNo") // 10 用戶編號
					+ occursList.get("OccCustId") // 11 統一證號
					+ occursList.get("OccStatusCode")// 12 狀況代號
					+ occursList.get("OccCheckInd") // 13 核對註記
					+ occursList.get("OccNoteB");// 14 保留欄
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//			1	資料別		0-1		X(1)	固定值為2	
//			2	委託機構代號	1-4		X(3)	同明細	
//			3	保留欄		4-8		X(4)	空白	
//			4	媒體產生日期	8-16	X(8)	同明細	
//			5	批號			16-19	9(3)	同明細	
//			6	建檔記號		19-20	X(1)	固定值B：委託機構送件	固定值F：郵局回送「帳戶至郵局辦理終止」檔	
//			7	總筆數		20-26	9(6)	右靠左補0	
//			8	資料建檔日期	26-34	X(8)	初始值為空白，	郵局回送「帳戶至郵局辦理終止」檔：空白回送時使用	
//			9	錯誤筆數		34-40	9(6)	初始值為0，回送時使用	
//			10	成功筆數		40-46	9(6)	初始值為0，回送時使用	
//			11	保留欄		46-100	X(54)		

			String thisLine = "" + this.get("FootDataClass") // 1 資料別
					+ this.get("FootOrgCode") // 2 委託機構代號
					+ this.get("FootNoteA") // 3 保留欄
					+ this.get("FootMediaDate")// 4 媒體產生日期
					+ this.get("FootBatchNo")// 5 批號
					+ this.get("FootCreateFlag") // 6 建檔記號
					+ this.get("FootDataCnt") // 7 總筆數
					+ this.get("FootCreateDate") // 8 資料建檔日期
					+ this.get("FootErrorCnt")// 9 錯誤筆數
					+ this.get("FootSuccsCnt") // 10 成功筆數
					+ this.get("FootNoteB");// 11 保留欄

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

}
