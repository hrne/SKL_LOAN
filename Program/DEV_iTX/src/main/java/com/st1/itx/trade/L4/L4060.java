package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * END=X,1<br>
 */

@Service("L4060")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4060 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4060.class);

	/* DB服務注入 */
	@Autowired
	public ClFacService clFacService;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;

	/* DB服務注入 */
	@Autowired
	public ClMainService clMainService;

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;

	/* DB服務注入 */
	@Autowired
	public InsuOrignalService insuOrignalService;

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public ClBuildingService clBuildingService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	private List<ClFac> listClFac;
	private CustMain tCustMain;
	private FacMain tFacMain;
	private FacCaseAppl tFacCaseAppl;
	private InsuOrignal tInsuOrignal;
	private InsuRenew tInsuRenew;
	private ClMain tClMain;
	private List<LoanBorMain> listLoanBorMain;
	private OccursList occursList;
	private BigDecimal drawdownAmt;
	private BigDecimal loanBalance;
	private int facmApplNo;
	private int statusCode;
	private int classFlag;
	private String newInsu;
	private String dtlInsu;

	// initialize variable
	@PostConstruct
	public void init() {
		this.tCustMain = new CustMain();
		this.tFacMain = new FacMain();
		this.tFacCaseAppl = new FacCaseAppl();
		this.tInsuOrignal = new InsuOrignal();
		this.tInsuRenew = new InsuRenew();
		this.tClMain = new ClMain();
		this.listClFac = new ArrayList<ClFac>();
		this.drawdownAmt = new BigDecimal(0);
		this.loanBalance = new BigDecimal(0);
		this.facmApplNo = 0;
		this.statusCode = 0;
		this.classFlag = 9;
		this.newInsu = "Y";
		this.dtlInsu = "N";
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4060 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<ClFac> sClFac = null;

		this.info("L4060-A test!! ");

		/* 1:用戶號找擔保品 */
		if (titaVo.getParam("FunctionCode").equals("1")) {
			if (parse.stringToInteger(titaVo.getParam("FacmNo")) == 0) {
				sClFac = clFacService.custNoEq(parse.stringToInteger(titaVo.getParam("CustNo")), this.index,
						this.limit);
			} else {
				sClFac = clFacService.facmNoEq(parse.stringToInteger(titaVo.getParam("CustNo")),
						parse.stringToInteger(titaVo.getParam("FacmNo")), this.index, this.limit);
			}

			/* 2:用擔保品找戶號 */
		} else if (titaVo.getParam("FunctionCode").equals("2")) {
			sClFac = clFacService.clNoEq(parse.stringToInteger(titaVo.getParam("ClCode1")),
					parse.stringToInteger(titaVo.getParam("ClCode2")), parse.stringToInteger(titaVo.getParam("ClNo")),
					this.index, this.limit);
		}

		listClFac = sClFac == null ? null : sClFac.getContent();

		if (listClFac == null || listClFac.size() == 0) {
			throw new LogicException(titaVo, "E0001", " 查無資料 ");
		}

		int clFac = 0;
		int facM = 0;

		/* 用找出的list做歷遍寫入totalist */
		/* ClFac #OOCustNoLB=戶號 #OOClNoLB=擔保品編號 */
		for (ClFac tClFac : listClFac) {
			init();

//			僅有房地押品才查詢
			if (tClFac.getClCode1() != 1) {
				this.info("ClCode1 != 1...，tClFac.getClCode1() = " + tClFac.getClCode1());
				continue;
			} else {
				clFac = 1;
			}

			tCustMain = custMainService.custNoFirst(tClFac.getCustNo(), tClFac.getCustNo());
			/* CusM #OOCustNm=戶名 */

			FacMainId tFacMainId = new FacMainId();
			tFacMainId.setCustNo(tClFac.getCustNo());
			tFacMainId.setFacmNo(tClFac.getFacmNo());
			tFacMain = facMainService.findById(tFacMainId, titaVo);

			/* FacM #OOLineAmt=核准額度 */

			if (tFacMain == null) {
				this.info("tFacMain == null... ");
				continue;
			} else {
				facM = 1;
			}

			facmApplNo = tFacMain.getApplNo();
			tFacCaseAppl = facCaseApplService.findById(facmApplNo);
			/* FacCaseAppl #OOApproveDate=核准日期 ??? how to join */

			Slice<ClMain> sClMain = null;

			sClMain = clMainService.findClNo(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(), this.index,
					this.limit);

			List<ClMain> l2ClMain = new ArrayList<ClMain>();

			l2ClMain = sClMain == null ? null : sClMain.getContent();

			BigDecimal evaAmt = BigDecimal.ZERO;

			if (l2ClMain != null && l2ClMain.size() != 0) {
				tClMain = l2ClMain.get(0);

				evaAmt = tClMain.getEvaAmt();
			}

			String bdLocation = "";

			ClBuilding tClBuilding = new ClBuilding();
			ClBuildingId tClBuildingId = new ClBuildingId();
			tClBuildingId.setClCode1(tClFac.getClCode1());
			tClBuildingId.setClCode2(tClFac.getClCode2());
			tClBuildingId.setClNo(tClFac.getClNo());

			tClBuilding = clBuildingService.findById(tClBuildingId);
			if (tClBuilding == null) {
				bdLocation = "";
			} else {
				bdLocation = limitLength(tClBuilding.getBdLocation(), 99);
			}

			tInsuOrignal = insuOrignalService.clNoFirst(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo());

			tInsuRenew = new InsuRenew();
			List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

			Slice<InsuRenew> sInsuRenew = null;

			sInsuRenew = insuRenewService.findNowInsuEq(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo(),
					this.index, this.limit);

			lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

//			保單明細按鈕 on
			if (tInsuOrignal != null || lInsuRenew != null) {
				dtlInsu = "Y";
			}

			if (lInsuRenew != null && lInsuRenew.size() != 0) {
				tInsuRenew = lInsuRenew.get(0);
			}

			/* InsuOrigin #OONowInsuNo */

			Slice<LoanBorMain> sLoanBorMain = null;

			sLoanBorMain = loanBorMainService.bormCustNoEq(tClFac.getCustNo(), tClFac.getFacmNo(), tClFac.getFacmNo(),
					0, 999, this.index, this.limit);
			/* Borm #OOStatus=狀態 0: 正常戶 4: 逾期戶 2: 催收戶 6: 呆帳戶 */
			/* #OOLOANAMT=貸放金額 SUM(DrawdownAmt) */
			/* #OOLOANBAL=貸放餘額 SUM(LoanBalance) */

			listLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

			if (listLoanBorMain == null || listLoanBorMain.size() == 0) {

			} else {
				for (LoanBorMain tLoanBorMain : listLoanBorMain) {
					drawdownAmt = drawdownAmt.add(tLoanBorMain.getDrawdownAmt());
					loanBalance = loanBalance.add(tLoanBorMain.getLoanBal());
					statusCode = tLoanBorMain.getStatus();
					calcStatus(statusCode);
				}
			}

			occursList = new OccursList();
			occursList.putParam("OOCustNo", tClFac.getCustNo());
			occursList.putParam("OOFacmNo", tClFac.getFacmNo());
			occursList.putParam("OOClCode1", tClFac.getClCode1());
			occursList.putParam("OOClCode2", tClFac.getClCode2());
			occursList.putParam("OOClNo", tClFac.getClNo());
			occursList.putParam("OOCustNm", tCustMain.getCustName());
			occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
			occursList.putParam("OOApproveDate", tFacCaseAppl.getApproveDate());
			occursList.putParam("OOEvaAmt", evaAmt);
			occursList.putParam("OOBdLocation", bdLocation);
			occursList.putParam("OODrawdownAmt", drawdownAmt);
			occursList.putParam("OOLoanBalance", loanBalance);
			occursList.putParam("OOStatus", statusCodeX(statusCode));
			occursList.putParam("OONEWINSU", newInsu);
			occursList.putParam("OODTLINSU", dtlInsu);
			occursList.putParam("OONowInsuNo", tInsuRenew.getNowInsuNo());

			this.totaVo.addOccursList(occursList);
		}

		if (clFac == 0 && facM == 0) {
			throw new LogicException(titaVo, "E0001", " 查無資料 ");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	/* 按照 6呆帳 -> 7部呆 -> 2催收 -> 0正常 -> else 階級顯示 */
	private int calcStatus(int tmpStatusCode) throws LogicException {
		if (tmpStatusCode == 6) {
			classFlag = 1;
		} else if (tmpStatusCode == 7 && classFlag >= 2) {
			classFlag = 2;
		} else if (tmpStatusCode == 2 && classFlag >= 3) {
			classFlag = 3;
		} else if (tmpStatusCode == 0 && classFlag >= 4) {
			classFlag = 4;
		}

		switch (classFlag) {
		case 1:
			statusCode = 6;
			break;
		case 2:
			statusCode = 7;
			break;
		case 3:
			statusCode = 2;
			break;
		case 4:
			statusCode = 0;
			break;
		default:
			/* status */
		}
		return statusCode;
	}

	private String statusCodeX(int statusCode) {
		String result = "";
		switch (statusCode) {
		case 0:
			result = "正常";
			break;
		case 2:
			result = "催收";
			break;
		case 6:
			result = "呆帳";
			break;
		case 7:
			result = "部呆";
			break;
		default:
			/* status */
		}
		return result;
	}

	private String limitLength(String str, int pos) {
		byte[] input = str.getBytes();

		int inputLength = input.length;

		this.info("str ..." + str);
		this.info("inputLength ..." + inputLength);

		int resultLength = inputLength;

		if (inputLength > pos) {
			resultLength = pos;
		}

		String result = "";

		if (resultLength > 0) {
			byte[] resultBytes = new byte[resultLength];
			System.arraycopy(input, 0, resultBytes, 0, resultLength);
			result = new String(resultBytes);
		}

		return result;
	}
}