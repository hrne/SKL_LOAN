CREATE OR REPLACE PROCEDURE "Usp_Cp_MlaundryRecord_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "MlaundryRecord" DROP STORAGE';

  INSERT INTO "MlaundryRecord" (
    "LogNo",
    "RecordDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayDate",
    "ActualRepayDate",
    "RepayAmt",
    "ActualRepayAmt",
    "Career",
    "Income",
    "RepaySource",
    "RepayBank",
    "Description",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogNo",
    "RecordDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayDate",
    "ActualRepayDate",
    "RepayAmt",
    "ActualRepayAmt",
    "Career",
    "Income",
    "RepaySource",
    "RepayBank",
    "Description",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."MlaundryRecord";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_MlaundryRecord_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;