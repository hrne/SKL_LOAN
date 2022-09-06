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
@Component("L4101OldVo")
@Scope("prototype")
public class L4101OldVo extends FileVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4183615389357447145L;
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
			if (t.getDrawdownCode() == 2 || t.getDrawdownCode() == 4 || t.getDrawdownCode() == 11) {
				continue;
			}
			String wkCustId = "";
			String wkCustNm = "";
			CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
			if (tCustMain != null) {
				wkCustId = tCustMain.getCustId();
			}
			seq = seq + 1;
////		DataSeq		序號			4	0	4
////		AcctNo		帳號			X	14	4	18
////		Amount		金額			X	13	18	31
////		UnitCode	解付單位代號	X	7	31	38
////		RemitName	代償專戶		X	59	38	97
////		ColumnA	新光人壽保險股份有限公司─放款服務課	X	35	97	132
////		ColumnB		space		X	59	132	191
////		ColumnC		00174		X	5	191	196
////		RemitDate	匯款日期		X	8	196	204
////		BatchNo		批號			X	2	204	206
			// 明細資料的單筆資料的欄位組合
			String thisLine = "" + FormatUtil.pad9("" + seq, 4) // 序號
					+ FormatUtil.padX(t.getRemitAcctNo(), 14) // 帳號
					+ FormatUtil.pad9("" + t.getRemitAmt(), 13) // 金額
					+ FormatUtil.pad9("" + t.getRemitBank(), 3)// 解付單位代號3
					+ FormatUtil.pad9("" + t.getRemitBranch(), 4)// 解付單位代號4
					+ FormatUtil.padX(t.getCustName(), 59)// 代償專戶
					+ "新光人壽保險股份有限公司─放款服務課"// 新光人壽保險股份有限公司─放款服務課
					+ FormatUtil.padX("", 59)// space
					+ "00174"// 00174
					+ t.getAcDate() + 19110000// 匯款日期
					+ t.getBatchNo().substring(4, 6);// 批號

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
