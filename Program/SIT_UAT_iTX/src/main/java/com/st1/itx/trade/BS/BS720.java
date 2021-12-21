package com.st1.itx.trade.BS;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Ias39IntMethod;
import com.st1.itx.db.domain.Ias39IntMethodId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.Ias39IntMethodService;
import com.st1.itx.db.service.springjpa.cm.BS720ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.Ias39IntMethodFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS720")
@Scope("prototype")
/**
 * 利息法帳面資料檔整批更新 call by L7203
 *
 * @author Yoko
 * @version 1.0.0
 */
public class BS720 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS720.class);

	@Autowired
	public Ias39IntMethodService ias39IntMethodService;
	@Autowired
	public BS720ServiceImpl bS720ServiceImpl;
	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public TxToDoCom txToDoCom;
	@Autowired
	public Ias39IntMethodFileVo Ias39IntMethodFileVo;
	@Autowired
	public WebClient webClient;

	private BigDecimal ovduAmt = BigDecimal.ZERO;
	private BigDecimal loanAmt = BigDecimal.ZERO;
	private BigDecimal ovduAmtLast = BigDecimal.ZERO;
	private BigDecimal loanAmtLast = BigDecimal.ZERO;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS720 ");
		this.totaVo.init(titaVo);
		int iYearMonth = parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

		int iYearMonthLast = iYearMonth - 1;
		if (iYearMonthLast % 100 == 0) {
			iYearMonthLast = iYearMonthLast - 100 + 12;
		}
		this.info("BS720 YearMonth=" + iYearMonth + ", YearMonthLast=" + iYearMonthLast);

		// 利息法帳面資料檔更新
		procUpload(iYearMonth, titaVo);

		this.batchTransaction.commit();

		// 抓取本月及上月的本期累應攤銷折溢價
		procFindData(iYearMonth, iYearMonthLast, titaVo);

		if (this.loanAmt.add(ovduAmt).compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E0015", "本期累計應攤銷折溢價=0 ");
		}

		this.batchTransaction.commit();

// 2021/2/24 考慮四捨五入差額，只出表、不入帳(維持至核心會計系統輸入)		
		// 寫入應處理清單 ACCL00-各項提存作業(折溢價攤銷)
//		procTxToDo(iYearMonth, titaVo);
//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001", titaVo.getTlrNo(),
//				"請執行 各項提存入帳作業(折溢價攤銷)", titaVo);
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L9719", titaVo.getTlrNo(), "請列印 放款利息法折溢價攤銷表", titaVo);

		this.batchTransaction.commit();
		return null;
	}

	private void procUpload(int iYearMonth, TitaVo titaVo) throws LogicException {
		List<Ias39IntMethod> lIas39IntMethod = new ArrayList<Ias39IntMethod>();

//      吃檔                                            
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new LogicException("E0014", "BS720(" + filename + ")");
		}

