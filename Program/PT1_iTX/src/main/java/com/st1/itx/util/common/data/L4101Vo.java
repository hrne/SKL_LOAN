package com.st1.itx.util.common.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Component("L4101Vo")
@Scope("prototype")
public class L4101Vo extends FileVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -897188639124657748L;
	@Autowired
	public CustMainService custMainService;
	// 設定首筆筆數
	private final int headerCounts = 1;
	// 設定尾筆筆數
	private final int footerCounts = 1;

	// 明細資料容器
	List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
	TitaVo titaVo;

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
//			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
//				OccursList occursList = new OccursList();
//
//				// 設定明細欄位的擷取位置                                              
//
//
////				PK 
////				AuthCreateDate	建檔日期    
////				CustNo	戶號
////				RepayBank	扣款銀行
////				RepayAcct	扣款帳號
////				CreateFlag		新增或取消
//				occursList.putParam("AuthCreateDate", thisLine.substring(71, 79));
//				occursList.putParam("CustNo", thisLine.substring(50, 70));
//				occursList.putParam("RepayBank", thisLine.substring(19, 22));
//				occursList.putParam("RepayAcct", thisLine.substring(26, 40));
//				occursList.putParam("CreateFlag", thisLine.substring(70, 71));
//
////				AuthStatus	授權狀態
//				occursList.putParam("AuthStatus", thisLine.substring(107, 108));
////				AuthCheck	確認是否為提回資料
//				occursList.putParam("AuthCheck", thisLine.substring(106, 107));
//
//				this.occursList.add(occursList);
//			}

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

