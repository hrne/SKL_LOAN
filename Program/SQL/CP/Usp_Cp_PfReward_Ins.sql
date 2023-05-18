CREATE OR REPLACE PROCEDURE "Usp_Cp_PfReward_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "PfReward" DROP STORAGE';

  INSERT INTO "PfReward" (
    "LogNo",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayType",
    "PieceCode",
    "ProdCode",
    "Introducer",
    "Coorgnizer",
    "InterviewerA",
    "InterviewerB",
    "IntroducerBonus",
    "IntroducerBonusDate",
    "IntroducerAddBonus",
    "IntroducerAddBonusDate",
    "CoorgnizerBonus",
    "CoorgnizerBonusDate",
    "WorkMonth",
    "WorkSeason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "LogNo",
    "PerfDate",
    "CustNo",
    "FacmNo",
    "BormNo",
    "RepayType",
    "PieceCode",
    "ProdCode",
    "Introducer",
    "Coorgnizer",
    "InterviewerA",
    "InterviewerB",
    "IntroducerBonus",
    "IntroducerBonusDate",
    "IntroducerAddBonus",
    "IntroducerAddBonusDate",
    "CoorgnizerBonus",
    "CoorgnizerBonusDate",
    "WorkMonth",
    "WorkSeason",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."PfReward";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_PfReward_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;