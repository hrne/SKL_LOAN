package com.st1.itx.trade.L7;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CoreAcMain;
import com.st1.itx.db.domain.CoreAcMainId;
import com.st1.itx.db.service.CoreAcMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L7401")
@Scope("prototype")
/**
 * 會計總帳檔上傳作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L7401 extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	MakeExcel makeExcel;
	@Autowired
	public WebClient webClient;

	@Autowired
	CoreAcMainService tCoreAcMainService;
	@Value("${iTXInFolder}")
	private String inFolder = "";

	int count = 0;

	private List<CoreAcMain> inCoreAcMain = new ArrayList<CoreAcMain>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7401 ");
		this.totaVo.init(titaVo);

		if (titaVo.getParam("FILENA").trim().length() == 0) {
			throw new LogicException(titaVo, "E0014", "沒有選擇檔案");
		}

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename = " + filename);

		// 路徑
		String[] extension = null;
		if (filename.contains("\\/")) {
			extension = filename.split("\\/");
		} else if (filename.contains("\\")) {
			extension = filename.split("\\\\");
		} else if (filename.contains("/")) {
			extension = filename.split("/");
		}

		String tmpNameG[] = extension[extension.length - 1].split("\\.");
		// 檔案名稱
		String tmpName = tmpNameG[tmpNameG.length - 2];
		// 附檔名
		String fileExt = tmpNameG[tmpNameG.length - 1].toLowerCase();
		this.info("file fileName=" + tmpName);
		this.info("file fileExt=" + fileExt);

		if ("xlsx".equals(fileExt) || "xls".equals(fileExt)) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);

			String dataDate = String.valueOf(makeExcel.getValue(12, 2));

			dataDate = dataDate.substring(0, 3) + dataDate.substring(4, 6) + dataDate.substring(7, 9);

			this.info("dataDate =" + dataDate);

			int bcDate = dataDate.length() == 7 ? Integer.valueOf(dataDate) + 19110000 : Integer.valueOf(dataDate);

			this.info("bcDate =" + bcDate);

			this.info("setDataBaseOnMon");
			titaVo.setDataBaseOnMon();
			Slice<CoreAcMain> fCoreAcMain = tCoreAcMainService.findByAcDate(bcDate, 0, Integer.MAX_VALUE, titaVo);
			List<CoreAcMain> lCoreAcMain = fCoreAcMain == null ? null : fCoreAcMain.getContent();

			if (addData(titaVo, filename, "SKL000_NTD", bcDate, lCoreAcMain)
					&& addData(titaVo, filename, "SKL000_00A_NTD", bcDate, lCoreAcMain)) {
				insertData(titaVo);

				this.totaVo.putParam("Count", count);

				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", "", "資料更新成功", titaVo);
				this.addList(this.totaVo);
			} else {
				
				String note = "此檔資料已存在";

				throw new LogicException(titaVo, "E0007", note);
			}

		} else {

			String ErrorMsg = "請上傳正確附檔名之檔案-xls,xlsx";

			throw new LogicException(titaVo, "E0014", ErrorMsg);

		}

		return this.sendList();

	}

	/**
	 * 寫入資料
	 * 
	 * @param titaVo
	 * @param bcDate    西元日期
	 * @param sheetName 工作表名稱
	 * @throws LogicException
	 */
	private boolean addData(TitaVo titaVo, String fileName, String sheetName, int bcDate, List<CoreAcMain> lCoreAcMain)
			throws LogicException {

		makeExcel.openExcel(fileName, sheetName);

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;
		this.info("lastRowNum=" + lastRowNum);

		String iAcBookCode = "";
		String iAcSubBookCode = "";
		String iCurrencyCode = "";
		String iAcNoCode = "";
		String iAcNoName = "";
		String iAcSubCode = "";
//		BigDecimal iAcDate = new BigDecimal(bcDate);
		BigDecimal iYdBal = BigDecimal.ZERO;
		BigDecimal iTdBal = BigDecimal.ZERO;
		BigDecimal iDbAmt = BigDecimal.ZERO;
		BigDecimal iCrAmt = BigDecimal.ZERO;

		if (lCoreAcMain == null || lCoreAcMain.size() == 0) {

			for (int row = 2; row <= lastRowNum; row++) {

				CoreAcMain iCoreAcMain = new CoreAcMain();
				CoreAcMainId iCoreAcMainId = new CoreAcMainId();
//				AcBookCode,AcSubBookCode,CurrencyCode,AcNoCode,AcSubCode,AcDate
				count++;

				// 帳冊別
				iAcBookCode = String.valueOf(makeExcel.getValue(row, 1));
				iAcBookCode = "SKL000".equals(iAcBookCode) ? "000" : iAcBookCode;
				iCoreAcMainId.setAcBookCode(iAcBookCode);
				// 區隔帳冊
				iAcSubBookCode = String.valueOf(makeExcel.getValue(row, 2));
				iAcSubBookCode = "00A".equals(iAcSubBookCode) ? "00A" : "201";
				iCoreAcMainId.setAcSubBookCode(iAcSubBookCode);
				// 幣別
				iCurrencyCode = String.valueOf(makeExcel.getValue(row, 9));
				iCoreAcMainId.setCurrencyCode(iCurrencyCode);
				// 科目代號
				iAcNoCode = String.valueOf(makeExcel.getValue(row, 4)).trim();
				iCoreAcMainId.setAcNoCode(converntScientificNotation(iAcNoCode));
				// 科目名稱
				iAcNoName = String.valueOf(makeExcel.getValue(row, 5)).trim();
				iCoreAcMain.setAcNoName(iAcNoName);
				// 子目代號
				iAcSubCode = "     ";
				iCoreAcMainId.setAcSubCode(iAcSubCode);
				// 會計日期
				iCoreAcMainId.setAcDate(bcDate);

				iCoreAcMain.setCoreAcMainId(iCoreAcMainId);

				// 昨日餘額
				iCoreAcMain.setYdBal(iYdBal);
				// 今日餘額
				// 科目開頭 1 5 6 9 的貸餘是負的，借餘是正的(原檔已有正負符號)
				iTdBal = new BigDecimal(String.valueOf(makeExcel.getValue(row, 8)));
				iCoreAcMain.setTdBal(iTdBal);
				// 借方金額
				iDbAmt = new BigDecimal(String.valueOf(makeExcel.getValue(row, 6)));
				iCoreAcMain.setDbAmt(iDbAmt);
				// 貸方金額
				iCrAmt = new BigDecimal(String.valueOf(makeExcel.getValue(row, 7)));
				iCoreAcMain.setCrAmt(iCrAmt);

				this.info("iAcBookCode = " + iAcBookCode);
				this.info("iAcSubBookCode = " + iAcSubBookCode);
				this.info("iCurrencyCode = " + iCurrencyCode);
				this.info("iAcNoCode = " + converntScientificNotation(iAcNoCode));
				this.info("iAcNoName = " + iAcNoName);
				this.info("iAcSubCode = " + iAcSubCode);
				this.info("iYdBal = " + iYdBal);
				this.info("iTdBal = " + iTdBal);
				this.info("iDbAmt = " + iDbAmt);
				this.info("iCrAmt = " + iCrAmt);
				this.info("AcDate = " + bcDate);

				inCoreAcMain.add(iCoreAcMain);

			}

		} else {
			this.info("Data already exists");
			return false;
		}
		return true;
	}

	/**
	 * 寫入資料
	 * 
	 * @param titaVo
	 * @throws LogicException
	 */
	private void insertData(TitaVo titaVo) throws LogicException {

		try {
			tCoreAcMainService.insertAll(inCoreAcMain, titaVo);

//			this.info("setDataBaseOnLine");
//			titaVo.setDataBaseOnLine();
//			tCoreAcMainService.insertAll(inCoreAcMain, titaVo);

		} catch (DBException e) {
			// TODO Auto-generated catch block
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());

		}

	}

	private String converntScientificNotation(String text) {
		String resText = "";
		this.info("text before= " + text);
		if (text.contains("E")) {
			BigDecimal decimalFormat = new BigDecimal(text);
			resText = decimalFormat.toPlainString();
		} else if (text.contains("-") || text.length() == 0) {
			resText = "-";
		} else {
			resText = text;
		}

		this.info("text after = " + resText);
		return resText;
	}
}