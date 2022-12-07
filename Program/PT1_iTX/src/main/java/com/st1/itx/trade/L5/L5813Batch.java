package com.st1.itx.trade.L5;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.db.service.springjpa.cm.L5811ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L5813ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L5813Batch")
@Scope("prototype")

public class L5813Batch extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CustMainService sCustMainService;

	@Autowired
	private YearlyHouseLoanIntService sYearlyHouseLoanIntService;

	@Autowired
	private ClFacService clFacService;

	@Autowired
	private ClBuildingService sClBuildingService;

	@Autowired
	private ClBuildingOwnerService sClBuildingOwnerService;

	@Autowired
	private CdCodeService sCdCodeService;

	@Autowired
	private L5813ServiceImpl l5813ServiceImpl;

	/* 轉型共用工具 */
	@Autowired
	private Parse parse;
	@Autowired
	private MakeFile makeFile;
	@Autowired
	private WebClient webClient;
	@Autowired
	private MakeExcel makeExcel;
	@Autowired
	private DateUtil dDateUtil;

	private Slice<ClBuildingOwner> tClBuildingOwner = null;
	private ClBuildingOwner cClBuildingOwner = null;
	private Boolean checkFlag = true;
	private String sendMsg = " ";
	private String iYear = "";
	private List<Map<String, String>> resultList = null;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);

		iYear = titaVo.getParam("Year");

		try {
			resultList = l5813ServiceImpl.checkAll(iYear, titaVo);
		} catch (Exception e) {
			throw new LogicException("E0013", e.getMessage());
		}

		this.info("resultList size==" + resultList.size());
		if (resultList == null || resultList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔無資料");
		}

		try {
			doIR(titaVo);// TO 國稅局
			doOW(titaVo);// TO 官網
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo() + "L5813", "L5813國稅局申報媒體檔已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(), sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void doIR(TitaVo titaVo) throws LogicException {
		// 上傳國稅局
		this.info("into doIR");

		int tYear = Integer.parseInt(titaVo.getParam("Year")) + 1;
		String fileCode = "L5813";
		String fileItem = "國稅局申報(國稅局-LNM57M1P)";
		String fileName = "LNM57M1P-" + tYear + "年度.csv";

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileCode + fileItem, fileName, 2);

		for (Map<String, String> result : resultList) {

			if (!(("2").equals(result.get("UsageCode")) || ("02").equals(result.get("UsageCode")))) {
				continue;
			}

			String iCustName = "";
			String iCustId = "";
			String bdOwner = ""; // 所有權人姓名
			String bdCustId = ""; // 所有權人身分證

			int iCustNo = Integer.parseInt(result.get("CustNo"));
			int houseBuyDate = Integer.parseInt(result.get("HouseBuyDate"));// 房屋取的日期
			if (houseBuyDate > 19110000) {
				houseBuyDate = houseBuyDate - 19110000;
			}
			int iFacmNo = Integer.parseInt(result.get("FacmNo"));
			BigDecimal iLoanAmt = parse.stringToBigDecimal(result.get("LoanAmt"));// 最初初貸金額
			int iFirstDrawdownDate = Integer.parseInt(result.get("FirstDrawdownDate"));// 貸款起日
			if (iFirstDrawdownDate > 19110000) {
				iFirstDrawdownDate = iFirstDrawdownDate - 19110000;
			}
			int iMaturityDate = Integer.parseInt(result.get("MaturityDate")); // 貸款迄日
			if (iMaturityDate > 19110000) {
				iMaturityDate = iMaturityDate - 19110000;
			}
			BigDecimal iLoanBal = parse.stringToBigDecimal(result.get("LoanBal"));// 放款餘額
			int iYYYMM = Integer.parseInt(result.get("YearMonth")) - 191100;

			CustMain tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
//				第170~222行
//				不是只要找主要擔保品記號Y的那筆,
//				而是找這一戶的全部擔保品,
//				所有權人戶名bdOwner、統編bdCustId要先以本人為主,如果沒有本人就用找到的第一個人的資料
			ClFac tClFac = clFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);// 戶號找擔保品

			if (tCustMain != null) {
				iCustName = tCustMain.getCustName();
				iCustName = covertToChineseFullChar(iCustName);
				if (iCustName.length() > 9) {
					iCustName = iCustName.substring(0, 9);
				} else if (iCustName.length() < 9) {
					for (int j = iCustName.length(); j < 9; j++) {
						iCustName = iCustName + '　';
					}
				}
				iCustId = tCustMain.getCustId();
			}

			if (tCustMain != null && tClFac != null && "1".equals(tCustMain.getCuscCd()) && tClFac.getClCode1() == 1) {
				this.info("getCuscCd==" + tCustMain.getCuscCd());
				this.info("getClCode1==" + tClFac.getClCode1());
				// 此筆為"自然人"且擔保品類別為"房地"時，才找所有權人
				Map<String, String> ownerMap = findBuildingOwner(tClFac, titaVo);
				bdCustId = ownerMap.get("OwnerId");
				bdOwner = ownerMap.get("OwnerName");
			} else {
				continue;
			}

			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(result.get("JsonFields"));

			String bdLocation = "";// 建物地址
			bdLocation = tTempVo.get("BdLoacation");
			if (bdLocation == null) {//YearlyHouseLoanInt無門牌資料則改放建物檔門牌地址
				bdLocation = result.get("BdLocation");
			}
			bdLocation = covertToChineseFullChar(bdLocation);
			if (bdLocation.length() < 28) {
				for (int a = bdLocation.length(); a < 28; a++) {
					bdLocation = bdLocation + '　';
				}
			}
			bdLocation = bdLocation.substring(0, 28);

			bdOwner = covertToChineseFullChar(bdOwner);
			if (bdOwner.length() > 9) {
				bdOwner = bdOwner.substring(0, 9);
			} else {
				for (int i = bdOwner.length(); i < 9; i++) {
					bdOwner = bdOwner + "　";
				}
			}

			// 貸款帳號
			String iAccounount = "";
			iAccounount = String.format("%07d", iCustNo) + "-" + String.format("%03d", iFacmNo);

			String iYearMonthSt = "";// 繳息年月起

			int yyymm = iYYYMM / 100;
			this.info("yyymm==" + yyymm);

			int firstdrawdowndate = iFirstDrawdownDate / 10000;
			this.info("firstdrawdowndate==" + firstdrawdowndate);

			// 若資料年度與貸款起日年度相等，則繳息年月放貸款起日年月
			// 若資料年度大於貸款起日，則放資料年度+01
			if (yyymm == firstdrawdowndate) {
				firstdrawdowndate = iFirstDrawdownDate / 100;
				iYearMonthSt = String.valueOf(firstdrawdowndate);
			} else if (yyymm > firstdrawdowndate) {
				iYearMonthSt = yyymm + "01";
			}
			this.info("iYearMonthSt==" + iYearMonthSt);

			String cUsageCode = result.get("UsageCode");
			if (!cUsageCode.isEmpty() && cUsageCode.length() < 2) {
				cUsageCode = "0" + cUsageCode;
			}

			String strField = "";
			String vertical = "|";

			strField += iYear; // 年度
			strField += vertical;
			strField += "458"; // 金融機構固定458
			strField += vertical;
			strField += "    ";// 分行代號 空白
			strField += vertical;
			strField += bdLocation + "  "; // 房屋座落地址
			strField += vertical;
			strField += bdOwner; // 房屋所有權人姓名
			strField += vertical;
			strField += FormatUtil.padX(bdCustId, 10); // 房屋所有權人身分證統一編號
			strField += vertical;
			if (houseBuyDate == 0) {
				strField += "       ";
			} else {
				strField += String.format("%07d", houseBuyDate); // 房屋所有權取得日 YYYMMDD
			}

			strField += vertical;
			strField += iCustName; // 借款人姓名
			strField += vertical;
			strField += makeFile.fillStringL(iCustId, 16); // 借款人身分證統一編號
			strField += vertical;
			strField += makeFile.fillStringR(iAccounount, 50); // 貸款帳號
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iLoanAmt), 10, '0'); // 最初貸款金額 右靠前埔0
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iFirstDrawdownDate), 7, '0'); // 貸款起日
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iMaturityDate), 7, '0'); // 貸款迄日
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iLoanBal), 10, '0'); // 截至本年度未償還本金餘額
			strField += vertical;
			strField += iYearMonthSt; // 繳息年月起
			strField += vertical;
			strField += String.valueOf(iYYYMM); // 繳息年月迄
			strField += vertical;
			strField += StringUtils.leftPad(result.get("YearlyInt"), 10, '0'); // 年度實際繳息金額 右靠前埔0

			makeFile.put(strField);
		}

		long sno1 = makeFile.close();

		this.info("sno1 : " + sno1);

		makeFile.toFile(sno1);

	}

	private Map<String, String> findBuildingOwner(ClFac tClFac, TitaVo titaVo) {
		this.info("findBuildingOwner start ...");

		Map<String, String> result = new HashMap<>();

		String ownerName = "";
		String ownerId = "";

		// 戶號找擔保品
		if (tClFac != null) {
			int custNo = tClFac.getCustNo();
			int facmNo = tClFac.getFacmNo();
			List<Map<String, String>> queryResultList = null;
			try {
				queryResultList = l5813ServiceImpl.queryBuildingOwner(custNo, facmNo, titaVo);
			} catch (Exception e) {
				this.error("queryBuildingOwner error = " + e.getMessage());
			}
			if (queryResultList != null && !queryResultList.isEmpty()) {
				Map<String, String> queryResult = queryResultList.get(0);
				ownerId = queryResult.get("CustId");
				ownerName = queryResult.get("CustName");
			}
		}
		result.put("OwnerId", ownerId);
		result.put("OwnerName", ownerName);
		this.info("findBuildingOwner end ... ownerName = " + ownerName + " , ownerId = " + ownerId);
		return result;
	}

	public void doOW(TitaVo titaVo) throws LogicException {
		// 上傳官網
		this.info("into doOW");

		int tYear = Integer.parseInt(titaVo.getParam("Year")) + 1;
		String fileCode = "L5813";
		String fileItem = "國稅局申報(官網-LNM572P)";
		String fileName = "LNM572P-" + tYear + "年度.csv";
		String dataCustNo = "";
		String dataFacmNo = "";
		int printfg = 0;
		String pUsageCode = "";// 國稅局-用途別只找一次,出檔用

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileCode + fileItem, fileName, 2);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5813", fileItem, fileName, "LNM572P-"+tYear);

		for (Map<String, String> result : resultList) {

			if ("".equals(dataCustNo)) {// 第一筆
				dataCustNo = result.get("CustNo");
				dataFacmNo = result.get("FacmNo");
			}
			if (!dataCustNo.equals(result.get("CustNo")) || !dataFacmNo.equals(result.get("FacmNo"))) {
				dataCustNo = result.get("CustNo");
				dataFacmNo = result.get("FacmNo");
				printfg = 0;// initialize
			}

			if (("2").equals(result.get("UsageCode")) || ("02").equals(result.get("UsageCode"))) {// 國稅局-紀錄有資料但不寫
				printfg = 1;

//				String cUsageCode = result.get("UsageCode");
//				if (("").equals(pUsageCode)) {// 只找一次
//					if (!cUsageCode.isEmpty() && cUsageCode.length() < 2) {
//						cUsageCode = "0" + cUsageCode;
//					}
//					CdCode tCdCode = sCdCodeService.findById(new CdCodeId("UsageCode", cUsageCode), titaVo);
//					if (tCdCode != null) {// 用途別
//						pUsageCode = tCdCode.getItem();
//					} else {
//						pUsageCode = "週轉金";
//					}
//				}

				continue;
			}

			String bdOwner = ""; // 所有權人姓名
			String bdCustId = ""; // 所有權人身分證
			String iCustName = "";
			String iCustId = "";
			String iUkey = "";

			int iCustNo = Integer.parseInt(result.get("CustNo"));
			int houseBuyDate = Integer.parseInt(result.get("HouseBuyDate"));// 房屋取的日期
			if (houseBuyDate > 19110000) {
				houseBuyDate = houseBuyDate - 19110000;
			}
			int iFacmNo = Integer.parseInt(result.get("FacmNo"));
			BigDecimal iLoanAmt = parse.stringToBigDecimal(result.get("LoanAmt"));// 最初初貸金額
			int iFirstDrawdownDate = Integer.parseInt(result.get("FirstDrawdownDate"));// 貸款起日
			if (iFirstDrawdownDate > 19110000) {
				iFirstDrawdownDate = iFirstDrawdownDate - 19110000;
			}
			int iMaturityDate = Integer.parseInt(result.get("MaturityDate")); // 貸款迄日
			if (iMaturityDate > 19110000) {
				iMaturityDate = iMaturityDate - 19110000;
			}
			BigDecimal iLoanBal = parse.stringToBigDecimal(result.get("LoanBal"));// 放款餘額
			int iYYYMM = Integer.parseInt(result.get("YearMonth")) - 191100;
//			String cUsageCode = result.get("UsageCode2"); // 使用LoanBorMain的用途別

			CustMain tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			ClFac tClFac = clFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);// 戶號找擔保品

			if (tCustMain != null) {
				iCustName = tCustMain.getCustName();
				iCustName = covertToChineseFullChar(iCustName);
				if (iCustName.length() > 5) {
					iCustName = iCustName.substring(0, 5);
				} else if (iCustName.length() < 5) {
					for (int j = iCustName.length(); j < 5; j++) {
						iCustName = iCustName + '　';
					}
				}
				iCustId = tCustMain.getCustId();
				iUkey = tCustMain.getCustUKey();
			}

			// 等於自然人 或 擔保品代號等於1，否則跳過
			if (tCustMain != null && tClFac != null && "1".equals(tCustMain.getCuscCd()) && tClFac.getClCode1() == 1) {
				this.info("getCuscCd==" + tCustMain.getCuscCd());
				this.info("getClCode1==" + tClFac.getClCode1());
				// 此筆為"自然人"且擔保品類別為"房地"時，才找所有權人
				Map<String, String> ownerMap = findBuildingOwner(tClFac, titaVo);
				bdCustId = ownerMap.get("OwnerId");
				bdOwner = ownerMap.get("OwnerName");
			} else {
				continue;
			}

			bdOwner = covertToChineseFullChar(bdOwner);
			for (int i = bdOwner.length(); i < 19; i++) {
				bdOwner = bdOwner + "　";
			}
			bdOwner = bdOwner.substring(0, 19);

			String bdLocation = "";// 建物地址
			String bdSpace = "";// 空白

			// 地址
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(result.get("JsonFields"));
			bdLocation = tTempVo.get("BdLoacation");
			if (bdLocation == null) {//YearlyHouseLoanInt無門牌資料則改放建物檔門牌地址
				bdLocation = result.get("BdLocation");
			}
			bdLocation = covertToChineseFullChar(bdLocation);
			if (bdLocation.length() < 28) {
				for (int a = bdLocation.length(); a < 28; a++) {
					bdLocation = bdLocation + '　';
				}
			}
			bdLocation = bdLocation.substring(0, 28);

			// 貸款帳號
			String iAccounount = "";
			iAccounount = String.format("%07d", iCustNo) + "-" + String.format("%03d", iFacmNo);

			// 繳息年月起

			String iYearMonthSt = "";

			int yyymm = iYYYMM / 100;
			this.info("yyymm==" + yyymm);

			int firstdrawdowndate = iFirstDrawdownDate / 10000;
			this.info("firstdrawdowndate==" + firstdrawdowndate);

			// 若資料年度與貸款起日年度相等，則繳息年月放貸款起日年月
			// 若資料年度大於貸款起日，則放資料年度+01
			if (yyymm == firstdrawdowndate) {
				firstdrawdowndate = iFirstDrawdownDate / 100;
				iYearMonthSt = String.valueOf(firstdrawdowndate);
			} else if (yyymm > firstdrawdowndate) {
				iYearMonthSt = yyymm + "01";
			}
			this.info("iYearMonthSt==" + iYearMonthSt);

			String iUsageCode = result.get("Item");
