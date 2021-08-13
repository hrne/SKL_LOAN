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
@Component("InsuRenewFileVo")
@Scope("prototype")
public class InsuRenewFileVo extends FileVo {

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
//				1	FireInsuMonth		火險到期年月	X	6	
//				2	ReturnCode			回傳碼		X	2	 回傳碼(00:正常,01:失敗),目前無用:99
//				3	InsuCampCode		保險公司代碼	X	2	(01:舊件,新產保單)
//				4	InsuCustId			提供人統一編號	X	10	(iissu)
//				5	InsuCustName		提供人姓名		X	12	(nissu) 右補空白
//				6	LoanCustId			借款人統一編號	X	10	(iins)
//				7	LoanCustName		借款人姓名		X	12	(nins) 右補空白
//				8	PostalCode			郵遞區號		X	5	
//				9	Address				門牌號碼		X	58	右補空白
//				10	BuildingSquare		主建物坪數		X	9	(整數7位小數2位)左補0
//				11	BuildingCode		建物結構代碼	X	2	
//				12	BuildingYears		建造年份		X	3	(3碼)左補0
//				13	BuildingFloors		樓層數		X	2	(整數2位)左補0
//				14	RoofCode			屋頂結構代碼	X	2	空白
//				15	BusinessUnit		營業單位別		X	4	0
//				16	CollCode1			押品別１		X	1	
//				17	CollCode2			押品別２		X	2	
//				18	CollNo				押品號碼			X	7	
//				19	Seq					序號			X	2	???
//				20	InsuNo				保單號碼		X	16	
//				21	InsuStartDate		保險起日		X	10	(YYYY/MM/DD)
//				22	InsuEndDate			保險迄日		X	10	(YYYY/MM/DD)
//				23	FireInsuAmt			火險保額		X	11	左補0
//				24	FireInsuFee			火險保費		X	7	左補0
//				25	EqInsuAmt			地震險保額		X	7	左補0
//				26	EqInsuFee			地震險保費		X	6	左補0
//				27	CustNo				借款人戶號		X	7	左補0
//				28	FacmNo				額度編號		X	3	左補0
//				29	Space				空白			X	4	
//				30	SendDate			傳檔日期		X	14	左補空白
//				31	NewInusNo			保單號碼(新)		X	16	空白
//				32	NewInsuStartDate	保險起日(新)		X	10	(YYYY/MM/DD)
//				33	NewInsuEndDate		保險迄日(新)		X	10	(YYYY/MM/DD)
//				34	NewFireInsuAmt		火險保額(新)		X	11	左補0
//				35	NewFireInsuFee		火險保費(新)		X	7	左補0
//				36	NewEqInsuAmt		地震險保額(新)	X	8	左補0
//				37	NewEqInsuFee		地震險保費(新)	X	6	左補0
//				38	NewTotalFee			總保費(新)		X	7	左補0
//				39	Remark1				備註一		X	16	
//				40	MailingAddress		通訊地址		X	60	
//				41	Remark2				備註二		X	39	
//				42	SklSalesName		新光人壽業務員名稱	X	20	
//				43	SklUnitCode			新光人壽單位代號		X	6	
//				44	SklUnitName			新光人壽單位中文		X	20	
//				45	SklSalesCode		新光人壽業務員代號	X	6	
//				46	RenewTrlCode		新產續保經辦代號		X	8	
//				47	RenewUnit			新產續保單位		X	7	

