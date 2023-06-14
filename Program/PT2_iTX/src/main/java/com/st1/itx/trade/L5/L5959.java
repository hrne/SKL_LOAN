package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfInsCheckId;
import com.st1.itx.db.service.PfInsCheckService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CheckInsurance;
import com.st1.itx.util.common.data.CheckInsuranceVo;
import com.st1.itx.util.parse.Parse;;

@Service("L5959")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5959 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5959.class);

	@Autowired
	public PfInsCheckService pfInsCheckService;

	@Autowired
	public CheckInsurance checkInsurance;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5959 ");
		this.totaVo.init(titaVo);

		PfInsCheckId pfInsCheckId = new PfInsCheckId();

		int iKind = parse.stringToInteger(titaVo.getParam("Kind"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		pfInsCheckId.setKind(iKind);
		pfInsCheckId.setCustNo(iCustNo);
		pfInsCheckId.setFacmNo(iFacmNo);

		PfInsCheck pfInsCheck = pfInsCheckService.findById(pfInsCheckId, titaVo);
		CheckInsuranceVo checkVo = new CheckInsuranceVo();
		if (pfInsCheck == null) {
			throw new LogicException(titaVo, "E0001", "房貸獎勵保費檢核檔");
		} else {
			if (!"Y".equals(pfInsCheck.getCheckResult())) {
				checkVo.setCustId(pfInsCheck.getCustId());

				// very importance
				checkInsurance.setTxBuffer(this.txBuffer);
				checkVo = checkInsurance.checkInsurance(titaVo, checkVo);
			} else {
				checkVo.setSuccess(true);
				checkVo.setMsgRs(pfInsCheck.getReturnMsg());
				checkVo = checkInsurance.parseXml(checkVo);
			}
		}
//		! eLoan案件編號  
//		#oCreditSysNo=A,7,S
//
//		! 借款人身份證字號
//		#oCustId=X,10,S
//
//		! 借款書申請日
//		#oApplDate=D,7,S
//
//		! 承保日 
//		#oInsDate=D,7,S
//
//		! 保單號碼    
//		#oInsNo=X,15,S
//
//		! 檢核結果
//		#oCheckResult=X,1,S
//
//		! 檢核工作月 
//		#oCheckWorkMonth=A,5,S

		this.totaVo.putParam("oCreditSysNo", pfInsCheck.getCreditSysNo());
		this.totaVo.putParam("oCustId", pfInsCheck.getCustId());
		this.totaVo.putParam("oApplDate", pfInsCheck.getApplDate());
		this.totaVo.putParam("oInsDate", pfInsCheck.getInsDate());
		this.totaVo.putParam("oInsNo", pfInsCheck.getInsNo());
		this.totaVo.putParam("oCheckResult", pfInsCheck.getCheckResult());
		if (pfInsCheck.getCheckWorkMonth() > 0) {
			this.totaVo.putParam("oCheckWorkMonth", pfInsCheck.getCheckWorkMonth() - 191100);
		} else {
			this.totaVo.putParam("oCheckWorkMonth", 0);
		}

		if (checkVo.isSuccess() && !checkVo.getDetail().isEmpty()) {
			for (HashMap<String, String> map : checkVo.getDetail()) {
				// if (highest_loan == null) -> 0 else -> [loan_amt] + [highest_loan]

				OccursList occursList = new OccursList();

				occursList.putParam("oPolicyNo", map.get("policy_no"));
				occursList.putParam("oApplicationDate", map.get("application_date")); // date
				occursList.putParam("oPoStatusCode", map.get("po_status_code"));
				occursList.putParam("oNamesO", map.get("names_o"));
				occursList.putParam("oIssueDate", map.get("issue_date")); // date
				occursList.putParam("oFaceAmt", map.get("face_amt"));
				occursList.putParam("oHighestLoan", map.get("highest_loan"));
				occursList.putParam("oLoanBal", map.get("loan_bal"));
				occursList.putParam("oExchageVal", map.get("exchage_val"));
				occursList.putParam("oModePremYear", map.get("mode_prem_year"));
				occursList.putParam("oInsuranceType", map.get("insurance_type_3"));
				occursList.putParam("oRvlValues", map.get("rvl_values"));
				occursList.putParam("oCurrency", map.get("currency_1"));
				occursList.putParam("oNamesI", map.get("names_i"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}