//			if (printfg == 1) {// 國稅局有資料-代表購置不動產
//				iUsageCode = pUsageCode ;
//			} else {
//				if (!cUsageCode.isEmpty() && cUsageCode.length() < 2) {
//					cUsageCode = "0" + cUsageCode;
//				}
//				CdCode tCdCode = sCdCodeService.findById(new CdCodeId("UsageCode", cUsageCode), titaVo);
//				if (tCdCode != null) {// 用途別
//					iUsageCode = tCdCode.getItem();
//				} else {
//					iUsageCode = "週轉金";
//				}
//			}
			if(iUsageCode.length() < 6) {
				for (int i = iUsageCode.length(); i < 6; i++) { 
					iUsageCode = iUsageCode + "  ";
				}
			}
			
			String strField = "";
			String vertical = ",";

			strField += iYear; // 年度
			strField += vertical;
			strField += "458"; // 金融機構固定458
			strField += vertical;
			strField += "    ";// 分行代號 空白
			strField += vertical;
			strField += bdLocation + FormatUtil.padX(bdSpace, 62); // 房屋座落地址
			strField += vertical;
			strField += bdOwner; // 房屋所有權人姓名
			strField += vertical;
			strField += FormatUtil.padX(bdCustId, 10); // 房屋所有權人身分證統一編號
			strField += vertical;
			if (houseBuyDate == 0) {
				strField += "       ";
			} else {
				strField += String.format("%07d", houseBuyDate); // 房屋所有權取得日 YYYMMDD
			}

			strField += vertical;
			strField += iCustName; // 借款人姓名
			strField += vertical;
			strField += FormatUtil.padX(iCustId, 10); // 借款人身分證統一編號
			strField += vertical;
			strField += "      " + makeFile.fillStringR(iAccounount, 50); // 貸款帳號
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iLoanAmt), 10, '0'); // 最初貸款金額 右靠前埔0
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iFirstDrawdownDate), 7, '0'); // 貸款起日
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iMaturityDate), 7, '0'); // 貸款迄日
			strField += vertical;
			strField += StringUtils.leftPad(String.valueOf(iLoanBal), 10, '0'); // 截至本年度未償還本金餘額
			strField += vertical;
			strField += iYearMonthSt; // 繳息年月起
			strField += vertical;
			strField += String.valueOf(iYYYMM); // 繳息年月迄
			strField += vertical;
			strField += StringUtils.leftPad(result.get("YearlyInt"), 10, '0'); // 年度實際繳息金額 右靠前埔0
			strField += vertical;
			strField += iUsageCode; // 用途別 12碼
			makeFile.put(strField);

		}

		long sno2 = makeFile.close();

		this.info("sno2 : " + sno2);

		makeFile.toFile(sno2);

	}

	public String covertToChineseFullChar(String str) {
		String result = "";
		if (!Objects.isNull(str) && !str.trim().isEmpty()) {
			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) { // bypass Chinese character
		        if (chars[i] >= 33 && chars[i] <= 126) {
		        	chars[i] = (char) (chars[i] + 65248);
		        } else if (chars[i] == 32) {
		        	chars[i] = (char) 12288;
		        }
			}
			result = String.valueOf(chars);
		}
		return result;
	}

}
