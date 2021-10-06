package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdAreaService;
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
import com.st1.itx.util.common.FacStatusCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4600Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4600Batch extends TradeBuffer {

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
	public FacStatusCom facStatusCom;

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
	private int insuEndDateFrom = 0;
	private int insuEndDateTo = 0;
	private ArrayList<OccursList> tmpList = new ArrayList<>();
	private int custNo = 0;
	private int facmNo = 0;
	private int repayCode = 0;
	private Boolean checkFlag = true;
	private String sendMsg = " ";
	private List<InsuRenew> lInsuRenewTemp = new ArrayList<InsuRenew>();
	private List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4600Batch ");
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
		insuEndDateFrom = parse.stringToInteger(iInsuEndMonth + "01");
		insuEndDateTo = parse.stringToInteger(iInsuEndMonth + "31");

//		檢核該月份是否做過詢價
		check(titaVo);

//		刪除舊資料
		deleInsuRenew(iInsuEndMonth, titaVo);

//      
//		step1 將到期年月，加入續保List
//		新保 ->續保
		orignalToList(titaVo);
//		續保 ->續保
		renewToList(titaVo);

//		續保 -> 續保(新年度)
//		step2 將續保檔內到期年月與tita相同者產出下一期
		toRenewList(titaVo);
		if (lInsuRenew.size() == 0) {
			throw new LogicException("E0001", "查無資料");
		}

//		續保(新年度) ->FILE
//		step3 將下一期產出file
//		因可能會有多筆保單合為一筆的情況，所以需先insert完再逐筆合計寫入File
//		將到期資料寫入File
		toFile(titaVo);

		// 把明細資料容器裝到檔案資料容器內
		insuRenewFileVo.setOccursList(tmpList);
		// 轉換資料格式
		ArrayList<String> file = insuRenewFileVo.toFile();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-火險到期檔",
				"LNM01P.txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();
		
		// INSERT 續保檔
		try {
			insuRenewService.insertAll(lInsuRenew);
		} catch (DBException e) {
			throw new LogicException("E0007", "InsuRenew : " + e.getErrorMsg());
		}

		this.info("sno : " + sno);

		makeFile.toFile(sno);
	}

	private void orignalToList(TitaVo titaVo) throws LogicException {
		Slice<InsuOrignal> slInsuOrignal = insuOrignalService.insuEndDateRange(insuEndDateFrom, insuEndDateTo, 0,
				Integer.MAX_VALUE, titaVo);
		if (slInsuOrignal != null) {
			for (InsuOrignal tInsuOrignal : slInsuOrignal.getContent()) {
				InsuRenew tempInsuRenew = new InsuRenew();
				tempInsuRenew.setClCode1(tInsuOrignal.getClCode1());
				tempInsuRenew.setClCode2(tInsuOrignal.getClCode2());
				tempInsuRenew.setClNo(tInsuOrignal.getClNo());
				tempInsuRenew.setPrevInsuNo(tInsuOrignal.getOrigInsuNo());
				tempInsuRenew.setOrigInsuNo(tInsuOrignal.getOrigInsuNo());
				tempInsuRenew.setNowInsuNo(tInsuOrignal.getOrigInsuNo());
				tempInsuRenew.setInsuCompany(tInsuOrignal.getInsuCompany());
				tempInsuRenew.setInsuTypeCode(tInsuOrignal.getInsuTypeCode());
				tempInsuRenew.setFireInsuCovrg(tInsuOrignal.getFireInsuCovrg());
				tempInsuRenew.setFireInsuPrem(tInsuOrignal.getFireInsuPrem());
				tempInsuRenew.setEthqInsuCovrg(tInsuOrignal.getEthqInsuCovrg());
				tempInsuRenew.setEthqInsuPrem(tInsuOrignal.getEthqInsuPrem());
				tempInsuRenew.setInsuStartDate(tInsuOrignal.getInsuStartDate());
				tempInsuRenew.setInsuEndDate(tInsuOrignal.getInsuEndDate());
				tempInsuRenew.setTotInsuPrem(tInsuOrignal.getFireInsuPrem().add(tInsuOrignal.getEthqInsuPrem()));
				if ("".equals(tInsuOrignal.getEndoInsuNo().trim())) {
					lInsuRenewTemp.add(tempInsuRenew);
				} else {
					endoInsuNoToList(tempInsuRenew, titaVo);
				}
			}
		}
	}

	private void endoInsuNoToList(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		for (InsuRenew t : lInsuRenewTemp) {
			if (tInsuRenew.getClCode1() == t.getClCode1() && tInsuRenew.getClCode2() == t.getClCode2()
					&& tInsuRenew.getClNo() == t.getClNo() && tInsuRenew.getClCode2() == t.getClCode2()
					&& tInsuRenew.getPrevInsuNo().equals(t.getPrevInsuNo())
					&& tInsuRenew.getInsuEndDate() == t.getInsuEndDate()) {
				t.setFireInsuCovrg(t.getFireInsuCovrg().add(tInsuRenew.getFireInsuCovrg()));
				t.setFireInsuPrem(t.getFireInsuPrem().add(tInsuRenew.getFireInsuPrem()));
				t.setEthqInsuCovrg(t.getEthqInsuCovrg().add(tInsuRenew.getEthqInsuCovrg()));
				t.setEthqInsuPrem(t.getEthqInsuPrem().add(tInsuRenew.getEthqInsuPrem()));
				t.setTotInsuPrem(t.getTotInsuPrem().add(tInsuRenew.getTotInsuPrem()));
			}
		}
	}

	private void renewToList(TitaVo titaVo) throws LogicException {
		Slice<InsuRenew> slInsuRenew = insuRenewService.insuEndDateRange(insuEndDateFrom, insuEndDateTo, 0,
				Integer.MAX_VALUE);
		if (slInsuRenew != null) {
			for (InsuRenew tInsuRenew : slInsuRenew.getContent()) {
				if ("".equals(tInsuRenew.getEndoInsuNo().trim())) {
					lInsuRenewTemp.add(tInsuRenew);
				} else {
					endoInsuNoToList(tInsuRenew, titaVo);
				}
			}
		}
	}
	

	private void toRenewList(TitaVo titaVo) throws LogicException {

		for (InsuRenew t : lInsuRenewTemp) {

//				無新保單號碼
			if ("".equals(t.getNowInsuNo())) {
				this.info("無新保單號碼 ... " + t.getNowInsuNo());
				continue;
			}

//				排除自保件
			InsuRenew nInsuRenew = insuRenewService.findById(
					new InsuRenewId(t.getClCode1(), t.getClCode2(), t.getClNo(), t.getNowInsuNo(), " "), titaVo);

			if (nInsuRenew != null) {
				this.info("排除自保件 ... " + t.getNowInsuNo());
				continue;
			}

			// 找額度
			findFacmNo(t, titaVo);
			if (this.facmNo == 0) {
				continue;
			}

			InsuRenew tInsuRenew = new InsuRenew();

			tInsuRenew.setClCode1(t.getClCode1());
			tInsuRenew.setClCode2(t.getClCode2());
			tInsuRenew.setClNo(t.getClNo());
			tInsuRenew.setPrevInsuNo(t.getNowInsuNo());
			tInsuRenew.setEndoInsuNo(" ");
			tInsuRenew.setInsuRenewId(
					new InsuRenewId(t.getClCode1(), t.getClCode2(), t.getClNo(), t.getNowInsuNo(), " "));
			tInsuRenew.setNowInsuNo("");
			tInsuRenew.setOrigInsuNo(t.getOrigInsuNo());
			tInsuRenew.setInsuYearMonth(iInsuEndMonth);
			tInsuRenew.setCustNo(this.custNo);
			tInsuRenew.setFacmNo(this.facmNo);
			tInsuRenew.setRenewCode(2); // 續保
			tInsuRenew.setInsuCompany(t.getInsuCompany());
			tInsuRenew.setInsuTypeCode(t.getInsuTypeCode());
			tInsuRenew.setRepayCode(this.repayCode);
			tInsuRenew.setFireInsuCovrg(BigDecimal.ZERO);
			tInsuRenew.setFireInsuPrem(BigDecimal.ZERO);
			tInsuRenew.setEthqInsuCovrg(BigDecimal.ZERO);
			tInsuRenew.setEthqInsuPrem(BigDecimal.ZERO);
			tInsuRenew.setTotInsuPrem(BigDecimal.ZERO);
			tInsuRenew.setInsuStartDate(t.getInsuEndDate());
			dateUtil.init();
			dateUtil.setDate_1(t.getInsuEndDate());
			dateUtil.setYears(1);
			tInsuRenew.setInsuEndDate(dateUtil.getCalenderDay());
			tInsuRenew.setAcDate(0);
			tInsuRenew.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
			tInsuRenew.setTitaTxtNo(this.getTxBuffer().getTxCom().getRelTno() + "");
			tInsuRenew.setNotiTempFg(""); // 待通知
			tInsuRenew.setStatusCode(0);
			tInsuRenew.setOvduDate(0);
			tInsuRenew.setOvduNo(BigDecimal.ZERO);
			lInsuRenew.add(tInsuRenew);
		}
	}

	private void findFacmNo(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("findFacmNo " + tInsuRenew.toString());

		this.custNo = 0;
		this.facmNo = 0;
		this.repayCode = 0;

//	 1.排除結案戶、呆帳戶、未撥款戶。
//   2.戶況順序 0.正常(4.逾期戶) > 2.催收> 7.部呆
//	 3.戶況>原續保戶號額度>核准編號較小

		int priorty = 9;
		int approveNo = 9999999;
		// 原額度為正常戶優先
		Slice<LoanBorMain> slLoanBorMain;
		if (tInsuRenew.getFacmNo() > 0) {
			slLoanBorMain = loanBorMainService.bormCustNoEq(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo(),
					tInsuRenew.getFacmNo(), 0, 900, 0, Integer.MAX_VALUE);
			if (slLoanBorMain != null) {
				int status = facStatusCom.settingStatus(slLoanBorMain.getContent(), tInsuRenew.getInsuEndDate());
				if (status == 0 || status == 4) {
					priorty = 0;
					custNo = tInsuRenew.getCustNo();
					facmNo = tInsuRenew.getFacmNo();
				}
			}
		}

		if (priorty > 0) {
			Slice<ClFac> slClFac = clFacService.clNoEq(tInsuRenew.getClCode1(), tInsuRenew.getClCode2(),
					tInsuRenew.getClNo(), 0, Integer.MAX_VALUE, titaVo);
			if (slClFac != null) {
				for (ClFac tClFac : slClFac.getContent()) {
					slLoanBorMain = loanBorMainService.bormCustNoEq(tClFac.getCustNo(), tClFac.getFacmNo(),
							tClFac.getFacmNo(), 0, 900, 0, Integer.MAX_VALUE);
					int priortyNow = 99;
					if (slLoanBorMain != null) {
						int status = facStatusCom.settingStatus(slLoanBorMain.getContent(),
								tInsuRenew.getInsuEndDate());
						if (status == 0 || status == 4) {
							priortyNow = 1;
						} else if (status == 2) {
							priortyNow = 2;
						} else if (status == 7) {
							priortyNow = 3;
						}
						if (priortyNow < priorty || (priortyNow == priorty && tClFac.getApproveNo() < approveNo)) {
							this.custNo = tClFac.getCustNo();
							this.facmNo = tClFac.getFacmNo();
							priorty = priortyNow;
						}
					}
				}
			}
		}

		if (priorty < 9) {
			FacMain tFacMain = facMainService.findById(new FacMainId(custNo, facmNo), titaVo);
			if (tFacMain != null) {
				this.repayCode = tFacMain.getRepayCode();
			}
		}

		this.info("findFacmNo CustNo=" + this.custNo + ", FacmNo=" + this.facmNo + ", priorty=" + priorty
				+ ", approveNo=" + approveNo);

	}

	private void deleInsuRenew(int insuMonth, TitaVo titaVo) throws LogicException {
		Slice<InsuRenew> sInsuRenew = insuRenewService.selectC(insuMonth, 0, Integer.MAX_VALUE, titaVo);

		if (sInsuRenew != null) {
			for (InsuRenew tInsuRenew : sInsuRenew.getContent()) {

				if (tInsuRenew.getAcDate() > 0) {
					this.info("continue... ，ACDATE > 0");
					continue;
				}

//				排除自保件
				if (tInsuRenew.getRenewCode() == 1) {
					this.info("排除自保件 ... ");
					continue;
				}

				insuRenewService.holdById(tInsuRenew, titaVo);

				try {
					insuRenewService.delete(tInsuRenew, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "InsuRenew delete error : " + e.getErrorMsg());
				}
			}
		}
	}

	private String findZipCode(CustMain tCustMain, TitaVo titaVo) throws LogicException {
		String zip = "";
		if (tCustMain != null) {
			CdArea tCdArea = new CdArea();
			CdAreaId tCdAreaId = new CdAreaId();
			tCdAreaId.setCityCode(tCustMain.getCurrCityCode());
			tCdAreaId.setAreaCode(tCustMain.getCurrAreaCode());
			tCdArea = cdAreaService.findById(tCdAreaId, titaVo);
			if (tCdArea != null) {
				zip = tCdArea.getZip3();
			}
		}
		return zip;
	}

	private String replaceComma(String addresss) {
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
	private void toFile(TitaVo titaVo) throws LogicException {

		for (InsuRenew t : lInsuRenew) {
			this.info("InsuRenew = " + t.toString());
			OccursList occursList = new OccursList();
			occursList = getOccurs("L4600", occursList, t, titaVo);
			tmpList.add(occursList);
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

	public OccursList getOccurs(String iTxCode, OccursList occursList, InsuRenew t, TitaVo titaVo)
			throws LogicException {
		occursList.putParam("FireInsuMonth", FormatUtil.padX("" + (t.getInsuYearMonth()), 6));
		occursList.putParam("ReturnCode", FormatUtil.pad9("99", 2));
		occursList.putParam("InsuCampCode", FormatUtil.pad9("01", 2));
		CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
		FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
		ClBuilding tClBuilding = null;
		ClBuildingOwner tClBuildingOwner = null;
		if (t.getClCode1() == 1) {
			tClBuilding = clBuildingService.findById(new ClBuildingId(t.getClCode1(), t.getClCode2(), t.getClNo()),
					titaVo);
			tClBuildingOwner = clBuildingOwnerService.clNoFirst(t.getClCode1(), t.getClCode2(), t.getClNo(), titaVo);
		}
		if (tClBuildingOwner != null) {
			CustMain custMain = custMainService.findById(tClBuildingOwner.getOwnerCustUKey(), titaVo);
			if (custMain != null) {
				occursList.putParam("InsuCustId", FormatUtil.padX(custMain.getCustId(), 10));
				occursList.putParam("InsuCustName", FormatUtil.padX(custMain.getCustName(), 12));
			} else {
				occursList.putParam("InsuCustId", FormatUtil.padX("", 10));
				occursList.putParam("InsuCustName", FormatUtil.padX("", 12));
			}
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
			Slice<ClBuildingParking> sClBuildingParking = clBuildingParkingService.clNoEq(tClBuilding.getClCode1(),
					tClBuilding.getClCode2(), tClBuilding.getClNo(), 0, Integer.MAX_VALUE, titaVo);
			Slice<ClBuildingPublic> sClBuildingPublic = clBuildingPublicService.clNoEq(tClBuilding.getClCode1(),
					tClBuilding.getClCode2(), tClBuilding.getClNo(), 0, Integer.MAX_VALUE, titaVo);
			if (sClBuildingParking != null) {
				for (ClBuildingParking tClBuildingParking : sClBuildingParking.getContent()) {
					parkArea = parkArea.add(tClBuildingParking.getArea());
				}
			}
			if (sClBuildingPublic != null) {
				for (ClBuildingPublic tClBuildingPublic : sClBuildingPublic.getContent()) {
					publicArea = publicArea.add(tClBuildingPublic.getArea());
				}
			}

			mainArea = tClBuilding.getFloorArea();
			subArea = tClBuilding.getBdSubArea();
			occursList.putParam("PostalCode", FormatUtil.padX("" + findZipCode(tCustMain, titaVo), 5));
			occursList.putParam("Address", FormatUtil.padX(replaceComma(tClBuilding.getBdLocation()), 58));
			occursList.putParam("BuildingSquare",
					FormatUtil.pad9(chgDot(mainArea.add(subArea).add(parkArea).add(publicArea)), 9));
			occursList.putParam("BuildingCode", FormatUtil.pad9("" + tClBuilding.getBdMtrlCode(), 2));
			occursList.putParam("BuildingYears", FormatUtil.pad9(("" + tClBuilding.getBdDate()), 7).substring(0, 3));
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
		occursList.putParam("ClCode1", FormatUtil.padX("" + t.getClCode1(), 1));
		occursList.putParam("ClCode2", FormatUtil.pad9("" + t.getClCode2(), 2));
		occursList.putParam("ClNo", FormatUtil.pad9("" + t.getClNo(), 7));

//				19	Seq					序號			X	2	???
		occursList.putParam("Seq", FormatUtil.pad9("", 2)); // ???
		occursList.putParam("InsuNo", FormatUtil.padX("" + t.getPrevInsuNo(), 16));

		int b4StartDate = 0;
		int b4EndDate = 0;
		BigDecimal fireInsuCovrg = BigDecimal.ZERO;
		BigDecimal fireInsuPrem = BigDecimal.ZERO;
		BigDecimal ethqInsuCovrg = BigDecimal.ZERO;
		BigDecimal eqthqInsuPrem = BigDecimal.ZERO;

//		原保單之年月
//		1.初保檔 = 原保險單號碼=原始保險單號碼
		Slice<InsuOrignal> slInsuOrignal = insuOrignalService.findOrigInsuNoEq(t.getClCode1(), t.getClCode2(),
				t.getClNo(), t.getPrevInsuNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slInsuOrignal != null) {
			for (InsuOrignal t2 : slInsuOrignal.getContent()) {
				if ("".equals(t2.getEndoInsuNo().trim())) {
					b4StartDate = t2.getInsuStartDate();
					b4EndDate = t2.getInsuEndDate();
				}
				fireInsuCovrg = fireInsuCovrg.add(t2.getFireInsuCovrg());
				fireInsuPrem = fireInsuPrem.add(t2.getFireInsuPrem());
				ethqInsuCovrg = ethqInsuCovrg.add(t2.getEthqInsuCovrg());
				eqthqInsuPrem = eqthqInsuPrem.add(t2.getEthqInsuPrem());
			}
		}

//		2.續保檔 = 原保險單號碼(t)=目前保險單號碼(t2)
		Slice<InsuRenew> slInsuRenew = insuRenewService.findNowInsuNoEq(t.getClCode1(), t.getClCode2(), t.getClNo(),
				t.getPrevInsuNo(), 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew t2 : slInsuRenew.getContent()) {
				if ("".equals(t2.getEndoInsuNo().trim())) {
					b4StartDate = t2.getInsuStartDate();
					b4EndDate = t2.getInsuEndDate();
				}
				fireInsuCovrg = fireInsuCovrg.add(t2.getFireInsuCovrg());
				fireInsuPrem = fireInsuPrem.add(t2.getFireInsuPrem());
				ethqInsuCovrg = ethqInsuCovrg.add(t2.getEthqInsuCovrg());
				eqthqInsuPrem = eqthqInsuPrem.add(t2.getEthqInsuPrem());
			}
		}
		occursList.putParam("InsuStartDate", FormatUtil.pad9("" + (b4StartDate + 19110000), 8));
		occursList.putParam("InsuEndDate", FormatUtil.pad9("" + (b4EndDate + 19110000), 8));
		if ("L4600".equals(iTxCode)) {
			occursList.putParam("FireInsuAmt", FormatUtil.pad9("" + t.getFireInsuCovrg(), 11));
			occursList.putParam("FireInsuFee", FormatUtil.pad9("" + t.getFireInsuPrem(), 7));
			occursList.putParam("EqInsuAmt", FormatUtil.pad9("" + t.getEthqInsuCovrg(), 7));
			occursList.putParam("EqInsuFee", FormatUtil.pad9("" + t.getEthqInsuPrem(), 6));
			occursList.putParam("NewInusNo", FormatUtil.padX("", 16));
			occursList.putParam("NewInsuStartDate", FormatUtil.padX("", 8));
			occursList.putParam("NewInsuEndDate", FormatUtil.padX("", 8));
			occursList.putParam("NewFireInsuAmt", FormatUtil.padX("", 11));
			occursList.putParam("NewFireInsuFee", FormatUtil.padX("", 7));
			occursList.putParam("NewEqInsuAmt", FormatUtil.padX("", 7));
			occursList.putParam("NewEqInsuFee", FormatUtil.padX("", 6));
			occursList.putParam("NewTotalFee", FormatUtil.padX("", 7));
		} else {
			occursList.putParam("FireInsuAmt", FormatUtil.pad9("" + fireInsuCovrg, 11));
			occursList.putParam("FireInsuFee", FormatUtil.pad9("" + fireInsuPrem, 7));
			occursList.putParam("EqInsuAmt", FormatUtil.pad9("" + ethqInsuCovrg, 7));
			occursList.putParam("EqInsuFee", FormatUtil.pad9("" + eqthqInsuPrem, 6));
			occursList.putParam("NewInusNo", FormatUtil.padX(t.getNowInsuNo(), 16));
			occursList.putParam("NewInsuStartDate", FormatUtil.pad9("" + (t.getInsuStartDate() + 19110000), 8));
			occursList.putParam("NewInsuEndDate", FormatUtil.pad9("" + (t.getInsuEndDate() + 19110000), 8));
			occursList.putParam("NewFireInsuAmt", FormatUtil.pad9("" + t.getFireInsuCovrg(), 11));
			occursList.putParam("NewFireInsuFee", FormatUtil.pad9("" + t.getFireInsuPrem(), 7));
			occursList.putParam("NewEqInsuAmt", FormatUtil.pad9("" + t.getEthqInsuCovrg(), 7));
			occursList.putParam("NewEqInsuFee", FormatUtil.pad9("" + t.getEthqInsuPrem(), 6));
			occursList.putParam("NewTotalFee", FormatUtil.pad9("" + t.getTotInsuPrem(), 7));
		}

		occursList.putParam("CustNo", FormatUtil.pad9("" + t.getCustNo(), 7));
		occursList.putParam("FacmNo", FormatUtil.pad9("" + t.getFacmNo(), 3));
		occursList.putParam("Space", FormatUtil.padX("", 4));
		occursList.putParam("SendDate",
				FormatUtil.padLeft("" + (parse.stringToInteger(titaVo.getCalDy()) + 19110000), 14));
//				SklSalesName 2.CdEmp.FullName
//				SklUnitCode  2.CdEmp.CenterCodeAcc
//				SklUnitName  2.CdEmp.CenterShortName
//				SklSalesCode 1.facm.Introducer ->CdEmp.EmployeeNo
//				RenewTrlCode 2.CdEmp.CenterCode1
//				RenewUnit    2.CdEmp.CenterCodeShort

//		L4602之後來回改抓取temp檔
//		L4600Batch第一次出去 放空白		

		CdEmp tCdEmp = new CdEmp();
		if (tFacMain != null) {
			tCdEmp = cdEmpService.findById(tFacMain.getIntroducer());
			if (tCdEmp != null) {
			}
		}
		if (tCdEmp != null) {
			occursList.putParam("SklSalesCode", FormatUtil.padX("" + tFacMain.getIntroducer(), 6));
			occursList.putParam("SklSalesName", FormatUtil.padX("" + tCdEmp.getFullname(), 10));
			occursList.putParam("SklUnitCode", FormatUtil.padX("" + tCdEmp.getCenterCodeAcc(), 6));
			occursList.putParam("SklUnitName", FormatUtil.padX("" + tCdEmp.getCenterShortName(), 10));
			occursList.putParam("RenewTrlCode", FormatUtil.padX("" + tCdEmp.getCenterCode1(), 8));
			occursList.putParam("RenewUnit", FormatUtil.padX("" + tCdEmp.getCenterCode1Short(), 10));
		} else {
			occursList.putParam("SklSalesCode", FormatUtil.padX("", 6));
			occursList.putParam("SklSalesName", FormatUtil.padX("", 10));
			occursList.putParam("SklUnitCode", FormatUtil.padX("", 6));
			occursList.putParam("SklUnitName", FormatUtil.padX("", 10));
			occursList.putParam("RenewTrlCode", FormatUtil.padX("", 8));
			occursList.putParam("RenewUnit", FormatUtil.padX("", 10));
		}
		occursList.putParam("Remark1", FormatUtil.padX("", 16));
		if (tCustMain == null) {
			occursList.putParam("MailingAddress", FormatUtil.padX("", 60));
		} else {
			occursList.putParam("MailingAddress",
					FormatUtil.padX("" + custNoticeCom.getCurrAddress(tCustMain, titaVo), 60));
		}
		occursList.putParam("Remark2", FormatUtil.padX("", 39));
		occursList.putParam("Space46", FormatUtil.padX("", 46));
		return occursList;
	}

	private void check(TitaVo titaVo) throws LogicException {
		String sInsuEndMonth = iInsuEndMonth + "";

		Slice<InsuRenewMediaTemp> sInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg(sInsuEndMonth,
				sInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		// 已執行L4602
		if (sInsuRenewMediaTemp != null) {
			throw new LogicException("E0015", "該批已送回詢價，不可再產檔 ");
		}
	}

}