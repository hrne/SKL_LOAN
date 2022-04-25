package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;

/**
 * 每日資料庫複製<BR>
 * <BR>
 * 本支程式發動複製Table的StoredProcedure<BR>
 * 每日將Online環境的資料複製到日報環境<BR>
 * <BR>
 * 月底營業日時，多複製一次到月報環境<BR>
 * <BR>
 * 應注意事項：<BR>
 * 1.新增、修改、刪除Table時，應同步上版至所有環境(Online/Day/Month/History)<BR>
 * 2.修改Table欄位時，應檢視負責複製該Table的StoredProcedure是否會受影響<BR>
 * 若有，需一併修改StoredProcedure並與修改Table之語法同時上版<BR>
 * 3.新增Table時，應製作複製該Table的StoredProcedure，並修改本支java程式，增加發動該支StoredProcedure的語句<BR>
 * 新增的StoredProcedure與修改後的本支程式需與Table同時上版<BR>
 * 4.刪除Table時，應製作drop procedure的語法，並與刪除Talbe的語法同時上版<BR>
 * <BR>
 * 註:相關的StoredProcedure都以Usp_Cp_開頭
 * 
 * @author ST1-Chih Wei
 * @version 1.0.0
 */
@Service("DailyCopy")
@Scope("step")
public class DailyCopy extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("DailyCopy 日報環境開始複製");

		TitaVo tempTitaVo = (TitaVo) this.titaVo.clone();

		// 複製到日報環境
		tempTitaVo.putParam(ContentName.dataBase, ContentName.onDay);

		doCopy(tempTitaVo);
		this.info("DailyCopy 日報環境複製完成");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		this.info("DailyCopy tbsdyf : " + tbsdyf);
		this.info("DailyCopy mfbsdyf : " + mfbsdyf);

		// 每月月底日才執行
		if (tbsdyf == mfbsdyf) {
			this.info("DailyCopy 月報環境開始複製");
			tempTitaVo.putParam(ContentName.dataBase, ContentName.onMon);
			// 複製到月報環境
			doCopy(tempTitaVo);
			this.info("DailyCopy 月報環境複製完成");
		}

		this.info("DailyCopy exit.");
	}

	private void doCopy(TitaVo tempTitaVo) throws LogicException {
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		String tlrNo = tempTitaVo.getTlrNo();

		String targetEnv = tempTitaVo.getParam(ContentName.dataBase);
		this.info("DailyCopy doCopy 目標環境: " + tempTitaVo.getParam(ContentName.dataBase));

		// 防呆
		if (targetEnv == null || targetEnv.isEmpty() || targetEnv.equals(ContentName.onLine)) {
			this.info("DailyCopy doCopy 目標環境是onLine 不可複製");
			return;
		}

		// 關閉全部ForeignKey
		sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 0, tempTitaVo);

		// 複製資料
		sJobMainService.Usp_Cp_CdCode_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcAcctCheck_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcAcctCheckDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcClose_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AchAuthLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AchAuthLogHistory_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AchDeductMedia_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcLoanInt_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcLoanRenew_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AcReceivable_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_AmlCustList_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankAuthAct_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankDeductDtl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRelationCompany_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRelationFamily_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRelationSelf_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRelationSuspected_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRemit_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BankRmtf_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BatxCheque_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BatxDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BatxHead_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BatxOthers_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_BatxRateChange_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdAcBook_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdAcCode_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdAoDept_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdAppraisalCompany_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdAppraiser_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdArea_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBank_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBankOld_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBaseRate_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBcm_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBonus_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBonusCo_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBranch_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBranchGroup_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBudget_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdBuildingCost_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdCashFlow_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdCity_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdCl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdCode_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdEmp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdGseq_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdGuarantor_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdIndustry_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdInsurer_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdLandOffice_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdLandSection_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdLoanNotYet_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdOverdue_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdPerformance_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdPfParms_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdReport_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdStock_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdSupv_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdSyndFee_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdVarValue_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CdWorkMonth_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClBuilding_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClBuildingOwner_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClBuildingPublic_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClBuildingReason_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClEva_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClFac_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClImm_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClImmRankDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClLand_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClLandOwner_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClLandReason_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClMovables_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClNoMap_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClOther_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClOtherRights_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClOwnerRelation_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClParking_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClParkingType_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ClStock_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollLaw_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollLetter_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollList_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollListTmp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollMeet_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollRemind_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CollTel_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CreditRating_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustCross_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustDataCtrl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustFin_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustNotice_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustomerAmlRating_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustRmk_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_CustTelNo_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_DailyLoanBal_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_EmpDeductDtl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_EmpDeductMedia_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_EmpDeductSchedule_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacCaseAppl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacClose_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacProd_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacProdAcctFee_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacProdPremium_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacProdStepRate_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacRelation_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacShareAppl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacShareLimit_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FacShareRelation_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportCashFlow_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportDebt_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportProfit_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportQuality_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportRate_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_FinReportReview_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ForeclosureFee_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ForeclosureFinished_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_GraceCondition_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Guarantor_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_GuildBuilders_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlAreaData_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlAreaLnYg6Pt_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlCusData_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlEmpLnYg5Pt_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlThreeDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_HlThreeLaqhcp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Ap_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Bp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Cp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Dp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Ep_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias34Gp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias39IntMethod_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias39LGD_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias39Loan34Data_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias39LoanCommit_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ias39Loss_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ifrs9FacData_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_Ifrs9LoanData_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InnDocRecord_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InnFundApl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InnLoanMeeting_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InnReCheck_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InsuComm_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InsuOrignal_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InsuRenew_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_InsuRenewMediaTemp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicAtomDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicAtomMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB080_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB085_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB090_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB091_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB092_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB093_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB094_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB095_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB096_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB201_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB204_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB207_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB211_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicB680_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicMonthlyLoanData_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicRel_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ040_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ040Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ041_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ041Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ042_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ042Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ043_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ043Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ044_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ044Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ045_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ045Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ046_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ046Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ047_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ047Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ048_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ048Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ049_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ049Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ050_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ050Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ051_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ051Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ052_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ052Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ053_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ053Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ054_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ054Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ055_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ055Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ056_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ056Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ060_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ060Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ061_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ061Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ062_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ062Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ063_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ063Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ440_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ440Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ442_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ442Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ443_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ443Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ444_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ444Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ446_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ446Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ447_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ447Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ448_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ448Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ450_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ450Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ451_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ451Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ454_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ454Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ570_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ570Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ571_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ571Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ572_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ572Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ573_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ573Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ574_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ574Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ575_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JcicZ575Log_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JobDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_JobMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanBook_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanBorMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanBorTx_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanCheque_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanCustRmk_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Ap_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Bp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Cp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Dp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Fp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Gp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Hp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Ip_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIfrs9Jp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanIntDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanNotYet_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanOverdue_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanRateChange_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanSynd_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_LoanSyndItem_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MlaundryChkDtl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MlaundryDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MlaundryParas_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MlaundryRecord_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM003_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM028_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM032_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM036Portfolio_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM052AssetClass_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM052LoanAsset_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM052Loss_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLM052Ovdu_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_MonthlyLoanBal_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegAppr_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegAppr01_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegAppr02_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegFinAcct_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegFinShare_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegFinShareLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegQueryCust_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_NegTrans_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfBsDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfBsDetailAdjust_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfBsOfficer_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfCoOfficer_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfCoOfficerLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfDeparment_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfInsCheck_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfIntranetAdjust_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfItDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfItDetailAdjust_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfReward_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfRewardMedia_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PfSpecParms_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PostAuthLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PostAuthLogHistory_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_PostDeductMedia_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_ReltMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RepayActChangeLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RptJcic_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RptRelationCompany_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RptRelationFamily_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RptRelationSelf_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_RptSubCom_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_SlipEbsRecord_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_SlipMedia_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_SlipMedia2022_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_SpecInnReCheck_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_StgCdEmp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_SystemParas_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TbJcicMu01_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TbJcicW020_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TbJcicZZ50_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAgent_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAmlCredit_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAmlLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAmlNotice_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAmlRating_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxApLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxApLogList_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxArchiveTable_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxArchiveTableLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAttachment_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAttachType_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAuthGroup_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAuthority_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxAuthorize_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxBizDate_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxControl_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxCruiser_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxCurr_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxDataLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxErrCode_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxFile_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxFlow_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxHoliday_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxInquiry_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxLock_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxPrinter_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxProcess_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxRecord_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxTeller_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxTellerAuth_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxTellerTest_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxTemp_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxToDoDetail_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxToDoDetailReserve_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxToDoMain_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxTranCode_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_TxUnLock_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_UspErrorLog_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_YearlyHouseLoanInt_Ins(tlrNo, tempTitaVo);
		sJobMainService.Usp_Cp_YearlyHouseLoanIntCheck_Ins(tlrNo, tempTitaVo);

		// 開啟全部ForeignKey
		sJobMainService.Usp_Cp_ForeignKeyControl_Upd(tbsdyf, tlrNo, 1, tempTitaVo);
	}
}