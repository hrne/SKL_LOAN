package com.st1.itx.trade.L4;

import java.io.IOException;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuCommService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.trade.L4.L4606Report1;
import com.st1.itx.trade.L4.L4606Report2;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuCommFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4606Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4606Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuCommFileVo insuCommFileVo;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public InsuCommService insuCommService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public WebClient webClient;

	@Autowired
	public L4606Report1 l4606Report1;

	@Autowired
	public L4606Report2 l4606Report2;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	public MakeFile makeFile;

	// 上傳預設目錄
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int iInsuEndMonth = 0;
	private int insuStartDate = 0;
	private int insuEndDate = 0;
//	寄送筆數
	private int commitCnt = 500;
	private String sendMsg = "";
	private Boolean flag = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4606Batch ");
		this.totaVo.init(titaVo);
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		insuStartDate = parse.stringToInteger(iInsuEndMonth + "01");
		insuEndDate = parse.stringToInteger(iInsuEndMonth + "31");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		try {
			execute(titaVo);
		} catch (LogicException e) {
			sendMsg = e.getErrorMsg();
			flag = false;
		}

//		執行成功者，指向查詢畫面
		if (flag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4606", "", sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execute(TitaVo titaVo) throws LogicException {
		String flagA = titaVo.getParam("FlagA");
		String flagB = titaVo.getParam("FlagB");
		String flagC = titaVo.getParam("FlagC");
		long sno = 0;

		deleinsuComm();

//		PC上傳媒體檔轉入佣金媒體檔
		if (!"".equals(flagA)) {

			this.info("flagA Start ...");

//			String fileName = "874-B1.txt";
			String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + titaVo.getParam("FILENA").trim();

			ArrayList<String> dataLineList = new ArrayList<>();

//			 編碼參數，設定為UTF-8 || big5
			try {
				dataLineList = fileCom.intputTxt(filePath1, "big5");
			} catch (IOException e) {
				throw new LogicException("E0014", "L4601(" + filePath1 + ") is error : " + e.getMessage());
			}

//			 使用資料容器內定義的方法切資料
			insuCommFileVo.setValueFromFile(dataLineList);

			ArrayList<OccursList> uploadFile = insuCommFileVo.getOccursList();

			int seq = 0;
			if (uploadFile != null && uploadFile.size() != 0) {
				this.info("tInsuCommId Start");
				for (OccursList tempOccursList : uploadFile) {
					seq = seq + 1;

					if (seq % commitCnt == 0) {
						this.info("Seq : " + seq);
						this.batchTransaction.commit();
					}

					InsuComm tInsuComm = new InsuComm();
					InsuCommId tInsuCommId = new InsuCommId();
					FacMain tFacMain = new FacMain();
					FacMainId tFacMainId = new FacMainId();
					CustMain tCustMain = new CustMain();

					CdEmp tCdEmp = new CdEmp();
					String empNo = "";
					String empId = "";
					String empName = "";
					String agStatusCode = "";
					int custNo = parse.stringToInteger(tempOccursList.get("CustNo").trim());
					int facmNo = parse.stringToInteger(tempOccursList.get("FacmNo").trim());

					tInsuCommId.setInsuYearMonth(iInsuEndMonth);
					tInsuCommId.setInsuCommSeq(seq);

					tInsuComm.setInsuCommId(tInsuCommId);
					tInsuComm.setNowInsuNo(tempOccursList.get("InsuNo").trim());
					tInsuComm.setInsuCate(parse.stringToInteger(tempOccursList.get("InsuType").trim()));
					tInsuComm.setManagerCode(tempOccursList.get("IndexCode").trim());
					tInsuComm.setBatchNo(tempOccursList.get("BatxNo").trim());
					tInsuComm.setInsuType(parse.stringToInteger(tempOccursList.get("InsuKind").trim()));
					tInsuComm.setInsuSignDate(parse.stringToInteger(tempOccursList.get("SignDate").trim()));
					tInsuComm.setInsuredName(tempOccursList.get("InsuredName").trim());
					tInsuComm.setInsuredAddr(tempOccursList.get("InsuredAddress").trim());
					tInsuComm.setInsuredTeleph(tempOccursList.get("InsuredTeleNo").trim());
					tInsuComm.setInsuStartDate(parse.stringToInteger(tempOccursList.get("InsuStartDate").trim()));
					tInsuComm.setInsuEndDate(parse.stringToInteger(tempOccursList.get("InsuEndDate").trim()));
					tInsuComm.setInsuPrem(parse.stringToBigDecimal(tempOccursList.get("InsuFee").trim()).abs());
					tInsuComm.setCommRate(parse.stringToBigDecimal(tempOccursList.get("InsuCommRate").trim()));
					tInsuComm.setCommision(parse.stringToBigDecimal(tempOccursList.get("InsuComm").trim()).abs());
					tInsuComm.setTotInsuPrem(parse.stringToBigDecimal(tempOccursList.get("TotalFee").trim()).abs());
					tInsuComm.setTotComm(parse.stringToBigDecimal(tempOccursList.get("TotalComm").trim()).abs());
					tInsuComm.setRecvSeq(tempOccursList.get("CaseNo").trim());
					tInsuComm.setChargeDate(parse.stringToInteger(tempOccursList.get("AcDate").trim()));
					tInsuComm.setCommDate(parse.stringToInteger(tempOccursList.get("CommDate").trim()));
					tInsuComm.setCustNo(custNo);
					tInsuComm.setFacmNo(facmNo);

//					By I.T. Mail 火險服務抓取 額度檔之火險服務，如果沒有則為戶號的介紹人，若兩者皆為空白者，則為空白(為未發放名單)
//					業務人員任用狀況碼 AgStatusCode =   1:在職 ，才發放 	

					tFacMainId.setCustNo(custNo);
					tFacMainId.setFacmNo(facmNo);
					tFacMain = facMainService.findById(tFacMainId, titaVo);

					if (tFacMain != null && tFacMain.getFireOfficer() != null) {
						empNo = tFacMain.getFireOfficer();
					} else {
						tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
						if (tCustMain != null && tCustMain.getIntroducer() != null) {
							empNo = tCustMain.getIntroducer();
						}
					}

					tCdEmp = cdEmpService.findById(empNo);
					if (tCdEmp != null) {
						empId = tCdEmp.getAgentId();
						empName = tCdEmp.getFullname();
						agStatusCode = tCdEmp.getStatusCode();
					}

					tInsuComm.setFireOfficer(empNo);
					tInsuComm.setEmpId(empId);
					tInsuComm.setEmpName(empName);
					InsuRenew tInsuRenew = insuRenewService.findNowInsuNoFirst(custNo, facmNo,
							tempOccursList.get("InsuNo").trim(), titaVo);
					if (tInsuRenew != null && "1".equals(agStatusCode)) {
						tInsuComm.setDueAmt(parse.stringToBigDecimal(tempOccursList.get("TotalComm").trim()));
					} else {
						tInsuComm.setDueAmt(BigDecimal.ZERO);
					}

					try {
						insuCommService.insert(tInsuComm);
					} catch (DBException e) {
						throw new LogicException("E0005", "InsuComm insert Error : " + e.getErrorMsg());
					}
				}
				this.info("InsuComm insert end");
			}
		}

//		火險佣金發放報表(未發放 = '佣金日期=0?')
//		未發放 -> 火險服務(FireOfficer) = 空白(null)

		if (!"".equals(flagB))

		{
			this.info("flagB Start ...");

			l4606Report1.exec(titaVo);

			l4606Report2.exec(titaVo);

			sendMsg = "火險佣金發放報表已完成";
		}

//		產生下傳媒體
		if (!"".equals(flagC)) {
			this.info("flagC Start ...");

			List<InsuComm> lInsuComm = new ArrayList<InsuComm>();

//			暫定 : D:\\temp\\LNM23P.TXT
//			String path = outFolder + "LNM23P.TXT";

			ArrayList<OccursList> tmp = new ArrayList<>();

			Slice<InsuComm> sInsuComm = null;

			sInsuComm = insuCommService.insuYearMonthRng(iInsuEndMonth, iInsuEndMonth, this.index, this.limit);

			lInsuComm = sInsuComm == null ? null : sInsuComm.getContent();

			if (lInsuComm != null && lInsuComm.size() != 0) {

//				1.Group by ID
				HashMap<String, BigDecimal> sumComm = new HashMap<>();
				HashMap<String, BigDecimal> sumPrem = new HashMap<>();
				HashMap<String, Integer> cntComm = new HashMap<>();

				for (InsuComm tInsuComm : lInsuComm) {
					if (!"".equals(tInsuComm.getFireOfficer().trim())) {
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

//				2.if already output -> continue
				HashMap<String, Integer> flagComm = new HashMap<>();

				int seq = 0;

				for (InsuComm tInsuComm : lInsuComm) {
					if (!"".equals(tInsuComm.getFireOfficer().trim())) {
						OccursList occursList = new OccursList();

						this.info("FireOfficer ... '" + tInsuComm.getFireOfficer().trim() + "'");

						String empId = tInsuComm.getEmpId();

						if (flagComm.containsKey(empId)) {
							continue;
						} else {
							flagComm.put(empId, 1);
						}
						seq = seq + 1;

						if (seq % commitCnt == 0) {
							this.batchTransaction.commit();
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
				ArrayList<String> file = insuCommFileVo.toFile();

//				try {
//					// 用共用工具寫入檔案
//					fileCom.outputTxt(file, path);
//				} catch (IOException e) {
//					throw new LogicException("XXXXX", "LNM23P output error : " + e.getMessage());
//				}

				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-火險佣金媒體檔", "LNM23P.txt", 2);

				for (String line : file) {
					makeFile.put(line);
				}

				sno = makeFile.close();

				this.info("sno : " + sno);

				makeFile.toFile(sno);
			}
		}
	}

	private void deleinsuComm() throws LogicException {
		List<InsuComm> deleinsuComm = new ArrayList<InsuComm>();

		Slice<InsuComm> sInsuComm = null;

		sInsuComm = insuCommService.insuYearMonthRng(iInsuEndMonth, iInsuEndMonth, this.index, this.limit);

		deleinsuComm = sInsuComm == null ? null : sInsuComm.getContent();

		if (deleinsuComm != null && deleinsuComm.size() != 0) {
			int seq = 0;
			for (InsuComm tInsuComm : deleinsuComm) {
				seq = seq + 1;
				if (seq % commitCnt == 0) {
					this.info("seq : " + seq);
					this.batchTransaction.commit();
				}

				tInsuComm = insuCommService.holdById(tInsuComm.getInsuCommId());
				try {
					insuCommService.delete(tInsuComm);
				} catch (DBException e) {
					throw new LogicException("XXXXX", "InsuComm delete Error : " + e.getErrorMsg());
				}
			}
		}
	}

	private void setReportA() {
		this.info("ReportA Start...");

		HashMap<tmpComm, Integer> commCnt = new HashMap<>();
		HashMap<tmpComm, BigDecimal> commSum = new HashMap<>();

		List<InsuComm> lInsuComm = new ArrayList<InsuComm>();

		Slice<InsuComm> sInsuComm = null;

		sInsuComm = insuCommService.findL4606A(iInsuEndMonth, insuStartDate, insuEndDate, this.index, this.limit);

		lInsuComm = sInsuComm == null ? null : sInsuComm.getContent();

//		第一筆        :筆數、金額初始
//		第二筆~
//		倒數第一筆:判斷是否有這個ID
//				若有:筆數、金額+1，寫一筆明細
//				若無:產生小計欄位、小計結尾"-----"，並寫入下一組Id
//		最後一筆之小計與結尾: 判斷補上
//		總計筆數、服務人筆數、金額
		int cnt = 0;
		int cntEmp = 0;
		BigDecimal sum = BigDecimal.ZERO;

		if (lInsuComm != null && lInsuComm.size() != 0) {
			int rounds = 1;
			for (InsuComm tInsuComm : lInsuComm) {
				this.info("rounds = " + rounds);

				if (rounds % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				tmpComm comm1 = new tmpComm(tInsuComm.getEmpId(), tInsuComm.getFireOfficer(), tInsuComm.getEmpName());
				OccursList occursList = new OccursList();

				if (rounds == 1) {
					cntEmp = cntEmp + 1;
					commCnt.put(comm1, 1);
					commSum.put(comm1, tInsuComm.getCommision());
				} else {
					if (commCnt.containsKey(comm1)) {
						commCnt.put(comm1, commCnt.get(comm1) + 1);
						commSum.put(comm1, commSum.get(comm1).add(tInsuComm.getCommision()));
					} else {
						occursList.putParam("ReportALine",
								FormatUtil.padLeft("小計:", 89) + FormatUtil.padLeft("" + commCnt.get(comm1), 8) + "筆"
										+ FormatUtil.padLeft("" + commSum.get(comm1), 79));
						totaA.addOccursList(occursList);

						occursList = new OccursList();
						occursList.putParam("ReportALine", addMark("-", 200));
						totaA.addOccursList(occursList);

						commCnt.put(comm1, 1);
						commSum.put(comm1, tInsuComm.getCommision());

						cntEmp = cntEmp + 1;
					}
				}

				occursList = new OccursList();
				occursList.putParam("ReportALine",
						FormatUtil.padX(tInsuComm.getNowInsuNo(), 20) + " "
								+ FormatUtil.pad9("" + tInsuComm.getInsuCate(), 2) + "  "
								+ FormatUtil.padLeft("" + tInsuComm.getInsuPrem(), 21) + FormatUtil.padX("", 4)
								+ FormatUtil.padX("" + tInsuComm.getInsuStartDate(), 8) + " "
								+ FormatUtil.padX("" + tInsuComm.getInsuEndDate(), 8) + " "
								+ FormatUtil.padX("" + tInsuComm.getInsuredAddr(), 40) + " "
								+ FormatUtil.pad9("" + tInsuComm.getCustNo(), 7) + " "
								+ FormatUtil.pad9("" + tInsuComm.getFacmNo(), 3) + " "
								+ FormatUtil.padX("" + tInsuComm.getEmpId(), 10) + " "
								+ FormatUtil.padX("" + tInsuComm.getFireOfficer(), 6) + " "
								+ FormatUtil.padX("" + tInsuComm.getEmpName(), 20) + " "
								+ FormatUtil.padLeft("" + tInsuComm.getCommision(), 21) + FormatUtil.padX("", 18));
				totaA.addOccursList(occursList);

				if (rounds == lInsuComm.size()) {
					occursList = new OccursList();
					occursList.putParam("ReportALine",
							FormatUtil.padLeft("小計:", 89) + FormatUtil.padLeft("" + commCnt.get(comm1), 8) + " 筆"
									+ FormatUtil.padLeft("" + commSum.get(comm1), 79));
					totaA.addOccursList(occursList);

					occursList = new OccursList();
					occursList.putParam("ReportALine", addMark("-", 200));
					totaA.addOccursList(occursList);
				}
				rounds++;
				cnt = cnt + 1;
				sum = sum.add(tInsuComm.getCommision());
			}
		}
		totaA.putParam("MSGID", "L466A");
		totaA.putParam("ReportACnt", cnt);
		totaA.putParam("ReportACntEmp", cntEmp);
		totaA.putParam("ReportASum", sum);

		this.info("tota A : " + totaA.toString());
		this.addList(totaA);
	}

	private void setReportB() {
		this.info("ReportB Start...");

		List<InsuComm> lInsuComm = new ArrayList<InsuComm>();

		Slice<InsuComm> sInsuComm = null;

		sInsuComm = insuCommService.findL4606A(iInsuEndMonth, 0, 0, this.index, this.limit);

		lInsuComm = sInsuComm == null ? null : sInsuComm.getContent();

		int seq = 0;

		if (lInsuComm != null && lInsuComm.size() != 0) {
			for (InsuComm tInsuComm : lInsuComm) {
				seq = seq + 1;

				if (seq % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				OccursList occursList = new OccursList();

				occursList.putParam("ReportBInsuNo", tInsuComm.getNowInsuNo());
				occursList.putParam("ReportBInsuType", tInsuComm.getInsuCate());
				occursList.putParam("ReportBInsuFee", tInsuComm.getInsuPrem());
				occursList.putParam("ReportBInsuStartDate", tInsuComm.getInsuStartDate());
				occursList.putParam("ReportBInsuEndDate", tInsuComm.getInsuEndDate());
				occursList.putParam("ReportBInsuredAddress", tInsuComm.getInsuredAddr());
				occursList.putParam("ReportBCustNo", tInsuComm.getCustNo());
				occursList.putParam("ReportBFacmNo", tInsuComm.getFacmNo());
				occursList.putParam("ReportBTelCode", tInsuComm.getEmpId());
				occursList.putParam("ReportBTelId", tInsuComm.getFireOfficer());
				occursList.putParam("ReportBTelName", tInsuComm.getEmpName());
				occursList.putParam("ReportBCommAmt", tInsuComm.getCommision());

				totaB.addOccursList(occursList);
				totaB.putParam("MSGID", "L466B");
			}
			this.addList(totaB);
		}
	}

//	暫時紀錄戶號額度
	private class tmpComm implements Comparable<tmpComm> {
		private String empId = "";
		private String empNo = "";
		private String empName = "";

		public tmpComm(String empId, String empNo, String empName) {
			this.setEmpId(empId);
			this.setEmpNo(empNo);
			this.setEmpName(empName);
		}

		public void setEmpId(String empId) {
			this.empId = empId;
		}

		public void setEmpName(String empName) {
			this.empName = empName;
		}

		public void setEmpNo(String empNo) {
			this.empNo = empNo;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpComm other = (tmpComm) obj;
			if (empId == null) {
				if (other.empId != null)
					return false;
			} else if (!empId.equals(other.empId))
				return false;
			if (empName == null) {
				if (other.empName != null)
					return false;
			} else if (!empName.equals(other.empName))
				return false;
			if (empNo == null) {
				if (other.empNo != null)
					return false;
			} else if (!empNo.equals(other.empNo))
				return false;
			return true;
		}

		@Override
		public int compareTo(tmpComm other) {
			if (this.empId.compareTo(other.empId) != 0) {
				return this.empId.compareTo(other.empId);
			} else if (this.empName.compareTo(other.empName) != 0) {
				return this.empName.compareTo(other.empName);
			} else if (this.empNo.compareTo(other.empNo) != 0) {
				return this.empNo.compareTo(other.empNo);
			} else {
				return 0;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((empId == null) ? 0 : empId.hashCode());
			result = prime * result + ((empName == null) ? 0 : empName.hashCode());
			result = prime * result + ((empNo == null) ? 0 : empNo.hashCode());
			return result;
		}
	}

	private String addMark(String mark, int number) {
		String result = mark;
		for (int i = 1; i < number; i++) {
			result = result + mark;
		}
		return result;
	}
}