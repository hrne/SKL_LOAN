CREATE OR REPLACE PROCEDURE "Usp_Cp_DailyTav_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "DailyTav" DROP STORAGE';

  INSERT INTO "DailyTav" (
    "AcctCode",
    "AcDate",
    "CustNo",
    "FacmNo",
    "SelfUseFlag",
    "TavBal",
    "LatestFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "AcctCode",
    "AcDate",
    "CustNo",
    "FacmNo",
    "SelfUseFlag",
    "TavBal",
    "LatestFlag",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."DailyTav";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_DailyTav_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;