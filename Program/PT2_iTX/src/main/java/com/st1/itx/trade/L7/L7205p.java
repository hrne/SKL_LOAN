package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;
import com.st1.itx.db.domain.Ias34Ap;
import com.st1.itx.db.domain.Ifrs9FacData;
import com.st1.itx.db.domain.Ifrs9FacDataId;
import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;
import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.domain.MonthlyLM055AssetLossId;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.db.service.MonthlyLM055AssetLossService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L7205p")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L7205p extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public MonthlyFacBalService tMothlyFacBalService;
	@Autowired
	public Ifrs9FacDataService tIfrs9FacDataService;
	@Autowired
	public LoanIfrs9ApService tLoanIfrs9ApService;
	@Autowired
	public Ias34ApService tIas34ApService;
	@Autowired
	MonthlyLM052AssetClassService sLM052AssetClass;
	@Autowired
	MonthlyLM052LoanAssetService sLM052LoanAsset;
	@Autowired
	MonthlyLM052OvduService sLM052Ovdu;
	@Autowired
	MonthlyLM052LossService sLM052LossService;
	@Autowired
	MonthlyLM055AssetLossService sLM055AssetLossService;
	@Autowired
	CdCommService sCdCommService;

	@Autowired
	L7205 l7205;

	@Autowired
	JobMainService sJobMainService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Value("${iTXInFolder}")
	private String inFolder = "";

//	public String extension = "";

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();
	private List<MonthlyFacBal> facBalSumList = new ArrayList<MonthlyFacBal>();
	private List<MonthlyLM052AssetClass> lLM052AssetClass = new ArrayList<MonthlyLM052AssetClass>();
	private List<MonthlyLM055AssetLoss> lLM055AssetLoss = new ArrayList<MonthlyLM055AssetLoss>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7205p ");
		this.totaVo.init(titaVo);

		int iYearMonth = parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

		int dateSent = Integer.parseInt(titaVo.getParam("YearMonth") + "01");
		dateUtil.init();
		dateUtil.setDate_1(dateSent);
