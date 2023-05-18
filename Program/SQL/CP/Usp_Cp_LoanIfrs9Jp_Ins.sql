CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanIfrs9Jp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanIfrs9Jp" DROP STORAGE';

  INSERT INTO "LoanIfrs9Jp" (
    "DataYM",
    "AcDateYM",
    "CustNo",
    "NewFacmNo",
    "NewBormNo",
    "OldFacmNo",
    "OldBormNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "DataYM",
    "AcDateYM",
    "CustNo",
    "NewFacmNo",
    "NewBormNo",
    "OldFacmNo",
    "OldBormNo",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanIfrs9Jp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanIfrs9Jp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;