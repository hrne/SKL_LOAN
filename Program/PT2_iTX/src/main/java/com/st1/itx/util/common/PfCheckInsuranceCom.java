package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfInsCheckId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.PfInsCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.CheckInsuranceVo;
import com.st1.itx.util.common.data.PfInsDetailVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 房貸獎勵保費檢核機制<BR>
 * 1.check 房貸獎勵保費檢核 call by 產生獎勵金發放媒體批次程式(L5510Batch,L5511Batch,L5512Batch)<BR>
 * 2.getInsDetailList 拆解回應訊息為保單明細資料 call by 房貸獎勵保費檢核檔查詢<BR>
 * 
 * @author st1
 *
 */
@Component("pfCheckInsuranceCom")
@Scope("prototype")
public class PfCheckInsuranceCom extends TradeBuffer {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	EmployeeCom employeeCom;

	@Autowired
	PfInsCheckService pfInsCheckService;

	@Autowired
	FacMainService facMainService;

	@Autowired
	FacCaseApplService facCaseApplService;

	@Autowired
	CustMainService custMainService;

	@Autowired
	CheckInsurance checkInsurance;

	private CheckInsuranceVo checkVo = new CheckInsuranceVo();
	private PfInsCheckId tPfInsCheckId = new PfInsCheckId();
	private PfInsCheck tPfInsCheck = new PfInsCheck();
	private String checkResult = "N";
	private int insDate = 0;
	private String insNo = null;
	private List<PfInsDetailVo> lDetailVo = new ArrayList<>();

	/**
	 * 抓取核心系統保單資料，產生房貸獎勵保費檢核結果
	 * 
	 * @param iKind           0:換算業績、業務報酬 1:介紹獎金、協辦獎金 2:介紹人加碼獎勵津貼
	 * @param iCustNo         CustNo
	 * @param iFacmNo         FacmNo
	 * @param iPerfWorkMonth  業績工作月
	 * @param iCheckWorkMonth 檢核工作月
	 * @param titaVo          TitaVo
	 * @return PfInsCheck
	 * @throws LogicException ...
	 */
	public PfInsCheck check(int iKind, int iCustNo, int iFacmNo, int iPerfWorkMonth, int iCheckWorkMonth, TitaVo titaVo)
			throws LogicException {
		this.info("PfInsCheck check ..... Kind = " + iKind + "," + iCustNo + "-" + iFacmNo + "," + iCheckWorkMonth);

		String returnMsg = null;
		boolean isInsert = false;

		tPfInsCheck = pfInsCheckService.findPerfWorkMonthFirst(iPerfWorkMonth, iKind, iCustNo, iFacmNo, titaVo);
		//如前工作月的保費檢核已是Y者(已追回),則本月份不再做保費檢核
		if (tPfInsCheck != null && tPfInsCheck.getPerfWorkMonth() != iCheckWorkMonth && "Y".equals(tPfInsCheck.getCheckResult())) {
			return tPfInsCheck;
		}
		//如本工作月已做保費檢核則本月份不再做保費檢核
		if (tPfInsCheck != null && tPfInsCheck.getCheckWorkMonth() == iCheckWorkMonth) {
			return tPfInsCheck;
		}

		// hold房貸獎勵保費檢核檔
		tPfInsCheckId.setKind(iKind);
		tPfInsCheckId.setCustNo(iCustNo);
		tPfInsCheckId.setFacmNo(iFacmNo);
		tPfInsCheckId.setPerfWorkMonth(iPerfWorkMonth);
		tPfInsCheckId.setCheckWorkMonth(iCheckWorkMonth);
		tPfInsCheck = pfInsCheckService.holdById(tPfInsCheckId, titaVo);

		// 新增房貸獎勵保費檢核檔
		if (tPfInsCheck == null) {
			isInsert = true;
			tPfInsCheck = new PfInsCheck();
			tPfInsCheck.setPfInsCheckId(tPfInsCheckId);
			getCaseAppl(iCustNo, iFacmNo, titaVo); // 抓取案件資料
		}

		// 以借款書申請日與保費資料的承保日前後相對3個月，追回介紹獎金
		dateUtil.init();
		dateUtil.setDate_1(tPfInsCheck.getApplDate());
		dateUtil.setMons(-3);
		int startDate = dateUtil.getCalenderDay();

		dateUtil.init();
		dateUtil.setDate_1(tPfInsCheck.getApplDate());
		dateUtil.setMons(3);
		int endDate = dateUtil.getCalenderDay();

		// Query 核心系統保單資料
		returnMsg = queryCoreInsurance(tPfInsCheck, titaVo);

		// 拆解回應訊息為保單明細資料
		if (returnMsg != null && returnMsg.length() > 0) {
			getInsDetailList();
		}
		// 檢核保單明細資料
		if (lDetailVo != null && !lDetailVo.isEmpty()) {
			this.info("startDate=" + startDate + " endDate=" + endDate);
			for (PfInsDetailVo detailVo : lDetailVo) {
				this.info("detailVo=" + detailVo.toString());
				if (detailVo.getApplication_date() >= startDate && detailVo.getApplication_date() <= endDate) {
					insDate = detailVo.getApplication_date();
					insNo = detailVo.getPolicy_no();
					checkResult = "Y";
					break;
				}
			}
		}
		// 檢核結果放入房貸獎勵保費檢核檔
		tPfInsCheck.setKind(iKind);
		tPfInsCheck.setCustNo(iCustNo);
		tPfInsCheck.setFacmNo(iFacmNo);
		tPfInsCheck.setInsDate(insDate);// 承保日
		tPfInsCheck.setInsNo(insNo);// 保單號碼
		tPfInsCheck.setCheckResult(checkResult); // 檢核結果(Y/N)
		tPfInsCheck.setCheckWorkMonth(iCheckWorkMonth); // 檢核工作月
		tPfInsCheck.setReturnMsg(""); // 回應訊息
		tPfInsCheck.setReturnMsg2(""); // 回應訊息2
		tPfInsCheck.setReturnMsg3(""); // 回應訊息3
		tPfInsCheck.setReturnMsg(returnMsg.length() > 2000 ? returnMsg.substring(0, 2000) : returnMsg); // 回應訊息
		if (returnMsg.length() > 2000) {
			tPfInsCheck.setReturnMsg2(returnMsg.length() > 4000 ? returnMsg.substring(2000, 4000)
					: returnMsg.substring(2000, returnMsg.length())); // 回應訊息
			if (returnMsg.length() > 4000) {
				tPfInsCheck.setReturnMsg3(returnMsg.length() > 6000 ? returnMsg.substring(4000, 6000)
						: returnMsg.substring(4000, returnMsg.length())); // 回應訊息
			}
			if (returnMsg.length() > 6000) {
				this.info("returnMsg Drop length = " + returnMsg.length());
			}

		}

		// 更新房貸獎勵保費檢核檔
		if (isInsert) {
			try {
				pfInsCheckService.insert(tPfInsCheck, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005",
						"PfInsCheck insert " + tPfInsCheckId.toString() + e.getErrorMsg());
			}
		} else {
			try {
				pfInsCheckService.update(tPfInsCheck, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007",
						"PfInsCheck update " + tPfInsCheckId.toString() + e.getErrorMsg());
			}
		}

		return tPfInsCheck;

	}