//		TxBizDate tTxBizDate = dateUtil.getForTxBizDate(true);// 若1號為假日,參數true則會找次一營業日,不會踢錯誤訊息
//		int iMfbsDy = tTxBizDate.getMfbsDy() + 19110000;// 畫面輸入年月的月底營業日

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename=" + filename);

		ArrayList<String> dataLineList = new ArrayList<>();

		titaVo.keepOrgDataBase();// 保留原本記號

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			this.info("L7205(" + filename + ") : " + e.getMessage());

			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;

			throw new LogicException(titaVo, "E0014", ErrorMsg);

		}

		String extension[] = filename.split("\\.");
		this.info("file extension=" + extension[extension.length - 1]);
		if ("xlsx".equals(extension[extension.length - 1]) || "xls".equals(extension[extension.length - 1])) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			// 切資料
			setValueFromFileExcelNew(titaVo, iYearMonth);
		} else if ("csv".equals(extension[extension.length - 1].toLowerCase())) {

			setValueFromFile(titaVo, dataLineList, iYearMonth);

		} else {

			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";

			throw new LogicException(titaVo, "E0014", ErrorMsg);

		}

		int CountAll = occursList.size();
		int CountS = 0;
		int CountF = 0;

		Slice<Ias34Ap> sIas34Ap = null;
		Slice<LoanIfrs9Ap> sLoanIfrs9Ap = null;

		List<MonthlyFacBal> lMonthlyFacBal = new ArrayList<MonthlyFacBal>();

		Slice<MonthlyFacBal> slMothlyFacBal = tMothlyFacBalService.findYearMonthAll(iYearMonth, 0, Integer.MAX_VALUE,
				titaVo);

		if (slMothlyFacBal == null) {
			throw new LogicException(titaVo, "E0001", "MothlyFacBal"); // 查詢資料不存在
		}
		lMonthlyFacBal = slMothlyFacBal.getContent();
		for (OccursList tempOccursList : occursList) {

			int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
			int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
			String assetclass = tempOccursList.get("AssetClass");
			BigDecimal lawAmount = BigDecimal.ZERO;
			String lawAssetClass = "";

			if ("xlsx".equals(extension) || "xls".equals(extension)) {
				lawAmount = new BigDecimal(tempOccursList.get("LawAmount"));
				lawAssetClass = tempOccursList.get("LawAssetClass");
			}

			for (MonthlyFacBal t : lMonthlyFacBal) {
				if (custno == t.getCustNo() && facmno == t.getFacmNo()) {
					t.setAssetClass(assetclass.substring(0, 1));

					if ("xlsx".equals(extension[extension.length - 1])
							|| "xls".equals(extension[extension.length - 1])) {
						t.setLawAmount(lawAmount);
						t.setLawAssetClass(lawAssetClass);
						t.setAssetClass2(assetclass);

					}

					CountS++; // 成功筆數+1
					continue;
				}
			}

		}

		titaVo.keepOrgDataBase();// 保留原本記號

		try {
			tMothlyFacBalService.updateAll(lMonthlyFacBal, titaVo);

		} catch (DBException e) {

			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		titaVo.setDataBaseOnMon();// 指定月報環境

		try {
			tMothlyFacBalService.updateAll(lMonthlyFacBal, titaVo);
		} catch (DBException e) {

			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		titaVo.setDataBaseOnLine(); // 連線環境

		for (OccursList tempOccursList : occursList) {

			int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
			int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
			int yearmonth = parse.stringToInteger(tempOccursList.get("YearMonth"));
			String assetclass = tempOccursList.get("AssetClass");

			// 維護Ifrs9FacData
			Ifrs9FacDataId ifrs9FacDataId = new Ifrs9FacDataId();
			ifrs9FacDataId.setCustNo(custno);
			ifrs9FacDataId.setFacmNo(facmno);
			ifrs9FacDataId.setDataYM(yearmonth);

			Ifrs9FacData tIfrs9FacData = tIfrs9FacDataService.findById(ifrs9FacDataId, titaVo);

			if (tIfrs9FacData == null) {
			} else {
				tIfrs9FacData.setAssetClass(parse.stringToInteger(assetclass));
				try {
					tIfrs9FacDataService.update(tIfrs9FacData, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
			}

			// 維護Ias34Ap

			sIas34Ap = tIas34ApService.dataEq(custno, facmno, yearmonth, this.index, this.limit, titaVo);

			List<Ias34Ap> lIas34Ap = sIas34Ap == null ? null : sIas34Ap.getContent();
			if (lIas34Ap == null || lIas34Ap.size() == 0) {
			} else {

				for (Ias34Ap t : lIas34Ap) {
					t.setAssetClass(parse.stringToInteger(assetclass));
					try {
						tIas34ApService.update(t, titaVo);
					} catch (DBException e) {

						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

				titaVo.setDataBaseOnMon();// 指定月報環境

				for (Ias34Ap t : lIas34Ap) {
					t.setAssetClass(parse.stringToInteger(assetclass));
					try {
						tIas34ApService.update(t, titaVo);
					} catch (DBException e) {

						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

			}

			// 維護LoanIfrs9Ap
			titaVo.setDataBaseOnLine(); // 連線環境
			sLoanIfrs9Ap = tLoanIfrs9ApService.dataEq(custno, facmno, yearmonth, this.index, this.limit, titaVo);

			List<LoanIfrs9Ap> lLoanIfrs9Ap = sLoanIfrs9Ap == null ? null : sLoanIfrs9Ap.getContent();
			if (lLoanIfrs9Ap == null || lLoanIfrs9Ap.size() == 0) {
			} else {

				for (LoanIfrs9Ap t : lLoanIfrs9Ap) {
					t.setAssetClass(parse.stringToInteger(assetclass));
					try {
						tLoanIfrs9ApService.update(t, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

				titaVo.setDataBaseOnMon();// 指定月報環境

				for (LoanIfrs9Ap t : lLoanIfrs9Ap) {
					t.setAssetClass(parse.stringToInteger(assetclass));
					try {
						tLoanIfrs9ApService.update(t, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

			}
		} // for

		this.batchTransaction.commit();
		titaVo.setDataBaseOnMon();// 指定月報環境
		this.batchTransaction.commit();

		titaVo.setDataBaseOnLine(); // 連線環境
		updLM052ReportSPAndMonthlyFacBalData(titaVo, iYearMonth);

		titaVo.setDataBaseOnMon();// 指定月報環境
		updLM052ReportSPAndMonthlyFacBalData(titaVo, iYearMonth);

		// 更新MonthlyLM055AssetLoss LM055重要放款餘額明細表
		getMonthlyLM055AssetLoss(titaVo, iYearMonth);

		titaVo.setDataBaseOnLine(); // 連線環境
		try {
			sLM055AssetLossService.updateAll(this.lLM055AssetLoss, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		titaVo.setDataBaseOnMon();// 指定月報環境
		try {
			sLM055AssetLossService.updateAll(this.lLM055AssetLoss, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		// 重產LM051報表
		titaVo.setBatchJobId("jLM051");

		titaVo.setDataBaseOnOrg();// 還原原本的環境
		String note = "總筆數：" + CountAll + ",成功筆數：" + CountS + ",失敗筆數：" + CountF;

		this.totaVo.putParam("CountAll", CountAll);
		this.totaVo.putParam("CountS", CountS);
		this.totaVo.putParam("CountF", CountF);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "",
				titaVo.getParam("TLRNO"), note, titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void setValueFromFile(TitaVo titaVo, ArrayList<String> lineList, int iYearMonth) throws LogicException {

		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 明細
			OccursList occursList = new OccursList();

			// 設定明細欄位的擷取位置
			// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
			// 2 CustNo 戶號 Decimal 7
			// 3 FacmNo 額度編號 Decimal 3
			// 4 AssetClass 資產五分類代號(有擔保部分) Decimal 1

			String[] thisColumn = thisLine.split(",");
			String yearMonth = "";
			if (thisColumn.length >= 1 && thisColumn != null) {

				if (Integer.valueOf(thisColumn[0].toString()) < 191100) {
					yearMonth = String.valueOf((Integer.valueOf(thisColumn[0].toString()) + 191100));
				} else {
					yearMonth = thisColumn[0];
				}

				if (Integer.valueOf(yearMonth) != iYearMonth) {

					String ErrorMsg = "年月份錯誤 : 應為" + iYearMonth + ",資料上為：" + yearMonth;

					throw new LogicException(titaVo, "E0015", ErrorMsg);
				}

				occursList.putParam("YearMonth", yearMonth);
				occursList.putParam("CustNo", thisColumn[1]);
				occursList.putParam("FacmNo", thisColumn[2]);

				// 判斷資產分類位置是否為空
				if (thisColumn[3].trim().length() == 0) {
					String ErrorMsg = "L7205(戶號 " + thisColumn[1] + "  的資產分類欄位不得為空值，請重新上傳)";

					throw new LogicException(titaVo, "E0015", ErrorMsg);
				}

				occursList.putParam("AssetClass", thisColumn[3]);

			}

			this.occursList.add(occursList);
		}

	}

	public void setValueFromFileExcelNew(TitaVo titaVo, int YearMonth) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		int iYearMonth = YearMonth;
		BigDecimal iCustNo = BigDecimal.ZERO;
		BigDecimal iFacmNo = BigDecimal.ZERO;
		String iAssetClass = "";
		BigDecimal iLawAmount = BigDecimal.ZERO;
		String iLawAssetClass = "";

		int fileYearMonth = new BigDecimal(makeExcel.getValue(1, 10).toString()).intValue() + 191100;

		this.info("輸入的年份：" + iYearMonth);
		this.info("檔案的年份：" + fileYearMonth);
		if (fileYearMonth != iYearMonth) {

			String ErrorMsg = "年月份錯誤 : 應為" + iYearMonth + ",資料上為：" + fileYearMonth;

			throw new LogicException(titaVo, "E0015", ErrorMsg);
		}

		String assetClasss[] = { "11", "12", "21", "22", "23", "3", "4", "5" };
		List<String> assetClasssList = Arrays.asList(assetClasss);
		for (int i = 2; i <= lastRowNum; i++) {

			OccursList occursList = new OccursList();

			// 正常是連續的資料串，遇到空值強行結束
			if (makeExcel.getValue(i, 2).toString().trim().length() == 0
					|| makeExcel.getValue(i, 2).toString().trim() == "") {
				break;
			}

			// 第8欄位為資產分類
			if (makeExcel.getValue(i, 8).toString().trim().length() == 0) {

				String ErrorMsg = "L7205(第" + i + "列，戶號 " + makeExcel.getValue(i, 2).toString()
						+ " 的資產分類欄位不得為空值，請重新上傳)";

				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}

			try {
				iCustNo = new BigDecimal(makeExcel.getValue(i, 2).toString());
				iFacmNo = new BigDecimal(makeExcel.getValue(i, 3).toString());
				iAssetClass = makeExcel.getValue(i, 8).toString().trim();
				iLawAmount = new BigDecimal(makeExcel.getValue(i, 9).toString());
				iLawAssetClass = makeExcel.getValue(i, 10).toString().trim();
			} catch (Exception e) {

				String ErrorMsg = "L7205(Excel欄位應為戶號在B欄、額度在C欄、資產分類為H欄)，請確認";

				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}
			// 檢查資產分類
			if (!assetClasssList.contains(iAssetClass)) {
				String ErrorMsg = "L7205(第" + i + "列，戶號 " + iCustNo + " 的資產分類錯誤)";
				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}
			// 檢查無擔保資產分類
			if (iLawAmount.compareTo(BigDecimal.ZERO) > 0) {
				if (iLawAssetClass.isEmpty()) {
					String ErrorMsg = "L7205(第" + i + "列，戶號 " + iCustNo + " 的無擔保資產分類應有值)";
					throw new LogicException(titaVo, "E0015", ErrorMsg);
				}
				if (!assetClasssList.contains(iLawAssetClass) || iLawAssetClass.equals(iAssetClass)) {
					String ErrorMsg = "L7205(第" + i + "列，戶號 " + iCustNo + " 的無擔保資產分類錯誤)";
					throw new LogicException(titaVo, "E0015", ErrorMsg);
				}
			}

			// 設定明細欄位的擷取位置
			// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
			// 2 CustNo 戶號 Decimal 7
			// 3 FacmNo 額度編號 Decimal 3
			// 4 AssetClass 資產五分類代號(有擔保部分) Decimal 1

			occursList.putParam("YearMonth", iYearMonth);
			occursList.putParam("CustNo", iCustNo.intValue());
			occursList.putParam("FacmNo", iFacmNo.intValue());
			occursList.putParam("AssetClass", iAssetClass);
			occursList.putParam("LawAmount", iLawAmount.intValue());
			occursList.putParam("LawAssetClass", iLawAssetClass);

			this.occursList.add(occursList);
		}

	}

	public void setValueFromFileExcelOld(TitaVo titaVo, int YearMonth) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		int iYearMonth = YearMonth;
		BigDecimal iCustNo = BigDecimal.ZERO;
		BigDecimal iFacmNo = BigDecimal.ZERO;
		BigDecimal iAssetClass = BigDecimal.ZERO;
		BigDecimal iLawAmount = BigDecimal.ZERO;
		for (int i = 1; i <= lastRowNum; i++) {

			OccursList occursList = new OccursList();

			// 正常是連續的資料串，遇到空值強行結束
			if (makeExcel.getValue(i, 1).toString().length() == 0 || makeExcel.getValue(i, 2).toString().length() == 0
					|| makeExcel.getValue(i, 3).toString().length() == 0
					|| makeExcel.getValue(i, 4).toString().length() == 0) {
				break;
			}

			if (Integer.valueOf(makeExcel.getValue(i, 1).toString()) != iYearMonth) {
				this.info("輸入的年份：" + iYearMonth);
				this.info("檔案的年份：" + Integer.valueOf(makeExcel.getValue(i, 1).toString()));

				String ErrorMsg = "年月份錯誤 : 應為" + iYearMonth + ",資料上為：" + makeExcel.getValue(i, 1);

				throw new LogicException(titaVo, "E0015", ErrorMsg);

			}
			try {
				iCustNo = new BigDecimal(makeExcel.getValue(i, 2).toString());
				iFacmNo = new BigDecimal(makeExcel.getValue(i, 3).toString());
				iAssetClass = new BigDecimal(makeExcel.getValue(i, 4).toString());
				iLawAmount = new BigDecimal(makeExcel.getValue(i, 5).toString());
			} catch (Exception e) {

				String ErrorMsg = "L7205(Excel欄位應為戶號在B欄、額度在C欄、資產分類為H欄)，請確認";

				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}

			// 設定明細欄位的擷取位置
			// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
			// 2 CustNo 戶號 Decimal 7
			// 3 FacmNo 額度編號 Decimal 3
			// 4 AssetClass 資產五分類代號(有擔保部分) Decimal 1

			occursList.putParam("YearMonth", iYearMonth);
			occursList.putParam("CustNo", iCustNo.intValue());
			occursList.putParam("FacmNo", iFacmNo.intValue());
			occursList.putParam("AssetClass", iAssetClass.intValue());
			occursList.putParam("LawAmount", iLawAmount.intValue());

			this.occursList.add(occursList);
		}

	}

	/**
	 * 上傳五類資產檔案後，同步更新以下檔案 MonthlyLM052AssetClass 資產分類檔 MonthlyLM052LoanAsset 特定資產分類檔
	 * MonthlyLM052Ovdu 逾期分類表
	 * 
	 * 20230912新增 MothlyFacBal SpecialAssetFlag 特定資產記號 MothlyFacBal BuildingFlag
	 * 建築貸款記號 MothlyFacBal GovProjectFlag 政策性貸款記號
	 */
	private void updLM052ReportSPAndMonthlyFacBalData(TitaVo titaVo, int yearMonth) {
		this.info("upd LM052 SP start.");
		String empNo = titaVo.getTlrNo();
		this.info("empNo=" + empNo);

		sLM052AssetClass.Usp_L9_MonthlyLM052AssetClass_Ins(yearMonth, empNo, "", titaVo);
		sLM052LoanAsset.Usp_L9_MonthlyLM052LoanAsset_Ins(yearMonth, empNo, "", titaVo);
		sLM052Ovdu.Usp_L9_MonthlyLM052Ovdu_Ins(yearMonth, empNo, "", titaVo);
		// 更新MonthlyFacBal GovProjectFlag,BuildingFlag,SpecialAssetFlag
		String txcd = titaVo.getTxcd();
		this.info("txcd=" + txcd);
		tMothlyFacBalService.Usp_L7_UploadToMonthlyFacBal_Upd(yearMonth, txcd, empNo, "", titaVo);

		this.info("upd LM052 SP finished.");
	}

	/**
	 * 更新MonthlyLM055AssetLoss LM055重要放款餘額明細表
	 * 
	 * @param titaVo    TitaVo
	 * @param yearMonth 西元年月
	 * @throws LogicException ...
	 */
	public void getMonthlyLM055AssetLoss(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info("updMonthlyLM055AssetLoss ...");
// Load MonthlyLM052AssetClass LM052資產分類表
		Slice<MonthlyLM052AssetClass> sLM052LoanAsset = sLM052AssetClass.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (sLM052LoanAsset == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM052AssetClass LM052資產分類表"); // 查詢資料不存在
		}
		this.lLM052AssetClass = sLM052LoanAsset.getContent();
// Load MothlyFacBal 額度月報工作檔
		Slice<MonthlyFacBal> slMothlyFacBal = tMothlyFacBalService.findYearMonthAll(yearMonth, 0, Integer.MAX_VALUE,
				titaVo);
		if (slMothlyFacBal == null) {
			throw new LogicException(titaVo, "E0001", "MothlyFacBal"); // 查詢資料不存在
		}

// govProjectAdjustAmt 政策性專案貸款調整數=	 ToTalLoanBal 專案貸款總額-調整 - oToTalLoanBal 專案貸款總額 - 88LoanBal 88風災調整數		
		CdComm tCdComm = sCdCommService.findById(new CdCommId("02", "02", yearMonth * 100 + 1), titaVo);
		if (tCdComm == null) {
			throw new LogicException(titaVo, "E0001", "CdComm 雜項資料檔 政策性專案貸款"); // 查詢資料不存在
		}
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(tCdComm.getJsonFields());
		if (tTempVo.get("LoanBal") == null || tTempVo.get("oLoanBal") == null || tTempVo.get("88LoanBal") == null) {
			throw new LogicException(titaVo, "E0001", "CdComm 政策性專案貸款"); // 查詢資料不存在
		}
		BigDecimal govProjectAdjustAmt = parse.stringToBigDecimal(tTempVo.get("LoanBal"))
				.subtract(parse.stringToBigDecimal(tTempVo.get("oLoanBal")))
				.subtract(parse.stringToBigDecimal(tTempVo.get("88LoanBal")));
		this.info("LoanBal=" + tTempVo.get("LoanBal"));
		this.info("oLoanBal=" + tTempVo.get("oLoanBal"));
		this.info("88LoanBal=" + tTempVo.get("88LoanBal"));
		this.info("govProjectAdjustAmt=" + govProjectAdjustAmt);
		// totalStorageAmt 備呆總金額(不含應收利息)
// totalEvaAmt 折溢價與催收費用
		BigDecimal totalStorageAmt = BigDecimal.ZERO;
		BigDecimal totalEvaAmt = BigDecimal.ZERO;
		for (MonthlyLM052AssetClass t : lLM052AssetClass) {
			if (!"7".equals(t.getAssetClassNo())) {
				totalStorageAmt = totalStorageAmt.add(t.getStorageAmt());
			}
			if ("6".equals(t.getAssetClassNo())) {
				totalEvaAmt = totalEvaAmt.add(t.getLoanBal());
			}
		}
// iFRS9AdjustAmt IFRS9增提金額(含應收利息) = 備呆總金額(不含應收利息) - 會計部核定備抵損失(MonthlyLM052Loss.ApprovedLoss)		
		MonthlyLM052Loss tMonthlyLM052Loss = sLM052LossService.findById(yearMonth, titaVo);
		if (tMonthlyLM052Loss == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM052Loss LM052備抵損失資料檔"); // 查詢資料不存在
		}
		BigDecimal iFRS9AdjustAmt = totalStorageAmt.subtract(tMonthlyLM052Loss.getApprovedLoss());

// 刪除舊表資料
		Slice<MonthlyLM055AssetLoss> slMonthlyLM055AssetLoss = sLM055AssetLossService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM055AssetLoss != null) {
			try {
				sLM055AssetLossService.deleteAll(slMonthlyLM055AssetLoss.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0004", e.getErrorMsg());
			}
		}
// 
		this.lLM055AssetLoss = new ArrayList<MonthlyLM055AssetLoss>();

// 政策性專案貸款調整		
		// G.政策性專案貸款：GovProjectAdjustAmt
		// C.不動產抵押放款 : 0 - GovProjectAdjustAmt
		MonthlyLM055AssetLoss l = new MonthlyLM055AssetLoss();
		l.setLoanType("A");
		this.lLM055AssetLoss.add(l);
		l = new MonthlyLM055AssetLoss();
		l.setLoanType("B");
		this.lLM055AssetLoss.add(l);
		l = new MonthlyLM055AssetLoss();
		l.setLoanType("C");
		l.setNormalAmount(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		l.setLoanAmountNor0(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		l.setIFRS9AdjustAmt(iFRS9AdjustAmt);
		this.lLM055AssetLoss.add(l);
		l = new MonthlyLM055AssetLoss();
		l.setLoanType("D");
		this.lLM055AssetLoss.add(l);
		l = new MonthlyLM055AssetLoss();
		l.setLoanType("G");
		l.setNormalAmount(govProjectAdjustAmt);
		l.setLoanAmountNor0(govProjectAdjustAmt);
		this.lLM055AssetLoss.add(l);
		l = new MonthlyLM055AssetLoss();
		l.setLoanType("Z");
		this.lLM055AssetLoss.add(l);

		// 借用欄位 : loanType => BuildingFlag

		MonthlyFacBal a = new MonthlyFacBal();
		a = new MonthlyFacBal();
		a.setBuildingFlag("G");
		a.setAssetClass2("11");
		a.setPrinBalance(govProjectAdjustAmt);
		this.facBalSumList.add(a);
		a = new MonthlyFacBal();
		a.setBuildingFlag("C");
		a.setAssetClass2("11");
		a.setPrinBalance(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		this.facBalSumList.add(a);

// 按放款種類累計資產五分類(2)本金餘額至 facBalSumList
		String loanType;
		for (MonthlyFacBal m : slMothlyFacBal.getContent()) {
			if (m.getPrinBalance().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			if (m.getAssetClass2().length() == 0) {
				throw new LogicException(titaVo, "E0015", "MonthlyFacBal AssetClass2 " + m.toString()); // 檢查錯誤
			}
			switch (m.getClCode1()) {
			case 1:
			case 2:
				if ("N".equals(m.getGovProjectFlag())) {
					loanType = "C";
					break;
				} else {
					loanType = "G";
				}
				break;
			case 3:
			case 4:
				loanType = "D";
				break;
			case 5:
				loanType = "A";
				break;
			case 9:
				loanType = "B";
				break;
			default:
				loanType = "C";
				break;
			}
			// load 放款餘額至 LM055 List
			addFacBalToLM055List(loanType, m);
			// 加總五分類金額
			sumfacBal(loanType, m.getAssetClass2(), m.getPrinBalance().subtract(m.getLawAmount()));
			if (m.getLawAmount().compareTo(BigDecimal.ZERO) > 0) {
				sumfacBal(loanType, m.getLawAssetClass(), m.getLawAmount());
			}
		}
		// 計算 & 放入lLM055AssetLoss
		for (MonthlyFacBal f : this.facBalSumList) {
			// 借用欄位 : loanType => BuildingFlag
			// C.不動產抵押放款的正常放款由總額減去其他計算
			if (!("C".equals(f.getBuildingFlag()) && "1".equals(f.getAssetClass2().substring(0, 1)))) {
				computeStorageAmt(f.getBuildingFlag(), f.getAssetClass2(), f.getPrinBalance());
			}
		}

		// Load 折溢價與費用
		for (MonthlyLM052AssetClass t : lLM052AssetClass) {
			if ("6".equals(t.getAssetClassNo().substring(0, 1))) {
				addLM052ToLM055List("Z", t.getAssetClassNo(), t.getLoanBal(), t.getStorageAmt());
			}
		}
		// Load 應收利息(本金不計，備呆金額放五分類2
		for (MonthlyLM052AssetClass t : lLM052AssetClass) {
			if ("7".equals(t.getAssetClassNo().substring(0, 1))) {
				addLM052ToLM055List("C", t.getAssetClassNo(), BigDecimal.ZERO, t.getStorageAmt());
			}
		}
		// 調整提存差額至C.不動產抵押放款 H(正常放款)
		for (MonthlyLM052AssetClass e : lLM052AssetClass) {
			if (!"6".equals(e.getAssetClassNo().substring(0, 1)) && !"7".equals(e.getAssetClassNo().substring(0, 1))) {
				addLM052ToLM055List("C", e.getAssetClassNo(), BigDecimal.ZERO, e.getStorageAmt());
			}
		}
		// 調整提存差額至C.不動產抵押放款
		for (MonthlyLM055AssetLoss e : this.lLM055AssetLoss) {
			if (!"Z".equals(e.getLoanType())) {
				if (!("C".equals(e.getLoanType()))) {
					addLM052ToLM055List("C", "1", BigDecimal.ZERO, BigDecimal.ZERO.subtract(e.getReserveLossAmt1()));
					addLM052ToLM055List("C", "2", BigDecimal.ZERO, BigDecimal.ZERO.subtract(e.getReserveLossAmt2()));
					addLM052ToLM055List("C", "3", BigDecimal.ZERO, BigDecimal.ZERO.subtract(e.getReserveLossAmt3()));
					addLM052ToLM055List("C", "4", BigDecimal.ZERO, BigDecimal.ZERO.subtract(e.getReserveLossAmt4()));
					addLM052ToLM055List("C", "5", BigDecimal.ZERO, BigDecimal.ZERO.subtract(e.getReserveLossAmt5()));
				}
			}
		}

		for (MonthlyLM055AssetLoss n : this.lLM055AssetLoss) {
			n.setMonthlyLM055AssetLossId(new MonthlyLM055AssetLossId(yearMonth, n.getLoanType()));
			n.setYearMonth(yearMonth);
			this.info("LM055AssetLoss=" + n.toString());
		}

		this.info("updMonthlyLM055AssetLoss finished.");
	}

	private void computeStorageAmt(String loanType, String assetClassNo2, BigDecimal loanAmt) {
		BigDecimal storageRate = BigDecimal.ZERO;
		// 借用欄位 : loanType => BuildingFlag
		for (MonthlyLM052AssetClass t : this.lLM052AssetClass) {
			if (assetClassNo2.equals(t.getAssetClassNo())) {
				storageRate = t.getStorageRate();
				break;
			}
		}
		//
		for (MonthlyLM052AssetClass t : this.lLM052AssetClass) {
			if (assetClassNo2.equals(t.getAssetClassNo())) {
				storageRate = t.getStorageRate();
				break;
			}
		}
		BigDecimal storageAmt = loanAmt.multiply(storageRate).setScale(0, RoundingMode.HALF_UP);
		addLM052ToLM055List(loanType, assetClassNo2, loanAmt, storageAmt);
		this.info("computeStorageAmt loanType=" + loanType + ", loanAmt=" + loanAmt + ", storageRate=" + storageRate
				+ ", storageAmt=" + storageAmt);
	}

	private void addLM052ToLM055List(String loanType, String assetClassNo, BigDecimal loanAmt, BigDecimal storageAmt) {
		for (MonthlyLM055AssetLoss t : this.lLM055AssetLoss) {
			if (loanType.equals(t.getLoanType())) {
				switch (assetClassNo.substring(0, 1)) {
				case "1":
					t.setReserveLossAmt1(t.getReserveLossAmt1().add(storageAmt));
					break;
				case "2":
					t.setLoanAmountClass2(t.getLoanAmountClass2().add(loanAmt));
					t.setReserveLossAmt2(t.getReserveLossAmt2().add(storageAmt));
					break;
				case "3":
					t.setLoanAmountClass3(t.getLoanAmountClass3().add(loanAmt));
					t.setReserveLossAmt3(t.getReserveLossAmt3().add(storageAmt));
					break;
				case "4":
					t.setLoanAmountClass4(t.getLoanAmountClass4().add(loanAmt));
					t.setReserveLossAmt4(t.getReserveLossAmt4().add(storageAmt));
					break;
				case "5":
					t.setLoanAmountClass5(t.getLoanAmountClass5().add(loanAmt));
					t.setReserveLossAmt5(t.getReserveLossAmt5().add(storageAmt));
					break;
				case "6": // 61:擔保放款折溢價, 62:催收折溢價與催收費用
					if ("61".equals(assetClassNo)) {
						t.setNormalAmount(t.getNormalAmount().add(loanAmt)); // 正常放款
						t.setReserveLossAmt1(t.getReserveLossAmt1().add(storageAmt)); // 備呆金額五分類1
					} else {
						t.setOverdueAmount(t.getOverdueAmount().add(loanAmt)); // 逾期放款
						t.setReserveLossAmt2(t.getReserveLossAmt2().add(storageAmt)); // 備呆金額五分類2
					}
					break;
				case "7":
					t.setReserveLossAmt2(t.getReserveLossAmt2().add(storageAmt));
					break;
				}
				break;
			}
			break;
		}
	}

	private void addFacBalToLM055List(String loanType, MonthlyFacBal m) {
		for (MonthlyLM055AssetLoss t : this.lLM055AssetLoss) {
			if (loanType.equals(t.getLoanType())) {
				if ("990".equals(m.getAcctCode())) {
					t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
					t.setLoanAmount990(t.getLoanAmount990().add(m.getPrinBalance()));
				} else {
					switch (m.getOvduTerm()) {
					case 0:
						if ("60".equals(m.getProdNo()) || "61".equals(m.getProdNo()) || "62".equals(m.getProdNo())) {
							t.setObserveAmount(t.getObserveAmount().add(m.getPrinBalance()));
							t.setLoanAmountNeg0(t.getLoanAmountNeg0().add(m.getPrinBalance()));
						} else {
							t.setNormalAmount(t.getNormalAmount().add(m.getPrinBalance()));
							t.setLoanAmountNor0(t.getLoanAmountNor0().add(m.getPrinBalance()));
						}
						break;
					case 1:
						t.setObserveAmount(t.getObserveAmount().add(m.getPrinBalance()));
						t.setLoanAmount1(t.getLoanAmount1().add(m.getPrinBalance()));
						break;
					case 2:
						t.setObserveAmount(t.getObserveAmount().add(m.getPrinBalance()));
						t.setLoanAmount2(t.getLoanAmount2().add(m.getPrinBalance()));
						break;
					case 3:
						t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
						t.setLoanAmount3(t.getLoanAmount3().add(m.getPrinBalance()));
						break;
					case 4:
						t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
						t.setLoanAmount4(t.getLoanAmount4().add(m.getPrinBalance()));
						break;
					case 5:
						t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
						t.setLoanAmount5(t.getLoanAmount5().add(m.getPrinBalance()));
						break;
					default:
						t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
						t.setLoanAmount6(t.getLoanAmount6().add(m.getPrinBalance()));
						break;
					}
					break;
				}
			}
		}
	}

	private void sumfacBal(String loanType, String assetClass2, BigDecimal loanBal) {
		// 借用欄位 : loanType => BuildingFlag
		boolean isNew = true;
		for (MonthlyFacBal t : this.facBalSumList) {
			if (loanType.equals(t.getBuildingFlag()) && assetClass2.equals(t.getAssetClass2())) {
				t.setPrinBalance(t.getPrinBalance().add(loanBal));
				isNew = false;
				break;
			}
		}
		if (isNew) {
			MonthlyFacBal a = new MonthlyFacBal();
			a.setBuildingFlag(loanType);
			a.setAssetClass2(assetClass2);
			a.setPrinBalance(a.getPrinBalance().add(loanBal));
			this.facBalSumList.add(a);
		}
	}
}