				String[] thisColumn = thisLine.split(",");
				if (thisColumn != null && thisColumn.length >= 46) {
					occursList.putParam("FireInsuMonth", thisColumn[0]);
					occursList.putParam("ReturnCode", thisColumn[1]);
					occursList.putParam("InsuCampCode", thisColumn[2]);
					occursList.putParam("InsuCustId", thisColumn[3]);
					occursList.putParam("InsuCustName", thisColumn[4]);
					occursList.putParam("LoanCustId", thisColumn[5]);
					occursList.putParam("LoanCustName", thisColumn[6]);
					occursList.putParam("PostalCode", thisColumn[7]);
					occursList.putParam("Address", thisColumn[8]);
					occursList.putParam("BuildingSquare", thisColumn[9]);
					occursList.putParam("BuildingCode", thisColumn[10]);
					occursList.putParam("BuildingYears", thisColumn[11]);
					occursList.putParam("BuildingFloors", thisColumn[12]);
					occursList.putParam("RoofCode", thisColumn[13]);
					occursList.putParam("BusinessUnit", thisColumn[14]);
					occursList.putParam("ClCode1", thisColumn[15]);
					occursList.putParam("ClCode2", thisColumn[16]);
					occursList.putParam("ClNo", thisColumn[17]);
					occursList.putParam("Seq", thisColumn[18]);
					occursList.putParam("InsuNo", thisColumn[19]);
					occursList.putParam("InsuStartDate", thisColumn[20]);
					occursList.putParam("InsuEndDate", thisColumn[21]);
					occursList.putParam("FireInsuAmt", thisColumn[22]);
					occursList.putParam("FireInsuFee", thisColumn[23]);
					occursList.putParam("EqInsuAmt", thisColumn[24]);
					occursList.putParam("EqInsuFee", thisColumn[25]);
					occursList.putParam("CustNo", thisColumn[26]);
					occursList.putParam("FacmNo", thisColumn[27]);
					occursList.putParam("Space", thisColumn[28]);
					occursList.putParam("SendDate", thisColumn[29]);
					occursList.putParam("NewInusNo", thisColumn[30]);
					occursList.putParam("NewInsuStartDate", thisColumn[31]);
					occursList.putParam("NewInsuEndDate", thisColumn[32]);
					occursList.putParam("NewFireInsuAmt", thisColumn[33]);
					occursList.putParam("NewFireInsuFee", thisColumn[34]);
					occursList.putParam("NewEqInsuAmt", thisColumn[35]);
					occursList.putParam("NewEqInsuFee", thisColumn[36]);
					occursList.putParam("NewTotalFee", thisColumn[37]);
					occursList.putParam("Remark1", thisColumn[38]);
					occursList.putParam("MailingAddress", thisColumn[39]);
					occursList.putParam("Remark2", thisColumn[40]);
					occursList.putParam("SklSalesName", thisColumn[41]);
					occursList.putParam("SklUnitCode", thisColumn[42]);
					occursList.putParam("SklUnitName", thisColumn[43]);
					occursList.putParam("SklSalesCode", thisColumn[44]);
					occursList.putParam("RenewTrlCode", thisColumn[45]);
					occursList.putParam("RenewUnit", thisColumn[46]);
				} else {
					throw new LogicException("E0014", "請確認上傳檔案是否正確");
				}
				this.occursList.add(occursList);
			}

			// 頁尾
