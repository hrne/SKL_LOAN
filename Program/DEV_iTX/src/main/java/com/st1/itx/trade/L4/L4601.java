package com.st1.itx.trade.L4;

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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.domain.InsuRenewMediaTempId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4601")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4601 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4601.class);

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	@Autowired
	public WebClient webClient;

	private String checkMsg = "";
	private int errorACnt = 0;
	private int errorBCnt = 0;
	private int errorCCnt = 0;

	private int succesCnt = 0;

	private int iInsuEndMonth = 0;
	private int insuStartDate = 0;
	private int insuEndDate = 0;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4601 ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		insuStartDate = parse.stringToInteger(iInsuEndMonth + "01");
		insuEndDate = parse.stringToInteger(iInsuEndMonth + "31");

		String reportA = titaVo.getParam("ReportA");
		String reportB = titaVo.getParam("ReportB");
		String reportC = titaVo.getParam("ReportC");

		this.info("reportA : " + reportA);
		this.info("reportB : " + reportB);
		this.info("reportC : " + reportC);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		if (!"".equals(reportA)) {
			totaA.init(titaVo);

			this.info("ReportA Start ...");
//			吃檔
//			String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\1)R-10904LNM01P.txt";
			String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + titaVo.getParam("FILENA").trim();

			ArrayList<String> dataLineList = new ArrayList<>();

			if (titaVo.getParam("FILENA").trim().indexOf("LNM01P") < 0) {
				throw new LogicException("E0014", "檔案錯誤，檔名 : " + titaVo.getParam("FILENA").trim());
			}

//			 編碼參數，設定為UTF-8 || big5
			try {
				dataLineList = fileCom.intputTxt(filePath1, "big5");
			} catch (IOException e) {
				throw new LogicException("E0014", "L4601(" + filePath1 + ") : " + e.getMessage());
			}

//			 使用資料容器內定義的方法切資料
			insuRenewFileVo.setValueFromFile(dataLineList);

			ArrayList<OccursList> uploadFile = insuRenewFileVo.getOccursList();

			if (uploadFile != null && uploadFile.size() != 0) {

				deleInsuRenewMediaTemp(titaVo);

				for (OccursList tempOccursList : uploadFile) {
					if (iInsuEndMonth != parse.stringToInteger(tempOccursList.get("FireInsuMonth"))) {
						throw new LogicException("E0014", "輸入火險年月與檔案不同");
					}

					List<InsuRenew> lerror2InsuRenew = new ArrayList<InsuRenew>();
					List<InsuRenew> lerror3InsuRenew = new ArrayList<InsuRenew>();

					Slice<InsuRenew> serror2InsuRenew = null;
					Slice<InsuRenew> serror3InsuRenew = null;

					this.info("CustNo : " + tempOccursList.get("CustNo"));
					this.info("FacmNo : " + tempOccursList.get("FacmNo"));
					this.info("ClCode1 : " + tempOccursList.get("ClCode1"));
					this.info("ClCode2 : " + tempOccursList.get("ClCode2"));
					this.info("ClNo : " + tempOccursList.get("ClNo"));
					this.info("NewTotalFee : " + tempOccursList.get("NewTotalFee"));
					this.info("InsuStartDate : " + tempOccursList.get("InsuStartDate"));

					serror2InsuRenew = insuRenewService.findL4601A(iInsuEndMonth,
							parse.stringToInteger(tempOccursList.get("CustNo")),
							parse.stringToInteger(tempOccursList.get("FacmNo")), this.index, this.limit);

					serror3InsuRenew = insuRenewService.findL4601B(iInsuEndMonth,
							parse.stringToInteger(tempOccursList.get("ClCode1")),
							parse.stringToInteger(tempOccursList.get("ClCode2")),
							parse.stringToInteger(tempOccursList.get("ClNo")), this.index, this.limit);

					lerror2InsuRenew = serror2InsuRenew == null ? null : serror2InsuRenew.getContent();
					lerror3InsuRenew = serror3InsuRenew == null ? null : serror3InsuRenew.getContent();

//		1.火險詢價上傳檔轉檔作業(檢核清單)
//			1.總保費=0) 
//			2.無資料(無此戶號額度、擔保品號碼
//				a.檢核
//				總金額=0
					if (parse.stringToBigDecimal(tempOccursList.get("NewTotalFee")).compareTo(BigDecimal.ZERO) == 0) {
						totaA = errorReportA(tempOccursList, 11);
//				無此戶號額度
					} else if (lerror2InsuRenew == null || lerror2InsuRenew.size() == 0) {
						totaA = errorReportA(tempOccursList, 12);
//				無此擔保品
					} else if (lerror3InsuRenew == null || lerror3InsuRenew.size() == 0) {
						totaA = errorReportA(tempOccursList, 13);
//				b.檢核無誤者上傳
					} else {
						InsuRenew tInsuRenew = new InsuRenew();
						InsuRenewId tInsuRenewId = new InsuRenewId();

						tInsuRenewId.setClCode1(parse.stringToInteger(tempOccursList.get("ClCode1").trim()));
						tInsuRenewId.setClCode2(parse.stringToInteger(tempOccursList.get("ClCode2").trim()));
						tInsuRenewId.setClNo(parse.stringToInteger(tempOccursList.get("ClNo").trim()));
						tInsuRenewId.setPrevInsuNo(tempOccursList.get("InsuNo").trim());
						tInsuRenewId.setEndoInsuNo(" ");

						tInsuRenew = insuRenewService.holdById(tInsuRenewId);
						if (tInsuRenew != null) {
//							0-起帳 1-銷帳 2-起帳刪除
							resetAcReceivable(2, tInsuRenew, titaVo);

							tInsuRenew.setFireInsuCovrg(
									parse.stringToBigDecimal(tempOccursList.get("NewFireInsuAmt").trim()));
							tInsuRenew.setEthqInsuCovrg(
									parse.stringToBigDecimal(tempOccursList.get("NewEqInsuAmt").trim()));
							tInsuRenew.setFireInsuPrem(
									parse.stringToBigDecimal(tempOccursList.get("NewFireInsuFee").trim()));
							tInsuRenew.setEthqInsuPrem(
									parse.stringToBigDecimal(tempOccursList.get("NewEqInsuFee").trim()));
							tInsuRenew.setInsuStartDate(
									parse.stringToInteger(tempOccursList.get("NewInsuStartDate").replaceAll("/", "")));
							tInsuRenew.setInsuEndDate(
									parse.stringToInteger(tempOccursList.get("NewInsuEndDate").replaceAll("/", "")));
							tInsuRenew
									.setTotInsuPrem(parse.stringToBigDecimal(tempOccursList.get("NewTotalFee").trim()));
							try {
								insuRenewService.update(tInsuRenew);
							} catch (DBException e) {
								throw new LogicException("E0007", "L4601 InsuRenew update " + e.getErrorMsg());
							}

							resetAcReceivable(0, tInsuRenew, titaVo);

						} else {
							this.info("InsuRenew is null... PK : " + tInsuRenewId.toString());
						}
						succesCnt = succesCnt + 1;
						setInsuRenewMediaTemp(tempOccursList, titaVo);
					}
				}
			}
		}
