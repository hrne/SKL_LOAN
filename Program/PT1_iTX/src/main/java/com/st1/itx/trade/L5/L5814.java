package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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

@Service("L5814")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L5814 extends TradeBuffer {

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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5814 ");
		this.totaVo.init(titaVo);

		int iYear = parse.stringToInteger(titaVo.getParam("Year")) + 1911;
		int iYearMonth = parse.stringToInteger(iYear + "01");
//		this.info("L5814 Year : " + iYear + ",L5814 iYearMonth : " + iYearMonth);

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename=" + filename);

		ArrayList<String> dataLineList = new ArrayList<>();

//      編碼參數，設定為UTF-8 || big5
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
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			// 檢查資料
			getValueFromFileExcel(titaVo, iYear);
		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		MySpring.newTask("L5814p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void getValueFromFileExcel(TitaVo titaVo, int iYear) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		// 統一編號
		String id = makeExcel.getValue(3, 24).toString().trim();

		if (StringUtils.isEmpty(id)) {
			throw new LogicException(titaVo, "E0015", id);
		}

		int fileYear = 0;

		// 繳息所屬年月
		if (makeExcel.getValue(3, 33).toString().trim().length() > 0) {
			fileYear = Integer.parseInt(makeExcel.getValue(3, 33).toString().trim().substring(0, 3)) + 1911;
		}

		if (fileYear != iYear) {
			String ErrorMsg = "年度錯誤 : 應為" + iYear + ",資料上為：" + fileYear;
			throw new LogicException(titaVo, "E0015", ErrorMsg);
		}

	}

}