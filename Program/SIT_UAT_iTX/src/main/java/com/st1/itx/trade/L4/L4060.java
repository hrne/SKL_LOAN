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
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
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
import com.st1.itx.util.common.FacStatusCom;
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

	@Autowired
	public FacStatusCom facStatusCom;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	private List<ClFac> listClFac = new ArrayList<ClFac>();
	private CustMain tCustMain;
	private FacMain tFacMain;
	private FacCaseAppl tFacCaseAppl;
	private InsuOrignal tInsuOrignal;
	private InsuRenew tInsuRenew;
	private OccursList occursList;
	private BigDecimal drawdownAmt;
	private BigDecimal loanBalance;
//	private int facmApplNo;
	private int statusCode;
	private int classFlag;
	private String newInsu;
	private String dtlInsu;

	// initialize variable
	@PostConstruct
	public void init() {
		this.tCustMain = null;
		this.tFacMain = null;
		this.tFacCaseAppl = null;
		this.tInsuOrignal = null;
		this.tInsuRenew = null;
		this.drawdownAmt = new BigDecimal(0);
		this.loanBalance = new BigDecimal(0);
		this.statusCode = 10;
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
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		Slice<ClFac> sClFac = null;

		this.info("L4060-A test!! ");

		/* 1:用戶號找擔保品 */
		if (titaVo.getParam("FunctionCode").equals("1")) {
			if (parse.stringToInteger(titaVo.getParam("FacmNo")) == 0) {
				sClFac = clFacService.custNoEq(iCustNo, this.index, this.limit, titaVo);
			} else {
				sClFac = clFacService.facmNoEq(iCustNo, iFacmNo, this.index, this.limit, titaVo);
			}

			/* 2:用擔保品找戶號 */
		} else if (titaVo.getParam("FunctionCode").equals("2")) {
			sClFac = clFacService.clNoEq(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
		}

		if (sClFac != null) {
			listClFac = sClFac.getContent();
		}

		// 找未建額度關聯的擔保品
		if (titaVo.getParam("FunctionCode").equals("2") && listClFac.size() == 0) {
			this.info("L4060-a test!! ");
			Slice<ClMain> sClMain = clMainService.findClNo(iClCode1, iClCode2, iClNo, this.index, this.limit, titaVo);
			if (sClMain != null) {
				ClFac tClFac = new ClFac();
				tClFac.setClCode1(iClCode1);
				tClFac.setClCode2(iClCode2);
				tClFac.setClNo(iClNo);
				listClFac.add(tClFac);
				this.info("L4060-b test!! ");
			}
		}

		if (listClFac.size() == 0) {
			throw new LogicException(titaVo, "E0001", " 查無資料 ");
		}

//		int clFac = 0;
//		int facM = 0;

		/* 用找出的list做歷遍寫入totalist */
		/* ClFac #OOCustNoLB=戶號 #OOClNoLB=擔保品編號 */
		for (ClFac tClFac : listClFac) {
			init();
			if (tClFac.getApproveNo() > 0) {
				tFacCaseAppl = facCaseApplService.findById(tClFac.getApproveNo(), titaVo);
				tCustMain = custMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
				tFacMain = facMainService.facmApplNoFirst(tClFac.getApproveNo(), titaVo);
			}

			BigDecimal evaAmt = BigDecimal.ZERO;
			ClMain tClMain = clMainService
					.findById(new ClMainId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo()), titaVo);

			if (tClMain != null) {
				evaAmt = tClMain.getEvaAmt();
			}

			String bdLocation = "";
			ClBuilding tClBuilding = clBuildingService
					.findById(new ClBuildingId(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo()), titaVo);
			if (tClBuilding != null) {
				bdLocation = limitLength(tClBuilding.getBdLocation(), 99);
			}

			tInsuOrignal = insuOrignalService.clNoFirst(tClFac.getClCode1(), tClFac.getClCode2(), tClFac.getClNo());

			Slice<InsuRenew> sInsuRenew = insuRenewService.findNowInsuEq(tClFac.getClCode1(), tClFac.getClCode2(),
					tClFac.getClNo(), this.index, this.limit, titaVo);

//			保單明細按鈕 on
			if (tInsuOrignal != null || sInsuRenew != null) {
				dtlInsu = "Y";
			}

			String nowInsuNo = "";
			if (sInsuRenew != null) {
				InsuRenew tInsuRenew = sInsuRenew.getContent().get(0);
				if ("".equals(tInsuRenew.getNowInsuNo())) {
					nowInsuNo = tInsuRenew.getPrevInsuNo();
				} else {
					nowInsuNo = tInsuRenew.getNowInsuNo();
				}
			} else if (tInsuOrignal != null) {
				nowInsuNo = tInsuOrignal.getOrigInsuNo();
			}

			/* Borm #OOStatus=狀態 0: 正常戶 4: 逾期戶 2: 催收戶 6: 呆帳戶 */
			/* #OOLOANAMT=貸放金額 SUM(DrawdownAmt) */
			/* #OOLOANBAL=貸放餘額 SUM(LoanBalance) */

			Slice<LoanBorMain> sLoanBorMain = loanBorMainService.bormCustNoEq(tClFac.getCustNo(), tClFac.getFacmNo(),
					tClFac.getFacmNo(), 0, 900, this.index, this.limit, titaVo);
			List<LoanBorMain> lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();
			if (sLoanBorMain != null) {
				for (LoanBorMain tLoanBorMain : sLoanBorMain.getContent()) {
					drawdownAmt = drawdownAmt.add(tLoanBorMain.getDrawdownAmt());
					loanBalance = loanBalance.add(tLoanBorMain.getLoanBal());
				}
				this.statusCode = facStatusCom.settingStatus(lLoanBorMain,this.txBuffer.getTxBizDate().getTbsDy());
			}
			occursList = new OccursList();
			occursList.putParam("OOCustNo", tClFac.getCustNo());
			occursList.putParam("OOFacmNo", tClFac.getFacmNo());
			occursList.putParam("OOClCode1", tClFac.getClCode1());
			occursList.putParam("OOClCode2", tClFac.getClCode2());
			occursList.putParam("OOClNo", tClFac.getClNo());
			String custName = "";
			if (tCustMain != null) {
				custName = tCustMain.getCustName();
			}
			BigDecimal lineAmt = BigDecimal.ZERO;
			if (tFacMain != null) {
				lineAmt = tFacMain.getLineAmt();
			}
			int approveDate = 0;
			if (tFacCaseAppl != null) {
				approveDate = tFacCaseAppl.getApproveDate();
			}
			occursList.putParam("OOCustNm", custName);
			occursList.putParam("OOLineAmt", lineAmt);
			occursList.putParam("OOApproveDate", approveDate);
			occursList.putParam("OOEvaAmt", evaAmt);
			occursList.putParam("OOBdLocation", bdLocation);
			occursList.putParam("OODrawdownAmt", drawdownAmt);
			occursList.putParam("OOLoanBalance", loanBalance);
			occursList.putParam("OOStatus", statusCodeX(statusCode));
			occursList.putParam("OONEWINSU", newInsu);
			occursList.putParam("OODTLINSU", dtlInsu);
			occursList.putParam("OONowInsuNo", nowInsuNo);

			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private String statusCodeX(int statusCode) {
//		 priority    status  
//		  1  --  6.呆帳戶
//		  2  --  7.部分轉呆戶
//		  3  --  2.催收戶
//		  4  --  4.逾期戶(正常戶逾期超過一個月)
//		  5  --  0.正常戶
//		  6  --  5.催收結案戶
//		  7  --  8.債權轉讓戶
//		  8  --  9.呆帳結案戶
//		  9  --  3.結案戶
//       10  --  1.展期
		String result = "";
		switch (statusCode) {
		case 0:
		case 4:
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
		case 3:
		case 5:
		case 8:
		case 9:
			result = "結案";
			break;
		case 1:
			result = "展期";
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