package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4950")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4950 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4950.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public CustMainService custMainService;

	int nextPayIntDateFrom = 0;
	int nextPayIntDateTo = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4950 ");
		this.totaVo.init(titaVo);

		nextPayIntDateFrom = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;
		nextPayIntDateTo = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;

		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<LoanBorMain> sLoanBorMain = null;

		sLoanBorMain = loanBorMainService.nextPayIntDateRange(0, nextPayIntDateTo, 0, this.index,
				this.limit);
//		sLoanBorMain = loanBorMainService.nextPayIntDateRange(20200416, 20200416, 0, this.index, this.limit);
		int errCnt = 0;

//		1.LoanBorm 扣款日=入帳日
//		2.Facm repayCode = 03(員工扣薪)
//		3.CdEmp 檢查是否在職

		lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

		if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
			for (LoanBorMain tLoanBorMain : lLoanBorMain) {
				FacMain tFacMain = new FacMain();
				FacMainId tFacMainId = new FacMainId();
				tFacMainId.setCustNo(tLoanBorMain.getCustNo());
				tFacMainId.setFacmNo(tLoanBorMain.getFacmNo());
				tFacMain = facMainService.findById(tFacMainId);

				if (tFacMain != null) {
					if (tFacMain.getRepayCode() == 3) {
//						1.在職檔無此資料
//						2.制度別不屬於員工扣薪(0/2/3/5)
//						3.非車貸，制度別0/2才可設定員工薪
						CustMain tCustMain = new CustMain();
						tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo());
						if (tCustMain != null) {
							CdEmp tCdEmp = new CdEmp();
							tCdEmp = cdEmpService.findById(tCustMain.getEmpNo());
							if (tCdEmp != null) {
								if ("0".equals(tCdEmp.getAgType1()) || "2".equals(tCdEmp.getAgType1())
										|| "3".equals(tCdEmp.getAgType1()) || "5".equals(tCdEmp.getAgType1())) {
								} else {
									errCnt = errCnt + 1;
									setreport(tLoanBorMain, tCustMain, 2);
								}
								if ("0".equals(tCdEmp.getAgType1()) || "2".equals(tCdEmp.getAgType1())) {
								} else {
									errCnt = errCnt + 1;
									setreport(tLoanBorMain, tCustMain, 3);
								}
							} else {
								errCnt = errCnt + 1;
								setreport(tLoanBorMain, tCustMain, 1);
							}
						}
					}
				}
			}
		}
		
		if (errCnt == 0) {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		totaVo.putParam("OErrCnt", errCnt);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setreport(LoanBorMain tLoanBorMain, CustMain tCustMain, int errorFlag) {
		OccursList occursList = new OccursList();
//		OOCustNo OOFacmNo OOBormNo OOEmpName OOEmpNo OOEmpId OOErrMsg
//		OErrCnt

		int nameLength = 20;
		if (tCustMain.getCustName().length() < 20) {
			nameLength = tCustMain.getCustName().length();
		}

		occursList.putParam("OOCustNo", tLoanBorMain.getCustNo());
		occursList.putParam("OOFacmNo", tLoanBorMain.getFacmNo());
		occursList.putParam("OOBormNo", tLoanBorMain.getBormNo());
		switch (errorFlag) {
		case 1:
			occursList.putParam("OOEmpName", "");
			occursList.putParam("OOEmpNo", "");
			occursList.putParam("OOEmpId", "");
			occursList.putParam("OOErrMsg", "在職檔無此資料");
			break;
		case 2:
			occursList.putParam("OOEmpName", tCustMain.getCustName().substring(0, nameLength));
			occursList.putParam("OOEmpNo", tCustMain.getEmpNo());
			occursList.putParam("OOEmpId", tCustMain.getCustId());
			occursList.putParam("OOErrMsg", "制度別不屬於員工扣薪(0/2/3/5)");
			break;
		case 3:
			occursList.putParam("OOEmpName", tCustMain.getCustName().substring(0, nameLength));
			occursList.putParam("OOEmpNo", tCustMain.getEmpNo());
			occursList.putParam("OOEmpId", tCustMain.getCustId());
			occursList.putParam("OOErrMsg", "非車貸，制度別0/2才可設定員工薪");
			break;
		default:
			occursList.putParam("OOErrMsg", "");
			break;
		}

		totaVo.addOccursList(occursList);
	}
}