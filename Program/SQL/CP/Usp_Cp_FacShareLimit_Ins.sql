CREATE OR REPLACE PROCEDURE "Usp_Cp_FacShareLimit_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "FacShareLimit" DROP STORAGE';

  INSERT INTO "FacShareLimit" (
    "ApplNo",
    "MainApplNo",
    "CustNo",
    "FacmNo",
    "KeyinSeq",
    "CurrencyCode",
    "LineAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "ApplNo",
    "MainApplNo",
    "CustNo",
    "FacmNo",
    "KeyinSeq",
    "CurrencyCode",
    "LineAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."FacShareLimit";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_FacShareLimit_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;