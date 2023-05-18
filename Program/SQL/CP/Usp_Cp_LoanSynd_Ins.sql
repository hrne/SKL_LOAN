CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanSynd_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanSynd" DROP STORAGE';

  INSERT INTO "LoanSynd" (
    "SyndNo",
    "SyndName",
    "LeadingBank",
    "AgentBank",
    "SigningDate",
    "SyndTypeCodeFlag",
    "PartRate",
    "CurrencyCode",
    "SyndAmt",
    "PartAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "SyndNo",
    "SyndName",
    "LeadingBank",
    "AgentBank",
    "SigningDate",
    "SyndTypeCodeFlag",
    "PartRate",
    "CurrencyCode",
    "SyndAmt",
    "PartAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanSynd";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanSynd_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;