//       使用資料容器內定義的方法切資料
		Ias39IntMethodFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = Ias39IntMethodFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {
			for (OccursList tempOccursList : uploadFile) {

				if (!(iYearMonth == parse.stringToInteger(tempOccursList.get("YearMonth")))) {
					throw new LogicException(titaVo, "E0015", "年月份錯誤 : " + tempOccursList.get("YearMonth"));
				}

//				this.info("BS720 data : " + tempOccursList.get("YearMonth") + "-" + tempOccursList.get("CustNo") + "-"
//						+ tempOccursList.get("FacmNo") + "-" + tempOccursList.get("BormNo") + "-"
//						+ tempOccursList.get("Principal") + "-" + tempOccursList.get("BookValue") + "-"
//						+ tempOccursList.get("AccumDPAmortized") + "-" + tempOccursList.get("AccumDPunAmortized") + "-"
//						+ tempOccursList.get("DPAmortized"));

				Ias39IntMethod tIas39IntMethod = new Ias39IntMethod();
				Ias39IntMethodId tIas39IntMethodId = new Ias39IntMethodId();

				tIas39IntMethodId.setYearMonth(parse.stringToInteger(tempOccursList.get("YearMonth")));
				tIas39IntMethodId.setCustNo(parse.stringToInteger(tempOccursList.get("CustNo")));
				tIas39IntMethodId.setFacmNo(parse.stringToInteger(tempOccursList.get("FacmNo")));
				tIas39IntMethodId.setBormNo(parse.stringToInteger(tempOccursList.get("BormNo")));
				tIas39IntMethod.setIas39IntMethodId(tIas39IntMethodId);
				tIas39IntMethod.setYearMonth(parse.stringToInteger(tempOccursList.get("YearMonth")));
				tIas39IntMethod.setCustNo(parse.stringToInteger(tempOccursList.get("CustNo")));
				tIas39IntMethod.setFacmNo(parse.stringToInteger(tempOccursList.get("FacmNo")));
				tIas39IntMethod.setBormNo(parse.stringToInteger(tempOccursList.get("BormNo")));
				tIas39IntMethod.setPrincipal(parse.stringToBigDecimal(tempOccursList.get("Principal")));
				tIas39IntMethod.setBookValue(parse.stringToBigDecimal(tempOccursList.get("BookValue")));
				tIas39IntMethod.setAccumDPAmortized(parse.stringToBigDecimal(tempOccursList.get("AccumDPAmortized")));
				tIas39IntMethod.setAccumDPunAmortized(parse.stringToBigDecimal(tempOccursList.get("AccumDPunAmortized")));
				tIas39IntMethod.setDPAmortized(parse.stringToBigDecimal(tempOccursList.get("DPAmortized")));
				lIas39IntMethod.add(tIas39IntMethod);
			}

		}
		if (lIas39IntMethod.size() > 0) {
			// 刪檔
			Slice<Ias39IntMethod> slIas39IntMethod = ias39IntMethodService.findYearMonthEq(iYearMonth, 0, Integer.MAX_VALUE, titaVo);
			List<Ias39IntMethod> oListIas39IntMethod = slIas39IntMethod == null ? null : new ArrayList<Ias39IntMethod>(slIas39IntMethod.getContent());
			if (oListIas39IntMethod != null) {
				try {
					ias39IntMethodService.deleteAll(oListIas39IntMethod);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException(titaVo, "E0008", "Ias39IntMethod利息法帳面資料檔" + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
			// 新增
			try {
				ias39IntMethodService.insertAll(lIas39IntMethod);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException("E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}

	}

	private void procFindData(int iYearMonth, int iYearMonthLast, TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lBS720Sql = new ArrayList<Map<String, String>>();
		String bS720Sql = "";

		try {
			bS720Sql = bS720ServiceImpl.FindBS720(iYearMonth, iYearMonthLast);
		} catch (Exception e) {
			// E5003 組建SQL語法發生問題
			this.info("BS720 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5003", "");
		}
		try {
			lBS720Sql = bS720ServiceImpl.FindData(bS720Sql, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("BS720 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		this.info("db return = " + lBS720Sql.toString());
		if (lBS720Sql.size() == 0) {
		}
		for (Map<String, String> rBS720Sql : lBS720Sql) {
			if (Integer.valueOf(rBS720Sql.get("F0")) == iYearMonth) {
				this.ovduAmt = parse.stringToBigDecimal(rBS720Sql.get("F1")).setScale(0, RoundingMode.HALF_UP);
				this.loanAmt = parse.stringToBigDecimal(rBS720Sql.get("F2")).setScale(0, RoundingMode.HALF_UP);
			}
			if (Integer.valueOf(rBS720Sql.get("F0")) == iYearMonthLast) {
				this.ovduAmtLast = parse.stringToBigDecimal(rBS720Sql.get("F1")).setScale(0, RoundingMode.HALF_UP);
				this.loanAmtLast = parse.stringToBigDecimal(rBS720Sql.get("F2")).setScale(0, RoundingMode.HALF_UP);
			}
		}
	}

	private void procTxToDo(int iYearMonth, TitaVo titaVo) throws LogicException {
		// AIL 擔保放款－折溢價 AIO 催收款項－折溢價 AII 利息收入－折溢價
		txToDoCom.setTxBuffer(this.getTxBuffer());
		int iDb = 0;
		int iCr = 0;
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode("ACCL00"); // ACCL00-各項提存作業
		// 借：AII 利息收入－折溢價
		// 貸：AIL 擔保放款－折溢價 AIO 催收款項－折溢價
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("AcclType", "折溢價攤銷");
		tTempVo.putParam("AcBookCode", "000");
		tTempVo.putParam("SlipNote", iYearMonth / 100 + "年" + iYearMonth % 100 + "月" + "折溢價攤銷");
		BigDecimal lnAmt = loanAmt.subtract(loanAmtLast);
		BigDecimal ovAmt = loanAmt.subtract(ovduAmtLast);
		BigDecimal intAmt = loanAmt.add(ovduAmt);
		if (lnAmt.compareTo(BigDecimal.ZERO) >= 0) {
			iCr++;
			tTempVo.putParam("CrAcctCode" + iCr, "AIL");
			tTempVo.putParam("CrTxAmt" + iCr, lnAmt);
		} else {
			iDb++;
			tTempVo.putParam("DbAcctCode" + iDb, "AIL");
			tTempVo.putParam("DbTxAmt" + iDb, BigDecimal.ZERO.subtract(lnAmt));
		}
		if (ovAmt.compareTo(BigDecimal.ZERO) >= 0) {
			iCr++;
			tTempVo.putParam("CrAcctCode" + iCr, "AIO");
			tTempVo.putParam("CrTxAmt" + iCr, ovAmt);
		} else {
			iDb++;
			tTempVo.putParam("DbAcctCode" + iDb, "AIO");
			tTempVo.putParam("DbTxAmt" + iDb, BigDecimal.ZERO.subtract(ovAmt));
		}
		if (intAmt.compareTo(BigDecimal.ZERO) >= 0) {
			iDb++;
			tTempVo.putParam("DbAcctCode" + iDb, "AII");
			tTempVo.putParam("DbTxAmt" + iDb, intAmt);
		} else {
			iCr++;
			tTempVo.putParam("CrAcctCode" + iCr, "AII");
			tTempVo.putParam("CrTxAmt" + iCr, BigDecimal.ZERO.subtract(intAmt));
		}
		tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
		txToDoCom.addDetail(false, 0, tTxToDoDetail, titaVo); // DupSkip = false ->重複 error
	}
}