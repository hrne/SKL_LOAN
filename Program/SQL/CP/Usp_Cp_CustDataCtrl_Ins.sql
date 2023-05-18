CREATE OR REPLACE PROCEDURE "Usp_Cp_CustDataCtrl_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CustDataCtrl" DROP STORAGE';

  INSERT INTO "CustDataCtrl" (
    "CustNo",
    "CustUKey",
    "ApplMark",
    "Reason",
    "CustId",
    "CustName",
    "XXCustId",
    "SetDate",
    "SetEmpNo",
    "ReSetDate",
    "ReSetEmpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "CustUKey",
    "ApplMark",
    "Reason",
    "CustId",
    "CustName",
    "XXCustId",
    "SetDate",
    "SetEmpNo",
    "ReSetDate",
    "ReSetEmpNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CustDataCtrl";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CustDataCtrl_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;