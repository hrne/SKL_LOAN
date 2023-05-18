CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ049Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ049Log" DROP STORAGE';

  INSERT INTO "JcicZ049Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "ClaimStatus",
    "ApplyDate",
    "CourtCode",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "ClaimDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "Ukey",
    "TxSeq",
    "TranKey",
    "ClaimStatus",
    "ApplyDate",
    "CourtCode",
    "Year",
    "CourtDiv",
    "CourtCaseNo",
    "Approve",
    "ClaimDate",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ049Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ049Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;