package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyLM042Statis;
import com.st1.itx.db.domain.MonthlyLM042StatisId;
import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.domain.MonthlyLM055AssetLossId;
import com.st1.itx.db.service.CdCommService;
import com.st1.itx.db.service.CoreAcMainService;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.MonthlyLM042StatisService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.db.service.MonthlyLM055AssetLossService;
import com.st1.itx.db.service.springjpa.cm.LM042ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LM051ServiceImpl;
import com.st1.itx.eum.ContentName;
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
	MonthlyLM052AssetClassService sLM052AssetClassService;
	@Autowired
	MonthlyLM052LoanAssetService sLM052LoanAssetService;
	@Autowired
	MonthlyLM052OvduService sLM052OvduService;
	@Autowired
	MonthlyLM052LossService sLM052LossService;
	@Autowired
	MonthlyLM042StatisService sMonthlyLM042StatisService;
	@Autowired
	MonthlyLM055AssetLossService sLM055AssetLossService;
	@Autowired
	CoreAcMainService sCoreAcMainService;
	@Autowired
	CdCommService sCdCommService;
	@Autowired
	LM051ServiceImpl lM051ServiceImpl;
	@Autowired
	LM042ServiceImpl lM042ServiceImpl;

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

	private int CountAll = 0;
	private int CountS = 0;
	private String extension = "";
	private BigDecimal oProDiff = BigDecimal.ZERO; // 專案差異
	private BigDecimal oApprovedLoss = BigDecimal.ZERO; // 會計核准備呆
	private BigDecimal oApprovedLossDiff = BigDecimal.ZERO;
	private int iYearMonth = 0;
	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();
	private List<MonthlyLM052AssetClass> lMonthlyLM052AssetClass = new ArrayList<MonthlyLM052AssetClass>();
	private List<MonthlyLM055AssetLoss> lMonthlyLM055AssetLoss = new ArrayList<MonthlyLM055AssetLoss>();
	private List<MonthlyLM042Statis> lMonthlyLM042Statis = new ArrayList<MonthlyLM042Statis>();
	private MonthlyLM052Loss tMonthlyLM052Loss = new MonthlyLM052Loss();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7205p ");
		this.totaVo.init(titaVo);

		iYearMonth = parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

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

		String extensionAll[] = filename.split("\\.");

		extension = extensionAll[extensionAll.length - 1];

		this.info("file extension=" + extension);
		if ("xlsx".equals(extension) || "xls".equals(extension)) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			// 切資料
			setValueFromFileExcelNew(titaVo, iYearMonth);
		} else if ("csv".equals(extension.toLowerCase())) {

			setValueFromFile(titaVo, dataLineList, iYearMonth);

		} else {

			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";

			throw new LogicException(titaVo, "E0014", ErrorMsg);

		}

		CountAll = occursList.size();
		CountS = 0;

		// 維護MonthlyFacBal
		Slice<MonthlyFacBal> slMothlyFacBal = tMothlyFacBalService.findYearMonthAll(iYearMonth, 0, Integer.MAX_VALUE,
				titaVo);

		if (slMothlyFacBal == null) {
			throw new LogicException(titaVo, "E0001", "MothlyFacBal"); // 查詢資料不存在
		} else {

			List<MonthlyFacBal> lMonthlyFacBal = updateMonthlyFacBal(slMothlyFacBal, titaVo);

			if (lMonthlyFacBal.size() > 0) {

				try {
					tMothlyFacBalService.updateAll(lMonthlyFacBal, titaVo);
				} catch (DBException e) {

					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}

				this.batchTransaction.commit();

				// 切換環境
				changeDBEnv(titaVo);

				try {
					tMothlyFacBalService.updateAll(lMonthlyFacBal, titaVo);
				} catch (DBException e) {

					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}

				this.batchTransaction.commit();

				// 切換環境
				changeDBEnv(titaVo);
			}

		}

		// 維護Ifrs9FacData
		Slice<Ifrs9FacData> sIfrs9FacData = tIfrs9FacDataService.findAll(0, Integer.MAX_VALUE, titaVo);

		if (sIfrs9FacData == null) {
//			throw new LogicException(titaVo, "E0001", "Ifrs9FacData1"); // 查詢資料不存在
			this.info("E0001 ... Ifrs9FacData"); // 查詢資料不存在
		} else {

			List<Ifrs9FacData> tIfrs9FacData = updateIfrs9FacData(sIfrs9FacData, titaVo);

			if (tIfrs9FacData.size() > 0) {
				try {
					tIfrs9FacDataService.updateAll(tIfrs9FacData, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				this.batchTransaction.commit();
			}
		}

		// 維護Ias34Ap(連線)
		Slice<Ias34Ap> sIas34Ap = tIas34ApService.findAll(0, Integer.MAX_VALUE, titaVo);

		if (sIas34Ap == null) {
//			throw new LogicException(titaVo, "E0001", "Ias34Ap1"); // 查詢資料不存在
			this.info("E0001 ... Ias34Ap"); // 查詢資料不存在
		} else {

			List<Ias34Ap> tIas34Ap = updateIas34Ap(sIas34Ap, titaVo);

			if (tIas34Ap.size() > 0) {

				try {
					tIas34ApService.updateAll(tIas34Ap, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				this.batchTransaction.commit();

				// 切換環境
				changeDBEnv(titaVo);

				try {
					tIas34ApService.updateAll(tIas34Ap, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				this.batchTransaction.commit();
				// 切換環境
				changeDBEnv(titaVo);
			}
		}

		// 維護LoanIfrs9Ap
		Slice<LoanIfrs9Ap> sLoanIfrs9Ap = tLoanIfrs9ApService.findAll(0, Integer.MAX_VALUE, titaVo);

		if (sLoanIfrs9Ap == null) {
//			throw new LogicException(titaVo, "E0001", "LoanIfrs9Ap1"); // 查詢資料不存在
			this.info("E0001 ... LoanIfrs9Ap"); // 查詢資料不存在

		} else {

			List<LoanIfrs9Ap> tLoanIfrs9Ap = updateLoanIfrs9Ap(sLoanIfrs9Ap, titaVo);

			if (tLoanIfrs9Ap.size() > 0) {
				try {
					tLoanIfrs9ApService.updateAll(tLoanIfrs9Ap, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				this.batchTransaction.commit();

				// 切換環境
				changeDBEnv(titaVo);

				try {
					tLoanIfrs9ApService.updateAll(tLoanIfrs9Ap, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				this.batchTransaction.commit();

				changeDBEnv(titaVo);
			}
		}

		updLM052ReportSPAndMonthlyFacBalData(titaVo, iYearMonth);
		this.batchTransaction.commit();

		changeDBEnv(titaVo);
		updLM052ReportSPAndMonthlyFacBalData(titaVo, iYearMonth);
		this.batchTransaction.commit();

		// 更新MonthlyLM052Loss
		changeDBEnv(titaVo);
		updMonthlyLM052Loss(titaVo, iYearMonth);
		this.batchTransaction.commit();

		changeDBEnv(titaVo);
		try {
			sLM052LossService.update(tMonthlyLM052Loss, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.batchTransaction.commit();

		// 更新MonthlyLM042Statis LM042RBC統計數
		changeDBEnv(titaVo);
		updMonthlyLM042Statis(titaVo, iYearMonth);
		this.batchTransaction.commit();

		changeDBEnv(titaVo);
		try {
			sMonthlyLM042StatisService.updateAll(this.lMonthlyLM042Statis, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		// 更新MonthlyLM055AssetLoss LM055重要放款餘額明細表
		changeDBEnv(titaVo);
		updMonthlyLM055AssetLoss(titaVo, iYearMonth);
		this.batchTransaction.commit();

		changeDBEnv(titaVo);
		try {
			sLM055AssetLossService.updateAll(this.lMonthlyLM055AssetLoss, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		this.batchTransaction.commit();

		// 重產LM051報表
		titaVo.setBatchJobId("jLM051");

		titaVo.setDataBaseOnOrg();// 還原原本的環境

		String note = "總筆數：" + CountAll + ",更新成功筆數：" + CountS;

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

		sLM052AssetClassService.Usp_L9_MonthlyLM052AssetClass_Ins(yearMonth, empNo, "", titaVo);
		sLM052LoanAssetService.Usp_L9_MonthlyLM052LoanAsset_Ins(yearMonth, empNo, "", titaVo);
		sLM052OvduService.Usp_L9_MonthlyLM052Ovdu_Ins(yearMonth, empNo, "", titaVo);
		// 更新MonthlyFacBal GovProjectFlag,BuildingFlag,SpecialAssetFlag

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String txcd = titaVo.getTxcd();
		this.info("txcd=" + txcd);
		tMothlyFacBalService.Usp_L7_UploadToMonthlyFacBal_Upd(yearMonth, txcd, empNo, "", titaVo);

		this.info("upd LM052 SP finished.");
	}

	/**
	 * 更新MonthlyLLM042Statis LM042RBC統計數
	 * 
	 * @param titaVo    TitaVo
	 * @param yearMonth 西元年月
	 * @throws LogicException ...
	 */
	public void updMonthlyLM042Statis(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info("updMonthlyLM042Statis ...");

		// Load MonthlyLM052AssetClass LM052資產分類表
		Slice<MonthlyLM052AssetClass> sLM052AssetClass = sLM052AssetClassService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (sLM052AssetClass == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM052AssetClass LM052資產分類表"); // 查詢資料不存在
		}
		this.lMonthlyLM052AssetClass = sLM052AssetClass.getContent();

		List<Map<String, String>> statisticsList2 = null;
		List<Map<String, String>> statisticsList3 = null;

		try {
			statisticsList2 = lM042ServiceImpl.findStatistics2(titaVo, yearMonth);
			statisticsList3 = lM042ServiceImpl.findStatistics3(titaVo, yearMonth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		if (statisticsList2.size() == 0 && statisticsList3.size() == 0) {
			return;
		}
		// 刪除舊表資料
		Slice<MonthlyLM042Statis> slMonthlyLM042Statis = sMonthlyLM042StatisService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM042Statis != null) {
			try {
				sMonthlyLM042StatisService.deleteAll(slMonthlyLM042Statis.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0004", e.getErrorMsg());
			}
		}
		// 專案貸款
		BigDecimal sProAmt = BigDecimal.ZERO;
		BigDecimal sProLoan = BigDecimal.ZERO;

		for (Map<String, String> lm42Vo2 : statisticsList2) {
			String item = lm42Vo2.get("Item");
			String relCd = lm42Vo2.get("RelCd");
			int assetClass = parse.stringToInteger(lm42Vo2.get("AssetClass"));
			BigDecimal amt = parse.stringToBigDecimal(lm42Vo2.get("AMT"));
			if (item.length() == 1) {
				MonthlyLM042Statis tMonthlyLM042Statis = new MonthlyLM042Statis();
				tMonthlyLM042Statis.setLoanItem(item);
				tMonthlyLM042Statis.setRelatedCode(relCd);
				tMonthlyLM042Statis.setAssetClass("" + assetClass);
				tMonthlyLM042Statis.setLoanBal(amt);
				addStatis(tMonthlyLM042Statis, titaVo);
			}
			// 專案貸款
			if ("ProLoan".equals(item)) {
				sProLoan = sProLoan.add(amt);

			}
			// 服務課專案數字
			if ("ProAmt".equals(item)) {
				sProAmt = sProAmt.add(amt);
			}
			// 會計核准備呆 P13
			if ("ApprovedLoss".equals(item)) {
				oApprovedLoss = oApprovedLoss.add(amt);

			}
		}
		// 專案差異
		oProDiff = sProAmt.subtract(sProLoan);
		// 1.5% 提存差異 oApprovedLossDiff(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass)
		oApprovedLossDiff = oApprovedLoss;
		for (MonthlyLM052AssetClass t : this.lMonthlyLM052AssetClass) {
			if (!"7".equals(t.getAssetClassNo())) {
				oApprovedLossDiff = oApprovedLossDiff.subtract(t.getStorageAmt());
			}
		}

		this.info("ProAmt=" + sProAmt + ", ProLoan=" + sProLoan + ", oProDiff=" + oProDiff);
		this.info("oApprovedLossDiff=" + oApprovedLossDiff);

		BigDecimal cHouseAndRepair = BigDecimal.ZERO;
		BigDecimal zHouseAndRepair = BigDecimal.ZERO;

		// P欄位 (購置住宅+修繕貸款)
		for (Map<String, String> lm42Vo3 : statisticsList3) {

			String type = lm42Vo3.get("TYPE");
			BigDecimal amt = parse.stringToBigDecimal(lm42Vo3.get("AMT"));

			// 擔保品類別
			switch (type) {
			case "C":
				cHouseAndRepair = cHouseAndRepair.add(amt);
				break;
			case "Z":
				zHouseAndRepair = zHouseAndRepair.add(amt);
				break;
			}
		}
		this.info("cHouseAndRepair=" + cHouseAndRepair + ", zHouseAndRepair=" + zHouseAndRepair);
		MonthlyLM042Statis tMonthlyLM042Statis = new MonthlyLM042Statis();
		tMonthlyLM042Statis.setLoanItem("C");
		tMonthlyLM042Statis.setRelatedCode("N");
		tMonthlyLM042Statis.setAssetClass("1");
		tMonthlyLM042Statis.setLoanBal(BigDecimal.ZERO.subtract(oProDiff));
		tMonthlyLM042Statis.setHouseAndRepairBal(cHouseAndRepair);
		addStatis(tMonthlyLM042Statis, titaVo);
		tMonthlyLM042Statis = new MonthlyLM042Statis();
		tMonthlyLM042Statis.setLoanItem("Z");
		tMonthlyLM042Statis.setRelatedCode("N");
		tMonthlyLM042Statis.setAssetClass("1");
		tMonthlyLM042Statis.setLoanBal(BigDecimal.ZERO.add(oProDiff));
		tMonthlyLM042Statis.setHouseAndRepairBal(zHouseAndRepair);
		addStatis(tMonthlyLM042Statis, titaVo);

		BigDecimal percent0_005 = new BigDecimal("0.005"); // 0.5%
		BigDecimal percent0_015 = new BigDecimal("0.015");// 1.5%
		BigDecimal percent0_02 = new BigDecimal("0.02");// 2%
		BigDecimal percent0_1 = new BigDecimal("0.1");// 10%
		BigDecimal percent0_5 = new BigDecimal("0.5");// 50%
		BigDecimal percent1 = new BigDecimal("1");// 100%

		for (MonthlyLM042Statis t2 : lMonthlyLM042Statis) {
			int assetClass = parse.stringToInteger(t2.getAssetClass());
			String item = t2.getLoanItem();
			String relCd = t2.getRelatedCode();
			BigDecimal lossAmt = BigDecimal.ZERO;
			BigDecimal loanBal = t2.getLoanBal();
			switch (assetClass) {
			case 1:
				if ("C".equals(item) && "N".equals(relCd)) {
					lossAmt = (loanBal.subtract(cHouseAndRepair)).multiply(percent0_005)
							.add((cHouseAndRepair).multiply(percent0_015)).setScale(0, BigDecimal.ROUND_HALF_UP);
				} else if ("Z".equals(item) && "N".equals(relCd)) {
					lossAmt = (loanBal.subtract(zHouseAndRepair)).multiply(percent0_005)
							.add((zHouseAndRepair).multiply(percent0_015)).setScale(0, BigDecimal.ROUND_HALF_UP);
				} else {
					lossAmt = loanBal.multiply(percent0_005).setScale(0, BigDecimal.ROUND_HALF_UP);
				}
				break;
			case 2:
				lossAmt = loanBal.multiply(percent0_02).setScale(0, BigDecimal.ROUND_HALF_UP);
				break;
			case 3:
				lossAmt = loanBal.multiply(percent0_1).setScale(0, BigDecimal.ROUND_HALF_UP);
				break;
			case 4:
				lossAmt = loanBal.multiply(percent0_5).setScale(0, BigDecimal.ROUND_HALF_UP);
				break;
			case 5:
				lossAmt = loanBal.multiply(percent1).setScale(0, BigDecimal.ROUND_HALF_UP);
				break;
			}
			t2.setReserveLossAmt(lossAmt);
			// 放款餘額扣除備呆,
			// LoanItem=C RelatedCode=N 減1.5%差異數(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass)
			t2.setNetAmt(loanBal.subtract(lossAmt));
			if ("C".equals(item) && "N".equals(relCd)) {
				t2.setNetAmt(t2.getNetAmt().subtract(oApprovedLossDiff));
			}
		}

		// 提存差額
		for (MonthlyLM042Statis t4 : lMonthlyLM042Statis) {
			int assetClass = parse.stringToInteger(t4.getAssetClass());
			if ("C".equals(t4.getLoanItem()) && "N".equals(t4.getRelatedCode())) {
				BigDecimal lm042StoreAmt = BigDecimal.ZERO;
				for (MonthlyLM042Statis t5 : lMonthlyLM042Statis) {
					if (parse.stringToInteger(t5.getAssetClass()) == assetClass) {
						lm042StoreAmt = lm042StoreAmt.add(t5.getReserveLossAmt());
					}
				}
				BigDecimal lm052StoreAmt = BigDecimal.ZERO;
				for (MonthlyLM052AssetClass t1 : this.lMonthlyLM052AssetClass) {
					switch (assetClass) {
					case 1:
						if ("1".equals(t1.getAssetClassNo().substring(0, 1)) || "61".equals(t1.getAssetClassNo())) {
							lm052StoreAmt = lm052StoreAmt.add(t1.getStorageAmt());
						}
						break;
					case 2:
						if ("2".equals(t1.getAssetClassNo().substring(0, 1)) || "7".equals(t1.getAssetClassNo())) {
							lm052StoreAmt = lm052StoreAmt.add(t1.getStorageAmt());
						}
						break;
					case 3:
						if ("3".equals(t1.getAssetClassNo())) {
							lm052StoreAmt = lm052StoreAmt.add(t1.getStorageAmt());
						}
						break;
					case 4:
						if ("4".equals(t1.getAssetClassNo())) {
							lm052StoreAmt = lm052StoreAmt.add(t1.getStorageAmt());
						}
						break;
					case 5:
						if ("5".equals(t1.getAssetClassNo())) {
							lm052StoreAmt = lm052StoreAmt.add(t1.getStorageAmt());
						}
						break;
					}
				}
				t4.setReserveLossDiff(lm042StoreAmt.subtract(lm052StoreAmt));
			}
		}
		for (MonthlyLM042Statis n : this.lMonthlyLM042Statis) {
			n.setMonthlyLM042StatisId(
					new MonthlyLM042StatisId(yearMonth, n.getLoanItem(), n.getRelatedCode(), n.getAssetClass()));
			n.setYearMonth(yearMonth);
			this.info(n.toString());
		}

		try {
			sMonthlyLM042StatisService.insertAll(this.lMonthlyLM042Statis, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		this.info("updMonthlyLM042Statis finished.");
	}

	public void addStatis(MonthlyLM042Statis t, TitaVo titaVo) {
		Boolean isfind = false;
		for (MonthlyLM042Statis t2 : lMonthlyLM042Statis) {
			if (t.getLoanItem().equals(t2.getLoanItem()) && t.getRelatedCode().equals(t2.getRelatedCode())
					&& t.getAssetClass().equals(t2.getAssetClass())) {
				isfind = true;
				t2.setLoanBal(t2.getLoanBal().add(t.getLoanBal()));
				t2.setReserveLossAmt(t2.getReserveLossAmt().add(t.getReserveLossAmt()));
				t2.setReserveLossDiff(t2.getReserveLossDiff().add(t.getReserveLossDiff()));
				t2.setHouseAndRepairBal(t2.getHouseAndRepairBal().add(t.getHouseAndRepairBal()));
			}
		}
		if (!isfind) {
			MonthlyLM042StatisId monthlyLM042StatisId = new MonthlyLM042StatisId();
			monthlyLM042StatisId.setYearMonth(t.getYearMonth());
			monthlyLM042StatisId.setLoanItem(t.getLoanItem());
			monthlyLM042StatisId.setRelatedCode(t.getRelatedCode());
			monthlyLM042StatisId.setAssetClass(t.getAssetClass());
			t.setMonthlyLM042StatisId(monthlyLM042StatisId);
			lMonthlyLM042Statis.add(t);
		}
	}

	/**
	 * 更新MonthlyLM055AssetLoss LM055重要放款餘額明細表
	 * 
	 * @param titaVo    TitaVo
	 * @param yearMonth 西元年月
	 * @throws LogicException ...
	 */
	public void updMonthlyLM055AssetLoss(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info("updMonthlyLM055AssetLoss ...");
		// Load MonthlyLM042Statis LM042RBC統計數
		Slice<MonthlyLM042Statis> slMonthlyLM042Statis = sMonthlyLM042StatisService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM042Statis == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM042Statis LM042RBC統計數"); // 查詢資料不存在
		}
		this.lMonthlyLM042Statis = slMonthlyLM042Statis.getContent();
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
		if (tTempVo.get("LoanBal") == null) {
			throw new LogicException(titaVo, "E0001", "CdComm 政策性專案貸款"); // 查詢資料不存在
		}
		BigDecimal govProjectBal = BigDecimal.ZERO;
		for (MonthlyFacBal m : slMothlyFacBal.getContent()) {
			if ("Y".equals(m.getGovProjectFlag()) || "C".equals(m.getGovProjectFlag())) {
				govProjectBal = govProjectBal.add(m.getPrinBalance());
			}
		}

		BigDecimal govProjectAdjustAmt = parse.stringToBigDecimal(tTempVo.get("LoanBal")).subtract(govProjectBal);
		this.info("govProjectBal(CdComm)=" + tTempVo.get("LoanBal"));
		this.info("govProjectBal(FacBal)=" + govProjectBal);
		this.info("govProjectAdjustAmt=" + govProjectAdjustAmt);
// iFRS9AdjustAmt IFRS9增提金額 = 五分類備呆總金額- 會計部核定備抵損失(MonthlyLM052Loss.ApprovedLoss)		                              
		MonthlyLM052Loss tMonthlyLM052Loss = sLM052LossService.findById(yearMonth, titaVo);
		if (tMonthlyLM052Loss == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM052Loss LM052備抵損失資料檔"); // 查詢資料不存在
		}
		BigDecimal iFRS9AdjustAmt = tMonthlyLM052Loss.getAssetEvaTotal().subtract(tMonthlyLM052Loss.getApprovedLoss());

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
		this.lMonthlyLM055AssetLoss = new ArrayList<MonthlyLM055AssetLoss>();

// 政策性專案貸款調整		                                                                                                              
		// G.政策性專案貸款：GovProjectAdjustAmt
		// C.不動產抵押放款 : 0 - GovProjectAdjustAmt
		MonthlyLM055AssetLoss l = new MonthlyLM055AssetLoss();
		l.setLoanType("A");
		this.lMonthlyLM055AssetLoss.add(l);

		l = new MonthlyLM055AssetLoss();
		l.setLoanType("B");
		this.lMonthlyLM055AssetLoss.add(l);

		l = new MonthlyLM055AssetLoss();
		l.setLoanType("C");
		l.setGovProjectAdjustAmt(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		l.setNormalAmount(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		l.setLoanAmountNor0(BigDecimal.ZERO.subtract(govProjectAdjustAmt));
		l.setIFRS9AdjustAmt(iFRS9AdjustAmt);
		this.lMonthlyLM055AssetLoss.add(l);

		l = new MonthlyLM055AssetLoss();
		l.setLoanType("D");
		this.lMonthlyLM055AssetLoss.add(l);

		l = new MonthlyLM055AssetLoss();
		l.setLoanType("E");
		l.setGovProjectAdjustAmt(govProjectAdjustAmt);
		l.setNormalAmount(govProjectAdjustAmt);
		l.setLoanAmountNor0(govProjectAdjustAmt);
		this.lMonthlyLM055AssetLoss.add(l);

		l = new MonthlyLM055AssetLoss();
		l.setLoanType("F");
		this.lMonthlyLM055AssetLoss.add(l);

		// Load LM042Statis
		for (MonthlyLM042Statis t : this.lMonthlyLM042Statis) {
			// 折溢價與費用含餘額，其他僅Load備呆金額
			if ("6".equals(t.getAssetClass())) {
				addLM044ToLM055List("F", t.getAssetClass(), t.getLoanBal(),
						t.getReserveLossAmt().subtract(t.getReserveLossDiff()));
			} else {
				addLM044ToLM055List("Z".equals(t.getLoanItem()) ? "E" : t.getLoanItem(), t.getAssetClass(),
						BigDecimal.ZERO, t.getReserveLossAmt().subtract(t.getReserveLossDiff()));
			}
		}

// 按放款種類累計MonthlyFacBal 至 LM055List                                                                                
		String loanType;
		for (MonthlyFacBal m : slMothlyFacBal.getContent()) {
			if (m.getPrinBalance().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			switch (m.getClCode1()) {
			case 1:
			case 2:
				if ("Y".equals(m.getGovProjectFlag()) || "C".equals(m.getGovProjectFlag())) {
					loanType = "E";
					break;
				} else {
					loanType = "C";
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
		}

		for (MonthlyLM055AssetLoss n : this.lMonthlyLM055AssetLoss) {
			n.setMonthlyLM055AssetLossId(new MonthlyLM055AssetLossId(yearMonth, n.getLoanType()));
			n.setYearMonth(yearMonth);
			this.info(n.toString());
		}

		try {
			sLM055AssetLossService.insertAll(this.lMonthlyLM055AssetLoss, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		this.info("updMonthlyLM055AssetLoss finished.");
	}

	private void addLM044ToLM055List(String loanType, String assetClass, BigDecimal loanAmt, BigDecimal storageAmt) {
		this.info("addLM052ToLM055List loanType=" + loanType + ", assetClass=" + assetClass + ", loanAmt=" + loanAmt
				+ ", storageAmt=" + storageAmt);
		for (MonthlyLM055AssetLoss t : this.lMonthlyLM055AssetLoss) {
			if (loanType.equals(t.getLoanType())) {
				switch (assetClass) {
				case "1":
					t.setNormalAmount(t.getNormalAmount().add(loanAmt));
					t.setReserveLossAmt1(t.getReserveLossAmt1().add(storageAmt));
					break;
				case "2":
					t.setOverdueAmount(t.getOverdueAmount().add(loanAmt));
					t.setLoanAmountClass2(t.getLoanAmountClass2().add(loanAmt));
					t.setReserveLossAmt2(t.getReserveLossAmt2().add(storageAmt));
					break;
				case "3":
					t.setOverdueAmount(t.getOverdueAmount().add(loanAmt));
					t.setLoanAmountClass3(t.getLoanAmountClass3().add(loanAmt));
					t.setReserveLossAmt3(t.getReserveLossAmt3().add(storageAmt));
					break;
				case "4":
					t.setOverdueAmount(t.getOverdueAmount().add(loanAmt));
					t.setLoanAmountClass4(t.getLoanAmountClass4().add(loanAmt));
					t.setReserveLossAmt4(t.getReserveLossAmt4().add(storageAmt));
					break;
				case "5":
					t.setOverdueAmount(t.getOverdueAmount().add(loanAmt));
					t.setLoanAmountClass5(t.getLoanAmountClass5().add(loanAmt));
					t.setReserveLossAmt5(t.getReserveLossAmt5().add(storageAmt));
					break;
				}
				this.info("addLM052ToLM055List " + t.toString());
				break;
			}
		}
	}

	private void addFacBalToLM055List(String loanType, MonthlyFacBal m) {
		for (MonthlyLM055AssetLoss t : this.lMonthlyLM055AssetLoss) {
			if (loanType.equals(t.getLoanType())) {
				if ("990".equals(m.getAcctCode())) {
					t.setOverdueAmount(t.getOverdueAmount().add(m.getPrinBalance()));
					t.setLoanAmount990(t.getLoanAmount990().add(m.getPrinBalance()));
				} else {
					switch (m.getOvduTerm()) {
					case 0:
						if ("60".equals(m.getProdNo()) || "61".equals(m.getProdNo()) || "62".equals(m.getProdNo())) {
							t.setLoanAmountNeg0(t.getLoanAmountNeg0().add(m.getPrinBalance()));
							t.setObserveAmount(t.getObserveAmount().add(m.getPrinBalance()));
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

	private void updMonthlyLM052Loss(TitaVo titaVo, int yearMonth) throws LogicException {

		try {
			tMonthlyLM052Loss = sLM052LossService.findById(yearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("llMonthlyLM052Loss findAll error = " + errors.toString());
		}

		// 五類資產評估合計
		BigDecimal assetClassTotal = BigDecimal.ZERO;

		Slice<MonthlyLM052AssetClass> sLM052AssetClass = sLM052AssetClassService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (sLM052AssetClass == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM052AssetClass LM052資產分類表"); // 查詢資料不存在
		}

		for (MonthlyLM052AssetClass t : sLM052AssetClass.getContent()) {
			assetClassTotal = assetClassTotal.add(t.getStorageAmt());
		}
		// 會計部備抵損失提撥
		BigDecimal lossTotal = BigDecimal.ZERO;

		List<Map<String, String>> lM051List = null;

		try {

			lM051List = lM051ServiceImpl.findAll(titaVo, yearMonth, yearMonth, 6);

		} catch (Exception e) {

			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM051ServiceImpl.findAll error = " + errors.toString());

		}

		if (lM051List == null) {
			throw new LogicException(titaVo, "E0001", "LM051ServiceImpl CoreAcMain 會計部備抵損失提撥"); // 查詢資料不存在
		}

		lossTotal = parse.stringToBigDecimal(lM051List.get(0).get("LossTotal"));

		// 判斷有無當月資料
		if (tMonthlyLM052Loss == null) {
			this.info("insert data");
			tMonthlyLM052Loss = new MonthlyLM052Loss();

			tMonthlyLM052Loss.setYearMonth(yearMonth);
			tMonthlyLM052Loss.setApprovedLoss(lossTotal);
			tMonthlyLM052Loss.setAssetEvaTotal(assetClassTotal);

			try {

				sLM052LossService.insert(tMonthlyLM052Loss, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			tMonthlyLM052Loss.setApprovedLoss(lossTotal);
			tMonthlyLM052Loss.setAssetEvaTotal(assetClassTotal);

			try {

				sLM052LossService.update(tMonthlyLM052Loss, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 更新 MonthlyFacBal.AssetClass
	 */
	private List<MonthlyFacBal> updateMonthlyFacBal(Slice<MonthlyFacBal> slMothlyFacBal, TitaVo titaVo)
			throws LogicException {

		List<MonthlyFacBal> lMonthlyFacBal = new ArrayList<MonthlyFacBal>();
		lMonthlyFacBal = slMothlyFacBal.getContent();

		List<MonthlyFacBal> tmpMonthlyFacBal = new ArrayList<MonthlyFacBal>();

		this.info("lMonthlyFacBal.size1 = " + lMonthlyFacBal.size());
		for (OccursList tempOccursList : occursList) {

			int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
			int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
			String assetclass = tempOccursList.get("AssetClass");
			BigDecimal lawAmount = BigDecimal.ZERO;
			String lawAssetClass = "";

			MonthlyFacBal tMonthlyFacBal = new MonthlyFacBal();

			tMonthlyFacBal.setAssetClass(assetclass.substring(0, 1));

			for (MonthlyFacBal r : lMonthlyFacBal) {
				if (custno == r.getCustNo() && facmno == r.getFacmNo()) {

					r.setAssetClass(assetclass.substring(0, 1));

					if ("xlsx".equals(extension) || "xls".equals(extension)) {
						lawAmount = new BigDecimal(tempOccursList.get("LawAmount"));
						lawAssetClass = tempOccursList.get("LawAssetClass");

						tMonthlyFacBal.setLawAmount(lawAmount);
						tMonthlyFacBal.setLawAssetClass(lawAssetClass);

					}

					tmpMonthlyFacBal.add(r);

					CountS++;

					continue;

				}

			}
		}

		return tmpMonthlyFacBal;
	}

	/**
	 * 更新 frs9FacData.AssetClass
	 */
	private List<Ifrs9FacData> updateIfrs9FacData(Slice<Ifrs9FacData> sIfrs9FacData, TitaVo titaVo)
			throws LogicException {

		List<Ifrs9FacData> lIfrs9FacData = new ArrayList<Ifrs9FacData>();
		lIfrs9FacData = sIfrs9FacData.getContent();

		// 同月份資料
		List<Ifrs9FacData> tmpIfrs9FacData = new ArrayList<Ifrs9FacData>();
		for (Ifrs9FacData r : lIfrs9FacData) {
			if (iYearMonth == r.getDataYM()) {
				tmpIfrs9FacData.add(r);
			}
		}

		List<Ifrs9FacData> tmp2Ifrs9FacData = new ArrayList<Ifrs9FacData>();
		if (tmpIfrs9FacData.size() > 0) {

			for (OccursList tempOccursList : occursList) {

				int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
				int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
				String assetclass = tempOccursList.get("AssetClass");

				// 維護Ifrs9FacData

				for (Ifrs9FacData r : tmpIfrs9FacData) {
					if (custno == r.getCustNo() && facmno == r.getFacmNo()) {
						r.setAssetClass(parse.stringToInteger(assetclass));
						tmp2Ifrs9FacData.add(r);
						continue;

					}

				}

			}
		}

		return tmp2Ifrs9FacData;
	}

	/**
	 * 更新 Ias34Ap.AssetClass
	 */
	private List<Ias34Ap> updateIas34Ap(Slice<Ias34Ap> sIas34Ap, TitaVo titaVo) throws LogicException {

		List<Ias34Ap> lIas34Ap = new ArrayList<Ias34Ap>();
		lIas34Ap = sIas34Ap.getContent();

		// 同月份資料
		List<Ias34Ap> tmpIas34Ap = new ArrayList<Ias34Ap>();
		for (Ias34Ap r : lIas34Ap) {
			if (iYearMonth == r.getDataYM()) {
				tmpIas34Ap.add(r);
			}
		}

		List<Ias34Ap> tmp2Ias34Ap = new ArrayList<Ias34Ap>();

		if (tmpIas34Ap.size() > 0) {

			for (OccursList tempOccursList : occursList) {

				int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
				int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
				String assetclass = tempOccursList.get("AssetClass");

				// 維護Ifrs9FacData

				for (Ias34Ap r : tmpIas34Ap) {
					if (custno == r.getCustNo() && facmno == r.getFacmNo()) {
						r.setAssetClass(parse.stringToInteger(assetclass));
						tmp2Ias34Ap.add(r);
						continue;

					}

				}
			}

		}

		return tmp2Ias34Ap;
	}

	/**
	 * 更新 LoanIfrs9ApService.AssetClass
	 */
	private List<LoanIfrs9Ap> updateLoanIfrs9Ap(Slice<LoanIfrs9Ap> sLoanIfrs9Ap, TitaVo titaVo) throws LogicException {

		List<LoanIfrs9Ap> lLoanIfrs9Ap = new ArrayList<LoanIfrs9Ap>();
		lLoanIfrs9Ap = sLoanIfrs9Ap.getContent();

		// 同月份資料
		List<LoanIfrs9Ap> tmpLoanIfrs9Ap = new ArrayList<LoanIfrs9Ap>();
		for (LoanIfrs9Ap r : lLoanIfrs9Ap) {
			if (iYearMonth == r.getDataYM()) {
				tmpLoanIfrs9Ap.add(r);
			}
		}

		List<LoanIfrs9Ap> tmp2LoanIfrs9Ap = new ArrayList<LoanIfrs9Ap>();

		if (tmpLoanIfrs9Ap.size() > 0) {

			for (OccursList tempOccursList : occursList) {

				int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
				int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
				String assetclass = tempOccursList.get("AssetClass");

				// 維護Ifrs9FacData

				for (LoanIfrs9Ap r : tmpLoanIfrs9Ap) {
					if (custno == r.getCustNo() && facmno == r.getFacmNo()) {
						r.setAssetClass(parse.stringToInteger(assetclass));
						tmp2LoanIfrs9Ap.add(r);
						continue;

					}

				}

			}
		}

		return tmp2LoanIfrs9Ap;
	}

	/**
	 * 切換環境
	 */
	private void changeDBEnv(TitaVo titaVo) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("now env = " + ContentName.onLine);
		this.info("now getDataBase = " + titaVo.getDataBase());
		if (ContentName.onLine.equals(titaVo.getDataBase())) {
			titaVo.setDataBaseOnMon();// 指定月報環境
		} else {
			titaVo.setDataBaseOnLine();// 指定連線環境
		}
//		titaVo.setDataBaseOnLine();// 指定連線環境
	}

//	private void limitCommit() {
//
//		this.cnt++;
//
//		if (500 <= this.cnt) {
//			this.batchTransaction.commit();
//			this.cnt = 0;
//
//		}
//
//	}

}