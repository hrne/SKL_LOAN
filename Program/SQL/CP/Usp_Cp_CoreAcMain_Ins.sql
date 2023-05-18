CREATE OR REPLACE PROCEDURE "Usp_Cp_CoreAcMain_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CoreAcMain" DROP STORAGE';

  INSERT INTO "CoreAcMain" (
    "AcBookCode",
    "AcSubBookCode",
    "CurrencyCode",
    "AcNoCode",
    "AcNoName",
    "AcSubCode",
    "AcDate",
    "YdBal",
    "TdBal",
    "DbAmt",
    "CrAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcBookCode",
    "AcSubBookCode",
    "CurrencyCode",
    "AcNoCode",
    "AcNoName",
    "AcSubCode",
    "AcDate",
    "YdBal",
    "TdBal",
    "DbAmt",
    "CrAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CoreAcMain";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CoreAcMain_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;