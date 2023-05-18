CREATE OR REPLACE PROCEDURE "Usp_Cp_CdTeamReward_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "CdTeamReward" DROP STORAGE';

  INSERT INTO "CdTeamReward" (
    "WorkMonthStart",
    "WorkMonthEnd",
    "TeamLevel",
    "RewardType",
    "RewardStandard",
    "RewardAmt",
    "MinimumStandard",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
)
  SELECT
    "WorkMonthStart",
    "WorkMonthEnd",
    "TeamLevel",
    "RewardType",
    "RewardStandard",
    "RewardAmt",
    "MinimumStandard",
    "CreateDate",
    "CreateEmpNo",
    "LastUpdate",
    "LastUpdateEmpNo"
  FROM ITXADMIN."CdTeamReward";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_CdTeamReward_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;