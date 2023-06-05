package com.st1.itx.util.common.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

/**
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Component("L4101OldVo")
@Scope("prototype")
public class L4101OldVo extends FileVo {

	/**
	 * 
	 */

	private static final long serialVersionUID = -4183615389357447145L;
	@Autowired
	public MakeReport makeReport;
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

		int seq = 0;
		// 組明細
		for (BankRemit t : lBankRemit) {

			seq = seq + 1;
////		DataSeq		序號			4	0	4
////		AcctNo		帳號			X	14	4	18
////		Amount		金額			X	13	18	31
////		UnitCode	解付單位代號	X	7	31	38
////		RemitName	代償專戶		X	57	38	97
////		ColumnA	新光人壽保險股份有限公司─放款服務課	X	36	97	132
////		ColumnB		space		X	38	132	191
////		ColumnC		00174		X	5	191	196
////		RemitDate	匯款日期		X	8	196	204
////		BatchNo		批號			X	2	204	206

//			20230503請IT提供正確的layout
//			序號(4)
//			帳號(14)
//			金額(13)(AS400只放前面11位後面兩位補0)
//			行庫代號(7) 
//			收款人姓名(60 J) => 29個全形字
//			匯款人姓名(40 J) => 19個全形字
//			附言(40 J) => 19個全形字
//			空白(15)
//			單位代號(5)
//			匯款日期(8)
//			批號(2)
//			空白(2)

			String thisLine = "";
			try {
				thisLine += makeReport.fillUpWord("" + seq, 4, "0", "L");// 序號
				thisLine += makeReport.fillUpWord("" + t.getRemitAcctNo(), 14, "0", "L");// 帳號
				thisLine += makeReport.fillUpWord("" + t.getRemitAmt(), 11, "0", "L") + "00";// 金額
				thisLine += makeReport.fillUpWord("" + t.getRemitBank(), 3, "0", "L");// 解付單位代號3
				thisLine += makeReport.fillUpWord("" + t.getRemitBranch(), 4, "0", "L");// 解付單位代號4
				String name = t.getCustName().replace("o", "Ｏ");
				thisLine += makeReport.fillUpWord("" + name, 58, "　", "R") + "  ";// 代償專戶(29全形+2半形)
				thisLine += makeReport.fillUpWord("新光人壽保險股份有限公司─放款服務課", 38, " ", "R") + "  ";// 代償專戶
//				thisLine += "　　　　　　　　　　　　　　　　　　　" + "  ";// 附言(19全形+2半形)
				String remark = t.getRemark().replace("o", "Ｏ");
				thisLine += makeReport.fillUpWord("" + remark, 19, "　", "R") + "  ";// 附言(19全形+2半形)
				thisLine += "               ";// 空白(15個半形空白)
				thisLine += "00174";// 單位代號
				thisLine += (Integer.valueOf(t.getAcDate()) + 19110000);// 匯款日期
				thisLine += "01  ";// 批號
			} catch (LogicException e) {
		
				e.printStackTrace();
			}

			result.add(thisLine);
		}

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
