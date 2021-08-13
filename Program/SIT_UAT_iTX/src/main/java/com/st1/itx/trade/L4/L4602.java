package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingParking;
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuRenew;
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
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4602")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4602 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4602.class);

	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CustNoticeCom custNoticeCom;

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
	public InsuOrignalService insuOrignalService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	private HashMap<tmpFacm, Integer> custFacm = new HashMap<>();
	private ArrayList<OccursList> tmp = new ArrayList<>();

	private int iInsuEndMonth = 0;
	private int InsuStartDate = 0;
	private int InsuEndDate = 0;
	private BigDecimal FireInsuAmt = BigDecimal.ZERO;
	private BigDecimal FireInsuFee = BigDecimal.ZERO;
	private BigDecimal EqInsuAmt = BigDecimal.ZERO;
	private BigDecimal EqInsuFee = BigDecimal.ZERO;
	private BigDecimal TotalFee = BigDecimal.ZERO;
	private HashMap<String, Integer> selfFlag = new HashMap<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4602 ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		Slice<InsuRenew> sInsuRenew = null;

//		出表明細
		sInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
			for (InsuRenew tInsuRenew : lInsuRenew) {

				this.info("tInsuRenew.getClCode1 : " + tInsuRenew.getClCode1());
				this.info("tInsuRenewId.getClCode1 : " + tInsuRenew.getInsuRenewId().getClCode1());

				CustMain tcustMain = new CustMain();
				tcustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

				TempVo tempVo = new TempVo();
				tempVo = custNoticeCom.getReportCode("L4603", tInsuRenew.getCustNo(), tInsuRenew.getFacmNo());

				int noticeFlag = parse.stringToInteger(tempVo.getParam("ReportCode"));

				this.info("noticeFlag : " + noticeFlag);

				InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();
				InsuRenewMediaTempId tInsuRenewMediaTempId = new InsuRenewMediaTempId();
				tInsuRenewMediaTempId.setFireInsuMonth("" + iInsuEndMonth);
				tInsuRenewMediaTempId.setInsuNo(tInsuRenew.getPrevInsuNo());

				tInsuRenewMediaTemp = insuRenewMediaTempService.findById(tInsuRenewMediaTempId);

				if (tInsuRenewMediaTemp == null) {
					throw new LogicException("E0001", "查無火險詢價媒體暫存檔");
				} else {
					InsuStartDate = parse.stringToInteger(tInsuRenewMediaTemp.getNewInsuStartDate());
					InsuEndDate = parse.stringToInteger(tInsuRenewMediaTemp.getNewInsuEndDate());
					FireInsuAmt = parse.stringToBigDecimal(tInsuRenewMediaTemp.getNewFireInsuAmt());
					FireInsuFee = parse.stringToBigDecimal(tInsuRenewMediaTemp.getNewFireInsuFee());
					EqInsuAmt = parse.stringToBigDecimal(tInsuRenewMediaTemp.getNewEqInsuAmt());
					EqInsuFee = parse.stringToBigDecimal(tInsuRenewMediaTemp.getNewEqInsuFee());
					TotalFee = parse.stringToBigDecimal(tInsuRenewMediaTemp.getNewTotalFee());
				}

				if (InsuStartDate > 19110000) {
					InsuStartDate = InsuStartDate - 19110000;
				}
				if (InsuEndDate > 19110000) {
					InsuEndDate = InsuEndDate - 19110000;
				}

				OccursList occursListReport = new OccursList();
				occursListReport.putParam("OOClCode1", tInsuRenew.getClCode1());
				occursListReport.putParam("OOClCode2", tInsuRenew.getClCode2());
				occursListReport.putParam("OOClNo", tInsuRenew.getClNo());
				occursListReport.putParam("OOPrevInsuNo", tInsuRenew.getPrevInsuNo());
				occursListReport.putParam("OOCustNo", tInsuRenew.getCustNo());
				occursListReport.putParam("OOFacmNo", tInsuRenew.getFacmNo());
				occursListReport.putParam("OORepayCodeX", getRepayCode(tInsuRenew.getRepayCode()));
				occursListReport.putParam("OOCustName", tcustMain.getCustName());
				occursListReport.putParam("OONewInsuStartDate", InsuStartDate);
				occursListReport.putParam("OONewInsuEndDate", InsuEndDate);
				occursListReport.putParam("OOFireAmt", FireInsuAmt);
				occursListReport.putParam("OOFireFee", FireInsuFee);
				occursListReport.putParam("OOEthqAmt", EqInsuAmt);
				occursListReport.putParam("OOEthqFee", EqInsuFee);
				occursListReport.putParam("OOTotlFee", TotalFee);
				occursListReport.putParam("OONoticeWay", getNoticeWay(noticeFlag, tempVo));

				this.totaVo.addOccursList(occursListReport);
			}
		}

