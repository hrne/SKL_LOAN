CREATE OR REPLACE PROCEDURE "Usp_Cp_NegFinShareLog_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegFinShareLog" DROP STORAGE';

  INSERT INTO "NegFinShareLog" (
    "CustNo",
    "CaseSeq",
    "Seq",
    "FinCode",
    "ContractAmt",
    "AmtRatio",
    "DueAmt",
    "CancelDate",
    "CancelAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "CaseSeq",
    "Seq",
    "FinCode",
    "ContractAmt",
    "AmtRatio",
    "DueAmt",
    "CancelDate",
    "CancelAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegFinShareLog";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegFinShareLog_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;