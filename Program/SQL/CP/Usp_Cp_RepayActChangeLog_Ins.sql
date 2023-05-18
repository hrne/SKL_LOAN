CREATE OR REPLACE PROCEDURE "Usp_Cp_RepayActChangeLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "RepayActChangeLog" DROP STORAGE';

  INSERT INTO "RepayActChangeLog" (
    "LogNo",
    "CustNo",
    "FacmNo",
    "RepayCode",
    "RepayBank",
    "PostDepCode",
    "RepayAcct",
    "Status",
    "RelDy",
    "RelTxseq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogNo",
    "CustNo",
    "FacmNo",
    "RepayCode",
    "RepayBank",
    "PostDepCode",
    "RepayAcct",
    "Status",
    "RelDy",
    "RelTxseq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."RepayActChangeLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_RepayActChangeLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;