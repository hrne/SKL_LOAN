CREATE OR REPLACE PROCEDURE "Usp_Cp_AcReceivable_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcReceivable" DROP STORAGE';

  INSERT INTO "AcReceivable" (
    "AcctCode",
    "CustNo",
    "FacmNo",
    "RvNo",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "BranchNo",
    "CurrencyCode",
    "ClsFlag",
    "AcctFlag",
    "ReceivableFlag",
    "RvAmt",
    "RvBal",
    "AcBal",
    "SlipNote",
    "AcBookCode",
    "AcSubBookCode",
    "OpenAcDate",
    "OpenTxCd",
    "OpenKinBr",
    "OpenTlrNo",
    "OpenTxtNo",
    "LastAcDate",
    "LastTxDate",
    "TitaTxCd",
    "TitaKinBr",
    "TitaTlrNo",
    "TitaTxtNo",
    "JsonFields",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
)
  SELECT
    "AcctCode",
    "CustNo",
    "FacmNo",
    "RvNo",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "BranchNo",
    "CurrencyCode",
    "ClsFlag",
    "AcctFlag",
    "ReceivableFlag",
    "RvAmt",
    "RvBal",
    "AcBal",
    "SlipNote",
    "AcBookCode",
    "AcSubBookCode",
    "OpenAcDate",
    "OpenTxCd",
    "OpenKinBr",
    "OpenTlrNo",
    "OpenTxtNo",
    "LastAcDate",
    "LastTxDate",
    "TitaTxCd",
    "TitaKinBr",
    "TitaTlrNo",
    "TitaTxtNo",
    "JsonFields",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate"
  FROM ITXADMIN."AcReceivable";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcReceivable_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;