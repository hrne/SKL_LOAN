package com.st1.itx.trade.L5;

import java.util.ArrayList;
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
import com.st1.itx.util.common.PfCheckInsuranceCom;
import com.st1.itx.util.common.data.PfInsDetailVo;
import com.st1.itx.util.parse.Parse;

/**
 * Tita 
 * Kind=9,1 
 * CustNo=9,7 
 * FacmNo=9,3  
 * END=X,1
 */
/**
 * 房貸獎勵保費檢核檔查詢
 * 
 * @author st1
 *
 */
@Service("L5959")
@Scope("prototype")
public class L5959 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public PfInsCheckService pfInsCheckService;

	@Autowired
	Parse parse;

	@Autowired
	public PfCheckInsuranceCom pfCheckInsuranceCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5905 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iKind = this.parse.stringToInteger(titaVo.getParam("Kind"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		PfInsCheck tPfInsCheck = pfInsCheckService.findById(new PfInsCheckId(iKind, iCustNo, iFacmNo), titaVo);
		if (tPfInsCheck == null) {
			throw new LogicException(titaVo, "E0001", "房貸獎勵保費檢核檔"); // 查無資料
		}

		this.totaVo.putParam("OCreditSysNo", tPfInsCheck.getCreditSysNo());// 徵審系統案號(eLoan案件編號)
		this.totaVo.putParam("OCustId", tPfInsCheck.getCustId()); // 借款人身份證字號
		this.totaVo.putParam("OApplDate", tPfInsCheck.getApplDate()); // 借款書申請日
		this.totaVo.putParam("OInsDate", tPfInsCheck.getInsDate()); // 承保日
		this.totaVo.putParam("OInsNo", tPfInsCheck.getInsNo()); // 保單號碼
		this.totaVo.putParam("OCheckResul", tPfInsCheck.getCheckResult());// 檢核結果(Y/N)
		this.totaVo.putParam("OCheckWorkMonth", tPfInsCheck.getCheckWorkMonth()); // 檢核工作月
		// 拆解回應訊息為保單明細資料
		ArrayList<PfInsDetailVo> lPfInsDetailVo = pfCheckInsuranceCom.getInsDetailList(tPfInsCheck.getReturnMsg(),
				titaVo);
		if (lPfInsDetailVo != null && lPfInsDetailVo.size() > 0) {
			for (PfInsDetailVo detailVo : lPfInsDetailVo) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOApplication_date", detailVo.getApplication_date());// 承保日
				occursList.putParam("OOPolicy_no", detailVo.getPolicy_no());// 保單號碼
				occursList.putParam("OOPo_status_code", detailVo.getPo_status_code());// 狀況
				occursList.putParam("OOCustName", detailVo.getCustName());// 要保人
				occursList.putParam("OOBeginDate", detailVo.getBeginDate());// 始期
				occursList.putParam("OOFace_amt", detailVo.getFace_amt());// 保額
				occursList.putParam("OOHighest_loan", detailVo.getHighest_loan());// 已貸金額
				occursList.putParam("OOLoan_amt", detailVo.getLoan_amt());// 可貸金額
				occursList.putParam("OOExchage_val", detailVo.getExchage_val());// 匯率
				occursList.putParam("OOMode_prem_year", detailVo.getMode_prem_year());// 年繳化保費(NTD)
				occursList.putParam("OOInsurance_type_3", detailVo.getInsurance_type_3()); // 保險型態
				occursList.putParam("OORvl_values", detailVo.getRvl_values());// 帳戶價值
				occursList.putParam("OOCurrency_1", detailVo.getCurrency_1());// 幣別
				occursList.putParam("OONames_i", detailVo.getNames_i());// 被保人

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}