//		續保(新年度) ->FILE
//		step3 將下一期產出file
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

		totaVo.put("PdfSnoM", "" + sno);

		this.addList(this.totaVo);
		return this.sendList();
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

//				initial
				if (!selfFlag.containsKey(insuNo)) {
					selfFlag.put(insuNo, 0);
				}

//				排除自保的批單號碼
				if (tInsuRenew.getRenewCode() == 1) {
					selfFlag.put(insuNo, 1);
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
				if (selfFlag.get(insuNo) == 1) {
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

					String bdYear = "";

					if (tClBuilding.getBdDate() > 0) {
						bdYear = FormatUtil.pad9(("" + tClBuilding.getBdDate()).substring(0, 3), 3);
					}
					occursList.putParam("BuildingYears", bdYear);
					
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
				occursList.putParam("InsuStartDate", dateSlashFormat(tInsuRenew.getInsuStartDate()));
				occursList.putParam("InsuEndDate", dateSlashFormat(tInsuRenew.getInsuEndDate()));
				occursList.putParam("FireInsuAmt", FormatUtil.pad9("" + tInsuRenew.getFireInsuCovrg(), 11));
				occursList.putParam("FireInsuFee", FormatUtil.pad9("" + tInsuRenew.getFireInsuPrem(), 7));
				occursList.putParam("EqInsuAmt", FormatUtil.pad9("" + tInsuRenew.getEthqInsuCovrg(), 7));
				occursList.putParam("EqInsuFee", FormatUtil.pad9("" + tInsuRenew.getEthqInsuPrem(), 6));
				occursList.putParam("CustNo", FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7));
				occursList.putParam("FacmNo", FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3));
				occursList.putParam("Space", FormatUtil.padX("", 4));
				occursList.putParam("SendDate", FormatUtil.padX("" + this.getTxBuffer().getTxCom().getTbsdyf(), 14));

				InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();
				InsuRenewMediaTempId tInsuRenewMediaTempId = new InsuRenewMediaTempId();
				tInsuRenewMediaTempId.setFireInsuMonth("" + iInsuEndMonth);
				tInsuRenewMediaTempId.setInsuNo(tInsuRenew.getPrevInsuNo());

				tInsuRenewMediaTemp = insuRenewMediaTempService.findById(tInsuRenewMediaTempId);
//						SklSalesName 2.CdEmp.FullName
//						SklUnitCode  2.CdEmp.CenterCodeAcc
//						SklUnitName  2.CdEmp.CenterShortName
//						SklSalesCode 1.facm.Introducer ->CdEmp.EmployeeNo
//						RenewTrlCode 2.CdEmp.CenterCode1
//						RenewUnit    2.CdEmp.CenterCodeShort

