package com.st1.itx.util.common.data;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.OccursList;

/**
 * @author YuJiaXing
 * @version 1.0.0
 */

@Component("L2309Vo")
@Scope("prototype")
public class L2309Vo extends FileVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7071969613475637614L;
	// 設定首筆筆數
	private final int headerCounts = 0;
	// 設定尾筆筆數
	private final int footerCounts = 0;

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	/**
	 * 從檔案讀取到的資料，依照此方法中的定義，擷取欄位值
	 * 
	 * @param lineList 放入FileCom的intputTxt取得的ArrayList
	 */
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
			}

			// 明細
			if (i >= headerCounts && i <= (LastIndex - footerCounts)) {
				OccursList occursList = new OccursList();
//

				// 設定明細欄位的擷取位置

				occursList.putParam("ReltId", thisLine);

				this.occursList.add(occursList);
			}

			// 頁尾
			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置
			}

			i++;
		}
	}

//		不懂6/14

	/**
	 * 把目前FileVo內的欄位資料轉成ArrayList
	 * 
	 * @return ArrayList 可放入FileCom的outputTxt
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
//			relsUKey        	ukey		X	32	
//			relsId				關係人統編	X	10	
//			relsName			關係人姓名	X	100	
//			relsCode			關係人職稱	X	9	
//			EffectStartDate		生效起日		A	8	
//			EffectEndDate		生效止日		A	8	
			// ukey
			// 關係人統編
			// 關係人姓名
			// 關係人職稱
			// 生效起日
			// 生效止日

			String thisLine = occursList.get("relsUKey") + "," + occursList.get("relsId") + "," + occursList.get("relsName") + "," + occursList.get("relsCode") + ","
					+ occursList.get("EffectStartDate") + "," + occursList.get("EffectEndDate");

			result.add(thisLine);
		}

		// 組頁尾
		for (int i = 0; i < footerCounts; i++) {
			// 頁尾的欄位組合
//			1	資料別		0-1		X(1)	固定值為2	

//			String thisLine = "" + this.get("FootNoteB");// 11 保留欄

//			result.add(thisLine);
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
