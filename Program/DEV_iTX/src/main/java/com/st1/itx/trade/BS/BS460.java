package com.st1.itx.trade.BS;

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
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.domain.InsuRenewMediaTempId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS460")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class BS460 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(BS460.class);

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public ClBuildingParkingService clBuildingParkingService;

	@Autowired
	public ClBuildingPublicService clBuildingPublicService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public ClBuildingOwnerService clBuildingOwnerService;

	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public WebClient webClient;

	private int iInsuEndMonth = 0;
	private int insuStartDate = 0;
	private int insuEndDate = 0;
	private ArrayList<OccursList> tmp = new ArrayList<>();
	private int custNo = 0;
	private int facmNo = 0;
	private int repayCode = 0;
	private Boolean checkFlag = true;
	private String sendMsg = " ";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS460 ");
		this.totaVo.init(titaVo);

		try {
			execute(titaVo);
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					"L4600 已產生火險到期檔", titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4600", titaVo.getTlrNo(),
					sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execute(TitaVo titaVo) throws LogicException {
//		火險到期檔產生作業(到期前一個月)
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		insuStartDate = parse.stringToInteger(iInsuEndMonth + "01");
		insuEndDate = parse.stringToInteger(iInsuEndMonth + "31");
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

//		檢核該月份是否做過詢價
		check(titaVo);

//		刪除舊資料
		deleInsuRenew(iInsuEndMonth);

//		新保 ->續保
//		step1 將到期年月與tita相同之新保者，自動寫入續保檔下一期
		orignalRenew(titaVo);

//		續保 -> 續保(新年度)
//		step2 將續保檔內到期年月與tita相同者產出下一期
		renewRenew(titaVo);

//		續保(新年度) ->FILE
//		step3 將下一期產出file
//		因可能會有多筆保單合為一筆的情況，所以需先insert完再逐筆合計寫入File
//		將到期資料寫入File
		toFile(titaVo);

		// 把明細資料容器裝到檔案資料容器內
		insuRenewFileVo.setOccursList(tmp);
		// 轉換資料格式
		ArrayList<String> file = insuRenewFileVo.toFile();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-火險到期檔",
				"LNM01P.txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);
	}

	private void orignalRenew(TitaVo titaVo) throws LogicException {
//		新保 ->續保
//		step1 將到期年月與tita相同之新保者，自動寫入續保檔下一期
		List<ClFac> lClFac = new ArrayList<ClFac>();

		List<InsuOrignal> lInsuOrignal = new ArrayList<InsuOrignal>();

		Slice<InsuOrignal> sInsuOrignal = null;

		sInsuOrignal = insuOrignalService.insuEndDateRNG(insuStartDate, insuEndDate, this.index, this.limit, titaVo);

		lInsuOrignal = sInsuOrignal == null ? null : sInsuOrignal.getContent();

		if (lInsuOrignal != null && lInsuOrignal.size() != 0) {
//			合計
			HashMap<String, Integer> flag = new HashMap<>();
			HashMap<String, BigDecimal> fireAmt = new HashMap<>();
			HashMap<String, BigDecimal> fireFee = new HashMap<>();
			HashMap<String, BigDecimal> eqthAmt = new HashMap<>();
			HashMap<String, BigDecimal> eqthFee = new HashMap<>();

			for (InsuOrignal tInsuOrignal : lInsuOrignal) {
				String insuNo = tInsuOrignal.getInsuOrignalId().getOrigInsuNo();
				BigDecimal fa = tInsuOrignal.getFireInsuCovrg();
				BigDecimal ff = tInsuOrignal.getFireInsuPrem();
				BigDecimal ea = tInsuOrignal.getEthqInsuCovrg();
				BigDecimal ef = tInsuOrignal.getEthqInsuPrem();

				if (!fireAmt.containsKey(insuNo)) {
					fireAmt.put(insuNo, fa);
				} else {
					fireAmt.put(insuNo, fa.add(fireAmt.get(insuNo)));
				}

				if (!fireFee.containsKey(insuNo)) {
					fireFee.put(insuNo, ff);
				} else {
					fireFee.put(insuNo, ff.add(fireFee.get(insuNo)));
				}

				if (!eqthAmt.containsKey(insuNo)) {
					eqthAmt.put(insuNo, ea);
				} else {
					eqthAmt.put(insuNo, ea.add(eqthAmt.get(insuNo)));
				}

				if (!eqthFee.containsKey(insuNo)) {
					eqthFee.put(insuNo, ef);
				} else {
					eqthFee.put(insuNo, ef.add(eqthFee.get(insuNo)));
				}
			}

			for (InsuOrignal tInsuOrignal : lInsuOrignal) {
				InsuRenew tempInsuRenew = new InsuRenew();
				InsuRenew t2InsuRenew = new InsuRenew();
				InsuRenewId tempInsuRenewId = new InsuRenewId();
				Slice<ClFac> sClFac = null;

				String insuNo = tInsuOrignal.getInsuOrignalId().getOrigInsuNo();

				tempInsuRenewId.setClCode1(tInsuOrignal.getClCode1());
				tempInsuRenewId.setClCode2(tInsuOrignal.getClCode2());
				tempInsuRenewId.setClNo(tInsuOrignal.getClNo());
				tempInsuRenewId.setPrevInsuNo(tInsuOrignal.getOrigInsuNo());
				tempInsuRenewId.setEndoInsuNo(" ");

//				排除自保件及其批單號碼
				t2InsuRenew = insuRenewService.findById(tempInsuRenewId, titaVo);

//				自保件跳過
				if (t2InsuRenew != null) {
					this.info("排除自保件 ... " + insuNo);
					continue;
				}

//				重複保單號碼跳過
				if (flag.containsKey(insuNo)) {
					this.info("重複保單號碼 ... " + insuNo);
					continue;
				} else {
					flag.put(insuNo, 1);
				}

//				戶號 抓取 擔保品關聯額度檔
				sClFac = clFacService.clNoEq(tInsuOrignal.getClCode1(), tInsuOrignal.getClCode2(),
						tInsuOrignal.getClNo(), this.index, this.limit);

				lClFac = sClFac == null ? null : sClFac.getContent();

				findFacmNo(lClFac);

//				同擔保品內 判斷status
//				額度 0.正常>2.催收>7.部呆>6.呆帳，排除結案戶(其他戶況)。
//				若戶況相同選擇額度最大者
				this.info("custNo : " + custNo);
				this.info("facmNo : " + facmNo);

				if (custNo == 0) {
					this.info("custNo == 0 ，檢核是否無撥款號碼");
					continue;
				}

				tempInsuRenew.setCustNo(custNo);
				tempInsuRenew.setFacmNo(facmNo);

				tempInsuRenew.setInsuRenewId(tempInsuRenewId);
				tempInsuRenew.setInsuYearMonth(iInsuEndMonth);
				tempInsuRenew.setNowInsuNo("");
				tempInsuRenew.setOrigInsuNo(tInsuOrignal.getOrigInsuNo());
				tempInsuRenew.setRenewCode(2);
				tempInsuRenew.setInsuCompany(tInsuOrignal.getInsuCompany());
				tempInsuRenew.setInsuTypeCode(tInsuOrignal.getInsuTypeCode());
				tempInsuRenew.setRepayCode(repayCode);
//				tempInsuRenew.setFireInsuCovrg(BigDecimal.ZERO);
//				tempInsuRenew.setFireInsuPrem(BigDecimal.ZERO);
//				tempInsuRenew.setEthqInsuCovrg(BigDecimal.ZERO);
//				tempInsuRenew.setEthqInsuPrem(BigDecimal.ZERO);
				tempInsuRenew.setFireInsuCovrg(fireAmt.get(insuNo));
				tempInsuRenew.setFireInsuPrem(fireFee.get(insuNo));
				tempInsuRenew.setEthqInsuCovrg(eqthAmt.get(insuNo));
				tempInsuRenew.setEthqInsuPrem(eqthFee.get(insuNo));

				tempInsuRenew.setInsuStartDate(tInsuOrignal.getInsuEndDate());
				tempInsuRenew.setInsuEndDate(tInsuOrignal.getInsuEndDate() + 10000);
				tempInsuRenew.setTotInsuPrem(BigDecimal.ZERO);
				tempInsuRenew.setAcDate(0);
				tempInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
				tempInsuRenew.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
				tempInsuRenew.setNotiTempFg("N");
				tempInsuRenew.setStatusCode(0);
				tempInsuRenew.setOvduDate(0);
				tempInsuRenew.setOvduNo(BigDecimal.ZERO);

				try {
					insuRenewService.insert(tempInsuRenew);
				} catch (DBException e) {
					throw new LogicException("E0005", "L4600 " + e.getErrorMsg());
				}
			}
		}
	}

	private void renewRenew(TitaVo titaVo) throws LogicException {
//		續保 -> 續保(新年度)
//		step2 將續保檔內到期年月與tita相同者產出下一期
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		Slice<InsuRenew> sInsuRenew = null;

		sInsuRenew = insuRenewService.findL4965Z(insuStartDate, insuEndDate, this.index, this.limit);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			HashMap<String, Integer> flag = new HashMap<>();
			HashMap<String, BigDecimal> fireAmt = new HashMap<>();
			HashMap<String, BigDecimal> fireFee = new HashMap<>();
			HashMap<String, BigDecimal> eqthAmt = new HashMap<>();
			HashMap<String, BigDecimal> eqthFee = new HashMap<>();

			for (InsuRenew tInsuRenew : lInsuRenew) {
				String insuNo = tInsuRenew.getInsuRenewId().getPrevInsuNo();
				BigDecimal fa = tInsuRenew.getFireInsuCovrg();
				BigDecimal ff = tInsuRenew.getFireInsuPrem();
				BigDecimal ea = tInsuRenew.getEthqInsuCovrg();
				BigDecimal ef = tInsuRenew.getEthqInsuPrem();

				if (!fireAmt.containsKey(insuNo)) {
					fireAmt.put(insuNo, fa);
				} else {
					fireAmt.put(insuNo, fa.add(fireAmt.get(insuNo)));
				}

				if (!fireFee.containsKey(insuNo)) {
					fireFee.put(insuNo, ff);
				} else {
					fireFee.put(insuNo, ff.add(fireFee.get(insuNo)));
				}

				if (!eqthAmt.containsKey(insuNo)) {
					eqthAmt.put(insuNo, ea);
				} else {
					eqthAmt.put(insuNo, ea.add(eqthAmt.get(insuNo)));
				}

				if (!eqthFee.containsKey(insuNo)) {
					eqthFee.put(insuNo, ef);
				} else {
					eqthFee.put(insuNo, ef.add(eqthFee.get(insuNo)));
				}
			}

			for (InsuRenew tInsuRenew : lInsuRenew) {
				String insuNo = tInsuRenew.getInsuRenewId().getPrevInsuNo();
				String insuNoL = tInsuRenew.getNowInsuNo();

//				尚未續保跳過
				if ("".equals(insuNoL)) {
					this.info("無新保單號碼 ... " + insuNo);
					continue;
				}

//				已提前自保跳過
				InsuRenewId nInsuRenewId = new InsuRenewId();
				nInsuRenewId.setClCode1(tInsuRenew.getClCode1());
				nInsuRenewId.setClCode2(tInsuRenew.getClCode2());
				nInsuRenewId.setClNo(tInsuRenew.getClNo());
				nInsuRenewId.setPrevInsuNo(insuNoL);
				nInsuRenewId.setEndoInsuNo(" ");

				InsuRenew nInsuRenew = insuRenewService.findById(nInsuRenewId, titaVo);

//				排除自保件
				if (nInsuRenew != null) {
					this.info("排除自保件 ... " + insuNo);
					continue;
				}

//				重複保單號碼跳過
				if (flag.containsKey(insuNo)) {
					this.info("重複保單號碼 ... " + insuNo);
					continue;
				} else {
					flag.put(insuNo, 1);
				}

				InsuRenewId tInsuRenewId = new InsuRenewId();
				InsuRenew tempInsuRenew = new InsuRenew();

				InsuOrignal tInsuOrignal = new InsuOrignal();
				tInsuOrignal = insuOrignalService.clNoFirst(tInsuRenew.getClCode1(), tInsuRenew.getClCode2(),
						tInsuRenew.getClNo());

				tInsuRenewId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
				tInsuRenewId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
				tInsuRenewId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
				tInsuRenewId.setPrevInsuNo(tInsuRenew.getNowInsuNo());
				tInsuRenewId.setEndoInsuNo(" ");

				tempInsuRenew.setInsuRenewId(tInsuRenewId);
				tempInsuRenew.setNowInsuNo("");

				this.info("iInsuEndMonth : " + iInsuEndMonth);
				this.info("getCustNo : " + tInsuRenew.getCustNo());
				this.info("getFacmNo : " + tInsuRenew.getFacmNo());

				tempInsuRenew.setInsuYearMonth(iInsuEndMonth);
				tempInsuRenew.setCustNo(tInsuRenew.getCustNo());
				tempInsuRenew.setFacmNo(tInsuRenew.getFacmNo());

				if (tInsuOrignal != null) {
					tempInsuRenew.setOrigInsuNo(tInsuOrignal.getOrigInsuNo());
				} else {
					tempInsuRenew.setOrigInsuNo(lastYearInsuRenew(insuStartDate, insuEndDate));
//					前一年的InsuRenew.nowInsuNo
				}

				tempInsuRenew.setRenewCode(2);
				tempInsuRenew.setInsuCompany(tInsuRenew.getInsuCompany());
				tempInsuRenew.setInsuTypeCode(tInsuRenew.getInsuTypeCode());
				tempInsuRenew.setRepayCode(tInsuRenew.getRepayCode());
				tempInsuRenew.setFireInsuCovrg(fireAmt.get(insuNo));
				tempInsuRenew.setFireInsuPrem(fireFee.get(insuNo));
				tempInsuRenew.setEthqInsuCovrg(eqthAmt.get(insuNo));
				tempInsuRenew.setEthqInsuPrem(eqthFee.get(insuNo));
				tempInsuRenew.setInsuStartDate(tInsuRenew.getInsuEndDate());
				tempInsuRenew.setInsuEndDate(tInsuRenew.getInsuEndDate() + 10000);
				tempInsuRenew.setTotInsuPrem(tInsuRenew.getEthqInsuPrem().add(tInsuRenew.getFireInsuPrem()));
				tempInsuRenew.setAcDate(0);
				tempInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
				tempInsuRenew.setTitaTxtNo(this.getTxBuffer().getTxCom().getRelTno() + "");
				tempInsuRenew.setNotiTempFg("N");
				tempInsuRenew.setStatusCode(0);
				tempInsuRenew.setOvduDate(0);
				tempInsuRenew.setOvduNo(BigDecimal.ZERO);

				try {
					insuRenewService.insert(tempInsuRenew);
				} catch (DBException e) {
					throw new LogicException("E0007", "InsuRenew update error : " + e.getErrorMsg());
				}
			}
		}
	}

	private void findFacmNo(List<ClFac> lClFac) {
		int flagStatus = 0;
		HashMap<tmpFacm, Integer> applNo = new HashMap<>();
		custNo = 0;
		facmNo = 0;
		repayCode = 0;

		if (lClFac != null && lClFac.size() != 0) {
			for (ClFac pClFac : lClFac) {
				List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

				Slice<LoanBorMain> sLoanBorMain = null;

				sLoanBorMain = loanBorMainService.bormCustNoEq(pClFac.getCustNo(), pClFac.getFacmNo(),
						pClFac.getFacmNo(), 0, 999, this.index, this.limit);

				lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

//				1.戶況順序 0.正常>2.催收>7.部呆>6.呆帳，排除結案戶。
//				2.若同戶況，根據申請序號最大者
				if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
					for (LoanBorMain tLoanBorMain : lLoanBorMain) {
						tmpFacm temp = new tmpFacm(tLoanBorMain.getCustNo(), tLoanBorMain.getStatus());

						FacMain tFacMain = new FacMain();
						FacMainId tFacMainId = new FacMainId();
						tFacMainId.setCustNo(tLoanBorMain.getCustNo());
						tFacMainId.setFacmNo(tLoanBorMain.getFacmNo());
						tFacMain = facMainService.findById(tFacMainId);
						if (tFacMain != null) {
							if (!applNo.containsKey(temp)) {
								applNo.put(temp, 0);
							}
						}

						switch (tLoanBorMain.getStatus()) {
						case 0:
							if (flagStatus == 1) {
								if (applNo.get(temp) < tFacMain.getApplNo()) {
									custNo = tLoanBorMain.getCustNo();
									facmNo = tLoanBorMain.getFacmNo();
									repayCode = tFacMain.getRepayCode();
									applNo.put(temp, tFacMain.getApplNo());
								}
							} else {
								flagStatus = 1;
								custNo = tLoanBorMain.getCustNo();
								facmNo = tLoanBorMain.getFacmNo();
								repayCode = tFacMain.getRepayCode();
							}
							break;
						case 2:
							if (flagStatus > 2 || flagStatus == 0) {
								flagStatus = 2;
								custNo = tLoanBorMain.getCustNo();
								facmNo = tLoanBorMain.getFacmNo();
								repayCode = tFacMain.getRepayCode();
							} else if (flagStatus == 2) {
								if (applNo.get(temp) < tFacMain.getApplNo()) {
									custNo = tLoanBorMain.getCustNo();
									facmNo = tLoanBorMain.getFacmNo();
									repayCode = tFacMain.getRepayCode();
									applNo.put(temp, tFacMain.getApplNo());
								}
							}
							break;
						case 7:
							if (flagStatus > 3 || flagStatus == 0) {
								flagStatus = 3;
								custNo = tLoanBorMain.getCustNo();
								facmNo = tLoanBorMain.getFacmNo();
								repayCode = tFacMain.getRepayCode();
							} else if (flagStatus == 3) {
								if (applNo.get(temp) < tFacMain.getApplNo()) {
									custNo = tLoanBorMain.getCustNo();
									facmNo = tLoanBorMain.getFacmNo();
									repayCode = tFacMain.getRepayCode();
									applNo.put(temp, tFacMain.getApplNo());
								}
							}
							break;
						case 6:
							if (flagStatus > 4 || flagStatus == 0) {
								flagStatus = 4;
								custNo = tLoanBorMain.getCustNo();
								facmNo = tLoanBorMain.getFacmNo();
								repayCode = tFacMain.getRepayCode();
							} else if (flagStatus == 4) {
								if (applNo.get(temp) < tFacMain.getApplNo()) {
									custNo = tLoanBorMain.getCustNo();
									facmNo = tLoanBorMain.getFacmNo();
									repayCode = tFacMain.getRepayCode();
									applNo.put(temp, tFacMain.getApplNo());
								}
							}
							break;
						}
					}
				}
			}
		}
	}

	private String dateSlashFormat(int today) {
		String slashedDate = "";
		String acToday = "";
		if (today >= 1 && today < 19110000) {
			acToday = FormatUtil.pad9("" + (today + 19110000), 8);
		} else if (today >= 19110000) {
			acToday = FormatUtil.pad9("" + today, 8);
		}
//		slashedDate = acToday.substring(0, 4) + "/" + acToday.substring(4, 6) + "/" + acToday.substring(6, 8);
		slashedDate = acToday;

		return slashedDate;
	}

	private void deleInsuRenew(int insuMonth) throws LogicException {
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		Slice<InsuRenew> sInsuRenew = null;

		sInsuRenew = insuRenewService.selectC(insuMonth, this.index, this.limit);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			for (InsuRenew tInsuRenew : lInsuRenew) {

				if (tInsuRenew.getAcDate() > 0) {
					this.info("continue... ，ACDATE > 0");
					continue;
				}

//				排除自保件
				if (tInsuRenew.getRenewCode() == 1) {
					this.info("排除自保件 ... ");
					continue;
				}

				insuRenewService.holdById(tInsuRenew.getInsuRenewId());

				try {
					insuRenewService.delete(tInsuRenew);
				} catch (DBException e) {
					throw new LogicException("E0008", "InsuRenew delete error : " + e.getErrorMsg());
				}
			}
		}
	}

	private String lastYearInsuRenew(int insuStartDate, int insuEndDate) {
		String result = "";

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		Slice<InsuRenew> sInsuRenew = null;

		sInsuRenew = insuRenewService.findL4965Z(insuStartDate, insuEndDate, this.index, this.limit);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			for (InsuRenew tInsuRenew : lInsuRenew) {
				result = tInsuRenew.getNowInsuNo();
			}
		}
		return result;
	}

	private String findZipCode(CustMain tCustMain) {
		String zip = "";
		if (tCustMain != null) {
			CdArea tCdArea = new CdArea();
			CdAreaId tCdAreaId = new CdAreaId();
			tCdAreaId.setCityCode(tCustMain.getCurrCityCode());
			tCdAreaId.setAreaCode(tCustMain.getCurrAreaCode());
			tCdArea = cdAreaService.findById(tCdAreaId);
			if (tCdArea != null) {
				zip = tCdArea.getZip3();
			}
		}
		return zip;
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public String replaceComma(String addresss) {
		String result = addresss;

		if (addresss.indexOf(",") >= 0) {
			this.info("has , ");
			this.info("b4 addresss : " + addresss);
			result = addresss.replace(",", "，");
			this.info("ft addresss = " + result);
		}

		return result;
	}

//	將下一期產出file
	public void toFile(TitaVo titaVo) throws LogicException {
//		條件 : 次一年度到期年月 & 續保\自保記號==2.續保
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		Slice<InsuRenew> sInsuRenew = null;

		sInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

//		QC.360 同保單號碼合計

		HashMap<String, Integer> flag = new HashMap<>();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			HashMap<String, BigDecimal> fireAmt = new HashMap<>();
			HashMap<String, BigDecimal> fireFee = new HashMap<>();
			HashMap<String, BigDecimal> eqthAmt = new HashMap<>();
			HashMap<String, BigDecimal> eqthFee = new HashMap<>();

			for (InsuRenew tInsuRenew : lInsuRenew) {
				String insuNo = tInsuRenew.getInsuRenewId().getPrevInsuNo();

//				排除自保件
				if (tInsuRenew.getRenewCode() == 1) {
					this.info("排除自保件 ... " + insuNo);
					continue;
				}

				BigDecimal fa = tInsuRenew.getFireInsuCovrg();
				BigDecimal ff = tInsuRenew.getFireInsuPrem();
				BigDecimal ea = tInsuRenew.getEthqInsuCovrg();
				BigDecimal ef = tInsuRenew.getEthqInsuPrem();

				if (!fireAmt.containsKey(insuNo)) {
					fireAmt.put(insuNo, fa);
				} else {
					fireAmt.put(insuNo, fa.add(fireAmt.get(insuNo)));
				}

				if (!fireFee.containsKey(insuNo)) {
					fireFee.put(insuNo, ff);
				} else {
					fireFee.put(insuNo, ff.add(fireFee.get(insuNo)));
				}

				if (!eqthAmt.containsKey(insuNo)) {
					eqthAmt.put(insuNo, ea);
				} else {
					eqthAmt.put(insuNo, ea.add(eqthAmt.get(insuNo)));
				}

				if (!eqthFee.containsKey(insuNo)) {
					eqthFee.put(insuNo, ef);
				} else {
					eqthFee.put(insuNo, ef.add(eqthFee.get(insuNo)));
				}
			}

			for (InsuRenew tInsuRenew : lInsuRenew) {
				String insuNo = tInsuRenew.getInsuRenewId().getPrevInsuNo();

//				重複保單號碼跳過
				if (flag.containsKey(insuNo)) {
					this.info("重複保單號碼 ... " + insuNo);
					continue;
				} else {
					flag.put(insuNo, 1);
				}

//				排除自保件
				if (tInsuRenew.getRenewCode() == 1) {
					this.info("排除自保件 ... " + insuNo);
					continue;
				}

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

				ClBuildingId tClBuildingId = new ClBuildingId();
				tClBuildingId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
				tClBuildingId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
				tClBuildingId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
				ClBuilding tClBuilding = new ClBuilding();
				tClBuilding = clBuildingService.findById(tClBuildingId);

				FacMainId tFacMainId = new FacMainId();
				FacMain tFacMain = new FacMain();
				tFacMainId.setCustNo(tInsuRenew.getCustNo());
				tFacMainId.setFacmNo(tInsuRenew.getFacmNo());
				tFacMain = facMainService.findById(tFacMainId);

				ClBuildingOwner tClBuildingOwner = new ClBuildingOwner();
				tClBuildingOwner = clBuildingOwnerService.clNoFirst(tInsuRenew.getInsuRenewId().getClCode1(),
						tInsuRenew.getInsuRenewId().getClCode2(), tInsuRenew.getInsuRenewId().getClNo());

				OccursList occursList = new OccursList();
				occursList.putParam("FireInsuMonth", FormatUtil.padX("" + (tInsuRenew.getInsuYearMonth()), 6));
				occursList.putParam("ReturnCode", FormatUtil.pad9("99", 2));
				occursList.putParam("InsuCampCode", FormatUtil.pad9("01", 2));
				if (tClBuildingOwner != null) {
					occursList.putParam("InsuCustId", FormatUtil.padX(tClBuildingOwner.getOwnerId(), 10));
					occursList.putParam("InsuCustName", FormatUtil.padX(tClBuildingOwner.getOwnerName(), 12));
				} else {
					occursList.putParam("InsuCustId", FormatUtil.padX("", 10));
					occursList.putParam("InsuCustName", FormatUtil.padX("", 12));
				}
				if (tCustMain != null) {
					occursList.putParam("LoanCustId", FormatUtil.padX(tCustMain.getCustId(), 10));
					occursList.putParam("LoanCustName", FormatUtil.padX(tCustMain.getCustName(), 12));
				} else {
					occursList.putParam("LoanCustId", FormatUtil.padX("", 10));
					occursList.putParam("LoanCustName", FormatUtil.padX("", 12));
				}

				BigDecimal mainArea = BigDecimal.ZERO;
				BigDecimal subArea = BigDecimal.ZERO;
				BigDecimal parkArea = BigDecimal.ZERO;
				BigDecimal publicArea = BigDecimal.ZERO;

				if (tClBuilding != null) {
					List<ClBuildingParking> lClBuildingParking = new ArrayList<ClBuildingParking>();
					List<ClBuildingPublic> lClBuildingPublic = new ArrayList<ClBuildingPublic>();

					Slice<ClBuildingParking> sClBuildingParking = clBuildingParkingService.clNoEq(
							tClBuilding.getClCode1(), tClBuilding.getClCode2(), tClBuilding.getClNo(), this.index,
							this.limit, titaVo);
					Slice<ClBuildingPublic> sClBuildingPublic = clBuildingPublicService.clNoEq(tClBuilding.getClCode1(),
							tClBuilding.getClCode2(), tClBuilding.getClNo(), this.index, this.limit, titaVo);

					lClBuildingParking = sClBuildingParking == null ? null : sClBuildingParking.getContent();
					lClBuildingPublic = sClBuildingPublic == null ? null : sClBuildingPublic.getContent();

					if (lClBuildingParking != null && lClBuildingParking.size() != 0) {
						for (ClBuildingParking tClBuildingParking : lClBuildingParking) {
							parkArea = parkArea.add(tClBuildingParking.getArea());
						}
					}
					if (lClBuildingPublic != null && lClBuildingPublic.size() != 0) {
						for (ClBuildingPublic tClBuildingPublic : lClBuildingPublic) {
							publicArea = publicArea.add(tClBuildingPublic.getArea());
						}
					}

					mainArea = tClBuilding.getFloorArea();
					subArea = tClBuilding.getBdSubArea();
					occursList.putParam("PostalCode", FormatUtil.padX("" + findZipCode(tCustMain), 5));
					occursList.putParam("Address", FormatUtil.padX(replaceComma(tClBuilding.getBdLocation()), 58));
					occursList.putParam("BuildingSquare",
							FormatUtil.pad9(chgDot(mainArea.add(subArea).add(parkArea).add(publicArea)), 9));
					occursList.putParam("BuildingCode", FormatUtil.pad9("" + tClBuilding.getBdMtrlCode(), 2));
					occursList.putParam("BuildingYears",
							FormatUtil.pad9(("" + tClBuilding.getBdDate()), 7).substring(0, 3));
					occursList.putParam("BuildingFloors", FormatUtil.pad9("" + tClBuilding.getFloor(), 2));
					occursList.putParam("RoofCode", FormatUtil.pad9("" + tClBuilding.getRoofStructureCode(), 2));
					occursList.putParam("BusinessUnit", FormatUtil.pad9("" + tClBuilding.getBdMainUseCode(), 4));
				} else {
					occursList.putParam("PostalCode", FormatUtil.padX("", 5));
					occursList.putParam("Address", FormatUtil.padX("", 58));
					occursList.putParam("BuildingSquare", FormatUtil.padX("", 9));
					occursList.putParam("BuildingCode", FormatUtil.padX("", 2));
					occursList.putParam("BuildingYears", FormatUtil.padX((""), 3));
					occursList.putParam("BuildingFloors", FormatUtil.padX("", 2));
					occursList.putParam("RoofCode", FormatUtil.padX("", 2));
					occursList.putParam("BusinessUnit", FormatUtil.padX("", 4));
				}
				occursList.putParam("ClCode1", FormatUtil.padX("" + tInsuRenew.getInsuRenewId().getClCode1(), 1));
				occursList.putParam("ClCode2", FormatUtil.pad9("" + tInsuRenew.getInsuRenewId().getClCode2(), 2));
				occursList.putParam("ClNo", FormatUtil.pad9("" + tInsuRenew.getInsuRenewId().getClNo(), 7));

//						19	Seq					序號			X	2	???
				occursList.putParam("Seq", FormatUtil.pad9("", 2)); // ???

				this.info("PrevInsuNo : " + tInsuRenew.getPrevInsuNo());
				this.info("Id's PrevInsuNo : " + tInsuRenew.getInsuRenewId().getPrevInsuNo());
				occursList.putParam("InsuNo", FormatUtil.padX("" + tInsuRenew.getInsuRenewId().getPrevInsuNo(), 16));

				int b4StartDate = 0;
				int b4EndDate = 0;

//				原保單之年月
//				1.初保檔 = 原保險單號碼=原始保險單號碼
				if (tInsuRenew.getInsuRenewId().getPrevInsuNo().equals(tInsuRenew.getOrigInsuNo())) {
					InsuOrignal tInsuOrignal = new InsuOrignal();
					InsuOrignalId tInsuOrignalId = new InsuOrignalId();

					tInsuOrignalId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
					tInsuOrignalId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
					tInsuOrignalId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
					tInsuOrignalId.setOrigInsuNo(tInsuRenew.getInsuRenewId().getPrevInsuNo());
					tInsuOrignalId.setEndoInsuNo(" ");

					tInsuOrignal = insuOrignalService.findById(tInsuOrignalId, titaVo);

					b4StartDate = tInsuOrignal.getInsuStartDate();
					b4EndDate = tInsuOrignal.getInsuEndDate();
				}
//				2.續保檔 = 原保險單號碼(t)=目前保險單號碼(t2)
				else {
					InsuRenew t2InsuRenew = insuRenewService.findL4600AFirst(tInsuRenew.getInsuRenewId().getClCode1(),
							tInsuRenew.getInsuRenewId().getClCode2(), tInsuRenew.getInsuRenewId().getClNo(),
							tInsuRenew.getInsuRenewId().getPrevInsuNo(), titaVo);
					b4StartDate = t2InsuRenew.getInsuStartDate();
					b4EndDate = t2InsuRenew.getInsuEndDate();
				}

				occursList.putParam("InsuStartDate", dateSlashFormat(b4StartDate));
				occursList.putParam("InsuEndDate", dateSlashFormat(b4EndDate));
				occursList.putParam("FireInsuAmt", FormatUtil.pad9("" + fireAmt.get(insuNo), 11));
				occursList.putParam("FireInsuFee", FormatUtil.pad9("" + fireFee.get(insuNo), 7));
				occursList.putParam("EqInsuAmt", FormatUtil.pad9("" + eqthAmt.get(insuNo), 7));
				occursList.putParam("EqInsuFee", FormatUtil.pad9("" + eqthFee.get(insuNo), 6));
				occursList.putParam("CustNo", FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7));
				occursList.putParam("FacmNo", FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3));
				occursList.putParam("Space", FormatUtil.padX("", 4));
				occursList.putParam("SendDate", FormatUtil.padLeft("" + this.getTxBuffer().getTxCom().getTbsdyf(), 14));

				InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();
				InsuRenewMediaTempId tInsuRenewMediaTempId = new InsuRenewMediaTempId();
				tInsuRenewMediaTempId.setFireInsuMonth("" + iInsuEndMonth);
				tInsuRenewMediaTempId.setInsuNo(tInsuRenew.getInsuRenewId().getPrevInsuNo());

				tInsuRenewMediaTemp = insuRenewMediaTempService.findById(tInsuRenewMediaTempId);
//						SklSalesName 2.CdEmp.FullName
//						SklUnitCode  2.CdEmp.CenterCodeAcc
//						SklUnitName  2.CdEmp.CenterShortName
//						SklSalesCode 1.facm.Introducer ->CdEmp.EmployeeNo
//						RenewTrlCode 2.CdEmp.CenterCode1
//						RenewUnit    2.CdEmp.CenterCodeShort

//				L4602之後來回改抓取temp檔
				if (tInsuRenewMediaTemp != null) {
					throw new LogicException("E0015", "該批已送回詢價 ");
				}
//				BS460第一次出去 放空白		
				else {
					occursList.putParam("NewInusNo", FormatUtil.padX("", 16));
					occursList.putParam("NewInsuStartDate", FormatUtil.padX("", 10));
					occursList.putParam("NewInsuEndDate", FormatUtil.padX("", 10));
					occursList.putParam("NewFireInsuAmt", FormatUtil.padX("", 11));
					occursList.putParam("NewFireInsuFee", FormatUtil.padX("", 7));
					occursList.putParam("NewEqInsuAmt", FormatUtil.padX("", 7));
					occursList.putParam("NewEqInsuFee", FormatUtil.padX("", 6));
					occursList.putParam("NewTotalFee", FormatUtil.padX("", 7));

					CdEmp tCdEmp = new CdEmp();
					if (tFacMain != null) {
						tCdEmp = cdEmpService.findById(tFacMain.getIntroducer());
						occursList.putParam("SklSalesCode", FormatUtil.padX("" + tFacMain.getIntroducer(), 6));
					} else {
						occursList.putParam("SklSalesCode", FormatUtil.padX("", 6));
					}
					if (tCdEmp != null) {
						occursList.putParam("SklSalesName", FormatUtil.padX("" + tCdEmp.getFullname(), 10));
						occursList.putParam("SklUnitCode", FormatUtil.padX("" + tCdEmp.getCenterCodeAcc(), 6));
						occursList.putParam("SklUnitName", FormatUtil.padX("" + tCdEmp.getCenterShortName(), 10));
						occursList.putParam("RenewTrlCode", FormatUtil.padX("" + tCdEmp.getCenterCode1(), 8));
						occursList.putParam("RenewUnit", FormatUtil.padX("" + tCdEmp.getCenterCode1Short(), 10));
					} else {
						occursList.putParam("SklSalesName", FormatUtil.padX("", 10));
						occursList.putParam("SklUnitCode", FormatUtil.padX("", 6));
						occursList.putParam("SklUnitName", FormatUtil.padX("", 10));
						occursList.putParam("RenewTrlCode", FormatUtil.padX("", 8));
						occursList.putParam("RenewUnit", FormatUtil.padX("", 10));
					}
					occursList.putParam("Remark1", FormatUtil.padX("", 16));
					occursList.putParam("MailingAddress",
							FormatUtil.padX("" + custNoticeCom.getCurrAddress(tCustMain), 60));
					occursList.putParam("Remark2", FormatUtil.padX("", 39));
					occursList.putParam("Space46", FormatUtil.padX("", 46));

//					CdEmp tCdEmpRN = new CdEmp();
//					tCdEmpRN = cdEmpService.findById(this.getTxBuffer().getTxCom().getRelNo());
//					if (tCdEmpRN != null) {
//						occursList.putParam("RenewTrlCode", FormatUtil.padX("" + this.getTxBuffer().getTxCom().getRelNo(), 8));
//						occursList.putParam("RenewUnit", FormatUtil.padX("" + tCdEmpRN.getCenterCode(), 7));
//					} else {
//						occursList.putParam("RenewTrlCode", FormatUtil.padX("", 8));
//						occursList.putParam("RenewUnit", FormatUtil.padX("", 7));
//					}
				}
				tmp.add(occursList);
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
	}

	private String chgDot(BigDecimal bd) throws LogicException {
		String result = "" + bd;
//		去除小數點
//		1.若有小數點將小數點去除
//		2.若無小數點補兩位零
		if (result.indexOf(".") >= 0) {
			result = result.replace(".", "");
		} else {
			result += "00";
		}

		this.info("bd = " + bd);
		this.info("result = " + result);

		return result;
	}

	private void check(TitaVo titaVo) throws LogicException {
		List<InsuRenewMediaTemp> lInsuRenewMediaTemp = new ArrayList<InsuRenewMediaTemp>();

		Slice<InsuRenewMediaTemp> sInsuRenewMediaTemp = null;

		String sInsuEndMonth = iInsuEndMonth + "";

		sInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg(sInsuEndMonth, sInsuEndMonth, this.index,
				this.limit, titaVo);

		lInsuRenewMediaTemp = sInsuRenewMediaTemp == null ? null : sInsuRenewMediaTemp.getContent();

//		SklSalesName 2.CdEmp.FullName
//		SklUnitCode  2.CdEmp.CenterCodeAcc
//		SklUnitName  2.CdEmp.CenterShortName
//		SklSalesCode 1.facm.Introducer ->CdEmp.EmployeeNo
//		RenewTrlCode 2.CdEmp.CenterCode1
//		RenewUnit    2.CdEmp.CenterCodeShort

		// L4602之後來回改抓取temp檔
		if (lInsuRenewMediaTemp != null && lInsuRenewMediaTemp.size() != 0) {
			throw new LogicException("E0015", "該批已送回詢價 ");
		}
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int status = 0;

		public tmpFacm(int custNo, int status) {
			this.setCustNo(custNo);
			this.setStatus(status);
		}

		private int getCustNo() {
			return custNo;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + status;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (status != other.status)
				return false;
			return true;
		}

		private int getStatus() {
			return status;
		}

		private void setStatus(int status) {
			this.status = status;
		}

		private BS460 getEnclosingInstance() {
			return BS460.this;
		}
	}
}