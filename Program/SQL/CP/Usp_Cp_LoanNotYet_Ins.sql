CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanNotYet_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanNotYet" DROP STORAGE';

  INSERT INTO "LoanNotYet" (
    "CustNo",
    "FacmNo",
    "NotYetCode",
    "YetDate",
    "CloseDate",
    "ReMark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "FacmNo",
    "NotYetCode",
    "YetDate",
    "CloseDate",
    "ReMark",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanNotYet";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanNotYet_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;