	// 抓取案件資料
	private void getCaseAppl(int iCustNo, int iFacmNo, TitaVo titaVo) throws LogicException {
		FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", "額度主檔" + iCustNo + "-" + iFacmNo); // 查詢資料不存在
		}

		FacCaseAppl tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0001", "案件主檔" + tFacMain.getApplNo()); // 查詢資料不存在
		}

		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶主檔" + iCustNo); // 查詢資料不存在
		}

		tPfInsCheck.setCreditSysNo(tFacMain.getCreditSysNo());
		tPfInsCheck.setCustId(tCustMain.getCustId());
		tPfInsCheck.setApplDate(tFacCaseAppl.getApplDate());

	}

	// Query 核心系統保單資料
	private String queryCoreInsurance(PfInsCheck iPf, TitaVo titaVo) throws LogicException {
		checkInsurance.setTxBuffer(this.getTxBuffer());
		checkVo = new CheckInsuranceVo();
		checkVo.setCustId(iPf.getCustId());
		checkVo = checkInsurance.checkInsurance(titaVo, checkVo);
		this.info("MsgRs=" + checkVo.getMsgRs());
		return checkVo.getMsgRs();
	}

	private void getInsDetailList() {
		if (checkVo.isSuccess() && checkVo.getDetail() != null && !checkVo.getDetail().isEmpty()) {
			lDetailVo = new ArrayList<>();
			for (Map<String, String> dtl : checkVo.getDetail()) {
				PfInsDetailVo pfInsDetailVo = new PfInsDetailVo();
				pfInsDetailVo.setApplication_date(Integer.parseInt(dtl.get("application_date")));
				pfInsDetailVo.setPolicy_no(dtl.get("policy_no"));
				pfInsDetailVo.setPo_status_code(dtl.get("po_status_code"));
				lDetailVo.add(pfInsDetailVo);
			}
		}
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return new ArrayList<>();
	}

	private void init() {
		checkVo = new CheckInsuranceVo();
		tPfInsCheckId = new PfInsCheckId();
		tPfInsCheck = new PfInsCheck();
		checkResult = "N";
		insDate = 0;
		insNo = null;
		lDetailVo = new ArrayList<>();
	}

}