//		2.火險詢價重複投保報表
//			1.不同年月期間 errorBCode = 21
//				新保單起訖日年月不相等(key clno+PinsuNo Value insuDate)
//			2.同年月重複投保 errorBCode = 22
//				同年月同擔保品有兩筆 (key clno+insuDate Value PinsuNo)
		if (!"".equals(reportB)) {
			totaB.init(titaVo);

			this.info("ReportB Start ...");
			List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

			Slice<InsuRenew> sInsuRenew = null;

			sInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);

			lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

			if (lInsuRenew != null && lInsuRenew.size() != 0) {
				for (InsuRenew tInsuRenew : lInsuRenew) {
					Slice<InsuRenew> s2InsuRenew = null;

//					續保檔上期之保費迄日 不等於 新一期之保險起日(預設一期為一年)
					List<InsuRenew> l2InsuRenew = new ArrayList<InsuRenew>();
					s2InsuRenew = insuRenewService.findL4601B(calYear(iInsuEndMonth, -1), tInsuRenew.getClCode1(),
							tInsuRenew.getClCode2(), tInsuRenew.getClNo(), this.index, this.limit);

					l2InsuRenew = s2InsuRenew == null ? null : s2InsuRenew.getContent();

					InsuRenew t2InsuRenew = new InsuRenew();
					if (l2InsuRenew != null && l2InsuRenew.size() != 0) {
						t2InsuRenew = l2InsuRenew.get(0);
					}

//					或 新保檔之保險迄日 不等於 新寫入續保檔之保險起日
					InsuOrignal t2InsuOrignal = new InsuOrignal();
					t2InsuOrignal = insuOrignalService.clNoFirst(tInsuRenew.getClCode1(), tInsuRenew.getClCode2(),
							tInsuRenew.getClNo());

					if (t2InsuRenew != null && t2InsuRenew.getInsuEndDate() != 0) {
						this.info("t2InsuRenew.InsuEndDate = " + t2InsuRenew.getInsuEndDate());
						this.info("tInsuRenew.InsuStartDate = " + tInsuRenew.getInsuStartDate());
						if (t2InsuRenew.getInsuEndDate() != tInsuRenew.getInsuStartDate()) {
							totaB = errorReportB(tInsuRenew, 21);
						}
					} else if (t2InsuOrignal != null && t2InsuOrignal.getInsuEndDate() != 0) {
						this.info("InsuOrignal.InsuEndDate = " + t2InsuOrignal.getInsuEndDate());
						this.info("InsuRenew.InsuStartDate = " + tInsuRenew.getInsuStartDate());
						if (t2InsuOrignal.getInsuEndDate() != tInsuRenew.getInsuStartDate()) {
							totaB = errorReportB(tInsuRenew, 22);
						}
					}

//					重複投保
					List<InsuRenew> lerror3InsuRenew = new ArrayList<InsuRenew>();

					Slice<InsuRenew> serror3InsuRenew = null;

					serror3InsuRenew = insuRenewService.findL4601B(iInsuEndMonth, tInsuRenew.getClCode1(),
							tInsuRenew.getClCode2(), tInsuRenew.getClNo(), this.index, this.limit);

					lerror3InsuRenew = serror3InsuRenew == null ? null : serror3InsuRenew.getContent();

					if (lerror3InsuRenew != null && lerror3InsuRenew.size() >= 2) {
						totaB = errorReportB(tInsuRenew, 23);
					}
				}
			}
		}

