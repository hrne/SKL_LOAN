CREATE OR REPLACE PROCEDURE "Usp_Cp_AcMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcMain" DROP STORAGE';

  INSERT INTO "AcMain" (
    "AcBookCode",
    "AcSubBookCode",
    "BranchNo",
    "CurrencyCode",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcDate",
    "YdBal",
    "TdBal",
    "DbCnt",
    "DbAmt",
    "CrCnt",
    "CrAmt",
    "CoreDbCnt",
    "CoreDbAmt",
    "CoreCrCnt",
    "CoreCrAmt",
    "AcctCode",
    "MonthEndYm",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcBookCode",
    "AcSubBookCode",
    "BranchNo",
    "CurrencyCode",
    "AcNoCode",
    "AcSubCode",
    "AcDtlCode",
    "AcDate",
    "YdBal",
    "TdBal",
    "DbCnt",
    "DbAmt",
    "CrCnt",
    "CrAmt",
    "CoreDbCnt",
    "CoreDbAmt",
    "CoreCrCnt",
    "CoreCrAmt",
    "AcctCode",
    "MonthEndYm",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."AcMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;