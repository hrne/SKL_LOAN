package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.ReportUtil;

@Service("L5814p")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L5814p extends TradeBuffer {

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
	YearlyHouseLoanIntService sYearlyHouseLoanIntService;
	@Autowired
	ReportUtil reportUtil;

	@Autowired
	L5814 l5814;

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

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5814p ");
		this.totaVo.init(titaVo);

		int iYear = parse.stringToInteger(titaVo.getParam("Year")) + 1911;

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

		// 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			this.info("L5814(" + filename + ") : " + e.getMessage());
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

//		titaVo.setDataBaseOnMon();// 指定月報環境

		String extension[] = filename.split("\\.");
		if ("xlsx".equals(extension[extension.length - 1]) || "xls".equals(extension[extension.length - 1])) {
			makeExcel.openExcel(filename, 1); // 打開上傳的excel檔案，預設讀取第1個工作表
			getValueFromFileExcel(titaVo); // 從excel取想要的資料
		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		this.info("總筆數 occursList.size=" + occursList.size());
		int CountAll = occursList.size();
		int CountS = 0;
		int CountF = 0;

		YearlyHouseLoanInt tYearlyHouseLoanInt = new YearlyHouseLoanInt();
		YearlyHouseLoanIntId tYearlyHouseLoanIntId = new YearlyHouseLoanIntId();

		for (OccursList tempOccursList : occursList) {

			int iCustNo = parse.stringToInteger(tempOccursList.get("CustNo"));
			int iFacmNo = parse.stringToInteger(tempOccursList.get("FacmNo"));
			int iYearMonth = parse.stringToInteger(tempOccursList.get("YearMonth"));
			BigDecimal loanAmt = parse.stringToBigDecimal(tempOccursList.get("LoanAmt"));
			this.info("YearMonth :" + iYearMonth + ",CustNo :" + iCustNo + ",FacmNo :" + iFacmNo + ",LoanAmt :"	+ loanAmt);

			tYearlyHouseLoanInt = sYearlyHouseLoanIntService.holdById(new YearlyHouseLoanIntId(iYearMonth, iCustNo, iFacmNo, "02"));
			if (tYearlyHouseLoanInt == null) {
				tYearlyHouseLoanInt = sYearlyHouseLoanIntService.holdById(new YearlyHouseLoanIntId(iYearMonth, iCustNo, iFacmNo, "00"));
				if (tYearlyHouseLoanInt == null) {
					CountF++;// 失敗筆數+1
					this.info("取不到資料 YearlyHouseLoanInt");
					continue;
				}
			}

			if (tYearlyHouseLoanInt != null) {
				tYearlyHouseLoanInt.setLoanAmt(loanAmt); // 設定excel 38攔需修正的金額
				try {
					tYearlyHouseLoanInt = sYearlyHouseLoanIntService.update(tYearlyHouseLoanInt, titaVo);
					CountS++; // 成功筆數+1
				} catch (DBException e) {
					CountF++;// 失敗筆數+1
					this.info("更新資料表  YearlyHouseLoanInt 失敗：" + e.toString());
					continue;
				}

			}
		}
		
		String note = "更新總筆數：" + CountAll + " ,更新成功筆數：" + CountS + " ,更新失敗筆數：" + CountF;
		this.info(note);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "",titaVo.getParam("TLRNO"), note, titaVo);
		this.addList(this.totaVo);	
		return this.sendList();
	}

	public void getValueFromFileExcel(TitaVo titaVo) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		int iYearMonth = 0;
		int iCustNo = 0;
		int iFacmNo = 0;
		BigDecimal iLoanAmt = BigDecimal.ZERO; // 最初貸款金額為0者， 填補最初貸款金額
		BigDecimal oLoanAmt = BigDecimal.ZERO; // 初貸金額

		for (int i = 3; i <= lastRowNum; i++) {

			iYearMonth = 0;
			iCustNo = 0;
			iFacmNo = 0;			
			oLoanAmt = BigDecimal.ONE; // 初值給1
			iLoanAmt = BigDecimal.ZERO;
			OccursList occursList = new OccursList();

			if (StringUtils.isEmpty(makeExcel.getValue(i, 24))) {
				this.info("資料到第幾列結束 i=" + i);
				break;
			}

			try {

				this.info("資料到第幾列開始 i=" + i);

				if (!StringUtils.isEmpty(makeExcel.getValue(i, 33))) {
					iYearMonth = new BigDecimal(makeExcel.getValue(i, 33).toString()).intValue() + 191100; // 繳息所屬年月
				}
				if (!StringUtils.isEmpty(makeExcel.getValue(i, 18))) {
					iCustNo = new BigDecimal(makeExcel.getValue(i, 18).toString()).intValue();
				}
				if (!StringUtils.isEmpty(makeExcel.getValue(i, 19))) {
					iFacmNo = new BigDecimal(makeExcel.getValue(i, 19).toString()).intValue();
				}
				if (!StringUtils.isEmpty(makeExcel.getValue(i, 28))) {
					oLoanAmt = new BigDecimal(makeExcel.getValue(i, 28).toString());
				}
				if (!StringUtils.isEmpty(makeExcel.getValue(i, 38))) {
					iLoanAmt = new BigDecimal(makeExcel.getValue(i, 38).toString());
				}				

				if (iYearMonth > 0 && iCustNo > 0 && iFacmNo > 0) {

					// 第28欄=0才要使用第38欄更新YearlyHouseLoanInt.LoanAmt(若第38欄=0或null或空白,則該筆不更新)
					this.info("oLoanAmt.compareTo(BigDecimal.ZERO)=" + oLoanAmt.compareTo(BigDecimal.ZERO));
					this.info("iLoanAmt.compareTo(BigDecimal.ZERO)=" + iLoanAmt.compareTo(BigDecimal.ZERO));

					if (oLoanAmt.compareTo(BigDecimal.ZERO) == 0 && iLoanAmt.compareTo(BigDecimal.ZERO) == 1) {

						this.info("塞入資料到第幾列開始 i=" + i);
						this.info("YearMonth :" + iYearMonth + ",CustNo :" + iCustNo + ",FacmNo :" + iFacmNo + ",LoanAmt :" + iLoanAmt);

						occursList.putParam("YearMonth", iYearMonth);
						occursList.putParam("CustNo", iCustNo);
						occursList.putParam("FacmNo", iFacmNo);
						occursList.putParam("LoanAmt", iLoanAmt);
						this.occursList.add(occursList);
					}
				}

			} catch (Exception e) {

				String ErrorMsg = e.toString();
				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}

		}

	}

}