CREATE OR REPLACE PROCEDURE "Usp_Cp_BankAuthAct_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "BankAuthAct" DROP STORAGE';

  INSERT INTO "BankAuthAct" (
    "CustNo",
    "FacmNo",
    "AuthType",
    "RepayBank",
    "PostDepCode",
    "RepayAcct",
    "Status",
    "LimitAmt",
    "AcctSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "AuthType",
    "RepayBank",
    "PostDepCode",
    "RepayAcct",
    "Status",
    "LimitAmt",
    "AcctSeq",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."BankAuthAct";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_BankAuthAct_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;