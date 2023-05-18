CREATE OR REPLACE PROCEDURE "Usp_Cp_CollListTmp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollListTmp" DROP STORAGE';

  INSERT INTO "CollListTmp" (
    "CustNo",
    "FacmNo",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ClCustNo",
    "ClFacmNo",
    "ClRowNo",
    "CaseCode",
    "PrevIntDate",
    "NextIntDate",
    "CurrencyCode",
    "PrinBalance",
    "BadDebtBal",
    "Status",
    "AcctCode",
    "FacAcctCode",
    "RenewCode",
    "AcDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "ClCustNo",
    "ClFacmNo",
    "ClRowNo",
    "CaseCode",
    "PrevIntDate",
    "NextIntDate",
    "CurrencyCode",
    "PrinBalance",
    "BadDebtBal",
    "Status",
    "AcctCode",
    "FacAcctCode",
    "RenewCode",
    "AcDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollListTmp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollListTmp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;