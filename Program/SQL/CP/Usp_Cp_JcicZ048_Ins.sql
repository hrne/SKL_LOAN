CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ048_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ048" DROP STORAGE';

  INSERT INTO "JcicZ048" (
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "CustRegAddr",
    "CustComAddr",
    "CustRegTelNo",
    "CustComTelNo",
    "CustMobilNo",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
)
  SELECT
    "TranKey",
    "SubmitKey",
    "CustId",
    "RcDate",
    "CustRegAddr",
    "CustComAddr",
    "CustRegTelNo",
    "CustComTelNo",
    "CustMobilNo",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ048";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ048_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;