//		3.續保資料錯誤明細表
		if (!"".equals(reportC)) {
			totaC.init(titaVo);

			this.info("ReportC Start ...");
			List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

			Slice<InsuRenew> sInsuRenew = null;

//		新增加之續保資料檢核
			sInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);

			lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

			if (lInsuRenew != null && lInsuRenew.size() != 0) {
				for (InsuRenew tInsuRenew : lInsuRenew) {
					int errorCode = 0;
					List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

					Slice<LoanBorMain> sLoanBorMain = null;

					sLoanBorMain = loanBorMainService.bormCustNoEq(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo(),
							tInsuRenew.getFacmNo(), 0, 999, this.index, this.limit);

					lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

//		額度下無正常或催收之撥款
					if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
						for (LoanBorMain tLoanBorMain : lLoanBorMain) {
							if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 3) {
								errorCode = 0;
								break;
							} else {
								errorCode = 31;
							}
						}
					} else {
						errorCode = 31;
					}
					totaC = errorReportC(tInsuRenew, errorCode);
				}
			}
		}
		this.info("errorACnt  = " + errorACnt);
		totaA.putParam("ErrorACnt", errorACnt);
		this.addList(totaA);

		this.info("ErrorBCnt  = " + errorBCnt);
		totaB.putParam("ErrorBCnt", errorBCnt);
		this.addList(totaB);

		this.info("errorCCnt  = ");
		totaC.putParam("ErrorCCnt", errorCCnt);
		this.addList(totaC);

		this.info("totavoList L4601  = " + this.sendList());

		if (!"".equals(reportA)) {
			checkMsg = "檢核正常筆數：" + succesCnt + "筆，檢核錯誤筆數：" + errorACnt + "筆。";
		}

		if (!"".equals(reportB)) {
			checkMsg += "火險詢價重複投保：" + errorBCnt + "筆。";
		}

		if (!"".equals(reportC)) {
			checkMsg += "續保資料錯誤：" + errorCCnt + "筆。";
		}

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4602", titaVo.getTlrNo(),
				checkMsg, titaVo);

//		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setInsuRenewMediaTemp(OccursList tempOccursList, TitaVo titaVo) throws LogicException {
		InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();
		InsuRenewMediaTempId tInsuRenewMediaTempId = new InsuRenewMediaTempId();
		tInsuRenewMediaTempId.setFireInsuMonth("" + iInsuEndMonth);
		tInsuRenewMediaTempId.setInsuNo(tempOccursList.get("InsuNo").trim());

		tInsuRenewMediaTemp.setInsuRenewMediaTempId(tInsuRenewMediaTempId);
