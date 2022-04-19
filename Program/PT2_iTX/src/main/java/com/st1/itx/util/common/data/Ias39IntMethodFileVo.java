package com.st1.itx.util.common.data;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.OccursList;
import com.st1.itx.util.parse.Parse;

/**
 *
 * @author Yoko
 * @version 1.0.0
 */
@Component("Ias39IntMethodFileVo")
@Scope("prototype")
public class Ias39IntMethodFileVo extends FileVo {

	private static final long serialVersionUID = 6472151252016092881L;
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
				// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
				// 2 CustNo 戶號 Decimal 7
				// 3 CustId 統一編號 VARCHAR2 10
				// 4 FacmNo 額度編號 Decimal 3
				// 5 BormNo 撥款序號 Decimal 3
				// 6 Principal 本期本金餘額 Decimal 15 4
				// 7 BookValue 本期帳面價值 Decimal 15 4
				// 8 AccumDPAmortized 本期累應攤銷折溢價 Decimal 15 4
				// 9 AccumDPunAmortized 本期累未攤銷折溢價 Decimal 15 4
				// 10 DPAmortized 本期折溢價攤銷數 Decimal 15 4

				String[] thisColumn = thisLine.split(",");
				if (thisColumn.length >= 1 && thisColumn != null) {
					occursList.putParam("YearMonth", thisColumn[0]);
					occursList.putParam("CustNo", thisColumn[1]);
					occursList.putParam("CustId", thisColumn[2]);
					occursList.putParam("FacmNo", thisColumn[3]);
					occursList.putParam("BormNo", thisColumn[4]);
					occursList.putParam("Principal", thisColumn[5]);
					occursList.putParam("BookValue", thisColumn[6]);
					occursList.putParam("AccumDPAmortized", thisColumn[7]);
					occursList.putParam("AccumDPunAmortized", thisColumn[8]);
					occursList.putParam("DPAmortized", thisColumn[9]);
				}
				this.occursList.add(occursList);
			}

			// 頁尾
//      Footer
//      1 FootDataClass   資料別   0-1   X(1)  固定值為2

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
//        this.put("FootErrorCnt", thisLine.substring(34, 40));
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
			// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
			// 2 CustNo 戶號 Decimal 7
			// 3 CustId 統一編號 VARCHAR2 10
			// 4 FacmNo 額度編號 Decimal 3
			// 5 BormNo 撥款序號 Decimal 3
			// 6 Principal 本期本金餘額 Decimal 15 4
			// 7 BookValue 本期帳面價值 Decimal 15 4
			// 8 AccumDPAmortized 本期累應攤銷折溢價 Decimal 15 4
			// 9 AccumDPunAmortized 本期累未攤銷折溢價 Decimal 15 4
			// 10 DPAmortized 本期折溢價攤銷數 Decimal 15 4

			String thisLine = occursList.get("YearMonth") + "," + occursList.get("CustNo") + "," + occursList.get("CustId") + "," + occursList.get("FacmNo") + "," + occursList.get("BormNo") + ","
					+ occursList.get("Principal") + "," + occursList.get("BookValue") + "," + occursList.get("AccumDPAmortized") + "," + occursList.get("AccumDPunAmortized") + ","
					+ occursList.get("DPAmortized");
			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//      1 資料別   0-1   X(1)  固定值為2

//      String thisLine = "" + this.get("FootNoteB");// 11 保留欄

//      result.add(thisLine);
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
