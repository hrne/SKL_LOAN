CREATE OR REPLACE PROCEDURE "Usp_Cp_PfInsCheck_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfInsCheck" DROP STORAGE';

  INSERT INTO "PfInsCheck" (
    "Kind",
    "CustNo",
    "FacmNo",
    "CreditSysNo",
    "CustId",
    "ApplDate",
    "InsDate",
    "InsNo",
    "CheckResult",
    "CheckWorkMonth",
    "ReturnMsg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Kind",
    "CustNo",
    "FacmNo",
    "CreditSysNo",
    "CustId",
    "ApplDate",
    "InsDate",
    "InsNo",
    "CheckResult",
    "CheckWorkMonth",
    "ReturnMsg",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfInsCheck";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfInsCheck_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;