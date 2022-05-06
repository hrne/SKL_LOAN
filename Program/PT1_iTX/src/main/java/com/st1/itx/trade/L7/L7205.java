package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Ias34Ap;
import com.st1.itx.db.domain.Ifrs9FacData;
import com.st1.itx.db.domain.Ifrs9FacDataId;
import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7205")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L7205 extends TradeBuffer {

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
	JobMainService sJobMainService;

	@Autowired
	MakeExcel makeExcel;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7205 ");
		this.totaVo.init(titaVo);

		int iYearMonth = parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;
		this.info("L7205 YearMonth : " + iYearMonth);

		int dateSent = Integer.parseInt(titaVo.getParam("YearMonth") + "01");
		dateUtil.init();
		dateUtil.setDate_1(dateSent);
		TxBizDate tTxBizDate = dateUtil.getForTxBizDate(true);// 若1號為假日,參數true則會找次一營業日,不會踢錯誤訊息
		int iMfbsDy = tTxBizDate.getMfbsDy() + 19110000;// 畫面輸入年月的月底營業日
		this.info("L7205 iMfbsDy : " + iMfbsDy);

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename=" + filename);

		ArrayList<String> dataLineList = new ArrayList<>();

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			this.info("L7205(" + filename + ") : " + e.getMessage());
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;

			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		titaVo.setDataBaseOnMon();// 指定月報環境

		String extension[] = filename.split("\\.");
		if ("xlsx".equals(extension[extension.length - 1]) || "xls".equals(extension[extension.length - 1])) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			int fileYearMonth = new BigDecimal(makeExcel.getValue(1, 9).toString()).intValue() + 191100;
			this.info("fileYearMonth=" + fileYearMonth);

			if (fileYearMonth != iYearMonth) {
				String ErrorMsg = "輸入的年月份(" + (iYearMonth-191100) + ")與檔案年月份(" + (fileYearMonth-191100) + ")不同，請確認檔案年月份。";
				throw new LogicException(titaVo, "E0014", ErrorMsg);
			}
			// 切資料
			setValueFromFileExcel(titaVo, iYearMonth);
		} else if ("csv".equals(extension[extension.length - 1])) {
			setValueFromFile(dataLineList);
		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		int CountAll = occursList.size();
		int CountS = 0;
		int CountF = 0;

		Slice<Ias34Ap> sIas34Ap = null;
		Slice<LoanIfrs9Ap> sLoanIfrs9Ap = null;

		for (OccursList tempOccursList : occursList) {

			int custno = parse.stringToInteger(tempOccursList.get("CustNo"));
			int facmno = parse.stringToInteger(tempOccursList.get("FacmNo"));
			int yearmonth = parse.stringToInteger(tempOccursList.get("YearMonth"));
			String assetclass = tempOccursList.get("AssetClass");

			if (!(iYearMonth == yearmonth)) {
				throw new LogicException(titaVo, "E0015", "年月份錯誤 : " + yearmonth);
			}
			// 維護monthlyFacBal
			MonthlyFacBalId monthlyFacBalId = new MonthlyFacBalId();
			monthlyFacBalId.setCustNo(custno);
			monthlyFacBalId.setFacmNo(facmno);
			monthlyFacBalId.setYearMonth(yearmonth);

			MonthlyFacBal tMonthlyFacBal = tMothlyFacBalService.findById(monthlyFacBalId, titaVo);

			if (tMonthlyFacBal == null) {
				CountF++; // 失敗筆數+1
			} else {
				tMonthlyFacBal.setAssetClass(assetclass);
				try {
					tMothlyFacBalService.update(tMonthlyFacBal, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				}
				CountS++; // 成功筆數+1
			}
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
			}

			// 維護LoanIfrs9Ap

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
			}
		} // for

		this.totaVo.putParam("CountAll", CountAll);
		this.totaVo.putParam("CountS", CountS);
		this.totaVo.putParam("CountF", CountF);

//		 重產LM051報表
		titaVo.setBatchJobId("jLM051");

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void setValueFromFile(ArrayList<String> lineList) {

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
			if (thisColumn.length >= 1 && thisColumn != null) {
				occursList.putParam("YearMonth", thisColumn[0]);
				occursList.putParam("CustNo", thisColumn[1]);
				occursList.putParam("FacmNo", thisColumn[2]);
				occursList.putParam("AssetClass", thisColumn[3]);
			}
			this.occursList.add(occursList);
		}

	}

	public void setValueFromFileExcel(TitaVo titaVo, int YearMonth) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.sheet.getLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		int iYearMonth = YearMonth;
		BigDecimal iCustNo = BigDecimal.ZERO;
		BigDecimal iFacmNo = BigDecimal.ZERO;
		BigDecimal iAssetClass = BigDecimal.ZERO;

		for (int i = 2; i <= lastRowNum; i++) {

			OccursList occursList = new OccursList();

			// 等於1，表示前大於後
			// 等於0，表示前等於後
			// 等於-1，表示前小於後
//			if ((iCustNo.compareTo(BigDecimal.ZERO) == -1 || iCustNo == null)
//					&& (iFacmNo.compareTo(BigDecimal.ZERO) == -1 || iFacmNo == null)
//					&& (iAssetClass.compareTo(BigDecimal.ZERO) == -1 || iAssetClass == null)) {
//		}

			try {
				iCustNo = new BigDecimal(makeExcel.getValue(i, 2).toString());
				iFacmNo = new BigDecimal(makeExcel.getValue(i, 3).toString());
				iAssetClass = new BigDecimal(makeExcel.getValue(i, 8).toString());
			} catch (Exception e) {
//				this.info("L7205(Excel欄位應為iCustNo在B欄、iFacmNo在C欄、iAssetClass為H欄)請確認格式 : " + e.getMessage());

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

			this.occursList.add(occursList);
		}

	}

}