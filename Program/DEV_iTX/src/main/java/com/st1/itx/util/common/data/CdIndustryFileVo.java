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
@Component("CdIndustryFileVo")
@Scope("prototype")
public class CdIndustryFileVo extends FileVo {

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
				// 1 IndustryCode 行業代號 VARCHAR2 6
				// 2 IndustryItem 行業說明 NVARCHAR2 50
				// 3 MainType 主計處大類 VARCHAR2 1

				String[] thisColumn = thisLine.split(",");
				if (thisColumn.length >= 1 && thisColumn != null) {
					occursList.putParam("IndustryCode", thisColumn[0]);
					occursList.putParam("IndustryItem", thisColumn[1]);
					occursList.putParam("MainType", thisColumn[2]);
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
			// 1 IndustryCode 行業代號 VARCHAR2 6
			// 2 IndustryItem 行業說明 NVARCHAR2 50
			// 3 MainType 主計處大類 VARCHAR2 1

			String thisLine = occursList.get("IndustryCode") + "," + occursList.get("IndustryItem") + "," + occursList.get("MainType");
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