//				L4602之後來回改抓取temp檔
				if (tInsuRenewMediaTemp != null) {
					occursList.putParam("NewInusNo", FormatUtil.padX("" + tInsuRenewMediaTemp.getNewInusNo(), 16));
					occursList.putParam("NewInsuStartDate",
							FormatUtil.padX("" + tInsuRenewMediaTemp.getNewInsuStartDate(), 10));
					occursList.putParam("NewInsuEndDate",
							FormatUtil.padX("" + tInsuRenewMediaTemp.getNewInsuEndDate(), 10));
					occursList.putParam("NewFireInsuAmt",
							FormatUtil.padX("" + tInsuRenewMediaTemp.getNewFireInsuAmt(), 11));
					occursList.putParam("NewFireInsuFee",
							FormatUtil.padX("" + tInsuRenewMediaTemp.getNewFireInsuFee(), 7));
					occursList.putParam("NewEqInsuAmt", FormatUtil.padX("" + tInsuRenewMediaTemp.getNewEqInsuAmt(), 8));
					occursList.putParam("NewEqInsuFee", FormatUtil.padX("" + tInsuRenewMediaTemp.getNewEqInsuFee(), 6));
					occursList.putParam("NewTotalFee", FormatUtil.padX("" + tInsuRenewMediaTemp.getNewTotalFee(), 7));

					occursList.putParam("SklSalesName",
							FormatUtil.padX("" + tInsuRenewMediaTemp.getSklSalesName(), 20));
					occursList.putParam("SklUnitCode", FormatUtil.padX("" + tInsuRenewMediaTemp.getSklUnitCode(), 6));
					occursList.putParam("SklUnitName", FormatUtil.padX("" + tInsuRenewMediaTemp.getSklUnitName(), 20));
					occursList.putParam("SklSalesCode", FormatUtil.padX("" + tInsuRenewMediaTemp.getSklSalesCode(), 6));
					occursList.putParam("RenewTrlCode", FormatUtil.padX("" + tInsuRenewMediaTemp.getRenewTrlCode(), 8));
					occursList.putParam("RenewUnit", FormatUtil.padX("" + tInsuRenewMediaTemp.getRenewUnit(), 7));
				} else {
					throw new LogicException("E0001", "查無火險詢價媒體暫存檔");
				}

				occursList.putParam("Remark1", FormatUtil.padX("", 16));
				occursList.putParam("MailingAddress", FormatUtil.padX("" + findAddress(tCustMain), 60));
				occursList.putParam("Remark2", FormatUtil.padX("", 39));
				occursList.putParam("Space46", FormatUtil.padX("", 46));

				tmp.add(occursList);
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
	}

	private String getNoticeWay(int noticeFlag, TempVo tempVo) throws LogicException {
		String result = "";
//		1.書信 2.簡訊 3.Email 4.不通知
		switch (noticeFlag) {
		case 1:
			result = "書信";
			break;
		case 2:
			result = "簡訊";
			break;
		case 3:
			result = "Email";
			break;
		case 9:
		case 0:
			if (!"".equals(tempVo.getParam("ReportPhoneNo"))) {
				result = "簡訊";
			} else if (!"".equals(tempVo.getParam("ReportEmailAd"))) {
				result = "Email";
			} else {
				result = "書信";
			}
			break;
		default:
			break;
		}
		return result;
	}

	private String getRepayCode(int repayCode) {
		String sRepayCode = "";

		switch (repayCode) {
		case 1:
			sRepayCode = "匯款轉帳";
			break;
		case 2:
			sRepayCode = "銀行扣款";
			break;
		case 3:
			sRepayCode = "員工扣薪";
			break;
		case 4:
			sRepayCode = "支票";
			break;
		case 5:
			sRepayCode = "特約金";
			break;
		case 6:
			sRepayCode = "人事特約金";
			break;
		case 7:
			sRepayCode = "定存特約";
			break;
		case 8:
			sRepayCode = "劃撥存款";
			break;
		default:
			sRepayCode = "";
			break;
		}

		return sRepayCode;
	}

	private int findFacmNo(List<LoanBorMain> lLoanBorMain, List<Integer> facmlist) {
		int flagStatus = 0;
		int facmNo = 0;

//		額度 0.正常>2.催收>7.部呆>6.呆帳，排除結案戶。
		if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
			for (LoanBorMain tLoanBorMain : lLoanBorMain) {

//				判斷撥款檔裡之額度，是否存在於額度擔保品關聯檔
				Boolean existFlag = false;

//				前面找出額度擔保品關聯檔，向下之所有額度
				for (Integer iFacmNo : facmlist) {
					if (iFacmNo == tLoanBorMain.getFacmNo()) {
						existFlag = true;
						break;
					}
				}

//				如果無相同者，跳過
				if (!existFlag) {
					continue;
				}

				tmpFacm temp = new tmpFacm(tLoanBorMain.getCustNo(), tLoanBorMain.getStatus());
				switch (tLoanBorMain.getStatus()) {
				case 0:
					flagStatus = 1;
//					facmNo = tLoanBorMain.getFacmNo();
					putHashMap(temp, tLoanBorMain.getFacmNo());
					facmNo = custFacm.get(temp);
					break;
				case 2:
					if (flagStatus >= 2) {
						flagStatus = 2;
//						facmNo = tLoanBorMain.getFacmNo();
						putHashMap(temp, tLoanBorMain.getFacmNo());
						facmNo = custFacm.get(temp);
					}
					break;
				case 7:
					if (flagStatus >= 3) {
						flagStatus = 3;
//						facmNo = tLoanBorMain.getFacmNo();
						putHashMap(temp, tLoanBorMain.getFacmNo());
						facmNo = custFacm.get(temp);
					}
					break;
				case 6:
					if (flagStatus >= 4) {
						flagStatus = 4;
//						facmNo = tLoanBorMain.getFacmNo();
						putHashMap(temp, tLoanBorMain.getFacmNo());
						facmNo = custFacm.get(temp);
					}
					break;
				}
			}
		}
		return facmNo;
	}