//		ReturnCode	回傳碼
		tInsuRenewMediaTemp.setReturnCode(tempOccursList.get("ReturnCode").trim());
//		InsuCampCode	保險公司代碼
		tInsuRenewMediaTemp.setInsuCampCode(tempOccursList.get("InsuCampCode").trim());
//		InsuCustId	提供人統一編號
		tInsuRenewMediaTemp.setInsuCustId(tempOccursList.get("InsuCustId").trim());
//		InsuCustName	提供人姓名
		tInsuRenewMediaTemp.setInsuCustName(tempOccursList.get("InsuCustName").trim());
//		LoanCustId	借款人統一編號
		tInsuRenewMediaTemp.setLoanCustId(tempOccursList.get("LoanCustId").trim());
//		LoanCustName	借款人姓名
		tInsuRenewMediaTemp.setLoanCustName(tempOccursList.get("LoanCustName").trim());
//		PostalCode	郵遞區號
		tInsuRenewMediaTemp.setPostalCode(tempOccursList.get("PostalCode").trim());
//		Address	門牌號碼
		tInsuRenewMediaTemp.setAddress(tempOccursList.get("Address").trim());
//		BuildingSquare	主建物坪數
		tInsuRenewMediaTemp.setBuildingSquare(tempOccursList.get("BuildingSquare").trim());
//		BuildingCode	建物結構代碼
		tInsuRenewMediaTemp.setBuildingCode(tempOccursList.get("BuildingCode").trim());
//		BuildingYears	建造年份
		tInsuRenewMediaTemp.setBuildingYears(tempOccursList.get("BuildingYears").trim());
//		BuildingFloors	樓層數
		tInsuRenewMediaTemp.setBuildingFloors(tempOccursList.get("BuildingFloors").trim());
//		RoofCode	屋頂結構代碼
		tInsuRenewMediaTemp.setRoofCode(tempOccursList.get("RoofCode").trim());
//		BusinessUnit	營業單位別
		tInsuRenewMediaTemp.setBusinessUnit(tempOccursList.get("BusinessUnit").trim());
//		ClCode1	押品別１
		tInsuRenewMediaTemp.setClCode1(tempOccursList.get("ClCode1").trim());
//		ClCode2	押品別２
		tInsuRenewMediaTemp.setClCode2(tempOccursList.get("ClCode2").trim());
//		ClNo	押品號碼
		tInsuRenewMediaTemp.setClNo(tempOccursList.get("ClNo").trim());
//		Seq	序號
		tInsuRenewMediaTemp.setSeq(tempOccursList.get("Seq").trim());
//		InsuStartDate	保險起日
		tInsuRenewMediaTemp.setInsuStartDate(tempOccursList.get("InsuStartDate").trim());
//		InsuEndDate	保險迄日
		tInsuRenewMediaTemp.setInsuEndDate(tempOccursList.get("InsuEndDate").trim());
//		FireInsuAmt	火險保額
		tInsuRenewMediaTemp.setFireInsuAmt(tempOccursList.get("FireInsuAmt").trim());
//		FireInsuFee	火險保費
		tInsuRenewMediaTemp.setFireInsuFee(tempOccursList.get("FireInsuFee").trim());
//		EqInsuAmt	地震險保額
		tInsuRenewMediaTemp.setEqInsuAmt(tempOccursList.get("EqInsuAmt").trim());
//		EqInsuFee	地震險保費
		tInsuRenewMediaTemp.setEqInsuFee(tempOccursList.get("EqInsuFee").trim());
//		CustNo	借款人戶號
		tInsuRenewMediaTemp.setCustNo(tempOccursList.get("CustNo").trim());
//		FacmNo	額度編號
		tInsuRenewMediaTemp.setFacmNo(tempOccursList.get("FacmNo").trim());
//		SendDate	傳檔日期
		tInsuRenewMediaTemp.setSendDate(tempOccursList.get("SendDate").trim());
//		NewInusNo	保單號碼(新)
		tInsuRenewMediaTemp.setNewInusNo(tempOccursList.get("NewInusNo").trim());
//		NewInsuStartDate	保險起日(新)
		tInsuRenewMediaTemp.setNewInsuStartDate(tempOccursList.get("NewInsuStartDate").trim());
//		NewInsuEndDate	保險迄日(新)
		tInsuRenewMediaTemp.setNewInsuEndDate(tempOccursList.get("NewInsuEndDate").trim());
