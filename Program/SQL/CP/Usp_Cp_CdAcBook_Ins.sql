CREATE OR REPLACE PROCEDURE "Usp_Cp_CdAcBook_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdAcBook" DROP STORAGE';

  INSERT INTO "CdAcBook" (
    "AcBookCode",
    "AcSubBookCode",
    "CurrencyCode",
    "TargetAmt",
    "ActualAmt",
    "AssignSeq",
    "AcctSource",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcBookCode",
    "AcSubBookCode",
    "CurrencyCode",
    "TargetAmt",
    "ActualAmt",
    "AssignSeq",
    "AcctSource",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdAcBook";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdAcBook_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;