//		// 組頁首
//		for (int i = 0; i < headerCounts; i++) {
//			// 頁首的欄位組合
//
//			String thisLine = "" + "請款序號 (PK)" + "," + "付款號碼" + "," + "付款型態" + "," + "付款狀況碼" + "," + "付款狀況異動日" + ","
//					+ "付款方式" + "," + "付款處理(人工付款指示)" + "," + "相關號碼" + "," + "受款人" + "," + "付款摘要" + "," + "支票格式" + ","
//					+ "支票號碼" + "," + "支票(匯款)日期" + "," + "兌領日期" + "," + "開票日期/匯款日期" + "," + "受款總行" + "," + "受款分行" + ","
//					+ "受款人帳號" + "," + "付款金額" + "," + "程式代碼" + "," + "功能碼/變更項目別" + "," + "作業者" + "," + "作業日期" + ","
//					+ "處理日期" + "," + "開票者/匯款者" + "," + "請款單批號(付款憑單批號)" + "," + "最近異動最遠票日" + "," + "掛號型態" + ","
//					+ "支票郵寄通知單型式" + "," + "郵寄標籤" + "," + "郵寄通知人(單位)代號" + "," + "請款單位代號" + "," + "扣匯款手續費" + "," + "缺單據"
//					+ "," + "缺單據原因" + "," + "業務員代號" + "," + "付款來源" + "," + "傳輸匯款通知" + "," + "傳輸匯款通知單位" + "," + "匯款磁片編號"
//					+ "," + "付款銀行" + "," + "類別碼(簡碼)" + "," + "支票郵寄日" + "," + "入帳號碼" + "," + "到期作業指示" + "," + "郵遞區號"
//					+ "," + "寄達地址" + "," + "付款開票格式" + "," + "受款人ID" + "," + "前期建檔號碼" + "," + "付款單位代送" + "," + "取消建檔原因碼"
//					+ "," + "群組請款序號" + "," + "合併付款群組號碼" + "," + "權責單位(請款單位)覆核日" + "," + "權責單位(請款單位)覆核者" + ","
//					+ "付款單位(出納)覆核日" + "," + "付款單位(出納)覆核者" + "," + "服務中心-匯款彙總批號" + "," + "付款單位-覆核批號" + "," + "帳號錯誤通知單位/人"
//					+ "," + "幣別" + "," + "櫃檯支票簽收類別" + "," + "櫃檯支票簽收者ID" + "," + "櫃檯支票簽收者姓名" + "," + "批次櫃台指示碼(件類別)" + ","
//					+ "支票寄達單位" + "," + "批號付款序號";
//			result.add(thisLine);
//		}

		// 組明細
		for (BankRemit t : lBankRemit) {
			if (t.getDrawdownCode() == 2 || t.getDrawdownCode() == 4 || t.getDrawdownCode() == 11) {
				continue;
			}
			String wkCustId = "";
			String wkCustNm = "";
			CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
			if (tCustMain != null) {
				wkCustId = tCustMain.getCustId();
			}
			// 明細資料的單筆資料的欄位組合
			String thisLine = "" + "" // 請款序號 (PK) VARCHAR2 12
					+ "," + "" // 付款號碼 VARCHAR2 12
					+ "," + "" // 付款型態 VARCHAR2 1
					+ "," + ""// 付款狀況碼 VARCHAR2 1
					+ "," + ""// 付款狀況異動日 Date 7
					+ "," + "R"// 付款方式 VARCHAR2 1
					+ "," + "A";// 付款處理(人工付款指示) VARCHAR2 1
//			撥款:戶號-額度-撥款(15)
//			退款:員編(6)+交易序號(8)
			if (t.getDrawdownCode() == 1) { // 相關號碼 VARCHAR2 15
				thisLine = thisLine + "," + FormatUtil.pad9("" + t.getCustNo(), 7) + "-" + FormatUtil.pad9("" + t.getFacmNo(), 3) + "-" + FormatUtil.pad9("" + t.getBormNo(), 3);
			} else {
				thisLine = thisLine + "," + FormatUtil.pad9("" + t.getTitaTlrNo(), 6) + "-" + FormatUtil.pad9("" + t.getTitaTxtNo(), 8);
			}
			if (t.getCustName().length() > 40) {
				wkCustNm = t.getCustName().substring(0, 40);
			} else {
				wkCustNm = t.getCustName();
			}
			thisLine = thisLine + "," + wkCustNm // 受款人 VARCHAR2 40
					+ "," + "新壽放款"// 付款摘要 VARCHAR2 45 TODO:未來可能需要判斷是否為代償件 清河:先預設放新壽放款
					+ "," + ""// 支票格式 VARCHAR2 1
					+ "," + ""// 支票號碼 VARCHAR2 9
					+ "," + t.getAcDate()// 支票(匯款)日期 Date 7
					+ "," + ""// 兌領日期 Date 7
					+ "," + ""// 開票日期/匯款日期 Date 7
					+ "," + t.getRemitBank()// 受款總行 VARCHAR2 3
					+ "," + t.getRemitBranch()// 受款分行 VARCHAR2 4
					+ "," + t.getRemitAcctNo()// 受款人帳號 VARCHAR2 16
					+ "," + t.getRemitAmt()// 付款金額 NUMBER (12,2) TODO:是否需做超過10位處理
					+ "," + ""// 程式代碼 VARCHAR2 8
					+ "," + ""// 功能碼/變更項目別 VARCHAR2 2
					+ "," + ""// 作業者 VARCHAR2 8
					+ "," + ""// 作業日期 Date 7
					+ "," + ""// 處理日期 Date 7
					+ "," + ""// 開票者/匯款者 VARCHAR2 8
					+ "," + ""// 請款單批號(付款憑單批號) NUMBER 10
					+ "," + ""// 最近異動最遠票日 Date 7
					+ "," + ""// 掛號型態 VARCHAR2 1
					+ "," + ""// 支票郵寄通知單型式 VARCHAR2 1
					+ "," + ""// 郵寄標籤 VARCHAR2 1
					+ "," + ""// 郵寄通知人(單位)代號 VARCHAR2 12
					+ "," + ""// 請款單位代號 VARCHAR2 6
					+ "," + ""// 扣匯款手續費 VARCHAR2 1
					+ "," + ""// 缺單據 VARCHAR2 1
					+ "," + ""// 缺單據原因 VARCHAR2 1
					+ "," + ""// 業務員代號 VARCHAR2 12
					+ "," + "t"// 付款來源 VARCHAR2 1
					+ "," + ""// 傳輸匯款通知 VARCHAR2 1
					+ "," + ""// 傳輸匯款通知單位 VARCHAR2 6
					+ "," + ""// 匯款磁片編號 NUMBER 10
					+ "," + ""// 付款銀行 VARCHAR2 7
					+ "," + ""// 類別碼(簡碼) VARCHAR2 10
					+ "," + ""// 支票郵寄日 Date 7
					+ "," + ""// 入帳號碼 VARCHAR2 12
					+ "," + ""// 到期作業指示 VARCHAR2 1
					+ "," + ""// 郵遞區號 VARCHAR2 5
					+ "," + ""// 寄達地址 VARCHAR2 80
					+ "," + ""// 付款開票格式 VARCHAR2 1
					+ "," + wkCustId// 受款人ID VARCHAR2 10
					+ "," + ""// 前期建檔號碼 VARCHAR2 12
					+ "," + ""// 付款單位代送 VARCHAR2 1
					+ "," + ""// 取消建檔原因碼 VARCHAR2 2
					+ "," + ""// 群組請款序號 VARCHAR2 12
					+ "," + ""// 合併付款群組號碼 VARCHAR2 12
					+ "," + ""// 權責單位(請款單位)覆核日 Date 7
					+ "," + ""// 權責單位(請款單位)覆核者 VARCHAR2 8
					+ "," + ""// 付款單位(出納)覆核日 Date 7
					+ "," + ""// 付款單位(出納)覆核者 VARCHAR2 8
					+ "," + ""// 服務中心-匯款彙總批號 NUMBER 10
					+ "," + ""// 付款單位-覆核批號 NUMBER 10
					+ "," + ""// 帳號錯誤通知單位/人 VARCHAR2 8
					+ "," + "NTD"// 幣別 VARCHAR2 3
					+ "," + ""// 櫃檯支票簽收類別 VARCHAR2 1
					+ "," + ""// 櫃檯支票簽收者ID VARCHAR2 10
					+ "," + ""// 櫃檯支票簽收者姓名 VARCHAR2 40
					+ "," + ""// 批次櫃台指示碼(件類別) VARCHAR2 1
					+ "," + ""// 支票寄達單位 VARCHAR2 6
					+ "," + ""// 批號付款序號 NUMBER 10

			;

			result.add(thisLine);
		}

		// 組頁尾
//		for (int i = 0; i < footerCounts; i++) {
//			// 頁尾的欄位組合
//
//			String iTotCnt = "";
//
//			if (this.get("TotaCnt") != null) {
//				iTotCnt = "" + this.get("TotaCnt");
//			} else if (this.get("TotbCnt") != null) {
//				iTotCnt = "" + this.get("TotbCnt");
//			}
//			String thisLine = "" + this.get("FootIndex") // 1尾錄別 EOF
//					+ FormatUtil.pad9(iTotCnt, 8) // 2總筆數
//					+ this.get("FootNote"); // 3備用
//			result.add(thisLine);
//		}
		return result;
	}

	public List<BankRemit> getOccursList() {
		return lBankRemit;
	}

	public void setOccursList(List<BankRemit> l, TitaVo titaVo) {
		this.lBankRemit = l;
		this.titaVo = titaVo;
	}

}
