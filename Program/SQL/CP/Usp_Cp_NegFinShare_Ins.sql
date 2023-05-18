CREATE OR REPLACE PROCEDURE "Usp_Cp_NegFinShare_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NegFinShare" DROP STORAGE';

  INSERT INTO "NegFinShare" (
    "CustNo",
    "CaseSeq",
    "FinCode",
    "ContractAmt",
    "AmtRatio",
    "DueAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "CustNo",
    "CaseSeq",
    "FinCode",
    "ContractAmt",
    "AmtRatio",
    "DueAmt",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."NegFinShare";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_NegFinShare_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;