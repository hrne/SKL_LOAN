CREATE OR REPLACE PROCEDURE "Usp_Cp_AcDetail_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcDetail" DROP STORAGE';

  INSERT INTO "AcDetail" (
    "RelDy",
    "RelTxseq",
    "AcSeq",
    "AcDate",
    "BranchNo",
    "CurrencyCode",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcctCode",
    "DbCr",
    "TxAmt",
    "EntAc",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RvNo",
    "AcctFlag",
    "ReceivableFlag",
    "AcBookFlag",
    "AcBookCode",
    "AcSubBookCode",
    "SumNo",
    "DscptCode",
    "SlipNote",
    "SlipBatNo",
    "SlipNo",
    "TitaHCode",
    "TitaKinbr",
    "TitaTlrNo",
    "TitaTxtNo",
    "TitaTxCd",
    "TitaSecNo",
    "TitaBatchNo",
    "TitaBatchSeq",
    "TitaSupNo",
    "TitaRelCd",
    "SlipSumNo",
    "JsonFields",
    "MediaSlipNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "RelDy",
    "RelTxseq",
    "AcSeq",
    "AcDate",
    "BranchNo",
    "CurrencyCode",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcctCode",
    "DbCr",
    "TxAmt",
    "EntAc",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RvNo",
    "AcctFlag",
    "ReceivableFlag",
    "AcBookFlag",
    "AcBookCode",
    "AcSubBookCode",
    "SumNo",
    "DscptCode",
    "SlipNote",
    "SlipBatNo",
    "SlipNo",
    "TitaHCode",
    "TitaKinbr",
    "TitaTlrNo",
    "TitaTxtNo",
    "TitaTxCd",
    "TitaSecNo",
    "TitaBatchNo",
    "TitaBatchSeq",
    "TitaSupNo",
    "TitaRelCd",
    "SlipSumNo",
    "JsonFields",
    "MediaSlipNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AcDetail";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcDetail_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;