CREATE OR REPLACE PROCEDURE "Usp_Cp_JcicZ044Log_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "JcicZ044Log" DROP STORAGE';

  INSERT INTO "JcicZ044Log" (
    "Ukey",
    "TxSeq",
    "TranKey",
    "DebtCode",
    "NonGageAmt",
    "Period",
    "Rate",
    "MonthPayAmt",
    "ReceYearIncome",
    "ReceYear",
    "ReceYear2Income",
    "ReceYear2",
    "CurrentMonthIncome",
    "LivingCost",
    "CompName",
    "CompId",
    "CarCnt",
    "HouseCnt",
    "LandCnt",
    "ChildCnt",
    "ChildRate",
    "ParentCnt",
    "ParentRate",
    "MouthCnt",
    "MouthRate",
    "GradeType",
    "PayLastAmt",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
    "PayLastAmt2",
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
    "DebtCode",
    "NonGageAmt",
    "Period",
    "Rate",
    "MonthPayAmt",
    "ReceYearIncome",
    "ReceYear",
    "ReceYear2Income",
    "ReceYear2",
    "CurrentMonthIncome",
    "LivingCost",
    "CompName",
    "CompId",
    "CarCnt",
    "HouseCnt",
    "LandCnt",
    "ChildCnt",
    "ChildRate",
    "ParentCnt",
    "ParentRate",
    "MouthCnt",
    "MouthRate",
    "GradeType",
    "PayLastAmt",
    "Period2",
    "Rate2",
    "MonthPayAmt2",
    "PayLastAmt2",
    "OutJcicTxtDate",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."JcicZ044Log";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_JcicZ044Log_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;