//		NewFireInsuAmt	火險保額(新)
		tInsuRenewMediaTemp.setNewFireInsuAmt(tempOccursList.get("NewFireInsuAmt").trim());
//		NewFireInsuFee	火險保費(新)
		tInsuRenewMediaTemp.setNewFireInsuFee(tempOccursList.get("NewFireInsuFee").trim());
//		NewEqInsuAmt	地震險保額(新)
		tInsuRenewMediaTemp.setNewEqInsuAmt(tempOccursList.get("NewEqInsuAmt").trim());
//		NewEqInsuFee	地震險保費(新)
		tInsuRenewMediaTemp.setNewEqInsuFee(tempOccursList.get("NewEqInsuFee").trim());
//		NewTotalFee	總保費(新)
		tInsuRenewMediaTemp.setNewTotalFee(tempOccursList.get("NewTotalFee").trim());
//		Remark1	備註一
		tInsuRenewMediaTemp.setRemark1(tempOccursList.get("Remark1").trim());
//		MailingAddress	通訊地址
		tInsuRenewMediaTemp.setMailingAddress(tempOccursList.get("MailingAddress").trim());
//		Remark2	備註二
		tInsuRenewMediaTemp.setRemark2(tempOccursList.get("Remark2").trim());
//		SklSalesName	新光人壽業務員名稱
		tInsuRenewMediaTemp.setSklSalesName(tempOccursList.get("SklSalesName").trim());
//		SklUnitCode	新光人壽單位代號
		tInsuRenewMediaTemp.setSklUnitCode(tempOccursList.get("SklUnitCode").trim());
//		SklUnitName	新光人壽單位中文
		tInsuRenewMediaTemp.setSklUnitName(tempOccursList.get("SklUnitName").trim());
//		SklSalesCode	新光人壽業務員代號
		tInsuRenewMediaTemp.setSklSalesCode(tempOccursList.get("SklSalesCode").trim());
//		RenewTrlCode	新產續保經辦代號
		tInsuRenewMediaTemp.setRenewTrlCode(tempOccursList.get("RenewTrlCode").trim());
