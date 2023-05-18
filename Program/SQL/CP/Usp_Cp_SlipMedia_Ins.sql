CREATE OR REPLACE PROCEDURE "Usp_Cp_SlipMedia_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "SlipMedia" DROP STORAGE';

  INSERT INTO "SlipMedia" (
    "BranchNo",
    "AcDate",
    "BatchNo",
    "AcBookCode",
    "MediaSeq",
    "MediaSlipNo",
    "Seq",
    "AcBookItem",
    "AcNoCode",
    "AcSubCode",
    "AcNoItem",
    "CurrencyCode",
    "DbCr",
    "TxAmt",
    "ReceiveCode",
    "DeptCode",
    "SlipRmk",
    "CostMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "BranchNo",
    "AcDate",
    "BatchNo",
    "AcBookCode",
    "MediaSeq",
    "MediaSlipNo",
    "Seq",
    "AcBookItem",
    "AcNoCode",
    "AcSubCode",
    "AcNoItem",
    "CurrencyCode",
    "DbCr",
    "TxAmt",
    "ReceiveCode",
    "DeptCode",
    "SlipRmk",
    "CostMonth",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."SlipMedia";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_SlipMedia_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;