//			Footer
//			1	FootDataClass   資料別		0-1		X(1)	固定值為2	

			if (i > (LastIndex - footerCounts)) {
				// 設定頁尾欄位的擷取位置s
//				this.put("FootErrorCnt", thisLine.substring(34, 40));
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
//			1	FireInsuMonth		火險到期年月	X	6	
//			2	ReturnCode			回傳碼		X	2	 回傳碼(00:正常,01:失敗),目前無用:99
//			3	InsuCampCode		保險公司代碼	X	2	(01:舊件,新產保單)
//			4	InsuCustId			提供人統一編號	X	10	(iissu)
//			5	InsuCustName		提供人姓名		X	12	(nissu) 右補空白
//			6	LoanCustId			借款人統一編號	X	10	(iins)
//			7	LoanCustName		借款人姓名		X	12	(nins) 右補空白
//			8	PostalCode			郵遞區號		X	5	
//			9	Address				門牌號碼		X	58	右補空白
//			10	BuildingSquare		主建物坪數		X	9	(整數7位小數2位)左補0
//			11	BuildingCode		建物結構代碼	X	2	
//			12	BuildingYears		建造年份		X	3	(3碼)左補0
//			13	BuildingFloors		樓層數		X	2	(整數2位)左補0
//			14	RoofCode			屋頂結構代碼	X	2	空白
//			15	BusinessUnit		營業單位別		X	4	0
//			16	CollCode1			押品別１		X	1	
//			17	CollCode2			押品別２		X	2	
//			18	CollNo				押品號碼			X	7	
//			19	Seq					序號			X	2	???
//			20	InsuNo				保單號碼		X	16	
//			21	InsuStartDate		保險起日		X	10	(YYYY/MM/DD)
//			22	InsuEndDate			保險迄日		X	10	(YYYY/MM/DD)
//			23	FireInsuAmt			火險保額		X	11	左補0
//			24	FireInsuFee			火險保費		X	7	左補0
//			25	EqInsuAmt			地震險保額		X	7	左補0
//			26	EqInsuFee			地震險保費		X	6	左補0
//			27	CustNo				借款人戶號		X	7	左補0
//			28	FacmNo				額度編號		X	3	左補0
//			29	Space				空白			X	4	
//			30	SendDate			傳檔日期		X	14	左補空白
//			31	NewInusNo			保單號碼(新)		X	16	空白
//			32	NewInsuStartDate	保險起日(新)		X	10	(YYYY/MM/DD)
//			33	NewInsuEndDate		保險迄日(新)		X	10	(YYYY/MM/DD)
//			34	NewFireInsuAmt		火險保額(新)		X	11	左補0
//			35	NewFireInsuFee		火險保費(新)		X	7	左補0
//			36	NewEqInsuAmt		地震險保額(新)	X	8	左補0
//			37	NewEqInsuFee		地震險保費(新)	X	6	左補0
//			38	NewTotalFee			總保費(新)		X	7	左補0
//			39	Remark1				備註一		X	16	
//			40	MailingAddress		通訊地址		X	60	
//			41	Remark2				備註二		X	39	
//			42	SklSalesName		新光人壽業務員名稱	X	20	
//			43	SklUnitCode			新光人壽單位代號		X	6	
//			44	SklUnitName			新光人壽單位中文		X	20	
//			45	SklSalesCode		新光人壽業務員代號	X	6	
//			46	RenewTrlCode		新產續保經辦代號		X	8	
//			47	RenewUnit			新產續保單位		X	7	

			String thisLine = occursList.get("FireInsuMonth") + "," + occursList.get("ReturnCode") + "," + occursList.get("InsuCampCode") + "," + occursList.get("InsuCustId") + ","
					+ occursList.get("InsuCustName") + "," + occursList.get("LoanCustId") + "," + occursList.get("LoanCustName") + "," + occursList.get("PostalCode") + "," + occursList.get("Address")
					+ "," + occursList.get("BuildingSquare") + "," + occursList.get("BuildingCode") + "," + occursList.get("BuildingYears") + "," + occursList.get("BuildingFloors") + ","
					+ occursList.get("RoofCode") + "," + occursList.get("BusinessUnit") + "," + occursList.get("ClCode1") + "," + occursList.get("ClCode2") + "," + occursList.get("ClNo") + ","
					+ occursList.get("Seq") + "," + occursList.get("InsuNo") + "," + occursList.get("InsuStartDate") + "," + occursList.get("InsuEndDate") + "," + occursList.get("FireInsuAmt") + ","
					+ occursList.get("FireInsuFee") + "," + occursList.get("EqInsuAmt") + "," + occursList.get("EqInsuFee") + "," + occursList.get("CustNo") + "," + occursList.get("FacmNo") + ","
					+ occursList.get("Space") + "," + occursList.get("SendDate") + "," + occursList.get("NewInusNo") + "," + occursList.get("NewInsuStartDate") + "," + occursList.get("NewInsuEndDate")
					+ "," + occursList.get("NewFireInsuAmt") + "," + occursList.get("NewFireInsuFee") + "," + occursList.get("NewEqInsuAmt") + "," + occursList.get("NewEqInsuFee") + ","
					+ occursList.get("NewTotalFee") + "," + occursList.get("Remark1") + "," + occursList.get("MailingAddress") + "," + occursList.get("Remark2") + "," + occursList.get("SklSalesName")
					+ "," + occursList.get("SklUnitCode") + "," + occursList.get("SklUnitName") + "," + occursList.get("SklSalesCode") + "," + occursList.get("RenewTrlCode") + ","
					+ occursList.get("RenewUnit") + "," + occursList.get("Space46");
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
