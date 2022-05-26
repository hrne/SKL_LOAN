CREATE OR REPLACE PROCEDURE "Usp_Cp_AcLoanRenew_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "AcLoanRenew" DROP STORAGE';

  INSERT INTO "AcLoanRenew" (
    "CustNo",
    "NewFacmNo",
    "NewBormNo",
    "OldFacmNo",
    "OldBormNo",
    "RenewCode",
    "MainFlag",
    "AcDate",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate",
    "OtherFields"
)
  SELECT
    "CustNo",
    "NewFacmNo",
    "NewBormNo",
    "OldFacmNo",
    "OldBormNo",
    "RenewCode",
    "MainFlag",
    "AcDate",
    "CreateEmpNo",
    "CreateDate",
    "LastUpdateEmpNo",
    "LastUpdate",
    "OtherFields"
  FROM ITXADMIN."AcLoanRenew";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_AcLoanRenew_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;