//		RenewUnit	新產續保單位
		tInsuRenewMediaTemp.setRenewUnit(tempOccursList.get("RenewUnit").trim());

		try {
			insuRenewMediaTempService.insert(tInsuRenewMediaTemp);
		} catch (DBException e) {
			throw new LogicException("E0005", "L4601 InsuRenewMediaTemp insert " + e.getErrorMsg());
		}
	}

	private void deleInsuRenewMediaTemp(TitaVo titaVo) throws LogicException {
		Slice<InsuRenewMediaTemp> sInsuRenewMediaTemp = null;
		List<InsuRenewMediaTemp> lInsuRenewMediaTemp = new ArrayList<InsuRenewMediaTemp>();
		sInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg("" + iInsuEndMonth, "" + iInsuEndMonth,
				this.index, this.limit, titaVo);
		lInsuRenewMediaTemp = sInsuRenewMediaTemp == null ? null : sInsuRenewMediaTemp.getContent();

		if (lInsuRenewMediaTemp != null && lInsuRenewMediaTemp.size() != 0) {
			for (InsuRenewMediaTemp tInsuRenewMediaTemp : lInsuRenewMediaTemp) {
				InsuRenewMediaTemp t2InsuRenewMediaTemp = insuRenewMediaTempService
						.holdById(tInsuRenewMediaTemp.getInsuRenewMediaTempId(), titaVo);
				try {
					insuRenewMediaTempService.delete(t2InsuRenewMediaTemp, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "InsuRenew insert error : " + e.getErrorMsg());
				}
			}
		}
	}

	private TotaVo errorReportA(OccursList tempOccursList, int errorCode) {
//		c.有誤者寫入報表
		if (errorCode >= 11 && errorCode <= 19) {
//			戶號 額度 擔保品序號 錯誤原因	總筆數
			OccursList occursListReport = new OccursList();
			occursListReport.putParam("ReportACustNo", tempOccursList.get("CustNo"));
			occursListReport.putParam("ReportAFacmNo", tempOccursList.get("FacmNo"));
			occursListReport.putParam("ReportAClCode1", tempOccursList.get("ClCode1"));
			occursListReport.putParam("ReportAClCode2", tempOccursList.get("ClCode2"));
			occursListReport.putParam("ReportAClNo", tempOccursList.get("ClNo"));

			if (errorCode == 11) {
				occursListReport.putParam("ReportAErrorMsg", "總保費 = 0");
			} else if (errorCode == 12) {
				occursListReport.putParam("ReportAErrorMsg", "資料更新失敗");
			} else if (errorCode == 13) {
				occursListReport.putParam("ReportAErrorMsg", "資料更新失敗");
			} else {
				occursListReport.putParam("ReportAErrorMsg", "");
			}

//			totaPatch(occursListReportA, "A");
			totaA.addOccursList(occursListReport);
			totaA.putParam("MSGID", "L461A");

//			this.totaVo.addOccursList(occursListReport);
//			this.totaVo.putParam("MSGID", "L461A");

			errorACnt = errorACnt + 1;
		}
		return totaA;
	}

	private TotaVo errorReportB(InsuRenew tInsuRenew, int errorCode) throws LogicException {
		this.info("ReportB Start...");
		this.info("errorCode : " + errorCode);

//		寫報表
//		戶號 額度 借款人 押品號碼 新保險單起日 新保險單迄日 原有保險單號 保險起日 保險迄日
		if (errorCode >= 21 && errorCode <= 29) {
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

			OccursList occursListReport = new OccursList();

			occursListReport.putParam("ReportBCustNo", tInsuRenew.getCustNo());
			occursListReport.putParam("ReportBFacmNo", tInsuRenew.getFacmNo());
			if (tCustMain != null) {
				occursListReport.putParam("ReportBCustName", tCustMain.getCustName());
			} else {
				occursListReport.putParam("ReportBCustName", "");
			}
			occursListReport.putParam("ReportBClCode1", tInsuRenew.getInsuRenewId().getClCode1());
			occursListReport.putParam("ReportBClCode2", tInsuRenew.getInsuRenewId().getClCode2());
			occursListReport.putParam("ReportBClNo", tInsuRenew.getInsuRenewId().getClNo());
			occursListReport.putParam("ReportBNewInsuStartDate", tInsuRenew.getInsuStartDate() + 19110000);
			occursListReport.putParam("ReportBNewInsuEndDate", tInsuRenew.getInsuEndDate() + 19110000);
			occursListReport.putParam("ReportBPrevInsuNo", tInsuRenew.getInsuRenewId().getPrevInsuNo());
//			上年份 續保檔 or 初保檔
			List<InsuRenew> lastYearInsuRenew = new ArrayList<InsuRenew>();

			Slice<InsuRenew> slastYearInsuRenew = null;

			slastYearInsuRenew = insuRenewService.findL4601B(calYear(iInsuEndMonth, -1),
					tInsuRenew.getInsuRenewId().getClCode1(), tInsuRenew.getInsuRenewId().getClCode2(),
					tInsuRenew.getInsuRenewId().getClNo(), this.index, this.limit);

			lastYearInsuRenew = slastYearInsuRenew == null ? null : slastYearInsuRenew.getContent();

			InsuOrignal lastYearInsuOrignal = new InsuOrignal();
			lastYearInsuOrignal = insuOrignalService.clNoFirst(tInsuRenew.getInsuRenewId().getClCode1(),
					tInsuRenew.getInsuRenewId().getClCode2(), tInsuRenew.getInsuRenewId().getClNo());

			if (lastYearInsuRenew != null && lastYearInsuRenew.size() != 0) {
				occursListReport.putParam("ReportBInsuStartDate",
						lastYearInsuRenew.get(0).getInsuStartDate() + 19110000);
				occursListReport.putParam("ReportBInsuEndDate", lastYearInsuRenew.get(0).getInsuEndDate() + 19110000);
			} else if (lastYearInsuOrignal != null) {
				occursListReport.putParam("ReportBInsuStartDate", lastYearInsuOrignal.getInsuStartDate() + 19110000);
				occursListReport.putParam("ReportBInsuEndDate", lastYearInsuOrignal.getInsuEndDate() + 19110000);
			} else {
				occursListReport.putParam("ReportBInsuStartDate", "");
				occursListReport.putParam("ReportBInsuEndDate", "");
			}

			ClBuilding tClBuilding = new ClBuilding();
			ClBuildingId tClBuildingId = new ClBuildingId();
			tClBuildingId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
			tClBuildingId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
			tClBuildingId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
			tClBuilding = clBuildingService.findById(tClBuildingId);

			if (tClBuilding != null) {
				occursListReport.putParam("ReportBAddress", tClBuilding.getBdLocation().trim());
			} else {
				occursListReport.putParam("ReportBAddress", "");
			}
//			totaPatch(occursListReportB, "B");
			totaB.addOccursList(occursListReport);
			totaB.putParam("MSGID", "L461B");

//			this.totaVo.addOccursList(occursListReport);
//			this.totaVo.putParam("MSGID", "L461B");

			errorBCnt = errorBCnt + 1;
		}
		return totaB;
	}

	private TotaVo errorReportC(InsuRenew tInsuRenew, int errorCode) {
//		寫報表
//		押品號碼 原保單號碼 戶號 額度 戶名 新保險起日 新保險迄日 火險保額 火線保費 地震險保額 地震險保費 總保費 錯誤說明
		if (errorCode >= 31 && errorCode <= 39) {
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

			OccursList occursListReport = new OccursList();

			occursListReport.putParam("ReportCClCode1", tInsuRenew.getInsuRenewId().getClCode1());
			occursListReport.putParam("ReportCClCode2", tInsuRenew.getInsuRenewId().getClCode2());
			occursListReport.putParam("ReportCClNo", tInsuRenew.getInsuRenewId().getClNo());
			occursListReport.putParam("ReportCPrevInsuNo", tInsuRenew.getInsuRenewId().getPrevInsuNo());
			occursListReport.putParam("ReportCCustNo", tInsuRenew.getCustNo());
			occursListReport.putParam("ReportCFacmNo", tInsuRenew.getFacmNo());
			if (tCustMain != null) {
				occursListReport.putParam("ReportCCustName", tCustMain.getCustName());
			} else {
				occursListReport.putParam("ReportCCustName", "");
			}
			occursListReport.putParam("ReportCNewInsuStartDate", tInsuRenew.getInsuStartDate() + 19110000);
			occursListReport.putParam("ReportCNewInsuEndDate", tInsuRenew.getInsuEndDate() + 19110000);
			occursListReport.putParam("ReportCFireAmt", tInsuRenew.getFireInsuCovrg());
			occursListReport.putParam("ReportCFireFee", tInsuRenew.getFireInsuPrem());
			occursListReport.putParam("ReportCEthqAmt", tInsuRenew.getEthqInsuCovrg());
			occursListReport.putParam("ReportCEthqFee", tInsuRenew.getEthqInsuPrem());
			occursListReport.putParam("ReportCTotlFee", tInsuRenew.getTotInsuPrem());
			if (errorCode == 31) {
				occursListReport.putParam("ReportCErrMsg", "此額度無正常戶／催收戶之撥款");
			} else {
				occursListReport.putParam("ReportCErrMsg", "");
			}

//			totaPatch(occursListReportC, "C");
			totaC.addOccursList(occursListReport);
			totaC.putParam("MSGID", "L461C");

//			this.totaVo.addOccursList(occursListReport);
//			this.totaVo.putParam("MSGID", "L461C");

			errorCCnt = errorCCnt + 1;
		}
		return totaC;
	}

	private int calYear(int today, int year) throws LogicException {
		int resultMonth = 0;
//	10801
//	201901
//	1080101
//	trans today = Bc format
		if (today < 100000) {
			today = parse.stringToInteger((today + 191100) + "01");
		} else if (today < 1000000) {
			today = parse.stringToInteger(today + "01");
		} else if (today < 10000000) {
			today = today + 19110000;
		}

		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setYears(year);
		today = dateUtil.getCalenderDay();

		resultMonth = parse.stringToInteger((today + "").substring(0, 6));

		return resultMonth;
	}

	private void resetAcReceivable(int flag, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

		if (!"Y".equals(tInsuRenew.getNotiTempFg())) {
			this.info("該筆未入通知，NotiTempFg : " + tInsuRenew.getNotiTempFg());
			return;
		}

		if (tInsuRenew.getStatusCode() == 0) {
			AcReceivable acReceivable = new AcReceivable();

			acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			acReceivable.setAcctCode("TMI"); // 業務科目
			acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
			acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
			acReceivable.setFacmNo(tInsuRenew.getFacmNo());
			acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
			acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
			acReceivableList.add(acReceivable);

			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(flag, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
		} else {
			throw new LogicException(titaVo, "E0015", "該筆狀態不為正常，Status : " + tInsuRenew.getStatusCode());
		}
	}
}