//	貸放狀態相同時，取額度最大者
	private void putHashMap(tmpFacm temp, int facmNo) {
		if (custFacm.containsKey(temp)) {
			if (custFacm.get(temp) < facmNo) {
				custFacm.put(temp, facmNo);
			}
		} else {
			custFacm.put(temp, facmNo);
		}
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

	private String dateSlashFormat(int today) {
		String slashedDate = "";
		String acToday = "";
		if (today >= 1 && today < 19110000) {
			acToday = FormatUtil.pad9("" + (today + 19110000), 8);
		} else if (today >= 19110000) {
			acToday = FormatUtil.pad9("" + today, 8);
		}
		slashedDate = acToday.substring(0, 4) + "/" + acToday.substring(4, 6) + "/" + acToday.substring(6, 8);

		return slashedDate;
	}

	private String findAddress(CustMain tCustMain) {
		String address = "";

		if (tCustMain != null) {
			CdArea tCdArea = new CdArea();
			CdAreaId tCdAreaId = new CdAreaId();
			tCdAreaId.setCityCode(tCustMain.getCurrCityCode());
			tCdAreaId.setAreaCode(tCustMain.getCurrAreaCode());
			tCdArea = cdAreaService.findById(tCdAreaId);

			CdCity tCdCity = new CdCity();
			tCdCity = cdCityService.findById(tCustMain.getCurrCityCode());

			if (tCdArea != null && tCdArea != null) {
				address = tCdCity.getCityItem().trim() + tCdArea.getAreaShort().trim() + tCustMain.getCurrRoad().trim()
						+ tCustMain.getCurrSection().trim() + tCustMain.getCurrAlley().trim()
						+ tCustMain.getCurrLane().trim() + tCustMain.getCurrNum().trim()
						+ tCustMain.getCurrNumDash().trim() + tCustMain.getCurrFloor().trim()
						+ tCustMain.getCurrFloorDash().trim();
			}
		}
		return address;
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int status = 0;

		public tmpFacm(int custNo, int status) {
			this.setCustNo(custNo);
			this.setStatus(status);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

//		public int getFacmNo() {
//			return facmNo;
//		}
//
//		public void setFacmNo(int facmNo) {
//			this.facmNo = facmNo;
//		}
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

}