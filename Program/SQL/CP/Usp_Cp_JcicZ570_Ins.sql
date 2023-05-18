CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ570_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ570" DROP STORAGE';

  INSERT INTO "JcicZ570" (
    "TranKey",
    "CustId",
    "SubmitKey",
    "ApplyDate",
    "AdjudicateDate",
    "BankCount",
    "Bank1",
    "Bank2",
    "Bank3",
    "Bank4",
    "Bank5",
    "Bank6",
    "Bank7",
    "Bank8",
    "Bank9",
    "Bank10",
    "Bank11",
    "Bank12",
    "Bank13",
    "Bank14",
    "Bank15",
    "Bank16",
    "Bank17",
    "Bank18",
    "Bank19",
    "Bank20",
    "Bank21",
    "Bank22",
    "Bank23",
    "Bank24",
    "Bank25",
    "Bank26",
    "Bank27",
    "Bank28",
    "Bank29",
    "Bank30",
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
    "AdjudicateDate",
    "BankCount",
    "Bank1",
    "Bank2",
    "Bank3",
    "Bank4",
    "Bank5",
    "Bank6",
    "Bank7",
    "Bank8",
    "Bank9",
    "Bank10",
    "Bank11",
    "Bank12",
    "Bank13",
    "Bank14",
    "Bank15",
    "Bank16",
    "Bank17",
    "Bank18",
    "Bank19",
    "Bank20",
    "Bank21",
    "Bank22",
    "Bank23",
    "Bank24",
    "Bank25",
    "Bank26",
    "Bank27",
    "Bank28",
    "Bank29",
    "Bank30",
    "OutJcicTxtDate",
    "Ukey",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo",
    "ActualFilingDate",
    "ActualFilingMark"
  FROM ITXADMIN."JcicZ570";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ570_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;