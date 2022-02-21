package com.st1.itx.util.common.data;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.OccursList;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Component("AchAuthFileVo")
@Scope("prototype")
public class AchAuthFileVo extends FileVo {

	private static final long serialVersionUID = 1449715043719817704L;

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
	public void setValueFromFile(ArrayList<String> lineList) {

		// 總行數
		int LastIndex = lineList.size() - 1;

		int i = 0;
		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 頁首
			if (i < headerCounts) {
				// 設定頁首欄位的擷取位置
//				this.put("RetrDate", thisLine.substring(9, 17));
			}

			// 明細
			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
				OccursList occursList = new OccursList();

				// 設定明細欄位的擷取位置
//				1	OccTxseq			交易序號		9	6	0	6	從1開始，靠右左補0
//				2	OccTxCode			交易代號		X	3	6	9	801
//				3	OccSnederId			發動者統一編號	X	10	9	19	03458902
//				4	OccWdBankNo			提回行代號		9	7	19	26	"依扣款銀行代號區分台新：8120012合庫：0060567新光：1030019"
//				5	OccRepayAcct		委繳戶帳號		X	14	26	40	扣款帳號
//				6	OccCustId			委繳戶統一編號	X	10	40	50	身分證字號
//				7	OccCustNo			用戶號碼		X	20	50	70	借款人戶號
//				8	OccCreateFlag		新增或取消		X	1	70	71	"A：新增(人工紙本)O：舊檔轉換用"
//				9	OccAuthCreateDate	資料製作日期	9	8	71	79	
//				10	OccSnederNo			提出行代號		9	7	79	86	1030116
//				11	OccSenderRemarker	發動者專用區	X	20	86	106	戶號+額度
//				12	OccTxType			交易型態		X	1	106	107	R：提回
//				13	OccAuthStatus		回覆訊息		X	1	107	108	提出時填入空白
//				14	OccLimitAmt			每筆扣款限額	X	8	108	116	
//				15	OccNote				備用			X	4	116	120	

//				PK 
//				AuthCreateDate	建檔日期    
//				CustNo	戶號
//				RepayBank	扣款銀行
//				RepayAcct	扣款帳號
//				CreateFlag		新增或取消
				occursList.putParam("PropDate", thisLine.substring(71, 79));
				occursList.putParam("CustNo", thisLine.substring(50, 70));
				occursList.putParam("FacmNo", thisLine.substring(93, 96));
				occursList.putParam("RepayBank", thisLine.substring(19, 22));
				occursList.putParam("RepayAcct", thisLine.substring(26, 40));
				occursList.putParam("CreateFlag", thisLine.substring(70, 71));

//				AuthStatus	授權狀態
				occursList.putParam("AuthStatus", thisLine.substring(107, 108));
//				AuthCheck	確認是否為提回資料
				occursList.putParam("AuthCheck", thisLine.substring(106, 107));

				this.occursList.add(occursList);
			}

			// 頁尾
			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
				this.put("TotCnt", thisLine.substring(3, 11));
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

			String thisLine = "" + this.get("HeadIndex") // 1 首錄別 BOF
					+ this.get("HeadDataKCode") // 2 資料代號 ACHP02
					+ this.get("HeadRocTxday") // 3 交易日期 民國 YYYYMMDD
					+ this.get("HeadSubmitUnit")// 4 發送單位代號 代表行代號 1030000
					+ this.get("HeadNote");// 5 備用
			result.add(thisLine);
		}

		// 組明細
		for (OccursList occursList : occursList) {
			// 明細資料的單筆資料的欄位組合

			String thisLine = "" + occursList.get("OccTxseq") // 1交易序號 從1開始，靠右左補0
					+ occursList.get("OccTxCode") // 2交易代號 801
					+ occursList.get("OccSnederId") // 3發動者統一編號 03458902
					+ occursList.get("OccWdBankNo")// 4提回行代號 台新：8120012 合庫：0060567 新光：1030019"
					+ occursList.get("OccRepayAcct")// 5委繳戶帳號 扣款帳號
					+ occursList.get("OccCustId") // 6委繳戶統一編號 身分證字號
					+ occursList.get("OccCustNo") // 7用戶號碼 借款人戶號
					+ occursList.get("OccCreateFlag") // 8新增或取消 A：新增(人工紙本)O：舊檔轉換用
					+ occursList.get("OccPropDate")// 9資料製作日期
					+ occursList.get("OccSnederNo") // 10提出行代號 1030116
					+ occursList.get("OccSenderRemarker") // 11發動者專用區 戶號+額度
					+ occursList.get("OccTxType")// 12交易型態 N：提出
					+ occursList.get("OccAuthStatus") // 13回覆訊息 提出時填入空白
					+ occursList.get("OccLimitAmt")// 14每筆扣款限額
					+ occursList.get("OccNote");// 15備用
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合

			String iTotCnt = "";

			if (this.get("TotaCnt") != null) {
				iTotCnt = "" + this.get("TotaCnt");
			} else if (this.get("TotbCnt") != null) {
				iTotCnt = "" + this.get("TotbCnt");
			}
			String thisLine = "" + this.get("FootIndex") // 1尾錄別 EOF
					+ FormatUtil.pad9(iTotCnt, 8) // 2總筆數
					+ this.get("FootNote"); // 3備用
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
