package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcDetailRepositoryMon extends JpaRepository<AcDetail, AcDetailId> {

	// RelDy = ,AND RelTxseq =
	public Slice<AcDetail> findAllByRelDyIsAndRelTxseqIsOrderByAcSeqAsc(int relDy_0, String relTxseq_1, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND AcDate >= ,AND AcDate <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8,
			Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND SumNo >= ,AND SumNo <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String sumNo_5, String sumNo_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND TitaTlrNo >= ,AND TitaTlrNo <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaTlrNo_5, String titaTlrNo_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND TitaBatchNo >= ,AND TitaBatchNo <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaBatchNo_5, String titaBatchNo_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND DscptCode >= ,AND DscptCode <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String dscptCode_5, String dscptCode_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND SlipBatNo >= ,AND SlipBatNo <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, int slipBatNo_5, int slipBatNo_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <= ,AND TitaSecNo >= ,AND TitaSecNo <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(
			String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaSecNo_5, String titaSecNo_6, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode
	// <=
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String branchNo_0,
			String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND CustNo =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndCustNoIsOrderByRelTxseqAscAcSeqAsc(String branchNo_0, String currencyCode_1, int acDate_2, int custNo_3,
			Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaTlrNo =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTlrNoIsOrderByRelTxseqAscAcSeqAsc(String branchNo_0, String currencyCode_1, int acDate_2, String titaTlrNo_3,
			Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaBatchNo =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByRelTxseqAscAcSeqAsc(String branchNo_0, String currencyCode_1, int acDate_2, String titaBatchNo_3,
			Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaTxCd =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTxCdIsOrderByRelTxseqAscAcSeqAsc(String branchNo_0, String currencyCode_1, int acDate_2, String titaTxCd_3,
			Pageable pageable);

	// AcDate = ,AND SlipBatNo =
	public Slice<AcDetail> findAllByAcDateIsAndSlipBatNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(int acDate_0, int slipBatNo_1, Pageable pageable);

	// AcctCode = ,AND CustNo = ,AND RvNo = ,
	public Slice<AcDetail> findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByRelDyAscRelTxseqAscAcSeqAsc(String acctCode_0, int custNo_1, String rvNo_2, Pageable pageable);

	// AcDate = ,AND TitaKinbr = ,AND TitaTlrNo = ,AND TitaTxtNo =
	public Slice<AcDetail> findAllByAcDateIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByAcSeqAsc(int acDate_0, String titaKinbr_1, String titaTlrNo_2, int titaTxtNo_3, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaBatchNo =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String branchNo_0, String currencyCode_1, int acDate_2,
			String titaBatchNo_3, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo = ,AND FacmNo = ,AND
	// AcDate >= ,AND AcDate <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoIsAndFacmNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int custNo_7, int facmNo_8, int acDate_9,
			int acDate_10, Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcctFlag = ,AND AcDate >= ,AND
	// AcDate <=
	public Slice<AcDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcctFlagIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateDescSlipNoDesc(int custNo_0, int facmNo_1, int bormNo_2,
			int acctFlag_3, int acDate_4, int acDate_5, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND SumNo >= ,AND SumNo <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaTlrNo >= ,AND TitaTlrNo
	// <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaBatchNo >= ,AND
	// TitaBatchNo <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND DscptCode >= ,AND DscptCode
	// <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND SlipBatNo >= ,AND SlipBatNo
	// <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaSecNo >= ,AND TitaSecNo
	// <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String rvNo_7, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND SumNo >= ,AND SumNo <= ,AND
	// RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8, String rvNo_9,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaTlrNo >= ,AND TitaTlrNo
	// <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8,
			String rvNo_9, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaBatchNo >= ,AND
	// TitaBatchNo <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8,
			String rvNo_9, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND DscptCode >= ,AND DscptCode
	// <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8,
			String rvNo_9, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND SlipBatNo >= ,AND SlipBatNo
	// <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8, String rvNo_9,
			Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND TitaSecNo >= ,AND TitaSecNo
	// <= ,AND RvNo %
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8,
			String rvNo_9, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND
	// AcNoCode = ,AND AcSubCode = ,AND AcDate >= ,AND AcDate <=
	public Slice<AcDetail> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(
			String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, int acDate_6, int acDate_7, Pageable pageable);

	// AcctCode = ,AND CustNo = ,AND RvNo = ,AND AcDate =
	public Optional<AcDetail> findTopByAcctCodeIsAndCustNoIsAndRvNoIsAndAcDateIs(String acctCode_0, int custNo_1, String rvNo_2, int acDate_3);

	// AcDate = ,AND RelTxseq =
	public Slice<AcDetail> findAllByAcDateIsAndRelTxseqIsOrderByAcSeqAsc(int acDate_0, String relTxseq_1, Pageable pageable);

	// AcDate = ,AND SlipNo =
	public Slice<AcDetail> findAllByAcDateIsAndSlipNoIsOrderByAcSeqAsc(int acDate_0, int slipNo_1, Pageable pageable);

	// RelDy = ,AND SlipNo =
	public Slice<AcDetail> findAllByRelDyIsAndSlipNoIsOrderByAcSeqAsc(int relDy_0, int slipNo_1, Pageable pageable);

	// BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaBatchNo =
	public Slice<AcDetail> findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderBySlipNoAscRelTxseqAscAcSeqAsc(String branchNo_0, String currencyCode_1, int acDate_2,
			String titaBatchNo_3, Pageable pageable);

	// AcDate = ,AND SlipBatNo =
	public Slice<AcDetail> findAllByAcDateIsAndSlipBatNoIs(int acDate_0, int slipBatNo_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AcDetail> findByAcDetailId(AcDetailId acDetailId);

}
