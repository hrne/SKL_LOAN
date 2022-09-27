package com.st1.itx.trade.L4;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.InsuComm;
import com.st1.itx.db.domain.InsuCommId;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuCommService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4606ServiceImpl;
import com.st1.itx.trade.L4.L4606Report1;
import com.st1.itx.trade.L4.L4606Report2;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.common.data.InsuCommFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4606Batch")
@Scope("prototype")
/**
 * 火險佣金作業<BR>
 * 1. 佣金為負不寫入、僅顯示筆數<BR>
 * 2. 應領金額為零，不出表<BR>
 * 3. 業務人員任用狀況碼 AgStatusCode = 1:在職 ，才發放<BR>
 * 4. 要發放的，應領金額為零，仍寫入佣金媒體檔
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4606Batch extends TradeBuffer {

	private TitaVo titaVo;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private FileCom fileCom;

	@Autowired
	private InsuCommFileVo insuCommFileVo;

	@Autowired
	private InsuCommService insuCommService;

	@Autowired
	private FacMainService facMainService;

	@Autowired
	private CustMainService custMainService;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	private L4606ServiceImpl l4606ServiceImpl;

	@Autowired
	private TotaVo totaA;

	@Autowired
	private TotaVo totaB;

	@Autowired
	private WebClient webClient;

	@Autowired
	private L4606Report1 l4606Report1;

	@Autowired
	private L4606Report2 l4606Report2;

	@Autowired
	private L4606Report3 l4606Report3;

	@Autowired
	private L4606Report4 l4606Report4;
	
	@Autowired
	private L4606Report5 l4606Report5;
	@Autowired
	private L4606Report6 l4606Report6;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	private MakeFile makeFile;

	// 上傳預設目錄
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int iInsuEndMonth = 0;

//	寄送筆數
	private int commitCnt = 500;
	private String sendMsg = "";
	private Boolean isFinished = true;

	private long sno = 0;
	private int totCnt = 0;
	private int minusCnt = 0;
	private int zeroDueAmtCnt = 0;
	private int paidCnt = 0;
	private int unPaidCnt = 0;
	private int custErrorCnt = 0;

	private List<OccursList> successList = new ArrayList<>();
	private List<OccursList> errorList = new ArrayList<>();

	@Autowired
	SortMapListCom sortMapListCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4606Batch ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		try {
			execute(titaVo);
		} catch (LogicException e) {
			sendMsg = e.getErrorMsg();
			isFinished = false;
		}

//		執行成功者，指向查詢畫面
		if (isFinished) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4606", "", sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void init() {
		sno = 0;
		totCnt = 0;
		minusCnt = 0;
		zeroDueAmtCnt = 0;
		paidCnt = 0;
		unPaidCnt = 0;
		custErrorCnt = 0;
		successList = new ArrayList<>();
		errorList = new ArrayList<>();
	}

	private void execute(TitaVo titaVo) throws LogicException {
		String flagA = titaVo.getParam("FlagA");
		String flagB = titaVo.getParam("FlagB");
		String flagC = titaVo.getParam("FlagC");

		init();

		setTimer();
		deleteInsuComm(titaVo);
		printUsedTime("deleteInsuComm");

//		PC上傳媒體檔轉入佣金媒體檔
		if (!"".equals(flagA)) {
			setTimer();
			insertIntoInsuComm();
			printUsedTime("insertIntoInsuComm");
		}

//		火險佣金發放報表(未發放 = '佣金日期=0?')
//		未發放 -> 火險服務(FireOfficer) = 空白(null)
		if (!"".equals(flagB)) {
			setTimer();
			this.info("flagB Start ...");

			if (custErrorCnt > 0) {
				l4606Report4.exec(titaVo, errorList);
				sendMsg = "匯入資料有誤";
			} else {
				l4606Report1.exec(titaVo);
				l4606Report2.exec(titaVo);
				l4606Report3.exec(titaVo, successList);
				l4606Report4.exec(titaVo, errorList);
				l4606Report5.exec(titaVo);
				l4606Report6.exec(titaVo);	
				sendMsg = "上傳筆數：" + totCnt + ", 發放筆數：" + paidCnt + ", 未發放筆數：" + unPaidCnt + ", 應領金額為零筆數："
						+ zeroDueAmtCnt + ", 戶號有誤筆數：" + custErrorCnt + ", 剔除佣金為負筆數：" + minusCnt;

			}
			printUsedTime("flagB");
		}

//		產生下傳媒體
		if (!"".equals(flagC)) {
			if (custErrorCnt == 0) {
				setTimer();
				generateMediaFile();
				printUsedTime("generateMediaFil'");
			}
		}
	}

	private void printUsedTime(String title) {
		String usedTime = title + " 耗時 " + (double) ((double) (System.currentTimeMillis() - starttime) / 1000.0) + " 秒";
		this.info(usedTime);
	}

	Long starttime;

	private void setTimer() {
		starttime = System.currentTimeMillis();
	}

	private void generateMediaFile() throws LogicException {

		this.info("generateMediaFile Start ...");

		List<InsuComm> lInsuComm = new ArrayList<InsuComm>();

//		暫定 : D:\\temp\\LNM23P.TXT
//		String path = outFolder + "LNM23P.TXT";

		ArrayList<OccursList> tmp = new ArrayList<>();

		Slice<InsuComm> sInsuComm = null;

		sInsuComm = insuCommService.insuYearMonthRng(iInsuEndMonth, iInsuEndMonth, this.index, this.limit, titaVo);

		lInsuComm = sInsuComm == null ? null : sInsuComm.getContent();

		if (lInsuComm != null && lInsuComm.size() != 0) {

//			1.Group by ID
			HashMap<String, BigDecimal> sumComm = new HashMap<>();
			HashMap<String, BigDecimal> sumPrem = new HashMap<>();
			HashMap<String, Integer> cntComm = new HashMap<>();

			for (InsuComm tInsuComm : lInsuComm) {
				if ("Y".equals(tInsuComm.getMediaCode())) {
					String empId = tInsuComm.getEmpId();

					if (sumComm.containsKey(empId)) {
						sumComm.put(empId, sumComm.get(empId).add(tInsuComm.getCommision()));
					} else {
						sumComm.put(empId, tInsuComm.getCommision());
					}

					if (sumPrem.containsKey(empId)) {
						sumPrem.put(empId, sumPrem.get(empId).add(tInsuComm.getInsuPrem()));
					} else {
						sumPrem.put(empId, tInsuComm.getInsuPrem());
					}

					if (cntComm.containsKey(empId)) {
						cntComm.put(empId, cntComm.get(empId) + 1);
					} else {
						cntComm.put(empId, 1);
					}
				}
			}

//			2.if already output -> continue
			Map<String, Integer> flagComm = new HashMap<>();

			int seq = 0;

			for (InsuComm tInsuComm : lInsuComm) {
				if (!"".equals(tInsuComm.getFireOfficer())) {
					OccursList occursList = new OccursList();

					this.info("FireOfficer ... '" + tInsuComm.getFireOfficer() + "'");

					String empId = tInsuComm.getEmpId();

					if (flagComm.containsKey(empId)) {
						continue;
					} else {
						flagComm.put(empId, 1);
					}
					seq = seq + 1;

					if (seq % commitCnt == 0) {
						this.batchTransaction.commit(); // TODO: 確認用途? 這個for迴圈並沒有寫DB
					}

					occursList.putParam("SalesId", FormatUtil.padX(empId, 10));
					occursList.putParam("FireInsuMonth", iInsuEndMonth);
					occursList.putParam("ColumnA", 0);
					occursList.putParam("TotCommA", FormatUtil.pad9("" + sumComm.get(empId), 9));
					occursList.putParam("TotCommB", FormatUtil.pad9("" + sumComm.get(empId), 9));
					occursList.putParam("ColumnB", 0);
					occursList.putParam("ColumnC", 0);
					occursList.putParam("ColumnD", 0);
					occursList.putParam("ColumnE", 0);
					occursList.putParam("Count", FormatUtil.pad9("" + cntComm.get(empId), 5));
					occursList.putParam("TotFee", FormatUtil.pad9("" + sumPrem.get(empId), 9));
					occursList.putParam("TotCommC", FormatUtil.pad9("" + sumComm.get(empId), 9));
					occursList.putParam("ColumnF", 0);
					occursList.putParam("ColumnG", 0);
					occursList.putParam("ColumnH", 0);

					tmp.add(occursList);
				}
			}
			// 把明細資料容器裝到檔案資料容器內
			ArrayList<OccursList> occursList = new ArrayList<OccursList>();
			insuCommFileVo.setOccursList(occursList);

			insuCommFileVo.setOccursList(tmp);

			// 轉換資料格式
			List<String> file = insuCommFileVo.toFile();

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
					titaVo.getTxCode() + "-火險佣金媒體檔", "LNM23P.txt", 2);

			for (String line : file) {
				makeFile.put(line);
			}

			sno = makeFile.close();

			this.info("sno : " + sno);

//			makeFile.toFile(sno);
		}
	}

	private void insertIntoInsuComm() throws LogicException {
		this.info("insertIntoInsuComm Start ...");

//		String fileName = "874-B1.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA");

		ArrayList<String> dataLineList = new ArrayList<>();

//		 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filePath1, "big5");

		} catch (IOException e) {
			throw new LogicException("E0014", "L4606(" + filePath1 + ") is error : " + e.getMessage());
		}

//		 使用資料容器內定義的方法切資料
		insuCommFileVo.setValueFromFile(dataLineList);

		List<OccursList> uploadFile = insuCommFileVo.getOccursList();

		this.info("insuCommFileVo   = " + insuCommFileVo.getOccursList());

		int seq = 0;
		int count = 0;
		if (uploadFile != null && !uploadFile.isEmpty()) {
			this.info("tInsuCommId Start");
			InsuComm tInsuComm;
			InsuCommId tInsuCommId;

			if (uploadFile != null && uploadFile.size() != 0) {
				uploadFile.sort((c1, c2) -> {
					int result = 0;
					if (c1.get("CustNo") != c2.get("CustNo")) {
						result = 1;
					} else if (c1.get("FacmNo") != c2.get("FacmNo")) {
						result = 1;
					} else if (c1.get("InsuNo") != c2.get("InsuNo")) {
						result = 1;
					} else if (c1.get("InsuFee") != c2.get("InsuFee")) {
						result = 1;
					} else if (c1.get("InsuCommRate") != c2.get("InsuCommRate")) {
						result = 1;
					} else if (c1.get("AcSeq") != c2.get("AcSeq")) {
						result = 1;
					}
					return result;
				});
			}
			this.info("uploadFile    = " + uploadFile);
			

			for (OccursList tempOccursList : uploadFile) {
				this.info(tempOccursList.toString());
				
				
				seq++;
				tempOccursList.putParam("Seq", seq);

				if (seq % commitCnt == 0) {
					this.info("Seq : " + seq);
					this.batchTransaction.commit();
				}
				totCnt++;
				// 佣金為負不寫入、僅顯示筆數
				if (parse.stringToBigDecimal(tempOccursList.get("InsuComm")).compareTo(BigDecimal.ZERO) < 0) {
					minusCnt++;
					tempOccursList.putParam("ErrorMsg", "佣金為負:" + tempOccursList.get("InsuComm"));
					errorList.add(tempOccursList);
					continue;
				}
				tInsuComm = new InsuComm();
				tInsuCommId = new InsuCommId();

				String empNo = "";
				String empId = "";
				String empName = "";
				String agStatusCode = "";
				String mediaCode = "";
				int custNo = parse.stringToInteger(tempOccursList.get("CustNo"));
				int facmNo = parse.stringToInteger(tempOccursList.get("FacmNo"));

				if (custNo == 0) {
					custErrorCnt++;
					tempOccursList.putParam("ErrorMsg", "戶號錯誤:" + custNo);
					errorList.add(tempOccursList);
					continue;
				}

				tInsuCommId.setInsuYearMonth(iInsuEndMonth);
				tInsuCommId.setInsuCommSeq(seq);

				tInsuComm.setInsuCommId(tInsuCommId);
				tInsuComm.setNowInsuNo(tempOccursList.get("InsuNo"));
				tInsuComm.setInsuType(parse.stringToInteger(tempOccursList.get("InsuKind")));
				tInsuComm.setManagerCode(tempOccursList.get("IndexCode"));
				tInsuComm.setBatchNo(tempOccursList.get("BatxNo"));
				tInsuComm.setInsuSignDate(parse.stringToInteger(tempOccursList.get("SignDate")));
				tInsuComm.setInsuredName(tempOccursList.get("InsuredName"));
				tInsuComm.setInsuredAddr(tempOccursList.get("InsuredAddress"));
				tInsuComm.setInsuredTeleph(tempOccursList.get("InsuredTeleNo"));
				tInsuComm.setInsuStartDate(parse.stringToInteger(tempOccursList.get("InsuStartDate")));
				tInsuComm.setInsuEndDate(parse.stringToInteger(tempOccursList.get("InsuEndDate")));
				tInsuComm.setInsuCate(parse.stringToInteger(tempOccursList.get("InsuType")));
				tInsuComm.setInsuPrem(parse.stringToBigDecimal(tempOccursList.get("InsuFee")));
				tInsuComm.setCommRate(parse.stringToBigDecimal(tempOccursList.get("InsuCommRate")));
				tInsuComm.setCommision(parse.stringToBigDecimal(tempOccursList.get("InsuComm")));
				tInsuComm.setTotInsuPrem(parse.stringToBigDecimal(tempOccursList.get("TotalFee")));
				tInsuComm.setTotComm(parse.stringToBigDecimal(tempOccursList.get("TotalComm")));
				tInsuComm.setRecvSeq(tempOccursList.get("CaseNo"));
				tInsuComm.setChargeDate(parse.stringToInteger(tempOccursList.get("AcDate")));
				tInsuComm.setCommDate(parse.stringToInteger(tempOccursList.get("CommDate")));
				tInsuComm.setCustNo(custNo);
				tInsuComm.setFacmNo(facmNo);
				BigDecimal commBase = parse.stringToBigDecimal(tempOccursList.get("CommBase"));
				BigDecimal commRate = parse.stringToBigDecimal(tempOccursList.get("CommRate"));
				;
//				this.info("commBase=" + commBase + ", commRate = " + commRate);
				BigDecimal dueAmt = commBase.multiply(commRate).setScale(0, RoundingMode.HALF_UP);
				tInsuComm.setDueAmt(dueAmt);

//				By I.T. Mail 火險服務抓取 額度檔之火險服務，如果沒有則為戶號的介紹人，若兩者皆為空白者，則為空白(為未發放名單)
//				業務人員任用狀況碼 AgStatusCode =   1:在職 ，才發放 	

				List<Map<String, String>> resultList = l4606ServiceImpl.findEmployee(custNo, facmNo);

				if (resultList != null && !resultList.isEmpty()) {
					Map<String, String> result = resultList.get(0);
					int checkCustNo = parse.stringToInteger(result.get("CustNo") == null ? "" : result.get("CustNo"));
					if (checkCustNo == 0) {
						custErrorCnt++;
						tempOccursList.putParam("ErrorMsg", "戶號錯誤:" + custNo);
						errorList.add(tempOccursList);
						continue;
					}
					empNo = result.get("EmployeeNo") == null ? "" : result.get("EmployeeNo");
					empId = result.get("AgentId") == null ? "" : result.get("AgentId");
					empName = result.get("Fullname") == null ? "" : result.get("Fullname");
					agStatusCode = result.get("AgStatusCode") == null ? "" : result.get("AgStatusCode");
				}

				tInsuComm.setFireOfficer(empNo);
				tInsuComm.setEmpId(empId);
				tInsuComm.setEmpName(empName);
				if ("1".equals(agStatusCode)) {
					mediaCode = "Y";
				}
				tInsuComm.setMediaCode(mediaCode);
				if (dueAmt.compareTo(BigDecimal.ZERO) == 0) {
					zeroDueAmtCnt++;
				} else if ("Y".equals(mediaCode)) {
					paidCnt++;
				} else {
					unPaidCnt++;
				}
				tempOccursList.putParam("Seq", seq);
				successList.add(tempOccursList);
				try {
					insuCommService.insert(tInsuComm, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "InsuComm insert Error : " + e.getErrorMsg());
				}
				count++;
			}

			this.info("InsuComm insert end");
		}
	}

	private void deleteInsuComm(TitaVo titaVo) throws LogicException {

		Slice<InsuComm> sInsuComm = null;

		sInsuComm = insuCommService.insuYearMonthRng(iInsuEndMonth, iInsuEndMonth, this.index, this.limit, titaVo);

		List<InsuComm> deleinsuComm = sInsuComm == null ? null : sInsuComm.getContent();

		if (deleinsuComm != null && !deleinsuComm.isEmpty()) {
			try {
				insuCommService.deleteAll(deleinsuComm, titaVo);
			} catch (DBException e) {
				throw new LogicException("XXXXX", "InsuComm delete Error : " + e.getErrorMsg());
			}
		}
	}
}