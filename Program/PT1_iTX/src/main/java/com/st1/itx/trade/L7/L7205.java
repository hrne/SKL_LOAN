package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.Ias34ApService;
import com.st1.itx.db.service.Ifrs9FacDataService;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L7205")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L7205 extends TradeBuffer {

	public String code = "";
	public String codeName = "";

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
//	public ArrayList<OccursList> occursList = new ArrayList<>();

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

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

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

		String extension[] = filename.split("\\.");
		this.info("file extension=" + extension[extension.length - 1]);
		if ("xlsx".equals(extension[extension.length - 1]) || "xls".equals(extension[extension.length - 1])) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);
			checkExcelData(titaVo, iYearMonth);
		} else if ("csv".equals(extension[extension.length - 1].toLowerCase())) {
			checkCSVData(titaVo, dataLineList, iYearMonth);
		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		MySpring.newTask("L7205p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void checkCSVData(TitaVo titaVo, ArrayList<String> lineList, int iYearMonth) throws LogicException {
		// 設定明細欄位的擷取位置
		// 1 YearMonth 年月份 Decimal 6 YYYYMM 西元年月
		// 2 CustNo 戶號 Decimal 7
		// 3 FacmNo 額度編號 Decimal 3
		// 4 AssetClass 資產五分類代號(有擔保部分) Decimal 1
		String[] thisColumn = lineList.get(0).split(",");
		String yearMonth = "";
		if (Integer.valueOf(thisColumn[0].toString()) < 191100) {
			yearMonth = String.valueOf((Integer.valueOf(thisColumn[0].toString()) + 191100));
		} else {
			yearMonth = thisColumn[0];
		}

		if (Integer.valueOf(yearMonth) != iYearMonth) {

			String ErrorMsg = "年月份錯誤 : 應為" + iYearMonth + ",資料上為：" + yearMonth;

			throw new LogicException(titaVo, "E0015", ErrorMsg);
		}

	}

	public void checkExcelData(TitaVo titaVo, int iYearMonth) throws LogicException {

		int fileYearMonth = new BigDecimal(makeExcel.getValue(1, 10).toString()).intValue() + 191100;

		this.info("輸入的年份：" + iYearMonth);
		this.info("檔案的年份：" + fileYearMonth);
		if (fileYearMonth != iYearMonth) {

			String ErrorMsg = "年月份錯誤 : 應為" + iYearMonth + ",資料上為：" + fileYearMonth;

			throw new LogicException(titaVo, "E0015", ErrorMsg);
		}

	}

}