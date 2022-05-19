CREATE OR REPLACE PROCEDURE "Usp_Cp_LoanCustRmk_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "LoanCustRmk" DROP STORAGE';

  INSERT INTO "LoanCustRmk" (
    "CustNo",
    "AcDate",
    "RmkNo",
    "RmkCode",
    "RmkDesc",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "AcDate",
    "RmkNo",
    "RmkCode",
    "RmkDesc",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."LoanCustRmk";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_LoanCustRmk_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;