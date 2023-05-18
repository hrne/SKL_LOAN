CREATE OR REPLACE PROCEDURE "Usp_Cp_CollList_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CollList" DROP STORAGE';

  INSERT INTO "CollList" (
    "CustNo",
    "FacmNo",
    "CaseCode",
    "TxDate",
    "TxCode",
    "PrevIntDate",
    "NextIntDate",
    "OvduTerm",
    "OvduDays",
    "CurrencyCode",
    "PrinBalance",
    "BadDebtBal",
    "AccCollPsn",
    "LegalPsn",
    "Status",
    "AcctCode",
    "FacAcctCode",
    "ClCustNo",
    "ClFacmNo",
    "ClRowNo",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "RenewCode",
    "AcDate",
    "IsSpecify",
    "CityCode",
    "AccTelArea",
    "AccTelNo",
    "AccTelExt",
    "LegalArea",
    "LegalNo",
    "LegalExt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "CaseCode",
    "TxDate",
    "TxCode",
    "PrevIntDate",
    "NextIntDate",
    "OvduTerm",
    "OvduDays",
    "CurrencyCode",
    "PrinBalance",
    "BadDebtBal",
    "AccCollPsn",
    "LegalPsn",
    "Status",
    "AcctCode",
    "FacAcctCode",
    "ClCustNo",
    "ClFacmNo",
    "ClRowNo",
    "ClCode1",
    "ClCode2",
    "ClNo",
    "RenewCode",
    "AcDate",
    "IsSpecify",
    "CityCode",
    "AccTelArea",
    "AccTelNo",
    "AccTelExt",
    "LegalArea",
    "LegalNo",
    "LegalExt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CollList";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CollList_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;