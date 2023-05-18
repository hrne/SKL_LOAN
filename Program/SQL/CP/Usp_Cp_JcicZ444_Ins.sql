CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ444_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ444" DROP STORAGE';

  INSERT INTO "JcicZ444" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
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
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "CourtCode",
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
  FROM ITXADMIN."JcicZ444";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ444_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;