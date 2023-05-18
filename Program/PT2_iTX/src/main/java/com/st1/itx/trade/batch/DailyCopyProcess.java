package com.st1.itx.trade.batch;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * DailyCopyProcess
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("DailyCopyProcess")
@Scope("prototype")
public class DailyCopyProcess extends TradeBuffer {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("DailyCopyProcess start...");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		String tlrNo = titaVo.getTlrNo();
		String targetEnv = titaVo.getParam(ContentName.dataBase);

		this.info("DailyCopyProcess tbsdyf = " + tbsdyf);
		this.info("DailyCopyProcess tlrNo = " + tlrNo);
		this.info("DailyCopyProcess targetEnv = " + targetEnv);


		
		// 關閉全部ForeignKey
		this.info("DailyCopyProcess 關閉全部ForeignKey ...");
		sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 0, titaVo);
		this.batchTransaction.commit();

		// 複製資料-代碼檔
		this.info("DailyCopyProcess Usp_Cp_CdAcBook_Ins ...");
		sJobMainService.Usp_Cp_CdAcBook_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdAcCode_Ins ...");
		sJobMainService.Usp_Cp_CdAcCode_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdAoDept_Ins ...");
		sJobMainService.Usp_Cp_CdAoDept_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdAppraisalCompany_Ins ...");
		sJobMainService.Usp_Cp_CdAppraisalCompany_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdAppraiser_Ins ...");
		sJobMainService.Usp_Cp_CdAppraiser_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdArea_Ins ...");
		sJobMainService.Usp_Cp_CdArea_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBank_Ins ...");
		sJobMainService.Usp_Cp_CdBank_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBankOld_Ins ...");
		sJobMainService.Usp_Cp_CdBankOld_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBaseRate_Ins ...");
		sJobMainService.Usp_Cp_CdBaseRate_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBcm_Ins ...");
		sJobMainService.Usp_Cp_CdBcm_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBonus_Ins ...");
		sJobMainService.Usp_Cp_CdBonus_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBonusCo_Ins ...");
		sJobMainService.Usp_Cp_CdBonusCo_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBranch_Ins ...");
		sJobMainService.Usp_Cp_CdBranch_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBranchGroup_Ins ...");
		sJobMainService.Usp_Cp_CdBranchGroup_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBudget_Ins ...");
		sJobMainService.Usp_Cp_CdBudget_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdBuildingCost_Ins ...");
		sJobMainService.Usp_Cp_CdBuildingCost_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdCashFlow_Ins ...");
		sJobMainService.Usp_Cp_CdCashFlow_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdCity_Ins ...");
		sJobMainService.Usp_Cp_CdCity_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdCl_Ins ...");
		sJobMainService.Usp_Cp_CdCl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdCode_Ins ...");
		sJobMainService.Usp_Cp_CdCode_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdEmp_Ins ...");
		sJobMainService.Usp_Cp_CdEmp_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdGseq_Ins ...");
		sJobMainService.Usp_Cp_CdGseq_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdGuarantor_Ins ...");
		sJobMainService.Usp_Cp_CdGuarantor_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdIndustry_Ins ...");
		sJobMainService.Usp_Cp_CdIndustry_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdInsurer_Ins ...");
		sJobMainService.Usp_Cp_CdInsurer_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdLandOffice_Ins ...");
		sJobMainService.Usp_Cp_CdLandOffice_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdLandSection_Ins ...");
		sJobMainService.Usp_Cp_CdLandSection_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdLoanNotYet_Ins ...");
		sJobMainService.Usp_Cp_CdLoanNotYet_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdOverdue_Ins ...");
		sJobMainService.Usp_Cp_CdOverdue_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdPerformance_Ins ...");
		sJobMainService.Usp_Cp_CdPerformance_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdPfParms_Ins ...");
		sJobMainService.Usp_Cp_CdPfParms_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdReport_Ins ...");
		sJobMainService.Usp_Cp_CdReport_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdStock_Ins ...");
		sJobMainService.Usp_Cp_CdStock_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdSupv_Ins ...");
		sJobMainService.Usp_Cp_CdSupv_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdSyndFee_Ins ...");
		sJobMainService.Usp_Cp_CdSyndFee_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdVarValue_Ins ...");
		sJobMainService.Usp_Cp_CdVarValue_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CdWorkMonth_Ins ...");
		sJobMainService.Usp_Cp_CdWorkMonth_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		// 複製資料-客戶資料檔
		this.info("DailyCopyProcess Usp_Cp_CustMain_Ins ...");
		sJobMainService.Usp_Cp_CustMain_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustCross_Ins ...");
		sJobMainService.Usp_Cp_CustCross_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustDataCtrl_Ins ...");
		sJobMainService.Usp_Cp_CustDataCtrl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustFin_Ins ...");
		sJobMainService.Usp_Cp_CustFin_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustNotice_Ins ...");
		sJobMainService.Usp_Cp_CustNotice_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustomerAmlRating_Ins ...");
		sJobMainService.Usp_Cp_CustomerAmlRating_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustRmk_Ins ...");
		sJobMainService.Usp_Cp_CustRmk_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_CustTelNo_Ins ...");
		sJobMainService.Usp_Cp_CustTelNo_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		// 複製資料-額度資料檔
		this.info("DailyCopyProcess Usp_Cp_FacProd_Ins ...");
		sJobMainService.Usp_Cp_FacProd_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacProdAcctFee_Ins ...");
		sJobMainService.Usp_Cp_FacProdAcctFee_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacProdPremium_Ins ...");
		sJobMainService.Usp_Cp_FacProdPremium_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacProdStepRate_Ins ...");
		sJobMainService.Usp_Cp_FacProdStepRate_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacRelation_Ins ...");
		sJobMainService.Usp_Cp_FacRelation_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacShareAppl_Ins ...");
		sJobMainService.Usp_Cp_FacShareAppl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacShareLimit_Ins ...");
		sJobMainService.Usp_Cp_FacShareLimit_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacShareRelation_Ins ...");
		sJobMainService.Usp_Cp_FacShareRelation_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacCaseAppl_Ins ...");
		sJobMainService.Usp_Cp_FacCaseAppl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacMain_Ins ...");
		sJobMainService.Usp_Cp_FacMain_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FacClose_Ins ...");
		sJobMainService.Usp_Cp_FacClose_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		// 複製資料-放款資料檔
		this.info("DailyCopyProcess Usp_Cp_LoanBook_Ins ...");
		sJobMainService.Usp_Cp_LoanBook_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanBorMain_Ins ...");
		sJobMainService.Usp_Cp_LoanBorMain_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanBorTx_Ins ...");
		sJobMainService.Usp_Cp_LoanBorTx_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanCheque_Ins ...");
		sJobMainService.Usp_Cp_LoanCheque_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanCustRmk_Ins ...");
		sJobMainService.Usp_Cp_LoanCustRmk_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanNotYet_Ins ...");
		sJobMainService.Usp_Cp_LoanNotYet_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanOverdue_Ins ...");
		sJobMainService.Usp_Cp_LoanOverdue_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanRateChange_Ins ...");
		sJobMainService.Usp_Cp_LoanRateChange_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanSynd_Ins ...");
		sJobMainService.Usp_Cp_LoanSynd_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_LoanSyndItem_Ins ...");
		sJobMainService.Usp_Cp_LoanSyndItem_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		// 複製資料-其他
		this.info("DailyCopyProcess Usp_Cp_AcAcctCheck_Ins ...");
		sJobMainService.Usp_Cp_AcAcctCheck_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcAcctCheckDetail_Ins ...");
		sJobMainService.Usp_Cp_AcAcctCheckDetail_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcClose_Ins ...");
		sJobMainService.Usp_Cp_AcClose_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcDetail_Ins ...");
		sJobMainService.Usp_Cp_AcDetail_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AchAuthLog_Ins ...");
		sJobMainService.Usp_Cp_AchAuthLog_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AchAuthLogHistory_Ins ...");
		sJobMainService.Usp_Cp_AchAuthLogHistory_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AchDeductMedia_Ins ...");
		sJobMainService.Usp_Cp_AchDeductMedia_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcLoanInt_Ins ...");
		sJobMainService.Usp_Cp_AcLoanInt_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcLoanRenew_Ins ...");
		sJobMainService.Usp_Cp_AcLoanRenew_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcMain_Ins ...");
		sJobMainService.Usp_Cp_AcMain_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AcReceivable_Ins ...");
		sJobMainService.Usp_Cp_AcReceivable_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_AmlCustList_Ins ...");
		sJobMainService.Usp_Cp_AmlCustList_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_BankAuthAct_Ins ...");
		sJobMainService.Usp_Cp_BankAuthAct_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankDeductDtl_Ins ...");
		sJobMainService.Usp_Cp_BankDeductDtl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRelationCompany_Ins ...");
		sJobMainService.Usp_Cp_BankRelationCompany_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRelationFamily_Ins ...");
		sJobMainService.Usp_Cp_BankRelationFamily_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRelationSelf_Ins ...");
		sJobMainService.Usp_Cp_BankRelationSelf_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRelationSuspected_Ins ...");
		sJobMainService.Usp_Cp_BankRelationSuspected_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRemit_Ins ...");
		sJobMainService.Usp_Cp_BankRemit_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BankRmtf_Ins ...");
		sJobMainService.Usp_Cp_BankRmtf_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BatxCheque_Ins ...");
		sJobMainService.Usp_Cp_BatxCheque_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BatxDetail_Ins ...");
		sJobMainService.Usp_Cp_BatxDetail_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BatxHead_Ins ...");
		sJobMainService.Usp_Cp_BatxHead_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BatxOthers_Ins ...");
		sJobMainService.Usp_Cp_BatxOthers_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_BatxRateChange_Ins ...");
		sJobMainService.Usp_Cp_BatxRateChange_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClBuilding_Ins ...");
		sJobMainService.Usp_Cp_ClBuilding_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClBuildingOwner_Ins ...");
		sJobMainService.Usp_Cp_ClBuildingOwner_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClBuildingPublic_Ins ...");
		sJobMainService.Usp_Cp_ClBuildingPublic_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClBuildingReason_Ins ...");
		sJobMainService.Usp_Cp_ClBuildingReason_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClEva_Ins ...");
		sJobMainService.Usp_Cp_ClEva_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_ClFac_Ins ...");
		sJobMainService.Usp_Cp_ClFac_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClImm_Ins ...");
		sJobMainService.Usp_Cp_ClImm_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClImmRankDetail_Ins ...");
		sJobMainService.Usp_Cp_ClImmRankDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClLand_Ins ...");
		sJobMainService.Usp_Cp_ClLand_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClLandOwner_Ins ...");
		sJobMainService.Usp_Cp_ClLandOwner_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClLandReason_Ins ...");
		sJobMainService.Usp_Cp_ClLandReason_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClMain_Ins ...");
		sJobMainService.Usp_Cp_ClMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClMovables_Ins ...");
		sJobMainService.Usp_Cp_ClMovables_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_ClNoMap_Ins ...");
		sJobMainService.Usp_Cp_ClNoMap_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClOther_Ins ...");
		sJobMainService.Usp_Cp_ClOther_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_ClOtherRights_Ins ...");
		sJobMainService.Usp_Cp_ClOtherRights_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClOwnerRelation_Ins ...");
		sJobMainService.Usp_Cp_ClOwnerRelation_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClParking_Ins ...");
		sJobMainService.Usp_Cp_ClParking_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClParkingType_Ins ...");
		sJobMainService.Usp_Cp_ClParkingType_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ClStock_Ins ...");
		sJobMainService.Usp_Cp_ClStock_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollLaw_Ins ...");
		sJobMainService.Usp_Cp_CollLaw_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollLetter_Ins ...");
		sJobMainService.Usp_Cp_CollLetter_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollList_Ins ...");
		sJobMainService.Usp_Cp_CollList_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollListTmp_Ins ...");
		sJobMainService.Usp_Cp_CollListTmp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollMeet_Ins ...");
		sJobMainService.Usp_Cp_CollMeet_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollRemind_Ins ...");
		sJobMainService.Usp_Cp_CollRemind_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CollTel_Ins ...");
		sJobMainService.Usp_Cp_CollTel_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_CreditRating_Ins ...");
		sJobMainService.Usp_Cp_CreditRating_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_DailyLoanBal_Ins ...");
		sJobMainService.Usp_Cp_DailyLoanBal_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_EmpDeductDtl_Ins ...");
		sJobMainService.Usp_Cp_EmpDeductDtl_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_EmpDeductMedia_Ins ...");
		sJobMainService.Usp_Cp_EmpDeductMedia_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_EmpDeductSchedule_Ins ...");
		sJobMainService.Usp_Cp_EmpDeductSchedule_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_FinReportCashFlow_Ins ...");
		sJobMainService.Usp_Cp_FinReportCashFlow_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FinReportDebt_Ins ...");
		sJobMainService.Usp_Cp_FinReportDebt_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FinReportProfit_Ins ...");
		sJobMainService.Usp_Cp_FinReportProfit_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FinReportQuality_Ins ...");
		sJobMainService.Usp_Cp_FinReportQuality_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FinReportRate_Ins ...");
		sJobMainService.Usp_Cp_FinReportRate_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_FinReportReview_Ins ...");
		sJobMainService.Usp_Cp_FinReportReview_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ForeclosureFee_Ins ...");
		sJobMainService.Usp_Cp_ForeclosureFee_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ForeclosureFinished_Ins ...");
		sJobMainService.Usp_Cp_ForeclosureFinished_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_GraceCondition_Ins ...");
		sJobMainService.Usp_Cp_GraceCondition_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Guarantor_Ins ...");
		sJobMainService.Usp_Cp_Guarantor_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_GuildBuilders_Ins ...");
		sJobMainService.Usp_Cp_GuildBuilders_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_HlAreaData_Ins ...");
		sJobMainService.Usp_Cp_HlAreaData_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_HlAreaLnYg6Pt_Ins ...");
		sJobMainService.Usp_Cp_HlAreaLnYg6Pt_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_HlCusData_Ins ...");
		sJobMainService.Usp_Cp_HlCusData_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_HlEmpLnYg5Pt_Ins ...");
		sJobMainService.Usp_Cp_HlEmpLnYg5Pt_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_HlThreeDetail_Ins ...");
		sJobMainService.Usp_Cp_HlThreeDetail_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_HlThreeLaqhcp_Ins ...");
		sJobMainService.Usp_Cp_HlThreeLaqhcp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias34Ap_Ins ...");
		sJobMainService.Usp_Cp_Ias34Ap_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_Ias34Bp_Ins ...");
		sJobMainService.Usp_Cp_Ias34Bp_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_Ias34Cp_Ins ...");
		sJobMainService.Usp_Cp_Ias34Cp_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_Ias34Dp_Ins ...");
		sJobMainService.Usp_Cp_Ias34Dp_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_Ias34Ep_Ins ...");
		sJobMainService.Usp_Cp_Ias34Ep_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_Ias34Gp_Ins ...");
		sJobMainService.Usp_Cp_Ias34Gp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias39IntMethod_Ins ...");
		sJobMainService.Usp_Cp_Ias39IntMethod_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias39LGD_Ins ...");
		sJobMainService.Usp_Cp_Ias39LGD_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias39Loan34Data_Ins ...");
		sJobMainService.Usp_Cp_Ias39Loan34Data_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias39LoanCommit_Ins ...");
		sJobMainService.Usp_Cp_Ias39LoanCommit_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ias39Loss_Ins ...");
		sJobMainService.Usp_Cp_Ias39Loss_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ifrs9FacData_Ins ...");
		sJobMainService.Usp_Cp_Ifrs9FacData_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_Ifrs9LoanData_Ins ...");
		sJobMainService.Usp_Cp_Ifrs9LoanData_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_InnDocRecord_Ins ...");
		sJobMainService.Usp_Cp_InnDocRecord_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_InnFundApl_Ins ...");
		sJobMainService.Usp_Cp_InnFundApl_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_InnLoanMeeting_Ins ...");
		sJobMainService.Usp_Cp_InnLoanMeeting_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_InnReCheck_Ins ...");
		sJobMainService.Usp_Cp_InnReCheck_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_InsuComm_Ins ...");
		sJobMainService.Usp_Cp_InsuComm_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_InsuOrignal_Ins ...");
		sJobMainService.Usp_Cp_InsuOrignal_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_InsuRenew_Ins ...");
		sJobMainService.Usp_Cp_InsuRenew_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_InsuRenewMediaTemp_Ins ...");
		sJobMainService.Usp_Cp_InsuRenewMediaTemp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicAtomDetail_Ins ...");
		sJobMainService.Usp_Cp_JcicAtomDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicAtomMain_Ins ...");
		sJobMainService.Usp_Cp_JcicAtomMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB080_Ins ...");
		sJobMainService.Usp_Cp_JcicB080_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB085_Ins ...");
		sJobMainService.Usp_Cp_JcicB085_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB090_Ins ...");
		sJobMainService.Usp_Cp_JcicB090_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB091_Ins ...");
		sJobMainService.Usp_Cp_JcicB091_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB092_Ins ...");
		sJobMainService.Usp_Cp_JcicB092_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB093_Ins ...");
		sJobMainService.Usp_Cp_JcicB093_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB094_Ins ...");
		sJobMainService.Usp_Cp_JcicB094_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB095_Ins ...");
		sJobMainService.Usp_Cp_JcicB095_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB096_Ins ...");
		sJobMainService.Usp_Cp_JcicB096_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB201_Ins ...");
		sJobMainService.Usp_Cp_JcicB201_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB204_Ins ...");
		sJobMainService.Usp_Cp_JcicB204_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB207_Ins ...");
		sJobMainService.Usp_Cp_JcicB207_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB211_Ins ...");
		sJobMainService.Usp_Cp_JcicB211_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicB680_Ins ...");
		sJobMainService.Usp_Cp_JcicB680_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicMonthlyLoanData_Ins ...");
		sJobMainService.Usp_Cp_JcicMonthlyLoanData_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicRel_Ins ...");
		sJobMainService.Usp_Cp_JcicRel_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicZ040_Ins ...");
		sJobMainService.Usp_Cp_JcicZ040_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ040Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ040Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ041_Ins ...");
		sJobMainService.Usp_Cp_JcicZ041_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ041Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ041Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ042_Ins ...");
		sJobMainService.Usp_Cp_JcicZ042_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ042Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ042Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ043_Ins ...");
		sJobMainService.Usp_Cp_JcicZ043_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ043Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ043Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ044_Ins ...");
		sJobMainService.Usp_Cp_JcicZ044_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ044Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ044Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ045_Ins ...");
		sJobMainService.Usp_Cp_JcicZ045_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ045Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ045Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ046_Ins ...");
		sJobMainService.Usp_Cp_JcicZ046_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ046Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ046Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ047_Ins ...");
		sJobMainService.Usp_Cp_JcicZ047_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ047Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ047Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ048_Ins ...");
		sJobMainService.Usp_Cp_JcicZ048_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ048Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ048Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ049_Ins ...");
		sJobMainService.Usp_Cp_JcicZ049_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ049Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ049Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ050_Ins ...");
		sJobMainService.Usp_Cp_JcicZ050_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ050Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ050Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ051_Ins ...");
		sJobMainService.Usp_Cp_JcicZ051_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ051Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ051Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ052_Ins ...");
		sJobMainService.Usp_Cp_JcicZ052_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ052Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ052Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ053_Ins ...");
		sJobMainService.Usp_Cp_JcicZ053_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ053Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ053Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ054_Ins ...");
		sJobMainService.Usp_Cp_JcicZ054_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ054Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ054Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ055_Ins ...");
		sJobMainService.Usp_Cp_JcicZ055_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ055Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ055Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ056_Ins ...");
		sJobMainService.Usp_Cp_JcicZ056_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ056Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ056Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ060_Ins ...");
		sJobMainService.Usp_Cp_JcicZ060_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ060Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ060Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ061_Ins ...");
		sJobMainService.Usp_Cp_JcicZ061_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ061Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ061Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ062_Ins ...");
		sJobMainService.Usp_Cp_JcicZ062_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ062Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ062Log_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ063_Ins ...");
		sJobMainService.Usp_Cp_JcicZ063_Ins(tlrNo, titaVo);

		this.info("DailyCopyProcess Usp_Cp_JcicZ063Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ063Log_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JcicZ440_Ins ...");
		sJobMainService.Usp_Cp_JcicZ440_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ440Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ442_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ442Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ443_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ443Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ444_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ444Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ446_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ446Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ447_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ447Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ448_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ448Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ450_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ450Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ451_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ451Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ454_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ454Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ570_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ570Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ571_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ571Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ572_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ572Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ573_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ573Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ574_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ574Log_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_JcicZ575_Ins(tlrNo, titaVo);
		this.info("DailyCopyProcess Usp_Cp_JcicZ575Log_Ins ...");
		sJobMainService.Usp_Cp_JcicZ575Log_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_JobDetail_Ins ...");
		sJobMainService.Usp_Cp_JobDetail_Ins(tlrNo, titaVo);
		this.info("DailyCopyProcess Usp_Cp_JobMain_Ins ...");
		sJobMainService.Usp_Cp_JobMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Ap_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Ap_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Bp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Bp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Cp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Cp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Dp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Dp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Fp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Fp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Gp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Gp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Hp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Hp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Ip_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Ip_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIfrs9Jp_Ins ...");
		sJobMainService.Usp_Cp_LoanIfrs9Jp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_LoanIntDetail_Ins ...");
		sJobMainService.Usp_Cp_LoanIntDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_MlaundryChkDtl_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MlaundryDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MlaundryParas_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MlaundryRecord_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_MonthlyLM003_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM028_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM032_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM036Portfolio_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM052AssetClass_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM052LoanAsset_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM052Loss_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_MonthlyLM052Ovdu_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_MonthlyLoanBal_Ins ...");
		sJobMainService.Usp_Cp_MonthlyLoanBal_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_NegAppr_Ins ...");
		sJobMainService.Usp_Cp_NegAppr_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegAppr01_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegAppr02_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegFinAcct_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegFinShare_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegFinShareLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_NegMain_Ins ...");
		sJobMainService.Usp_Cp_NegMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_NegQueryCust_Ins(tlrNo, titaVo);
		this.info("DailyCopyProcess Usp_Cp_NegTrans_Ins ...");
		sJobMainService.Usp_Cp_NegTrans_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_PfBsDetail_Ins ...");
		sJobMainService.Usp_Cp_PfBsDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfBsDetailAdjust_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfBsOfficer_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfCoOfficer_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfCoOfficerLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfDeparment_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_PfDetail_Ins ...");
		sJobMainService.Usp_Cp_PfDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfInsCheck_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfIntranetAdjust_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_PfItDetail_Ins ...");
		sJobMainService.Usp_Cp_PfItDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfItDetailAdjust_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_PfReward_Ins ...");
		sJobMainService.Usp_Cp_PfReward_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfRewardMedia_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PfSpecParms_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_PostAuthLog_Ins ...");
		sJobMainService.Usp_Cp_PostAuthLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PostAuthLogHistory_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_PostDeductMedia_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_ReltMain_Ins ...");
		sJobMainService.Usp_Cp_ReltMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_RepayActChangeLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_RptJcic_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_RptRelationCompany_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_RptRelationFamily_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_RptRelationSelf_Ins(tlrNo, titaVo);
		sJobMainService.Usp_Cp_RptSubCom_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_SlipEbsRecord_Ins ...");
		sJobMainService.Usp_Cp_SlipEbsRecord_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_SlipMedia_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_SlipMedia2022_Ins ...");
		sJobMainService.Usp_Cp_SlipMedia2022_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_SpecInnReCheck_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_StgCdEmp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_SystemParas_Ins ...");
		sJobMainService.Usp_Cp_SystemParas_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		sJobMainService.Usp_Cp_TbJcicMu01_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TbJcicW020_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TbJcicZZ50_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		this.info("DailyCopyProcess Usp_Cp_TxAgent_Ins ...");
		sJobMainService.Usp_Cp_TxAgent_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAmlCredit_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAmlLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAmlNotice_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAmlRating_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxApLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxApLogList_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxArchiveTable_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxArchiveTableLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAttachment_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAttachType_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_TxAuthGroup_Ins ...");
		sJobMainService.Usp_Cp_TxAuthGroup_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAuthority_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxAuthorize_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxBizDate_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxControl_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxCruiser_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxCurr_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxDataLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxErrCode_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxFile_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxFlow_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_TxHoliday_Ins ...");
		sJobMainService.Usp_Cp_TxHoliday_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxInquiry_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxLock_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxPrinter_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxProcess_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxRecord_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_TxTeller_Ins ...");
		sJobMainService.Usp_Cp_TxTeller_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxTellerAuth_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxTellerTest_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxTemp_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxToDoDetail_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxToDoDetailReserve_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxToDoMain_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_TxTranCode_Ins ...");
		sJobMainService.Usp_Cp_TxTranCode_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		sJobMainService.Usp_Cp_TxUnLock_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		
		this.info("DailyCopyProcess Usp_Cp_UspErrorLog_Ins ...");
		sJobMainService.Usp_Cp_UspErrorLog_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		
		this.info("DailyCopyProcess Usp_Cp_YearlyHouseLoanInt_Ins ...");
		sJobMainService.Usp_Cp_YearlyHouseLoanInt_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();
		this.info("DailyCopyProcess Usp_Cp_YearlyHouseLoanIntCheck_Ins ...");
		sJobMainService.Usp_Cp_YearlyHouseLoanIntCheck_Ins(tlrNo, titaVo);
		this.batchTransaction.commit();

		// 開啟全部ForeignKey
		this.info("DailyCopyProcess 開啟全部ForeignKey ...");
		sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 1, titaVo);
		this.batchTransaction.commit();
		
		this.info("DailyCopyProcess Finished.");
		return null;
	}
}